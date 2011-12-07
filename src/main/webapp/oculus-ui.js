function Menu(linkDivMap) {
    this.linkMap = linkDivMap;
    this.active = null;

    this.menuClick = function (source) {
        menu.activate($(source.target).attr('id'));
    };

    this.activate = function (link) {
        this.active = link;
        this.invalidate();
    };

    this.invalidate = function() {
        $('#content').children(":not(#notifications)").hide();
        this.linkMap[this.active]['cb']();
        $("#" + this.linkMap[this.active]['div']).show();
    }

    this.init = function () {
        for (var link in this.linkMap) {
            if(this.active == null ) {
                this.active = link;
            }
            $("#" + link).click(this.menuClick);
        }
    };
}

function _confirmedCall(message, callback) {
    $("#confirmModalText").text(message);
    $("#confirmModalYes").unbind();
    $("#confirmModalYes").click(callback);
    $("#confirmModal").modal('show');
}

function _confirmedCallFinished() {
    $("#confirmModal").modal('hide');
}

function _ajaxCall(url, callback, message, noEndCallback) {
    console.log("get " + url);
    var messageText = 'Loading...';
    if (message != null) {
        messageText = message + '...';
    }

    $("#loadingLabel").text(messageText);
    $("#loadingLabel").show();
    get = $.get(url, function () {
    })
        .error(_ajaxCallFail)
        .success(callback)
    ;
    if (noEndCallback == null || noEndCallback == false) {
        get.complete(_ajaxCallEnd);
    }
}
function _ajaxCallFail(error) {
    _notificationError("" + error.status + ": " + error.responseText);
}

function _ajaxCallEnd(json) {
    $("#loadingLabel").hide();
    console.log("got");
    console.log(json);
}

function _notificationInfo(message) {
    _notification('info', message);
}
function _notificationError(message) {
    _notification('error', message);
}
function _notification(type, message) {
    $('#tmplNotification').tmpl({
        'type':type,
        'message':message
    }).prependTo($('#notifications'));
}