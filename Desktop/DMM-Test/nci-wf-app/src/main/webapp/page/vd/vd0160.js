$(function() {
	let ctx = NCI.flushScope(NCI.getQueryString(FLUSH_SCOPE_KEY));
	NCI.init('/vd0160/init').done(function(res) {
		let root = ctx.root, designMap = ctx.designMap;

		// ツリーHTML作成
		let $tree = $('#tree');
		let level = 1;
		createTreeHtml( $tree, root, designMap, level, res.businessInfoCodes);

		// ツリー化
		$tree.jstree().on('changed.jstree', function(e, data) {
			if(data.selected.length) {
				// コールバック関数の呼び出し
				let partsId = data.selected[0];
				Popup.close(partsId);
			}
			return true;
		});

		// ルートコンテナ
		let $span = $(document.createElement('span'))
			.text(root.containerCode + ' ' + root.containerName)
			.addClass('glyphicon glyphicon-home jstree-disabled');
		$tree.prepend($span);

		// 閉じるボタン押下
		$('#btnClose').on('click', function(ev) {
			Popup.close();
		});
	});

	// ツリーHTML作成
	function createTreeHtml($root, container, designMap, level, businessInfoCodes) {
		let $ul = $(document.createElement('ul')).appendTo($root);
		let len = container.childPartsIds.length;
		for (let i = 0; i < len; i++) {
			let partsId = container.childPartsIds[i];
			let design = designMap[partsId];
			let hasChild = design.childPartsIds && $.isArray(design.childPartsIds);
			let icon = PARTS.toIconClass(design.partsType);
			let label = design.designCode + ' : ' + design.labelText;
			if (design.businessInfoCode) {
				label += ' --> ' + design.businessInfoCode + ' ' + (businessInfoCodes[design.businessInfoCode] || '');
			}

			let $li = $(document.createElement('li'))
				.text(label)
				.addClass('jstree-open')
				.data('jstree', { "icon": icon, "disabled" : level > 1 })
				.attr('id', design.partsId)
				.appendTo($ul);

			// 子要素があれば再帰呼び出し
			if (hasChild) {
				createTreeHtml($li, design, designMap, level + 1, businessInfoCodes);
			}
		}
	}
});
