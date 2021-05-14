$(function() {
	const pager = new Pager($('#seach-result'), '/py0070/search', search).init();

	const params = {};
	NCI.init("/py0070/init", params).done(function(res) {
		if (res && res.success) {
			NCI.createOptionTags($('#companyCd'), res.companyCds);
			NCI.createOptionTags($('#accCd'), res.accCds);

			// 以前の検索条件を復元できれば再検索する
			if (pager.loadCondition()) {
				search();
			} else {
				$('#excZeroAmt').prop("checked",true);
			}

			// カレンダー（年月日）
			NCI.ymPicker($('input.ymPicker'));

			$(document).on('click', '#btnSearch', function(ev) {
				// 検索ボタン押下
				search(1);
				return false;
			})
			.on('click', 'ul.pagination a', function(ev) {
				// ページ番号リンク押下
				const pageNo = this.getAttribute('data-pageNo');
				search(pageNo);
				return false;
			})
			// リセットボタン押下
			.on('click', '#btnReset', function(ev) {
				$('#formCondition')[0].reset();
				$('#excZeroAmt').prop("checked",true);
			})
			// 検索結果のリンク押下
			.on('click', 'a[data-field]', function(ev) {
				var companyCd = $('>.companyCd', $(this).parent()).val();
				var splrCd = $('>.splrCd', $(this).parent()).val();
				var addYmS = $('>.addYmS', $(this).parent()).val();
				var addYmE = $('>.addYmE', $(this).parent()).val();
				var accCd = $('>.accCd', $(this).parent()).val();

				openEntry(companyCd, splrCd, addYmS, addYmE, accCd);
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
				NCI.download('/py0070/download', cond);
			})
			;
		}
	});

	/** 検索実行 */
	function search(pageNo, keepMessage) {
		const $targets = $('#formCondition').find('input, select, text');
		if (!Validator.validate($targets, true)) {
			return false;
		}
		const cond = createCondition(pageNo);
		pager.search(cond, keepMessage).done(function(res) {
			$('#seach-result').removeClass('hide');

			$('#prvTotalAmtJpy').val(NCI.addComma(res.prvTotalAmtJpy));
			$('#dbtTotalAmtJpy').val(NCI.addComma(res.dbtTotalAmtJpy));
			$('#cdtTotalAmtJpy').val(NCI.addComma(res.cdtTotalAmtJpy));
			$('#nxtTotalAmtJpy').val(NCI.addComma(res.nxtTotalAmtJpy));

			// 3桁カンマ区切り
			$('#seach-result tbody tr').find('.dataType-number').each(function(i, elem) {
				elem.textContent = NCI.addComma(elem.textContent);
			});
		});
	}

	/** 画面入力内容から検索条件を生成 */
	function createCondition(pageNo) {
		let $elements = $('#formCondition').find('input, select, textarea');
		let cond = pager.createCondition($elements, pageNo);
		// デフォルトソート条件
		if (!cond.sortColumn) {
			cond.sortColumn = 'COMPANY_CD, SPLR_CD';
			cond.sortAsc = true;
		}
		return cond;
	}

	/** 勘定科目選択ポップアップからのコールバック */
	function callbackFromSelectAcc(acc) {
		if (splr) {
			$('#accCd').val(acc['ACC_CD']).trigger('validate');
			$('#accNm').val(acc['ACC_NM']).trigger('validate');
		}
	}

	/** 勘定科目をクリア */
	function clearForAcc() {
		$('#accCd, #accNm').val('').trigger('validate');
	}

	/** 明細行（買掛残高詳細）を開く */
	function openEntry(companyCd, splrCd, addYmS, addYmE, accCd) {
		NCI.redirect("./py0071.html?companyCd=" + companyCd + "&splrCd=" + splrCd + "&addYmS=" + addYmS + "&addYmE=" + addYmE + "&accCd=" + accCd);
	}
});

