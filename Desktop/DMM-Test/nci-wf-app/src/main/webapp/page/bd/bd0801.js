let pager;
$(function() {
	pager =  new Pager($('#seach-result'), '/bd0801/search');
	pager.responsiveTable.modifyTR = BD0801.modifyTR;	// 検索結果の補正処理関数

	// 以前の検索条件を復元できれば再検索する
	const oldCondition = BD0801.restoreCondition();
	NCI.init('/bd0801/init', oldCondition).done(function(res) {
		if (res && res.success) {
			BD0801.init(res);
		}
	});

	// 本部
	$('#organizationCodeLv2').change(BD0801.changeOrgLevel2)
	// 検索ボタン
	$('#btnSearch').click(BD0801.search);
	// リセットボタン
	$('#btnReset').click(BD0801.reset);
	// 保存ボタン押下
	$('#btnSave').click(BD0801.save);
	// 履歴取得ボタン押下
	$('#btnCreateHistory').click(BD0801.moveToBD0802);
	// 履歴メンテナンスボタン押下
	$('#btnEditHistory').click(BD0801.moveToBD0803);

});

const BD0801 = {
	// システム年度
	year : null,
	// システム月
	month : null,

	// 以前の検索条件の復元
	restoreCondition : function() {
		// 通常のPager.loadCondition()では本部の選択内容によってダイナミックに変わる部・室の値を復元できない（選択肢がないから）
		// よって選択されていた値だけを復元し、サーバサイドで予め部・室の選択肢を生成できるようにする
		const value = JSON.parse(window.sessionStorage.getItem(pager.url));
		if (value) {
			const p = JSON.parse(value);
			return {
				"organizationCodeLv2" : p.organizationCodeLv2,
				"organizationCodeLv3" : p.organizationCodeLv3
			};
		}
		return {};
	},

	// 初期化
	init : function(res) {
		// システム年度・月
		const now = new Date();
		BD0801.month = now.getMonth() + 1;	// 月（月INDEXではなく)
		BD0801.year = now.getFullYear() + (BD0801.month < 3 ? -1 : 0);	// システム年度

		NCI.createOptionTags($('#yrCd'), res.years);
		NCI.createOptionTags($('#organizationCodeLv2'), res.orgLv2s).val(res.organizationCodeLv2);
		NCI.createOptionTags($('#organizationCodeLv3'), res.orgLv3s).val(res.organizationCodeLv3);
		NCI.createOptionTags($('#rcvCostPayTp'), res.rcvCostPayTps);
		NCI.createOptionTags($('#bsplTp'), res.bsplTp);

		if (pager.loadCondition()) {
			BD0801.search();
		}
	},

	// 本部の変更時
	changeOrgLevel2 : function(ev) {
		if (!Validator.validate($('#organizationCodeLv2'))) {
			return false;
		}
		const params = {
			"organizationCodeLv2" : $('#organizationCodeLv2').val()
		}
		NCI.post('/bd0801/changeOrgLevel2', params).done(function(res) {
			if (res && res.success) {
				NCI.createOptionTags($('#organizationCodeLv3'), res.orgLv3s);
			}
		});
	},

	// 検索条件リセット
	reset : function(ev) {
		$('#formCondition').find('select').val('');
		$('#seach-result').addClass('hide');
		$('#btnSave, #btnCreateHistory, #btnEditHistory').prop('disabled', true);
	},

	// 検索
	search : function(pageNo, keepMessage) {
		const $root = $('#formCondition');
		const $targets = $root.find('input, select, textarea');
		if (!Validator.validate($targets, true)) {
			return false;
		}

		const cond = pager.createCondition($targets);
		pager.search(cond).done(function(res) {
			BD0801.fillTable(res);
		});
	},

	// 検索結果行の補正処理
	modifyTR : function($tr, rowIndex, entity, labels, ignores) {
		$tr.find('input[data-field^=bgtAmt]').each(function(i, elem) {
			const yyyy = +entity.yrCd;
			elem.readOnly = (BD0801.year > yyyy);
		});
	},

	// 検索結果をテーブルへ反映
	fillTable : function(res) {
		if (res && res.success) {
			const disabled = (res.results.length === 0) || (res.results[0].yrCd < BD0801.year);
			$('#btnSave, #btnCreateHistory').prop('disabled', disabled);
			$('#btnEditHistory').prop('disabled', false);
			$('#seach-result').removeClass('hide');
		}
	},

	// 保存ボタン押下
	save : function(ev) {
		const $root = $('#seach-result');
		const $targets = $root.find('input,select,textarea');
		if (!Validator.validate($targets, true)) {
			return false;
		}

		const msg = '予算を保存します。よろしいですか';
		NCI.confirm(msg, function() {
			const params = BD0801.createParams();
			params.inputs = NCI.toArrayFromTable($root);
			NCI.post('/bd0801/save', params).done(function(res) {
				if (res && res.success) {
					BD0801.search(1);
				}
			});
		});
	},

	// 履歴取得ボタン押下
	moveToBD0802 : function(ev) {
		const $root = $('#formCondition');
		const $targets = $root.find('input,select,textarea');
		if (!Validator.validate($targets, true)) {
			return false;
		}
		NCI.redirect('./bd0802.html' + BD0801.toQueryString(BD0801.createParams()));
	},

	// 履歴メンテナンスボタン押下
	moveToBD0803 : function(ev) {
		const $root = $('#formCondition');
		const $targets = $root.find('input,select,textarea');
		if (!Validator.validate($targets, true)) {
			return false;
		}
		NCI.redirect('./bd0803.html' + BD0801.toQueryString(BD0801.createParams()));
	},

	// 画面遷移用パラメータ生成
	createParams : function() {
		return {
			"yrCd" : $('#yrCd').val(),
			"organizationCodeLv2" : $('#organizationCodeLv2').val(),
			"organizationCodeLv3" : $('#organizationCodeLv3').val(),
			"rcvCostPayTp" : $('#rcvCostPayTp').val(),
			"bsplTp" : $('#bsplTp').val()
		};
	},

	// 画面遷移用パラメータをQueryString化
	toQueryString : function(params) {
		return "?yrCd=" + params.yrCd
				+ "&organizationCodeLv2=" + params.organizationCodeLv2
				+ "&organizationCodeLv3=" + params.organizationCodeLv3
				+ "&rcvCostPayTp=" + params.rcvCostPayTp
				+ "&bsplTp=" + params.bsplTp;
	}
};
