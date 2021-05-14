$(function() {
	NCI.init('/up0200/init', { "messageCds" : ['MSG0069', 'profileInfo'] }).done(function(res) {
		if (res && res.success) {
			init(res);
		}
	});

	// イベント
	$('#btnRegister').click(register);
	$('#btnReset').click(reset);
	$('#btnDownload').click(download).prop('disabled', false)

	/** 初期化 */
	function init(res) {
		if (res && res.success) {
			// ドラッグ＆ドロップによるファイルアップロード
			FileUploader.setup("div.dragZone", "/up0200/upload", false, displayUploadResult);
			// ファイルコントロールによるファイルアップロード
			FileUploader.setup('input[type=file]', "/up0200/upload", false, displayUploadResult);
		}
	}

	/** アップロード結果を表示 */
	function displayUploadResult(res) {
		 if(res.success){
				NCI.toElementsFromObj(res, $('#dragAndDropArea'));
				NCI.toElementsFromObj(res, $('#uploadFileArea'));
				NCI.toElementsFromObj(res, $('#inputed'));

				const isEmpty = ($('#encoded').val() == '');
				$('#dragAndDropArea').toggleClass('hide', !isEmpty);
				$('#uploadFileArea').toggleClass('hide', isEmpty);
				$('#btnUploadRegister').prop('disabled', isEmpty);
				$('#btnRegister').prop('disabled', isEmpty);
				$('div.hide-initial').toggleClass('hide', isEmpty);
		 }else{
			 $('.screen-name').before('<div class="container alert alert-danger nci-messages" id="div-alert-danger"><ul><li>' + res.alerts[0] + '</li></ul></div>');
		 }
	}

	/** 登録 */
	function register(ev) {
		const $targets = $('input[type=text]')
		if (!Validator.validate($targets)) {
			return false;
		}

		// プロファイル情報を登録してもよろしいですか？
		let msg = NCI.getMessage('MSG0069', NCI.getMessage('profileInfo'));
		NCI.confirm(msg, function() {
			const params = {
				encoded : $('#encoded').val(),
				fileName : $('#fileName').text(),
				deleteIfNotUse : $('#deleteIfNotUse')[0].checked
			};
			NCI.post('/up0200/register', params).done(function(res) {
				reset();
			});
		});
	}

	/** 表示を初期状態へ戻す */
	function reset(ev) {
		const res = {
				success : true, encoded : "", fileName : "",
				orgCount : null, postCount : null, userCount : null, ubCount : null,
				deleteIfNotUse : false
		};
		displayUploadResult(res);
		if (ev) {
			NCI.clearMessage();	// リセットボタン押下ならメッセージもクリア
		}
	}

	/** テンプレートのダウンロード */
	function download() {
		NCI.clearMessage();
		NCI.download('/up0200/download');
	}
});
