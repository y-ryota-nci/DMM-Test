$(function() {
	const params = NCI.flushScope(NCI.getQueryString(FLUSH_SCOPE_KEY));
	const $root = $('#contents');
	NCI.init("/dc0902/init").done(function(res) {
		if (res && res.success) {
			NCI.toElementsFromObj(params, $root);
		}
	});

	$(document)
	.on('click', '#btnSelectDocFolder', function(ev) {
		const params = {"corporationCode" : $('#corporationCode').text(), "excludeDocFolderId" : $('#exclusionDocFolderId').text()};
		const url = "../cm/cm0200.html";
		Popup.open(url, callbackFromCm0200, params);
	})
	.on('click', '#btnClearDocFolder', function(ev) {
		const $div = $('#divFolderInfo');
		$div.find('input').val('');
	})
	.on('click', '#btnMove' , function(ev){
		let $targets = $root.find('input, select, textarea');
		if (!Validator.validate($targets, true)) {
			return false;
		}
		let result = NCI.toObjFromElements($root);
		Popup.close(result);
	})
	.on('click', '#btnClose', function() {
		Popup.close();
	});

	function callbackFromCm0200(result) {
		if (result) {
			const $div = $('#divFolderInfo');
			NCI.toElementsFromObj(result, $div);
		}
	}
});
