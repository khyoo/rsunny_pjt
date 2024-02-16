let isSkyview = false;
let isTerrain = false;
let isUseDistrict = false;
let isRoadView = false;

let _map = null;

function setOverlayMapTypeId(maptype, map) {
	_map = map;
	
	if (maptype === 'skyview') {
		if (isSkyview) {
			$('#mapTypeChange1').removeClass("on");
			_map.setMapTypeId(kakao.maps.MapTypeId.ROADMAP);
		} else {
			$('#mapTypeChange1').addClass("on");

			$('.map-layer-icon2').css("border", "3px solid #E46C0A");
			// maptype에 해당하는 지도타입을 지도에 추가합니다	
			_map.setMapTypeId(kakao.maps.MapTypeId.HYBRID);
		}
		isSkyview = !isSkyview;
	} else if (maptype === 'use_district') {
		if (isUseDistrict) {
			$('#mapTypeChange2').removeClass("on");
			_map.removeOverlayMapTypeId(kakao.maps.MapTypeId.USE_DISTRICT);
		} else {
			$('#mapTypeChange2').addClass("on");

			$('.map-layer-icon4').css("border", "3px solid #5D39EE");
			// maptype에 해당하는 지도타입을 지도에 추가합니다
			_map.addOverlayMapTypeId(kakao.maps.MapTypeId.USE_DISTRICT);
		}
		isUseDistrict = !isUseDistrict;
	} else if (maptype === 'terrain') {
		if (isTerrain) {
			$('#mapTypeChange3').removeClass("on");
			_map.removeOverlayMapTypeId(kakao.maps.MapTypeId.TERRAIN);
		} else {
			$('#mapTypeChange3').addClass("on");

			$('.map-layer-icon3').css("border", "3px solid #5D39EE");
			// maptype에 해당하는 지도타입을 지도에 추가합니다
			_map.addOverlayMapTypeId(kakao.maps.MapTypeId.TERRAIN);
		}
		isTerrain = !isTerrain;
	} else if (maptype === 'roadview') {
		if (isRoadView) {
			$('#mapTypeChange4').removeClass("on");
			clearRoadview();
		} else {
			$('#mapTypeChange4').addClass("on");
			setRoadviewRoad();
		}
		isRoadView = !isRoadView;
	}
}

function clearCategory() {
	if (isSkyview) {
		$('#mapTypeChange1').removeClass("on");
		_map.setMapTypeId(kakao.maps.MapTypeId.ROADMAP);
	}
	if (isUseDistrict) {
		$('#mapTypeChange2').removeClass("on");
		_map.removeOverlayMapTypeId(kakao.maps.MapTypeId.USE_DISTRICT);
	}
	if (isTerrain) {
		$('#mapTypeChange3').removeClass("on");
		_map.removeOverlayMapTypeId(kakao.maps.MapTypeId.TERRAIN);
	}
	if (isRoadView) {
		$('#mapTypeChange4').removeClass("on");
		clearRoadview();
	}
	
	isSkyview = false;
	isTerrain = false;
	isUseDistrict = false;
	isRoadView = false;

	$('.map-layer-icon2').css("border", "none");
	$('.map-layer-icon3').css("border", "none");
	$('.map-layer-icon4').css("border", "none");
}



/* 지도 > 거리뷰 함수 모음 Start */

