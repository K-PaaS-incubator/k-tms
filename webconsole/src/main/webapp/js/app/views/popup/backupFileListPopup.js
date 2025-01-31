/**
 * @ DB Backup File List Popup
 */
define(function (require) {

    "use strict";

    // require library
    var $ = require('jquery'),
            _ = require('underscore'),
            Backbone = require('backbone'),
            locale = require('i18n!nls/str'),
            dataExpression = require('utils/dataExpression'),
            Pagination = require('views/common/basicPagination'),
            BackupFilePopupModel = require('models/systemSettings/backupFileModel');

    // require template
    var Tpl = require('text!tpl/popup/backupFileListPopup.html'),
            ItemTpl = require('text!tpl/popup/backupFileItemPopup.html');

    var BackupFileCollection = require('collections/systemSettings/backupFileCollection');

    var BackupFileItemView = Backbone.View.extend({
        tagName: 'tr',
        template: _.template(ItemTpl),
        initialize: function (options) {
            options = options || {};
            this.tabView = options.tabView;

        },
        events: {
            'click #backupFileDownload': 'fileDownload'
        },
        render: function () {
            this.$el.html(this.template({
                model: this.model.toJSON(),
                locale: locale,
            }));

            return this;
        },
        fileDownload: function () {
            window.location.href = "api/common/fileDownload.do?type=dbBackupFile&code=" + this.model.get('lIndex');
        }
    });

    return Backbone.View.extend({
        className: 'tab-pane padding-r15 padding-l15',
        template: _.template(Tpl),
        events: {
            'click #searchBtn': 'search'
//            'click #searchBtn': 'test'
        },
        test: function () {
            console.log("loading start ~~~~");
//            this.spinLoading('.backupFileList', true);
//            this.$('.modalLoadingCustom').css('visibility', 'visible');
            $('#loadingImage').css('visibility','visible');
            this.sleep(5000);
            
            $('#hiddentest', this.el).trigger('click');
            console.log("loading end !!!!!!!!!!!");
        },
        showtest: function() {
            console.log("show~~~");
            $('#loadingImage').css('visibility','visible');
        },
        hiddentest: function() {
            console.log("hidden~~~");
            $('#loadingImage').css('visibility','hidden');
        },
        sleep: function(time) {
            var start = $.now();
            console.log("start time : " + start)
            var chk = true;
            while (chk) {
                if (($.now() - start) > time) {
                    chk = false;
                }
            }
            console.log("end time : " + $.now());
        },
        initialize: function () {

            this.collection = new BackupFileCollection();

            this.listenTo(this.collection, 'add', this.addOne);

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

            thisView.search();
//            $('.loadingImage', this.el).hide();
this.$('#loadingImage').css('visibility', 'hidden');
            return thisView;
        },
        search: function (paging) {

            var thisView = this;
            var searchCondition = {};

            this.paging = paging;
            if (this.paging == undefined || this.paging.startRowSize == undefined || this.paging.endRowSize == undefined) {
                this.pagination.setInitialization();
                this.paging = this.pagination.getPaginationRowValue();
            }

            _.extend(searchCondition, this.paging, {
                strBackupFilePath: $('input#searchInput', this.el).val(),
            });

            this.$tbody.empty();

            this.collection.fetch({
                method: 'POST',
                url: 'api/systemSetting/selectBackupFileList',
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
                        $('#totalCount', thisView.el).text(collection.at(0).get('totalRow'));
                        thisView.pagination.setTotalRowSize(collection.at(0).get('totalRow'));

                        $("#totalCount").digits();

                        $('#list', thisView.el).parent().removeClass('nodata');
                    } else {
                        $('#totalCount', thisView.el).text(0);
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
        addOne: function (model) {
            //일별 테이블 항목 설정 
            var tempDayToString = model.get("nTableCheckValue").toString(2);
            var tempDayFullValue = [0, 0, 0, 0, 0];
            var tempDayFullValueChecked = [];
            var backupTable = "";
            for (var i = 5 - tempDayToString.length; i < 5; i++) {
                tempDayFullValue[i] = parseInt(tempDayToString[i - (5 - tempDayToString.length)]);
            }

            for (var j = 0; j < 5; j++) {
                if (tempDayFullValue[j] == 0) {
                    tempDayFullValueChecked[j] = "";
                } else {
                    tempDayFullValueChecked[j] = "checked";
                }
            }
            if (tempDayFullValue[4] == 1) {
                backupTable = locale.intrusionDetection
            }
            if (tempDayFullValue[3] == 1) {
                if (backupTable != '') {
                    backupTable = backupTable + ','
                }
                backupTable = backupTable + locale.traffic
            }
            if (tempDayFullValue[2] == 1) {
                if (backupTable != '') {
                    backupTable = backupTable + ','
                }
                backupTable = backupTable + locale.auditLog
            }
            if (tempDayFullValue[1] == 1) {
                if (backupTable != '') {
                    backupTable = backupTable + ','
                }
                backupTable = backupTable + locale.session
            }
            if (tempDayFullValue[0] == 1) {
                if (backupTable != '') {
                    backupTable = backupTable + ','
                }
                backupTable = backupTable + locale.securePolicy
            }
            var tableDel = "";
            if (model.get('nTableDel') == 1) {
                tableDel = locale.remove;
            } else {
                tableDel = locale.unRemove;
            }

            model.set({
                severityColor: model.get('bSeverity'), //dataExpression.severityColorData(model.get('bSeverity')),
                //attackTypeName: dataExpression.getAttackTypeName(model.get('strAttackType'))
                attackTypeName: model.get('bType'),
                backupTable: backupTable,
                tableDel: tableDel,
            });

            var itemView = new BackupFileItemView({
                model: model,
                tabView: this.tabView,
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
