$(function() {
	var params = {
			corporationCode: NCI.getQueryString("corporationCode"),
			docId: NCI.getQueryString("docId"),
			copyDocId : NCI.getQueryString("copyDocId"),
			screenDocId: NCI.getQueryString("screenDocId"),
			messageCds: [
				'block0000', 'block0001', 'block0002', 'block0003', 'block0005', 'block0006', 'block0007',
				'block0008', 'block0009', 'block0010', 'block0011', 'block0014',
				'btnBack', 'btnClose', 'btnRegister', 'btnUpdate', 'btnCopy', 'btnLockAndEdit', 'btnUnlock',
				'btnExecute', 'btnCancel', 'btnAdd', 'btnDelete', 'btnNewMemo', 'btnFileUpload', 'btnWfApply',
				'confirmEdit', 'unlock', 'lockNotRelease', 'lockRelease', 'versionControl', 'editing',
				'bizDocName', 'comment', 'impressions', 'folder', 'documentOwner', 'publicClassification',
				'publish', 'unpublish', 'pupblicDate', 'registTimestamp', 'register', 'updateTimestamp', 'updater',
				'roleName', 'refer', 'download', 'edit', 'delete', 'copy', 'move', 'noRecord', 'fileName', 'version', 'processing',
				'rowNo', 'unableEdit', 'editOrRefer', 'dragAndDrop', 'activityEndType', 'updateContents', 'inputValue', 'metaName',
				'retentionTerm', 'year', 'months', 'noTerm', 'withTerm', 'attachFile', 'binderName', 'documentType', 'businessStatus',
				'applicationNo', 'organizationNameProcess', 'userNameProxyProcess', 'applicationDate', 'title',
				'MSG0068', 'MSG0213', 'MSG0255', 'MSG0256', 'cooperateWithWorkflow'
			]
	};

	NCI.init("/dc0100/init", params).done(function(res, textStatus, jqXHR) {
		if (res && res.success) {
			dc0100.init(res);
		}
	});

	// 画面サイズ変更
	$(window).on('resize', function(ev) {
		// 全パーツの再描画を遅延実行予約
		NCI.doLater(PARTS.redraw, 300);
	});
});

// DC0100の画面だがWF側に合わせて変数名を「vd0310」にしておく
var vd0310 = {
	contents: null,
};

var dc0100 = {
	/**
	 * 初期処理
	 */
	init: function(res) {
		vd0310.contents = res.contents;
		let trayType = vd0310.contents.trayType;

		$('#screenDocName').text(vd0310.contents.screenDocName);

		// 外部Javascript読み込み
		// ⇒parts.js の block0002.init() で外部Javascriptを読み込むよう変更;
		// カスタムCSSスタイル
		$('#customCssStyleTag').html(vd0310.contents.customCssStyleTag);

		const $breadcrumbs = $('ul.breadcrumbs').empty();
		$.each(vd0310.contents.blockList, function(rowIndex, entity) {
			if (entity.displayFlag === '1') {
				const blockId = 'docBlock' + ('0000' + entity.blockId).slice(-4);
				const blockName = 'block' + blockId;

				// パンくずリスト作成
				if (entity.blockId != 0) {
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

					// ブロック初期化（blocks はdocBlock.jsの中で定義している）
					const block = blocks[entity.blockId];
					if (block && block.init) {
						block.init(res);
					}
					// ブロック展開
					if (entity.expansionFlag === '1') {
						$('#' + blockId + '_expansion').trigger('click');
					}
				})
				.appendTo('#blocks');
			}
		});
	},

	/**
	 * 文書情報の登録／更新処理実行.
	 */
	execute: function() {
		// 文書情報は「業務文書ブロック」または「バインダーブロック」と「文書属性ブロック」からかき集める
		const bizDocInfo = block0001.getBizDocInfo();
		const binderInfo = block0009.getBinderInfo();
		const attribute  = block0003.getDocAttribute();
		const docInfo = {};
		$.each( bizDocInfo, function( key, value ) {
			docInfo[key] = value;
	    });
		$.each( binderInfo, function( key, value ) {
			docInfo[key] = value;
	    });
		$.each( attribute, function( key, value ) {
			docInfo[key] = value;
	    });

		let params = {
			contents : vd0310.contents,
			docInfo : docInfo,
			versionInfo : block0000.getVersionInfo(),
//			bizDocInfo : block0001.getBizDocInfo(),
//			attribute : block0003.getDocAttribute(),
			accessibles : block0004.getAccessibles() || [],
			attributeExs : block0005.getAttributeEx() || [],
			docFiles : block0006.getDocFiles() || [],
			attachFileDocs : block0010.getAttachFiles() || [],
			runtimeMap : PARTS.fillRuntimeMap()
		};

		NCI.post("/dc0100/execute", params).done(function(res) {
			if (res && res.success) {
				afterExecute(params, res);
			}
		});
	},

	/**
	 * 文書情報からWF申請処理実行.
	 */
	applyWf: function() {
		// 業務文書情報は「業務文書ブロック」と「文書属性ブロック」の2つからかき集める
		let bizDocInfo = block0001.getBizDocInfo();
		let attribute  = block0003.getDocAttribute();
		let docInfo = {};
		$.each( bizDocInfo, function( key, value ) {
			docInfo[key] = value;
	    });
		$.each( attribute, function( key, value ) {
			docInfo[key] = value;
	    });

		let params = {
			contents : vd0310.contents,
			docInfo : docInfo,
			versionInfo : block0000.getVersionInfo(),
//			bizDocInfo : block0001.getBizDocInfo(),
//			attribute : block0003.getDocAttribute(),
			accessibles : block0004.getAccessibles() || [],
			attributeExs : block0005.getAttributeEx() || [],
			docFiles : block0006.getDocFiles() || [],
			attachFileDocs : block0010.getAttachFiles() || [],
			runtimeMap : PARTS.fillRuntimeMap()
		};

		NCI.post("/dc0100/applyWf", params).done(function(res) {
			if (res && res.success) {
				afterExecute(params, res);
			}
		});
	},

	/** ボタン毎の確認メッセージ */
	getConfirmMsg : function(action) {
		// 仮保存は確認不要
		if (action.actionType == ActionType.SAVE)
			return null;

		// とりあえず actionName で確認メッセージをでっち上げておく
		// 厳密な確認メッセージにしたければ、actionCodeごとに個別で定義するべし
		return NCI.getMessage('MSG0068', action.actionName);
	},

	/** コピー作成 */
	copyDoc : function(ev) {
		NCI.redirect('../dc/dc0100.html' +
				'?screenProcessId=' + vd0310.contents.screenDocId +
				'&trayType=NEW' +
				'&copyDocId=' + vd0310.contents.docId);
	}
};

/** アクションボタン押下後の処理 */
function afterExecute(params, res) {
	if (!res.errors || res.errors.length == 0) {
		// エラーなしの場合は登録／更新内容で画面情報を再描画
		dc0100.init(res);
	}
	else {
		// サーバ側バリデーション結果を反映
		PARTS.displayValidationResult(res.errors, res.html, res.runtimeMap);
	}
}