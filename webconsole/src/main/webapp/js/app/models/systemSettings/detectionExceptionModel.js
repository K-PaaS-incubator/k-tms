/** 
 * 가상센서 > 탐지예외 
 */
define(function(require) {
	
	"use strict";

	// require library
	var $ 			= require('jquery'),
		Backbone 	= require('backbone');
	
	return Backbone.Model.extend({
		defaults: {
			lIndex: '',
			lVioCode: '',
			nClassType: '',
			lvsensorIndex: '',
			strDescriptionValue: '',
			strSrcIpFrom: '',
			strSrcIpTo: '',
			nSport: '',
			lsrcNetworkIndex: '',
			strDstIpFrom: '',
			strDstIpTo: '',
			nDport: '',
			ldstNetworkIndex: '',
			nDetect: '',
			nDetectValue: '0',
			changing: false,
			completed: false,
			strSrcIpFromValue: '',
			strSrcIpToValue: '',
			nSportValue: '',
			networkSrcSelectValue: '0',
			strDstIpFromValue: '',
			strDstIpToValue: '',
			nDportValue: '',
			networkDstSelectValue: '0',
			nProtocolSelectValue: '0'
		},
		toggle: function() {
			this.set({
				completed: !this.get('completed')
			});
		}
	});
});