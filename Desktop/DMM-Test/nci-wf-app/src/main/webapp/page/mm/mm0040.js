$(function() {
	let params = { messageCds : ['MSG0069', 'MSG0071'] };
	let $responsiveTable = new ResponsiveTable($('#searchResult'));
	$responsiveTable.modifyTR = function($tr, rowIndex, entity, labels, ignores) {
		// 画面パーツ入力可否フラグが"0:入力不可"であれば、画面パーツ入力可否フラグ・データ型を非活性に設定
		if (entity.screenPartsInputFlag === '0') {
			$tr.find('select.screenPartsInputFlag').prop('disabled', true);
			$tr.find('select.dataType').prop('disabled', true);
		}
	};
	NCI.init('/mm0040/init', params).done(function(res) {
		if (res && res.success) {
			// 選択肢の設定
			NCI.createOptionTags($('select.validFlag'), res.validFlags);
			NCI.createOptionTags($('select.screenPartsInputFlag'), res.screenPartsInputFlags);
			NCI.createOptionTags($('select.dataType'), res.dataTypes);

			// 結果反映
			$responsiveTable.fillTable(res.businessInfoNames);

			// ボタンを活性化
			$('#btnUpdate, #btnInitialize').prop('disabled', false);

			$(document)
			.on('click', '#btnUpdate', update)
			.on('click', '#btnInitialize', initialize)
			;
		}
	});

	// 更新処理
	function update() {
		let $targets = $('input, select');
		if (!Validator.validate($targets, true)) {
			return false;
		}

		let msg = NCI.getMessage('MSG0069', NCI.getMessage('docBusinessInfoName'));
		NCI.confirm(msg, function() {
			let businessInfoNameList = NCI.toArrayFromTable($('#searchResult'));
			// 更新処理
			let params = {"businessInfoNameList" : businessInfoNameList};
			NCI.post('/mm0040/save', params).done(function(res) {
				if (res && res.success) {
					$responsiveTable.fillTable(res.businessInfoNames);
				}
			});
		});
	}

	// リセット処理
	function initialize() {
		let msg = NCI.getMessage('MSG0071', NCI.getMessage('docBusinessInfoName'));
		NCI.confirm(msg, function() {
			// 更新処理
			let params = {};
			NCI.post('/mm0040/reset', params).done(function(res) {
				if (res && res.success) {
					$responsiveTable.fillTable(res.businessInfoNames);
				}
			});
		});

	}
});