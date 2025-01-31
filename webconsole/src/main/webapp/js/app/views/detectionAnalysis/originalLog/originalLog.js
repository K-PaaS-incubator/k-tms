/**
 * originalLog main view  
 * @author lee kyunghee
 * @since 2014-12-09
 * @description 원본로그 검색 화면을 보여준다. 
 */
define(function (require) {

    "use strict";

    // require library
    var $ 						= require('jquery'),
        Backbone 				= require('backbone'),
        jqueryMouseWheel 		= require('jqueryMouseWheel'),
        alertMessage 			= require('utils/AlertMessage'),
        tmsjs 					= require('tmsjs');

    // require locale
    var locale 					= require('i18n!nls/str'),
        errorLocale 			= require('i18n!nls/error');

    // require utils
    var serialize				= require('serialize');

    // require model, collection, view
    var OriginalLogSearchModel 	= require('models/detectionAnalysis/originalLogSearchModel'),
        AttackTypeCollection 	= require('collections/securityPolicy/attackTypeCollection'),
        ListView 				= require("views/detectionAnalysis/originalLog/originalLogList"),
        Pagination 				= require('views/common/pagination'),
        sessionManager 			= require('utils/sessionManager');

    // require template 
    var tpl = require('text!tpl/detectionAnalysis/originalLog.html');

    return Backbone.View.extend({
        formEl: "form.originalLogForm",
        detailSearchDiv: "div#detail-search",
        template: _.template(tpl),
        targetView: null,
        periodView: null,
        resultIp: true,
        resultMask: true,
        resultDestPort: true,
        resultAttackPort: true,
        initialize: function(options) {
            this.pagination = new Pagination();
            _.bindAll(this,  "render", "search", "getListView");
            this.evt 					= options.evt;
            this.model 					= new OriginalLogSearchModel();
            this.attackTypeCollection 	= new AttackTypeCollection();
        },
        events: {
            "click .searchBtn"		: "search",
            "click .cancelBtn"		: "settings",
            'blur #ipInput'			: 'validationIp', 			// ip validation
            'blur #maskInput'		: 'validationMask', 		// mask validation
            'blur #destPortInput'	: 'validationDestPort', 	// 피해포트 validation
            'blur #attackPortInput'	: 'validationAttackPort', 	// 공격포트 validation
            "click #ipType1"		: "changeIpv4Type",
            "click #ipType2"		: "changeIpType"

        },
        // 검색 초기화
        settings: function() {
            var value = "direction";
            Backbone.FormSerialize.setData(this.$(this.formEl), value);
            this.targetView = Backbone.TargetView.MakeTarget($('#targetType', this.el));
            this.periodView = Backbone.PeriodView.makeUI($('#period', this.el), {
                periodUnit: 5,
                category: "minute"
            });
            // hidden ip 초기화 
            $('#fromIpInput', this.el).val('');
            $('#toIpInput', this.el).val('');
            $('#attackTypeSelect', this.el).val('').attr('selected', 'selected');
            $('#winBoundSelect', this.el).val('').attr('selected', 'selected');

            // input 유효성(빨간테두리, 툴팁) 초기화
            this.resultIp = Backbone.Utils.Tip.validationTooltip($('#ipInput'), true);
            this.resultMask = Backbone.Utils.Tip.validationTooltip($('#maskInput'), true);
            this.resultAttackPort = Backbone.Utils.Tip.validationTooltip($('#attackPortInput'), true);
            this.resultDestPort = Backbone.Utils.Tip.validationTooltip($('#destPortInput'), true);
        },
        render: function() {
            var self = this;
            this.$el.html(this.template({
                locale: locale,
                SYSTEM_TYPE: SYSTEM_TYPE
            }));

            this.getListView();
            this.getAttackTypeList();
            this.targetView = Backbone.TargetView.MakeTarget(self.$('#targetType'));
            this.periodView = Backbone.PeriodView.makeUI(this.$('#period'), {
                periodUnit: 5,
                category: "minute"
            });
            this.search();

            return this;
        },
        search: function() {
            if (this.resultMask == true && this.resultIp == true && this.resultDestPort == true && this.resultAttackPort == true) {
                var analysisPeriod = this.periodView.getPeriod();
                if (Backbone.Utils.compareMaxDate(analysisPeriod.startDate, analysisPeriod.endDate) == false) {
                    alertMessage.infoMessage(errorLocale.validation.periodDate);

                    return false;
                }
                if ($('#maskInput').val() != "") {
                    if ($('#ipInput').val() == "") {
                        this.resultIp = Backbone.Utils.Tip.validationTooltip($('#ipInput'), errorLocale.validation.ipInputNullValid);
                        return false;
                    }
                }
                var setDateParams = {
                    periodType: analysisPeriod.periodType,
                    startDateInput: analysisPeriod.startDate,
                    endDateInput: analysisPeriod.endDate
                };
                var setParams = Backbone.FormSerialize.getData(this.$(this.formEl), this.targetView.currentSelOrgSensor(), setDateParams);
                this.model.set(setParams);
                this.evt.trigger('retrieve', this.model);

                return true;
            }
        },
        getListView: function() {
            var listView = new ListView({
                evt: this.evt,
                SYSTEM_TYPE: SYSTEM_TYPE
            });
            this.$("#table").append(listView.el);
            listView.render();
        },
        // 공격유형 조회 
        getAttackTypeList: function() {
            var self = this;
            this.attackTypeCollection.fetch({
                method: 'POST',
                data: JSON.stringify({}),
                url: 'api/securityPolicy/selectAttackTypeSelect',
                async: false,
                contentType: 'application/json',
                success: function () {
                    self.addAttackType();
                }
            });
        },
        // 공격유형 셀렉트 박스 템플릿에 append
        addAttackType: function() {
            var self = this;
            for (var i = 0; i < self.attackTypeCollection.length; i++) {
                $('#attackTypeSelect', self.el).append("<option value=" + self.attackTypeCollection.at(i).get("nClassType") + ">" + self.attackTypeCollection.at(i).get("strName") + "</option>");
            }
        },
        validationIp: function() {
            // ip 형식 체크 
            var ipValue = $('#ipInput').val();
            var maskValue = $('#maskInput').val();
            this.resultIp = Backbone.Utils.validation.validateIpDualCheck(ipValue);
            this.resultIp = Backbone.Utils.Tip.validationTooltip($('#ipInput'), this.resultIp);

            if (this.resultIp == true) {
                if (maskValue != '' && this.resultMask) {
                    // mask에 값이 있을때
                    // mask값에 맞는 ip형식인지 판단
                    if (this.resultIp != true) {
                        this.resultIp = Backbone.Utils.Tip.validationTooltip($('#ipInput'), this.resultIp);

                        return this.resultIp;
                    }
                    this.resultMask = Backbone.Utils.Tip.validationTooltip($('#maskInput'), true);
                    var resultData = Backbone.Utils.Calculation.parseCidrRangeIp(ipValue, maskValue);
                    $('#fromIpInput').val(resultData[0]);
                    $('#toIpInput').val(resultData[1]);
                    return this.resultIp;

                } else {
                    // mask에 값이 없을때
                    $('#toIpInput').val(ipValue);
                    $('#fromIpInput').val(ipValue);
                    return this.resultIp = true;
                }
            } else {
                // this.result == false 이면서  ipvalue 가 null인경우 
                if (ipValue == '' && maskValue == '') {
                    this.resultIp = true;
                    this.resultMask = true;
                    Backbone.Utils.Tip.validationTooltip($('#maskInput'), true);
                    Backbone.Utils.Tip.validationTooltip($('#ipInput'), true);
                    $('#toIpInput').val('');
                    $('#fromIpInput').val('');
                }
            }
            if (this.resultIp == false) {
                alertMessage.infoMessage(errorLocale.validation.invaildIp + errorLocale.validation.reInput, 'info', '', 'small');
            }
        },
        validationMask: function() {
            // mask 형식 체크 
            var maskValue = $('#maskInput').val();
            var ipValue = $('#ipInput').val();

            this.resultMask = Backbone.Utils.validation.validateMask(maskValue);
            this.resultMask = Backbone.Utils.Tip.validationTooltip($('#maskInput'), this.resultMask);

            if (this.resultMask == true) {

                if (ipValue != '' && this.resultIp) {
                    if (this.resultMask != true) {
                        this.resultMask = Backbone.Utils.Tip.validationTooltip($('#maskInput'), this.resultMask);
                        return this.resultMask;
                    } else {
                        var resultData = Backbone.Utils.Calculation.parseCidrRangeIp(ipValue, maskValue);
                        this.resultIp = Backbone.Utils.Tip.validationTooltip($('#ipInput'), true);
                        $('#fromIpInput').val(resultData[0]);
                        $('#toIpInput').val(resultData[1]);
                        return this.resultMask = true;
                    }
                }
            } else {
                if (ipValue == '' && maskValue == '') {
                    this.resultIp = true;
                    this.resultMask = true;
                    Backbone.Utils.Tip.validationTooltip($('#maskInput'), true);
                    Backbone.Utils.Tip.validationTooltip($('#ipInput'), true);
                    $('#toIpInput').val('');
                    $('#fromIpInput').val('');
                }

                if (ipValue != '' && this.resultIp) {
                    if (maskValue == '') {
                        this.resultMask = Backbone.Utils.Tip.validationTooltip($('#maskInput'), true);
                        return this.resultMask;
                    }
                }
            }
            if (this.resultMask == false) {
                //mask의 입력 범위는 8~32입니다.
                alertMessage.infoMessage(errorLocale.validation.maskRangeValid + errorLocale.validation.reInput, 'info', '', 'small');
            }
        },
        validationDestPort: function() {
            var destPortValue = $('#destPortInput', this.el).val();

            if (destPortValue == "" || destPortValue == null) {
                return this.resultDestPort = Backbone.Utils.Tip.validationTooltip($('#destPortInput', this.el), true);
            }
            this.resultDestPort = Backbone.Utils.validation.validatePort(destPortValue);
            this.resultDestPort = Backbone.Utils.Tip.validationTooltip($('#destPortInput', this.el), this.resultDestPort);
            if (this.resultDestPort == false) {
                alertMessage.infoMessage(errorLocale.validation.invaildPort + errorLocale.validation.reInput, 'info', '', 'small');
            }
        },
        validationAttackPort: function() {
            var attackPortValue = $('#attackPortInput', this.el).val();

            if (attackPortValue == "" || attackPortValue == null) {
                return this.resultAttackPort = Backbone.Utils.Tip.validationTooltip($('#attackPortInput', this.el), true);
            }
            this.resultAttackPort = Backbone.Utils.validation.validatePort(attackPortValue);
            this.resultAttackPort = Backbone.Utils.Tip.validationTooltip($('#attackPortInput', this.el), this.resultAttackPort);
            if (this.resultAttackPort == false) {
                alertMessage.infoMessage(errorLocale.validation.invaildPort + errorLocale.validation.reInput, 'info', '', 'small');
            }
        },
        changeIpv4Type: function() {
            var ipType = $("#ipType1").val();
            var changeIp = ipType == 4 ? false : true;

            $("#maskInput").attr("disabled", changeIp);
        },
        changeIpType: function() {
            var ipType = $("#ipType2").val();
            var changeIp = ipType == 6 ? true : false;

            $("#maskInput").attr("disabled", changeIp);
        }
    });
});