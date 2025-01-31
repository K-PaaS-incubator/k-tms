/**
 * @since 20160401 
 * @author leekyunghee
 * @description yara 룰 유형 추가
 */

define(function(require) {
	
	"use strict";
	
	var $ 			= require('jquery'),
		Backbone 	= require('backbone'),
		locale 		= require('i18n!nls/str');
	
	return Backbone.View.extend({
		
		tagName: 'ul',
		initialize: function(options) {
			this.model = options.model;
			this.listenTo(this.model, "change", this.render());
		},
		
		template: _.template([
		          '<li><div><%= model.groupIndex %> <%= model.groupName %> <% if(groupIndex >= 99 && WRITE_MODE == 1) { %> </div> <button type="button" id="deleteSclassTypeBtn" class="btn btn-default btn-sm col-xs-2 pull-right margin-tm20 margin-l5" data-lgroupindex="<%= model.groupIndex %>"><%= locale.remove %></button> <% } %> </li>'
		].join('')),
		render: function() {
			this.$el.html(this.template({
				locale: locale,
				groupIndex: this.model.get('groupIndex'),
				model: this.model.toJSON()
			}));
			return this;
		}
	});
});