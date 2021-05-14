$(function() {
	const pager = new Pager($('#seach-result'), '/mg0130/search', search).init();

	const params = { messageCds : [] };
	NCI.init("/mg0130/init", params).done(function(res) {
		if (res && res.success) {

			// 会社コード初期値
			NCI.createOptionTags($('#companyCd'), res.companyItems);
			$('#companyCd').val(res.companyCd);

			// 以前の検索条件を復元できれば再検索する
			if (pager.loadCondition()) {
				search();
			}

			// カレンダー（年月日）
			NCI.ymdPicker($('input.ymdPicker'));

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
				var accCd = $('>.first>.accCd', $(this).parent().parent()).val();
				var sqno = $('>.first>.sqno', $(this).parent().parent()).val();

				openEntry(companyCd, accCd, sqno);
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
			// アップロードボタンクリック時
			.on('click', '#btnUpload', function() {
				$('#btnUploadRegister').prop('disabled', true);
				$('#uploadFileSelect').modal({show: true});
			})
			//エラーアラート用
			.on('click', '#checkErrorAlertButtonArea>.btn-primary', function() {
				$('#checkErrorAlert').modal("hide");
			})
			// 削除ボタン
			.on('click', '#btnDelete', function() {
				let msg = '勘定科目情報を削除します。よろしいですか？';
				NCI.confirm(msg, function() {

					// 選択されている勘定科目情報取得
					var target = '';
					var targetCnt = 0;

					for(var i=0; i<$('#seach-result .selectable').size(); i++) {
						if ($('#seach-result .selectable').get(i).checked) {
							var checkTarget = $('#seach-result .selectable').get(i);
							var companyCd = $('>.first>.companyCd', $(checkTarget).parent().parent()).val();
							var accCd = $('>.first>.accCd', $(checkTarget).parent().parent()).val();
							var sqno = $('>.first>.sqno', $(checkTarget).parent().parent()).val();

							if (targetCnt != 0) {
								target += ',';
							}

							target += companyCd + '|' + accCd + '|' + sqno;
							targetCnt++;
						}
					}

					if (target) {
						var params = {'deleteTarget':target};

						NCI.init("/mg0130/delete", params).done(function(res) {
							if (res && res.success) {
								var msg = '勘定科目情報を削除しました。';
								$('#mg0130InformationContents').html(msg);
								$('#mg0130Information').modal({show: true});
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

			// イベント
			$('#btnUploadRegister').click(uploadRegister);
			$('#btnUploadReset').click(uploadReset);
			$('#btnDownload').click(download).prop('disabled', false);

			// ドラッグ＆ドロップによるファイルアップロード
			FileUploader.setup("div.dragZone", "/mg0130/upload", false, displayUploadResult);

			// ファイルコントロールによるファイルアップロード
			FileUploader.setup('input[type=file]', "/mg0130/upload", false, displayUploadResult);

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
			cond.sortColumn = 'AM.COMPANY_CD';
			cond.sortAsc = true;
		}
		return cond;
	}

	/** 会社選択ポップアップからのコールバック */
	function callbackFromSelectCompany(company) {
		if (company) {
			$('#companyCd').val(company['CORPORATION_CODE']).trigger('validate');
			$('#companyAddedInfo').val(company['CORPORATION_ADDED_INFO']);
		}
	}

	/** 会社コード選択ポップアップをクリア */
	function clearForCompany() {
		$('#companyCd').val('').trigger('validate');
		$('#companyAddedInfo').val('');
	}

	/** 追加ボタン押下時 */
	function openEntryForInsert() {
		NCI.redirect("./mg0131.html?insertFlg=true");
	}

	/** 明細行（勘定科目マスタ設定画面）を開く */
	function openEntry(companyCd, accCd, sqno) {
		NCI.redirect("./mg0131.html?companyCd=" + companyCd + "&accCd=" + accCd + "&sqno=" + sqno);
	}

	/** 行選択時の動作 */
	function whenSelectRow() {
		const len = $('tbody input.selectable[type=checkbox]:checked').length;
		$('#btnDelete').prop('disabled', len === 0);
	}

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

		let msg = '勘定科目情報を登録します。よろしいですか？';
		NCI.confirm(msg, function() {
			const params = {
				encoded : $('#encoded').val(),
				fileName : $('#fileName').text()
			};
			NCI.post('/mg0130/register', params).done(function(res) {
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
		NCI.download('/mg0130/download', cond);
	}
});

