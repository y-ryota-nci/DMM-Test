$(function() {
	var pager = new Pager($('#seach-result'), '/dc0060/search', search).init();
	pager.responsiveTable.modifyTR = modifyTR;

	var params = {messageCds : []};
	NCI.init("/dc0060/init", params).done(function(res) {
		if (res && res.success) {
			NCI.createOptionTags($('#corporationCode'), res.corporations);
			NCI.createOptionTags($('#inputType'), res.inputTypes);
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
		const metaId = $tr.find('[data-field=metaId]').text();
		const version = $tr.find('[data-field=version]').text();
		openEntry(metaId, version);
	})
	// 追加ボタン
	.on('click', '#btnAdd', function(ev) {
		NCI.redirect("./dc0062.html");

	})
	;

	/** 入力タインプごとにテンプレートを書き変えるメソッド（ResponsiveTableのデフォルト実装を上書きする想定）  */
	function modifyTR($tr, rowIndex, entity, labels, ignores) {
		// 初期値の表示方法切替
		if (entity.inputType === '5') {
			$tr.find('input[data-field=initialValue1]').toggleClass('hide', false);
		} else {
			$tr.find('span[data-field=initialValue1]').toggleClass('hide', false);
		}
		return $tr;
	}

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
			cond.sortColumn = 'META_CODE';
			cond.sortAsc = true;
		}
		return cond;
	}

	function openEntry(metaId, version) {
		if (metaId) {
			NCI.redirect("./dc0062.html?metaId=" + metaId + "&version=" + version);
		} else {
			NCI.redirect("./dc0062.html");
		}
	}
});

