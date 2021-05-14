$(function() {
	let paramters = NCI.flushScope(NCI.getQueryString(FLUSH_SCOPE_KEY));
	let parasm = {
			messageCds : ["MSG0069", "memo"]
	};
	NCI.init("/cm0180/init").done(function(res) {
		if (res && res.success) {
			$('#btnOK').click(update);
		}
		$('#btnClose').click(function() {
				Popup.close();
		})
	});

	/** コールバック関数を実行 */
	function update(ev) {
		// バリデーション
		let $inputs = $('input, select, textarea');
		if (!Validator.validate($inputs, true)) {
			return false;
		}
		let msg = NCI.getMessage('MSG0069', NCI.getMessage('memo'));
		NCI.confirm(msg, function() {
			// コールバック関数の呼び出し
			let processMemoInfo = NCI.toObjFromElements($('#inputed'));
			Popup.close(processMemoInfo);
		});
	}
});

