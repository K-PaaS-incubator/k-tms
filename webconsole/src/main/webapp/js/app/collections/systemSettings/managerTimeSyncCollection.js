define(function(require){
	var $ = require('jquery'),
		Backbone = require('backbone');
	
	var TimeSyncModel = require('models/systemSettings/managerTimeSyncModel');
	
	return Backbone.Collection.extend({
		
		model: TimeSyncModel
	});

});