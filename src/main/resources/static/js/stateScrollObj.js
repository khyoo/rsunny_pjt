function stateScrollObj(param,obj,btn,interval,speed,viewSize,moreSize,dir,data,auto,hover,method,op1){
	var _parame1 = param,
		_parame2 = obj,
		_parame3 = btn,
		_parame4 = interval,
		_parame5 = speed,
		_parame6 = viewSize,
		_parame7 = moreSize,
		_parame8 = dir,
		_parame9 = data,
		_parame10 = auto,
		_parame11 = hover,
		_parame12 = method,
		_parame13 = op1;
	var param = $(param);
	var btn = $(btn);
	var obj = param.find(obj);
	
	var elem = 0;
	var objYScale = obj.eq(elem).outerHeight(true)+moreSize;
	var objXScale = obj.eq(elem).outerWidth(true)+moreSize;
	var str;
	var returnNodes;

	var playdir = data;
	var data = data; // 0:default, 1:prev

	var play = btn.find("[data-control=play]");
	var stop = btn.find("[data-control=stop]");
	
	if(auto == true) play.hide(); else stop.hide(); 
	if(op1 == true) obj.not(elem).css({opacity:0}).eq(elem).css({opacity:1});

	function movement(){
		
		switch(data){
			case 0:
				if(dir == "x"){
					obj.parent().stop(true,true).animate({left:-objXScale},{duration:speed,easing:method,complete:
						function(){
							obj.parent().css("left",0);
							str = obj.eq(elem).detach();
							obj.parent().append(str);
							if(elem == obj.size()-1){
								elem = 0;
							}else{
								elem++;
							}
							objXScale = obj.eq(elem).outerWidth(true)+moreSize;
						}
					});
				}else{ 
					obj.parent().stop(true,true).animate({top:-objYScale},{duration:speed,easing:method,complete:
						function(){
							obj.parent().css("top",0);
							str = obj.eq(elem).detach();
							obj.parent().append(str);
							if(elem == obj.size()-1){
								elem = 0;
							}else{
								elem++;
							}
							objYScale = obj.eq(elem).outerHeight(true)+moreSize;
						}
					});
				}

				if(op1 == true){
					obj.eq(elem).stop(true,true).animate({opacity:0},{duration:speed,easing:method});
					obj.eq(elem).next().stop(true,true).animate({opacity:1},{duration:speed,easing:method});
					//obj.eq(elem).stop(true,true).fadeOut(speed);
					//obj.eq(elem).next().stop(true,true).fadeIn(speed);
					//obj.eq(elem).css({"z-index":"0"});
					//obj.eq(elem).next().css({"z-index":"1"});
				}
			break;
			
			case 1:
				if(dir == "x"){
					if(elem == 0){
						elem = obj.size()-1;
					}else{
						elem--;
					}
					objXScale = obj.eq(elem).outerWidth()+moreSize;
					obj.parent().css("left",-objXScale);
					str = obj.eq(elem).detach();
					obj.parent().prepend(str);
					obj.parent().stop(true,false).animate({left:0},{duration:speed,easing:method});
				}else{
					if(elem == 0){
						elem = obj.size()-1;
					}else{
						elem--;
					}
					objYScale = obj.eq(elem).outerHeight()+moreSize;
					obj.parent().css("top",-objYScale);
					str = obj.eq(elem).detach();
					obj.parent().prepend(str);
					obj.parent().stop(true,false).animate({top:0},{duration:speed,easing:method});
				}

				if(op1 == true){
					obj.eq(elem).stop(true,false).animate({opacity:1},{duration:speed,easing:method});
					obj.eq(elem).next().stop(true,false).animate({opacity:0},{duration:speed,easing:method});
					//obj.eq(elem).stop(true,false).fadeIn(speed);
					//obj.eq(elem).next().stop(true,false).fadeOut(speed);
					//obj.eq(elem).css({"z-index":"1"});
					//obj.eq(elem).next().css({"z-index":"0"});
				}
			break;
			
			default: alert("warning, 0:default, 1:prev, data:"+data);
		}
	}

	function rotate(){
		clearInterval(returnNodes);
		returnNodes = setInterval(function(){
			movement();
		},interval);
	}

	if(obj.size() <= viewSize) return false;
	/*
	obj.find("a").on("focusin",function(){
		clearInterval(returnNodes);
	});
	*/
	obj.find("a").on("focus",function(){
		clearInterval(returnNodes);
		if(auto == true){
			param.find(":animated").stop();
			stop.hide();
			play.show();
			return false;
		}
	});
	obj.find("a").on("focusout",function(){
		if(auto == true){
			data = playdir;
			play.hide();
			stop.show();
			rotate();
			return false;
		}
	});
	//var _objHtml = param.find(".obj").html();
	$("*").on("focus",function(){
		//console.log(">>> 슬라이더 포커스 시작");
		var _thisParam = $(this).parents("#"+param.attr("id")).attr("class");
		var _thisObj = $(this).parents(".obj").attr("class");
		if(_thisParam != undefined && _thisObj != undefined){
			//console.log(">>> "+param.attr("id")+"에 포커스 있다");			
			$(this).parents(".obj").addClass("focusIn");
		}else{
			//console.log(">>> "+param.attr("id")+"에 포커스 없다");
			param.find(".obj.focusIn").each(function(){
				//console.log(">>> 포커스 아웃");
				//$(this).find("li").detach();
				param.find(".obj").css("position","fixed");
				setTimeout(function(){
					/*
					param.find(".obj").append(_objHtml);
					stateScrollObj(_parame1,_parame2,_parame3,_parame4,_parame5,_parame6,_parame7,_parame8,_parame9,_parame10,_parame11,_parame12,_parame13);
					*/
					param.find(".obj").css("position","");
				},100);
				$(this).removeClass("focusIn");
			});
		}
	});

	btn.find("[data-control=play]").on("click",function(event){
		//console.log(">>>>>>> play");
		data = playdir;
		play.hide();
		stop.show();
		rotate();
		return false;
	});

	btn.find("[data-control=stop]").on("click",function(event){
		//console.log(">>>>>>> stop");
		clearInterval(returnNodes);
		param.find(":animated").stop();
		stop.hide();
		play.show();
		return false;
	});

	btn.find("[data-control=prev]").on("click",function(event){
		//console.log(">>>>>>> prev");
		if(obj.parent().find(":animated").size()) return false;
		clearInterval(returnNodes);
		data = 1;
		movement();
		// add
		stop.hide();
		play.show();
		return false;
	});

	btn.find("[data-control=next]").on("click",function(event){
		//console.log(">>>>>>> next");
		if(obj.parent().find(":animated").size()) return false;
		clearInterval(returnNodes);
		data = 0;
		movement();
		// add
		stop.hide();
		play.show();
		return false;
	});

	if(hover == true){
		obj.hover(function(){
			clearInterval(returnNodes);
		},function(){
			rotate();
		});
	}

	if(auto == true) rotate();

	// 터치 이벤트  플리킹 구현
	var xStartpoint,xEndpoint;

	function docSTART(event){
		if(event.originalEvent.changedTouches != undefined){
			xStartpoint = Math.floor(event.originalEvent.changedTouches[0].clientX - $(this).offset().left);
		}
	}

	function docEND(event){
		if(event.originalEvent.changedTouches != undefined){
			xEndpoint = Math.floor(event.originalEvent.changedTouches[0].clientX - $(this).offset().left) - xStartpoint;
		
			if(xEndpoint < -50){ 
				data = 0;
			}else if(xEndpoint > 50){
				data = 1;
			}else{
				return true;
			} 

			clearInterval(returnNodes);

			stop.hide();
			play.show();

			movement();

			event.preventDefault();
		}
	}

	param.on("touchstart",docSTART);
	param.on("touchend",docEND);
}