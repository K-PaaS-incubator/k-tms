define(function (require) {

    "use strict";

    // require library
    var $ = require('jquery'),
            _ = require('underscore'),
            Backbone = require('backbone'),
            Loading = require('utils/Loading'),
            aesUtil = require('utils/security/AesUtil'),
            spin = require('spin'),
            alertMessage = require('utils/AlertMessage'),
            Util = require('utils/utils');

    // require model
    var LoginModel = require('models/login'),
            loginModel = new LoginModel(),
            CommonModel = require('models/common');

    // require template
    var tpl = require('text!tpl/login.html'),
            template = _.template(tpl),
            DefaultUserInfoPopupView = require('views/popup/DefaultuserInfoPopup');
    
    // require deny demplate
    var denyTpl         = require('text!tpl/deny.html'),
        denyTemplate    = _.template(denyTpl)

    // require i18n
    var locale = require('i18n!nls/str'),
            errorLocale = require('i18n!nls/error');

    return Backbone.View.extend({
        model: loginModel,
        events: {
            'click #loginBtn'		: 'login',
            'keypress #password': 'enterLogin',
            'click .alert-close': 'closeAlert',
            'click .certRegist': 'showLoginForm',
            'click .popup_close': 'closePopup',
            'click #adminClose': 'loginModalClose',
            'click #adminSave': 'modifyAdmin'
        },
        initialize: function () {
            _.bindAll(this, 'success');
            this.render();
            this.dupleMsgCheck();
            Backbone.Loading = Loading;
            this.initLoginData();
            this.loginMode = 0;
            this.sessionID;
            this.strServerCert;
        },
        render: function () {
            this.$el.empty();
            this.$el.removeClass();

            this.$el.html(template({
                locale: locale
            }));
            
            //this.loadCSRF();
            return this;
        },
        enterLogin: function () {
            if (event.keyCode === 13) {
                this.login();
            }
        },
        login: function (event) {
            Backbone.Loading.setLoading($('body'));
            var view = this;
            setTimeout(function () {
                var hex = $('#userid').val().hexEncode();
                var password = aesUtil.encrypt(hex.toUpperCase(), $('#password').val());
                loginModel.set({
                    username: $('#userid').val(),
                    password: password,
                    isEpki: 0
                }, {
                    silent: true
                });
                if (loginModel.isValid()) {
                    loginModel.obtainCertification({
                        success: view.success
                    });
                } else {
                    view.showAlert(loginModel.validationError);
                }
                Backbone.Loading.removeLoading($('body'));
            });
        },
        success: function () {
            if (loginModel.get('successLogin') == 'Y') {
                var userid = $('#userid').val();
                location.href = 'home.html#dashboard';
            } else if (loginModel.get('successLogin') == 'C') {
                //관리자 최초 로그인 아이디 비밀번호 변경 팝업
                this.chandgeFirstAdmin();
            } else {
                this.showAlert(loginModel.get('errorMessage'));
            }
        },
        showAlert: function (message) {
            $('.alert-wrap', this.el).text(message);
            $('.header-alert', this.el).slideDown();
        },
        showLoginForm: function () {
            $('.frm_login', this.el).fadeIn(500);
            //$('.login-type-popup').slideDown();
        },
        closeAlert: function () {
            $('.header-alert', this.el).slideUp();
        },
        closePopup: function () {
            $('.frm_login', this.el).hide();
        },
        dupleMsgCheck: function () {
        	var csrfToken = $('#_csrf', self.el).val();
        	
        	
            Backbone.ajax({
                method: 'POST',
                contentType: 'application/json',
                url: 'api/loginDuplication',
//                data: JSON.stringify({_csrf: csrfToken}),
//                headers: {
//                	"_csrf": csrfToken,
//                },
                async: false,
                success: function (data) {
                	console.log(data);
                }
            });
        },
        initLoginData: function () {
            var thisView = this;
            var csrfToken = $('#_csrf', self.el).val();
            Backbone.ajax({
                method: 'POST',
                contentType: 'application/json',
                url: 'api/loginInitData',
                headers: {
                	"_csrf": csrfToken,
                },
                data: JSON.stringify({_csrf: csrfToken}),
                async: false,
                success: function (data) {
                    var obj = $.parseJSON(data);
                    if (obj.status == "405") {
                        console.log("ERROR")
                        $("#content").empty();
                        $("#content").html(denyTemplate({
                            locale: locale
                        }));
                    } else {
	                    thisView.loginMode = parseInt(obj.loginMode);
	                    thisView.sessionID = obj.sessionID;
	                    thisView.strServerCert = obj.strServerCert;
	                    $("#content").html(template({
                            locale: locale
                        }));
                    }
                }
            });
        },
        chandgeFirstAdmin: function () {
//            Backbone.ModalView.msg({
//                size: 'small',
//                type: 'info',
//                title: locale.DefaultaccountInfo,
//                isFooter: false,
//                body: new DefaultUserInfoPopupView({
//                })
//            });
            $('.loginModal', this.el).css('display', 'block');
        },
        loginModalClose: function () {
            $('#popupUserId', this.el).val('');
            $('#popupPassword', this.el).val('');
            $('#popupComparePassword', this.el).val('');
            $('.loginModal', this.el).css('display', 'none');
        },
        modifyAdmin: function() {
            var thisView = this;
            var chk = this.isValid();
            if (chk == false) {
                return;
            }
            var hex = $('#popupUserId').val().hexEncode();
            var password = aesUtil.encrypt(hex.toUpperCase(), $('#popupPassword').val());
            loginModel.set({
                id: $('#popupUserId').val(),
                password: password,
            }, {
                silent: true
            });
            var common = new CommonModel();
            Backbone.ajax({
                method: 'POST',
                contentType: 'application/json',
                url: 'api/updateAdmin',
                data: JSON.stringify(loginModel),
                async: false,
                success: function (data) {
                    var obj = $.parseJSON(data);
                    if (obj.errorMessage == null || obj.errorMessage == '') {
                        thisView.loginModalClose();
                        alertMessage.infoMessage("관리자 아이디 패스워드가 변경 되었습니다.\n다시 로그인 하십시오.", 'info', '', 'small');
                    } else {
                        thisView.showAlert(data.errorMessage);
                    }
                }
            });
        },
        messageTest: function() {
            alertMessage.infoMessage("관리자 아이디 패스워드가 변경 되었습니다.\n다시 로그인 하십시오.", 'info', '', 'small');
//            var ModalContent = Backbone.View.extend({
//                render: function () {
//                    this.$el.html('<p class="modal-body-white-padding">관리자 아이디 패스워드가 변경 되었습니다.\n다시 로그인 하십시오.</p>');
//                    return this;
//                }
//            });
//            ModalView.msg({
//                type: 'info',
//                size: 'small',
//                title: 'Message',
//                body: new ModalContent()
//            });
        },
        PopupWindow: function () {
            var popupX = (window.screen.width / 2) - (200 / 2);
            // 만들 팝업창 좌우 크기의 1/2 만큼 보정값으로 빼주었음
            var popupY = (window.screen.height / 2) - (300 / 2);
            // 만들 팝업창 상하 크기의 1/2 만큼 보정값으로 빼주었음
            window.open('/js/tpl/popupWin/adminLogin.html', "tms_popup", "width=350, height=250, scrollbars=no, location=no, resizeable=no, toolbar=no, titlebar=no, menubar=no, status=no, fullscreen=no, directories=no, left=" + popupX + ", top=" + popupY + ", screenX=" + popupX + ", screenY=" + popupY);
        },
        isValid: function() {

            this.valid = true;

            var $userId = this.$('#popupUserId');
            var $password = this.$("#popupPassword");
            var $comparePassword = this.$("#popupComparePassword");

            // 입력 아이디 유효성 검사
            // 아이디 입력값 null 체크 
//            this.resultUserIdVal = Utils.validation.validateIsNull($userId.val());
            this.resultUserIdVal = Util.validation.validateIsNull($userId.val());
            if (this.valid && this.resultUserIdVal != true) {
                this.showAlert(this.resultUserIdVal);
                this.validationTooltip2($userId, null);
                this.valid = false;
            }
            // 아이디는 6자이상 15자이내의 영소문자 또는 영소문자, 숫자 조합이어야 하고 첫글자는 영문이어야 합니다.
            this.resultUserIdVal = Util.validation.idCheck($userId.val());
            if (this.valid && this.resultUserIdVal != true) {
                this.showAlert(this.resultUserIdVal);
                this.validationTooltip2($userId, null);
                this.valid = false;
            }
            // 비밀번호 입력값 null 체크 
            this.passwordChkVal = Util.validation.validateIsNull($password.val());
            // 비밀번호 입력확인값 null 체크 
            this.rePasswordChkVal = Util.validation.validateIsNull($comparePassword.val());
            
            if (this.valid && this.passwordChkVal != true) {
                this.validationTooltip2($password, null);
                this.valid = false;
            }
            if (this.valid && this.rePasswordChkVal != true) {
                this.showAlert(this.rePasswordChkVal);
                this.validationTooltip2($comparePassword, null);
                this.valid = false;
            }
            
            this.passwordVal = true;
            if (this.passwordChkVal == true || this.rePasswordChkVal == true) {
                this.passwordVal = Util.validation.validatePassword($userId.val(), $password.val(), $comparePassword.val());
            }
            
            if (this.valid && this.passwordVal != true) {
                if (this.passwordVal == errorLocale.validation.password.notEqualRetype) {
                    this.validationTooltip2($password, true);
                    this.showAlert(this.passwordVal);
                    this.validationTooltip2($comparePassword, null);
                } else {
                    this.showAlert(this.passwordVal);
                    this.validationTooltip2($password, null);
                }
                this.valid = false;
            }
            
            if (this.resultUserIdVal == true) {
                this.validationTooltip2($userId, true);
            }
            if (this.passwordChkVal == true && this.passwordVal == true) {
                this.validationTooltip2($password, true);
            }
            if (this.rePasswordChkVal == true && this.passwordVal == true) {
                this.validationTooltip2($comparePassword, true);
            }

            return this.valid;
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
        },
        loadCSRF : function(){
        	var self = this;
        	var generateCsrfToken = function () {
                function generateRandomString(length) {
                    var text = "";
                    var possible = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
                    for (var i = 0; i < length; i++) {
                        text += possible.charAt(Math.floor(Math.random() * possible.length));
                    }
                    return text;
                }
                ;
                return btoa(generateRandomString(32));
            }
            var setCookie = function (cname, cvalue) { document.cookie = cname + "=" + cvalue + ";path=/"; }
            
            var csrfToken = generateCsrfToken();
            setCookie('CSRF_TOKEN', encodeURIComponent(csrfToken));
            $('#_csrf', self.el).val(csrfToken);
        }
    });
});
