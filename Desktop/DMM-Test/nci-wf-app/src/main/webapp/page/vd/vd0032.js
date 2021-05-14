var condMap;
var calcMap;
var type;
$(function() {
	let params = NCI.flushScope(NCI.getQueryString(FLUSH_SCOPE_KEY));
	condMap = params.conds;
	calcMap = params.calcs;
	type = params.type
	NCI.init('/vd0032/init', {'containerId' : params.containerId}).done(function(res) {
		let ctx = res.ctx;
		let root = ctx.root
		let designMap = ctx.designMap;

		// ツリーHTML作成
		let $tree = $('#tree');
		let level = 1;
		createTreeHtml( $tree, root, designMap, level, type);

		// ツリー化
		$tree.jstree().on('changed.jstree', function(e, data) {
			if(data.selected.length) {
				let partsId = data.selected[0];
				if (type == 1) {
					let conds = condMap[partsId];
					let params = { 'ctx':ctx, 'partsId' : partsId, 'conds':conds, 'type':'screen' }
					Popup.open("../vd/vd0111.html", fromVd0111, params);
				} else if (type == 2) {
					let calcs = calcMap[partsId];
					let params = { 'ctx':ctx, 'partsId' : partsId, 'calcs':calcs, 'type':'screen' }
					Popup.open("../vd/vd0143.html", fromVd0143, params);
				}
			}
			return true;
		});

		// ルートコンテナ
		let $span = $(document.createElement('span'))
			.text(root.containerCode + ' ' + root.containerName)
			.addClass('glyphicon glyphicon-home jstree-disabled');
		$tree.prepend($span);

		// 更新ボタン押下
		$('#btnUpdate').on('click', function(ev) {
			let result = {'conds' : condMap, 'calcs' : calcMap};
			// コールバック関数の呼び出し
			Popup.close(result);
		});

		// 閉じるボタン押下
		$('#btnClose').on('click', function(ev) {
			Popup.close();
		});
	});

	// ツリーHTML作成
	function createTreeHtml($root, container, designMap, level, type) {
		let $ul = $(document.createElement('ul')).appendTo($root);
		let len = container.childPartsIds.length;
		for (let i = 0; i < len; i++) {
			let partsId = container.childPartsIds[i];
			let design = designMap[partsId];
			let partsType = design.partsType;
			let hasChild = design.childPartsIds && $.isArray(design.childPartsIds);
			let icon = PARTS.toIconClass(design.partsType);
			let label = design.designCode + ' : ' + design.labelText;
			// 以下の条件に合致する場合は非活性
			// ・ラベルパーツ
			// ・引数のtypeが"1"で、既にパーツ有効条件が設定されてある場合
			// ・引数のtypeが"2"で、テキストパーツ以外か、テキストパーツで入力タイプが「数値」以外か、既にパーツ計算式が設定されてある場合
			let disabled = false;
			if (partsType == PartsType.LABEL) {
				disabled = true;
			} else if (type == 1 && (design.partsConds.length > 0)) {
				disabled = true;
			} else if (type == 2) {
				if (partsType != PartsType.TEXTBOX) {
					disabled = true;
				} else if (partsType == PartsType.TEXTBOX) {
					if (design.inputType != PartsInputType.NUMBER || design.partsCalcs.length > 0) {
						disabled = true;
					}
				}
			}

			let $li = $(document.createElement('li'))
				.text(label)
				.addClass('jstree-open')
				.data('jstree', { "icon": icon, "disabled" : disabled })
				.attr('id', design.partsId)
				.appendTo($ul);

			// 子要素があれば再帰呼び出し
			if (hasChild) {
				createTreeHtml($li, design, designMap, level + 1, type);
			}
		}
	}

});

function fromVd0111(result) {
	if (result) {
		condMap[result.partsId] = result.conds;
	}
}

function fromVd0143(result) {
	if (result) {
		calcMap[result.partsId] = result.calcs;
	}
}