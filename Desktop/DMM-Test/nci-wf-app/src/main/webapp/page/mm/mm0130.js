$(function() {
	// 更新後のノード内容書き換え用変数
	let node;

	var $menuLevel = $('#menuLevel');
	NCI.init('/mm0130/init').done(function(res) {
		if (res && res.success) {
			$menuLevel.jstree({
				'core': {
					'data' : res.treeItems
				},
				'types': {
					'#': {
						'max_children': 1,
						'max_depth': -1,
						'valid_children': []
					},
					'home': {
						'valid_children': []
					},
					'book': {
						'valid_children': []
					},
					'file': {
						'valid_children': []
					}
				},
				'plugins' : [
					'wholerow', 'types'	/* 行全体で選択できるようにする */
				]
			})
			.on('changed.jstree', function(e, data) {
				if (data.selected.length) {
					if (data.node.type != 'home') {
						popup(data.selected);
					}
				}
			});

			$('#btnUpdate').click(update);
		}
	});
	function popup(selects) {

		Validator.hideBalloon();

		if (selects && selects.length) {
			node = $menuLevel.jstree().get_node(selects[0]);
			$('#popMenuId').val(node.data.menuId);
			$('#popMenuName').val(node.data.menuName);
			$('#btnModal').click();
		}
		return false;
	}

	/** 登録 */
	function update(ev) {
		NCI.clearMessage();
		// バリデーション
		let $inputs = $('input, select, textarea');
		if (!Validator.validate($inputs, true)) {
			return false;
		}
		let msg = NCI.getMessage('MSG0071', NCI.getMessage('menuName'));
		// 更新処理
		let params = {
				"menuId" : $('#popMenuId').val(),
				"menuName" : $('#popMenuName').val(),
		};

		NCI.confirm(msg, function() {
			NCI.post('/mm0130/update', params).done(function(res) {

				node.data.menuName = res.menuName;
				$menuLevel.jstree().set_text(node, res.menuName)

				// ポップアップを閉じる
				$('#btnModal').click();
			});
		});
	}
});
