/** 
 * @author leekyunghee
 * @since 2014-12-09
 * @description 원본로그 검색조건을 위한 모델
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
			
			attackNameInput: '',
			attackTypeSelect: null,
			severityHCheck: false,
			severityMCheck: false,
			severityLCheck: false,
			severityICheck: false,

			winBoundSelect: 0,
			simpleTimeSelect: '',
			sortSelect: 'nsum',
	
			thresholdSelect: null,
			thresholdNumInput: null,
			destPortInput: null
		},
		getRequiredParamsToJson: function() {
			return {
				'startDateInput' 	: this.get('startDateInput'),
				'endDateInput' 		: this.get('endDateInput'),
				'lnetgroupIndex' 	: this.get('lnetgroupIndex'),
				'lnetworkIndex' 	: this.get('lnetworkIndex'),
				'lvsensorIndex' 	: this.get('lvsensorIndex'),
				'lsensorIndex' 		: this.get('lsensorIndex'),
				'winBoundSelect' 	: this.get('winBoundSelect'),
				'listViewInput' 	: this.get('listViewInput')
			};
		}
	});
});