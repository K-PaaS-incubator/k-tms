/**
 * 공격자 IP Collection
 */
define(function(require){
	"use strict";
	
	// require library
	var $ = require('jquery'),
		Backbone = require('backbone');
	
	var AttackIpModel = require('models/detectionAnalysis/attackIpModel');
	
	return Backbone.Collection.extend({
		model : AttackIpModel
	});
	
});