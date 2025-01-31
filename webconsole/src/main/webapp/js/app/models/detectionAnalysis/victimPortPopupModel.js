define(function(require) {
	"use strict";

	// require library
	var $ = require('jquery'),
		Backbone = require('backbone');

	return Backbone.Model.extend({
		defaults: {
			rNum: 0,
			nProtocol: 0,
			nDestPort: 0,
			sLcodeCount: 0,
			srcIpCount: 0,
			destIpCount: 0,
			nSum: 0,
			totalRowSize: 0,
			totalNSum: 0,
			bps: 0
		}
	});
});