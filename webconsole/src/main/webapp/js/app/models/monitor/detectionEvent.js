define(function(require) {
	
	"use strict";

	var $ 			= require('jquery'),
		_ 			= require('underscore'),
		Backbone 	= require('backbone');

	return Backbone.Model.extend({
		
		defaults: {
			lIndex: '',
			lCode: '',
			tmStart: '',
			tmEnd: '',
			strStartTime: '',
			dwSourceIp: '',
			deDestinationIp: '',
			nProtocol: '',
			bType: '',
			strTitle: '',
			lvsensorIndex: '',
			strSourceMac: '',
			strDestinationMac: '',
			nSourcePort: '',
			nDestinationPort: '',
			dwPacketCounter: '',
			deSrcPortCounter: '',
			dwDstPortCounter: '',
			dwSrcIpCounter: '',
			dwDstIpCounter: '',
			bSeverity: '',
			nTtl: '',
			lAlertResponse1: '',
			lResetResponse1: '',
			lEmailResponse1: '',
			lSnmpResponse1: '',
			lIwResponse1: '',
			LIwResponse2: '',
			lFwResponse1: '',
			lFwResponse2: '',
			dwEventNum: '',
			wInbound: '',
			ucCreateLogType: '',
			wVlanInfo: '',
			dwPktSize: '',
			dwMaliciousSrvFrame: '',
			dwMaliciousCliFrame: '',
			dwMaliciousSrvByte: '',
			dwMaliciousCliByte: '',
			ucIntrusionDir: '',
			ucAccessDir: '',
			lSrcNetIndex: '',
			lDstNetIndex: '',
			lSrcUserIndex: '',
			lDstUserIndex: '',
			lUrlIndex: '',
			strSrcNationIso: '',
			strDestNationIso: '',
			lSensorIndex: '',
			tmDbTime: '',
			attackerIp :'',
			victimIp: '',
			attackPort: '',
			victimPort: '',
			changing: false,
			ipType: ''
		},
		setView: function(view) {
			this.view = view;
		},

		validate: function(attrs, options) {
			if (options.attribute == 'attackerIp') {
				this.set({
					attackerIp : attrs.attackerIp
				});
				var result = Backbone.Utils.validation.validateIsNull(attrs.attackerIp);
				
				if (result == true) {
					return Backbone.Utils.validation.validateIpDualCheck(attrs.attackerIp);
				} else {
					return Backbone.Utils.Tip.validationTooltip(attrs.attackerIp, true);
				}
			}
			
			if (options.attribute == 'victimIp') {
				this.set({
					victimIp : attrs.victimIp
				});
				var result = Backbone.Utils.validation.validateIsNull(attrs.victimIp);
				
				if (result == true) {
					return Backbone.Utils.validation.validateIpDualCheck(attrs.victimIp);
				} else {
					return Backbone.Utils.Tip.validationTooltip(attrs.victimIp, true);
				}
			}
			
			if (options.attribute == 'attackPort') {
				this.set({
					attackPort: attrs.attackPort
				});
				var result = Backbone.Utils.validation.validateIsNull(attrs.attackPort);
				
				if (result == true) {
					return Backbone.Utils.validation.validatePort(attrs.attackPort);
				} else {
					return Backbone.Utils.Tip.validationTooltip(attrs.attackPort, true);
				}
			}
			
			if (options.attribute == 'victimPort') {
				this.set({
					victimPort : attrs.victimPort
				});
				var result = Backbone.Utils.validation.validateIsNull(attrs.victimPort);
				
				if (result == true) {
					return Backbone.Utils.validation.validatePort(attrs.victimPort);
				} else {
					return Backbone.Utils.Tip.validationTooltip(attrs.victimPort, true);
				}
			}
		}
		
	});
});
