/** 入力可能なトレイタイプの配列 */
const INPUTABLE_TRAY_TYPES = ["NEW", "WORKLIST"];

$(function() {
	// ボタンブロックのイベント定義
	block0000.bindEvent();
	// 業務文書ブロックのイベント定義
	block0001.bindEvent();
	// 文書内容ブロックのイベント定義
	block0002.bindEvent();
	// 文書属性ブロックのイベント定義
	block0003.bindEvent();
	// 権限設定ブロックのイベント定義
	block0004.bindEvent();
	// 文書属性(拡張)ブロックのイベント定義
	block0005.bindEvent();
	// 文書ファイルブロックのイベント定義
	block0006.bindEvent();
	// 更新履歴ブロックのイベント定義
	block0007.bindEvent();
	// メモ情報ブロックのイベント定義
	block0008.bindEvent();
	// バインダー情報ブロックのイベント定義
	block0009.bindEvent();
	// 添付ファイルブロック(バージョン管理しない)のイベント定義
	block0010.bindEvent();
	// WF連携ブロックのイベント定義
	block0011.bindEvent();
});

/** ボタンブロック */
var block0000 = {
	/** イベントのバインドが終わっているか */
	boundEvent : false,

	/**
	 * 初期処理
	 */
	init: function(res) {
		// 版管理の更新区分の選択肢
		let $updateVersionType = $('#updateVersionType');
		NCI.createOptionTags($updateVersionType, res.contents.updateVersionTypes);
		$updateVersionType.prop('selectedIndex', 0);

		// ポップアップ画面なら戻るボタンではなく閉じるボタンで、メニューがない
		const isPopup = Popup.isPopup();
		$('#btnBack').toggleClass('hide', isPopup);
		$('#btnClose').toggleClass('hide', !isPopup);
		$('#nav').toggleClass('hide', isPopup);

		// 各種ボタンの表示／非表示設定
		const isNew = res.contents.trayType === 'NEW';					//新規登録か
		const isLock = res.contents.docInfo.lockFlag === '1';			//ロック中か
		const isOwnLock = res.contents.docInfo.ownLockFlag === '1';		//自身がロックしているか
		const isCopy = res.contents.docInfo.authCopy === '1';			//コピー権限があるか
		const isEdit = res.contents.docInfo.authEdit === '1';			//編集権限があるか
		const isWfApplying = res.contents.docInfo.wfApplying === '1';	//WF連携中か
		const isWfCooperable = (res.contents.docInfo.contentsType === '1' && res.contents.bizDocInfo.screenProcessCode != null);	//WF連携可能か
		$('#btnRegist').toggleClass('hide', !isNew);
		$('#btnUpdate').toggleClass('hide', !isOwnLock || isWfApplying);
		$('#btnCopy').toggleClass('hide', !(!isNew && !isOwnLock && isCopy && !isPopup));
		$('#btnLock').toggleClass('hide', isNew || isLock || isOwnLock || !isEdit || isWfApplying);
		$('#btnUnlock').toggleClass('hide', isNew || !isOwnLock);
		$('#btnWfApply').toggleClass('hide', !isWfCooperable || isNew || !isOwnLock || isWfApplying);

		// データを設定
		let $target = $('#confirmEdit');
		NCI.toElementsFromObj(res.contents.docInfo, $target);
		// 版管理の表示／非表示を切り替え
		$('div.regist', $target).toggleClass('hide', !isNew);
		$('div.update', $target).toggleClass('hide', isNew);

		// 誰かがロックしていたら下記を設定
		if (isLock) {
			let $lockInfo =  $('#lockInfo');
			$lockInfo.toggleClass('hide', false);
			NCI.toElementsFromObj(res.contents.docInfo, $lockInfo);
		}

		block0000.boundEvent = true;
	},

	/** イベント定義 */
	bindEvent : function() {
		$(document)
			.on('click', '#btnBack'   , block0000.doBack)
			.on('click', '#btnClose'  , Popup.close)
			.on('click', '#btnRegist' , block0000.doRegist)
			.on('click', '#btnUpdate' , block0000.doUpdate)
			.on('click', '#btnWfApply', block0000.doWfApply)
			.on('click', '#btnExecute', block0000.doExecute)
			.on('click', '#btnCancel' , block0000.doCancel)
			.on('click', '#btnLock'   , block0000.doLockAndEdit)
			.on('click', '#btnUnlock' , block0000.doUnlock)
			.on('click', '#btnCopy'   , block0000.doCopy);
	},

	doBack : function(ev) {
		if (vd0310.contents.trayType === 'NEW') {
			NCI.redirect('../dc/dc0030.html');
		} else {
			NCI.redirect('../dc/dc0020.html');
		}
	},

	doRegist : function() {
		// 各ブロック毎にバリデーション処理をコール
		var error = false;
		$.each(vd0310.contents.blockList, function(rowIndex, entity) {
			if (entity.displayFlag === '1') {
				// ブロック初期化（blocks はdocBlock.jsの中で定義している）
				const block = blocks[entity.blockId];
				if (block && block.validate) {
					if (!block.validate()) {
						error = true;
					}
				}
			}
		});
		if (!error) {
			$('#confirmEdit').modal();
		}
	},

	doUpdate : function() {
		// 各ブロック毎にバリデーション処理をコール
		var error = false;
		$.each(vd0310.contents.blockList, function(rowIndex, entity) {
			if (entity.displayFlag === '1') {
				// ブロック初期化（blocks はdocBlock.jsの中で定義している）
				const block = blocks[entity.blockId];
				if (block && block.validate) {
					if (!block.validate()) {
						error = true;
					}
				}
			}
		});
		if (!error) {
			$('#confirmEdit').modal();
		}
	},

	doWfApply : function() {
		const attention1 = NCI.getMessage('MSG0255');
		const attention2 = NCI.getMessage('MSG0256');
		const msg = NCI.getMessage('MSG0068', NCI.getMessage('cooperateWithWorkflow')) + '\r\n' + attention1 + '\r\n' + attention2;
		if (NCI.confirm(msg, function() {
			dc0100.applyWf();
		}));
	},

	doLockAndEdit : function() {
		let params = {
			docId : vd0310.contents.docId,
			version : vd0310.contents.version,
			corporationCode : vd0310.contents.corporationCode
		};
		NCI.post("/dc0100/lock", params).done(function(res) {
			if (res && res.success) {
				afterExecute(params, res);
			}
		});
	},

	doUnlock : function() {
		let params = {
			docId : vd0310.contents.docId,
			version : vd0310.contents.version,
			corporationCode : vd0310.contents.corporationCode
		};
		NCI.post("/dc0100/unlock", params).done(function(res) {
			if (res && res.success) {
				afterExecute(params, res);
			}
		});
	},

	doExecute : function() {
		let $root = $('#confirmEdit')
		let $targets = $root.find('input, select, textarea');
		if (!Validator.validate($targets, true)) {
			return false;
		}
		$('#confirmEdit').modal('hide');
		dc0100.execute();
	},

	doCopy : function() {
		NCI.redirect('../dc/dc0100.html' +
				'?screenDocId=' + vd0310.contents.screenDocId +
				'&copyDocId=' + vd0310.contents.docId +
				'&corporationCode=' + vd0310.contents.corporationCode);
	},

	doCancel : function() {
		$('#confirmEdit').modal('hide');
	},

	getVersionInfo : function() {
		let versionInfo = NCI.toObjFromElements($('#confirmEdit'), []);
		return versionInfo;
	}
};

