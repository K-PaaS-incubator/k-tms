define(function(require) {
	"use strict";

	// require library
	var $ = require('jquery'),
		Backbone = require('backbone'),
		AttackInfoModel = require('models/dashboard/attackInfoModel');

	return Backbone.Collection.extend({
		model : AttackInfoModel
	});

});