// 전달받은 좌표(position)에 가까운 로드뷰의 파노라마 ID를 추출하여
// 로드뷰를 설정하는 함수입니다
function toggleRoadview(position) {
	let rvContainer = document.getElementById('roadview'); //로드뷰를 표시할 div 입니다
	let rvClient = new kakao.maps.RoadviewClient();
	let rv = new kakao.maps.Roadview(rvContainer); // 로드뷰 객체를 생성합니다 

	kakao.maps.event.addListener(rv, 'position_changed', function() {
		// 현재 로드뷰의 위치 좌표를 얻어옵니다 
		let rvPosition = rv.getPosition();
		// 지도의 중심을 현재 로드뷰의 위치로 설정합니다
		_map.setCenter(rvPosition);

		// 지도 위에 로드뷰 도로 오버레이가 추가된 상태이면
		if (isRoadView) {
			// 마커의 위치를 현재 로드뷰의 위치로 설정합니다
			_roadviewMarker.setPosition(rvPosition);
		}
	});

	rvClient.getNearestPanoId(position, 50, function(panoId) {
		// 파노라마 ID가 null 이면 로드뷰를 숨깁니다
		if (panoId === null) {
			toggleMapWrapper(true, position);
		} else {
			toggleMapWrapper(false, position);

			// panoId로 로드뷰를 설정합니다
			rv.setPanoId(panoId, position);
		}
	});
}

// 지도를 감싸고 있는 div의 크기를 조정하는 함수입니다
function toggleMapWrapper(active, position) {
	let container = document.getElementById('gis_container');
	if (active) {

		// 지도를 감싸고 있는 div의 너비가 100%가 되도록 class를 변경합니다 
		container.className = '';

		// 지도의 크기가 변경되었기 때문에 relayout 함수를 호출합니다
		_map.relayout();

		// 지도의 너비가 변경될 때 지도중심을 입력받은 위치(position)로 설정합니다
		_map.setCenter(position);
	} else {

		// 지도만 보여지고 있는 상태이면 지도의 너비가 50%가 되도록 class를 변경하여
		// 로드뷰가 함께 표시되게 합니다
		if (container.className.indexOf('view_roadview') === -1) {
			container.className = 'view_roadview';

			// 지도의 크기가 변경되었기 때문에 relayout 함수를 호출합니다
			_map.relayout();

			// 지도의 너비가 변경될 때 지도중심을 입력받은 위치(position)로 설정합니다
			_map.setCenter(position);
		}
	}
}


// 지도 위의 로드뷰 버튼을 눌렀을 때 호출되는 함수입니다
function setRoadviewRoad() {
	_map.addOverlayMapTypeId(kakao.maps.MapTypeId.ROADVIEW);

	// 마커 이미지를 생성합니다
	let markImage = new kakao.maps.MarkerImage(
		'https://t1.daumcdn.net/localimg/localimages/07/2018/pc/roadview_minimap_wk_2018.png',
		new kakao.maps.Size(26, 46),
		{
			// 스프라이트 이미지를 사용합니다.
			// 스프라이트 이미지 전체의 크기를 지정하고
			spriteSize: new kakao.maps.Size(1666, 168),
			// 사용하고 싶은 영역의 좌상단 좌표를 입력합니다.
			// background-position으로 지정하는 값이며 부호는 반대입니다.
			spriteOrigin: new kakao.maps.Point(705, 114),
			offset: new kakao.maps.Point(13, 46)
		}
	);

	let mapCenter = _map.getCenter();

	// 드래그가 가능한 로드뷰 마커를 생성합니다
	_roadviewMarker = new kakao.maps.Marker({
		image: markImage,
		position: mapCenter,
		draggable: true
	});

	_roadviewMarker.setMap(_map); // 지도에 로드뷰 마커를 표시합니다.

	toggleRoadview(_map.getCenter());  // 로드뷰의 위치를 지도 중심으로 설정합니다

	// 마커에 dragend 이벤트를 등록합니다
	kakao.maps.event.addListener(_roadviewMarker, 'dragend', function(mouseEvent) {
		// 현재 마커가 놓인 자리의 좌표입니다 
		var position = _roadviewMarker.getPosition();

		// 마커가 놓인 위치를 기준으로 로드뷰를 설정합니다
		toggleRoadview(position);
	});

	//지도에 클릭 이벤트를 등록합니다
	kakao.maps.event.addListener(_map, 'click', function(mouseEvent) {
		// 지도 위에 로드뷰 도로 오버레이가 추가된 상태가 아니면 클릭이벤트를 무시합니다 
		if (!isRoadView) {
			return;
		}

		// 클릭한 위치의 좌표입니다 
		var position = mouseEvent.latLng;

		// 마커를 클릭한 위치로 옮깁니다
		_roadviewMarker.setPosition(position);

		// 클락한 위치를 기준으로 로드뷰를 설정합니다
		toggleRoadview(position);
	});
}

