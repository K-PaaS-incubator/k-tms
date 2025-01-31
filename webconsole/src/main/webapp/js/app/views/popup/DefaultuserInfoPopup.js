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
    var Tpl = require('text!tpl/popup/DefaultuserInfoPopup.html');

    return Backbone.View.extend({
        className: 'tab-pane padding-r15 padding-l15',
        template: _.template(Tpl),
        initialize: function (options) {
            this.model = new UserModel();
            this.userId = options.userId;
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
                            dataType: 'text',
                            data: JSON.stringify(params),
                            success: function (model) {
                                alertMessage.infoMessage(locale.msg.saveMsg, 'info', '', 'small');
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
                userIndex: this.lUserIndex
            };

            return setParams;
        },
        isValid: function () {
            this.valid = true;

            var $userId = this.$('#userId');
            var $password = this.$("#password");
            var $comparePassword = this.$("#comparePassword");

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

            return this.valid;
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