// Grid：vd0114.jsから呼び出される初期化ロジック
function initSpecific(ctx, design) {
	let $root = $('#vd0114_15');
	let responsiveTable = new ResponsiveTable($('#fsRelation'));

	NCI.post('/vd0114/initGrid', { 'design' : design }).done(function(res) {
		// ページ制御(ページサイズ)の選択肢
		NCI.createOptionTags($root.find("select.pageSize"), res.pageSizes).val(design.pageSize);
		// 子コンテナの選択肢
		NCI.createOptionTags($('#childContainerId'), res.childContainers);
		// 子コンテナ配下でグリッドのセルに指定可能なパーツの選択肢
		NCI.createOptionTags($('select[data-field=targetPartsId]'), res.targetParts);
		// セルの幅の選択肢
		NCI.createOptionTags($('select[data-field=width]'), res.columnSizes);

		// データを画面へ反映
		NCI.toElementsFromObj(design, $root);
		// パーツ関連情報を列定義としてをテーブルへ反映
		responsiveTable.fillTable(design.relations);
		// 最小入力パーツIDリストの反映
		fillInputedJudgePartsIds(design);
		resetLineStyle();

		// ソート機能を付与
		$root.find('table.responsive>tbody').sortable({
			handle : 'i.glyphicon.glyphicon-align-justify',
			start: function() {
				$root.find('table.responsive>tbody>tr').removeClass('vd0114_15_bottom_line');
			},
			update : function(ev, ui) {
				//並び順をリセット
				resetSortOrder();
				resetLineStyle();
			}
		});

		enableButtons();

		// 子コンテナを変更
		$('#childContainerId').change(function() {
			// 新たに選択された子コンテナはまだ読み込まれていないため、ctx.designMapにはパーツが存在しない。
			// サーバから対象コンテナのパーツを取得する
			let params = {
					"childContainerId" : this.value
			}
			NCI.post('/vd0114/findGridChildParts', params).done(function(targetParts) {
				if (targetParts) {
					NCI.createOptionTags($('select[data-field=targetPartsId]'), targetParts);
				}
				enableButtons();
			});
		});
		// 追加ボタン押下
		$('#btnAdd').click(function() {
			responsiveTable.addRowResult({ "width" : 2 });
			resetSortOrder();
			resetLineStyle();
		});
		// 削除ボタン押下
		$('#btnDelete').click(function() {
			$('input.selectable:checked').each(function(i, elem) {
				$(elem).closest('tr').remove();
			});
			enableButtons();
		});
		// 行選択チェックボックスON/OFF時
		$root.on('click', 'input.selectable', function() {
			enableButtons();
		})
		// 幅変更時
		.on('change', 'select[data-field=width]', function() {
			resetLineStyle();
		})
		// パーツ変更時
		.on('change', 'select[data-field=targetPartsId]', function() {
			let $select = $(this);
			let $text = $select.closest('tr').find('input[data-field=columnName]');
			if (this.value != '' && $text.val() == '') {
				let label = this.options[this.selectedIndex].label;
				label = label.substring(label.indexOf(' ') + 1);
				$text.val(label);
			}
		});

	});

	/** 追加／削除ボタン有効判定 */
	function enableButtons() {
		$('#btnAdd').prop('disabled', ($('#childContainerId').prop('selectedIndex') < 0));
		$('#btnDelete').prop('disabled', ($('input.selectable:checked').length === 0));
	}

	/** 並び順を現在の表示内容順にリセット */
	function resetSortOrder() {
		$root.find('table.responsive>tbody').find('span[data-field=sortOrder]').each(function(i, elem) {
			$(elem).text(i + 1);
		});
	}

	/** 12行ごとの境界線を現在の表示内容に合わせてリセット */
	function resetLineStyle() {
		$root.find('table.responsive>tbody>tr').removeClass('vd0114_15_bottom_line');
		let col = 0;
		$root.find('table.responsive>tbody>tr').each(function(i, elem) {
			let ddl = $('td>select[data-field=width]', elem)[0];
			col += +ddl.value;
			if (col >= 12) {
				$(elem).addClass('vd0114_15_bottom_line');
				col = 0;
			}
		});
		$root.find('table.responsive>tbody>tr:last').addClass('vd0114_15_bottom_line');
	}
}

//パーツ固有のバリデーション処理
function validateSpecific(ctx, design) {
	let uniques = {};
	let result = true;
	$('#tblRelation>tbody>tr>td>select[data-field=targetPartsId]').each(function(i, ddl) {
		if (ddl.value != '') {
			if (uniques[ddl.value] != null) {
				//MSG0121": "{0}の値「{1}」が重複しています。"
				let msg = NCI.getMessage('MSG0121', [NCI.getMessage('parts'), ddl.options[ddl.selectedIndex].label]);
				Validator.showBalloon($(ddl), msg);
				result &= false;
				return false;
			}
			uniques[ddl.value] = ddl.value;
		}
	});
	let total = 0;
	let $width = $('#tblRelation>tbody>tr>td>select[data-field=width]');

	let rows = [];
	let row = 0;
	let col = 0;
	$width.each(function(i, ddl) {
		total += +ddl.value;
		if (rows.length - 1 < row) {
			rows.push([]);
		}
		rows[row].push(+ddl.value);
		col += +ddl.value;
		if (col >= 12) {
			row++;
			col = 0;
		}
	});

	// 一行の列チェック
	let index = 0;
	for (var i in rows) {
		let colLength = rows[i].filter(function (c) {return c > 0}).length;
		if (colLength > 12) {
			let msg = NCI.getMessage('MSG0241', [+i + 1]);
			Validator.showBalloon($('#tblRelation>tbody>tr').eq(index).find('select[data-field=width]'), msg);
			result &= false;
		}
		let colTotal = rows[i].reduce(function (p, c) { return p + c; });
		if (colTotal > 0 && colTotal != 12) {
			let msg = NCI.getMessage('MSG0242', [+i + 1, colTotal]);
			Validator.showBalloon($('#tblRelation>tbody>tr').eq(index).find('select[data-field=width]'), msg);
			result &= false;
		}
		index += rows[i].length;
	}
	if (!result) {
		return result;
	}

	// 総列数チェック
	if (total % 12 > 0) {
		// "MSG0140": "{0}の合計は{1}にしてください。"
		let msg = NCI.getMessage('MSG0243', [total]);
		Validator.showBalloon($width.first(), msg);
		result &= false;
		return false;
	}
	return result;
}

//パーツの共通「入力内容の吸い上げ処理」を上書き
function toObjOverride(ctx, design) {
	let $root = $('#commonItem, #divExtProperties');
	let data = NCI.toObjFromElements($root, ['columnName']);
	data.relations = NCI.toArrayFromTable($('#fsRelation'));

	// 選択されている入力済み判定パーツIDを吸上げ
	data.inputedJudgePartsIds = [];
	$('input.inputedJudgePartsIds:checked').each(function(i, checkbox) {
		const $tr = $(checkbox).closest('tr');
		const targetPartsId = $tr.find('select[data-field=targetPartsId]').val();
		if (targetPartsId) {
			data.inputedJudgePartsIds.push(+targetPartsId);
		}
	});
	return data;
}

// 入力済み判定パーツIDリストを画面へ反映
function fillInputedJudgePartsIds(design) {
	$('select[data-field=targetPartsId]').each(function(i, select) {
		if (select.value) {
			const targetPartsId = +select.value;
			const checked = design.inputedJudgePartsIds.indexOf(targetPartsId) > -1;
			$(select).closest('tr').find('input.inputedJudgePartsIds').prop('checked', checked);
		}
	});
}
