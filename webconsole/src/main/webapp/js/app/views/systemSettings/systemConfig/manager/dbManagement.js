/**
 * manager DB관리 tabs
 */
define(function (require) {

    "use strict";

    var $ = require('jquery'),
            Backbone = require('backbone');

    var locale = require('i18n!nls/str'),
            errorLocale = require('i18n!nls/error');

    var tpl = require('text!tpl/systemSettings/dbManagement.html'),
            editTpl = require('text!tpl/systemSettings/dbManagementEdit.html');

    var ManagerModel = require('models/systemSettings/managerModel'),
            sessionManager = require('utils/sessionManager');

    var alertMessage = require('utils/AlertMessage');

    return Backbone.View.extend({
        editable: false,
        formEl: "form.dbmanagementForm",
        template: _.template(tpl),
        templateEdit: _.template(editTpl),
        // validation을 하기 위한 유지기간 값, 초기값이 true인 이유 : 매니저는 신규생성이 없기때문에 update 상황에서 유지기간이 초기값이 true여야함 그래야 변경없이 저장시 문제없음 
        nDiskUsage: true,
        nDiskWarn: true,
        resultnAuditPeriodic: true,
        resultnRawPeriodic: true,
        initialize: function () {
            this.model = new ManagerModel();
        },
        events: {
            "change #nRawPeriodicInput": "checkRawEnable",
            "change #nAuditPeriodicInput": "checkAuditEnable",
            "change #nDiskUsage": "checkDiskUsage",
            "change #nDiskWarn": "checkDiskWarn",
            "keydown #nRawPeriodicInput": "numberKeyDown",
            "keydown #nAuditPeriodicInput": "numberKeyDown",
            "keydown #nDiskUsage": "numberKeyDown",
            "keyup #nRawPeriodicInput": "numberKeyUp",
            "keyup #nAuditPeriodicInput": "numberKeyUp",
            "keyup #nDiskUsage": "numberKeyUp",
            "keyup #nDiskWarn": "numberKeyUp",
        },
        render: function () {
            this.editable = false;
            this.getdbManagement();
            this.$el.html(this.template({
                locale: locale,
                model: this.model.toJSON()
            }));
            return this;
        },
        renderEdit: function () {
            this.editable = true;
            var disabled = SYSTEM_TYPE == 2 ? "disabled" : "";

            this.$el.html(this.templateEdit({
                locale: locale,
                model: this.model.toJSON()
            }));
            return this;
        },
        toggle: function () {
            if (this.editable) {
                this.mergeDbManagement();
                if (this.model.attributes.managerDbSettingRtn == -99)
                    this.valid = false;
                return this.render();
            } else {
                return this.renderEdit();
            }
        },
        mergeDbManagement: function () {

            var setParams = {
                nRawPeriodicInput: this.$("#nRawPeriodicInput").val(),
                nAuditPeriodicInput: this.$("#nAuditPeriodicInput").val(),
                nDiskUsage: this.$("#nDiskUsage").val(),
                nDiskWarn: this.$("#nDiskWarn").val(),
            };
            this.model.fetch({
                method: 'POST',
                url: 'api/systemSetting/updateDbManagement',
                contentType: 'application/json',
                dataType: '',
                data: JSON.stringify(setParams),
                async: false,
                success: function (model) {
                    model.get('managerDbSettingRtn');
                }
            });
        },
        getdbManagement: function () {
            var self = this;
            self.model.fetch({
                method: 'POST',
                async: false,
                data: JSON.stringify({}),
                url: 'api/systemSetting/selectDbManagement',
                contentType: 'application/json',
                success: function (model) {
                	console.log(model);
                }
            });
        },
        isValid: function () {
            this.valid = true;

            if (!( this.resultnRawPeriodic == true &&
                    this.nDiskUsage == true &&
                    this.nDiskWarn == true &&
                    this.resultnAuditPeriodic == true)) {
                this.valid = false;
            }
            var rawPeriodicValue = $('#nRawPeriodicInput', this.el).val();
            this.resultnRawPeriodic = Backbone.Utils.validation.rangeFrom1To999(rawPeriodicValue);
             if (this.resultnRawPeriodic != true) {
                alertMessage.infoMessage(locale.daysLog + "는 " + this.resultnRawPeriodic, 'info', '', 'small');
            }
            
            var nAuditPeriodicValue = $('#nAuditPeriodicInput', this.el).val();
            this.resultnAuditPeriodic = Backbone.Utils.validation.rangeFrom1To999(nAuditPeriodicValue);
             if (this.resultnAuditPeriodic != true) {
                alertMessage.infoMessage(locale.auditLog + "는 " + this.resultnAuditPeriodic, 'info', '', 'small');
            }
            
            var nDiskUsageValue = $('#nDiskWarn', this.el).val();
            this.nDiskWarn = Backbone.Utils.validation.rangeFromMinTo99(nDiskUsageValue, 60);
            if (this.nDiskWarn != true) {
                alertMessage.infoMessage(locale.diskWarn + "는 " +this.nDiskWarn, 'info', '', 'small');
            }
            
            var nDiskUsageValue = $('#nDiskUsage', this.el).val();
            this.nDiskUsage = Backbone.Utils.validation.rangeFromMinTo99(nDiskUsageValue, 70);
            if (this.nDiskUsage != true) {
                alertMessage.infoMessage(locale.diskUsed + "는 " +this.nDiskUsage, 'info', '', 'small');
            }
            
            if ($('#nDiskWarn', this.el).val() >= $('#nDiskUsage', this.el).val()) {
                this.valid = false;
                alertMessage.infoMessage(errorLocale.validation.diskWarnUsagedValid, 'info', '', 'small');
                return this.valid;
            }
            if (this.$("#nRawPeriodicInput").val() == 0 && this.$("#nAuditPeriodicInput").val() == 0 && this.$("#nDiskUsage").val() == 0) {
                this.valid = false;
                alertMessage.infoMessage(errorLocale.validation.maintenancePeriodZeroValid, 'info', '', 'small');
                return this.valid;
            }

            return this.valid;
        },
        isResult: function () {
            var result = {
                resultTableMaintenancePeriod: this.model.get('changeTableMaintenancePeriod'),
                resultTableSpaceMaintenancePeriod: this.model.get('changeTableSpaceMaintenancePeriod')
            };
            return result;
        },
        checkRawEnable: function () {
            var rawPeriodicValue = $('#nRawPeriodicInput', this.el).val();
            this.resultnRawPeriodic = Backbone.Utils.validation.rangeFrom1To999(rawPeriodicValue);
             if (this.resultnRawPeriodic != true) {
                alertMessage.infoMessage(this.resultnRawPeriodic, 'info', '', 'small');
                Backbone.Utils.Tip.validationTooltip($('#nRawPeriodicInput', this.el), this.resultnRawPeriodic);
            } else {
                this.model.set({
                    nRawPeriodic: $('#nRawPeriodicInput', this.el).val(),
                    changeTableMaintenancePeriod: true
                });
                Backbone.Utils.Tip.validationTooltip($('#nRawPeriodicInput', this.el), true);
            }
        },
        checkAuditEnable: function () {
            var nAuditPeriodicValue = $('#nAuditPeriodicInput', this.el).val();
            this.resultnAuditPeriodic = Backbone.Utils.validation.rangeFrom1To999(nAuditPeriodicValue);
             if (this.resultnAuditPeriodic != true) {
                alertMessage.infoMessage(this.resultnAuditPeriodic, 'info', '', 'small');
                Backbone.Utils.Tip.validationTooltip($('#nAuditPeriodicInput', this.el), this.resultnAuditPeriodic);
            } else {
                this.model.set({
                    nAuditPeriodic: $('#nAuditPeriodicInput', this.el).val(),
                    changeTableMaintenancePeriod: true
                });
                Backbone.Utils.Tip.validationTooltip($('#nAuditPeriodicInput', this.el), true);
            }
        },
        checkDiskUsage: function () {
            var nDiskUsageValue = $('#nDiskUsage', this.el).val();
            this.nDiskUsage = Backbone.Utils.validation.rangeFromMinTo99(nDiskUsageValue, 70);
            if (this.nDiskUsage != true) {
                alertMessage.infoMessage(locale.diskUsed + "는 " +this.nDiskUsage, 'info', '', 'small');
                Backbone.Utils.Tip.validationTooltip($('#nDiskUsage', this.el), this.nDiskUsage);
            } else {
                this.model.set({
                    nDiskUsage: $('#nDiskUsage', this.el).val(),
                    changeTableMaintenancePeriod: true
                });
                Backbone.Utils.Tip.validationTooltip($('#nDiskUsage', this.el), true);
            }
        },
        checkDiskWarn: function () {
            var nDiskUsageValue = $('#nDiskWarn', this.el).val();
            this.nDiskWarn = Backbone.Utils.validation.rangeFromMinTo99(nDiskUsageValue, 60);
            if (this.nDiskWarn != true) {
                alertMessage.infoMessage(locale.diskWarn + "는 " +this.nDiskWarn, 'info', '', 'small');
                Backbone.Utils.Tip.validationTooltip($('#nDiskWarn', this.el), this.nDiskWarn);
            } else {
                this.model.set({
                    nDiskWarn: $('#nDiskWarn', this.el).val(),
                    changeTableMaintenancePeriod: true
                });
                Backbone.Utils.Tip.validationTooltip($('#nDiskWarn', this.el), true);
            }
        },
        numberKeyDown: function (e) {
            return Backbone.Utils.validation.keyDownNumber(e);
        },
        numberKeyUp: function (e) {
            Backbone.Utils.validation.keyUpNumber(e);
        }
    });
});