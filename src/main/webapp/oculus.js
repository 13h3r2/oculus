
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
   $("#dumpInstall").click(dumpInstall);

 });


function _getSelectedSid() {
    var selectedOption = $("#sids").find(":selected");
    return selectedOption.attr('host') + "/" + selectedOption.attr('sid');
}

function _getSelectedSchema() {
	return $("#schemaDetailsName").text();
}

function refreshSchemas() {
    $("#schemas").find("tbody").children().remove();
    _ajaxCall("/api/sid/" + _getSelectedSid() + "/schema?withTable=DB_PATCHES", refreshSchemasCB);
}
function refreshSchemasCB(json) {
    table = $("#schemas").find("tbody");
    for (i in json) {
        $('#tmplSchema').tmpl(json[i]).appendTo(table);
    }
    $("#tSchemas").tablesorter();
    table.find("tr").click(showSchemaDetails);
}
function showSchemaDetails(source) {    
    var clickedSchema = $(source.target).parent().find("td").first().text();
    $("#schemaDetailsName").text(clickedSchema);
    menu.activate("schemaRefresh");
}
function refreshSchemaInfo() {
    $("#tables").find("tbody").children().remove();
    $("#schemaInfoSize").text("");
    $("#schemaInfoLastPatch").text("");
    $("#schemaInfoConnectionCount").text("");
    _ajaxCall("/api/sid/" + _getSelectedSid() + "/schema/" + _getSelectedSchema(), refreshSchemaInfoCB);
}
function refreshSchemaInfoCB(json) {
    $("#schemaInfoSize").text(json['size']);
    $("#schemaInfoLastPatch").text(json['lastPatch']);
    $("#schemaInfoConnectionCount").text(json['connectionCount']);
    _ajaxCall("/api/sid/" + _getSelectedSid() + "/schema/" + _getSelectedSchema() + "/table?minSize=1000", refreshSchemaInfoCBTable);
}
function refreshSchemaInfoCBTable(json) {
    table = $("#tables").find("tbody");
    for (i in json) {
        $('#tmplTable').tmpl(json[i]).appendTo(table);
    }
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


function disconnectSchema() {
	_confirmedCall("Disconnect all clients?", disconnectSchemaDo);
}
function disconnectSchemaDo() {
	_confirmedCallFinished();
	_ajaxCall("/api/sid/" + _getSelectedSid() + "/schema/" + _getSelectedSchema() + "?action=disconnectAll", disconnectSchemaDoCB, 'Disconnecting');
}
function disconnectSchemaDoCB(json) {
	menu.activate('schemaRefresh');
}


function dropSchema() {
	_confirmedCall("Drop schema?", dropSchemaDo);
}
function dropSchemaDo() {
	_confirmedCallFinished();
	_ajaxCall("/api/sid/" + _getSelectedSid() + "/schema/" + _getSelectedSchema() + "?action=drop", dropSchemaDoCB, 'Dropping');
}
function dropSchemaDoCB(json) {
	menu.activate('menuSchemas');
}

function truncateSchema() {
	_confirmedCall("Truncate some shit from schema?", truncateSchemaDo);
}
function truncateSchemaDo() {
	_confirmedCallFinished();
	_ajaxCall("/api/sid/" + _getSelectedSid() + "/schema/" + _getSelectedSchema() + "?action=truncate", truncateSchemaDoCB, 'Truncating');
}
function truncateSchemaDoCB(json) {
	menu.activate('schemaRefresh');
}
