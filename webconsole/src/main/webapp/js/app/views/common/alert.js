/**
 * Alert View
 */
define(function(require) {

	"use strict";

	// require library
	var $ = require('jquery'),
		_ = require('underscore'),
		Backbone = require('backbone');

	var AlertView = Backbone.View.extend({
		className: 'header-alert',
		alerts: ['success', 'error', 'warning', 'info'],
		template: _.template('<span class="alert-wrap"><%= message %></span><button type="button" data-dismiss="alert" class="close alert-close">X</button>'),

		/**
		 * @param {String}
		 *            [options.alert] alert. Default: none
		 * @param {String}
		 *            [options.message] message. Default: none
		 */
		initialize: function(options) {
			var message = options.message || '';
			var alert = options.hasOwnProperty('alert') ? options.alert : 'info';

			if (_.indexOf(this.alerts, alert) === -1) {
				throw new Error('Invalid alert: [' + alert + '] Must be one of: ' + this.alerts.join(', '));
			}

			this.alert = alert;
			this.message = message;
		},
		events: {
			"click .alert-close": "onClose"
		},
		render: function() {
			var output = this.template({
				message: this.message
			});
			this.$el.html(output);
			this.$el.addClass(this.alert);
			this.$el.css('display', 'block');
			return this;
		},
		onClose: function() {
			this.unbind();
			this.remove();
		}
	});

	AlertView.msg = function($el, options) {
		var alert = new AlertView(options).render();
		$el.html(alert.el);
		return alert;
	};

	return AlertView;
});
