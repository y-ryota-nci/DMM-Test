$(function() {
	// トレイ設定と検索処理を管理するコントローラー
	var searchUrl = '/wl0030/';
	var gadget = NCI.getQueryString('gadget');
	if (gadget === 'application') {
		searchUrl += 'searchFromGadgetApplication';
	} else if (gadget === 'approval') {
		searchUrl += 'searchFromGadgetApproval';
	} else {
		searchUrl += 'search';
	}
	const tray = new TRAY(searchUrl, $('#formCondition'), $('#seach-result'), $('#tray-template'), search);
	const params = { "messageCds" : ['sendback', 'pullback', 'pullforward', 'MSG0135']};
	NCI.init('/wl0030/init', params).done(function(res) {
		if (res && res.success) {
			// トレイ設定から検索条件＆検索結果テンプレートを読み込んで展開
			tray.init(res).done(function() {
				// 以前の検索条件が保存されていればそれを復元したうえで自動検索
				if (tray.pager.loadCondition()) {
//					search(null);
				}
				search(null);	// ワークリストは以前の検索の有無に関わらず、常に初期検索
			});

			// イベントのバインド
			$(document)
				// 検索ボタン押下
				.on('click', '#btnSearch', function() {
					search(1);
					return false;
				})
				// ページ番号リンク押下
				.on('click', 'ul.pagination a', function(ev) {
					const pageNo = ev.currentTarget.getAttribute('data-pageNo')
					search(pageNo);
					return false;
				})
				// 検索結果のリンク押下
				.on('click', 'a[data-field]', function(ev) {
					const $tr = $(ev.currentTarget).closest('tr');
					tray.openDetail(ev, $tr);
					return false;
				})
				// パーソナライズ設定を開く
				.on('click', '#btnPersonalize', function(ev) {
					NCI.redirect('./wl0020.html?trayType=' + tray.trayType);
				})
				// 全選択
				.on('click', 'button.btnSelectAll', function(ev) {
					selectAll(true);
				})
				// 全解除
				.on('click', 'button.btnCancelAll', function(ev) {
					selectAll(false);
				})
				// 行選択
				.on('click', 'input.selectable[type=checkbox]', function() {
					whenSelectRow();
				})
				// CSVダウンロード
				.on('click', '#btnDownloadCsv', downloadCsv)
				// 連続処理
				.on('click', '#btnContinuousProcessing', function(ev) {
					tray.continuousProcessing(ev);
				})
		}
	});


	/** 検索実行 */
	function search(pageNo) {
		const $elements = $('#formCondition').find('input, select, textarea');
		if (!Validator.validate($elements, true))
			return false;

		const cond = toCondition($elements, pageNo);
		tray.pager.search(cond).done(function() {
			$('#seach-result').removeClass('hide');
			$('#btnApproveAtOnce, #btnContinuousProcessing').prop('disabled', true);

			// 3桁カンマ区切り
			$('#seach-result tbody tr').find('.dataType-number').each(function(i, elem) {
				elem.textContent = NCI.addComma(elem.textContent);
			});
		});
	};

	/** CSVダウンロード */
	function downloadCsv(ev) {
		const $elements = $('#formCondition').find('input, select, textarea');
		if (!Validator.validate($elements, true))
			return false;

		// ダウンロード
		const cond = toCondition($elements);
		NCI.download('/wl0030/downloadCsv', cond);

		return false;
	}

	/** 検索条件の生成 */
	function toCondition($elements, pageNo) {
		const cond = tray.pager.createCondition($elements, pageNo);
		// デフォルトソート条件
		if (!cond.sortColumn) {
			cond.sortColumn = tray.initSortColumnName;
			cond.sortAsc = tray.initialSortAsc;
		}
		// トレイ設定検索条件の定義
		cond.trayType = tray.trayType;
		cond.trayConfigId = tray.trayConfigId;
		cond.configConditions = tray.configConditions;
		return cond;
	}

	/** 全選択／全解除 */
	function selectAll(choice) {
		$('tbody input.selectable').prop('checked', choice);
		whenSelectRow();
	}

	/** 行選択時の動作 */
	function whenSelectRow() {
		const len = $('tbody input.selectable[type=checkbox]:checked').length;
		$('#btnContinuousProcessing, #btnApproveAtOnce').prop('disabled', len === 0);
	}
});
