/**
 * @author lee kyunghee
 * @since 2015-12-24
 * @description FileMeta 데이터 조회 결과를 model에 담는다.  
*/
define(function(require){
	
	"use strict";
	
	// require library
	var $ = require('jquery'),
		Backbone = require('backbone');

	return Backbone.Model.extend({
		defaults: {
			// 리스트 
			rNum : 0, 
			deDestinationIp : '', // 피해호스트 
			dwSourceIp: '',		  // 침해호스트 
			strMagic: '',
			nSourcePort: '',
			nDestinationPort: '',
			totalRowSize : '0',
			nProtocol: '',
			vsensorName: '',
			sensorName: '',
			strFileName: '',
			dwFileSize: '',
                        lCode: 0,
                        nGrpIndex: 0,
                        bSeverity: 0,
                        strRuleName: '',
                        strGrpName: ''
		}
	});
});