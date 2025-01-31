/**
 * manager tabs
 */
define(function (require) {

    "use strict";

    var $ = require('jquery'),
            Backbone = require('backbone'),
            alertMessage = require('utils/AlertMessage');

    var locale = require('i18n!nls/str'),
        errorLocale = require('i18n!nls/error');

    var tpl = require('text!tpl/systemSettings/dbBackup.html'),
            editTpl = require('text!tpl/systemSettings/dbBackupEdit.html');

    var ManagerDbBackupModel = require('models/systemSettings/managerDbBackupModel');

    var DbBackupPopupView = require('views/systemSettings/systemConfig/manager/dbBackupPopup');
    var BackupFilePopupView = require('views/popup/backupFileListPopup');

    return Backbone.View.extend({
        editable: false,
        template: _.template(tpl),
        templateEdit: _.template(editTpl),
        resultstrDayFileName: true,
        resultnMonthBookDayBefore: true,
        resultnDayBookDayBefore: true,
        resultStrBackupFolder:true,
        initialize: function () {
            this.model = new ManagerDbBackupModel();
        },
        events: {
            "click #nDayConfigFlag": "checkDayConfigFlag",
            "change #nDayConfigFlag": "changenDayConfigFlag",
            "change #nDayBookDayBeforeInput": "changenDayBookDayBefore",
            "keydown #nDayBookDayBeforeInput": "numberKeyDown",
            "keyup #nDayBookDayBeforeInput": "numberKeyUp",
            "blur #strDayFileNameInput": "changeStrDayFileNameInput",
            "blur #strBackupPathNameInput": "changeStrBackupPathInput",
            "click #backupBtn": "backupDb",
            "click #backupFilePopup": "backupFilePopup",
            "keyup #strDayFileNameInput" : "inputKeyUpNonKr",
            "focusout #strDayFileNameInput" : "inputKeyUpNonKr",
        },
        render: function () {
            this.editable = false;
            this.getdbBackup();
            this.$el.html(this.template({
                locale: locale,
                model: this.model.toJSON()
            }));
            return this;
        },
        renderEdit: function () {
            this.editable = true;

            this.$el.html(this.templateEdit({
                locale: locale,
                model: this.model.toJSON()
            }));

            //예약시간 설정 (시)
            for (var i = 0; i < 24; i++) {
                var hourText;
                if (i < 10) {
                    hourText = "0" + i;
                } else {
                    hourText = i;
                }
                this.$("#nDayBookTimeH").append("<option value=" + hourText + ">" + hourText + "</option>");
                this.$("#nMonthBookTimeH").append("<option value=" + hourText + ">" + hourText + "</option>");
            }

            this.$("#nDayBookTimeH > option[value=" + this.model.get('strDayBookTime').substr(0, 2) + "]").attr("selected", "true");
            this.$("#nMonthBookTimeH > option[value=" + this.model.get('strMonthBookTime').substr(0, 2) + "]").attr("selected", "true");

            //예약시간 설정 (분, 초)
            for (var i = 0; i < 60; i++) {
                var minText;
                if (i < 10) {
                    minText = "0" + i;
                } else {
                    minText = i;
                }
                this.$("#nDayBookTimeM").append("<option value=" + minText + ">" + minText + "</option>");
                this.$("#nMonthBookTimeM").append("<option value=" + minText + ">" + minText + "</option>");

                this.$("#nDayBookTimeS").append("<option value=" + minText + ">" + minText + "</option>");
                this.$("#nMonthBookTimeS").append("<option value=" + minText + ">" + minText + "</option>");
            }
            this.$("#nDayBookTimeM > option[value=" + this.model.get('strDayBookTime').substr(3, 2) + "]").attr("selected", "true");
            this.$("#nMonthBookTimeM > option[value=" + this.model.get('strMonthBookTime').substr(3, 2) + "]").attr("selected", "true");
            this.$("#nDayBookTimeS > option[value=" + this.model.get('strDayBookTime').substr(6, 2) + "]").attr("selected", "true");
            this.$("#nMonthBookTimeS > option[value=" + this.model.get('strMonthBookTime').substr(6, 2) + "]").attr("selected", "true");

            //예약시간 설정 (일)
            for (var i = 1; i <= 28; i++) {
                var dayText;
                if (i < 10) {
                    dayText = "0" + i;
                } else {
                    dayText = i;
                }
                this.$("#nMonthBookTimeD").append("<option value=" + i + ">" + dayText + "</option>");
            }
            this.$("#nMonthBookTimeD > option[value=" + this.model.get('nMonthBookDay') + "]").attr("selected", "true");

            //파일 생성 방식 
            this.$("input[name=nDayFileFlagRadio]:radio[value=" + this.model.get("nDayFileFlag") + "]").attr("checked", true);
            this.$("input[name=nMonthFileFlagRadio]:radio[value=" + this.model.get("nMonthFileFlag") + "]").attr("checked", true);

            //백업 테이블 삭제 여부
            if (this.model.get("nDayTableDeleteFlag") == 1) {
                this.$("#nDayTableDeleteFlagChecked").prop("checked", true);
            } else {
                this.$("#nDayTableDeleteFlagChecked").prop("checked", false);
            }

            if (this.model.get("nMonthTableDeleteFlag") == 1) {
                this.$("#nMonthTableDeleteFlagChecked").prop("checked", true);
            } else {
                this.$("#nMonthTableDeleteFlagChecked").prop("checked", false);
            }

            this.checkDayConfigFlag();
            return this;
        },
        toggle: function () {
            if (this.editable) {
                this.mergedbBackupInfo();
                return this.render();
            } else {
                return this.renderEdit();
            }
        },
        mergedbBackupInfo: function () {
            var nDayConfigFlagChecked;
            if (this.$("#nDayConfigFlag").prop("checked")) {
                nDayConfigFlagChecked = 1;
            } else {
                nDayConfigFlagChecked = 0;
            }

            var nMonthConfigFlagChecked;
            if (this.$("#nMonthConfigFlag").prop("checked")) {
                nMonthConfigFlagChecked = 1;
            } else {
                nMonthConfigFlagChecked = 0;
            }

            var strDayBookTimeStr;
            strDayBookTimeStr = this.$("#nDayBookTimeH option:selected").val() + ":" + this.$("#nDayBookTimeM option:selected").val() + ":" + this.$("#nDayBookTimeS option:selected").val();

            var strMonthBookTimeStr;
            strMonthBookTimeStr = this.$("#nMonthBookTimeH option:selected").val() + ":" + this.$("#nMonthBookTimeM option:selected").val() + ":" + this.$("#nMonthBookTimeS option:selected").val();

            var nMonthBookDay;
            nMonthBookDay = this.$("#nMonthBookTimeD").val();

            var nDayFileFlagChecked;
            var nDayFileFlagCheckedValue;
            nDayFileFlagChecked = this.$("input:radio[name='nDayFileFlagRadio']:checked").val();
            if(nDayFileFlagChecked == 0) {
            	nDayFileFlagCheckedValue = locale.tableBackup;
            } else {
            	nDayFileFlagCheckedValue = locale.daysAllBackup;
            }

            var nMonthFileFlagChecked;
            var nMonthFileFlagCheckedValue;
            nMonthFileFlagChecked = this.$("input:radio[name='nMonthFileFlagRadio']:checked").val();
            if(nMonthFileFlagChecked == 0) {
            	nMonthFileFlagCheckedValue = locale.tableBackup;
            } else {
            	nMonthFileFlagCheckedValue = locale.daysAllBackup;
            }
            
            var nDayTableDeleteFlagChecked;
            if (this.$("#nDayTableDeleteFlagChecked").prop("checked")) {
                nDayTableDeleteFlagChecked = 1;
            } else {
                nDayTableDeleteFlagChecked = 0;
            }
            var nMonthTableDeleteFlagChecked;
            if (this.$("#nMonthTableDeleteFlagChecked").prop("checked")) {
                nMonthTableDeleteFlagChecked = 1;
            } else {
                nMonthTableDeleteFlagChecked = 0;
            }

            var nDayTableCheckValueChecked = "";
            $("input[name=nDayTableCheck]:checkbox").each(function () {
                if ($(this).prop("checked")) {
                    nDayTableCheckValueChecked = nDayTableCheckValueChecked + "1";
                } else {
                    nDayTableCheckValueChecked = nDayTableCheckValueChecked + "0";
                }
            });
            nDayTableCheckValueChecked = parseInt(nDayTableCheckValueChecked.split("").reverse().join(""), 2);
            
            var setParams = {
                nDayConfigFlag: nDayConfigFlagChecked,
                nMonthConfigFlag: nMonthConfigFlagChecked,
                strDayBookTime: strDayBookTimeStr,
                strDayBookTimeValue: this.$("#nDayBookTimeH option:selected").val() + ":" + this.$("#nDayBookTimeM option:selected").val() + ":" + this.$("#nDayBookTimeS option:selected").val(),
                nMonthBookDay: nMonthBookDay,
                strMonthBookTime: strMonthBookTimeStr,
                strMonthBookTimeValue: this.$("#nMonthBookTimeH option:selected").val() + ":" + this.$("#nMonthBookTimeM option:selected").val() + ":" + this.$("#nMonthBookTimeS option:selected").val(),
                nDayBookDayBefore: this.$("#nDayBookDayBeforeInput").val(), 	// 백업 기간 범위 (일)
                nMonthBookDayBefore: this.$("#nMonthBookDayBeforeInput").val(), // 백업 기간 범위 (월)
                strBackupPathName: this.$("#strBackupPathNameInput").val(),
                nDayFileFlag: nDayFileFlagChecked,
                nMonthFileFlag: nMonthFileFlagChecked,
                strDayFileName: this.$("#strDayFileNameInput").val(),
                strMonthFileName: this.$("#strMonthFileNameInput").val(),
                nDayTableDeleteFlag: nDayTableDeleteFlagChecked,
                nMonthTableDeleteFlag: nMonthTableDeleteFlagChecked,
                nDayTableCheckValue: nDayTableCheckValueChecked,
                nDayFileFlagCheckedValue: nDayFileFlagCheckedValue,		// 추가
                nMonthFileFlagCheckedValue: nMonthFileFlagCheckedValue	// 추가 
            };

            this.model.fetch({
                method: 'POST',
                url: 'api/systemSetting/updateDbBackup',
                contentType: 'application/json',
                dataType: '',
                data: JSON.stringify(setParams),
                async: false,
                success: function (model) {
                    var resultValue = model.get('managerBackupRtn');
                }
            });
        },
        getdbBackup: function () {
            var self = this;
            self.model.fetch({
                method: 'POST',
                async: false,
                data: JSON.stringify({}),
                url: 'api/systemSetting/selectDbBackup',
                contentType: 'application/json',
                success: function (model) {
                    self.setDbBackupData();
                }
            });
        },
        setDbBackupData: function () {
            var tempDayConfigFlagChecked, tempMonthConfigFlagChecked, tempDayConfigFlag, tempMonthConfigFlag, tempDayBookTime, tempMonthBookDay, tempMonthBookTime;
            var tempDayBookDayBefore, tempMonthBookDayBefore, tempDayFileFlag, tempMonthFileFlag;
            var tempDayTableDeleteFlag, tempMonthTableDeleteFlag;

            if (this.model.get("nDayConfigFlag") == 1) {
                tempDayConfigFlag = locale.daily + " <b>" + this.model.get("strDayBookTime") + "</b> " + locale.backupStart;
                tempDayBookTime = this.model.get("strDayBookTime");
                tempDayBookDayBefore = locale.startExecut + " <b>" + this.model.get("nDayBookDayBefore") + "</b> " + locale.prevDay;

                if (this.model.get("nDayFileFlag") == 1) {
                    tempDayFileFlag = "<b>" + locale.classifiedByDayIntegrated + "</b> " + locale.backup + "</br><span class='description margin-r5'>" + locale.fileName + "</span>" + this.model.get("strDayFileName") + "_yy_mm_dd.dmp";
                } else {
                    tempDayFileFlag = "<b>" + locale.byTable + "</b> " + locale.backup;
                }

                if (this.model.get("nDayTableDeleteFlag") == 1) {
                    tempDayTableDeleteFlag = locale.deleted;
                } else {
                    tempDayTableDeleteFlag = locale.unDeleted;
                }

                tempDayConfigFlagChecked = "checked";
            } else {
                tempDayConfigFlag = locale.noActionSet;
                tempDayBookTime = locale.noActionSet;
                tempDayBookDayBefore = locale.noActionSet;
                tempDayFileFlag = locale.noActionSet;
                tempDayTableDeleteFlag = locale.noActionSet;
                tempDayConfigFlagChecked = "";
            }
            //일별 테이블 항목 설정 
            var tempDayToString = this.model.get("nDayTableCheckValue").toString(2);
            var tempDayFullValue = [0, 0, 0, 0, 0];
            var tempDayFullValueChecked = [];
            for (var i = 5 - tempDayToString.length; i < 5; i++) {
                tempDayFullValue[i] = parseInt(tempDayToString[i - (5 - tempDayToString.length)]);
            }

            for (var j = 0; j < 5; j++) {
                if (tempDayFullValue[j] == 0) {
                    tempDayFullValueChecked[j] = "";
                } else {
                    tempDayFullValueChecked[j] = "checked";
                }
            }

            //월별 테이블 항목 설정 
            var tempMonthToString = this.model.get("nMonthTableCheckValue").toString(2);
            var tempMonthFullValue = [0, 0];
            var tempMonthFullValueChecked = [];
            for (var i = 2 - tempMonthToString.length; i < 2; i++) {
                tempMonthFullValue[i] = parseInt(tempMonthToString[i - (2 - tempMonthToString.length)]);
            }

            for (var j = 0; j < 2; j++) {
                if (tempMonthFullValue[j] == 0) {
                    tempMonthFullValueChecked[j] = "";
                } else {
                    tempMonthFullValueChecked[j] = "checked";
                }
            }

            this.model.set({
                nDayConfigFlagStr: tempDayConfigFlag,
                nMonthConfigFlagStr: tempMonthConfigFlag,
                nDayBookTimeStr: tempDayBookTime,
                nMonthBookTimeStr: tempMonthBookTime,
                nDayBookDayBeforeStr: tempDayBookDayBefore,
                nMonthBookDayBeforeStr: tempMonthBookDayBefore,
                nDayFileFlagStr: tempDayFileFlag,
                nMonthFileFlagStr: tempMonthFileFlag,
                nDayTableDeleteFlagStr: tempDayTableDeleteFlag,
                nMonthTableDeleteFlagStr: tempMonthTableDeleteFlag,
                nDayTableCheckValueChecked: tempDayFullValueChecked,
                nMonthTableCheckValueChecked: tempMonthFullValueChecked,
                nDayConfigFlagChecked: tempDayConfigFlagChecked,
                nMonthConfigFlagChecked: tempMonthConfigFlagChecked
            });
        },
        checkDayConfigFlag: function () {
            if (this.$("#nDayConfigFlag").prop("checked")) {
                //항목 
                $("input[name=nDayTableCheck]:checkbox").each(function () {
                    $(this).prop("disabled", false);
                });
                //백업 기간 범위
                this.$("#nDayBookDayBeforeInput").attr("disabled", false);

                //백업 테이블 삭제 여부
                this.$("#nDayTableDeleteFlagChecked").prop("disabled", false);

                //백업 파일 생성 방식
                $("input[name=nDayFileFlagRadio]:radio").each(function () {
                    $(this).prop("disabled", false);
                });
                //백업 folder
                this.$("#strBackupPathNameInput").attr("disabled", false);
                
                //백업 파일명
                this.$("#strDayFileNameInput").attr("disabled", false);

                //예약시간
                this.$("#nDayBookTimeH").prop("disabled", false);
                this.$("#nDayBookTimeM").prop("disabled", false);
                this.$("#nDayBookTimeS").prop("disabled", false);
            } else {
                //항목 
                $("input[name=nDayTableCheck]:checkbox").each(function () {
                    $(this).prop("disabled", true);
                });
                //백업 기간 범위
                this.$("#nDayBookDayBeforeInput").attr("disabled", true);
                //백업 테이블 삭제 여부
                this.$("#nDayTableDeleteFlagChecked").prop("disabled", true);
                //백업 파일 생성 방식
                $("input[name=nDayFileFlagRadio]:radio").each(function () {
                    $(this).prop("disabled", true);
                });
                //백업 folder
                this.$("#strBackupPathNameInput").attr("disabled", true);
                //백업 파일명
                this.$("#strDayFileNameInput").attr("disabled", true);
                //예약시간
                this.$("#nDayBookTimeH").prop("disabled", true);
                this.$("#nDayBookTimeM").prop("disabled", true);
                this.$("#nDayBookTimeS").prop("disabled", true);
            }
        },
        changenDayConfigFlag: function () {
            this.model.set({
                changeInfo: true
            });
        },
        changenDayBookDayBefore: function () {

            var nDayBookDayBeforeInputValue = $('#nDayBookDayBeforeInput', this.el).val();
            this.resultnDayBookDayBefore = Backbone.Utils.validation.rangeFrom1To999(nDayBookDayBeforeInputValue);
            if (this.resultnDayBookDayBefore != true) {
                alertMessage.infoMessage(this.resultnDayBookDayBefore, "warn");
                Backbone.Utils.Tip.validationTooltip($('#nDayBookDayBeforeInput', this.el), this.resultnDayBookDayBefore);
                return false;
            } else {
                Backbone.Utils.Tip.validationTooltip($('#nDayBookDayBeforeInput', this.el), true);
            }
            return true;
        },
        changeStrDayFileNameInput: function () {
            var strDayFileNameValue = $('#strDayFileNameInput', this.el).val();
            if (strDayFileNameValue == "") {
                alertMessage.infoMessage(errorLocale.validation.invaildNull, "warn");
                Backbone.Utils.Tip.validationTooltip($('#strDayFileNameInput', this.el), false);
                return false;
            } else {
                // this.resultstrDayFileName = Backbone.Utils.validation.validateIsNull(strDayFileNameValue);
                this.resultstrDayFileName = Backbone.Utils.validation.isAlphaNumWithUnderscore(strDayFileNameValue);
                if (!this.resultstrDayFileName) {
                    // alertMessage.infoMessage(this.resultstrDayFileName, "warn");
                    alertMessage.infoMessage(errorLocale.validation.isAlphaNumWithUnderscore, 'info', '', 'small');
                    Backbone.Utils.Tip.validationTooltip($('#strDayFileNameInput', this.el), this.resultstrDayFileName);
                    return false;
                } else {
                    Backbone.Utils.Tip.validationTooltip($('#strDayFileNameInput', this.el), true);
                }
            }
            return true
        },
        changeStrBackupPathInput: function () {
            var strDayFileNameValue = $('#strBackupPathNameInput', this.el).val();
            this.resultStrBackupFolder = Backbone.Utils.validation.validateIsNull(strDayFileNameValue);
            if (this.resultStrBackupFolder != true) {
                alertMessage.infoMessage(this.resultStrBackupFolder, "warn");
                Backbone.Utils.Tip.validationTooltip($('#strBackupPathNameInput', this.el), this.resultStrBackupFolder);
                return false;
            } else {
                Backbone.Utils.Tip.validationTooltip($('#strBackupPathNameInput', this.el), true);
            }
            return true;
        },
        isValid: function () {
            this.valid = true;
            if (this.$("#nDayConfigFlag").prop("checked")) {
                var tableCheck = false;
                $("input[name=nDayTableCheck]:checkbox").each(function () {
                    if ($(this).is(":checked") == true) {
                        tableCheck = true;
                    }
                });
                if (tableCheck == false) {
                    alertMessage.infoMessage(errorLocale.validation.dbbackupSelectedValid, "warn");
                    this.valid = false
                    return false;
                } else {
                    this.valid = true;
                }

                var chk = true;
                chk = this.changenDayBookDayBefore();
                if (chk == false) {
                    this.valid = false;
                    return false;
                }
                chk = this.changeStrBackupPathInput();
                if (chk == false) {
                    this.valid = false;
                    return false;
                }
                chk = this.changeStrDayFileNameInput();
                if (chk == false) {
                    this.valid = false;
                    return false;
                }
            }
            
            return this.valid;
        },
        isResult: function () {
            var result = {
                resultDbBackup: this.model.get('changeInfo')
            };
            return result;
        },
        backupDb: function () {
            Backbone.ModalView.msg({
                size: 'large',
                type: 'info',
                title: "즉시 DB 백업",
                isFooter: false,
                body: new DbBackupPopupView({
                    strDayFileName: this.model.get('strDayFileName'),
                    strMonthFileName: this.model.get('strMonthFileName')
                })
            });
        },
        backupFilePopup: function () {
            Backbone.ModalView.msg({
                size: 'large',
                type: 'info',
                title: "DB 백업 파일 목록",
                isFooter: false,
                body: new BackupFilePopupView({
                    strDayFileName: this.model.get('strDayFileName'),
                    strMonthFileName: this.model.get('strMonthFileName')
                })
            });
        },
        numberKeyDown: function (e) {
            return Backbone.Utils.validation.keyDownNumber(e);
        },
        numberKeyUp: function (e) {
            Backbone.Utils.validation.keyUpNumber(e);
        },
        inputKeyUpNonKr: function(e) {
            Backbone.Utils.validation.keyUpNonKr(e);
        }
    });
});