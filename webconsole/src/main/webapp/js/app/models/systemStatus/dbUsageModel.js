/** 
 * @author kim yoon
 * @since 2014-12-18
 * @description db사용량 조회
 */
define(function(require) {
	
	"use strict";

	// require library
	var $ = require('jquery'),
		Backbone = require('backbone');
	
	return Backbone.Model.extend({
		defaults: {
			ltablespaceindex : 0,
			strName : '',
			tmoccur : '',
			dblTotal : 0,
			dblUsed : 0,
			dblSpace : 0,
			dblUsedPer : 0,
			dblSpacePer : 0,
			status : '',
			statusStr : '',
			pieChart: ''
		}
	});
});