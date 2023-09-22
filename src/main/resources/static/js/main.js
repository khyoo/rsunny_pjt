$(document).ready(function(){
	stateScrollObj("#slide_ads", ".item", "#slide_ads .control", 3000, 300, 0, 0, "x", 0, false, false, "easeInOutCubic", false);
});

$(window).load(function(){
    imgResizeOut("#slide_ads .thumb img");
    imgResizeOut(".manual_wrap .thumb img");
});

$(window).resize(function(){
    imgResizeOut("#slide_ads .thumb img");
    imgResizeOut(".manual_wrap .thumb img");
});