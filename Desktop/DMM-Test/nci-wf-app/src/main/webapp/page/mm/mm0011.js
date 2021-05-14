$(function() {
	var params = {
		corporationCode : NCI.getQueryString("corporationCode"),
		messageCds : [ 'MSG0069', 'lookupGroup' ]
	};
	NCI.init("/mm0011/init", params).done(function(res) {
		if (res && res.success) {
			// カレンダー（年月日）
			NCI.ymdPicker($('input.ymdPicker'));

			$('#btnRegister').prop('disabled', false);

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
			}

			$(document)
			// 戻るボタン押下
			.on('click', '#btnBack', function(ev) {
				NCI.redirect("./mm0010.html");
			})
			// 登録ボタン
			.on('click', '#btnRegister', function(ev) {
				var $root = $('#inputed');
				var $targets = $root.find('input, select, textarea');
				if (!Validator.validate($targets, true))
					return false;

				var msg = NCI.getMessage("MSG0069", NCI.getMessage("lookupGroup"));
				if (NCI.confirm(msg, function() {
					var params = {
						lookupGroup : NCI.toObjFromElements($root)
					};
					NCI.post("/mm0011/insert", params).done(function(res) {
						if (res && res.success && res.lookupGroup) {
							$('#btnRegister').hide();
							$("#lookupGroupId").prop('disabled', true);
							$("#lookupGroupName").prop('disabled', true);
							$("#sortOrder").prop('disabled', true);
						}
					});
				}));
			})
			;
		}
	});

	/** 検索実行 */
	function search(pageNo) {
		var cond = createCondition(pageNo);
		pager.search(cond);
		$('#seach-result').removeClass('hide');
	}

	/** 画面入力内容から検索条件を生成 */
	function createCondition(pageNo) {
		var $elements = $('#formCondition').find('input, select, textarea');
		var cond = pager.createCondition($elements, pageNo);
		// デフォルトソート条件
		if (!cond.sortColumn) {
			cond.sortColumn = 'LOOKUP_GROUP_ID, ID';
			cond.sortAsc = true;
		}
		return cond;
	}

	function openEntry(corporationCode, lookupGroupId) {
		NCI.redirect("./mm0011.html?corporationCode=" + corporationCode + "&lookupGroupId=" + lookupGroupId);
	}
});

