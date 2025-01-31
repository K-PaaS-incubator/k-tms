define(function(require) {
	
	"use strict";
	
	var $ 					= require('jquery'),
		_ 					= require('underscore'),
		Backbone 			= require('backbone'),
		bootstrap 			= require('bootstrap'),
		locale 				= require('i18n!nls/str');

	var basicPaginationTpl 	= require('text!tpl/common/basicPagination.html');

	var Pagination = Backbone.View.extend({
		paginationLiTemplate: _.template('<li><a href="#" class="page"><span><%= pageNumber %></span></a></li>'),
		paginationLiActiveTemplate: _.template('<li><a href="#" class="page"><span class="page_active"><%= pageNumber %></span></a></li>'),
		leftBtnTemplate: _.template('<li><a href="#" class="glyphicon glyphicon-chevron-left prev" ><span></span></a></li>'),
		rightBtnTemplate: _.template('<li><a href="#" class="glyphicon glyphicon-chevron-right next"><span></span></a></li>'),
		tagName: 'ul',
		className: 'pagination',
		initialize: function(options) {
			this.rowSize = 10; 			// 결과행수 리스트 로우수
			this.totalRowSize = null; 	// 총 리스트 로우수
			this.currentPage = 1; 		// 현재페이지
			this.blockSize = 5; 		// 페이지 개수 범위 10일때 1~10, 5일때 1~5 
			this.totalBlockSize; 		// 페이지 개수 범위의 총값
			this.totalPages; 			// 총페이지
			this.startPage; 			// 시작 페이지
			this.endPage; 				// 마지막 페이지
			this.currentBlock; 			// 현재 블럭범위 값 
			this.evt = options.evt;

			//			next = this.getNext();			// 다음 페이지으로 이동
			//			previous = this.getPrevious();	// 앞페이지로 이동
			//			pageSet = this.getPageSet();	// 페이지 값들 계산
			//			leftTruncated = this.leftTruncated(); // 페이지 전체를 왼쪽으로 이동
			//			rightTruncated = this.rightTruncated(); // 페이지 전체를 오른쪽으로 이동  

		},
		events: {
			//'click a.first': 'goToFirst',
			'click a.prev': 'goToPrev',
			'click a.page': 'goToPage',
			'click a.next': 'goToNext',
			//'click a.last': 'goToLast'
		},
		render: function() {
			var self = this;
			self.$el.empty();
			self.$el.append(self.leftBtnTemplate());

			var startRenderPage = parseInt(self.currentBlock * self.blockSize - self.blockSize) + parseInt(1);
			var endRenderPage = parseInt(self.currentBlock * self.blockSize);

			if (this.totalPages <= endRenderPage) {
				endRenderPage = this.totalPages;
			}
			for (var i = startRenderPage; i <= endRenderPage; i++) {
				if(i == self.currentPage) {
					self.$el.append(self.paginationLiActiveTemplate({
						pageNumber: i
					}));					
				}
				else {
					self.$el.append(self.paginationLiTemplate({
						pageNumber: i
					}));					
				}
			}
			self.$el.append(self.rightBtnTemplate());
			
			if(this.currentPage <= 1) {
				// 현재페이지가 1일때 < 에 효과없애기
				$('ul.pagination').children('li:first-child',this.el).removeClass('active');
				$('ul.pagination').children('li:first-child',this.el).addClass('disabled');
			}
			if(this.currentPage >= this.totalPages) {
				// 현재페이지가 마지막페이지일때, >효과없애기, 
				$('ul.pagination').children('li:last-child',this.el).removeClass('active');
				$('ul.pagination').children('li:last-child',this.el).addClass('disabled');
			}
			
			return this;
		},
		getPaginationRowValue: function() {
			return {
				'startRowSize': 0,
				'endRowSize': this.rowSize,
                                'rowSize': this.rowSize
			};
		},
		setInitialization: function() {
			// 초기화
			this.rowSize = 10; 			// 결과행수 리스트 로우수
			this.totalRowSize = null; 	// 총 리스트 로우수
			this.currentPage = 1; 		// 현재페이지
			this.blockSize = 10; 		// 페이지 개수 범위 10일때 1~10, 5일때 1~5 
			this.totalBlockSize = null; // 페이지 개수 범위의 총값
			this.totalPages = null; 	// 총페이지
			this.startPage = null; 		// 시작 페이지
			this.endPage = null; 		// 마지막 페이지
			this.currentBlock = null; 	// 현재 블럭범위 값 
		},
		setTotalRowSize: function(total) {

			this.totalRowSize = parseInt(total);
			var endRowSize;

			// 화면에 그릴페이징 정보를 호출 
			this.getPageSet();
		},
		getPageSet: function() {
			// 페이지 총개수 
			this.totalPages = Math.ceil(parseInt(this.totalRowSize) / parseInt(this.rowSize));
			// 페이지 N개씩 한화면에서 페이지를 보여주기 위한 총 블럭수 ( 방향키사용에 필요) 
			this.totalBlockSize = Math.ceil(parseInt(this.totalRowSize) / parseInt(this.blockSize));
			// 현재 블록 
			this.currentBlock = Math.ceil(parseInt(this.currentPage) / parseInt(this.blockSize));
			// 시작페이지 
			this.startPage = Math.floor(((parseInt(this.currentPage) - 1) / parseInt(this.blockSize)) * (parseInt(this.blockSize) - 1));
			// 끝 페이지 
			this.endPage = (parseInt(this.startPage) - 1) + parseInt(this.blockSize);
			// 이전 페이지
			this.previous = parseInt(this.startPage) - parseInt(this.blockSize);
			// 다음페이지
			this.next = ((parseInt(this.currentBlock) + 1) * parseInt(this.blockSize) - (parseInt(this.blockSize) - 1));
			this.render();
		},
		goToNextPage: function(e) {
			if (this.currentPage < this.totalPages) {
				$(e.target, this.el).parent('li', this.el).addClass('active');
				this.currentPage++;
				this.pager();
			} else if (this.currentPage >= this.totalPages) {
				$(e.target, this.el).parent('li', this.el).removeClass('active');
				$(e.target, this.el).parent('li', this.el).addClass('disabled');
			}
		},
		goToPreviousPage: function(e) {
			if (this.currentPage > 1) {
				$(e.target, this.el).parent('li', this.el).addClass('active');
				this.currentPage--;
				this.pager();
			} else if (this.currentPage <= 1) {
				$(e.target, this.el).parent('li', this.el).removeClass('active');
				$(e.target, this.el).parent('li', this.el).addClass('disabled');
			}
		},
		// 맨처음 페이지로 이동
		goToFirst: function() {
			this.goTo(1);
		},
		// 맨 마지막 페이지로 이동
		goToLast: function() {
			this.goTo(this.totalPages);
		},
		goTo: function(e) {
			var page = $(e.target).text();
			if (page !== undefined) {
				this.currentPage = parseInt(page, 10);
				//TODO 클릭한 현재 페이지에 active 주기 
				//$(e.target, self.el).parent('li', self.el).addClass('active');
				this.pager();
			}
		},
		pager: function() {
			var startRowSize = (this.currentPage - 1) * this.rowSize;
			var endRowSize = parseInt(startRowSize) + parseInt(this.rowSize);

			if (!this.totalRowSize) {
				this.totalPages = Math.ceil(this.totalRowSize / this.rowSize);
			}

			this.getPageSet();
			
			// 모델을 이용하여 새탭을 출력하는 이벤트 호출
			this.evt.trigger('pagination', {
				'startRowSize': startRowSize,
				'endRowSize': endRowSize,
                                'rowSize': this.rowSize
			});
		},
		getNext: function() {
			//this.next = ((parseInt(this.currentBlock) +1 ) * parseInt(this.blockPageSize) - (parseInt(this.blockPageSize) - 1));	
			return (this.currentPage < this.totalPages) ? this.currentPage + 1 : false;
		},
		getPrevious: function() {
			return (this.currentPage > 1) ? this.currentPage - 1 : false;
		},
		leftTruncated: function() {
			return this.getPageSet()[0] > 1;
		},
		rightTruncated: function() {
			var pageSet = this.getPageSet();
			return pageSet[pageSet.length - 1] < this.totalPages;
		},
		goToPage: function(e) {
			e.preventDefault();
			this.goTo(e);
		},
		goToPrev: function(e) {
			e.preventDefault();
			this.goToPreviousPage(e);
		},
		goToNext: function(e) {
			e.preventDefault();
			this.goToNextPage(e);
		},
	});

	return Pagination;
});
