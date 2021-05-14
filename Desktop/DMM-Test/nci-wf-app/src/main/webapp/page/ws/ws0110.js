$(function() {
	var pager = new Pager($('#seach-result'), '/ws0110/search', search).init();

	NCI.init("/ws0110/init").done(function(res) {
		if (res && res.success) {
			$('#corporationCode').val(NCI.loginInfo.corporationCode);
			$('#btnAdd').prop('disabled', false);
		}
	});

	$(document)
	.on('click', '#btnSelectUser', function() {
		// ユーザ選択
		const params = null, corporationCode = $('#corporationCode').val();
		let url = "../cm/cm0040.html?initSearch=true";
		if (corporationCode)
			url += "&corporationCode=" + corporationCode;
		Popup.open(url, callbackFromPopup, params, this);
	})
	.on('click', '#btnClearUser', function() {
		$(this).closest('.input-group').find('input,select,textarea').val('');
		pager.fillTable([]);
		$('#btnDelete').prop('disabled', true);
		$('#seach-result').addClass('hide');
	})
	.on('click', 'ul.pagination a', function(ev) {
		// ページ番号リンク押下
		var pageNo = this.getAttribute('data-pageNo');
		search(pageNo);
		return false;
	})
	// 検索結果の選択チェックボックス
	.on('change', 'input[type=checkbox].selectable', function() {
		var selected = $('input[type=checkbox].selectable:checked').length === 0;
		$('#btnDelete').prop('disabled', selected);
	})
	// 検索結果のリンク押下
	.on('click', 'a[data-field]', function(ev) {
		var $tr = $(this).closest('tr');
		var corporationCode   = $tr.find('[data-field=corporationCode]').text();
		var seqNoAuthTransfer = $tr.find('[data-field=seqNoAuthTransfer]').text();
		var params = { corporationCode : corporationCode, seqNoAuthTransfer : seqNoAuthTransfer };
		NCI.post("/ws0110/check", params).done(function(res) {
			if (res && res.success) {
				if (res.wfmAuthTransfer) {
					openEntry(res.wfmAuthTransfer.corporationCode, res.wfmAuthTransfer.seqNoAuthTransfer);
				} else {
					// alert();
				}
			}
		});
	})
	// 追加ボタン
	.on('click', '#btnAdd', function(ev) {
		openEntry($('#corporationCode').val(), '', $('#userCode').val());
	})
	// 削除ボタン
	.on('click', '#btnDelete', function(ev) {
		var msg = NCI.getMessage("MSG0072", '選択した代理情報');
		if (NCI.confirm(msg, function() {
			var params = { deleteList : [] };
			$('input[type=checkbox].selectable:checked').each(function(i, elem) {
				const $tr = $(elem).closest('tr');
				const entry = $tr.find('[data-field=corporationCode]').text() + "\t" +
					$tr.find('[data-field=seqNoAuthTransfer]').text();
				params.deleteList.push(entry);
			});
			NCI.post("/ws0110/delete", params).done(function(res) {
				search(1, true);
			});
		}));
	});

	/** 検索実行 */
	function search(pageNo, keepMessage) {
		var $targets = $('#search-condition').find('input, select, text');
		if (!Validator.validate($targets, true)) {
			return false;
		}
		var cond = createCondition(pageNo);
		pager.search(cond, keepMessage);
		$('#btnDelete').prop('disabled', true);
		$('#seach-result').removeClass('hide');
	}

	/** 画面入力内容から検索条件を生成 */
	function createCondition(pageNo) {
		var $elements = $('#search-condition').find('input.condition, select, textarea');
		var cond = pager.createCondition($elements, pageNo);
		return cond;
	}

	function openEntry(corporationCode, seqNoAuthTransfer, userCode) {
		var url = "./ws0111.html";
		if (corporationCode != "")   { url += "?corporationCode="   + corporationCode };
		if (seqNoAuthTransfer != "") { url += "&seqNoAuthTransfer=" + seqNoAuthTransfer };
		if (userCode != "") { url += "&userCode=" + userCode };
		NCI.redirect(url);
	}

	window.callbackFromPopup = function(entity, trigger) {
		if (entity) {
			const $root = $(trigger).closest('div.input-group');
			NCI.toElementsFromObj(entity, $root);
			$root.find('input').trigger('validate');
			search(1);
		}
	};
});
