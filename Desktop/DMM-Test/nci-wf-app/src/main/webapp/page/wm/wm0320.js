$(function() {
	var pager = new Pager($('#seach-result'), '/wm0320/search', search).init();

	var params = {
		corporationCode : NCI.getQueryString("corporationCode"),
		changeRoleCode : NCI.getQueryString("changeRoleCode"),
		messageCds : [ 'MSG0069', 'changeRole' , 'MSG0071', 'MSG0072', 'changeRoleDetail']
	};
	NCI.init("/wm0320/init", params).done(function(res) {
		if (res && res.success) {
			$("#changeRoleCode").prop('disabled', true);
			$('#btnUpdate').prop('disabled', false);
			$('#btnAdd').prop('disabled', false)

			// 削除区分の選択肢
			if (res.deleteFlagList) {
				NCI.createOptionTags($('#deleteFlag'), res.deleteFlagList);
			}

			if (res.changeRole) {
				$('input, select, textarea, span', '#inputed').filter('[id]').each(function(i, elem) {
					var val = res.changeRole[elem.id];
					var tagName = elem.tagName, type = elem.type;
					if (tagName === 'INPUT' || tagName === 'SELECT' || tagName === 'TEXTAREA') {
						elem.value = val;
					} else {
						$(elem).text(val);
					}
				});
				search();

				if (res.changeRole.deleteFlag == '1') {
					$('#btnUpdate').prop('disabled', false);
					$("#changeRoleName").prop('disabled', true);
					$("#sortOrder").prop('disabled', true);
				} else {
					$('#btnUpdate').prop('disabled', false);
				}
			}
			// カレンダー（年月日）
			//	存在しなかったため追記 12/11
			NCI.ymdPicker($('input.ymdPicker'));
		}
	});

	$(document)
		// 戻るボタン押下
		.on('click', '#btnBack', function(ev) {
			NCI.redirect("./wm0300.html");
		})
		// 更新ボタン
		.on('click', '#btnUpdate', function(ev) {
			var $root = $('#inputed');
			var $targets = $root.find('input, select, textarea');
			if (!Validator.validate($targets, true))
				return false;

			var msg = NCI.getMessage("MSG0071", NCI.getMessage("changeRole"));
			if (NCI.confirm(msg, function() {
				var params = {
					changeRole : NCI.toObjFromElements($root)
				};
				NCI.post("/wm0320/update", params).done(function(res) {
					if (res && res.success && res.changeRole) {
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
			var changeRoleCode = $tr.find('[data-field=changeRoleCode]').text();
			var seqNoChangeRoleDetail = $tr.find('[data-field=seqNoChangeRoleDetail]').text();
			openEntry(corporationCode, changeRoleCode, seqNoChangeRoleDetail);
		})
		// 追加ボタン
		.on('click', '#btnAdd', function(ev) {
			openEntry($("#corporationCode").val(), $("#changeRoleCode").val(), null);
		})
		// 検索結果の選択チェックボックス
		.on('change', 'input[type=checkbox].selectable', function() {
			var selected = $('input[type=checkbox].selectable:checked').length === 0;
			$('#btnDelete').prop('disabled', selected);
		})
		// 削除ボタン
		.on('click', '#btnDelete', function(ev) {
			var msg = NCI.getMessage("MSG0072", NCI.getMessage("changeRoleDetail"));
			if (NCI.confirm(msg, function() {
				var params = { deleteChangeRoleDetails : [] };
				$('input[type=checkbox].selectable:checked').each(function(i, elem) {
					var $tr = $(elem).closest('tr');
					var changeRoleDetail = {
						corporationCode : $tr.find('[data-field=corporationCode]').text(),
						changeRoleCode : $tr.find('[data-field=changeRoleCode]').text(),
						seqNoChangeRoleDetail : $tr.find('[data-field=seqNoChangeRoleDetail]').text()
					};
					params.deleteChangeRoleDetails.push(changeRoleDetail);
				});
				NCI.post("/wm0320/delete", params).done(function(res) {
					search(1);
				});
			}));
		})

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
			cond.sortColumn = 'SEQ_NO_CHANGE_ROLE_DETAIL';
			cond.sortAsc = true;
		}
		return cond;
	}
});

function openEntry(corporationCode, changeRoleCode, seqNoChangeRoleDetail) {
	NCI.redirect("./wm0330.html?corporationCode=" + corporationCode + "&changeRoleCode=" + changeRoleCode + "&seqNoChangeRoleDetail=" + seqNoChangeRoleDetail);
}

function refresh(corporationCode, changeRoleCode) {
	NCI.redirect("./wm0320.html?corporationCode=" + corporationCode + "&changeRoleCode=" + changeRoleCode);
}
