/**
 * @author leekyunghee update 18.02.05
 * @title 보안정책_악성코드_목록  
 * @description 아코디언 형태의 악성코드 목록을 검색 및 조회한다 
 */
define(function(require) {

    "use strict";

    var $ 								= require('jquery'),
        Backbone 						= require('backbone'),
        tpl 							= require('text!tpl/securityPolicy/yaraRulePolicy.html'),
        locale 							= require('i18n!nls/str'),
        errorLocale 					= require('i18n!nls/error'),
        TabView 						= require('views/common/tab'),
        alertMessage 					= require('utils/AlertMessage'),
        YaraRulePolicyGroupModel 		= require('models/securityPolicy/yaraRulePolicyModel'),
        YaraRulePolicyCollection 		= require('collections/securityPolicy/yaraRulePolicyCollection'),
        YaraRulePolicyGroupCollection 	= require('collections/securityPolicy/yaraRulePolicyGroupCollection'),
        RegisterYaraRulePolicy 			= require('views/securityPolicy/yaraRulePolicy/registerYaraRulePolicy'),
        YaraRuleTypeItemView 			= require('views/securityPolicy/yaraRulePolicy/yaraRuleTypeItem');

    return Backbone.View.extend({
        template: _.template(tpl),
        formEl: "form.yaraRuleForm",
        yaraRulePolicyTab: null,
        currentTab: '',
        parentTemplate: _.template([
            '<div class="panel panel-default panel-dp-selected" id="panel<%= target.groupIndex %>">',
            '<div class="panel-heading">',
            '<h4 class="panel-title">',
            /*'<a style="text-decoration:none" id="<%= target.groupIndex %>" data-toggle="collapse" data-parent="#accordion" href="#collapse<%= target.groupIndex %>" aria-expanded="true" aria-controls="collapse" class="<%= targetName %>" data-groupindex="<%= target.groupIndex %>"> <%= targetName %> <span id="attackTypeCount" class="badge pull-right"><%= target.ruleTotal %></span>',*/
            '<a id="<%= target.groupIndex %>" data-toggle="collapse" data-parent="#accordion" href="#collapse<%= target.groupIndex %>" aria-expanded="true" aria-controls="collapse" class="<%= targetName %>" data-groupindex="<%= target.groupIndex %>" style="text-decoration:none"> <%= targetName %> <span id="attackTypeCount" class="badge pull-right"><%= target.ruleTotal %></span>',
            '</a>',
            '</h4>',
            '</div>',
            '<div id="collapse<%= target.groupIndex %>" class="panel-collapse collapse" role="tabpanel" aria-labelledby="collapse<%= target.groupIndex %>">',
            '<div class="panel-body">',
            '</div>',
            '</div>'
        ].join('')),
        childTemplate: _.template([
            '<a href="#securityPolicy_yaraRulePolicy" class="overflow-ellipsis <% if(target.groupIndex >= 99) { %> col-xs-11 lIndex<%= target.lIndex %> <%}%>" data-lindex="<%= target.lIndex %>" data-rulename="<%= target.ruleName %>" data-groupindex="<%= target.groupIndex %>" style="text-decoration:none;">',
            '<%= target.ruleName %>',
            '<% if (target.groupIndex >= 99) { %>',
            '<% if (WRITE_MODE == 1) { %>',
            '<a class="icon-del <% if(target.groupIndex >= 99) { %> col-xs-1 deleteBtn <% } %>" data-lindex="<%= target.lIndex %>" data-groupindex="<%= target.groupIndex %>">',
            '</a>',
            '<% } %>',
            '<% } %>',
            '</a>'
        ].join('')),
        initialize: function () {
            this.yaraRulePolicyCollection 		= new YaraRulePolicyCollection();
            this.yaraRulePolicyGroupCollection 	= new YaraRulePolicyGroupCollection();
            this.model 							= new YaraRulePolicyGroupModel();
            this.groupSelect = [];
        },
        events: {
            "click #attackSearchBtn"			: "yaraRuleSearch", 				// 검색 버튼 클릭
            "click .panel-body > a"				: "createYaraRulePolicyTab", 		// 상세정보 화면
            "click #newTabBtn"					: "createYaraRulePolicyTab", 		// 신규등록 
            "click .changeBtn"					: "changeYaraRulePolicy", 			// toggle 버튼 이벤트 
            "click .panel .panel-heading a"		: "getYaraRuleList", 				// 목록 선택에 따라 유형그룹에 해당하는 시그니처를 조회해온다.
            "click .addUserSignatureBtn"		: "addSignatureGroup", 				// 그룹유형 신규등록
            "click #deleteSclassTypeBtn"		: "deleteSclassType", 				// 그룹유형 삭제
            "click #accordion-opening"			: "showAccordion", 					// 모두펼치기 버튼
            "click #accordion-closing"			: "closeAccordion", 				// 모두닫기 버튼
            "click .deleteBtn"					: "deleteYaraRulePolicyList", 		// 선택된 목록에서 삭제
            "click #cancelBtn"					: "settings",						// 검색 항목 초기화 
            "blur #strName"						: "validateStrName"					// 그룹 유형 이름 유효성 검증
        },
        yaraRuleGroup: [],
        render: function() {
            this.$el.html(this.template({locale: locale}));

            // YARA RULE 그룹 유형 목록 조회
            this.getYaraGroupList();
//            $(".panel .panel-heading a").trigger("click");

            // YARA RULE 그룹 유형 등록을 위해 인덱스 추가 
            this.getYaraRuleGroupIndex();
            //$("#attackTypeCount", self.el).digits();

            // 최초 조회 시 보여질 화면 호출
            $("#newTabBtn").trigger("click");
            
            if (WRITE_MODE != 1) {
                $('.changeBtn', this.el).hide();
                $('#newTabBtn', this.el).hide();
                $('#deleteSclassTypeBtn', this.el).hide();
                $('#importCSVBtn', this.el).hide();
                $('.addUserSignatureBtn', this.el).hide();
                $('.deleteBtn', this.el).hide();
            }
            return this;
        },
        showAccordion: function() {
            var self = this;
            self.getAllYaraRulePerGroup();
            _.each(self.yaraRuleGroup, function (group) {
                $('#collapse' + group.groupIndex, self.el).addClass('in');
            });
        },
        closeAccordion: function() {
            var self = this;
            _.each(self.yaraRuleGroup, function (group) {
                $('#collapse' + group.groupIndex, self.el).removeClass('in');
            });
        },
        settings: function() {
            Backbone.FormSerialize.setData($(this.formEl));
            $('#severityLevel', this.el).val('').attr('selected', 'selected');
            $('#attackTypeSelect', this.el).val('').attr('selected', 'selected');
        },
        /**
         * 악성코드 검색 및 조회 
         * 검색 결과에 따라 아코디언 목록을 hide 또는 show 
         */
        yaraRuleSearch: function() {
            var self = this;
            $('.panel-collapse .panel-body', self.el).empty();
            $("#accordion #attackTypeCount", self.el).empty();

            var yaraRuleGroupType = $("#attackTypeSelect option:selected").val();
            var all = yaraRuleGroupType == '' ? true : false;
            var ruleName = this.$("#attackNameInput").val();
            var severityLevel = parseInt($("#severityLevel", this.el).val());
            _.each(self.yaraRuleGroup, function (group) {
                if (all || yaraRuleGroupType === "" + group.groupIndex) {
                    //panel show
                    $('#panel' + group.groupIndex, self.el).show();
//                    $("a[href='#collapse" + group.groupIndex + "']").trigger("click");
                    self.getGroupGerYaraRuleList(ruleName, group.groupIndex, severityLevel);

                } else if (!all && yaraRuleGroupType !== "" + group.groupIndex) {
                    $('#panel' + group.groupIndex, self.el).hide();
                }
            });
            $('#accordion-closing', self.el).trigger("click");
        },
        /**
         * 그룹 유형에 따라 악성코드 목록 조회
         * 아코디언 헤더 클릭시 append  
         */
        getYaraRuleList: function(e) {
            var self = this;
            var groupIndex = $(e.currentTarget).data("groupindex");
            var ruleName = this.$("#attackNameInput").val();
            var severityLevel = parseInt($("#severityLevel", this.el).val());

            var id = "#collapse" + groupIndex;
            if ($(id, this.el).hasClass('in')) {
                $(id, this.el).removeClass('in');
                return false;
            }

            var subId = id + " .panel-body";
            if ($(subId, this.el).text() != '') {
                $(id, this.el).addClass('in');
                self.groupClose(groupIndex);
                return false;
            }
            self.getGroupGerYaraRuleList(ruleName, groupIndex, severityLevel);
        },
        getGroupGerYaraRuleList: function(ruleName, groupIndex, severityLevel) {
            var self = this;
            $('#collapse' + groupIndex + ' .panel-body', self.el).empty();
            $('#panel' + groupIndex + ' #attackTypeCount', self.el).empty();

            var params = {
                groupIndex: groupIndex,
                severityLevel: severityLevel
            };
            if (ruleName !== '') {
                params['ruleName'] = ruleName;
            }
            this.yaraRulePolicyCollection.fetch({
                method: 'POST',
                data: JSON.stringify(params),
                url: 'api/securityPolicy/selectYaraRuleList',
                //async: false,
                contentType: 'application/json',
                beforeSend: function() {
                    if (!$('#accordion', self.el).hasClass('loading')) {
                        Backbone.Loading.setLoading($('#accordion', self.el));
                    }
                },
                success: function(collection) {
                    self.setYaraSignaturePerGroup(collection.toJSON());
                    if (collection.length > 0) {
                        $('#panel' + groupIndex + ' #attackTypeCount', self.el).append(collection.length);
                    } else {
                        $('#panel' + groupIndex + ' #attackTypeCount', self.el).append('0');
                    }
                    $('#panel' + groupIndex + ' #attackTypeCount', self.el).digits();
                },
                complete: function () {
                    Backbone.Loading.removeLoading($('#accordion', self.el));
                }
            });
        },
        /**
         * 악성코드 전체 목록 조회
         * 모두 펼치기 버튼 클릭시 append  
         */
        getAllYaraRulePerGroup: function() {
            var self = this;

            var ruleName = this.$("#attackNameInput").val();
            var severityLevel = parseInt($("#severityLevel", this.el).val());
            var index = 0;
            var tot = this.yaraRuleGroup.length - 1;
            var endclassType = this.yaraRuleGroup[this.yaraRuleGroup.length - 1].groupIndex;
            self.groupSelect = [];
            _.each(self.yaraRuleGroup, function(group) {
                var id = "#collapse" + group.groupIndex;
                var subId = id + " .panel-body";
                if ($(subId, self.el).text() != '') {
                    self.groupSelect.push(group.groupIndex);
                    return true;
                }

                var params = {
                    //groupIndex : groupIndex,
                    groupIndex: group.groupIndex,
                    severityLevel: severityLevel
                };
                if (ruleName !== '') {
                    params['ruleName'] = ruleName;
                }
                self.yaraRulePolicyCollection.fetch({
                    method: 'POST',
                    data: JSON.stringify(params),
                    //url: 'api/securityPolicy/getYaraGroupList', 
                    url: 'api/securityPolicy/selectYaraRuleList', 	// @leekyunghee 180205 변경사항 모두 펼침 버튼 클릭시 전체 목록을 조회 
                    contentType: 'application/json',
                    beforeSend: function() {
                        if (index == 0) {
                            Backbone.Loading.setLoading($('#accordion', self.el));
                        }
                        index++;
                    },
                    success: function(collection) {
                        self.setYaraSignaturePerGroup(collection.toJSON());
                    },
                    complete: function() {
                        Backbone.Loading.removeLoading($('#accordion', self.el));
                    }
                });
            });
        },
        /**
         * 악성코드 목록 setting  
         * 아코디언 바디에 악성코드 목록을 append 
         */
        setYaraSignaturePerGroup: function(yaraRulePolicyList) {
            var self = this;
            var groupIndex;
            _.each(yaraRulePolicyList, function(target, i) {
                groupIndex = target.groupIndex;
//                if (i == 0) {
//                    $('#collapse' + target.groupIndex + ' .panel-body', self.el).empty();
//                    $('#panel' + target.groupIndex + ' #attackTypeCount', self.el).empty();
//                }
                $('#collapse' + target.groupIndex + ' .panel-body', self.el).append(self.childTemplate({
                    target: target,
                    targetName: target.ruleName
                }));

            });

            var totRow = 0;
            var checkClass;
            _.each(self.yaraRuleGroup, function(group, i) {
                if (i == 0) {
                    if (self.groupSelect.indexOf(group.groupIndex) == -1) {
                        totRow = parseInt(group.totalRowSize);
                        checkClass = group.groupIndex;
                    }
                } else {
                    if (totRow < parseInt(group.totalRowSize)) {
                        if (self.groupSelect.indexOf(group.groupIndex) == -1) {
                            totRow = parseInt(group.totalRowSize);
                            checkClass = group.groupIndex;
                        }
                    }
                }
            });
            if (groupIndex == checkClass) {
                Backbone.Loading.removeLoading($('#accordion', self.el));
            }
        },
        /**
         * 악성코드 유형 추가/삭제 
         */
        getYaraGroupList: function() {
            var self = this;
            this.yaraRulePolicyGroupCollection.fetch({
                method: 'POST',
                data: JSON.stringify({}),
                url: 'api/securityPolicy/getYaraGroupList',
                async: false,
                contentType: 'application/json',
                success: function (collection) {
                    self.setYaraGroupList(collection.toJSON());
                    self.addYaraSignatureClassType();
                    self.addYaraRuleSelectType();
                }
            });
        },
        setYaraGroupList: function(yaraRuleType) {
            var self = this;
            self.yaraRuleGroup = yaraRuleType;
            _.each(yaraRuleType, function(target) {
//                self.yaraRuleGroup.push(target);
                $("#accordion", self.el).append(self.parentTemplate({
                    target: target,
                    targetName: target.groupName,
                    totalRowSize: target.ruleTotal
                }));
            });
        },
        groupClose: function(group) {
            var self = this;
            for (var i = 0; i < self.yaraRuleGroup.length; i++) {
                if (group != self.yaraRuleGroup[i].groupIndex) {
                    var id = "#collapse" + self.yaraRuleGroup[i].groupIndex;
                    if ($(id, self.el).hasClass('in')) {
                        $(id, self.el).removeClass('in');
                    }
                }
            }
        },
        /**
         * 탭뷰 생성 
         */
        createYaraRulePolicyTab: function(e) {
            var lIndex = $(e.currentTarget).data('lindex');
            var groupIndex = $(e.currentTarget).data('groupindex');
            var ruleName = $(e.currentTarget).data('rulename');

            this.$el.find('.changeBtn').show();

            if (_.isUndefined(lIndex)) {
                $(".changeBtn", this.el).val(locale.registCompleted);
            } else {
                this.$(".changeBtn").val(locale.modify);
            }

            var tabs = [
                {title: locale.registrationInfo, viewObj: this.viewFactory('registerYaraRulePolicy', lIndex, groupIndex, ruleName), active: true, removable: false, icon: 'info', changeable: false, hidden: true},
            ];
            this.yaraRulePolicyTab = new TabView({
                el: "#tabs",
                data: tabs
            });
            this.yaraRulePolicyTab.render();
            this.currentTab = this.yaraRulePolicyTab;
            this.selTab = 'yaraRulePolicyTab';
            this.selIndex = lIndex;
            this.selRuleName = ruleName;
        },
        /**
         * 탭뷰 change event  
         */
        changeYaraRulePolicy: function() {
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
                // editable 폼 가져오기
                _.each(tabs, function (tab, i) {
                    if (tab.toggle != undefined)
                        self.currentTab.setBody(tab.toggle().el, i);
                    tab.delegateEvents();
                });
                $(".changeBtn", this.el).val(locale.modifyCompleted);
            } else {
                // insert상태가 true인지 확인이 되면  
                // tab중에 invalid한 tab이 있을 경우
                var thisView = this;
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

                            var insertIndex;
                            var groupIndex;
                            if (self.selTab == 'yaraRulePolicyTab') {
                                $.when(function () {
                                    if (tabs[0].toggle != undefined)
                                        self.currentTab.setBody(tabs[0].toggle().el, 0);
                                    insertIndex = tabs[0].getInsertIndex();
                                    groupIndex = tabs[0].getInsertGroupIndex();
                                    tabs[0].delegateEvents();
                                }()).done(function () {
                                    for (var i = 1; i < tabs.length; i++) {
                                        if (tabs[i].toggle != undefined)
                                            self.currentTab.setBody(tabs[i].toggle(insertIndex, groupIndex).el, i);
                                        tabs[i].delegateEvents();
                                    }
                                    $("#accordion", self.el).empty();
                                    thisView.getYaraGroupList();
                                    //$(".panel .panel-heading a").trigger("click");
                                    insertIndex = tabs[0].getInsertIndex();
                                    $(".lIndex" + insertIndex, thisView.el).trigger("click");
                                });
                            } else {
                                _.each(tabs, function (tab, i) {
                                    if (tab.toggle != undefined)
                                        self.currentTab.setBody(tab.toggle().el, i);
                                    tab.delegateEvents();
                                });
                                thisView.getYaraGroupList();
                            }
                            $(".changeBtn", self.el).val(locale.modify);
                        }
                    });
                }
            }
        },
        viewFactory: function(viewName, lIndex, groupIndex, ruleName) {
            if (viewName == "registerYaraRulePolicy") {
                return new RegisterYaraRulePolicy({
                    lIndex: lIndex,
                    groupIndex: groupIndex,
                    ruleName: ruleName
                });
            }
        },
        /**
         * 악성코드 유형 append
         */
        addYaraSignatureClassType: function() {
            var self = this;
            $(".signatureClassType-list", this.el).empty();
            for (var i = 0; i < self.yaraRulePolicyGroupCollection.length; i++) {
                var rowItem = new YaraRuleTypeItemView({
                    model: this.yaraRulePolicyGroupCollection.at(i),
                    editable: this.editable,
                    type: this.type
                });
                $('.signatureClassType-list', self.el).append(rowItem.render().el);
            }
        },
 
        /**
         * 사용자 정의 악성코드 유형 
         */
        addSignatureGroup: function() {
            var self = this;
            if ($("#strName", self.el).val() == "") {
                alertMessage.infoMessage(errorLocale.validation.validYaragroupName, 'info', '', 'small');
            } else {
                if (this.resultStrName == true) {
                    var ModalContent = Backbone.View.extend({
                        render: function () {
                            this.$el.html('<p class="modal-body-white-padding">' + locale.msg.isYaraRuleGroupType + '</p>');
                            return this;
                        }
                    });
                    Backbone.ModalView.msgWithOkCancelBtn({
                        title: "info",
                        body: new ModalContent(),
                        size: 'small',
                        okButtonCallback: function(e) {
                            var setParams = {
                                groupIndex: $("#nClassType", self.el).val(),
                                groupName: $("#strName", self.el).val()
                            };
                            self.yaraRulePolicyGroupCollection.set({
                                groupIndex: $("#nClassType", self.el).val(),
                                groupName: $("#strName", self.el).val()
                            });
                            self.addRowItem();
                            self.yaraRulePolicyGroupCollection.fetch({
                                method: 'POST',
                                url: 'api/securityPolicy/insertYaraGroup',
                                contentType: 'application/json',
                                dataType: 'text',
                                data: JSON.stringify(setParams),
                                async: false,
                                success: function() {
                                    // 공격유형 목록 갱신 및 입력창 초기화 
                                    //self.getAttackTypeList();
                                    //$("#nClassType", self.el).val("");
                                    $("#strName", self.el).val("");
                                    // 지우고 갱신
                                    $("#accordion", self.el).empty();
                                    // 아코디언 공격유형 목록 조회 
                                    self.getYaraGroupList();
                                    self.getYaraRuleGroupIndex();
                                    $("#newTabBtn").trigger("click");
                                    // 하위 데이터 조회 
//                                    $(".panel .panel-heading a").trigger("click");
                                }
                            });
                            alertMessage.infoMessage(locale.msg.saveYaraGroup, 'info', '', 'small');
                        }
                    });
                } else {
                    alertMessage.infoMessage(errorLocale.validation.yaraGroupName, 'info', '', 'small');
                }
            }
        },
        addRowItem: function() {
            var self = this;
            $(".signatureClassType-list", this.el).empty();
            for (var i = 0; i < self.yaraRulePolicyGroupCollection.length; i++) {
                var rowItem = new YaraRuleTypeItemView({
                    model: self.yaraRulePolicyGroupCollection.at(i)
                });
                $('.signatureClassType-list', self.el).append(rowItem.render().el);
            }
        },
        addYaraRuleSelectType: function() {
            var self = this;
            for (var i = 0; i < self.yaraRulePolicyGroupCollection.length; i++) {
                $('#attackTypeSelect', this.el).append("<option value=" + self.yaraRulePolicyGroupCollection.at(i).get("groupIndex") + ">" + self.yaraRulePolicyGroupCollection.at(i).get("groupName") + "</option>");
            }
        },
        /**
         * 그룹유형 삭제 
         */
        deleteSclassType: function(e) {
            var self = this;
            var groupIndex = $(e.currentTarget).data("lgroupindex");
            if (parseInt(groupIndex) == 99) {
                alertMessage.infoMessage(errorLocale.validation.noneDeleteDefaultUserGroup);
                return false;
            }
            var ModalContent = Backbone.View.extend({
                render: function () {
                    this.$el.html('<p class="modal-body-white-padding">' + locale.msg.isDeleteYaraRuleGroupType + '</p>');
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

                        self.yaraRulePolicyGroupCollection.fetch({
                            method: 'POST',
                            url: 'api/securityPolicy/deleteYaraRuleGroupType',
                            contentType: 'application/json',
                            dataType: 'text',
                            data: JSON.stringify({groupIndex: groupIndex}),
                            success: function() {
                                $("#accordion", self.el).empty();
                                self.getYaraGroupList();
                                self.getYaraRuleGroupIndex();
                                $("#newTabBtn").trigger("click");
//                                $(".panel .panel-heading a").trigger("click");
                            }
                        });
                        Backbone.Loading.removeLoading($('body'));

                        alertMessage.infoMessage(locale.msg.deleteYaraRuleGroup, 'info', '', 'small');

                        return true;
                    });
                }
            });
        },
        /**
         * 사용자 정의 악성코드 삭제시 팝업 
         * 삭제 후 탭 화면을 지우고 목록을 갱신하면 완료 
         */
        deleteYaraRulePolicyList: function(e) {
            var self = this;
            var lIndex = $(e.currentTarget).data("lindex");
            $(".lIndex" + lIndex).trigger("click");

            var ModalContent = Backbone.View.extend({
                render: function () {
                    this.$el.html('<p class="modal-body-white-padding">'+ locale.msg.isDeleteYaraRule +'</p>');
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

                        self.yaraRulePolicyGroupCollection.fetch({
                            method: 'POST',
                            url: 'api/securityPolicy/deleteYaraUserRule',
                            contentType: 'application/json',
                            dataType: 'text',
                            data: JSON.stringify({lIndex: self.selIndex, ruleName: self.selRuleName}),
                            success: function() {
                                self.$('.changeBtn').attr("style", "display:none");
                                self.$("#tabs").empty();
                                $("#accordion", self.el).empty();
                                self.getYaraGroupList();
//                                $(".panel .panel-heading a").trigger("click");
                            }
                        });
                        Backbone.Loading.removeLoading($('body'));

                        alertMessage.infoMessage(locale.msg.deleteYaraRule, 'info', '', 'small');

                        return true;

                    });
                }
            });
        },
        validateStrName: function() {
            var strNameValue = $("#strName").val();
            if (strNameValue == "" || strNameValue == null) {
                return this.resultStrName = Backbone.Utils.Tip.validationTooltip($('#strName'), true);
            }
            this.resultStrName = Backbone.Utils.validation.isEngAndNum(strNameValue);
            this.resultStrName = Backbone.Utils.Tip.validationTooltip($('#strName'), this.resultStrName);
        },

        /**
         * 그룹유형 인덱스 조회 
         * max + 1
         */
        getYaraRuleGroupIndex: function() {
            var self = this;
            this.model.fetch({
                method: 'POST',
                url: 'api/securityPolicy/selectYaraGroupIndex',
                contentType: 'application/json',
                data: JSON.stringify({}),
                async: false,
                success: function(model) {
                    $("#nClassType", self.el).val(model.get('lIndex'));
                }
            });
        }
    });
});