// 独立画面パーツ：vd0114.jsから呼び出される初期化ロジック
function initSpecific(ctx, design) {
	const params = { 'design' : design };
	NCI.post('/vd0114/initStandAloneScreen', params).done(function(res) {
		// 共通：必須は不要
		toggleDisplay($('#requiredFlag'), false, false);

		// 子コンテナの選択肢
		NCI.createOptionTags($('#childContainerId'), res.childContainers);
		// ボタンサイズの選択肢
		NCI.createOptionTags($("#buttonSize"), res.buttonSizes);

		// データを画面へ反映
		const $root = $('#vd0114_51');
		NCI.toElementsFromObj(design, $root);
	});
}
