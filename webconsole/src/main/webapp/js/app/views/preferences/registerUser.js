define(function (require) {

    "use strict";

    var $ 				= require('jquery'),
        Backbone 		= require('backbone'),
        aesUtil 		= require('utils/security/AesUtil'),
        locale 			= require('i18n!nls/str'),
        errorLocale 	= require('i18n!nls/error'),
        alertMessage 	= require('utils/AlertMessage');

    var tpl 			= require('text!tpl/preferences/editUser.html');

    var AccountModel 	= require('models/preferences/accountModel');

    return Backbone.View.extend({
        editable: false,
        template: _.template([
            '<div class="icon-subtitle"><%=locale.general%> <%=locale.user%> <%=locale.basicInformation%></div>',
            '<div class="border-dotted-all overflow-hidden padding-t5">',
            '<table class="view-table col-xs-12">',
            '<tr class="border-clear">',
            '<th width="20%"><%=locale.user%> ID</th>',
            /*'<td><strong><%= model.id %></strong>',*/
            '<td><strong><span id="id"></span></strong>',
            '</td>',
            '</tr>',
            '<tr class="border-clear">',
            '<th><%=locale.encryption%></th>',
            '<td>*********</td>',
            '</tr>',
            '<% if (SYSTEM_TYPE != 2) { %><tr class="border-clear">',
            '<th>' + locale.loginAuthIp + '</th>',
            '<td><span id="loginIpList"></span></td>',
            '</tr><% } %>',
            '</table>',
            '</div>',
            '<div class="icon-subtitle"><%=locale.additionalInformation%></div>',
            '<div class="border-dotted-all overflow-hidden padding-t5 padding-b5">',
            '<table class="view-table col-xs-12">',
            '<tr class="border-clear">',
            '<th width="20%"><%=locale.name%></th>',
            /*'<td><%= model.name %></td>',*/
            '<td><span id="name"></span></td>',
            '</tr>',
            '<tr class="border-clear">',
            '<th><%=locale.affiliation%></th>',
            /*'<td><%= model.company %></td>',*/
            '<td><span id="company"></span></td>',
            '</tr>',
            '<tr class="border-clear">',
            '<th><%=locale.comment%></th>',
            /*'<td><%= model.description %></td>',*/
            '<td><span id="description"></span></td>',
            '</tr>',
            '<tr class="border-clear">',
            '<th><%=locale.telephone%></th>',
            /*'<td><%= model.telephone %></td>',*/
            '<td><span id="telephone"></span></td>',
            '</tr>',
            '<tr class="border-clear">',
            '<th><%=locale.cellphone%></th>',
            /*'<td><%= model.mobile %></td>',*/
            '<td><span id="mobile"></span></td>',
            '</tr>',
            '<tr class="border-clear">',
            '<th><%=locale.email%></th>',
            /*'<td><%= model.email %></td>',*/
            '<td><span id="email"></span></td>',
            '</tr>',
            '</table>',
            '</div>',
            '<div class="icon-subtitle"><%=locale.rangeTarget%></div>',
            '<div class="border-dotted-all overflow-hidden padding-t5 padding-b5">',
            '<table class="view-table col-xs-12">',
            '<tr class="border-clear">',
            '<th width="20%"><%=locale.classification%></th>',
            /*'<td><%= model.stationName %></td>',*/
            '<td><span id="stationName"></span></td>',
            '</tr>',
            '</table>',
            '</div>',
            /*'<div class="icon-subtitle">사용자 권한</div>',
             '<div class="border-dotted-all overflow-hidden">',
             '<table class="view-table col-xs-12">',
             '<tr class="border-clear">',
             '<td class="padding-t5">',
             '<label><input type="checkbox" value="1" <%=model.role3 %> disabled/> 일반 조회</label>',
             '<label class="margin-l30"><input type="checkbox" value="2" <%=model.role2 %> disabled/> 설정/관리</label>',
             '<label class="margin-l30"><input type="checkbox" value="4" <%=model.role1 %> disabled/> 보고서</label>',
             '</td>',
             '</tr>',
             '</table>',
             '</div>',*/
            '<div class="icon-subtitle"><%=locale.accountStatus%></div>',
            '<div class="border-dotted-all overflow-hidden padding-b5">',
            '<table class="view-table col-xs-12">',
            '<tr class="border-clear">',
            /*'<td class="padding-t5"><%= model.strLockout %></td>',*/
            '<td class="padding-t5"><span id="strLockout"></span></td>',
            '</tr>',
            '</table>',
            '</div>'
        ].join('')),
        editTemplate: _.template(tpl),
        targetView: null,
        initialize: function(options) {
            this.userIndex = options.userIndex;
            this.strId = options.strId;
            this.model = new AccountModel();
            this.loginAuthIpArr = [];
        },
        status: function() {
            return this.editable;
        },
        statusType: function() {
            return this.type;
        },
        events: {
            'change .defaultRole'		: 'changeDefaultRoleType',
            'change .settingsRole'		: 'changeSettingsRoleType',
            'change .reportRole'		: 'changeReportRoleType',
            "click #userIdCheckBtn"		: "checkDuplicateUserId",
            "click .loginIpInsertBtn"	: "loginIpAdd",
            "click .loginIpDeleteBtn"	: "loginIpDelete",
        },
        render: function() {
            if (this.userIndex != undefined) {
                this.editable = false;
                this.type = "update";
                // template에 데이터 넣는 방법 변경
//                this.getUserDetailData();
//                if (this.model.get('lockout') == 0) {
//                    this.strLockout = "로그인 허용";
//                } else {
//                    this.strLockout = "계정 잠금";
//                }
//                this.model.set({
//                    strLockout: this.strLockout
//                });
                //this.addOneData();	// 제거 
                this.$el.html(this.template({
                    model: this.model.toJSON(),
                    locale:locale
                }));
                this.getUserDetailData();

                return this;

            } else {
                this.type = "insert";
                this.editable = true;
                this.disabled = "";
                this.model.set({
                    disabled: this.disabled,
                    checked: 'checked'
                });
                // 중복검사 버튼 insert일때만 visible
                //this.getUserDetailData();		// 제거 
                //this.addOneData();			// 제거 
                this.$el.html(this.editTemplate({
                    locale: locale,
                    model: this.model.toJSON()
                }));
                $('.btnContent').show();
                this.targetView = Backbone.TargetView.MakeTarget(this.$('#targetType'));
                
                return this;
            }
        },
        renderEdit: function() {
            this.editable = true;
            this.type = "update";
            if (this.model.get('lockout') == 0) {
                this.checked = "checked";
                this.checked1 = "";
            } else if (this.model.get('lockout') == 1 || this.model.get('lockout') == 2) {
                this.checked1 = "checked";
                this.checked = "";
            }
            this.disabled = "disabled";
            this.model.set({
                checked: this.checked,
                checked1: this.checked1,
                disabled: this.disabled
            });
            //this.addOneData();	// 사용자 권한 정보 add // 제거 
            this.$el.html(this.editTemplate({
                locale: locale,
                model: this.model.toJSON()
            }));
            $('.btnContent').hide();
            this.addTargetForm();	// 기관/센서 target view add
            this.setLoginIp();
            
            return this;
        },
        getInsertIndex: function() {
            return this.userIndex;
        },
        toggle: function() {
            if (this.type == "update") {
                if (this.editable) {
                    var self = this;
                    var lnetgroupIndex, lnetworkIndex, lvsensorIndex, lsensorIndex;
                    var category, refIndex;
                    var currentTarget = this.targetView.currentSelOrgSensor();
                    $.each(currentTarget, function (key, value) {
                        if (key == "lnetgroupIndex")
                            lnetgroupIndex = value;
                        if (key == "lnetworkIndex")
                            lnetworkIndex = value;
                        if (key == "lvsensorIndex")
                            lvsensorIndex = value;
                        if (key == "lsensorIndex")
                            lsensorIndex = value;
                    });
                    if (lnetgroupIndex > 0) {
                        // 네트워크 그룹 선택시 
                        category = 1;
                        refIndex = lnetgroupIndex;
                    } else if (lnetworkIndex > 0) {
                        // 네트워크 선택시
                        category = 2;
                        refIndex = lnetworkIndex;
                    } else if (lvsensorIndex > 0) {
                        // 가상센서 선택시 
                        category = 4;
                        refIndex = lvsensorIndex;
                    } else if (lsensorIndex > 0) {
                        // 물리센서 선택시 
                        category = 3;
                        refIndex = lsensorIndex;
                    } else {
                        // 전체 선택시 
                        category = 0;
                        refIndex = 0;
                    }
                    var hex = this.$("#userId").val().hexEncode();
                    var password = "";
                    if (this.$('#password').val() != "") {
                        password = aesUtil.encrypt(hex.toUpperCase(), this.$('#password').val());
                    }
                    var setParams = {
                        userIndex: this.userIndex,
                        id: this.$("#userId").val(),
                        userIpList: this.loginAuthIpArr,
                        password: password,
                        name: this.$("#userName").val(),
                        company: this.$("#company").val(),
                        description: this.$("#description").val(),
                        telephone: this.$("#telephone").val(),
                        mobile: this.$("#mobile").val(),
                        email: this.$("#email").val(),
                        category: category,
                        refIndex: refIndex,
                        lockout: this.$("input:radio[name=lockout]:checked").val(),
                        pathName: $('#path', this.el).text(),
                    };
                    this.model.fetch({
                        method: 'POST',
                        url: 'api/preferences/updateUserAccountDetails',
                        contentType: 'application/json',
                        data: JSON.stringify(setParams),
                        async: false,
                        success: function (model) {
                        	console.log(model);
                        }
                    });
                    self.userIndex = this.userIndex;

                    return this.render();
                } else {
                    return this.renderEdit();
                }
            } else {
                if (this.editable) {
                    var self = this;
                    var lnetgroupIndex, lnetworkIndex, lvsensorIndex, lsensorIndex;
                    var category, refIndex;
                    var currentTarget = this.targetView.currentSelOrgSensor();
                    $.each(currentTarget, function (key, value) {
                        if (key == "lnetgroupIndex")
                            lnetgroupIndex = value;
                        if (key == "lnetworkIndex")
                            lnetworkIndex = value;
                        if (key == "lvsensorIndex")
                            lvsensorIndex = value;
                        if (key == "lsensorIndex")
                            lsensorIndex = value;
                    });
                    if (lnetgroupIndex > 0) {
                        // 네트워크 그룹 선택시 
                        category = 1;
                        refIndex = lnetgroupIndex;
                    } else if (lnetworkIndex > 0) {
                        // 네트워크 선택시
                        category = 2;
                        refIndex = lnetworkIndex;
                    } else if (lvsensorIndex > 0) {
                        // 가상센서 선택시 
                        category = 4;
                        refIndex = lvsensorIndex;
                    } else if (lsensorIndex > 0) {
                        // 물리센서 선택시 
                        category = 3;
                        refIndex = lsensorIndex;
                    } else {
                        // 전체 선택시 
                        category = 0;
                        refIndex = 0;
                    }
                    var hex = this.$("#userId").val().hexEncode();
                    var password = "";
                    if (this.$('#password').val() != "") {
                        password = aesUtil.encrypt(hex.toUpperCase(), this.$('#password').val());
                    }
                    var setParams = {
                        id: this.$("#userId").val(),
                        groupType: 2,
                        password: password,
                        name: this.$("#userName").val(),
                        company: this.$("#company").val(),
                        description: this.$("#description").val(),
                        telephone: this.$("#telephone").val(),
                        mobile: this.$("#mobile").val(),
                        email: this.$("#email").val(),
                        accountStatus: 0,
                        category: category,
                        refIndex: refIndex,
                        role: 1,
                        lockout: this.$("input:radio[name=lockout]:checked").val(),
                        failLogin: null,
                        tmLogin: null,
                        userIpList: this.loginAuthIpArr,
                        pathName: $('#path', this.el).text(),
                    };
                    //var setParams = $.extend(setData, this.targetView.currentSelOrgSensor());

                    this.model.fetch({
                        method: 'POST',
                        url: 'api/preferences/insertUserAccountDetails',
                        contentType: 'application/json',
                        //dataType: 'text', 
                        data: JSON.stringify(setParams),
                        async: false,
                        success: function(model) {
                            self.userIndex = model.get('userIndex');
                            self.type = "update"
                        }
                    });

                    return this.render();
                }
            }
        },
        getUserDetailData: function() {
            var self = this;
            this.model.fetch({
                method: 'POST',
                url: 'api/preferences/selectAccountDetails',
                contentType: 'application/json',
                data: JSON.stringify({'userIndex': this.userIndex}),
                async: false,
                success: function(model) {
                    //self.addOneData();		// 제거 
                    self.setUserDetailData();
                }
            });
        },
        setUserDetailData: function() {
            if (this.model.get('lockout') == 0) {
                this.strLockout = locale.loginAuth;
            } else {
                this.strLockout = locale.accountBlock;
            }
            this.model.set({
                strLockout: this.strLockout
            });
            var name = this.model.get('name') == null ? "" : this.model.get('name');
            var company = this.model.get('company') == null ? "" : _.unescape(this.model.get('company'));
            var description = this.model.get('description') == null ? "" : _.unescape(this.model.get('description'));
            var telephone = this.model.get('telephone') == null ? "" : _.unescape(this.model.get('telephone'));
            var mobile = this.model.get('mobile') == null ? "" : _.unescape(this.model.get('mobile'));
            var email = this.model.get('email') == null ? "" : _.unescape(this.model.get('email'));
            
            $('#id', this.el).text(this.model.get('id'));
            $('#name', this.el).text(name);
            $('#company', this.el).text(company);
            $('#description', this.el).text(description);
            $('#telephone', this.el).text(telephone);
            $('#mobile', this.el).text(mobile);
            $('#email', this.el).text(email);
            
//          $('#name', this.el).text(this.model.get('name'));
//          $('#company', this.el).text(_.unescape(this.model.get('company')));
//          $('#description', this.el).text(_.unescape(this.model.get('description')));
//          $('#telephone', this.el).text(this.model.get('telephone'));
//          $('#mobile', this.el).text(this.model.get('mobile'));
//          $('#email', this.el).text(this.model.get('email'));
            $('#stationName', this.el).text(this.model.get('stationName'));
            $('#strLockout', this.el).text(this.model.get('strLockout'));
            this.loginAuthIpArr = this.model.get("userIpList");
            this.setLoginIPLine();
        },
        addTargetForm: function() {
            // 수정시 변경되는 값을 저장하기 위해 셋팅  
            var targetType, lnetgroupIndex, lnetworkIndex, lvsensorIndex, lsensorIndex;
            if (this.model.get('category') == 0) {
                // 전체 
                targetType = 0, lnetgroupIndex = 0, lnetworkIndex = 0, lvsensorIndex = 0, lsensorIndex = null;
            } else if (this.model.get('category') == 2) {
                // 네트워크 
                targetType = 1, lnetgroupIndex = null, lnetworkIndex = this.model.get('refIndex'), lvsensorIndex = 0, lsensorIndex = -1;
            } 
            this.selCondition = {
                targetType: targetType,
                lnetgroupIndex: lnetgroupIndex,
                lnetworkIndex: lnetworkIndex,
                lvsensorIndex: lvsensorIndex,
                lsensorIndex: lsensorIndex
            };
            this.targetView = Backbone.TargetView.MakeTarget(this.$('#targetType'), this.selCondition);
        },
        addOneData: function() {
            var roleVal = ['', '', ''];
            if (this.model.get('role') != null) {
                var roleValToString = this.bitOrConvertor(this.model.get('role'));
                for (var j = 3 - roleValToString.length; j < 3; j++) {
                    var result = parseInt(roleValToString[j - (3 - roleValToString.length)]);
                    if (result == 0) {
                        roleVal[j] = '';
                    } else {
                        roleVal[j] = 'checked';
                    }
                }
            } else {
                this.model.set({
                    role: 0		// 3개 모두 체크가 안되어 있을때 0으로 셋팅    
                });
            }
            this.model.set({
                role1: roleVal[0], // value: 4
                role2: roleVal[1], // value: 2
                role3: roleVal[2]	 // value: 1 
            });
        },
        changeDefaultRoleType: function() {
            var value = this.$(".defaultRole").val();
            var textValue;
            var role = this.model.get('role');
            if (this.$(".defaultRole").prop('checked')) {
                textValue = 'checked';
                role = parseInt(role) + parseInt(value);
            } else {
                textValue = '';
                role = parseInt(role) - parseInt(value);
            }
            this.model.set({
                role3: textValue,
                role: role
            });
        },
        changeSettingsRoleType: function() {
            var value = this.$(".settingsRole").val();
            var textValue;
            var role = this.model.get('role');
            if (this.$(".settingsRole").prop('checked')) {
                textValue = 'checked';
                role = parseInt(role) + parseInt(value);
            } else {
                textValue = '';
                role = parseInt(role) - parseInt(value);
            }
            this.model.set({
                role2: textValue,
                role: role
            });
        },
        changeReportRoleType: function() {
            var value = this.$(".reportRole").val();
            var textValue;
            var role = this.model.get('role');
            if (this.$(".reportRole").prop('checked')) {
                textValue = 'checked';
                role = parseInt(role) + parseInt(value);
            } else {
                textValue = '';
                role = parseInt(role) - parseInt(value);
            }
            this.model.set({
                role1: textValue,
                role: role
            });
        },
        bitOrConvertor: function(data) {
            var result = parseInt(data).toString(2);
            return result;
        },
        isValid: function() {
            this.valid = true;

            var $userId = this.$('#userId');
            var $password = this.$("#password");
            var $comparePassword = this.$("#comparePassword");
            var $telephone = this.$('#telephone');
            var $mobile = this.$('#mobile');
            var $email = this.$("#email");
            var $userIpList = this.$(".loginIpSelect");
            var $userIp = this.$(".loginIpInput");

            // 입력 아이디 유효성 검사
            // 아이디 입력값 null 체크 
            this.resultUserIdVal = Backbone.Utils.validation.validateIsNull($userId.val());
            if (this.valid && this.resultUserIdVal != true) {
                this.$('#adminIdScriptRow').show();
                this.$("#adminIdScript").html("");
                this.$("#adminIdScript").html("<font color='red'>" + this.resultUserIdVal + "</font>");
                this.validationTooltip2($userId, null);
                this.valid = false;
            }
            // 아이디는 6자이상 15자이내의 영소문자 또는 영소문자, 숫자 조합이어야 하고 첫글자는 영문이어야 합니다.
            this.resultUserIdVal = Backbone.Utils.validation.idCheck($userId.val());
            if (this.valid && this.resultUserIdVal != true) {
                this.$('#adminIdScriptRow').show();
                this.$("#adminIdScript").html("");
                this.$("#adminIdScript").html("<font color='red'>" + this.resultUserIdVal + "</font>");
                this.validationTooltip2($userId, null);
                this.valid = false;
            }
            
            if (SYSTEM_TYPE == 1) {
                if (this.loginAuthIpArr.length <= 0) {
                    this.$('#userIpScriptRow').show();
                    this.$("#userIpScript").html("");
                    this.$("#userIpScript").html("<font color='red'>" + errorLocale.validation.loginIpValid + "</font>");
                    this.validationTooltip2($userIpList, null);
                    this.valid = false;
                } else {
                    this.$('#userIpScriptRow').hide();
                    this.validationTooltip2($userIpList, true);
                }
            }

            // 전화번호 입력값 null 체크
            // 필수 입력 항목이 아님 
            if ($telephone.val() != "") {
                this.resultTelephone = Backbone.Utils.validation.validateTelephone($telephone.val());
                if (this.valid && this.resultTelephone != true) {
                    this.$('#telScriptRow').show();
                    this.$("#adminTelScript").html("");
                    this.$("#adminTelScript").html("<font color='red'>" + this.resultTelephone + "</font>");
                    this.validationTooltip2($telephone, null);
                    this.valid = false;
                }
                if (this.resultTelephone == true) {
                    this.$('#telScriptRow').hide();
                    this.validationTooltip2($telephone, true);
                }
            } else {
                this.$('#telScriptRow').hide();
                this.validationTooltip2($telephone, true);
            }

            if ($mobile.val() != "") {
                this.resultMobile = Backbone.Utils.validation.validateMobile($mobile.val());
                if (this.valid && this.resultMobile != true) {
                    this.$('#mobileScriptRow').show();
                    this.$("#adminMobileScript").html("");
                    this.$("#adminMobileScript").html("<font color='red'>" + this.resultMobile + "</font>");
                    this.validationTooltip2($mobile, null);
                    this.valid = false;
                }
                if (this.resultMobile == true) {
                    this.$('#mobileScriptRow').hide();
                    this.validationTooltip2($mobile, true);
                }
            } else {
                this.$('#mobileScriptRow').hide();
                this.validationTooltip2($mobile, true);
            }

            // 이메일 입력 형식 유효성 검사
            this.resultEmailType = Backbone.Utils.validation.validateEmail($email.val());
            if ($email.val() != "" && $email.val() != null) {
                if (this.valid && this.resultEmailType != true) {
                    this.$('#emailScriptRow').show();
                    this.$("#adminEmailScript").html("");
                    this.$("#adminEmailScript").html("<font color='red'>" + this.resultEmailType + "</font>");
                    this.validationTooltip2($email, null);
                    this.valid = false;
                }
                if (this.resultEmailType == true) {
                    this.$('#emailScriptRow').hide();
                    this.validationTooltip2($email, true);
                }
            } else {
                this.$('#emailScriptRow').hide();
                this.validationTooltip2($email, true);
            }

            // 비밀번호 입력값 null 체크 
            this.passwordChkVal = Backbone.Utils.validation.validateIsNull($password.val());
            // 비밀번호 입력확인값 null 체크 
            this.rePasswordChkVal = Backbone.Utils.validation.validateIsNull($comparePassword.val());

            if (this.type != "update") {
                if (this.valid && this.passwordChkVal != true) {
                    this.$('#passwordScriptRow').show();
                    this.$("#adminPasswordScript").html("");
                    this.$("#adminPasswordScript").html("<font color='red'>" + this.passwordChkVal + "</font>");
                    this.validationTooltip2($password, null);
                    this.valid = false;
                }
                if (this.valid && this.rePasswordChkVal != true) {
                    this.$('#rePasswordScriptRow').show();
                    this.$("#adminRePasswordScript").html("");
                    this.$("#adminRePasswordScript").html("<font color='red'>" + this.rePasswordChkVal + "</font>");
                    this.validationTooltip2($comparePassword, null);
                    this.valid = false;
                }
            } else {
                if (this.passwordChkVal != true && this.rePasswordChkVal != true) {
                    this.$('#passwordScriptRow').hide();
                    this.$('#rePasswordScriptRow').hide();
                    this.validationTooltip2($password, true);
                    this.validationTooltip2($comparePassword, true);
                }
            }

            this.passwordVal = true;
            if (this.passwordChkVal == true || this.rePasswordChkVal == true) {
                this.passwordVal = Backbone.Utils.validation.validatePassword($userId.val(), $password.val(), $comparePassword.val());
            }

            if (this.valid && this.passwordVal != true) {
                if (this.passwordVal == errorLocale.validation.password.notEqualRetype) {
                    this.$('#passwordScriptRow').hide();
                    this.validationTooltip2($password, true);
                    this.$('#rePasswordScriptRow').show();
                    this.$("#adminRePasswordScript").html("");
                    this.$("#adminRePasswordScript").html("<font color='red'>" + this.passwordVal + "</font>");
                    this.validationTooltip2($comparePassword, null);
                } else {
                    this.$('#passwordScriptRow').show();
                    this.$("#adminPasswordScript").html("");
                    this.$("#adminPasswordScript").html("<font color='red'>" + this.passwordVal + "</font>");
                    this.validationTooltip2($password, null);
                }
                this.valid = false;
            }

            if (this.resultUserIdVal == true) {
                this.validationTooltip2($userId, true);
                this.$('#adminIdScriptRow').hide();
            }
            if (this.passwordChkVal == true && this.passwordVal == true) {
                this.$('#passwordScriptRow').hide();
                this.validationTooltip2($password, true);
            }
            if (this.rePasswordChkVal == true && this.passwordVal == true) {
                this.validationTooltip2($comparePassword, true);
                this.$('#rePasswordScriptRow').hide();
            }
            if (this.type != "update") {
                var self = this;
                var setParam = {
                    id: $("#userId", self.el).val()
                };
                this.model.fetch({
                    method: 'POST',
                    url: 'api/preferences/isDuplicateAccountID',
                    contentType: 'application/json',
                    data: JSON.stringify(setParam),
                    async: false,
                    success: function(model) {
                        var userId = model.get('id');
                        if (userId === null || userId === "") {
                            self.resultUserIdVal = true;
                        } else {
                            if (self.strId != self.$("#userId").val()) {
                                self.$('#adminIdScriptRow').show();
                                self.$("#adminIdScript").html("");
                                self.$("#adminIdScript").html("<font color='red'>" + errorLocale.validation.isDuplicateId + errorLocale.validation.reInput + "</font>");
                                self.validationTooltip2($userId, null);
                                self.resultUserIdVal = false;
                                self.resultStrIdVal = false;
                                self.valid = false;
                            } else {
                                self.resultUserIdVal = true;
                            }
                        }
                        //변경이 완료된 후에는 중복 검사를 다시 할 필요가 없음
                        //한번만 검사할것 
                    }
                });
            }

            if (this.resultStrIdVal == true)
                this.validationTooltip2($userId, true);

            if (this.valid == true)
                this.valid = this.isDuplicatedPassword();

            return this.valid;
        },
        isDuplicatedPassword: function() {
            var result = true;
            var hex = this.$("#userId").val().hexEncode();
            var password = "";
            if (this.$('#password').val() != "") {
                password = aesUtil.encrypt(hex.toUpperCase(), this.$('#password').val());
            }
            var setParam = {
                password: password,
                userIndex: this.userIndex,
                id: this.$("#userId").val()
            }

            if (this.$("#password").val() != '') {
                this.model.fetch({
                    method: 'POST',
                    url: 'api/preferences/isComparedPassword',
                    contentType: 'application/json',
                    data: JSON.stringify(setParam),
                    async: false,
                    success: function (data) {
                        if (data.get('changeYn') != '0') {
                            console.log("Password not collect!!!");
                            self.$('#passwordScriptRow').show();
                            self.$("#adminPasswordScript").html("");
                            self.$("#adminPasswordScript").html("<font color='red'>" + errorLocale.validation.password.invalidRenewalReinput + "</font>");
                            self.resultCheckPassword = false;
                            /*Backbone.Utils.Tip.validationTooltip(password, errorLocale.validation.password.invalidRenewalReinput);*/
                            alertMessage.infoMessage(errorLocale.validation.password.invalidRenewalReinput, 'info', '', 'small');
                            result = false;
                        }
                    }
                })
            }
            return result;
        },
        setLoginIPLine: function() {
            var thisView = this;
            $('#loginIpList', thisView.el).empty();
            for (var i = 0; i < thisView.loginAuthIpArr.length; i++) {
                $('#loginIpList', thisView.el).append(thisView.loginAuthIpArr[i] + "<br/>");
            }
        },
        setLoginIp: function() {
            var thisView = this;
            $('.loginIpSelect', thisView.el).empty();
            for (var i = 0; i < thisView.loginAuthIpArr.length; i++) {
                $('.loginIpSelect', thisView.el).append('<option value=' + thisView.loginAuthIpArr[i] + '>' + thisView.loginAuthIpArr[i] + '</option>');
            }
            this.loginInputValidate = Backbone.Utils.Tip.validationTooltip($('.loginIpInput', this.el), true);
        },
        loginIpAdd: function() {
            var loginAuthIp = $('.loginIpInput', this.el).val().trim();
            if (loginAuthIp == null || loginAuthIp == '') {
                alertMessage.infoMessage(errorLocale.validation.inputIpNullValid, 'info', '', 'small');
                return false;
            }
            this.loginInputValidate = Backbone.Utils.validation.validateIpDualCheck($('.loginIpInput', this.el).val());
            if (this.loginInputValidate != true) {
                Backbone.Utils.Tip.validationTooltip($('.loginIpInput', this.el), this.loginInputValidate);
                return false;
            } else {
                this.validationTooltip2($('.loginIpSelect', this.el), true);
                this.$('#userIpScriptRow').hide();
            }
            if (this.loginAuthIpArr.length >= LOGIN_IP_COUNT) {
                alertMessage.infoMessage(errorLocale.validation.authIpCount + LOGIN_IP_COUNT + errorLocale.validation.isCount, 'info', '', 'small');
                return false;
            }
            if (this.loginAuthIpArr.length > 0) {
                var index = this.loginAuthIpArr.indexOf(loginAuthIp);
                if (index >= 0) {
                	alertMessage.infoMessage(errorLocale.validation.duplicateIpMsg + errorLocale.validation.reInput, 'info', '', 'small');
                    return false;
                }
            }
            this.loginAuthIpArr.push(loginAuthIp);
            this.setLoginIp();
            $('.loginIpInput', this.el).val('');
        },
        loginIpDelete: function() {
            var selIp = String($('.loginIpSelect', this.el).val());
            var index = this.loginAuthIpArr.indexOf(selIp);
            this.loginAuthIpArr.splice(index, index + 1);
            this.setLoginIp();
        },
        validationTooltip2: function(attr, msg) {
            var selector = $(attr.selector, this.el);
            var $control = selector.closest('.form-control', this.el);
            $control.removeAttr('data-toggle');
            $control.removeClass('validation-error');

            if (msg == null) {
                $control.attr('data-toggle', 'tooltip');
                $control.addClass('validation-error');
                return false;
            }

            return true;
        }
    });
});