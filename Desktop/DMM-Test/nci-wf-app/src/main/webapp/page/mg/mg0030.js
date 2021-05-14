$(function() {
	const pager = new Pager($('#seach-result'), '/mg0030/search', search).init();

	const params = { messageCds : [] };
	NCI.init("/mg0030/init", params).done(function(res) {
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
				clearForOrg();
				clearForItmExps1();
				clearForItmExps2();
				clearForJrn();
			})
			// 検索結果のリンク押下
			.on('click', 'a[data-field]', function(ev) {
				var companyCd = $('>.first>.companyCd', $(this).parent().parent()).val();
				var orgnzCd = $('>.first>.orgnzCd', $(this).parent().parent()).val();
				var itmExpsCd1 = $('>.first>.itmExpsCd1', $(this).parent().parent()).val();
				var itmExpsCd2 = $('>.first>.itmExpsCd2', $(this).parent().parent()).val();

				openEntry(companyCd, orgnzCd, itmExpsCd1, itmExpsCd2);
			})
			// 行選択
			.on('click', 'input.selectable[type=checkbox]', function() {
				whenSelectRow();
			})
			// 一覧選択時
			.on('click', '#seach-result .selectable', function() {

			})
			.on('click', '#btnSelectOrg', function() {
				// 組織選択（企業を選択済みならその企業の）
				const params = null, corporationCode = $('#companyCd option:checked').val();
				let url = "../cm/cm0020.html?initSearch=true";
				if (corporationCode)
					url += "&corporationCode=" + corporationCode + "&organizationLevel=3";
				Popup.open(url, callbackSearchOrgnz, params, this);
			})
			.on('click', '#btnClearOrg', function(){
				// マスタのクリア
				clearForOrg();
			})

			.on('click', '#btnSelectItmExps1', function() {
				const conds = { "COMPANY_CD" : $('#companyCd select').val(), "ITMEXPS_LEVEL":"1" };
				NCI.openMasterSearch('ITMEXPS_MST', 'FOR_ITMEXPS_POPUP_SEARCH', callbackSearchItmExps1, conds);
			})
			.on('click', '#btnClearItmExps1', function(){
				// マスタのクリア
				clearForItmExps1();
			})

			.on('click', '#btnSelectItmExps2', function() {
				const conds = { "COMPANY_CD" : $('#companyCd select').val(), "ITMEXPS_LEVEL":"2" };
				NCI.openMasterSearch('ITMEXPS_MST', 'FOR_ITMEXPS_POPUP_SEARCH', callbackSearchItmExps2, conds);
			})
			.on('click', '#btnClearItmExps2', function(){
				// マスタのクリア
				clearForItmExps2();
			})

			.on('click', '#btnSelectJrn', function() {
				const conds = {};
				NCI.openMasterSearch('JRN_MST', 'FOR_JRN_POPUP_SEARCH', callbackSearchJrn, conds);
			})

			// クリックイベントに指定されているIDが間違っているので修正
			// そもそも仕分けマスタ自体が対象外になっているため不要？
			// 柚木さんか藻形さんに確認をとること上のselect含め
			.on('click', '#btnClearJrn', function(){
				// マスタのクリア
				clearForJrn();
			})

			//エラーアラート用
			.on('click', '#checkErrorAlertButtonArea>.btn-primary', function() {
				$('#checkErrorAlert').modal("hide");
			})
			// 削除ボタン
			.on('click', '#btnDelete', function() {
				let msg = '費目関連情報を削除します。よろしいですか？';
				NCI.confirm(msg, function() {

					// 選択されている費目関連情報取得
					var target = '';
					var targetCnt = 0;

					for(var i=0; i<$('#seach-result .selectable').size(); i++) {
						if ($('#seach-result .selectable').get(i).checked) {
							var checkTarget = $('#seach-result .selectable').get(i);
							var companyCd = $('>.first>.companyCd', $(checkTarget).parent().parent()).val();
							var orgnzCd = $('>.first>.orgnzCd', $(checkTarget).parent().parent()).val();
							var itmExpsCd1 = $('>.first>.itmExpsCd1', $(checkTarget).parent().parent()).val();
							var itmExpsCd2 = $('>.first>.itmExpsCd2', $(checkTarget).parent().parent()).val();

							if (targetCnt != 0) {
								target += ',';
							}

							target += companyCd + '|' + orgnzCd + '|' + itmExpsCd1 + '|' + itmExpsCd2;
							targetCnt++;
						}
					}

					if (target) {
						var params = {'deleteTarget':target};

						NCI.init("/mg0030/delete", params).done(function(res) {
							if (res && res.success) {
								var msg = '費目関連情報を削除しました。';
								$('#mg0030InformationContents').html(msg);
								$('#mg0030Information').modal({show: true});
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
			FileUploader.setup("div.dragZone", "/mg0030/upload", false, displayUploadResult);

			// ファイルコントロールによるファイルアップロード
			FileUploader.setup('input[type=file]', "/mg0030/upload", false, displayUploadResult);

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
			cond.sortColumn = 'I2.COMPANY_CD, I2.ORGNZ_CD, I2.ITMEXPS_CD1, I2.ITMEXPS_CD2';
			cond.sortAsc = true;
		}
		return cond;
	}

	/** 追加ボタン押下時 */
	function openEntryForInsert() {
		NCI.redirect("./mg0031.html?insertFlg=true");
	}

	/** 明細行（費目関連マスタ）を開く */
	function openEntry(companyCd, orgnzCd, itmExpsCd1, itmExpsCd2) {
		NCI.redirect("./mg0031.html?companyCd=" + companyCd + "&orgnzCd=" + escape(orgnzCd) + "&itmExpsCd1=" + itmExpsCd1 + "&itmExpsCd2=" + itmExpsCd2);
	}

	/** 行選択時の動作 */
	function whenSelectRow() {
		const len = $('tbody input.selectable[type=checkbox]:checked').length;
		$('#btnDelete').prop('disabled', len === 0);
	}

	callbackSearchOrgnz = function (org, trigger) {
		if (org) {
			$('#orgnzCd').val(org.organizationCode);
			$('#orgnzNm').val(org.organizationName);
		}
	};

	callbackSearchItmExps1 = function (condition) {
		if (condition) {
			$('#itmExpsCd1').val(condition["ITMEXPS_CD"]);
			$('#itmExpsNm1').val(condition["ITMEXPS_NM"]);
		}
	};

	callbackSearchItmExps2 = function (condition) {
		if (condition) {
			$('#itmExpsCd2').val(condition["ITMEXPS_CD"]);
			$('#itmExpsNm2').val(condition["ITMEXPS_NM"]);
		}
	};

	callbackSearchJrn = function (condition) {
		if (condition) {
			$('#jrnCd').val(condition["JRN_CD"]);
			$('#jrnNm').val(condition["JRN_NM"]);
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

		let msg = '費目関連情報を登録します。よろしいですか？';
		NCI.confirm(msg, function() {
			const params = {
				encoded : $('#encoded').val(),
				fileName : $('#fileName').text()
			};
			NCI.post('/mg0030/register', params).done(function(res) {
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
		NCI.download('/mg0030/download', cond);
	}

	/** 組織コード選択ポップアップをクリア */
	function clearForOrg(){
		$('#orgnzCd').val('').trigger('validate');
		$('#orgnzNm').val('');
	}

	/** 費目1コード選択ポップアップをクリア */
	function clearForItmExps1(){
		$('#itmExpsCd1').val('').trigger('validate');
		$('#itmExpsNm1').val('');

	}

	/** 費目2コード選択ポップアップをクリア */
	function clearForItmExps2(){
		$('#itmExpsCd2').val('').trigger('validate');
		$('#itmExpsNm2').val('');

	}

	/** 仕分けコード選択ポップアップをクリア */
	function clearForJrn(){
		$('#jrnCd').val('').trigger('validate');
		$('#jrnNm').val('');

	}

});

