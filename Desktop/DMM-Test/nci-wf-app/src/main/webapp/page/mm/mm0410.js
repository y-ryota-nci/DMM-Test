$(function() {
	var pager = new Pager($('#seach-result'), '/mm0410/search', search).init();

	var params = {
		messageCds : ['MSG0003', 'MSG0070', 'corporation']
	};
	NCI.init("/mm0410/init", params).done(function(res) {
		if (res && res.success) {
			// 企業が指定されていればその企業コードを、なければ自社
			NCI.createOptionTags($('#corporationGroupCode'), res.corporationGroups);
			NCI.createOptionTags($('#corporationCode'), res.corporations);
			NCI.createOptionTags($('#deleteFlag'), res.deleteFlags);
			$('#btnAdd').toggle(NCI.loginInfo.aspAdmin).prop('disabled', false);

			// 以前の検索条件を復元
			if (pager.loadCondition()) {
				search();
			} else {
				$('#deleteFlag').val('0');
				// 企業グループに属していればグループ内の企業が検索できるよう企業グループコードだけを初期条件にする
				if (NCI.loginInfo.corporationGroupCode)
					$('#corporationGroupCode').val(NCI.loginInfo.corporationGroupCode);
				else
					$('#corporationCode').val(NCI.loginInfo.corporationCode);
			}
		}
	});

	$(document)
	.on('click', '#btnSearch', function(ev) {
		// 検索ボタン押下
		search(1);
		return false;
	})
	.on('click', 'ul.pagination a', function(ev) {
		// ページ番号リンク押下
		const pageNo = this.getAttribute('data-pageNo');
		search(pageNo);
		return false;
	})
	// リセットボタン押下
	.on('click', '#btnReset', function(ev) {
		$('#formCondition')[0].reset();
		$('#deleteFlag').val('0');
	})
	// 検索結果のリンク押下
	.on('click', 'a[data-field]', function(ev) {
		const $tr = $(this).closest('tr');
		const corporationCode = $tr.find('[data-field=corporationCode]').text();
		const timestampUpdated = $tr.find('[data-field=timestampUpdated]').text();
		openEntry(corporationCode, timestampUpdated);
	})
	// 追加ボタン
	.on('click', '#btnAdd', openCorpPopup)
	// （企業追加ポップアップ）企業コード＋名称の入力を促すダイアログのＯＫボタン
	.on('click', '#btnAddCorpOk', callbackCorpPopup)
	// （企業追加ポップアップ）企業コード＋名称の入力を促すダイアログのキャンセルボタン
	.on('click', '#btnAddCorpCancel', closeCorpPopup)
	;

	/** 検索実行 */
	function search(pageNo, keepMessage) {
		const $targets = $('#formCondition').find('input, select, text');
		if (!Validator.validate($targets, true)) {
			return false;
		}
		const cond = createCondition(pageNo);
		pager.search(cond, keepMessage);
		$('#seach-result').removeClass('hide');
	}

	/** 画面入力内容から検索条件を生成 */
	function createCondition(pageNo) {
		const $elements = $('#formCondition').find('input, select, textarea');
		const cond = pager.createCondition($elements, pageNo);
		// デフォルトソート条件
		if (!cond.sortColumn) {
			cond.sortColumn = 'CORPORATION_CODE';
			cond.sortAsc = true;
		}
		return cond;
	}

	function openEntry(corporationCode, timestampUpdated) {
		NCI.redirect("./mm0001.html" +
				"?corporationCode=" + corporationCode +
				"&timestampUpdated=" + NCI.timestampToLong(timestampUpdated));
	}

	// 企業追加時の企業コード・企業名の入力を促すダイアログを開く
	function openCorpPopup(ev) {
		// 企業コード＋名称の入力を促すダイアログを表示
		$('#newCorporationCode, #newCorporationName').val('');
		$('#confirmAddCorp').modal();
		return false;

		const msg = NCI.getMessage('MSG0070', NCI.getMessage('corporation'));
		NCI.confirm(msg, function() {
			const params = {
					"newCorporationCode" : "",
					"newCorporationName" : "",
					"baseDate" : NCI.today(),
					"displayValidOnly" : true
			};
			NCI.post('/mm0410/add', params).done(function(res) {
				if (res && res.success) {
					search(null, true);
				}
			});
		});
		return false;
	}

	// 企業追加時の企業コード・企業名の入力を促すダイアログでOK押下
	function callbackCorpPopup(ev) {
		if (!Validator.validate($('#newCorporationCode, #newCorporationName'), true))
			return false;

		$('#confirmAddCorp').modal('hide');
		const params = {
			newCorporationCode : $('#newCorporationCode').val(),
			newCorporationName : $('#newCorporationName').val(),
			baseDate : NCI.today(),
			displayValidOnly : true
		};
		NCI.post('/mm0410/add', params).done(function(res) {
			if (res && res.success) {
				const c = res.newCorporation;
				openEntry(c.corporationCode, c.timestampUpdated);
			}
		});
		return true;
	}

	/** 企業追加用ポップアップを閉じる */
	function closeCorpPopup(e) {
		Validator.hideBalloon( $('#newCorporationCode, #newCorporationName') );
		$('#confirmAddCorp').modal('hide');
	}
});

