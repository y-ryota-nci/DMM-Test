$(function() {
	const pager = new Pager($('#seach-result'), '/py0080/search', search).init();
	const params = {};
	NCI.init("/py0080/init", params).done(function(res) {
		if (res && res.success) {
			NCI.createOptionTags($('#companyCd'), res.companyCds);

			// 以前の検索条件を復元できれば再検索する
			if (pager.loadCondition()) {
				search();
			}
			else {
//				// 初期値
//				$('#payStsApproved').prop('checked', true);
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
				const payNo = $tr.find('[data-field=payNo]').text();
				const screenCode = $tr.find('[data-field=screenCode]').text();
				openEntry(companyCd, payNo, screenCode);
			})
			.on('click', '#btnDownload', function(ev) {
				const $targets = $('#formCondition').find('input, select, text');
				if (!Validator.validate($targets, true)) {
					return false;
				}
				NCI.clearMessage();
				const cond = createCondition(1);
				NCI.download('/py0080/download', cond);
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
			cond.sortColumn = 'A.COMPANY_CD,A.ADVPAY_NO';
			cond.sortAsc = true;
		}
		return cond;
	}

	/** 明細行を開く */
	function openEntry(companyCd, payNo, screenCode) {
		NCI.flushScope('_vd0330', {
			'keys' : { 'companyCd' : companyCd, 'payNo' : payNo, 'advpayFg': '1' },
			'corporationCode' : companyCd,
			'screenCode' : screenCode,
			'screenName' : '管理_前払登録',	// screenNameを指定していればVD0330でそれが画面名として使われる
			'backUrl' : '../py/py0080.html',
			'dcId' : 2						// 指定されていればその表示条件IDを使う
		});
		NCI.redirect('../vd/vd0330.html');
	}
});

