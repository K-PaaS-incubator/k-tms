define(function(require) {

	"use strict";

	// require library
	var $ = require('jquery'),
		Backbone = require('backbone');

	// require i18n
	var locale = require('i18n!nls/str');

	return Backbone.Model.extend({
		defaults: {
			lindex: '',
			lLoggingDataSize: '',
			lLimitFreeSpace: '',
			tmStart: '',
			tmEnd: '',
			lPacketCount: '',
			nBackupSelect: '',
			nOverwrite: '',
			nStopOption: '',
			strTrafficFilter: '',
			lSensorIndex: '',
			tmRegDate: '',
			dblTotalFrame: '',
			dblTotalBytes: '',
			dblPps: '',
			dblBps: '',
			dblElpasedTime: ''
		}
	});

});
