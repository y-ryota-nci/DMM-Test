$(function() {
	const params = {
			"messageCds" : ["user", "organization", 'MSG0072'],
			"corporationCode" : NCI.getQueryString('corporationCode'),
			"organizationCode" : NCI.getQueryString('organizationCode'),
			"displayValidOnly" : NCI.getQueryString('displayValidOnly'),
			"baseDate" : NCI.getQueryString("baseDate")
	};
	NCI.init('/mm0000/init', params).done(function(res) {
		if (res && res.success) {
			// 基準日
			NCI.ymdPicker($('#baseDate').val(res.baseDate));
			// 有効データのみ表示
			$('#displayValidOnly').prop('checked', res.displayValidOnly);

			// プロファイルのツリーデータ
			$('#treeOrg').jstree({
				'core': {
					'data' : {
						"url" : "../../endpoint/mm0000/getOrgTreeItem",
						"data" : function (node) {
							return { "nodeId" : node.id, "baseDate" : $('#baseDate').val(), "displayValidOnly" : $('#displayValidOnly').prop('checked') };
						},
						"cache" : false,
						"dataType" : "json"
					}
				},
				"plugins" : [
					"wholerow"	/* 行全体で選択できるようにする */
				]
			})
			// 企業・組織の選択時
			.on('changed.jstree', selectOrgTree)
			// 下位組織を開く前
			.on('before_open.jstree', function(node) { return validate(); })
			;

			// 企業追加ボタン
			$('#btnAddCorp').toggle(NCI.loginInfo.aspAdmin).prop('disabled', false);

			// 初期表示するノードを開く
			expandNode(res.nodeIds);
//			if (res.nodeIds && res.nodeIds.length)
//				expandNode(res.nodeIds[res.nodeIds.length - 1]);

			$(document)
				// 企業追加ボタン押下
				.on('click', '#btnAddCorp', openCorpPopup)
				// （企業追加ポップアップ）企業コード＋名称の入力を促すダイアログのＯＫボタン
				.on('click', '#btnAddCorpOk', callbackCorpPopup)
				// （企業追加ポップアップ）企業コード＋名称の入力を促すダイアログのキャンセルボタン
				.on('click', '#btnAddCorpCancel', closeCorpPopup)
				// 企業編集ボタン
				.on('click', '#btnEditCorp', editCorp)
				// 組織追加ボタン
				.on('click', '#btnAddOrg', addOrg)
				// 組織編集ボタン
				.on('click', '#btnEditOrg', editOrg)
				// 組織削除ボタン
				.on('click', '#btnDeleteOrg', deleteOrg)
				// ユーザ追加ボタン
				.on('click', '#btnAddUser', addUser)
				// ユーザ編集ボタン
				.on('click', '#btnEditUser', editUser)
				// 「基準日」「有効データのみ表示」の変更
				.on('change', '#baseDate, #displayValidOnly', function() { refreshOrgTree(); })
				;
		}
	});
});

/** 企業追加用ポップアップを開く */
function openCorpPopup(e) {
	if (!validate()) {
		return false;
	}
	// 企業コード＋名称の入力を促すダイアログを表示
	$('#newCorporationCode, #newCorporationName').val('');
	$('#confirmAddCorp').modal();
}

/** 企業追加用ポップアップからのコールバック関数 */
function callbackCorpPopup(e) {
	if (!Validator.validate($('#newCorporationCode, #newCorporationName'), true))
		return false;

	const params = {
		newCorporationCode : $('#newCorporationCode').val(),
		newCorporationName : $('#newCorporationName').val(),
		baseDate : $('#baseDate').val(),
		displayValidOnly : $('#displayValidOnly').prop('checked')
	};
	NCI.post('/mm0000/addCorp', params).done(function(res) {
		if (res && res.success) {
			// 組織ツリーの読み直し
			refreshOrgTree();

			$('#confirmAddCorp').modal("hide");
		}
	});
	return true;
}

/** 企業追加用ポップアップを閉じる */
function closeCorpPopup(e) {
	Validator.hideBalloon( $('#newCorporationCode, #newCorporationName') );
	$('#confirmAddCorp').modal('hide');
}

/** 企業編集を開く */
function editCorp(e) {
	if (!validate()) {
		return false;
	}
	const selects = $('#treeOrg').jstree().get_selected();
	if (selects && selects.length) {
		const nodeIds = selects[0].split("/");
		const nodeId = nodeIds[0];
		redirectMM0001(nodeId);
	}
	return false;
}

