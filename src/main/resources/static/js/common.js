$(document).ready(function(){
	$("a").click(function(e){
		var href = $(this).attr("href");
		if(href == "#none" || href == "#" || href == ""){
			e.preventDefault();
		}
  	});

	$(".category_tool_mobile").on("click", function(){
		$("html").addClass("gnbOn");
		$("#gnb_tool_layout").css("display", "inline-block");
		$("#gnb_tool_layout .gnb_tool_wrap").animate({"right":0},300);
		$("#gnb_tool_layout .gnb_tool_close").animate({"right":"10px"},300);	
	});
	
	$("#gnb_tool_layout .gnb_tool_close").on("click",function(){
		$("#gnb_tool_layout .gnb_tool_wrap").animate({"right":"-200px"},300);
		$("#gnb_tool_layout .gnb_tool_close").animate({"right":"-220px"},300,function(){
			$("html").removeClass("gnbOn");
			$("#gnb_tool_layout").css("display", "none");
		});
	});

	$("#top_layout .moBtn .gnb_open a").on("click",function(){
		$("html").addClass("gnbOn");
		$("#gnb_layout .gnb_wrap,#gnb_layout .gnb_wrap .util,#gnb_layout .gnb_bottom").animate({"right":0},300);
		$("#gnb_layout .gnb_close").animate({"right":"20px"},300);
	});
	$("#gnb_layout .gnb_close").on("click",function(){
		$("#gnb_layout .gnb_wrap,#gnb_layout .gnb_wrap .util,#gnb_layout .gnb_bottom").animate({"right":"-320px"},300);
		$("#gnb_layout .gnb_close").animate({"right":"-340px"},300,function(){
			$("html").removeClass("gnbOn");
			$("#gnb_layout .gnb_wrap,#gnb_layout .gnb_wrap .util,#gnb_layout .gnb_bottom,#gnb_layout .gnb_close").removeAttr("style");
		});
	});
	$("#top_layout .moBtn .filter_open a").on("click",function(){
		var isOn = $(".filter_wrap").hasClass("on");
		if(!isOn){
			$(".filter_wrap").addClass("on");
			$("#gis_layout").addClass("filterOn");
		}
	});

	$("body").on("click", ".lp_open", function(){
		var lpID = $(this).attr("data-lp");
		$("#lp_layout").addClass("on");
		$("#lp_"+lpID).addClass("on");
		$("html").css("overflow","hidden");
	});
	$("body").on("click", ".lp_close", function(){
		$("#lp_layout").removeClass("on");
		$("#lp_layout .lp_wrap").removeClass("on lv1 lv2");
		$("html").css("overflow","");
	});

	$(".lp_open2").on("click",function(){
		var lpID = $(this).attr("data-lp");
		$("#lp_layout .lp_wrap.on").addClass("lv1");
		$("#lp_"+lpID).addClass("on lv2");
	});
	$(".lp_close2").on("click",function(){
		if($(this).parent().hasClass("lv2")){
		$("#lp_layout .lp_wrap.lv1").removeClass("lv1");
		$("#lp_layout .lp_wrap.lv2").removeClass("on lv2");
		}else{
		$("#lp_layout").removeClass("on");
		$("#lp_layout .lp_wrap").removeClass("on");
		}
	});

	$( ".selectBox select" ).selectmenu();
	$( "#pointType select" ).selectmenu({
		change:function(event,data){
		if(data.item.value == 1){
			$("#buyList").addClass("on");
		}else{
			$("#buyList").removeClass("on");
		}
	}
});


  $("#lp_layout .inputBox .fileBox input").bind( 'change', function (e){
    var _val = $(this).val();
    //console.log(">>>>>>>> _val : "+_val);
    if(_val != ""){
      $(this).parent().addClass("on");
    }
  });
  $("#lp_layout .inputBox .fileBox a").on("click",function(){
    $(this).parent().removeClass("on").find("input").val("");
    //console.log(">>>>>>>> _val : "+$(this).parent().find("input").val());
  });


	$(".tabCon_wrap").scroll(function(){
		$(".selectBox .ui-selectmenu-button-open").click();
	});
	$("#lp_layout .lp_wrap.type2 .body").scroll(function(){
		$(".selectBox .ui-selectmenu-button-open").click();
	});

});



