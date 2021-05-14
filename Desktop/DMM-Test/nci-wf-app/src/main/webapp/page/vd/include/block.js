/** 入力可能なトレイタイプの配列 */
const INPUTABLE_TRAY_TYPES = ["NEW", "WORKLIST", "FORCE", "BATCH"];

$(function() {
	// 起案者情報／入力者情報ブロックのイベント定義
	block0001.bindEvent();
	// 文書内容ブロック
	block0002.bindEvent();
	// 添付ファイルブロック
	block0003.bindEvent();
	// 決裁関連文書ブロック
	block0005.bindEvent();
	// 履歴情報ブロック
	block0006.bindEvent();
	// 承認者情報ブロック
	block0007.bindEvent();
	// 情報共有者ブロック
	block0008.bindEvent();
	// 参照者ブロックのイベント定義
	block0009.bindEvent();
	// 要説明ブロック
	block0010.bindEvent();
	// メモ情報ブロック
	block0011.bindEvent();
	// アクションコメント情報ブロック
	block0013.bindEvent();
	// 文書管理関連文書ブロック
	block0014.bindEvent();
	// ワークフロー文書ファイルブロック
	block0015.bindEvent();
});

/** アクション情報(1)ブロック */
var block0000 = {
	rootHtmlId : 'block0000',

	/** イベントのバインドが終わっているか */
	boundEvent : false,

	/**
	 * ボタン⇒スタイル・アイコン
	 */
	button: {
		/** [0]状態遷移. */
		'0': {cssClass: 'btn-primary', icon: 'fa fa-check'},
		/** [1]引戻. */
		'1': {cssClass: 'btn-default', icon: 'fa fa-level-down'},
		/** [2]差戻. */
		'2': {cssClass: 'btn-warning', icon: 'fa fa-reply'},
		/** [3]取消. */
		'3': {cssClass: 'btn-danger', icon: 'fa fa-times'},
//		/** [4]要説明. */
//		'4': {cssClass: 'btn-success', icon: 'glyphicon glyphicon-tag'},
//		/** [9]説明回答. */
//		'9'INFO: {cssClass: 'btn-success', icon: 'glyphicon glyphicon-tags'},
		/** [S]保存. */
		'S': {cssClass: 'btn-default', icon: 'fa fa-pencil'},
		/** [A]アクション機能実行. */
		'A': {cssClass: 'btn-info', icon: 'fa fa-print'},
		/** [B]差戻(チェックなし). */
		'B': {cssClass: 'btn-warning', icon: 'fa fa-reply'},
		/** [F]引上. */
		'F': {cssClass: 'btn-default', icon: 'fa fa-level-up'}
	},
	/**
	 * 初期処理
	 */
	init: function(res) {
		const $rightArea = $('#' + this.rootHtmlId).find('div.btn-list ul.btn_1');
		const $leftArea = $('#' + this.rootHtmlId).find('div.btn-list ul.btn_2');
		$.each(vd0310.contents.actionList, function(rowIndex, action) {
			let id = 'btnAction_';
			if (action.seqNoActionDef) {
				id += action.seqNoActionDef;
			} else {
				if (action.actionType === ActionType.SAVE) {
					id += 'save';
				} else if (action.actionType === ActionType.PULLBACK) {
					id += 'pullback';
				}
			}
			const $button = $('<button></button>')
					.attr('id', id)
					.addClass('btn btn-m btn-sm ' + block0000.button[action.actionType].cssClass)
					.append('<i class="' + block0000.button[action.actionType].icon + '"></i>')
					.append($('<span>').text(action.actionName));


			const $li = $('<li></li>');
			$li.append($button);
			$li.addClass('btn-group');

			if (action.actionType === ActionType.SENDBACK || action.actionType === ActionType.SENDBACK_NC) {
				const sendbackList = vd0310.contents.sendbackList[action.seqNoActionDef];
				if (sendbackList) {
					if (sendbackList.length > 1) {
						$button.attr('data-toggle', 'dropdown');
						$button.addClass('dropdown-toggle');
						$button.append('<span class="caret"></span>');

						const $ul = $('<ul></ul>');
						$li.append($ul);
						$ul.attr('aria-labelledby', id);
						$ul.addClass('dropdown-menu');

						$.each(vd0310.contents.sendbackList[action.seqNoActionDef], function(i, sendback) {
							const $span = $('<span></span>');
							$span.text(sendback.activityDefNameTransit);

							const $a = $('<a class="menuitem" tabindex="-1" href="javascript:void(0)"></a>');
							$a.append($span);
							$a.on('click', function(e) {vd0310.execute(action, sendback);});
							$ul.append($('<li></li>').append($a));
						});
						$ul.find('li').hover(function() {$(this).addClass('active');}, function() {$(this).removeClass('active');});
					} else {
						$button.on('click', function(e) {vd0310.execute(action, sendbackList[0]);});
					}

				} else {
					$button.on('click', function(e) {vd0310.execute(action);});
				}
			} else if (action.actionType === ActionType.PULLBACK) {
				const pullbackList = vd0310.contents.pullbackList;
				if (pullbackList && pullbackList.length === 1) {
					$button.on('click', function(e) {vd0310.execute(action, null, pullbackList[0]);});
				} else if (pullbackList) {
					$button.attr('data-toggle', 'dropdown');
					$button.addClass('dropdown-toggle');
					$button.append('<span class="caret"></span>');

					const $ul = $('<ul></ul>');
					$li.append($ul);
					$ul.attr('aria-labelledby', id);
					$ul.addClass('dropdown-menu');

					$.each(pullbackList, function(i, pullback) {
						const $span = $('<span></span>');
						$span.text(pullback.activityDefName);
						const $a = $('<a class="menuitem" tabindex="-1" href="javascript:void(0)"></a>');
						$a.append($span);
						$a.on('click', function(e) {vd0310.execute(action, null, pullback);});
						$ul.append($('<li></li>').append($a));
					});
					$ul.find('li').hover(function() {$(this).addClass('active');}, function() {$(this).removeClass('active');});
				}
			} else {
				$button.on('click', function(e) {vd0310.execute(action);});
			}
			if (action.actionType === ActionType.CANCEL)
				$leftArea.append($li);
			else
				$rightArea.append($li);
		});

		block0000.boundEvent = true;
	}
};