/** 組織を追加 */
function addOrg(e) {
	if (!validate()) {
		return false;
	}

	const msg = NCI.getMessage('MSG0070', NCI.getMessage('organization'));
	if (NCI.confirm(msg, function() {
		// 現在の選択組織へ所属ユーザを追加する
		const selects = $('#treeOrg').jstree().get_selected();
		if (selects && selects.length) {
			const nodeId = selects[0];
			const params = { "nodeId": nodeId, "baseDate": $('#baseDate').val(), "displayValidOnly" : $('#displayValidOnly').prop('checked') };
			NCI.post('/mm0000/addOrg', params).done(function(res) {
				if (res && res.success) {
					// 組織ツリーの読み直し
					refreshOrgTree(res.nodeIds);
				}
			});
		}
	}));
	return false;
}

/** 組織編集画面を開く */
function editOrg(e) {
	if (!validate()) {
		return false;
	}
	const selects = $('#treeOrg').jstree().get_selected();
	if (selects && selects.length) {
		const nodeIds = selects[0].split("/");
		const nodeId = nodeIds[nodeIds.length - 1];
		redirectMM0002(nodeId);
	}
	return false;
}

/** 組織の削除 */
function deleteOrg(e) {
	if (!validate()) {
		return false;
	}

	const msg = NCI.getMessage('MSG0072', NCI.getMessage('organization'));
	if (NCI.confirm(msg, function() {
		// 現在の選択組織を削除
		const selects = $('#treeOrg').jstree().get_selected();
		if (selects && selects.length) {
			const nodeId = selects[0];
			const params = { "nodeId": nodeId, "baseDate" : $('#baseDate').val() };
			NCI.post('/mm0000/deleteOrg', params).done(function(res) {
				if (res && res.success) {
					// 組織ツリーの読み直し
					refreshOrgTree();
				}
			});
		}
	}));
	return false;
}

/** ユーザの追加 */
function addUser(e) {
	if (!validate()) {
		return false;
	}

	const msg = NCI.getMessage('MSG0070', NCI.getMessage('user'));
	if (NCI.confirm(msg, function() {
		// 現在の選択組織へ所属ユーザを追加する
		const selects = $('#treeOrg').jstree().get_selected();
		if (selects && selects.length) {
			const nodeId = selects[0];
			const params = { "nodeId": nodeId, "baseDate" : $('#baseDate').val(), "displayValidOnly" : $('#displayValidOnly').prop('checked') };
			NCI.post('/mm0000/addUser', params).done(function(res) {
				if (res && res.success) {
					// 所属ユーザの読み直し
					loadUser(nodeId);
				}
			});
		}
	}));
	return false;
}

/** ユーザ編集画面を開く */
function editUser(e) {
	if (!validate()) {
		return false;
	}
	const selects = $('#treeUser').jstree().get_selected();
	if (selects && selects.length) {
		const nodeId = selects[0];
		redirectMM0003(nodeId);
	}
	return false;
}

/** 選択した組織下のユーザを表示 */
function loadUser(nodeId) {
	const r = validate();
	if (r) {
		const params = { "nodeId" : nodeId, "baseDate": $('#baseDate').val(), "displayValidOnly" : $('#displayValidOnly').prop('checked') };
		$('#treeUser').jstree('destroy');
		NCI.get('/mm0000/getUser', params).done(function(users) {
			if (users && users.length) {
				$('#treeUser').jstree({
					'core': { 'data': users },
					"plugins" : [
						"wholerow",	/* 行全体で選択できるようにする */
					]
				})
				// ユーザの選択時
				.on('changed.jstree', function(e, data) {
					// 選択内容によるボタン制御
					$('#btnEditUser').prop('disabled', data.selected.length === 0);
				})
				// ユーザを開く前
				.on('before_open.jstree', function(node) {
					return (Validator.validate($('#baseDate')));
				});
			}
		});
		$('#btnEditUser').prop('disabled', true);
	}
	return r;
}

/** 企業／組織ツリーの選択項目を変更 */
function selectOrgTree(e, data) {
	// ※企業／組織の取得は jsTree側で勝手にやるので特に処理なし。
	// ※これ以降でやっているのは選択した組織配下のユーザの一覧の抽出処理
	if (!validate()) {
		return false;
	}
	$('#btnEditCorp').prop('disabled', data.selected.length === 0);
	$('#btnAddOrg').prop('disabled', data.selected.length === 0);
	$('#btnEditOrg, #btnDeleteOrg').prop('disabled', false);
	$('#btnAddUser').prop('disabled', false);
	$('#btnEditUser').prop('disabled', true);
	if(data.selected.length) {
		// 選択内容によるユーザ・組織の追加ボタン制御
		const nodeId = data.selected[0];
		const isCorp = (nodeId.split('@').length === 2);
		$('#btnEditCorp').prop('disabled', !isCorp);
		$('#btnEditOrg, #btnDeleteOrg').prop('disabled', isCorp);
		$('#btnAddUser').prop('disabled', isCorp);
		// 選択組織の所属ユーザを表示
		return loadUser(nodeId);
	}
	return true;
}

