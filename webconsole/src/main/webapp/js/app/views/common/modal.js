/**
 * Modal View
 */
define(function(require) {

	"use strict";

	// require library
	var $ = require('jquery'),
		_ = require('underscore'),
		Backbone = require('backbone'),
		Bootstrap = require('bootstrap');

	// require template
	var tpl = require('text!tpl/common/modal.html');

	// require i18n
	var locale = require('i18n!nls/str');

	var ModalView = Backbone.View.extend({
		types: ['info', 'search', 'chart'],
		sizes: ['large', 'medium-large', 'small'],
		className: "modal backbone-modal",
		events: {
			"hidden.bs.modal": "close"
		},
		template: _.template(tpl),
		buttonTemplate: _.template('<button type="button" class="btn <%=className%>"><%=label%></button>"'),
		buttonDefaults: {
			className: "",
			label: "",
			okButtonCallback: "",
			cancelButtonCallback : "",
			close: false
		},
		defaults: {
			type: 'info',
			size: 'large',
			title: "",
			//backdrop: true,
			//모달 팝업 닫힘 현상 속성 변경
			backdrop: 'static',
			body: "",
			buttons: [{
				className: "btn-primary",
				label: locale.close,
				close: true
			}]
		},
		initialize: function(options) {
			options || (options = {});
			var type = options.type || 'info';
			var size = options.size || 'large';
                        this.isFooter = options.isFooter;
                        if(typeof this.isFooter == 'undefined') {
                            this.isFooter = true;
                        }
			if (_.indexOf(this.types, type) === -1) {
				throw new Error('Invalid type: [' + type + '] Must be one of: ' + this.types.join(', '));
			}

			if (_.indexOf(this.sizes, size) === -1) {
				throw new Error('Invalid size: [' + size + '] Must be one of: ' + this.sizes.join(', '));
			}

			_.defaults(this, this.defaults);
			_.extend(this, _.pick(options, _.keys(this.defaults)));
			_.bindAll(this, "close");
		},
		render: function() {
			var view = this;
			
			this.$el.html(this.template({
				type: this.type,
				title: this.title,
				body: this.body,
				isFooter: this.isFooter
			}));
			this.$header = this.$el.find('.modal-header');
			this.$body = this.$el.find('.modal-body');
			this.$footer = this.$el.find('.modal-footer');
			
			switch (this.size) {
				case 'large':
					$('.modal-dialog', this.el).addClass('modal-lg');
					break;
				case 'medium-large':
					$('.modal-dialog', this.el).addClass('modal-ml');
					break;
				case 'small':
					$('.modal-dialog', this.el).addClass('modal-sm');
					break;
				default:
					$('.modal-dialog', this.el).addClass('modal-lg');
					break;
			}

			//Insert the main content if it's a view
			if (this.body && this.body.$el) {
				this.$el.find('.modal-body').html(this.body.$el);
				this.body.render();
			}
                       
                        if (this.isFooter) {
                            _.each(this.buttons, function(button) {
                                    _.defaults(button, view.buttonDefaults);
                                    var $button = $(view.buttonTemplate(button));
                                    view.$footer.append($button);
                                    if (button.okButtonCallback) {
                                            if (_.isFunction(button.okButtonCallback)) $button.on("click", function(e) {
                                                    var result = button.okButtonCallback.apply(this, arguments);
                                                    if (result == false) {
                                                            e.stopImmediatePropagation();
                                                    }
                                            });
                                    }
                                    if (button.cancelButtonCallback) {
                                            if (_.isFunction(button.cancelButtonCallback)) $button.on("click", function(e) {
                                                    var result = button.cancelButtonCallback.apply(this, arguments);
                                                    if (result == false) {
                                                            e.stopImmediatePropagation();
                                                    }
                                            });
                                    }
                                    if (button.close) $button.on("click", view.close);
                            });
                        }

			this.$el.modal({
				keyboard: false,
				backdrop: this.backdrop
			});

			this.$header.find(".close").click(view.close);

			if (this.backdrop === true) {
				$('.modal-backdrop').click(view.close);
			}

			this.postRender();
			// Jquery draggable
			$('.modal-dialog').draggable({
			   handle: ".modal-header"
			});
			return this;
		},
		postRender: function() {
			return this;
		},
		onClose: function(e) {
			if (e)
				e.preventDefault();
			if (this.body && this.body.$el) {
				this.body.close();
			}
			$('body').removeClass('modal-open');
			$('body').removeAttr("style");
			$('.modal-backdrop').remove();
		}
	});

	ModalView.msg = function(options) {
		var modal = new Backbone.ModalView({
			type: options.type,
			size: options.size,
			title: options.title,
			body: options.body,
                        isFooter: options.isFooter
		}).render();
		return modal;
	};
        
	ModalView.msgWithOkBtn = function(options) {
		var modal = new Backbone.ModalView({
			type: options.type,
			size: options.size,
			title: options.title,
			body: options.body,
			buttons: [{
				className: "btn-info",
				label: locale.ok,
				okButtonCallback: options.okButtonCallback,
				close: true
			}]
		}).render();
		return modal;
	};

	ModalView.msgWithOkCancelBtn = function(options) {
                var optionLabel = locale.ok;
                if (typeof options.okLabel != 'undefined') {
                    optionLabel = options.okLabel;
                }
                var okClose = true;
                if (typeof options.okClose != 'undefined') {
                    okClose = options.okClose;
                }
		var modal = new Backbone.ModalView({
			type: options.type,
			size: options.size,
			title: options.title,
			body: options.body,
			buttons: [{
				className: "btn-info",
				label: optionLabel,
				okButtonCallback: options.okButtonCallback,
				close: okClose
			}, {
				className: "btn-default",
				label: locale.close,
				cancelButtonCallback: options.cancelButtonCallback,
				close: true
			}]
		}).render();
		return modal;
	};

	return ModalView;
});
