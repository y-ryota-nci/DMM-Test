$(function() {
	var pager = new Pager($('#seach-result'), '/dc0070/search', search).init();

	var params = {
		messageCds : []
	};
	NCI.init("/dc0070/init", params).done(function(res) {
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
	// 検索結果のリンク押下
	.on('click', 'a[data-field]', function(ev) {
		const $tr = $(this).closest('tr');
		const metaTemplateId = $tr.find('[data-field=metaTemplateId]').text();
		const version = $tr.find('[data-field=version]').text();
		openEntry(metaTemplateId, version);
	})
	// 追加ボタン
	.on('click', '#btnAdd', function(ev) {
		openEntry();

	})
	;

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
			cond.sortColumn = 'META_TEMPLATE_CODE';
			cond.sortAsc = true;
		}
		return cond;
	}

	function openEntry(metaTemplateId, version) {
		if (metaTemplateId) {
			NCI.redirect("./dc0072.html?metaTemplateId=" + metaTemplateId + "&version=" + version);
		} else {
			NCI.redirect("./dc0072.html");
		}
	}
});