//로드뷰를 지도 뒤로 숨기는 함수입니다
function clearRoadview() {
	_map.removeOverlayMapTypeId(kakao.maps.MapTypeId.ROADVIEW);

	_roadviewMarker.setMap(null); // 지도에 로드뷰 마커를 없앱니다.			   
	toggleMapWrapper(true, _roadviewMarker.getPosition()); // 로드뷰 div를 숨기기
}

/* 지도 > 거리뷰 함수 모음 End */



let drawingFlag = false; // 선이 그려지고 있는 상태를 가지고 있을 변수입니다
let moveLine; // 선이 그려지고 있을때 마우스 움직임에 따라 그려질 선 객체 입니다
let clickLine // 마우스로 클릭한 좌표로 그려질 선 객체입니다
let distanceOverlay; // 선의 거리정보를 표시할 커스텀오버레이 입니다
let dots = {}; // 선이 그려지고 있을때 클릭할 때마다 클릭 지점과 거리를 표시하는 커스텀 오버레이 배열입니다.

let isLengthList = false;
let lengthListCnt = 0;
let lengthDisSum = 0;

var measuerLengthClickHandler = function(mouseEvent) {

	// 마우스로 클릭한 위치입니다 
	var clickPosition = mouseEvent.latLng;

	let subHtml = '';

	subHtml += '<div class="rbc-cerulean" style="display: none;height: 120px;" id="mt_total_area_view">';
	//subHtml += '	<div class="rfc-cerulean" style="background: #fff;width: 144px;display: inline-block;border-radius: 22px;height: 40px;position: relative;top:7px;line-height:40px;cursor:pointer;pointer-events:auto;" id="mt_last_btn">';
	//subHtml += '		<span style="display: inline-block;"><img src="https://aurora.disco.re/static/common/img/ico_close_b.png?ver=1688364125" style="width:16px;"></span>';
	//subHtml += '		<span style="display: inline-block;">마지막 지점 삭제</span>';
	//subHtml += '	</div>';
	subHtml += '	<div style="height: 40px;line-height: 46px;font-size: 16px;color: #fff;">선택된 지점 <span id="mt_count" style="display: inline-block;"></span>개</div>';
	subHtml += '	<div style="height: 40px;position: relative;">';
	subHtml += '		<div style="display: inline-block;width: 49%;padding-left: 16px;color: #fff;">';
	subHtml += '			<span style="display: inline-block;font-size: 14px;color: #7eb6e7;margin-right: 10px;">거리 합계 </span>';
	subHtml += '			<span style="display: inline-block;font-size: 20px;color: #fff;" id="mt_total_length"></span>';
	subHtml += '		</div>';
	subHtml += '</div>';

	if (!isLengthList) {
		isLengthList = true;

		$('#map_tool_msg').hide();
		$('#map_tool_field').append(subHtml);

		$('#mt_last_btn').on('click', function() {
			// 그려지고 있는 선의 좌표 배열을 얻어옵니다
			var path = clickLine.getPath();

			// 좌표 배열에 마지막 요소 제거
			path.pop();

			// 다시 선에 좌표 배열을 설정하여 클릭 위치까지 선을 그리도록 설정합니다
			clickLine.setPath(path);

			deleteLastCircleDot();

			if (lengthListCnt > 0) {
				lineDistanceArr.pop();
				lengthDisSum = lineDistanceArr[lineDistanceArr.length - 1];


				$('#mt_count').text((--lengthListCnt) + 1);
				$('#mt_total_length').text(lengthDisSum + 'm');
			} else {
				$('#mt_count').text(0);
				drawingFlag = false;
			}
		});
	}

	let temp_html = '<span class="temp_add_close" style="display: inline-block; position: relative; top: -25px; right: 5px;">';
	temp_html += '<img src="/img/sub/ico_btn_close.svg" width="20px" height="20px">';
	temp_html += '</span>';

	// 지도 클릭이벤트가 발생했는데 선을 그리고있는 상태가 아니면
	if (!drawingFlag) {

		// 상태를 true로, 선이 그리고있는 상태로 변경합니다
		drawingFlag = true;

		// 지도 위에 선이 표시되고 있다면 지도에서 제거합니다
		deleteClickLine();

		// 지도 위에 커스텀오버레이가 표시되고 있다면 지도에서 제거합니다
		deleteDistnce();

		// 지도 위에 선을 그리기 위해 클릭한 지점과 해당 지점의 거리정보가 표시되고 있다면 지도에서 제거합니다
		deleteCircleDot();

		// 클릭한 위치를 기준으로 선을 생성하고 지도위에 표시합니다
		clickLine = new kakao.maps.Polyline({
			map: map, // 선을 표시할 지도입니다 
			path: [clickPosition], // 선을 구성하는 좌표 배열입니다 클릭한 위치를 넣어줍니다
			strokeWeight: 3, // 선의 두께입니다 
			strokeColor: '#db4040', // 선의 색깔입니다
			strokeOpacity: 1, // 선의 불투명도입니다 0에서 1 사이값이며 0에 가까울수록 투명합니다
			strokeStyle: 'solid' // 선의 스타일입니다
		});

		// 선이 그려지고 있을 때 마우스 움직임에 따라 선이 그려질 위치를 표시할 선을 생성합니다
		moveLine = new kakao.maps.Polyline({
			strokeWeight: 3, // 선의 두께입니다 
			strokeColor: '#db4040', // 선의 색깔입니다
			strokeOpacity: 0.5, // 선의 불투명도입니다 0에서 1 사이값이며 0에 가까울수록 투명합니다
			strokeStyle: 'solid' // 선의 스타일입니다    
		});

		// 클릭한 지점에 대한 정보를 지도에 표시합니다
		displayCircleDot(clickPosition, 0);

		$('.dotOverlay').eq(0).html('시작 <span class="number">0</span>m');
		$('.dotOverlay').eq(0).parent().append(temp_html);

		$('.temp_add_close').on('contextmenu', function() {
			// 그려지고 있는 선의 좌표 배열을 얻어옵니다
			var path = clickLine.getPath();

			// 좌표 배열에 마지막 요소 제거
			path.pop();

			// 다시 선에 좌표 배열을 설정하여 클릭 위치까지 선을 그리도록 설정합니다
			clickLine.setPath(path);

			deleteLastCircleDot();

			if (lengthListCnt > 0) {
				lineDistanceArr.pop();
				lengthDisSum = lineDistanceArr[lineDistanceArr.length - 1];


				$('#mt_count').text((--lengthListCnt) + 1);
				$('#mt_total_length').text(lengthDisSum + 'm');
			} else {
				$('#mt_count').text(0);
				drawingFlag = false;
			}
		});
	} else { // 선이 그려지고 있는 상태이면

		// 그려지고 있는 선의 좌표 배열을 얻어옵니다
		var path = clickLine.getPath();

		// 좌표 배열에 클릭한 위치를 추가합니다
		path.push(clickPosition);

		// 다시 선에 좌표 배열을 설정하여 클릭 위치까지 선을 그리도록 설정합니다
		clickLine.setPath(path);

		var distance = Math.round(clickLine.getLength());
		displayCircleDot(clickPosition, distance);

		lengthListCnt++;
		lengthDisSum = distance;

		$('#mt_count').text(lengthListCnt + 1);
		$('#mt_total_length').text(lengthDisSum + 'm');

		$('#mt_total_area_view').css('display', 'block');

		//console.log($('.dotOverlay').length);
		$('.temp_add_close').hide();
		$('.dotOverlay').eq($('.dotOverlay').length - 1).parent().append(temp_html);

		$('.temp_add_close').off('contextmenu');
		$('.temp_add_close').on('contextmenu', function() {
			// 그려지고 있는 선의 좌표 배열을 얻어옵니다
			var path = clickLine.getPath();

			// 좌표 배열에 마지막 요소 제거
			path.pop();

			// 다시 선에 좌표 배열을 설정하여 클릭 위치까지 선을 그리도록 설정합니다
			clickLine.setPath(path);

			deleteLastCircleDot();

			if (lengthListCnt > 0) {
				lineDistanceArr.pop();
				lengthDisSum = lineDistanceArr[lineDistanceArr.length - 1];


				$('#mt_count').text((--lengthListCnt) + 1);
				$('#mt_total_length').text(lengthDisSum + 'm');
			} else {
				$('#mt_count').text(0);
				drawingFlag = false;
			}

			$('.temp_add_close').eq($('.dotOverlay').length - 1).show();
		});
	}
};



