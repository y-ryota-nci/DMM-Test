$(function() {
	// トレイ設定と検索処理を管理するコントローラー
	const tray = new TRAY('/dc0022/search', $('#formCondition'), $('#seach-result'), $('#tray-template'), search);
	const params = { "messageCds" : ['sendback', 'pullback', 'pullforward']};
	NCI.init('/dc0022/init', params).done(function(res) {
		if (res && res.success) {
			// トレイ設定から検索条件＆検索結果テンプレートを読み込んで展開
			tray.init(res).done(function() {
				// 以前の検索条件が保存されていればそれを復元したうえで自動検索
				if (tray.pager.loadCondition()) {
					search(null);
				} else {
					$('#corporationCode').val(NCI.loginInfo.corporationCode);
					$('#deleteFlag').val('0');
				}
			});

			// イベントのバインド
			$(document)
				// 戻るボタン押下
				.on('click', '#btnBack', function() {
					NCI.redirect('../dc/dc0020.html');
				})
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
					const $tr = $(this).closest('tr');
					const docId = $tr.find('[data-field=docId]').text();
					const corporationCode = $tr.find('[data-field=corporationCode]').text();
					const url = "./dc0100.html?docId=" + docId + '&corporationCode=' + corporationCode;
					NCI.redirect(url);
					return false;
				})
				// 文書責任者選択ボタン押下
				.on('click', '#btnSelectOwnerUser', function(ev) {
					Popup.open("../cm/cm0050.html", callbackFromCm0050);
				})
				// 文書責任者クリアボタン押下
				.on('click', 'btnClearOwnerUser', function(ev) {
					const $div = $('#divOwnerUser');
					$div.find('input').val('');
				})
				// パーソナライズ設定を開く
				.on('click', '#btnPersonalize', function(ev) {
					NCI.redirect('./dc0200.html?docTrayType=' + tray.trayType);
				})
				// CSVダウンロード
				.on('click', '#btnDownloadCsv', downloadCsv);
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
		NCI.download('/wl0033/downloadCsv', cond);

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

	/** ユーザ選択画面からのコールバック */
	function callbackFromCm0050(ub) {
		if (ub) {
			$('#ownerCorporationCode').val(ub.corporationCode);
			$('#ownerUserCode').val(ub.userCode);
			$('#ownerUserName').val(ub.userName);
		}
	}
});
