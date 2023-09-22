$(document).ready(function() {

	$('body').on('click', 'a.[data-lp="lp_forsale"]', function () {
		var userid = $(this).closest('tr').data('userid');
		var formdata = {userid : userid, rows: 5};
		$.get('/siteadmin/popup/forsale', formdata, function(result) {
			$('#lp_forsale').find('div.body').html(result);
		});
	});

	$('body').on('click', 'a.[data-lp="lp_item"]', function () {
		var userid = $(this).closest('tr').data('userid');
		var formdata = {sessionId : userid};
		$.get('/siteadmin/popup/item', formdata, function(result) {
			$('#lp_item').find('div.body').html(result);
		});
	});

	$('body').on('click', 'a.[data-lp="lp_point"]', function () {
		var userid = $(this).closest('tr').data('userid');
		var formdata = {sessionId : userid};
		$.get('/siteadmin/popup/point', formdata, function(result) {
			$('#lp_point').find('div.body').html(result);
		});
	});

	$('body').on('click', 'a.[data-lp="lp_agent"]', function () {
		var userid = $(this).closest('tr').data('userid');
		var formdata = {sessionId : userid, userid : userid};
		$.get('/siteadmin/popup/agent', formdata, function(result) {
			$('#lp_agent').find('div.body').html(result);
		});
	});
	
});
