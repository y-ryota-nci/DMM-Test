var pager = null;
var displayMode = '0';

$(function() {

	// 検索キー
	mg0251.searchParam = {
		"companyCd" : NCI.getQueryString("companyCd"),
		"bumonCd" : NCI.getQueryString("bumonCd"),
		"orgnzCd" : NCI.getQueryString("orgnzCd")
	};

	if(NCI.getQueryString("insertFlg")){
		// 登録モード
		displayMode = '2';
	}

	// 初期化処理開始
	mg0251.init();

	// イベント
	$(document)
	// 編集ボタン
	.on('click', '#btnChangeMode', function() {
		displayMode = '1';
		mg0251.searchParam['needMenuHtml'] = false;
		mg0251.searchParam['needFooterHtml'] = false;
		mg0251.init();
	})
	// 戻るボタン
	.on('click', '#btnBack', function() {
		NCI.redirect("./mg0250.html");
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
			var params = mg0251.getSubmitValue();
			params['needMenuHtml'] = false;
			params['needFooterHtml'] = false;

			// 初期化処理開始
			NCI.init("/mg0251/update", params).done(function(res, textStatus, jqXHR) {
				if (res && res.success) {
					// 完了メッセージ
					var msg = '登録内容の更新が完了しました。';
					$('#mg0251InformationContents').html(msg);
					$('#mg0251Information').modal({show: true});
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
				"bumonCd" : $("#bumonCd input").val(),
				"orgnzCd" : $("#orgnzCd input").val(),
				"needMenuHtml": false,
				"needFooterHtml": false
			};

		// 登録内容をチェックする
		NCI.init("/mg0251/insertCheck", paramsForCheck).done(function(res) {

			// 同一の通貨コードがすでに登録されていた場合
			if (res) {
				var msg = '部門関連はすでに登録済みです。';
				$('#checkErrorAlertBody').html(msg);
				$('#checkErrorAlert').modal({show: true});
			} else {
				var messege = '登録します。よろしいですか？';

				NCI.confirm(messege, function() {

					// 更新処理
					var params = mg0251.getSubmitValue();
					params['needMenuHtml'] = false;
					params['needFooterHtml'] = false;

					// 初期化処理開始
					NCI.init("/mg0251/insert", params).done(function(res, textStatus, jqXHR) {
						if (res && res.success) {
							// 完了メッセージ
							var msg = '登録が完了しました。';
							$('#mg0251InformationContents').html(msg);
							$('#mg0251Information').modal({show: true});
							displayMode = '1';
							mg0251.searchParam['companyCd'] = params.companyCd;
							mg0251.searchParam['bumonCd'] = params.bumonCd;
							mg0251.searchParam['orgnzCd'] = params.orgnzCd;
							mg0251.searchParam['needMenuHtml'] = false;
							mg0251.searchParam['needFooterHtml'] = false;
							mg0251.init();
						}
					});
				})
			}
		});
	})
	//エラーアラート用
	.on('click', '#checkErrorAlertButtonArea>.btn-primary', function() {
		$('#checkErrorAlert').modal("hide");
	})

	// 部門検索ボタン
	.on('click', '#bumonCdSettings>.btn-primary', function() {
		// 部門選択（企業を選択済みならその企業の）
		const conds = { "COMPANY_CD" : $('#companyCd option:selected').val()};
		NCI.openMasterSearch('BUMON_MST', 'FOR_BTN_BUMON', callbackSearchBumon, conds);
	})
	// 部門のクリアボタン
	.on('click', '#bumonCdSettings>.btn-default', function() {
		MstCommon.setValue('bumonCd', '');
		MstCommon.setValue('bumonNm', '');
	})

	// 組織検索ボタン
	.on('click', '#orgnzCdSettings>.btn-primary', function() {
		// 選択ポップアップ起動
		// 組織選択（企業を選択済みならその企業の）
		const params = null, corporationCode = MstCommon.getValue('companyCd');
		let url = "../cm/cm0020.html?initSearch=true";
		if (corporationCode)
			url += "&corporationCode=" + corporationCode + "&organizationLevel=3";
		Popup.open(url, callbackSearchOrgnz, params, this);
	})
	// 組織のクリアボタン
	.on('click', '#orgnzCdSettings>.btn-default', function() {
		MstCommon.setValue('orgnzCd', '');
		MstCommon.setValue('orgnzNm', '');
	})
	;

	callbackSearchBumon = function (condition) {
		if (condition) {
			MstCommon.setValue('bumonCd', condition["BUMON_CD"]);
			MstCommon.setValue('bumonNm', condition["BUMON_NM"]);
		}
	};

	callbackSearchOrgnz = function (org, trigger) {
		if (org) {
			MstCommon.setValue('orgnzCd', org.organizationCode);
			MstCommon.setValue('orgnzNm', org.organizationName);
		}
	};
});

// 画面固有JS
var mg0251 = {

	// 検索キー
	searchParam : {},

	// 初期化処理
	init : function () {
		NCI.init("/mg0251/init", mg0251.searchParam).done(function(res, textStatus, jqXHR) {
			if (res && res.success) {

				// 基本情報設定
				MstCommon.baseRenderInfo = res;

				// フィールド初期化
				MstCommon.createField(mg0251.partsInfo, res.entity, displayMode);

				// ボタン表示
				if (displayMode == '0') {
					$('#btnUpdate').hide();
					$('#btnChangeMode').show();
					$('#btnRegister').hide();
					$('#bumonCdSettings').hide();
					$('#orgnzCdSettings').hide();
				// 編集モード
				} else if(displayMode == '1') {
					$('#btnUpdate').show();
					$('#btnChangeMode').hide();
					$('#btnRegister').hide();
					$('#companyCd select').prop('disabled', true);
					$('#bumonCdSettings').hide();
					$('#orgnzCdSettings').hide();

				// 登録モード
				} else {
					$('#btnUpdate').hide();
					$('#btnChangeMode').hide();
					$('#btnRegister').show();
					$('#dltFg input:first').prop('checked', true);
					$('#dltFg input').prop('disabled', true);
					$('#companyCd select').prop('disabled', false);
					$('#bumonCdSettings').show();
					$('#orgnzCdSettings').show();
				}
			}
		});
	},

	// パーツ情報
	partsInfo : [
		{'selectorKey':'#companyCd', 'id':'companyCd', 'class':'form-control required', 'data-role':'dropdownCode', 'data-field':'companyCd', 'data-validate':'', 'readonly':false, 'disabled':false, 'listName':'companyItems', 'rows':'','checkLabel':'','style':''},
		{'selectorKey':'#bumonCd', 'id':'bumonCd', 'class':'form-control required', 'data-role':'text', 'data-field':'bumonCd', 'data-validate':'{"maxlength":14, "pattern": "alphaNumber"}', 'readonly':true, 'disabled':false, 'listName':'', 'rows':'','checkLabel':'','style':''},
		{'selectorKey':'#bumonNm', 'id':'bumonNm', 'class':'form-control', 'data-role':'text', 'data-field':'bumonNm', 'data-validate':'', 'readonly':true, 'disabled':false, 'listName':'', 'rows':'','checkLabel':'','style':''},
		{'selectorKey':'#orgnzCd', 'id':'orgnzCd', 'class':'form-control required', 'data-role':'text', 'data-field':'orgnzCd', 'data-validate':'{"maxlength":10, "pattern": "alphaNumber"}', 'readonly':true, 'disabled':false, 'listName':'', 'rows':'','checkLabel':'','style':''},
		{'selectorKey':'#orgnzNm', 'id':'orgnzNm', 'class':'form-control', 'data-role':'text', 'data-field':'orgnzNm', 'data-validate':'', 'readonly':true, 'disabled':false, 'listName':'', 'rows':'','checkLabel':'','style':''},
		{'selectorKey':'#dltFg', 'id':'dltFg', 'class':'form-control required', 'data-role':'radioCode', 'data-field':'dltFg', 'data-validate':'', 'readonly':false, 'disabled':false, 'listName':'dltFg', 'rows':'','checkLabel':'','style':''}
	],

	// サブミット時の値取得
	getSubmitValue : function () {
		var obj = {
			'companyCd' : MstCommon.getValue('companyCd'),
			'bumonCd' : MstCommon.getValue('bumonCd'),
			'orgnzCd' : MstCommon.getValue('orgnzCd'),
			'dltFg' : MstCommon.getValue('dltFg')
		};
		return obj;
	},
};