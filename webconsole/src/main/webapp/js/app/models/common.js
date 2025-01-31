define(function(require) {

	"use strict";

	// require library
	var $ = require('jquery'),
		Backbone = require('backbone');

	var CommonModel = Backbone.Model.extend({
		defaults: {
		},
		obtainCertification: function(options) {
			this.fetch({
				method: "POST",
				contentType: 'application/json',
				data: JSON.stringify(this.toJSON()),
				success: options.success
			});
		}
	});

	return CommonModel;

});