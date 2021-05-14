$(function() {
	var pager = new Pager($('#seach-result'), '/wm0220/search', search).init();

	var params = {
		corporationCode : NCI.getQueryString("corporationCode"),
		assignRoleCode : NCI.getQueryString("assignRoleCode"),
		messageCds : [ 'MSG0069', 'assignRole' , 'MSG0071', 'MSG0072', 'assignRoleDetail']
	};
	NCI.init("/wm0220/init", params).done(function(res) {
		if (res && res.success) {
			$("#assignRoleCode").prop('disabled', true);
			$('#btnUpdate').prop('disabled', false);
			$('#btnAdd').prop('disabled', false)
			$('#btnDelete').prop('disabled', true);

			// 削除区分の選択肢
			if (res.deleteFlagList) {
				NCI.createOptionTags($('#deleteFlag'), res.deleteFlagList);
			}

			if (res.assignRole) {
				$('input, select, textarea, span', '#inputed').filter('[id]').each(function(i, elem) {
					var val = res.assignRole[elem.id];
					var tagName = elem.tagName, type = elem.type;
					if (tagName === 'INPUT' || tagName === 'SELECT' || tagName === 'TEXTAREA') {
						elem.value = val;
					} else {
						$(elem).text(val);
					}
				});
				search();

				// カレンダー（年月日）
				//	初期状態だとフォーカスアウト時にblankを保存してしまうため位置を修正
				NCI.ymdPicker($('input.ymdPicker'));

				if (res.assignRole.deleteFlag == '1') {
					$('#btnUpdate').prop('disabled', false);
					$("#assignRoleName").prop('disabled', true);
					$("#sortOrder").prop('disabled', true);
				} else {
					$('#btnUpdate').prop('disabled', false);
				}
			}
		}
	});

	$(document)
		// 戻るボタン押下
		.on('click', '#btnBack', function(ev) {
			NCI.redirect("./wm0200.html");
		})
		// 更新ボタン
		.on('click', '#btnUpdate', function(ev) {
			var $root = $('#inputed');
			var $targets = $root.find('input, select, textarea');
			if (!Validator.validate($targets, true))
				return false;

			var msg = NCI.getMessage("MSG0071", NCI.getMessage("assignRole"));
			if (NCI.confirm(msg, function() {
				var params = {
					assignRole : NCI.toObjFromElements($root)
				};
				NCI.post("/wm0220/update", params).done(function(res) {
					if (res && res.success && res.assignRole) {
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
			var assignRoleCode = $tr.find('[data-field=assignRoleCode]').text();
			var seqNoAssignRoleDetail = $tr.find('[data-field=seqNoAssignRoleDetail]').text();
			openEntry(corporationCode, assignRoleCode, seqNoAssignRoleDetail);
		})
		// 追加ボタン
		.on('click', '#btnAdd', function(ev) {
			openEntry($("#corporationCode").val(), $("#assignRoleCode").val(), null);
		})
		// 検索結果の選択チェックボックス
		.on('change', 'input[type=checkbox].selectable', function() {
			var selected = $('input[type=checkbox].selectable:checked').length === 0;
			$('#btnDelete').prop('disabled', selected);
		})
		// 削除ボタン
		.on('click', '#btnDelete', function(ev) {
			var msg = NCI.getMessage("MSG0072", NCI.getMessage("assignRoleDetail"));
			if (NCI.confirm(msg, function() {
				var params = { deleteAssignRoleDetails : [] };
				$('input[type=checkbox].selectable:checked').each(function(i, elem) {
					var $tr = $(elem).closest('tr');
					var assignRoleDetail = {
						corporationCode : $tr.find('[data-field=corporationCode]').text(),
						assignRoleCode : $tr.find('[data-field=assignRoleCode]').text(),
						seqNoAssignRoleDetail : $tr.find('[data-field=seqNoAssignRoleDetail]').text()
					};
					params.deleteAssignRoleDetails.push(assignRoleDetail);
				});
				NCI.post("/wm0220/delete", params).done(function(res) {
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
			cond.sortColumn = 'SEQ_NO_ASSIGN_ROLE_DETAIL';
			cond.sortAsc = true;
		}
		return cond;
	}
});

function openEntry(corporationCode, assignRoleCode, seqNoAssignRoleDetail) {
	NCI.redirect("./wm0230.html?corporationCode=" + corporationCode + "&assignRoleCode=" + assignRoleCode + "&seqNoAssignRoleDetail=" + seqNoAssignRoleDetail);
}

function refresh(corporationCode, assignRoleCode) {
	NCI.redirect("./wm0220.html?corporationCode=" + corporationCode + "&assignRoleCode=" + assignRoleCode);
}
