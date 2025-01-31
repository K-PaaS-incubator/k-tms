/**
 * @author lee kyunghee
 * @since 2015-12-24
 * @description FileMeta 화면 검색 조건을 model에 담아서 전달한다. 
*/
define(function(require) {
	
	"use strict";

	// require library
	var $ 			= require('jquery'),
		Backbone 	= require('backbone');
	
	return Backbone.Model.extend({
		
		defaults: {
			// 검색어
			lnetgroupIndex: 0,
			lnetworkIndex: 0,
			lvsensorIndex: 0,
			lsensorIndex: null,
			pathName: '',
			startDateInput: '',
			endDateInput: '',
			strMagic: '',  			// 파일 타입: pdf, excel...
			simpleTimeSelect: '',
			destPortInput: null, 	// 출발지 포트
			attackPortInput: null, 	// 도착지 포트
			toIpInput: null,
			fromIpInput: null,
			fileNameInput: null 	// 파일 이름 
		},
	});
});