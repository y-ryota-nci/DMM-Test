$(function() {
	// ワークリストは初期表示で自分のTODOリストを表示するので、初期表示＝検索である
	let pager = new Pager($('#seach-result'), '/cm0060/search', search).init();
	pager.responsiveTable.modifyTR = function($tr, rowIndex, entity, labels, ignores) {
		$tr.find('input[type=radio]').prop('disabled', false);
	};
	let searchParams;
	NCI.init("/cm0060/init").done(function(res) {
		if (res && res.success) {
			// プロセス定義の選択肢
			NCI.createOptionTags($('#processDefCode'), res.processDefs);
			// 業務プロセス状態の選択肢
			NCI.createOptionTags($('#businessProcessStatus'), res.businessProcessStatus);

			searchParams = NCI.flushScope(NCI.getQueryString(FLUSH_SCOPE_KEY))
//			// 以前の検索条件を復元
//			pager.loadCondition();
//			search();
		}
	});

	$(document).on('click', '#btnSearch', function() {
		// 検索ボタン押下
		search(1);
		return false;
	})
	.on('click', 'ul.pagination a', function() {
		// ページ番号リンク押下
		let pageNo = this.getAttribute('data-pageNo');
		search(pageNo);
		return false;
	})
	.on('click', 'button.btnClose', function() {
		Popup.close();
	})
	// 検索結果のリンク押下
	.on('click', 'a[data-field]', function(ev) {
		let $tr = $(this).closest('tr');
		openDetail($tr);
	})
	// 選択ボタン
	.on('click', '#btnSelect', function() {
		let $tr = $('input[name=processId]:checked').parent().parent();
		let entity = {};
		$tr.find('[data-field]').each(function(i, elem) {
			let fieldName = elem.getAttribute('data-field');
			if ("processId" == fieldName)
				entity[fieldName] = $(elem).val();
			else
				entity[fieldName] = $(elem).text();
		});

		// コールバック関数の呼び出し
		Popup.close(entity);
	})
	// 選択用ラジオボタン
	.on('click', 'input[data-field=processId]', function(ev) {
		$('#btnSelect').prop('disabled', false);
	})
	;

	/** 検索実行 */
	function search(pageNo) {
		let $targets = $('#formCondition').find('input, select');
		if (!Validator.validate($targets, true))
			return false;

		let cond = createCondition(pageNo);
		if (searchParams) {
			cond.notInProcessIds = searchParams.processIds;
		}
		pager.search(cond).done(function() {
			$('#seach-result').removeClass('invisible');
		});
		// 選択ボタン
		$('#btnSelect').prop('disabled', true);
	}

	/** 画面入力内容から検索条件を生成 */
	function createCondition(pageNo) {
		let $elements = $('#formCondition').find('input[type=text], select, textarea');
		let cond = pager.createCondition($elements, pageNo);
		// デフォルトソート条件
		if (!cond.sortColumn) {
			cond.sortColumn = 'APPLICATION_NO, PROCESS_ID';
			cond.sortAsc = false;
		}
		return cond;
	}

	/** 申請・承認画面を開く */
	function openDetail($tr) {
		// 汎用案件はプロセスベースなので、アクティビティIDが特定できない（パフォーマンス的な都合で）。
		// よって操作者のアクセス可能な最新のアクティビティを別途求める。
		let corporationCode = $tr.find('[data-field=corporationCode]').text();
		let processId = $tr.find('[data-field=processId]').val();
		let timestampUpdatedLong = $tr.find('[data-field=timestampUpdatedProcessLong]').text();
		let params = { "corporationCode" : corporationCode, "processId" : processId, "trayType" : "ALL" };
		NCI.post('/cm0060/getAccessibleActivity', params).done(function(res) {
			if (!res || !res.length) {
				throw new Error("アクセス可能なアクティビティがありません。");
			}
			Popup.open("../vd/vd0310.html?corporationCode=" + corporationCode +
					"&processId=" + processId +
					"&activityId=" + res[0].activityId +
					"&trayType=ALL" +
					"&timestampUpdated=" + timestampUpdatedLong);
		});
		return false;
	}
});
