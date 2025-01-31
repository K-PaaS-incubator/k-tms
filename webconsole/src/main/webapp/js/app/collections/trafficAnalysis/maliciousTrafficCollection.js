define(function(require) {
	
	var $ = require('jquery'),
		Backbone = require('backbone');
	
	var MaliciousTrafficModel = require('models/trafficAnalysis/maliciousTrafficModel');
	
	return Backbone.Collection.extend({
		
		model : MaliciousTrafficModel
		
	});
	
});