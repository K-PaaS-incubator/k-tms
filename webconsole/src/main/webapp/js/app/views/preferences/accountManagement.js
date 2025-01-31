/*
 * 계정관리 상위 view 
 */

define(function (require) {

    "use strict";

    var $ 				= require('jquery'),
        Backbone 		= require('backbone'),
        locale 			= require('i18n!nls/str'),
        alertMessage 	= require('utils/AlertMessage'),
        TabView 		= require('views/common/tab'),
        tpl 			= require('text!tpl/preferences/accountManagement.html'),
        RegisterAdmin 	= require('views/preferences/registerAdmin'),
        RegisterUser 	= require('views/preferences/registerUser'),
        RegisterGroup 	= require('views/preferences/registerGroup'),
        sessionManager  = require('utils/sessionManager'),
        aesUtil 		= require('utils/security/AesUtil');

    var AccountCollection = require('collections/preferences/accountCollection'),
        AccountGroupCollection = require('collections/preferences/accountGroupCollection');

    return Backbone.View.extend({
        template: _.template(tpl),
        adminTab: null,
        userTab: null,
        groupTab: null,
        currentTab: '',
        initialize: function() {
            this.collection = new AccountCollection();
            this.accountGroupcollection = new AccountGroupCollection();
        },
        events: {
            "click .dropdown-menu li > a": "registerUserList", 	// 사용자 신규생성
            "click #userCreate": "registerUser", 				// 사용자 신규생성
            "click .changeBtn": "changeTabView", 				// 생성된 탭 상세보기의 변경 처리 이벤트
            "click .deleteBtn": "removeAccountList", 			// 사용자 삭제 
            "click #admin > a": "createAdminTab", 				// 목록 선택시 관리자 탭 생성
            "click #user  > a": "createUserTab", 				// 목록 선택시 사용자 탭 생성
            "click #group > a": "createGroupTab" 				// 목록 선택시 그룹 탭 생성
        },
        render: function() {
            this.$el.html(this.template({
                locale: locale
            }));

            // 계정 목록 
            this.getAccountList();

            // 계정 그룹 목록
            this.getGroupListData();

            // 초기 조회시 보여질 화면 
            //$("#userCreate", this.el).trigger("click");

            return this;
        },
        registerUserList: function(e) {
            var newTabIndex = $(e.currentTarget).data("sgrouptype");
            if (newTabIndex == 2) {
                this.createUserTab(e);		// 사용자 신규 생성
            } else if (newTabIndex == 0) {
                this.createAdminTab(e);		// 관리자 신규 생성 
            } else if (newTabIndex == 3) {
                this.createGroupTab(e);		// 그룹 신규 생성 
            }
            $(".changeBtn", this.el).val(locale.registCompleted);
        },
        // 등록완료가 아닌 버튼으로 오류 발생 주석처리
//        registerUser: function (e) {
//            this.createUserTab(e);
//        },
        createAdminTab: function(e) {
            this.$el.find('.changeBtn').show();
            this.$(".changeBtn").val(locale.modify);

            var userIndex = $(e.currentTarget).data("userindex");
            var strId = $(e.currentTarget).data("strid");
            var groupType = $(e.currentTarget).data("grouptype");
            if (strId != sessionManager.UserName) {
                this.$(".changeBtn").hide();
            } else {
                this.$(".changeBtn").show();
            }

            var tabs = [
                {title: locale.registInfo, viewObj: this.viewFactory('registerAdmin', userIndex, strId), active: true, removable: false, icon: 'info', changeable: false, hidden: true}
            ];
            this.adminTab = new TabView({
                el: "#tabs",
                data: tabs
            });
            this.adminTab.render();
            this.currentTab = this.adminTab;

            this.selTab = 'admin';
            this.userIndex = userIndex;
            this.groupType = groupType;		// 삭제시 필요한 인덱스
        },
        createUserTab: function (e) {
            this.$el.find('.changeBtn').show();
            this.$(".changeBtn").val(locale.modify);
            this.$(".changeBtn").prop("disabled", false)

            var userIndex = $(e.currentTarget).data("userindex");
            var strId = $(e.currentTarget).data("strid");
            var groupType = $(e.currentTarget).data("grouptype");

            var tabs = [
                {title: locale.registInfo, viewObj: this.viewFactory('registerUser', userIndex, strId), active: true, removable: false, icon: 'info', changeable: false, hidden: true}
            ];
            this.userTab = new TabView({
                el: "#tabs",
                data: tabs
            });
            this.userTab.render();
            this.currentTab = this.userTab;

            this.selTab = 'user';
            this.userIndex = userIndex;
            this.groupType = groupType;
        },
        createGroupTab: function(e) {
            this.$el.find('.changeBtn').show();
            this.$(".changeBtn").val(locale.modify);
            this.$(".changeBtn").prop("disabled", false)

            var lIndex = $(e.currentTarget).data("lindex");
            var strId = $(e.currentTarget).data("strname");

            var tabs = [
                {title: locale.registInfo, viewObj: this.viewFactory('registerGroup', lIndex, strId), active: true, removable: false, icon: 'info', changeable: false, hidden: true}
            ];
            this.groupTab = new TabView({
                el: "#tabs",
                data: tabs
            });
            this.groupTab.render();
            this.currentTab = this.groupTab;

            this.selTab = 'group';
            this.lIndex = lIndex;
        },
        changeTabView: function() {
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
            if (status == false) { 	//수정 폼 가져오기
                _.each(tabs, function (tab, i) {
                    if (tab.toggle != undefined)
                        tab.toggle();
                    tab.delegateEvents();
                });
                $(".changeBtn", this.el).val(locale.modifyCompleted);
            } else {
                var thisView = this;
                // tab중에 invalid한 tab이 있을 경우
                var isValid = true;
                for (var i = 0; i < tabs.length; i++) {
                    if (tabs[i].isValid != undefined) {
                        isValid = tabs[i].isValid();
                        if (!isValid)
                            break;
                    }
                }
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

                            Backbone.Loading.setLoading($('body'));

                            setTimeout(function () {
                                _.each(tabs, function (tab, i) {
                                    if (tab.toggle != undefined)
                                        tab.toggle();
                                    thisView.insertIndex = tab.getInsertIndex();
                                    tab.delegateEvents();
                                });
                                // insert 인덱스 가져오도록 추가 
                                // 가져온 인덱스로 trigger 
                                if (thisView.selTab == 'admin') {
                                    thisView.getAccountList();
                                    thisView.$("#selAdmin" + thisView.insertIndex).trigger("click");
                                } else if (thisView.selTab == 'user') {
                                    thisView.getAccountList();
                                    thisView.$("#selUser" + thisView.insertIndex).trigger("click");
                                } else if (thisView.selTab == 'group') {
                                    thisView.getGroupListData();
                                    thisView.$("#selGroup" + thisView.insertIndex).trigger("click");
                                }
                                Backbone.Loading.removeLoading($('body'));
                                $(".changeBtn", this.el).val(locale.modify);

                                alertMessage.infoMessage(locale.msg.saveMsg, 'info', '', 'small');
                            });
                        }
                    });
                }
            }
        },
        /*
         * 계정 목록 조회 
         */
        getAccountList: function() {
            var self = this;
            self.collection.fetch({
                method: 'POST',
                data: JSON.stringify({}),
                url: 'api/preferences/selectAccountList',
                contentType: 'application/json',
                success: function (collection) {
                    $("#admin", self.el).empty();
                    $("#user", self.el).empty();
                    var userCount = 0;
                    var adminCount = 0;
                    for (var i = 0; i < self.collection.length; i++) {
                        if (self.collection.at(i).get('groupType') == 0) {
                            if (self.collection.at(i).get('userIndex') == 1) {
                                $("#admin", self.el).append('<a href="#systemSettings_account" class="overflow-ellipsis col-xs-11" id="selAdmin' + collection.at(i).get('userIndex') + '" style="text-decoration:none" data-strid="' + collection.at(i).get('id') + '" data-userindex="' + collection.at(i).get('userIndex') + '" data-grouptype="' + collection.at(i).get('groupType') + '"><span class="sensor-on"></span>' + collection.at(i).get('id') + '</a>');
                            } else {
                                $("#admin", self.el).append('<a href="#systemSettings_account" class="overflow-ellipsis col-xs-11" id="selAdmin' + collection.at(i).get('userIndex') + '" style="text-decoration:none" data-strid="' + collection.at(i).get('id') + '" data-userindex="' + collection.at(i).get('userIndex') + '" data-grouptype="' + collection.at(i).get('groupType') + '"><span class="sensor-on"></span>' + collection.at(i).get('id') + '<a class="icon-del col-xs-1 deleteBtn" data-selindex="' + collection.at(i).get('userIndex') + '" data-seltab="admin" data-userindex="' + collection.at(i).get('userIndex') + '"></a></a>');
                            }
                            //if (adminCount == 0) {
                            // 최초 페이지 로딩시 보여질 화면을 클릭 이벤트로 발생시킴 -> 신규생성 등록정보 탭이 보여지도록 변경   
                            //$("#selAdmin" + collection.at(i).get('userIndex'), self.el).trigger("click");
                            //}
                            adminCount++;
                        } else if (self.collection.at(i).get('groupType') == 2) {
                            $("#user", self.el).append('<a href="#systemSettings_account" class="overflow-ellipsis col-xs-11" id="selUser' + collection.at(i).get('userIndex') + '" style="text-decoration:none" data-strid="' + collection.at(i).get('id') + '" data-userindex="' + collection.at(i).get('userIndex') + '" data-grouptype="' + collection.at(i).get('groupType') + '"><span class="sensor-on"></span>' + collection.at(i).get('id') + '<a class="icon-del col-xs-1 deleteBtn" data-selindex="' + collection.at(i).get('userIndex') + '" data-seltab="user" data-userindex="' + collection.at(i).get('userIndex') + '"></a></a>');
                            userCount++;
                        }
                    }
                    $("#userTotalCount", self.el).text(userCount);
                    $("#adminTotalCount", self.el).text(adminCount);

                    $("#userTotalCount", self.el).digits();
                    $("#adminTotalCount", self.el).digits();
                }
            });
            this.groupType = this.$("#groupType").val();
            this.userIndex = this.$("#userIndex").val();
        },
        getGroupListData: function() {
            var self = this;
            self.accountGroupcollection.fetch({
                method: 'POST',
                data: JSON.stringify({}),
                url: 'api/preferences/selectAccountGroupList',
                contentType: 'application/json',
                success: function (accountGroupcollection) {
                    $("#group", self.el).empty();
                    for (var i = 0; i < self.accountGroupcollection.length; i++) {
                        $("#group", self.el).append('<a href="#systemSettings_account" class="overflow-ellipsis col-xs-11" id="selGroup' + accountGroupcollection.at(i).get('lIndex') + '" style="text-decoration:none" data-strname="' + accountGroupcollection.at(i).get('strName') + '" data-lindex="' + accountGroupcollection.at(i).get('lIndex') + '"><span class="sensor-on"></span>' + accountGroupcollection.at(i).get('strName') + '<a class="icon-del col-xs-1 deleteBtn" data-selindex="' + accountGroupcollection.at(i).get('lIndex') + '" data-seltab="group" data-lindex="' + accountGroupcollection.at(i).get('lIndex') + '"></a></a>');
                    }
                    if (self.accountGroupcollection.length > 0) {
                        $("#groupTotalCount", self.el).text(self.accountGroupcollection.length);
                        $("#groupTotalCount", self.el).digits();
                    } else {
                        $("#groupTotalCount", self.el).text('0');
                    }
                }
            });
        },
        removeAccountList: function(e) {
            var thisView = this;
            var selTab = $(e.currentTarget).data("seltab");
            var selIndex = $(e.currentTarget).data("selindex");
            var selType = selTab === 'group' ? 'api/preferences/deleteAccountGroupList' : 'api/preferences/deleteAccountList';
            var setParams = selTab === 'group' ? {lIndex: selIndex} : {userIndex: selIndex};

            var ModalContent = Backbone.View.extend({
                render: function () {
                    this.$el.html('<p class="modal-body-white-padding">' + locale.msg.isDeleteDefault +  '</p>');
                    return this;
                }
            });
            Backbone.ModalView.msgWithOkCancelBtn({
                title: "info",
                body: new ModalContent(),
                size: 'small',
                okButtonCallback: function (e) {
                    thisView.accountGroupcollection.fetch({
                        method: 'POST',
                        url: selType,
                        contentType: 'application/json',
                        dataType: 'text',
                        data: JSON.stringify(setParams),
                        async: false,
                        success: function () {
                            if (selTab == 'group') {
                                thisView.getGroupListData();
                            } else {
                                thisView.getAccountList();
                            }
                            thisView.$('.changeBtn').attr("style", "display:none");
                            thisView.$("#tabs").empty();
                            alertMessage.infoMessage(locale.msg.deleteMsg, 'info', '', 'small');
                        }
                    });
                }
            });
        },
        viewFactory: function(viewName, userIndex, strId) {
            if (viewName == "registerAdmin") {
                return new RegisterAdmin({
                    userIndex: userIndex,
                    strId: strId
                });
            } else if (viewName == "registerUser") {
                return new RegisterUser({
                    userIndex: userIndex,
                    strId: strId
                });
            } else if (viewName == "registerGroup") {
                return new RegisterGroup({
                    lIndex: userIndex,
                    strId: strId
                });
            }
        }
    });
});