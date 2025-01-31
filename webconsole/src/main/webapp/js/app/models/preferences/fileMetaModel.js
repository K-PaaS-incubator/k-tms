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
			nSrcPort: '',
			nSrcPortColor: '',
			nSrcPortEnabled: '',
			nDstPort: '',
			nDstPortColor: '',
			nDstPortEnabled: '',
			strFileName: '',
			nFileNameColor: '',
			nFileNameEnabled: '',
			strSrcIp: '',
			nSrcIpColor: '',
			nSrcIpEnabled: '',
			strDstIp: '',
			nDstIpColor: '',
			nDstIpEnabled: '',
			lUserIndex: ''
		}
	});
});