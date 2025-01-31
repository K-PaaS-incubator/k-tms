/**
 * 감사로그 리스트 화면 
 */
define(function (require) {

    "use strict";

    // require library
    var $ = require('jquery'),
            Backbone = require('backbone'),
            dataExpression = require('utils/dataExpression');

    var locale = require('i18n!nls/str');
    // require model, collection, view
    var AuditLogCollection = require('collections/systemStatus/auditLogCollection'),
            AuditLogModel = require('models/systemStatus/auditLogModel'),
            AuditLogListItemView = require('views/systemStatus/auditLog/auditLogListItem'),
            Pagination = require('views/common/pagination'),
            ExportView = require('views/common/export');

    // require template
    var tpl = require('text!tpl/systemStatus/auditLogList.html');

    return Backbone.View.extend({
        template: _.template(tpl),
        //화면 , 엑셀 총 리스트 수 설정 추가
        modelLimit : 2000,
        initialize: function (options) {

            this.evt = options.evt;
            this.collection = new AuditLogCollection();
            this.model = new AuditLogModel();
            this.pagination = new Pagination();
            this.exportCollection = new AuditLogCollection();
            this.exportTotalModel = new AuditLogModel();

            _.bindAll(this, "onClose", "customOn", "customOff", "render", "removeItemViews", "retrieve", "getListData", "addOne", "addAll");

            this.customOn();

            this.listenTo(this.model, 'change', this.setTotalData);
            this.listenTo(this.collection, 'add', this.addOne);
            this.listenTo(this.collection, 'reset', this.addAll);
        },
        events: {
            'click #readMoreBtn': 'getListData',
            'click .btnExport': 'callMakeExport'
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
        // collection 조회
        getExportList: function (templateFile) {

            var self = this;
            var paging = {};
            var reset = true;
            var start = 0;
            if(this.pagination.endRowSize > this.modelLimit){
            	start = this.pagination.endRowSize - this.modelLimit;
            }
            _.extend(paging, {"startRowSize": start, "endRowSize": this.pagination.endRowSize, "rowSize":(this.pagination.endRowSize-start) });
            var listParam = $.extend(paging, self.model.toJSON());

            if (self.listType == "default") {
                var defaultTime = analysisDate.getDefaultTime(this.model.get('startDateInput'), this.model.get('endDateInput'));
                self.defaultTimeModel = $.parseJSON(JSON.stringify(this.model));
                self.defaultTimeModel = $.extend(this.defaultTimeModel, {"startDateInput": defaultTime[0], "endDateInput": defaultTime[1], "startDateBeforeInput": defaultTime[2], "endDateBeforeInput": defaultTime[3]});
                // 변화량 제거에 따른 변경 
                // listParam 				= $.extend(paging, this.defaultTimeModel);
                // self.url 				= "api/systemStatus/selectAuditLogList";
                listParam = $.extend(paging, this.model.toJSON());
                self.url = "api/systemStatus/selectAuditLogList";
            } else {
                listParam = $.extend(paging, this.model.toJSON());
                self.url = "api/systemStatus/selectAuditLogList";
            }

            self.exportCollection.fetch({
                method: 'POST',
                data: JSON.stringify(listParam),
                url: self.url,
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
                    '<td class=\"content-table-header\" >' + locale.type + '</td>' +
                    '<td class=\"content-table-header\" >' + locale.user + ', ' + locale.system + '</td>' +
                    '<td class=\"content-table-header\" >' + locale.auditContents + '</td>' +
                    '<td class=\"content-table-header\" >' + locale.auditTime + '</td>' +
                    '</tr>');
            var forMat = [];

            new ExportView({
                menuOptions: "auditLog",
                templateFile: templateFile, // 공통
                docTitle: locale.auditLog, // file 명 (출력메뉴)
                requireModel: new AuditLogModel(), // 리스트 모델			
                listParam: listParam, // 선택 조건 
                fetchCollection: self.exportCollection, // 리스트 데이터		
                contentTable: contentTable, // 리스트
                totalColomn: 4, // 테이블 총 컬럼		
                listKeys: new Array(_.pick(new AuditLogModel().attributes), // Model에서 출력에 사용되는 key  
                        'ltype1', // 유형 
                        'strOperator', // 사용자, 시스템
                        'strContent', // 감사 내용
                        'tmOccur' 				// 감사 시간  
                        ),
                forMat: forMat
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
            var paginationBasicInfo = this.pagination.getPaginationType('readMorePaginator');
            $('#pageLayout', this.el).html(paginationBasicInfo.type.el);

            return this;
        },
        removeItemViews: function () {
            this.evt.trigger('cleanUpItemView');
        },
        addOne: function (model) {
            model.set({
                setType: 'log-' + dataExpression.auditEngCheck(model.get("ltype1")),
                ltype1: dataExpression.auditTypeCheck(model.get("ltype1"))
            });
            var view = new AuditLogListItemView({
                model: model,
                searchModel: this.model
            });
            //view.listenTo(this.evt, 'cleanUpItemView', view.close);
            $("#list", this.el).append(view.render().el);
        },
        addAll: function () {
            //this.removeItemViews();
            this.$("#list").empty();
            this.collection.each(this.addOne, this);
        },
        retrieve: function (model) {
            this.model = model;
            this.pagination.setInitialization();
            this.getListData();
        },
        getListData: function () {
            var self = this;

            var paging = this.pagination.readMorePagination();
            if (!paging) {
                return;
            }
            var listParam = $.extend(paging, this.model.toJSON());
            var reset = true;
            
            if (paging.startRowSize == 0) {
                self.reset = true;
            } else {
            	//화면 최대 갯수 로직 추가
            	if(paging.endRowSize > this.modelLimit){
            		self.reset = true;
            		//self.reset = false;
            		this.pagination.setMaxListPageValue(this.modelLimit);
            	}
            	else {
            		self.reset = false;
            	}
            }
            self.collection.fetch({
                method: "POST",
                data: JSON.stringify(listParam),
                url: 'api/systemStatus/selectAuditLogList',
                contentType: 'application/json',
                reset: self.reset,
                beforeSend: function () {
                    Backbone.Loading.setLoading($('.progressbar', self.el));
                },
                success: function (data) {
                    // 감사로그 > 전체 총수 
                    if (self.collection.length > 0) {
                        $('.btnExport').attr('disabled', false);
                        $('input:radio[name="exportRange"]').attr('disabled', false);
                        $(".table", self.el).removeClass("nodata");
                        self.pagination.setTotalRowSize(self.collection.at(0).get('totalRowSize'));
                        self.$('#totalRowSize').text(self.collection.at(0).get("totalRowSize"));

                        $("#totalRowSize").digits();
                    } else {
                        $('.btnExport').attr('disabled', true);
                        $('input:radio[name="exportRange"]').attr('disabled', true);

                        self.pagination.setTotalRowSize(0);
                        self.$('#totalRowSize').text(0);
                        $(".table", self.el).addClass("nodata");
                    }
                },
                error: function (model, response) {
                	console.log('error', response)
                },
                complete: function () {
                    Backbone.Loading.removeLoading($('.progressbar', self.el));
                }
            });
        }
    });
});