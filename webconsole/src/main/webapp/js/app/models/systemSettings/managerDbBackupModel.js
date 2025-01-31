/** 
 *
 */
define(function(require) {
	
	"use strict";

	// require library
	var $ = require('jquery'),
		Backbone = require('backbone');
	
	return Backbone.Model.extend({
		defaults: {
			'nDayConfigFlag' : '',
			'strDayBookTime' : '',
			'nDayBookDayBefore' : '',
			'nDayFileFlag' : '',
			'strDayFileName' : '',
			'nDayTableDeleteFlag' : '',
			'nDayTableCheckValue' : '',
			
			'nMonthConfigFlag' : '',
			'strMonthBookTime' : '',
			'nMonthBookDay' : '',
			'nMonthBookDayBefore' : '',
			'nMonthFileFlag' : '',
			'strMonthFileName' : '',
			'nMonthTableDeleteFlag' : '',
			'nMonthTableCheckValue' : '',
			
			'strBackupPathName' : '',
			'nMinDriveFreeSize' : '',
			
			'changeInfo': false,
                        'managerBackupRtn' : ''
		}
	});
});