/** 起案者情報／入力者情報ブロック */
var block0001 = {
	init: function(res) {
		// 入力時の内容をエレメントへ退避
		$('#startUserInfo').data('startUserInfo', vd0310.contents.startUserInfo);

		// 起案担当者の変更ボタン
		let isStartActivity = (vd0310.contents.activityDefCode === vd0310.contents.startActivityDefCode);
		let isWorklist = INPUTABLE_TRAY_TYPES.indexOf(vd0310.contents.trayType) > -1;
		$('#btnChangeStartUser')
			.toggle(isWorklist && isStartActivity)
			.prop('disabled', false);
		// 個々の画面で起案担当者変更の限定条件があるならそれを優先。
		// ただし画面の初期化は非同期なので、ここでは画面の初期化が完了していればそれに従い、未定義なら有効としておく
		vd0310.verifyChangeStartUser();
	},

	/** イベント定義 */
	bindEvent : function() {
		$(document)
			.on('click', '#block0001 .collapse_btn', block0001.expand)
			.on('click', '#block0001 .btnChangeStartUser', block0001.openCm0050);
	},

	/** ブロックの開閉 */
	expand : function(ev) {
		if ($(this).hasClass('collapsed')) {
			return;
		}
		block0001.fillData();
	},

	/** ユーザ選択 */
	openCm0050 : function(ev) {
		// バリデーション
		if (!PARTS.validateParts(false)) {
			return false;
		}
		// 画面項目をRuntimeMapへ反映
		PARTS.fillRuntimeMap();

		const params = {
			'corporationCode' : vd0310.contents.processUserInfo.corporationCode,
			'userCodeTransfer' : vd0310.contents.processUserInfo.userCode,
			'corporationCodeP' : vd0310.contents.corporationCode,
			'processDefCode' : vd0310.contents.processDefCode,
			'processDefDetailCode' : vd0310.contents.processDefDetailCode
		};
		Popup.open("../wl/wl0100.html", block0001.callbackFromCm0050, params);
	},

	fillData : function() {
		$('span[data-field]', '#processUserInfo').each(function(i, elem) {
			let fieldName = elem.getAttribute('data-field');
			if (fieldName && fieldName in vd0310.contents.processUserInfo) {
				let value = vd0310.contents.processUserInfo[fieldName];
				$(elem).text(value);
			}
		});
		$('span[data-field]', '#startUserInfo').each(function(i, elem) {
			let fieldName = elem.getAttribute('data-field');
			if (fieldName && fieldName in vd0310.contents.startUserInfo) {
				let value = vd0310.contents.startUserInfo[fieldName];
				$(elem).text(value);
			} else {
				$(elem).text('');
			}
		});
	},

	callbackFromCm0050 : function(ub) {
		if (ub) {
			// 画面へ反映
			let $startUserInfo = $('#startUserInfo');
			NCI.toElementsFromObj(ub, $startUserInfo);

			// 既存データを書き換え（既存のキーが新データにもあるなら転写）
			let startUserInfo = $('#startUserInfo').data('startUserInfo');
			$.each(startUserInfo, function(name, val) {
				if (name in ub) {
					startUserInfo[name] = ub[name]
				} else {
					startUserInfo[name] = '';
				}
			});
			// 代理元として起案担当者を設定
			vd0310.contents.proxyUser = startUserInfo.corporationCode + "_" + startUserInfo.userCode;

			// 新しい起案担当者をもとに、承認者情報ブロックを読み直し
			const params = {
					'corporationCode': vd0310.contents.corporationCode,
					'processId': vd0310.contents.processId,
					'processDefCode': vd0310.contents.processDefCode,
					'processDefDetailCode': vd0310.contents.processDefDetailCode,
					'startUserInfo': startUserInfo,
					'processUserInfo': vd0310.contents.processUserInfo,
					'contents' : vd0310.contents,
					'runtimeMap' : $('#editArea').data('runtimeMap')
			};
			// GetRouteを呼び出す前に、画面項目を吸い上げしていることを確認せよ。
			// さもないとGetRouteで比較条件変数が正しく反映されない。
			// (通常はユーザ選択ポップアップを呼び出す前に Parts.fillRuntimeMap() を呼び出し済みなはず)
			NCI.post('/vd0310/getRoute', params).done(function(res) {
				if (res && res.success) {
					vd0310.contents.routeList = res.routeList;
					block0007.redrawRoute(res.routeList);

					// 画面イベント：起案担当者変更
					if (vd0310.contents.changeStartUserFunctions) {
						$.each(vd0310.contents.changeStartUserFunctions, function(i, func) {
							PARTS.execFunction(func.funcName, 'changeStartUser', func.param);
						});
					}
				}
			});
		}
	}
};

/** 文書内容ブロック */
var block0002 = {
	/** イベントのバインドが終わっているか */
	boundEvent : false,

	init: function(res) {
		$('#editArea')
		.html(res.html)
		.data('runtimeMap', res.contents.runtimeMap);
		NCI.ymdPicker($('input.ymdPicker', '#editArea'));
		NCI.ymPicker($('input.ymPicker', '#editArea'));

		// 外部Javascriptの読み込み
		if (res.contents.javascriptIds && res.contents.javascriptIds.length) {
			const outsideURL = '../../endpoint/javascript/outside?'
				+ PARTS.toJavascriptQueryString(res.contents.javascriptIds);

			// 外部Javascriptの読み込み後に画面定義の「ロード時の関数名」をコール
			const $outsideJavascript = $('#outside-javascript');
			if (!$outsideJavascript.attr('src')) {
				// 外部Javascriptがまだ読み込まれていなかったら、ロード
				$outsideJavascript.attr('src', outsideURL).load(function() {
					block0002.callInitFunction(res);
					// 個々の画面で起案担当者変更の限定条件があるならそれを優先。
					vd0310.verifyChangeStartUser();
				});
			} else {
				block0002.callInitFunction(res);
				// 個々の画面で起案担当者変更の限定条件があるならそれを優先。
				vd0310.verifyChangeStartUser();
			}
		}
	},

	/** 画面定義の「ロード時の関数名」を実行 */
	callInitFunction : function(res) {
		// 注意：「ロード時の関数名」は保存されるたびに呼び出されるので「ロード時の関数」のなかでイベントのバインドを
		// 行うと同じイベントが保存した回数分だけバインドされてしまう。イベントのバインドは「ロード時の関数」ではなく定義すること。
		const loadFunctions = res.contents.loadFunctions;
		if (loadFunctions && loadFunctions.length) {
			$.each(loadFunctions, function(i, func) {
				PARTS.execFunction(func.funcName, 'init', func.param);
			});
		}
	},

	/** イベント定義 */
	bindEvent : function() {
		// 文書内容ブロックに固有のイベントはない。
		// パーツは parts.js内でダイナミックにバインドされているのでここでは不要。
		// 外部Javascriptのイベント定義を「ロード時の呼び出し関数」で行うと、申請文書を一時保存したときに
		// 「ロード時の呼び出し関数」が再び呼ばれてしまうので、イベント定義が重複して行われてしまう
		// よって外部Javascriptのイベントは必ず $(function() { ...}) でバインドすること。
	}
};

