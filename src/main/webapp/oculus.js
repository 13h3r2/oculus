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
            $("#" + link).click(this.menuClick);
        }
        this.active = this.linkMap[0];
    };
}

$(document).ready(function(){
    
    menu = new Menu({
        menuSchemas:{div:"schemas", cb: refreshSchemas},
        menuTablespaces:{div:"tablespaces", cb: refreshTablespace },
        menuDumps:{div:"dumps", cb:refreshDumps},
        schemaInfoFake:{div:"schemaInfo", cb:refreshSchemaInfo}
        });
    menu.init();
    
   $('#reloadSidList').click(function(){
       _ajaxCall("/api/sid", reloadSidListCB);
   });
   
   $("#confirmModalNo").click(function() {
       $("#confirmModal").modal('hide');
   });
 });



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
    table.find("tr").click(showSchemaDetails);
}
function showSchemaDetails(source) {    
    //var clickedSchema = $(source.target).parents("table").find("td").first().text();
    var clickedSchema = $(source.target).parent().find("td").first().text();
    console.log("schema info for" + clickedSchema);
    $("#schemaDetailsName").text(clickedSchema);
    menu.activate("schemaInfoFake");
}
function refreshSchemaInfo() {
    var schemaName = $("#schemaDetailsName").text();
    $("#schemaInfoSize").text("");
    $("#schemaInfoLastPatch").text("");
    $("#schemaInfoConnectionCount").text("");
    _ajaxCall("/api/sid/" + _getSelectedSid() + "/schema/" + schemaName, refreshSchemaInfoCB);
}
function refreshSchemaInfoCB(json) {
    _ajaxCallEnd(json);
    $("#schemaInfoSize").text(json['size']);
    $("#schemaInfoLastPatch").text(json['lastPatch']);
    $("#schemaInfoConnectionCount").text(json['connectionCount']);
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
