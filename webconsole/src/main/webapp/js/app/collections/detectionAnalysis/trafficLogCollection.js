define(function(require) {
	//	"use strict";

	var $ = require('jquery'),
		_ = require('underscore'),
		Backbone = require('backbone'),
		TrafficLogModel = require('models/detectionAnalysis/trafficLogModel');

	return Backbone.Collection.extend({
		limit: 100,
		model: TrafficLogModel,
		refresh: function() {
			if (this.length > this.limit) {
				var loopSize = this.length - this.limit;
				while (loopSize > 0) {
					var removeModel = this.pop();
					removeModel.trigger('destroy');
					removeModel.clear();
					removeModel.unbind();
					removeModel = null;
					loopSize--;
				}
			}
		}
	});
});
