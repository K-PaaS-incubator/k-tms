/**
 * Export
 */
define(function(require) {
	
	"use strict";
	
	var $ 					= require('jquery'),
		_ 					= require('underscore'),
		Backbone 			= require('backbone'),
		bootstrap 			= require('bootstrap'),
		locale 				= require('i18n!nls/str'),
		LoginStatusModel 	= require('models/loginStatus');	// session 체크	
	
	var fileName 			= null;
	var content 			= null;
	
	var ACPExport = Backbone.View.extend({
		
		initialize: function(options) {
			this.fileName 	= options.fileName;
			this.content 	= options.content;
		},
		
		makeFile: function() {
			var self = this;
			var div = document.createElement('div');
			$(div).append($('<form 		id=\"hiddenForm\" method=\"POST\" action="js/tpl/common/createacp.jsp" >'+
							'<input type=\"hidden\"   name=\"fileName\" id=\"fileName\" value=\"'+self.fileName+'\" />'+
							'<input type=\"hidden\"   name=\"content\" id=\"content\" value=\"'+self.content+'\" />'));
			$(div).append($('</form>'));
			$('body').append($(div).html());
			var loginModel = new LoginStatusModel();
			loginModel.fetch({
				url: "requireLogin",
				async: false,
				method: 'POST',
				contentType: 'application/json',
				success: function(m){
					if(m.get('loginYN')) {
						$('body #hiddenForm').submit();
					}
				},
			});
		},
	});

	return ACPExport;

});
