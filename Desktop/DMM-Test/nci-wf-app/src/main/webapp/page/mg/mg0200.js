$(function() {
	const pager = new Pager($('#seach-result'), '/mg0200/search', search).init();

	const params = { messageCds : [] };
	NCI.init("/mg0200/init", params).done(function(res) {
		if (res && res.success) {
			// 以前の検索条件を復元できれば再検索する
			$("#companyCd").val(res.companyCd);
			$("#clndYm").val(res.clndYm);

			// カレンダー（年月日）
			NCI.ymPicker($('input.ymPicker'));

			$(document).on('click', '#btnSearch', function(ev) {
				search(0);
				return false;
			})
			.on('click', '#btnPrev', function(ev) {
				search(1);
				return false;
			})
			.on('click', '#btnNext', function(ev) {
				search(2);
				return false;
			})
			.on('click', '#btnCreate', function(ev) {
				create();
				return false;
			})
			.on('click','#btnUpdate', function(ev) {
				update();
				return false;
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
			FileUploader.setup("div.dragZone", "/mg0200/upload", false, displayUploadResult);

			// ファイルコントロールによるファイルアップロード
			FileUploader.setup('input[type=file]', "/mg0200/upload", false, displayUploadResult);

			//	モーダルが閉じられたときエラーメッセージ消去
			$('#uploadFileSelect').on('hidden.bs.modal',function() {
				NCI.clearMessage();
				uploadReset();
			});
		}
	});

	/** 検索実行 */
	function search(searchType) {
		const $targets = $('#formCondition').find('input, select, text');
		if (!Validator.validate($targets, true)) {
			return false;
		}

		var params = mg0200.getConditionValue();
		params.searchType = searchType;

		// 検索処理開始
		NCI.post("/mg0200/search", params).done(function(res) {
			if (res && res.success) {
				$("#clndYm").val(res.clndYm);
				new ResponsiveTable($('#resultArea')).fillTable(res.entitys);
				$("#resultArea").removeClass("hide");
				if (res.count>0) {
					$("#btnUpdate").removeClass("hide");
				} else {
					$("#btnUpdate").addClass("hide");
				}
			}
		});
	}

	/** 生成実行 */
	function create() {
		const $targets = $('#formCondition').find('input, select, text');
		if (!Validator.validate($targets, true)) {
			return false;
		}
		var messege = '会計カレンダマスタを生成します。よろしいですか？';

		NCI.confirm(messege, function() {
			// 更新処理
			var params = mg0200.getConditionValue();

			// 生成処理開始
			NCI.init("/mg0200/create", params).done(function(res, textStatus, jqXHR) {
				if (res && res.success) {
					// 完了メッセージ
					var msg = '会計カレンダマスタの生成が完了しました。';
					$('#mg0200InformationContents').html(msg);
					$('#mg0200Information').modal({show: true});
					search(0);

					new ResponsiveTable($('#resultArea')).fillTable(res.entitys);
					$("#resultArea").removeClass("hide");
					$("#btnUpdate").removeClass("hide");
				}
			});
		});
	}

	/** 更新実行 */
	function update() {
		const $targets = $('#result-contents').find('input, select, text');
		if (!Validator.validate($targets, true)) {
			return false;
		}
		var messege = '会計カレンダマスタを更新します。よろしいですか？';

		NCI.confirm(messege, function() {
/*
			var inputs = mg0200.getUpdateValue();
			var params = {
				"entitys"	: inputs
			}
*/
			var params = mg0200.getUpdateValue();

			// 更新処理開始
			NCI.init("/mg0200/update", params).done(function(res, textStatus, jqXHR) {
				if (res && res.success) {
					// 完了メッセージ
					var msg = '会計カレンダマスタの更新が完了しました。';
					$('#mg0200InformationContents').html(msg);
					$('#mg0200Information').modal({show: true});
					search(0);
				}
			});
		});
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

		let msg = '会計カレンダー情報を登録します。よろしいですか？';
		NCI.confirm(msg, function() {
			const params = {
				encoded : $('#encoded').val(),
				fileName : $('#fileName').text()
			};
			NCI.post('/mg0200/register', params).done(function(res) {
				uploadReset();

				// モーダルを閉じる
				$('#uploadFileSelect').modal('hide');

				// 再検索
				search(0);
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
		var cond = mg0200.getConditionValue();
		NCI.download('/mg0200/download', cond);
	}

});

var mg0200 = {
	// サブミット時の値取得
	getConditionValue : function () {
		let obj = {
			"companyCd"	: $("#companyCd").val(),
			"clndYm"	: $("#clndYm").val()
		};
		return obj;
	},

	getUpdateValue : function () {
		let inputs = [];
		var companyCd = "";
		const $targets = $('#result-contents').find('tbody').find('tr');
		$targets.each(function (i, tr) {
			if (companyCd=="") {
				companyCd = $(tr).find('td[data-field=companyCd]').text();
			}
			inputs.push({
				"companyCd"			: $(tr).find('td[data-field=companyCd]').text(),
				"clndDt"			: $(tr).find('td[data-field=clndDt]').text(),
				"clndDay"			: $(tr).find('td[data-field=clndDay]').text(),
				"hldayTp"			: $(tr).find('select[data-field=hldayTp]').val(),
				"bnkHldayTp"		: $(tr).find('select[data-field=bnkHldayTp]').val(),
				"stlTpPur"			: $(tr).find('select[data-field=stlTpPur]').val(),
				"stlTpFncobl"		: $(tr).find('select[data-field=stlTpFncobl]').val(),
				"stlTpFncaff"		: $(tr).find('select[data-field=stlTpFncaff]').val(),
				"mlClsTm"			: $(tr).find('input[data-field=mlClsTmFmt]').val().replace(':',''),
				"timestampUpdated"	: $(tr).find('td[data-field=timestampUpdated]').text()
			});
		});
		var params = {
				"companyCd"	: companyCd,
				"entitys"	: inputs
			}
		return params;
	}
};
