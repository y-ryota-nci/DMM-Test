// Hyperlink ： vd0114.jsから呼び出される初期化ロジック
function initSpecific(ctx, design) {
	// データを画面へ反映
	let $root = $('#vd0114_17');
	NCI.toElementsFromObj(design, $root);

	createLink();

	$(document).on('click', 'input[name=requiredUpload]', function() {
		enableControls();
	}).on('click', '#btnClearFile', function() {
		$('#link').text('');
		enableControls();
	});

	// ファイルコントロールによるファイルアップロード
	FileUploader.setup('#file', "/vd0114/uploadAttachFile", false, uploadCallback, prepareCallback);
}

/** アップロード用の追加パラメータを生成して返す */
function prepareCallback() {
	return { "partsId" : design.partsId };
}

/** コントロールの有効／表示の制御 */
function enableControls() {
	Validator.hideBalloon();
	let requiredUpload = ($('input[name=requiredUpload]:checked').val() == 'true');
	if (requiredUpload) {
		$('#url').prop('disabled', true).addClass('hide').val('');

		if ($('#link').text() == '') {
			$('#link').addClass('hide');
			$('#btnClearFile').prop('disabled', true).addClass('hide');
			$('#file').prop('disabled', false).removeClass('hide');
		} else {
			$('#link').removeClass('hide');
			$('#btnClearFile').prop('disabled', false).removeClass('hide');
			$('#file').prop('disabled', true).addClass('hide');
		}
	} else {
		$('#url').prop('disabled', false).removeClass('hide');
		$('#file').prop('disabled', true).addClass('hide').val('');
		$('#btnClearFile').prop('disabled', true).addClass('hide');
		$('#link').addClass('hide').text('').attr('href', '#');
	}
}

/** リンクを再生成 */
function createLink() {
	let fileName = '', partsAttachFileId = 0;
	if (design.attachFiles && design.attachFiles.length) {
		fileName = design.attachFiles[0].fileName;
		partsAttachFileId = design.attachFiles[0].partsAttachFileId;
	}
	$('#link')
		.text(fileName)
		.attr('href', '../../partsAttachFile?partsAttachFileId=' + partsAttachFileId);

	enableControls();
}

/** ファイルアップロード後のコールバック関数 */
function uploadCallback(res) {
	if (res.success) {
		design.attachFiles = res.files;
		createLink();
	}
}

//パーツの共通「入力内容の吸い上げ処理」を上書き
function toObjOverride(ctx, design) {
	let $root = $('#commonItem, #vd0114_17');
	let data = NCI.toObjFromElements($root, ['file', 'link']);
	return data;
}
