/**
 *
 * target view
 */
define(function (require) {

    "use strict";

    // require library
    var $ = require('jquery'),
            Backbone = require('backbone'),
            sessionManager = require('utils/sessionManager');

    // require model, collection, view
    var TargetOrgCollection = require('collections/common/targetCollection');
    var TargetSensorCollection = require('collections/common/targetCollection');

    var locale = require('i18n!nls/str');

    // require template
    // 검색부 공통 사용 모듈 가져오기
    var targetTypeViewTpl = require('text!tpl/common/target.html'),
            targetTypeTemplate = _.template(targetTypeViewTpl);

    var dataExpression = require('utils/dataExpression');

    /*
     * 값이 null 로 세팅 되어져야 하는 컬럼에는 -100 을 fix 해 놓음. 쿼리에서 -100 이 아닌 경우 조건을 넣어준다.
     *
     * */
    var TargetView = Backbone.View.extend({
        orgParentPlusTemplate: _.template('<li class="parentTarget<%= target.lIndex %> overflow-ellipsis"><a class="cursor-pointer" title="<%=targetName%>"><span class="icon-plus-sign" class="cursor-pointer"></span><span class="tree-icon <%=targetImg%>"></span><%=targetName%></a><ul></ul></li>'),
        orgParentMinusTemplate: _.template('<li class="parentTarget<%= target.lIndex %> overflow-ellipsis"><a title="<%=targetName%>"><span class="icon-minus-sign"></span><span class="tree-icon <%=targetImg%> cursor-pointer"></span><%=targetName%></a><ul></ul></li>'),
        orgChildTemplate: _.template('<li class="target<%= target.lIndex %> overflow-ellipsis"><a class="tree-icon network cursor-pointer" title="<%=targetName%>"><%=targetName%></a></li>'),
        orgParentMinusTemplate2: _.template('<li class="target<%= target.lIndex %> overflow-ellipsis"><a class="network cursor-pointer" title="<%=targetName%>"><span class="icon-minus-sign"></span><span class="tree-icon <%=targetImg%> cursor-pointer"></span><%=targetName%></a><ul></ul></li>'),
        sensorParentTemplate: _.template('<li class="parentTarget<%= target.lIndex %> overflow-ellipsis"><a class="cursor-pointer" title="<%=targetName%>"><span class="<%=targetSubYn%>"></span><span class="<%=targetImage%> cursor-pointer" ></span><%=targetName%></a><ul></ul></li>'),
        sensorSensorChildTemplate: _.template('<li class="sensorTarget<%= target.lIndex %> overflow-ellipsis"><a class="<%=targetImage%> cursor-pointer" title="<%=targetName%>"><%=targetName%></a><ul></ul></li>'),
        sensorNetworkChildTemplate: _.template('<li class="target<%= target.lIndex %> overflow-ellipsis"><a class="tree-icon network cursor-pointer" title="<%=targetName%>"><%=targetName%></a></li>'),
        targetCategory: sessionManager.Category, // 범위 분류 (0: 전체, 1: 네트워크 그룹, 2: 네트워크)
        targetRefIndex: sessionManager.RefIndex, // 네트워크 그룹 또는 네트워크 인덱스
        targetPathName: sessionManager.PathName,
        events: {
            "click #allChk": "clickAllChk"			// 전체
        },
        initialize: function (options) {
            options = options || {};
            this.viewType = options.viewType || 0;    // 0 : 전체(ALL), 1 : 네트워크 제외 2: 센서 제외
            this.indexType = 0; // 조회대상에 따라 인덱스 값을 달리한다. 화면에 따라 값이 필요한 경우가 있음.
            // 전체 =0; 네트워크그룹 =1; 네트워크그룹>네트워크 :2; 가상센서 :3; 가상센서 >네트워크:4; 가상센서> 센서 : 5; 
            this.targetType = options.targetType || 0;	// 0 : 조회대상 기관, 1 : 조회대상 센서
            //이 부분 정리 필요 함
            if (JSON.stringify(options) == "{}") { //디폴트 조회 시
                this.selPath = this.targetPathName;
                if (this.targetCategory == 0) {
                    this.selNetworkGroup = 0, this.selNetwork = 0, this.selVsensor = 0, this.selSensor = null;
                } else if (this.targetCategory == 2) { //네트워크로 로그인
                    this.selNetworkGroup = null, this.selNetwork = this.targetRefIndex, this.selVsensor = null, this.selSensor = -1;
                }
            } else { //트리에서 선택
                this.selNetwork = options.lnetworkIndex;
                this.selPath = options.pathName;
            }

            this.targetOrgCollection = new TargetOrgCollection();
            this.targetSensorCollection = new TargetSensorCollection();

            var locationUrl = dataExpression.getUrl(document.location.href);
            //var superUrl = locationUrl.split("_");
            this.superMenuName = locationUrl;
        },
        render: function () {
            var self = this;

            $.when(function () {
                self.$el.html(targetTypeTemplate({
                    locale: locale,
                    targetCategory: self.targetCategory,
                    targetRefIndex: self.targetRefIndex,
                    superMenuName: self.superMenuName,
                    viewType: self.viewType
                }));
            }()).done(function () {
                var searchParam = {'refIndex': self.targetRefIndex, 'category': self.targetCategory};
                // 기관 fetch
                if (self.viewType != 3) {
                    if (self.targetCategory == 0 || self.targetCategory == 1 || self.targetCategory == 2) {
                        self.targetOrgCollection.fetch({
                            method: 'POST',
                            contentType: 'application/json',
                            url: 'api/common/selectTargetOrg',
                            data: JSON.stringify(searchParam),
                            success: function () {
                                self.orgDataView();

                                if (self.selNetwork == 0) {
                                    self.clickAllChk();
                                } else {
                                    $('.targetOrgTree .target' + self.selNetwork + ' a', self.el).eq(0).trigger("click");
                                }
                            }
                        });
                    }
                }
            });
            return this;
        },
        // 기관 ( 네트워크 )
        orgDataView: function () {
            var thisView = this;
            //그룹의 갯수 만큼 each 문을 돌려서, <li></li> 태그 그리기
            thisView.targetOrgCollection = thisView.targetOrgCollection.toJSON();
            if (thisView.targetOrgCollection.length > 0) {
                _.each(thisView.targetOrgCollection, function (target) {
                    thisView.targetImg = "network";

                    thisView.$(".targetOrgTree", thisView.el).append(thisView.orgParentMinusTemplate2({
                        target: target,
                        targetName: target.strName,
                        targetImg: thisView.targetImg
                    }));

                    thisView.$('.targetOrgTree .target' + target.lIndex + ' a', thisView.el).click(function () {
                        $("#path", thisView.el).text(target.path);
                        $("#path", thisView.el).attr("title", target.path);
                        thisView.targetType = 0;
                        thisView.selPath = target.path;
                        thisView.selVsensor = null;
                        thisView.selSensor = -1;
                        thisView.selNetworkGroup = null;
                        thisView.selNetwork = target.lIndex;
                        thisView.indexType = 2;

                    });

                });
            } else {
                $('.networkTree').css('height', '50px');
            }
            $(function () {
                $('.tree .targetOrgTree li li', thisView.el).css("display", "none");
                $('.tree .targetOrgTree li:has(ul)', thisView.el).addClass('parent_li').find(' > a');
                $('.tree .targetOrgTree li.parent_li > a > span', thisView.el).on('click', function (e) {
                    var children = $(this).closest('.targetOrgTree li.parent_li').find(' > ul > li');
                    if (children.is(":visible")) {
                        children.hide('fast');
                        $(this).addClass('icon-plus-sign').removeClass('icon-minus-sign');
                    } else {
                        children.show('fast');
                        $(this).addClass('icon-minus-sign').removeClass('icon-plus-sign');
                    }
                    e.stopPropagation();
                });
            });
        },
        //트리에서 전체 선택 시 값 세팅
        clickAllChk: function () {
            var thisView = this;

            $("#path", thisView.el).text("");
            $("#path", thisView.el).text(locale.all);	// 경로 출력

            thisView.targetType = 0;
            thisView.selPath = locale.all;

            thisView.selVsensor = 0;
            thisView.selSensor = null;
            thisView.selNetworkGroup = 0;
            thisView.selNetwork = 0;
            thisView.indexType = 0;
      },
        // 하위 네트워크가 있는지 유무 체크
        orgDataPlusMinus: function (lindex) {
            var thisView = this;
            var hasChild = false;
            _.each(thisView.targetOrgCollection, function (target) {
                if (lindex == target.lParentGroupIndex) {
                    hasChild = true;
                }
            });
            return hasChild;
        },
        // 센서 하위 존재 유무
        sensorDataPlusMinus: function (lindex) {
            var thisView = this;
            var hasChild = false;
            _.each(thisView.targetSensorCollection, function (target) {
                if (lindex == target.lParentGroupIndex) {
                    hasChild = true;
                }
            });
            return hasChild;
        },
        currentSelOrgSensor: function () {
//			+"\nlnetgroupIndex:"+ this.selNetworkGroup
//			+"\nlnetworkIndex:"+ this.selNetwork
//			+"\nlvsensorIndex:"+ this.selVsensor
//			+"\nlsensorIndex:"+ this.selSensor
//			+"\npathName:"+ this.selPath
//			);

            return {
                targetType: this.targetType, // targetType
                lnetgroupIndex: this.selNetworkGroup, // 네트워크 그룹 YN
                lnetworkIndex: this.selNetwork, // 네트워크	 YN
                lvsensorIndex: this.selVsensor, // 가상센서	 YN
                lsensorIndex: this.selSensor, // 센서		 YN
                //pathName: this.selPath,
                //pathName : escape(encodeURIComponent(this.selPath)),
                pathName: encodeURIComponent(this.selPath),
                indexType: this.indexType
            };
        }
    });

    TargetView.MakeTarget = function ($el, options) {
        options = options || {};

        var target = new TargetView(options);

        $el.html(target.render().el);
        return target;
    };

    return TargetView;
});