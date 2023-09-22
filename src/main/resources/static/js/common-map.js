
function mapFromCurrentPosition(container) {
    if (navigator.geolocation) {
        //위치 정보를 얻기
        try {
	        navigator.geolocation.getCurrentPosition (function(pos) {
	            // latitude=위도, longitude=경도
				var options = { //지도를 생성할 때 필요한 기본 옵션
						center: new kakao.maps.LatLng(pos.coords.latitude, pos.coords.longitude), //지도의 중심좌표.
						level: 5 //지도의 레벨(확대, 축소 정도)
					};
	
				var map = new kakao.maps.Map(container, options); //지도 생성 및 객체 리턴
	        });
		}
		catch(e) {
			alert(e);
		}
    } else {
        alert("이 브라우저에서는 Geolocation이 지원되지 않습니다.")
    }
}
