/*
 * 
 */
define(function (require) {

    //"use strict";

    var $ = require('jquery'),
            _ = require('underscore'),
            Backbone = require('backbone'),
            locale = require('i18n!nls/str'),
            analysisDate 	= require('utils/analysisDate'),
            errorLocale = require('i18n!nls/error'),
            alertMessage = require('utils/AlertMessage');
    var ManagerDbBackupModel = require('models/systemSettings/managerDbBackupModel');

    // require template
    var Tpl = require('text!tpl/systemSettings/dbBackupPopup.html');

    return Backbone.View.extend({
        template: _.template(Tpl),
        resultstrMonthFileName: true,
        resultstrDayFileName: true,
        resultnMonthBookDayBefore: true,
        resultnDayBookDayBefore: true,
        initialize: function (options) {
            this.model = new ManagerDbBackupModel();
            this.strDayFileName = options.strDayFileName;
            this.strMonthFileName = options.strMonthFileName;
        },
        events: {
            "change #tmFromDayInput" : "compareToDateBackUp",
            "change #tmToDayInput"   : "compareToDateBackUp",
            "click #nDayConfigFlag": "checkDayConfigFlag",
            "click #excuteBtn": "excuteDbBackup",
//            "click .excuteBtn": "test",
            "keyup #strDayFileNameInputPopup": "inputKeyUpNonKr",
            "focusout #strDayFileNameInputPopup": "inputKeyUpNonKr",
        },
        render: function () {
            this.editable = false;
            this.$el.html(this.template({
                locale: locale,
                model: this.model.toJSON()
            }));

            this.checkDayConfigFlag();
            this.setDayPeriod();
            // 백업 파일 생성 방식 > 파일명 표기
            this.$("#strDayFileNameInput").val(this.strDayFileName);

            return this;
        },
        checkDayConfigFlag: function () {
            if (this.$("#nDayConfigFlag").prop("checked")) {
                //항목 
                $("input[name=nDayTableCheckPop]:checkbox", this.el).each(function () {
                    $(this).prop("disabled", false);
                });
                //백업 기간 범위
                this.$("#nDayBookDayBeforeInput").attr("readOnly", false);

                //백업 테이블 삭제 여부
                this.$("#nDayTableDeleteFlagChecked").prop("disabled", false);

                //백업 파일 생성 방식
                $("input[name=nDayFileFlagRadio]:radio", this.el).each(function () {
                    $(this).prop("disabled", false);
                });
                //백업 파일명
                this.$("#strDayFileNameInput").attr("readOnly", false);

                this.$("#tmFromDayInput").prop("disabled", false);
                this.$("#tmToDayInput").prop("disabled", false);
            } else {
                //항목 
                $("input[name=nDayTableCheckPop]:checkbox", this.el).each(function () {
                    $(this).prop("disabled", true);
                });
                //백업 기간 범위
                this.$("#nDayBookDayBeforeInput").attr("readOnly", true);
                //백업 테이블 삭제 여부
                this.$("#nDayTableDeleteFlagChecked").prop("disabled", true);
                //백업 파일 생성 방식
                $("input[name=nDayFileFlagRadio]:radio", this.el).each(function () {
                    $(this).prop("disabled", true);
                });
                //백업 파일명
                this.$("#strDayFileNameInput").attr("readOnly", true);
                this.$("#tmFromDayInput").prop("disabled", true);
                this.$("#tmToDayInput").prop("disabled", true);
            }
        },
        setDayPeriod: function () {
            Backbone.Calendar.setDayPicker($('#tmFromDayInput', this.el));
            Backbone.Calendar.setDayPicker($('#tmToDayInput', this.el));
        },
        compareToDateBackUp: function (e) {
            var timeBeforeChange = new Date(analysisDate.getAnalysisTimeInitialization())
            var timeTryToChange = new Date(e.currentTarget.value)
            if (timeTryToChange > timeBeforeChange) {
                $('.xdsoft_datetimepicker').hide()
                alertMessage.infoMessage(errorLocale.validation.dateInputErrorMsg, 'info', '', 'small');
                $("#"+e.currentTarget.id).val(analysisDate.getAnalysisTimeInitialization().split(" ")[0])
            }
        },
        test: function () {
            Backbone.Loading.setModalBackup($('body'));
//            Backbone.Loading.setModalLoading($('body'));
            console.log("loading start ~~~~");
            var start = new Date().getTime();
            var chk = true;
            while (chk) {
                if ((new Date().getTime() - start) > 5000) {
                    chk = false;
                }
            }
            Backbone.Loading.removeModalBackup($('body'));
//            Backbone.Loading.removeModalLoading($('body'));
            console.log("loading end !!!!!!!!!!!");
        },
        excuteDbBackup: function () {
            var isResultDay;
            var self = this;
            //일별
            if (this.$("#nDayConfigFlag").prop("checked")) {
                if (this.isValidDay()) {
                    //항목설정
                    var nDayTableCheckValueChecked = "";
                    $("input[name=nDayTableCheckPop]:checkbox").each(function () {
                        if ($(this).prop("checked")) {
                            nDayTableCheckValueChecked = nDayTableCheckValueChecked + "1";
                        } else {
                            nDayTableCheckValueChecked = nDayTableCheckValueChecked + "0";
                        }
                    });
                    nDayTableCheckValueChecked = parseInt(nDayTableCheckValueChecked.split("").reverse().join(""), 2);

                    //백업기간범위
                    var tmFromDay = this.$("#tmFromDayInput").val();
                    var tmToDay = this.$("#tmToDayInput").val();

                    //백업파일생성방식
                    var nDayFileFlagChecked;
                    nDayFileFlagChecked = this.$("input:radio[name='nDayFileFlagRadio']:checked").val();

                    //파일명
                    var strDayFileName;
                    strDayFileName = this.$("#strDayFileNameInputPopup").val();

                    //백업테이블 삭제
                    var nDayTableDeleteFlagChecked;
                    if (this.$("#nDayTableDeleteFlagChecked").prop("checked")) {
                        nDayTableDeleteFlagChecked = 1;
                    } else {
                        nDayTableDeleteFlagChecked = 0;
                    }

                    var setDayParams = {
                        nData: 1,
                        tmFrom: tmFromDay,
                        tmTo: tmToDay,
                        nFileFlag: nDayFileFlagChecked,
                        strFileName: strDayFileName,
                        nTableDel: nDayTableDeleteFlagChecked,
                        nTableCheckValue: nDayTableCheckValueChecked
                    };

                    this.model.fetch({
                        method: 'POST',
                        url: 'api/systemSetting/insertImDbBackup',
                        contentType: 'application/json',
                        dataType: '',
                        data: JSON.stringify(setDayParams),
                        async: false,
                        beforeSend: function () {
                            Backbone.Loading.setModalLoading($('body'));
                        },
                        success: function () {
                            isResultDay = true;
                        },
                        complete: function () {
                            Backbone.Loading.removeModalLoading($('body'));
                        }
                    });
                }

            }

            if (!this.$("#nDayConfigFlag").prop("checked")) {
                alertMessage.infoMessage(errorLocale.validation.dbbackupSelectedValid, 'info', '', 'small');
            }


            if (isResultDay == true) {
                alertMessage.infoMessage(locale.msg.dbbackupSaveSuccess, 'info', '', 'small');
                $('.btn-default', this.el).trigger("click");
//				this.render();
            }
        },
        isValidDay: function () {
            this.valid = true;
            var checkCnt = 0;

            $("input[name=nDayTableCheckPop]:checkbox").each(function () {
                if ($(this).prop("checked")) {
                    checkCnt++;
                }
            });
            if (this.valid && checkCnt == 0) {
                alertMessage.infoMessage(errorLocale.validation.dbbackupSelectedValid, 'info', '', 'small');
                this.valid = false;
                return false;
            }

            //기간 범위
            this.resultTmFromDayInput = Backbone.Utils.validation.validateIsNull($('#tmFromDayInput').val());
            if (this.valid && this.resultTmFromDayInput != true) {
                alertMessage.infoMessage(this.resultTmFromDayInput, "warn");
                Backbone.Utils.Tip.validationTooltip($('#tmFromDayInput'), this.resultTmFromDayInput);
                this.valid = false;
                return false;
            } else {
                Backbone.Utils.Tip.validationTooltip($('#tmFromDayInput'), true);
            }

            this.resultTmToDayInput = Backbone.Utils.validation.validateIsNull($('#tmToDayInput').val());
            if (this.valid && this.resultTmToDayInput != true) {
                alertMessage.infoMessage(this.resultTmToDayInput, "warn");
                Backbone.Utils.Tip.validationTooltip($('#tmToDayInput'), this.resultTmToDayInput);
                this.valid = false;
                return false;
            } else {
                Backbone.Utils.Tip.validationTooltip($('#tmToDayInput'), true);
            }

            if (Backbone.Utils.compareDay($('#tmFromDayInput').val(), $('#tmToDayInput').val()) == false) {
                alertMessage.infoMessage(errorLocale.validation.backupCompareValidation);

                return false;
            }

            //파일명
            this.resultStrDayFileName = Backbone.Utils.validation.isAlphaNumWithUnderscore($('#strDayFileNameInputPopup').val());

            if ($("#strDayFileNameInputPopup").val() == "") {
                alertMessage.infoMessage(errorLocale.validation.invaildNull, "warn");
                Backbone.Utils.Tip.validationTooltip($('#strDayFileNameInputPopup', this.el), false);
                return false;
            } else if (this.valid && !this.resultStrDayFileName) {
                alertMessage.infoMessage(errorLocale.validation.isAlphaNumWithUnderscore, 'info', '', 'small');
                Backbone.Utils.Tip.validationTooltip($('#strDayFileNameInputPopup'), this.resultStrDayFileName);
                this.valid = false;
                return false;
            } else {
                Backbone.Utils.Tip.validationTooltip($('#strDayFileNameInputPopup'), true);
            }

            return this.valid;
        },
        inputKeyUpNonKr: function (e) {
            Backbone.Utils.validation.keyUpNonKr(e);
        },
    });
});