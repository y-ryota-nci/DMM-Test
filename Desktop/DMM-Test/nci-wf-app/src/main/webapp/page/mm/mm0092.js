$(function() {
	var pager = new Pager($('#seach-result'), '/mm0092/search', search).init();

	var params = {
		corporationCode : NCI.getQueryString("corporationCode"),
		lookupTypeCode : NCI.getQueryString("lookupTypeCode"),
		timestampUpdated : NCI.getQueryString("timestampUpdated"),
		messageCds : [ 'MSG0069', 'lookupType' , 'MSG0071', 'MSG0072', 'lookup']
	};
	NCI.init("/mm0092/init", params).done(function(res) {
		if (res && res.success) {
			$("#lookupTypeCode").prop('disabled', true);
			$('#btnUpdate').prop('disabled', false);
			$('#btnAdd').prop('disabled', false)
			$('#btnDelete').prop('disabled', false);

			// 選択肢
			if (res.deleteFlagList) {
				NCI.createOptionTags($('#deleteFlag'), res.deleteFlagList);
			}
			if (res.updateFlagList) {
				NCI.createOptionTags($('#updateFlag'), res.updateFlagList);
			}

			if (res.lookupType) {
				$('input, select, textarea, span', '#inputed').filter('[id]').each(function(i, elem) {
					var val = res.lookupType[elem.id];
					var tagName = elem.tagName, type = elem.type;
					if (tagName === 'INPUT' || tagName === 'SELECT' || tagName === 'TEXTAREA') {
						elem.value = val;
					} else {
						$(elem).text(val);
					}
				});
				search();

				if (res.lookupType.updateFlag == '0') {
					$('#btnUpdate').hide();
					$('#btnAdd').hide();
					$('#btnDelete').hide();
					$("#lookupTypeName").prop('disabled', true);
					$("#sortOrder").prop('disabled', true);
					$("#updateFlag").prop('disabled', true);
					$("#deleteFlag").prop('disabled', true);
				} else if (res.lookupType.deleteFlag == '1') {
					$('#btnUpdate').prop('disabled', false);
					$("#lookupTypeName").prop('disabled', true);
					$("#sortOrder").prop('disabled', true);
					$("#updateFlag").prop('disabled', true);
				} else {
					$('#btnUpdate').prop('disabled', false);
				}
			}

			$(document)
			// 更新ボタン
			.on('click', '#btnUpdate', function(ev) {
				var $root = $('#inputed');
				var $targets = $root.find('input, select, textarea');
				if (!Validator.validate($targets, true))
					return false;

				var msg = NCI.getMessage("MSG0071", NCI.getMessage("lookupType"));
				if (NCI.confirm(msg, function() {
					var params = {
						lookupType : NCI.toObjFromElements($root)
					};
					NCI.post("/mm0092/update", params).done(function(res) {
						if (res && res.success && res.lookupType) {
//							refresh(res.lookupType.corporationCode, res.lookupType.lookupTypeCode);
						}
					});
				}));
			})
			.on('click', 'ul.pagination a', function(ev) {
				// ページ番号リンク押下
				var pageNo = this.getAttribute('data-pageNo');
				search(pageNo);
				return false;
			})
			// 検索結果のリンク押下
			.on('click', 'a[data-field]', function(ev) {
				var $tr = $(this).closest('tr');
				var corporationCode = $tr.find('[data-field=corporationCode]').text();
				var lookupTypeCode = $tr.find('[data-field=lookupTypeCode]').text();
				var lookupCode = $tr.find('[data-field=lookupCode]').text();
				var timestampUpdated = $tr.find('[data-field=timestampUpdated]').text();
				openEntry(corporationCode, lookupTypeCode, lookupCode, timestampUpdated);
			})
			// 追加ボタン
			.on('click', '#btnAdd', function(ev) {
				openEntry($("#corporationCode").val(), $("#lookupTypeCode").val());
			})
			// 検索結果の選択チェックボックス
			.on('change', 'input[type=checkbox].selectable', function() {
				var selected = $('input[type=checkbox].selectable:checked').length === 0;
				$('#btnDelete').prop('disabled', selected);
			})
			// 削除ボタン
			.on('click', '#btnDelete', function(ev) {
				var msg = NCI.getMessage("MSG0072", NCI.getMessage("lookup"));
				if (NCI.confirm(msg, function() {
					var params = { deleteLookups : [] };
					$('input[type=checkbox].selectable:checked').each(function(i, elem) {
						var $tr = $(elem).closest('tr');
						var lookup = {
							corporationCode : $tr.find('[data-field=corporationCode]').text(),
							lookupTypeCode : $tr.find('[data-field=lookupTypeCode]').text(),
							lookupCode : $tr.find('[data-field=lookupCode]').text()
						};
						params.deleteLookups.push(lookup);
					});
					NCI.post("/mm0092/delete", params).done(function(res) {
						search(1);
					});
				}));
			})
			;
		}
		// 戻るボタン押下
		$('#btnBack').on('click', function(ev) {
			NCI.redirect("./mm0090.html");
		})
	});

	/** 検索実行 */
	function search(pageNo) {
		var cond = createCondition(pageNo);
		pager.search(cond);
		$('#seach-result').removeClass('hide');
	}

	/** 画面入力内容から検索条件を生成 */
	function createCondition(pageNo) {
		var $elements = $('#inputed').find('.searchCond');
		var cond = pager.createCondition($elements, pageNo);
		// デフォルトソート条件
		if (!cond.sortColumn) {
			cond.sortColumn = 'LOOKUP_CODE';
			cond.sortAsc = true;
		}
		return cond;
	}

	function openEntry(corporationCode, lookupTypeCode, lookupCode, timestampUpdated) {
		NCI.redirect("./mm0093.html?corporationCode=" + corporationCode +
				"&lookupTypeCode=" + lookupTypeCode +
				"&lookupCode=" + lookupCode +
				"&timestampUpdated=" + NCI.timestampToLong(timestampUpdated));
	}

	function refresh(corporationCode, lookupTypeCode) {
		NCI.redirect("./mm0092.html?corporationCode=" + corporationCode + "&lookupTypeCode=" + lookupTypeCode);
	}
});

