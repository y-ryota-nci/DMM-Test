$(function() {
	const pager = new Pager($('#seach-result'), '/py0071/search', search).init();

	const params = {
		"companyCd" : NCI.getQueryString("companyCd"),
		"splrCd" : NCI.getQueryString("splrCd"),
		"addYmS" : NCI.getQueryString("addYmS"),
		"addYmE" : NCI.getQueryString("addYmE"),
		"accCd" : NCI.getQueryString("accCd")
	};
	NCI.init("/py0071/init", params).done(function(res) {
		if (res && res.success) {

			$('#companyCd').val(params["companyCd"]);
			$('#addYmS').val(params["addYmS"]);
			$('#addYmE').val(params["addYmE"]);
			$('#accCd').val(params["accCd"]);
			$('#splrCd').val(params["splrCd"]);
			$('#companyNm').val(res.companyNm);
			$('#accNm').val(res.accNm);
			$('#splrNmKj').val(res.splrNmKj);

			search();

			// 追加ボタン
			$(document).on('click', '#btnBack', function(ev) {
				NCI.redirect("./py0070.html");
			})
			.on('click', 'ul.pagination a', function(ev) {
				// ページ番号リンク押下
				const pageNo = this.getAttribute('data-pageNo');
				search(pageNo);
				return false;
			})
			.on('click', '#btnDownload', function(ev) {
				const $targets = $('#formCondition').find('input, select, text');
				var err = 0;
				if (!Validator.validate($targets, true)) {
					err = 1;
				}
				if (err==1) {
					return false;
				}
				NCI.clearMessage();
				const cond = createCondition(1);
				NCI.download('/py0071/download', cond);
			})
			;
		}
	});

	/** 検索実行 */
	function search(pageNo) {
		var cond = createCondition(pageNo);
		pager.search(cond).done(function(res) {
			$('#seach-result').removeClass('hide');

			$('#prvTotalAmtJpy').val(NCI.addComma(res.prvTotalAmtJpy));
			$('#dbtTotalAmtJpy').val(NCI.addComma(res.dbtTotalAmtJpy));
			$('#cdtTotalAmtJpy').val(NCI.addComma(res.cdtTotalAmtJpy));
			$('#nxtTotalAmtJpy').val(NCI.addComma(res.nxtTotalAmtJpy));

			// 3桁カンマ区切り
			$('#seach-result tbody tr').find('.dataType-number').each(function(i, elem) {
				elem.textContent = NCI.addComma(elem.textContent);
			});
		});;
	}

	/** 画面入力内容から検索条件を生成 */
	function createCondition(pageNo) {
		let $elements = $('#formCondition').find('input, select, textarea');
		let cond = pager.createCondition($elements, pageNo);
		// デフォルトソート条件
		if (!cond.sortColumn) {
			cond.sortColumn = 'JRN.ADD_DT, JRN.JRNSLP_NO, JRN.JRNSLP_DTL_NO';
			cond.sortAsc = true;
		}
		return cond;
	}

});

