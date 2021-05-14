$(function() {
	const params = {
			 'uploadFileId': NCI.getQueryString('uploadFileId'),
			 'messageCds' : ['MSG0069', 'MSG0003', 'screenDifinition', 'targetsOfImport']
	};
	NCI.init('/up0010/init', params).done(function(res) {
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
			.on('click', 'tbody input[type=checkbox].selectable', selectable_click)
			;

	/** 初期化 */
	function init(res) {
		if (res && res.success) {
			// ドラッグ＆ドロップによるファイルアップロード
			FileUploader.setup("div.dragZone", "/up0010/upload", false, displayUploadResult);
			// ファイルコントロールによるファイルアップロード
			FileUploader.setup('input[type=file]', "/up0010/upload", false, displayUploadResult);

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
		NCI.toElementsFromObj(res, $('#fileInfoArea'));
		NCI.toElementsFromObj(res, $('#inputed'));

		const render = new ResponsiveTable($('#inputed'));
		render.modifyTR = function($tr, rowIndex, entity, labels, ignores) {
			// テーブルの中の子テーブルをレンダリング
			new ResponsiveTable($tr).fillTable(entity.containerCodes);
		};
		render.fillTable(res.results, ['containerCode']);

		const isEmpty = ($('#uploadFileId').val() === '');
		$('#dragAndDropArea').toggleClass('hide', !isEmpty);
		$('#fileInfoArea').toggleClass('hide', isEmpty);
		$('#btnRegister').prop('disabled', isEmpty);
		$('div.hide-initial').toggleClass('hide', isEmpty);
		$('input[type=checkbox].screenProcess').click(screenProcess_onclick);
	}

	/** 登録 */
	function register(ev) {
		const $targets = $('input[type=text]')
		if (!Validator.validate($targets)) {
			return false;
		}
		const $selects = $('tbody input[type=checkbox].selectable:checked');
		if ($selects.length === 0) {
			NCI.addMessage('danger', NCI.getMessage('MSG0003', NCI.getMessage('targetsOfImport')));
			return false;
		}

		// 画面定義を登録してもよろしいですか？
		const msg = NCI.getMessage('MSG0069', NCI.getMessage('screenDifinition'));
		NCI.confirm(msg, function() {
			const params = { 'configs': [] };
			$selects.each(function() {
				const $tr = $(this).closest('tr');
				const config = {
						// 画面コードの新旧
						'corporationCode' : NCI.loginInfo.corporationCode,
						'screenCode' : $tr.find('span[data-field=screenCode]').text(),
						'newScreenCode' : $tr.find('input[data-field=screenCode]').val(),
						'isPublication' :  $tr.find('input[type=checkbox].publication').prop('checked'),
						'isScreenProcess' : $tr.find('input[type=checkbox].screenProcess').prop('checked'),
						'isFolder' : $tr.find('input[type=checkbox].folder').prop('checked'),
						'isScreenProcessMenu' : $tr.find('input[type=checkbox].screenProcessMenu').prop('checked'),
						'newContainerCodes' : {}
				}
				// コンテナコードの新旧
				$tr.find('td.container > table > tbody > tr').each(function() {
					const containerTR = $(this);
					const containerCode = containerTR.find('span[data-field=containerCode]').text();
					const newContainerCode = containerTR.find('input[data-field=containerCode]').val();
					config.newContainerCodes[containerCode] = newContainerCode;
				})
				params.configs.push(config);
			});

			// 新旧の画面コード／コンテナコードを比較し、同一であれば上書き確認
			NCI.post('/up0010/confirm', params).done(function(res) {
				NCI.confirm(res.confirmMessage, function() {
					// 実際の登録処理
					params.uploadFileId = $('#uploadFileId').val();
					NCI.post('/up0010/register', params).done(function(res) {
						reset();
					});
				})
			});
		});
	}

	/** 表示を初期状態へ戻す */
	function reset(ev) {
		const res = {
				"uploadFileId": "", "timestampCreated": "", "fileName": "",
				"dbDestination": "", "appVersion" : "", "hostIpAddr": "",
				"hostName": "", "corporationCode": "", "corporationName": "",
				"results": [], "allCount" : 0, "count": 0
		};
		displayUploadResult(res);
		if (ev) {
			NCI.clearMessage();	// リセットボタン押下ならメッセージもクリア
		}
	}

	/** 画面プロセス設定チェックボックスに連動するチェックボックス制御 */
	function screenProcess_onclick(ev) {
		// 「公開管理」と「新規フォルダ設定」が取り込めるかは画面プロセスに依存する
		const $tr = $(ev.currentTarget).closest('tr');
		const checked = ev.currentTarget.checked;	// 画面プロセスを取込むかのチェックボックス
		if (checked) {
			$tr.find('input[type=checkbox].publication').prop("disabled", false);
			$tr.find('input[type=checkbox].folder').prop("disabled", false);
		}
		else {
			$tr.find('input[type=checkbox].publication').prop("disabled", true).prop('checked', false);
			$tr.find('input[type=checkbox].folder').prop("disabled", true).prop('checked', false);
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
