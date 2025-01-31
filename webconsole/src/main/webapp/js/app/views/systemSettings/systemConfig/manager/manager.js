/**
 * @author leekyunghee update 18.02.05
 * @title 시스템관리_매니저_등록정보  
 * @description 매니저 설정 정보를 등록한다.  
 */
define(function (require) {

    "use strict";

    var $ = require('jquery'),
            Backbone = require('backbone'),
            alertMessage = require('utils/AlertMessage');

    var locale = require('i18n!nls/str'),
            errorLocale = require('i18n!nls/error'),
            aesUtil = require('utils/security/AesUtil'),
            dataExpression = require('utils/dataExpression');

    var tpl = require('text!tpl/systemSettings/managerTab.html'),
            manageTpl = require('text!tpl/systemSettings/managerEditTab.html'),
            ManagerModel = require('models/systemSettings/managerModel'),
            SystemConfModel = require('models/systemSettings/systemConfModel'),
            SystemConfCollection = require('collections/systemSettings/systemConfCollection');
    var SensorModel = require('models/systemSettings/sensorModel');
    var SensorCollection = require('collections/systemSettings/sensorCollection');

    return Backbone.View.extend({
        editable: false,
        formEl: "form.managerForm",
        template: _.template(tpl),
        templateEdit: _.template(manageTpl),
        resultTime: true,
        resultTimeMax: true,
        resultThreshold: true,
        resultThresholdMax: true,
        resultEmailServer: true,
        initialize: function () {
            this.model = new ManagerModel();
            this.patternUpdateModel = new ManagerModel();
            this.offlinePatternUpdateModel = new ManagerModel();
            this.sessionModel = new SystemConfModel();
            this.loginFailCountModel = new SystemConfModel();
            this.loginLockTime = new SystemConfModel();
            this.sftpId = new SystemConfModel();
            this.sftpPwd = new SystemConfModel();
            this.systemConfCollection = new SystemConfCollection();
            this.sftpConfCollection = new SystemConfCollection();
            this.sensorModel = new SensorModel();
            this.listenTo(this.patternUpdateModel, 'change', this.setPatternUpdateInfo);
            this.ipMonitorCollection = new SensorCollection();
            this.deleteIpMonitorCollection = new SensorCollection();

            this.loginAuthIpArr = [];
            this.monIpArr = [];
            this.reputeModel;
            this.emailServerValidate = true;
            this.emailPortValidate = true;
            this.sftpIdValidate = true;
            this.sftpPwdValidate = true;
        },
        events: {
//            "change #strEmailServerInput": "changEmailServer", // 이메일 서버 변경
            "change #strAlertProgramPathNameInput": "changeAlertProgramPathName", // 외부프로그램설정 변경
            "change #nTrafficThresholdInput": "changeTrafficThreshold", // 트래픽 이상설정> 기준트래픽변경
            "change #nTrafficThresholdTimeInput": "changeTrafficThreshold", // 트래픽 이상설정> 기준시간변경,
            "change #nTrafficThresholdMaxInput": "changeTrafficThresholdMax", // 트래픽 이상설정> 기준트래픽변경
            "change #nTrafficThresholdTimeMaxInput": "changeTrafficThresholdMax", // 트래픽 이상설정> 기준시간변경,
            "click .loginIpInsertBtn": "loginIpAdd",
            "click .loginIpDeleteBtn": "loginIpDelete",
            "keydown #nTrafficThresholdInput": "numberInput",
            "keyup #nTrafficThresholdInput": "numberKeyUp",
            "keydown #nTrafficThresholdTimeInput": "numberInput",
            "keyup #nTrafficThresholdTimeInput": "numberKeyUp",
            "keydown #nTrafficThresholdMaxInput": "numberInput",
            "keyup #nTrafficThresholdMaxInput": "numberKeyUp",
            "keydown #nTrafficThresholdTimeMaxInput": "numberInput",
            "keyup #nTrafficThresholdTimeMaxInput": "numberKeyUp",
//            "keydown #strEmailPortInput": "numberInput",
//            "keyup #strEmailPortInput": "numberKeyUp",
            "change #strEmailPortInput": "validEmailPort",
            "change #strEmailServerInput": "validEmailServerIp",
        },
        status: function () {
            return this.editable;
        },
        statusType: function () {
            return this.type;
        },
        render: function () {
            this.editable = false;
            this.type = "update";
            this.getManagerInfo();
            this.getSensorDetailInfo();
            this.setSensorIpMonitoringData();
            var etherNet = this.sensorModel.get('ethoNetList');
            var etherNetNameTr = "<tr>";
            var etherNetIpTr = "<tr>";
            var width = 100/etherNet.length;
            for(var i = 0 ; i < etherNet.length ; i++) {
                var ip = etherNet[i].ip;
                if (ip == 'null' || ip == null) {
                    ip = '';
                }
                etherNetNameTr += '<th width="' + width + '%">' + etherNet[i].ethoNet + '</th>';
                etherNetIpTr += "<td>" + ip + "</td>";
            }
            etherNetNameTr += "</tr>";
            etherNetIpTr += "</tr>";
            var sftpPwd = "";
            if (this.sftpId.get('value') != null && this.sftpId.get('value') != '') {
                sftpPwd = "********";
            }
            if(this.model.get('emailPwd') != null &&  this.model.get('emailPwd') != ''){
            	this.model.set({
            		strEmailUserPwdView : "********"
            	});
            }
            this.$el.html(this.template({
                locale: locale,
                model: this.model.toJSON(),
                sensorModel: this.sensorModel.toJSON(),
                sessionModel: this.sessionModel.toJSON(),
                loginFailCountModel: this.loginFailCountModel.toJSON(),
                loginLockTimeModel: this.loginLockTime.toJSON(),
                sftpId: this.sftpId.toJSON(),
                sftpPwd: sftpPwd,
                etherNetNameTr : etherNetNameTr,
                etherNetIpTr : etherNetIpTr
            }));
            this.setLoginIp();
            return this;
        },
        renderEdit: function () {
            var self = this;
            this.editable = true;
            this.type = "update";
            var tempStrServer = "";
            var pass = '';
//            console.log('sftp>>>',this.sftpPwd.toJSON());
//            if(this.sftpPwd.get('value') != null && this.sftpPwd.get('value') != ''){
//            	pass = aesUtil.decrypt(this.sftpId.get('value').hexEncode().toUpperCase(), this.sftpPwd.get('value'));
//            }
//            console.log('sftp>>>',pass);
            this.$el.html(this.templateEdit({
                locale: locale,
                model: this.model.toJSON(),
                sensorModel: this.sensorModel.toJSON(),
                sessionModel: this.sessionModel.toJSON(),
                loginFailCountModel: this.loginFailCountModel.toJSON(),
                loginLockTimeModel: this.loginLockTime.toJSON(),
                sftpId: this.sftpId.toJSON(),
                sftpPwd: this.sftpPwd.toJSON(),
            }));
            this.setLoginIp();

            $('#strEmailSecurity option').each((index, item) => {
                if ($(item).val() === self.model.get('emailSecurity'))
                    $(item).prop('selected', true);
                else
                    $(item).prop('selected', false);
            });

            return this;
        },
        toggle: function () {
            if (this.editable) {
                this.mergeManagerInfo();
                if (this.model.attributes.managerSettingRtn == -99)
                    this.valid = false;
                this.updateSensorInfo();
                return this.render();
            } else {
                return this.renderEdit();
            }
        },
        mergeManagerInfo: function () {
            var nAutoUpdateCheck;
            var emailPassword = this.snmpEncyption(this.$("#strEmailUserIdInput").val(), this.$("#strEmailUserPwdInput").val());
            var sftpPassword = this.snmpEncyption($('#sftpId').val(), $('#sftpPwd').val());
            
            console.log('emailPassword', emailPassword);

            this.sessionModel.set({value: $('#sessionTime').val()});
            this.loginFailCountModel.set({value: $('#lofinFailCount').val()});
            this.loginLockTime.set({value: $('#lockTime').val()});
            this.sftpId.set({value: $('#sftpId').val()});
            this.sftpPwd.set({value: sftpPassword});

            //console.log('sessionModel: '+ JSON.stringify(this.sessionModel));
            //console.log('loginFailCountModel: '+ JSON.stringify(this.loginFailCountModel));
            //console.log('loginLockTime: '+ JSON.stringify(this.loginLockTime));

            this.systemConfCollection.add(this.sessionModel);
            this.systemConfCollection.add(this.loginFailCountModel);
            this.systemConfCollection.add(this.loginLockTime);
            this.sftpConfCollection.add(this.sftpId);
            this.sftpConfCollection.add(this.sftpPwd);
            
            console.log('sftpConfCollection', this.sftpConfCollection);

            var setParams = {
                emailServer: this.$("#strEmailServerInput").val(),
                emailSecurity: this.$("#strEmailSecurity").val(),
                emailPort: this.$("#strEmailPortInput").val(),
                emailUserId: this.$("#strEmailUserIdInput").val(),
                emailUserPwd: emailPassword,
                systemConfList: this.systemConfCollection.toJSON(),
                sftpConfList: this.sftpConfCollection.toJSON(),
                lockValue: this.$("#lofinFailCount option:selected").val(),
                timeValue: this.$("#lockTime option:selected").val(),
                sessionValue: this.$("#sessionTime option:selected").val(),
                loginAuthIpList: this.loginAuthIpArr
            };
            console.log('param>> ',JSON.stringify(setParams));
            this.model.fetch({
                method: 'POST',
                url: 'api/systemSetting/updateManagerSettingInfo',
                contentType: 'application/json',
                dataType: '',
                data: JSON.stringify(setParams),
                async: false,
                success: function (model) {
                    model.get('managerSettingRtn');
                }
            });
        },
        getManagerInfo: function () {
            var self = this;

            this.model.fetch({
                method: 'POST',
                async: false,
                data: JSON.stringify({}),
                url: 'api/systemSetting/selectManagerSettingInfo',
                contentType: 'application/json',
                success: function (model) {
                    self.setManagerData();
                }
            });
        },
        setManagerData: function () {
            var tempStrEmailServer = "";


            //이메일 서버
            if (this.model.get("emailServer") == "" || this.model.get("emailServer") == null) {
                tempStrEmailServer = "-";
            } else {
                tempStrEmailServer = this.model.get("strEmailServer");
            }
            
            if(this.model.get("emailUserPwd") != null && this.model.get("emailUserPwd") != ""){
            	var hex = this.model.get("emailUserId").hexEncode(); 
                var ePwd = aesUtil.decrypt(hex.toUpperCase(), this.model.get("emailUserPwd"));
                this.model.set({
                	emailUserPwd : ePwd
                })
            }
            
            var systemConf = this.model.get('systemConfList');
            var sftpid = '', sftppwd = '';
            for (var i = 0; i < systemConf.length; i++) {
                if (systemConf[i].key == "session.time") {
                    this.sessionModel.set({idx: systemConf[i].idx, key: systemConf[i].key, value: systemConf[i].value});
                } else if (systemConf[i].key == "user.lock.fail.count") {
                    this.loginFailCountModel.set({idx: systemConf[i].idx, key: systemConf[i].key, value: systemConf[i].value});
                } else if (systemConf[i].key == "user.lock.time") {
                    this.loginLockTime.set({idx: systemConf[i].idx, key: systemConf[i].key, value: systemConf[i].value});
                } else if (systemConf[i].key == "sftp.id") {
                    this.sftpId.set({idx: systemConf[i].idx, key: systemConf[i].key, value: systemConf[i].value});
                    sftpid = systemConf[i].value;
                } else if (systemConf[i].key == "sftp.pwd") {
                    this.sftpPwd.set({idx: systemConf[i].idx, key: systemConf[i].key, value: systemConf[i].value});
                    sftppwd = systemConf[i].value;
                }
            }
            
            console.log('!!!!!!!!!!!!!!!',sftppwd);
            if(sftppwd != ''){
            	var hex = sftpid.hexEncode(); 
                var fPwd = aesUtil.decrypt(hex.toUpperCase(), sftppwd);
                this.sftpPwd.set({
                	value:fPwd
                });
                console.log('!!!!!!!!!!!!!!!!',fPwd);
            }
            
            this.loginAuthIpArr = this.model.get('loginAuthIpList');
            this.setLoginIp();
            var mailUserPwd = "";
            var mailPwdCss = "";
            if (this.model.get("emailUserPwd") != '') {
                mailUserPwd = "********";
            } else {
                mailPwdCss = 'autocomplete="new-password"';
            }
            this.model.set({
                strEmailServerView: tempStrEmailServer,
                strEmailUserPwdView:mailUserPwd,
                emailPwdCss:mailPwdCss,
            });
        },
        changEmailServer: function () {
            var value = $('#strEmailServerInput', this.el).val();
            if (value != null && value != "") {
                this.resultEmailServer = Backbone.Utils.validation.validateMailServer(value);

                if (this.resultEmailServer != true) {
                    Backbone.Utils.Tip.validationTooltip($('#strEmailServerInput'), this.resultEmailServer);
                } else {
                    Backbone.Utils.Tip.validationTooltip($('#strEmailServerInput'), true);
                    this.resultEmailServer = true;
                }
            }

            this.model.set({
                strEmailServer: value,
                changeStrEmailServer: true
            });
        },
        isValid: function () {
            this.valid = true;
            if (this.emailServerValidate != true) {
                this.valid = false;
            }
            if (this.emailPortValidate != true) {
                this.valid = false;
            }
            if (this.loginAuthIpArr.length == 0) {
                alertMessage.infoMessage(errorLocale.validation.loginIpValid, "warn");
                this.valid = false;
            }
            Backbone.Utils.Tip.validationTooltip($('#sftpPwd'), true);
            Backbone.Utils.Tip.validationTooltip($('#sftpId'), true);
            if ($('#sftpId').val() != '') {
                if ($('#sftpPwd').val() == '' ) {
                    alertMessage.infoMessage(errorLocale.validation.sftpPwdValid, "warn");
                    Backbone.Utils.Tip.validationTooltip($('#sftpPwd'), false);
                    this.valid = false;
                }
            } else {
                if ($('#sftpPwd').val() != '' ) {
                    alertMessage.infoMessage(errorLocale.validation.sftpIdValid, "warn");
                    Backbone.Utils.Tip.validationTooltip($('#sftpId'), false);
                    this.valid = false;
                }
            }
            return this.valid;
        },
        isResult: function () {
            var result = {
                resultstrEmailServer: this.model.get('changeStrEmailServer'),
            };
            return result;
        },
        setLoginIp: function () {
            var thisView = this;
            $('.loginIpSelect', thisView.el).empty();
            for (var i = 0; i < thisView.loginAuthIpArr.length; i++) {
                $('.loginIpSelect', thisView.el).append('<option value=' + thisView.loginAuthIpArr[i] + '>' + thisView.loginAuthIpArr[i] + '</option>');
            }
            this.loginInputValidate = Backbone.Utils.Tip.validationTooltip($('.loginIpInput', this.el), true);
        },
        loginIpAdd: function () {
            var loginAuthIp = $('.loginIpInput', this.el).val().trim();
            if (loginAuthIp == null || loginAuthIp == '') {
                alertMessage.infoMessage(errorLocale.validation.inputIpNullValid, 'info', '', 'small');
                return false;
            }
            this.loginInputValidate = Backbone.Utils.validation.validateLoginIpCheck($('.loginIpInput', this.el).val());
            if (this.loginInputValidate != true) {
                alertMessage.infoMessage(this.loginInputValidate, 'warn', '', 'small');
                Backbone.Utils.Tip.validationTooltip($('.loginIpInput', this.el), this.loginInputValidate);
                return false;
            }
            if (this.loginAuthIpArr.length >= 2) {
                alertMessage.infoMessage(errorLocale.validation.authIpTwo, 'info', '', 'small');
                return false;
            }
            if (this.loginAuthIpArr.length > 0) {
                var index = this.loginAuthIpArr.indexOf(loginAuthIp);
                if (index >= 0) {
                    alertMessage.infoMessage(errorLocale.validation.duplicateIp, 'info', '', 'small');
                    return false;
                }
            }
            this.loginAuthIpArr.push(loginAuthIp);
            this.setLoginIp();
            $('.loginIpInput', this.el).val('');
        },
        loginIpDelete: function () {
            var selIp = String($('.loginIpSelect', this.el).val());
            var index = this.loginAuthIpArr.indexOf(selIp);
            this.loginAuthIpArr.splice(index, index + 1);
            this.setLoginIp();
        },
        loginIpValidate: function () {
            return true;
        },
        setSensorIpMonitoringData: function () {
            var thisView = this;
            this.ipMonitorCollection.fetch({
                method: 'POST',
                url: 'api/systemSetting/selectSensorIpMonitoringList',
                contentType: 'application/json',
                data: JSON.stringify(),
                async: false,
                success: function (collection) {
                	console.log(collection);
                }
            });
        },
        addOneIpMonitor: function () {
            var thisView = this;
            if (thisView.editable == false) {
                $('#ipMonitorTbody', this.el).empty();

                if (thisView.ipMonitorCollection.length > 0) {
                    for (var i = 0; i < thisView.ipMonitorCollection.length; i++) {
                        thisView.ipMonitorCollection.at(i).set({
                            'ipIndex': i
                        });
                        var sensorIpMonitorItem = new SensorIpMonitorItem({
                            model: this.ipMonitorCollection.at(i)
                        });
                        this.monIpArr.push(sensorIpMonitorItem);
                        $('#ipMonitorTbody', this.el).append(sensorIpMonitorItem.render().el);
                    }
                } else {
                    $("#header-fix", thisView.el).addClass("nodata");
                }
            } else {
                $('#editIpMonitorTbody', this.el).empty();

                for (var i = 0; i < thisView.ipMonitorCollection.length; i++) {
                    thisView.ipMonitorCollection.at(i).set({
                        'ipIndex': i
                    });
                    var sensorIpMonitorItem = new SensorIpMonitorItem({
                        model: this.ipMonitorCollection.at(i)
                    });
                    this.monIpArr.push(sensorIpMonitorItem);
                    $('#editIpMonitorTbody', this.el).append(sensorIpMonitorItem.render().el);
                }
            }
        },
        getSensorDetailInfo: function () {
            var self = this;
            this.sensorModel.fetch({
                method: 'POST',
                url: 'api/systemSetting/selectSensorDetailInfo',
                contentType: 'application/json',
                data: JSON.stringify({'lIndex': this.lIndex}),
                async: false,
                success: function (model) {
                    self.setSensorDetailData();
                }
            });
        },
        setSensorDetailData: function () {
//            $('#lPrivateIp', this.el).text(this.sensorModel.get('lPrivateIp'));
//            var etherNet = this.sensorModel.get('ethoNetList');
//            var etherNetNameTr = "<tr>";
//            var etherNetIpTr = "<tr>";
//            for(var i = 0 ; i < etherNet.length ; i++) {
//                etherNetNameTr += "<th>" + etherNet[i].ethoNet + "</th>";
//                etherNetIpTr += "<td>" + etherNet[i].ip + "</td>";
//            }
//            etherNetNameTr += "</tr>";
//            etherNetIpTr += "</tr>";
//            $("#etherNet", this.el).append(etherNetNameTr);
//            $("#etherNet", this.el).append(etherNetIpTr);
            
            $("#preProcessCount", this.el).text(this.sensorModel.get('nHyperScanHitCount'));
            if (this.sensorModel.get('sUseBlackList') == 1) { 	//true
                $("input[name='sUseBlackList']", this.el).attr("checked", true);
                this.sUseBlackListChecked = "checked";
            } else {
                $("input[name='sUseBlackList']", this.el).attr("checked", false);
                this.sUseBlackListChecked = "";
            }
            this.sensorModel.set({
                sUseBlackListChecked: this.sUseBlackListChecked
            });
        },
        makeSensorParams: function () {
            if ($("#sUseBlackList", this.el).prop("checked")) {
                this.sUseBlackList = 1;		// 블랙리스트
            } else {
                this.sUseBlackList = 0;		// 화이트리스트
            }
            var setParams = {
                nHyperScanHitCount: $("#preProcessCount").val(),
                sUseBlackList: this.sUseBlackList,
                ipMonitorList: this.ipMonitorCollection.toJSON(),
                deleteIpMonitorList: this.deleteIpMonitorCollection.toJSON()
            };
            
            return setParams;
        },
        updateSensorInfo: function (action) {
            var thisView = this;
            var params = this.makeSensorParams();
            
            this.model.fetch({
                method: 'POST',
                url: 'api/systemSetting/updateSensorDetailInfo',
                contentType: 'application/json',
                data: JSON.stringify(params),
                dataType: 'text',
                async: false,
                success: function (model) {
                    thisView.deleteIpMonitorCollection = new SensorCollection();
                }
            });
        },
        numberInput: function (e) {
            return Backbone.Utils.validation.keyDownNumber(e);
        },
        numberKeyUp: function (e) {
            Backbone.Utils.validation.keyUpNumber(e);
        },
        validEmailServerIp: function() {
        	if($('#strEmailServerInput', this.el).val() == '' && $('#strEmailPortInput', this.el).val() == ''){
        		this.emailServerValidate = true;
        		this.emailPortValidate = true;
        		Backbone.Utils.Tip.validationTooltip($('#strEmailPortInput', this.el), true);
        		Backbone.Utils.Tip.validationTooltip($('#strEmailServerInput', this.el), true);
        		return true;
        	}
        	else {
        		// 센서에서 ipv6 지원하지 않아 ipv4 만 적용
//            this.emailServerValidate = Backbone.Utils.validation.validateIpDualCheck($('#strEmailServerInput', this.el).val());
        		this.emailServerValidate = Backbone.Utils.validation.validateIp($('#strEmailServerInput', this.el).val());
        		if (this.emailServerValidate != true) {
        			alertMessage.infoMessage(this.emailServerValidate, 'warn', '', 'small');
        			this.emailServerValidate = Backbone.Utils.Tip.validationTooltip($('#strEmailServerInput', this.el), this.emailServerValidate);
        			return false;
        		} else {
        			Backbone.Utils.Tip.validationTooltip($('#strEmailServerInput', this.el), this.emailServerValidate);
        		}
        	}
        },
        validEmailPort: function() {
        	if($('#strEmailServerInput', this.el).val() == '' && $('#strEmailPortInput', this.el).val() == ''){
        		this.emailServerValidate = true;
        		this.emailPortValidate = true;
        		Backbone.Utils.Tip.validationTooltip($('#strEmailPortInput', this.el), true);
        		Backbone.Utils.Tip.validationTooltip($('#strEmailServerInput', this.el), true);
        		return true;
        	}
        	else {
        		this.emailPortValidate = Backbone.Utils.validation.validatePort($('#strEmailPortInput', this.el).val());
        		if (this.emailPortValidate != true) {
        			alertMessage.infoMessage(this.emailPortValidate, 'warn', '', 'small');
        			this.emailPortValidate = Backbone.Utils.Tip.validationTooltip($('#strEmailPortInput', this.el), this.emailPortValidate);
        			return false;
        		} else {
        			Backbone.Utils.Tip.validationTooltip($('#strEmailPortInput', this.el), this.emailPortValidate);
        		}
        	}
        	
        },
        snmpEncyption : function (salt, password) {
        	var result = '';
        	console.log(salt,password);
        	if(salt == '' &&  password == ''){
        		return result;
        	}
        	else {
        		var hex = salt.hexEncode();
        		result = aesUtil.encrypt(hex.toUpperCase(), password);
        	}
        	console.log(salt, result);
            return result;
        }, 
    });
});