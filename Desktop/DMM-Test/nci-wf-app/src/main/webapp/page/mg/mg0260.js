$(function() {
	const pager = new Pager($('#seach-result'), '/mg0260/search', search).init();

	const params = { messageCds : [] };
	NCI.init("/mg0260/init", params).done(function(res) {
		if (res && res.success) {

			// 会社コード初期値
			NCI.createOptionTags($('#companyCd'), res.companyItems);
			$('#companyCd').val(res.companyCd);

			// 以前の検索条件を復元できれば再検索する
			if (pager.loadCondition()) {
				search();
			}

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
				clearForBankAcc();
				clearForUser();
			})
			// 検索結果のリンク押下
			.on('click', 'a[data-field]', function(ev) {
				var companyCd = $('>.first>.companyCd', $(this).parent().parent()).val();
				var splrCd = $('>.first>.splrCd', $(this).parent().parent()).val();
				var usrCd = $('>.first>.usrCd', $(this).parent().parent()).val();

				openEntry(companyCd, splrCd, usrCd);
			})

			.on('click', '#btnSelectBankAcc', function() {
				// 部門選択（企業を選択済みならその企業の）
				const conds = { "COMPANY_CD" : $('#companyCd option:selected').val()};
				NCI.openMasterSearch('V_BNKACC_MST', 'FOR_POPUP', callbackSearchBankAcc, conds);
			})
			.on('click', '#btnClearBankAcc', function(){
				//	マスタのクリア
				clearForBankAcc();
			})
			.on('click', '#btnSelectUser', function() {
				// ユーザ選択
				const params = null, corporationCode = $('#companyCd option:checked').val();
				let url = "../cm/cm0040.html?initSearch=true";
				if (corporationCode)
					url += "&corporationCode=" + corporationCode;
				Popup.open(url, callbackSearchUser, params, this);
			})
			.on('click', '#btnClearUser', function(){
				//	マスタのクリア
				clearForUser();
			})

			// 行選択
			.on('click', 'input.selectable[type=checkbox]', function() {
				whenSelectRow();
			})
			//エラーアラート用
			.on('click', '#checkErrorAlertButtonArea>.btn-primary', function() {
				$('#checkErrorAlert').modal("hide");
			})
			// 削除ボタン
			.on('click', '#btnDelete', function() {
				let msg = 'ｸﾚｶ口座マスタを削除します。よろしいですか？';
				NCI.confirm(msg, function() {

					// 選択されている通貨マスタ取得
					var target = '';
					var targetCnt = 0;

					for(var i=0; i<$('#seach-result .selectable').size(); i++) {
						if ($('#seach-result .selectable').get(i).checked) {
							var checkTarget = $('#seach-result .selectable').get(i);
							var companyCd = $('>.first>.companyCd', $(checkTarget).parent().parent()).val();
							var splrCd = $('>.first>.splrCd', $(checkTarget).parent().parent()).val();
							var usrCd = $('>.first>.usrCd', $(checkTarget).parent().parent()).val();

							if (targetCnt != 0) {
								target += ',';
							}

							target += companyCd + '|' + splrCd + '|' + usrCd;
							targetCnt++;
						}
					}

					if (target) {
						var params = {'deleteTarget':target};

						NCI.init("/mg0260/delete", params).done(function(res) {
							if (res && res.success) {
								var msg = 'ｸﾚｶ口座マスタを削除しました。';
								$('#mg0260InformationContents').html(msg);
								$('#mg0260Information').modal({show: true});
								search(1, true);
								$('#btnDelete').attr('disabled','disabled');
							}
						})
					};
				});
			})
			// 追加ボタン
			.on('click', '#btnInsert', function() {
				openEntryForInsert();
			})

			// アップロードボタンクリック時
			.on('click', '#btnUpload', function() {
				$('#btnUploadRegister').prop('disabled', true);
				$('#uploadFileSelect').modal({show: true});
			});

			$('#btnUploadRegister').click(uploadRegister);
			$('#btnUploadReset').click(uploadReset);
			$('#btnDownload').click(download).prop('disabled', false);

			// ドラッグ＆ドロップによるファイルアップロード
			FileUploader.setup("div.dragZone", "/mg0260/upload", false, displayUploadResult);

			// ファイルコントロールによるファイルアップロード
			FileUploader.setup('input[type=file]', "/mg0260/upload", false, displayUploadResult);

			//	モーダルが閉じられたときエラーメッセージ消去
			$('#uploadFileSelect').on('hidden.bs.modal',function() {
				NCI.clearMessage();
				uploadReset();
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
			cond.sortColumn = 'cbm.COMPANY_CD';
			cond.sortAsc = true;
		}
		return cond;
	}

	/** 追加ボタン押下時 */
	function openEntryForInsert() {
		NCI.redirect("./mg0261.html?insertFlg=true");
	}

	/** 明細行（ｸﾚｶ口座マスタ）を開く */
	function openEntry(companyCd, splrCd, usrCd) {
		NCI.redirect("./mg0261.html?companyCd=" + companyCd + "&splrCd=" + splrCd + "&usrCd=" + usrCd);
	}

	/** 行選択時の動作 */
	function whenSelectRow() {
		const len = $('tbody input.selectable[type=checkbox]:checked').length;
		$('#btnDelete').prop('disabled', len === 0);
	}

	callbackSearchUser = function (org, trigger) {
		if (org) {
			$('#usrCd').val(org.userCode);
			$('#usrNm').val(org.userName);
		}
	};

	callbackSearchBankAcc = function (condition) {
		if (condition) {
			$('#bnkaccCd').val(condition["BNKACC_CD"]);
			$('#bnkaccNm').val(condition["BNKACC_NM"]);
		}
	};

	/** アップロード結果を表示 */
	function displayUploadResult(res) {
		 if(res.success){
				NCI.toElementsFromObj(res, $('#dragAndDropArea'));
				NCI.toElementsFromObj(res, $('#uploadFileArea'));

				const isEmpty = ($('#encoded').val() == '');
				$('#dragAndDropArea').toggleClass('hide', !isEmpty);
				$('#uploadFileArea').toggleClass('hide', isEmpty);
				$('#btnUploadRegister').prop('disabled', isEmpty);
		 }else{
			 $('#error-alert-message_after_sub-title').before('<div class="alert alert-danger nci-messages" id="div-alert-danger"><ul><li>' + res.alerts[0] + '</li></ul></div>');
		 }
	}

	/** 登録 */
	function uploadRegister(ev) {
		const $targets = $('input[type=text]')
		if (!Validator.validate($targets)) {
			return false;
		}

		let msg = 'クレカ口座情報を登録します。よろしいですか？';
		NCI.confirm(msg, function() {
			const params = {
				encoded : $('#encoded').val(),
				fileName : $('#fileName').text()
			};
			NCI.post('/mg0260/register', params).done(function(res) {
				uploadReset();

				// モーダルを閉じる
				$('#uploadFileSelect').modal('hide');

				// 再検索
				search(1);
			});
		});
	}

	/** 表示を初期状態へ戻す */
	function uploadReset(ev) {
		const res = {
				success : true, encoded : "", fileName : "",
				count : null,
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
		NCI.download('/mg0260/download', cond);
	}

	/** 銀行口座コード選択ポップアップをクリア */
	function clearForBankAcc() {
		$('#bnkaccCd').val('').trigger('validate');
		$('#bnkaccNm').val('');
	}

	/** ユーザコード選択ポップアップをクリア */
	function clearForUser() {
		$('#usrCd').val('').trigger('validate');
		$('#usrNm').val('');
	}

});