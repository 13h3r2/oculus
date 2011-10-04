function Menu(linkDivMap) {
	this.linkMap = linkDivMap;
	this.active = null;

	this.menuClick = function(source) {
		menu.activate($(source.target).attr('id'));
	};

	this.activate = function(link) {
		this.active = link;
		$('#content').children(":not(#notifications)").hide();
		this.linkMap[link]['cb']();
		$("#" + this.linkMap[link]['div']).show();
	};

	this.init = function() {
		for ( var link in this.linkMap) {
			$("#" + link).click(this.menuClick);
		}
		this.active = this.linkMap[0];
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

function _ajaxCall(url, callback, message) {
	console.log("get " + url);
	if(message == null ) {
		$("#loadingLabel").text('Loading...');
	}else {
		$("#loadingLabel").text(message + '...');
	}
	$("#loadingLabel").show();
	$.get(url, function() {})
		.success(callback)
		.error(_ajaxCallFail)
		.complete(_ajaxCallEnd);
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
		'type' : type,
		'message' : message
	}).prependTo($('#notifications'));
}