/** 添付ファイルブロック */
var block0003 = {
	isWorklist: null,
	isInputable: null,
	_table: null,
	init: function(res) {
		block0003.isWorklist = (INPUTABLE_TRAY_TYPES.indexOf(res.contents.trayType) > -1);
		block0003.isInputable = (res.contents.activityDef.attachedDocumentFlag === '1');
		const $block0003Contents = $('#block0003_contents').data({
			"additionAttachFileWfList" : [],
			"removeAttachFileWfIdList" : []
		});
		block0003._table = new Pager($block0003Contents);
		block0003._table.fillTable(res.contents.attachFileWfList);
		block0003.buttonDisabled();

		if (block0003.isWorklist && block0003.isInputable) {
			$('#attachFileWfInputArea').removeClass('hide');
		} else {
			$('#attachFileWfInputArea').addClass('hide');
		}

	},

	/** イベント定義 */
	bindEvent : function() {
		$(document)
			.on('click', '#block0003 .collapse_btn', block0003.expand)
			.on('click', '#block0003 a.lnkDownloadAttachFileWf', block0003.download)
			.on('click', '#updFile', function() {
				$('#block0003').find('input[type=file]').click();
			})
			.on('click', 'button.btnRemoveAttachFileWf, a.btnRemoveAttachFileWf', block0003.remove);

		const selector = '#block0003_contents div.dragZone, #block0003_contents input[type=file]';
		FileUploader.setup(selector, "/vd0310/uploadWf", false, block0003.add);
	},

	/** 添付ファイルブロックを展開 */
	expand : function(ev) {
		if (!$(ev.target).hasClass('collapsed')
				&& (!vd0310.contents.attachFileWfList
						|| !vd0310.contents.attachFileWfList.length)) {
			const params = {
					corporationCode: vd0310.contents.corporationCode,
					processId: vd0310.contents.processId
			};
			NCI.get('/vd0310/getAttachFile', params).done(function(attachFileList) {
				if (attachFileList && attachFileList.length) {
					vd0310.contents.attachFileWfList = attachFileList;
				}
				block0003._table.fillTable(vd0310.contents.attachFileWfList);
				block0003.buttonDisabled();
			});
		}
	},
	/** ダウンロード */
	download : function(ev) {
		const $tr = $(ev.target).closest('tr');
		const attachFileWfId = $tr.find('[data-field=attachFileWfId]').text();
		NCI.download('/vd0310/downloadWf?attachFileWfId=' + attachFileWfId);
	},
	/** 追加 */
	add : function(res) {
		if (res && res.success && res.attachFileWfList && res.attachFileWfList.length) {
			if (!vd0310.contents.attachFileWfList) {
				vd0310.contents.attachFileWfList = [];
			}
			const size = $('table.responsive tbody tr', $('#block0003_contents')).length + 1;
			$.each(res.attachFileWfList, function(rowIndex, entity) {
				entity.rowNo = rowIndex + size;
				vd0310.contents.attachFileWfList.push(entity);
				$('#block0003_contents').data('additionAttachFileWfList').push(entity);
				block0003._table.addRowResult(entity);
			});
			block0003.buttonDisabled();
		}
	},
	/** 削除 */
	remove : function(ev) {
		const $tr = $(ev.target).closest('tr');
		const attachFileWfId = $tr.find('[data-field=attachFileWfId]').text();
		const processId = $tr.find('[data-field=processId]').text();
		const msg = NCI.getMessage('MSG0072', NCI.getMessage('attachFile'));
		if (NCI.confirm(msg, function() {
			if (processId) {
				$('#block0003_contents').data('removeAttachFileWfIdList').push(attachFileWfId);
				block0003.removeRow(attachFileWfId);
			} else {
				const params = {
					attachFileWfId: attachFileWfId
				};
				NCI.post('/vd0310/deleteWf', params).done(function(res) {
					if (res && res.success) {
						$.each(res.attachFileWfList, function(rowIndex, entity) {
							block0003.removeRow(entity.attachFileWfId);
						});
					}
				});
			}
		}));
	},
	/** 行削除 */
	removeRow : function(attachFileWfId) {
		const result = [];
		let nowNo = 1;
		$.each(vd0310.contents.attachFileWfList, function(rowIndex, entity) {
			if (attachFileWfId != entity.attachFileWfId) {
				entity.rowNo = nowNo++;
				entity.comments = $('table.responsive tbody tr', $('#block0003_contents')).filter(function(index) {
					return entity.attachFileWfId == $(this).find('span[data-field=attachFileWfId]').text();
				}).find('textarea').val()
				result.push(entity);
			}
		});
		vd0310.contents.attachFileWfList = result;
		block0003._table.fillTable(vd0310.contents.attachFileWfList);
	},
	buttonDisabled : function() {
		$('table.responsive tbody tr', $('#block0003_contents')).each(function(i, e) {
			let $tr = $(e);
			let processId = $tr.find('span[data-field=processId]').text();
			$tr.find('textarea').prop('readonly', !block0003.isWorklist || !!processId);
			$tr.find('a.btn').toggleClass('hide', !block0003.isWorklist);
		});
	},
	getAdditionAttachFileWfList: function() {
		const additionAttachFileWfList = $('#block0003_contents').data('additionAttachFileWfList') || [];
		$.each(additionAttachFileWfList, function(i, e) {
			const attachFileWfId = e.attachFileWfId;
			const comments = $('table.responsive tbody tr', $('#block0003_contents')).filter(function(index) {
				return attachFileWfId == $(this).find('span[data-field=attachFileWfId]').text();
			}).find('textarea').val();
			e.comments = comments;
		});
		return additionAttachFileWfList;
	},
	getRemoveAttachFileWfIdList: function() {
		return $('#block0003_contents').data('removeAttachFileWfIdList') || [];
	}
};

