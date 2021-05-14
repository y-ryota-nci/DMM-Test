$(function() {
	var params = {
		corporationCode : NCI.getQueryString("corporationCode"),
		postCode : NCI.getQueryString("postCode"),
		timestampUpdated : NCI.getQueryString("timestampUpdated"),
		messageCds : [ 'MSG0071', 'MSG0072', 'post' ]
	};
	NCI.init("/mm0431/init", params).done(function(res) {
		if (res && res.success) {

			$('#btnUpdate, #btnDelete').prop('disabled', false);

			if (res.post) {
				$('input, select, textarea, span', '#inputed').filter('[id]').each(function(i, elem) {
					var val = res.post[elem.id];
					var tagName = elem.tagName, type = elem.type;
					if (tagName === 'INPUT' || tagName === 'SELECT' || tagName === 'TEXTAREA') {
						elem.value = val;
					} else {
						$(elem).text(val);
					}
				});
			}

			// カレンダー（年月日）
			//	初期状態だとフォーカスアウト時にblankを保存してしまうため位置を修正
			NCI.ymdPicker($('input.ymdPicker'));

			$(document)
			// 更新ボタン
			.on('click', '#btnUpdate', function(ev) {
				var $root = $('#inputed');
				var $targets = $root.find('input, select, textarea');
				if (!Validator.validate($targets, true))
					return false;

				var msg = NCI.getMessage("MSG0071", NCI.getMessage("post"));
				if (NCI.confirm(msg, function() {
					var params = {
						post : NCI.toObjFromElements($root)
					};
					NCI.post("/mm0431/update", params).done(function(res) {
						if (res && res.success && res.newPost) {
							openEntry(res.newPost.corporationCode, res.newPost.postCode);
						}
					});
				}));
			})
			// 削除ボタン
			.on('click', '#btnDelete', function(ev) {
				var msg = NCI.getMessage("MSG0072", NCI.getMessage("post"));
				if (NCI.confirm(msg, function() {
					var params = {
						corporationCode : $('#corporationCode').val(),
						postCode : $('#postCode').val()
					};
					NCI.post("/mm0431/delete", params).done(function(res) {
						$('#btnBack').click();
					});
				}));
			})
			;
		}
		// 戻るボタン押下
		$('#btnBack').on('click', function(ev) {
			NCI.redirect("./mm0430.html");
		});
	});

	/** 検索実行 */
	function search(pageNo) {
		var cond = createCondition(pageNo);
		pager.search(cond);
		$('#btnDelete').prop('disabled', true);
		$('#seach-result').removeClass('hide');
	}

	/** 画面入力内容から検索条件を生成 */
	function createCondition(pageNo) {
		var $elements = $('#formCondition').find('input, select, textarea');
		var cond = pager.createCondition($elements, pageNo);
		// デフォルトソート条件
		if (!cond.sortColumn) {
			cond.sortColumn = 'POST_CODE, ID';
			cond.sortAsc = true;
		}
		return cond;
	}

	function openEntry(corporationCode, postCode) {
		NCI.redirect("./mm0431.html?corporationCode=" + corporationCode + "&postCode=" + postCode);
	}
});

