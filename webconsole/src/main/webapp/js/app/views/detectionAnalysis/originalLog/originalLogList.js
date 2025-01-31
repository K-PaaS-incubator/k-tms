/**
 * originalLog table view  
 * @author lee kyunghee
 * @since 2014-12-09
 * @description 원본로그 검색 화면을 보여준다. 
 */
define(function (require) {

    "use strict";

    // require library
    var $ = require('jquery'),
            Backbone = require('backbone');

    // require locale
    var locale = require('i18n!nls/str');
    var alertMessage 				= require('utils/AlertMessage');

    // require model, collection, view
    var OriginalLogCollection = require('collections/detectionAnalysis/originalLogCollection'),
            OriginalLogModel = require('models/detectionAnalysis/originalLogModel'),
            OriginalLogListItemView = require('views/detectionAnalysis/originalLog/originalLogListItem'),
            Pagination = require('views/common/pagination'),
            ExportView = require('views/common/export'),
            TableModel = require('models/common/tableModel');

    // require template
    var tpl = require('text!tpl/detectionAnalysis/originalLogList.html');

    return Backbone.View.extend({
        template: _.template(tpl),
        initialize: function (options) {
            this.menuKey = "detectionOriginal";
            this.collection = new OriginalLogCollection();
            this.model = new OriginalLogModel();
            this.pagination = new Pagination();
            this.exportCollection = new OriginalLogCollection();
            this.exportTotalModel = new OriginalLogModel();
            this.defaultTimeModel;

            this.evt = options.evt;

            _.bindAll(this, "removeItemViews", "onClose", "customOn", "customOff", "render", "retrieve", "getListData", "addOne", "addAll");
            
            this.tableModel = new TableModel();
            this.tableModel = {
                'menuKey' : this.menuKey
            };

            this.customOn();

            this.listenTo(this.model, 'change', this.setTotalData);
            this.listenTo(this.collection, 'add', this.addOne);
            this.listenTo(this.collection, 'reset', this.addAll);

        },
        events: {
            'click #readMoreBtn': 'getListData',
            'click .btnExport': 'callMakeExport',
            'click .selColumn': 'selColumn',
            'click #tableRegist': 'insertTable',
        },
        callMakeExport: function (e) {
            var self = this;
            var format = $(e.currentTarget).data('format');
            var exportRange = $("input:radio[name='exportRange']:checked").val();

            var templateFile = 'js/tpl/common/excel.jsp';

            if (format == 'excel') {
                templateFile = 'js/tpl/common/excel.jsp';
            } else if (format == 'word') {
                templateFile = 'js/tpl/common/word.jsp';
            } else if (format == 'html') {
                templateFile = 'js/tpl/common/html.jsp';
            }

            if (exportRange == 'all') {
                self.model.set('isDownload', 'Y');
            } else {
                self.model.set('isDownload', '');
            }

            self.getExportList(templateFile);
        },
        getExportList: function (templateFile) {

            var self = this;
            var paging = {};

            _.extend(paging, {"startRowSize": 0, "endRowSize": this.pagination.endRowSize, "rowSize":(this.pagination.endRowSize)});
            var listParam = $.extend(paging, self.model.toJSON());

            self.exportCollection.fetch({
                method: 'POST',
                data: JSON.stringify(listParam),
                url: 'api/detectionAnalysis/selectOriginalLogBylindex',
                contentType: 'application/json',
                beforeSend: function () {
                    Backbone.Loading.setLoading($('.progressbar', self.el));
                },
                success: function (collection) {
                    self.model.set('isDownload', '');
                    if (self.exportCollection.length > 0) {
                        self.excuteExport(templateFile, listParam);
                    }
                },
                error: function (message) {
                    self.model.set('isDownload', '');
                },
                complete: function () {
                    Backbone.Loading.removeLoading($('.progressbar', self.el));
                }
            });
        },
        // export UI 세팅
        excuteExport: function (templateFile, listParam) {
            var self = this;

            var contentTable = document.createElement('table');
            $(contentTable).append('<tr>' +
                    '<td class=\"content-table-header\" >' + locale.rank + '</td>' +
                    '<td class=\"content-table-header\" >' + locale.attackName + '</td>' +
                    '<td class=\"content-table-header\" >' + locale.attackType + '</td>' +
                    '<td class=\"content-table-header\" >' + locale.detectionTime + '</td>' +
                    '<td class=\"content-table-header\" >' + locale.attackIp + '</td>' +
                    '<td class=\"content-table-header\" >' + locale.protocol + '</td>' +
                    '<td class=\"content-table-header\" >' + locale.attackPort + '</td>' +
                    '<td class=\"content-table-header\" >' + locale.victimIP + '</td>' +
                    '<td class=\"content-table-header\" >' + locale.protocol + '</td>' +
                    '<td class=\"content-table-header\" >' + locale.victimPort + '</td>' +
                    '<td class=\"content-table-header\" >' + locale.attackCount + '</td>' +
                    '<td class=\"content-table-header\" >' + locale.winbound + '</td>' +
                    '<td class=\"content-table-header\" >' + locale.network + '</td>' +
//                    '<td class=\"content-table-header\" >' + locale.vsensor + '</td>' +
//                    '<td class=\"content-table-header\" >' + locale.sensor + '</td>' +
                    '</tr>');
            var forMat = [];

            new ExportView({
                menuOptions: "originalLog",
                templateFile: templateFile, // 공통
                docTitle: locale.originalLog, // file 명 (출력메뉴)
                requireModel: new OriginalLogModel(), // 리스트 모델				
                listParam: listParam, // 선택 조건 
                fetchCollection: self.exportCollection, // 리스트 데이터			
                contentTable: contentTable, // 리스트
                totalColomn: 13, // 테이블 총 컬럼			
                listKeys: new Array(_.pick(new OriginalLogModel().attributes),
                        'rNum', // Model에서 출력에 사용되는 key  
                        'strTitle', // 공격명
                        'bType', // 공격유형
                        'startDate', // 탐지시간
                        'dwSourceIp', // 공격자IP
                        'nProtocol', // 공격프로토콜 
                        'nSourcePort', // 공격포트
                        'deDestinationIp', // 피해자IP
                        'nProtocol', // 피해프로토콜
                        'nDestinationPort', // 피해포트
                        'dweventnum', // 공격횟수
                        'wInbound', // In/Out
                        'srcNetworkName' // 네트워크		// 네트워크명이 없을 경우 '-'로 표시할것 
                        ),
                forMat: forMat,
                pageMode: 'horizontal'
            }).makeFile();
        },
        onClose: function () {
            //this.removeItemViews();
            this.customOff();
        },
        customOn: function () {
            this.evt.bind("retrieve", this.retrieve);
        },
        customOff: function () {
            this.evt.unbind("retrieve");
        },
        render: function () {
            this.$el.html(this.template({
                locale: locale
            }));

            this.paginationBasicInfo = this.pagination.getPaginationType('readMorePaginator');
            $('#pageLayout', this.el).append(this.paginationBasicInfo.type.el);
            this.getTableColumns();
            return this;
        },
        removeItemViews: function () {
            this.evt.trigger('cleanUpItemView');
        },
        addOne: function (model) {
            var self = this;
            var view = new OriginalLogListItemView({
                collection: this.collection,
                model: model,
                searchCondition: this.listParam
            });
            //view.listenTo(this.evt, 'cleanUpItemView', view.close);
            $("#list", self.el).append(view.render().el);
            $(".attackCount", self.el).digits();
        },
        addAll: function () {
            var self = this;
            //this.removeItemViews();
            this.$("#list").empty();
            self.collection.each(self.addOne, self);
            this.columnCheck();
        },
        retrieve: function (model) {
            this.model = model;
            this.pagination.setInitialization();
            this.getListData();
        },
        getListData: function () {
            var self = this;
            var paging = this.pagination.readMorePagination();
            // 더보기 유효성이 false경우 리스트 append를 막음 
            if (!paging) {
                return;
            }
            this.listParam = $.extend(paging, this.model.toJSON());
            var reset = true;

            $("#listDate", self.el).text(this.model.get('startDateInput') + " ~ " + this.model.get('endDateInput'));

            if (paging.startRowSize == 0) {
                self.reset = true;
            } else {
                self.reset = false;
            }
            self.collection.fetch({
                method: "POST",
                data: JSON.stringify(this.listParam),
                url: 'api/detectionAnalysis/selectOriginalLogBylindex',
                contentType: 'application/json',
                reset: self.reset,
                beforeSend: function () {
                    Backbone.Loading.setLoading($('.progressbar', self.el));
                    $('#pageLayout', self.el).hide();
                },
                success: function (data) {
                    // 원본로그 > 전체 총수 
                    if (self.collection.length > 0) {
                        $('.btnExport').attr('disabled', false);
                        $('input:radio[name="exportRange"]').attr('disabled', false);

                        self.pagination.setTotalRowSize(self.collection.at(0).get('totalRowSize'));
                        self.$(".table").removeClass("nodata");
                        self.$('#totalRowSize').text(self.collection.at(0).get("totalRowSize"));
                        //위험도 표시
                        self.$("#severityHCount").text(self.collection.at(0).get("cntHigh"));
                        self.$('#severityMCount').text(self.collection.at(0).get("cntMed"));
                        self.$('#severityLCount').text(self.collection.at(0).get("cntLow"));
                        self.$('#severityICount').text(self.collection.at(0).get("cntInfo"));

                        $("#totalRowSize").digits();
                        $("#severityHCount").digits();
                        $("#severityMCount").digits();
                        $("#severityLCount").digits();
                        $("#severityICount").digits();

                    } else {
                        $('.btnExport').attr('disabled', true);
                        $('input:radio[name="exportRange"]').attr('disabled', true);

                        self.pagination.setTotalRowSize(0);
                        self.$(".table").addClass("nodata");
                        self.$('#totalRowSize').text(0);
                        self.$('#severityHCount').text("0");
                        self.$('#severityMCount').text("0");
                        self.$('#severityLCount').text("0");
                        self.$('#severityICount').text("0");
                    }
                    self.columnCheck();
                },
                complete: function () {
                    Backbone.Loading.removeLoading($('.progressbar', self.el));
                    $('#pageLayout', self.el).show();
                }
            });
        },
        selColumn: function(e) {
            var val = $(e.currentTarget).val();
            var id = "#" + val;
            var index = $(id, this.el).index() + 1;
            
            if ($(e.currentTarget).is(':checked')) {
                $('#content_table th:nth-child(' + index + ')').show();
                $('#content_table td:nth-child(' + index + ')').show();
            } else {
                $('#content_table th:nth-child(' + index + ')').hide();
                $('#content_table td:nth-child(' + index + ')').hide();
            }
        },
        columnCheck: function() {
            $("input[name='selColumn']").each(function(i) {
                var val = $("input[name='selColumn']").eq(i).val();
                var id = "#" + val;
                var index = $(id).index() + 1;
                if ($("input[name='selColumn']").eq(i).is(':checked')) {
                    
                    $('#content_table th:nth-child(' + index + ')').show();
                    $('#content_table td:nth-child(' + index + ')').show();
                } else {
                    $('#content_table th:nth-child(' + index + ')').hide();
                    $('#content_table td:nth-child(' + index + ')').hide();
                }
            });
        },
        getTableColumns: function() {
            var thisView = this;
            Backbone.ajax({
                method: 'POST',
                contentType: 'application/json',
                url: 'api/common/selectTableColumns',
                data: JSON.stringify(thisView.tableModel),
                async: true,
                cache: true,
                success: function(data) {
                    thisView.initTableColumn(data);
                }
            });
        },
        insertTable: function() {
            var thisView = this;
            var param = [];
            $("input[name='selColumn']").each(function(i) {
                var val = $("input[name='selColumn']").eq(i).val()
                var enabled = "N";
                if ($("input[name='selColumn']").eq(i).is(':checked')) {
                    enabled = "Y";
                }
                param.push({
                    menuKey: thisView.menuKey,
                    colId: val,
                    enabled: enabled
                });
            });
            Backbone.ajax({
                method: 'POST',
                contentType: 'application/json',
                url: 'api/common/insertTableColumns',
                data: JSON.stringify(param),
                dataType: 'text',
                success: function() {
                    alertMessage.infoMessage(locale.msg.insertMsg, 'info', '', 'small');
                }
            });
        },
        initTableColumn: function(data) {
            if (_.isEmpty(data)) {
                $('.selColumn', this.el).attr('checked', true);
            } else {
                $("input[name='selColumn']").each(function(i) {
                    var val = $("input[name='selColumn']").eq(i).val()
                    if (data[val] == 'Y') {
                        $("input[name='selColumn']").eq(i).attr('checked', true);
                    } else {
                        $("input[name='selColumn']").eq(i).attr('checked', false);
                    }
                });
            }
            this.columnCheck();
        }
    });
});