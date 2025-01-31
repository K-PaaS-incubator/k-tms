define(function (require) {

    "use strict";

    // require library
    var $ = require('jquery'),
            Backbone = require('backbone'),
            thisMenuKey = 'securityPolicy';

    var frameView;

    return Backbone.Router.extend({
        routes: {
            "securityPolicy_intrusionDetectionPolicy": "intrusionDetectionPolicy", // 침입탐지정책
            "securityPolicy_yaraRulePolicy": "yaraRulePolicy", // 악성코드탐지정책
            "securityPolicy_auditLogPolicy": "auditLogPolicy"						// 감사로그 정책
        },
        setOptions: function (options) {
            frameView = options.frameView;
        },
        intrusionDetectionPolicy: function () {
            if (WRITE_MODE == 1) {
                frameView.closeCurrentContainer();
                frameView.selectMenuItem(thisMenuKey, 'intrusionDetectionPolicy', false);
                require(['views/securityPolicy/detectionPolicy/detectionPolicy'], function (DetectionPolicy) {
                    var detectionPolicy = new DetectionPolicy();
                    $('#content', frameView.el).append(detectionPolicy.el);
                    frameView.setCurrentContainer(detectionPolicy);
                    Backbone.Loading.setLoading($('body'));
                    setTimeout(function () {
                        detectionPolicy.render();
                        Backbone.Loading.removeLoading($('body'));
                    });
                });
            } else {
                location.href = "#dashboard";
            }
        },
        yaraRulePolicy: function () {
            if (WRITE_MODE == 1) {
                frameView.closeCurrentContainer();
                frameView.selectMenuItem(thisMenuKey, 'yaraRulePolicy', false);
                require(['views/securityPolicy/yaraRulePolicy/yaraRulePolicy'], function (YaraRule) {
                    var yaraRule = new YaraRule();
                    $("#content", frameView.el).append(yaraRule.el);
                    frameView.setCurrentContainer(yaraRule);
                    Backbone.Loading.setLoading($('body'));
                    setTimeout(function () {
                        yaraRule.render();
                        Backbone.Loading.removeLoading($('body'));
                    });
                });
            } else {
                location.href = "#dashboard";
            }
        },
        auditLogPolicy: function () {
            if (WRITE_MODE == 1) {
                frameView.closeCurrentContainer();
                frameView.selectMenuItem(thisMenuKey, 'auditLogPolicy', false);
                require(['views/securityPolicy/auditLogPolicy/auditLogPolicy'], function (AuditLogPolicy) {
                    var auditLogPolicy = new AuditLogPolicy();
                    $('#content', frameView.el).append(auditLogPolicy.el);
                    frameView.setCurrentContainer(auditLogPolicy);
                    Backbone.Loading.setLoading($('body'));
                    setTimeout(function () {
                        auditLogPolicy.render();
                        Backbone.Loading.removeLoading($('body'));
                    });
                });
            } else {
                location.href = "#dashboard";
            }
        }
    });
});