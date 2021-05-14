$(function() {

	let screenDocId = NCI.getQueryString('screenDocId');
	let version = NCI.getQueryString('version');
	let params = {
			'screenDocId' : screenDocId,
			'version' : version,
			'messageCds' : [ 'screenDocInfo', 'MSG0069', 'MSG0071' ]
	};
	NCI.init('/dc0111/init', params).done(function(res) {
		if (res && res.success) {
			// 選択肢
			NCI.createOptionTags($('#screenId'), res.screenIds);
			NCI.createOptionTags($('#screenProcessCode'), res.screenProcessCodes);

			// データを反映
			NCI.toElementsFromObj(res.entity);

			// 画面文書定義コードは新規の場合のみ入力可
			let $screenDocCode = $('#screenDocCode');
			$screenDocCode.prop('readonly', $screenDocCode.val() !== '');

			// カレンダー（年月日）
			NCI.ymdPicker($('input.ymdPicker'));

			// 画面変更時処理
			$('#screenId').change(changeScreen);

			// 文書フォルダ選択ボタン押下
			$('#btnSelectDocFolder').click(openCm0200);

			// 文書フォルダクリアボタン押下
			$('#btnClearDocFolder').click(clearFolder);

			// 更新ボタン押下
			$('#btnUpdate')
				.prop('disabled', false)
				.click(update);
		}

		$('#btnBack').click(function(ev) {
			NCI.redirect('./dc0110.html');
		});
	});

	/** 画面変更時処理 */
	function changeScreen(ev) {
		let params = {'corporationCode': $('#corporationCode').val(), 'screenId': $('#screenId').val()};
		NCI.post('/dc0111/change', params).done(function(res) {
			if (res && res.success) {
				// WF連携先の選択肢を切り替え
				NCI.createOptionTags($('#screenProcessCode'), res.screenProcessCodes);
			}
		});
	}

	/** 文書フォルダ選択 */
	function openCm0200(ev) {
		const params = {"corporationCode": $('#corporationCode').val()};
		const url = '../cm/cm0200.html';
		Popup.open(url, callbackFromCm0200, params);
	}

	/** 文書フォルダ選択画面からのコールバック */
	function callbackFromCm0200(result) {
		if (result) {
			$('#folderPath').val(result.folderPath);
			$('#folderCode').val(result.folderCode);
		}
	}

	function clearFolder(ev) {
		$('#folderPath').val('');
		$('#folderCode').val('');
	}

	/** 更新処理 */
	function update(ev) {
		// バリデーション
		let $root = $('#inputed');
		let $targets = $root.find('input, select');
		if (!Validator.validate($targets, true)) {
			return false;
		}

		// 確認メッセージ
		let msgCode = ($('#screenDocCode').val() === '' ? 'MSG0069' : 'MSG0071');
		let msg = NCI.getMessage(msgCode, NCI.getMessage('screenDocInfo'));
		NCI.confirm(msg, function() {
			// 更新処理
			let params = {
					'entity' : NCI.toObjFromElements($root, ['folderPath'])
			};
			NCI.post('/dc0111/save', params).done(function(res) {
				if (res && res.success) {
					$('#btnBack').click();
				}
			})
		});
	}
});
