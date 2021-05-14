$(function() {
	let pager = new Pager($('#seach-result'), '/dc0110/search', search).init();

	let params = { messageCds : ['screenDocInfo'] };
	NCI.init("/dc0110/init", params).done(function(res) {
		if (res && res.success) {
			NCI.createOptionTags($('#corporationCode'), res.corporations);
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
			.on('click', '#screenDocDef input[type=checkbox][data-field=screenDocId]', function() {
				let noSelect = getSelectedScreenDocIds().length === 0;
				disableButtonsByCheckbox(noSelect);
			})
			// 検索結果のリンク押下
			.on('click', 'a[data-field]', function(ev) {
				let $tr = $(this).closest('tr');
				let screenDocId = $tr.find('[data-field=screenDocId]').val();
				let version = $tr.find('[data-field=version]').text();
				openEntry(screenDocId, version);
			})
			// 追加ボタン
			.on('click', '#btnAdd', function(ev) {
				openEntry(null);
			})
			// 削除ボタン
			.on('click', '#btnDelete', function(ev) {
				let msg = NCI.getMessage("MSG0072", NCI.getMessage("screenDocInfo"));
				if (NCI.confirm(msg, function() {
					let params = { "screenDocIds" : getSelectedScreenDocIds() };
					NCI.post("/dc0110/delete", params).done(function(res) {
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
			cond.sortColumn = 'SCREEN_DOC_CODE, SORT_ORDER, SCREEN_DOC_ID';
			cond.sortAsc = true;
		}
		return cond;
	}

	/** 選択行のデータを開く */
	function openEntry(screenDocId, version) {
		if (screenDocId)
			NCI.redirect("./dc0111.html?screenDocId=" + screenDocId + "&version=" + version);
		else
			NCI.redirect("./dc0111.html");
	}

	/** 検索結果の選択行によるボタン制御 */
	function disableButtonsByCheckbox(disable) {
		$('#btnDelete').prop('disabled', disable);
	}

	/** 選択中の画面プロセスIDを返す */
	function getSelectedScreenDocIds() {
		let screenDocIds = [];
		$('#screenDocDef input[type=checkbox][data-field=screenDocId]:checked').each(function(i, elem) {
			screenDocIds.push( +elem.value );
		});

		return screenDocIds;
	}

});
