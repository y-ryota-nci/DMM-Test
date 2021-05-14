$(function() {
	let params = {
		"corporationCode" : NCI.getQueryString("corporationCode"),
		"menuRoleCode" : NCI.getQueryString("menuRoleCode"),
		"messageCds" : ['masterAuthority']
	};
	NCI.init('/ti0021/init', params).done(function(res) {
		if (res && res.success) {
			$('button.btnUpdate').click(execUpdate);
			$(document).on('click', 'input[type=checkbox].categories', function(ev) {
				categoryCheckbox_click(ev.currentTarget);
			});

			refresh(res);
		}
		$('#btnBack, #btnBack2').click(function(ev) {
			NCI.redirect('./ti0020.html');
		});
	});
});

/** 更新処理 */
function execUpdate() {
	let msg = NCI.getMessage('MSG0071', NCI.getMessage('masterAuthority'));
	if (NCI.confirm(msg, function() {
		// カテゴリ権限
		let categories = [];
		$('#tbodyCategory>tr>td>input.categories:checked').each(function(i, checkbox) {
			let $tr = $(checkbox).parent().parent();
			let row = NCI.toObjFromElements($tr);
			categories.push(row);
		});
		// テーブル権限
		let tables = [];
		$('tbody>tr>td>input.tables:checked').each(function(i, checkbox) {
			let $tr = $(checkbox).parent().parent();
			let row = NCI.toObjFromElements($tr);
			tables.push(row);
		});
		let params = {
			"corporationCode" : $('#corporationCode').val(),
			"menuRoleCode" : $('#menuRoleCode').val(),
			"categories" : categories,
			"tables" : tables
		};
		// 更新処理
		NCI.post('/ti0021/save', params).done(function(res) {
			if (res && res.success) {
				refresh(res);
			}
		});
	}));
}

/** カテゴリ・チェックボックスのクリック */
function categoryCheckbox_click(checkbox) {
	if (checkbox) {
		let $tr = $(checkbox).parent().parent().next();
		let $tableCheckbox = $tr.find('tbody input[type=checkbox].tables').prop('disabled', checkbox.checked);
		if (checkbox.checked) {
			$tableCheckbox.prop("checked", false);
		}
	}
}

/** 再描画 */
function refresh(res) {
	// メニューロールのレンダリング
	let ignores = ['tableId'];	// ネストしている子テーブルの属性に影響しないように、無視リストへ登録
	NCI.toElementsFromObj(res.menuRole, $('#menuRole'));

	// カテゴリのレンダリング
	let $root = $('#divCategories');
	new ResponsiveTable($root).fillTable(res.categories, ignores);

	// カテゴリ内のテーブルをレンダリング
	$root.find('table.categories>tbody>tr>td.category').each(function(i, td) {
		let $td = $(td);
		let categoryId = $td.find('span[data-field=categoryId]:first').text();
		let rows = res.tables[categoryId];
		new ResponsiveTable($td).fillTable(rows);
	});

	// カテゴリのCheckbox選択内容を反映
	$.each(res.categories, function(i, row) {
		if (row.selected) {
			let $checkbox = $('input[type=checkbox][value=' + row.categoryId + ']');
			$checkbox.prop('checked', true);
			categoryCheckbox_click($checkbox[0]);
		}
	});

	// テーブルのCheckbox選択内容を反映
	$.each(res.tables, function(propName, value) {
		let rows = res.tables[propName];
		$.each(rows, function(i, row) {
			if (row.selected) {
				$('input[type=checkbox][value=' + row.tableId + ']').prop('checked', true);
			}
		})
	});

	$('button.btnUpdate').prop('disabled', false);
}
