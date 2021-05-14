$(function() {
	let pager = new Pager($('#seach-result'), '/ml0010/search', search).init();
	pager.responsiveTable.modifyTR = function($tr, rowIndex, entity, labels, ignores) {
		$tr.find('input[type=radio]').prop('disabled', false);
	};
	let params = {
			"messageCds" : ['mailTemplateFile', 'MSG0072'],
	};
	NCI.init("/ml0010/init", params).done(function(res) {
		if (res && res.success) {
			NCI.createOptionTags($('#corporationCode'), res.corporations);
			$('#btnAdd').prop('disabled', false)

			// 以前の検索条件を復元できれば再検索する
			if (pager.loadCondition()) {
				search();
			} else {
				$('#corporationCode').val(NCI.loginInfo.corporationCode);
			}

			$(document).on('click', '#btnSearch', function(ev) {
				// 検索ボタン押下
				search(1);
				return false;
			})
			.on('click', 'ul.pagination a', function(ev) {
				// ページ番号リンク押下
				var pageNo = this.getAttribute('data-pageNo');
				search(pageNo);
				return false;
			})
			// リセットボタン押下
			.on('click', '#btnReset', function(ev) {
				$('#formCondition')[0].reset();
			})
			// 検索結果のリンク押下
			.on('click', 'a[data-field]', function(ev) {
				let $tr = $(this).closest('tr');
				let mailTemplateFileId = $tr.find('[data-field=mailTemplateFileId]').text();
				let mailTemplateHeaderId = $tr.find('[data-field=mailTemplateHeaderId]').text();
				let version = $tr.find('[data-field=version]').text();
				openEntry(mailTemplateFileId, mailTemplateHeaderId, version);
			})
			// ダウンロードボタン押下
			.on('click', '#btnDownload', function(ev) {
				const corporationCode = $('#corporationCode').val() || NCI.loginInfo.corporationCode;
				NCI.download('ml0010/download?corporationCode=' + corporationCode);
			})
			// 追加ボタン押下
			.on('click', '#btnAdd', function(ev) {
				NCI.redirect('./ml0030.html')
			})
			// チェックボックス選択時
			.on('click', 'input[name=mailTemplateFileId]', function(ev) {
				const count = $('input[name=mailTemplateFileId]:checked').length;
				$('#btnDelete').prop('disabled', count === 0);
			})
			// 削除ボタン押下時
			.on('click', '#btnDelete', doDelete)
			;
		}
	});

	/** 検索実行 */
	function search(pageNo, keepMessage) {
		var $targets = $('#formCondition').find('input, select, text');
		if (!Validator.validate($targets, true)) {
			return false;
		}
		var cond = createCondition(pageNo);
		pager.search(cond, keepMessage);
		$('#seach-result').removeClass('hide');
		$('#btnDownload').prop('disabled', false);
	}

	/** 画面入力内容から検索条件を生成 */
	function createCondition(pageNo) {
		var $elements = $('#formCondition').find('input, select, textarea');
		var cond = pager.createCondition($elements, pageNo);
		// デフォルトソート条件
		if (!cond.sortColumn) {
			cond.sortColumn = 'F.MAIL_TEMPLATE_FILENAME, F.MAIL_TEMPLATE_FILE_ID';
			cond.sortAsc = true;
		}
		return cond;
	}

	function openEntry(mailTemplateFileId, mailTemplateHeaderId, version) {
			NCI.redirect("./ml0011.html" +
					"?fileId=" + mailTemplateFileId +
					"&headerId=" + mailTemplateHeaderId +
					"&version=" + version);
	}

	// 削除処理
	function doDelete(ev) {
		const mailTemplateFileId = $('input[name=mailTemplateFileId]:checked').first().val();
		if (!mailTemplateFileId) {
			return false;
		}

		const msg = NCI.getMessage('MSG0072', NCI.getMessage('mailTemplateFile'));
		NCI.confirm(msg, function() {
			// 更新処理
			const params = { "mailTemplateFileId" : mailTemplateFileId };
			NCI.post('/ml0010/delete', params).done(function(res) {
				if (res && res.success) {
					search(1, true);
				}
			})
		});
		return false;
	}

});

