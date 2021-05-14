$(function() {
	const pager = new Pager($('#seach-result'), '/pr0010/search', search).init();

	const params = {};
	NCI.init("/pr0010/init", params).done(function(res) {
		if (res && res.success) {
			NCI.createOptionTags($('#prcFldTp'), res.prcFldTpItems);

			// 以前の検索条件を復元できれば再検索する
			if (pager.loadCondition()) {
				search();
			}
			else {
				// 初期値
				$('#payStsApproved').prop('checked', true);
			}

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
			})
			// 検索結果のリンク押下
			.on('click', 'a[data-field]', function(ev) {
				const $tr = $(this).closest('tr');
				const companyCd = $tr.find('[data-field=companyCd]').val();
				const purrqstNo = $tr.find('[data-field=purrqstNo]').text();
				const screenCode = $tr.find('[data-field=screenCode]').text();
				openEntry(companyCd, purrqstNo, screenCode);
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
			cond.sortColumn = 'P.PURRQST_NO';
			cond.sortAsc = true;
		}
		return cond;
	}

	/** 明細行を開く */
	function openEntry(companyCd, purrqstNo, screenCode) {
		NCI.flushScope('_vd0330', {
			'keys' : { 'companyCd' : companyCd, 'purrqstNo' : purrqstNo },
			'corporationCode' : companyCd,
			'screenCode' : screenCode,
			'screenName' : '管理_購入依頼登録',	// screenNameを指定していればVD0330でそれが画面名として使われる
			'backUrl' : '../pr/pr0010.html',
			'dcId' : 2						// 指定されていればその表示条件IDを使う
		});
		NCI.redirect('../vd/vd0330.html');
	}
});

