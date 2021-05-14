$(function() {
	// 遷移元からの受信パラメータ
	var params = {
		"corporationCode" : NCI.getQueryString("corporationCode"),
		"organizationLevel" : NCI.getQueryString("organizationLevel"),
	};
	var pager = new Pager($('#seach-result'), '/cm0020/search', search).init();
	pager.responsiveTable.modifyTR = function($tr, rowIndex, entity, labels, ignores) {
		$tr.find('input[type=radio]').prop('disabled', false);
	};

	NCI.init("/cm0020/init", params).done(function(res) {
		if (res && res.success) {
			// カレンダー（年月日）
			NCI.ymdPicker($('input.ymdPicker'));
			// 組織レベルの選択肢
			NCI.createOptionTags($('#organizationLevel'), ['',1,2,3,4,5,6,7,8,9])
					.val(NCI.getQueryString("organizationLevel"));
			// 企業が指定されていればその企業コードを、なければ自社
			NCI.createOptionTags($('#corporationCode'), res.corporations)
					.val(NCI.getQueryString("corporationCode") || NCI.loginInfo.corporationCode);

			// 初期検索するか？
			const initSearch = NCI.getQueryString("initSearch");
			if (initSearch && $('#tabSearch').hasClass('active')) {
				search(1);
			}

			$(document)
			.on('click', '#btnSearch', function() {
				// 検索ボタン押下
				search(1);
				return false;
			})
			.on('click', 'ul.pagination a', function() {
				// ページ番号リンク押下
				var pageNo = this.getAttribute('data-pageNo');
				search(pageNo);
				return false;
			})
			// 検索結果の選択ラジオボタン
			.on('change', 'input[type=radio][name=rdoSelect]', function() {
				$('#btnSelect1').prop('disabled', false);
			})
			// 戻るボタン
			.on('click', 'button.btnClose, button.btnBack', function() {
				Popup.close();
			})
			// （検索して選択）選択ボタン
			.on('click', '#btnSelect1', function() {
				var $tr = $('input[name=rdoSelect]:checked').parent().parent();
				var entity = {};
				$tr.find('[data-field]').each(function(i, elem) {
					var fieldName = elem.getAttribute('data-field');
					entity[fieldName] = $(elem).text();
				});

				// コールバック関数の呼び出し
				Popup.close(entity);
			})
			// （組織ツリーから選択）選択ボタン
			.on('click', '#btnSelect2', function() {
				const nodeIds = $('#treeOrg').jstree(true).get_selected();
				if (nodeIds && nodeIds.length) {
					const ids = nodeIds[0].split('@');
					const url = '/cm0020/getWfmOrganization?corporationCode=' + ids[0] + '&organizationCode=' + ids[1];
					NCI.get(url).done(function(entity) {
						// コールバック関数の呼び出し
						Popup.close(entity);
					});
				}
			})
			// 「基準日」「有効データのみ表示」の変更
			.on('change', '#baseDate', function() { refreshOrgTree(); })
			;

			// 基準日
			NCI.ymdPicker($('#baseDate').val(NCI.today()));

			// プロファイルのツリーデータ
			$('#treeOrg').jstree({
				'core': {
					'data' : {
						"url" : "../../endpoint/cm0020/getOrgTreeItem",
						"data" : function (node) {
							return { "nodeId" : node.id, "baseDate" : $('#baseDate').val(), "displayValidOnly" : true };
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
		}
	});

	/** 検索実行 */
	function search(pageNo) {
		var $targets = $('#formCondition').find('input, select');
		if (!Validator.validate($targets, true))
			return false;

		var cond = createCondition(pageNo);
		pager.search(cond);
		$('#btnSelect1').prop('disabled', true);
		$('#seach-result').removeClass('invisible');
	}

	/** 画面入力内容から検索条件を生成 */
	function createCondition(pageNo) {
		var $elements = $('#formCondition').find('input, select, textarea');
		var cond = pager.createCondition($elements, pageNo);
		// デフォルトソート条件
		if (!cond.sortColumn) {
			cond.sortColumn = 'ORGANIZATION_ADDED_INFO, ID';
			cond.sortAsc = true;
		}
		return cond;
	}

	/** 企業／組織ツリーの選択項目を変更 */
	function selectOrgTree(e, data) {
		// ※企業／組織の取得は jsTree側で勝手にやるので特に処理なし。
		// ※これ以降でやっているのは選択した組織配下のユーザの一覧の抽出処理
		if (!validate()) {
			return false;
		}
		$('#btnSelect2').prop('disabled', data.selected.length === 0);
		return true;
	}

	function validate() {
		const r = Validator.validate($('#baseDate'));
		return r;
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
			for (let i = 0; i < nodeIds.length; i++) {
				const nodeId = nodeIds[i];
				$tree.load_node(nodeIds[i]);
			}
			// ロード完了待ちをしつつ、各ノードを開く
			let count = 0;
			const lastNodeId = nodeIds[nodeIds.length - 1];
			const timer = setInterval(function() {
				for (let i = 0; i < nodeIds.length; i++) {
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
});

