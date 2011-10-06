
$(document).ready(function(){
    
    menu = new Menu({
        menuSchemas:{div:"schemas", cb: refreshSchemas},
        menuTablespaces:{div:"tablespaces", cb: refreshTablespace},
        menuDumps:{div:"dumps", cb:refreshDumps},
        schemaRefresh:{div:"schemaInfo", cb:refreshSchemaInfo},
        dumpInfoRefresh:{div:"dumpInfo", cb:refreshDumpInfo}
        });
    menu.init();
    
   $('#reloadSidList').click(function(){
       _ajaxCall("/api/sid", reloadSidListCB);
   });
   
   $("#confirmModalNo").click(function(){$("#confirmModal").modal('hide');});
   $("#schemaInfoDrop").click(dropSchema);
   $("#schemaInfoTruncate").click(truncateSchema);
   $("#schemaInfoDisconnect").click(disconnectSchema);
   $("#schemaInfoGrant").click(grantSchema);
   $("#dumpInstall").click(dumpInstall);
   
   $("#schemas").children("table").tablesorter();

 });


function _getSelectedSid() {
    var selectedOption = $("#sids").find(":selected");
    return selectedOption.attr('host') + "/" + selectedOption.attr('sid');
}


function showDumpDetails(source) {    
	var clickedDump = $(source.target).parent().find("td").first().text();
	$("#dumpDetailsName").text(clickedDump);
	menu.activate("dumpInfoRefresh");
}
function refreshDumpInfo() {
	$("#remapSchemaName").val("B_ASR_NEW");
	$("#schemaName").val("");
}


function dumpInstall() {
	var dumpName = $("#dumpDetailsName").text();
	var schema = $("#schemaName").val();
	var remapFrom = $("#remapSchemaName").val();
	_ajaxCall("/api/sid/" + _getSelectedSid() + "/dump/" + dumpName + "?schema=" + schema + "&remapFrom=" + remapFrom + "&action=install", dumpInstallCB);
}
function dumpInstallCB(json) {
	_notificationInfo("Dump installation started");
	$("#schemaName").val("");
}



function refreshDumps() {
	$("#dumps").find("tbody").children().remove();
	_ajaxCall("/api/sid/" + _getSelectedSid() + "/dump", refreshDumpsCB);
}
function refreshDumpsCB(json) {
    table = $("#dumps").find("tbody");
    for (i in json) {
        $('#tmplDump').tmpl(json[i]).appendTo(table);
    }
    table.find("tr").click(showDumpDetails);
}


function refreshTablespace() {
    $("#tablespaces").find("tbody").children().remove();
    _ajaxCall("/api/sid/" + _getSelectedSid() + "/tablespace", refreshTablespaceCB);
}
function refreshTablespaceCB(json) {
    table = $("#tablespaces").find("tbody");
    for (i in json) {
        $('#tmplTablespace').tmpl(json[i]).appendTo(table);
    }
}


function reloadSidListCB(json) {
    list = $('#sids');
    list.children().remove();
    for (i in json) {
        $('#tmplSidItem').tmpl(json[i]).appendTo(list);
    }
}


