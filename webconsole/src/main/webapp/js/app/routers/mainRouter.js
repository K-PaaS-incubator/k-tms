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
	});
});
