$(function() {
	var $treeScreenProcessLevel = $('#treeScreenProcessLevel');

	NCI.init("/na0010/init").done(function(res) {
		if (res && res.success) {
			$treeScreenProcessLevel.jstree({
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
				'sort': function(f, s) {
					var $f = $treeScreenProcessLevel.jstree().get_node(f);
					var $s = $treeScreenProcessLevel.jstree().get_node(s);
					if ($f.type === $s.type) {
						// ソート処理は並び順の昇順＞IDの昇順で行う
						if (parseInt($f.data.sortOrder) == parseInt($s.data.sortOrder)) {
							return parseInt($f.id) > parseInt($s.id) ? 1 : -1;
						} else {
							return parseInt($f.data.sortOrder) > parseInt($s.data.sortOrder) ? 1 : -1;
						}
					} else if ($f.type !== $s.type) {
						// fileを前、bookを後
						if ($f.type === 'file' && $s.type === 'book') {
							return -1;
						} else if ($f.type === 'book' && $s.type === 'file') {
							return 1;
						}
					}
					return -1;
				},
				'plugins' : [
					'wholerow', 'types', 'sort'	/* 行全体で選択できるようにする */
				]
			})
			.on('ready.jstree', function() {
				var all = $treeScreenProcessLevel.jstree().get_json(null, {flat: true});
				for (var i = 0; i < all.length; i++) {
					var node = all[i];
					if (node.type !== 'file') {
						continue;
					}
					if (node.a_attr.description) {
						var el = $treeScreenProcessLevel.jstree().get_node(node.id, true);
						var label = '&nbsp;&nbsp;<span class="label label-default hidden-xs" style="font-size: 85%;">' + node.a_attr.description + '</span>';
						$('a', el).append(label);
					}
				}
			})
			.on('before_open.jstree', function(ev, node) {
				var o = node.node;
				var all = $treeScreenProcessLevel.jstree().get_json(o.id, {flat: true});
				for (var i = 0; i < all.length; i++) {
					var node = all[i];
					if (node.type !== 'file') {
						continue;
					}
					if (node.a_attr.description) {
						var el = $treeScreenProcessLevel.jstree().get_node(node.id, true);
						var label = '&nbsp;&nbsp;<span class="label label-default" style="font-size: 85%;">' + node.a_attr.description + '</span>';
						$('a', el).append(label);
					}
				}
			})
			.on('changed.jstree', function(e, data) {
				if (data.selected.length) {
					if (data.node.type === 'file') {
						newApplication(data.selected);
					}
				}
			});
		}
	});

	function newApplication(selects) {
		if (selects && selects.length) {
			const node = $treeScreenProcessLevel.jstree().get_node(selects[0]);
			const screenProcessId = node.data.screenProcessId;
			const uri = "../vd/vd0310.html?screenProcessId=" + screenProcessId + "&trayType=NEW";
			NCI.redirect(uri);
		}
		return false;
	}
});
