// 添付ファイルパーツ ： vd0114.jsから呼び出される初期化ロジック
function initSpecific(ctx, design) {
	// ページあたりの行数を生成
	let pageSizes = [];
	for (let i = 0; i < PAGE_SIZE_ARRAY.length; i++) {
		if (i === 0) {	// 「ページ制御なし」を追加
			pageSizes.push({ "value" : 0, "label" : NCI.getMessage('noPaging') });
		}
		let pageSize = PAGE_SIZE_ARRAY[i];
		pageSizes.push({ "value" : pageSize, "label" : pageSize });
	}
	NCI.createOptionTags($("#pageSize"), pageSizes);

	// データを画面へ反映
	let $root = $('#vd0114_20');
	NCI.toElementsFromObj(design, $root);
	// 「拡張子の制限」データを反映
	click_fileExtLimitation();

	change_multiple();

	$('#multiple').click(change_multiple);
	$('#requiredFileCount').change(change_requiredFileCount);
	$('#fileExtLimitation').on('click', 'input[type=checkbox]', click_fileExtLimitation);
}

/** 複数添付ファイルを使うか否かで表示内容を変更 */
function change_multiple() {
	let multiple = $('#multiple').prop('checked');
	toggleDisplay($('#requiredFileCount'), multiple, multiple);
	toggleDisplay($('#maxFileCount'), multiple, multiple);
	toggleDisplay($('#pageSize'), multiple, multiple);
	toggleDisplay($('#initSampleCount'), multiple, multiple);
	$('#notUseComment').parent().find('*').toggle(multiple);
	if (!multiple) {
		$('#notUseComment').prop('checked', false);
	}
}

/** 最小入力行数＞０なら必須チェックボックスをONにする */
function change_requiredFileCount(ev) {
	let requiredFileCount = +$(ev.target).val();
	$('#requiredFlag').prop('checked', requiredFileCount > 0);
}

/** ファイルの制限チェックボックスのクリック時 */
function click_fileExtLimitation() {
	const $targets = $('#fileExtLimitation').find('input[type=checkbox]')
	const $enableAny = $('#enableAny');
	const $regExpOther = $('#regExpOther');
	$targets.each(function(i, checkbox) {
		if (checkbox !== $enableAny[0]) {
			if ($enableAny[0].checked) {
				checkbox.checked = false;
				checkbox.disabled = true;
			} else {
				checkbox.disabled = false;
			}
		}
	});
	if ($('#enableOther')[0].checked)
		$regExpOther.prop('disabled', false);
	else
		$regExpOther.prop('disabled', true).val('');
}