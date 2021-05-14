$(function() {
	const pager = new Pager($('#seach-result'), '/md0010/search', search).init();

	const params = { messageCds : [] };
	NCI.init("/md0010/init", params).done(function(res) {
		if (res && res.success) {
			// 会社コード初期値
			$('#companyCd').val(res.companyCd);
			$('#companyNm').val(res.companyNm);

			if ("00053" != res.companyCd) {
				$('#companySelectBtnGrp').hide();
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
//				$('#formCondition')[0].reset();

				$('#splrCd').val('');
				$('#splrNmKj').val('');
				$('#splrNmKn').val('');
				$('#splrNmE').val('');
				$('#adrPrfCd').val('');

				$('#crpPrsTp1').get(0).checked = false;
				$('#crpPrsTp2').get(0).checked = false;
				$('#trdStsTp2').get(0).checked = false;
				$('#trdStsTp3').get(0).checked = false;
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
			// アップロードボタンクリック時
			.on('click', '#btnUpload', function() {
				$('#btnUploadRegister').prop('disabled', true);
				$('#uploadFileSelect').modal({show: true});
			})
			// ファイルアップロードコントロールでファイルが選択された際の入力チェック
			.on('change', "input[type=file]", md0010.check)
			// ドラッグ＆ドロップ領域へドロップされた際の入力チェック
			.on('drop', "input[type=file]", md0010.check);

			// イベント
			$('#btnUploadRegister').click(uploadRegister);
			$('#btnUploadReset').click(uploadReset);
			$('#btnDownload').click(download).prop('disabled', false);

			// ドラッグ＆ドロップによるファイルアップロード
			FileUploader.setup("div.dragZone", "/md0010/upload", false, displayUploadResult);

			// ファイルコントロールによるファイルアップロード
			FileUploader.setup('input[type=file]', "/md0010/upload", false, displayUploadResult);

			//	モーダルが閉じられたときエラーメッセージ消去
			$('#uploadFileSelect').on('hidden.bs.modal',function() {
				NCI.clearMessage();
				uploadReset();
			});
		}
	});

	// ファイルサイズチェック（とりあえず）
	var md0010 = {
		check : function (e) {
			var $file = $(e.target);
			if ($file.is('input[type=file]:enabled')) {
				var files = e.target.files;
				var bytes = 0;
				for (let i = 0; i < files.length; i++) {
					bytes += files[i].size;
				}

				if (bytes) {
					var mega = bytes/1024/1024;

					if (mega > 20) {
						$(e.target).val('');
						var msg = 'アップロードしたファイルのサイズが上限を超過しました。１ファイルずつアップロードするか、あるいはファイル自体を小さくしてください（合計ファイルサイズ=' + mega.toFixed(1) + 'MB、上限=20.0MB）';
						alert(msg);
					}
				}
			}
		}
	};

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
			cond.sortColumn = 'S.COMPANY_CD, S.SPLR_CD';
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
		NCI.redirect("./md0011.html?companyCd=" + companyCd + "&splrCd=" + splrCd);
	}

	/** 行選択時の動作 */
	function whenSelectRow() {
		const len = $('tbody input.selectable[type=checkbox]:checked').length;
		$('#btnComplete, #btnRestore').prop('disabled', len === 0);
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

		let msg = '取引先情報を登録します。よろしいですか？';
		NCI.confirm(msg, function() {
			const params = {
				encoded : $('#encoded').val(),
				fileName : $('#fileName').text()
			};
			NCI.post('/md0010/register', params).done(function(res) {
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
				rltPrtCount : null, orgCrmCount : null,
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
		NCI.download('/md0010/download', cond);
	}
});

