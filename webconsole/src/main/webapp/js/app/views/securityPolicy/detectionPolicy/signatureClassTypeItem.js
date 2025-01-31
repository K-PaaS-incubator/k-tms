/*
 * 유형 추가 
 */

define(function(require) {
	
	"use strict";
	
	var $ 			= require('jquery'),
		Backbone 	= require('backbone'),
		locale 		= require('i18n!nls/str');
	
	return Backbone.View.extend({
		
		initialize: function(options) {
			this.model = options.model;
			this.listenTo(this.model, "change", this.render());
		},
		tagName: 'ul',
		
		template: _.template([
		          '<li><div><%= model.nClassType %> <%= model.strName %> <% if(nClassType >= 99) { %></div> <% if (WRITE_MODE == 1) {%> <button type="button" id="deleteSclassTypeBtn" class="btn btn-default btn-sm col-xs-2 pull-right margin-tm20 margin-l5" data-nclasstype="<%= model.nClassType %>" data-strname="<%= model.strName %>"><%= locale.remove %></button> <% } %><% } %> </li>'
		].join('')),
		render: function() {
			this.$el.html(this.template({
				nClassType: this.model.get('nClassType'),
				model: this.model.toJSON(),
				locale: locale
			}));
			return this;
		}
	});
});