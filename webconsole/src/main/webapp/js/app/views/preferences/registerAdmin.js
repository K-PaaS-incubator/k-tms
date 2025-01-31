define(function (require) {

    "use strict";

    var $ 				= require('jquery'),
        Backbone 		= require('backbone'),
        aesUtil 		= require('utils/security/AesUtil'),
        locale 			= require('i18n!nls/str'),
        errorLocale 	= require('i18n!nls/error'),
        sessionManager = require('utils/sessionManager'),
        alertMessage 			= require('utils/AlertMessage');

    var AccountModel 	= require('models/preferences/accountModel');
    var tpl 			= require('text!tpl/preferences/editAdmin.html');

    return Backbone.View.extend({
        editable: false,
        template: _.template([
                              '<div class="icon-subtitle"><%= locale.admin %> <%= locale.basicInformation %></div>',
                              '<div class="border-dotted-all overflow-hidden padding-t5">',
                              '<table class="view-table col-xs-12">',
                              '<tr class="border-clear">',
                              '<th width="20%"><%= locale.user %> ID</th>',
                              /*'<td><strong><%= model.id %></strong></td>',*/
                              '<td><strong><span id="strId"></span></strong></td>',
                              '</tr>',
                              '<tr class="border-clear">',
                              '<th><%= locale.encryption %></th>',
                              '<td>*********</td>',
                              '</tr>',
                              '<% if (SYSTEM_TYPE != 2) { %><tr class="border-clear">',
                              '<th>'+locale.loginAuthIp+'</th>',
                              '<td><span id="loginIpList"></span></td>',
                              '</tr><% } %>',
                              '</table>',
                              '</div>',
                              '<div class="icon-subtitle"><%= locale.additionalInformation %></div>',
                              '<div class="border-dotted-all overflow-hidden padding-t5 padding-b5">',
                              '<table class="view-table col-xs-12">',
                              '<tr class="border-clear">',
                              '<th width="20%"><%= locale.name %></th>',
                              /*'<td><%= model.name %></td>',*/
                              '<td><span id="strName"></span></td>',
                              '</tr>',
                              '<tr class="border-clear">',
                              '<th><%= locale.affiliation %></th>',
                              /*'<td><%= model.company %></td>',*/
                              '<td><span id="company"></span></td>',
                              '</tr>',
                              '<tr class="border-clear">',
                              '<th><%= locale.comment %></th>',
                              /*'<td><%= model.description %></td>',*/
                              '<td><span id="description"></span></td>',
                              '</tr>',
                              '<tr class="border-clear">',
                              '<th><%= locale.telephone %></th>',
                              /*'<td><%= model.telephone %></td>',*/
                              '<td><span id="telephone"></span></td>',
                              '</tr>',
                              '<tr class="border-clear">',
                              '<th><%= locale.cellphone %></th>',
                              /*'<td><%= model.mobile %></td>',*/
                              '<td><span id="mobile"></span></td>',
                              '</tr>',
                              '<tr class="border-clear">',
                              '<th><%= locale.email %></th>',
                              /*'<td><%= model.email %></td>',*/
                              '<td><span id="email"></span></td>',
                              '</tr>',
                              '</table>',
                              '</div>'
        ].join('')),
        editTemplate: _.template(tpl),
        initialize: function(options) {
            this.userIndex 	= options.userIndex;
            this.strId 		= options.strId;
            this.model 		= new AccountModel();
            this.loginAuthIpArr = [];
        },
        events: {
            "click .loginIpInsertBtn"	: "loginIpAdd",
            "click .loginIpDeleteBtn"	: "loginIpDelete"
        },
        status: function() {
            return this.editable;
        },
        statusType: function() {
            return this.type;
        },
        render: function() {
            if (this.userIndex != undefined) {
                this.type = "update";
                this.editable = false;
                //template에 데이터 text로 넣도록 변경 
                //this.getAdminDetailData();
                this.$el.html(this.template({
                    model: this.model.toJSON(),
                    locale:locale
                }));
                this.getAdminDetailData();
                
                return this;
                
            } else {
                this.type = "insert";
                this.editable = true;
                this.disabled = "";
                this.model.set({
                    disabled: this.disabled
                });
                this.$el.html(this.editTemplate({
                    locale: locale,
                    model: this.model.toJSON()
                }));
                $('.btnContent').show();
                
                return this;
            }
        },
        renderEdit: function() {
            this.type = "update";
            this.editable = true;
            this.disabled = "disabled";
            this.model.set({
                disabled: this.disabled,
                pwdDisabled : ''
            });
//            console.log('param', model);
//        	console.log('sessionManager', sessionManager);
//
//        	console.log('model.get(\'role\')', model.get('role'));
//        	console.log('sessionManager.UserName', sessionManager.UserName);
//        	console.log('model.get(\'id\')', model.get('id'));
//
//        	//다른 관리자 계정 비변변경 차단
        	if(this.model.get('role') == 7 && this.model.get('id') != sessionManager.UserName){
//        		console.log('AAAAAAAAAAAAA');
//        		$('input[type="password"]', this.el).attr('disabled', "disabled");
        		this.model.set({
                    pwdDisabled: this.disabled
                });
        	}
            this.$el.html(this.editTemplate({
                locale: locale,
                model: this.model.toJSON()
            }));
            $('.btnContent').hide();
            this.setLoginIp();
            
            return this;
        },
        toggle: function() {
            if (this.editable) {
                this.mergeAdminAccount(this.type);
                return this.render();
            } else {
                return this.renderEdit();
            }
        },
        getInsertIndex: function() {
            return this.userIndex;
        },
        getUrlAction: function(action) {
            return action === 'update' ? 'api/preferences/updateAccountDetails' : 'api/preferences/insertAdminAccountDetails';
        },
        makeParams: function(action) {
            var hex = this.$("#userId").val().hexEncode();
            var password = "";
            if (this.$('#password').val() != "") {
                password = aesUtil.encrypt(hex.toUpperCase(), this.$('#password').val());
            }
            var setParams = {
                id: this.$("#userId").val(),
                groupType: 0,
                role: 7,
                password: password,
                name: this.$("#userName").val(),
                company: this.$("#company").val(),
                description: this.$("#description").val(),
                telephone: this.$("#telephone").val(),
                mobile: this.$("#mobile").val(),
                email: this.$("#email").val(),
                userIpList: this.loginAuthIpArr
            };
            if (action === 'update') {
                setParams.userIndex = this.userIndex;
            }
            return setParams;
        },
        mergeAdminAccount: function(action) {
            var self = this;
            var url = this.getUrlAction(action);
            var params = this.makeParams(action);
            
            this.model.fetch({
                method: 'POST',
                url: url,
                contentType: 'application/json',
                //dataType: 'text', 			// 인덱스를 못가져오므로 제거 
                data: JSON.stringify(params),
                async: false,
                success: function (model) {
                    if (action == 'insert') {
                        self.userIndex = model.get('userIndex');
                        self.type = 'update';
                    }
                }
            });
        },
        getAdminDetailData: function() {
        	var self = this;
            this.model.fetch({
                method: 'POST',
                url: 'api/preferences/selectAccountDetails',
                contentType: 'application/json',
                data: JSON.stringify({'userIndex': this.userIndex}),
                async: false,
                success: function (model) {
                	self.setAdminDetailData(model);
                }
            });
        },
        setAdminDetailData: function(model) {
        	
        	var name = model.get('name') == null ? "" : model.get('name');
            var company = model.get('company') == null ? "" : _.unescape(model.get('company'));
            var description = model.get('description') == null ? "" : _.unescape(model.get('description'));
            var telephone = model.get('telephone') == null ? "" : _.unescape(model.get('telephone'));
            var mobile = model.get('mobile') == null ? "" : _.unescape(model.get('mobile'));
            var email = model.get('email') == null ? "" : _.unescape(model.get('email'));
            
        	$('#strId', this.el).text(model.get('id'));
        	$('#strName', this.el).text(name);
        	$('#company', this.el).text(company);
        	$('#description', this.el).text(description);
        	$('#telephone', this.el).text(telephone);
        	$('#mobile', this.el).text(mobile);
        	$('#email', this.el).text(email);
        	
//        	$('#strName', this.el).text(model.get('name'));
//        	$('#company', this.el).text(_.unescape(model.get('company')));
//        	$('#description', this.el).text(_.unescape(model.get('description')));
//        	$('#telephone', this.el).text(model.get('telephone'));
//        	$('#mobile', this.el).text(model.get('mobile'));
//        	$('#email', this.el).text(model.get('email'));
            this.loginAuthIpArr = model.get("userIpList");
            this.setLoginIPLine();
        },
        isValid: function() {

            this.valid = true;

            var $userId = this.$('#userId');
            var $password = this.$("#password");
            var $comparePassword = this.$("#comparePassword");
            var $telephone = this.$('#telephone');
            var $mobile = this.$('#mobile');
            var $email = this.$("#email");
            var $userIpList = this.$(".loginIpSelect");
            var $userIp = this.$(".loginIpInput");

            // 입력 아이디 유효성 검사
            // 아이디 입력값 null 체크 
            this.resultUserIdVal = Backbone.Utils.validation.validateIsNull($userId.val());
            if (this.valid && this.resultUserIdVal != true) {
                this.$('#adminIdScriptRow').show();
                this.$("#adminIdScript").html("");
                this.$("#adminIdScript").html("<font color='red'>" + this.resultUserIdVal + "</font>");
                this.validationTooltip2($userId, null);
                this.valid = false;
            }
            // 아이디는 6자이상 15자이내의 영소문자 또는 영소문자, 숫자 조합이어야 하고 첫글자는 영문이어야 합니다.
            this.resultUserIdVal = Backbone.Utils.validation.idCheck($userId.val());
            if (this.valid && this.resultUserIdVal != true) {
                this.$('#adminIdScriptRow').show();
                this.$("#adminIdScript").html("");
                this.$("#adminIdScript").html("<font color='red'>" + this.resultUserIdVal + "</font>");
                this.validationTooltip2($userId, null);
                this.valid = false;
            }
            if (SYSTEM_TYPE == 1) {
                if (this.loginAuthIpArr.length <= 0) {
                    this.$('#userIpScriptRow').show();
                    this.$("#userIpScript").html("");
                    this.$("#userIpScript").html("<font color='red'>" + errorLocale.validation.loginIpValid + "</font>");
                    this.validationTooltip2($userIpList, null);
                    this.valid = false;
                } else {
                    this.$('#userIpScriptRow').hide();
                    this.validationTooltip2($userIpList, true);
                }
            }

            // 전화번호 입력값 null 체크
            // 필수 입력 항목이 아님 
            if ($telephone.val() != "") {
                this.resultTelephone = Backbone.Utils.validation.validateTelephone($telephone.val());
                if (this.valid && this.resultTelephone != true) {
                    this.$('#telScriptRow').show();
                    this.$("#adminTelScript").html("");
                    this.$("#adminTelScript").html("<font color='red'>" + this.resultTelephone + "</font>");
                    this.validationTooltip2($telephone, null);
                    this.valid = false;
                }
                if (this.resultTelephone == true) {
                    this.$('#telScriptRow').hide();
                    this.validationTooltip2($telephone, true);
                }
            } else {
                this.$('#telScriptRow').hide();
                this.validationTooltip2($telephone, true);
            }

            if ($mobile.val() != "") {
                this.resultMobile = Backbone.Utils.validation.validateMobile($mobile.val());
                if (this.valid && this.resultMobile != true) {
                    this.$('#mobileScriptRow').show();
                    this.$("#adminMobileScript").html("");
                    this.$("#adminMobileScript").html("<font color='red'>" + this.resultMobile + "</font>");
                    this.validationTooltip2($mobile, null);
                    this.valid = false;
                }
                if (this.resultMobile == true) {
                    this.$('#mobileScriptRow').hide();
                    this.validationTooltip2($mobile, true);
                }
            } else {
                this.$('#mobileScriptRow').hide();
                this.validationTooltip2($mobile, true);
            }

            // 이메일 입력 형식 유효성 검사
            this.resultEmailType = Backbone.Utils.validation.validateEmail($email.val());
            if ($email.val() != "" && $email.val() != null) {
                if (this.valid && this.resultEmailType != true) {
                    this.$('#emailScriptRow').show();
                    this.$("#adminEmailScript").html("");
                    this.$("#adminEmailScript").html("<font color='red'>" + this.resultEmailType + "</font>");
                    this.validationTooltip2($email, null);
                    this.valid = false;
                }
                if (this.resultEmailType == true) {
                    this.$('#emailScriptRow').hide();
                    this.validationTooltip2($email, true);
                }
            } else {
                this.$('#emailScriptRow').hide();
                this.validationTooltip2($email, true);
            }

            // 비밀번호 입력값 null 체크 
            this.passwordChkVal = Backbone.Utils.validation.validateIsNull($password.val());
            // 비밀번호 입력확인값 null 체크 
            this.rePasswordChkVal = Backbone.Utils.validation.validateIsNull($comparePassword.val());
            
            if (this.type != "update") {
                if (this.valid && this.passwordChkVal != true) {
                    this.$('#passwordScriptRow').show();
                    this.$("#adminPasswordScript").html("");
                    this.$("#adminPasswordScript").html("<font color='red'>" + this.passwordChkVal + "</font>");
                    this.validationTooltip2($password, null);
                    this.valid = false;
                }
                if (this.valid && this.rePasswordChkVal != true) {
                    this.$('#rePasswordScriptRow').show();
                    this.$("#adminRePasswordScript").html("");
                    this.$("#adminRePasswordScript").html("<font color='red'>" + this.rePasswordChkVal + "</font>");
                    this.validationTooltip2($comparePassword, null);
                    this.valid = false;
                }
            }  else {
                if (this.passwordChkVal != true && this.rePasswordChkVal != true) {
                    this.$('#passwordScriptRow').hide();
                    this.$('#rePasswordScriptRow').hide();
                    this.validationTooltip2($password, true);
                    this.validationTooltip2($comparePassword, true);
                }
            }
            
            this.passwordVal = true;
            if (this.passwordChkVal == true || this.rePasswordChkVal == true) {
                this.passwordVal = Backbone.Utils.validation.validatePassword($userId.val(), $password.val(), $comparePassword.val());
            }
            
            if (this.valid && this.passwordVal != true) {
                if (this.passwordVal == errorLocale.validation.password.notEqualRetype) {
                    this.$('#passwordScriptRow').hide();
                    this.validationTooltip2($password, true);
                    this.$('#rePasswordScriptRow').show();
                    this.$("#adminRePasswordScript").html("");
                    this.$("#adminRePasswordScript").html("<font color='red'>" + this.passwordVal + "</font>");
                    this.validationTooltip2($comparePassword, null);
                } else {
                    this.$('#passwordScriptRow').show();
                    this.$("#adminPasswordScript").html("");
                    this.$("#adminPasswordScript").html("<font color='red'>" + this.passwordVal + "</font>");
                    this.validationTooltip2($password, null);
                }
                this.valid = false;
            }
            
            if (this.resultUserIdVal == true) {
                this.validationTooltip2($userId, true);
                this.$('#adminIdScriptRow').hide();
            }
            if (this.passwordChkVal == true && this.passwordVal == true) {
                this.$('#passwordScriptRow').hide();
                this.validationTooltip2($password, true);
            }
            if (this.rePasswordChkVal == true && this.passwordVal == true) {
                this.validationTooltip2($comparePassword, true);
                this.$('#rePasswordScriptRow').hide();
            }

            var self = this;
            var setParam = {
                id: $(".adminId", self.el).val()
            };
            if (this.valid == true) {
                if (this.type == "insert") {
                    this.model.fetch({
                        method: 'POST',
                        url: 'api/preferences/isDuplicateAccountID',
                        contentType: 'application/json',
                        data: JSON.stringify(setParam),
                        async: false,
                        success: function (model) {
                            var userId = model.get('id');
                            if (userId === null || userId === "") {
                                self.resultUserIdVal = true;
                            } else {
                                if (self.strId != self.$(".adminId").val()) {
                                    //                            self.resultUserIdVal = Backbone.Utils.Tip.validationTooltip($('.adminId'), "중복된 아이디가 있습니다. 다시 입력해주세요.");
                                    self.$('#adminIdScriptRow').show();
                                    self.$("#adminIdScript").html("");
                                    self.$("#adminIdScript").html("<font color='red'>" + errorLocale.validation.isDuplicateId + errorLocale.validation.reInput + "</font>");
                                    self.validationTooltip2($userId, null);
                                    self.valid = false;
                                } else {
                                    self.resultUserIdVal = true;
                                }
                            }
                        }
                    });
                }
                this.valid = this.isDuplicatedPassword();
            }
            return this.valid;
        },
        // refs #21391 - 패스워드 규칙 기능 추가 요청
        isDuplicatedPassword: function() {
            var result = true;
            var hex = this.$("#userId").val().hexEncode();
            var password = "";
            if (this.$('#password').val() != "") {
                password = aesUtil.encrypt(hex.toUpperCase(), this.$('#password').val());
            }
            var setParam = {
                password: password,
                userIndex: this.userIndex,
                id: this.$("#userId").val()
            }

            if (this.$("#password").val() != '') {
                this.model.fetch({
                    method: 'POST',
                    url: 'api/preferences/isComparedPassword',
                    contentType: 'application/json',
                    data: JSON.stringify(setParam),
                    async: false,
                    success: function (data) {
                        if (data.get('changeYn') != '0') {
                            console.log("Password not collect!!!");
                            self.$('#passwordScriptRow').show();
                            self.$("#adminPasswordScript").html("");
                            self.$("#adminPasswordScript").html("<font color='red'>" + errorLocale.validation.password.invalidRenewalReinput + "</font>");
                            self.resultCheckPassword = false;
                            /*Backbone.Utils.Tip.validationTooltip(password, errorLocale.validation.password.invalidRenewalReinput);*/
                            alertMessage.infoMessage(errorLocale.validation.password.invalidRenewalReinput, 'info', '', 'small');
                            result = false;
                        }
                    }
                })
            }
            return result;
        },
        setLoginIPLine: function() {
            var thisView = this;
            $('#loginIpList', thisView.el).empty();
            for (var i = 0; i < thisView.loginAuthIpArr.length; i++) {
                $('#loginIpList', thisView.el).append(thisView.loginAuthIpArr[i] + "<br/>");
            }
        },
        setLoginIp: function() {
            var thisView = this;
            $('.loginIpSelect', thisView.el).empty();
            for (var i = 0; i < thisView.loginAuthIpArr.length; i++) {
                $('.loginIpSelect', thisView.el).append('<option value=' + thisView.loginAuthIpArr[i] + '>' + thisView.loginAuthIpArr[i] + '</option>');
            }
            this.loginInputValidate = Backbone.Utils.Tip.validationTooltip($('.loginIpInput', this.el), true);
        },
        loginIpAdd: function() {
            var loginAuthIp = $('.loginIpInput', this.el).val().trim();
            if (loginAuthIp == null || loginAuthIp == '') {
                alertMessage.infoMessage(errorLocale.validation.inputIpNullValid, 'info', '', 'small');
                return false;
            }
            this.loginInputValidate = Backbone.Utils.validation.validateIpDualCheck($('.loginIpInput', this.el).val());
            if (this.loginInputValidate != true) {
                Backbone.Utils.Tip.validationTooltip($('.loginIpInput', this.el), this.loginInputValidate);
                return false;
            } else {
                this.validationTooltip2($('.loginIpSelect', this.el), true);
                this.$('#userIpScriptRow').hide();
            }
            if (this.loginAuthIpArr.length >= LOGIN_IP_COUNT) {
                alertMessage.infoMessage(errorLocale.validation.authIpCount + LOGIN_IP_COUNT + errorLocale.validation.isCount, 'info', '', 'small');
                return false;
            }
            if (this.loginAuthIpArr.length > 0) {
                var index = this.loginAuthIpArr.indexOf(loginAuthIp);
                if (index >= 0) {
                	alertMessage.infoMessage(errorLocale.validation.duplicateIpMsg + errorLocale.validation.reInput, 'info', '', 'small');
                    return false;
                }
            }
            this.loginAuthIpArr.push(loginAuthIp);
            this.setLoginIp();
            $('.loginIpInput', this.el).val('');
        },
        loginIpDelete: function() {
            var selIp = String($('.loginIpSelect', this.el).val());
            var index = this.loginAuthIpArr.indexOf(selIp);
            this.loginAuthIpArr.splice(index, index + 1);
            this.setLoginIp();
        },
        validationTooltip2: function(attr, msg) {
            var selector = $(attr.selector, this.el);
            var $control = selector.closest('.form-control', this.el);
            $control.removeAttr('data-toggle');
            $control.removeClass('validation-error');

            if (msg == null) {
                $control.attr('data-toggle', 'tooltip');
                $control.addClass('validation-error');
                return false;
            }

            return true;
        }
    });
});