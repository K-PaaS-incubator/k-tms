define(function(require) {

	"use strict";

	var $ = require('jquery'),
		_ = require('underscore'),
		Backbone = require('backbone'),
		LoginStatusModel = require('models/loginStatus');

	var loginStatusModel = new LoginStatusModel();
	

	loginStatusModel.fetch({
		url: "requireLogin",
		async: false,
		method: 'POST',
		contentType: 'application/json'
	});
	return {
		lUserIndex: loginStatusModel.get('userIndex'),
		UserName: loginStatusModel.get('userName'),
		LoginDate: loginStatusModel.get('loginDate'),
		isLogin: loginStatusModel.get('loginYN'),
		role: loginStatusModel.get('role'),
		isController: loginStatusModel.get('controller'),
		Category: loginStatusModel.get('category'),
		RefIndex: loginStatusModel.get('refIndex'),
		PathName: loginStatusModel.get('pathName'),
                systemType: loginStatusModel.get('systemType'),
                loginType: loginStatusModel.get('loginType'),
                loginIpCount: loginStatusModel.get('loginIpCount'),
                dualSystem: loginStatusModel.get('dualSystem'),
	};
	
});
