/*
	endPoint: custManager / subCode / custCompany / userList
*/
function setCodeList(endPoint, params, obj, val) {
	var firstText = (obj.find('option').length > 0 && obj.find('option:first').text().indexOf('-전체-') >= 0)? '-전체-' : '-선택-';
	//초기화.
	obj.empty().append($.validator.format('<option value="">{0}</option>', firstText));
	//공란 포함 항목인 경우 강제로 빈 아이템 하나 추가.
	if( obj.attr('space-item')=='true' ) {
		obj.append($.validator.format('<option value="-">{0}</option>', ' '));
	}
	obj.selectmenu('refresh');
	
	var url = $.validator.format('/code/rest/{0}', endPoint);
	
	//목록 조회 및 설정.
	$.post(url, params, function(result) {
		//alert(JSON.stringify(result));
		$.each(result.rows, function(idx, item) {
			var option = $.validator.format('<option value="{0}">{1}</option>', item.cd, item.nm);
			obj.append( option );
		});
		
		if(val != null && val.length > 0) {
			obj.val(val);
		}
		
		obj.selectmenu('refresh');
		
		obj.trigger('change');	//변경 이벤트 강제 발생시킴.
		obj.trigger('selectmenuchange');	//변경 이벤트 강제 발생시킴.
		
	});
	
}