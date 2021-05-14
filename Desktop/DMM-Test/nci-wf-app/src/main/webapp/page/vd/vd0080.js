var rt;
$(function() {
	const p = NCI.flushScope(NCI.getQueryString(FLUSH_SCOPE_KEY));
	const runtimeMap = p.runtimeMap, htmlId = p.htmlId;
	rt = runtimeMap[htmlId];
	const params = { "messageCds" : ['MSG0208', 'picture'] };
	NCI.init('/vd0080/init', params).done(function(res) {
		if (rt) {
			// ドラッグ＆ドロップによるファイルアップロード
			FileUploader.setup("div.dragZone", "/vd0310/addPartsAttachFileWf", false, showRowResult, prepareCallback);
			// ファイルコントロールによるファイルアップロード
			FileUploader.setup('input[type=file]', "/vd0310/addPartsAttachFileWf", false, showRowResult, prepareCallback);

			$('#btnOK').click(function() {
				// 画面内容を吸い上げて、コールバック関数を呼び出し
				const params = {
						"rows" : [
							{
								"fileName" : $('#fileName').val(),
								"partsAttachFileWfId" : $('#partsAttachFileWfId').val()
							}
						],
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
	return {
		"partsId" : rt.partsId,
		"multiple" : false,
		"maxFileCount" : 1,
		"fileRegExp" : rt.fileRegExp	// ファイル名の制限用の正規表現
	};
}

function showRowResult(rows) {
	if (rows && rows.length) {
		// 拡張子が画像か？
		const fileName = rows[0].fileName, pos = fileName.indexOf('.');
		const ext = (pos < 0 ? '' : fileName.substring(pos + 1));
		if (!/PNG|GIF|JPG|JPEG/i.test(ext)) {
			NCI.alert(NCI.getMessage('MSG0208', NCI.getMessage('picture')));
			return false;
		}
		// 正しい画像っぽいので、イメージを表示してみる
		const partsAttachFileWfId = rows[0].partsAttachFileWfId;
		$('#imageArea, #btnOK').removeClass('hide');
		$('#sampleImage').attr('src', '../../endpoint/vd0310/download/partsAttachFileWf' +
				'?partsAttachFileWfId=' + partsAttachFileWfId);

		// コールバック用にアップロード内容を退避
		$('#partsAttachFileWfId').val(partsAttachFileWfId);
		$('#fileName').val(fileName);
	} else {
		// 既存をクリア
		$('#partsAttachFileWfId, #fileName').val('');
		$('#sampleImage').removeAttr('src');
	}
}

