$(function() {
	var parameters = NCI.flushScope(NCI.getQueryString(FLUSH_SCOPE_KEY));
	NCI.init('/vd0125/init').done(function(res) {
		if (res && res.success) {
			// データを反映
			new ResponsiveTable($('#inputed')).fillTable(parameters.parts.events);

			// 更新ボタン押下
			$('#btnOK')
				.prop('disabled', false)
				.click(update);
		}

		$('#btnClose')
			.click(function() { Popup.close(); });
	});
});

function update() {
	var $root = $('#inputed');
	if (!Validator.validate($root.find("input, textarea, select", true))) {
		return false;
	}
	// 入力内容を吸い上げ
	const events = NCI.toArrayFromTable($root);

	// コールバック関数の呼び出し
	Popup.close(events);
}