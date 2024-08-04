$(document).ready(function() {

	$('#mapTypeChange1').click(function() {
		setOverlayMapTypeId('skyview');
	});
	$('#mapTypeChange2').click(function() {
		setOverlayMapTypeId('use_district');
	});
	$('#mapTypeChange3').click(function() {
		setOverlayMapTypeId('terrain');
	});
	$('#mapTypeChange4').click(function() {
		setOverlayMapTypeId('roadview');
	});
});

let polygon = null;
let realTradingOverlayClick = [false, false, false, false, false]; 	//실거래가 오버레이 팝업 클릭 배열
		
let isMap = false;
let isMeasure = false;
let isAround = false;
let isAreaExtent = false;
let isPrice = false;

let isSkyview = false;
let isTerrain = false;
let isUseDistrict = false;
let isRoadView = false;

let isArea = false;
let isLength = false;

let isLengthList = false;
let isAreaList = false;

let drawingFlag = false; // 선이 그려지고 있는 상태를 가지고 있을 변수입니다

let lengthListCnt = 0;
let lengthDisSum = 0;
			
let areaListCnt = 0;
let areaListSum = 0.0;
let areaListPriceSum = 0.0;

let lineDistanceArr = [];

let polygonArr = [];
let areaOverlayArr = [];
let areaAddressArr = [];

let around_markers = [];
			
let _map = null;

function _initMap(map) {
	_map = map;
}

function setOverlayMapTypeId(maptype) {
	if (maptype === 'skyview') {
		if (isSkyview) {
			$('#mapTypeChange1').removeClass("on");
			
			$('.map-layer-icon2').css("border", "none");
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
			
			$('.map-layer-icon4').css("border", "none");
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
			
			$('.map-layer-icon3').css("border", "none");
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

function initCategory() {
	isMap = false;
	isMeasure = false;
	isAround = false;
	//isAreaExtent = false;
	//isPrice = false;
}

function initCategorySub() {
	isLength = false;
	isArea = false;
	
	isLengthList = false;
	isAreaList = false;
	
	drawingFlag = false;
	
	lengthListCnt = 0;
	lengthDisSum = 0;
	
	areaListCnt = 0;
	areaListSum = 0.0;
	
	polygonArr = [];
	areaOverlayArr = [];
	areaAddressArr = [];
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