/** 業務文書ブロック */
var block0001 = {
	init: function(res) {
		// 入力可能なトレイモードか？
		let isInputable = (INPUTABLE_TRAY_TYPES.indexOf(res.contents.trayType) > -1);

		let isNew = res.contents.trayType === 'NEW';
		let isOwnLock = res.contents.docInfo.ownLockFlag === '1';
		// 新規登録／更新時と参照時で見せるエレメントを切り替える
		if (isInputable) {
			$('#title').toggleClass('hide', false);
			$('#comments').toggleClass('hide', false);
		} else {
			$('span[data-field=title]').toggleClass('hide', false);
			$('span[data-field=comments]').toggleClass('hide', false);
			// 参照時はコメントの改行を<BR>タグで置換しておく
			res.contents.docInfo.comments = NCI.escapeHtmlBR(res.contents.docInfo.comments);
		}
		// データを設定
		NCI.toElementsFromObj(res.contents.docInfo, $('#docBlock0001_contents'));
	},

	/** イベント定義 */
	bindEvent : function() {
		// 特に必要なし
	},

	/** バリデーション */
	validate: function() {
		let $targets = $('#docBlock0001_contents').find('input, select, textarea');
		if (!Validator.validate($targets, true)) {
			return false;
		}
		return true;
	},

	/** 業務文書情報取得 */
	getBizDocInfo : function() {
		let bizDocInfo = NCI.toObjFromElements($('#docBlock0001_contents'), []);
		return bizDocInfo;
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
			$('#outside-javascript').attr('src', outsideURL).load(function() {
				// 外部Javascriptの読み込み後にLoad時の呼び出し関数をコール
				const loadFunctions = res.contents.loadFunctions;
				if (loadFunctions && loadFunctions.length) {
					for (let i = 0; i < loadFunctions.length; i++) {
						const funcName = loadFunctions[i].funcName;
						const param = loadFunctions[i].param;
						PARTS.execFunction(funcName, param);
					}
				}
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
	},

	/** バリデーション */
	validate: function() {
		// バリデーション：公開するのであれば必須として処理する
		// TODO ひとまず仮実装
		let required = true;
		if (!PARTS.validateParts(required, null, vd0310.contents.submitFunctions)) {
			return false;
		}
		return true;
	}
};

/** 文書属性ブロック */
var block0003 = {
	init: function(res) {
		// 入力可能なトレイモードか？
		let isInputable = (INPUTABLE_TRAY_TYPES.indexOf(res.contents.trayType) > -1);

		NCI.ymdPicker($('input.ymdPicker', '#docBlock0003_contents'));

		// データを設定
		NCI.toElementsFromObj(res.contents.docInfo, $('#docBlock0003_contents'));
		// 新規登録／更新時とそれ以外で見せるエレメントを切り替える
		if (isInputable) {
			$('#divFolderInfo').toggleClass('hide', false);
			$('#divOwnerUser').toggleClass('hide', false);
			$('#divPublishFlag').toggleClass('hide', false);
			$('#publicDate').toggleClass('hide', false);
			$('#divRetentionTerm').toggleClass('hide', false);
			block0003.changeRetentionTerm();
		} else {
			$('span[data-field=folderPath]').toggleClass('hide', false);
			$('span[data-field=ownerUserName]').toggleClass('hide', false);
			$('#publishFlagName').toggleClass('hide', false);
			$('#dispPublishDate').toggleClass('hide', false);
			$('#dispRetentionTerm').toggleClass('hide', false);
			// 保存期間区分が無期限の場合、期間は表示しない
			$('#dispRetentionTermYearMonth').toggleClass('hide', (res.contents.docInfo.retentionTermType === '0'));
		}


	},

	/** イベント定義 */
	bindEvent : function() {
		$(document)
			.on('change', 'input[type=radio][name=rdoPublishFlag]', block0003.changePublishFlag)
			.on('click',  '#btnSelectDocFolder', block0003.openCm0200)
			.on('click',  '#btnClearDocFolder', block0003.clearFolder)
			.on('click',  '#btnChangeOwnerUser', block0003.openCm0050)
			.on('click',  '#btnClearOwnerUser', block0003.clearOwner)
			.on('change', 'input[type=radio][name=rdoRetentionTermType]', block0003.changeRetentionTerm);
	},

	/** バリデーション */
	validate: function() {
		let $root = $('#docBlock0003_contents')
		let $targets = $root.find('input, select, textarea');
		if (!Validator.validate($targets, true)) {
			return false;
		}
		return true;
	},

	/**
	 * 文書フォルダ変更時処理.
	 * 権限設定ブロックの設定されてある権限を選択した文書フォルダが持つ権限情報にて置き換える
	 */
	changeFolder : function() {
		let $folders = $('#docFolderId');
		let docFolderId = $folders.val();
		if (docFolderId != '') {
			let params = {corporationCode : vd0310.contents.corporationCode, docFolderId : docFolderId};
			NCI.post("/dc0100/getAccessibles", params).done(function(res) {
				if (res && res.success) {
					block0004._table.fillTable(res.accessibles);
					// 設定状況に応じてチェックボックスの活性／非活性を切り替える
					block0004.switchingAuth();
					// フォルダに紐づく文書属性(拡張)のメタ項目一覧があれば切り替える
					if (res.attributeExs) {
						$('#metaTemplateId').val(res.folder.metaTemplateId);
						block0005._table.fillTable(res.attributeExs);
					}
				}
			});
		}
	},

	/**
	 * 公開区分変更時処理.
	 * 公開であれば公開日付は必須
	 */
	changePublishFlag : function(ev) {
		const isPublish = ($('input[type=radio][name=rdoPublishFlag]:checked').val() === '1');
		$('#spanPupblicDate, #publishStartDate, #publishEndDate').toggleClass('required', isPublish);
	},

	/** 文書フォルダ選択 */
	openCm0200 : function(ev) {
		const params = {"corporationCode" : vd0310.contents.corporationCode};
		const url = '../cm/cm0200.html';
		Popup.open(url, block0003.callbackFromCm0200, params);
	},

	/** 文書フォルダクリア */
	clearFolder : function(ev) {
		const $div = $('#divFolderInfo');
		$div.find('input').val('');
	},

	/** 文書フォルダ選択画面からのコールバック */
	callbackFromCm0200 : function(result) {
		if (result) {
			$('#folderPath').val(result.folderPath);
			let $docFolderId = $('#docFolderId');
			// 文書フォルダが変更になったか
			const isChangeFolder = ($docFolderId.val() !== result.docFolderId);
			$docFolderId.val(result.docFolderId);
			// 文書フォルダが変更になった場合、文書フォルダ変更時処理をコール
			if (isChangeFolder) {
				block0003.changeFolder();
			}
		}
	},

	/** 文書責任者のユーザ選択 */
	openCm0050 : function(ev) {
		Popup.open("../cm/cm0050.html", block0003.callbackFromCm0050);
	},

	/** 文書責任者のクリア */
	clearOwner : function(ev) {
		const $div = $('#divOwnerUser');
		$div.find('input').val('');
	},

	/** ユーザ選択画面からのコールバック */
	callbackFromCm0050 : function(ub) {
		if (ub) {
			$('#ownerCorporationCode').val(ub.corporationCode);
			$('#ownerCorporationName').val(ub.corporationName);
			$('#ownerUserCode').val(ub.userCode);
			$('#ownerUserName').val(ub.userName);
		}
	},

	/** 業務文書情報取得 */
	getDocAttribute : function() {
		let attribute = NCI.toObjFromElements($('#docBlock0003_contents'), ['divPublishFlag', 'dispPublishDate', 'publicDate', 'userName', 'dispRetentionTerm', 'dispRetentionTermYearMonth', 'spanPupblicDate']);
		return attribute;
	},

	/** 保存期間区分変更時処理. */
	changeRetentionTerm : function(ev) {
		const retentionTermType = $('input[type=radio][name=rdoRetentionTermType]:checked').val();
		const isWithTerm = (retentionTermType === '1');
		$('#retentionTermYear, #retentionTermMonths').prop('disabled', !isWithTerm).toggleClass('required', isWithTerm);
		if (!isWithTerm) {
			$('#retentionTermYear, #retentionTermMonths').val('');
		}
	}
};

/** 権限設定ブロック */
var block0004 = {
	_table : null,

	init: function(res) {
		// 入力可能なトレイモードか？
		const isInputable = (INPUTABLE_TRAY_TYPES.indexOf(res.contents.trayType) > -1);
		// ロール選択ブロックの表示／非表示を切り替え
		$('#accessibleInfo').toggleClass('hide', !isInputable);
		// 一覧のチェックボックスの表示／非表示を切り替え
		let $table = $('#accessibles');
		$('input[type=checkbox].selectable', $table).toggleClass('hide', !isInputable);
		$('input[type=checkbox].inputable', $table).prop('disabled', !isInputable);

//		// 権限設定用ロールの選択肢
//		let $roles = $('#roles');
//		NCI.createOptionTags($roles, res.contents.roles);
//		$roles.prop('selectedIndex', 0);

		// 権限情報を展開
		block0004._table = new ResponsiveTable($('#docBlock0004_contents'));
		block0004._table.modifyTR = block0004.modifyTR;
		block0004._table.fillTable(res.contents.accessibles);
		// 入力可能な場合、設定状況に応じてチェックボックスの活性／非活性を切り替える
		if (isInputable) {
			block0004.switchingAuth();
		}
	},

	/** 入力タインプごとにテンプレートを書き変えるメソッド（ResponsiveTableのデフォルト実装を上書きする想定）  */
	modifyTR : function($tr, rowIndex, entity, labels, ignores) {
		$tr.find('span[data-field=roleName]').toggleClass('hide', (entity.roleCode == null));
		$tr.find('span[data-field=userName]').toggleClass('hide', (entity.userCode == null));
	},

	/** イベント定義 */
	bindEvent : function() {
		$(document)
		.on('click', '#btnAddRole', block0004.addRole)
		.on('click', '#btnDelRole', block0004.delRole)
		.on('click', 'input[type=checkbox][data-field=authRefer]', function() {
			block0004.changeAuthRefer($(this));
		})
		.on('click', 'input[type=checkbox][data-field=authEdit]', function() {
			block0004.changeAuthEdit($(this));
		});
	},

	/** バリデーション */
	validate: function() {
		let $table = $('#accessibles');
		let error = false;
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
	},

	/** 権限ロール追加 */
	addRole : function(ev) {
		const params = {"corporationCode": vd0310.contents.corporationCode, "docFlag": "1"};
		const url = "../cm/cm0090.html";
		Popup.open(url, block0004.callbackFromCm0090, params);
//		let code = $('#roles').val();
//		// codeは「会社コード|ロールコード」の形式になっているので分割しておく
//		let arrays = code.split('|');
//		let corporationCode = arrays[0];
//		let roleCode = arrays[1];
//		let exist = false;
//		$('tbody>tr',  $('#accessibles')).each(function(i, tr) {
//			if (roleCode === $('span[data-field=roleCode]', $(tr)).text()) {
//				exist = true;
//				return false;
//			}
//		});
//		if (!exist) {
//			let name = $('#roles option:selected').text();
//			let obj = {corporationCode:corporationCode, roleCode:roleCode, roleName:name};
//			block0004._table.addRowResult(obj);
//		}
	},

	callbackFromCm0090 : function(result) {
		if (result) {
			let exist = false;
			$('tbody>tr',  $('#accessibles')).each(function(i, tr) {
				if (result.assignRoleCode === $('span[data-field=roleCode]', $(tr)).text()) {
					exist = true;
					return false;
				}
			});
			if (!exist) {
				const corporationCode = result.corporationCode;
				const code = result.assignRoleCode;
				const name = result.assignRoleName;
				const obj = {corporationCode:corporationCode, roleCode:code, roleName:name};
				block0004._table.addRowResult(obj);
			}
		}
	},

	/** 権限ロール削除 */
	delRole : function(ev) {
		$('input[type=checkbox].selectable:checked', $('#accessibles')).each(function(i, elem) {
			let $tr = $(elem).closest('tr');
			$tr.remove();
		});
	},

	switchingAuth : function() {
		// 設定状況に応じてチェックボックスの活性／非活性を切り替える
		$('tbody>tr', $('#accessibles')).each(function(i, tr) {
			let $tr = $(tr);
			block0004.changeAuthRefer($('input[type=checkbox][data-field=authRefer]', $tr));
			block0004.changeAuthEdit($('input[type=checkbox][data-field=authEdit]', $tr));
		});
	},

	changeAuthRefer : function($authRefer) {
		Validator.hideBalloon($authRefer);
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
	},

	changeAuthEdit : function($authEdit) {
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
	},

	getAccessibles : function() {
		let accessibles = NCI.toArrayFromTable($('#docBlock0004_contents'), ['roleName', 'userName']);
		return accessibles;
	}
};

/** 文書属性(拡張)ブロック */
var block0005 = {
	_table : null,
	isInputable : false,
	targetTD : null,

	init: function(res) {
		// 入力可能なトレイモードか？
		block0005.isInputable = (INPUTABLE_TRAY_TYPES.indexOf(res.contents.trayType) > -1);

		// テンプレートの選択肢
		let $metaTemplateId = $('#metaTemplateId');
		NCI.createOptionTags($metaTemplateId, res.contents.metaTemplates);
		$metaTemplateId.prop('selectedIndex', 0);
		// 表示／非表示を切り替え
		$('#attributeExInfo').toggleClass('hide', !block0005.isInputable);

		// 文書属性(拡張)情報を展開
		block0005._table = new ResponsiveTable($('#docBlock0005_contents'));
		block0005._table.modifyTR = block0005.modifyTR
		block0005._table.fillTable(res.contents.attributeExs);
	},

	/** 入力タインプごとにテンプレートを書き変えるメソッド（ResponsiveTableのデフォルト実装を上書きする想定）  */
	modifyTR : function($tr, rowIndex, entity, labels, ignores) {
		const isRequired = (entity.requiredFlag === '1');
		$tr.find('span[data-field=metaName]').toggleClass('required', isRequired);
		const $td = $tr.find('td.metaValues');
		const inputType = (entity.inputType	);
		// 入力モードか参照モードかに応じて表示するDIVを切り替える
		if (block0005.isInputable) {
			$('div.refer', $td).remove();
			// 入力モードのときは更に入力タイプに応じて表示項目等を切り替え
			const lengths = (entity.maxLengths);
			let $target = null;
			// 入力タイプが"数値"の場合
			if (inputType === '1') {
				$target = $('input.numMetaValue', $td);
				let validate = {"pattern":"numeric","maxLength":(lengths || 300)};
				$target.attr('maxlength', lengths || 300).data('validate', validate).toggleClass('required', isRequired);
			}
			// 入力タイプが"文字列"の場合
			else if (inputType === '2') {
				$target = $('input.txtMetaValue', $td);
				let validate = {"maxLength":(lengths || 300)};
				$target.attr('maxlength', lengths || 300).data('validate', validate).toggleClass('required', isRequired);
			}
			// 入力タイプが"文章"の場合
			else if (inputType === '3') {
				let validate = {"maxLength":(lengths || 300)};
				$target = $('textarea.txtareaMetaValue', $td).data('validate', validate).toggleClass('required', isRequired);
			}
			// 入力タイプが"日付"の場合
			else if (inputType === '4') {
				$target = $('input.dateMetaValue', $td).toggleClass('required', isRequired);
				NCI.ymdPicker($target);
			}
			// 入力タイプが"チェックボックス"の場合
			else if (inputType === '5') {
				$target = $('input.chkMetaValue', $td).toggleClass('required', isRequired);
			}
			// 入力タイプが"ラジオボタン"の場合
			else if (inputType === '6') {
				$target = $('span.radioTarget', $td);
				block0005.createRadioButtons($target, entity.optionItems, entity.metaTemplateDetailId);

			// 入力タイプが"コンボボックス"の場合
			}else if (inputType === '7') {
				$target = $('select.ddlMetaValue', $td);
				$target.toggleClass('required', isRequired);
				NCI.createOptionTags($target, entity.optionItems);
			}
			// 入力タイオが"組織選択"の場合
			else if (inputType === '8') {
				$target = $('input.orgMetaValue', $td);
				$target.toggleClass('required', isRequired);
			}
			// 入力タイオが"ユーザ選択"の場合
			else if (inputType === '9') {
				$target = $('input.usrMetaValue', $td);
				$target.toggleClass('required', isRequired);
			}
//			// $targetに対してID付与
//			$target.attr('id', entity.metaCode);
			// 表示対象となるdivに対してClass属性"hide"を除去
			$target.closest('div').removeClass('hide');
			// 残りのclass属性で"hide"がついているものは全削除
			$td.find('div.hide').each(function(i, elem) {
				$(elem).remove();
			})
		} else {
			$('div.inputable', $td).remove();
			// 入力タイプが"チェックボックス"であればチェックボックス形式で表示する
			if (inputType === '5') {
				$td.find('div.refer>span[data-field=metaValue1]').remove();
			} else {
				$td.find('div.refer>input[data-field=metaValue1]').remove();
				// 入力タイプが"文章"の場合、改行コードを<BR>タグで置換
				if (inputType === '3') {
					entity.metaValue1 = NCI.escapeHtmlBR(entity.metaValue1);
//					$td.find('div.refer>span[data-field=metaValue1].normal').remove();
				} else {
//					$td.find('div.refer>span[data-field=metaValue1].html-escaped').remove();
				}
			}
		}
		return $tr;
	},

	/** イベント定義 */
	bindEvent : function() {
		$(document)
		.on('change', '#metaTemplateId', block0005.changeTemplate)
		.on('change', 'select.ddlMetaValue', block0005.changeSelect)
		.on('change', 'input[type=radio].rdoMetaValue', block0005.changeRadio)
		.on('click', '#btnSelectOrg', function(ev) {
			block0005.targetTD = $(ev.target).closest('td');
			const params = null, corporationCode = $('#corporationCode').val();
			let url = "../cm/cm0020.html";
			if (corporationCode)
				url += "?corporationCode=" + corporationCode;
			Popup.open(url, block0005.callbackFromCm0020, params, this);
		})
		.on('click', '#btnClearOrg', function(ev) {
			const $td = $(ev.target).closest('td');
			$('input[data-field]', $td).val('');
		})
		.on('click', '#btnSelectUser', function(ev) {
			block0005.targetTD = $(ev.target).closest('td');
			const params = null, corporationCode = $('#corporationCode').val();
			let url = "../cm/cm0040.html";
			if (corporationCode)
				url += "?corporationCode=" + corporationCode;
			Popup.open(url, block0005.callbackFromCm0040, params, this);
		})
		.on('click', '#btnClearUser', function(ev) {
			const $td = $(ev.target).closest('td');
			$('input[data-field]', $td).val('');
		});
	},

	/** バリデーション */
	validate: function() {
		let $root = $('#attributeExs')
		let $targets = $root.find('input, select, textarea');
		if (!Validator.validate($targets, true)) {
			return false;
		}
		return true;
	},

	/**
	 * テンプレート変更時処理.
	 */
	changeTemplate : function(ev) {
		const params = {corporationCode : vd0310.contents.corporationCode, metaTemplateId : $('#metaTemplateId').val()};
		NCI.post("/dc0100/getAttributeExs", params).done(function(res) {
			if (res && res.success) {
				block0005._table.fillTable(res.attributeExs);
			}
		});
	},

	changeSelect : function(ev) {
		const $td = $(ev.target).closest('td');
		const $selectOption = $('select.ddlMetaValue option:selected', $td);
		const val = $selectOption.length > 0 ? $selectOption.val() : ''
		$('input[type=hidden][data-field=metaValue1]', $td).val(val === '' ? '' : $selectOption.text());
		$('input[type=hidden][data-field=metaValue2]', $td).val(val);
	},

	changeRadio : function(ev) {
		const $td = $(ev.target).closest('td');
		const $radio = $(ev.target);
		const $label = $radio.next('label');
		$('input[type=hidden][data-field=metaValue1]', $td).val($label.text());
		$('input[type=hidden][data-field=metaValue2]', $td).val($radio.val());
	},

	callbackFromCm0020 : function(result) {
		if (result) {
			$('input[data-field=metaValue1]', block0005.targetTD).val(result.organizationName);
			$('input[data-field=metaValue2]', block0005.targetTD).val(result.corporationCode);
			$('input[data-field=metaValue3]', block0005.targetTD).val(result.organizationCode);
			$('input[data-field=metaValue4]', block0005.targetTD).val('');
			$('input[data-field=metaValue5]', block0005.targetTD).val('');
		}
		block0005.targetTD = null;
	},

	callbackFromCm0040 : function(result) {
		if (result) {
			$('input[data-field=metaValue1]', block0005.targetTD).val(result.userName);
			$('input[data-field=metaValue2]', block0005.targetTD).val(result.corporationCode);
			$('input[data-field=metaValue3]', block0005.targetTD).val(result.userCode);
			$('input[data-field=metaValue4]', block0005.targetTD).val('');
			$('input[data-field=metaValue5]', block0005.targetTD).val('');
		}
		block0005.targetTD = null;
	},

	createRadioButtons : function($target, optionItems, metaId, isRequired) {
		const df = document.createDocumentFragment();
		if (optionItems && optionItems.length > 0) {
			const len = optionItems.length;
			const type = typeof optionItems[0];
			let radio, label, item;
			for (let i = 0; i < len; i++) {
				item = optionItems[i];
				radio = document.createElement('input');
				radio.type = 'radio';
				radio.name = 'rdoMetaValue_' + metaId;
				radio.value = item.value;
				radio.setAttribute('class', (isRequired ? 'rdoMetaValue required' : 'rdoMetaValue'));
				radio.setAttribute('data-field', 'metaValue2');
				df.appendChild(radio);
				label = document.createElement('label');
				label.appendChild(document.createTextNode(item.label));
				df.appendChild(label);
			}
		}
		$target[0].appendChild(df);
	},

	getAttributeEx : function() {
		let attributeExs = NCI.toArrayFromTable($('#docBlock0005_contents'), []);
		return attributeExs;
	}
};

/** 文書ファイルブロック */
var block0006 = {
	_table : null,

	init: function(res) {
		// 入力可能なトレイモードか？
		let isInputable = (INPUTABLE_TRAY_TYPES.indexOf(res.contents.trayType) > -1);

		$('#btnUploadFile').toggleClass('hide', !isInputable);
		$('#btnDeleteFile').toggleClass('hide', !isInputable);
		$('#docFileDragableArea').toggleClass('hide', !isInputable);
		let $table = $('#docFiles');
		$('.line-number', $table).toggleClass('hide', isInputable);
		$('input[type=checkbox].selectable', $table).toggleClass('hide', !isInputable);

		// 文書ファイル情報を展開
		block0006._table = new ResponsiveTable($('#divDocFiles'));
		block0006._table.modifyTR = block0006.modifyTR
		block0006._table.fillTable(res.contents.docFiles);
	},

	/** 入力タインプごとにテンプレートを書き変えるメソッド（ResponsiveTableのデフォルト実装を上書きする想定）  */
	modifyTR : function($tr, rowIndex, entity, labels, ignores) {
		const isNew = (entity.docFileId == null);
		if (isNew) {
			$tr.find('span[data-field=fileName]').remove();
			$tr.find('th.processing').children().remove();
			$tr.find('div.dispVersion').remove();
			$tr.find('span[data-field=comments]').remove();
		} else {
			// 編集権限またはダウンロード権限がなければファイル名はリンク表示しない(＝ダウンロードできない)
			if (entity.authEdit !== '1' && entity.authDownload !== '1') {
				$tr.find('a[data-field=fileName]').remove();
			} else {
				$tr.find('span[data-field=fileName]').remove();
			}
			// バージョンは表示のみ
			$tr.find('div.inputVersion').remove();
			// 各種アイコンの表示／非表示切り替え
			// なおWF連携中はロック不可
			$tr.find('i.fa-lock').toggleClass('hide', (entity.wfApplying === '1' || entity.authEdit !== '1' || entity.locked === '1'));
			$tr.find('i.fa-unlock-alt').toggleClass('hide', (entity.authEdit !== '1' || entity.locked !== '1'));
			$tr.find('i.fa-edit').toggleClass('hide', (entity.authEdit !== '1'));
			$tr.find('i.fa-download').toggleClass('hide', (entity.authEdit !== '1' && entity.authDownload !== '1'));
			$tr.find('i.fa-copy').toggleClass('hide', (entity.authCopy !== '1'));
			$tr.find('i.fa-arrows').toggleClass('hide', (entity.authMove !== '1' || entity.locked !== '1'));
			$tr.find('i.fa-trash').toggleClass('hide', (entity.authDelete !== '1' || entity.locked !== '1'));
			// コメントは表示のみ
			$tr.find('input[data-field=comments]').remove();
		}
	},

	/** イベント定義 */
	bindEvent : function() {
		$(document)
			.on('click', '#btnUploadFile', function() {$('#fileUpload').click();})
			.on('click', '#btnDeleteFile', block0006.removeMultiple)
			.on('click', 'a[data-field=fileName].lnkDownloadDocFile, i.fa-download', block0006.download)
			.on('click', 'a>i.fa-lock', block0006.lock)
			.on('click', 'a>i.fa-unlock-alt', block0006.unlock)
			.on('click', 'a>i.fa-edit', block0006.edit)
			.on('click', 'a>i.fa-copy', block0006.copy)
			.on('click', 'a>i.fa-arrows', block0006.move)
			.on('click', 'a>i.fa-trash', block0006.removeOne);

		const selector = '#docFileDragableArea, #fileUpload';
		FileUploader.setup(selector, "/dc0100/upload/docFileData", false, block0006.add);
	},

	/** 追加 */
	add : function(res) {
		if (res && res.success && res.docFiles && res.docFiles.length > 0) {
			$.each(res.docFiles, function(idx, docFile) {
				block0006._table.addRowResult(docFile);
			});
		}
	},

	/** 選択されたファイル削除 */
	removeMultiple : function(ev) {
		let deleteList = [];
		let trIndexList = [];
		const $table = $('#docFiles');
		$('input[type=checkbox]:checked',$table).each(function(i, elem) {
			const $tr = $(this).closest('tr');
			const docFileId = $tr.find('span[data-field=docFileId]').text();
			const docFileVersion = $tr.find('span[data-field=version]').text();
			const docFileDataId = $tr.find('span[data-field=docFileDataId]').text();
			deleteList.push({docFileId:docFileId, version:docFileVersion, docFileDataId:docFileDataId});
			trIndexList.push($tr.index());
		});
		// 削除実行
		block0006.remove(deleteList, trIndexList);
	},

	/** 削除アイコンを押下したファイル削除 */
	removeOne : function(ev) {
		let $tr = $(ev.target).closest('tr');
		let docFileId = $tr.find('span[data-field=docFileId]').text();
		let docFileVersion = $tr.find('span[data-field=version]').text();
		let docFileDataId = $tr.find('span[data-field=docFileDataId]').text();
		const deleteList = [{docFileId:docFileId, version:docFileVersion, docFileDataId:docFileDataId}];
		const trIndexList = [$tr.index()];
		// 削除実行
		block0006.remove(deleteList, trIndexList);
	},

	/**
	 * 削除
	 * @param deleteList 削除対象の文書ファイル一覧
	 * @param tdIndexList 削除対象の行番号一覧
	 */
	remove : function(deleteList, tdIndexList) {
		if (deleteList.length > 0) {
			const msg = NCI.getMessage('MSG0072', '文書ファイル');
			if (NCI.confirm(msg, function() {
				const params = {docId : vd0310.contents.docId, corporationCode : vd0310.contents.corporationCode, deleteDocFiles : deleteList};
				NCI.post('/dc0100/deleteFile', params).done(function(res) {
					if (res && res.success) {
						const $table = $('#docFiles');
						$('tbody>tr',$table).each(function(i, elem) {
							const $tr = $(this);
							if ($.inArray($tr.index(), tdIndexList) > -1) {
								$tr.remove();
							}
						});
					}
				});
			}));
		}
	},

	/** ロック */
	lock : function(ev) {
		const $tr = $(ev.target).closest('tr');
		const docFileId = $tr.find('[data-field=docFileId]').text();
		const version = $tr.find('[data-field=version]').text();
		let params = {
			docId : vd0310.contents.docId,
			corporationCode : vd0310.contents.corporationCode,
			docFileId : docFileId,
			version : version
		};
		NCI.post("/dc0100/lockFile", params).done(function(res) {
			if (res && res.success && res.docFiles && res.docFiles.length > 0) {
				$.each(res.docFiles, function(i, entity) {
					block0006._table.fillRowResult($tr, $tr.index(), entity);
				});
			}
		});
	},

	/** ロック解除 */
	unlock : function(ev) {
		const $tr = $(ev.target).closest('tr');
		const docFileId = $tr.find('[data-field=docFileId]').text();
		const version = $tr.find('[data-field=version]').text();
		const params = {
			docId : vd0310.contents.docId,
			corporationCode : vd0310.contents.corporationCode,
			docFileId : docFileId,
			version : version
		};
		NCI.post("/dc0100/unlockFile", params).done(function(res) {
			if (res && res.success && res.docFiles && res.docFiles.length > 0) {
				$.each(res.docFiles, function(i, entity) {
					block0006._table.fillRowResult($tr, $tr.index(), entity);
				});
			}
		});
	},

	_editParams : null,

	/** 編集 */
	edit : function(ev) {
		const $tr = $(ev.target).closest('tr');
		const docFileId = $tr.find('[data-field=docFileId]').text();
		const params = {
			docId : vd0310.contents.docId,
			corporationCode : vd0310.contents.corporationCode,
			docFileId : docFileId
		};
		block0006._editParam = params;
		Popup.open("../dc/dc0101.html", block0006.fromDc0101, params, ev);
	},

	/** ダウンロード */
	download : function(ev) {
		const $tr = $(ev.target).closest('tr');
		const docFileDataId = $tr.find('[data-field=docFileDataId]').text();
		NCI.download('/dc0100/download/docFileData?docFileDataId=' + docFileDataId);
	},

	/** 移動 */
	move : function(ev) {
		const $tr = $(this).closest('tr');
		const docFileId = $tr.find('[data-field=docFileId]').text();
		const version = $tr.find('[data-field=version]').text();
		const params = {excludeDocId : vd0310.contents.docId, corporationCode : vd0310.contents.corporationCode, docFileId : docFileId, version : version};
		Popup.open("../dc/dc0905.html", block0006.fromDc0905, params, ev);
	},

	/** コピー */
	copy : function(ev) {
		const $tr = $(this).closest('tr');
		const docFileId = $tr.find('[data-field=docFileId]').text();
		const version = $tr.find('[data-field=version]').text();
		const params = {excludeDocId : vd0310.contents.docId, corporationCode : vd0310.contents.corporationCode, docFileId : docFileId, version : version};
		Popup.open("../dc/dc0906.html", block0006.fromDc0906, params, ev);
	},

	/** バリデーション */
	validate: function() {
		let $root = $('#docFiles')
		let $targets = $root.find('input, select, textarea');
		if (!Validator.validate($targets, true)) {
			return false;
		}
		return true;
	},

	/** 文書ファイル一覧取得 */
	getDocFiles : function() {
		let docFiles = NCI.toArrayFromTable($('#divDocFiles'), ['dispFileName', 'dispMajorVersion', 'dispMinorVersion', 'dispComments']);
		return docFiles;
	},

	/** 文書ファイル編集画面からのコールバック処理. */
	fromDc0101 : function(result) {
		let params = block0006._editParam;
		if (params != null && params.docId != null && params.docFileId != null) {
			NCI.post("/dc0100/getFile", params).done(function(res) {
				if (res && res.success && res.docFiles.length > 0) {
					$('tbody>tr', $('#divDocFiles')).each(function() {
						let $tr = $(this);
						if ($tr.find('span[data-field=docFileId]').text() === params.docFileId) {
							block0006._table.fillRowResult($tr, $tr.index(), res.docFiles[0]);
							return false;
						}
					});
				}
			});
		}
		block0006._editParam = null;
	},

	/** 文書ファイル移動先選択画面からのコールバック処理. */
	fromDc0905 : function(result) {
		if (result) {
			const params = {docId : vd0310.contents.docId, corporationCode : vd0310.contents.corporationCode, docFileId : result.docFileId, version : result.version, toDocId : result.toDocId};
			NCI.post("/dc0100/moveFile", params).done(function(res) {
				if (res && res.success) {
					$('tbody>tr', $('#divDocFiles')).each(function() {
						let $tr = $(this);
						if ($tr.find('span[data-field=docFileId]').text() === params.docFileId) {
							$tr.remove();
						}
					});
				}
			});
		}
	},

	/** 文書ファイルコピー先選択画面からのコールバック処理. */
	fromDc0906 : function(result) {
		if (result) {
			const params = {docId : vd0310.contents.docId, corporationCode : vd0310.contents.corporationCode, docFileId : result.docFileId, version : result.version, toDocId : result.toDocId, notVersionCopy : result.notVersionCopy};
			NCI.post("/dc0100/copyFile", params).done(function(res) {
				if (res && res.success) {
				}
			});
		}
	}
};

/** 更新履歴ブロック */
var block0007 = {
	init: function(res) {
	},

	/** イベント定義 */
	bindEvent : function() {
		$(document)
			.on('click', '#docBlock0007 .collapse_btn', block0007.expand);
	},

	/** バリデーション */
	validate: function() {
		return true;
	},

	/** ブロックの開閉 */
	expand : function(ev) {
		const closed = $(this).hasClass('collapsed');
		if (!closed && vd0310.contents.docId && !vd0310.contents.updateLogs) {
			const params = {
					docId: vd0310.contents.docId
			};
			NCI.get('/dc0100/getHistory', params).done(function(results) {
				if (results && results.length) {
					vd0310.contents.updateLogs = results;
					new ResponsiveTable($('#docBlock0007_contents')).fillTable(vd0310.contents.updateLogs);
				}
			});
		}
		new ResponsiveTable($('#docBlock0007_contents')).fillTable(vd0310.contents.updateLogs);
	}
};

/** メモ情報ブロック  */
var block0008 = {
	init: function(res) {
		// 入力可能なトレイモードか？
		let isInputable = (INPUTABLE_TRAY_TYPES.indexOf(res.contents.trayType) > -1);
		// 文書作成後、かつ入力可能な場合のみ、投稿可能
		$("#btnNewMemo").toggleClass('hide', !vd0310.contents.docId || !isInputable);
	},

	/** イベント定義 */
	bindEvent : function() {
		$(document)
			.on('click', '#btnNewMemo', block0008.openPopup)
			.on('click', '#docBlock0008 .collapse_btn', block0008.expand);
	},

	expand : function(ev) {
		if (!$(ev.target).hasClass('collapsed')) {
			// メモ情報を読み直し
			let params = {
					"docId" : vd0310.contents.docId
			};
			NCI.post('/dc0100/getDocMemo', params, true).done(function(res) {
				if (res && res.success && res.memos) {
					block0008.displayDocMemo(res.memos);
				}
			});
		}
	},

	/** メモ情報入力画面をポップアップで開く */
	openPopup : function() {
		Popup.open('../cm/cm0180.html', block0008.callback);
	},

	/** メモ情報入力画面からのコールバック関数 */
	callback : function(memoInfo) {
		if (memoInfo) {
			let params = {
					"docId" : vd0310.contents.docId,
					"memo" : memoInfo.memo
			};
			// 入力された内容でメモ情報をインサートし、結果を反映
			NCI.post('/dc0100/addDocMemo', params).done(function(res) {
				if (res && res.success && res.memos) {
					block0008.displayDocMemo(res.memos);
				}
			});
		}
	},

	/** 文書メモリストを表示 */
	displayDocMemo : function(memos) {
		let count = memos != null ? memos.length : 0;
		$('#bl0008-noRecord').toggleClass('hide', (count > 0));
		let $root = $('#bl0008-hasRecord')
			.empty()
			.toggleClass('hide', (count == 0));

		if (count > 0) {
			for (let i = 0; i < memos.length; i++) {
				let memo = memos[i];
				block0008.appendMemo(memo, $root);
			}
		}
	},

	/** 親エレメントの配下に記事を追加 */
	appendMemo : function(pb, $parent) {
		// テンプレートをコピーして、記事を反映
		let mySubmit = NCI.loginInfo.corporationCode == pb.corporationCodeSubmit
			&& NCI.loginInfo.userCode == pb.userCodeSubmit;
		let template = document.getElementById('bl0008-template');
		let $media = $(template.cloneNode(true));
		$media.removeAttr('id');
		NCI.toElementsFromObj(pb, $media);
		$media.find('[data-field=memo]').html(NCI.escapeHtmlBR(pb.memo));

		// 親の配下として追加
		if ($parent.is('#bl0008-hasRecord'))
			$parent.append($media);
		else
			$parent.find('>div.media-body>div.replies').append($media);
	}
};

/** バインダーブロック */
var block0009 = {
	init: function(res) {
		// 入力可能なトレイモードか？
		const isInputable = (INPUTABLE_TRAY_TYPES.indexOf(res.contents.trayType) > -1);

		const isNew = res.contents.trayType === 'NEW';
		const isOwnLock = res.contents.docInfo.ownLockFlag === '1';
		const $root = $('#docBlock0009_contents');
		// 新規登録／更新時と参照時で見せるエレメントを切り替える
		if (isInputable) {
			$root.find('#title, #comments').toggleClass('hide', false);
//			$('#title').toggleClass('hide', false);
//			$('#comments').toggleClass('hide', false);
		} else {
			$root.find('span[data-field=title], span[data-field=comments]').toggleClass('hide', false);
//			$('span[data-field=title]').toggleClass('hide', false);
//			$('span[data-field=comments]').toggleClass('hide', false);
			// 参照時はコメントの改行を<BR>タグで置換しておく
			res.contents.docInfo.comments = NCI.escapeHtmlBR(res.contents.docInfo.comments);
		}
		// データを設定
		NCI.toElementsFromObj(res.contents.docInfo, $('#docBlock0009_contents'));
	},

	/** イベント定義 */
	bindEvent : function() {
		// 特に必要なし
	},

	/** バリデーション */
	validate: function() {
		let $targets = $('#docBlock0009_contents').find('input, select, textarea');
		if (!Validator.validate($targets, true)) {
			return false;
		}
		return true;
	},

	/** バインダー情報取得 */
	getBinderInfo : function() {
		const binderInfo = NCI.toObjFromElements($('#docBlock0009_contents'), []);
		return binderInfo;
	}
};

/** 添付ファイルブロック */
var block0010 = {
	_table : null,

	init: function(res) {
		// 入力可能なトレイモードか？
		let isInputable = (INPUTABLE_TRAY_TYPES.indexOf(res.contents.trayType) > -1);

		$('#btnUploadAttachFile').toggleClass('hide', !isInputable);
		$('#btnDeleteAttachFile').toggleClass('hide', !isInputable);
		$('#attachFileDragableArea').toggleClass('hide', !isInputable);
		let $table = $('#attachFiles');
		$('.line-number', $table).toggleClass('hide', isInputable);
		$('input[type=checkbox].selectable', $table).toggleClass('hide', !isInputable);

		// 添付ファイル情報を展開
		block0010._table = new ResponsiveTable($('#divAttachFiles'));
		block0010._table.modifyTR = block0010.modifyTR;
		block0010._table.fillTable(res.contents.attachFileDocs);
	},

	/** 入力タインプごとにテンプレートを書き変えるメソッド（ResponsiveTableのデフォルト実装を上書きする想定）  */
	modifyTR : function($tr, rowIndex, entity, labels, ignores) {
		if (entity.attachFileDocId != null) {
			$tr.find('input[data-field=comments]').toggleClass('hide', true);
		} else {
			$tr.find('span[data-field=comments]').toggleClass('hide', true);
		}
	},

	/** イベント定義 */
	bindEvent : function() {
		$(document)
			.on('click', '#btnUploadAttachFile', function() {$('#attachFileUpload').click();})
			.on('click', '#btnDeleteAttachFile', block0010.remove)
			.on('click', 'a[data-field=fileName].lnkDownloadAttachFile', block0010.download);

		const selector = '#attachFileDragableArea, #attachFileUpload';
		FileUploader.setup(selector, "/dc0100/upload/attachFileData", false, block0010.add);
	},

	/** 追加 */
	add : function(res) {
		if (res && res.success && res.attachFiles && res.attachFiles.length) {
			$.each(res.attachFiles, function(idx, attachFile) {
				block0010._table.addRowResult(attachFile);
			});
		}
	},

	/** ファイル削除 */
	remove : function(ev) {
		let deleteTargets = [];
		const $table = $('#attachFiles');
		$('input[type=checkbox]:checked',$table).each(function(i, elem) {
			let $tr = $(this).closest('tr');
			let attachFileDocId = $tr.find('span[data-field=attachFileDocId]').text();
			let attachFileVersion = $tr.find('span[data-field=version]').text();
			let docFileDataId = $tr.find('span[data-field=docFileDataId]').text();
			deleteTargets.push({attachFileDocId:attachFileDocId, version:attachFileVersion, docFileDataId:docFileDataId});
		});
		if (deleteTargets.length > 0) {
			const msg = NCI.getMessage('MSG0072', NCI.getMessage('attachFile'));
			if (NCI.confirm(msg, function() {
				const params = {deleteAttachFiles : deleteTargets};
				NCI.post('/dc0100/deleteAttachFile', params).done(function(res) {
					if (res && res.success) {
						$('input[type=checkbox]:checked',$table).each(function(i, elem) {
							$(this).closest('tr').remove();
						});
					}
				});
			}));
		}
		return;
	},

	/** ダウンロード */
	download : function(ev) {
		const $tr = $(ev.target).closest('tr');
		const docFileDataId = $tr.find('[data-field=docFileDataId]').text();
		NCI.download('/dc0100/download/attachFileData?docFileDataId=' + docFileDataId);
	},

	/** 添付ファイル一覧取得 */
	getAttachFiles : function() {
		let attachFiles = NCI.toArrayFromTable($('#divAttachFiles'), ['dispComments']);
		return attachFiles;
	}
};

/** WF連携ブロック */
var block0011 = {
	init: function(res) {
		// WF連携情報を展開
		new ResponsiveTable($('#divWfRelation')).fillTable(res.contents.docWfRelations);
	},

	/** イベント定義 */
	bindEvent : function() {
		$(document)
			.on('click', 'a[data-field=businessProcessStatus]', block0011.openDocumentAsPopup);
	},

	/** 申請・承認画面をポップアップとして参照モードで開く【アクセス可能な最新のアクティビティ】 */
	openDocumentAsPopup : function(ev) {
		// 連携先のプロセス情報
		let $tr = $(ev.target).closest('tr');
		let corporationCode = $tr.find('[data-field=corporationCode]').text();
		let processId = $tr.find('[data-field=processId]').text();
		let timestampUpdatedLong = $tr.find('[data-field=timestampUpdatedProcessLong]').text();

		// 連携先のプロセス情報のうち、操作者のアクセス可能な最新のアクティビティを求める。
		let params = { "corporationCode" : corporationCode, "processId" : processId };
		NCI.post('/dc0100/getAccessibleActivity', params, true).done(function(res) {
			if (res && res.success) {
				Popup.open("../vd/vd0310.html?corporationCode=" + corporationCode +
						"&processId=" + processId +
						"&activityId=" + res.activityId +
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

var blocks = {
	'0' : block0000,
	'1' : block0001,
	'2' : block0002,
	'3' : block0003,
	'4' : block0004,
	'5' : block0005,
	'6' : block0006,
	'7' : block0007,
	'8' : block0008,
	'9' : block0009,
	'10': block0010,
	'11': block0011
};