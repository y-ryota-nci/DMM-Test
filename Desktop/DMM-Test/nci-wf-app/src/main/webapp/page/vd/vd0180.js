$(function() {
	const parameters = NCI.flushScope(NCI.getQueryString(FLUSH_SCOPE_KEY));
	NCI.init('/vd0180/init').done(function(res) {
		if (res && res.success) {
			NCI.toElementsFromObj(parameters, $('#inputs'));
		}

		$(document)
		// 更新ボタン押下
		.on('click', '#btnUpdate', function(e) {
			const $newPartsCode = $('#newPartsCode');
			if (!Validator.validate($newPartsCode, true))
				return false;

			parameters.newPartsCode = $newPartsCode.val();
			NCI.post('/vd0180/change', parameters).done(function(res) {
				if (res && res.success) {
					Popup.close(parameters.newPartsCode, parameters.ctx.designMap);
				}
			});
			return false;
		})
		.on('click', '#btnClose', function() {
			Popup.close();
		})
	});

});
