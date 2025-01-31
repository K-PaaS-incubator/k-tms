/**
 * @description 시스템 현황 > 감사로그 
 * 조회 검색 화면 
 */
define(function (require) {

    "use strict";

    // require library
    var $ 					= require('jquery'),
        Backbone 			= require('backbone'),
        jqueryMouseWheel 	= require('jqueryMouseWheel'),
        tmsjs 				= require('tmsjs'),
        serialize 			= require('serialize'),
        alertMessage 		= require('utils/AlertMessage');

    // require locale
    var locale = require('i18n!nls/str'),
    errorLocale = require('i18n!nls/error');

    // require model, collection, view
    var AuditLogSearchModel = require('models/systemStatus/auditLogSearchModel'),
        ListView 			= require("views/systemStatus/auditLog/auditLogList");

    // require template 
    var tpl 				= require('text!tpl/systemStatus/auditLog.html');

    return Backbone.View.extend({
        formEl: "form.auditLogForm",
        template: _.template(tpl),
        periodView: null,
        initialize: function (options) {
            _.bindAll(this,  "render", "search", "getListView");
            this.evt = options.evt;
            this.model = new AuditLogSearchModel();
            this.type = options.type;
        },
        events: {
            "click .searchBtn"	: "search",
            "click .cancelBtn"	: "settings"
        },
        render: function() {
            this.$el.html(this.template({
                locale: locale, type: this.type
            }));

            this.getListView();
            this.periodView = Backbone.PeriodView.makeUI(this.$('#period'), {
                periodUnit: 5,
                category: "12hours",
                menuOptions: "AUDIT"
            });
            if (this.type == "auditAction") {
                this.$("#auditType > option[value=1]").attr("selected", "true");
            } else if (this.type == "auditError") {
                this.$("#auditType > option[value=2]").attr("selected", "true");
            } else if (this.type == "auditWarning") {
                this.$("#auditType > option[value=3]").attr("selected", "true");
            } else {
                this.$("#auditType > option[value=0]").attr("selected", "true");
            }
            this.search();

            return this;
        },
        search: function() {
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
            var setParams = Backbone.FormSerialize.getData(this.$(this.formEl), setDateParams);
            var excludeRegExp = ["..", "=", "/", "\\", "%", "?", ":", ";", "'", "\"", "<", ">", "(", ")", "-"]
            for ( var i in excludeRegExp ) {
                if (setParams.strContent.includes(excludeRegExp[i]) || setParams.strOperator.includes(excludeRegExp[i]) ) {
                    alertMessage.infoMessage(errorLocale.validation.specialCharecterInputValidation, 'info', '', 'small');
                    this.$("#strContent").val(null)
                    this.$("#strOperator").val(null)
                    return false;
                }
            }
            this.model.set(setParams);
            this.evt.trigger('retrieve', this.model);

            // 조회 구간 표기 
            $("#listDate", this.el).text(this.model.get('startDateInput') + " ~ " + this.model.get('endDateInput'));

            return true;
        },
        // 검색 조건 초기화
        settings: function () {
            var value = "direction";
            Backbone.FormSerialize.setData(this.$(this.formEl), value);
            this.periodView = Backbone.PeriodView.makeUI(this.$('#period'), {
                periodUnit: 5,
                category: "12hours",
                menuOptions: "AUDIT"
            });
            $('#auditType').val('0').attr('selected', 'selected');
        },
        getListView: function () {
            var listView = new ListView({
                evt: this.evt
            });
            $("#table", this.el).append(listView.el);
            listView.render();
        }
    });
});