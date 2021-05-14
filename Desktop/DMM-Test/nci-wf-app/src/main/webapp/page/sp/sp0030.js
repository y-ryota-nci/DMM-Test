$(function() {

	const params = { messageCds : [] };
	NCI.init("/sp0030/init", params).done(function(res) {
		if (res && res.success) {

			// イベント
			$('#btnUploadRegister').click(uploadRegister);
			$('#btnUploadReset').click(uploadReset);

			$('#btnUploadRegister').prop('disabled', false);

			// ドラッグ＆ドロップによるファイルアップロード
			FileUploader.setup("div.dragZone", "/sp0030/upload", false, displayUploadResult);

			// ファイルコントロールによるファイルアップロード
			FileUploader.setup('input[type=file]', "/sp0030/upload", false, displayUploadResult);
		}
	});

	/** アップロード結果を表示 */
	function displayUploadResult(res) {
		NCI.toElementsFromObj(res, $('#dragAndDropArea'));
		NCI.toElementsFromObj(res, $('#uploadFileArea'));

		const isEmpty = ($('#encoded').val() == '');
		$('#dragAndDropArea').toggleClass('hide', !isEmpty);
		$('#uploadFileArea').toggleClass('hide', isEmpty);
		$('#btnUploadRegister').prop('disabled', isEmpty);
	}

	/** 登録 */
	function uploadRegister(ev) {
		const $targets = $('input[type=text]')
		if (!Validator.validate($targets)) {
			return false;
		}

		let msg = '取引先情報を登録します。よろしいですか？';
		NCI.confirm(msg, function() {
			const params = {
				encoded : $('#encoded').val(),
				fileName : $('#fileName').text()
			};
			NCI.post('/sp0030/register', params).done(function(res) {
				uploadReset();
			});
		});
	}

	/** 表示を初期状態へ戻す */
	function uploadReset(ev) {
		const res = {
				success : true, encoded : "", fileName : "",
				splrCount : null, payeeBnkaccCount : null,
				deleteIfNotUse : false
		};
		displayUploadResult(res);
		if (ev) {
			NCI.clearMessage();	// リセットボタン押下ならメッセージもクリア
		}
	}
});

