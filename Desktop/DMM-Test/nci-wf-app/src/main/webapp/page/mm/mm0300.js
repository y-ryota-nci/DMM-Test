$(function() {
	var pager = new Pager($('#seach-result'), '/mm0300/search', search).init();
	pager.responsiveTable.modifyTR = function($tr, rowIndex, entity, labels, ignores) {
		$tr.find('input.selectable').prop('disabled', false);
	};

	var $branchUpdate = $('#branchUpdate');

	var params = {
		messageCds : ['MSG0011']
	};
	NCI.init("/mm0300/init", params).done(function(res) {
		if (res && res.success) {
			// カレンダー（年月日）
			NCI.ymdPicker($('input.ymdPicker'));
			NCI.createOptionTags($('#deleteFlag'), res.deleteFlags);

			$('#btnAddition').prop('disabled', false)

			// 以前の検索条件を復元
			if (pager.loadCondition()) {
				search();
			} else {
				$('#deleteFlag').val('0');
			};

			$(document).on('click', '#btnSearch', function(ev) {
				// 検索ボタン押下
				search(1);
				return false;
			})
			.on('click', 'ul.pagination a', function(ev) {
				// ページ番号リンク押下
				var pageNo = this.getAttribute('data-pageNo');
				search(pageNo);
				return false;
			})
			// リセットボタン押下
			.on('click', '#btnReset', function(ev) {
				$('#formCondition')[0].reset();
			})
			// 検索結果のリンク押下
			.on('click', 'a.lnkOpenEdit', function(ev) {
				var $tr = $(this).closest('tr');
				var corporationCode = $tr.find('[data-field=corporationCode]').text();
				var processDefCode = $tr.find('[data-field=processDefCode]').text();
				var processDefDetailCode = $tr.find('[data-field=processDefDetailCode]').text();
				var timestampUpdated = $tr.find('[data-field=timestampUpdated]').text();
				openEdit(corporationCode, processDefCode, processDefDetailCode, timestampUpdated);
			})
			.on('click', 'button.btnInformationSharer', function(ev) {
				var $tr = $(this).closest('tr');
				var corporationCode = $tr.find('[data-field=corporationCode]').text();
				var processDefCode = $tr.find('[data-field=processDefCode]').text();
				var processDefDetailCode = $tr.find('[data-field=processDefDetailCode]').text();
				openInformationSharerDef(corporationCode, processDefCode, processDefDetailCode);
			})
			// 追加ボタン
			.on('click', '#btnAddition', function(ev) {
				NCI.redirect("./mm0301.html");
			})
			// 検索結果チェックボックスを選択
			.on('change', 'tbody>tr>th>input.selectable', function() {
				setButtonEnable();
			})
			// ダウンロードボタン
			.on('click', '#btnDownload', function() {
				const params = { 'procList' : [] };
				$('tbody>tr>th>input.selectable:checked').each(function() {
					const $tr = $(this).closest('tr');
					params.procList.push({
						'corporationCode' : $tr.find('[data-field="corporationCode"]').text(),
						'processDefCode' : $tr.find('[data-field="processDefCode"]').text(),
						'processDefDetailCode' : $tr.find('[data-field="processDefDetailCode"]').text()
					});
				});
				if (params.procList.length) {
					NCI.download('/mm0300/downloadZip', params);
				}
			})
			// 枝番更新
			.on('click', '#btnBranchUpdate', function() {
				const $tr = $('tbody>tr>th>input.selectable:checked').eq(0).closest('tr');
				if ($tr.length > 0) {
					const corporationCode = $tr.find('[data-field="corporationCode"]').text();
					const processDefCode = $tr.find('[data-field="processDefCode"]').text();
					const processDefDetailCode = $tr.find('[data-field="processDefDetailCode"]').text();
					const processDefName = $tr.find('[data-field="processDefName"]').text();
					const validStartDate = $tr.find('[data-field="validStartDate"]').text();

					$('#branchCorporationCode').text(corporationCode);
					$('#branchProcessDefCode').text(processDefCode);
					$('#branchProcessDefDetailCode').text(processDefDetailCode);
					$('#branchProcessDefName').val(processDefName);
					$('#branchValidStartDate').val(validStartDate);
					$branchUpdate.modal();
				}
			})
			.on('click', '#btnUpdateOk', function(e) {
				if (!Validator.validate($('#updateValidStartDate'), true)) return false;
				const from = NCI.getPureValue(Validator.getValue($('#branchValidStartDate')));
				const value = NCI.getPureValue(Validator.getValue($('#updateValidStartDate')));
				if (Validator._dateTo(value, from)) {
					Validator.showBalloon($('#updateValidStartDate'), NCI.getMessage('MSG0011', [NCI.getMessage('updateValidStartDate'), NCI.getMessage('validStartDate')]));
					return false;
				}

				var params = NCI.toObjFromElements($branchUpdate, ['branchProcessDefName', 'branchValidStartDate']);
				NCI.post('/mm0300/updateBranch', params)
				.done(function(res) {
					if (res && res.success) {
						search(null, true);
					}
				})
				.always(function() {
					$branchUpdate.modal('hide');
				})
				;
				return true;
			})
			.on('click', '#btnUpdateCancel', function(e) {
				$branchUpdate.modal('hide');
			})
			.on('click', '#btnSelectAll', function(){
				selectAll(true);
			})
			.on('click', '#btnCancelAll', function(){
				selectAll(false);
			})
			;
			$branchUpdate.on('hidden.bs.modal', function() {
				$('#branchCorporationCode,#branchProcessDefCode,#branchProcessDefDetailCode').text('');
				$('#branchProcessDefName,#branchValidStartDate,#updateValidStartDate').val('');
				Validator.hideBalloon();
			});
		}
	});

	/** 検索実行 */
	function search(pageNo, keepMessage) {
		var $targets = $('#formCondition').find('input, select, text');
		if (!Validator.validate($targets, true)) {
			return false;
		}
		var cond = createCondition(pageNo);
		pager.search(cond, keepMessage).done(function() {
			$('#btnBranchUpdate,#btnDownload').prop('disabled', true);
			$('#seach-result').removeClass('hide');
		});
	}

	/** 画面入力内容から検索条件を生成 */
	function createCondition(pageNo) {
		var $elements = $('#formCondition').find('input, select, textarea');
		var cond = pager.createCondition($elements, pageNo);
		// デフォルトソート条件
		if (!cond.sortColumn) {
			cond.sortColumn = 'PROCESS_DEF_CODE, PROCESS_DEF_DETAIL_CODE, ID';
			cond.sortAsc = true;
		}
		return cond;
	}

	function openEdit(corporationCode, processDefCode, processDefDetailCode, timestampUpdated) {
		NCI.redirect("./mm0302.html" +
				"?corporationCode=" + corporationCode +
				"&processDefCode=" + processDefCode +
				"&processDefDetailCode=" + processDefDetailCode +
				"&timestampUpdated=" + NCI.timestampToLong(timestampUpdated));
	}

	function openInformationSharerDef(corporationCode, processDefCode, processDefDetailCode) {
		NCI.redirect("./mm0100.html" +
				"?corporationCode=" + corporationCode +
				"&processDefCode=" + processDefCode +
				"&processDefDetailCode=" + processDefDetailCode);
	}

	/** 全選択／全解除 */
	function selectAll(checked) {
		$('tbody>tr>th>input.selectable').prop('checked', checked);
		setButtonEnable();
	}

	/** 選択行数によるボタン制御 */
	function setButtonEnable() {
		const count = $('tbody>tr>th>input.selectable:checked').length;
		$('#btnBranchUpdate').prop('disabled', count !== 1);
		$('#btnDownload').prop('disabled', count === 0);
	}

	/** 全選択／全解除 */
	function selectAll(checked) {
		$('tbody input[type=checkbox].selectable').prop('checked', checked);
		setButtonEnable();
	}
});

