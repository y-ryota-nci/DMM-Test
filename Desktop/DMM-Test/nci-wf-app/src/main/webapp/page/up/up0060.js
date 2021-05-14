$(function() {
	const params = {
			'uploadFileId': NCI.getQueryString('uploadFileId'),
			'messageCds' : ['MSG0069', 'systemProperty']
	};
	NCI.init('/up0060/init', params).done(function(res) {
		if (res && res.success) {
			init(res);
		}
	});

	/** 初期化 */
	function init(res) {
		if (res && res.success) {
			// ドラッグ＆ドロップによるファイルアップロード
			FileUploader.setup("div.dragZone", "/up0060/upload", false, displayUploadResult);
			// ファイルコントロールによるファイルアップロード
			FileUploader.setup('input[type=file]', "/up0060/upload", false, displayUploadResult);

			$('#btnRegister').click(register);
			$('#btnReset').click(reset);
			$('#btnSelectAll').click(function() { selectAll(true); });
			$('#btnCancelAll').click(function() { selectAll(false); });

			// 戻るボタンはアップロードファイルIDが指定されたときのみ
			$('#btnBack')
					.click(back)
					.toggleClass('hide', (res.uploadFileId == null));

			// ファイルアップロードIDが指定されていれば、対応するファイル内容を表示
			if (res.uploadFileId) {
				displayUploadResult(res);
			}
		}
	}

	/** アップロード結果を表示 */
	function displayUploadResult(res) {
		NCI.toElementsFromObj(res, $('#dragAndDropArea'));
		NCI.toElementsFromObj(res, $('#processInfoArea'));
		NCI.toElementsFromObj(res, $('#inputed'));

		let isEmpty = ($('#uploadFileId').val() == '');
		$('#dragAndDropArea').toggleClass('hide', !isEmpty);
		$('#processInfoArea').toggleClass('hide', isEmpty);
		$('#btnRegister').prop('disabled', isEmpty);
		$('div.hide-initial').toggleClass('hide', isEmpty);
		selectAll(true);
	}

	/** 登録 */
	function register(ev) {
		const $targets = $('input[type=text]')
		if (!Validator.validate($targets)) {
			return false;
		}

		// システムプロパティを登録してもよろしいですか？
		let msg = NCI.getMessage('MSG0069', NCI.getMessage('systemProperty'));
		NCI.confirm(msg, function() {
			const params = NCI.toObjFromElements($('#inputed'));
			params.uploadFileId = $('#uploadFileId').val();
			NCI.post('/up0060/register', params).done(function(res) {
				reset();
			});
		});
	}

	/** 表示を初期状態へ戻す */
	function reset(ev) {
		let res = {
				success : true, uploadFileId : "", timestampCreated : "", file : "",
				hostIpAddr : "", appVersion : "", dbDestination : "", hostName : "",
				corporationCode : "", corporationName : "",
				enableCorporationProperty : true, enableCorpPropMaster : true, enableMailConfig : true
		};
		displayUploadResult(res);
		if (ev) {
			NCI.clearMessage();	// リセットボタン押下ならメッセージもクリア
		}
	}

	/** 全選択／全解除 */
	function selectAll(checked) {
		$('#inputed').find('input[data-field=mailTemplateFilename]').prop('checked', checked);
	}

	function back(ev) {
		NCI.redirect('./up0100.html');
	}
});
