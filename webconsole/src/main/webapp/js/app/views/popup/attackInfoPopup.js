/**
 * @description tool bar 공격정보 팝업
 */
define(function (require) {

    "use strict";

    // require library
    var $ = require('jquery'),
            _ = require('underscore'),
            Backbone = require('backbone'),
            locale = require('i18n!nls/str'),
            dataExpression = require('utils/dataExpression'),
            d3 = require('d3'),
            Pagination = require('views/common/basicPagination'),
            AttackHelpPopupModel = require('models/detectionAnalysis/attackHelpPopupModel');

    // require template
    var Tpl = require('text!tpl/popup/attackInfoPopup.html'),
            ItemTpl = require('text!tpl/popup/attackInfoItemPopup.html'),
            AttackDefinitionTpl = require('text!tpl/detectionAnalysis/attackDefinition.html');

    var AttackInfoCollection = require('collections/dashboard/attackInfoPopupCollection');

    var TbAttackDefinitionView = Backbone.View.extend({
        className: 'tab-pane padding-r15 padding-l15',
        template: _.template(AttackDefinitionTpl),
        initialize: function (options) {
            options = options || {};
            this.searchCondition = options.searchCondition;
            this.tabViewInfo = options.tabViewItem;
            this.model = new AttackHelpPopupModel();
            this.attackModel = options.model;
            this.attackName = options.attackName;
            this.strTitle = options.strTitle;
            this.sSeverity = options.sSeverity;
            this.children = [];
            this.info = {
                key: this.cid,
                title: locale.attack + locale.detailInfo
            };

        },
        render: function () {
            var self = this;
            self.$el.html(self.template({
                locale: locale,
                dataType: self.dataType
            }));

            // 탐지정보를 숨긴다.
            $('#detectionInfoTitle', self.el).empty();
            $('#detectionInfoContent', self.el).empty();
            $('#detectionInfoContent', self.el).removeClass();

            self.$el.prop('id', self.info.key);

            self.registerAttackDefinition();

            return self;
        },
        registerAttackDefinition: function () {
            var self = this;
            this.model.fetch({
                method: 'POST',
                url: 'api/selectAttackHelpPopupData',
                contentType: 'application/json',
                async: false,
                data: JSON.stringify(self.searchCondition),
                success: function (model) {
                    self.setRegistrationAttackHelp();
                }
            });
        },
        setRegistrationAttackHelp: function () {
            var self = this;
            // 공격 정보 
            // SYSTEM_SIGHELP 테이블에 strDescription과 strAtkType이 없을 경우가 있으므로 공격 정보 팝업에서 넘겨받도록 변경.
            $('#attackName', self.el).text(this.strTitle);
            $('#attackType', self.el).text(this.attackName);
            $('#severityColor', self.el).addClass('severity-bg-lv' + this.sSeverity);

            // 도움말 정보 SYSTEM_SIGHELP 테이블 조회.
            // 위험도 변경 
            //$('#severityColor', self.el).addClass('threat-bg-lv' + self.model.get('bSeverity'));
            //$('#severityColor', self.el).addClass('severity-bg-lv' + dataExpression.severityColorData(self.model.get('bSeverity')));
            //$('#severity', self.el).append(dataExpression.severityData(self.model.get('bSeverity')));
            $('#severity', self.el).text(dataExpression.severityData(this.sSeverity));
            $('#lCode', self.el).text(self.model.get('lCode'));
            $('#patternInfo', self.el).text(self.model.get('signatureRule'));
            $('#summary', self.el).text(self.model.get('strSummary'));
            $('#detailComment', self.el).text(self.model.get('strDescription'));
            $('#solution', self.el).text(self.model.get('strSolution'));
            $('#reference', self.el).text(self.model.get('strReference'));

            // 명확히 확인되지 않은 것이여서 당분간 사용안함
            //$('#detectionSucceedOrNot', this.el).append(this.model.get());
            //$('#packetSave', this.el).append('');
            //$('#threshold', this.el).append('');
            //$('#trapId', this.el).append('');
        },
        getInfo: function () {
            return this.info;
        }
    });

    var TbAttackInfoItemView = Backbone.View.extend({
        tagName: 'tr',
        template: _.template(ItemTpl),
        initialize: function (options) {
            options = options || {};
            this.tabView = options.tabView;
        },
        events: {
            'click #attackNamePopup': 'attackNamePopup'
        },
        render: function () {
            this.$el.html(this.template({
                model: this.model.toJSON()
            }));

            return this;
        },
        attackNamePopup: function (e) {
            var strTitle = $(e.currentTarget).data("strdescription");
            var attackName = $(e.currentTarget).data("attackname");
            var sSeverity = $(e.currentTarget).data("sseverity");
            var searchCondition = {};
            _.extend(searchCondition, {
                'lCode': this.model.get('lCode'),
                'strTitle': this.model.get('strTitle'),
                'strAttackType': this.model.get('strAttackType')
            });
            var tbAttackHelp = new TbAttackDefinitionView({
                tabView: this.tabView,
                searchCondition: searchCondition,
                strTitle: strTitle,
                attackName: attackName,
                sSeverity: sSeverity
            });
            this.tabView.addTab(tbAttackHelp);
        }
    });

    return Backbone.View.extend({
        className: 'tab-pane padding-r15 padding-l15',
        template: _.template(Tpl),
        events: {
            'click #searchBtn': 'search'
        },
        initialize: function (options) {
            options = options || {};

            this.collection = new AttackInfoCollection();
            this.typeOfVulnerabilityCollection = new AttackInfoCollection();
            this.tabView = options.tabView;

            this.listenTo(this.collection, 'add', this.addOne);
            this.listenTo(this.typeOfVulnerabilityCollection, 'add', this.addOneTypeOfVulnerability);

            this.pagination = new Pagination({
                evt: this
            });
            this.bind('pagination', this.search, this);
            this.children = [];

            this.info = {
                key: this.cid,
                title: locale.attack + locale.information
            };

        },
        render: function () {
            var thisView = this;
            thisView.$el.html(thisView.template({
                locale: locale,
                dataType: thisView.dataType
            }));
            this.$tbody = $('.table tbody', this.el);
            $('.more-option', thisView.el).append(thisView.pagination.render().el);
            thisView.getTypeOfVulnerability(); // 취약유형 리스트 조회

            thisView.search();

            return thisView;
        },
        search: function (paging) {

            var thisView = this;
            var searchCondition = {};

            $('.more-option', thisView.el).append(thisView.pagination.render().el);
            this.paging = paging;
            if (this.paging == undefined || this.paging.startRowSize == undefined || this.paging.endRowSize == undefined) {
                this.pagination.setInitialization();
                this.paging = this.pagination.getPaginationRowValue();
            }

            _.extend(searchCondition, this.paging, {
                attackTypeSelect: $('select#attackTypeSelect', this.el).val(),
                searchSelect: $('select#searchSelect', this.el).val(),
                searchInput: $('input#searchInput', this.el).val(),
                severityHCheck: $('input#severityHCheck', this.el).is(':checked') == true,
                severityMCheck: $('input#severityMCheck', this.el).is(':checked') == true,
                severityLCheck: $('input#severityLCheck', this.el).is(':checked') == true,
                severityICheck: $('input#severityICheck', this.el).is(':checked') == true
            });

            this.$tbody.empty();

            this.collection.fetch({
                method: 'POST',
                url: 'api/selectAttackInfoPopup',
                contentType: 'application/json',
                data: JSON.stringify(searchCondition),
                async: false,
                beforeSend: function () {
                    Backbone.Loading.setLoading($('.progressbar', thisView.el));
                    $('.more-option', thisView.el).hide();
                },
                success: function (collection) {
                    $('tbody.list', thisView).empty();
                    if (collection.length > 0) {
                        $('#totalCount', thisView.el).text(collection.at(0).get('totalRowSize'));
                        $('#cntHigh', thisView.el).text(collection.at(0).get('cntHigh'));
                        $('#cntMed', thisView.el).text(collection.at(0).get('cntMed'));
                        $('#cntLow', thisView.el).text(collection.at(0).get('cntLow'));
                        $('#cntInfo', thisView.el).text(collection.at(0).get('cntInfo'));
                        thisView.pagination.setTotalRowSize(collection.at(0).get('totalRowSize'));

                        $("#totalCount").digits();
                        $("#cntHigh").digits();
                        $("#cntMed").digits();
                        $("#cntLow").digits();
                        $("#cntInfo").digits();

                        $('#list', thisView.el).parent().removeClass('nodata');
                    } else {
                        $('#totalCount', thisView.el).text(0);
                        $('#cntHigh', thisView.el).text(0);
                        $('#cntMed', thisView.el).text(0);
                        $('#cntLow', thisView.el).text(0);
                        $('#cntInfo', thisView.el).text(0);
                        thisView.pagination.setTotalRowSize(0);
                        $('#list', thisView.el).parent().addClass('nodata');
                    }
                },
                complete: function () {
                    Backbone.Loading.removeLoading($('.progressbar', thisView.el));
                    $('.more-option', thisView.el).show();
                }
            });
        },
        getTypeOfVulnerability: function () {
            this.typeOfVulnerabilityCollection.fetch({
                method: 'POST',
                url: 'api/selectTypeOfVulnerabilityList',
                contentType: 'application/json',
                data: JSON.stringify({}),
                async: false,
                success: function (collection) {
                	console.log(collection);
                }
            });
        },
        addOneTypeOfVulnerability: function (model) {
            if (model.get('nClassType') != 5) {
                $('#attackTypeSelect', this.el).append("<option value=" + model.get("nClassType") + ">" + model.get("strName") + "</option>");
            }
        },
        addOne: function (model) {
            model.set({
                severityColor: model.get('bSeverity'), //dataExpression.severityColorData(model.get('bSeverity')),
                //attackTypeName: dataExpression.getAttackTypeName(model.get('strAttackType'))
                attackTypeName: model.get('strAttackType')
            });

            var itemView = new TbAttackInfoItemView({
                model: model,
                tabView: this.tabView
            });
            $('tbody.list', this.el).append(itemView.el);
            itemView.render();
            this.children.push(itemView);
        },
        onClose: function () {
            if (this.children.length > 0) {
                _.each(this.children, function (child) {
                    child.close();
                });
            }
            this.pagination.close();
        },
        getInfo: function () {
            return this.info;
        }
    });

});
