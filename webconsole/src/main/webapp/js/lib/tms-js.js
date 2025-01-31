//$(document).ready(function(){
//	alert("$(document).ready ALERT 경고창");
//});
//
//window.onload = function(){
//	alert("window.onload ALERT 경고창");
//};

$(document).on('click', ".btn", function() {
	$(this).blur();
});	

$(document).on('click', ".detailBtn", function() {
	$("#detail-search").toggle("slow");
});

$(document).ready(function() {
	
	setTimeout(function() {
		
		ResizeContentList();
		
		// 설정 - '보안정책', '설정' 등 좌측 트리 메뉴 컨트롤 - 배경 세로 100% 채우기, 스크롤 영역 사이즈 지정 등
		$("#sidebar").on('click', function() {
			
	    	setTimeout(function() {
	    		ResizeContentList();
	    	}, 800);
	    });
		$(".brand-image").on('click', function() {
			
	    	setTimeout(function() {
	    		ResizeContentList();
	    	}, 800);
	    });
	}, 1000);
	
});

$(window).resize(function() {
	maxHeight = $(document).height() - 135;	// header + footer + subside = 235
	maxHeight_margin = maxHeight-150;
});

function ResizeContentList() {
	/*

	if($(".content-tree-list").length == 1) {
		$(".content").css("height", $(window).height()-40+"px");
		$(".content-tree-list").css("max-height", $(".content").height()-130+"px");
		$(".content-tree-list").css("height", $(".content").height()-130+"px");
		$(".content-list-setup-left").animate({height:$(".content").height()+"px"}, 100);
		$(".tabContent").css("max-height", $(".content").height()-120+"px");
	}
	else {
		$(".content").css("height", "");
	}
	*/
	
}
//아코디언 모두펼치기, 모두닫기 버튼 이벤트 
//$("#accordion-open").click(function(){
//$(".collapse").collapse('show');
//});

$(document).on('click', "#accordion-open", function() {
	//$(".collapse").collapse('show');
	$(".collapse").addClass('in');
});

$(document).on('click', "#accordion-close", function() {
	//$(".collapse").collapse('hide');
	$(".collapse").removeClass('in');
});

$.fn.fixedHeader = function (options) {
 var config = {
   topOffset: 40,
   bgColor: '#EEEEEE'
 };
 if (options){ $.extend(config, options); }

 return this.each( function() {
  var o = $(this);

  var $win = $(window)
    , $head = $('thead.header', o)
    , isFixed = 0;
  var headTop = $head.length && $head.offset().top - config.topOffset;

  function processScroll() {
    if (!o.is(':visible')) return;
    var i, scrollTop = $win.scrollTop();
    var t = $head.length && $head.offset().top - config.topOffset;
    if (!isFixed && headTop != t) { headTop = t; }
    if      (scrollTop >= headTop && !isFixed) { isFixed = 1; }
    else if (scrollTop <= headTop && isFixed) { isFixed = 0; }
    isFixed ? $('thead.header-copy', o).removeClass('hide')
            : $('thead.header-copy', o).addClass('hide');
  }
  $win.on('scroll', processScroll);

  // hack sad times - holdover until rewrite for 2.1
  $head.on('click', function () {
    if (!isFixed) setTimeout(function () {  $win.scrollTop($win.scrollTop() - 47); }, 10);
  });

  $head.clone().removeClass('header').addClass('header-copy header-fixed').appendTo(o);
  var ww = [];
  o.find('thead.header > tr:first > th').each(function (i, h){
    ww.push($(h).width());
  });
  $.each(ww, function (i, w){
    o.find('thead.header > tr > th:eq('+i+'), thead.header-copy > tr > th:eq('+i+')').css({width: w});
  });

  o.find('thead.header-copy').css({ margin:'0 auto',
                                    width: o.width(),
                                   'background-color':config.bgColor });
  processScroll();
 });
};

$("#toggle-field").click(function(){
  $(".toggle-field").toggle("slow");
});

$(".has-subcategory-subitem>a").click(function(){
  $(this).siblings('ul').toggle("slow");
});

