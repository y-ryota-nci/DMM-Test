var pager = null;
var displayMode = '0';

$(function() {

	// 検索キー
	mg0321.searchParam = {
		"companyCd" : NCI.getQueryString("companyCd"),
		"taxKndCd" : NCI.getQueryString("taxKndCd"),
		"taxSpc" : NCI.getQueryString("taxSpc")
	};

	if(NCI.getQueryString("insertFlg")){
		// 登録モード
		displayMode = '2';
	}

	// 初期化処理開始
	mg0321.init();

	// イベント
	$(document)
	// 消費税検索ボタン
	.on('click', '#taxCdSettings>.btn-primary', function() {
		const conds = {"COMPANY_CD" : MstCommon.getValue('companyCd')};
		NCI.openMasterSearch('TAX_MST', 'FOR_SEARCH_TAX_MST', callbackSearchTaxCd, conds);
	})
	// 消費税のクリアボタン
	.on('click', '#taxCdSettings>.btn-default', function() {
		MstCommon.setValue('taxCd', '');
		MstCommon.setValue('taxNm', '');
	})
	// 編集ボタン
	.on('click', '#btnChangeMode', function() {
		displayMode = '1';
		mg0321.searchParam['needMenuHtml'] = false;
		mg0321.searchParam['needFooterHtml'] = false;
		mg0321.init();
	})
	// 戻るボタン
	.on('click', '#btnBack', function() {
		NCI.redirect("./mg0320.html");
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
			var params = mg0321.getSubmitValue();
			params['needMenuHtml'] = false;
			params['needFooterHtml'] = false;

			// 初期化処理開始
			NCI.init("/mg0321/update", params).done(function(res, textStatus, jqXHR) {
				if (res && res.success) {
					// 完了メッセージ
					var msg = '登録内容の更新が完了しました。';
					$('#mg0321InformationContents').html(msg);
					$('#mg0321Information').modal({show: true});
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
				"companyCd" : $("#companyCd select").val(),
				"taxKndCd" : $("#taxKndCd select").val(),
				"taxSpc" : $("#taxSpc select").val(),
				"needMenuHtml": false,
				"needFooterHtml": false
			};

		// 登録内容をチェックする
		NCI.init("/mg0321/insertCheck", paramsForCheck).done(function(res) {

			// 同一の通貨コードがすでに登録されていた場合
			if (res) {
				var msg = '消費税種類コードはすでに登録済みです。';
				$('#checkErrorAlertBody').html(msg);
				$('#checkErrorAlert').modal({show: true});
			} else {
				var messege = '登録します。よろしいですか？';

				NCI.confirm(messege, function() {

					// 更新処理
					var params = mg0321.getSubmitValue();
					params['needMenuHtml'] = false;
					params['needFooterHtml'] = false;

					// 初期化処理開始
					NCI.init("/mg0321/insert", params).done(function(res, textStatus, jqXHR) {
						if (res && res.success) {
							// 完了メッセージ
							var msg = '登録が完了しました。';
							$('#mg0321InformationContents').html(msg);
							$('#mg0321Information').modal({show: true});
							displayMode = '1';
							mg0321.searchParam['companyCd'] = params.companyCd;
							mg0321.searchParam['taxKndCd'] = params.taxKndCd;
							mg0321.searchParam['taxSpc'] = params.taxSpc;
							mg0321.searchParam['needMenuHtml'] = false;
							mg0321.searchParam['needFooterHtml'] = false;
							mg0321.init();
						}
					});
				})
			}
		});
	})
	//エラーアラート用
	.on('click', '#checkErrorAlertButtonArea>.btn-primary', function() {
		$('#checkErrorAlert').modal("hide");
	});

	callbackSearchTaxCd = function (condition) {
		if (condition) {
			MstCommon.setValue('taxCd', condition["TAX_CD"]);
			MstCommon.setValue('taxNm', condition["TAX_NM"]);
		}
	};
});

// 画面固有JS
var mg0321 = {

	// 検索キー
	searchParam : {},

	// 初期化処理
	init : function () {
		NCI.init("/mg0321/init", mg0321.searchParam).done(function(res, textStatus, jqXHR) {
			if (res && res.success) {

				// 基本情報設定
				MstCommon.baseRenderInfo = res;

				// フィールド初期化
				MstCommon.createField(mg0321.partsInfo, res.entity, displayMode);

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

					$('#companyCd select').prop('disabled', true);
					$('#taxKndCd select').prop('disabled', true);
					$('#taxSpc select').prop('disabled', true);

				// 登録モード
				} else {
					$('#btnUpdate').hide();
					$('#btnChangeMode').hide();
					$('#btnRegister').show();

					$('#dltFg input:first').prop('checked', true);
					$('#dltFg input').prop('disabled', true);

					$('#companyCd select').prop('disabled', false);
					$('#taxKndCd select').prop('disabled', false);
					$('#taxSpc select').prop('disabled', false);
				}
			}

			// カレンダー（年月日）
			//	初期状態だとフォーカスアウト時に現在日付を保存してしまうため位置を修正
			NCI.ymdPicker($('input.ymdPicker'));

		});
	},

	// パーツ情報
	partsInfo : [
		{'selectorKey':'#companyCd', 'id':'companyCd', 'class':'form-control required', 'data-role':'dropdownCode', 'data-field':'companyCd', 'data-validate':'', 'readonly':false, 'disabled':false, 'listName':'companyItems', 'rows':'','checkLabel':'','style':''},
		{'selectorKey':'#taxKndCd', 'id':'taxKndCd', 'class':'form-control required', 'data-role':'dropdownCode', 'data-field':'taxKndCd', 'data-validate':'{"maxlength":1, "pattern": "integer"}', 'readonly':false, 'disabled':false, 'listName':'taxKndCdItems', 'rows':'','checkLabel':'','style':''},
		{'selectorKey':'#taxSpc', 'id':'taxSpc', 'class':'form-control required', 'data-role':'dropdownCode', 'data-field':'taxSpc', 'data-validate':'{"maxlength":1, "pattern": "integer"}', 'readonly':false, 'disabled':false, 'listName':'taxSpcItems', 'rows':'','checkLabel':'','style':''},
		{'selectorKey':'#taxCd', 'id':'taxCd', 'class':'form-control required', 'data-role':'text', 'data-field':'taxCd', 'data-validate':'{"maxlength":3}', 'readonly':true, 'disabled':false, 'listName':'', 'rows':'','checkLabel':'','style':''},
		{'selectorKey':'#taxNm', 'id':'taxNm', 'class':'form-control', 'data-role':'text', 'data-field':'taxNm', 'data-validate':'', 'readonly':true, 'disabled':false, 'listName':'', 'rows':'','checkLabel':'','style':''},
		{'selectorKey':'#dltFg', 'id':'dltFg', 'class':'form-control required', 'data-role':'radioCode', 'data-field':'dltFg', 'data-validate':'', 'readonly':false, 'disabled':false, 'listName':'dltFg', 'rows':'','checkLabel':'','style':''}
	],

	// サブミット時の値取得
	getSubmitValue : function () {
		var obj = {
			'companyCd' : MstCommon.getValue('companyCd'),
			'taxKndCd' : MstCommon.getValue('taxKndCd'),
			'taxSpc' : MstCommon.getValue('taxSpc'),
			'taxCd' : MstCommon.getValue('taxCd'),
			'dltFg' : MstCommon.getValue('dltFg')
		};
		return obj;
	},
};