//이미지 리사이즈
function imgResizeOut(res){
  $(res).each(function(){
	  $(this).removeAttr("class");
	  $(this).removeClass("resizeH");
	  $(this).removeClass("resizeW");

    var _boxW = $(this).parent().width();
    var _boxH = $(this).parent().height();
    var _imgW = $(this).width();
    var _imgH = $(this).height();

    if(_boxH > _imgH){
      $(this).addClass("resizeH");
    }else if(_boxH < _imgH){
      $(this).addClass("resizeW");
    }
  });
}
function imgResizeIn(res){
  $(this).removeAttr("class");
  $(this).removeClass("resizeH");
  $(this).removeClass("resizeW");
  $(res).each(function(){

    var _boxW = $(this).parent().width();
    var _boxH = $(this).parent().height();
    var _imgW = $(this).width();
    var _imgH = $(this).height();

    if(_boxH > _imgH){
      $(this).addClass("resizeW");
    }else if(_boxH < _imgH){
      $(this).addClass("resizeH");
    }
  });
}




//슬라이드 시작
function touchSlider(id,num,autoplay,slide,speed){
	var $touchSlider = $(id);
	var $autoplay = autoplay;
	var $slidNum = num;
	var $slide = slide;
	var $speed = speed;
	if($slidNum==null || $slidNum=="auto" || $slidNum==0){
		var boxWidth = $touchSlider.find(".sliderBox").width();
		var conWidth = $touchSlider.find(".sliderBox > ul > li").width();
		$slidNum = Number(boxWidth/conWidth).toFixed();
		//console.log("$slidNum:"+$slidNum);
	}
	if($autoplay==null || $autoplay == true){
		$touchSlider.find(".btn_play").hide();
		$touchSlider.find(".btn_stop").show();
	}else{
		$touchSlider.find(".btn_play").show();
		$touchSlider.find(".btn_stop").hide();
	}
	if ($touchSlider.find('.sliderBox > ul > li').length > 1) {
		$touchSlider.find("> .sliderBox").touchSlider({
			view:$slidNum,
			speed : $speed,
			transition : false,
			autoplay : {
				enable : $autoplay,
				pauseHover : false,
				addHoverTarget : "", // 다른 오버영역 추가 ex) ".someBtn, .someContainer"
				interval : 7500,
			},
			initComplete : function (e) {
				var _this = this;
				var $this = $(this);
				var paging = $touchSlider.find(".paging");
				
				this._btn_play = null;
				this._btn_stop = null;	
 
				paging.html("");
				$this.find(" > ul > li").each(function (i, el) {
					var num = (i+1) / _this._view;
					if((i+1) % _this._view == 0) {
						paging.append('<button type="button" class="btn_page">page' + num + '</button>');
					}
				});
				paging.find(".btn_page").bind("click", function (e) {
					_this.go_page($(this).index());
					$touchSlider.find(".btn_play").show();
					$touchSlider.find(".btn_stop").hide();
					_this.autoStop();
				});				
				
				$touchSlider.find(".btn_play").bind("click", function() {
					$touchSlider.find(".btn_play").hide();
					$touchSlider.find(".btn_stop").show();
					_this.autoPlay();
				});
				$touchSlider.find(".btn_stop").bind("click", function() {
					$touchSlider.find(".btn_play").show();
					$touchSlider.find(".btn_stop").hide();
					_this.autoStop();
				});
				$this.find(" > ul > li a").on("focus",function(e){
					e.preventDefault();
					if(Number($slidNum) > 1){
						var _thisNumb = Math.floor(Number($(this).parents("li").index()/$slidNum));
					}else{
						var _thisNumb = $(this).parents("li").index();
					}
					$touchSlider.find(".btn_play").show();
					$touchSlider.find(".btn_stop").hide();
					_this.autoStop();
					_this.go_page(_thisNumb);
					
					$touchSlider.find("> .sliderBox > ul").css({"position":"fixed"});
					setTimeout(function(){
						$touchSlider.find("> .sliderBox > ul").css({"position":""});
					},50);
					
				});
				$this.find(" > ul > li a").on("focusout",function(){
					$touchSlider.find(".btn_play").hide();
					$touchSlider.find(".btn_stop").show();
					_this.autoPlay();
				});
			},
			counter : function (e) {
				$touchSlider.find(".paging .btn_page").removeClass("on").eq(e.current-1).addClass("on");
				$touchSlider.find(".pageCount").html("<span class='num'>"+e.current + "</span>/<span class='totalNum'>" + e.total+"</sapn>");				
				if($slide==false){
					$touchSlider.find(".sliderBox > ul > li").stop();
					$touchSlider.find(".sliderBox > ul > li").css({"opacity":"0","z-index":"1","left":"0"});
					$touchSlider.find(".sliderBox > ul > li.on").removeClass("on").css({"z-index":"2","opacity":"1","left":"0"});
					$touchSlider.find(".sliderBox > ul > li").eq(e.current-1).addClass("on").css({"z-index":"3","opacity":"0","left":"0"}).animate({"opacity":"1"},1000);
				}
			},
			btn_prev : $touchSlider.find(".btn_prev"),
			btn_next : $touchSlider.find(".btn_next")
		});
	}
}
//슬라이드 끝