var previewMap = {};
$(function() {
	let params = {
			"messageCds" : ['MSG0069', 'MSG0072'],
			"mailTemplateFileId" : NCI.getQueryString('fileId'),
			"version" : NCI.getQueryString('fileVersion')
	};
	NCI.init('/ml0030/init', params).done(function(res) {
		if (res && res.success) {
			// 初期表示
			NCI.toElementsFromObj(res.file, $('#frmHeader'));

			$('#mailTemplateFilename').prop('disabled', !!res.file.mailTemplateFileId)

			$('#btnRegister').click(doRegist).prop('disabled', false);
		}
	});

	// 戻るボタン押下
	$('#btnBack').click(function(ev) {
		let headerId = NCI.getQueryString('headerId');
		let fileId = NCI.getQueryString('fileId');
		let version = NCI.getQueryString('headerVersion');
		if (!fileId)
			NCI.redirect('./ml0010.html');
		else
			NCI.redirect('./ml0011.html'
					+ '?headerId=' + headerId
					+ '&fileId=' + fileId
					+ '&version=' + version);
	});

	// 更新処理
	function doRegist(ev) {
		let $targets = $('input, textarea');
		if (!Validator.validate($targets, true))
			return false;

		let msg = NCI.getMessage('MSG0069', NCI.getMessage('mailTemplateFile'));
		NCI.confirm(msg, function() {
			// 更新処理
			let params = { "file" : NCI.toObjFromElements($('#frmHeader')) };
			NCI.post('/ml0030/save', params).done(function(res) {
				if (res && res.success) {
					$('#btnBack').click();
				}
			})
		});
		return false;
	}
});
