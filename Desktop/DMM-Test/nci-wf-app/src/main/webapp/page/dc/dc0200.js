$(function() {
	let docTrayConfigOptions;
	const docTrayType = NCI.getQueryString('docTrayType');
	NCI.init('/dc0200/init').done(function(res) {
		if (res && res.success) {
			// トレイ設定の選択肢をあとで使用できるよう退避しておく
			docTrayConfigOptions = res.docTrayConfigOptions;

			// 現在内容の反映
			const responsiveTable = new ResponsiveTable($('#docTrayConfigs'));
			responsiveTable.modifyTR = modifyTR;	// データを反映する前に、文書トレイタイプごとの選択肢を設定
			responsiveTable.fillTable(res.entities)

			$('#btnUpdate').prop('disabled', false).click(doUpdate);
		}
		$('#btnBack').click(doBack);
	});

	/** 更新処理 */
	function doUpdate(ev) {
		const $targets = $('select, input');
		if (!Validator.validate($targets, true))
			return false;

		const msg = NCI.getMessage('MSG0071', NCI.getMessage('docTrayConfig'));
		NCI.confirm(msg, function() {
			const params = {
				"inputs" : NCI.toArrayFromTable($('#docTrayConfigs'))
			}
			NCI.post('/dc0200/save', params).done(function() {

			});
		});
		return false;
	}

	/** 呼び元の画面へ戻る */
	function doBack(ev) {
		TrayHelper.backToTray(docTrayType);
	}

	/** 業務管理項目ごとにテンプレートを書き変えるメソッド（ResponsiveTableのデフォルト実装を上書きする想定）  */
	function modifyTR($tr, rowIndex, entity, labels, ignores) {
		const options = docTrayConfigOptions[entity.docTrayType];
		const $select = $tr.find('select.docTrayConfigId');
		NCI.createOptionTags($select, options);
	}
});
