$(function() {
	var pager = new Pager($('#seach-result'), '/rm0902/search', search).init();

	var params = {
		corporationCode : NCI.getQueryString("corporationCode"),
		menuRoleCode : NCI.getQueryString("menuRoleCode"),
		messageCds : [ 'MSG0069', 'menuRole' , 'MSG0071', 'MSG0072', 'menuRoleDetail']
	};
	NCI.init("/rm0902/init", params).done(function(res) {
		if (res && res.success) {
			$("#menuRoleCode").prop('disabled', true);
			$('#btnUpdate').prop('disabled', false);
			$('#btnAdd').prop('disabled', false)
			$('#btnDelete').prop('disabled', true);

			// 削除区分の選択肢
			if (res.deleteFlagList) {
				NCI.createOptionTags($('#deleteFlag'), res.deleteFlagList);
			}

			if (res.menuRole) {
				$('input, select, textarea, span', '#inputed').filter('[id]').each(function(i, elem) {
					var val = res.menuRole[elem.id];
					var tagName = elem.tagName, type = elem.type;
					if (tagName === 'INPUT' || tagName === 'SELECT' || tagName === 'TEXTAREA') {
						elem.value = val;
					} else {
						$(elem).text(val);
					}
				});
				search();

				if (res.menuRole.deleteFlag == '1') {
					$('#btnUpdate').prop('disabled', false);
					$("#menuRoleName").prop('disabled', true);
					$("#sortOrder").prop('disabled', true);
				} else {
					$('#btnUpdate').prop('disabled', false);
				}
			}
		}
	});

	$(document)
		// 更新ボタン
		.on('click', '#btnUpdate', function(ev) {
			var $root = $('#inputed');
			var $targets = $root.find('input, select, textarea');
			if (!Validator.validate($targets, true))
				return false;

			var msg = NCI.getMessage("MSG0071", NCI.getMessage("menuRole"));
			if (NCI.confirm(msg, function() {
				var params = {
					menuRole : NCI.toObjFromElements($root)
				};
				NCI.post("/rm0902/update", params).done(function(res) {
					if (res && res.success && res.menuRole) {
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
			var menuRoleCode = $tr.find('[data-field=menuRoleCode]').text();
			var seqNoMenuRoleDetail = $tr.find('[data-field=seqNoMenuRoleDetail]').text();
			openEntry(corporationCode, menuRoleCode, seqNoMenuRoleDetail);
		})
		// 追加ボタン
		.on('click', '#btnAdd', function(ev) {
			openEntry($("#corporationCode").val(), $("#menuRoleCode").val(), null);
		})
		// 検索結果の選択チェックボックス
		.on('change', 'input[type=checkbox].selectable', function() {
			var selected = $('input[type=checkbox].selectable:checked').length === 0;
			$('#btnDelete').prop('disabled', selected);
		})
		// 削除ボタン
		.on('click', '#btnDelete', function(ev) {
			var msg = NCI.getMessage("MSG0072", NCI.getMessage("menuRoleDetail"));
			if (NCI.confirm(msg, function() {
				var params = {
						corporationCode : $("#corporationCode").val(),
						menuRoleCode : $("#menuRoleCode").val(),
						deleteMenuRoleDetails : []
				};
				$('input[type=checkbox].selectable:checked').each(function(i, elem) {
					var $tr = $(elem).closest('tr');
					var menuRoleDetail = {
						corporationCode : $tr.find('[data-field=corporationCode]').text(),
						menuRoleCode : $tr.find('[data-field=menuRoleCode]').text(),
						seqNoMenuRoleDetail : $tr.find('[data-field=seqNoMenuRoleDetail]').text()
					};
					params.deleteMenuRoleDetails.push(menuRoleDetail);
				});
				NCI.post("/rm0902/delete", params).done(function(res) {
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
			cond.sortColumn = 'SEQ_NO_MENU_ROLE_DETAIL';
			cond.sortAsc = true;
		}
		return cond;
	}
});

function openEntry(corporationCode, menuRoleCode, seqNoMenuRoleDetail) {
	NCI.redirect("./rm0903.html?corporationCode=" + corporationCode + "&menuRoleCode=" + menuRoleCode + "&seqNoMenuRoleDetail=" + seqNoMenuRoleDetail);
}

function refresh(corporationCode, menuRoleCode) {
	NCI.redirect("./rm0902.html?corporationCode=" + corporationCode + "&menuRoleCode=" + menuRoleCode);
}
