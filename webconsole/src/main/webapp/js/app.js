define(function (require) {
    "use strict";

    // require library
    var $ = require('jquery'),
            _ = require('underscore'),
            Backbone = require('backbone'),
            Calendar = require('utils/Calendar'),
            Loading = require('utils/Loading'),
            ModalView = require('views/common/modal'),
            AlertView = require('views/common/alert'),
            ChartView = require('views/common/chart'),
            Utils = require('utils/utils'),
            FormSerialize = require('utils/FormUtil'),
            TemplateHelper = require('utils/templateHelper'),
            TargetView = require('views/common/target'),
            sessionManager = require('utils/sessionManager'),
            PeriodView = require('views/common/period');

    var application = {
    		//세선감지 추가 멤버변수 및 체크함수
    		SERVER_SESSIONTIMEOUT : 0,
    		CRRUNT_TIME : 0,
    		CURR_INTERVAL_ID : null,
    		//일반 user 우회접근 차단
    		USER_ROLE_ID : 1,
    		NONE_USER_URI : [
    			'#systemSettings_systemConfig',
    			'#systemSettings_account',
    			'#securityPolicy_intrusionDetectionBatchDeployment',
    			'#securityPolicy_intrusionDetectionPolicy',
    			'#securityPolicy_yaraRuleBatchDeployment',
    			'#securityPolicy_yaraRulePolicy',
    			'#securityPolicy_sessionMonitorPolicy',
    			'#securityPolicy_anomaliesDetectionPolicy',
    			'#securityPolicy_hostManagement',
    			'#securityPolicy_threatPolicy',
    			'#securityPolicy_threatAlert',
    			'#securityPolicy_auditLogPolicy',
    			'#systemStatus_prevention',
    			'#systemStatus_auditLog',
    			'#systemStatus_managerState',
    			'#systemStatus_sensorState',
    			'#systemStatus_serviceInfo',
    		],
    		startSessionTimeOutCheck : function(){
    			
    			if(application.CURR_INTERVAL_ID != undefined || application.CURR_INTERVAL_ID  != null){
    				clearInterval(application.CURR_INTERVAL_ID );
    			}
    			application.CURR_INTERVAL_ID = setInterval(function(){
    				//console.log('getSessionTimeOut : '+application.CRRUNT_TIME);
    				if(application.CRRUNT_TIME == 0){
    					Backbone.ajax({
    						method: 'POST',
    						contentType: 'application/json',
    						url: 'api/common/invalidateSession',
    						async: true,
    						cache: true,
    						success: function(data) {
    							console.log('invalid session');
    							location.href='/';
    						}
    					});
    				}
    				application.CRRUNT_TIME --;
    			}, 1000);
    		},
    		//end 세선감지 추가 멤버변수 및 체크함수
        initialize: function () {
//            var generateCsrfToken = function () {
//                function generateRandomString(length) {
//                    var text = "";
//                    var possible = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
//                    for (var i = 0; i < length; i++) {
//                        text += possible.charAt(Math.floor(Math.random() * possible.length));
//                    }
//                    return text;
//                }
//                ;
//                return btoa(generateRandomString(32));
//            }
//            var setCookie = function (cname, cvalue) { document.cookie = cname + "=" + cvalue + ";path=/"; }
            
            var _sync = Backbone.sync;
            Backbone.sync = function (method, model, options) {
//                options.beforeSend = function (xhr) {
//                    var csrfToken = generateCsrfToken();
//                    setCookie('CSRF_TOKEN', encodeURIComponent(csrfToken));
//                    xhr.setRequestHeader("_csrf", csrfToken);
//                };
                options.error = function (model, response, options) {
                    if (options === 'Unauthorized') {
                        eval(model.responseText);
                    }
                };

                _sync(method, model, options);
            };

            //<---- 세션 감지 추가
            var $doc = $(document);
            var noneCheckUrl = [ 'requireLogin', 'api/common/'];//
            
            //ajax 발송 감지
			$doc.ajaxSend(function(event, xhr, settings ) {
				//sessionManager.isLogin
				
				//2018-06-11 세션타임아웃 체크및 강제 세션 종료 기능 추가
				var isCheck = true;
				for(var i in noneCheckUrl){
					if(settings.url.indexOf(noneCheckUrl[i]) > -1){
						isCheck = false;
						break;
					}
				}
				if(isCheck){
					
					Backbone.ajax({
	                    method: 'POST',
	                    contentType: 'application/json',
	                    url: '/api/common/getSessionTime',
	                    async: true,
	                    cache: true,
	                    success: function(data) { 
	                    	if(data.result == null || data.result == undefined){//세션종료체크 추가
	                    		location.href = '/';
	                    	}
	                    	//console.log('start url : ' + settings.url);
	                    	var time = data.result;
	                    	if(time > 0){
	                    		application.SERVER_SESSIONTIMEOUT = time * 60;
	                    		application.CRRUNT_TIME = application.SERVER_SESSIONTIMEOUT;
	                    		application.startSessionTimeOutCheck();
	                    	}
	                    }
	                });
					
					//console.log(settings.url);
				}
				//end (세션타임아웃 감치종료)
				
				if(!sessionManager.isLogin){
					location.href = '/';
				}
			});
            //ajax 오류 감지(세션감지)
            $doc.ajaxError(function (event, xhr) {
                if (xhr.status == 401) {
                    location.href = '/';
                }
            });
            //<---- 세션 감지 추가 End

            /*
             * var _constructor = Backbone.View.prototype.constructor;
             * Backbone.View.prototype.constructor = function(options){
             * alert('init'); _constructor(options); }
             */

            Backbone.View.prototype.close = function () {
                if (this.beforeClose) {
                    this.beforeClose();
                }
                this.remove();
                this.unbind();
                if (this.onClose) {
                    this.onClose();
                }
                // TODO: model과 collection 객체 제거
            };

            Backbone.ajax({
                method: 'POST',
                contentType: 'application/json',
                url: 'api/common/getLocale',
                async: true,
                cache: true,
                success: function (data) { 
                	console.log('locale', data);
                }
            });
            Backbone.Calendar = Calendar;
            Backbone.Loading = Loading;
            Backbone.ModalView = ModalView;
            Backbone.AlertView = AlertView;
            Backbone.ChartView = ChartView;
            Backbone.Utils = Utils;
            Backbone.FormSerialize = FormSerialize;
            Backbone.TargetView = TargetView;
            Backbone.PeriodView = PeriodView;
            Backbone.TemplateHelper = TemplateHelper;
        },
    };

    return application;
});
