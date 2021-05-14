$(function() {
	var params = {
		corporationCode : NCI.getQueryString("corporationCode"),
		messageCds : [ 'MSG0069', 'lookupType' ]
	};
	NCI.init("/mm0091/init", params).done(function(res) {
		if (res && res.success) {
			// カレンダー（年月日）
			NCI.ymdPicker($('input.ymdPicker'));

			$('#btnRegister').prop('disabled', false);

			if (res.updateFlags) {
				NCI.createOptionTags($('#updateFlag'), res.updateFlags);
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
			}

			$(document)
			// 戻るボタン押下
			.on('click', '#btnBack', function(ev) {
				NCI.redirect("./mm0090.html");
			})
			// 登録ボタン
			.on('click', '#btnRegister', function(ev) {
				var $root = $('#inputed');
				var $targets = $root.find('input, select, textarea');
				if (!Validator.validate($targets, true))
					return false;

				var msg = NCI.getMessage("MSG0069", NCI.getMessage("lookupType"));
				if (NCI.confirm(msg, function() {
					var params = {
						lookupType : NCI.toObjFromElements($root)
					};
					NCI.post("/mm0091/insert", params).done(function(res) {
						if (res && res.success && res.lookupType) {
							$('#btnRegister').hide();
							$("#lookupTypeCode").prop('disabled', true);
							$("#lookupTypeName").prop('disabled', true);
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
			cond.sortColumn = 'LOOKUP_TYPE_CODE';
			cond.sortAsc = true;
		}
		return cond;
	}

	function openEntry(corporationCode, lookupTypeCode) {
		NCI.redirect("./mm0091.html?corporationCode=" + corporationCode + "&lookupTypeCode=" + lookupTypeCode);
	}
});