var measuerLengthMouseMoveHandler = function(mouseEvent) {

	// 지도 마우스무브 이벤트가 발생했는데 선을 그리고있는 상태이면
	if (drawingFlag) {

		// 마우스 커서의 현재 위치를 얻어옵니다 
		var mousePosition = mouseEvent.latLng;

		// 마우스 클릭으로 그려진 선의 좌표 배열을 얻어옵니다
		var path = clickLine.getPath();

		// 마우스 클릭으로 그려진 마지막 좌표와 마우스 커서 위치의 좌표로 선을 표시합니다
		var movepath = [path[path.length - 1], mousePosition];
		moveLine.setPath(movepath);
		moveLine.setMap(map);

		var distance = Math.round(clickLine.getLength() + moveLine.getLength()), // 선의 총 거리를 계산합니다
			content = '<div class="dotOverlay distanceInfo">총거리 <span class="number">' + distance + '</span>m</div>'; // 커스텀오버레이에 추가될 내용입니다

		// 거리정보를 지도에 표시합니다
		showDistance(content, mousePosition);
	}
};


var measuerLengthRightClickHandler = function(mouseEvent) {

	// 지도 오른쪽 클릭 이벤트가 발생했는데 선을 그리고있는 상태이면
	if (drawingFlag) {

		// 마우스무브로 그려진 선은 지도에서 제거합니다
		moveLine.setMap(null);
		moveLine = null;

		// 마우스 클릭으로 그린 선의 좌표 배열을 얻어옵니다
		var path = clickLine.getPath();

		// 선을 구성하는 좌표의 개수가 2개 이상이면
		if (path.length > 1) {

			// 마지막 클릭 지점에 대한 거리 정보 커스텀 오버레이를 지웁니다
			if (dots[dots.length - 1].distance) {
				dots[dots.length - 1].distance.setMap(null);
				dots[dots.length - 1].distance = null;
			}

			var distance = Math.round(clickLine.getLength()), // 선의 총 거리를 계산합니다
				content = getTimeHTML(distance); // 커스텀오버레이에 추가될 내용입니다

			// 그려진 선의 거리정보를 지도에 표시합니다
			showDistance(content, path[path.length - 1]);

		} else {

			// 선을 구성하는 좌표의 개수가 1개 이하이면 
			// 지도에 표시되고 있는 선과 정보들을 지도에서 제거합니다.
			deleteClickLine();
			deleteCircleDot();
			deleteDistnce();

		}

		// 상태를 false로, 그리지 않고 있는 상태로 변경합니다
		drawingFlag = false;
	}
};

