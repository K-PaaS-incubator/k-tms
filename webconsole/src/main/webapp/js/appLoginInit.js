define(function (require) {
    "use strict";

    // require library
    var $ = require('jquery'),
            _ = require('underscore'),
            Backbone = require('backbone'),
            Calendar = require('utils/Calendar'),
            Loading = require('utils/Loading'),
            ModalView = require('views/common/modal'),
            AlertView = require('views/common/alert'),
            ChartView = require('views/common/chart'),
            Utils = require('utils/utils'),
            FormSerialize = require('utils/FormUtil'),
            TemplateHelper = require('utils/templateHelper'),
            TargetView = require('views/common/target'),
            sessionManager = require('utils/sessionManager'),
            PeriodView = require('views/common/period');

    var application = {
        initialize: function () {
//            var generateCsrfToken = function () {
//                function generateRandomString(length) {
//                    var text = "";
//                    var possible = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
//                    for (var i = 0; i < length; i++) {
//                        text += possible.charAt(Math.floor(Math.random() * possible.length));
//                    }
//                    return text;
//                }
//                ;
//                return btoa(generateRandomString(32));
//            }
//            var setCookie = function (cname, cvalue) { document.cookie = cname + "=" + cvalue + ";path=/"; }
            var _sync = Backbone.sync;
            Backbone.sync = function (method, model, options) {
//                options.beforeSend = function (xhr) {
//                    var csrfToken = generateCsrfToken();
//                    setCookie('CSRF_TOKEN', encodeURIComponent(csrfToken));
//                    xhr.setRequestHeader("_csrf", csrfToken);
//                };
                options.error = function (model, response, options) {
                    if (options === 'Unauthorized') {
                        eval(model.responseText);
                    }
                };

                _sync(method, model, options);
            };

            /*
             * var _constructor = Backbone.View.prototype.constructor;
             * Backbone.View.prototype.constructor = function(options){
             * alert('init'); _constructor(options); }
             */

            Backbone.View.prototype.close = function () {
                if (this.beforeClose) {
                    this.beforeClose();
                }
                this.remove();
                this.unbind();
                if (this.onClose) {
                    this.onClose();
                }
                // TODO: model과 collection 객체 제거
            };

            Backbone.ajax({
                method: 'POST',
                contentType: 'application/json',
                url: 'api/common/getLocale',
                async: true,
                cache: true,
                success: function (data) {
                	console.log('locale', data);
                }
            });
            Backbone.Calendar = Calendar;
            Backbone.Loading = Loading;
            Backbone.ModalView = ModalView;
            Backbone.AlertView = AlertView;
            Backbone.ChartView = ChartView;
            Backbone.Utils = Utils;
            Backbone.FormSerialize = FormSerialize;
            Backbone.TargetView = TargetView;
            Backbone.PeriodView = PeriodView;
            Backbone.TemplateHelper = TemplateHelper;
        },
    };

    return application;
});
