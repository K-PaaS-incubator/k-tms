/** 
 * sensor information
 */
define(function(require) {
	
	"use strict";

	// require library
	var $ 			= require('jquery'),
		Backbone 	= require('backbone');
	
	return Backbone.Model.extend({
		defaults: {
			lIndex : '',
			sType: '',
			strName: '',
			strDescription: '',
			lPrivateIp: '',
			lPublicIp: '',
			sPort: '',
			sMode: '',
			lvsensorIndex: '',
			sZip1: '',
			lResponse:'',
			lResponseValue: '131073',
			lThresholdTime: '',
			lThresholdTimeValue: '0',
			lThresholdNum: '',
			lThresholdNumValue: '0',
			lUsed: '0',
			lUsedValue: '0',
			changing: false
		}
	});
});