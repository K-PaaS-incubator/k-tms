/**
 * manager 시간 동기화 tabs
 */
define(function (require) {

    "use strict";

    var $ 					= require('jquery'),
        Backbone 			= require('backbone'),
        locale 				= require('i18n!nls/str'),
        errorLocale 		= require('i18n!nls/error'),
        alertMessage 		= require('utils/AlertMessage');

    var tpl 				= require('text!tpl/systemSettings/managerTimeSync.html'),
        TimeSyncModel 		= require('models/systemSettings/managerTimeSyncModel'),
        TimeSyncCollection 	= require('collections/systemSettings/managerTimeSyncCollection');

    return Backbone.View.extend({
        editable: false,
        resultServerCount: false, // 표준시간서버 개수 체크
        resultperiodValue: true,
        template: _.template(tpl),
        templateEdit: _.template([
            '<div class="icon-subtitle"><%= locale.timeSyncOption %></div>',
            '<div class="border-dotted-all overflow-hidden padding-t5">',
            '<span class="col-xs-12 description"><input type="checkbox" value="" class="timeSyncCheckbox" name="timeSyncCheckbox"/> <%= locale.standardTimeSyncUsed %></span>',
            '<table class="view-table col-xs-12">',
            '<tr class="border-clear">',
            '<th width="20%"><%= locale.inspectionPeriod %></th>',
            '<td><input type="text" class="form-control col-xs-2 margin-r5 periodInput align-right" name="periodInput" maxlength="5"/> <%= locale.timeMin %></td>',
            '</tr>',
            '</table>',
            '</div>',
            '<div class="icon-subtitle"><%= locale.standardTimeServer %></div>',
            '<div class="border-dotted-all overflow-hidden padding-t5 padding-b5">',
            '<table class="view-table col-xs-12">',
            '<tr class="border-clear">',
            '<th width="20%"><%= locale.server %></th>',
            '<td>',
            '<input class="form-control col-xs-4 margin-r5 serverNameInput" type="text">',
            '<button class="btn btn-default timeSyncInsertBtn margin-r5" type="button "><%= locale.add %></button>',
            '<button class="btn btn-default timeSyncDeleteBtn" type="button"><%= locale.remove %></button>',
            '<select class="form-control float-clear col-xs-6 margin-t10 serverSelect" size="10" multiple="multiple">',
            '</select>',
            '</td>',
            '</tr>',
            '</table>',
            '</div>'
        ].join('')),
        initialize: function () {
            this.model 				= new TimeSyncModel();
            this.collection 		= new TimeSyncCollection();
            this.resultServerName 	= true;
        },
        events: {
            'click .timeSyncInsertBtn'	: 'addServerName',
            'click .timeSyncDeleteBtn'	: 'deleteServerName',
            'change .timeSyncCheckbox'	: 'setCheckBox',
            'change .periodInput'		: 'setCheckBox',
            'keydown .periodInput'		: 'numberKeyDown',
            'keyup .periodInput'		: 'numberKeyUp',
            'blur .serverNameInput'	: 'setServerCheck'

        },
        render: function() {
            this.editable = false;
            this.type = 'update';
            this.$el.html(this.template({
                locale: locale
            }));
            this.getTimeSyncInfo();
            return this;
        },
        renderEdit: function() {
            this.editable = true;
            this.type = 'update';
            this.$el.html(this.templateEdit({
                locale: locale
            }));
            this.setTimeSyncInfo();
            return this;
        },
        toggle: function() {
            if (this.editable) {
                this.mergeTimeSyncInfo();
                return this.render();
            } else {
                return this.renderEdit();
            }
        },
        mergeTimeSyncInfo: function() {
            var thisView = this;
            //var setParams = null;
            var timeServerNameVal = null;
            var periodVal = 0;
            var timeSyncVal = 0;

            // ip개수가 n개일경우 구분자 | 를 사용하여 string 타입으로 만든다.
            if (thisView.collection.length > 0) {
                timeServerNameVal = "";
            }
            for (var i = 0; i < thisView.collection.length; i++) {
                timeServerNameVal += this.collection.at(i).get('strTimeServerName');
                if ((i + 1) != thisView.collection.length) {
                    timeServerNameVal += "|";
                }
            }
            var setParams = {
                nUseTimeSync: this.model.get('nUseTimeSync'),
                strTimeServerName: timeServerNameVal,
                nTimeSyncPeriod: this.model.get('nTimeSyncPeriod')
            };
            this.model.fetch({
                method: 'POST',
                url: 'api/systemSetting/updateManagerTimeSync',
                contentType: 'application/json',
                dataType: '',
                data: JSON.stringify(setParams),
                async: false,
                success: function(result) {
                    var resultValue = result.get('managerTimeSyncRtn');
                }
            });
        },
        getTimeSyncInfo: function() {
            var thisView = this;
            this.model.fetch({
                method: 'POST',
                url: 'api/systemSetting/selectManagerTimeSync',
                contentType: 'application/json',
                data: JSON.stringify({}),
                async: false,
                success: function() {
                    thisView.setTimeSyncInfo();
                }
            });

        },
        setTimeSyncInfo: function() {

            if (!this.editable) {
                // 조회
                if (this.model.get('nUseTimeSync') == 1) {
                    $('.timeSyncCheckbox', this.el).prop('checked', true);
                    $('.period', this.el).append(this.model.get('nTimeSyncPeriod'));
                } else {
                    $('.timeSyncCheckbox', this.el).prop('checked', false);
                }

                var timeServerNameList = this.splitData(this.model.get('strTimeServerName'));
                this.collection.reset();
                if (timeServerNameList != null) {
                    for (var i = 0; i < timeServerNameList.length; i++) {
                        this.collection.add({
                            'strTimeServerName': timeServerNameList[i],
                            'strTimeServerIndex': i
                        });
                    }
                }
            } else {
                // 수정
                if (this.model.get('nUseTimeSync') == 1) {
                    $('.timeSyncCheckbox', this.el).prop('checked', true);
                    $('.periodInput', this.el).val(this.model.get('nTimeSyncPeriod'));
                } else {
                    $('.timeSyncCheckbox', this.el).prop('checked', false);
                }
                for (var i = 0; i < this.collection.length; i++) {
                    this.collection.at(i).set({
                        'strTimeServerIndex': i
                    });
                }
            }
            this.appendServerName();
        },
        setChangeTimeSyncInfo: function() {
            for (var i = 0; i < this.collection.length; i++) {
                this.collection.at(i).set({
                    'strTimeServerIndex': i
                });
            }
            this.appendServerName();
        },
        appendServerName: function() {
            var thisView = this;
            $('.serverSelect', thisView.el).empty();
            for (var i = 0; i < thisView.collection.length; i++) {
                $('.serverSelect', thisView.el).append('<option value=' + thisView.collection.at(i).get('strTimeServerIndex') + '>' + thisView.collection.at(i).get('strTimeServerName') + '</option>');
            }
            this.resultServerCount = Backbone.Utils.Tip.validationTooltip($('.serverNameInput', this.el), true);
        },
        addServerName: function() {
            var serverName = $('.serverNameInput', this.el).val().trim();

            if (serverName == "" || serverName == null) {
                alertMessage.infoMessage(errorLocale.validation.selectedAddServerValid, 'info', '', 'small');
                return false;
            }
            this.serverNameInputValid = Backbone.Utils.validation.validateIpDualCheck($('.serverNameInput', this.el).val());
            if (this.serverNameInputValid != true) {
                Backbone.Utils.Tip.validationTooltip($('.serverNameInput', this.el), this.serverNameInputValid);
                alertMessage.infoMessage(errorLocale.validation.timeSyncNtpServer, 'info', '', 'small');
                return false;
            }
            if (this.collection.length > 0) {
                for (var i = 0; i < this.collection.length; i++) {
                    if (serverName == this.collection.at(i).get('strTimeServerName')) {
                        alertMessage.infoMessage(errorLocale.validation.duplicateServerAddress, 'info', '', 'small');
                        return false;
                    }
                }
            }

            if (this.collection.length >= 10) {
                // 표준시간 서버 추가 개수가 최대 10개 
                alertMessage.infoMessage(errorLocale.validation.addServerMaxValid, 'info', '', 'small');
                // this.resultServerCount = Backbone.Utils.Tip.validationTooltip($('.serverNameInput', this.el), errorLocale.validation.addServerMaxValid);
            } else {
                this.collection.add({
                    'strTimeServerName': serverName,
                    'strTimeServerIndex': this.collection.length
                });
                this.model.set({
                    'changeInfo': true
                });
                this.appendServerName();
            }
        },
        deleteServerName: function() {
            var self = this;
            var del = new TimeSyncCollection();
            $('.serverSelect option:selected').each(function(){
                del.add(self.collection.at($(this).val()));
            });
            for(var i = 0 ; i < del.length; i++) {
                self.collection.remove(del.at(i));
            }
            this.model.set({
                'changeInfo': true
            });
            this.setChangeTimeSyncInfo();
        },
        setCheckBox: function() {
            var enable, checked, disable;
            if ($('.timeSyncCheckbox', this.el).prop('checked')) {
                $('.periodInput', this.el).prop('disabled', false);
                enable = 1;
                checked = "checked";
                disable = "";

                var periodValue = $('.periodInput', this.el).val();
                this.resultperiodValue = Backbone.Utils.validation.rangeFrom1To99999(periodValue);
                if (this.resultperiodValue != true) {
                    alertMessage.infoMessage(this.resultperiodValue, "warn");
                    Backbone.Utils.Tip.validationTooltip($('.periodInput', this.el), this.resultperiodValue);
                } else {
                    Backbone.Utils.Tip.validationTooltip($('.periodInput', this.el), true);
                }

            } else {
                $('.periodInput', this.el).prop('disabled', true);
                enable = 0;
                checked = "";
                disable = "disable";
            }

            this.model.set({
                nUseTimeSync: enable,
                nUseTimeSyncChecked: checked,
                nTimeSyncPeriod: $('.periodInput', this.el).val(),
                changeInfo: true
            });
        },
        // 표준시간 서버 추가시 유효성 검증 및 잘못 입력시 버튼 disabled
        setServerCheck: function() {
            var serverValue = $('.serverNameInput', this.el).val();
            if (serverValue != '') {
                this.resultServerName = Backbone.Utils.validation.validateIpDualCheck(serverValue);
                if (this.resultServerName != true) {
                    alertMessage.infoMessage(errorLocale.validation.timeSyncNtpServer, 'info', '', 'small');
                    Backbone.Utils.Tip.validationTooltip($('.serverNameInput', this.el), this.resultServerName);
                    this.$(".timeSyncInsertBtn").prop("disabled", true);
                } else {
                    Backbone.Utils.Tip.validationTooltip($('.serverNameInput', this.el), true);
                    this.$(".timeSyncInsertBtn").prop("disabled", false);
                }
            } else {
                this.resultServerName = true;
                Backbone.Utils.Tip.validationTooltip($('.serverNameInput', this.el), true);
            }
        },
        splitData: function(data) {
            if (data.lastIndexOf('|') == (data.length - 1)) {
                data = data.substring(0, data.lastIndexOf('|'));
            }
            if (data == null) {
                return data;
            } else {
                return data.split("|");
            }
        },
        isValid: function() {
            this.valid = true;
            if (this.collection.length <= 0) {
                alertMessage.infoMessage(errorLocale.validation.timeSyncServerLength, "warn");
                this.valid = false;
                return false;
            }
            if (!(this.resultServerCount == true && this.resultperiodValue == true && this.resultServerName == true)) {
                this.valid = false;
            }
            return this.valid;
        },
        isResult: function() {
            var result = {
                resultTimeSync: this.model.get('changeInfo')
            };
            return result;
        },
        numberKeyDown: function(e) {
            return Backbone.Utils.validation.keyDownNumber(e);
        },
        numberKeyUp: function(e) {
            Backbone.Utils.validation.keyUpNumber(e);
        }
    });
});