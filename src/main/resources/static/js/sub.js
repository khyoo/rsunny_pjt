$(document).ready(function(){
    $(".fileBox input").bind( 'change', function (e){
        var _val = $(this).val();
        //console.log(">>>>>>>> _val : "+_val);
        if(_val != ""){
        $(this).parent().addClass("on");
        }
    });
    $(".fileBox a").on("click",function(){
        $(this).parent().removeClass("on").find("input").val("");
        //console.log(">>>>>>>> _val : "+$(this).parent().find("input").val());
    });

    
    $(".imgUpload input").bind( 'change', function (e){
      var _val = $(this).val();
      //console.log(">>>>>>>> _val : "+_val);
      console.log(">>>>>>>> isImage(_val) : "+isImage(_val));
      if(isImage(_val)){   
        if(_val != ""){
          fileUp(this);
        }
      }else{
        alert("이미지 파일이 아닙니다.");
      }
    });
    
    function fileUp(res){
        var fileObj, pathHeader , pathMiddle, pathEnd, allFilename, fileName, extName, _target;
        
        _target = $(res);
        fileObj = _target.val();
        pathHeader = fileObj.lastIndexOf("\\");
        pathMiddle = fileObj.lastIndexOf(".");
        pathEnd = fileObj.length;
        extName = fileObj.substring(pathMiddle+1, pathEnd);
        fileName = fileObj.substring(pathHeader+1, pathMiddle)+"."+extName;

        if(fileObj != ""){
            if(extName == "jpg" || extName == "png" || extName == "gif" || extName == "jpeg" || extName == "bmp"){
                _target.parents("li").addClass("on");
                
                var reader = new FileReader();
                reader.onload = function (e) {
                    _target.parents("li").find("a").css({'background-image':'url('+e.target.result+')'});
                }
                reader.readAsDataURL(res.files[0]);
            }else{
                alert("사진 파일만 업로드 가능합니다.");
                _target.val("");
            }
        }        
    }

	$(".imgUpload a").on("click",function(){
		$(this).css({'background-image':''});
		$(this).closest('li').removeClass("on").find("input").val("");
	});

    $(".input_wrap .flex_bill a").on("click",function(){
      var bill = $(this).parent().find("input").val();
      var optionNum = $(this).parent().parent().find(".billBox .txtRadio").length+1;
      var optionName = $(this).parent().parent().find(".billBox").attr("id");
      if(bill != ""){
        //$(this).parent().parent().find(".billBox").prepend("<div class='txtRadio'><input id='"+optionName+"_"+optionNum+"' type='checkbox'><label for='"+optionName+"_"+optionNum+"'>"+bill+"</label></div>");
        $(this).parent().parent().find(".billBox").prepend("<div class='txtRadio'><input id='"+optionName+"_"+optionNum+"' type='checkbox' value='"+bill+"'><label for='"+optionName+"_"+optionNum+"'>"+bill+"</label></div>");
        //$(this).parent().parent().find(".billBox").prepend("<a href='#'>"+bill+"</a>");
        $(this).parent().find("input").val("");
      }
      $(this).parent().parent().find(".billBox a").on("click",function(e){
        e.preventDefault();
        $(this).detach();
      });
    });
    $(".input_wrap .billBox a").on("click",function(e){
      e.preventDefault();
      $(this).detach();
    });

    $(".customer_wrap.type_acco li a").on("click",function(e){
      e.preventDefault();
      var isOn = $(this).parent().hasClass("on");
      if(isOn){
        $(this).parent().removeClass("on");
      }else{
        $(this).parent().addClass("on");
      }
    })

    $(".filter_wrap > a").on("click",function(){
      var isOn = $(this).parent().hasClass("on");
      if(isOn){
        $(this).parent().removeClass("on");
        $("#gis_layout").removeClass("filterOn");
      }else{
        $(this).parent().addClass("on");
      }
    });

    $(".tab_wrap.type2 a").on("click",function(){
      $(".tab_wrap.type2 li").removeClass("on");
      $(this).parent().addClass("on");
      $(".tabCon_wrap").removeClass("on");
      var dataValue = $(this).attr("data-tab");
      $(".tabCon_wrap."+dataValue).addClass("on");
      imgResizeOut(".tabCon_wrap .thumb img");
    });

    $(".detail_wrap .btn_wrap .etc .share a").on("click",function(){
      var isOn = $(this).parents(".share").hasClass("on");
      if(isOn){
        $(this).parents(".share").removeClass("on");
      }else{
        $(this).parents(".share").addClass("on");
      }
    });

    $("body").on("click",".tabCon_wrap .list .menu", function(){
      var isOn = $(this).hasClass("on");
      var isView = $("#gis_layout").hasClass("detailOn");
      var isMov = $("#gis_layout").hasClass("detailMov");
      var detailW = $(".detail_wrap").width();
      var listW = $(".left_wrap").width();
      var isMo = $("#top_layout .moBtn").css("display");
      /*
      if(isMo != "none"){
        listW = 0;
      }
      */
	 if(listW <= 360){
        listW = 0;
      }
      if(!isOn){
        $(".tabCon_wrap .list .menu").removeClass("on");
        $(this).addClass("on");
        if(!isMov){
          $("#gis_layout").addClass("detailMov");
          if(isView){
            $("#gis_layout").removeClass("brokerOn reportOn");
            $(".lp_gis").css("top","").removeClass("lp1 lp2");
            $(".detail_wrap").animate({"left":detailW*-1},200,function(){
              $(".detail_wrap").animate({"left":listW},200,function(){
                $("#gis_layout").removeClass("detailMov");
              });
            });
          }else{
            $("#gis_layout").addClass("detailOn");
            $(".detail_wrap").css("left",detailW*-1);            
            $(".detail_wrap").animate({"left":listW},200,function(){
              $("#gis_layout").removeClass("detailMov");
            });
          }
        }
      }
    });

    $(".detail_wrap .saleNum a").on("click",function(){
      var isMov = $("#gis_layout").hasClass("detailMov");
      var detailW = $(".detail_wrap").width();
      if(!isMov){
        $("#gis_layout").addClass("detailMov");
        $("#gis_layout").removeClass("brokerOn reportOn");
        $(".lp_gis").css("top","").removeClass("lp1 lp2");
        $(".detail_wrap").animate({"left":detailW*-1},200,function(){
          $("#gis_layout").removeClass("detailMov").removeClass("detailOn");
          $(".tabCon_wrap .list .menu").removeClass("on");
        });
        
        $('.left_wrap').show();
        
        if (polygon != null) {
        	polygon.setMap(null);
        }
      }
      
      realTradingOverlayClick = [false, false, false, false, false];
    });

    $(".gislp_open").on("click",function(){
      var lp = $(this).attr("data-lp");
      var isLp1 = $(".lp_gis").hasClass("lp1");
      var isOpen = $("#gis_layout").hasClass(lp+"On");
      if(!isOpen){
        $("#gis_layout").addClass(lp+"On");
        if(isLp1){
          $(".lp_gis."+lp+"_wrap").addClass("lp2");
          $(".lp_gis."+lp+"_wrap").css("top",$(".lp_gis.lp1").height()+82);
        }else{
          $(".lp_gis."+lp+"_wrap").addClass("lp1");
        }
      }
    });
    $(".wrap_close").on("click",function(){
      var lp = $(this).attr("data-lp");
      var isLp1 = $(".lp_gis."+lp+"_wrap").hasClass("lp1");
      $("#gis_layout").removeClass(lp+"On");
      if(isLp1){
        $(".lp_gis.lp2").css("top","").removeClass("lp2").addClass("lp1");
        $(".lp_gis."+lp+"_wrap").removeClass("lp1");
      }else{   
        $(".lp_gis."+lp+"_wrap").css("top","").removeClass("lp2");
      }
    });

    filter();
    leftMo();
    leftTouch();
})