$(function () {
	$('.tree li li').css("display","none");
	$('.tree li:has(ul)').addClass('parent_li').find(' > a');
    $('.tree li.parent_li > a > span').on('click', function (e) {
        var children = $(this).closest('li.parent_li').find(' > ul > li');
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

//$(function () {
//  $('[data-toggle="tooltip"]').tooltip();
//});

//// 보안정책의 정책목록 스크롤을 위한 스크립트로 150, 170은 레이아웃의 header 높이 + 목록상단 내용 높이 입니다.
var viewportHeight = $("body").innerHeight();
$('.content-scroll').css("height",viewportHeight-150+'px');

$(window).resize(function() {
	var windowHeight = $(window).height();
	$('.content-scroll').css("height",windowHeight-170+'px','important');
	
	ResizeContentList();
});

// 보안정책 폼 overlay
$(".btn-overlay-form").click(function(){
	var overlayWidth = $(".content-white").width();
	var overlayHeight = $(".content-white").height();
	var styles = {
	      width : overlayWidth+'40'+'px',
	      height: overlayHeight+'40'+'px'
	    };
	$('.overlay-form').css( styles );
});
$(".btn-overlay-form-clear").click(function(){
	$('.overlay-form').css({'width':'0', 'height':'0'});
});

function PopupWindow() {
	window.open('/js/tpl/popupWin/popupWin.html', "tms_popup", "width=600, height=400, scrollbars=0, location=0, resizable=0, titlebar=0");
}

$.fn.digits = function(){ 
    return this.each(function(){ 
        $(this).text( $(this).text().replace(/(\d)(?=(\d\d\d)+(?!\d))/g, "$1,") ); 
    })
}


var waitingDialog = waitingDialog || (function ($) {

	// Creating modal dialog's DOM
	var $dialog = $(
		'<div class="modal fade" data-backdrop="static" data-keyboard="false" tabindex="-1" role="dialog" aria-hidden="true" style="padding-top:15%; overflow-y:visible;">' +
		'<div class="modal-dialog modal-m">' +
		'<div class="modal-content">' +
			'<div class="modal-header"><h3 style="margin:0;"></h3></div>' +
			'<div class="modal-body">' +
				'<div class="progress progress-striped active" style="margin-bottom:0;"><div class="progress-bar" style="width: 100%"></div></div>' +
			'</div>' +
		'</div></div></div>');

	return {
		/**
		 * Opens our dialog
		 * @param message Custom message
		 * @param options Custom options:
		 * 				  options.dialogSize - bootstrap postfix for dialog size, e.g. "sm", "m";
		 * 				  options.progressType - bootstrap postfix for progress bar type, e.g. "success", "warning".
		 */
		show: function (message, options) {
			// Assigning defaults
			if (typeof options === 'undefined') {
				options = {};
			}
			if (typeof message === 'undefined') {
				message = 'Loading';
			}
			var settings = $.extend({
				dialogSize: 'm',
				progressType: '',
				onHide: null // This callback runs after the dialog was hidden
			}, options);

			// Configuring dialog
			$dialog.find('.modal-dialog').attr('class', 'modal-dialog').addClass('modal-' + settings.dialogSize);
			$dialog.find('.progress-bar').attr('class', 'progress-bar');
			if (settings.progressType) {
				$dialog.find('.progress-bar').addClass('progress-bar-' + settings.progressType);
			}
			$dialog.find('h3').text(message);
			// Adding callbacks
			if (typeof settings.onHide === 'function') {
				$dialog.off('hidden.bs.modal').on('hidden.bs.modal', function (e) {
					settings.onHide.call($dialog);
				});
			}
			// Opening dialog
			$dialog.modal();
		},
		/**
		 * Closes dialog
		 */
		hide: function () {
			$dialog.modal('hide');
		}
	};

});


// sidebar sub-menu scrolling issue start //

var maxHeight = $(document).height() - 135;	// header + footer + subside = 235
var maxHeight_margin = maxHeight-150;

$(document).on('mouseover', ".sidebar", function(){
    
    var $container = $(this),
        $list = $(".sidebar").find(".nav"),
        $list_height = $list.height();
        $multiplier = $list_height / maxHeight;
        
	$list.data("origHeight", $list.height());
   
   	if($multiplier > 1) {
		$container.mousemove(function(e) {
			var offset = $list.offset();
			var offset_top = offset.top;
			
			if(e.pageY >= 40 && e.pageY <= maxHeight_margin) {
				$list.css("margin-top", "0px");
			}
			else {
				$relative_y = (e.pageY - maxHeight_margin) * $multiplier;
				$list.css("margin-top", -$relative_y+"px");				
			}
			
		});		
	}
});


$(document).on('mouseout', ".sidebar", function(){
	$list = $(".sidebar").find(".nav");
	//$list.animate({marginTop: 0}, 1000);
});


// sidebar sub-menu scrolling issue end //


// popup loading bar start //
$(document).on('click', ".popup_loading", function() {
	
	target = $(this).data('target');
	type = $(this).data('type');
	
	PopupLoading(target, type);
});

$(document).on('click', ".popup_loading_close", function() {
	
	target = $(this).data('target');
	
	PopupLoadingClose(target);
});

function PopupLoading(target, type) {
	
	browser = GetBrowserInfo();
	//alert(browser);
	loading_class = "loading_layer";
	
	if(type == "bar") progress_height = 16;
	else progress_height = 100;
	
	if(browser == "MSIE") loading_class = "loading_layer_ie";
	
	draw_width = $("#"+target).width();
	draw_height = $("#"+target).height();
	pos_x = $("#"+target).offset().left;
	pos_y = $("#"+target).offset().top;
	inner_pos_y = (draw_height/2) - (progress_height/2);
	
	tpl_bg = "width:"+draw_width+"px;height:"+draw_height+"px;";	

	tpl_bar = "<div class='progress' style='margin-top:"+inner_pos_y+"px;'>";
	tpl_bar += "	<div class='progress-bar progress-bar-striped active' role='progressbar' aria-valuenow='45' aria-valuemin='0' aria-valuemax='100' style='width: 100%'>";
	tpl_bar += "		<span class='sr-only'>45% Complete</span>";
	tpl_bar += "	</div>";
	tpl_bar += "</div>";
	
	tpl_circle = "<div class='loading_ani01' style='margin-top:"+inner_pos_y+"px;'></div>";	
	
	tpl = "<div class='"+loading_class+"' style=\""+tpl_bg+"\">";
	tpl += eval("tpl_"+type);
	tpl += "</div>";
	
	$("#"+target).children(":first").addClass('loading_blur');	
	$("#"+target).prepend(tpl);
	
	if(browser == "MSIE" || browser == "Firefox") {
		LoadingAniAct();
	}
	
	//$(".loading_ani01").rotate({ count:4, duration:0.6, easing:'ease-out' });
	//$(".loading_ani01").css('transform','rotate(45deg)');
}

function PopupLoadingClose(target) {
	
	clearInterval(ani_do);
	
	browser = GetBrowserInfo();
	loading_class = "loading_layer";
	if(browser == "MSIE") loading_class = "loading_layer_ie";
	
	$("#"+target).find('.'+loading_class).remove();
	$("#"+target).find('.loading_blur').removeClass('loading_blur');
}
// popup loading bar end //

var ani_do;

function LoadingAniAct() {	
	loading_cnt = 0;

	ani_do = setInterval(function() {		
		loading_cnt++;
		angle = (loading_cnt%18) * 20;
		$(".loading_ani01").css('transform', 'rotate('+angle+'deg)');		
	}, 50);
	
}

function GetBrowserInfo() {

	if(!jQuery.browser){

		jQuery.browser = {};
		jQuery.browser.mozilla = false;
		jQuery.browser.webkit = false;
		jQuery.browser.opera = false;
		jQuery.browser.safari = false;
		jQuery.browser.chrome = false;
		jQuery.browser.msie = false;
		jQuery.browser.android = false;
		jQuery.browser.blackberry = false;
		jQuery.browser.ios = false;
		jQuery.browser.operaMobile = false;
		jQuery.browser.windowsMobile = false;
		jQuery.browser.mobile = false;

		var nAgt = navigator.userAgent;
		jQuery.browser.ua = nAgt;

		jQuery.browser.name  = navigator.appName;
		jQuery.browser.fullVersion  = ''+parseFloat(navigator.appVersion);
		jQuery.browser.majorVersion = parseInt(navigator.appVersion,10);
		var nameOffset,verOffset,ix;

	// In Opera, the true version is after "Opera" or after "Version"
		if ((verOffset=nAgt.indexOf("Opera"))!=-1) {
			jQuery.browser.opera = true;
			jQuery.browser.name = "Opera";
			jQuery.browser.fullVersion = nAgt.substring(verOffset+6);
			if ((verOffset=nAgt.indexOf("Version"))!=-1)
				jQuery.browser.fullVersion = nAgt.substring(verOffset+8);
		}

	// In MSIE < 11, the true version is after "MSIE" in userAgent
		else if ( (verOffset=nAgt.indexOf("MSIE"))!=-1) {
			jQuery.browser.msie = true;
			jQuery.browser.name = "Microsoft Internet Explorer";
			jQuery.browser.fullVersion = nAgt.substring(verOffset+5);
		}

	// In TRIDENT (IE11) => 11, the true version is after "rv:" in userAgent
		else if (nAgt.indexOf("Trident")!=-1 ) {
			jQuery.browser.msie = true;
			//jQuery.browser.name = "Microsoft Internet Explorer";
			jQuery.browser.name = "MSIE";
			var start = nAgt.indexOf("rv:")+3;
			var end = start+4;
			jQuery.browser.fullVersion = nAgt.substring(start,end);
		}

	// In Chrome, the true version is after "Chrome"
		else if ((verOffset=nAgt.indexOf("Chrome"))!=-1) {
			jQuery.browser.webkit = true;
			jQuery.browser.chrome = true;
			jQuery.browser.name = "Chrome";
			jQuery.browser.fullVersion = nAgt.substring(verOffset+7);
		}
	// In Safari, the true version is after "Safari" or after "Version"
		else if ((verOffset=nAgt.indexOf("Safari"))!=-1) {
			jQuery.browser.webkit = true;
			jQuery.browser.safari = true;
			jQuery.browser.name = "Safari";
			jQuery.browser.fullVersion = nAgt.substring(verOffset+7);
			if ((verOffset=nAgt.indexOf("Version"))!=-1)
				jQuery.browser.fullVersion = nAgt.substring(verOffset+8);
		}
	// In Safari, the true version is after "Safari" or after "Version"
		else if ((verOffset=nAgt.indexOf("AppleWebkit"))!=-1) {
			jQuery.browser.webkit = true;
			jQuery.browser.name = "Safari";
			jQuery.browser.fullVersion = nAgt.substring(verOffset+7);
			if ((verOffset=nAgt.indexOf("Version"))!=-1)
				jQuery.browser.fullVersion = nAgt.substring(verOffset+8);
		}
	// In Firefox, the true version is after "Firefox"
		else if ((verOffset=nAgt.indexOf("Firefox"))!=-1) {
			jQuery.browser.mozilla = true;
			jQuery.browser.name = "Firefox";
			jQuery.browser.fullVersion = nAgt.substring(verOffset+8);
		}
	// In most other browsers, "name/version" is at the end of userAgent
		else if ( (nameOffset=nAgt.lastIndexOf(' ')+1) < (verOffset=nAgt.lastIndexOf('/')) ){
			jQuery.browser.name = nAgt.substring(nameOffset,verOffset);
			jQuery.browser.fullVersion = nAgt.substring(verOffset+1);
			if (jQuery.browser.name.toLowerCase()==jQuery.browser.name.toUpperCase()) {
				jQuery.browser.name = navigator.appName;
			}
		}

		/*Check all mobile environments*/
		jQuery.browser.android = (/Android/i).test(nAgt);
		jQuery.browser.blackberry = (/BlackBerry/i).test(nAgt);
		jQuery.browser.ios = (/iPhone|iPad|iPod/i).test(nAgt);
		jQuery.browser.operaMobile = (/Opera Mini/i).test(nAgt);
		jQuery.browser.windowsMobile = (/IEMobile/i).test(nAgt);
		jQuery.browser.mobile = jQuery.browser.android || jQuery.browser.blackberry || jQuery.browser.ios || jQuery.browser.windowsMobile || jQuery.browser.operaMobile;


	// trim the fullVersion string at semicolon/space if present
		if ((ix=jQuery.browser.fullVersion.indexOf(";"))!=-1)
			jQuery.browser.fullVersion=jQuery.browser.fullVersion.substring(0,ix);
		if ((ix=jQuery.browser.fullVersion.indexOf(" "))!=-1)
			jQuery.browser.fullVersion=jQuery.browser.fullVersion.substring(0,ix);

		jQuery.browser.majorVersion = parseInt(''+jQuery.browser.fullVersion,10);
		if (isNaN(jQuery.browser.majorVersion)) {
			jQuery.browser.fullVersion  = ''+parseFloat(navigator.appVersion);
			jQuery.browser.majorVersion = parseInt(navigator.appVersion,10);
		}
		jQuery.browser.version = jQuery.browser.majorVersion;
	}

	var txt = '' + 'navigator.appName = ' + navigator.appName + '<br>' + 'navigator.userAgent = ' + navigator.userAgent + '<br><br><br>' + 'jQuery.browser.name  = ' + jQuery.browser.name + '<br>' + 'jQuery.browser.fullVersion  = ' + jQuery.browser.fullVersion + '<br>' + 'jQuery.browser.version = ' + jQuery.browser.version + '<br>' + 'jQuery.browser.majorVersion = ' + jQuery.browser.majorVersion + '<br><br><br>' + 'jQuery.browser.msie = ' + jQuery.browser.msie + '<br>' + 'jQuery.browser.mozilla = ' + jQuery.browser.mozilla + '<br>' + 'jQuery.browser.opera = ' + jQuery.browser.opera + '<br>' + 'jQuery.browser.chrome = ' + jQuery.browser.chrome + '<br>'+ 'jQuery.browser.webkit = ' + jQuery.browser.webkit + '<br>' + '<br>' + 'jQuery.browser.android = ' + jQuery.browser.android + '<br>' + 'jQuery.browser.blackberry = ' + jQuery.browser.blackberry + '<br>' + 'jQuery.browser.ios = ' + jQuery.browser.ios + '<br>' +  'jQuery.browser.operaMobile = ' + jQuery.browser.operaMobile + '<br>' + 'jQuery.browser.windowsMobile = ' + jQuery.browser.windowsMobile + '<br>' + 'jQuery.browser.mobile = ' + jQuery.browser.mobile;

	//$("#result").html(txt);
	return jQuery.browser.name;
}


// '조회대상' dropdown 여백 클릭 시 닫히지 않도록 함 
$(document).on('click', ".dropdown-target", function(e) {
	
	if($(e.target).hasClass('cursor-pointer') != true ) {
		e.stopPropagation();		
	}
});


$(document).on('click', ".chk_set_col", function() {
	
	target_col = $(this).data('col-name');
	//console.log(target_col);
	/*
	console.log($("."+target_col).is(":visible"));
	if($("."+target_col).is(":visible") == true) $("."+target_col).hide(500);
	else $("."+target_col).show(500);
	*/
	console.log("by prop : "+$(this).prop('checked'));
	if($(this).prop('checked') == true) $("."+target_col).show(200);
	else $("."+target_col).hide(200);
	
});

$(document).on('click', '.panel-dp-selected a', function(e) {
	//$(e).siblings.removeClass('panel-selected');
	//alert($(this).attr('href'));
	//$(this).siblings('a').removeClass('panel-selected');
	$('.panel-dp-selected a').removeClass('panel-selected');
	$(this).addClass('panel-selected');
	
});
