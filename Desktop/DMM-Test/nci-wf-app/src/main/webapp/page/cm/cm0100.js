$(function() {
	let corpCode = NCI.getQueryString("corporationCode")
	let params = NCI.flushScope( NCI.getQueryString(FLUSH_SCOPE_KEY) );
	let pager = new Pager($('#seach-result'), '/cm0100/search', search).init();

	var initParams = {
		  corporationCode		 : NCI.getQueryString("corporationCode")
		, processDefCode		 : NCI.getQueryString("processDefCode")
		, processDefDetailCode	 : NCI.getQueryString("processDefDetailCode")
		, activityDefCode		 : NCI.getQueryString("activityDefCode")
		, organizationCodeStart	 : NCI.getQueryString("organizationCodeStart")
	}
	NCI.init("/cm0100/init", initParams).done(function(res) {
		if (res && res.success) {
			// カレンダー（年月日）
			NCI.ymdPicker($('input.ymdPicker'));

			if (corpCode != null) {
				$('#corporationCode').val(corpCode);
			} else {
				$('#corporationCode').val(NCI.loginInfo.corporationCode);
			}
			$('#processDefCode').val(NCI.getQueryString("processDefCode"));
			$('#processDefDetailCode').val(NCI.getQueryString("processDefCode"));
			$('#activityDefCode').val(NCI.getQueryString("processDefCode"));
			$('#organizationCodeStart').val(NCI.getQueryString("organizationCodeStart"));

			NCI.createOptionTags($('#changeRoleCode'), res.changeDefList);

			$(document)
			.on('click', '#btnSearch', function() {
				// 検索ボタン押下
				search(1);
				return false;
			})
			.on('click', 'ul.pagination a', function() {
				// ページ番号リンク押下
				var pageNo = this.getAttribute('data-pageNo');
				search(pageNo);
				return false;
			})
			// リセットボタン押下
			.on('click', '#btnReset', function() {
				$('#formCondition')[0].reset();
			})
			// 検索結果の選択ラジオボタン
			.on('change', 'input[type=checkbox][name=chkSelect]', function() {
				let cnt = $('input[type=checkbox][name=chkSelect]:checked').length
				if (cnt > 0) {
					$('#btnSelect').prop('disabled', false);
				} else {
					$('#btnSelect').prop('disabled', true);
				}
			})

			// 戻るボタン
			.on('click', '#btnClose, #btnClose2', function() {
				Popup.close();
			})
			// 選択ボタン
			.on('click', '#btnSelect', function() {
				var entityList = [];
				$('input[name=chkSelect]:checked').each(function(j) {
					var $tr = $(this).parent().parent();
					var entity = {};
					$tr.find('[data-field]').each(function(i, elem) {
						var fieldName = elem.getAttribute('data-field');
						entity[fieldName] = $(elem).text();
					});
					entityList.push(entity);
				});

				// 呼出元へ戻すパラメータがあれば一緒に戻す
				let result = null;
				if (params && params.callbackParam != undefined) {
					result = {users : entityList, callbackParam : params.callbackParam};
				} else {
					result = entityList;
				}
				Popup.close(result);
			})
			;
		}
	});

	/** 検索実行 */
	function search(pageNo) {
		var $targets = $('#formCondition').find('input, select');
		if (!Validator.validate($targets, true))
			return false;

		var cond = createCondition(pageNo);
		pager.search(cond);
		$('#btnSelect').prop('disabled', true);
		$('#seach-result').removeClass('invisible');
	}

	/** 画面入力内容から検索条件を生成 */
	function createCondition(pageNo) {
		var $elements = $('#formCondition').find('input, select, textarea');
		var cond = pager.createCondition($elements, pageNo);
		// デフォルトソート条件
		if (!cond.sortColumn) {
			cond.sortColumn = 'USER_ADDED_INFO, ID';
			cond.sortAsc = true;
		}
		return cond;
	}
});