/** 決裁関連文書ブロック */
var block0005 = {
	_table : null,

	init: function(res) {
		// 入力可能なトレイモードか？
		let isInputable = (INPUTABLE_TRAY_TYPES.indexOf(res.contents.trayType) > -1);
		if (isInputable) {
			$('#btnAddApprovalRelation, #btnDeleteApprovalRelation').removeClass('hide');

		}
		block0005._table = new ResponsiveTable($('#block0005_contents'));
		block0005._table.modifyTR = function($tr, rowIndex, entity, labels, ignores) {
			$tr.find('input[type=radio]').prop('disabled', false);
		};
		block0005._table.fillTable(res.contents.approvalRelationList);

		$('input[name=rdoApprovalRelation]').toggleClass('hide', !isInputable);
		$('#btnAddApprovalRelation').prop('disabled', false);
		$('#btnDeleteApprovalRelation').prop('disabled', true);
	},

	/** イベント定義 */
	bindEvent : function(ev) {
		$(document)
			.on('click', 'input[name=rdoApprovalRelation]', block0005.onCheck)
			.on('click', '#block0005_contents a[data-field]', block0005.openDocumentAsPopup)
			.on('click', '#block0005_expansion', block0005.expand)
			.on('click', '#btnAddApprovalRelation', block0005.openCm0060)
			.on('click', '#btnDeleteApprovalRelation', block0005.removeRow);
	},

	/** 決裁関連文書ブロックを展開 */
	expand : function(ev) {
		if ($(ev.target).prop('checked')) {
			// 決裁関連文書を読み直し
			if (!vd0310.contents.approvalRelationList) {
				let params = {
						"corporationCode" : vd0310.contents.corporationCode,
						"processId" : vd0310.contents.processId
				};
				NCI.post('/vd0310/getApprovalRelationList', params, true).done(function(res) {
					if (res && res.success && res.approvalRelationList) {
						block0005._table.fillTable(res.approvalRelationList);
					}
				});
			}
		}
	},

	/** 行の選択時 */
	onCheck : function(ev) {
		$('#btnDeleteApprovalRelation').prop('disabled', false);
	},

	/** 行追加用に、決裁関連文書選択ポップアップを開く */
	openCm0060 : function(ev) {
		let params = {};
		let processIds = '';
		$('#block0005 tbody tr').each(function() {
			if (processIds == '') {
				processIds = $(this).find('[data-field=processIdRelation]').text();
			} else {
				processIds += ',' + $(this).find('[data-field=processIdRelation]').text();
			}
		});
		params = { "processIds" : processIds };

		Popup.open('../cm/cm0060.html', block0005.callback, params);
	},
	/** 行の削除 */
	removeRow : function(ev) {
		$('input[name=rdoApprovalRelation]:checked').each(function(i, checkbox) {
			$(checkbox).closest('tr').remove();
		});
		block0005._table.dispNoRecordMessage();
	},
	/** 決裁関連文書選択ポップアップからのコールバック関数 */
	callback : function(r) {
		if (r) {
			// 検索結果のプロセスIDが連携先プロセスIDになるので主/客が逆になる
			r.corporationCodeRelation = r.corporationCode;
			r.corporationCode = '';
			r.processIdRelation = r.processId;
			r.processId = '';
			block0005._table.addRowResult(r);
		}
	},
	/** 現在の決裁関連文書内容をObject化 */
	toApprovalRelationList : function() {
		let list = [];
		$('#block0005_contents table.responsive>tbody>tr').each(function(i, tr) {
			list.push(NCI.toObjFromElements($(tr)));
		});
		return list;
	},
	/** 申請・承認画面をポップアップとして参照モードで開く【アクセス可能な最新のアクティビティ】 */
	openDocumentAsPopup : function(ev) {
		// 連携先のプロセス情報
		let $tr = $(ev.target).closest('tr');
		let corporationCode = $tr.find('[data-field=corporationCodeRelation]').text();
		let processId = $tr.find('[data-field=processIdRelation]').text();
		let timestampUpdatedLong = $tr.find('[data-field=timestampUpdatedProcessLong]').text();

		// 連携先のプロセス情報のうち、操作者のアクセス可能な最新のアクティビティを求める。
		let params = { "corporationCode" : corporationCode, "processId" : processId, "trayType": "ALL" };
		NCI.post('/cm0060/getAccessibleActivity', params, true).done(function(res) {
			if (res && res.length) {
				Popup.open("../vd/vd0310.html?corporationCode=" + corporationCode +
						"&processId=" + processId +
						"&activityId=" + res[0].activityId +
						"&trayType=ALL" +
						"&timestampUpdated=" + timestampUpdatedLong);
			}
			else {
				if (window.console) window.console.error("操作者がアクセス可能なアクティビティがありませんでした。");
			}
		});
		return false;
	}
};

/** 履歴情報ブロック */
var block0006 = {
	init: function(res) {
	},

	/** イベント定義 */
	bindEvent : function() {
		$(document)
			.on('click', '#block0006 .collapse_btn', block0006.expand);
	},

	/** ブロックの開閉 */
	expand : function(ev) {
		const closed = $(this).hasClass('collapsed');
		if (!closed && vd0310.contents.processId && !vd0310.contents.historyList) {
			const params = {
					corporationCode: vd0310.contents.corporationCode,
					processId: vd0310.contents.processId
			};
			NCI.get('/vd0310/getHistory', params).done(function(results) {
				if (results && results.length) {
					vd0310.contents.historyList = results;
				}
			});
		}
		new Pager($('#historyList')).fillTable(vd0310.contents.historyList);
	}
};

