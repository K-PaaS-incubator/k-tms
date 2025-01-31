define(function (require) {

    "use strict";

    // require library
    var $ = require('jquery'),
            Backbone = require('backbone'),
            thisMenuKey = 'systemStatus';

    var frameView;

    return Backbone.Router.extend({
        routes: {
            "systemStatus_auditLog": "auditLog",
            "systemStatus_auditLog/:type": "auditLog",
        },
        initialize: function () {
            this.tmsEvents = _.extend({}, Backbone.Events);
        },
        setOptions: function (options) {
            frameView = options.frameView;
        },
        auditLog: function (type) {
            if (WRITE_MODE == 1) {
                frameView.closeCurrentContainer();
                frameView.selectMenuItem(thisMenuKey, 'auditLog', false);
                var self = this;

                if (type != "") {
                    this.type = type;
                } else {
                    this.type = "";
                }

                require(['views/systemStatus/auditLog/auditLog'], function (AuditLog) {
                    var auditLog = new AuditLog({
                        evt: self.tmsEvents,
                        type: self.type
                    });
                    $('#content', frameView.el).append(auditLog.el);
                    frameView.setCurrentContainer(auditLog);
                    Backbone.Loading.setLoading($('body'));
                    setTimeout(function () {
                        auditLog.render();
                        Backbone.Loading.removeLoading($('body'));
                    });
                });
            } else {
                location.href = "#dashboard";
            }
        },
    });
});