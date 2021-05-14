$(function() {
	var scopeKey = NCI.getQueryString(FLUSH_SCOPE_KEY);
	var parentParams = NCI.flushScope(scopeKey);
	var processDef = parentParams.processDef

	var params = {
		messageCds : ['informationSharingPerson']
	};

	/** 検索実行 */
	var search = function(pageNo) {
		var $targets = $('#formCondition').find('input');
		if (!Validator.validate($targets, true)) {
			return false;
		}
		var cond = createCondition(pageNo);
		pager.search(cond);
		$('#seach-result').removeClass('hide');
		$('#btnCreate').prop('disabled', true);
	};

	/** 画面入力内容から検索条件を生成 */
	var createCondition = function(pageNo) {
		var $elements = $('#formCondition').find('input');
		var cond = pager.createCondition($elements, pageNo);
		// デフォルトソート条件
		if (!cond.sortColumn) {
			cond.sortColumn = 'ASSIGN_ROLE_CODE';
			cond.sortAsc = true;
		}
		return cond;
	};

	var pager = new Pager($('#seach-result'), '/mm0101/search', search).init();

	NCI.init('/mm0101/init', params).done(function(res) {
		if (res && res.success) {
			// カレンダー（年月日）
			NCI.ymdPicker($('input.ymdPicker'));
			$('#btnCreate').prop('disabled', true);

			NCI.createOptionTags($('select.informationSharerType'), res.informationSharerTypes);
			$('select.informationSharerType').prop('selectedIndex', 0);

			// 以前の検索条件を復元
			if (pager.loadCondition()) {
				search();
			};

			$(document)
			.on('click', '#btnSearch', function(ev) {
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
			.on('change', '#seach-result tbody input[type=checkbox].selectable', function() {
				var checked = $('#seach-result tbody input[type=checkbox].selectable:checked').length === 0;
				$('#btnCreate').prop('disabled', checked);
			})
			// 追加ボタン
			.on('click', '#btnCreate', function(ev) {
				var msg = NCI.getMessage('MSG0069', NCI.getMessage('informationSharingPerson'));
				NCI.confirm(msg, function() {
					var informationSharerDefs = [];
					$('#seach-result tbody input[type=checkbox].selectable:checked').each(function(i, e) {
						var $tr = $(this).closest('tr');
						informationSharerDefs.push({
							corporationCode: processDef.corporationCode,
							processDefCode: processDef.processDefCode,
							processDefDetailCode: processDef.processDefDetailCode,
							informationSharerType: $tr.find('select').val(),
							corporationCodeAssign: $tr.find('[data-field=corporationCode]').text(),
							assignRoleCode: $tr.find('[data-field=assignRoleCode]').text(),
							expressionInfoSharerType: '3',
							displayFlag: $tr.find('input[type=checkbox].displayFlag').prop('checked') ? '1' : '0'
						});
					});
					NCI.post('/mm0101/create', {informationSharerDefs: informationSharerDefs}).done(function(res) {
						if (res && res.success) {
							Popup.close(res);
						}
					});
				});
			})
			;
		}
	});

	$(document)
	// 戻るボタン押下
	.on('click', 'button.btnClose', function(ev) {
		Popup.close();
	})
	;

});

