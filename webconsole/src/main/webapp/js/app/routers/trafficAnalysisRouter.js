define(function(require) {

	"use strict";

	// require library
	var $ = require('jquery'),
		Backbone = require('backbone'),
		thisMenuKey = 'trafficAnalysis';

	var frameView;

	return Backbone.Router.extend({
		routes: {
			"trafficAnalysis_protocol" 			: "protocol",
			"trafficAnalysis_services" 			: "services",
			"trafficAnalysis_ip" 				: "ip",
			"trafficAnalysis_maliciousTraffic" 	: "maliciousTraffic"
		},
		initialize: function(){
			this.tmsEvents = _.extend({}, Backbone.Events);
		},
		setOptions: function(options) {
			frameView = options.frameView;
		},
		protocol: function() {
			frameView.closeCurrentContainer();
			frameView.selectMenuItem(thisMenuKey, 'protocol', false);
			var self = this;
			require(['views/trafficAnalysis/protocol/protocol'], function(Protocol) {
				var protocol = new Protocol({evt: self.tmsEvents});
				$('#content', frameView.el).append(protocol.el);
				frameView.setCurrentContainer(protocol);
				Backbone.Loading.setLoading($('body'));
				setTimeout(function () {
					protocol.render();
					Backbone.Loading.removeLoading($('body'));
				});
			});
		},
		services : function() {
			frameView.closeCurrentContainer();
			frameView.selectMenuItem(thisMenuKey, 'services', false);
			var self = this;
			require(['views/trafficAnalysis/service/service'], function(Service) {
				var service = new Service({evt: self.tmsEvents});
				$('#content', frameView.el).append(service.el);
				frameView.setCurrentContainer(service);
				Backbone.Loading.setLoading($('body'));
				setTimeout(function () {
					service.render();
					Backbone.Loading.removeLoading($('body'));
				});
			});
		},
		ip: function() {
			frameView.closeCurrentContainer();
			frameView.selectMenuItem(thisMenuKey, 'ip', false);
			var self = this;
			require(['views/trafficAnalysis/ip/ip'], function(Ip) {
				var ip = new Ip({evt: self.tmsEvents});
				$('#content', frameView.el).append(ip.el);
				frameView.setCurrentContainer(ip);
				Backbone.Loading.setLoading($('body'));
				setTimeout(function () {
					ip.render();
					Backbone.Loading.removeLoading($('body'));
				});
			});
		},
		maliciousTraffic: function() {
			frameView.closeCurrentContainer();
			frameView.selectMenuItem(thisMenuKey, 'maliciousTraffic', false);
			var self = this;
			require(['views/trafficAnalysis/maliciousTraffic/maliciousTraffic'], function(MaliciousTraffic) {
				var maliciousTraffic = new MaliciousTraffic({evt: self.tmsEvents});
				$("#content", frameView.el).append(maliciousTraffic.el);
				frameView.setCurrentContainer(maliciousTraffic);
				Backbone.Loading.setLoading($('body'));
				setTimeout(function () {
					maliciousTraffic.render();
					Backbone.Loading.removeLoading($('body'));
				});
			});
		}
	});
});