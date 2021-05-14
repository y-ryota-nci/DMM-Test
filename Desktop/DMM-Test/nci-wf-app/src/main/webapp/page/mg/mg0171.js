var pager = null;
var displayMode = '0';

$(function() {

	// 検索キー
	mg0171.searchParam = {
		"companyCd" : NCI.getQueryString("companyCd"),
		"mnyCd" : NCI.getQueryString("mnyCd")
	};

	if(NCI.getQueryString("insertFlg")){
		// 登録モード
		displayMode = '2';
	}

	// 初期化処理開始
	mg0171.init();

	// イベント
	$(document)
	// 編集ボタン
	.on('click', '#btnChangeMode', function() {
		displayMode = '1';
		mg0171.searchParam['needMenuHtml'] = false;
		mg0171.searchParam['needFooterHtml'] = false;
		mg0171.init();
	})
	// 戻るボタン
	.on('click', '#btnBack', function() {
		NCI.redirect("./mg0170.html");
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
			var params = mg0171.getSubmitValue();
			params['needMenuHtml'] = false;
			params['needFooterHtml'] = false;

			// 初期化処理開始
			NCI.init("/mg0171/update", params).done(function(res, textStatus, jqXHR) {
				if (res && res.success) {
					// 完了メッセージ
					var msg = '登録内容の更新が完了しました。';
					$('#mg0171InformationContents').html(msg);
					$('#mg0171Information').modal({show: true});
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
				"mnyCd" : $("#mnyCd input").val(),
				"needMenuHtml": false,
				"needFooterHtml": false
			};

		// 登録内容をチェックする
		NCI.init("/mg0171/insertCheck", paramsForCheck).done(function(res) {

			// 同一の通貨コードがすでに登録されていた場合
			if (res) {
				var msg = '通貨コードはすでに登録済みです。';
				$('#checkErrorAlertBody').html(msg);
				$('#checkErrorAlert').modal({show: true});
			} else {
				var messege = '登録します。よろしいですか？';

				NCI.confirm(messege, function() {

					// 更新処理
					var params = mg0171.getSubmitValue();
					params['needMenuHtml'] = false;
					params['needFooterHtml'] = false;

					// 初期化処理開始
					NCI.init("/mg0171/insert", params).done(function(res, textStatus, jqXHR) {
						if (res && res.success) {
							// 完了メッセージ
							var msg = '登録が完了しました。';
							$('#mg0171InformationContents').html(msg);
							$('#mg0171Information').modal({show: true});
							displayMode = '1';
							mg0171.searchParam['companyCd'] = params.companyCd;
							mg0171.searchParam['mnyCd'] = params.mnyCd;
							mg0171.searchParam['needMenuHtml'] = false;
							mg0171.searchParam['needFooterHtml'] = false;
							mg0171.init();
						}
					});
				})
			}
		});
	})
	// 会社コードの選択ポップアップ起動
	.on('click', '#btnCompanyCd', function() {
		const conds = {};
		NCI.openMasterSearch('WFM_CORPORATION_V', 'FOR_POPUP_COMPANY', callbackFromSelectCompany, conds);
	})
	// 会社コードのクリアボタン
	.on('click', '#btnClearCompany', function() {
		clearForCompany();
	})
	//エラーアラート用
	.on('click', '#checkErrorAlertButtonArea>.btn-primary', function() {
		$('#checkErrorAlert').modal("hide");
	});
});

// 画面固有JS
var mg0171 = {

	// 検索キー
	searchParam : {},

	// 初期化処理
	init : function () {
		NCI.init("/mg0171/init", mg0171.searchParam).done(function(res, textStatus, jqXHR) {
			if (res && res.success) {

				// 基本情報設定
				MstCommon.baseRenderInfo = res;

				// フィールド初期化
				MstCommon.createField(mg0171.partsInfo, res.entity, displayMode);

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

				// 登録モード
				} else {
					$('#btnUpdate').hide();
					$('#btnChangeMode').hide();
					$('#btnRegister').show();
					$('#mnyCd input').removeAttr('readonly');
					$('#dltFg input:first').prop('checked', true);
					$('#dltFg input').prop('disabled', true);
					$('#companyCd select').prop('disabled', false);
				}
			}

			// カレンダー（年月日）
			//	初期状態だとフォーカスアウト時に現在日付を保存してしまうため位置を修正
			NCI.ymdPicker($('input.ymdPicker'));

		});
	},

	// パーツ情報（銀行マスタ）
	partsInfo : [
		{'selectorKey':'#companyCd', 'id':'companyCd', 'class':'form-control required', 'data-role':'dropdownCode', 'data-field':'companyCd', 'data-validate':'', 'readonly':false, 'disabled':false, 'listName':'companyItems', 'rows':'','checkLabel':'','style':''},
		{'selectorKey':'#mnyCd', 'id':'mnyCd', 'class':'form-control required', 'data-role':'text', 'data-field':'mnyCd', 'data-validate':'{"maxlength":3, "pattern": "alphaNumber"}', 'readonly':true, 'disabled':false, 'listName':'', 'rows':'','checkLabel':'','style':''},
		{'selectorKey':'#mnyNm', 'id':'mnyNm', 'class':'form-control required', 'data-role':'text', 'data-field':'mnyNm', 'data-validate':'{"maxlength":40}', 'readonly':false, 'disabled':false, 'listName':'', 'rows':'','checkLabel':'','style':''},
		{'selectorKey':'#mnyMrk', 'id':'mnyMrk', 'class':'form-control', 'data-role':'text', 'data-field':'mnyMrk', 'data-validate':'{"maxlength":3}', 'readonly':false, 'disabled':false, 'listName':'', 'rows':'','checkLabel':'','style':''},
		{'selectorKey':'#rdxpntGdt', 'id':'rdxpntGdt', 'class':'form-control required', 'data-role':'text', 'data-field':'rdxpntGdt', 'data-validate':'{"maxlength":1, "pattern":"numeric"}', 'readonly':false, 'disabled':false, 'listName':'', 'rows':'','checkLabel':'','style':''},
		{'selectorKey':'#sortOrder', 'id':'sortOrder', 'class':'form-control', 'data-role':'text', 'data-field':'sortOrder', 'data-validate':'{"maxlength":5, "pattern":"numeric"}', 'readonly':false, 'disabled':false, 'listName':'', 'rows':'','checkLabel':'','style':''},
		{'selectorKey':'#dltFg', 'id':'dltFg', 'class':'form-control required', 'data-role':'radioCode', 'data-field':'dltFg', 'data-validate':'', 'readonly':false, 'disabled':false, 'listName':'dltFg', 'rows':'','checkLabel':'','style':''}
	],

	// サブミット時の値取得
	getSubmitValue : function () {
		var obj = {
			'companyCd' : MstCommon.getValue('companyCd'),
			'mnyCd' : MstCommon.getValue('mnyCd'),
			'mnyNm' : MstCommon.getValue('mnyNm'),
			'mnyMrk' : MstCommon.getValue('mnyMrk'),
			'rdxpntGdt' : MstCommon.getValue('rdxpntGdt'),
			'sortOrder' : MstCommon.getValue('sortOrder'),
			'dltFg' : MstCommon.getValue('dltFg')
		};
		return obj;
	},
};