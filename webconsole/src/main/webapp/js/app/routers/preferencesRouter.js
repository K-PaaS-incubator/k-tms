define(function (require) {

    "use strict";

    // require library
    var $ = require('jquery'),
            Backbone = require('backbone'),
            locale = require('i18n!nls/str'),
            thisMenuKey = 'systemSettings';

    var frameView;

    return Backbone.Router.extend({
        routes: {
            "preferences_account": "accountManagement",
            "preferences_about": "systemInformation",
        },
        setOptions: function (options) {
            frameView = options.frameView;
        },
        accountManagement: function () {
            if (WRITE_MODE == 1) {
                frameView.closeCurrentContainer();
                frameView.selectMenuItem(thisMenuKey, 'account', false);
                require(['views/preferences/accountManagement'], function (AccountManagement) {
                    var accountManagement = new AccountManagement();
                    $('#content', frameView.el).append(accountManagement.el);
                    frameView.setCurrentContainer(accountManagement);
                    Backbone.Loading.setLoading($('body'));
                    setTimeout(function () {
                        accountManagement.render();
                        Backbone.Loading.removeLoading($('body'));
                    });
                });
            } else {
                location.href = "#dashboard";
            }
        },
        systemInformation: function () {
            require(['views/preferences/systemInformation'], function (SystemInformation) {
                var systemInformation = new SystemInformation();
                Backbone.ModalView.msg({
                    size: 'medium-large',
                    title: 'TESS TAS CLOUD ' + locale.about,
                    body: systemInformation
                });
            });
        },
    });
});