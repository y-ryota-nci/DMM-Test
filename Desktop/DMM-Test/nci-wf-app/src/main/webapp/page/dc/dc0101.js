$(function() {
	const params = NCI.flushScope(NCI.getQueryString(FLUSH_SCOPE_KEY));
	NCI.init("/dc0101/init", params).done(function(res, textStatus, jqXHR) {
		if (res && res.success) {
			dc0101.init(res);
		}
		$(document)
			.on('click', '#btnBack'   , dc0101.back)
			.on('click', '#btnClose'  , Popup.close)
			.on('click', '#btnUpdate' , dc0101.update)
			.on('click', '#btnLock'   , dc0101.lock)
			.on('click', '#btnUnlock' , dc0101.unlock)
			.on('click', '#btnUploadFile', function() {$('#fileUpload').click();})
			.on('click', 'a[data-field=fileName].lnkDownloadDocFile, i.fa-download', dc0101.download)
		;
		const selector = '#docFileDragableArea, #fileUpload';
		FileUploader.setup(selector, "/dc0101/upload", false, dc0101.upload);
	});
});

var dc0101 = {

	/**
	 * 初期処理
	 */
	init: function(res) {
		let docFileInfo = res.docFileInfo;

		// ポップアップ画面なら戻るボタンではなく閉じるボタンで、メニューがない
		let isPopup = Popup.isPopup();
		$('#btnBack').toggleClass('hide', isPopup);
		$('#btnClose').toggleClass('hide', !isPopup);
		$('#nav').toggleClass('hide', isPopup);

		// 更新・ロックして編集・ロック解除の表示／非表示設定
		const isLock = docFileInfo.lockFlag === '1';		// ロック中か
		const isOwnLock = docFileInfo.locked === '1';		// 操作者自身がロック中か
		const isEdit = docFileInfo.authEdit === '1';		// 編集権限ありか
		const isDownload = (isEdit || docFileInfo.authDownload === '1');	// 編集権限ないしダウンロード権限があればよし
		const isWfApplying = docFileInfo.wfApplying === '1';	// WF連携中か
		$('#btnLock').toggleClass('hide', isWfApplying || isLock || !isEdit);
		$('#btnUnlock').toggleClass('hide', !isOwnLock);
		$('#uploadArea').toggleClass('hide',!isOwnLock);
		$('#btnUpdate').toggleClass('hide', true);
		$('#lockInfo').toggleClass('hide', !isLock);

		// ファイル名のLink／Span切り替え
		$('span[data-field=fileName].dispFileName').toggleClass('hide', (isDownload));
		$('a[data-field=fileName]').toggleClass('hide', !(isDownload));
		// アイコンの表示／非表示切り替え
		$('i.fa-download').toggleClass('hide', !(isDownload));
		// コメント入力欄、バージョンの更新区分の表示／非表示切り替え
		$('#updateVersionType').toggleClass('hide', true);
		$('#comments').toggleClass('hide', true);
		$('#dispComments').toggleClass('hide', false);
		// 更新ボタンを非表示切り替え
		$('#btnUpdate').toggleClass('hide', true);

		// 版管理の更新区分の選択肢
		let $updateVersionType = $('#updateVersionType');
		NCI.createOptionTags($updateVersionType, res.updateVersionTypes);
		$updateVersionType.prop('selectedIndex', 0);

		// データを設定
		NCI.toElementsFromObj(docFileInfo, $('#lockInfo'));
		NCI.toElementsFromObj(docFileInfo, $('#fileInfo-contents'));
		new ResponsiveTable($('#history-contents')).fillTable(res.histories);

		// 各articleが閉じていた場合開く
		$('span.collapse_btn').each(function() {
			if ($(this).hasClass('collapsed'))
				$(this).trigger('click');
		});
	},

	back : function() {

	},

	/** ロック */
	lock : function(ev) {
		let params = {
			docId : $('#docId').text(),
			docFileId : $('#docFileId').text(),
			version : $('#version').text()
		};
		NCI.post("/dc0101/lock", params).done(function(res) {
			if (res && res.success) {
				dc0101.init(res);
			}
		});
	},

	/** ロック解除 */
	unlock : function(ev) {
		let params = {
			docId : $('#docId').text(),
			docFileId : $('#docFileId').text(),
			version : $('#version').text()
		};
		NCI.post("/dc0101/unlock", params).done(function(res) {
			if (res && res.success) {
				dc0101.init(res);
			}
		});
	},

	/** アップロード */
	upload : function(res) {
		if (res && res.success && res.docFiles && res.docFiles.length == 1) {
			$('#fileName').text(res.docFiles[0].fileName);
			$('#docFileDataId').text(res.docFiles[0].docFileDataId);
			// コメント入力欄、バージョンの更新区分を表示
			$('#updateVersionType').toggleClass('hide', false);
			$('#comments').toggleClass('hide', false);
			$('#dispComments').toggleClass('hide', true);
			// 更新ボタンを表示
			$('#btnUpdate').toggleClass('hide', false);
		}
	},

	/** ダウンロード */
	download : function(ev) {
		const $td = $(ev.target).closest('td');
		const docFileDataId = $td.find('[data-field=docFileDataId]').text();
		NCI.download('/dc0101/download?docFileDataId=' + docFileDataId);
	},

	update : function(ev) {
		let $root = $('#confirmEdit')
		let $targets = $root.find('input, select, textarea');
		if (!Validator.validate($targets, true)) {
			return false;
		}
		const msg = NCI.getMessage('MSG0071', '文書ファイル');
		if (NCI.confirm(msg, function() {
			let docFileInfo = {
				docId : $('#docId').text(),
				docFileId : $('#docFileId').text(),
				version : $('#version').text(),
				docFileDataId : $('#docFileDataId').text(),
				comments : $('#comments').val(),
				fileName : $('#fileName').text()
			};
			let params = {docFileInfo : docFileInfo, updateVersionType : $('#updateVersionType').val()};
			NCI.post("/dc0101/update", params).done(function(res) {
				if (res && res.success) {
					dc0101.init(res);
				}
			});
		}));
	},

	/** バリデーション */
	validate: function() {
		return true;
	}
};
