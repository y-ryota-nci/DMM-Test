$(function() {
	let $treeDocFolder = $('#treeDocFolder');
	let $confirmFolder = $('#confirmFolder');
	let moves = {};
//	let parentId = null;
	let $responsiveTable = new ResponsiveTable($('#accessible-contents'));
	$responsiveTable.modifyTR = modifyTR;

	NCI.init("/dc0050/init", {messageCds: ['MSG0064', 'MSG0066', 'MSG0067', 'MSG0069', 'MSG0071', 'MSG0072', 'MSG0213', 'docFolder']}).done(function(res) {
		if (res && res.success) {
			// メタテンプレートの選択肢
			let $metaTemplate = $('#metaTemplateId');
			NCI.createOptionTags($metaTemplate, res.metaTemplates);
			$metaTemplate.prop('selectedIndex', 0);

			$treeDocFolder
			// フォルダ移動時処理
			.on('move_node.jstree', function(ev, data) {
				if (data.parent !== data.old_parent || data.position !== data.old_position) {
					const parentNode = $treeDocFolder.jstree().get_node(data.parent);
					let count = 1;
					$.each(parentNode.children, function(i, d) {
						const childrenNode = $treeDocFolder.jstree().get_node(d);
						if (childrenNode.type !== 'home') {
							childrenNode.data.sortOrder = count;
							count++;
							// 複数フォルダをいっぺんに移動するとmove_nodeが複数回よばれてしまう
							// なのでmovesをIDをキーにした連想配列にして重複を許さないようにしている
							moves["'"+childrenNode.id+"'"] = childrenNode;
						}
					});
				}
			})
			.jstree({
				core: {
//					data: {
//						url: '../../endpoint/dc0050/getTreeItems',
//						data: function (node) {return { "nodeId": (node.id == 'top' ? '0' : node.id) };},
//						cache: false,
//						dataType: 'json'
//					},
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
						'valid_children': ['book']
					},
					'book': {
						'valid_children': ['book']
					}
				},
				sort: function(f, s) {
					var $f = $treeDocFolder.jstree().get_node(f);
					var $s = $treeDocFolder.jstree().get_node(s);
					// ソート処理は並び順の昇順＞IDの昇順で行う
					if (parseInt($f.data.sortOrder) == parseInt($s.data.sortOrder)) {
						return parseInt($f.id) > parseInt($s.id) ? 1 : -1;
					} else {
						return parseInt($f.data.sortOrder) > parseInt($s.data.sortOrder) ? 1 : -1;
					}
				},
				plugins: ['wholerow', 'dnd', 'types', 'sort']
			})
			// フォルダ選択時処理
			.on('changed.jstree', function(e, data) {
				$('#btnAdd').prop('disabled', data.selected.length > 1);
				$('#btnEdit').prop('disabled', data.node.type === 'home' || data.selected.length !== 1);
				let removeButtonDisabled = (data.node.type === 'home' || data.selected.length !== 1);
				if (!removeButtonDisabled && data.selected.length === 1) {
					removeButtonDisabled = $treeDocFolder.jstree().get_node(data.selected[0]).children.length > 0;
				}
				$('#btnRemove').prop('disabled', removeButtonDisabled);
			});
		}

	});

	$(document)
		.on('click', '#btnAdd', function(ev) {
			editFolder('1');
		})
		.on('click', '#btnEdit', function(ev) {
			editFolder('2');
		})
		.on('click', '#btnRemove', function(ev) {
			var msg = NCI.getMessage('MSG0072', NCI.getMessage("docFolder"));
			NCI.confirm(msg, function() {
				editFolder('3');
			});
		})
		.on('click', '#btnAddRole', function(ev) {
			const params = {"corporationCode": $('#corporationCode').val(), "docFlag": "1"};
			const url = "../cm/cm0090.html";
			Popup.open(url, callbackFromCm0090, params);
		})
		.on('click', '#btnDelRole', function(ev) {
			let $root = $('#accessibles');
			$('input[type=checkbox].selectable:checked', $root).each(function(i, elem) {
				let $tr = $(elem).closest('tr');
				$tr.remove();
			});
		})
		.on('click', '#btnRegist', function(ev) {
			saveFolder('1');
		})
		.on('click', '#btnUpdate', function(ev) {
			saveFolder('2');
		})
		.on('click', '#btnDelete', function(ev) {
			saveFolder('3');
		})
		.on('click', '#btnCancel', function(e) {
			$confirmFolder.modal('hide');
		})
		.on('click', '#btnInformDlgOK', function() {
			$('#informDlg').modal('hide');
		})
		.on('dnd_start.vakata.jstree', function(e, data) {
			moves = {};
		})
		.on('dnd_stop.vakata.jstree', function(e, data) {
			// movesは連想配列なので要素数の取得がちょっと特殊
			if (!moves || Object.keys(moves).length === 0) return;
			let folders = [];
			let parentDocFolderId = null;
			$.each(moves, function(key, value) {
				const node = value;
				if (parentDocFolderId == null) {
					const parent = $treeDocFolder.jstree().get_node(node.parent);
					parentDocFolderId = (parent.id === '#' || parent.id === 'top' ? 0 : parent.id);
				}
				folders.push({docFolderId: node.id, version: node.data.version, docFolderHierarchyId: node.data.hierarchyId, sortOrder: node.data.sortOrder});
			});
			const params = {'folders': folders, 'parentDocFolderId': parentDocFolderId};
			NCI.post('/dc0050/move', params).done(function(res) {
				if (res && res.success) {
					$.each(res.treeItems, function(i, t) {
						let node = $treeDocFolder.jstree().get_node(t.id);
						if (node) {
							node.data.version = t.data.version;
						}
					});
				}
			});
		})
		.on('click', 'input[type=checkbox][data-field=authRefer]', function() {
			changeAuthRefer($(this));
		})
		.on('click', 'input[type=checkbox][data-field=authEdit]', function() {
			changeAuthEdit($(this));
		});

	/** 文書フォルダの追加・編集・削除 */
	function editFolder(type) {

		const isAdd  = (type === '1');
		const isEdit = (type === '2');
		const isDel  = (type === '3');
		const selected = $treeDocFolder.jstree().get_selected();
		const id = selected.length && selected[0] != 'top' ? selected[0] : '0';
		const params = {'parentDocFolderId': isAdd ? id : null, 'docFolderId': isAdd ? null : id};
		if (isAdd || isEdit) {
			NCI.post('/dc0050/edit', params).done(function(res) {
				if (res && res.success) {
					// 文書フォルダ情報を展開
					let $folderContents = $('#folder-contents');
					NCI.toElementsFromObj(res.folder);
					// 権限情報を展開
					$responsiveTable.fillTable(res.accessibles);

					// カレンダー（年月日）
					// 初期状態だとフォーカスアウト時にblankを保存してしまうため位置を修正
					NCI.ymdPicker($('input.ymdPicker'));
					// 上記のコードのみだとフォルダを編集→キャンセル→別のフォルダを編集とした場合に
					// ymdpickerのchangeDateが動かないため強制的にchangeDateを動かす
					if (res.folder) {
							var start = res.folder.validStartDate;
							var end = res.folder.validEndDate;
							$("#validStartDate").datepicker("setDate", start);
							$("#validEndDate").datepicker("setDate", end);
					}

					// 設定状況に応じてチェックボックスの活性／非活性を切り替える
					switchingAuth();
					// 登録／更新／削除ボタンは表示切り替え
					$('#btnRegist').toggleClass('hide', (res.folder.docFolderId != null));
					$('#btnUpdate').toggleClass('hide', (res.folder.docFolderId == null));
					$('#btnDelete').toggleClass('hide', (res.folder.docFolderId == null));

					$confirmFolder.modal();
					// modal()を実行して作成された[div.modal-backdrop]に対して[modal-stack]のclassを付与
					// これで参加者ロール選択子画面を立ち上げ、その後閉じても$confirmFolderが裏に隠れないようになる
					$('.modal-backdrop').not('.modal-stack').addClass('modal-stack');
				}

			});
		} else if (isDel) {
			NCI.post('/dc0050/delete', params).done(function(res) {
				if (res && res.success) {
					$treeDocFolder.jstree().delete_node(id);
				}
			});
		}

	}

	function saveFolder(type) {
		const isRegist = (type === '1');
		const isUpdate = (type === '2');
		const isDelete = (type === '3');
		// 登録・更新時はバリデーションを実行
		if (isRegist || isUpdate) {
			if (!validate()) {
				return;
			}
		}
		let messageCd = null;
		if (isRegist) {
			messageCd = 'MSG0069';
		} else if (isUpdate) {
			messageCd = 'MSG0071';
		} else if (isDelete) {
			messageCd = 'MSG0072';
		}
		let msg = NCI.getMessage(messageCd, NCI.getMessage("docFolder"));
		if (NCI.confirm(msg, function() {
			let $folderContents = $('#folder-contents');
			let $accessibleContents = $('#accessible-contents');
			let folder = NCI.toObjFromElements($folderContents);
			folder.deleteFlag = (isDelete ? '1' : '0');
			let accessibles = NCI.toArrayFromTable($accessibleContents, ['assignRoleName', 'userName']);
			let params = {folder: folder, accessibles: accessibles};
			NCI.post("/dc0050/save", params).done(function(res) {
				if (res && res.success && res.alerts.length == 0) {
					if (isRegist) {
						$treeDocFolder.jstree().create_node(res.treeItem.parent, res.treeItem);
					} else if (isUpdate) {
						$treeDocFolder.jstree().set_text(res.treeItem.id, res.treeItem.text);
						$treeDocFolder.jstree().get_node(res.treeItem.id).data.version = res.treeItem.data.version;
					} else if (isDelete) {
						$treeDocFolder.jstree().delete_node(folder.docFolderId);
					}
				}
				$confirmFolder.modal('hide');
			});
		}));
	}

	function modifyTR($tr, rowIndex, entity, labels, ignores) {
		$tr.find('span[data-field=roleName]').toggleClass('hide', (entity.roleCode == null));
		$tr.find('span[data-field=userName]').toggleClass('hide', (entity.userCode == null));
	}

	function switchingAuth() {
		// 設定状況に応じてチェックボックスの活性／非活性を切り替える
		$('tbody>tr', $('#accessibles')).each(function(i, tr) {
			let $tr = $(tr);
			changeAuthRefer($('input[type=checkbox][data-field=authRefer]', $tr));
			changeAuthEdit($('input[type=checkbox][data-field=authEdit]', $tr));
		});
	}

	function changeAuthRefer($authRefer) {
		let $tr = $authRefer.closest('tr');
		let $authDownload = $('input[type=checkbox][data-field=authDownload]', $tr);
		// 参照権限がONであればダウンロード権限は活性化
		if ($authRefer.prop('checked')) {
			$authDownload.prop('disabled', false);
		}
		// 参照権限がOFFであればダウンロード権限は非活性、かつチェックもOFFへ
		else {
			$authDownload.prop('disabled', true).prop('checked', false);
		}
	}

	function changeAuthEdit($authEdit) {
		let $tr = $authEdit.closest('tr');
		let $authDelete = $('input[type=checkbox][data-field=authDelete]', $tr);
		let $authCopy = $('input[type=checkbox][data-field=authCopy]', $tr);
		let $authMove = $('input[type=checkbox][data-field=authMove]', $tr);
		let $authRefer = $('input[type=checkbox][data-field=authRefer]', $tr);
		let $authDownload = $('input[type=checkbox][data-field=authDownload]', $tr);
		// 編集権限があれば参照・ダウンロード・削除・コピー・移動の各権限を活性化。さらに参照権限は自動的にONにし編集不可にする
		if ($authEdit.prop('checked')) {
			$authDelete.prop('disabled', false);
			$authCopy.prop('disabled', false);
			$authMove.prop('disabled', false);
			$authRefer.prop('disabled', true).prop('checked', true);
			$authDownload.prop('disabled', false);
			Validator.hideBalloon($authRefer);
		}
		// 編集権限がなければ削除・コピー・移動権限は非活性化、かつチェックもOFFへ。参照権限は活性化。
		else {
			$authDelete.prop('disabled', true).prop('checked', false);
			$authCopy.prop('disabled', true).prop('checked', false);
			$authMove.prop('disabled', true).prop('checked', false);
			$authRefer.prop('disabled', false);
		}
	}

	function validate() {
		let error = false;
		let $folderContents = $('#folder-contents');
		let $accessibleContents = $('#accessible-contents');
		if (!Validator.validate($folderContents.find("input"), true)) {
			error = true;
		}
		let $table = $('#accessibles');
		const msg = NCI.getMessage('MSG0213');
		$('tbody>tr', $table).each(function(i, tr) {
			const $tr = $(this);
			if ($('input[type=checkbox][data-field]:checked', $tr).length == 0) {
				const $target = $('input[type=checkbox][data-field=authRefer]', $tr);
				Validator.hideBalloon($target);
				Validator.showBalloon($target, msg);
				error = true;
			}
		});
		return !error;
	}

	function callbackFromCm0090(result) {
		if (result) {
			let exist = false;
			$('tbody>tr',  $('#accessibles')).each(function(i, tr) {
				if (result.assignRoleCode === $('span[data-field=assignRoleCode]', $(tr)).text()) {
					exist = true;
					return false;
				}
			});
			if (!exist) {
				const corporationCode = result.corporationCode;
				const code = result.assignRoleCode;
				const name = result.assignRoleName;
				const obj = {corporationCode:corporationCode, assignRoleCode:code, assignRoleName:name};
				$responsiveTable.addRowResult(obj);
			}
		}
	}
});
