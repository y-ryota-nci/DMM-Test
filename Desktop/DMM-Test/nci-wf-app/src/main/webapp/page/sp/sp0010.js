$(function() {
	const pager = new Pager($('#seach-result'), '/sp0010/search', search).init();

	const params = { messageCds : [] };
	NCI.init("/sp0010/init", params).done(function(res) {
		if (res && res.success) {

			// 会社コード初期値
			$('#companyCd').val(res.companyCd);
			$('#companyNm').val(res.companyNm);

			if ("00053" != res.companyCd) {
				$('#companySelectBtnGrp').hide();
			}

			if (res.loginInfo.corporationCode == '00053') {
				$('#splrList .section').hide();
			}


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
				// 会社コード初期値
				$('#companyCd').val(res.companyCd);
				$('#companyNm').val(res.companyNm);
			})
			// 検索結果のリンク押下
			.on('click', 'a[data-field]', function(ev) {
				var companyCd = $('>.first>.companyCd', $(this).parent().parent()).val();
				var splrCd = $('>.first>.splrCd', $(this).parent().parent()).val();

				openEntry(companyCd, splrCd);
			})
			.on('click', '.btnCompanyCd', function() {
				// 会社コードの選択ポップアップ起動
				const conds = {};
				NCI.openMasterSearch('WFM_CORPORATION_V', 'FOR_POPUP_COMPANY', callbackFromSelectCompany, conds);
			})
			.on('click', '.btnClearCompany', function() {
				// 会社コードのクリアボタン
				clearForCompany();
			})
			// 行選択
			.on('click', 'input.selectable[type=checkbox]', function() {
				whenSelectRow();
			})
			// 変更_取引先申請
			.on('click', '#btnUpdateRequest', function() {

				// 選択されている取引先情報取得
				var checkTarget = null;
				for(var i=0; i<$('#seach-result .selectable').size(); i++) {
					if ($('#seach-result .selectable').get(i).checked) {
						checkTarget = $('#seach-result .selectable').get(i);
						break;
					}
				}

				if (checkTarget) {
					var companyCd = $('>.first>.companyCd', $(checkTarget).parent().parent()).val();
					var splrCd = $('>.first>.splrCd', $(checkTarget).parent().parent()).val();

					if(res.loginInfo.corporationCode != '00053' && res.loginInfo.corporationCode != companyCd){
						var msg = '有効/無効が有効のもののみ変更申請可能です。';
						$('#checkErrorAlertBody').html(msg);
						$('#checkErrorAlert').modal({show: true});
						return;
					}

					// 画面ID取得
					var params = { companyCd : companyCd, splrCd : splrCd};
					NCI.init("/sp0010/validate", params).done(function(res) {
						// 変更申請
						NCI.redirect("../vd/vd0310.html?screenProcessId=" + res.screenProcessId + "&trayType=NEW&param1=" + companyCd + "&param2=" + splrCd);
					});
				}
			})
			//エラーアラート用
			.on('click', '#checkErrorAlertButtonArea>.btn-primary', function() {
				$('#checkErrorAlert').modal("hide");
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

					// 有効なもののみ対象
					var enabled = $(this).closest('tr').find('a[data-field=enabled]').text();
					$('#btnUpdateRequest').prop('disabled', res.loginInfo.corporationCode != '00053'  && enabled != '有効');
				} else {
					$('#btnUpdateRequest').prop('disabled',true);
				}
			})
			// アップロードボタンクリック時
			.on('click', '#btnUpload', function() {
				$('#btnUploadRegister').prop('disabled', true);
				$('#uploadFileSelect').modal({show: true});
			});

			// イベント
			$('#btnUploadRegister').click(uploadRegister);
			$('#btnUploadReset').click(uploadReset);
			$('#btnDownload').click(download).prop('disabled', false);

			// ドラッグ＆ドロップによるファイルアップロード
			FileUploader.setup("div.dragZone", "/sp0010/upload", false, displayUploadResult);

			// ファイルコントロールによるファイルアップロード
			FileUploader.setup('input[type=file]', "/sp0010/upload", false, displayUploadResult);
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
			$('#btnUpdateRequest').prop('disabled',true);
		});
	}

	/** 画面入力内容から検索条件を生成 */
	function createCondition(pageNo) {
		let $elements = $('#formCondition').find('input, select, textarea');
		let cond = pager.createCondition($elements, pageNo);
		// デフォルトソート条件
		if (!cond.sortColumn) {
			cond.sortColumn = 'S.SPLR_CD';
			cond.sortAsc = true;
		}
		return cond;
	}

	/** 会社選択ポップアップからのコールバック */
	function callbackFromSelectCompany(company) {
		if (company) {
			$('#companyCd').val(company['CORPORATION_CODE']).trigger('validate');
			$('#companyAddedInfo').val(company['CORPORATION_ADDED_INFO']);
			$('#companyNm').val(company['CORPORATION_NAME']);
		}
	}

	/** 会社コード選択ポップアップをクリア */
	function clearForCompany() {
		$('#companyCd').val('');
		$('#companyAddedInfo').val('');
		$('#companyNm').val('');
	}

	/** 明細行（取引先マスタ）を開く */
	function openEntry(companyCd, splrCd) {
		NCI.redirect("./sp0011.html?companyCd=" + companyCd + "&splrCd=" + splrCd);
	}

	/** 行選択時の動作 */
	function whenSelectRow() {
		const len = $('tbody input.selectable[type=checkbox]:checked').length;
		$('#btnComplete, #btnRestore').prop('disabled', len === 0);
	}

	/** アップロード結果を表示 */
	function displayUploadResult(res) {
		NCI.toElementsFromObj(res, $('#dragAndDropArea'));
		NCI.toElementsFromObj(res, $('#uploadFileArea'));

		const isEmpty = ($('#encoded').val() == '');
		$('#dragAndDropArea').toggleClass('hide', !isEmpty);
		$('#uploadFileArea').toggleClass('hide', isEmpty);
		$('#btnUploadRegister').prop('disabled', isEmpty);
	}

	/** 登録 */
	function uploadRegister(ev) {
		const $targets = $('input[type=text]')
		if (!Validator.validate($targets)) {
			return false;
		}

		let msg = '取引先情報を登録します。よろしいですか？';
		NCI.confirm(msg, function() {
			const params = {
				encoded : $('#encoded').val(),
				fileName : $('#fileName').text()
			};
			NCI.post('/sp0010/register', params).done(function(res) {
				uploadReset();

				// モーダルを閉じる
				$('#uploadFileSelect').modal('hide');
			});
		});
	}

	/** 表示を初期状態へ戻す */
	function uploadReset(ev) {
		const res = {
				success : true, encoded : "", fileName : "",
				splrCount : null, payeeBnkaccCount : null,
				deleteIfNotUse : false
		};
		displayUploadResult(res);
		if (ev) {
			NCI.clearMessage();	// リセットボタン押下ならメッセージもクリア
		}
	}

	/** テンプレートのダウンロード */
	function download() {
		NCI.clearMessage();
		var cond = createCondition(1);
		NCI.download('/sp0010/download', cond);
	}
});

