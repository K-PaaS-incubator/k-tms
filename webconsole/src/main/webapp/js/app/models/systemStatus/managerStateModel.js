/** 
 * @author 
 * @since 20150316
 * @description 매니저상태 + DB사용량 메뉴  
 */
define(function(require) {
	
	"use strict";

	// require library
	var $ = require('jquery'),
		Backbone = require('backbone');
	
	return Backbone.Model.extend({
		defaults: {
			strName: '',
			dwCpuSpeed: '',
			dwCpuNum: '',
			dblcurCpuUsage:'',
			dblcurMemUsed: '',
			dwMemTotal: '',
			dwHddUsed : '',
			dwHddTotal : '',
			dwProcessNum : '',
			tmoccur : ''
		}
	});
});