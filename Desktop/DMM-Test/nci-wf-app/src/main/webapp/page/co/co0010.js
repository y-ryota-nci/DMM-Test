$(function() {
	const pager = new Pager($('#seach-result'), '/co0010/search', search).init();

	const params = { messageCds : [] };
	NCI.init("/co0010/init", params).done(function(res) {
		if (res && res.success) {
			// 以前の検索条件を復元できれば再検索する
			if (pager.loadCondition()) {
				search();
			} else {
				search(1);
			}

			// カレンダー（年月日）
			NCI.ymdPicker($('input.ymdPicker'));

			// 都道府県
			NCI.createOptionTags($('#adrPrfCd'), res.adrPrfCds);
			// 依頼種別
			NCI.createOptionTags($('#cntrctshtFrmt'), res.cntrctshtFrmts);

			$(document).on('click', '#btnSearch', function(ev) {
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
			})
			// 検索結果のリンク押下
			.on('click', 'a[data-field]', function(ev) {
				var companyCd = $('>.first>.companyCd', $(this).parent().parent()).val();
				var cntrctNo = $('>.first>.cntrctNo', $(this).parent().parent()).val();
				var rtnPayNo = $('>.first>.rtnPayNo', $(this).parent().parent()).val();

				openEntry(companyCd,cntrctNo, rtnPayNo);
			})
			// 行選択
			.on('click', 'input.selectable[type=checkbox]', function() {
			})
			// 変更_契約申請
			.on('click', '#btnUpdateRequest', function() {

				// 選択されている契約情報取得
				var checkTarget = null;
				for(var i=0; i<$('#seach-result .selectable').size(); i++) {
					if ($('#seach-result .selectable').get(i).checked) {
						checkTarget = $('#seach-result .selectable').get(i);
						break;
					}
				}

				if (checkTarget) {
					var companyCd = $('>.first>.companyCd', $(checkTarget).parent().parent()).val();
					var cntrctNo = $('>.first>.cntrctNo', $(checkTarget).parent().parent()).val();
					var rtnPayNo = $('>.first>.rtnPayNo', $(checkTarget).parent().parent()).val();

					// 画面ID取得
					var params = { companyCd : companyCd, cntrctNo : cntrctNo };

					NCI.init("/co0010/validate", params).done(function(res) {
						// 変更申請
						NCI.redirect("../vd/vd0310.html?screenProcessId=" + res.screenProcessId + "&trayType=NEW&param1=" + companyCd + "&param2=" + cntrctNo + "&param3=" + rtnPayNo);
					});
				}
			})
			// 一覧選択時
			.on('click', '#seach-result .selectable', function() {
				// とりあえずラジオボタン形式
				if ($(this).get(0).checked) {
					for(var i=0; i<$('#seach-result .selectable').size(); i++) {
						if ($('#seach-result .selectable').get(i) != this) {
							$('#seach-result .selectable').get(i).checked = false;
						}
					}
					$('#btnUpdateRequest').removeAttr('disabled');
				} else {
					$('#btnUpdateRequest').attr('disabled','disabled');
				}
			});
		}
	});

	/** 検索実行 */
	function search(pageNo, keepMessage) {
		const $targets = $('#formCondition').find('input, select, text');
		if (!Validator.validate($targets, true)) {
			return false;
		}
		const cond = createCondition(pageNo);
		pager.search(cond, keepMessage).done(function() {
			$('#seach-result').removeClass('hide');
		});
	}

	/** 画面入力内容から検索条件を生成 */
	function createCondition(pageNo) {
		let $elements = $('#formCondition').find('input, select, textarea');
		let cond = pager.createCondition($elements, pageNo);
		// デフォルトソート条件
		if (!cond.sortColumn) {
			cond.sortColumn = 'C.CNTRCT_NO';
			cond.sortAsc = true;
		}
		return cond;
	}

	/** 明細行を開く */
	function openEntry(companyCd, cntrctNo, rtnPayNo) {
//		NCI.redirect("./co0011.html?companyCd=" + companyCd + "&cntrctNo=" + cntrctNo);
		NCI.flushScope('_vd0330', {
		'keys' : { 'companyCd' : companyCd, 'cntrctNo' : cntrctNo, 'rtnPayNo' : rtnPayNo },
		'corporationCode' : companyCd,
		'screenCode' : 'SCR0056',
		'screenName' : '管理_法務依頼・契約申請',	// screenNameを指定していればVD0330でそれが画面名として使われる
		'backUrl' : '../co/co0010.html',
		'dcId' : 1						// 指定されていればその表示条件IDを使う
	});
	NCI.redirect('../vd/vd0330.html');
	}

});

