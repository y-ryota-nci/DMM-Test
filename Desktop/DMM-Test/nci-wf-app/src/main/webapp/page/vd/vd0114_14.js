// 画像パーツ ： vd0114.jsから呼び出される初期化ロジック
function initSpecific(ctx, design) {
	// データを画面へ反映
	let $root = $('#vd0114_14');
	NCI.toElementsFromObj(design, $root);

	createImage();

	$(document).on('click', '#btnClearImage', function() {
		$('#sampleImage').removeAttr('src');
		design.partsAttachFileId = null;
		enableControls();
	}).on('click', '#useBorderLine', function() {
		enableControls();
	});

	// ファイルコントロールによるファイルアップロード
	FileUploader.setup('#file', "/vd0114/uploadAttachFile", false, uploadCallback, prepareCallback);
}

/** アップロード用の追加パラメータを生成して返す */
function prepareCallback() {
	return { "partsId" : design.partsId, "fileRegExp": design.fileRegExp };
}

/** コントロールの有効／表示の制御 */
function enableControls() {
	Validator.hideBalloon();
	const useBorderLine = ($('#useBorderLine:checked').val() === 'true');
	const $targets = $('#borderLineWidth, #borderLineColor, #borderWidth, #borderHeight');
	if (useBorderLine) {
		$targets.prop('disabled', false);
	} else {
		$targets.prop('disabled', true).val('');
	}

	const hasImage = !!$('#sampleImage').attr('src');
	$('#btnClearImage').prop('disabled', !hasImage).toggleClass('hide', !hasImage);
	$('#file').prop('disabled', hasImage).toggleClass('hide', hasImage);
}

/** ファイルアップロード後のコールバック関数 */
function uploadCallback(res) {
	if (res.success && res.files && res.files.length) {
		// 画像ファイルか？　→拡張子で判定
		const file = res.files[0], fileName = file.fileName, pos = fileName.indexOf('.');
		const ext = (pos < 0 ? '' : fileName.substring(pos + 1));
		if (!/PNG|JPG|JPEG|GIF/i.test(ext)) {
			NCI.alert(NCI.getMessage('MSG0208', NCI.getMessage('picture')));
			return false;
		}
		design.partsAttachFileId = file.partsAttachFileId;
		createImage();
	}
}

/** アップロードされている画像をプレビュー表示 */
function createImage() {
	if (design.partsAttachFileId)
		$('#sampleImage').attr('src', '../../endpoint/vd0310/download/partsAttachFile' +
				'?partsAttachFileId=' + design.partsAttachFileId +
				'&t=' + new Date().getTime());
	else
		$('#sampleImage').removeAttr('src');

	enableControls();
}

//パーツの共通「入力内容の吸い上げ処理」を上書き
function toObjOverride(ctx, design) {
	const $root = $('#commonItem, #vd0114_14');
	const data = NCI.toObjFromElements($root, ['file', 'sampleImage']);
	return data;
}
