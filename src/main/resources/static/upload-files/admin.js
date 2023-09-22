$(document).ready(function(){
	$("a").click(function(e){
		var href = $(this).attr("href");
		if(href == "#none" || href == "#" || href == ""){
			e.preventDefault();
		}
  });
  
  //레이어팝업 시작
	$(".lp_open").click(function(e){
        e.preventDefault();
        var _id = $(this).attr("data-lp");
        if($("#lp_layout").find("#"+_id).length > 0){
          $("#lp_layout").addClass("on");
          $("#lp_layout").find("#"+_id).addClass("on");
          if(_id == "lp_a11"){
            touchSlider('#roomInfo',1,false,true,500);
            imgResizeIn("#lp_a11 .sliderBox li img");
          }
        }
    });
    $(".lp_close").click(function(e){
        e.preventDefault();
        $("#lp_layout").removeClass("on");
        $("#lp_layout .lp_wrap.on").removeClass("on");
        $("#lp_layout .lp_wrap").removeClass("lp1");
        $("#lp_layout .lp_wrap").removeClass("lp2");
    });
    $(".lp_open2").click(function(e){
          e.preventDefault();
          var _id = $(this).attr("data-lp");
          if($("#lp_layout").find("#"+_id).length > 0){
            $("#lp_layout .lp_wrap.on").addClass("lp1");
            $("#lp_layout").find("#"+_id).addClass("on lp2");
          }
      });
      $(".lp_close2").click(function(e){
          e.preventDefault();
          $("#lp_layout .lp_wrap.lp2").removeClass("on");
          $("#lp_layout .lp_wrap").removeClass("lp1");
          $("#lp_layout .lp_wrap").removeClass("lp2");
      });
    //레이어팝업 끝


    //lnb메뉴 시작
    lnb();
    function lnb(){
        var url = window.location.href;
        var arr = url.split("/");
        var arrNum = arr.length;
        $("#lnb_layout a").each(function(){
          if($(this).attr("href") == arr[arrNum-1].split(".html")[0].substring(0,9)+".html"){
              $("#lnb_layout li").removeClass("on");
              $(this).parent().addClass("on");
              $(this).parent().parent().parent().addClass("on");
          }
        });
        $("#lnb_layout .depth_1 > li").on("mouseenter",function(){
          if($(this).find(".depth_2").length > 0){
            $("#lnb_layout li").removeClass("ov");
            $(this).addClass("ov");
          }
        }).mouseleave(function(){
          $("#lnb_layout li").removeClass("ov");
        });
    }
    //lnb메뉴 끝
    

    //jquery-ui 시작
    $( ".jquerySelect select" ).selectmenu();

    var dateFormat = "mm/dd/yy",
    from = $( "#fromDate" )
    .datepicker({
      showMonthAfterYear:true,
      monthNamesShort: [ "1월", "2월", "3월", "4월", "5월", "6월", "7월", "8월", "9월", "10월", "11월", "12월" ],
      dayNamesMin: [ "일","월","화","수","목","금","토" ],
      yearSuffix: "년",
      defaultDate: "+1w",
      changeMonth: true,
      changeYear: true,
      numberOfMonths: 1
    })
    .on( "change", function() {
      to.datepicker( "option", "minDate", getDate( this ) );
    })
    .on( "change", function() {
      from.datepicker( "option", "dateFormat", "yy-mm-dd" );
    }),
    to = $( "#toDate" ).datepicker({
    showMonthAfterYear:true,
    monthNamesShort: [ "1월", "2월", "3월", "4월", "5월", "6월", "7월", "8월", "9월", "10월", "11월", "12월" ],
    dayNamesMin: [ "일","월","화","수","목","금","토" ],
    yearSuffix: "년",
    defaultDate: "+1w",
    changeMonth: true,
    changeYear: true,
    numberOfMonths: 1
    })
    .on( "change", function() {
      from.datepicker( "option", "maxDate", getDate( this ) );
    })
    .on( "change", function() {
      to.datepicker( "option", "dateFormat", "yy-mm-dd" );
    });
 
    function getDate( element ) {
      var date;
      try {
        date = $.datepicker.parseDate( dateFormat, element.value );
      } catch( error ) {
        date = null;
      }
 
      return date;
    }
    //jquery-ui 끝

    $(".file input[type=file]").bind( 'change', function (e){
      var _val = $(this).val();
      console.log(">>>>>>>> _val : "+_val);
      var fileName = _val.split('\\').pop().toLowerCase();
      console.log(">>>>>>>> fileName : "+fileName);
      $(this).next().val(fileName);
    });

    $(".bbsList .acco_btn a").on("click",function(){
      var isOn = $(this).parents("li").hasClass("on");

      if(isOn){
        $(this).parents("li").removeClass("on");
      }else{
        $(".bbsList .acco_wrap li").removeClass("on");
        $(this).parents("li").addClass("on");
      }
    })
})




//이미지 리사이즈
function imgResizeOut(res){
  $(res).each(function(){
    $(this).removeAttr("class");

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
  $(res).each(function(){
    $(this).removeAttr("class");

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

function nextView(id){
  var _id = $("#qNum");
  _id.show();
  $("#contents_layout .head .btn").removeAttr("style")
  $("#contents_layout").removeClass().addClass(id);
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
				interval : 5500,
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
						paging.append('<button type="button" class="btn_page">0' + num + '</button>');
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
				$touchSlider.removeAttr("class").addClass("slide_wrap").addClass("n"+e.current);
				$touchSlider.find(".pageCount").html("<span class='num'>"+e.current + "</span> / <span class='totalNum'>" + e.total+"</sapn>");
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