/** 承認者情報ブロック */
var block0007 = {
	/**  */
	ignores1 : ['userName', 'organizationName', 'postName', 'proxyUsers', 'corporationCode', 'organizationCode', 'postCode', 'userCode'],
	/**  */
	ignores2 : ['id', 'activityDefName', 'activityDefCode', 'parentKey'],

	init: function(res) {
		if (res && res.success && res.contents.routeList) {
			let routeList = res.contents.routeList;
			block0007.redrawRoute(routeList);

			$('#btnOpenRouteStatus').toggle(!!res.contents.processId);
		}
	},

	/** ルート情報を再描画 */
	redrawRoute : function(routeList) {
			let $root = $('#block0007');
			let $tbl = $('#tblRoute');
			let responsiveTable = new ResponsiveTable($root);
			let templates = responsiveTable.getTemplate();
			let $tbody = $('tbody', $tbl).empty();
			$.each(routeList, function(idx, route) {
				let $trs = block0007.createRow(idx, route, responsiveTable, templates);
				$.each($trs, function(idx, $tr) {
					$tbody.append($tr);
				});
			});
	},

	/** イベント定義 */
	bindEvent : function() {
		$(document)
			// 並行承認のリンク押下
			.on('click', '#tblRoute>tbody>tr.parallel a', function() {
				const $tr = $(this).closest('tr');
				const $tbody = $('#tblRoute').find('tbody');
				block0007.onClickParallelLink($tr, $tbody, false);
			})
			// 承認状況ボタン押下
			.on('click', '#btnOpenRouteStatus', block0007.openRouteStatus);
	},

	/** 承認ルートのアクティビティ1つに対する行を生成 */
	createRow : function(rowIndex, route, responsiveTable, templates) {
		let $trs = [];

		// 並行承認・連携フローアクティビティの場合
		if (route.branchStartActivity) {
			let $tr = $(responsiveTable.toNewLine(templates)[0]);
			block0007.fillRowResultActivity($tr, rowIndex, route, 0)
			$trs.push($tr);

			// 並行承認内のルート情報を再帰呼出
			if (route.children && route.children.length > 0) {
				$.each(route.children, function(idx, child) {
					let $ctrs = block0007.createRow(++rowIndex, child, responsiveTable, templates);
					$.each($ctrs, function(idx, $tr) {
						$trs.push($tr);
					});
				});
			}
		}
		// 通常アクティビティの場合
		else {
			// 承認者の行数分作成
			$.each(route.assignedUserList, function(idx, usr) {
				let $tr = $(responsiveTable.toNewLine(templates)[1]);
				block0007.fillRowResultActivity($tr, rowIndex, route, idx)
				block0007.fillRowResultAssigned($tr, usr, route)
				$trs.push($tr);
				rowIndex++;
			});
		}

		return $trs;
	},

	/**
	 * 承認ルートのアクティビティに対する情報を反映.
	 * @param $tr 行データ
	 * @param rowIndex 行番号
	 * @param route ルート情報
	 * @param idx １アクティビティ内の参加者に対する番号（0～）
	 * @param init 初期表示か
	 */
	fillRowResultActivity : function($tr, rowIndex, route, idx) {
		// 並行承認内にあるアクティビティには親キーを設定
		if (route.parentKey != null) {
			$tr.addClass('hide ' + route.parentKey);
		}
		// 行の背景色の設定
		if (route.currentActivity) {
			$tr.addClass('current');
		} else if (route.closeActivity) {
			if (rowIndex%2 == 0) {
				$tr.addClass('closed-even');
			} else {
				$tr.addClass('closed-odd');
			}
		}
		// 行の最初のカラムを取得
		let $th = $tr.children().eq(0);
		if (idx == 0) {
			// アクティビティに関連する項目をセット
			NCI.toElementsFromObj(route, $tr, block0007.ignores1);
			// rowspanを設定
			let len = route.assignedUserList.length;
			if (len > 1) {
				$th.attr('rowspan', len);
			}
		} else {
			$th.remove();
		}
	},

	/**
	 * 参加者(承認者)に対する情報を反映.
	 * @param $tr 行データ
	 * @param usr 参加者(承認者)情報
	 * @param route ルート情報
	 */
	fillRowResultAssigned : function($tr, usr, route) {
		// 参加者に関連する項目を設定
		NCI.toElementsFromObj(usr, $tr, block0007.ignores2);
		// 矢印の表示
		if (route.currentActivity && usr.showArrow) {
			$tr.find('span.arrow').removeClass('invisible');
		}
	},

	/**
	 * 並行承認リンク押下時処理.
	 * @param $tr
	 * @param $tbody
	 * @param flg 親の並行承認リンクの開閉フラグ trueなら閉じる
	 */
	onClickParallelLink : function($tr, $tbody, flg) {
		let key = $('td[data-field=activityDefCode]', $tr).text();
		let selector = 'tr.' + key;
		$(selector, $tbody).each(function() {
			let $target = $(this);
			if (!flg && $target.hasClass('hide')) {
				$target.removeClass('hide');
			} else {
				$target.addClass('hide');
				if ($target.hasClass('parallel')) {
					// 親が閉じた場合は子供は常に閉じるよ
					block0007.onClickParallelLink($target, $tbody, true);
				}

			}
		});
	},

	/** 承認状況ポップアップを開く */
	openRouteStatus : function() {
		const params = {
				contents : vd0310.contents
		};
		Popup.open('../vd/vd0170.html', null, params);
	}
};

/** 情報共有者ブロック */
var block0008 = {
	init: function(res) {
	},

	/** イベント定義 */
	bindEvent : function() {
		$(document)
			.on('click', '#block0008 .collapse_btn', block0008.expand);
	},

	/** ブロック開閉 */
	expand : function(ev) {
		const closed = $(this).hasClass('collapsed');
		if (!closed && !vd0310.contents.informationSharerDefList) {
			const params = {
					corporationCode: vd0310.contents.corporationCode,
					processDefCode: vd0310.contents.processDefCode,
					processDefDetailCode: vd0310.contents.processDefDetailCode,
					processId: vd0310.contents.processId
			};
			NCI.get('/vd0310/getInformationSharerDef', params).done(function(results) {
				if (results && results.length) {
					vd0310.contents.informationSharerDefList = results;
				}
			});
		}
		new Pager($('#informationSharerDefList')).fillTable(vd0310.contents.informationSharerDefList);
	}
};

