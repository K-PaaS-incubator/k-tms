/**
 * @description 메인 > 상단 프레임 > 계정 정보 팝업
 */
define(function (require) {

    "use strict";

    var $ = require('jquery'),
            Backbone = require('backbone'),
            locale = require('i18n!nls/str'),
            alertMessage = require('utils/AlertMessage'),
            errorLocale = require('i18n!nls/error'),
            aesUtil = require('utils/security/AesUtil'),
            UserModel = require('models/preferences/accountModel');

    // require template
    var Tpl = require('text!tpl/popup/userInfoPopup.html');

    return Backbone.View.extend({
        className: 'tab-pane padding-r15 padding-l15',
        template: _.template(Tpl),
        initialize: function (options) {
            this.model = new UserModel();
            this.lUserIndex = options.lUserIndex;
            this.strId = options.UserName;
            this.role = options.role;

        },
        events: {
            "click #editBtn": "editUserInfo"
        },
        render: function () {
            this.$el.html(this.template({
                locale: locale
            }));
            this.getData();

            return this;
        },
        getData: function () {
            var url = this.role == 1 ? 'api/selectUserAccountDetails' : 'api/preferences/selectAccountDetails';
            var self = this;
            self.model.fetch({
                method: 'POST',
                url: url,
                contentType: 'application/json',
                data: JSON.stringify({userIndex: self.lUserIndex}),
                async: false,
                success: function (model) {
                    self.setData(model);
                }
            });
        },
        setData: function (model) {
            $("#userId", this.el).val(model.get("id"));
            $("#userName", this.el).val(model.get("name"));
            $("#company", this.el).val(model.get("company"));
            $("#description", this.el).val(model.get("description"));
            $("#telephone", this.el).val(model.get("telephone"));
            $("#mobile", this.el).val(model.get("mobile"));
            $("#email", this.el).val(model.get("email"));
        },
        editUserInfo: function () {
            var self = this;
            var isValid;
            isValid = this.isValid();
            var params = this.makeParams();
            if (isValid == true) {
                var url = self.role == 1 ? 'api/updateUserAccountDefault' : 'api/preferences/updateAccountDefault';
                var ModalContent = Backbone.View.extend({
                    render: function () {
                        this.$el.html('<p class="modal-body-white-padding">' + locale.msg.isSaved + '</p>');
                        return this;
                    }
                });
                Backbone.ModalView.msgWithOkCancelBtn({
                    size: 'small',
                    type: 'info',
                    title: 'Message',
                    body: new ModalContent(),
                    okButtonCallback: function (e) {
                        self.model.fetch({
                            method: 'POST',
                            url: url,
                            contentType: 'application/json',
                            // dataType: 'text',
                            data: JSON.stringify(params),
                            success: function (model) {
                                var message = locale.msg.saveMsg
                                if (model.get('accountUpdateRtn') == -99) {
                                    message = locale.msg.saveFail
                                }
                                alertMessage.infoMessage(message, 'info', '', 'small');
                                self.render();
                            }
                        });

                    }
                });
            }
        },
        makeParams: function () {
            var hex = this.$("#userId").val().hexEncode();
            var password = "";
            if (this.$('#password').val() != "") {
                password = aesUtil.encrypt(hex.toUpperCase(), this.$('#password').val());
            }
            var setParams = {
                id: this.$("#userId").val(),
                password: password,
                name: this.$("#userName").val(),
                company: this.$("#company").val(),
                description: this.$("#description").val(),
                telephone: this.$("#telephone").val(),
                mobile: this.$("#mobile").val(),
                email: this.$("#email").val(),
                userIndex: this.lUserIndex
            };

            return setParams;
        },
        isValid: function () {
            this.valid = true;

            var $userId = this.$('#userId');
            var $password = this.$("#password");
            var $comparePassword = this.$("#comparePassword");
            var $telephone = this.$("#telephone");
            var $mobile = this.$("#mobile");
            var $eMail = this.$("#email");

            //비밀번호 체크
            if ($password.val() != '' || $comparePassword.val() != '') {
                this.resultPasswordVal = Backbone.Utils.validation.validatePassword($userId.val(), $password.val(), $comparePassword.val());
                if (this.valid && this.resultPasswordVal != true) {
                    if (this.resultPasswordVal == errorLocale.validation.password.notEqualRetype) {
                        this.validationTooltip2($comparePassword, null);
                        this.$("#comparePasswordScript").text("");
                        this.$("#comparePasswordScript").append("<font color='grey'>" + this.resultPasswordVal + "</font>");
                    } else {
                        this.validationTooltip2($password, null);
                        this.$("#passwordScript").text("");
                        this.$("#passwordScript").append("<font color='grey'>" + this.resultPasswordVal + "</font>");
                    }
                    this.valid = false;
                }
            }
            if (this.resultPasswordVal == true) {
                this.validationTooltip2($password, true);
                this.validationTooltip2($comparePassword, true);
                this.$("#passwordScript").text("");
                this.$("#comparePasswordScript").text("");
            }

            //전화번호 체크			
            var telephone = $telephone.val();
            if (telephone != "") {
                this.resultTelephone = Backbone.Utils.validation.validateTelephone(telephone);
                if (this.valid && this.resultTelephone != true) {
                    this.validationTooltip2($telephone, null);
                    this.$("#telephoneScript").text("");
                    this.$("#telephoneScript").append("<font color='grey'>" + this.resultTelephone + "</font>");
                    this.valid = false;
                }
                if (this.resultTelephone == true) {
                    this.validationTooltip2($telephone, true);
                    this.$("#telephoneScript").text("");
                }
            }

            //휴대폰번호 체크 
            var mobile = $mobile.val();
            if (mobile != "") {
                this.resultMobile = Backbone.Utils.validation.validateMobile(mobile);
                if (this.valid && this.resultMobile != true) {
                    this.validationTooltip2($mobile, null);
                    this.$("#mobileScript").text("");
                    this.$("#mobileScript").append("<font color='grey'>" + this.resultMobile + "</font>");
                    this.valid = false;
                }
                if (this.resultMobile == true) {
                    this.validationTooltip2($mobile, true);
                    this.$("#mobileScript").text("");
                }
            }

            //이메일 체크			
            var email = $eMail.val();
            if (email != "") {
                this.resultEmail = Backbone.Utils.validation.validateEmail(email);
                if (this.valid && this.resultEmail != true) {
                    this.validationTooltip2($eMail, null);
                    this.$("#emailScript").text("");
                    this.$("#emailScript").append("<font color='grey'>" + this.resultEmail + "</font>");
                    this.valid = false;
                }
                if (this.resultEmail == true) {
                    this.validationTooltip2($eMail, true);
                    this.$("#emailScript").text("");
                }
            }

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
        maxLengthCheck: function(obj){
            if (obj.value.length > obj.maxLength){
                obj.value = obj.value.slice(0, obj.maxLength);
            }
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
                userIndex: this.lUserIndex,
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
        validationTooltip2: function (attr, msg) {
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