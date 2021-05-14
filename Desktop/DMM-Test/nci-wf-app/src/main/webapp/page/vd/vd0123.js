$(function() {
	var ctx = NCI.flushScope(NCI.getQueryString(FLUSH_SCOPE_KEY));
	var params = { ctx : ctx };
	NCI.init('/vd0123/init', params).done(function(res) {
		if (res && res.success) {
			// データを反映
			let tbl = new ResponsiveTable($('#inputed'));
			tbl.fillTable(res.scripts);

			// ソート機能を付与
			$('#tbl tbody').sortable({
				handle : 'i.glyphicon.glyphicon-align-justify',
				update : function(ev, ui) {
					resetSortOrder();
				}
			});

			// 選択行の変更
			$('input[data-field=selected]')
				.on('change', resetSortOrder);

			// 更新ボタン押下
			$('#btnOK')
				.prop('disabled', false)
				.click(update);
		}

		$('#btnClose')
			.click(function() { Popup.close(); });
	});
});

// テーブル内の位置による並び順付与
function resetSortOrder() {
	let sortOrder = 0;
	$('#tbl tbody tr').each(function(i, tr) {
		let $tr = $(tr);
		let selected = $tr.find('input[data-field=selected]').prop('checked');
		$tr.find('input[data-field=sortOrder]').val(selected ? ++sortOrder : '');
		$tr.find('glyphicon glyphicon-align-justify clickable').toggleClass('hide', !selected);
	});
}

function update() {
	var $root = $('#inputed');
	if (!Validator.validate($root.find("input, textarea, select", true))) {
		return false;
	}
	// 入力内容を吸い上げ
	let javascripts = [];
	$('#tbl tbody tr').each(function(i, tr) {
		let $tr = $(tr);
		if ($tr.find('input[data-field=selected]').prop('checked')) {
			javascripts.push({
				"javascriptId" : + $tr.find('span[data-field=javascriptId]').text(),
				"sortOrder" : + $tr.find('input[data-field=sortOrder]').val(),
			});
		}
	});


	// コールバック関数の呼び出し
	Popup.close(javascripts);
}