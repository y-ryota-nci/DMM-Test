$(function() {
	const pager = new Pager($('#seach-result'), '/ss0010/search', search).init();

	const params = {};
	NCI.init("/ss0010/init", params).done(function(res) {
		if (res && res.success) {

			// 以前の検索条件を復元できれば再検索する
			if (pager.loadCondition()) {
				search();
			} else {
				$("#sndStsNon").prop("checked",true);
				$("#datTpGL").prop("checked",true);
				$("#datTpAP").prop("checked",true);
			}
			$("#companyCd").val(res.companyCd);

			// カレンダー（年月日）
			NCI.ymdPicker($('input.ymdPicker'));

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
				$("#sndStsNon").prop("checked",true);
				$("#datTpGL").prop("checked",true);
				$("#datTpAP").prop("checked",true);
			})
			.on('click', '#btnDownload', function(ev) {
				const $targets = $('#formCondition').find('input, select, text');
				var err = 0;
				if (!Validator.validate($targets, true)) {
					err = 1;
				}
				Validator.hideBalloon($('#datTpLbl'));
				if ($('.datTp').filter(":checked").length==0) {
					Validator.showBalloon($('#datTpLbl'), '必須入力です。');
					err = 1;
				}
				Validator.hideBalloon($('#sndStsLbl'));
				if ($('.sndSts').filter(":checked").length==0) {
					Validator.showBalloon($('#sndStsLbl'), '必須入力です。');
					err = 1;
				}
				if (err==1) {
					return false;
				}
				NCI.clearMessage();
				const cond = createCondition(1);
				NCI.download('/ss0010/download', cond);
			})
			.on('click', '.datTp', function(ev) {
				Validator.hideBalloon($('#datTpLbl'));
			})
			.on('click', '.sndSts', function(ev) {
				Validator.hideBalloon($('#sndStsLbl'));
			})
			;
		}
	});

	/** 検索実行 */
	function search(pageNo, keepMessage) {
		const $targets = $('#formCondition').find('input, select, text');
		var err = 0;
		if (!Validator.validate($targets, true)) {
			err = 1;
		}
		Validator.hideBalloon($('#datTpLbl'));
		if ($('.datTp').filter(":checked").length==0) {
			Validator.showBalloon($('#datTpLbl'), '必須入力です。');
			err = 1;
		}
		Validator.hideBalloon($('#sndStsLbl'));
		if ($('.sndSts').filter(":checked").length==0) {
			Validator.showBalloon($('#sndStsLbl'), '必須入力です。');
			err = 1;
		}
		if (err==1) {
			return false;
		}
		const cond = createCondition(pageNo);
		pager.search(cond, keepMessage).done(function() {
			$('#seach-result').removeClass('hide');

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
			cond.sortColumn = 'SND.MAK_DT, SND.MAK_TM, SND.SND_NO';
			cond.sortAsc = true;
		}
		return cond;
	}
});
