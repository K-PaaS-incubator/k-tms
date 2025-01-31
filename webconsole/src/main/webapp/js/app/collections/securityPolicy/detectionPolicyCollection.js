/** 
 * 탐지정책 등록정보
 */
define(function(require){
	
	"use strict";
	
	// require library
	var $ = require('jquery'),
		Backbone = require('backbone');
	
	var DetectionPolicyModel = require('models/securityPolicy/detectionPolicyModel');
	
	var DetectionPolicyCollection = Backbone.Collection.extend({
		model : DetectionPolicyModel,
		byName : function (name) {
			var filtered = this.filter(function(policy) {
			    return policy.get("strDescription").indexOf(name) !== -1;
			});
			return new DetectionPolicyCollection(filtered);
		}
	});
	return DetectionPolicyCollection;
});