/** 参照者ブロック(閲覧可能者ブロック) */
var block0009 = {
	init: function(res) {
		$('#informationSharerList').data({
			additionInformationSharerList: [],
			removeInformationSharerList: []
		});
	},

	/** イベント定義 */
	bindEvent : function() {
		$(document)
			.on('click', '#block0009 .collapse_btn', block0009.expand)
			.on('click', '#block0009 .bthRemoveInfoSharer', block0009.remove)
			.on('click', '#block0009 .btnOrgAddInfoSharer', block0009.openCm0020)
			.on('click', '#block0009 .btnUsrAddInfoSharer', block0009.openCm0050);
	},

	/** ブロックの開閉 */
	expand : function(ev) {
		const closed = $(this).hasClass('collapsed');
		if (!closed && !vd0310.contents.informationSharerList && vd0310.contents.processId) {
			const params = {
					corporationCode: vd0310.contents.corporationCode,
					processId: vd0310.contents.processId
			};
			NCI.get('/vd0310/getInformationSharer', params).done(function(results) {
				if (results && results.length) {
					vd0310.contents.informationSharerList = results;
				}
			});
		}
		new Pager($('#informationSharerList')).fillTable(vd0310.contents.informationSharerList);
	},

	/** 参照者の削除 */
	remove : function(ev) {
		const $tr = $(this).closest('tr');
		const index = $('#informationSharerList tbody tr').index($tr);
		const data = vd0310.contents.informationSharerList[index];
		if (data.informationSharerId) {
			$('#informationSharerList').data('removeInformationSharerList').push(data);
		}
		vd0310.contents.informationSharerList.splice(index, 1);
		new Pager($('#informationSharerList')).fillTable(vd0310.contents.informationSharerList);
	},

	openCm0020 : function(ev) {
		Popup.open("../cm/cm0020.html", block0009.callbackFromCm0020);
	},

	callbackFromCm0020: function(org) {
		if (org) {
			if (!vd0310.contents.informationSharerList) {
				vd0310.contents.informationSharerList = [];
			}
			const data = {
				corporationCode: vd0310.contents.corporationCode,
				corporationCodeShare: org.corporationCode,
				organizationCode: org.organizationCode,
				organizationName: org.organizationName
			};
			vd0310.contents.informationSharerList.push(data);
			$('#informationSharerList').data('additionInformationSharerList').push(data);

			new Pager($('#informationSharerList')).addRowResult(data);
		}
	},
	openCm0050 : function(ev) {
		Popup.open("../cm/cm0050.html", block0009.callbackFromCm0050);
	},
	callbackFromCm0050: function(ub) {
		if (ub) {
			if (!vd0310.contents.informationSharerList) {
				vd0310.contents.informationSharerList = [];
			}
			const data = {
				corporationCode: vd0310.contents.corporationCode,
				corporationCodeShare: ub.corporationCode,
				organizationCode: ub.organizationCode,
				organizationName: ub.organizationName,
				postCode: ub.postCode,
				postName: ub.postName,
				userCode: ub.userCode,
				userName: ub.userName,
				userAddedInfo: ub.userAddedInfo
			}
			vd0310.contents.informationSharerList.push(data);
			$('#informationSharerList').data('additionInformationSharerList').push(data);

			new Pager($('#informationSharerList')).addRowResult(data);
		}
	}
};

/** 要説明ブロック */
var block0010 = {
	init: function(res) {
		// プロセスインスタンスを作成後のみ、投稿可能
		$("#btnNewThread").toggleClass('hide', !vd0310.contents.processId);
	},

	/** イベント定義 */
	bindEvent : function() {
		$(document)
			.on('click', '#btnNewThread', block0010.newThread)
			.on('click', 'a.btnReplyArticle', block0010.reply)
			.on('click', 'a.btnRemoveArticle', block0010.remove)
			.on('click', '#block0010 .collapse_btn', block0010.expand);
	},

	/** ブロックの開閉 */
	expand : function(ev) {
		if (!$(ev.target).hasClass('collapsed')) {
			// 要説明を読み直し
			let params = {
					"corporationCode" : vd0310.contents.corporationCode,
					"processId" : vd0310.contents.processId
			};
			NCI.post('/vd0310/getProcessBbsList', params, true).done(function(res) {
				if (res && res.success && res.processBbsList) {
					block0010.displayProcessBbs(res.processBbsList);
				}
			});
		}
	},

	/** 新規スレッドを投稿 */
	newThread : function(ev) {
		let processBbsIdUp = 0;	// 親プロセス掲示板ID=0 はスレッドの起点を表す
		block0010.openPopup(processBbsIdUp);
	},

	/** 対象投稿に対して、返信を投稿 */
	reply : function(ev) {
		let processBbsIdUp = $(ev.target).closest('div.media').attr('data-processBbsId');
		block0010.openPopup(processBbsIdUp);
	},

	/** 新しい要説明(掲示板)情報の入力画面をポップアップで開く */
	openPopup : function(processBbsIdUp) {
		let params = {
				"processBbsIdUp" : processBbsIdUp
		};
		Popup.open('../cm/cm0170.html', block0010.callback, params);
	},

	/** 要説明(掲示板)情報の入力画面からのコールバック関数 */
	callback : function(processBbsInfo) {
		if (processBbsInfo) {
			let params = {
					"corporationCode" : vd0310.contents.corporationCode,
					"processId" : vd0310.contents.processId,
					"processBbsInfo" : processBbsInfo
			};
			// 入力された内容でプロセス掲示板情報をインサートし、結果を反映
			NCI.post('/vd0310/submitProcessBbs', params).done(function(res) {
				if (res && res.success && res.processBbsList) {
					block0010.displayProcessBbs(res.processBbsList);
				}
			});
		}
	},

	/** 対象投稿を削除 */
	remove : function(ev) {
		let processBbsId = $(ev.target).closest('div.media').attr('data-processBbsId');
		let msg = NCI.getMessage('MSG0072', NCI.getMessage('processBbs'));
		NCI.confirm(msg, function() {
			// 指定されたプロセス掲示板IDを削除し、結果を反映
			let params = {
					"corporationCode" : vd0310.contents.corporationCode,
					"processId" : vd0310.contents.processId,
					"processBbsId" : processBbsId
			};
			NCI.post('/vd0310/deleteProcessBbs', params).done(function(res) {
				if (res && res.success && res.processBbsList) {
					block0010.displayProcessBbs(res.processBbsList);
				}
			});
		});
	},

	/** プロセス掲示板情報リストを表示 */
	displayProcessBbs : function(processBbsList) {
		let count = processBbsList != null ? processBbsList.length : 0;
		$('#bl0010-noRecord').toggleClass('hide', (count > 0));
		let $root = $('#bl0010-hasRecord')
			.empty()
			.toggleClass('hide', (count == 0));

		if (count > 0) {
			for (let i = 0; i < processBbsList.length; i++) {
				let pb = processBbsList[i];
				block0010.appendArticle(pb, $root);
			}
		}
	},

	/** 親エレメントの配下に記事を追加 */
	appendArticle : function(pb, $parent) {
		// テンプレートをコピーして、記事を反映
		let mySubmit = NCI.loginInfo.corporationCode == pb.corporationCodeSubmit
			&& NCI.loginInfo.userCode == pb.userCodeSubmit;
		let template = document.getElementById('bl0010-template');
		let $media = $(template.cloneNode(true));
		$media
			.attr('data-processBbsId', pb.processBbsId)
			.removeAttr('id');
		$media.find('a.btnReplyArticle')
			.toggleClass('hide', pb.deleteFlag == '1');
		$media.find('a.btnRemoveArticle')
			.toggleClass('hide', pb.deleteFlag == '1' || !mySubmit);

		NCI.toElementsFromObj(pb, $media);
		$media.find('[data-field=contents]').html(NCI.escapeHtmlBR(pb.contents));

		// 添付ファイルが添付されているなら添付処理
		block0010.appendAttachFiles(pb.attachFiles, $media);

		// 返信リストありなら再帰呼び出し
		if (pb.replies && pb.replies.length) {
			for (let i = 0; i < pb.replies.length; i++) {
				let reply = pb.replies[i];
				block0010.appendArticle(reply, $media);
			}
		}

		// 親の配下として追加
		if ($parent.is('#bl0010-hasRecord'))
			$parent.append($media);
		else
			$parent.find('>div.media-body>div.replies').append($media);
	},
	/** 添付ファイル表示 */
	appendAttachFiles: function(results, $parent) {
		let template = $('#bl0010-template-a a')[0];
		$.each(results, function(i, result) {
			let $a = $(template.cloneNode(true));
			let url = '../../endpoint/vd0310/download/bbsAttachFileWf?bbsAttachFileWfId=' + result.bbsAttachFileWfId;
			$a.attr('href', url);
			NCI.toElementsFromObj(result, $a);
			let fileType = result.fileType || '';
			if (fileType) {
				fileType = ('-' + fileType);
			}
			$a.find('span.file-icon').addClass('fa-file' + fileType + '-o');
			$parent.find('div.bbs-attach-files').append($a);
		});
		$parent.find('div.bbs-attach-files').parent().toggleClass('hide', $parent.find('div.bbs-attach-files a').length == 0);
	}
};

