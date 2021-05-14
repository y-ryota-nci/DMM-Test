$(function() {
	const params = NCI.flushScope(NCI.getQueryString(FLUSH_SCOPE_KEY));
	const $root = $('#contents');
	NCI.init("/dc0906/init").done(function(res, textStatus, jqXHR) {
		if (res && res.success) {
			NCI.toElementsFromObj(params, $root);
		}
	});

	$(document)
	.on('click', '#btnSelectDocInfo', function(ev) {
		const params = {"corporationCode" : $('#corporationCode').text(), "excludeDocId" : $('#excludeDocId').text()};
		const url = "../cm/cm0210.html";
		Popup.open(url, callbackFromCm0210, params);
	})
	.on('click', '#btnClearDocInfo', function(ev) {
		const $div = $('#divDocInfo');
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

	function callbackFromCm0210(result) {
		if (result) {
			$('#title').val(result.title);
			$('#toDocId').val(result.docId);
		}
	}
});
