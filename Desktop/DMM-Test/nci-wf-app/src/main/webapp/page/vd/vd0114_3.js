// Checkbox：vd0114.jsから呼び出される初期化ロジック
function initSpecific(ctx, design) {
	var $root = $('#vd0114_3');
	var params = { 'design' : design };
	NCI.post('/vd0114/initCheckbox', params).done(function(res) {
		if (res && res.success) {
			// デフォルト値の選択肢
			NCI.createOptionTags($('#defaultValue'), res.defaultValueList);

			// データを画面へ反映
			NCI.toElementsFromObj(design, $root);
		}
	});
}
