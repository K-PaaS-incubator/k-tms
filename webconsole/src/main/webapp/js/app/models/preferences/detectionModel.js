/** 
 * 계정 정보  
 */
define(function(require) {
	
	"use strict";

	// require library
	var $ = require('jquery'),
		Backbone = require('backbone');
	
	return Backbone.Model.extend({
		defaults: {
			lIndex: '',
			strFilterViewName: '',
			nTargetCategory: '',
			nTargetRefIndex: '',
			nTargetColor: '',
			nTargetEnabled: '',
			nAttackPort: '',
			nAttackPortColor: '',
			nAttackPortEnabled: '',
			nVictimPort: '',
			nVictimPortColor: '',
			nVictimPortEnabled: '',
			strDtcName: '',
			nDtcNameColor: '',
			nDtcNameEnabled: '',
			nClassType: '',
			nClassTypeColor: '',
			nClassTypeEnabled: '',
			nWinbound: '',
			nWinboundColor: '',
			nWinboundEnabled: '',
			strAttackIp: '',
			nAttackIpColor: '',
			nAttackIpEnabled: '',
			strVictimIp: '',
			nVictimIpColor: '',
			nVictimIpEnabled: '',
			nBlock: '',
			nBlockColor: '',
			nBlockEnabled: '',
			lUserIndex: ''
		}
	});
});