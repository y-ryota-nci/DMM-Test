// Repeater：vd0114.jsから呼び出される初期化ロジック
function initSpecific(ctx, design) {
	const $root = $('#vd0114_52');
	const params = { 'design' : design };
	const responsiveTable = new ResponsiveTable($('#fsRelation'));
	NCI.post('/vd0114/initRepeater', params).done(function(res) {
		// ページ制御(ページサイズ)の選択肢
		const $pageSize = $root.find("select.pageSize");
		NCI.createOptionTags($root.find("select.pageSize"), res.pageSizes);
		$pageSize.val(design.pageSize);

		// 子コンテナの選択肢
		NCI.createOptionTags($('#childContainerId'), res.childContainers);
		// 子コンテナ配下でグリッドのセルに指定可能なパーツの選択肢
		NCI.createOptionTags($('select[data-field=targetPartsId]'), res.targetParts);

		// データを画面へ反映
		NCI.toElementsFromObj(design, $root);
		// 最小入力パーツIDリストの反映
		fillInputedJudgePartsIds(design, responsiveTable);

		enableButtons();

		// 子コンテナを変更
		$('#childContainerId').change(function() {
			// 新たに選択された子コンテナはまだ読み込まれていないため、ctx.designMapにはパーツが存在しない。
			// サーバから対象コンテナのパーツを取得する
			const params = { "childContainerId" : this.value };
			NCI.post('/vd0114/findInputedJudgeParts', params).done(function(targetParts) {
				if (targetParts) {
					NCI.createOptionTags($('select[data-field=targetPartsId]'), targetParts);
				}
				enableButtons();
			});
		});
		// 追加ボタン押下
		$('#btnAdd').click(function() {
			responsiveTable.addRowResult({});
		});
		// 削除ボタン押下
		$('#btnDelete').click(function() {
			$('input.selectable:checked').each(function(i, elem) {
				$(elem).closest('tr').remove();
			});
			enableButtons();
		});
		// 行選択チェックボックスON/OFF時
		$root.on('click', 'input.selectable', function() {
			enableButtons();
		})
	});
}

/** 追加／削除ボタン有効判定 */
function enableButtons() {
	$('#btnAdd').prop('disabled', ($('#childContainerId').prop('selectedIndex') < 0));
	$('#btnDelete').prop('disabled', ($('input.selectable:checked').length === 0));
}

//パーツの共通「入力内容の吸い上げ処理」を上書き
function toObjOverride(ctx, design) {
	let $root = $('#commonItem, #vd0114_52');
	let data = NCI.toObjFromElements($root, ['targetPartsId']);

	// 選択されている入力済み判定パーツIDを吸上げ
	data.inputedJudgePartsIds = [];
	$('tbody>tr>*>select[data-field=targetPartsId]').each(function(i, select) {
		if (select.value) {
			data.inputedJudgePartsIds.push(+select.value);
		}
	});
	return data;
}

//入力済み判定パーツIDリストを画面へ反映
function fillInputedJudgePartsIds(design, responsiveTable) {
	const entities = [];
	for (let i = 0; i < design.inputedJudgePartsIds.length; i++) {
		const partsId = design.inputedJudgePartsIds[i];
		entities.push({ "targetPartsId" : partsId})
	}
	responsiveTable.fillTable(entities);
}
