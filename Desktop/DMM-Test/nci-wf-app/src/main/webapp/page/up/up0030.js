$(function() {
	const params = {
			'uploadFileId': NCI.getQueryString('uploadFileId'),
			'messageCds' : ['MSG0069', 'menuRole']
	};
	NCI.init('/up0030/init', params).done(function(res) {
		if (res && res.success) {
			init(res);
		}
	});

	/** 初期化 */
	function init(res) {
		if (res && res.success) {
			// ドラッグ＆ドロップによるファイルアップロード
			FileUploader.setup("div.dragZone", "/up0030/upload", false, displayUploadResult);
			// ファイルコントロールによるファイルアップロード
			FileUploader.setup('input[type=file]', "/up0030/upload", false, displayUploadResult);

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
		new ResponsiveTable($('#inputed')).fillTable(res.menuRoles);

		let isEmpty = ($('#uploadFileId').val() == '');
		$('#dragAndDropArea').toggleClass('hide', !isEmpty);
		$('#processInfoArea').toggleClass('hide', isEmpty);
		$('#btnRegister').prop('disabled', isEmpty);
		$('div.hide-initial').toggleClass('hide', isEmpty);
		selectAll(true);
	}

	/** 登録 */
	function register(ev) {
		let $targets = $('input[type=text]')
		if (!Validator.validate($targets)) {
			return false;
		}

		// プロセス定義を登録してもよろしいですか？
		let msg = NCI.getMessage('MSG0069', NCI.getMessage('menuRole'));
		NCI.confirm(msg, function() {
			// コンテナコードの新旧対応リスト
			let menuRoleCodes = [];
			$('tbody input[data-field=menuRoleCode][value]:checked').each(function(i, radio) {
				menuRoleCodes.push(radio.value);
			});
			let params = {
					"uploadFileId" : $('#uploadFileId').val(),
					"menuRoleCodes" : menuRoleCodes,
					"isMenuRoleDetail" : $('#isMenuRoleDetail').prop('checked')
			};

			// 実際の登録処理
			NCI.post('/up0030/register', params).done(function(res) {
				reset();
			});
		});
	}

	/** 表示を初期状態へ戻す */
	function reset(ev) {
		let res = {
				success : true, uploadFileId : "", timestampCreated : "", file : "",
				hostIpAddr : "", appVersion : "", dbDestination : "", hostName : "",
				corporationCode : "", corporationName : "", menuRoles : []
		};
		displayUploadResult(res);
		if (ev) {
			NCI.clearMessage();	// リセットボタン押下ならメッセージもクリア
		}
	}

	/** 全選択／全解除 */
	function selectAll(checked) {
		$('#inputed').find('input[data-field=menuRoleCode]').prop('checked', checked);
	}

	function back(ev) {
		NCI.redirect('./up0100.html');
	}
});
