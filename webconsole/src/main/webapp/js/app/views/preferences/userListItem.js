define(function(require) {
	
	var $ 		 = require('jquery'),
		Backbone = require('backbone');
	
	return Backbone.View.extend({
		
		tagName : 'tr',
		initialize: function(options) {
			this.type 	= options.type;
			this.model 	= options.model;
		},
		events: {
			'change #userCheckBox'		: 'checkUserVal'
		},
		template: _.template([   
		        '<td class="align-left">',
		        '<input type="checkbox" id="userCheckBox" name="userCheckBox" value="<%= model.userIndex %>" <% if (model.checked) { %> checked="checked" <% } %> disabled/> <%= model.id %>',
		        '</td>'
		].join('')), 
		templateEdit: _.template([
		        '<td class="align-left">',
		        '<input type="checkbox" id="userCheckBox" name="userCheckBox" value="<%= model.userIndex %>" <% if (model.checked) { %> checked="checked" <% } %>/>', 
		        '<%= model.id %>',
		        '</td>'         
		].join('')),
		render: function() {
			if (this.type == 'info') {
				this.$el.html(this.template({
					model : this.model.toJSON()
				}));
			} else if(this.type == 'edit') {
				this.$el.html(this.templateEdit({
					model : this.model.toJSON()
				}));
			}
			this.$userCheckBox = this.$('#userCheckBox');
			
			return this;
		},
		checkUserVal : function() {
			if (this.$userCheckBox.prop('checked')) {
				this.model.set({checked:true});
			} else {
				this.model.set({checked:false});
			}
		}
	});
});