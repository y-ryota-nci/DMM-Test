/* PO0020 管理_定期発注マスタ一覧 */
$(function() {
	const pager = new Pager($('#seach-result'), '/po0020/search', search).init();
	NCI.init('/po0020/init').done(function(res) {
		if (res && res.success) {
			$('#companyCd').val(NCI.loginInfo.corporationCode);

			// 以前の検索条件を復元できれば再検索する
			if (pager.loadCondition()) {
				search();
			}
			// カレンダー（年月日）
			NCI.ymdPicker($('input.ymdPicker'));
			// カレンダー（年月）
			NCI.ymPicker($('input.ymPicker'));
		}
	});

	// イベント定義
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
		return false;
	})
	// 検索結果のリンク押下
	.on('click', 'a[data-field]', function(ev) {
		const $tr = $(this).closest('tr');
		const companyCd = $tr.find('[data-field=companyCd]').text();
		const purordNo = $tr.find('[data-field=purordNo]').text();
		openEntry(companyCd, purordNo);
		return false;
	})
	;

	/** 検索実行 */
	function search(pageNo, keepMessage) {
		const $targets = $('#formCondition').find('input, select, text');
		if (!Validator.validate($targets, true)) {
			return false;
		}
		const cond = createCondition(pageNo);
		pager.search(cond, keepMessage);
		$('#seach-result').removeClass('hide');
	}

	/** 画面入力内容から検索条件を生成 */
	function createCondition(pageNo) {
		const $elements = $('#formCondition').find('input, select, textarea');
		const cond = pager.createCondition($elements, pageNo);
		// デフォルトソート条件
		if (!cond.sortColumn) {
			cond.sortColumn = 'O.COMPANY_CD, O.PURORD_NO';
			cond.sortAsc = true;
		}
		return cond;
	}

	/** 定期発注情報登録画面を開く */
	function openEntry(companyCd, purordNo) {
		NCI.flushScope('_vd0330', {
			'keys' : { 'companyCd' : companyCd, 'purordNo' : purordNo },
			'corporationCode' : companyCd,
			'screenCode' : 'SCR0086',
			'screenName' : '管理_発注予約',	// screenNameを指定していればVD0330でそれが画面名として使われる
			'backUrl' : '../po/po0020.html',
			'dcId' : 1,						// 指定されていればその表示条件ID、無指定なら申請
			'trayType' : 'WORKLIST'			// 指定されていればそのトレイタイプ、無指定なら汎用案件
		});
		NCI.redirect('../vd/vd0330.html');
	}
});

