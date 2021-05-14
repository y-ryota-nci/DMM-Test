$(function() {
	var pager = new Pager($('#seach-result'), '/dc0072/search', search).init();
	pager.responsiveTable.modifyTR = modifyTR;

	const params = {'metaTemplateId' : NCI.getQueryString("metaTemplateId"), 'version' : NCI.getQueryString("version"), 'messageCds':['metaTemplate', 'metaTemplateDetail']};
	NCI.init("/dc0072/init", params).done(function(res) {
		if (res && res.success) {
			NCI.createOptionTags($('#deleteFlag'), res.deleteFlags);
			init(res);
		}
	});

	$(document)
	.on('click', '#btnRegister', function(ev) {
		const $root = $('#metaTemplateInfo');
		const $targets = $root.find('input, select, textarea');
		if (!Validator.validate($targets, true)) {
			return false;
		}
		var msg = NCI.getMessage("MSG0069", NCI.getMessage("metaTemplate"));
		if (NCI.confirm(msg, function() {
			const obj = NCI.toObjFromElements($root);
			const params = {entity : obj};
			NCI.post("/dc0072/save", params).done(function(res) {
				if (res && res.success) {
					init(res);
				}
			});
		}));
	})
	.on('click', '#btnUpdate', function(ev) {
		const $root = $('#metaTemplateInfo');
		const $targets = $root.find('input, select, textarea');
		if (!Validator.validate($targets, true)) {
			return false;
		}
		var msg = NCI.getMessage("MSG0071", NCI.getMessage("metaTemplate"));
		if (NCI.confirm(msg, function() {
			const obj = NCI.toObjFromElements($root);
			const params = {entity : obj};
			NCI.post("/dc0072/save", params).done(function(res) {
				if (res && res.success) {
					init(res);
				}
			});
		}));
	})
	.on('click', '#btnBack', function(ev) {
		NCI.redirect("./dc0070.html");
	})
	.on('click', 'ul.pagination a', function(ev) {
		// ページ番号リンク押下
		var pageNo = this.getAttribute('data-pageNo');
		search(pageNo);
		return false;
	})
	// 検索結果の選択チェックボックス
	.on('change', 'input[type=checkbox].selectable', function() {
		var selected = $('input[type=checkbox].selectable:checked').length === 0;
		$('#btnDelete').prop('disabled', selected);
	})
	// 検索結果のリンク押下
	.on('click', 'a[data-field]', function(ev) {
		const $tr = $(this).closest('tr');
		const metaTemplateId = $('#metaTemplateId').val();
		const metaTemplateDetailId = $tr.find('[data-field=metaTemplateDetailId]').text();
		const version = $tr.find('[data-field=version]').text();
		openEntry(metaTemplateId, metaTemplateDetailId, version);
	})
	// 追加ボタン
	.on('click', '#btnAdd', function(ev) {
		const metaTemplateId = $('#metaTemplateId').val();
		openEntry(metaTemplateId);
	})
	// 削除ボタン
	.on('click', '#btnDelete', function(ev) {
		var msg = NCI.getMessage("MSG0072", NCI.getMessage("metaTemplateDetail"));
		if (NCI.confirm(msg, function() {
			var params = {metaTemplateId : $('#metaTemplateId').val(),  deleteList : [] };
			$('input[type=checkbox].selectable:checked').each(function(i, elem) {
				const $tr = $(elem).closest('tr');
				const metaTemplateDetailId = $tr.find('[data-field=metaTemplateDetailId]').text();
				const version = $tr.find('[data-field=version]').text();
				params.deleteList.push({metaTemplateDetailId : metaTemplateDetailId, version : version});
			});
			NCI.post("/dc0072/delete", params).done(function(res) {
				search(null, true);
			});
		}));
	});
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

	function init(res) {
		const entity = res.entity;
		const isNew = (entity.metaTemplateCode == null);
		const isDelete = (entity.deleteFlag === '1');
		$('input, select, textarea, button', $('#metaTemplateInfo')).prop('disabled', (isDelete));
		$('#metaTemplateCode').prop('disabled', !(isNew));
		$('div.deleteFlag').toggleClass('hide', (isNew));
		$('#deleteFlag').prop('disabled', false);
		$('#btnRegister').prop('disabled', !(isNew)).toggleClass('hide', !(isNew));
		$('#btnUpdate').prop('disabled', (isNew)).toggleClass('hide', (isNew));
		$('#btnBack').prop('disabled', false);
		// 画面項目設定
		NCI.toElementsFromObj(entity, $('#metaItemInfo'));

		// テンプレート明細一覧を反映
		if (isNew || isDelete) {
			$('#seach-result').toggleClass('hide', true);
		} else {
			$('#seach-result').toggleClass('hide', false);
			search(1);
		}
	}

	/** 検索実行 */
	function search(pageNo, keepMessage) {
		var cond = createCondition(pageNo);
		pager.search(cond, keepMessage);
		$('#btnDelete').prop('disabled', true);
		$('#seach-result').removeClass('hide');
		$('#buttons').removeClass('hide');

	}

	/** 画面入力内容から検索条件を生成 */
	function createCondition(pageNo) {
		var $elements = $('#metaTemplateId');
		var cond = pager.createCondition($elements, pageNo);
		// デフォルトソート条件
		if (!cond.sortColumn) {
			cond.sortColumn = 'B.SORT_ORDER, B.META_TEMPLATE_DETAIL_ID';
			cond.sortAsc = true;
		}
		return cond;
	}

	function openEntry(metaTemplateId, metaTemplateDetailId, version) {
		var url = "./dc0074.html?metaTemplateId=" + metaTemplateId;
		if (metaTemplateDetailId) {
			url += "&metaTemplateDetailId=" + metaTemplateDetailId + "&version=" + version;
		}
		NCI.redirect(url);
	}
});

