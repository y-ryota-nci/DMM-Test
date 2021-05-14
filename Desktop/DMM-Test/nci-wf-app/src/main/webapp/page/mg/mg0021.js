var pager = null;
var displayMode = '0';

$(function() {

	// 検索キー
	mg0021.searchParam = {
		"companyCd" : NCI.getQueryString("companyCd"),
		"itmexpsCd" : NCI.getQueryString("itmexpsCd")
	};

	if(NCI.getQueryString("insertFlg")){
		// 登録モード
		displayMode = '2';
	}

	// 初期化処理開始
	mg0021.init();

	// イベント
	$(document)
	// 編集ボタン
	.on('click', '#btnChangeMode', function() {
		// 編集モード
		displayMode = '1';
		mg0021.searchParam['needMenuHtml'] = false;
		mg0021.searchParam['needFooterHtml'] = false;
		mg0021.init();
	})
	// 戻るボタン
	.on('click', '#btnBack', function() {
		NCI.redirect("./mg0020.html");
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
			var params = mg0021.getSubmitValue();
			params['needMenuHtml'] = false;
			params['needFooterHtml'] = false;

			// 初期化処理開始
			NCI.init("/mg0021/update", params).done(function(res, textStatus, jqXHR) {
				if (res && res.success) {
					// 完了メッセージ
					var msg = '登録内容の更新が完了しました。';
					$('#mg0021InformationContents').html(msg);
					$('#mg0021Information').modal({show: true});
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
				"itmexpsCd" : $("#itmexpsCd input").val(),
				"needMenuHtml": false,
				"needFooterHtml": false
			};

		// 登録内容をチェックする
		NCI.init("/mg0021/insertCheck", paramsForCheck).done(function(res) {

			// 同一の銀行コードがすでに登録されていた場合
			if (res) {
				var msg = '費目コードはすでに登録済みです。';
				$('#checkErrorAlertBody').html(msg);
				$('#checkErrorAlert').modal({show: true});
			} else {
				var messege = '登録します。よろしいですか？';

				NCI.confirm(messege, function() {

					// 更新処理
					var params = mg0021.getSubmitValue();
					params['needMenuHtml'] = false;
					params['needFooterHtml'] = false;

					// 初期化処理開始
					NCI.init("/mg0021/insert", params).done(function(res, textStatus, jqXHR) {
						if (res && res.success) {
							// 完了メッセージ
							var msg = '登録が完了しました。';
							$('#mg0021InformationContents').html(msg);
							$('#mg0021Information').modal({show: true});
							displayMode = '1';
							mg0021.searchParam['companyCd'] = params.companyCd;
							mg0021.searchParam['itmexpsCd'] = params.itmexpsCd;
							mg0021.searchParam['needMenuHtml'] = false;
							mg0021.searchParam['needFooterHtml'] = false;
							mg0021.init();
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
	})
});

// 画面固有JS
var mg0021 = {

	// 検索キー
	searchParam : {},

	// 初期化処理
	init : function () {
		NCI.init("/mg0021/init", mg0021.searchParam).done(function(res, textStatus, jqXHR) {
			if (res && res.success) {

				// 基本情報設定
				MstCommon.baseRenderInfo = res;

				// フィールド初期化
				MstCommon.createField(mg0021.partsInfo, res.entity, displayMode);

				// ボタン表示
				// 閲覧モード
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
					$('#itmexpsCd input').removeAttr('readonly');
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

	// パーツ情報（費目マスタ）
	partsInfo : [
		{'selectorKey':'#companyCd', 'id':'companyCd', 'class':'form-control required', 'data-role':'dropdownCode', 'data-field':'companyCd', 'data-validate':'', 'readonly':false, 'disabled':false, 'listName':'companyItems', 'rows':'','checkLabel':'','style':''},
		{'selectorKey':'#itmexpsCd', 'id':'itmexpsCd', 'class':'form-control required', 'data-role':'text', 'data-field':'itmexpsCd', 'data-validate':'{"maxlength":8, "pattern":"alphaNumberUnderscore"}', 'readonly':true, 'disabled':false, 'listName':'', 'rows':'','checkLabel':'','style':''},
		{'selectorKey':'#itmexpsLevel', 'id':'itmexpsLevel', 'class':'form-control required', 'data-role':'text', 'data-field':'itmexpsLevel', 'data-validate':'{"maxlength":5, "pattern":"numberOnly"}', 'readonly':false, 'disabled':false, 'listName':'', 'rows':'','checkLabel':'','style':''},
		{'selectorKey':'#itmexpsNm', 'id':'itmexpsNm', 'class':'form-control required', 'data-role':'text', 'data-field':'itmexpsNm', 'data-validate':'{"maxlength":60}', 'readonly':false, 'disabled':false, 'listName':'', 'rows':'','checkLabel':'','style':''},
		{'selectorKey':'#itmexpsNmS', 'id':'itmexpsNmS', 'class':'form-control required', 'data-role':'text', 'data-field':'itmexpsNmS', 'data-validate':'{"maxlength":20}', 'readonly':false, 'disabled':false, 'listName':'', 'rows':'','checkLabel':'','style':''},
		{'selectorKey':'#dltFg', 'id':'dltFg', 'class':'form-control required', 'data-role':'radioCode', 'data-field':'dltFg', 'data-validate':'', 'readonly':false, 'disabled':false, 'listName':'dltFgNm', 'rows':'','checkLabel':'','style':''}
	],

	// サブミット時の値取得
	getSubmitValue : function () {
		var obj = {
			'companyCd' : MstCommon.getValue('companyCd'),
			'itmexpsCd' : MstCommon.getValue('itmexpsCd'),
			'itmexpsLevel' : MstCommon.getValue('itmexpsLevel'),
			'itmexpsNm' : MstCommon.getValue('itmexpsNm'),
			'itmexpsNmS' : MstCommon.getValue('itmexpsNmS'),
			'dltFg' : MstCommon.getValue('dltFg')
		};
		return obj;
	},
};