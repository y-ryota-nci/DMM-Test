$(function() {
	const args = {
		"yrCd" : NCI.getQueryString("yrCd"),
		"organizationCodeLv2" : NCI.getQueryString("organizationCodeLv2"),
		"organizationCodeLv3" : NCI.getQueryString("organizationCodeLv3"),
		"rcvCostPayTp" : NCI.getQueryString("rcvCostPayTp"),
		"bsplTp" : NCI.getQueryString("bsplTp")
	};
	NCI.init('/bd0802/init', args).done(function(res) {
		if (res && res.success) {
			bd0802.init(args, res);
		}
	});

	// 履歴作成ボタン押下
	$('#btnCreateHistory').click(bd0802.create);
	// キャンセルボタン押下
	$('#btnCancel').click(bd0802.back);

});

const bd0802 = {
	// 初期化
	init : function(args, res) {
		new Pager($('#search-result')).fillTable(res.results);
		NCI.toElementsFromObj(res, $('body'));

		// カンマ区切り
		$('[data-field^=bgtAmt]').each(function(i, elem) {
			elem.textContent = NCI.addComma(elem.textContent);
		});

		$('#btnCreateHistory').prop('disabled', false);
	},

	// 履歴作成ボタン押下
	create : function(ev) {
		const $root = $('#formCondition');
		const $targets = $root.find('input');
		if (!Validator.validate($targets, true)) {
			return false;
		}

		const msg = '予算計画履歴を作成します。よろしいですか';
		NCI.confirm(msg, function() {
			const params = {
				"yrCd" : $("#yrCd").text(),
				"organizationCodeLv3" : $("#organizationCodeLv3").text(),
				"rcvCostPayTp" : $("#rcvCostPayTp").text(),
				"hstNm" : $("#hstNm").val(),
				"hstRmk" : $("#hstRmk").val(),
				"bsplTp" : $("#bsplTp").text()
			};
			NCI.post('/bd0802/create', params);
		});
	},

	// キャンセルボタン押下
	back : function(ev) {
		NCI.redirect('./bd0801.html');
	}
};
