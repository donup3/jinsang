//더미데이터
var koreaarea = [{
    area: "seoul",
    count: 250
}, {
    area: "incheon",
    count: 80
}, {
    area: "gyeonggi",
    count: 130
}, {
    area: "chungbuk",
    count: 30
}, {
    area: "chungnam",
    count: 35
}, {
    area: "kangwon",
    count: 68
}, {
    area: "gwangju",
    count: 53
}, {
    area: "daejeon",
    count: 82
}, {
    area: "ulsan",
    count: 60
}, {
    area: "gyeongbuk",
    count: 12
}, {
    area: "gyeongnam",
    count: 44
}, {
    area: "busan",
    count: 118
}, {
    area: "jeju",
    count: 120
}, {
    area: "daegu",
    count: 43
}, {
    area: "jeonbuk",
    count: 65
}, {
    area: "jeonnam",
    count: 32
}];

var prevcolor;

$(function () { 

    $(".for-mob > button").on("click", function (e) {
        e.preventDefault();
        var nav_wrap = $("body");
        if( !nav_wrap.hasClass("__onmenu") ){
            nav_wrap.addClass("__onmenu");
        }
        else {
            nav_wrap.removeClass("__onmenu");
        }
    });

    // main map script
    if( $("#status").length ){
        colorset(koreaarea);

        $("g").mouseover(function (event) {
            var _path = event.target;
            var province = $(_path).parent()[0].id;
            if ($(_path).attr("fill") != "#ff5151") {
                prevcolor = $(_path).attr("fill");
            }
            $(_path).attr("fill", "#ff5151");

            if ($("." + province + "").length) {
                $("." + province + "").parent().addClass("__highlight");
            }

        }).mouseout(function (event) {
            var _path = event.target;
            var province = $(_path).parent()[0].id;
            $(_path).attr("fill", prevcolor);
            $("." + province + "").parent().removeClass("__highlight");
        });
    }

    
});

// main map color set
function colorset(areadata) {
    var basefillcolor = ["#efebfd", "#e8e3f9", "#dfd9f5", "#d8d1f0", "#d0c8eb", "#c7bfe6",
        "#beb5e1",
        "#b7aedc", "#afa5d7", "#a89dd2", "#a094cd", "#988bc7", "#9284c3", "#8a7cbd", "#8374b8",
        "#7d6db4"
    ];
    var basemax;
    var basemin;
    basemin = koreaarea[0].count;
    basemax = koreaarea[0].count;
    for (var i = 1; i < koreaarea.length; i++) {
        if (basemax < koreaarea[i].count)
            basemax = koreaarea[i].count;
        if (basemin > koreaarea[i].count)
            basemin = koreaarea[i].count;
    }
    var baseavg = (basemax - basemin) / koreaarea.length;
    var j = 0;
    $("#svgMap > g > g").each(function () {
        if (basemax == koreaarea[j].count)
            $(this).children("path").attr("fill", basefillcolor[basefillcolor.length - 1]);
        else
            $(this).children("path").attr("fill", basefillcolor[(Math.floor(((koreaarea[j]
                .count) - basemin) / baseavg))]);
        j++;
    });
}

function checkLogin(id){
	if(!id){
		alert('로그인 후 이용해주세요');
		location.href='/jinsang/login.php';
	}else{
		return true;
	}
}

var check = /^(?=.*[a-zA-Z])(?=.*[!@#$%^~*+=-])(?=.*[0-9]).{10,20}$/;

function sendMail(type,userid){
	var param = {id:userid,type:type};
	$.ajax({
		method: 'POST',
		url: '/jinsang/ajax/sendmail.php',
		data: out,
		dataType:'json',
		success: function (data) {
			console.log(data);
		},
		error: function (jqXHR, textStatus, errorThrown) {
			console.error(textStatus + " " + errorThrown);
		}
	});
}