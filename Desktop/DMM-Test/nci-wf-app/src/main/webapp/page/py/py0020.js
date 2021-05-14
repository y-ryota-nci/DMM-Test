$(function() {
	// トレイ設定と検索処理を管理するコントローラー
	const tray = new TRAY('/py0020/search', $('#formCondition'), $('#seach-result'), $('#tray-template'), search);
	const params = { "messageCds" : ['sendback', 'pullback', 'pullforward', 'MSG0135', 'MSG0271']};
	NCI.init('/py0020/init', params).done(function(res) {
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
					const companyCd = $tr.find('[data-field=corporationCode]').text();
					const payNo = $tr.find('[data-field=applicationNo]').text();
					const params = { companyCd: companyCd, payNo: payNo};
					NCI.init("/py0020/getScreenCode", params).done(function(res) {
						if (res && res.success) {
							const screenCode = res.screenCode;
							const advpayFg = res.advpayFg;
							openEntry(companyCd, payNo, screenCode, advpayFg);
						}
					});
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
				// 一括処理
				.on('click', '#btnApproveAtOnce', function() {
					const targets = [], self = this;
					$('#tblSearchResult>tbody input.selectable').each(function() {
						const $tr = $(this).closest('tr');
						targets.push({
							'corporationCode' : $tr.find('[data-field=corporationCode]').first().text(),
							'processId' : $tr.find('[data-field=processId]').first().text(),
							'activityId' : $tr.find('[data-field=activityId]').first().text(),
							'timestampUpdated' : NCI.timestampToLong($tr.find('[data-field=timestampUpdatedProcess]').first().text()),
							'proxyUser' : tray.pager.condition.proxyUser,
							'trayType' : tray.trayType,
							'actionType' : ($(this).prop('checked') ? ActionType.SENDBACK : ActionType.NORMAL)
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
		tray.pager.search(cond, keepMessage).done(function(res) {
			$('#seach-result').removeClass('hide');
			$('#btnApproveAtOnce').prop('disabled', res.allCount == 0);
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
		NCI.post('/py0020/execute', params, true, true)
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

	/** 明細行を開く */
	function openEntry(companyCd, payNo, screenCode, advpayFg) {
		NCI.flushScope('_vd0330', {
			'keys' : { 'companyCd' : companyCd, 'payNo' : payNo, 'advpayFg': advpayFg },
			'corporationCode' : companyCd,
			'screenCode' : screenCode,
			'screenName' : (advpayFg == '1' ? '管理_前払登録' : '管理_支払登録'),	// screenNameを指定していればVD0330でそれが画面名として使われる
			'backUrl' : '../py/py0020.html',
			'dcId' : 2						// 指定されていればその表示条件IDを使う
		});
		NCI.redirect('../vd/vd0330.html');
	}
});
