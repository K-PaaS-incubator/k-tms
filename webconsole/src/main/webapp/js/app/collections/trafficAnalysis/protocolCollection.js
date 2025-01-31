/**
 * Protocol List Collection
 */
define(function(require){
	"use strict";
	
	// require library
	var $ = require('jquery'),
		Backbone = require('backbone');
	
	var ProtocolModel = require('models/trafficAnalysis/protocolModel');
	
	return Backbone.Collection.extend({
		model : ProtocolModel
	});
	
});