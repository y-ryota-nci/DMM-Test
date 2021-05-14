$(function() {
	let responsitveTable = new ResponsiveTable($('#divRoot'));
	let params = { "messageCds" : ['MSG0071', 'MSG0072', 'category'] };
	NCI.init('/ti0011/init', params).done(function(res) {
		if (res.success) {
			responsitveTable.fillTable(res.categories);
			$('#divRoot table>tbody input[data-field=categoryCode]').prop('disabled', true);
		}

		$('#btnAdd').click(addRow).prop('disabled', false);
		$('#btnDelete').click(execDelete);
		$('#btnUpdate').click(execUpdate).prop('disabled', false);
		$(document).on('click', 'input[type=checkbox]', whenSelected);
	});

	$('#btnClose').click(function(ev) {
		Popup.close();
	});

	/** CHECKBOXを選択 */
	function whenSelected() {
		$('#btnDelete').prop('disabled', $('input[type=checkbox]:checked').length === 0);
	}

	/** 更新処理 */
	function execUpdate() {
		let $root = $('#divRoot');
		let $targets = $root.find('input');
		if (!Validator.validate($targets, true)) {
			return false;
		}
		let msg = NCI.getMessage('MSG0071', NCI.getMessage('category'));
		if (NCI.confirm(msg, function() {
			let params = {
					"inputs" : NCI.toArrayFromTable($root)
				};
				NCI.post('/ti0011/save', params).done(function(res) {
					if (res.success) {
						// コールバック関数の呼び出し
						Popup.close(res.categories);

						$('#btnBack').click();
					}
				});
		}));
		return true;
	}

	/** 削除処理 */
	function execDelete() {
		let msg = NCI.getMessage('MSG0072', NCI.getMessage('category'));
		if (NCI.confirm(msg, function() {
			// 選択行のみ吸い上げる
			let inputs = [];
			$('#divRoot>table>tbody input[type=checkbox]:checked').each(function(i, elem) {
				let $tr = $(elem).parent().parent();
				let row = NCI.toObjFromElements($tr);
				if (row.categoryId) {
					inputs.push(row);
				}
			});

			let params = { "inputs" : inputs };
			NCI.post('/ti0011/delete', params).done(function(res) {
				if (res && res.success) {
					// コールバック関数の呼び出し
					Popup.close(res.categories);

					$('#btnBack').click();
				}
			});
		}));
	}

	/** 空行の追加 */
	function addRow() {
		let entity = {
				"corporationCode" : NCI.loginInfo.corporationCode,
				"categoryId" : null,
				"categoryCode" : '',
				"categoryName" : ''
		}
		responsitveTable.addRowResult(entity);
	}
});
