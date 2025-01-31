define(function(require) {

	"use strict";

	// require library
	var $ 			= require('jquery'),
		Backbone 	= require('backbone'),
//		thisMenuKey = 'intrusionDetectionAnalysis';
		thisMenuKey = 'originalLog';

	var frameView;

	return Backbone.Router.extend({
		routes: {
			"originalLog_detection"		: "detectionLog",
			"originalLog_application"		: "applicationLog",
			"originalLog_filemeta"		: "filemetaLog",
		},
		initialize: function() {
			
			// 발행구독패턴(pub/sub)으로 사용할 evt 객체
            // Backbone Events를 확장하여 사용함. (_extend({}, Backbone.Events))
			this.tmsEvents = _.extend({}, Backbone.Events);
			
		},
		setOptions: function(options) {
			frameView = options.frameView;
		},
		detectionLog: function() {
			frameView.closeCurrentContainer();
			frameView.selectMenuItem(thisMenuKey, 'detectionLog', false);
			var self = this;
			require(['views/detectionAnalysis/originalLog/originalLog'], function(OriginalLog) {
				var originalLog = new OriginalLog({
					evt: self.tmsEvents
				});
				$('#content', frameView.el).append(originalLog.el);
				frameView.setCurrentContainer(originalLog);
				Backbone.Loading.setLoading($('body'));
				setTimeout(function () {
					originalLog.render();
					Backbone.Loading.removeLoading($('body'));
				});
			});			
		},
		applicationLog: function() {
			frameView.closeCurrentContainer();
			frameView.selectMenuItem(thisMenuKey, 'applicationLog', false);
			var self = this;
			require(['views/detectionAnalysis/applicationLog/applicationLog'], function(ApplicationLog) {
				var applicationLog = new ApplicationLog({
					evt: self.tmsEvents
				});
				$('#content', frameView.el).append(applicationLog.el);
				frameView.setCurrentContainer(applicationLog);
				Backbone.Loading.setLoading($('body'));
				setTimeout(function () {
					applicationLog.render();
					Backbone.Loading.removeLoading($('body'));
				});
			});		
		},
		filemetaLog: function() {
			frameView.closeCurrentContainer();
			frameView.selectMenuItem(thisMenuKey, 'fileMetaDetection', false);
			var self = this;
			require(['views/detectionAnalysis/fileMeta/fileMeta'], function(FileMeta) {
				var fileMeta = new FileMeta({
					evt: self.tmsEvents
				});
				$('#content', frameView.el).append(fileMeta.el);
				frameView.setCurrentContainer(fileMeta);
				Backbone.Loading.setLoading($('body'));
				setTimeout(function () {
					fileMeta.render();
					Backbone.Loading.removeLoading($('body'));
				});
			});	
		},
	});
});
