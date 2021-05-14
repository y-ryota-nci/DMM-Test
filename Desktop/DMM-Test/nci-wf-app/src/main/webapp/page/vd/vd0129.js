$(function() {
	var ctx = NCI.flushScope(NCI.getQueryString(FLUSH_SCOPE_KEY));
	var params = { ctx : ctx, messageCds : ['MSG0121', 'MSG0273'] };
	NCI.init('/vd0129/init', params).done(function(res) {
		$('#tableName').text(ctx.root.tableName);

		// DB予約語
		vd0129.dbmsReservedColNames = res.dbmsReservedColNames;
		// 画面デザイナー予約語
		vd0129.designerReservedColNames = res.designerReservedColNames;

		// データを反映
		let tbl = new ResponsiveTable($('#inputed'));
		tbl.fillTable(res.columns);

		// テーブルがすでにあれば、並び順は変更できない
		if (res.existRecord) {
			$('#tableColNotSortable').removeClass('hide');

			$('#tblColumns tbody').find('i.glyphicon.glyphicon-align-justify').hide();
		}
		else {
			// ソート機能を付与
			$('#tblColumns tbody').sortable({
				handle : 'i.glyphicon.glyphicon-align-justify',
				update : function(ev, ui) {
					resetSortOrder();
				}
			});
		}

		$('#btnOK').prop('disabled', false);
	});

	$(document)
	// 更新ボタン押下
	.on('click', '#btnOK', function(ev) {
		var $root = $('#inputed');
		if (!Validator.validate($root.find("input, textarea, select", true))) {
			return false;
		}
		// 入力内容を吸い上げ
		var rows = NCI.toArrayFromTable($root, ['label']);

		NCI.clearMessage('danger');
		var uniques = {};
		for (let i = 0; i < rows.length; i++) {
			let columnName = rows[i].columnName;
			if (uniques[columnName] != null) {
				let msg = NCI.getMessage('MSG0121', [NCI.getMessage('dbColumnName'), columnName]);
				NCI.addMessage('danger', [msg]);
				return false;
			}
			// DB予約語／画面デザイナー予約語をカラム名に使用していないか
			if (vd0129.dbmsReservedColNames.indexOf(columnName) > -1
					|| vd0129.designerReservedColNames.indexOf(columnName) > -1
			) {
				const msg = NCI.getMessage('MSG0273', [NCI.getMessage('dbColumnName'), columnName]);
				NCI.addMessage('danger', [msg]);
				return false;
			}

			uniques[columnName] = columnName;
		}

		// コールバック関数の呼び出し
		Popup.close(rows);
	})
	// 閉じるボタン押下
	.on('click', '#btnClose', function(ev) {
		Popup.close();
	});

});

// 並び順をリセット
function resetSortOrder() {
	var i = 0;
	$('#tblColumns tbody').find('input[data-field=sortOrder]').each(function(i, elem) {
		elem.value = ++i;
	});
}

const vd0129 = {
	/** DB予約語 */
	dbmsReservedColNames : [],

	/** 画面デザイナー予約語 */
	designerReservedColNames : []
};
