define(function(require) {
	
	"use strict";

	// require library
	var $ 			= require('jquery'),
		Backbone 	= require('backbone');

	return Backbone.Model.extend({
		defaults: {
			lIndex: null,
			strMagic: null,
			tmLogTime: null,
			dwSourceIp: null,
			deDestinationIp: null,
			nProtocol: null,
			lvsensorIndex: null,
			vsensorName: null,
			nSourcePort: null,
			nDestinationPort: null,
			lSrcNetIndex: null,
			srcNetworkName: null,
			lDstNetIndex: null,
			dstNetworkName: null,
			lSensorIndex: null,
			sensorName: null,
			strHost: null,
			strFileHash: null,
			dwFileSize: null,
			tmDbTime: null,
            strStoreFileName: null
		}
	});

});
