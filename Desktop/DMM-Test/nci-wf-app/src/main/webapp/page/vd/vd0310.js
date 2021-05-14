$(function() {
	const params = {
			corporationCode: NCI.getQueryString("corporationCode"),
			processId: NCI.getQueryString("processId"),
			copyProcessId : NCI.getQueryString("copyProcessId"),
			activityId: NCI.getQueryString("activityId"),
			screenProcessId: NCI.getQueryString("screenProcessId"),
			trayType: NCI.getQueryString("trayType"),
			proxyUser: NCI.getQueryString("proxyUser"),
			timestampUpdated: NCI.getQueryString("timestampUpdated"),
			messageCds: [
				'block0000', 'block0001', 'block0002', 'block0003', 'block0005', 'block0006', 'block0007',
				'block0008', 'block0009', 'block0010', 'block0011', 'block0014', 'processUserInfo',
				'startUserInfo', 'name', 'organization', 'post', 'proxyUser', 'step', 'approver',
				'proxyUser3', 'processing', 'executionDate', 'rowNo', 'fileName', 'comment', 'dragAndDrop',
				'btnFileUpload', 'MSG0068', 'assignRole', 'user', 'btnDelete', 'choice', 'btnAdd',
				'applicationNo', 'title', 'businessStatus', 'processDefName', 'applicationDate',
				'organizationNameProcess', 'userNameProxyProcess', 'btnReply', 'noRecord', 'processBbs',
				'btnRouteStatus', 'thisRow', 'attachFile', 'docFile', 'MSG0110', 'MSG0205',
				'MSG0247', 'MSG0248', 'MSG0249', 'MSG0250', 'MSG0251', 'MSG0252', 'MSG0253', 'MSG0254'
			],
			param1: NCI.getQueryString("param1"),
			param2: NCI.getQueryString("param2"),
			param3: NCI.getQueryString("param3")
	};

	NCI.init("/vd0310/init", params).done(function(res, textStatus, jqXHR) {
		if (res && res.success) {
			vd0310.init(res);
		}
	});

	// ポップアップ画面なら戻るボタンではなく閉じるボタンで、メニューがない
	// (DMM限定)新規の場合は戻るボタン／閉じるボタンを非表示
	let isPopup = Popup.isPopup();
	const isTrayTypeNEW = NCI.getQueryString("trayType") === 'NEW';
	$('button.btnBack').toggleClass('hide', isPopup || isTrayTypeNEW);
	$('button.btnClose').toggleClass('hide', !isPopup || isTrayTypeNEW);
	$('#nav').toggleClass('hide', isPopup);

	// 戻るボタン押下
	$('button.btnBack').on('click', function(ev) {
		TrayHelper.backToTray(params.trayType);
	});
	// 閉じるボタン押下
	$('button.btnClose').on('click', function() {
		// ここには関連文書ブロック→関連文書選択→リンク押下などから遷移してきているはず。
		Popup.close();
	});

	// 連続処理：前へ
	$('#btnMovePrev').on('click', function() {
		const t = vd0310.processList.getPrev();
		vd0310.processList.move(t, NCI.getQueryString("trayType"));
	});
	// 連続処理：先頭へ
	$('#btnMoveFirst').on('click', function(ev) {
		const t = vd0310.processList.getFirst();
		vd0310.processList.move(t, NCI.getQueryString("trayType"));
	});
	// 連続処理：次へ
	$('#btnMoveNext').on('click', function(ev) {
		const t = vd0310.processList.getNext();
		vd0310.processList.move(t, NCI.getQueryString("trayType"));
	});
	// 連続処理：末尾へ
	$('#btnMoveLast').on('click', function(ev) {
		const t = vd0310.processList.getLast();
		vd0310.processList.move(t, NCI.getQueryString("trayType"));
	});

	// 画面サイズ変更
	$(window).on('resize', function(ev) {
		// 全パーツの再描画を遅延実行予約
		NCI.doLater(PARTS.redraw, 300);
	});
});

