/**
 * main view
 * systemSettings sublist 
 * dependency common/tab.js  
 * 
 */
define(function (require) {

    "use strict";

    var $ 			= require('jquery'),
        Backbone 	= require('backbone');

    var locale 		= require('i18n!nls/str'),
        errorLocale = require('i18n!nls/error');

    var tpl 						= require('text!tpl/systemSettings/systemConfig.html'),
        TabView 					= require('views/common/tab'), 														// 탭 생성뷰
        alertMessage 				= require('utils/AlertMessage'),
        RegisterManagerView 		= require('views/systemSettings/systemConfig/manager/manager'), 					// 매니저 > 등록정보
        DbManagementView 			= require('views/systemSettings/systemConfig/manager/dbManagement'), 				// 매니저 > DB관리
        TimeSyncView 				= require('views/systemSettings/systemConfig/manager/managerTimeSync'), 			// 매니저 > 시간동기화 
        DbBackupView 				= require('views/systemSettings/systemConfig/manager/dbBackup'), 					// 매니저 > DB백업
        RegisterNetworkView 		= require('views/systemSettings/systemConfig/network/network');					// 네트워크 > 등록 정보

    var NetworkCollection 			= require('collections/systemSettings/networkCollection');
    var sessionManager 				= require('utils/sessionManager');

    return Backbone.View.extend({
        template: _.template(tpl),
        sensorTab: null,
        managerTab: null,
        vSensorTab: null,
        networkTab: null,
        networkGroupTab: null,
        currentTab: '',
        selTab: '',
        selIndex: '',
        initialize: function(options) {
            this.networkCollection 			= new NetworkCollection();
        },
        events: {
            "click .dropdown-menu li > a"	: "insertTab", 				// 신규생성 드롭다운 선택 이벤트
            "click .changeBtn"				: "editTab", 				// 생성된 탭 상세보기의 변경 처리 이벤트 
            "click .deleteBtn"				: "removeSystemList", 		// 선택된 목록에서 삭제 
            "click #manager"				: "createManagerTab", 		// 목록 선택에 따라 매니저탭 생성
            "click #network > a"			: "createNetworkTab"		// 목록 선택에 따라 네트워크탭 생성
        },
        render: function() {
            this.$el.html(this.template({
                locale: locale
            }));
            // 네트워크 목록 호출 
            this.getNetworkList();
            // 초기 조회시 보여질 화면 
            $("#manager", this.el).trigger("click");

            return this;
        },
        // 매니저 탭 생성 
        createManagerTab: function() {
            // 탭이 생성될 때 변경 버튼이 보여진다.
            this.$el.find('.changeBtn').show();
            this.$(".changeBtn").val(locale.modify);
            var tabs = [
                {title: locale.registInfo, viewObj: this.viewFactory('registerManager'), active: true, removable: false, icon: 'info', changeable: false, hidden: true},
                {title: locale.dbManagement, viewObj: this.viewFactory('dbManagement'), active: false, removable: false, icon: 'dbManagement', changeable: false, hidden: true},
                {title: locale.timeSynchronization, viewObj: this.viewFactory('timeSync'), active: false, removable: false, icon: 'timesyn', changeable: false, hidden: true},
                {title: locale.dbBackup, viewObj: this.viewFactory('dbBackup'), active: false, removable: false, icon: 'dbbackup', changeable: false, hidden: true},
                // {title: locale.integrityCheck, viewObj: this.viewFactory('managerIntegraityCheck'), active: false, removable: false, icon: 'action', changeable: false, hidden: true},
                // {title: locale.inboundSetting, viewObj: this.viewFactory('inboundSetting', 1), active: false, removable: false, icon: 'application', changeable: false, hidden: true},
                // {title: locale.dbPassword, viewObj: this.viewFactory('dbPassword'), active: false, removable: false, icon: 'dbPassword', changeable: false, hidden: true}
            ];
            this.managerTab = new TabView({
                el: "#tabs",
                data: tabs
            });
            this.managerTab.render();
            this.currentTab = this.managerTab;

            this.selTab = 'manager';
        },
        // 네트워크 탭 생성 
        createNetworkTab: function(e) {
            var self = this;
            self.$el.find('.changeBtn').show();
            self.$(".changeBtn").val(locale.modify);
            var lnetworkIndex = $(e.currentTarget).data('lnetworkindex');
            var strName = $(e.currentTarget).data("strname");

            var tabs = [
                {title: locale.registInfo, viewObj: self.viewFactory('registerNetwork', lnetworkIndex, strName), active: true, removable: false, icon: 'info', changeable: false, hidden: true}
            ];
            self.networkTab = new TabView({
                el: "#tabs",
                data: tabs
            });
            self.networkTab.render();
            self.currentTab = self.networkTab;

            self.selTab = 'network';
            self.selIndex = lnetworkIndex;

        },
        getNetworkList: function() {
            var self = this;
            self.networkCollection.fetch({
                method: 'POST',
                data: JSON.stringify({}),
                url: 'api/systemSetting/selectNetworkList',
                async: false,
                contentType: 'application/json',
                success: function (collection) {
                    $("#network", self.el).empty();
                    self.networkCollection.each(function (model) {
                        $("#network", self.el).append('<a style="text-decoration:none" href="#systemSettings_systemConfig" class="overflow-ellipsis col-xs-11 networkIndex' + model.get('lnetworkIndex') + '" data-strname="' + model.get('strName') + '" data-lnetworkindex="' + model.get('lnetworkIndex') + '"><span></span>' + model.get('strName') + '<a class="icon-del col-xs-1 deleteBtn" data-seltab="network" data-lnetworkindex="' + model.get('lnetworkIndex') + '" data-strname="' + model.get('strName') + '"></a></a>');
                    }, self);
                    // 목록 개수 표시
                    if (self.networkCollection.length > 0) {
                        $("#netotalCount", self.el).text(self.networkCollection.length);
                        $("#netotalCount", self.el).digits();
                    } else {
                        $("#netotalCount", self.el).text(0);
                    }
                }
            });
        },
        // 컨텐츠뷰 객체 생성
        viewFactory: function(viewName, lIndex, strName) {

            if (viewName == "registerManager") {
                return new RegisterManagerView();
            } else if (viewName == "dbManagement") {
                return new DbManagementView();
            } else if (viewName == "dbPassword") {
                return new DbPasswordView();
            } else if (viewName == "timeSync") {
                return new TimeSyncView();
            } else if (viewName == "dbBackup") {
                return new DbBackupView();
            } else if (viewName == "managerIntegraityCheck") {
                return new ManagerIntegrityCheckView();
            } else if (viewName == "inboundSetting") {
                return new InboundSettingView({
                    lIndex: lIndex
                });
            } else if (viewName == "registerNetwork") {
                return new RegisterNetworkView({
                    lnetworkIndex: lIndex,
                    strName: strName
                });
            }
        },
        editTab: function() {
            var self = this;
            var tabs = this.currentTab.tabs;
            var status = true;
            var statusType;
            _.each(tabs, function (tab, i) {
                if (tab.status != undefined) {
                    status = tab.status();
                }
                if (tab.statusType != undefined) {
                    statusType = tab.statusType();
                }
            });

            if (status == false) {
                _.each(tabs, function (tab, i) {
                    if (tab.toggle != undefined) //self.currentTab.setBody(tab.toggle().el, i);
                        tab.toggle();
                    tab.delegateEvents();
                });
                $(".changeBtn", this.el).val(locale.modifyCompleted);
            } else {
                var thisView = this;
                // // tab중에 invalid한 tab이 있을 경우
                var isValid = true;
                for (var i = 0; i < tabs.length; i++) {
                    if (tabs[i].isValid != undefined) {
                        isValid = tabs[i].isValid();
                        if (!isValid)
                            break;
                    }
                }
                // 각 탭별로 validation 체크 및 insert후 목록 갱신을 위해 필요함
                if (isValid != false) {
                    var ModalContent = Backbone.View.extend({
                        render: function () {
                            this.$el.html('<p class="modal-body-white-padding">' + locale.msg.isSaved + '</p>');
                            return this;
                        }
                    });
                    Backbone.ModalView.msgWithOkCancelBtn({
                        size: 'small',
                        type: 'info',
                        title: 'Message',
                        body: new ModalContent(),
                        okButtonCallback: function (e) {
                            var success = true;
                            Backbone.Loading.setLoading($('body'));
                            setTimeout(function () {
                                _.each(tabs, function (tab, i) {
                                    if (tab.toggle != undefined)
                                        self.currentTab.setBody(tab.toggle().el, i);
                                    tab.delegateEvents();
                                    if (!tab.valid) { // refs #21735 - Server Valid Value
                                        success = false;
                                    }
                                });
                                thisView.getNetworkList();

                                $(".changeBtn", this.el).val(locale.modify);
                                if (success) {
                                    var title = 'Info', message = locale.msg.saveMsg;
                                } else {
                                    title = 'Error';
                                    message = locale.msg.saveFail;
                                }
                                Backbone.Loading.removeLoading($('body'));
                                alertMessage.infoMessage(message, title);
                            });
                        }
                    });
                }
            }
        },
        insertTab: function(e) {
            var self = this;
            var newTabIndex = $(e.currentTarget).data("station");
            if (newTabIndex == 0) {
                this.createSensorTab(e);
            } else if (newTabIndex == 1) {
                this.createVsensorTab(e);
            } else if (newTabIndex == 2) {
                // 네트워크가 최소 한개 이상 있을 경우에만 네트워크 그룹을 생성합니다.
                if (self.networkCollection.length >= 1) {
                    this.createNetworkGroupTab(e);
                } else {
                    alertMessage.infoMessage(errorLocale.validation.networkGroupInsertFailNetwork + " " + errorLocale.validation.insertNetwork, 'info', '', 'small');
                }
            } else if (newTabIndex == 3) {
                // 가상센서 등록(존재) 여부에 따라 네트워크 신규생성을 하거나 못하게 해야한다.
                this.createNetworkTab(e);
            }
            $(".changeBtn", this.el).val(locale.registCompleted);
        },
        removeSystemList: function(e) {
            var thisView = this;
            var selTab = $(e.currentTarget).data("seltab");
            var networkIndex = $(e.currentTarget).data("lnetworkindex");
            var strname = $(e.currentTarget).data("strname");
            var flag = true;
            var message = '';

            //삭제시 연동된 네트워크 그룹이 있고, 이상징후 정책에 네트워크 설정을 확인하여 삭제 여부를 확인할것 
            var self = this;
            var networkInfo = _.where(this.networkCollection.toJSON(), {lnetworkIndex: networkIndex});
            var threatPolicy = '', anomalyPolicy = '', lnetworkgroup = '';
            if (networkInfo[0].lparentgroupIndex != '-1') {
                lnetworkgroup = locale.precedence + " " + locale.networkGrouup;
            }
            thisView.url = 'api/systemSetting/deleteNetworkSettingInfo';
            thisView.param = {lnetworkIndex: networkIndex};
            thisView.system = locale.network;
            flag = networkInfo[0].lparentgroupIndex != '-1' || this.anomalyPolicyCollection.length > 0 || this.threatPolicyCollection.at(0).get('lSetupIndex') != '0';
            message = strname + ' ' + locale.msg.isNetworkDelete;

            if (flag == false) {
                flag = true;
                message = '' + thisView.system + locale.msg.isDelete;
            }
            if (flag) {
                var ModalContent = Backbone.View.extend({
                    render: function () {
                        this.$el.html('<p class="modal-body-white-padding">' + message + '</p>');
                        return this;
                    }
                });
                Backbone.ModalView.msgWithOkCancelBtn({
                    title: "info",
                    body: new ModalContent(),
                    size: 'small',
                    okButtonCallback: function(e) {

                        Backbone.Loading.setLoading($('body'));

                        setTimeout(function() {
                            thisView.networkCollection.fetch({
                                method: 'POST',
                                url: thisView.url,
                                contentType: 'application/json',
                                dataType: 'text',
                                data: JSON.stringify(thisView.param),
                                async: false,
                                success: function(model) {
                                    thisView.getNetworkList();
                                    thisView.$('.changeBtn').attr("style", "display:none");
                                    thisView.$("#tabs").empty();
                                    alertMessage.infoMessage(locale.msg.deleteMsg, 'info', '', 'small');
                                }
                            });
                            Backbone.Loading.removeLoading($('body'));
                        });
                    }
                });
            }
        }
    });
});