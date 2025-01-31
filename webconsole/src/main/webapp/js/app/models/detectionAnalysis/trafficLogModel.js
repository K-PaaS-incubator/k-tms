define(function(require) {
	"use strict";

	var $ = require('jquery'),
		_ = require('underscore'),
		Backbone = require('backbone');

	return Backbone.Model.extend({
		defaults: {
			serviceName: '',
			serverPort: '',
			serverIp: '',
			clientIp: '',
			totalTraffic: '',
			serverBytes: '',
			clientBytes: '',
			tmStart: '',
			tmEnd: '',
			vSensorIndex: '',
			vSensorName: '',
			srcNetIndex: '',
			srcNetworkName: '',
			dstNetIndex: '',
			dstNetworkName: '',
			sessionInput: '',
			changing: false
		},
		setView: function(view) {
			this.view = view;
		},

		validate: function(attrs, options) {
			if (options.attribute == 'serverIp') {
				this.set({
					serverIp : attrs.serverIp
				});
				var result = Backbone.Utils.validation.validateIsNull(attrs.serverIp);
				
				if (result == true) {
					return Backbone.Utils.validation.validateIpDualCheck(attrs.serverIp);
				} else {
					return Backbone.Utils.Tip.validationTooltip(attrs.serverIp, true);
				}
			}
			
			if (options.attribute == 'clientIp') {
				this.set({
					victimIp : attrs.clientIp
				});
				var result = Backbone.Utils.validation.validateIsNull(attrs.clientIp);
				
				if (result == true) {
					return Backbone.Utils.validation.validateIpDualCheck(attrs.clientIp);
				} else {
					return Backbone.Utils.Tip.validationTooltip(attrs.clientIp, true);
				}
			}
			
			if (options.attribute == 'sessionInput') {
				this.set({
					sessionInput : attrs.sessionInput
				});
				var result = Backbone.Utils.validation.validateIsNull(attrs.sessionInput);
				
				if (result == true) {
					return Backbone.Utils.validation.validatePort(attrs.sessionInput);
				} else {
					return Backbone.Utils.Tip.validationTooltip(attrs.sessionInput, true);
				}
			}
		}
	});
});
