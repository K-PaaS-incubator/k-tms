/**
 * ipRetrievePopup
 * ip 조회 popup
 * 
 * @author 
 * @since 
 * @description 공통 > ip조회팝업
 */
define(function (require) {

    "use strict";

    // require library
    var $ = require('jquery'),
            _ = require('underscore'),
            Backbone = require('backbone'),
            Sockjs = require('sockjs'),
            TabView = require('views/popup/tab'),
            Util = require('utils/utils'),
            alertMessage = require('utils/AlertMessage');

    var Tpl = require('text!tpl/tools/ipRetrievePopup.html');

    // require i18n
    var locale = require('i18n!nls/str'),
            errorLocale = require('i18n!nls/error');

    return Backbone.View.extend({
        template: _.template(Tpl),
        initialize: function (options) {
            this.ip = options.ip;
        },
        events: {
            "click #whois-tab .btn": "whois",
            "click #trace-route-tab .btn": "traceRoute",
            "click #ping-tab .btn": "ping",
            "keydown #hops": "numberKeyDown",
            "keyup #hops": "numberKeyUp",
            "keydown #timeout": "numberKeyDown",
            "keyup #timeout": "numberKeyUp",
            "change [name=\"ip\"]":"validIP",
        },
        render: function () {
            this.$el.html(this.template({
                locale: locale
            }));

            if (this.ip != null) {
                // ip링크를 걸어서 조회할때 
                $('#whois-tab #ip', this.el).val(this.ip);
                $('#trace-route-tab #ip', this.el).val(this.ip);
                $('#ping-tab #ip', this.el).val(this.ip);
                this.whois();
            }
            return this;
        },
        whois: function (event) {
            var $ip = $('#whois-tab #ip', this.el);
            var ipVaild = Util.validation.validateIpDualCheck($ip.val());

            if (ipVaild != true) {
                alertMessage.infoMessage(ipVaild, "warn");
                return Util.Tip.validationTooltip($ip, ipVaild);
            } else {
                Util.Tip.validationTooltip($ip, ipVaild);
            }
            $('#whois-tab .search-msg', this.el).empty();
            var thisView = this;

            var url = '/api/socketjs/requestWhois';
            if (url.indexOf('socketjs') == -1) {
                if (window.location.protocol == 'http:') {
                    url = 'ws://' + window.location.host + url;
                } else {
                    url = 'wss://' + window.location.host + url;
                }
            }
            var transports = [];
            this.whoisWS = (url.indexOf('socketjs') != -1) ? new SockJS(url, undefined, {
                protocols_whitelist: transports
            }) : new WebSocket(url);
            this.whoisWS.onopen = this.websocketOnOpen;
            this.whoisWS.onmessage = this.whoisOnMessage;
            this.whoisWS.onerror = this.websocketOnError;
            this.whoisWS.onclose = this.websocketOnClose;
            this.whoisWS.onheartbeat = this.websocketOnHeartBeat;

            this.whoisTimer = setInterval(function () {
                if (thisView.whoisWS && thisView.whoisWS.readyState === SockJS.OPEN) {
                    thisView.whoisWS.send(JSON.stringify({
                        ip: $ip.val(),
                        whoisServer: $('#whois-tab select', thisView.el).val()
                    }));
                    clearInterval(thisView.whoisTimer);
                }
            }, 1000);
        },
        whoisOnMessage: function (event) {
            var data = JSON.parse(event.data);
            $('#whois-tab .search-msg', this.el).append(data + "<br />");
        },
        traceRoute: function (event) {
            var $ip = $('#trace-route-tab #ip', this.el);
            var ipVaild = Util.validation.validateIpDualCheck($ip.val());

            if (ipVaild != true) {
                alertMessage.infoMessage(ipVaild, "warn");
                return Util.Tip.validationTooltip($ip, ipVaild);
            } else {
                Util.Tip.validationTooltip($ip, ipVaild);
            }

//			var $hostMapping = $('#trace-route-tab #hostmap', this.el);

            var $maximumHops = $('#trace-route-tab #hops', this.el);
            if ($maximumHops.val() != "") {
                if ($maximumHops.val() < 1 || $maximumHops.val() > 100) {
                    Util.Tip.validationTooltip($maximumHops, errorLocale.validation.invalidMaximumHops);
                    alertMessage.infoMessage(errorLocale.validation.invalidMaximumHops, "warn");
                    return;
                } else {
                    Util.Tip.validationTooltip($maximumHops, true);
                }
            }

            var $timeout = $('#trace-route-tab #timeout', this.el);
            if ($timeout.val() != "") {
                if ($timeout.val() < 1000 || $timeout.val() > 30000) {
                    alertMessage.infoMessage(errorLocale.validation.invalidTraceRouteTimeout, "warn");
                    return Util.Tip.validationTooltip($timeout, errorLocale.validation.invalidTraceRouteTimeout);
                } else {
                    Util.Tip.validationTooltip($timeout, true);
                }
            }
            else {
            	alertMessage.infoMessage(errorLocale.validation.invalidTraceRouteTimeout, "warn");
                return Util.Tip.validationTooltip($timeout, errorLocale.validation.invalidTraceRouteTimeout);
            }
            

            $('#trace-route-tab .search-msg', this.el).empty();
            var thisView = this;

            var url = '/api/socketjs/requestTraceRoute';
            if (url.indexOf('socketjs') == -1) {
                if (window.location.protocol == 'http:') {
                    url = 'ws://' + window.location.host + url;
                } else {
                    url = 'wss://' + window.location.host + url;
                }
            }
            var transports = [];
            this.traceRouteWS = (url.indexOf('socketjs') != -1) ? new SockJS(url, undefined, {
                protocols_whitelist: transports
            }) : new WebSocket(url);
            this.traceRouteWS.onopen = this.websocketOnOpen;
            this.traceRouteWS.onmessage = this.traceRouteOnMessage;
            this.traceRouteWS.onerror = this.websocketOnError;
            this.traceRouteWS.onclose = this.websocketOnClose;
            this.traceRouteWS.onheartbeat = this.websocketOnHeartBeat;

            this.traceRouteTimer = setInterval(function () {
                if (thisView.traceRouteWS && thisView.traceRouteWS.readyState === SockJS.OPEN) {
                    thisView.traceRouteWS.send(JSON.stringify({
                        ip: $ip.val(),
//						hostMapping: $hostMapping.is(":checked"),
                        maximumHops: $maximumHops.val(),
                        timeout: $timeout.val() / 1000	//리눅스 초단위로 ms입력후 나눔
                    }));
                    clearInterval(thisView.traceRouteTimer);
                }
            }, 1000);
        },
        traceRouteOnMessage: function (event) {
            var data = JSON.parse(event.data);
            $('#trace-route-tab .search-msg', this.el).append(data + "<br />");
        },
        ping: function (event) {
            var $ip = $('#ping-tab #ip', this.el);
            var ipVaild = Util.validation.validateIpDualCheck($ip.val());

            if (ipVaild != true) {
                alertMessage.infoMessage(ipVaild, "warn");
                return Util.Tip.validationTooltip($ip, ipVaild);
            } else {
                Util.Tip.validationTooltip($ip, ipVaild);
            }
            $('#ping-tab .search-msg', this.el).empty();
            var thisView = this;

            var url = '/api/socketjs/requestPing';
            if (url.indexOf('socketjs') == -1) {
                if (window.location.protocol == 'http:') {
                    url = 'ws://' + window.location.host + url;
                } else {
                    url = 'wss://' + window.location.host + url;
                }
            }
            var transports = [];
            this.pingWS = (url.indexOf('socketjs') != -1) ? new SockJS(url, undefined, {
                protocols_whitelist: transports
            }) : new WebSocket(url);
            this.pingWS.onopen = this.websocketOnOpen;
            this.pingWS.onmessage = this.pingOnMessage;
            this.pingWS.onerror = this.websocketOnError;
            this.pingWS.onclose = this.websocketOnClose;
            this.pingWS.onheartbeat = this.websocketOnHeartBeat;

            this.pingTimer = setInterval(function () {
                if (thisView.pingWS && thisView.pingWS.readyState === SockJS.OPEN) {
                    thisView.pingWS.send(JSON.stringify({
                        ip: $ip.val()
                    }));
                    clearInterval(thisView.pingTimer);
                }
            }, 1000);
        },
        pingOnMessage: function (event) {
            var data = JSON.parse(event.data);
            $('#ping-tab .search-msg', this.el).append(data + "<br />");
        },
        websocketOnOpen: function (event) {
        	console.log('websocketOnOpen');
        },
        websocketOnClose: function (event) {
        	console.log('websocketOnClose');
        },
        websocketOnHeartBeat: function (event) {
        	console.log('websocketOnHeartBeat');
        },
        websocketOnError: function (event) {
        	console.log('websocketOnError');
        },
        onClose: function () {
            if (this.whoisTimer) {
                clearInterval(this.managerTimer);
            }
            if (this.traceRouteTimer) {
                clearInterval(this.traceRouteTimer);
            }
            if (this.pingTimer) {
                clearInterval(this.pingTimer);
            }
            if (this.whoisWS) {
                this.whoisWS.close();
                this.whoisWS = null;
            }
            if (this.traceRouteWS) {
                this.traceRouteWS.close();
                this.traceRouteWS = null;
            }
            if (this.pingWS) {
                this.pingWS.close();
                this.pingWS = null;
            }
        },
        numberKeyDown: function (e) {
            return Backbone.Utils.validation.keyDownNumber(e);
        },
        numberKeyUp: function (e) {
            Backbone.Utils.validation.keyUpNumber(e);
        },
        validIP : function(e){
            var self = this;
            self.validateIP = true;
            var $ip = $(e.target);
            var ipValue = $ip.val();
            var selector = "";
            if($ip.hasClass('whoisip')){
                selector = 'whoisip';
            }
            if($ip.hasClass('traceip')){
                selector = 'traceip';
            }
            if($ip.hasClass('pingip')){
                selector = 'pingip';
            }
            Backbone.Utils.Tip.validationTooltip($('.'+selector), true);
            if(ipValue == "" || ipValue == null){
                alertMessage.infoMessage(errorLocale.validation.invaildIp, 'warn', '', 'small');
                self.validateIP  = false;
                Backbone.Utils.Tip.validationTooltip($('.'+selector), errorLocale.validation.invaildIp);
                return self.validateIP
            }
            var resultIp = Backbone.Utils.validation.validateIpDualCheck(ipValue);
            if(resultIp != true){
                alertMessage.infoMessage(resultIp, 'warn', '', 'small');
                self.validateIP  = false;
                Backbone.Utils.Tip.validationTooltip($('.'+selector), false);
                return self.validateIP;
            }
        }
    });

});
