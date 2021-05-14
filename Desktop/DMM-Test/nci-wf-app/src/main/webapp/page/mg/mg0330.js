$(function() {
	const pager = new Pager($('#seach-result'), '/mg0330/search', search).init();
	const params = { messageCds : ['MSG0208'] };
	NCI.init("/mg0330/init", params).done(function(res) {
		if (res && res.success) {

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
			})
			// 検索結果のリンク押下
			.on('click', 'a[data-field]', function(ev) {
				let $tr = $(ev.target).closest('tr');
				let companyCd = $tr.find('input[data-field=companyCd]').val();
				let lndCd = $tr.find('input.selectable').val();
				openEntry(companyCd, lndCd);
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
				let msg = '国マスタを削除します。よろしいですか？';
				NCI.confirm(msg, function() {
					// 選択されている通貨マスタ取得
					var target = '';
					$('#seach-result tbody input.selectable').each(function(i, elem) {
						let $elem = $(elem);
						let lndCd = $elem.val();
						if ($elem.prop('checked') && !!lndCd) {
							if (!!target) {
								target += ',';
							}
							target += lndCd;
						}
					});
					if (target) {
						var params = {'deleteTarget': target};
						NCI.init("/mg0330/delete", params).done(function(res) {
							if (res && res.success) {
								var msg = '国マスタを削除しました。';
								$('#mg0330InformationContents').html(msg);
								$('#mg0330Information').modal({show: true});
								search(1, true);
								$('#btnDelete').attr('disabled', 'disabled');
							}
						})
					}
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

			/// ドラッグ＆ドロップによるファイルアップロード
			FileUploader.setup("div.dragZone", "/mg0330/upload", false, displayUploadResult);

			// ファイルコントロールによるファイルアップロード
			FileUploader.setup('input[type=file]', "/mg0330/upload", false, displayUploadResult);

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
			cond.sortColumn = 'L.COMPANY_CD,L.SORT_ORDER';
			cond.sortAsc = true;
		}
		return cond;
	}

	/** 追加ボタン押下時 */
	function openEntryForInsert() {
		NCI.redirect("./mg0331.html?insertFlg=true");
	}

	/** 明細行を開く */
	function openEntry(companyCd, lndCd) {
		NCI.redirect("./mg0331.html?companyCd=" + companyCd + "&lndCd=" + lndCd);
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

		let msg = '国マスタ情報を登録します。よろしいですか？';
		NCI.confirm(msg, function() {
			const params = {
				encoded: $('#encoded').val(),
				fileName: $('#fileName').text()
			};

			// モーダルを閉じる
			$('#uploadFileSelect').modal('hide');

			NCI.post('/mg0330/register', params).done(function(res) {
				uploadReset();

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
		NCI.download('/mg0330/download', cond);
	}

});

