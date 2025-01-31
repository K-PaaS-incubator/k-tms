define(function(require) {
	
	"use district";

	var $ 				 	= require('jquery'),
		_ 				 	= require('underscore'),
		Backbone 		 	= require('backbone'),
		jquerySlimScroll 	= require('jquerySlimScroll'),
		jqueryScrollTabs 	= require('jqueryScrollTabs'),
		jqueryMouseWheel 	= require('jqueryMouseWheel');

	var tabTpl 				= require('text!tpl/popup/tabTemplate.html');

	var TabHeaderView = Backbone.View.extend({
		tagName: 'li',
		template: _.template('<span><a href="#<%= key %>" role="tab" data-toggle="tab"><%= title %></a></span>'),
		closeButtonTemplate: _.template('<button type="button" class="closetab icon-tab-btn"></button>'),
		initialize: function(options) {
			options = options || {};
			this.tabInfo = options.tabInfo || {};
			this.removable = options.removable;
			if (this.removable == undefined) {
				this.removable = true;
			}
		},
		render: function() {
			this.$el.html(this.template(this.tabInfo));

			if (this.removable) $('a', this.el).append(this.closeButtonTemplate());
			
			return this;
		}
	});

	return Backbone.View.extend({
		events: {
			"click #scroll-tab ul > li:not(#plusTab) a": "showTab",
			"click #plusTab a"	: "plusTab",
			"click .closetab"	: "removeTab"
		},
		initialize: function(options) {
			//	newTab: true or false, : 탭 헤더의 오른쪽 끝에 + 탭이 나타나게 한다.
			//	ContentType: Content Prototype : + 탭을 클릭할 경우 생성되는 Content 유형
			
			options = options || {};
			this.tabs = [];

			if (options.newTab) {
				this.newTabFlag = options.newTab;
				this.ContentType = options.ContentType;
			}
		},
		template: _.template(tabTpl),
		render: function() {
                        CURRENT_TAB_INDEX = undefined;
                        TAB_MAP = new newMap();
			this.$el.html(this.template());

			$('#scroll-tab', this.el).scrollTabs();
			this.$tabHeader = $('#scroll-tab ul', this.el);
			this.$tabContent = $('.tab-content', this.el);

			if (this.newTabFlag) {
				this.$tabHeader.append('<li id="plusTab"><span><a role="tab" data-toggle="tab" class="cursor-pointer">+</a></span></li>');
			}

			return this;
		},
		addTab: function(tab, removable) {
			var newTab = new TabHeaderView({
				parent: this,
				tabInfo: tab.getInfo(),
				removable: removable
			});

			if (this.newTabFlag) {
				var $insertLocation = $('#scroll-tab ul > li:last-child', this.el);
				$insertLocation.before(newTab.el);
			} else {
				this.$tabHeader.append(newTab.el);
			}

			newTab.render();

			this.$tabContent.append(tab.el);
			tab.render();
			this.tabs.push({
				header: newTab,
				content: tab
			});
			this.showTab(this.tabs.length - 1);
                        TAB_MAP.put(this.tabs.length - 1, WC_KEY);
		},
		plusTab: function(e, condition) {
			e.stopPropagation();
			var content = null;
			if (this.newTabFlag) {
				content = new this.ContentType({
					tabView: this,
					condition: condition
				});
				this.addTab(content);
			}
			return content;
		},
		/**
		 * 탭을 활성화하는 함수
		 *   - 탭에 active 클래스를 추가한다.
		 *   - 활성탭뷰의 이벤트 bind 한다.
		 * param[0] : events or number
		 */
		showTab: function(e) {
			var tabIndex = (typeof e === "number" ? e : this.$("#scroll-tab .nav li").index($(e.target).closest("li")));

			if (tabIndex < 0) return;
			this.hideTab();
			$("#scroll-tab .nav li:eq(" + (tabIndex) + ")", this.el).addClass('active');
			$(".tab-content > div:eq(" + (tabIndex) + ")", this.el).addClass('active');
                        CURRENT_TAB_INDEX = tabIndex;
		},
		/**
		 * 모든 탭을 비활성화하는 함수
		 *   - 모든 탭에 active 클래스를 삭제한다.
		 *   - 모든 탭뷰의 이벤트도 unbind 한다.
		 */
		hideTab: function() {
			this.$("#scroll-tab .nav li, .tab-content div").removeClass('active');
		},
		/**
		 * 동적탭을 삭제하는 함수
		 *   - 삭제탭을 제거한다.
		 *   - 활성탭을 재조정한다.
		 */
		removeTab: function(e) {
			var totalTabLength = this.$("#scroll-tab .nav > li").length - 1; // 전체탭 사이즈
			var showTabIndex = this.$("#scroll-tab .nav > li.active").index(); // 활성탭 인덱스
			var delTabIndex = this.$("#scroll-tab .nav > li").index($(e.target).closest("li")); // 삭제탭 인덱스

			this.$("#scroll-tab .nav li:eq(" + delTabIndex + "), .tab-content > div:eq(" + delTabIndex + ")").remove();
			// 탭 정보를 담은 배열에서 삭제탭을 제거한다.
//			var tab = this.tabs.splice(delTabIndex, 1);
//			tab[0].header.close();
//			tab[0].content.close();

			/**
			 *  활성화 탭을 선택한다.
			 *	1. 활성탭과 삭제탭이 동일하며 마지막탭일 경우에는 삭제탭인덱스 - 1
			 *  2. 활성탭과 삭제탭이 동일하면 마지막탭이 아닐 경우에는 삭제탭인덱스 + 1
			 *  3. 활성탭이 삭제탭보다 크면 활성탭인덱스 - 1
			 *  4. 그 외는 활성탭인덱스
			 */
			var tabIndex;
			if (this.newTabFlag) {
                            if (showTabIndex >= delTabIndex) {
                                tabIndex = showTabIndex - 1;
                            } else {
                                tabIndex = showTabIndex;
                            }
//				tabIndex = (showTabIndex == delTabIndex && showTabIndex == (totalTabLength - 1)) ? delTabIndex - 1 :
//					(showTabIndex == delTabIndex && showTabIndex < totalTabLength) ? delTabIndex + 1 :
//					(showTabIndex > delTabIndex) ? showTabIndex - 1 : showTabIndex;
			} else {
				tabIndex = (showTabIndex == delTabIndex && showTabIndex == totalTabLength) ? delTabIndex - 1 :
					(showTabIndex == delTabIndex && showTabIndex < totalTabLength) ? delTabIndex + 1 :
					(showTabIndex > delTabIndex) ? showTabIndex - 1 : showTabIndex;
			}
                        this.tabWcRemove(delTabIndex);
			this.showTab(tabIndex);
		},
		onClose: function() {
			var tab = null;
			// 탭뷰에 대한 이벤트 clear
			while ((tab = this.tabs.pop()) != undefined) {
				tab.header.close();
				tab.content.close();
			}
		},
                tabWcRemove: function(index) {
                    TAB_MAP.remove(index)
                    if ((index+1) != this.tabs.length) {
                        for (var i = index ; i < this.tabs.length ; i ++) {
                            var obj = TAB_MAP.get(i+1);
                            TAB_MAP.put(i, obj);
                            TAB_MAP.remove(i+1);
                        }
                    }
                    this.tabs.splice(index,1);
                }
	});
});
