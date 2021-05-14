const DC0020_TREE_CACHE = "#DC0020_TREE_CACHE#";

$(function() {
	var $treeDocFolder = $('#treeDocFolder');
	var pager = new Pager($('#seach-result'), '/dc0020/search', search).init();
	pager.responsiveTable.modifyTR = modifyTR;	// データを反映する前に、コンテンツ種別毎の設定

	var treeCache = NCI.flushScope(DC0020_TREE_CACHE);
	var loadCondition = !!treeCache;

	NCI.init("/dc0020/init", {messageCds: ['MSG0064', 'MSG0066', 'MSG0067', 'MSG0069', 'MSG0071', 'MSG0072', 'docFolder']}).done(function(res) {
		if (res && res.success) {
			$treeDocFolder.jstree({
				core: {
					data: {
						url: '../../endpoint/dc0020/getTreeItems',
						data: function (node) {return { "nodeId": (node.id == 'top' ? '0' : node.id) };},
						cache: false,
						dataType: 'json'
					},
					multiple: false
				},
				types: {
					'top': {
						'max_children': 1,
						'max_depth': -1,
						'valid_children': ['home']
					},
					'home': {
						'valid_children': ['folder']
					},
					'folder': {
						'valid_children': ['folder']
					}
				},
				sort: function(f, s) {
					var $f = $treeDocFolder.jstree().get_node(f);
					var $s = $treeDocFolder.jstree().get_node(s);
					if ($f.type === $s.type && parseInt($f.data.sortOrder) < parseInt($s.data.sortOrder)) return -1;
					if ($f.type === $s.type && parseInt($f.data.sortOrder) > parseInt($s.data.sortOrder)) return 1;
					return 0;
				},
				plugins: ['wholerow', 'dnd', 'types', 'sort']
			})
			.on('ready.jstree', function(ev, instance) {
				if (treeCache) {
					pager.loadCondition();
					// 文書ファイルの含めるかのチェック結果
					$('#dispIncludeDocFile').prop('checked', ($('#includeDocFile').val() === '1'));
					expandNode(treeCache);
					treeCache = null;
				} else {
					$treeDocFolder.jstree('select_node', 'top');
				}

			})
			.on('deselect_node.jstree', function(ev, data) {
				$treeDocFolder.jstree('select_node', data.node.id);
			})
			.on('select_node.jstree', function(ev, data) {
				// IE/firefoxで"overflow:hidden;"を指定していると
				// テキストが長いNodeを選択した際にノードが左にずれてしまう
				// それを補正するためここでjsTreeの位置を再度元に戻してやる
				// なおChromeは問題はない（IE/firefoxのバグ？）
				$('#treeDocFolder').scrollLeft(0);

				var selected = data.selected[0];
				$('#folderPath').text(data.node.data.folderPath).prop('title', data.node.text);
				$('#docFolderId').val(selected == 'top' ? '0' : selected);
				let pageNo = null;
				if (loadCondition) {
					loadCondition = false;
				} else {
					$('#searchType').val('1');
					// ツリー上のフォルダ押下した場合は常に１頁目で検索
					pageNo = 1;
				}
				search(pageNo);
				return false;
			})
			;

			$(document)
			.on('click', 'ul.pagination a', function(ev) {
				// ページ番号リンク押下
				var pageNo = this.getAttribute('data-pageNo');
				search(pageNo);
				return false;
			})
			// 簡易検索ボタン押下
			.on('click', '#btnSimpleSearch', function(ev) {
				const $elements = $('#formCondition').find('input');
				if (Validator.validate($elements, true)) {
					$('#searchType').val('2');
					// 文書ファイルの含めるかのチェック結果を反映
					let checked  = $('#dispIncludeDocFile').prop('checked');
					$('#includeDocFile').val(checked ? '1' : '');
					search(1);
				}
				return false;
			})
			// 詳細検索ボタン押下
			.on('click', '#btnDetailSearch', function(ev) {
				const url = "./dc0022.html";
				NCI.redirect(url);
			})
			// 検索結果のフォルダリンク押下
			.on('click', 'a[data-field=title].folder', function(ev) {
				const $div = $(this).closest('div.media');
				$treeDocFolder.jstree('deselect_all');
				const docFolderId = $div.find('[data-field=docFolderId]').text();
				$treeDocFolder.jstree('open_node', docFolderId == '0' ? 'top' : docFolderId, function() {
					const id = $div.find('[data-field=id]').text();
					$treeDocFolder.jstree('select_node', id);
				});
			})
			// 検索結果の業務文書・バインダー・編集アイコンリンク押下
			.on('click', 'a[data-field=title].bizdoc, a[data-field=title].binder, a>i.fa-edit', function(ev) {
				NCI.flushScope(DC0020_TREE_CACHE, $treeDocFolder.jstree().get_json());
				const $div = $(this).closest('div.media');
				const docId = $div.find('[data-field=id]').text();
				const corporationCode = $div.find('[data-field=corporationCode]').text();
				var url = "./dc0100.html?docId=" + docId + '&corporationCode=' + corporationCode;
				NCI.redirect(url);
			})
			// 検索結果の文書ファイルリンク押下
			.on('click', 'a.fileName', function(ev) {
				const $span = $(this).closest('span');
				const docFileDataId = $span.find('span.docFileDataId').text();
				if (docFileDataId) {
					NCI.download('/dc0020/download?docFileDataId=' + docFileDataId);
				}
			})
			// 検索結果の編集不可アイコンリンク押下(=ロック)
			.on('click', 'a>i.fa-lock', function(ev) {
				const $tr = $(this).closest('tr');
				const docId = $tr.find('[data-field=id]').text();
				const corporationCode = $tr.find('[data-field=corporationCode]').text();
				const docFileId = $tr.find('[data-field=docFileId]').text();
				const version = $tr.find('[data-field=version]').text();
				const contentsType = $tr.find('[data-field=contentsType]').text();
				if (contentsType === '1' || contentsType === '2') {
					const params = {docId : docId, corporationCode : corporationCode, version : version};
					NCI.post("/dc0020/lock", params).done(function(res) {
						if (res && res.success && res.results) {
							$.each(res.results, function(i, entity) {
								pager.fillRowResult($tr, $tr.index(), entity, null);
							});
						}
					});
				} else if (contentsType === '3') {
					const params = {docId : docId, corporationCode : corporationCode, docFileId : docFileId, version : version};
					NCI.post("/dc0020/lockFile", params).done(function(res) {
						if (res && res.success && res.docFileInfo) {
						}
					});
				}

			})
			// 検索結果の編集可アイコンリンク押下(=ロック解除)
			.on('click', 'a>i.fa-unlock-alt', function(ev) {
				const $tr = $(this).closest('tr');
				const docId = $tr.find('[data-field=id]').text();
				const corporationCode = $tr.find('[data-field=corporationCode]').text();
				const docFileId = $tr.find('[data-field=docFileId]').text();
				const version = $tr.find('[data-field=version]').text();
				const contentsType = $tr.find('[data-field=contentsType]').text();
				if (contentsType === '1' || contentsType === '2') {
					const params = {docId : docId, corporationCode : corporationCode, version : version};
					NCI.post("/dc0020/unlock", params).done(function(res) {
						if (res && res.success && res.results) {
							$.each(res.results, function(i, entity) {
								pager.fillRowResult($tr, $tr.index(), entity, null);
							});
						}
					});
				} else if (contentsType === '3') {
					const params = {docId : docId, corporationCode : corporationCode, docFileId : docFileId, version : version};
					NCI.post("/dc0020/unlockFile", params).done(function(res) {
						if (res && res.success && res.docFileInfo) {
						}
					});
				}
			})
			.on('click', 'a>i.fa-copy', function(ev) {
				const $tr = $(this).closest('tr');
				const docId = $tr.find('[data-field=id]').text();
				const docFileId = $tr.find('[data-field=docFileId]').text();
				const corporationCode = $tr.find('[data-field=corporationCode]').text();
				const version = $tr.find('[data-field=version]').text();
				const docFolderId = $tr.find('[data-field=docFolderId]').text();
				const contentsType = $tr.find('[data-field=contentsType]').text();
				if (contentsType === '1' || contentsType === '2') {
					const params = {docId : docId, corporationCode : corporationCode, version : version, exclusionDocFolderId:docFolderId};
					Popup.open("../dc/dc0903.html", fromDc0903, params, ev);
				} else if (contentsType === '3') {

				}
			})
			.on('click', 'a>i.fa-arrows', function(ev) {
				const $tr = $(this).closest('tr');
				const docId = $tr.find('[data-field=id]').text();
				const corporationCode = $tr.find('[data-field=corporationCode]').text();
				const docFileId = $tr.find('[data-field=docFileId]').text();
				const version = $tr.find('[data-field=version]').text();
				const docFolderId = $tr.find('[data-field=docFolderId]').text();
				const contentsType = $tr.find('[data-field=contentsType]').text();
				if (contentsType === '1' || contentsType === '2') {
					const params = {docId : docId, corporationCode : corporationCode, version : version, exclusionDocFolderId:docFolderId};
					Popup.open("../dc/dc0902.html", fromDc0902, params, ev);
				} else if (contentsType === '3') {

				}
			})
			.on('click', 'a>i.fa-trash', function(ev) {
				const $tr = $(this).closest('tr');
				const docId = $tr.find('[data-field=id]').text();
				const corporationCode = $tr.find('[data-field=corporationCode]').text();
				const docFileId = $tr.find('[data-field=docFileId]').text();
				const version = $tr.find('[data-field=version]').text();
				const contentsType = $tr.find('[data-field=contentsType]').text();
				if (contentsType === '1' || contentsType === '2') {
					const msg = NCI.getMessage('MSG0072', '文書情報');
					if (NCI.confirm(msg, function() {
						const params = {targetDocId : docId, corporationCode : corporationCode, version : version};
//						const pageNo = $($('ul.pagination [data-pageno].active')[0]).text();
						const condition = createCondition();
						for (let key in params) {
							condition[key] = params[key];
						}
						NCI.post("/dc0020/delete", condition).done(function(res) {
							if (res && res.success && res.alerts.length == 0) {
								pager.showSearchResult(res);
							}
						});
					}));
				} else if (contentsType === '3') {
					const msg = NCI.getMessage('MSG0072', '文書ファイル');
					if (NCI.confirm(msg, function() {
						const params = {docId : docId, corporationCode : corporationCode, docFileId : docFileId, version : version};
//						const pageNo = $($('ul.pagination [data-pageno].active')[0]).text();
						const condition = createCondition();
						for (let key in params) {
							condition[key] = params[key];
						}
						NCI.post("/dc0020/deleteFile", condition).done(function(res) {
							if (res && res.success && res.alerts.length == 0) {
								pager.showSearchResult(res);
							}
						});
					}));
				}
			});
			;
		}
	});

	/** 検索実行 */
	function search(pageNo) {
		var cond = createCondition(pageNo);
		pager.search(cond);
		$('#seach-result').removeClass('invisible');
	}

	function createCondition(pageNo) {
		var $elements = $('#formCondition').find('input');
		var cond = pager.createCondition($elements, pageNo);
		// デフォルトソート条件
		if (!cond.sortColumn) {
			cond.sortColumn = 'D.CONTENTS_TYPE, D.ID';
			cond.sortAsc = true;
		}
		return cond;
	}

	/** 業務管理項目ごとにテンプレートを書き変えるメソッド（ResponsiveTableのデフォルト実装を上書きする想定）  */
	function modifyTR($tr, rowIndex, entity, labels, ignores) {
		const isFolder = entity.contentsType === '0';
		const isBizDoc = entity.contentsType === '1';
		const isBinder = entity.contentsType === '2';
		const isFile = entity.contentsType === '3';
		const isLock = (entity.lockFlag === '1');
		const isOwnLock = (entity.lockUserFlag === '1');
		const isEdit   = (entity.authEdit === '1');
		const isDownload = (isEdit || entity.authDownload === '1');
		const isDelete = (entity.authDelete === '1');
		const isCopy = (entity.authEdit === '1');
		const isMove = (entity.authEdit === '1');
		const isWfApplying = (entity.wfApplying === '1');

		// アイコン設定
		let iconClass = '';
		let addClass = '';
		if (isFolder) {
			iconClass = 'fa-folder';
			addClass = 'folder';
		} else if (isBizDoc) {
			iconClass = 'fa-file';
			addClass = 'bizdoc';
		} else if (isBinder) {
			iconClass = 'fa-book';
			addClass = 'binder';
		} else if (isFile) {
			iconClass = 'fa-file';
			addClass = 'docfile';
		}
		$tr.find('span.contents-icon').addClass(iconClass);
		$tr.find('a[data-field=title]').addClass(addClass);

		// ロック情報設定
		$tr.find('span.lock-info').toggleClass('hide', (isFolder || !isLock));

		// WF連携情報設定
		$tr.find('span.wf-apply-info').toggleClass('hide', (!isWfApplying));

		// 登録者情報設定
		$tr.find('div.register-info').toggleClass('hide', (isFolder));

		// ボタン情報設定
		$tr.find('div.button-info').toggleClass('hide', (isFolder));
		// フォルダ以外であればアイコン設定
		if (!isFolder) {
			$tr.find('a>i.fa-lock').toggleClass('hide', !(!isWfApplying && isEdit && !isLock && !isOwnLock));
			$tr.find('a>i.fa-unlock-alt').toggleClass('hide', !(isOwnLock));
			$tr.find('a>i.fa-edit').toggleClass('hide', false);
			$tr.find('a>i.fa-download').toggleClass('hide', !(isFile && isDownload));
			$tr.find('a>i.fa-copy').toggleClass('hide', !(isEdit && isCopy));
			$tr.find('a>i.fa-arrows').toggleClass('hide', !(isEdit && isMove && isOwnLock));
			$tr.find('a>i.fa-trash').toggleClass('hide', !(isEdit && isDelete && isOwnLock));
		}
		// 業務文書またはバインダーでダウンロード権限がある場合、文書ファイルを一覧に表示
		if ((isBizDoc || isBinder) && isDownload && entity.docFiles) {
			let docFiles = entity.docFiles.split(',');
			if (docFiles && docFiles.length > 0) {
				let $div = $tr.find('div.doc-files-info');
//				let spanTemplate = $div[0].querySelector('span.doc-file');
				let spanTemplate = $('<span><a href="#" class="fileName"></a><span class="docFileDataId hide"></span>&nbsp;&nbsp;&nbsp;</span>')[0];
				const df = document.createDocumentFragment();
				$.each(docFiles, function(i, obj) {
					let idx = obj.lastIndexOf('@');
					let fileName = obj.substring(0, idx);
					let docFileDataId = obj.substring(idx + 1);
					console.log('fileName -> {' + fileName + '} docFileDataId -> {' + docFileDataId + '}');
					const $span = $(spanTemplate.cloneNode(true));
					$span.find('a.fileName').text(fileName);
					$span.find('span.docFileDataId').text(docFileDataId);
					$span.each(function(i, span) {
						df.appendChild(span);
					});
				});
				$div[0].appendChild(df);
				$div.toggleClass('hide', false);
			}

		}
		return $tr;
	}

	/** 文書フォルダ移動画面から戻るときのコールバック */
	function fromDc0902(result) {
		if (result) {
			const params = {targetDocId : result.docId, corporationCode : result.corporationCode, version : result.version, docFolderIdTo:result.docFolderId};
//			const pageNo = $($('ul.pagination [data-pageno].active')[0]).text();
			const condition = createCondition();
			for (let key in params) {
				condition[key] = params[key];
			}
			NCI.post("/dc0020/move", condition).done(function(res) {
				if (res && res.success && res.alerts.length == 0) {
					pager.showSearchResult(res);
				}
			});
		}
	}

	/** 文書コピー画面から戻るときのコールバック */
	function fromDc0903(result) {
		if (result) {
			const params = {targetDocId : result.docId, corporationCode : result.corporationCode, version : result.version, docFolderIdTo:result.docFolderId, newTitle:result.newTitle};
//			const pageNo = $($('ul.pagination [data-pageno].active')[0]).text();
			const condition = createCondition();
			for (let key in params) {
				condition[key] = params[key];
			}
			NCI.post("/dc0020/copy", condition).done(function(res) {
				if (res && res.success && res.alerts.length == 0) {
//					pager.showSearchResult(res);
				}
			});
		}
	}
	/** 記憶していたフォルダ情報を元に復元を行う */
	function expandNode(nodes) {
		$.each(nodes, function(i, data) {
			if (data.state.opened) {
				$treeDocFolder.jstree('open_node', data.id, function() {
					if (data.children && data.children.length) {
						expandNode(data.children);
					}
				});
			}
			if (data.state.selected) {
				$treeDocFolder.jstree('select_node', data.id);
			}
		})
	}
});

