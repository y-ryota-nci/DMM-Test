let pager;
$(function() {
	pager =  new Pager($('#seach-result'), '/bd0809/search', BD0809.search).init();

	const args = {
		"companyCd" : NCI.getQueryString("companyCd"),
		"yrCd" : NCI.getQueryString("yrCd"),
		"organizationCodeLv2" : NCI.getQueryString("organizationCodeLv2"),
		"organizationCodeLv3" : NCI.getQueryString("organizationCodeLv3"),
		"rcvCostPayTp" : NCI.getQueryString("rcvCostPayTp"),
		"mm" : NCI.getQueryString("mm"),
		"bgtItmCd" : NCI.getQueryString("bgtItmCd")
	};
	NCI.init('/bd0809/init', args).done(function(res) {
		if (res && res.success) {
			BD0809.init(args, res);
		}
	});

	// 検索ボタン
	$('#btnSearch').click(function(ev) { BD0809.search(1); });
	// リセットボタン
	$('#btnReset').click(BD0809.reset);
	// 戻るボタン
	$('#btnBack').click(BD0809.back);

	$(document).on('click', 'ul.pagination a', function(ev) {
		// ページ番号リンク押下
		const pageNo = this.getAttribute('data-pageNo');
		BD0809.search(pageNo);
		return false;
	})
	// リンクを開く
	.on('click', 'a.open-document', BD0809.openDocument);
});

const BD0809 = {
	// 初期化
	init : function(args, res) {
		NCI.toElementsFromObj(args, $('#formCondition'));

		BD0809.search(1);
	},

	// 検索条件リセット
	reset : function(ev) {
		$('#formCondition').find('input[type=text]:visible').val('');
		$('#seach-result').addClass('hide');
	},

	// 検索
	search : function(pageNo, keepMessage) {
		const $root = $('#formCondition');
		const $targets = $root.find('input, select, textarea');
		if (!Validator.validate($targets, true)) {
			return false;
		}

		const cond = pager.createCondition($targets, (pageNo || 1));
		// デフォルトソート条件
		if (!cond.sortColumn) {
			cond.sortColumn = 'APPLICATION_NO, APPLICATION_DTL_NO';
			cond.sortAsc = true;
		}
		pager.search(cond, keepMessage).done(function(res) {
			// カンマ区切り
			$('[data-field^=amtJpy]').each(function(i, elem) {
				elem.textContent = NCI.addComma(elem.textContent);
			});
			$('#seach-result').removeClass('hide');
		});
	},

	// 戻るボタン押下
	back : function(ev) {
		const from = NCI.getQueryString('from');
		if (from === 'bd0806.html')
			NCI.redirect('./' + from
					+ '?yrCd=' + NCI.getQueryString('yrCd')
					+ '&organizationCodeLv2=' + NCI.getQueryString('organizationCodeLv2')
					+ '&organizationCodeLv3=' + NCI.getQueryString('organizationCodeLv3')
					+ '&rcvCostPayTp=' + NCI.getQueryString('rcvCostPayTp')
					+ '&bgtItmCd=' + NCI.getQueryString('bgtItmCd')
					+ '&hstVersion=' + NCI.getQueryString('hstVersion'));
		else
			NCI.redirect('./' + from
					+ '?yrCd=' + NCI.getQueryString('yrCd')
					+ '&organizationCodeLv2=' + NCI.getQueryString('organizationCodeLv2')
					+ '&organizationCodeLv3=' + NCI.getQueryString('organizationCodeLv3')
					+ '&rcvCostPayTp=' + NCI.getQueryString('rcvCostPayTp')
					+ '&hstVersion=' + NCI.getQueryString('hstVersion'));
	},

	openDocument : function(ev) {
		const $tr = $(ev.target).closest('tr');
		const entity = NCI.toObjFromElements($tr);
		const isPay = (entity.applicationNo.indexOf('PY') === 0);
		NCI.flushScope('_vd0330', {
				'keys' : {
					'companyCd' : entity.companyCd,
					'rcvinspNo' : (isPay ? null : entity.applicationNo),
					'payNo' : (isPay ? entity.applicationNo : null)
				},
				'corporationCode' : entity.companyCd,
				'screenCode' : (isPay ? 'SCR0050' : 'SCR0049'),
//				'screenName' : '管理_支払登録',	// screenNameを指定していればVD0330でそれが画面名として使われる
				'backUrl' : '../bd/bd0809.html?' + NCI.getQueryString(),
				'dcId' : 2						// 指定されていればその表示条件IDを使う
		});
		NCI.redirect('../vd/vd0330.html');
	}
};
