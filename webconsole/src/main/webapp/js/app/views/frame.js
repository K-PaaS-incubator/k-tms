var LIVE_REFRESH_TIME = 60000 * 5;  //5 min setting
var LIVE_INTERVAL = null;
var LIVE_DETECTION;
var WC_DETECTION;
var LIVE_SESSIONDETECTION;
var WC_SESSIONDECTION;
var LIVE_SYSTEM_SENSOR;
var WC_SYSTEM_SENSOR;
var LIVE_SYSTEM_MANAGER;
var WC_SYSTEM_MANAGER;
var LIVE_APPLICATION;
var WC_APPLICATION;
var LIVE_FILEMETA;
var WC_FILEMETA;

var WC_KEY;

var CURRENT_TAB_INDEX;
var TAB_MAP = new newMap();


var SYSTEM_TYPE;
var LOGIN_IP_COUNT;

var SYSTEM_TIME_INTERVAL;
var REFRESH_INTERVAL;

var WRITE_MODE;  //1 : write on, 0 : write off
var CURRENT_MODE;  //1 : master, 0 : slave
var MASTER_STAT;  //0 : off, 1 : on
var SLAVE_STAT;  //0 : off, 1 : on

function startsWith(str, prefix) {
    if (str.length < prefix.length) {
        return false;
    }
    return str.indexOf(prefix) == 0;
}

function newMap() {
    var map = {};
    map.value = {};
    map.getKey = function (id) {
        return 'k_' + id;
    }
    map.put = function (id, value) {
        var key = map.getKey(id);
        map.value[key] = value;
    };
    map.contains = function (id) {
        var key = map.getKey(id);
        if (map.value[key]) {
            return true;
        } else {
            return false;
        }
    };
    map.get = function (id) {
        var key = map.getKey(id);
        if (map.value[key]) {
            return map.value[key];
        }
        return null;
    };
    map.remove = function (id) {
        var key = map.getKey(id);
        if (map.contains(id)) {
            map.value[key] = undefined;
        }
    };
    map.length = function () {
        return map.value.length;
    }
    return map;
}

