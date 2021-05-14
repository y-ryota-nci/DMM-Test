$(function() {
	var pager = new Pager($('#seach-result'), '/mm0012/search', search).init();

	var params = {
		corporationCode : NCI.getQueryString("corporationCode"),
		lookupGroupId : NCI.getQueryString("lookupGroupId"),
		timestampUpdated : NCI.getQueryString("timestampUpdated"),
		messageCds : [ 'MSG0069', 'lookupGroup' , 'MSG0071', 'MSG0072', 'lookup']
	};
	NCI.init("/mm0012/init", params).done(function(res) {
		if (res && res.success) {
			$("#lookupGroupId").prop('disabled', true);
			$('#btnUpdate').prop('disabled', false);
			$('#btnAdd').prop('disabled', false)
			$('#btnDelete').prop('disabled', false);

			// 削除区分の選択肢
			if (res.deleteFlagList) {
				NCI.createOptionTags($('#deleteFlag'), res.deleteFlagList);
			}

			if (res.lookupGroup) {
				$('input, select, textarea, span', '#inputed').filter('[id]').each(function(i, elem) {
					var val = res.lookupGroup[elem.id];
					var tagName = elem.tagName, type = elem.type;
					if (tagName === 'INPUT' || tagName === 'SELECT' || tagName === 'TEXTAREA') {
						elem.value = val;
					} else {
						$(elem).text(val);
					}
				});
				search();

				if (res.lookupGroup.deleteFlag == '1') {
					$('#btnUpdate').prop('disabled', false);
					$("#lookupGroupName").prop('disabled', true);
					$("#sortOrder").prop('disabled', true);
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

				var msg = NCI.getMessage("MSG0071", NCI.getMessage("lookupGroup"));
				if (NCI.confirm(msg, function() {
					var params = {
						lookupGroup : NCI.toObjFromElements($root)
					};
					NCI.post("/mm0012/update", params).done(function(res) {
						if (res && res.success && res.lookupGroup) {
//							refresh(res.lookupGroup.corporationCode, res.lookupGroup.lookupGroupId);
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
				var lookupGroupId = $tr.find('[data-field=lookupGroupId]').text();
				var lookupId = $tr.find('[data-field=lookupId]').text();
				var timestampUpdated = $tr.find('[data-field=timestampUpdated]').text();
				openEntry(corporationCode, lookupGroupId, lookupId, timestampUpdated);
			})
			// 追加ボタン
			.on('click', '#btnAdd', function(ev) {
				openEntry($("#corporationCode").val(), $("#lookupGroupId").val());
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
							lookupGroupId : $tr.find('[data-field=lookupGroupId]').text(),
							lookupId : $tr.find('[data-field=lookupId]').text()
						};
						params.deleteLookups.push(lookup);
					});
					NCI.post("/mm0012/delete", params).done(function(res) {
						search(1);
					});
				}));
			})
			;
		}
		// 戻るボタン押下
		$('#btnBack').on('click', function(ev) {
			NCI.redirect("./mm0010.html");
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
			cond.sortColumn = 'LOOKUP_ID, SCREEN_LOOKUP_ID';
			cond.sortAsc = true;
		}
		return cond;
	}

	function openEntry(corporationCode, lookupGroupId, lookupId, timestampUpdated) {
		NCI.redirect("./mm0013.html?corporationCode=" + corporationCode +
				"&lookupGroupId=" + lookupGroupId +
				"&lookupId=" + lookupId +
				"&timestampUpdated=" + NCI.timestampToLong(timestampUpdated));
	}

	function refresh(corporationCode, lookupGroupId) {
		NCI.redirect("./mm0012.html?corporationCode=" + corporationCode + "&lookupGroupId=" + lookupGroupId);
	}
});

