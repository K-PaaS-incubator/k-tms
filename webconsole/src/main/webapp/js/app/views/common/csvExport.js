/**
 * Export
 */
define(function (require) {
    "use strict";

    var $ = require('jquery'),
            _ = require('underscore'),
            Backbone = require('backbone'),
            bootstrap = require('bootstrap'),
            locale = require('i18n!nls/str'),
            dataExpression = require('utils/dataExpression'),
            LoginStatusModel = require('models/loginStatus');	// session 체크	

    var templateFile = null;
    var detectionExportPolicyCollection = null;

    var CSVExport = Backbone.View.extend({
        initialize: function (options) {
            this.templateFile = options.templateFile;
            this.detectionExportPolicyCollection = options.detectionExportPolicyCollection;
        },
        makeFile: function () {

            var self = this;
            var detectionList = new Array();
            var contentData = "";

            var title = [
                {
                    'lCode': 'Code', // 코드			
                    'strDescription': locale.patternInfo, // 패턴정보
                    'protocol': locale.protocol, // 프로토콜	  
                    'sSeverity': locale.severity, // 1~ 10
                    'lThresholdTime/lThresholdNum': locale.threholdCountTime, // 임계값 개수/임계 시간
                    'lResponse': locale.isLogReponse, // 0 ~
                    'prevent': locale.block, // [임의사용]off
                    'lUsed': locale.used	// 0,1
                }
            ];

            for (var key in title[0]) {
                contentData += title[0][key] + ",";
            }
            contentData = contentData.substring(0, contentData.length - 1);
            contentData += "\n";

            self.detectionExportPolicyCollection.each(function (m) {
                var protocol = "";
                if (!(m.attributes.strSigRule.substring(6, 9).match(/^([0-9]|[1-9][0-9]+)$/)) && !_.isEmpty(m.attributes.strSigRule.substring(6, 9))) {
                    protocol = m.attributes.strSigRule.substring(6, 9);
                    protocol = protocol.toUpperCase();
                }

                contentData += m.get('lCode') + ",";
                contentData += m.get('strDescription') + ",";
                contentData += protocol + ",";
                contentData += dataExpression.severityData(m.get('sSeverity')) + ",";
                contentData += m.get('lThresholdTime') + locale.unitCount + "/" + m.get('lThresholdNum') + locale.timeSec + ",";
                contentData += m.get('lResponse') + ",";
                contentData += "off" + ",";
                //contentData += m.get('lUsedValue')+",";
                contentData += dataExpression.detectionUseCheck(m.get('lUsed'));
                contentData += "\n";
            });

            var date = new Date();
            var dateInfo = String(date.getFullYear())
                    + String((date.getMonth() + 1) < 10 ? "0" + (date.getMonth() + 1) : (date.getMonth() + 1))
                    + String(date.getDate() < 10 ? "0" + date.getDate() : date.getDate());

            var fileName = encodeURI(locale.detectionPolicy + '(' + dateInfo + ')', 'UTF-8');
            var content = encodeURI(contentData, 'UTF-8');
            var div = document.createElement('div');

            $(div).append($('<form id=\"hiddenForm\" method=\"POST\" action=\"' + self.templateFile + '\" >' +
                    '<input type=\"hidden\" name=\"fileName\" id=\"fileName\" value=\"' + fileName + '\" />' +
                    '<input type=\"hidden\" name=\"content\" id=\"content\" value=\"' + content + '\" />'));
            $(div).append($('</form>'));
            $('body').append($(div).html());

            var loginModel = new LoginStatusModel();
            loginModel.fetch({
                url: "requireLogin",
                async: false,
                method: 'POST',
                contentType: 'application/json',
                success: function (m) {
                    if (m.get('loginYN')) {
                        $('body #hiddenForm').submit();
                    }
                },
            });
        },
    });

    return CSVExport;

});
