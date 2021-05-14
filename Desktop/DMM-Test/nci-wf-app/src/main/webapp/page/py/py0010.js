$(function() {
	NCI.init('/py0010/init', { "messageCds" : [] }).done(function(res) {
		if (res && res.success) {
			// カレンダー（年月）
			NCI.ymPicker($('input.ymPicker'));

			// 支払月の初期値を設定
			$('#payYm').val(res.payYm);
			$("#targetYmArea").toggleClass('hide', false);
			$("#dragAndDropArea").toggleClass('hide', false);

			// ドラッグ＆ドロップによるファイルアップロード
			FileUploader.setup("div.dragZone", "/py0010/upload", false, complete, prepareCallback);
			// ファイルコントロールによるファイルアップロード
			FileUploader.setup("input[type=file]", "/py0010/upload", false, complete, prepareCallback);
		}
	});

	// 処理対象月に対するイベント
	$('#payYm').on('validate', function(e) {
		// 未入力であればアップロードエリアを非表示にする
		if ($(this).val() === '') {
			$("#dragAndDropArea").toggleClass('hide', true);
		} else {
			$("#dragAndDropArea").toggleClass('hide', false);
		}
	});

	/** アップロード処理完了後のコールバック */
	function complete(res) {
		// メッセージ
		if (res && res.successes && res.successes.length)
			NCI.addMessage('success', res.successes);
		if (res && res.warns && res.warns.length)
			NCI.addMessage('warning', res.warns);
		if (res && res.alerts && res.alerts.length)
			NCI.addMessage('danger', res.alerts);
	}

	/** アップロード用の追加パラメータを生成して返す */
	function prepareCallback() {
		return { "payYm" : $('#payYm').val()};
	}
});
