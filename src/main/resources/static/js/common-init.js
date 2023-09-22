$(document).ready(function() {
	/*
	$('input.comma').focus(function() {
		$(this).val( $(this).val().replace(/,/g, '') );
	});

	$('input.comma').blur(function() {
		var val = $(this).val();
		if(val.length==0 || isNaN(val)) return;
		
		try {
			var num = parseInt(val, 10);
			$(this).val( num.toLocaleString() );
		} catch(e) {
			alert(e);
		}
	});
	*/
	
	//모바일 메뉴 로그인 팝업시 메뉴 레이어 숨김.
	$('li.login').find('a').click(function() {
		$("#gnb_layout .gnb_close").trigger('click');
	});
	
	 $('body').on('click', 'a.LOGOUT', function() {
		if( !confirm('로그아웃하시겠습니까?') ) return;
		
		location.href = '/user/logout';
	});
	
	$('body').on('click','.lp_close', function(){
		$("#lp_layout").removeClass("on");
		$("#lp_layout .lp_wrap").removeClass("on lv1 lv2");
		$("html").css("overflow","");
	});

	$.fn.openModal = function(){
		var objid = $(this).attr('id');
		$("#lp_layout").addClass("on");
		$("#"+objid).addClass("on");
		$("html").css("overflow","hidden");
	}

	$.fn.closeModal = function(){
		$("#lp_layout").removeClass("on");
		$("#lp_layout .lp_wrap").removeClass("on lv1 lv2");
		$("html").css("overflow","");
	}

	$.fn.message = function(title, message, callback) {
		message_callback = null;	//message.html에 선언됨.
		if(callback) message_callback = callback;
		
		var objid = $(this).attr('id');
		var modal = $("#"+objid);
		modal.find('h2.title').text(title);
		modal.find('p.message').html(message.replace(/\n/g, '<br>'));
		this.openModal();
	}
	
	//로딩시 자동으로 콤마 설정.
	//$('input.comma').trigger('blur');
	$('input.comma').mask("#,##0", {reverse: true});
	$('input.py').mask("##0");
	$('input.year').mask("0000");
	//$('input.rate').mask("#0.0");
	
});
