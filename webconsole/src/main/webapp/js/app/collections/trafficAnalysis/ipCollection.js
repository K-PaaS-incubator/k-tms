/**
 * Protocol List Collection
 */
define(function(require){
	"use strict";
	
	// require library
	var $ = require('jquery'),
		Backbone = require('backbone');
	
	var IpModel = require('models/trafficAnalysis/ipModel');
	
	return Backbone.Collection.extend({
		model : IpModel
	});
	
});