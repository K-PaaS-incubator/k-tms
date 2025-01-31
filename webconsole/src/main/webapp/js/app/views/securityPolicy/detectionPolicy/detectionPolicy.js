/**
 * @date 2016-01-20
 * @author leekyunghee
 * @description 탐지정책 유형그룹별 조회 최상위 view
 */
define(function (require) {

    "use strict";

    var $ = require('jquery'),
            Backbone = require('backbone'),
            locale = require('i18n!nls/str'),
            errorLocale = require('i18n!nls/error'),
            alertMessage = require('utils/AlertMessage'),
            dataExpression = require('utils/dataExpression'),
            tpl = require('text!tpl/securityPolicy/detectionPolicy.html'),
            TabView = require('views/common/tab'),
            RegisterDetectionPolicy = require('views/securityPolicy/detectionPolicy/registerDetectionPolicy'),
            DetectionPolicyHelp = require('views/securityPolicy/detectionPolicy/detectionPolicyHelp'),
            RowItemView = require('views/securityPolicy/detectionPolicy/signatureClassTypeItem'),
            DetectionPolicyFileRead = require('views/popup/detectionPolicyFileReader');

    var DetectionPolicyCollection = require('collections/securityPolicy/detectionPolicyCollection'),
            DetectionPolicyModel = require('models/securityPolicy/detectionPolicyModel'),
            AttackTypeCollection = require('collections/securityPolicy/attackTypeCollection'),
            SignatureClassTypeCollection = require('collections/securityPolicy/signatureClassTypeCollection'),
            CSVExport = require('views/common/csvExport'), // 내보내기 기능 추가
            DetectionExportPolicyCollection = require('collections/securityPolicy/detectionPolicyCollection');

    return Backbone.View.extend({
        resultlThresholdNum: true,
        resultlThresholdTime: true,
        template: _.template(tpl),
        formEl: "form.detectionPolicyForm",
        detectionPolicyTab: null,
        currentTab: '',
        selTab: '',
        selIndex: '',
        parentTemplate: _.template([
            '<div class="panel panel-default panel-dp-selected" id="panel<%= target.nClassType %>">',
            '<div class="panel-heading">',
            '<h4 class="panel-title">',
            '<a id="<%= target.nClassType %>" data-toggle="collapse" data-parent="#accordion" href="#collapse<%= target.nClassType %>" aria-expanded="true" aria-controls="collapse" class="<%= target.strName %>" data-nclasstype="<%= target.nClassType %>" style="text-decoration:none"> <%= target.strName %> <span id="attackTypeCount" class="badge pull-right"><%=totalRowSize%></span>',
            '</a>',
            '</h4>',
            '</div>',
            '<div id="collapse<%= target.nClassType %>" class="panel-collapse collapse" role="tabpanel" aria-labelledby="collapse<%= target.nClassType %>">',
            '<div class="panel-body">',
            '</div>',
            '</div>'
        ].join('')),
        childTemplate: _.template([
            '<a href="#securityPolicy_intrusionDetectionPolicy" class="overflow-ellipsis <% if(target.lCode >= 1000000) {%> col-xs-11 lCode<%= target.lCode %> <%}%>" data-lcode="<%= target.lCode %>" data-attackname="<%= target.strDescription %>" data-sclasstype="<%= target.sClassType %>" style="text-decoration:none;">',
            '<%= target.strDescription %>',
            '<% if (target.lCode >= 1000000) { %>',
            '<% if (WRITE_MODE == 1) { %>',
            '<a class="icon-del <% if(target.lCode >= 1000000) {%> col-xs-1 deleteBtn <%}%>" data-lcode="<%= target.lCode %>" >',
            '</a>',
            '<% } %>',
            '<% } %>',
            '</a>'
        ].join('')),
        initialize: function () {
            this.detectionPolicyCollection = new DetectionPolicyCollection();
            this.attackTypeCollection = new AttackTypeCollection();
            this.signatureClassTypeCollection = new SignatureClassTypeCollection();
            this.detectionExportPolicyCollection = new DetectionExportPolicyCollection();
            this.groupSelect = [];
            this.detectionPolicyModel = new DetectionPolicyModel();
            this.resultlThresholdNum = true;
            this.resultlThresholdTime = true;
        },
        events: {
            "click #attackSearchBtn": "search", // 검색 버튼 클릭
            "click .panel-body > a": "createDetectionPolicyTab", // 상세정보 화면
            "click #newTabBtn": "createDetectionPolicyTab", // 신규등록 화면
            "click .changeBtn": "changeDetectionPolicy", // 생성된 탭 상세보기의 변경 처리 이벤트 
            "click .deleteBtn": "deleteDetectionPolicyList", // 선택된 목록에서 삭제 
            "click .panel .panel-heading a": "getPolicySignature", // 목록 선택에 따라 유형그룹에 해당하는 시그니처를 조회해온다.
            "click .addUserSignatureBtn": "addSignatureGroup", // 그룹유형 신규등록
            "click #deleteSclassTypeBtn": "deleteSclassType", // 그룹유형 삭제
            "click #accordion-opening": "showAccordion", // 모두펼치기 버튼
            "click #accordion-closing": "closeAccordion", // 모두닫기 버튼
            "click #cancelBtn": "settings", // 검색 조건 초기화
            "keydown #thresholdNumInput": "numberInput",
            "keydown #thresholdTimeInput": "numberInput",
            "blur #thresholdNumInput": "validatelThresholdNum",
            "blur #thresholdTimeInput": "validatelThresholdTime",
        },
        render: function () {
            this.$el.html(this.template({
                locale: locale
            }));

            // 그룹유형 조회 
            this.getSignatureGroup();
            // 그룹유형 삭제를 위해 목록 조회 
            this.getSignatureClassType();
            // 간편 검색 기능 추가 
            this.getAttackTypeList();
            // 최초 조회 시 보여질 화면 
            $("#newTabBtn").trigger("click");
            // 유형별 시그니처 전체 개수 표시를 위해 이벤트 trigger

//			$(".panel .panel-heading a").trigger("click");
            //$("#accordion-closing").trigger("click");
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
        showAccordion: function () {
            var self = this;
            self.getAllSignaturePerGroup();
//              $(".panel .panel-heading a").trigger("click");
            _.each(self.signatureGroup, function (group) {
                $('#collapse' + group.nClassType, self.el).addClass('in');
            });
        },
        closeAccordion: function () {
            var self = this;
            _.each(self.signatureGroup, function (group) {
                $('#collapse' + group.nClassType, self.el).removeClass('in');
            });
        },
        settings: function () {
            Backbone.FormSerialize.setData($(this.formEl));
            $('#severityLevel', this.el).val('').attr('selected', 'selected');
            $('#protocol', this.el).val('').attr('selected', 'selected');
            $('#lResponseSelect', this.el).val('131073').attr('selected', 'selected');
            $('#attackTypeSelect', this.el).val('').attr('selected', 'selected');
        },
        search: function () {
            var self = this;
            if (self.resultlThresholdNum != true && self.resultlThresholdTime != true) {
                return false;
            }
            
            if ($("#attackTypeSelect option:selected").val() == '' && $("#attackNameInput").val() == '' && $("#severityLevel option:selected").val() == '' 
                    && $("#protocol option:selected").val() == '' && $("#thresholdNumInput").val() == '' && $("#thresholdTimeInput").val() == '') {
                alertMessage.infoMessage("검색조건을 선택/입력 하세요.", "info");
                return false;
            }
            // 초기화 
            $('.panel-collapse .panel-body', self.el).empty();
            $("#accordion #attackTypeCount", self.el).empty();
            // 검색 결과가 없을 경우 0처리
            $("#accordion #attackTypeCount", self.el).text(0);

            var signatureGroupType = $("#attackTypeSelect option:selected").val();
            var all = signatureGroupType == '' ? true : false;
            //2018.03.15 해당 카테고리에서만 조회되도록 수정
            if(all){
            	for (var i = 0; i < self.signatureGroup.length; i++) {
            		this.getCodePolicySignature(self.signatureGroup[i].nClassType);
            	}
            }
            else {
            	this.getCodePolicySignature(signatureGroupType);
            }
            //end
            
            $('.detectionPolicyEdit', this.el).trigger('click');
            $('#accordion-closing', self.el).trigger("click");
        },
        signatureGroup: [],
        //signatureByGroup: {},
        getSignatureGroup: function () {
            var self = this;
            this.attackTypeCollection.fetch({
                method: 'POST',
                data: JSON.stringify({}),
                url: 'api/securityPolicy/selectAttackTypeSelect',
                async: false,
                contentType: 'application/json',
                success: function (collection) {
                    self.setSignatureGroup(collection.toJSON());
                }
            });
        },
        /**
         * 아코디언헤더 영역 : 그룹유형 카테고리  
         */
        setSignatureGroup: function (attackTypeList) {
            var self = this;
            self.signatureGroup = [];
            _.each(attackTypeList, function (target) {
                if (target.nClassType != 5) {
                    self.signatureGroup.push(target);
                    $("#accordion", self.el).append(self.parentTemplate({
                        target: target,
                        targetName: target.strName,
                        totalRowSize: dataExpression.numberFormat2(target.totalRowSize)
                    }));
                }
            });
//				$("#accordion #attackTypeCount", self.el).append(0);
        },
        makeSearchParams: function (nClassType) {
            var severityLevel = parseInt($("#severityLevel", this.el).val());
            var protocol = $("#protocol option:selected").val();
            var thresholdNum = this.$("#thresholdNumInput").val();
            var thresholdTime = this.$("#thresholdTimeInput").val();
            var lResponseSelect = this.$("#lResponseSelect option:selected").val();
            var blockSelect = this.$("#blockSelect option:selected").val();
            var cveId = this.$("#cveId").val();
            var attackType = this.$("#attackNameInput").val();

            var params = {
                nClassType: nClassType,
                severityLevel: severityLevel,
                protocol: protocol,
                thresholdNum: thresholdNum,
                thresholdTime: thresholdTime,
                lResponseSelect: lResponseSelect,
                blockSelect: blockSelect,
                cveId: cveId
            };
            if (attackType !== '') {
                params['attackNameInput'] = attackType;
            }

            return params;
        },
        getCodePolicySignature: function (nClassType) {
            var params = this.makeSearchParams(nClassType);
            var self = this;
            self.detectionPolicyCollection.fetch({
                method: 'POST',
                data: JSON.stringify(params),
                url: 'api/securityPolicy/selectSignaturePerGroup',
                contentType: 'application/json',
                beforeSend: function () {
                    if (!$('#accordion', self.el).hasClass('loading')) {
                        Backbone.Loading.setLoading($('#accordion', self.el));
                    }
                },
                success: function (collection) {
                    self.setSignaturePerGroup(collection.toJSON());
                },
                complete: function () {
                    Backbone.Loading.removeLoading($('#accordion', self.el));
                }
            });
        },
        groupClose: function (group) {
            var self = this;
            for (var i = 0; i < self.signatureGroup.length; i++) {
                if (group != self.signatureGroup[i].nClassType) {
                    var id = "#collapse" + self.signatureGroup[i].nClassType;
                    if ($(id, self.el).hasClass('in')) {
                        $(id, self.el).removeClass('in');
                    }
                }
            }
        },
        /**
         * 아코디언 헤더를 클릭시 상세 목록을 조회한다.
         */
        getPolicySignature: function (e) {
            var self = this;
            var nClassType = $(e.currentTarget).data("nclasstype");

            var id = "#collapse" + nClassType;
            if ($(id, this.el).hasClass('in')) {
                $(id, this.el).removeClass('in');
                return false;
            }

            var subId = id + " .panel-body";
            if ($(subId, this.el).text() != '') {
                $(id, this.el).addClass('in');
                self.groupClose(nClassType);
                return false;
            }

            //var params = { nClassType: nClassType };
            var params = this.makeSearchParams(nClassType);
            self.detectionPolicyCollection.fetch({
                method: 'POST',
                data: JSON.stringify(params),
                url: 'api/securityPolicy/selectSignaturePerGroup',
                contentType: 'application/json',
                beforeSend: function () {
                    Backbone.Loading.setLoading($('#accordion', self.el));
                },
                success: function (collection) {
                    //var sigList = _.clone(collection);
                    //self.signatureByGroup[nClassType] = sigList.length > 0 ? sigList : [];
                    self.setSignaturePerGroup(collection.toJSON());
                    //$("#accordion-closing", self.el).trigger("click");
                },
                complete: function () {
                    Backbone.Loading.removeLoading($('#accordion', self.el));
                }
            });
        },
        getAllSignaturePerGroup: function () {
            var self = this;

            var severityLevel = parseInt($("#severityLevel", this.el).val());
            var protocol = $("#protocol option:selected").val();
            var thresholdNum = this.$("#thresholdNumInput").val();
            var thresholdTime = this.$("#thresholdTimeInput").val();
            var lResponseSelect = this.$("#lResponseSelect option:selected").val();
            var blockSelect = this.$("#blockSelect option:selected").val();
            var cveId = this.$("#cveId").val();
            var attackType = this.$("#attackNameInput").val();
            var index = 0;
            var tot = this.signatureGroup.length - 1;
            var endclassType = this.signatureGroup[this.signatureGroup.length - 1].nClassType;
            self.groupSelect = [];
            _.each(self.signatureGroup, function (group) {
                var id = "#collapse" + group.nClassType;
                var subId = id + " .panel-body";
                if ($(subId, self.el).text() != '') {
                    self.groupSelect.push(group.nClassType);
                    return true;
                }

                var params = {
                    nClassType: group.nClassType,
                    severityLevel: severityLevel,
                    protocol: protocol,
                    thresholdNum: thresholdNum,
                    thresholdTime: thresholdTime,
                    lResponseSelect: lResponseSelect,
                    blockSelect: blockSelect,
                    cveId: cveId
                };
                if (attackType !== '') {
                    params['attackNameInput'] = attackType;
                }
                self.detectionPolicyCollection.fetch({
                    method: 'POST',
                    data: JSON.stringify(params),
                    url: 'api/securityPolicy/selectSignaturePerGroup',
                    contentType: 'application/json',
                    beforeSend: function () {
                        if (index == 0) {
                            Backbone.Loading.setLoading($('#accordion', self.el));
                        }
                        index++;
                    },
                    success: function (collection) {
                        self.setSignaturePerGroup(collection.toJSON());
                    },
                    complete: function () {
                        Backbone.Loading.removeLoading($('#accordion', self.el));
                    }
                });
            });
        },
        /**
         * 아코디언바디 영역 : 벤더룰, 사용자정의 룰 
         */
        setSignaturePerGroup: function (detectionPolicyList) {
            var self = this;
            var sClassType;
            _.each(detectionPolicyList, function (target, i) {

                //$('#panel'+target.sClassType+' #attackTypeCount', self.el).empty();
                sClassType = target.sClassType;
                if (i == 0) {
                    $('#collapse' + target.sClassType + ' .panel-body', self.el).empty();
                    $('#panel' + target.sClassType + ' #attackTypeCount', self.el).empty();
                }
                $('#collapse' + target.sClassType + ' .panel-body', self.el).append(self.childTemplate({
                    target: target,
                    targetName: target.strDescription
                }));

                $('#panel' + target.sClassType + ' #attackTypeCount', self.el).empty();
                $('#panel' + target.sClassType + ' #attackTypeCount', self.el).append(detectionPolicyList.length);

                $('#panel' + target.sClassType + ' #attackTypeCount', self.el).digits();

            });
            var totRow = 0;
            var checkClass;
            _.each(self.signatureGroup, function (group, i) {
                if (i == 0) {
                    if (self.groupSelect.indexOf(group.nClassType) == -1) {
                        totRow = parseInt(group.totalRowSize);
                        checkClass = group.nClassType;
                    }
                } else {
                    if (totRow < parseInt(group.totalRowSize)) {
                        if (self.groupSelect.indexOf(group.nClassType) == -1) {
                            totRow = parseInt(group.totalRowSize);
                            checkClass = group.nClassType;
                        }
                    }
                }
            });
            if (sClassType == checkClass) {
                Backbone.Loading.removeLoading($('#accordion', self.el));
            }
        },
        /**         
         * 신규생성 탭, 상세정보 탭 
         */
        createDetectionPolicyTab: function (e) {
            // 등록정보와 배포대상 변경은 일괄 인지 확인
            // 목록 클릭시 new탭을 생성하고 상세 정보를 가져온다. 
            var lCode = $(e.currentTarget).data('lcode');
            var sClassType = $(e.currentTarget).data('sclasstype');
            // 공격명 중복검사를 위해 현재 목록에 가지고 있는 정보를 알고 있어야함.
            var attackName = $(e.currentTarget).data('attackname');
            var tabs = [
                {title: locale.registInfo, viewObj: this.viewFactory('registerDetectionPolicy', lCode, sClassType, attackName), active: true, removable: false, icon: 'info', changeable: false, hidden: true},
                {title: locale.helpMsg, viewObj: this.viewFactory('detectionPolicyHelp', lCode, sClassType, attackName), active: false, removable: false, icon: 'help', changeable: false, hidden: true},
            ];
            this.$el.find('.changeBtn').show();
            if (lCode == undefined) {
                this.$(".changeBtn").val(locale.registCompleted);
            } else {
                this.$(".changeBtn").val(locale.modify);
            }
            this.detectionPolicyTab = new TabView({
                el: "#tabs",
                data: tabs
            });
            this.detectionPolicyTab.render();
            this.currentTab = this.detectionPolicyTab;

            this.selTab = 'detectionPolicy';
            this.selIndex = lCode;
        },
        changeDetectionPolicy: function () {
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
            if (status == false) { //수정 폼 가져오기
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

                            Backbone.Loading.setLoading($('body'));

                            setTimeout(function () {
                                var insertIndex;
                                if (self.selTab == 'detectionPolicy') {
                                    $.when(function () {
                                        if (tabs[0].toggle != undefined)
                                            self.currentTab.setBody(tabs[0].toggle().el, 0);
                                        insertIndex = tabs[0].getInsertIndex();
                                        tabs[0].delegateEvents();
                                    }()).done(function () {
                                        for (var i = 1; i < tabs.length; i++) {
                                            if (tabs[i].toggle != undefined)
                                                self.currentTab.setBody(tabs[i].toggle(insertIndex).el, i);
                                            tabs[i].delegateEvents();
                                        }
                                        $("#accordion", self.el).empty();
                                        thisView.getSignatureGroup();
//											$(".panel .panel-heading a").trigger("click");
                                    });
                                } else {
                                    _.each(tabs, function (tab, i) {
                                        if (tab.toggle != undefined)
                                            self.currentTab.setBody(tab.toggle().el, i);
                                        tab.delegateEvents();
                                    });
                                    thisView.getSignatureGroup();
                                }
                                $(".changeBtn", self.el).val(locale.modify);

                                Backbone.Loading.removeLoading($('body'));

                                alertMessage.infoMessage(locale.msg.saveMsg, 'info', '', 'small');

                                setTimeout(function () {
                                    $(".lCode" + insertIndex["lCode"], self.el).trigger("click");
                                }, 5000);
                            });

                        }
                    });
                }
            }
        },
        deleteDetectionPolicyList: function (e) {
            var self = this;
            var ModalContent = Backbone.View.extend({
                render: function () {
                    this.$el.html('<p class="modal-body-white-padding">' + locale.msg.isDeleteDetectionList + ' </p>');
                    return this;
                }
            });
            Backbone.ModalView.msgWithOkCancelBtn({
                title: "info",
                body: new ModalContent(),
                size: 'small',
                okButtonCallback: function (e) {

                    Backbone.Loading.setLoading($('body'));

                    setTimeout(function () {

                        self.detectionPolicyCollection.fetch({
                            method: 'POST',
                            url: 'api/securityPolicy/deleteUserSignature',
                            contentType: 'application/json',
                            dataType: 'text',
                            data: JSON.stringify({lCode: self.selIndex}),
                            success: function () {
                                self.$('.changeBtn').attr("style", "display:none");
                                self.$("#tabs").empty();
                                $("#accordion", self.el).empty();
                                self.getSignatureGroup();
//								$(".panel .panel-heading a").trigger("click");
                                $("#newTabBtn").trigger("click");
                            }
                        });
                        Backbone.Loading.removeLoading($('body'));

                        alertMessage.infoMessage(locale.msg.deleteDetection, 'info', '', 'small');

                        return true;

                    });
                }
            });
        },
        /**
         * 탭 컨텐츠 뷰 객체 생성
         */
        viewFactory: function (viewName, lCode, sClassType, attackName) {
            if (viewName == "registerDetectionPolicy") {
                return new RegisterDetectionPolicy({
                    lCode: lCode,
                    sClassType: sClassType,
                    attackName: attackName
                });
            } else if (viewName == "detectionPolicyHelp") {
                return new DetectionPolicyHelp({
                    lCode: lCode,
                    attackName: attackName
                });
            }
        },
        // 사용자 정의 그룹 유형
        addSignatureGroup: function () {
            var self = this;
//            var classType = $("#nClassType", self.el).val();
//            var classTypeInteger = Backbone.Utils.validation.validateIsNumber(classType);
//            if (classTypeInteger != true) {
//                alertMessage.infoMessage(classTypeInteger, 'info', '', 'small');
//                return false;
//            }
//            
//            if ($("#nClassType", self.el).val().empty() == "") {
//                alertMessage.infoMessage('코드를 입력하십시오.', 'info', '', 'small');
//                return false;
//            }
            if ($("#strName", self.el).val() == "") {
                alertMessage.infoMessage(errorLocale.validation.validDetectiongroupName, 'info', '', 'small');
                return false;
            } else {
                var nameCheck = Backbone.Utils.validation.isEngAndNum($("#strName", self.el).val());
                if (nameCheck != true) {
                    alertMessage.infoMessage(errorLocale.validation.detectionGroupName, 'info', '', 'small');
                    return false;
                }
            }
            var setParams = {
                strName: $("#strName", self.el).val()
            };
            Backbone.ajax({
                method: 'POST',
                contentType: 'application/json',
                url: 'api/securityPolicy/isDuplicateSignatureClassTypeName',
                data: JSON.stringify(setParams),
                async: true,
                cache: true,
                success: function (data) {
                    if (data) {
                        alertMessage.infoMessage('"' + $("#strName", self.el).val() + '"' +errorLocale.validation.isDeplicateName + errorLocale.validation.reInput, 'info', '', 'small');
                        $("#strName", self.el).val('');
                        return false;
                    } else {
                        self.detectionPolicyModel.fetch({
                            method: 'POST',
                            url: 'api/securityPolicy/insertSignatureClassType',
                            contentType: 'application/json',
                            dataType: 'text',
                            data: JSON.stringify(setParams),
                            async: false,
                            success: function (model) {
                                self.getSignatureClassType();
                                // 공격유형 목록 갱신 및 입력창 초기화 
                                self.getAttackTypeList();
                                $("#nClassType", self.el).val("");
                                $("#strName", self.el).val("");
                                // 지우고 갱신
                                $("#accordion", self.el).empty();
                                // 아코디언 공격유형 목록 조회 
                                self.getSignatureGroup();
                                // 하위 데이터 조회 
                                // $(".panel .panel-heading a").trigger("click");
                                //self.createDetectionPolicyTab(e);
                                $(".newTabBtn").trigger("click");
                                alertMessage.infoMessage(locale.msg.saveAttackType, 'info', '', 'small');
                            }
                        });
                    }
                }
            });
        },
        addRowItem: function () {
            var self = this;
            for (var i = 0; i < self.signatureClassTypeCollection.length; i++) {
                var rowItem = new RowItemView({
                    model: self.signatureClassTypeCollection.at(i)
                });
                $('.signatureClassType-list', self.el).append(rowItem.render().el);
            }
        },
        getSignatureClassType: function () {
            var self = this;
            this.signatureClassTypeCollection.fetch({
                method: 'POST',
                url: 'api/securityPolicy/selectSignatureClassType',
                contentType: 'application/json',
                data: JSON.stringify({}),
                async: false,
                success: function () {
                    self.addOneSignatureType();
                }
            });
        },
        addOneSignatureType: function () {
            var self = this;
            $(".signatureClassType-list", this.el).empty();
            for (var i = 0; i < self.signatureClassTypeCollection.length; i++) {
                if (parseInt(this.signatureClassTypeCollection.at(i).get('nClassType')) != 5) {
                    var rowItem = new RowItemView({
                        sessionModel: this.model,
                        model: this.signatureClassTypeCollection.at(i),
                        editable: this.editable,
                        type: this.type
                    });
                    $('.signatureClassType-list', self.el).append(rowItem.render().el);
                }
            }
        },
        /**
         * 그룹 유형 삭제 action
         */
        deleteSclassType: function (e) {
            var self = this;
            var nClassType = $(e.currentTarget).data("nclasstype");
            var strName = $(e.currentTarget).data("strname");
            if (parseInt(nClassType) == 99) {
                alertMessage.infoMessage(errorLocale.validation.noneDefaultAttackTypeDelete);
                return false;
            }
            var ModalContent = Backbone.View.extend({
                render: function () {
                    this.$el.html('<p class="modal-body-white-padding"> ' + locale.msg.isDeleteAttackType + ' </p>');
                    return this;
                }
            });
            Backbone.ModalView.msgWithOkCancelBtn({
                title: "info",
                body: new ModalContent(),
                size: 'small',
                okButtonCallback: function (e) {

                    Backbone.Loading.setLoading($('body'));

                    setTimeout(function () {

                        self.signatureClassTypeCollection.fetch({
                            method: 'POST',
                            url: 'api/securityPolicy/deleteSignatureClassType',
                            contentType: 'application/json',
                            dataType: 'text',
                            data: JSON.stringify({nClassType: nClassType, strName: strName}),
                            //async: false,
                            success: function () {
                                self.getSignatureClassType();
                                $("#accordion", self.el).empty();
                                self.getSignatureGroup();
                                //$(".panel .panel-heading a").trigger("click");
                                self.createDetectionPolicyTab(e);
                            }
                        });
                        Backbone.Loading.removeLoading($('body'));

                        alertMessage.infoMessage(locale.msg.deleteAttackType, 'info', '', 'small');

                        return true;
                    });
                }
            });
//				}else {
//					alertMessage.infoMessage('삭제할 수 없는 유형입니다.', 'info', '', 'small');
//				}
        },
        // 사용자정의로 추가된 공격유형 조회 
        getAttackTypeList: function () {
            var self = this;
            this.attackTypeCollection.fetch({
                method: 'POST',
                data: JSON.stringify({}),
                async: false,
                url: 'api/securityPolicy/selectAttackTypeSelect',
                contentType: 'application/json',
                success: function () {
                    self.addAttackType();
                }
            });
        },
        // 사용자 정의 공격유형 신규 생성시 셀렉트 박스 템플릿에 append
        addAttackType: function () {
            var self = this;
            for (var i = 0; i < self.attackTypeCollection.length; i++) {
                $('#attackTypeSelect', this.el).append("<option value=" + self.attackTypeCollection.at(i).get("nClassType") + ">" + self.attackTypeCollection.at(i).get("strName") + "</option>");
            }
        },
        callMakeCSV: function () {
            var el = this;
            var templateFile = 'js/tpl/common/csv.jsp';
//				el.getExport(templateFile);		
//                                el.exportDetectionPolicy();
            el.exportTest();
        },
        // collection 조회
        getExport: function (templateFile) {

            var self = this;
            var setParams = Backbone.FormSerialize.getData(this.$(this.formEl));
            var isDownload = {isDownload: "Y"};
            var listParam = $.extend(setParams, isDownload);

            self.detectionExportPolicyCollection.fetch({
                method: 'POST',
                data: JSON.stringify(listParam),
                url: 'api/securityPolicy/selectDetectionPolicy',
                contentType: 'application/json',
                success: function (data) {
                    if (self.detectionExportPolicyCollection.length > 0) {
                        new CSVExport({
                            templateFile: templateFile, // 공통
                            detectionExportPolicyCollection: self.detectionExportPolicyCollection  // file 명 (출력메뉴)
                        }).makeFile();
                    }
                }
            });
        },
        exportDetectionPolicy: function () {
            Backbone.ajax({
                method: 'GET',
                url: "api/securityPolicy/exportDetectionPolicy",
                success: function (data) {
                	console.log(data);
//                                    alert("fileName : " + data);
//                                    location.href = "fileDownload.do?fileUrl=" + fileUrl;
                }
            });
        },
        exportTest: function () {
            window.location.href = "api/securityPolicy/exportDetectionPolicy.do";
        },
        numberInput: function (e) {
            return Backbone.Utils.validation.keyDownNumber(e);
        },
        validatelThresholdNum: function () {
            var lThresholdNum = this.$('#thresholdNumInput', this.el).val();
            this.resultlThresholdNum = false;
            if (_.isNull(lThresholdNum) && lThresholdNum == "") {
                return this.resultlThresholdNum = Backbone.Utils.Tip.validationTooltip($('#thresholdNumInput', this.el), true);
            }
            this.resultlThresholdNum = Backbone.Utils.validation.validateIThresholdRangeNum(lThresholdNum);
            Backbone.Utils.Tip.validationTooltip($('#thresholdNumInput'), this.resultlThresholdNum);

            if (this.resultlThresholdNum == true) {
                return this.resultlThresholdNum = Backbone.Utils.Tip.validationTooltip($('#thresholdNumInput', this.el), this.resultlThresholdNum);
            } else {
                alertMessage.infoMessage(this.resultlThresholdNum, "warn");
                return this.resultlThresholdNum = Backbone.Utils.Tip.validationTooltip($('#thresholdNumInput', this.el), this.resultlThresholdNum);
            }
            return this.resultlThresholdNum = true;
        },
        validatelThresholdTime: function () {
            var lThresholdTime = this.$('#thresholdTimeInput', this.el).val();
            this.resultlThresholdTime = false;
            if (_.isNull(lThresholdTime) && lThresholdTime == "") {
                return this.resultlThresholdTime = Backbone.Utils.Tip.validationTooltip($('#thresholdTimeInput', this.el), true);
            }
            this.resultlThresholdTime = Backbone.Utils.validation.validateIThresholdRangeTime(lThresholdTime);
            Backbone.Utils.Tip.validationTooltip($('#thresholdTimeInput'), this.resultlThresholdTime);

            if (this.resultlThresholdTime == true) {
                return this.resultlThresholdTime = Backbone.Utils.Tip.validationTooltip($('#thresholdTimeInput', this.el), this.resultlThresholdTime);
            } else {
                alertMessage.infoMessage(this.resultlThresholdTime, "warn");
                return this.resultlThresholdTime = Backbone.Utils.Tip.validationTooltip($('#thresholdTimeInput'), this.resultlThresholdTime);
            }
            return this.resultlThresholdTime = true;
        }
    });
});