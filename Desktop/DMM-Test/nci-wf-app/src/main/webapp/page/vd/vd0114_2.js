// Label：vd0114.jsから呼び出される初期化ロジック
function initSpecific(ctx, design) {
	// 関連付けするパーツの選択肢
	let options = [ { 'label' : '--', 'value' : ''} ];
	ctx.root.childPartsIds.forEach(function(partsId, i, array) {
		let d = ctx.designMap[partsId];
		if (d && design.partsId != partsId) {
			options.push({
				'label' : d.partsCode + ' ' + (d.labelText + ''),
				'value' : partsId
			});
		}
	});
	NCI.createOptionTags($("#partsIdFor"), options);

	// データを画面へ反映
	let $root = $('#vd0114_2');
	NCI.toElementsFromObj(design, $root);
}
