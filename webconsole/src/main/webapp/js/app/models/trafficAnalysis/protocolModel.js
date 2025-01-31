/**
 * protocol Model
 */
define(function(require){
	"use strict";
	
	// require library
	var $ = require('jquery'),
		Backbone = require('backbone');

	return Backbone.Model.extend({
		defaults : {
			strName : '',
			ucType : '',
			nProtocol : '',
			bps : '',
			pps : '',
			cps : '',
			ingressBps : 0,
			egressBps : 0,
			ingressPps : 0,
			egressPps : 0,
			ingressCps : 0,
			egressCps : 0,
			totalRowSize : 0,
			rNum : 0,
		}
	});
	
});