var vd0310 = {
	contents: null,

	processList : null,

	/**
	 * 初期処理
	 */
	init: function(res) {
		vd0310.contents = res.contents;
		let trayType = vd0310.contents.trayType;

		$('#screenProcessName').text(vd0310.contents.screenProcessName);
		$('#activityDefName').text(vd0310.contents.activityDefName);
		if (vd0310.contents.businessProcessStatusName) {
		$('#businessProcessStatusName').text('【' + vd0310.contents.businessProcessStatusName + '】');
		}
		// 自案件／汎用案件でポップアップでないこと（ポップアップ＝決裁関連文書から参照されているとみなす）
		// 【DMM固有】特定の申請のみコピー起票が可能である
		// ログイン者の会社コード＝申請書の会社コードであること
		const copyableScreenProcessCodes = [
			  '0000000053'	// 新規_発注申請
			, '0000000058'	// 購入依頼
			, '0000000061'	// 新規_発注申請（発注予約）
			, '0000000070'	// 新規_法務依頼・契約申請
			, '0000000077'	// 新規_前払申請
			, '0000000079'	// 新規_発注申請（集中購買）
			, '0000000081'	// 新規_支払申請（経費）
		];
		const canCopy = (trayType === 'ALL' || trayType === 'OWN')
				&& !Popup.isPopup()
				&& copyableScreenProcessCodes.indexOf(vd0310.contents.screenProcessCode) >= 0
				&& NCI.loginInfo.corporationCode === res.contents.corporationCode;
		$('button.btnCopyDoc')
			.toggleClass('hide', !canCopy)
			.click(vd0310.copyDoc);

		// 外部Javascript読み込み
		// ⇒parts.js の block0002.init() で外部Javascriptを読み込むよう変更;
		// カスタムCSSスタイル
		$('#customCssStyleTag').html(vd0310.contents.customCssStyleTag);

		const $breadcrumbs = $('ul.breadcrumbs').empty();
		$.each(vd0310.contents.blockList, function(rowIndex, entity) {
			if (entity.displayFlag === '1') {
				const blockId = 'block' + ('0000' + entity.blockId).slice(-4);

				// パンくずリスト作成（アクション情報とアクションコメントは不要）
				if (entity.blockId != 0 && entity.blockId != 12 && entity.blockId != 13) {
					$breadcrumbs.each(function(i, elem) {
						const $breadcrumb = $(elem);
						if ($breadcrumb.hasClass('forPC'))
							$breadcrumb.append('<li><a href="#' + blockId + '"><i class="fa fa-chevron-down"></i>' + entity.blockName + '</a></li>');
						else
							$breadcrumb.append('<li><a href="#' + blockId + '">' + entity.blockName + '</a></li>')
					});
				}

				// ブロック情報作成
				$('#' + blockId).remove();
				const $block = $(document.createElement('div'));
				$block.attr({
					'id' : blockId,
					'class' : 'form-group'
				})
				.load('./include/' + blockId + '.html', function() {
					$('span[data-field=blockName]', $block).text(entity.blockName);

					// ブロック内のコンテンツは遅延実行されているので、多言語リソースをここで設定してやる
					const $i18n = $('[data-i18n]', $block);
					$i18n.each(function(i, elem) {
						const key = $i18n.eq(i).data('i18n');
						if (key != null && key !== '') {
							const value = NCI.getMessage(key);
							if (value != null) {
								$(this).text(value);
							}
						}
					});

					// ブロック初期化（blocks はblock.jsの中で定義している）
					const block = blocks[entity.blockId];
					if (block && block.init) {
						block.init(res);
					}
					// ブロックの開閉状態
					if (block.wasOpened === false) {
						// 保存処理前にブロックが閉じられていたため、ブロックを開く処理をしない
					} else if (block.wasOpened === true || entity.expansionFlag === '1') {
						// 新しく申請画面を開いたら、ブロック表示順の定義にしたがってブロックを開く
						// または保存処理前にブロックが開かれていたら、ブロックを開く
						$('#' + blockId + '_expansion').trigger('click');
					}
				})
				.appendTo('#blocks');
			}
		});

		// 連続処理用の初期化
		const targets = NCI.flushScope(CONTINUOUS_PROCESSING);
		vd0310.processList = new ProcessList(
				NCI.getQueryString("corporationCode"),
				NCI.getQueryString("processId"),
				NCI.getQueryString("activityId"),
				targets);
		$('#continuousProcessing').toggleClass('hide', !targets);
		$('#btnMovePrev, #btnMoveFirst').prop('disabled', !vd0310.processList.hasPrev());
		$('#btnMoveNext, #btnMoveLast').prop('disabled', !vd0310.processList.hasNext());
		$('#processPositionInfo').text(vd0310.processList.getPositionInfo());
	},
	/**
	 *
	 */
	execute: function(action, sendback, pullback) {
		// 差戻し時はコメント必須
		const isSendback = ActionType.SENDBACK === action.actionType;
		const $actionComment = $('#actionComment').toggleClass('required', isSendback);

		// バリデーション：次遷移は必須入力あり
		const required = action.actionType === ActionType.NORMAL;
		if (!PARTS.validateParts(required, action, vd0310.contents.submitFunctions, $(document))) {
			return false;
		}
		if (!Validator.validate($actionComment, isSendback)) {
			return false;
		}

		let params = {
			actionInfo: action,
			contents : vd0310.contents,
			startUserInfo: $('#startUserInfo').data('startUserInfo') || vd0310.contents.startUserInfo,
			additionAttachFileWfList: block0003.getAdditionAttachFileWfList() || [],
			removeAttachFileWfIdList: block0003.getRemoveAttachFileWfIdList() || [],
			additionInformationSharerList: $('#informationSharerList').data('additionInformationSharerList') || [],
			removeInformationSharerList: $('#informationSharerList').data('removeInformationSharerList') || [],
			approvalRelationList : block0005.toApprovalRelationList() || vd0310.contents.approvalRelationList,
			runtimeMap : PARTS.fillRuntimeMap(),
			actionComment : $('#actionComment').val() || '',
			additionDocFileWfList: block0015.getAdditionDocFileWfList() || [],
			removeDocFileWfIdList: block0015.getRemoveDocFileWfIdList() || [],
		};

		// 次遷移時のみ承認ルート設定画面を呼び出す
		// ただし「承認者設定画面を利用する」がTrueの場合だけ
		// Falseとなっていれば設定画面はスキップ
		if (action.actionType === ActionType.NORMAL && vd0310.contents.useApproversSettingScreen) {
			// 申請・承認等の状態遷移
			Popup.open("../vd/vd0312.html", callbackFromVd0312, params);
		} else if (action.actionType === ActionType.DOACTION) {
			// アクション機能の実行（＝帳票のダウンロードを行う前提である）
			const timeout = 60 * 10 * 1000;	// タイムアウト時間(ミリ秒)
			NCI.download('/vd0310/doDownload', params, timeout).done(function(res) {
				if (res && res.success) {
					afterExecute(params, res);
				}
			});
		} else {
			if (action.actionType == ActionType.SENDBACK || action.actionType == ActionType.SENDBACK_NC)
				params.sendbackInfo = sendback;
			if (action.actionType == ActionType.PULLBACK)
				params.pullbackInfo = pullback;
			if (action.actionType == ActionType.PULLFORWARD)
				params.pullforwardInfo = vd0310.contents.pullforwardInfo;
			const msg = vd0310.getConfirmMsg(action);
			NCI.confirm(msg, function() {
				NCI.post("/vd0310/execute", params).done(function(res) {
					if (res && res.success) {
						afterExecute(params, res);
					}
				});
			});
		}
	},

	/** ボタン毎の確認メッセージ */
	getConfirmMsg : function(action) {
		switch (action.actionType) {
		case ActionType.SAVE:			// 仮保存は確認不要
			return null;
		case ActionType.NORMAL:			// 通常（申請／承認／確認／決裁など）
			switch (action.actionCode) {
			case '2':	// 承認
				return NCI.getMessage('MSG0248');
			case '8':	// 確認
				return NCI.getMessage('MSG0249');
			default:	// 申請、開始/終了、その他
				return NCI.getMessage('MSG0247');
			}
		case ActionType.PULLBACK:		// 引戻し
			return NCI.getMessage('MSG0250');
		case ActionType.SENDBACK:		// 差戻し
		case ActionType.SENDBACK_NC:
			return NCI.getMessage('MSG0251');
		case ActionType.CANCEL:			// 取消（破棄／却下など）
			return NCI.getMessage('MSG0252');
		case ActionType.PULLFORWARD:	// 引上げ
			return NCI.getMessage('MSG0253');
		default:
			return null;				// その他は確認メッセージなし
		}
	},

	/** コピー起票 */
	copyDoc : function(ev) {
		// 既に別会社のユーザに変更になっていないかチェックし、チェックOKならコピー起票を実行する
		NCI.post("/vd0310/canCopy").done(function(res) {
			if (res && res.success) {
				NCI.redirect('../vd/vd0310.html' +
						'?screenProcessId=' + vd0310.contents.screenProcessId +
						'&trayType=NEW' +
						'&copyProcessId=' + vd0310.contents.processId);
			}
		});
	},

	/** 起案担当者の変更可否判定 */
	verifyChangeStartUser : function() {
		let canChange = true;
		$('#btnChangeStartUser').prop('disabled', !canChange);
		return canChange;
	}
};

