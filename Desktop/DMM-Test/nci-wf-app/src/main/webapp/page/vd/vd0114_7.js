// 採番：vd0114.jsから呼び出される初期化ロジック
function initSpecific(ctx, design) {
	var $root = $('#vd0114_7');
	var params = { 'design' : design };
	NCI.post('/vd0114/initNumbering', params).done(function(res) {
		// 採番形式
		NCI.createOptionTags($('#partsNumberingFormatId'), res.partsNumberingFormats);

		// データを画面へ反映
		NCI.toElementsFromObj(design, $root);
	});
}