$(window).load(function(){
    // $(".dropDown > a").on("click",function(e){
    //     e.preventDefault();
    //     var isOn = $(this).parents(".dropDown").hasClass("on");
    //     console.log(">>>>>> isOn : "+isOn);
    //     if(isOn){
    //         $(this).parents(".dropDown").removeClass("on");
    //     }else{
    //         $(".dropDown").removeClass("on");
    //         $(this).parents(".dropDown").addClass("on");
    //     }
    // });
    $(".dropDown > ul a").on("mouseenter focusin",function(){
        $(this).parents(".dropDown").addClass("on");
    });
    $(".dropDown > ul").on("mouseleave",function(){
        $(this).parents(".dropDown").removeClass("on");
    });

  deviceCK();

  imgResizeOut(".complete_wrap.type2 .thumb img");
  imgResizeOut(".left_wrap .banner_wrap .thumb img");
  imgResizeOut(".tabCon_wrap .thumb img");
  
  touchSlider('#slide_detail',1,false,true,500);
  imgResizeOut(".detail_wrap .slide_wrap li img");
})

$(window).resize(function(){
  deviceCK();  
  //filter();
  leftMo();
});

function dropDown(res){
    var _dropBox = $(res).parent();
    var isOn = _dropBox.hasClass("on");
    console.log(">>>>> isOn : "+isOn);
    if(isOn){
        _dropBox.removeClass("on");
    }else{
        $(".dropDown").removeClass("on");
        _dropBox.addClass("on");
    }
}

