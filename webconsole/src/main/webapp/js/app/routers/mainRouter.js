define(function(require) {

	"use strict";

	// require library
	var $ = require('jquery'),
		Backbone = require('backbone');

	var frameView;

	return Backbone.Router.extend({
		routes: {
			""						: "dashboard",
			"dashboard"				: "dashboard",
			"trafficLogging"		: "trafficLogging",
			"trafficLogging/:type"	: "trafficLogging"
		},
		setOptions: function(options) {
			frameView = options.frameView;
		},
		dashboard: function() {
			frameView.closeCurrentContainer();
			frameView.selectMenuItem('', 'dashboard', false);
			require(['views/dashboard/dashboard'], function(Dashboard) {
				var dashboard = new Dashboard();
				$('#content', frameView.el).append(dashboard.el);
				frameView.setCurrentContainer(dashboard);
				Backbone.Loading.setLoading($('body'));
				dashboard.render();
				setTimeout(function () {
					dashboard.renderData();
					Backbone.Loading.removeLoading($('body'));
				});
			});
		},
		trafficLogging: function(type) {
			frameView.closeCurrentContainer();
			frameView.selectMenuItem('', 'trafficLogging', false);
			var self = this;
			if(type != ""){
				this.type = type;
			}else{
				this.type = "";
			}
			require(['views/tools/trafficLogging'], function(TrafficLogging) {
				var trafficLogging = new TrafficLogging({
					type: self.type
				});
				$('#content', frameView.el).append(trafficLogging.el);
				frameView.setCurrentContainer(trafficLogging);
				trafficLogging.render();
			});
		}
	});
});
