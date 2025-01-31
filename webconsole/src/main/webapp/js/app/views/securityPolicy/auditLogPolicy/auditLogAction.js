/**
 * 감사로그 정책 > 행위
 */
define(function(require) {

    "use strict";

    var $ 						= require('jquery'),
        _ 						= require('underscore'),
        Backbone 				= require('backbone'),
        locale 					= require('i18n!nls/str');

    var tpl 					= require('text!tpl/securityPolicy/auditLog.html'),
        AuditLogPolicyListItem 	= require('views/securityPolicy/auditLogPolicy/auditLogPolicyListItem');

    var AuditLogCollection 		= require('collections/securityPolicy/auditLogPolicyCollection');

    return Backbone.View.extend({
        editable: false,
        template: _.template(tpl),
        initialize: function (options) {
            this.sensorCollection 	= new AuditLogCollection();
            this.consoleCollection 	= new AuditLogCollection();
            this.allCollection 		= new AuditLogCollection();

            this.listenTo(this.sensorCollection, 'add', this.addOne);
            this.listenTo(this.consoleCollection, 'add', this.addOne);

            this.children = [];
        },
        status: function() {
            return this.editable;
        },
        events: {
            'change .contentSelect': 'changeContents'
        },
        render: function() {
            this.editable = false;
            this.$el.html(this.template({
                locale: locale
            }));

            this.$tbody = $('.auditLogTbody', this.el);
            this.$listSelect = $('.contentSelect', this.el);

            this.getSensorListData();
            this.getConsoleListData();

            return this;

        },
        getSensorListData: function() {
            this.sensorCollection.fetch({
                method: 'POST',
                url: 'api/securityPolicy/selectAuditLogPolicyActionSensorList',
                contentType: 'application/json',
                data: JSON.stringify({}),
                async: false,
                success: function(collection) {
                	console.log(collection);
                }
            });
        },
        getConsoleListData: function() {
            this.consoleCollection.fetch({
                method: 'POST',
                url: 'api/securityPolicy/selectAuditLogPolicyActionConsoleList',
                contentType: 'application/json',
                data: JSON.stringify({}),
                async: false,
                success: function(collection) {
                	console.log(collection);
                }
            });
        },
        addOne: function(model) {
            // auditLog Item에서 수정인지 조회인지 구분을 위한 변수 
            var type = 'edit';

            var mailType, mailType0 = '', mailType1 = '', mailType2 = '';	// 메일전송
            var smsType, smsType0 = '', smsType1 = '', smsType2 = '';		// sms전송
            var nApplyType; 		// 사용유무
            var allNApply = true;
            var allType0 = true;  	//프로그램
            var allType1 = true;  	//긴금
            var allType2 = true;  	//가시가청

            if (this.editable == false) {
                type = 'info';
                // [프로그램16,가시8,긴급4,sms2,mail1]
                // 프로그램 = 16,가시 = 8,긴급 = 4,sms = 2,mail=1
                var alarmTypeVal = ['', '', '', '', ''];
                if (model.get('lWarningIndex') != 0) {
                    var alarmTypeToString = this.bitOrConvertor(model.get('nAlarmType'));
                    var alarmTypeLength = 0;
                    if (alarmTypeToString != null) {
                        alarmTypeLength = alarmTypeToString.length;
                    }
                    for (var j = 5 - alarmTypeLength; j < 5; j++) {
                        var result = parseInt(alarmTypeToString[j - (5 - alarmTypeLength)]);
                        if (result == 0) {
                            alarmTypeVal[j] = '';
                        } else {
                            alarmTypeVal[j] = 'checked';
                        }
                    }
                } else {
                    model.set({
                        nAlarmType: 0,
                        lMailGroup: 0,
                        lSmsGroup: 0
                    });
                }
                if (model.get('nApply') == 1) {
                    nApplyType = 'checked';
                } else {
                    nApplyType = '';
                    allNApply = false;
                }

                if (model.get('lMailGroup') == 0) {
                    mailType = locale.beNotInUse;
                    mailType0 = 'selected';
                } else if (model.get('lMailGroup') == 1) {
                    mailType = locale.admin;
                    mailType1 = 'selected';
                } else if (model.get('lMailGroup') == 2) {
                    mailType = locale.user;
                    mailType2 = 'selected';
                } else {
                    mailType = locale.beNotInUse;
                    mailType0 = 'selected';
                }

                if (model.get('lSmsGroup') == 0) {
                    smsType = locale.beNotInUse;
                    smsType0 = 'selected';
                } else if (model.get('lSmsGroup') == 1) {
                    smsType = locale.admin;
                    smsType1 = 'selected';
                } else if (model.get('lSmsGroup') == 2) {
                    smsType = locale.user;
                    smsType2 = 'selected';
                } else {
                    smsType = locale.beNotInUse;
                    smsType0 = 'selected';
                }
                
                if (alarmTypeVal[0] == '') {
                    allType0 = false;
                }
                if (alarmTypeVal[1] == '') {
                    allType1 = false;
                }
                if (alarmTypeVal[2] == '') {
                    allType2 = false;
                }

                model.set({
                    secondType1: alarmTypeVal[0], // 프로그램
                    secondType2: alarmTypeVal[1], // 긴급
                    secondType3: alarmTypeVal[2], // 가시
                    secondType4: alarmTypeVal[3], // sms
                    secondType5: alarmTypeVal[4], // mail
                    nApplyType: nApplyType,
                    mailType: mailType,
                    mailType0: mailType0,
                    mailType1: mailType1,
                    mailType2: mailType2,
                    smsType: smsType,
                    smsType0: smsType0,
                    smsType1: smsType1,
                    smsType2: smsType2
                });
            }
            var item = new AuditLogPolicyListItem({
                model: model,
                type: type,
                tabType: 'action'
            });
            this.children.push(item);
            $('.auditLogTbody', this.el).append(item.render().el);

            if (this.editable == false) {
                item.toggleVisible(false);
            } else {
                item.toggleVisible(true);
            }
            $('.allUsedCheck', this.el).attr("checked", allNApply);
            $('.allProgramAlarm', this.el).attr("checked", allType0);
            $('.allEmergencyAlarm', this.el).attr("checked", allType1);
            $('.allAudibilityAlarm', this.el).attr("checked", allType2);
        },
        appendListItem: function(model) {
            var type;
            if (this.editable) {
                type = "edit";
            } else {
                type = "info";
            }
            var item = new AuditLogPolicyListItem({
                model: model,
                type: type,
                tabType: 'action'
            });
            this.children.push(item);
            this.$tbody.append(item.render().el);

            if (this.editable == false) {
                item.toggleVisible(false);
            } else {
                item.toggleVisible(true);
            }
            var allNApply = true;
            var allType1 = true;
            var allType2 = true;
            var allType3 = true;
            if (model.get('nApply') != 1) {
                allNApply = false;
            }
            if (model.get('secondType1') == '') {
                allType1 = false;
            }
            if (model.get('secondType2') == '') {
                allType2 = false;
            }
            if (model.get('secondType3') == '') {
                allType3 = false;
            }
            $('.allUsedCheck', this.el).prop("checked", allNApply);
            $('.allProgramAlarm', this.el).prop("checked", allType1);
            $('.allEmergencyAlarm', this.el).prop("checked", allType2);
            $('.allAudibilityAlarm', this.el).prop("checked", allType3);
        },
        changeContents: function() {
            var thisView = this;
            var value = this.$listSelect.val();
            this.$tbody.empty();

            if (value == 1) {
                for (var i = 0; i < this.sensorCollection.length; i++) {
                    thisView.appendListItem(thisView.sensorCollection.at(i));
                }
            } else if (value == 3) {
                for (var i = 0; i < this.consoleCollection.length; i++) {
                    thisView.appendListItem(thisView.consoleCollection.at(i));
                }
            } else {
                for (var i = 0; i < this.sensorCollection.length; i++) {
                    thisView.appendListItem(thisView.sensorCollection.at(i));
                }
                for (var i = 0; i < this.consoleCollection.length; i++) {
                    thisView.appendListItem(thisView.consoleCollection.at(i));
                }
            }
        },
        bitOrConvertor: function(data) {
            if (data != null) {
                var result = data.toString(2);
                return result;
            } else {
                return "";
            }
        },
        toggle: function() {
            if (this.editable) {
                _.each(this.children, function (child) {
                    child.toggleVisible(false);
                });
                this.allCollection = new AuditLogCollection();
                var updateParams = [];
//				var updateSensorParams = _.where(this.sensorCollection.toJSON(), {changeData: true});
//				var updateConsoleParams = _.where(this.consoleCollection.toJSON(), {changeData: true});
                this.allCollection.add(this.sensorCollection.toJSON());
                this.allCollection.add(this.consoleCollection.toJSON());
                updateParams = _.extend(updateParams, this.allCollection.toJSON());

                if (updateParams != undefined && updateParams.length > 0) {
                    this.allCollection.fetch({
                        method: 'POST',
                        url: 'api/securityPolicy/updateAuditLogPolicyActionList',
                        contentType: 'application/json',
                        data: JSON.stringify(updateParams),
                        dataType: 'text',
                        async: false,
                        success: function(collection) {
                        	console.log(collection);
                        }
                    });
                }
                this.editable = false;

                return this;
            } else {
                _.each(this.children, function(child) {
                    child.toggleVisible(true);
                });
                this.editable = true;
                return this;
            }
        },
        isValid: function() {
            this.valid = true;
            return this.valid;
        },
        allUsed: function(isChecked) {
            var data = 0;
            var textValue = '';
            if (isChecked) {
                data = 1;
                textValue = 'checked';
            }
            var selType = parseInt($('.contentSelect', this.el).val());
            if (selType == 0 || selType == 1) {
                this.sensorCollection.forEach(function(model) {
                    model.set({
                        nApply: data,
                        nApplyType: textValue,
                        changeData: true
                    });
                }, this);
            }
            if (selType == 0 || selType == 3) {
                this.consoleCollection.forEach(function(model) {
                    model.set({
                        nApply: data,
                        nApplyType: textValue,
                        changeData: true
                    });
                }, this);
            }
        },
        allProgramChecked: function(isChecked) {
            var frm = document.all;
            var value = parseInt(frm.programAlarm[0].value);
            var data = -1;
            var textValue = '';
            if (isChecked) {
                textValue = 'checked';
                data = 1;
            }
            var selType = parseInt($('.contentSelect', this.el).val());
            if (selType == 0 || selType == 1) {
                this.sensorCollection.forEach(function(model) {
                    var nAlarmType = parseInt(model.get('nAlarmType'));
                    var checked = model.get('secondType1');
                    if (isChecked && checked == 'checked') {
                        data = 0;
                    }
                    model.set({
                        secondType1: textValue,
                        nAlarmType: nAlarmType + (value * data),
                        changeData: true
                    });
                }, this);
            }
            if (selType == 0 || selType == 3) {
                this.consoleCollection.forEach(function(model) {
                    var nAlarmType = model.get('nAlarmType');
                    var checked = model.get('secondType1');
                    if (isChecked && checked == 'checked') {
                        data = 0;
                    }
                    model.set({
                        secondType1: textValue,
                        nAlarmType: nAlarmType + (value * data),
                        changeData: true
                    });
                }, this);
            }
        },
        allAudibilityChecked: function(isChecked) {
            var frm = document.all;
            var value = frm.audibilityAlarm[0].value;
            var data = -1;
            var textValue = '';
            if (isChecked) {
                textValue = 'checked';
                data = 1;
            }
            
            var selType = parseInt($('.contentSelect', this.el).val());
            if (selType == 0 || selType == 1) {
                this.sensorCollection.forEach(function (model) {
                    var nAlarmType = model.get('nAlarmType');
                    var checked = model.get('secondType2');
                    if (isChecked && checked == 'checked') {
                        data = 0;
                    }
                    model.set({
                        secondType2: textValue,
                        nAlarmType: nAlarmType + (value * data),
                        changeData: true
                    });
                }, this);
            }
            if (selType == 0 || selType == 3) {
                this.consoleCollection.forEach(function (model) {
                    var nAlarmType = model.get('nAlarmType');
                    var checked = model.get('secondType2');
                    if (isChecked && checked == 'checked') {
                        data = 0;
                    }
                    model.set({
                        secondType2: textValue,
                        nAlarmType: nAlarmType + (value * data),
                        changeData: true
                    });
                }, this);
            }
        },
        allEmergencyChecked: function(isChecked) {
            var frm = document.all;
            var value = frm.emergencyAlarm[0].value;
            var data = -1;
            var textValue = '';
            if (isChecked) {
                textValue = 'checked';
                data = 1;
            }
            
            var selType = parseInt($('.contentSelect', this.el).val());
            if (selType == 0 || selType == 1) {
                this.sensorCollection.forEach(function (model) {
                    var nAlarmType = model.get('nAlarmType');
                    var checked = model.get('secondType3');
                    if (isChecked && checked == 'checked') {
                        data = 0;
                    }
                    model.set({
                        secondType3: textValue,
                        nAlarmType: nAlarmType + (value * data),
                        changeData: true
                    });
                }, this);
            }
            if (selType == 0 || selType == 3) {
                this.consoleCollection.forEach(function (model) {
                    var nAlarmType = model.get('nAlarmType');
                    var checked = model.get('secondType3');
                    if (isChecked && checked == 'checked') {
                        data = 0;
                    }
                    model.set({
                        secondType3: textValue,
                        nAlarmType: nAlarmType + (value * data),
                        changeData: true
                    });
                }, this);
            }
        }
    });
});