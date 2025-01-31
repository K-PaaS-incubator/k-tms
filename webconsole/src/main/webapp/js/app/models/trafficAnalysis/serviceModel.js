/**
 * service model
 */
define(function(require){
	"use strict";
	
	// require library
	var $ = require('jquery'),
		Backbone = require('backbone');

	return Backbone.Model.extend({
		defaults: {
			nProtocol : 0,
			wService : 0,
			bps : 0,
			pps : 0,
			ingressBps : 0,
			egressBps : 0,
			ingressPps : 0,
			egressPps : 0,
			
			totalRowSize: 0,
			rNum : 0
		}
	});
	
});