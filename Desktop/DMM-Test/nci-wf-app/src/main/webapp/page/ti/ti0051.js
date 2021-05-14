var tableId = NCI.getQueryString("tableId");
$(function() {
	let params = {
		"tableId" : tableId,
		"tableSearchId" : NCI.getQueryString("tableSearchId"),
		"version" : NCI.getQueryString("version"),
		"messageCds" : ['tableSearchCondition']
	};
	NCI.init('/ti0051/init', params).done(function(res) {
		if (res && res.success) {
			init(res);

			$('#divColumns').on('click', 'a', link_click);
			$('#btnUpdate').click(execUpdate).prop('disabled', false);
			$('#btnPreview').click(preview).prop('disabled', false);
			$(window).on('resize', adjustSize);

			$('#tblColumns>tbody').selectable({filter : 'tr, input', cancel : 'a' });
		}
	});
	$('#btnBack').click(function() {
		NCI.redirect('./ti0050.html?tableId=' + tableId);
	});
});

function init(res) {
	let $divTable = $('#divTable');
	NCI.toElementsFromObj(res.tableDef, $divTable);
	NCI.toElementsFromObj(res.table, $divTable);
	let $divColumns = $('#divColumns');
	new ResponsiveTable($divColumns).fillTable(res.columns);

	$('#tableSearchCode').prop('disabled', !!res.table.tableSearchId);

	// windowサイズ依存でテーブルの高さを調整
	adjustSize();
}

/** 更新処理 */
function execUpdate(ev) {
	let $targets = $('input, select');
	if (!Validator.validate($targets, true)) {
		return false;
	}

	let msg = NCI.getMessage('MSG0071', NCI.getMessage('tableSearchCondition'));
	NCI.confirm(msg, function() {
		let params = {
			"table" : NCI.toObjFromElements($('#divTable'), ['tableName','logicalTableName', 'deleteFlagName', 'entityTypeNameTable', 'entityTypeNameView']),
			"columns" : NCI.toArrayFromTable($('#divColumns'))
		};
		NCI.post('/ti0051/save', params).done(function(res) {
			if (res && res.success) {
				init(res);
			}
		});
	});
}

/** リンク押下 */
function link_click(ev) {
	NCI.clearMessage();

	// コールバック関数用に、クリック行のカラム名を属性として保存
	let $link = $(ev.currentTarget);
	let columnName = $link.text();
	$link.attr('data-column-name', columnName);

	let $tr = $link.parent().parent();
	let row = NCI.toObjFromElements($tr);
	Popup.open('./ti0052.html', fromTi0052, row, this);
}

/** TI0052からのコールバック関数 */
function fromTi0052(row, trigger) {
	if (row) {
		let $link = $('#divColumns')
				.find('a[data-column-name=' + row.columnName + ']');
		let $tr = $link.parent().parent();
		NCI.toElementsFromObj(row, $tr);
	}
}

/** 画面サイズに応じてカラム定義エリアのサイズを調整 */
function adjustSize() {
	let h = $(window).height() - 400;
	$('#divColumns').height(h - 15);
}

/** プレビュー */
function preview() {
	let params = {
			"tableSearchId" : $('#tableSearchId').text(),
			"initConditions" : {}
	};
	Popup.open('./ti0000.html', null, params);
}
