define(function(require) {
	
	"use strict";
	
	var $ = require('jquery'),
		Backbone = require('backbone');
	
	return Backbone.Model.extend({

		defaults : {
			'nUseTimeSync' : '',
			'nUseTimeSyncChecked':'',
			'nTimeSyncPeriod': '',
			'strTimeServerName' : '',
			'strTimeServerIndex':'',
			'changeInfo': false,
                        'managerTimeSyncRtn':''
		}
	});
});