$(function() {
	let pager = new Pager($('#seach-result'), '/vd0060/search', search).init();

	let params = { messageCds : ['optionSetting'] };
	NCI.init("/vd0060/init", params).done(function(res) {
		if (res && res.success) {
			NCI.createOptionTags($('#corporationCode'), res.corporations);
			$('#btnAdd').prop('disabled', false)

			// 以前の検索条件を復元できれば再検索する
			if (pager.loadCondition()) {
				search();
			} else {
				$('#corporationCode').val(NCI.loginInfo.corporationCode);
			}

			$(document)
			.on('click', '#btnSearch', function(ev) {
				// 検索ボタン押下
				search(1);
				return false;
			})
			.on('click', 'ul.pagination a', function(ev) {
				// ページ番号リンク押下
				let pageNo = this.getAttribute('data-pageNo');
				search(pageNo);
				return false;
			})
			// リセットボタン押下
			.on('click', '#btnReset', function(ev) {
				$('#formCondition')[0].reset();
			})
			// 検索結果の選択チェックボックス
			.on('click', '#partsOption input[type=checkbox].selectable', function() {
				let noSelect = getSelections().length === 0;
				disableButtonsByCheckbox(noSelect);
			})
			// 検索結果のリンク押下
			.on('click', 'a[data-field]', function(ev) {
				let $tr = $(this).closest('tr');
				let optionId = $tr.find('[data-field=optionId]').val();
				let version  = $tr.find('[data-field=version]').text();
				openEntry(optionId, version);
			})
			// 追加ボタン
			.on('click', '#btnAdd', function(ev) {
				openEntry(null);
			})
			// 削除ボタン
			.on('click', '#btnDelete', function(ev) {
				let msg = NCI.getMessage("MSG0072", NCI.getMessage("optionSetting"));
				if (NCI.confirm(msg, function() {
					let params = { "optionIds" : getSelections() };
					NCI.post("/vd0060/delete", params).done(function(res) {
						search(1, true);
					});
				}));
			})
			;
		}
	});

	/** 検索実行 */
	function search(pageNo, keepMessage) {
		let $targets = $('#formCondition').find('input, select, text');
		if (!Validator.validate($targets, true)) {
			return false;
		}
		let cond = createCondition(pageNo);
		pager.search(cond, keepMessage);
		disableButtonsByCheckbox(true);
		$('#seach-result').removeClass('hide');
	}

	/** 画面入力内容から検索条件を生成 */
	function createCondition(pageNo) {
		let $elements = $('#formCondition').find('input, select, textarea');
		let cond = pager.createCondition($elements, pageNo);
		// デフォルトソート条件
		if (!cond.sortColumn) {
			cond.sortColumn = 'O.OPTION_CODE, O.OPTION_ID';
			cond.sortAsc = true;
		}
		return cond;
	}

	/** 選択行のデータを開く */
	function openEntry(optionId, version) {
		if (optionId)
			NCI.redirect("./vd0062.html?optionId=" + optionId + "&version=" + version);
		else
			NCI.redirect("./vd0061.html");
	}

	/** 検索結果の選択行によるボタン制御 */
	function disableButtonsByCheckbox(disable) {
		$('#btnDelete').prop('disabled', disable);
	}

	/** 選択肢一覧から選択中の行のoptionIdを返す */
	function getSelections() {
		let optionIds = [];
		$('#partsOption input[type=checkbox].selectable:checked').each(function(i, elem) {
			optionIds.push( +elem.value );
		});
		return optionIds;
	}
});

