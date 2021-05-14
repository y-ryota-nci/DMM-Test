$(function() {
	var pager = new Pager($('#seach-result'), '/mm0052/search', search).init();

	var params = {
		messageCds : []
	};
	NCI.init("/mm0052/init", params).done(function(res) {
		if ('mm0051' in parent) {
			// 採番形式設定から呼び出されるときはポップアップ表示なので
			// メニューを表示しない
			$('header')[0].style.display = 'none';
		}
		if (res && res.success) {
			NCI.createOptionTags($('#corporationCode'), res.corporations);
			NCI.createOptionTags($('#deleteFlag'), res.deleteFlags);
			$('#btnAdd').prop('disabled', false)

			// 以前の検索条件を復元
			if (pager.loadCondition()) {
				search();
			} else {
				$('#corporationCode').val(NCI.loginInfo.corporationCode);
				$('#deleteFlag').val('0');
			}
		}
	});

	$(document)
	.on('click', '#btnSearch', function(ev) {
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
		$('#deleteFlag').val('0');
	})
	// 検索結果のリンク押下
	.on('click', 'a[data-field]', function(ev) {
		var $tr = $(this).closest('tr');
		var corporationCode = $tr.find('[data-field=corporationCode]').text();
		var partsSequenceSpecCode = $tr.find('[data-field=partsSequenceSpecCode]').text();
		var version = $tr.find('[data-field=version]').text();
		openEntry(corporationCode, partsSequenceSpecCode, version);
	})
	// 追加ボタン
	.on('click', '#btnAdd', function(ev) {
		let corporationCode = params.corporationCode || NCI.loginInfo.corporationCode;
		NCI.redirect("./mm0053.html" +
				"?corporationCode=" + corporationCode +
				"&partsNumberingFormatCode=" + (params.partsNumberingFormatCode || '') +
				"&version=" + (params.version || ''));
	})
	;
	$('#btnClose').on('click', function(ev) {
		Popup.close();
	}).toggleClass('hide', !Popup.isPopup());

	/** 検索実行 */
	function search(pageNo) {
		var $targets = $('#formCondition').find('input, select, text');
		if (!Validator.validate($targets, true)) {
			return false;
		}
		var cond = createCondition(pageNo);
		pager.search(cond);
		$('#seach-result').removeClass('hide');
	}

	/** 画面入力内容から検索条件を生成 */
	function createCondition(pageNo) {
		var $elements = $('#formCondition').find('input, select, textarea');
		var cond = pager.createCondition($elements, pageNo);
		// デフォルトソート条件
		if (!cond.sortColumn) {
			cond.sortColumn = 'PARTS_SEQUENCE_SPEC_CODE, PARTS_SEQUENCE_SPEC_ID';
			cond.sortAsc = true;
		}
		return cond;
	}

	function openEntry(corporationCode, partsSequenceSpecCode, version) {
		NCI.redirect("./mm0053.html?corporationCode=" + corporationCode +
				"&partsSequenceSpecCode=" + partsSequenceSpecCode +
				"&version=" + version);
	}
});

