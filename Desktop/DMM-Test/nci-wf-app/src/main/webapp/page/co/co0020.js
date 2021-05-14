$(function() {
	const pager = new Pager($('#seach-result'), '/co0020/search', search).init();

	const params = { messageCds : [] };
	NCI.init("/co0020/init", params).done(function(res) {
		if (res && res.success) {
			// 以前の検索条件を復元できれば再検索する
			if (pager.loadCondition()) {
				search();
			}

			// カレンダー（年月）
			NCI.ymPicker($('input.ymPicker'));
			NCI.ymdPicker($('input.ymdPicker'));

			// 都道府県
			NCI.createOptionTags($('#adrPrfCd'), res.adrPrfCds);

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
				//汎用マスタリセット時検索条件が初期化されない問題について対応 hidden項目はリセットされないので手動で初期化
				clearForBumon();
				clearCntrctrDpt();
				clearForPaySite();
			})
			// 検索結果のリンク押下
			.on('click', 'a[data-field]', function(ev) {
				let companyCd = $('>.first>.companyCd', $(this).parent().parent()).val();
				let cntrctNo = $('>.first>.cntrctNo', $(this).parent().parent()).val();
				let rtnPayNo = $('>.first>.rtnPayNo', $(this).parent().parent()).val();
				let editable = res.editableFlg === '1';
				openEntry(companyCd, cntrctNo, rtnPayNo, editable);
			})
			.on('click', '.btnBumonCd', function() {
				// 部門マスタの選択ポップアップ起動
				const conds = {'COMPANY_CD':NCI.loginInfo.corporationCode};
				NCI.openMasterSearch('BUMON_MST', 'FOR_BTN_BUMON', callbackFromSelectBumon, conds);
			})
			.on('click', '.btnClearBumonCd', function() {
				// 部門のクリアボタン
				clearForBumon();
			})
			.on('click', '.btnCntrctrDptCd', function() {
				// 契約主体の選択ポップアップ起動
				const url = "../cm/cm0020.html?corporationCode=" + $('#companyCd').val();
				Popup.open(url, callbackFromSelectCntrctrDpt, null, this);
			})
			.on('click', '.btnClearCntrctrDptCd', function() {
				// 契約主体のクリアボタン
				clearCntrctrDpt();
			})
			.on('click', '.btnPaySiteCd', function() {
				// 支払サイトマスタの選択ポップアップ起動
				const conds = {'COMPANY_CD':NCI.loginInfo.corporationCode};
				NCI.openMasterSearch('PAY_SITE_MST', 'FOR_SEARCH_PAY_SITE_MST', callbackFromSelectPaySite, conds);
			})
			.on('click', '.btnClearPaySiteCd', function() {
				// 支払サイトのクリアボタン
				clearForPaySite();
			})
			// 行選択
			.on('click', 'input.selectable[type=checkbox]', function() {
			})
			;
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

			// いい案がないのでとりあえずここでフォーマット
			$.each($('#seach-result [data-field="payStartTime"], #seach-result [data-field="payEndTime"]'), function(index, field) {
				var val = $(field).html();

				if (val && val.indexOf('/') == -1 && val.length > 4) {
					var left = val.substring(0, 4);
					var right = val.substring(4);

					if (right.length == 1) {
						right = '0' + right;
					}

					$(field).html(left + '/' + right);
				}
			});
		});
	}

	/** 画面入力内容から検索条件を生成 */
	function createCondition(pageNo) {
		let $elements = $('#formCondition').find('input, select, textarea');
		let cond = pager.createCondition($elements, pageNo);
		// デフォルトソート条件
		if (!cond.sortColumn) {
			cond.sortColumn = 'pm.CNTRCT_NO';
			cond.sortAsc = true;
		}
		return cond;
	}

	/** 部門選択ポップアップからのコールバック */
	function callbackFromSelectBumon(bumon) {
		if (bumon) {
			$('#bumonCd').val(bumon['BUMON_CD']).trigger('validate');
			$('#bumonNm').val(bumon['BUMON_NM']);
		}
	}

	/** 契約主体選択ポップアップからのコールバック */
	function callbackFromSelectCntrctrDpt(cntrCtrDpt) {
		if (cntrCtrDpt) {
			$('#cntrctrDptCd').val(cntrCtrDpt['organizationCode']).trigger('validate');
			$('#cntrctrDptNm').val(cntrCtrDpt['organizationName']);
		}
	}

	/** 支払サイト選択ポップアップからのコールバック */
	function callbackFromSelectPaySite(paySite) {
		if (paySite) {
			$('#paySiteCd').val(paySite['PAY_SITE_CD']).trigger('validate');
			$('#paySiteNm').val(paySite['PAY_SITE_NM']);
		}
	}

	/** 部門選択ポップアップをクリア */
	function clearForBumon() {
		$('#bumonCd').val('').trigger('validate');
		$('#bumonNm').val('');
	}

	/** 契約主体ポップアップをクリア */
	function clearCntrctrDpt() {
		$('#cntrctrDptCd').val('').trigger('validate');
		$('#cntrctrDptNm').val('');
	}

	/** 支払サイト選択ポップアップをクリア */
	function clearForPaySite() {
		$('#paySiteCd').val('').trigger('validate');
		$('#paySiteNm').val('');
	}


	/** 明細行を開く */
	function openEntry(companyCd, cntrctNo, rtnPayNo, editable) {
//		NCI.redirect("./co0021.html?companyCd=" + companyCd + "&cntrctNo=" + cntrctNo + "&rtnPayNo=" + rtnPayNo);
		// 編集可能かどうかパラメータを切り替える
		let dcId = editable ? 1 : null;
		let trayType = editable ? 'WORKLIST' : null;
		NCI.flushScope('_vd0330', {
			'keys' : { 'companyCd' : companyCd, 'cntrctNo' : cntrctNo, 'rtnPayNo' : rtnPayNo },
			'corporationCode' : companyCd,
			'screenCode' : 'SCR0157',
			'screenName' : '管理_支払予約登録',	// screenNameを指定していればVD0330でそれが画面名として使われる
			'backUrl' : '../co/co0020.html',
			'dcId' : dcId,						// 指定されていればその表示条件IDを使う
			'trayType' : trayType			// 指定されていればそのトレイタイプ、無指定なら汎用案件
		});
		NCI.redirect('../vd/vd0330.html');
	}

});

