// Button：vd0114.jsから呼び出される初期化ロジック
function initSpecific(ctx, design) {
	const INPUT = "1", OUTPUT = "2", BOTH = "3";
	let $root = $('#vd0114_50');
	let responsiveTable = new ResponsiveTable($('#fsRelation'));
	let params = { 'design' : design, 'tableName' : ctx.root.tableName };
	NCI.post('/vd0114/initMaster', params).done(function(res) {
		// パーツの選択肢
		let targetPartsTypes = [PartsType.TEXTBOX, PartsType.CHECKBOX, PartsType.RADIO, PartsType.DROPDOWN, PartsType.MASTER];
		let targetPartsIds = [{ "value" : "", "label" : "--" }];
		$.each(ctx.root.childPartsIds, function(i, partsId) {
			let p = ctx.designMap[partsId];
			if (targetPartsTypes.indexOf(p.partsType) >= 0 && partsId != design.partsId) {
				targetPartsIds.push({ "value" : p.partsId, "label" : p.labelText })
			}
		});

		// 汎用テーブルIDの選択肢
		NCI.createOptionTags($('#tableId'), res.tableIds);
		// 汎用テーブル検索条件IDの選択肢
		NCI.createOptionTags($('#tableSearchId'), res.tableSearchIds);
		// 未選択行の表示の選択肢
		NCI.createOptionTags($('#emptyLineType'), res.emptyLineTypes);
		// パーツの選択肢
		NCI.createOptionTags($('select[data-field=targetPartsId]'), targetPartsIds);
		// カラム名の選択肢
		NCI.createOptionTags($('#columnNameValue'), res.columnNamesValue);
		NCI.createOptionTags($('#columnNameLabel'), res.columnNamesLabel);
		NCI.createOptionTags($('select.columnNamesInOrOut'), res.columnNamesInOut);
		// パーツI/O区分の選択肢
		NCI.createOptionTags($('select[data-field=partsIoType]'), res.partsSearchTypes);

		// データを画面へ反映
		NCI.toElementsFromObj(design, $root);
		// 検索条件をテーブルへ反映
		responsiveTable.fillTable(design.relations);

		enableButtons();

		// ソート機能を付与
		$root.find('table.responsive>tbody').sortable({
			handle : 'i.glyphicon.glyphicon-align-justify',
			update : function(ev, ui) {
				//並び順をリセット
				resetSortOrder();
			}
		});

		// テーブル名の変更
		$('#tableId').change(function() {
			// テーブル名に連動する選択条件の選択肢を生成
			const params = { "tableId" : $('#tableId').val() };
			NCI.post('/vd0114/findTableSearch', params).done(function(tableSearchIds) {
				if (tableSearchIds) {
					const $tableSearchId = $('#tableSearchId');
					const old = $tableSearchId.val();
					NCI.createOptionTags($tableSearchId, tableSearchIds);
					if ($tableSearchId.val() != old)
						$tableSearchId.change();
				}
			})
		});
		// 選択条件の変更
		$('#tableSearchId').change(function() {
			const params = { "tableSearchId" : this.value };
			// 値のカラム名の選択肢
			NCI.post('/vd0114/getTableSearchColumnsInAndOut', params).done(function(columns) {
				if (columns) {
					NCI.createOptionTags($('#columnNameValue'), columns);
					enableButtons();
				}
			});
			// ラベルのカラム名の選択肢
			NCI.post('/vd0114/getTableSearchColumnsOut', params).done(function(columns) {
				if (columns) {
					NCI.createOptionTags($('#columnNameLabel'), columns);
					enableButtons();
				}
			});
			// 絞込条件と検索条件のパーツの選択肢
			NCI.post('/vd0114/getTableSearchColumnsInOrOut', params).done(function(columns) {
				if (columns) {
					NCI.createOptionTags($('select.columnNamesInOrOut'), columns);
					enableButtons();
				}
			})
		});
		$('#btnAdd').click(function() {
			let entity = {
				"columnName" : null,
				"targetPartsId" : null,
				"partsIoType" : OUTPUT,
				"sortOrder" : -1
			};
			responsiveTable.addRowResult(entity);
			resetSortOrder();
		});
		$('#btnDelete').click(function() {
			$('input.selectable:checked').each(function(i, elem) {
				$(elem).parent().parent().remove();
				enableButtons();
			});
		})

		$root.on('click', 'input.selectable', function() {
			enableButtons();
		});
	});

	function enableButtons() {
		$('#btnAdd').prop('disabled', ($('#tableSearchId').prop('selectedIndex') < 0));
		$('#btnDelete').prop('disabled', ($('input.selectable:checked').length === 0));
	}

	function resetSortOrder() {
		$('#vd0114_50').find('table.responsive>tbody').find('span[data-field=sortOrder]').each(function(i, elem) {
			$(elem).text(i + 1);
		});
	}
}

// パーツ固有のバリデーション処理
function validateSpecific(ctx, design) {
	const uniques = {}, IN = '1', OUT = '2', BOTH = '3';
	let result = true;
	$('#tblRelation>tbody>tr>td>select[data-field=targetPartsId]').each(function(i, ddl) {
		if (ddl.value !== '') {
			const $targetParts = $(ddl), $tr = $targetParts.closest('tr');
			const targetPartsId = $targetParts.val();
			const columnName = $tr.find('[data-field=columnName]').val();
			const partsIoType = $tr.find('[data-field=partsIoType]').val();

			// 絞込条件ならカラム名でユニーク、検索結果ならパーツでユニーク、両方ならカラム名とパーツのいずれかで一致すれば重複とみなす
			if ((partsIoType === IN && uniques[columnName])
					|| (partsIoType === OUT && uniques[targetPartsId])
					|| (partsIoType === BOTH && (uniques[columnName] || uniques[targetPartsId]))
			) {
				//MSG0121": "{0}の値「{1}」が重複しています。"
				const msg = NCI.getMessage('MSG0121', [NCI.getMessage('searchConditionAndResult'), NCI.getMessage('parts') + " + " + NCI.getMessage('dbColumnName')]);
				Validator.showBalloon($targetParts, msg);
				result &= false;
				return false;
			}

			if (partsIoType === IN)
				uniques[columnName] = true;
			else if (partsIoType === OUT)
				uniques[targetPartsId] = true;
			else if (partsIoType === BOTH) {
				uniques[columnName] = true;
				uniques[targetPartsId] = true;
			}
		}
	});
	return result;
}

//パーツの共通「入力内容の吸い上げ処理」を上書き
function toObjOverride(ctx, design) {
	let $root = $('#commonItem, #divExtProperties');
	let data = NCI.toObjFromElements($root, ['columnName']);
	data.relations = NCI.toArrayFromTable($('#fsRelation'));
	return data;
}