/** ユーザ選択画面からのコールバック関数 */
function callbackFromVd0312(params) {
	if (params) {
		NCI.post("/vd0310/execute", params).done(function(res) {
			if (res && res.success) {
				afterExecute(params, res);
			}
		});
		return false;
	}
}

/** アクションボタン押下後の処理 */
function afterExecute(params, res) {
	if (!res.errors || res.errors.length == 0) {
		if (params.actionInfo.actionType === ActionType.SAVE) {
			// 仮保存なら再表示
			// 連続処理用のプロセス一覧をフラッシュスコープに退避
			NCI.flushScope(CONTINUOUS_PROCESSING, vd0310.processList.targets);
			// ブロックの開閉状態を記録
			saveBlockOpenStatus();
			vd0310.init(res);
		} else if (params.actionInfo.actionType === ActionType.DOACTION) {
			// アクション機能実行ならこれ以上なにもしない
			return;
		}
		else {
			// 正常なら処理結果画面へ
			// Redmine#149974対応
			// 上記対応に伴い完了画面への遷移処理はコメントアウト
//			let argument = {
//				corporationCode: NCI.getQueryString("corporationCode"),
//				processId: NCI.getQueryString("processId"),
//				copyProcessId : NCI.getQueryString("copyProcessId"),
//				activityId: NCI.getQueryString("activityId"),
//				screenProcessId: NCI.getQueryString("screenProcessId"),
//				trayType: NCI.getQueryString("trayType"),
//				proxyUser: NCI.getQueryString("proxyUser"),
//				timestampUpdated: NCI.getQueryString("timestampUpdated"),
//				subject: res.subject,
//				applicationNo: res.applicationNo,
//				approvalNo: res.approvalNo,
//				trayType: params.contents.trayType,
//				processTargets : vd0310.processList.targets
//			};
//			NCI.flushScope('argument', argument);
//			NCI.redirect("../vd/vd0311.html");
			// 連続処理中の場合、未処理のデータがあるか？あればそのデータを表示
			if (vd0310.processList.hasNext() || vd0310.processList.hasPrev()) {
				const next = vd0310.processList.removeCurrent();
				if (next) {
					redirectVd0310(next.corporationCode, next.processId, next.activityId,
							next.timestampUpdated, next.proxyUser, next.trayType, vd0310.processList.targets);
					return;
				}
			}
			// 上記以外であれば元の画面へ戻る
			TrayHelper.backToTray(NCI.getQueryString("trayType"));
		}
	}
	else {
		// サーバ側バリデーション結果を反映
		PARTS.displayValidationResult(res.errors, res.html, res.runtimeMap);
	}
}

/** ブロックの開閉状態を記録 */
function saveBlockOpenStatus() {
	for (let blockId in blocks) {
		const block = blocks[blockId];
		if (blockId.length === 1)
			block.wasOpened = !($('#block000' + blockId + ' .collapse_btn').hasClass('collapsed'));
		else if (blockId.length === 2)
			block.wasOpened = !($('#block00' + blockId + ' .collapse_btn').hasClass('collapsed'));
	}
}