// 클릭으로 그려진 선을 지도에서 제거하는 함수입니다
function deleteClickLine() {
	if (clickLine) {
		clickLine.setMap(null);
		clickLine = null;
	}
}

// 마우스 드래그로 그려지고 있는 선의 총거리 정보를 표시하거
// 마우스 오른쪽 클릭으로 선 그리가 종료됐을 때 선의 정보를 표시하는 커스텀 오버레이를 생성하고 지도에 표시하는 함수입니다
function showDistance(content, position) {

	if (distanceOverlay) { // 커스텀오버레이가 생성된 상태이면

		// 커스텀 오버레이의 위치와 표시할 내용을 설정합니다
		distanceOverlay.setPosition(position);
		distanceOverlay.setContent(content);

	} else { // 커스텀 오버레이가 생성되지 않은 상태이면

		// 커스텀 오버레이를 생성하고 지도에 표시합니다
		distanceOverlay = new kakao.maps.CustomOverlay({
			map: map, // 커스텀오버레이를 표시할 지도입니다
			content: content,  // 커스텀오버레이에 표시할 내용입니다
			position: position, // 커스텀오버레이를 표시할 위치입니다.
			xAnchor: 0,
			yAnchor: 0,
			zIndex: 3
		});
	}
}

// 그려지고 있는 선의 총거리 정보와 
// 선 그리가 종료됐을 때 선의 정보를 표시하는 커스텀 오버레이를 삭제하는 함수입니다
function deleteDistnce() {
	if (distanceOverlay) {
		distanceOverlay.setMap(null);
		distanceOverlay = null;
	}
}

