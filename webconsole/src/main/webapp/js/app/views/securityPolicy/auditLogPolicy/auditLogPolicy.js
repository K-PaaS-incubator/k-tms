/**
 * 감사로그 정책 
 */
define(function(require) {

    "use strict";

    var $ 					= require('jquery'),
        Backbone 			= require('backbone'),
        alertMessage 		= require('utils/AlertMessage'),
        TabView 			= require('views/common/tab'),
        locale 				= require('i18n!nls/str'),
        CommonModel 		= require('models/common'),
        AuditLogAction 		= require('views/securityPolicy/auditLogPolicy/auditLogAction'),
        AuditLogError 		= require('views/securityPolicy/auditLogPolicy/auditLogError'),
        AuditLogWarning 	= require('views/securityPolicy/auditLogPolicy/auditLogWarning');

    return Backbone.View.extend({
        template: _.template('<div id="tabs" class="content-white"></div>'),
        auditLogTab: null,
        initialize: function() {
            this.viewName = 1;
        },
        events: {
            "click .changeBtn"				: "changeEditView",
            "click #tabChange"				: "tabChange",
            "click .allUsedCheck"			: "allUsedCheck",
            "click .allEmergencyAlarm"		: "allEmergencyAlarm",
            "click .allAudibilityAlarm"		: "allAudibilityAlarm",
            "click .allProgramAlarm"		: "allProgramAlarm"
        },
        render: function() {
            this.$el.html(this.template());
            this.createAuditLogTab();
            return this;
        },
        changeEditView: function() {
            var tabs = this.auditLogTab.tabs;
//            $('.allUsedCheck').removeAttr("disabled");
//            $('.allEmergencyAlarm').removeAttr("disabled");
//            $('.allAudibilityAlarm').removeAttr("disabled");
//            $('.allProgramAlarm').removeAttr("disabled");

            var status = true;

            _.each(tabs, function(tab, i) {
                if (tab.status != undefined) {
                    status = tab.status();
                }
            });
            if (status == false) { //수정 폼 가져오기
                _.each(tabs, function (tab, i) {
                    if (tab.toggle != undefined)
                        tab.toggle();
                    tab.delegateEvents();
                });
                $(".changeBtn", this.el).text(locale.modifyCompleted);
            } else {
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
                    // 저장 여부 메세지 팝업
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
                        okButtonCallback: function(e) {
                            var editableTab = false;
                            Backbone.Loading.setLoading($('body'));
                            setTimeout(function() {
                                _.each(tabs, function (tab, i) {
                                    if (tab.toggle != undefined) {
                                        var view = tab.toggle();
                                        //self.currentTab.setBody(tab.toggle().el, i);
                                        editableTab = view.status();
                                    }
                                    tab.delegateEvents();
                                });
                                $(".changeBtn", this.el).text(locale.modify);

                                if (editableTab == false) {
                                    $('.allUsedCheck').attr("disabled", "disabled");
                                    $('.allEmergencyAlarm').attr("disabled", "disabled");
                                    $('.allAudibilityAlarm').attr("disabled", "disabled");
                                    $('.allProgramAlarm').attr("disabled", "disabled");
                                }
                                Backbone.Loading.removeLoading($('body'));
                                // 저장완료 메세지 팝업
                                alertMessage.infoMessage(locale.msg.saveMsg, 'info', '', 'small');
                            });
                        }});
                }
            }
        },
        createAuditLogTab: function() {
            var tabs = [
                {title: locale.dashboard.auditAction, viewObj: this.viewFactory('auditLogAction'), active: true, removable: false, icon: 'action', changeable: true, hidden: true},
                {title: locale.dashboard.auditError, viewObj: this.viewFactory('auditLogError'), active: false, removable: false, icon: 'error', changeable: false, hidden: true},
                {title: locale.dashboard.auditWarning, viewObj: this.viewFactory('auditLogWarning'), active: false, removable: false, icon: 'warning', changeable: false, hidden: true}
            ];
            this.auditLogTab = new TabView({
                el: "#tabs",
                data: tabs
            });
            this.auditLogTab.render();
            this.currentTab = this.auditLogTab;
        },
        viewFactory: function(viewName) {
            if (viewName == "auditLogAction") {
                return new AuditLogAction({});
            }
            else if (viewName == "auditLogError") {
                return new AuditLogError({});
            } else if (viewName == "auditLogWarning") {
                return new AuditLogWarning({});
            }
        },
        allUsedCheck: function() {
            var tabs = this.auditLogTab.tabs;
            var frm = document.all;
            var usedTot = frm.used.length;
            var lType = 1;
            var allUsedCheckVal = 0;
            if ($("#tabs .nav > li.active").index() == 2) {
                lType = 2;
                allUsedCheckVal = 1;
            } else if ($("#tabs .nav > li.active").index() == 3) {
                lType = 3;
                allUsedCheckVal = 2;
            }
            tabs[allUsedCheckVal].allUsed(frm.allUsedCheck[allUsedCheckVal].checked);
            for (var i = 0; i < usedTot; i++) {
                if (parseInt(frm.lType1[i].value) == lType) {
                    frm.used[i].checked = frm.allUsedCheck[allUsedCheckVal].checked;
                }
            }
        },
        allEmergencyAlarm: function() {
            var frm = document.all;
            var tot = frm.emergencyAlarm.length;
            var lType = 1;
            var allCheckVal = 0;
            if ($("#tabs .nav > li.active").index() == 2) {
                lType = 2;
                allCheckVal = 1;
            } else if ($("#tabs .nav > li.active").index() == 3) {
                lType = 3;
                allCheckVal = 2;
            }
            for (var i = 0; i < tot; i++) {
                if (parseInt(frm.lType1[i].value) == lType) {
                    frm.emergencyAlarm[i].checked = frm.allEmergencyAlarm[allCheckVal].checked;
                }
            }
        },
        allAudibilityAlarm: function() {
            var frm = document.all;
            var tot = frm.audibilityAlarm.length;
            var lType = 1;
            var allCheckVal = 0;
            if ($("#tabs .nav > li.active").index() == 2) {
                lType = 2;
                allCheckVal = 1;
            } else if ($("#tabs .nav > li.active").index() == 3) {
                lType = 3;
                allCheckVal = 2;
            }
            for (var i = 0; i < tot; i++) {
                if (parseInt(frm.lType1[i].value) == lType) {
                    frm.audibilityAlarm[i].checked = frm.allAudibilityAlarm[allCheckVal].checked;
                }
            }
        },
        allProgramAlarm: function() {
            var tabs = this.auditLogTab.tabs;
            var frm = document.all;
            var tot = frm.programAlarm.length;
            var lType = 1;
            var allCheckVal = 0;
            if ($("#tabs .nav > li.active").index() == 2) {
                lType = 2;
                allCheckVal = 1;
            } else if ($("#tabs .nav > li.active").index() == 3) {
                lType = 3;
                allCheckVal = 2;
            }
            tabs[allCheckVal].allProgramChecked(frm.allProgramAlarm[allCheckVal].checked);
            for (var i = 0; i < tot; i++) {
                if (parseInt(frm.lType1[i].value) == lType) {
                    frm.programAlarm[i].checked = frm.allProgramAlarm[allCheckVal].checked;
                }
            }
        },
    });
});