/** 
 * 공격유형 select box 목록   
 */
define(function(require){
	
	"use strict";
	
	// require library
	var $ = require('jquery'),
		Backbone = require('backbone');
	
	var AttackTypeModel = require('models/securityPolicy/attackTypeModel');
	
	var AttackTypeCollection = Backbone.Collection.extend({
		
		model : AttackTypeModel,
		byType : function(policyCollection) {
			/**
			 * type : [
			 * 	{type:1},{type:2},{type:3}
			 * ]
			 * policy : [
			 *   {type:1,name:a},{type:1,name:b},{type:2,name:C}
			 * ]
			 */
			// 타입 1,2번을 가져오기 위해 중복을 제거한다.
			// 타입과 비교 
			// TODO 중복 제거, 배열 만들기 
			var filtered = this.filter(function(type) {
				var attackType = type.get('nClassType');
				var isHave = false;
				
				policyCollection.each(function(value) {
					if(value.get('sClassType') === attackType) {
						isHave = true;
					}
				});
				
				return isHave;
			});
			return new AttackTypeCollection(filtered);
		}
	});
	return AttackTypeCollection;
});