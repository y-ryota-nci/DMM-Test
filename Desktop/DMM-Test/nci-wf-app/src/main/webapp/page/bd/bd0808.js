let pager;
$(function() {
	BD0808.args = {
			"yrCd" : NCI.getQueryString("yrCd"),
			"organizationCodeLv2" : NCI.getQueryString("organizationCodeLv2"),
			"organizationCodeLv3" : NCI.getQueryString("organizationCodeLv3"),
			"rcvCostPayTp" : NCI.getQueryString("rcvCostPayTp"),
			"hstVersion" : NCI.getQueryString("hstVersion"),
			"bsplTp" : NCI.getQueryString("bsplTp")
	};
	pager =  new Pager($('#search-result'), '/bd0808/search');
	pager.responsiveTable.modifyTR = BD0808.modifyTR;	// 検索結果の補正処理関数

	NCI.init('/bd0808/init', BD0808.args).done(function(res) {
		if (res && res.success) {
			BD0808.init(res);
		}
	});

	// 本部
	$('#organizationCodeLv2').change(BD0808.changeOrgLevel2);
	// 年度／検収基準・支払基準／部・室／予算科目
	$('#yrCd, #rcvCostPayTp, #organizationCodeLv3, #bgtItmCd').change(BD0808.getHstVersion);
	// 検索ボタン
	$('#btnSearch').click(BD0808.search);
	// リセットボタン
	$('#btnReset').click(BD0808.reset);
	// 戻るボタン
	$('#btnBack').click(BD0808.back)
	// 実績のリンク
	$(document).on('click', 'a.com, a.dcm', BD0808.moveToBd0809);
});

const BD0808 = {
	// 画面遷移時のパラメータ
	args : null,

	// 初期化
	init : function(res) {
		NCI.createOptionTags($('#yrCd'), res.years).val(res.yrCd);
		NCI.createOptionTags($('#organizationCodeLv2'), res.orgLv2s).val(res.organizationCodeLv2);
		NCI.createOptionTags($('#organizationCodeLv3'), res.orgLv3s).val(res.organizationCodeLv3);
		NCI.createOptionTags($('#rcvCostPayTp'), res.rcvCostPayTps).val(res.rcvCostPayTp);
		NCI.createOptionTags($('#bgtItmCd'), res.bgtItmCds).val(res.bgtItmCd);
		NCI.createOptionTags($('#hstVersion'), res.hstVersions).val(res.hstVersion);
		NCI.createOptionTags($('#bgtItmCd'), res.bgtItmCds).val(res.bgtItmCd);
		NCI.createOptionTags($('#bsplTp'), res.bsplTps).val(NCI.getQueryString("bsplTp"));

		BD0808.search(1);
	},

	// 本部の変更時
	changeOrgLevel2 : function(ev) {
		if (!Validator.validate($('#organizationCodeLv2'))) {
			return false;
		}
		const params = {
			"organizationCodeLv2" : $('#organizationCodeLv2').val()
		}
		NCI.post('/bd0808/changeOrgLevel2', params).done(function(res) {
			if (res && res.success) {
				NCI.createOptionTags($('#organizationCodeLv3'), res.orgLv3s);
			}
		});
	},

	// 予算バージョンの選択肢を再生成
	getHstVersion : function(ev) {
		const params = NCI.toObjFromElements($('#formCondition'));
		NCI.post('/bd0808/getHstVersion', params).done(function(res) {
			if (res && res.success) {
				NCI.createOptionTags($('#hstVersion'), res.hstVersions);
			}
		});
	},

	// 検索条件リセット
	reset : function(ev) {
		$('#formCondition').find('input[type=text], select').val('');
		$('#search-result').addClass('hide');
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
			// カンマ区切り
			$('[data-field^=bgtAmt], [data-field^=comAmt], [data-field^=dcmAmt], [data-field^=difAmt]').each(function(i, elem) {
				elem.textContent = NCI.addComma(elem.textContent);
			});
			$('#search-result').removeClass('hide');
		});
	},

	// 検索結果行の補正処理
	modifyTR : function($tr, rowIndex, entity, labels, ignores) {
		for (let id in entity) {
			// 差異がマイナスなら赤字
			if (id.indexOf('dif') === 0) {
				if (entity[id] && NCI.removeComma(entity[id]) < 0) {
					$tr.find('td[data-field=' + id + ']').addClass('bold-red');
				}
			}
		}
		$tr.data('entity', entity);
	},

	// 実績リンク押下
	moveToBd0809 : function(ev) {
		const dataField = $(ev.target).attr('data-field');
		const $tr = $(ev.target).closest('tr');
		const entity = $tr.data('entity');
		const companyCd = NCI.loginInfo.corporationCode;

		NCI.redirect('./bd0809.html'
				+ "?companyCd=" + companyCd
				+ "&yrCd=" + entity.yrCd
				+ "&organizationCodeLv2=" + entity.organizationCodeLv2
				+ "&organizationCodeLv3=" + entity.organizationCodeLv3
				+ "&rcvCostPayTp=" + entity.rcvCostPayTp
				+ "&mm=" + dataField.slice(-2)
				+ "&bgtItmCd=" + entity.bgtItmCd
				+ "&from=bd0808.html");
	},

	// 戻るボタン押下
	back : function(ev) {
		// http://ncc1975:8080/nci-wf-app/page/bd/bd0803.html?yrCd=2018&organizationCodeLv2=101801&organizationCodeLv3=101830&rcvCostPayTp=1&_tm=1549948197582
		NCI.redirect('./bd0803.html'
				+ '?yrCd=' + NCI.getQueryString('yrCd')
				+ '&organizationCodeLv2=' + NCI.getQueryString('organizationCodeLv2')
				+ '&organizationCodeLv3=' + NCI.getQueryString('organizationCodeLv3')
				+ '&rcvCostPayTp=' + NCI.getQueryString('rcvCostPayTp')
				+ '&bsplTp=' + NCI.getQueryString('bsplTp'));
	}
};
