function logJson(json) {
	console.log("got");
	console.log(json);
}

function reloadSidListCB(json) {
	logJson(json);
	
	list = $('#sids');
	list.children().remove();
	for (i in json) {
		$('#tmplSidItem').tmpl(json[i]).appendTo(list);   
	}
	
	$('.dbLink').click(clickDbTab);
	$('.dbLink').first().click();

	$('.hideShow').click(hideShow);
	$('#dumpInstall').click(dumpInstall);
}