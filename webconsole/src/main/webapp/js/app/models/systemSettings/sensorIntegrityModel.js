/** 
 * sensor information
 */
define(function(require) {
	
	"use strict";

	// require library
	var $ = require('jquery'),
		Backbone = require('backbone');
	
	return Backbone.Model.extend({
		defaults: {
			lIndex : '',
			nStartIntegrity: '',
			nAutoIntegrity: '',
			nAutoIntegrityMin: '',
			changing: false
		}
	});
});