$(function() {
	let trayConfigOptions;
	const trayType = NCI.getQueryString('trayType');
	NCI.init('/wl0021/init').done(function(res) {
		if (res && res.success) {
			// トレイ設定の選択肢をあとで使用できるよう退避しておく
			trayConfigOptions = res.trayConfigOptions;

			// 現在内容の反映
			const responsiveTable = new ResponsiveTable($('#trayConfigs'));
			responsiveTable.modifyTR = modifyTR;	// データを反映する前に、トレイタイプごとの選択肢を設定
			responsiveTable.fillTable(res.entities)

			$('#btnUpdate').prop('disabled', false).click(doUpdate);
		}
	});

	/** 更新処理 */
	function doUpdate(ev) {
		const $targets = $('select, input');
		if (!Validator.validate($targets, true))
			return false;

		const msg = NCI.getMessage('MSG0071', NCI.getMessage('trayConfig'));
		NCI.confirm(msg, function() {
			const params = {
				"inputs" : NCI.toArrayFromTable($('#trayConfigs'))
			}
			NCI.post('/wl0021/save', params).done(function() {

			});
		});
		return false;
	}

	/** 業務管理項目ごとにテンプレートを書き変えるメソッド（ResponsiveTableのデフォルト実装を上書きする想定）  */
	function modifyTR($tr, rowIndex, entity, labels, ignores) {
		const options = trayConfigOptions[entity.trayType];
		const $select = $tr.find('select.trayConfigId');
		NCI.createOptionTags($select, options);
	}
});
