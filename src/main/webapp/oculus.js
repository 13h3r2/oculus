function Menu(linkDivMap) {
    this.linkMap = linkDivMap;
    this.active = null;

    this.menuClick = function(source) {
        menu.activate($(source.target).attr('id'));
    };

    this.activate = function(link) {
        this.active = link;
        $('#content').children().hide();
        this.linkMap[link]['cb']();
        $("#" + this.linkMap[link]['div']).show();
    };

    this.init = function() {
        for ( var link in this.linkMap) {
            console.log("init " + link);
            console.log($("#" + link));
            $("#" + link).click(this.menuClick);
        }
        this.active = this.linkMap[0];
    };
}

function _ajaxCall(url, callback) {
    console.log("get " + url);
    $("#loadingLabel").show();
    $.get(url, callback);
}

function _ajaxCallEnd(json) {
    $("#loadingLabel").hide();
    console.log("got");
    console.log(json);
}

function _getSelectedSid() {
    var selectedOption = $("#sids").find(":selected");
    return selectedOption.attr('host') + "/" + selectedOption.attr('sid');
}

function refreshSchemas() {
    $("#schemas").find("tbody").children().remove();
    _ajaxCall("/api/sid/" + _getSelectedSid() + "/schema?withTable=DB_PATCHES", refreshSchemasCB);
}
function refreshSchemasCB(json) {
    _ajaxCallEnd(json);
    table = $("#schemas").find("tbody");
    for (i in json) {
        $('#tmplSchema').tmpl(json[i]).appendTo(table);
    }
}

function refreshDumpsCB(json) {
    _ajaxCallEnd(json);
    table = $("#dumps").find("tbody");
    for (i in json) {
        $('#tmplDump').tmpl(json[i]).appendTo(table);
    }
}

function refreshDumps() {
	$("#dumps").find("tbody").children().remove();
	_ajaxCall("/api/sid/" + _getSelectedSid() + "/dump", refreshDumpsCB);
    
}

function refreshTablespace() {
    $("#tablespaces").find("tbody").children().remove();
    _ajaxCall("/api/sid/" + _getSelectedSid() + "/tablespace", refreshTablespaceCB);
}
function refreshTablespaceCB(json) {
    _ajaxCallEnd(json);
    table = $("#tablespaces").find("tbody");
    for (i in json) {
        $('#tmplTablespace').tmpl(json[i]).appendTo(table);
    }
}

function reloadSidListCB(json) {
    _ajaxCallEnd(json);
    list = $('#sids');
    list.children().remove();
    for (i in json) {
        $('#tmplSidItem').tmpl(json[i]).appendTo(list);
    }
}