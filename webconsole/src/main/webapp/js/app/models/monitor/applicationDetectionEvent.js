define(function(require) {
	
	"use strict";

	var $ 			= require('jquery'),
		_ 			= require('underscore'),
		Backbone 	= require('backbone');

	return Backbone.Model.extend({
		defaults: {
			lIndex: '',
			tmLogTime: '',			// 로그 생성 시각 
			dwSourceIp: '',			// 출발지 IPV4
			strSourceIp: '',		// 출발지 IPV6
			deDestinationIp: '',	// 도착지 IPV4
			strDesctinationIp: '', 	// 도착지 IPV6 
			nProtocol: '',
			nSourcePort: '',
			nDesctinationPort: '',
			bType: '',				// 타입정보 (1:http, 2:dns, 3:TLS, 4:smtp, 5:ftp)
			bIpType: '',			// IP종류 
			wDataSize: '',
			lSrcNetIndex: '',
			lDstNetIndex: '',
			lvsensorIndex: '',
			lsensorIndex: '',
			sData: '',				// 세부정보 
			tmDbTime: '',			// DB 입력 시각
			attackerIp :'',
			victimIP: '',
			srcPort: '',
			destPort: '',
			changing: false
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
			
			if (options.attribute == 'victimIP') {
				this.set({
					victimIP: attrs.victimIP
				});
				var result = Backbone.Utils.validation.validateIsNull(attrs.victimIP);
				
				if (result == true) {
					return Backbone.Utils.validation.validateIpDualCheck(attrs.victimIP);
				} else {
					return Backbone.Utils.Tip.validationTooltip(attrs.victimIP, true);
				}
			}
			
			if (options.attribute == 'srcPort') {
				this.set({
					srcPort : attrs.srcPort
				});
				var result = Backbone.Utils.validation.validateIsNull(attrs.srcPort);
				
				if(result == true) {
					return Backbone.Utils.validation.validatePort(attrs.srcPort);
				} else {
					return Backbone.Utils.Tip.validationTooltip(attrs.srcPort, true);
				}
			}
			
			if (options.attribute == 'destPort') {
				this.set({
					destPort : attrs.destPort
				});
				var result = Backbone.Utils.validation.validateIsNull(attrs.destPort);
				
				if (result == true) {
					return Backbone.Utils.validation.validatePort(attrs.destPort);
				} else {
					return Backbone.Utils.Tip.validationTooltip(attrs.destPort, true);
				}
			}
		}
	});
});
