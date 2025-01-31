/** 
 *
 */
define(function(require){
	
	"use strict";
	
	// require library
	var $ 				= require('jquery'),
		Backbone 		= require('backbone');
	
	var NetworkModel 	= require('models/systemSettings/networkModel');
	
	return Backbone.Collection.extend({
		
		model: NetworkModel
	});
	
	
//	var NetworkIpBlockModel = Backbone.Model.extend({
//		initialize: function() {
//		},
//		defaults: {
//			lvsensorIndex: '',
//			lvsensorName: '',
//			dwToIp: '',
//			dwFromIp: '',
//			lId: '',
//			ipIndex:'',
//			mask:''
//		}
//	});
	
});