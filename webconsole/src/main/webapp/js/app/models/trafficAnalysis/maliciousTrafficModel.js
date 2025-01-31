define(function(require) {
	
	var $ = require('jquery'),
		Backbone = require('backbone');
	
	return Backbone.Model.extend({
		
		defaults: {
			bps: '',
			pps: '',
			cps : '',
			ingressBps : 0,
			egressBps : 0,
			ingressPps : 0,
			egressPps : 0,
			ingressCps : 0,
			egressCps : 0,
			totalRowSize: 0
		}
	});
	
	
});