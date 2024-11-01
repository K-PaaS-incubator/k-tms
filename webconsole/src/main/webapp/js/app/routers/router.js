define(function (require) {

    "use strict";

    // require library
    var $ = require('jquery'),
            _ = require('underscore'),
            Backbone = require('backbone'),
            FrameView = require('views/frame'),
            frameView = new FrameView({el: $('body')}).render(),
            MainRouter = require('routers/mainRouter'),
            MonitorRouter = require('routers/monitorRouter'),
            OriginalLogRouter = require('routers/originalLogRouter'),
            TrafficAnalysisRouter = require('routers/trafficAnalysisRouter'),
            SecurityPolicyRouter = require('routers/securityPolicyRouter'),
            SystemStatusRouter = require('routers/systemStatusRouter'),
            SystemSettingsRouter = require('routers/systemSettingsRouter');

    frameView.renderMenu();
    frameView.applyLibrary();
    frameView.renderCurrentSystemTime();

    return {
        initialize: function () {

            var mainRouter = new MainRouter();
            mainRouter.setOptions({
                frameView: frameView
            });
            var monitorRouter = new MonitorRouter();
            monitorRouter.setOptions({
                frameView: frameView
            });
            var originalLogRouter = new OriginalLogRouter();
            originalLogRouter.setOptions({
                frameView: frameView
            });
            var trafficAnalysisRouter = new TrafficAnalysisRouter();
            trafficAnalysisRouter.setOptions({
                frameView: frameView
            });
            var securityPolicyRouter = new SecurityPolicyRouter();
            securityPolicyRouter.setOptions({
                frameView: frameView
            });
            var systemStatusRouter = new SystemStatusRouter();
            systemStatusRouter.setOptions({
                frameView: frameView
            });
            var systemSettingsRouter = new SystemSettingsRouter();
            systemSettingsRouter.setOptions({
                frameView: frameView
            });
        },
        initHistoryApi: function () {
            Backbone.history.start();
        }
    };
});