// 선이 그려지고 있는 상태일 때 지도를 클릭하면 호출하여 
// 클릭 지점에 대한 정보 (동그라미와 클릭 지점까지의 총거리)를 표출하는 함수입니다
function displayCircleDot(position, distance) {

	// 클릭 지점을 표시할 빨간 동그라미 커스텀오버레이를 생성합니다
	var circleOverlay = new kakao.maps.CustomOverlay({
		content: '<span class="dot"></span>',
		position: position,
		zIndex: 1
	});

	// 지도에 표시합니다
	circleOverlay.setMap(map);

	if (distance > 0) {
		// 클릭한 지점까지의 그려진 선의 총 거리를 표시할 커스텀 오버레이를 생성합니다
		var distanceOverlay = new kakao.maps.CustomOverlay({
			content: '<div class="dotOverlay">거리 <span class="number">' + distance + '</span>m</div>',
			position: position,
			yAnchor: 1,
			zIndex: 2
		});

		// 지도에 표시합니다
		distanceOverlay.setMap(map);
	} else {
		var distanceOverlay = new kakao.maps.CustomOverlay({
			content: '<div class="dotOverlay">시작</div>',
			position: position,
			yAnchor: 1,
			zIndex: 2
		});

		// 지도에 표시합니다
		distanceOverlay.setMap(map);
	}

	lineDistanceArr.push(distance);
	// 배열에 추가합니다
	dots.push({ circle: circleOverlay, distance: distanceOverlay });
}

// 마지막 지점 지도에서 제거하는 함수입니다
function deleteLastCircleDot() {
	if (dots[dots.length - 1].circle) {
		dots[dots.length - 1].circle.setMap(null);
	}
	if (dots[dots.length - 1].distance) {
		dots[dots.length - 1].distance.setMap(null);
	}
	// 마지막 제거
	dots.pop();
}

// 전체 지점 지도에서 제거하는 함수입니다
function deleteAllCircleDot() {
	// 그려지고 있는 선의 좌표 배열을 얻어옵니다
	var path = clickLine.getPath();

	// 좌표 배열 모든 요소 제거
	path = [];

	// 다시 선에 좌표 배열을 설정하여 클릭 위치까지 선을 그리도록 설정합니다
	clickLine.setPath(path);

	for (let i = 0; i < dots.length; i++) {
		dots[i].circle.setMap(null);
		dots[i].distance.setMap(null);
	}

	$('#mt_count').text(0);
	$('#mt_total_length').text('0m');
}

// 클릭 지점에 대한 정보 (동그라미와 클릭 지점까지의 총거리)를 지도에서 모두 제거하는 함수입니다
function deleteCircleDot() {
	var i;

	for (i = 0; i < dots.length; i++) {
		if (dots[i].circle) {
			dots[i].circle.setMap(null);
		}

		if (dots[i].distance) {
			dots[i].distance.setMap(null);
		}
	}
	dots = [];
}

