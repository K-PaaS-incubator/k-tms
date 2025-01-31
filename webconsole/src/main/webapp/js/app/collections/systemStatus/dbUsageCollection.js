/** 
 * @author kim yoon
 * @since 2014-12-18
 * @description DB 사용량 조회
 */
define(function(require){
	
	"use strict";
	
	// require library
	var $ = require('jquery'),
		Backbone = require('backbone');
	
	var DbUsageModel = require('models/systemStatus/dbUsageModel');
	
	return Backbone.Collection.extend({
		model : DbUsageModel
	});
	
});