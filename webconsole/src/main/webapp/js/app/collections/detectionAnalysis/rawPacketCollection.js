/** 
 * @author 
 * @since 
 * @description 
 */
define(function(require){
	
	"use strict";
	
	// require library
	var $ = require('jquery'),
		Backbone = require('backbone');
	
	var RawPacketModel = require('models/detectionAnalysis/rawPacketModel');
	
	return Backbone.Collection.extend({
		
		model : RawPacketModel
	});
	
});