let pager;
$(function() {
	pager = new Pager($('#search-result'), '/bd0803/search', BD0803.search);
	pager.responsiveTable.modifyTR = BD0803.modifyTR;

	const args = {
		"yrCd" : NCI.getQueryString("yrCd"),
		"organizationCodeLv2" : NCI.getQueryString("organizationCodeLv2"),
		"organizationCodeLv3" : NCI.getQueryString("organizationCodeLv3"),
		"rcvCostPayTp" : NCI.getQueryString("rcvCostPayTp"),
		"bsplTp" : NCI.getQueryString("bsplTp"),
	};
	args.sortAsc = true;
	args.sortColumn = "V.HST_DT, V.HST_VERSION";

	NCI.init('/bd0803/init', args).done(function(res) {
		if (res && res.success) {
			BD0803.init(args, res);
		}
	});

	// 削除ボタン押下
	$('#btnDelete').click(BD0803.doDelete);
	// キャンセルボタン押下
	$('#btnCancel').click(BD0803.doBack);
	// 全選択／全解除
	$('button.btnSelectAll').click(function() { BD0803.selectAll(true); });
	$('button.btnCancelAll').click(function() { BD0803.selectAll(false); });
	// チェックボックスON/OFF
	$(document)
	.on('click', 'tbody > tr > td > input.selectable', BD0803.clickCheckbox)
	.on('click', 'a[data-field=hstNm]', BD0803.moveToBd0808);
});

const BD0803 = {
	// 初期化
	init : function(args, res) {
		NCI.toElementsFromObj(args, $('#search-condition'));

		const $root = $('#search-result');
		$root.removeClass('hide');
		$('#btnDelete').prop('disabled', true);

		BD0803.search(1);
	},

	// 検索処理
	search : function(pageNo, keepMessage) {
		const cond = pager.createCondition($(null), pageNo);
		cond.yrCd = $('#yrCd').text();
		cond.organizationCodeLv2 = $('#organizationCodeLv2').text();
		cond.organizationCodeLv3 = $('#organizationCodeLv3').text();
		cond.rcvCostPayTp = $('#rcvCostPayTp').text();
		cond.bsplTp = $('#bsplTp').text();

		// デフォルトソート条件
		if (!cond.sortColumn) {
			cond.sortColumn = 'V.HST_DT, V.HST_VERSION';
			cond.sortAsc = true;
		}

		pager.search(cond, keepMessage).done(function(res) {
			$('#seach-result').removeClass('hide');
		});
	},

	// 行レンダリングの補正処理
	modifyTR : function($tr, rowIndex, entity, labels, ignores) {
		$tr.data('entity', entity);
	},

	// 削除ボタン押下
	doDelete : function(ev) {
		// 選択行のエンティティを取り出す
		const selects = [];
		$('tbody > tr > td > input.selectable:checked').each(function(i, checkbox) {
			const entity = $(checkbox).closest('tr').data('entity');
			selects.push(entity);
		});

		// バリデーション
		if (selects.length === 0) {
			NCI.alert("少なくとも一行は選択してください");
			return false;
		}

		// 削除処理
		const msg = '予算計画履歴を削除します。よろしいですか';
		NCI.confirm(msg, function() {
			const params = $.extend(pager.condition, {
				"yrCd" : $("#yrCd").text(),
				"organizationCodeLv3" : $("#organizationCodeLv3").text(),
				"rcvCostPayTp" : $("#rcvCostPayTp").text(),
				"selects" : selects
			});
			NCI.post('/bd0803/delete', params).done(function(res) {
				if (res && res.success) {
					BD0803.init(pager.condition, res);
				}
			});
		});
	},

	// キャンセルボタン押下
	doBack : function(ev) {
		NCI.redirect('./bd0801.html');
	},

	// 全選択／全解除
	selectAll : function(isSelect) {
		$('tbody > tr > td > input.selectable').each(function(i, checkbox) {
			checkbox.checked = isSelect;
		});
		$('#btnDelete').prop('disabled', !isSelect);
	},

	// チェックボックスON/OFF時の動作
	clickCheckbox : function(ev) {
		const checked = $('tbody > tr > td > input.selectable:checked').length > 0;
		$('#btnDelete').prop('disabled', !checked);
	},

	// 特定組織指定バージョン予算／実績分析画面へ遷移
	moveToBd0808 : function(ev) {
		const hstVersion = $(ev.currentTarget).closest('tr').find('[data-field=hstVersion]').text();
		const url = "./bd0808.html"
				+ "?yrCd=" + $("#yrCd").text()
				+ "&organizationCodeLv2=" + $("#organizationCodeLv2").text()
				+ "&organizationCodeLv3=" + $("#organizationCodeLv3").text()
				+ "&rcvCostPayTp=" + $("#rcvCostPayTp").text()
				+ "&bsplTp=" + $("#bsplTp").text()
				+ "&hstVersion=" + hstVersion;
		NCI.redirect(url);
	}
};
