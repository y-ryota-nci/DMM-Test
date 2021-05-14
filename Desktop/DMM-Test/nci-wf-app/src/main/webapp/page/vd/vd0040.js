$(function() {
	let pager = new Pager($('#seach-result'), '/vd0040/search', search).init();

	let params = { messageCds : ['screenProcessInfo'] };
	NCI.init("/vd0040/init", params).done(function(res) {
		if (res && res.success) {
			NCI.createOptionTags($('#corporationCode'), res.corporations);
			NCI.createOptionTags($('#processDefCode'), res.processDefs);
			$('#btnAdd').prop('disabled', false)

			// カレンダー（年月日）
			NCI.ymdPicker($('input.ymdPicker'));

			// 以前の検索条件を復元できれば再検索する
			if (pager.loadCondition()) {
				search();
			} else {
				$('#corporationCode').val(NCI.loginInfo.corporationCode);
			}

			$(document).on('click', '#btnSearch', function(ev) {
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
			.on('click', '#screenProcessDef input[type=checkbox][data-field=screenProcessId]', function() {
				let noSelect = getSelectedScreenProcessIds().length === 0;
				disableButtonsByCheckbox(noSelect);
			})
			// 検索結果のリンク押下
			.on('click', 'a[data-field]', function(ev) {
				let $tr = $(this).closest('tr');
				let screenProcessId = $tr.find('[data-field=screenProcessId]').val();
				let version = $tr.find('[data-field=version]').text();
				openEntry(screenProcessId, version);
			})
			// 追加ボタン
			.on('click', '#btnAdd', function(ev) {
				openEntry(null);
			})
			// 削除ボタン
			.on('click', '#btnDelete', function(ev) {
				let msg = NCI.getMessage("MSG0072", NCI.getMessage("screenProcessInfo"));
				if (NCI.confirm(msg, function() {
					let params = { "screenProcessIds" : getSelectedScreenProcessIds() };
					NCI.post("/vd0040/delete", params).done(function(res) {
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
			cond.sortColumn = 'SCREEN_PROCESS_CODE, SORT_ORDER, SCREEN_PROCESS_ID';
			cond.sortAsc = true;
		}
		return cond;
	}

	/** 選択行のデータを開く */
	function openEntry(screenProcessId, version) {
		if (screenProcessId)
			NCI.redirect("./vd0041.html?screenProcessId=" + screenProcessId + "&version=" + version);
		else
			NCI.redirect("./vd0041.html");
	}

	/** 検索結果の選択行によるボタン制御 */
	function disableButtonsByCheckbox(disable) {
		$('#btnDelete').prop('disabled', disable);
	}

	/** 選択中の画面プロセスIDを返す */
	function getSelectedScreenProcessIds() {
		let screenProcessIds = [];
		$('#screenProcessDef input[type=checkbox][data-field=screenProcessId]:checked').each(function(i, elem) {
			screenProcessIds.push( +elem.value );
		});

		return screenProcessIds;
	}
});

