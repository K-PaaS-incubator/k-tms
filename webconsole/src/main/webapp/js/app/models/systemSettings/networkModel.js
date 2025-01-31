/** 
 * 네트워크 설정 
 */
define(function(require) {
	
	"use strict";

	// require library
	var $ 			= require('jquery'),
		Backbone 	= require('backbone');

	return Backbone.Model.extend({
		
		defaults: {
			lnetworkIndex: '',
			lvsensorIndex: '',
			sType: '',
			sTypeName: '',
			strName: '',
			strDescription: '',
			lValue1: '',
			lValue2: '',
			sZip1: '',
			dbLbandWidth : '',
			dbLbandWidthType: '',
			dbLbandWidthMbps: '',
			dwToIp: '',
			dwFromIp: '',
			longToIp: '',
			longFromIp: '',
			lId: '',
			lparentgroupIndex:''
		}
	});
});