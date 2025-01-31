/** 
 * 가상센서 > session Monitoring 
 */
define(function(require) {
	
	"use strict";

	// require library
	var $ 			= require('jquery'),
		Backbone 	= require('backbone');
	
	return Backbone.Model.extend({
		defaults: {
			lIndex : '',
			nPort: '',
			strName: '',
			nFilterOption: '',
			lTotalPacket: '',
			nKeepTime: '',
			nFunc: '',
			nRenewOption: '',
			nPeriod: '',
			lsensorIndex: '',
			lvsensorIndex: '',
			nCheck: '',
			changing: false
		}
	});
});