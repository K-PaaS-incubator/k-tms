define(function (require) {

    "use strict";

    // require library
    var $ = require('jquery'),
            Backbone = require('backbone'),
            spin = require('spin');

    // require i18n
    var locale = require('i18n!nls/str');

    var SignInModel = Backbone.Model.extend({
        defaults: {
            "username": "",
            "password": "",
            "successLogin": "",
            "isEpki": "0"
        },
        url: "api/login",
        validate: function (attrs, options) {
            if (!attrs.username) {
                return locale.requireUsername;
            }
            if (!attrs.password) {
                return locale.requirePassword;
            }
        },
        obtainCertification: function (options) {
            var self = this;
            
//            var csrfToken = $('#_csrf', self.el).val();
//            
//            this.set({
//            	_csrf : csrfToken
//            });
            
            this.fetch({
                method: "POST",
                contentType: 'application/json',
                data: JSON.stringify(this.toJSON()),
                async: false,
//                headers: {
//                	"_csrf": csrfToken,
//                },
                beforeSend: function () {
                    Backbone.Loading.setLoading($('.login-form', self.el));
                },
                success: options.success,
                complete: function () {
                    Backbone.Loading.removeLoading($('.login-form', self.el));
                }
            });
        }
    });

    return SignInModel;

});