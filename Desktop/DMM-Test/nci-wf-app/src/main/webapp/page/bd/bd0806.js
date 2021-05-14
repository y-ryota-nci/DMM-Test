let pager;
$(function() {
	BD0806.args = {
			"yrCd" : NCI.getQueryString("yrCd"),
			"organizationCodeLv2" : NCI.getQueryString("organizationCodeLv2"),
			"organizationCodeLv3" : NCI.getQueryString("organizationCodeLv3"),
			"rcvCostPayTp" : NCI.getQueryString("rcvCostPayTp"),
			"bsplTp" : NCI.getQueryString("bsplTp")
	};
	pager =  new Pager($('#search-result'), '/bd0806/search');
	pager.responsiveTable.modifyTR = BD0806.modifyTR;	// 検索結果の補正処理関数

	NCI.init('/bd0806/init', BD0806.args).done(function(res) {
		if (res && res.success) {
			BD0806.init(res);
		}
	});

	// 本部
	$('#organizationCodeLv2').change(BD0806.changeOrgLevel2);
	// 検索ボタン
	$('#btnSearch').click(BD0806.search);
	// リセットボタン
	$('#btnReset').click(BD0806.reset);
	// 実績のリンク
	$(document).on('click', 'a.com, a.dcm', BD0806.moveToBd0809);
});

const BD0806 = {
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
		NCI.createOptionTags($('#bsplTp'), res.bsplTps).val(res.bsplTp);

		// 以前の検索条件を復元できれば再検索する
		const conditions = pager.loadCondition();
		if (conditions) {
			// 復元された部・室を反映するには、まず本部／部・室の選択肢を生成しなければならないので、
			// 復元された本部をドロップダウンリストへ反映し、その後、改めて部・室の選択肢を求める
			$('#organizationCodeLv2').val(conditions.organizationCodeLv2);
			BD0806.changeOrgLevel2().done(function() {
				$('#organizationCodeLv3').val(conditions.organizationCodeLv3);
				BD0806.search();
			});
		}

		if(res.organizationCodeLv3 != null){
			$('#organizationCodeLv3Title, #organizationCodeLv3').toggleClass('required', true);
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
		return NCI.post('/bd0806/changeOrgLevel2', params).done(function(res) {
			if (res && res.success) {
				NCI.createOptionTags($('#organizationCodeLv3'), res.orgLv3s);
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

		const cond = pager.createCondition($targets, pageNo);
		pager.search(cond).done(function(res) {
			// カンマ区切り
			$('[data-field^=pfmAmt], [data-field^=comAmt], [data-field^=dcmAmt], [data-field^=difAmt]').each(function(i, elem) {
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
			if(entity['totalLineFlag'] === '1'){
                $tr.find('td[data-field=' + id + ']').addClass('bg-total');
                $tr.find('span[data-field=' + id + ']').parent().addClass('bg-total');
			}
		}

		// あとで参照できるよう、エンティティを退避
		$tr.data('entity', entity);
		// 組織欄のセル結合
		$tr.each(function(i, tr) {
			const $td = $(tr).find('td').eq(0);
			if (i !== 0 || entity.rowspan === 0)
				$td.addClass('hide');
			else
				$td.attr('rowspan', entity.rowspan);
		});

		const isLabel = (entity['bsPlTp'] == "1");
		$('td>a', $tr).toggleClass('hide', isLabel);
		$('td>span', $tr).toggleClass('hide', !isLabel);


//		const isLabel = ($('#bsplTp').val() == "1");
//		$('td>a', $tr).toggleClass('hide', isLabel);
//		$('td>span', $tr).toggleClass('hide', !isLabel);
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
				+ "&from=bd0806.html");
	}
};