// 마우스 우클릭 하여 선 그리기가 종료됐을 때 호출하여 
// 그려진 선의 총거리 정보와 거리에 대한 도보, 자전거 시간을 계산하여
// HTML Content를 만들어 리턴하는 함수입니다
function getTimeHTML(distance) {

	// 도보의 시속은 평균 4km/h 이고 도보의 분속은 67m/min입니다
	var walkkTime = distance / 67 | 0;
	var walkHour = '', walkMin = '';

	// 계산한 도보 시간이 60분 보다 크면 시간으로 표시합니다
	if (walkkTime > 60) {
		walkHour = '<span class="number">' + Math.floor(walkkTime / 60) + '</span>시간 '
	}
	walkMin = '<span class="number">' + walkkTime % 60 + '</span>분'

	// 자전거의 평균 시속은 16km/h 이고 이것을 기준으로 자전거의 분속은 267m/min입니다
	var bycicleTime = distance / 227 | 0;
	var bycicleHour = '', bycicleMin = '';

	// 계산한 자전거 시간이 60분 보다 크면 시간으로 표출합니다
	if (bycicleTime > 60) {
		bycicleHour = '<span class="number">' + Math.floor(bycicleTime / 60) + '</span>시간 '
	}
	bycicleMin = '<span class="number">' + bycicleTime % 60 + '</span>분'

	// 거리와 도보 시간, 자전거 시간을 가지고 HTML Content를 만들어 리턴합니다
	var content = '<ul class="dotOverlay distanceInfo">';
	content += '    <li>';
	content += '        <span class="label">총거리</span><span class="number">' + distance + '</span>m';
	content += '    </li>';
	content += '    <li>';
	content += '        <span class="label">도보</span>' + walkHour + walkMin;
	content += '    </li>';
	content += '    <li>';
	content += '        <span class="label">자전거</span>' + bycicleHour + bycicleMin;
	content += '    </li>';
	content += '</ul>'

	return content;
}




/* 지번 상세 미니맵 Start */

// 상세화면 미니맵(지번 상세화면)
function minimapShow(latlng) {
	let minimap_container = document.getElementById('minimap_container'),
		minimap_mapWrapper = document.getElementById('minimap_mapWrapper'),
		minimap_btnRoadview = document.getElementById('minimap_btnRoadview'),
		minimap_btnMap = document.getElementById('minimap_btnMap'),
		minimap_rvContainer = document.getElementById('minimap_roadview');

	$('#minimap_map').empty();
	let minimap_mapContainer = document.getElementById('minimap_map');

	let minimap_placePosition = new kakao.maps.LatLng(latlng.getLat(), latlng.getLng());
	let marker = {
		position: minimap_placePosition
	};
	let minimap_mapOption = {
		center: minimap_placePosition,
		level: 5,
		marker: marker
	};
	let minimap_map = new kakao.maps.StaticMap(minimap_mapContainer, minimap_mapOption);

	let minimap_roadview = new kakao.maps.Roadview(minimap_rvContainer);
	let roadviewClient = new kakao.maps.RoadviewClient();
	roadviewClient.getNearestPanoId(minimap_placePosition, 100, function(panoId) {
		minimap_roadview.setPanoId(panoId, minimap_placePosition);
	});

	minimap_roadview.setViewpoint({
		pan: 321,
		tilt: 0,
		zoom: 0
	});

	kakao.maps.event.addListener(minimap_roadview, 'init', function() {
		let rvMarker = new kakao.maps.Marker({
			position: minimap_placePosition,
			map: minimap_roadview
		});
	});
}

// 상세화면 미니맵 : 지도와 로드뷰를 감싸고 있는 div의 class를 변경하여 지도를 숨기거나 보이게 하는 함수 
function minimapToggle(active) {
	if (active) {
		minimap_container.className = "view_map"
	} else {
		minimap_container.className = "view_roadview"
	}
}
/* 지번 상세 미니맵 End */