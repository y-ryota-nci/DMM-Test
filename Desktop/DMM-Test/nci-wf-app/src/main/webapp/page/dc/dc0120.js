$(function() {
	var $treeScreenDocLevel = $('#treeScreenDocLevel');
	var $confirmLevel = $('#confirmLevel');
	var moves = [];

	NCI.init("/dc0120/init", {messageCds: ['MSG0068', 'associateFolder', 'associateWithFolder', 'folder']}).done(function(res) {
		if (res && res.success) {
			$treeScreenDocLevel
			.on('move_node.jstree', function(ev, data) {
				// ソート処理が「move_node.jstree」イベントで行われている為、
				// jstree本体がイベントをバインドする前にバインドしてソート順を書き換えてソートに備える
				console.log('move_node.jstree');
				if (data.parent !== data.old_parent || data.position !== data.old_position) {
					var parentNode = $treeScreenDocLevel.jstree().get_node(data.parent);
					var count = 0;
					$.each(parentNode.children, function(i, d) {
						var childrenNode = $treeScreenDocLevel.jstree().get_node(d);
						if (data.node.type === childrenNode.type) {
							childrenNode.data.sortOrder = count;
							count++;
							moves.push(childrenNode);
						}
					});
				}
			})
			.jstree({
				core: {
					data: res.treeItems,
					check_callback: function(operation, node, node_parent, node_position, more) {
						return !more || !more.pos || more.pos === 'b' || more.pos === 'i' || more.pos === 'a';
					}
				},
				types: {
					'#': {
						'max_children': 1,
						'max_depth': -1,
						'valid_children': ['home']
					},
					'home': {
						'valid_children': ['book', 'file']
					},
					'book': {
						'valid_children': ['book', 'file']
					},
					'file': {
						'valid_children': []
					}
				},
				sort: function(f, s) {
					var $f = $treeScreenDocLevel.jstree().get_node(f);
					var $s = $treeScreenDocLevel.jstree().get_node(s);
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
				plugins: ['wholerow', 'dnd', 'types', 'sort']
			})
			.on('changed.jstree', function(e, data) {
				$('#btnAdd').prop('disabled', data.node.type === 'file' || data.selected.length > 1);
				$('#btnEdit').prop('disabled', data.node.type === 'home' || data.node.type === 'file' || data.selected.length !== 1);
				var removeButtonDisabled = data.node.type === 'home' || data.node.type === 'file' || data.selected.length !== 1;
				if (!removeButtonDisabled && data.selected.length === 1) {
					removeButtonDisabled = $treeScreenDocLevel.jstree().get_node(data.selected[0]).children.length > 0;
				}
				$('#btnRemove').prop('disabled', removeButtonDisabled);
			})
			;

			$(document)
			.on('click', '#btnAdd', function(e) {
				var selected = $treeScreenDocLevel.jstree().get_selected();
				var id = selected.length ? selected[0] : 'home';
				var text = '';
				var sortOrder = 0;
				if (id) {
					var node = $treeScreenDocLevel.jstree().get_node(id);
					$.each(node.children, function(i, d) {
						var childrenNode = $treeScreenDocLevel.jstree().get_node(d);
						if (childrenNode.type === 'book') {
							sortOrder++;
						}
					});

					id = node.type !== 'home' ? id : '';
					text = node.type !== 'home' ? node.text : '';
				}

				$('#parentLevelCode').text(id);
				$('#parentLevelName').val(text);
				$('#sortOrder').text(sortOrder);

				if (id) {
					$('#parentLevelCode').parent().removeClass('hide');
				} else {
					$('#parentLevelCode').parent().addClass('hide');
				}
				$confirmLevel.modal();
			})
			.on('click', '#btnEdit', function(e) {
				var id = $treeScreenDocLevel.jstree().get_selected()[0];
				var data = $treeScreenDocLevel.jstree().get_node(id);
				var parentData = $treeScreenDocLevel.jstree().get_node(data.parent);

				$('#screenDocLevelId').text(data.data.screenDocLevelId);
				$('#levelCode').text(id);
				$('#levelName').val(data.text);
				$('#sortOrder').text(data.data.sortOrder);
				$('#expansionFlag').prop('checked', data.data.expansionFlag === '1');

				if (parentData.type === 'home') {
					$('#parentLevelCode').text(id);
					$('#parentLevelName').val('');
					$('#parentLevelCode').parent().addClass('hide');
				} else {
					$('#parentLevelCode').text(parentData.id);
					$('#parentLevelName').val(parentData.text);
					$('#parentLevelCode').parent().removeClass('hide');
				}
				$confirmLevel.modal();
			})
			.on('click', '#btnLevelOk', function(e) {
				if (!Validator.validate($('#levelName'), true)) return false;
				var params = NCI.toObjFromElements($confirmLevel, ['parentLevelName']);
				params.expansionFlag = params.expansionFlag || '0';
				NCI.post('/dc0120/save', params).done(function(res) {
					if (res && res.success) {
						if (params.screenDocLevelId) {
							$treeScreenDocLevel.jstree().set_text(res.treeItem.id, res.treeItem.text);
						} else {
							$treeScreenDocLevel.jstree().create_node(res.treeItem.parent, res.treeItem);
						}
						$confirmLevel.modal('hide');
					}
				});
				return true;
			})
			.on('click', '#btnLevelCancel', function(e) {
				$confirmLevel.modal('hide');
			})
			.on('click', '#btnRemove', function(e) {
				var id = $treeScreenDocLevel.jstree().get_selected()[0];
				var data = $treeScreenDocLevel.jstree().get_node(id);

				var msg = NCI.getMessage('MSG0072', NCI.getMessage(data.type === 'file' ? 'associateWithFolder' : 'folder'));
				NCI.confirm(msg, function() {
					var params = {};
					params[data.type === 'file' ? 'screenDocId' : 'screenDocLevelId'] = data.id;
					NCI.post('/dc0120/save', params).done(function(res) {
						if (res && res.success) {
							$treeScreenDocLevel.jstree().delete_node(id);
						}
					});
				});
			})
			.on('dnd_start.vakata.jstree', function(e, data) {
				moves = [];
			})
			.on('dnd_stop.vakata.jstree', function(e, data) {
				if (!moves || !moves.length) return;
				var levels = [];
				var defs = [];
				$.each(moves, function(i, e) {
					var node = e;
					if (node.type === 'file') {
						var parent = $treeScreenDocLevel.jstree().get_node(node.parent)
						defs.push({screenDocId: node.data.screenDocId, screenDocLevelId: (parent.id === 'home' ? null : parent.data.screenDocLevelId), sortOrder: node.data.sortOrder});
					} else {
						levels.push({screenDocLevelId: node.data.screenDocLevelId, parentLevelCode: (node.parent === 'home' ? node.id : node.parent), sortOrder: node.data.sortOrder});
					}
				});
				var params = {
					levels: levels,
					defs: defs
				};
				NCI.post('/dc0120/move', params);
			})
			;
			$confirmLevel.on('hide.bs.modal', function() {
				$('#screenDocLevelId,#parentLevelCode,#levelCode').text('');
				$('#parentLevelName,#levelName').val('');
				$('#expansionFlag').prop('cheked', false);
				Validator.hideBalloon();
			});
		}
	});
});