/** メモ情報ブロック  */
var block0011 = {
	init: function(res) {
		// プロセスインスタンスを作成後のみ、投稿可能
		$("#btnNewMemo").toggleClass('hide', !vd0310.contents.processId);
	},

	/** イベント定義 */
	bindEvent : function() {
		$(document)
			.on('click', '#btnNewMemo', block0011.openPopup)
			.on('click', '#block0011 .collapse_btn', block0011.expand);
	},

	expand : function(ev) {
		if (!$(ev.target).hasClass('collapsed')) {
			// メモ情報を読み直し
			let params = {
					"corporationCode" : vd0310.contents.corporationCode,
					"processId" : vd0310.contents.processId
			};
			NCI.post('/vd0310/getProcessMemoList', params, true).done(function(res) {
				if (res && res.success && res.processMemoList) {
					block0011.displayProcessMemo(res.processMemoList);
				}
			});
		}
	},

	/** メモ入力画面をポップアップで開く */
	openPopup : function() {
		Popup.open('../cm/cm0180.html', block0011.callback);
	},

	/** 要説明(掲示板)情報の入力画面からのコールバック関数 */
	callback : function(processMemoInfo) {
		if (processMemoInfo) {
			let params = {
					"corporationCode" : vd0310.contents.corporationCode,
					"processId" : vd0310.contents.processId,
					"memo" : processMemoInfo.memo
			};
			// 入力された内容でプロセス掲示板情報をインサートし、結果を反映
			NCI.post('/vd0310/submitProcessMemo', params).done(function(res) {
				if (res && res.success && res.processMemoList) {
					block0011.displayProcessMemo(res.processMemoList);
				}
			});
		}
	},

	/** プロセスメモリストを表示 */
	displayProcessMemo : function(processMemoList) {
		let count = processMemoList != null ? processMemoList.length : 0;
		$('#bl0011-noRecord').toggleClass('hide', (count > 0));
		let $root = $('#bl0011-hasRecord')
			.empty()
			.toggleClass('hide', (count == 0));

		if (count > 0) {
			for (let i = 0; i < processMemoList.length; i++) {
				let memo = processMemoList[i];
				block0011.appendMemo(memo, $root);
			}
		}
	},

	/** 親エレメントの配下に記事を追加 */
	appendMemo : function(pb, $parent) {
		// テンプレートをコピーして、記事を反映
		let mySubmit = NCI.loginInfo.corporationCode == pb.corporationCodeSubmit
			&& NCI.loginInfo.userCode == pb.userCodeSubmit;
		let template = document.getElementById('bl0011-template');
		let $media = $(template.cloneNode(true));
		$media.removeAttr('id');
		NCI.toElementsFromObj(pb, $media);
		$media.find('[data-field=memo]').html(NCI.escapeHtmlBR(pb.memo));

		// 親の配下として追加
		if ($parent.is('#bl0011-hasRecord'))
			$parent.append($media);
		else
			$parent.find('>div.media-body>div.replies').append($media);
	}
};

/** アクション情報(2)ブロック */
// アクション情報(2)はアクション情報(1)を使いまわす。違いは rootHtmlIdだけだ
var block0012 = {
	rootHtmlId : 'block0012',

	/** イベントのバインドが終わっているか */
	boundEvent : false,

	/**
	 * ボタン⇒スタイル・アイコン
	 */
	button: block0000.button,

	/**
	 * 初期処理
	 */
	init: block0000.init
};

/** アクションコメント情報ブロック */
var block0013 = {
	init: function(res) {
		// コメント欄
		$('#actionCommentArea').toggleClass('hide', !res.useActionComment);
		$('#actionComment').prop('disabled', !res.useActionComment);
	},

	/** イベント定義 */
	bindEvent : function() {
		block0013.boundEvent = true;
	}
};