function leftMo(){
  var isMo = $("#gis_layout").css("padding-left");
  var isMini = $(".left_wrap").hasClass("mini");
  var isFull = $(".left_wrap").hasClass("full");
  if(!isFull){
    if(isMo == "0px"){
      $(".left_wrap").addClass("mini").removeAttr("style");
      $(".detail_wrap").addClass("mini").removeAttr("style");
      
      $(".tabCon_wrap").scrollTop(0);
    }else{
      $(".left_wrap").removeClass("mini full");
      $(".detail_wrap").removeClass("mini full");
      
      $(".tabCon_wrap").scrollTop(0);
    }
  }
}

function leftTouch(){
  var ontouchY = [];
  var leftTop = [];
  $(".tabCon_wrap").on({
    touchstart:function(e){
        ontouchS = e.originalEvent.touches[0].screenY;
    },
    touchmove:function(e){
      ontouchY.push(e.originalEvent.touches[0].screenY);
    },
    touchend:function(e){
      var yNum = ontouchY.length;
      var Yend = ontouchY[yNum-1];
      var Ymov = (ontouchS - Yend)/10;
      var tabScroll = $(".tabCon_wrap.on").scrollTop();
      leftTop.push($(".left_wrap").offset().top);
      console.log(">>>>>>>>>> ontouchY : "+ontouchY);
      console.log(">>>>>>>>>> Ymov : "+Ymov);
      console.log(">>>>>>> tabScroll : "+tabScroll);

      if(Ymov > 10 && tabScroll > 0){
        console.log(">>>>>> 크다");
        console.log(">>>>>>> leftTop : "+leftTop[0]);
        $(".left_wrap.mini").css({"height":"auto","top":leftTop[0]}).animate({"top":46},300,function(){
          $(".left_wrap.mini").addClass("full").removeClass("mini");
          $(".tabCon_wrap").scrollTop(0);
        });
        ontouchY = [];
      }else if(Ymov < -10 && tabScroll == 0){
        console.log(">>>>>> 작다");
        console.log(">>>>>>> leftTop : "+leftTop[0]);
        $(".left_wrap.full").animate({"top":leftTop[0]},300,function(){
          $(".left_wrap.full").addClass("mini").removeClass("full").removeAttr("style");
          $(".tabCon_wrap").scrollTop(0);
        });
        ontouchY = [];
      }
    }
  })
}

function deviceCK(){
  var isMo = $("#top_layout .moBtn").css("display");
  if(isMo == "none"){
    $("body").removeClass("mo").addClass("pc");
  }else{
    $("body").removeClass("pc").addClass("mo");
  }
}

function filter(){
  var isMo = $("#top_layout .moBtn").css("display");
  if(isMo != "none"){
    $(".filter_wrap").removeClass("on");
    $("#gis_layout").removeClass("filterOn");
  }else{
    $(".filter_wrap").addClass("on");
  }
}


function getExtension(filename) {
  var parts = filename.split('.');
  return parts[parts.length - 1];
}
function isImage(filename) {
  var ext = getExtension(filename);
  switch (ext.toLowerCase()) {
    case 'jpg':
    case 'gif':
    case 'bmp':
    case 'png':
    //etc
    return true;
  } return false;
}

