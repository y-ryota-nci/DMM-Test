let multiple;
var rt;
$(function() {
	NCI.init('/vd0070/init').done(function(res) {
		let p = NCI.flushScope(NCI.getQueryString(FLUSH_SCOPE_KEY));
		let runtimeMap = p.runtimeMap, htmlId = p.htmlId;
		rt = runtimeMap[htmlId];
		multiple = rt.multiple;
		if (rt) {
			// ドラッグ＆ドロップによるファイルアップロード
			FileUploader.setup("div.dragZone", "/vd0310/addPartsAttachFileWf", false, showRowResult, prepareCallback);
			// ファイルコントロールによるファイルアップロード
			FileUploader.setup('input[type=file]', "/vd0310/addPartsAttachFileWf", false, showRowResult, prepareCallback);

			// コメント欄を使うか
			$('td.comments, th.comments').toggleClass('hide', rt.notUseComment || !rt.multiple)

			$('#btnOK').click(function() {
				// 画面内容を吸い上げて、コールバック関数を呼び出し
				let params = {
						"rows" : NCI.toArrayFromTable($('#attachFiles')),
						"htmlId" : htmlId
				}
				Popup.close(params);
			});
		}
	});

	$('#btnClose').click(function() {
		Popup.close();
	})
});

/** アップロード用の追加パラメータを生成して返す */
function prepareCallback() {
	return  {
		"partsId" : rt.partsId,
		"multiple" : multiple,
		"maxFileCount" : rt.maxFileCount,
		"fileRegExp" : rt.fileRegExp	// ファイル名の制限用の正規表現
	};
}

function showRowResult(rows) {
	$('#attachFiles, #btnOK').removeClass('hide');
	let tbl = new ResponsiveTable($('#attachFiles'));
	// 複数追加可能なときのみテーブルをクリアしない。
	if (!multiple) {
		tbl.empty();
	}
	if (rows) {
		$.each(rows, function(i, row) {
			let $tr = tbl.addRowResult(row);
			let url = '../../endpoint/vd0310/download/partsAttachFileWf?partsAttachFileWfId=' + row.partsAttachFileWfId;
			$tr.find('a[data-field=fileName]').attr('href', url);
		});
	}
}

