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
    console.log($("#schemas").children("table"));
    $("#schemas table").trigger("update");
    var sorting = [[1,1]]; 
    $("#schemas table").trigger("sorton",[sorting]); 
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
    _ajaxCall("/api/sid/" + _getSelectedSid() + "/schema/" + _getSelectedSchema(), refreshSchemaInfoCB, null, true);
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


function grantSchema() {
	_confirmedCall("Grant access to tablespaces?", grantSchemaDo);
}
function grantSchemaDo() {
	_confirmedCallFinished();
	_ajaxCall("/api/sid/" + _getSelectedSid() + "/schema/" + _getSelectedSchema() + "?action=grant", null, 'Granting');
}
