define(function (require) {

    "use strict";

    var $ = require('jquery'),
            Backbone = require('backbone'),
            locale = require('i18n!nls/str'),
            FileMetaHelpPopupModel = require('models/detectionAnalysis/fileMetaPopupModel'),
            TbIpRetrieve = require('views/tools/ipRetrievePopup'),
            sessionManager = require('utils/sessionManager'),
            dataExpression = require('utils/dataExpression');

    // require template
    var Tpl = require('text!tpl/detectionAnalysis/fileMetaDefinition.html');

    return Backbone.View.extend({
        className: 'tab-pane padding-r15 padding-l15',
        template: _.template(Tpl),
        initialize: function (options) {
            this.menuType = options.menuType;
            this.fileMetaMonitoringModel = options.fileMetaMonitoringModel;
            this.model = new FileMetaHelpPopupModel();
            this.fileMetaModel = new FileMetaHelpPopupModel();
            this.searchCondition = options.searchCondition;
            this.listModel = options.model;
            this.lindex;
            this.startDateInput = "";
            this.endDateInput = "";
        },
        events: {
            'click .sipRetrievePopupBtn': 'showsIpRetrievePopup',
            'click .dipRetrievePopupBtn': 'showdIpRetrievePopup',
            'click .fileDownload': 'fileDownload'
        },
        render: function () {
            this.$el.html(this.template({
                locale: locale,
                role : sessionManager.role
            }));
            if (this.menuType == "FileMetaDetectionMonitoring") {
                this.lindex = this.fileMetaMonitoringModel.get('lIndex');
                this.startDateInput = this.fileMetaMonitoringModel.get('tmLogTime');
                this.setMonitoringDefinition();
            } else if (this.menuType == "FileMetaDetection") {
                this.lindex = this.searchCondition.lIndex;
                this.startDateInput = this.searchCondition.startDateInput;
                this.endDateInput = this.searchCondition.endDateInput;
                this.fileMetaDefinition();
            }

            return this;
        },
        fileMetaDefinition: function () {
            var thisView = this;

            _.extend(this.searchCondition, this.listModel.toJSON(), {
                strDestIp: this.listModel.get('deDestinationIp'),
                strSrcIp: this.listModel.get('dwSourceIp')
            });

            this.fileMetaModel.fetch({
                method: 'POST',
                url: 'api/detectionAnalysis/selectFileMetaHelpPopupList',
                contentType: 'application/json',
                async: false,
                data: JSON.stringify(this.searchCondition),
                success: function (model) {
                    thisView.setFileMetaHelp(model);
                }
            });
        },
        setFileMetaHelp: function (model) {
            // FileMeta 정보 
            $('#fileType', this.el).append(model.get('strMagic'));
            $('#tmLogTime', this.el).append(model.get('tmLogTime'));
            $('#vsensorName', this.el).append(model.get('vsensorName'));
            $('#sensorName', this.el).append(model.get('sensorName'));
            $('#sip', this.el).append(model.get('bIpType') == 4 ? model.get('dwSourceIp') : model.get('strSourceIp'));
            $('#dip', this.el).append(model.get('bIpType') == 4 ? model.get('deDestinationIp') : model.get('strDestinationIp'));
            $('#sPort', this.el).append(model.get('nSourcePort'));
            $('#dPort', this.el).append(model.get('nDestinationPort'));
            $('#fileName', this.el).append(model.get('strFileName'));
            $('#fileSize', this.el).append(dataExpression.getFormatByteData(model.get('dwFileSize')));
            $('#host', this.el).append(model.get('strHost'));
            $('#hash', this.el).append(model.get('strFileHash'));
            if (parseInt(model.get('dwFileSize')) == 0) {
                $('.fileDownload', this.el).attr('disabled', true);
            } else {
                $('.fileDownload', this.el).attr('disabled', false);
            }
        },
        setMonitoringDefinition: function () {
            $('#fileType', this.el).append(this.fileMetaMonitoringModel.get('strMagic'));
            $('#tmLogTime', this.el).append(this.fileMetaMonitoringModel.get('tmLogTime'));
            $('#vsensorName', this.el).append(this.fileMetaMonitoringModel.get('vsensorName'));
            $('#sensorName', this.el).append(this.fileMetaMonitoringModel.get('sensorName'));
            $('#sip', this.el).append(this.fileMetaMonitoringModel.get('bIpType') == 4 ? this.fileMetaMonitoringModel.get('dwSourceIp') : this.fileMetaMonitoringModel.get('strSourceIp'));
            $('#dip', this.el).append(this.fileMetaMonitoringModel.get('bIpType') == 4 ? this.fileMetaMonitoringModel.get('destinationIp') : this.fileMetaMonitoringModel.get('strDestinationIp'));
            $('#sPort', this.el).append(this.fileMetaMonitoringModel.get('nSourcePort'));
            $('#dPort', this.el).append(this.fileMetaMonitoringModel.get('nDestinationPort'));
            $('#fileName', this.el).append(this.fileMetaMonitoringModel.get('strFileName'));
            $('#fileSize', this.el).append(dataExpression.getFormatByteData(this.fileMetaMonitoringModel.get('dwFileSize')));
            $('#host', this.el).append(this.fileMetaMonitoringModel.get('strHost'));
            $('#hash', this.el).append(this.fileMetaMonitoringModel.get('strFileHash'));
            if (parseInt(this.fileMetaMonitoringModel.get('dwFileSize')) == 0) {
                $('.fileDownload', this.el).attr('disabled', true);
            } else {
                $('.fileDownload', this.el).attr('disabled', false);
            }
        },
        showsIpRetrievePopup: function () {

            if (this.menuType == "FileMetaDetectionMonitoring") {
                this.ipValue = this.fileMetaMonitoringModel.get('bIpType') == 4 ? this.fileMetaMonitoringModel.get('dwSourceIp') : this.fileMetaMonitoringModel.get('strSourceIp');
            } else if (this.menuType == "FileMetaDetection") {
                this.ipValue = this.fileMetaModel.get('bIpType') == 4 ? this.fileMetaModel.get('dwSourceIp') : this.fileMetaModel.get('strSourceIp');
            }

            Backbone.ModalView.msg({
                size: 'medium-large',
                title: 'IP ' + locale.retrieve,
                body: new TbIpRetrieve({
                    ip: this.ipValue //this.fileMetaModel.get('dwSourceIp')
                })
            });
        },
        showdIpRetrievePopup: function () {

            if (this.menuType == "FileMetaDetectionMonitoring") {
                this.ipValue = this.fileMetaMonitoringModel.get('bIpType') == 4 ? this.fileMetaMonitoringModel.get('destinationIp') : this.fileMetaMonitoringModel.get('strDestinationIp');
            } else if (this.menuType == "FileMetaDetection") {
                this.ipValue = this.fileMetaModel.get('bIpType') == 4 ? this.fileMetaModel.get('deDestinationIp') : this.fileMetaModel.get('strDestinationIp');
            }

            Backbone.ModalView.msg({
                size: 'medium-large',
                title: 'IP ' + locale.retrieve,
                body: new TbIpRetrieve({
                    ip: this.ipValue //this.fileMetaModel.get('deDestinationIp')
                })
            });
        },
        fileDownLoadCursor: function () {
            $('#fileName', this.el).css('cursor', 'pointer');
        },
        fileDownload: function () {
            var fileName = "";
            if (this.menuType == "FileMetaDetectionMonitoring") {
                fileName = this.fileMetaMonitoringModel.get('strStoreFileName');
            } else if (this.menuType == "FileMetaDetection") {
                fileName = this.fileMetaModel.get('strStoreFileName');
            }
//            window.location.href="api/detectionAnalysis/fileDownload.do?fileName=" + fileName;
            window.location.href = "api/common/fileDownload.do?type=fileMeta&code=" + this.lindex + "&startDateInput=" + this.startDateInput + "&endDateInput=" + this.endDateInput;
        }
    });
});