define(function (require) {

    "use strict";

    var $ = require('jquery'),
            _ = require('underscore'),
            Backbone = require('backbone'),
            MenuView = require('views/common/menu'),
            menuLocale = require('i18n!nls/menu'),
            MenuCollection = require('collections/menuCollection'),
            CommonModel = require('models/common'),
            locale = require('i18n!nls/str'),
            tpl = require('text!tpl/frame.html'),
            Apps = require('apps'),
            template = _.template(tpl),
            sessionManager = require('utils/sessionManager'),
            dataExpression = require('utils/dataExpression'),
            MenuModel = require('models/menu'),
            UserInfoPopupView = require('views/popup/userInfoPopup'),
            DefaultUserInfoPopupView = require('views/popup/DefaultuserInfoPopup'),
            $menuItems;


    var currentContainer, timeInterval, allowedTime = 60 * 10, calAllowedTime = allowedTime;

    return Backbone.View.extend({
        events: {
            // 'click .menuLinkUrl': 'menuClick',
            "click .logout": "logout",
            "click .user-control button": "requireController",
            "click .strUserName": "getUserInfo",
//            "click #fiveMinRefreshChk": "fiveMinRefreshChkClick",
            "click #about": "about",
//            "keydown": "onKeydown",
            // 2018.06.28 / asj / temp
            "click #DefaultUserPopup": "getDefaultUserInfo"
        },
        initialize: function () {
            this.currentUrl = "";
            this.minifiedFlag = true;
            this.menuModel = new MenuModel();
            this.menuCollection = new MenuCollection();
            this.fiveMinCheck = true;

//            this.init5MintRefresh();

        },
        render: function () {
            if (sessionManager.isLogin) {
                this.$el.html(template({locale: locale}));
            }
            SYSTEM_TYPE = sessionManager.systemType;
            LOGIN_IP_COUNT = sessionManager.loginIpCount;
            if (parseInt(sessionManager.role) == 7) {
                WRITE_MODE = 1;
            } else {
                WRITE_MODE = 0;
            }
            var $control = $('.user-control button', this.el);
            if (sessionManager.isController) {
                $control.removeClass('threat-global-bg-lv2');
                $control.addClass('threat-global-bg-lv4');
                this.renderAllowedControlTime();
            } else {
                $control.removeClass('threat-global-bg-lv4');
                $control.addClass('threat-global-bg-lv2');
            }
            $(".strUserName").append(sessionManager.UserName);

            return this;
        },
        logout: function () {
            var common = new CommonModel();
            common.fetch({
                async: false,
                method: 'POST',
                contentType: 'application/json',
                url: 'api/logout',
                success: function () {
                    location.href = '/';
                }
            });
        },
        requireController: function () {
            var thisView = this;
            var common = new CommonModel();
            common.fetch({
                async: false,
                method: 'POST',
                contentType: 'application/json',
                url: 'api/requireController',
                success: function () {
                    if (common.get('returnType') === 'success') {
                        var $control = $('.user-control button', thisView.el);
                        if (sessionManager.isController) {
                            $control.removeClass('threat-global-bg-lv4');
                            $control.addClass('threat-global-bg-lv2');
                            sessionManager.isController = false;

                            $.when(thisView.resetAllowedControlTime()).done(thisView.renderCurrentSystemTime());
                        } else {
                            $control.removeClass('threat-global-bg-lv2');
                            $control.addClass('threat-global-bg-lv4');
                            sessionManager.isController = true;
                            thisView.renderAllowedControlTime();
                        }
                    } else {
                        var ModalContent = Backbone.View.extend({
                            render: function () {
                                this.$el.html('<p class="modal-body-white-padding">' + common.get('errorMessage') + '</p>');
                                return this;
                            }
                        });
                        Backbone.ModalView.msg({
                            title: common.get('returnType'),
                            body: new ModalContent()
                        });
                    }
                }
            });
        },
        renderAllowedControlTime: function () {
            var thisView = this;
            if (timeInterval) {
                clearInterval(timeInterval);
            }
            timeInterval = setInterval(function () {
                if (calAllowedTime > 0) {
                    var min = parseInt(calAllowedTime / 60);
                    var sec = calAllowedTime - (min * 60);
                    $('.service-time', thisView.el).text(locale.controlAllowedTime + ': ' + min + ':' + sec);
                } else {
                    thisView.requireController();
                }
                calAllowedTime--;
            }, 1000);
        },
        resetAllowedControlTime: function () {
            if (sessionManager.isController) {
                calAllowedTime = allowedTime;
            }
        },
        renderMenu: function () {
            var frame = this;
            frame.menuCollection.fetch({
                method: "POST",
                url: 'api/selectMenu',
                contentType: 'application/json',
                reset: frame.reset,
                success: function (collection) {
//                    collection.comparator = 'menuNo';
//                    collection.sort();
                    var menuView = new MenuView({
                        collection: collection.toJSON()
                    });
                    $('.sidebar div', frame.el).append(menuView.el);
                    //$('.sidebar div.sidebar-ul', frame.el).append(menuView.el);
                    $menuItems = $('.sidebar .nav li', frame.el);
                },
                async: false
            });
        },
        renderGlobalThreatCon: function () {
            var thisView = this;
            $.ajax({
                method: 'POST',
                url: 'api/selectGlobalThreatList',
                contentType: 'application/json',
                data: JSON.stringify({'startRowSize': 0, 'endRowSize': 1}),
                success: function (resultData) {
                    var bSeverityName;
                    switch (resultData[0].bSeverity) {
                        case 1:
                            bSeverityName = locale.globalThreatConLevel1;
                            break;
                        case 2:
                            bSeverityName = locale.globalThreatConLevel2;
                            break;
                        case 3:
                            bSeverityName = locale.globalThreatConLevel3;
                            break;
                        case 4:
                            bSeverityName = locale.globalThreatConLevel4;
                            break;
                        case 5:
                            bSeverityName = locale.globalThreatConLevel5;
                            break;
                        default :
                        	bSeverityName = "";
                    }
                    $('.alarm-msg', thisView.el).append('<span class="threat-global-bg-lv' + resultData[0].bSeverity + '"></span>' + bSeverityName);
                }
            });
        },
        renderPresence: function () {
            var thisView = this;
            var url = '/api/socketjs/requestPresenceMonitoring';
            if (url.indexOf('socketjs') == -1) {
                if (window.location.protocol == 'http:') {
                    url = 'ws://' + window.location.host + url;
                } else {
                    url = 'wss://' + window.location.host + url;
                }
            }
            var transports = [];
            this.presenceWS = (url.indexOf('socketjs') != -1) ? new SockJS(url, undefined, {
                protocols_whitelist: transports
            }) : new WebSocket(url);
            this.presenceWS.onopen = this.websocketOnOpen;
            this.presenceWS.onmessage = this.presenceOnMessage;
            this.presenceWS.onerror = this.websocketOnError;
            this.presenceWS.onclose = this.websocketOnClose;
            this.presenceWS.onheartbeat = this.websocketOnHeartBeat;

            this.presenceTimer = setInterval(function () {
                if (thisView.presenceWS && thisView.presenceWS.readyState === SockJS.OPEN) {
                    thisView.presenceWS.send(JSON.stringify({}));
                    clearInterval(thisView.presenceTimer);
                }
            }, 1000);
        },
        presenceOnMessage: function (event) {
            var thisView = this;
            var data = JSON.parse(event.data);
            require(['collections/loginStatusCollection'], function (LoginStatusCollection) {
                var collection = new LoginStatusCollection(data);
                $('.user-count a span', thisView.el).text(collection.length);
                $('.user-list ul', thisView.el).empty();
                collection.each(function (model) {
                    require(['views/presence'], function (PresenceView) {
                        var presenceView = new PresenceView({
                            model: model
                        });
                        $('.user-list ul', thisView.el).append(presenceView.render().el);
                    });
                });
            });
        },
        renderCurrentSystemTime: function () {
            if (!sessionManager.isController) {
                var thisView = this;
                var systemTimeInterVal;
                thisView.getSystemTime();
                systemTimeInterVal = setInterval(function () {
                    if ($('#fiveMinRefreshChk').is(':checked') && !startsWith(thisView.currentUrl, '#securityPolicy_')
                            && !startsWith(thisView.currentUrl, '#systemSettings_')) {
                        thisView.getSystemTime();
                    }
                }, 1000 * 30);
            }
        },
        getSystemTime: function () {
            var thisView = this;
            var common = new CommonModel();
            common.fetch({
                async: false,
                method: 'POST',
                contentType: 'application/json',
                url: 'api/currentSystemTime',
                success: function () {
                    thisView.setTimeInterVal(common.get('date'));
                }
            });
        },
        setTimeInterVal: function (dateTime) {
            var thisView = this;
            var d = new Date(dateTime);
            if (timeInterval) {
                clearInterval(timeInterval);
            }
            timeInterval = setInterval(function () {
                d.setSeconds(d.getSeconds() + 1);
                $('.service-time', thisView.el).text(d.toLocaleString());
            }, 1000);
        },
        applyLibrary: function () {
            Apps.init();
        },
        selectMenuItem: function (menuItem, title, minifiedFlag) {
            this.currentUrl = "#" + menuItem + "_" + title;
            if (LIVE_INTERVAL != null) {
                this.closeLive();
            }
            if (startsWith(menuItem, "monitor")) {
                this.pingLive();
            } else {
                this.closeLive();
            }
            $menuItems.removeClass('active');
            if (menuItem) {
                $('.' + menuItem).addClass('active');
            }
            if (title) {
                $('.' + menuItem + '_' + title).addClass('active');
                this.setTitle(menuItem, title);
            }
            if (this.minifiedFlag != minifiedFlag) {
                $("[data-click=sidebar-minify]").trigger('click');
            }
            this.minifiedFlag = minifiedFlag;
        },
        setTitle: function (menuItem, title) {
            // 조회 화면에서 대시보드로 돌아갔을 경우 네비게이션 title이 남아있는 현상 
            $('.navbar-header span a', this.el).empty();
            $('.navbar-header span:last', this.el).empty();
            $(".navbar-header .navigation", this.el).empty();

            // 대시보드를 제외한 나머지 메뉴에만 menuItem이 존재함 
            if (menuItem != "" && title != "dashboard") {
                $(".navbar-header .navigation", this.el).append(' > ');
            }
            if (menuItem) {
                $('.navbar-header span a', this.el).text(eval('menuLocale.menu.' + menuItem));
            }
            if (title) {
                $('.navbar-header span:last', this.el).text(eval('menuLocale.menu.' + title));
            }
        },
        setCurrentContainer: function (currentView) {
            currentContainer = currentView;
        },
        getCurrentContainer: function () {
            return currentContainer;
        },
        closeCurrentContainer: function () {
            if (currentContainer) {
                currentContainer.close();
            }
        },
        onClose: function () {
            if (this.presenceTimer) {
                clearInterval(this.presenceTimer);
            }
            if (this.presenceWS) {
                this.presenceWS.close();
                this.presenceWS = null;
            }
        },
        onKeydown: function (e) {
            var backspaceFlag = false;
            console.log(e);
            if (e.keyCode === 8) {
                if (e.target.type == "text" && $("#" + e.target.name).val() != "") {
                    backspaceFlag = true;
                } else if (e.target.type == "textarea") {
                    backspaceFlag = true;
                } else if (e.target.type == "text" && $(".attackNameInput").val() != "") {
                    // 탐지정책 공격명input
                    backspaceFlag = true;
                } else if (e.target.type == "password" && $("#password").val() != "") {
                    // 시스템관리>계정관리 암호 
                    backspaceFlag = true;
                } else {
                    backspaceFlag = false;
                }
                return backspaceFlag;
            }
        },
        geUserInfo: function () {
            Backbone.ModalView.msg({
                size: 'large',
                type: 'info',
                title: locale.accountInfo,
                isFooter: false,
                body: new UserInfoPopupView({
                    lUserIndex: sessionManager.lUserIndex,
                    UserName: sessionManager.UserName,
                    role: sessionManager.role
                })
            });
        },
        // 2018.06.28 / asj / temp
        getDefaultUserInfo: function () {
            Backbone.ModalView.msg({
                size: 'small',
                type: 'info',
                title: locale.DefaultaccountInfo,
                isFooter: false,
                body: new DefaultUserInfoPopupView({
                    lUserIndex: sessionManager.lUserIndex,
                    UserName: sessionManager.UserName,
                    role: sessionManager.role
                })
            });
        },
        pingLive: function () {
            LIVE_INTERVAL = setInterval(function (intervalFlag) {
                Backbone.ajax({
                    method: 'POST',
                    contentType: 'application/json',
                    url: 'api/currentSystemTime',
                    async: true,
                    cache: true,
                    success: function (data) {
                    	console.log(data);
                    }
                });
            }, LIVE_REFRESH_TIME);
        },
        closeLive: function () {
            clearInterval(LIVE_INTERVAL);
        },
        // 공통 5분 refresh
//        reFresh5Min: function () {
//            if (REFRESH_INTERVAL) {
//                clearInterval(REFRESH_INTERVAL);
//            }
//            if (this.fiveMinCheck) {
//                REFRESH_INTERVAL = setInterval(function () {
//                    if (!$('#loading', this.el).hasClass('in')) {
//                        $('.refreshBtn').trigger('click');
//                        $('.searchBtn').trigger('click');
//                        $('#searchBtn').trigger('click');
//                    }
//                }, LIVE_REFRESH_TIME);
//            }
//        },
//        fiveMinRefreshChkClick: function () {
//            this.fiveMinCheck = $('#fiveMinRefreshChk').is(':checked');
//            if (!startsWith(this.currentUrl, '#monitor_') && !startsWith(this.currentUrl, '#securityPolicy_') && !startsWith(this.currentUrl, '#systemSettings_')) {
//               // this.reFresh5Min();
//            } else {
//                if (REFRESH_INTERVAL) {
//                    clearInterval(REFRESH_INTERVAL);
//                }
//            }
//        },
        about: function () {
            require(['views/preferences/systemInformation'], function (SystemInformation) {
                var systemInformation = new SystemInformation();
                Backbone.ModalView.msg({
                    size: 'medium-large',
                    title: 'TESS TAS CLOUD ' + locale.about,
                    body: systemInformation
                });
            });

        },
//        init5MintRefresh: function () {
//            if (!startsWith(this.currentUrl, '#monitor_') && !startsWith(this.currentUrl, '#securityPolicy_') && !startsWith(this.currentUrl, '#systemSettings_')) {
//                this.reFresh5Min();
//            }
//        },
    });

});
