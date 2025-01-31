define(function (require) {

    "use strict";

    var $ 				= require('jquery'),
        Backbone 		= require('backbone'),
        alertMessage 	= require('utils/AlertMessage'),
        errorLocale 	= require('i18n!nls/error'),
        locale 			= require('i18n!nls/str');

    var AccountGroupModel 	= require('models/preferences/accountModel'),
        AccountGroupItem 	= require('views/preferences/userListItem');

    var AccountCollection 		= require('collections/preferences/accountCollection'),
        AccountGroupCollection 	= require('collections/preferences/accountGroupCollection');

    return Backbone.View.extend({
        editable: false,
        template: _.template([
            '<div class="icon-subtitle"><%= locale.basicInformation %></div>',
            '<div class="border-dotted-all overflow-hidden padding-t5">',
            '<table class="view-table col-xs-12">',
            '<tr class="border-clear">',
            '<th width="20%"><%= locale.accountGroupName %></th>',
            /*'<td><strong><%= model.strName %></strong></td>',*/
            '<td><strong><span id="strName"></span></strong></td>',
            '</tr>',
            '<tr class="border-clear">',
            '<th><%= locale.affiliation %> <%= locale.account %></th>',
            '<td>',
            '<div class="col-lg-12 col-md-12 col-xs-12 padding-clear">',
            '<table class="table table-vertical-line table-striped border-solid-top">',
            '<thead>',
            '<tr>',
            '<th class="align-center">ID</th>',
            '</tr>',
            '</thead>',
            '<tbody id="userGroupList">',
            '</tbody>',
            '</table>',
            '</div>',
            '</td>',
            '</tr>',
            '</table>',
            '</div>'
        ].join('')),
        templateForm: _.template([
            '<div class="icon-subtitle"><%= locale.basicInformation %><span class="pull-right">* <%= locale.required %></span></div>',
            '<div class="border-dotted-all overflow-hidden padding-t5">',
            '<table class="view-table col-xs-12">',
            '<tr class="border-clear">',
            '<th width="20%"><%= locale.accountGroupName %> *</th>',
            '<td><input type="text" id="strGroupName" class="form-control col-xs-4 strGroupName" value="<%= model.strName %>" maxlength="32"/></td>',
            '</tr>',
            '<tr class="border-clear">',
            '<th><%= locale.affiliation %> <%= locale.account %> *</th>',
            '<td class="align-right">',
            '<div class="col-lg-12 col-md-12 col-xs-12 padding-clear">',
            '<table class="table table-vertical-line table-striped border-solid-top">',
            '<thead>',
            '<tr>',
            '<th class="align-center">ID</th>',
            '</tr>',
            '</thead>',
            '<tbody id="userGroupList">',
            '</tbody>',
            '</table>',
            '</div>',
            '</td>',
            '</tr>',
            '</table>',
            '</div>'
        ].join('')),
        initialize: function (options) {
            this.lIndex = options.lIndex;
            this.strId = options.strId;
            this.model = new AccountGroupModel();
            this.collection = new AccountCollection();
            this.accountGroupcollection = new AccountGroupCollection();
        },
        status: function () {
            return this.editable;
        },
        statusType: function () {
            return this.type;
        },
        render: function () {
            if (this.lIndex != undefined) {
                this.type = "update";
                this.editable = false;
                this.getGroupDetailList();
                this.$el.html(this.template({
                    model: this.model.toJSON(),
                    locale:locale
                }));
                this.getGroupUserData();
                return this;
            } else {
                this.type = "insert";
                this.editable = true;
                this.$el.html(this.templateForm({
                    model: this.model.toJSON(),
                    locale:locale
                }));
                this.getGroupUserData();
                return this;
            }
        },
        renderEdit: function () {
            this.editable = true;
            this.type = "update";
            this.getGroupDetailList();
            this.$el.html(this.templateForm({
            	locale: locale,
                model: this.model.toJSON()
            }));
            this.getGroupUserData();
            return this;
        },
        toggle: function () {
            if (this.editable) {
                this.mergeAccountGroup(this.type); // update | insert
                return this.render();
            } else {
                return this.renderEdit();
            }
        },
        getInsertIndex: function () {
            return this.lIndex;
        },
        makeParams: function (action) {
            var userList = [];
            var indexs = _.pluck(_.where(this.collection.toJSON(), {checked: true}), 'userIndex');
            for (var i = 0; i < indexs.length; i++) {
                userList.push({
                    strAccIndex: indexs[i]
                });
            }
            var setParams = {
                strName: $("#strGroupName", this.el).val(),
                nacIndexCount: 1,
                userList: userList
            };
            if (action === 'update') {
                setParams.lIndex = this.lIndex;
            }

            return setParams;
        },
        getActionUrl: function (action) {
            return action === 'update' ? 'api/preferences/updateGroupAccountDetails' : 'api/preferences/insertGroupAccountDetails';
        },
        mergeAccountGroup: function (action) {
            var self = this;
            var type;
            if (action == 'update') {
                type: 'text';
            } else {
                type: '';
            }
            var url = this.getActionUrl(action);
            var params = this.makeParams(action);
            this.model.fetch({
                method: 'POST',
                url: url,
                contentType: 'application/json',
                dataType: type, // insert일 경우 제거  
                data: JSON.stringify(params),
                async: false,
                success: function (model) {
                    if (action == 'insert') {
                        self.lIndex = model.get('lIndex');
                    }
                    // 조회 화면 갱신
                    // 조회 에러로 주석처리  
                    //self.getGroupDetailList();
                }
            });
        },
        // 그룹에 포함될 사용자 목록 조회
        getGroupUserData: function () {
            var self = this;
            self.collection.fetch({
                method: 'POST',
                data: JSON.stringify({}),
                url: 'api/preferences/selectAccountList',
                contentType: 'application/json',
                success: function (collection) {
                    self.addOneUser('select');
                }
            });
        },
        addOneUser: function (type) {
            var self = this;

            var strAccIndex = self.model.get('strAccIndex');
            var templateType = this.editable ? 'edit' : 'info';
            var replaceAccIndex = (strAccIndex && strAccIndex.split('|')) || [];

            for (var i = 0; i < self.collection.length; i++) {
                for (var j = 0; j < replaceAccIndex.length; j++) {
                    if (replaceAccIndex[j] == self.collection.at(i).get('userIndex')) {
                        self.collection.at(i).set({checked: true});		//  model에 set해줘야함
                        break;
                    }
                }
                if (self.collection.at(i).get('groupType') == 2) {
                    var accountGroupItem = new AccountGroupItem({
                        model: this.collection.at(i),
                        type: templateType
                    });
                    $("#userGroupList", self.el).append(accountGroupItem.render().el);
                }
            }
            $("#strName", self.el).text(_.unescape(this.model.get('strName')));

        },
        getGroupDetailList: function () {
            this.model.fetch({
                method: 'POST',
                url: 'api/preferences/selectGroupDetailList',
                contentType: 'application/json',
                data: JSON.stringify({lIndex: this.lIndex}),
                async: false,
                success: function (model) {
                	console.log(model);
                }
            });
        },
        isValid: function () {
            this.valid = true;

            // 그룹명 입력 체크 
            this.resultStrNameVal = Backbone.Utils.validation.validateIsNull($('#strGroupName', this.el).val());
            if (this.valid && this.resultStrNameVal != true) {
                alertMessage.infoMessage(this.resultStrNameVal, "warn");
                Backbone.Utils.Tip.validationTooltip($('#strGroupName', this.el), this.resultStrNameVal);
                this.valid = false;
                return false;
            }

            // 사용자 계정을 선택하지 않으면 등록되지 않음 체크 
            this.resultCheckVal = $("input[name='userCheckBox']:checked", this.el).val();
            if (this.resultCheckVal == undefined) {
                if ($('#strGroupName', this.el).val() != null || $("#strGroupName", this.el).val() != "") {
                    alertMessage.infoMessage(errorLocale.validation.validAccountSelected, 'warn', '', 'small');
                }
                Backbone.Utils.Tip.validationTooltip($("input[name='userCheckBox']", this.el), this.resultCheckVal);
                this.valid = false;
                return false;
            }

            var strNameLengthCheck = Backbone.Utils.validation.strByteCheck($('#strGroupName', this.el).val(), 32);
            if (strNameLengthCheck != true) {
                alertMessage.infoMessage(errorLocale.validation.names + strNameLengthCheck, 'warn', '', 'small');
                self.resultStrNameVal = Backbone.Utils.Tip.validationTooltip($('#strGroupName'), errorLocale.validation.reInputValue);
                this.valid = false;
                return this.valid;
            }

            if (this.resultUserIdVal == true)
                Backbone.Utils.Tip.validationTooltip($('.strGroupName'), true);
            if (this.resultStrNameVal == true)
                Backbone.Utils.Tip.validationTooltip($('#strGroupName', this.el), true);
            if (this.resultCheckVal == true)
                Backbone.Utils.Tip.validationTooltip($("input[name='userCheckBox']", this.el), true);
            if (this.resultCheckVal == true)
                Backbone.Utils.Tip.validationTooltip($("input[name='userCheckBox']", this.el), true);

            var self = this;
            var setParam = {
                id: $(".strGroupName", self.el).val()
            };
            var self = this;
            this.model.fetch({
                method: 'POST',
                url: 'api/preferences/isDuplicateGroupName',
                contentType: 'application/json',
                data: JSON.stringify(setParam),
                async: false,
                success: function (model) {
                    var userId = model.get('id');
                    if (userId === null || userId === "") {
                        self.resultUserIdVal = true;
                    } else {
                        if (self.strId != self.$(".strGroupName").val()) {
                            self.resultUserIdVal = Backbone.Utils.Tip.validationTooltip($('.strGroupName'), errorLocale.validation.isDuplicateGroupName + errorLocale.validation.reInput);
                            self.resultUserIdVal = false;
                            self.valid = false;
                        } else {
                            self.resultUserIdVal = true;
                        }
                    }
                }
            });

            return this.valid;
        }
    });
});