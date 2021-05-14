$(function() {
	// トレイ設定と検索処理を管理するコントローラー
	const tray = new TRAY('/wl0037/search', $('#formCondition'), $('#seach-result'), $('#tray-template'), search);
	const params = { "messageCds" : ['sendback', 'pullback', 'pullforward', 'MSG0135', 'MSG0271']};
	NCI.init('/wl0037/init', params).done(function(res) {
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
				// 一括処理
				.on('click', '#btnApproveAtOnce', function() {
					const targets = [], self = this;
					$('#tblSearchResult>tbody input.selectable:checked').each(function() {
						const $tr = $(this).closest('tr');
						targets.push({
							'corporationCode' : $tr.find('[data-field=corporationCode]').first().text(),
							'processId' : $tr.find('[data-field=processId]').first().text(),
							'activityId' : $tr.find('[data-field=activityId]').first().text(),
							'timestampUpdated' : NCI.timestampToLong($tr.find('[data-field=timestampUpdatedProcess]').first().text()),
							'proxyUser' : tray.pager.condition.proxyUser,
							'trayType' : tray.trayType
						});
					});
					if (targets.length) {
						var count = 0;
						let $processingStatus = $('<span id="processingStatus" class="blockUI-content col-xs-12 col-sm-11"><span id="total-count"></span>件中、<span id="current-count"></span>件目を処理中...</span>');
						$processingStatus.find('#total-count').text(targets.length);
						$processingStatus.appendTo('#blockUI');
						showCount(count);
						NCI.openBlockUI();
						clearMessageDetails();
						batchProcessing(targets, ++count, 0, 0);
					} else {
						NCI.addMessage('danger', [NCI.getMessage('MSG0135', 1)]);
					}
				})
				;
		}
	});


	/** 検索実行 */
	function search(pageNo, keepMessage) {
		const $elements = $('#formCondition').find('input, select, textarea');
		if (!Validator.validate($elements, true))
			return false;

		const cond = toCondition($elements, pageNo);
		tray.pager.search(cond, keepMessage).done(function() {
			$('#seach-result').removeClass('hide');
			whenSelectRow();
		});
	};

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
		$('#btnApproveAtOnce').prop('disabled', len === 0);
	}

	/** 一括承認処理 */
	function batchProcessing(targets, count, success, error) {
		if (targets.length == 0) {
			$('#processingStatus').remove();
			NCI.closeBlockUI();
			NCI.addMessage('success', [NCI.getMessage('MSG0271', [success, error])]);
			if (error > 0) {
				const $message = $('#div-alert-success').find('li');
				$message.html('<a>' + $message.text() + '</a>');
				$message.find('a').on('click', function() {
					$('#messageDetailsDialog').modal();
				});
			}
			search(1, true);
			return;
		}
		let params = targets.shift();
		showCount(count);
		NCI.post('/wl0037/batchProcessing', params, true, true)
		.done(function(res) {
			if (res && res.success) {
				success++;
			} else if (res && !res.success) {
				if (res.alertMessage) {
					addMessageDetails(res.alertMessage);
				}
				error++;
			}
		})
		.fail(function(jqXHR, textStatus, errorThrown) {
			error++;
		})
		.always(function() {
			batchProcessing(targets, ++count, success, error);
		})
		;
	}

	function showCount(count) {
		$('#current-count').text(count);
	}

	function addMessageDetails(message) {
		const li = document.createElement('li');
		$(li.cloneNode(false)).html(message).appendTo($('#messageDetails ul'));
	}

	function clearMessageDetails() {
		NCI.clearMessage();
		$('#messageDetails ul').empty();
	}
});
