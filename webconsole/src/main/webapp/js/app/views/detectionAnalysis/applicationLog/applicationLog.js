/**
 * @author lee kyunghee
 * @since 2015-12-22
 * @description Application Layer Log 검색 화면을 보여준다. 
 */
define(function (require) {

    "use strict";

    // require library
    var $ = require('jquery'),
            Backbone = require('backbone'),
            jqueryMouseWheel = require('jqueryMouseWheel'),
            tmsjs = require('tmsjs');

    // require locale
    var locale = require('i18n!nls/str'),
        errorLocale = require('i18n!nls/error'),
            alertMessage = require('utils/AlertMessage');

    // require utils
    var serialize = require('serialize'),
            alertMessage = require('utils/AlertMessage');

    // require model, collection, view
    var ApplicationSearchModel = require('models/detectionAnalysis/applicationSearchModel'),
            ListView = require("views/detectionAnalysis/applicationLog/applicationLogList"),
            Pagination = require('views/common/pagination');

    // require template 
    var tpl = require('text!tpl/detectionAnalysis/applicationLog.html');

    return Backbone.View.extend({
        formEl: "form.applicationForm",
        detailSearchDiv: "div#detail-search",
        template: _.template(tpl),
        targetView: null,
        periodView: null,
        resultIp: true,
        resultMask: true,
        resultDestPort: true,
        resultAttackPort: true,
        initialize: function (options) {
            this.pagination = new Pagination();
            _.bindAll(this,  "render", "search", "getListView");
            this.evt = options.evt;
            this.model = new ApplicationSearchModel();
        },
        events: {
            "click .searchBtn": "search",
            "click .cancelBtn": "settings",
            'blur #ipInput': 'validationIp', // ip validation
            'blur #maskInput': 'validationMask', // mask validation
            'blur #destPortInput': 'validationDestPort', // 피해포트 validation
            'blur #attackPortInput': 'validationAttackPort', // 공격포트 validation 
            "click #ipType1": "changeIpv4Type",
            "click #ipType2": "changeIpType"

        },
        render: function () {
            var self = this;
            this.$el.html(this.template({
                locale: locale
            }));

            this.getListView();
//            this.targetView = Backbone.TargetView.MakeTarget(self.$('#targetType'), {viewType: 1});
            this.targetView = Backbone.TargetView.MakeTarget(self.$('#targetType'));
            this.periodView = Backbone.PeriodView.makeUI(this.$('#period'), {
                periodUnit: 5,
                category: "minute"
            });
            this.search();

            return this;
        },
        search: function () {
            if (this.resultMask == true && this.resultIp == true && this.resultDestPort == true && this.resultAttackPort == true) {
                if ($('#maskInput').val() != "") {
                    if ($('#ipInput').val() == "") {
                        this.resultIp = Backbone.Utils.Tip.validationTooltip($('#ipInput'), errorLocale.validation.ipInputNullValid);
                        return false;
                    }
                }
                var analysisPeriod = this.periodView.getPeriod();
                if (Backbone.Utils.compareMaxDate(analysisPeriod.startDate, analysisPeriod.endDate) == false) {
                    alertMessage.infoMessage(errorLocale.validation.periodDate, 'info', '', 'small');

                    return false;
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
        settings: function () {
            Backbone.FormSerialize.setData($(this.formEl));
            $('#bType', this.el).val('').attr('selected', 'selected');
            this.targetView = Backbone.TargetView.MakeTarget($('#targetType', this.el), {viewType: 1});
            this.periodView = Backbone.PeriodView.makeUI($('#period', this.el), {
                periodUnit: 5,
                category: "minute"
            });
            $('#fromIpInput', this.el).val('');
            $('#toIpInput', this.el).val('');
            Backbone.Utils.Tip.validationTooltip($('#ipInput'), true);
            Backbone.Utils.Tip.validationTooltip($('#maskInput'), true);
            Backbone.Utils.Tip.validationTooltip($('#destPortInput'), true);
            Backbone.Utils.Tip.validationTooltip($('#attackPortInput'), true);
            this.resultIp = true;
            this.resultMask = true;
            this.resultDestPort = true;
            this.resultAttackPort = true;
        },
        getListView: function () {
            var listView = new ListView({evt: this.evt});
            this.$("#table").append(listView.el);
            listView.render();
        },
        validationIp: function () {
            // ip 형식 체크 
            var ipValue = $('#ipInput').val();
            var maskValue = $('#maskInput').val();
            this.resultIp = Backbone.Utils.validation.validateIpDualCheck(ipValue);
            this.resultIp = Backbone.Utils.Tip.validationTooltip($('#ipInput'), this.resultIp);

            if (this.resultIp == true) {
                if (maskValue != '' && this.resultMask) {
                    // mask에 값이 있을때
                    // mask값에 맞는 ip형식인지 판단
                    // 1. 형식이틀리다면
                    if (this.resultIp != true) {
                        this.resultIp = Backbone.Utils.Tip.validationTooltip($('#ipInput'), this.resultIp);
                        return this.resultIp;
                    }
                    //2. 형식이 맞다면
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
        validationMask: function () {
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
                alertMessage.infoMessage(errorLocale.validation.invaildMask + errorLocale.validation.reInput, 'info', '', 'small');
            }
        },
        validationDestPort: function () {
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
        validationAttackPort: function () {
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
        changeIpv4Type: function () {
            $("#maskInput").attr("disabled", false);
        },
        changeIpType: function () {
            $("#maskInput").attr("disabled", true);
        }
    });
});