function refreshOrgTree(nodeIds) {
	const r = validate();
	if (r) {
		const $tree = $('#treeOrg').jstree(true);
		if ($tree.refresh) {
			// 読み直し
			$tree.refresh();
			// 指定された組織を展開
			expandNode(nodeIds);
		}
	}
	return r;
}

function validate() {
	const r = Validator.validate($('#baseDate'));
	return r;
}

/** 企業編集画面へリダイレクト */
function redirectMM0001(nodeId) {
	const ids = nodeId.split('@');
	const corporationCode = ids[0], timestampUpdated = ids[1]
		, baseDate = $('#baseDate').val(), displayValidOnly = $('#displayValidOnly').prop('checked');
	const uri = NCI.format(
			"./mm0001.html?corporationCode={0}&timestampUpdated={1}&baseDate={2}&displayValidOnly={3}",
			corporationCode, timestampUpdated, baseDate, displayValidOnly);
	NCI.redirect(uri);
}

/** 組織編集画面へリダイレクト */
function redirectMM0002(nodeId) {
	const ids = nodeId.split('@'), corporationCode = ids[0], organizationCode = ids[1]
		, timestampUpdated = ids[2], baseDate = $('#baseDate').val()
		, displayValidOnly = $('#displayValidOnly').prop('checked');
	const uri = NCI.format(
			"./mm0002.html?corporationCode={0}&organizationCode={1}&timestampUpdated={2}&baseDate={3}&displayValidOnly={4}",
			corporationCode, organizationCode, timestampUpdated, baseDate, displayValidOnly);
	NCI.redirect(uri);
}

/** ユーザ編集画面へリダイレクト */
function redirectMM0003(nodeId) {
	const ids = nodeId.split('@');
	const corporationCode = ids[0], organizationCode = ids[1], userCode = ids[2]
		, timestampUpdated = ids[3], baseDate = $('#baseDate').val()
		, displayValidOnly = $('#displayValidOnly').prop('checked');
	const uri = NCI.format(
			"./mm0003.html?corporationCode={0}&userCode={1}&organizationCode={2}&timestampUpdated={3}&baseDate={4}&displayValidOnly={5}"
			, corporationCode, userCode, organizationCode, timestampUpdated, baseDate, displayValidOnly);
	NCI.redirect(uri);
}

/**
 * 組織ノードを開く
 * @param nodeIds ノードID。先頭には企業ノードID、2つ目以降が開いておきたい組織ノードID。
 * 			展開したい最後の組織から上位ノードIDがすべてが必要だ。
 * 			なお、配列の末尾が表示したい組織の最下位となる。
 * [
 * 	'NCI@123456',
 * 	'NCI@00000001@1@123456',
 * 	'NCI@00000002@2@123456',
 * 	'NCI@00000003@3@123456', 	<--- 配列の末尾が表示したい組織の最下位となる
 * ]
 */
function expandNode(nodeIds) {
	if (nodeIds && nodeIds.length) {
		const $tree = $('#treeOrg').jstree(true);
		// ノードの一括ロード
		const len = nodeIds.length;
		for (let i = 0; i < len; i++) {
			$tree.load_node(nodeIds[i]);
		}
		// ロード完了待ちをしつつ、各ノードを開く
		let count = 0;
		const lastNodeId = nodeIds[len - 1];
		const timer = setInterval(function() {
			for (let i = 0; i < len; i++) {
				const nodeId = nodeIds[i];
				if (!$tree.is_open(nodeId))
					$tree.open_node(nodeId);
			}
			if ($tree.get_node(lastNodeId)) {
				// 前画面で指定されたノード（＝最終ノード）が開けたら終わり
				$tree.select_node(lastNodeId);
				clearInterval(timer);
			} else if (50 < count++) {
				// 試行回数が上限を超えれば終わり。
				// おそらく組織が削除されたためであろうから、親組織を表示しておく。
				if (nodeIds.length > 1) {
					$tree.select_node(nodeIds[nodeIds.length - 2]);
				}
				clearInterval(timer);
			}
		}, 300);
	}
}
