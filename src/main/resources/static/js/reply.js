var replyManger = (function () {
    var getAll = function (obj, callback) {
        console.log("get All reply!!!")
        $.getJSON('/jinsang/reply/' + obj, callback);
    };

    var add = function (obj, callback) {
        console.log("add reply!!!")
        $.ajax({
            type: 'post',
            url: '/jinsang/reply/write/' + obj.boardId,
            data: JSON.stringify(obj),
            beforeSend: function (xmlHttpRequest) {
                xmlHttpRequest.setRequestHeader("AJAX", "true");
            },
            dataType: 'json',
            contentType: 'application/json',
            success: callback
        })
    };


    var subAdd = function (obj, callback) {
        console.log("subAdd reply!!!")
        $.ajax({
            type: 'post',
            url: '/jinsang/reply/write/subReply/' + obj.boardId,
            data: JSON.stringify(obj),
            beforeSend: function (xmlHttpRequest) {
                xmlHttpRequest.setRequestHeader("AJAX", "true");
            },
            dataType: 'json',
            contentType: 'application/json',
            success: callback
        })
    };

    var remove = function (obj, callback) {
        console.log("remove reply!!!")
        $.ajax({
            type: 'delete',
            url: '/jinsang/reply/delete/' + obj.boardId+'/'+obj.replyId,
            beforeSend: function (xmlHttpRequest) {
                xmlHttpRequest.setRequestHeader("AJAX", "true");
            },
            dataType: 'json',
            contentType: 'application/json',
            success: callback
        })
    };

    return {
        getAll: getAll,
        add: add,
        subAdd:subAdd,
        remove: remove
    }
})();