var pager = null;
var displayMode = '0';
$(function() {
	// 検索キー
	mg0331.searchParam = {
		"companyCd" : NCI.getQueryString("companyCd"),
		"lndCd" : NCI.getQueryString("lndCd")
	};

	if(NCI.getQueryString("insertFlg")){
		// 登録モード
		displayMode = '2';
	}

	// 初期化処理開始
	mg0331.init();

	// イベント
	$(document)
	// 編集ボタン
	.on('click', '#btnChangeMode', function() {
		displayMode = '1';
		mg0331.searchParam['needMenuHtml'] = false;
		mg0331.searchParam['needFooterHtml'] = false;
		mg0331.init();
	})
	// 戻るボタン
	.on('click', '#btnBack', function() {
		NCI.redirect("./mg0330.html");
	})
	// 更新ボタン
	.on('click', '#btnUpdate', function() {
		const $targets = $('#editArea').find('input, select, text');
		if (!Validator.validate($targets, true)) {
			return false;
		}
		var messege = '登録内容を更新します。よろしいですか？';

		NCI.confirm(messege, function() {
			// 更新処理
			var params = mg0331.getSubmitValue();
			params['needMenuHtml'] = false;
			params['needFooterHtml'] = false;

			// 初期化処理開始
			NCI.init("/mg0331/update", params).done(function(res, textStatus, jqXHR) {
				if (res && res.success) {
					// 完了メッセージ
					var msg = '登録内容の更新が完了しました。';
					$('#mg0331InformationContents').html(msg);
					$('#mg0331Information').modal({show: true});
				}
			});
		});
	})
	// 登録ボタン
	.on('click', '#btnRegister', function() {

		const $targets = $('#editArea').find('input, select, text');
		if (!Validator.validate($targets, true)) {
			return false;
		}

		var paramsForCheck =  {
			"lndCd" : $("#lndCd input").val(),
			"needMenuHtml": false,
			"needFooterHtml": false
		};

		// 登録内容をチェックする
		NCI.init("/mg0331/insertCheck", paramsForCheck).done(function(res) {
			// 同一の通貨コードがすでに登録されていた場合
			if (res) {
				var msg = '国情報はすでに登録済みか有効でないか再度ご確認ください。';
				$('#checkErrorAlertBody').html(msg);
				$('#checkErrorAlert').modal({show: true});
			} else {
				var messege = '登録します。よろしいですか？';

				NCI.confirm(messege, function() {

					// 更新処理
					var params = mg0331.getSubmitValue();
					params['needMenuHtml'] = false;
					params['needFooterHtml'] = false;

					// 初期化処理開始
					NCI.init("/mg0331/insert", params).done(function(res, textStatus, jqXHR) {
						if (res && res.success) {
							// 完了メッセージ
							var msg = '登録が完了しました。';
							$('#mg0331InformationContents').html(msg);
							$('#mg0331Information').modal({show: true});
							displayMode = '1';
							mg0331.searchParam['companyCd'] = params.companyCd;
							mg0331.searchParam['zipCd'] = params.zipCd;
							mg0331.searchParam['sqno'] = res.sqno;
							mg0331.searchParam['needMenuHtml'] = false;
							mg0331.searchParam['needFooterHtml'] = false;
							mg0331.init();
						}
					});
				});
			}
		});
	})
	//エラーアラート用
	.on('click', '#checkErrorAlertButtonArea>.btn-primary', function() {
		$('#checkErrorAlert').modal("hide");
	});

});

// 画面固有JS
var mg0331 = {

	// 検索キー
	searchParam : {},

	// 初期化処理
	init : function () {
		NCI.init("/mg0331/init", mg0331.searchParam).done(function(res, textStatus, jqXHR) {
			if (res && res.success) {

				// 基本情報設定
				MstCommon.baseRenderInfo = res;

				// フィールド初期化
				MstCommon.createField(mg0331.partsInfo, res.entity, displayMode);

				// ボタン表示
				if (displayMode == '0') {
					$('#btnUpdate').hide();
					$('#btnChangeMode').show();
					$('#btnRegister').hide();
				// 編集モード
				} else if(displayMode == '1') {
					$('#btnUpdate').show();
					$('#btnChangeMode').hide();
					$('#btnRegister').hide();
					$('#lndCd input').prop('disabled', true);

				// 登録モード
				} else {
					$('#btnUpdate').hide();
					$('#btnChangeMode').hide();
					$('#btnRegister').show();
					$('#lndCd input').removeAttr('readonly');
					$('#lndCd input').prop('disabled', false);
					$('#dltFg input:first').prop('checked', true);
					$('#dltFg input').prop('disabled', true);
				}
			}
		});
	},

	// パーツ情報
	partsInfo : [
		{'selectorKey':'#lndCd', 'id':'lndCd', 'class':'form-control required', 'data-role':'text', 'data-field':'lndCd', 'data-validate':'{"maxlength":3, "pattern": "alphaNumber"}', 'readonly':true, 'disabled':false, 'listName':'', 'rows':'','checkLabel':'','style':''},
		{'selectorKey':'#lndNm', 'id':'lndNm', 'class':'form-control required', 'data-role':'text', 'data-field':'lndNm', 'data-validate':'{"maxlength":100}', 'readonly':false, 'disabled':false, 'listName':'', 'rows':'','checkLabel':'','style':''},
		{'selectorKey':'#lndCdDjii', 'id':'lndCdDjii', 'class':'form-control required', 'data-role':'text', 'data-field':'lndCdDjii', 'data-validate':'{"maxlength":10, "pattern": "alphaNumber"}', 'readonly':false, 'disabled':false, 'listName':'', 'rows':'','checkLabel':'','style':''},
		{'selectorKey':'#sortOrder', 'id':'sortOrder', 'class':'form-control required', 'data-role':'text', 'data-field':'sortOrder', 'data-validate':'{"maxlength":3, "pattern":"numeric"}', 'readonly':false, 'disabled':false, 'listName':'', 'rows':'','checkLabel':'','style':''},
		{'selectorKey':'#dltFg', 'id':'dltFg', 'class':'form-control required', 'data-role':'radioCode', 'data-field':'dltFg', 'data-validate':'', 'readonly':false, 'disabled':false, 'listName':'dltFgItems', 'rows':'','checkLabel':'','style':''}
	],

	// サブミット時の値取得
	getSubmitValue : function () {
		var obj = {
			'lndCd' : MstCommon.getValue('lndCd'),
			'lndNm' : MstCommon.getValue('lndNm'),
			'lndCdDjii' : MstCommon.getValue('lndCdDjii'),
			'sortOrder' : MstCommon.getValue('sortOrder'),
			'dltFg' : MstCommon.getValue('dltFg')
		};
		return obj;
	},
};