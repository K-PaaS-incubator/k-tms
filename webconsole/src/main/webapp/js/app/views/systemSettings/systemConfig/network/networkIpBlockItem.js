define(function(require) {
	
	"use strict";
	
	var $ = require('jquery'),
		_ = require('underscore'),
		Backbone = require('backbone'),
		locale = require('i18n!nls/str');
	
	return Backbone.View.extend({
		
		template : _.template([
					'<td class="align-center"><input type="radio" class="ipIndex" id="ipIndex" name="ipIndex" value="<%=model.ipIndex%>"/></td>',
					'<td class="align-center"><%= model.dwFromIp%></td>',
					'<td class="align-center"><%= model.dwToIp %></td>',
					'<td class="align-center"><%= model.mask %></td>'
					].join('')),
		tagName: 'tr',
		render: function(){
			var thisView = this;
			this.$el.append(this.template({
				model : thisView.model.toJSON()
			}));
			
			return this;
		}
		
	});
	
});
	