/** 文書管理関連文書ブロック */
var block0014 = {
	init: function(res) {
	},

	/** イベント定義 */
	bindEvent : function() {
	}
};

/** ワークフロー文書ファイルブロック */
var block0015 = {
	isWorklist: null,
	isInputable: null,
	isDeletable: null,
	_table: null,
	init: function(res) {
		block0015.isWorklist = (INPUTABLE_TRAY_TYPES.indexOf(res.contents.trayType) > -1);
		block0015.isInputable = (res.contents.activityDef.attachedDocumentFlag === '1');
		block0015.isDeletable = (res.contents.applicationStatus !== '2');
		const $block0015Contents = $('#block0015_contents').data({
			"additionDocFileWfList" : [],
			"removeDocFileWfIdList" : []
		});
		block0015._table = new ResponsiveTable($block0015Contents);
		block0015._table.modifyTR = block0015.modifyTR;
		block0015._table.fillTable(res.contents.docFileWfList);

		if (block0015.isWorklist && block0015.isInputable) {
			$('#docFileWfInputArea').removeClass('hide');
		} else {
			$('#docFileWfInputArea').addClass('hide');
		}

	},
	modifyTR : function($tr, rowIndex, entity, labels, ignores) {
		let processId = entity.processId;
		$tr.find('textarea').prop('readonly', !block0015.isWorklist || !!processId);
		$tr.find('a.btn').toggleClass('hide', !block0015.isWorklist || (!block0015.isDeletable && !!processId));
	},
	/** イベント定義 */
	bindEvent : function() {
		$(document)
			.on('click', '#block0015 .collapse_btn', block0015.expand)
			.on('click', '#block0015 a.lnkDownloadDocFileWf', block0015.download)
			.on('click', '#updFileDoc', function() {
				$('#block0015').find('input[type=file]').click();
			})
			.on('click', 'button.btnRemoveDocFileWf, a.btnRemoveDocFileWf', block0015.remove);

		const selector = '#block0015_contents div.dragZone, #block0015_contents input[type=file]';
		FileUploader.setup(selector, "/vd0310/uploadWfDocFile", false, block0015.add);
	},

	/** 文書ファイルブロックを展開 */
	expand : function(ev) {
		if (!$(ev.target).hasClass('collapsed')
				&& (!vd0310.contents.docFileWfList
						|| !vd0310.contents.docFileWfList.length)) {
			const params = {
					corporationCode: vd0310.contents.corporationCode,
					processId: vd0310.contents.processId
			};
			NCI.get('/vd0310/getDocFile', params).done(function(docFileList) {
				if (docFileList && docFileList.length) {
					vd0310.contents.docFileWfList = docFileList;
				}
				block0015._table.fillTable(vd0310.contents.docFileWfList);
			});
		}
	},
	/** ダウンロード */
	download : function(ev) {
		const $tr = $(ev.target).closest('tr');
		const docFileWfId = $tr.find('[data-field=docFileWfId]').text();
		NCI.download('/vd0310/downloadWfDocFile?docFileWfId=' + docFileWfId);
	},
	/** 追加 */
	add : function(res) {
		if (res && res.success && res.docFileWfList && res.docFileWfList.length) {
			if (!vd0310.contents.docFileWfList) {
				vd0310.contents.docFileWfList = [];
			}
			const size = $('table.responsive tbody tr', $('#block0015_contents')).length + 1;
			$.each(res.docFileWfList, function(rowIndex, entity) {
				entity.rowNo = rowIndex + size;
				vd0310.contents.docFileWfList.push(entity);
				$('#block0015_contents').data('additionDocFileWfList').push(entity);
				block0015._table.addRowResult(entity);
			});
		}
	},
	/** 削除 */
	remove : function(ev) {
		const $tr = $(ev.target).closest('tr');
		const docFileWfId = $tr.find('[data-field=docFileWfId]').text();
		const processId = $tr.find('[data-field=processId]').text();
		const msg = NCI.getMessage('MSG0072', NCI.getMessage('docFile'));
		if (NCI.confirm(msg, function() {
			if (processId) {
				$('#block0015_contents').data('removeDocFileWfIdList').push(docFileWfId);
				block0015.removeRow(docFileWfId);
			} else {
				const params = {
					docFileWfId: docFileWfId
				};
				NCI.post('/vd0310/deleteWfDocFile', params).done(function(res) {
					if (res && res.success) {
						$.each(res.docFileWfList, function(rowIndex, entity) {
							block0015.removeRow(entity.docFileWfId);
						});
					}
				});
			}
		}));
	},
	/** 行削除 */
	removeRow : function(docFileWfId) {
		const result = [];
		let nowNo = 1;
		$.each(vd0310.contents.docFileWfList, function(rowIndex, entity) {
			if (docFileWfId != entity.docFileWfId) {
				entity.rowNo = nowNo++;
				entity.comments = $('table.responsive tbody tr', $('#block0015_contents')).filter(function(index) {
					return entity.docFileWfId == $(this).find('span[data-field=docFileWfId]').text();
				}).find('textarea').val()
				result.push(entity);
			}
		});
		vd0310.contents.docFileWfList = result;
		block0015._table.fillTable(vd0310.contents.docFileWfList);
	},
	getAdditionDocFileWfList: function() {
		const additionDocFileWfList = $('#block0015_contents').data('additionDocFileWfList') || [];
		$.each(additionDocFileWfList, function(i, e) {
			const docFileWfId = e.docFileWfId;
			const comments = $('table.responsive tbody tr', $('#block0015_contents')).filter(function(index) {
				return docFileWfId == $(this).find('span[data-field=docFileWfId]').text();
			}).find('textarea').val();
			e.comments = comments;
		});
		return additionDocFileWfList;
	},
	getRemoveDocFileWfIdList: function() {
		return $('#block0015_contents').data('removeDocFileWfIdList') || [];
	}
};

var blocks = {
	'0': block0000,		// アクション情報(1)
	'1': block0001,
	'2': block0002,
	'3': block0003,
	'5': block0005,
	'6': block0006,
	'7': block0007,
	'8': block0008,
	'9': block0009,
	'10': block0010,
	'11': block0011,
	'12': block0012,	// アクション情報(2)
	'13': block0013,	// アクションコメント
	'14': block0014,
	'15': block0015
};
