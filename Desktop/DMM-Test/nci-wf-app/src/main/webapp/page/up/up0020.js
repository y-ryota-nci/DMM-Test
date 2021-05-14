$(function() {
	const params = {
			'uploadFileId': NCI.getQueryString('uploadFileId'),
			'messageCds' : ['MSG0069', 'MSG0003', 'processDef', 'targetsOfImport']
	};
	NCI.init('/up0020/init', params).done(function(res) {
		if (res && res.success) {
			init(res);
		}
	});

	$(document)
			.on('click', '#btnRegister', register)
			.on('click', '#btnReset', reset)
			.on('click', '#btnBack', back)
			.on('click', '#btnSelectAll', function(){selectAll(true);})
			.on('click', '#btnCancelAll', function(){selectAll(false);})
			.on('click', 'tbody input[type=checkbox].selectable', selectable_click);

	/** 初期化 */
	function init(res) {
		if (res && res.success) {
			// ドラッグ＆ドロップによるファイルアップロード
			FileUploader.setup("div.dragZone", "/up0020/upload", false, displayUploadResult);
			// ファイルコントロールによるファイルアップロード
			FileUploader.setup('input[type=file]', "/up0020/upload", false, displayUploadResult);

			// 戻るボタンはアップロードファイルIDが指定されたときのみ
			$('#btnBack').toggleClass('hide', (res.uploadFileId == null));

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

        new ResponsiveTable($('#inputed')).fillTable(res.results)

		const isEmpty = ($('#uploadFileId').val() === '');
		$('#dragAndDropArea').toggleClass('hide', !isEmpty);
		$('#processInfoArea').toggleClass('hide', isEmpty);
		$('#btnRegister').prop('disabled', isEmpty);
		$('.hide-initial').toggleClass('hide', isEmpty);
	}

	/** 登録 */
	function register(ev) {
		const $targets = $('#inputed input')
		if (!Validator.validate($targets)) {
			return false;
		}
		const $selects = $('tbody input[type=checkbox].selectable:checked');
		if ($selects.length === 0) {
			NCI.addMessage('danger', NCI.getMessage('MSG0003', NCI.getMessage('targetsOfImport')));
			return false;
		}

		// プロセス定義を登録してもよろしいですか？
		const msg = NCI.getMessage('MSG0069', NCI.getMessage('processDef'));
		NCI.confirm(msg, function() {
			// コンテナコードの新旧対応リスト
			const params = {
					'uploadFileId' : $('#uploadFileId').val(),
					'isAssignRoleDetail': $('#isAssignRoleDetail').prop('checked'),
					'isChangeRoleDetail': $('#isChangeRoleDetail').prop('checked'),
					'configs' : []
			};
			$selects.each(function() {
				const $tr = $(this).closest('tr');
				params.configs.push({
					"corporationCode" : $tr.find('span[data-field=corporationCode]').text(),
					"processDefCode" : $tr.find('span[data-field=processDefCode]').text(),
					"processDefDetailCode" : $tr.find('span[data-field=processDefDetailCode]').text(),
					"newProcessDefCode" : $tr.find('input[data-field=processDefCode]').val(),
					"newProcessDefDetailCode" : $tr.find('input[data-field=processDefDetailCode]').val(),
					"isAuthTransfer" : $tr.find('input[data-field=isAuthTransfer]').prop('checked')
				});
			});

			// 新旧のプロセス定義明細コードを比較し、同一であれば上書き確認
			NCI.post('/up0020/confirm', params).done(function(res) {
				NCI.confirm(res.confirmMessage, function() {
					// 実際の登録処理
					NCI.post('/up0020/register', params).done(function(res) {
						reset();
					});
				})
			});
		});
		return false;
	}

	/** 表示を初期状態へ戻す */
	function reset(ev) {
		let res = {
				"uploadFileId": "", "timestampCreated": "", "fileName": "",
				"dbDestination": "", "appVersion" : "", "hostIpAddr": "",
				"hostName": "", "corporationCode": "", "corporationName": "",
				"isChangeRoleDetail": false, "isAssignRoleDetail": false,
				"results": [], "allCount" : 0, "count": 0
		};
		displayUploadResult(res);
		if (ev) {
			NCI.clearMessage();	// リセットボタン押下ならメッセージもクリア
		}
	}


	/** 全選択／全解除 */
	function selectAll(checked) {
		$('tbody input[type=checkbox].selectable').prop('checked', checked);
		selectable_click();
	}

	/** 選択チェックボックス押下時 */
	function selectable_click() {
		$('#count').text($('tbody input[type=checkbox].selectable:checked').size())
	}

	function back(ev) {
		NCI.redirect('./up0100.html');
	}
});
