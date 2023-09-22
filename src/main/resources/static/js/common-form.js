String.prototype.checkEmail = function(){
	var regExp = /^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$/i;
	return regExp.test(this);
}

String.prototype.checkAlphaNumeric = function(){
	var regExp = /^[A-Za-z0-9]*$/;
	return regExp.test(this);
}

String.prototype.checkTel = function(){
	//숫자가 아니면 오류 처리.
	if( !this.checkNumber() ) return false;
	
	var regExp = /^\d{2,3}\d{3,4}\d{4}$/;
	return regExp.test(this);
}

String.prototype.checkNumber = function(){
	var regExp = /^[0-9]*$/
	return regExp.test(this);
}



String.prototype.checkUrl = function(){
	var regExp = /(http(s)?:\/\/.)?(www\.)?[-a-zA-Z0-9@:%._\+~#=]{2,256}\.[a-z]{2,6}\b([-a-zA-Z0-9@:%_\+.~#?&//=]*)/g;
	return regExp.test(this);
}

if (!String.prototype.endsWith) {
	String.prototype.endsWith = function(searchString, position) {
		var subjectString = this.toString();
		if (typeof position !== 'number' || !isFinite(position) || Math.floor(position) !== position || position > subjectString.length) {
			position = subjectString.length;
		}
		position -= searchString.length;
		var lastIndex = subjectString.indexOf(searchString, position);
		return lastIndex !== -1 && lastIndex === position;
	};
}

$.fn.serializeObject = function() {
    var o = {};
    
    //콤마 제거
    $.each( $('input.comma'), function(idx, item) {
		$(this).val( $(this).val().replace(/,/g, '') );
    });
    
    //배열 데이터로 변환.
    var a = this.serializeArray();

	//콤마 다시 설정.
	$('input.comma').trigger('blur');

    $.each(a, function() {
        if (o[this.name]) {
			var value = o[this.name];
			value += (((value && value.length>0)? ',':'') + this.value);
            o[this.name] = value;
        } else {
            o[this.name] = this.value || '';
        }
    });
    return o;
};

$.fn.listToJson = function() {
    var list = [];
    //alert( $(this).length );
    $.each($(this), function(idx, row) {
    	var rows = $(this).find('input, select');
    	var data = {};
    	$.each(rows, function(idx, item) {
    		var tag = $(item).prop('tagName').toLowerCase();
    		var type = $(item).attr('type');
    		var isNum = $(item).hasClass('number');
    		
    		if(type=='checkbox' || type=='radio') {	//체크박스인 경우.
 		   		data[ $(item).attr("name") ] = ($(item).is(':checked'))? $(item).val() : 'N';
    		}
    		else if(tag=='input' && isNum) {	//숫자입력 박스인 경우.
 		   		data[ $(item).attr("name") ] = $(item).val().replace(/,/g, '');	//콤마 제거.
    		}
    		else {	//일반 텍스트인 경우.
 		   		data[ $(item).attr("name") ] = $(item).val();
    		}
    	});
    	list.push(data);
    });
    //alert(JSON.stringify(list));
    
    return list;
};

$.fn.listToString = function(seperator) {
	if(seperator==undefined || seperator=='') seperator = '|';
	
	if( $(this).length==0 ) return '';
	
    var list = [];
    $.each($(this), function(idx, item) {
		var tagName = $(item).prop('tagName');
    	list.push( (tagName=='a')? $(item).text() : $(item).val() );
    });
    //alert(JSON.stringify(list));
    
    return list.join(seperator);
};

$.fn.extend({
	setFormData: function(data) {
		if(data==undefined || data==null || data.length==0) return;
		
		var form = $(this);
	    $.each(data, function(key, value) {
	    	var expr = $.validator.format('[name="{0}"]:first', key);
	    	var obj = form.find(expr);
	    	if(obj.length==0) return true;
	    	
	    	var tag = $(obj).prop('tagName').toLowerCase();
	    	var type = (tag=='input')? $(obj).prop('type').toLowerCase() : '';
	    	//alert(key + '...' + obj + '...' + obj.length + '...' + tag );
	    	if(obj.length==0 || obj.data('empty')) return;
	    	
	    	if(tag=='input' && (type=='radio' || type=='checkbox')) {
	    		var selExpr = $.validator.format('[name="{0}"][value="{1}"]', key, value);
	    		form.find(selExpr).prop('checked', true);
	    	}
	    	else {
	    		form.find(expr).val(value);
	    	}
	    });
	    
		var isNew = (data && data.isNew=='Y');
	    if( !isNew ) form.find('input[data-updatable=false]').prop('readonly', true);
	    if( !isNew ) form.find('select[data-updatable=false]').prop('disabled', true);
	    form.find('select[trigger]').trigger('change');
	    $.each(form.find('select'), function(idx, item) {
			$(this).parent().find('span.ui-selectmenu-text').text( $(this).find('option:checked').text());
		});
	}
});
	
$.fn.extend({
	resetForm: function(initHidden) {
		var form = $(this);
		form[0].reset();
		
		form.find('input[type=text],input[type=hidden]').val('');
		form.find('input[type=radio][default]:first').prop('checked', true);
		form.find('input[type=radio][default]').prop('checked', true);
		
	    form.find('input[data-updatable=false]').prop('readonly', false);
	    form.find('select[data-updatable=false]').prop('disabled', false);
	    form.find('select[trigger]').trigger('change');
	}
});

function printPageList(totalRows, currPage, rows){
	var totalPages = Math.floor((totalRows + rows - 1) / rows);
	if(totalPages==0) totalPages = 1;
	var pages = 10;

	var groupSeed = Math.floor( (currPage - 1) / pages );
	var lastSeed = Math.floor( (totalPages - 1) / pages );
	var frPage = groupSeed * pages + 1;
	var toPage = (groupSeed+1) * pages;
	if(toPage > totalPages) toPage = totalPages;

	var goPage = 0;
	
	var pageHtml = '<ul>';
	if(groupSeed > 0) {
		pageHtml += $.validator.format('<li class="first"><a href="javascript:movePage({0});">처음</a></li>', 1);
		goPage = (groupSeed - 1) * pages + 1;
		pageHtml += $.validator.format('<li class="prev"><a href="javascript:movePage({0});">이전</a></li>', goPage);
	}
	
	for(var i = frPage; i <= toPage; i++) {
		if(i==currPage) {
			pageHtml += $.validator.format('<li><a href="javascript:movePage({0});">{0}</a></li>', i);
		}
		else {
			pageHtml += $.validator.format('<li class="on"><a href="javascript:movePage({0});">{0}</a></li>', i);
		}
	}

	if(groupSeed < lastSeed) {
		goPage = (groupSeed + 1) * pages + 1;
		pageHtml += $.validator.format('<li class="next"><a href="javascript:movePage({0});">다음</a></li>', goPage);
		pageHtml += $.validator.format('<li class="end"><a href="javascript:movePage({0});">마지막</a></li>', totalPages);
	}
	pageHtml += '</ul>';
	
	//alert(pageHtml);
	document.write( pageHtml );
	
}

function movePage(page) {
	var obj = $('#page');
	obj.val(page);
	obj.closest('form').action = location.pathname;
	obj.closest('form').submit();
}
