/** 
 * @author 
 * @since 
 * @description 
 */
define(function(require) {
	
	"use strict";

	// require library
	var $ 			= require('jquery'),
		Backbone 	= require('backbone');
	
	return Backbone.Model.extend({
		defaults: {
			lIndex: '',
			lsensorindex: '',
			lvsensorindex: '',
			lMode:'',
			wDataSize: '',
			sData:'',
			packetType:''
		}
	});
});