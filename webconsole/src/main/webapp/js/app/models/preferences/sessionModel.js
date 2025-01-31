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
			nSession: '',
			nSessionColor: '',
			nSessionEnabled: '',
			strServerIp: '',
			nServerIpColor: '',
			nServerIpEnabled: '',
			strClientIp: '',
			nClientIpColor: '',
			vClientIpEnabled: '',
			lUserIndex: ''
		}
	});
});