var pager = null;
var displayMode = '0';

$(function() {

	// 検索キー
	mg0261.searchParam = {
		"companyCd" : NCI.getQueryString("companyCd"),
		"splrCd" : NCI.getQueryString("splrCd"),
		"usrCd" : NCI.getQueryString("usrCd")
	};

	if(NCI.getQueryString("insertFlg")){
		// 登録モード
		displayMode = '2';
	}

	// 初期化処理開始
	mg0261.init();

	// イベント
	$(document)
	// 編集ボタン
	.on('click', '#btnChangeMode', function() {
		displayMode = '1';
		mg0261.searchParam['needMenuHtml'] = false;
		mg0261.searchParam['needFooterHtml'] = false;
		mg0261.init();
	})
	// 戻るボタン
	.on('click', '#btnBack', function() {
		NCI.redirect("./mg0260.html");
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
			var params = mg0261.getSubmitValue();
			params['needMenuHtml'] = false;
			params['needFooterHtml'] = false;

			// 初期化処理開始
			NCI.init("/mg0261/update", params).done(function(res, textStatus, jqXHR) {
				if (res && res.success) {
					// 完了メッセージ
					var msg = '登録内容の更新が完了しました。';
					$('#mg0261InformationContents').html(msg);
					$('#mg0261Information').modal({show: true});
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
				"splrCd" : $("#splrCd input").val(),
				"usrCd" : $("#usrCd input").val(),
				"needMenuHtml": false,
				"needFooterHtml": false
			};

		// 登録内容をチェックする
		NCI.init("/mg0261/insertCheck", paramsForCheck).done(function(res) {

			// 同一の通貨コードがすでに登録されていた場合
			if (res) {
				var msg = 'ｸﾚｶ口座はすでに登録済みです。';
				$('#checkErrorAlertBody').html(msg);
				$('#checkErrorAlert').modal({show: true});
			} else {
				var messege = '登録します。よろしいですか？';

				NCI.confirm(messege, function() {

					// 更新処理
					var params = mg0261.getSubmitValue();
					params['needMenuHtml'] = false;
					params['needFooterHtml'] = false;

					// 初期化処理開始
					NCI.init("/mg0261/insert", params).done(function(res, textStatus, jqXHR) {
						if (res && res.success) {
							// 完了メッセージ
							var msg = '登録が完了しました。';
							$('#mg0261InformationContents').html(msg);
							$('#mg0261Information').modal({show: true});
							displayMode = '1';
							mg0261.searchParam['companyCd'] = params.companyCd;
							mg0261.searchParam['splrCd'] = params.splrCd;
							mg0261.searchParam['usrCd'] = params.usrCd;
							mg0261.searchParam['needMenuHtml'] = false;
							mg0261.searchParam['needFooterHtml'] = false;
							mg0261.init();
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

	// ユーザ検索ボタン
	.on('click', '#usrCdSettings>.btn-primary', function() {
		// 選択ポップアップ起動
		// 組織選択（企業を選択済みならその企業の）
		const params = null, corporationCode = MstCommon.getValue('companyCd');
		let url = "../cm/cm0040.html?initSearch=true";
		if (corporationCode)
			url += "&corporationCode=" + corporationCode;
		Popup.open(url, callbackSearchUser, params, this);
	})
	// 組織のクリアボタン
	.on('click', '#usrCdSettings>.btn-default', function() {
		MstCommon.setValue('usrCd', '');
		MstCommon.setValue('usrNm', '');
	})

	// 部門検索ボタン
	.on('click', '#bnkaccCdSettings>.btn-primary', function() {
		// 部門選択（企業を選択済みならその企業の）
		const conds = { "COMPANY_CD" : $('#companyCd option:selected').val()};
		NCI.openMasterSearch('V_BNKACC_MST', 'FOR_POPUP', callbackSearchBankAcc, conds);
	})
	// 部門のクリアボタン
	.on('click', '#bnkaccCdSettings>.btn-default', function() {
		MstCommon.setValue('bnkaccCd', '');
		MstCommon.setValue('bnkaccNm', '');
	})

	// 取引先検索ボタン
	.on('click', '#splrCdSettings>.btn-primary', function() {
		const conds = {"COMPANY_CD" : MstCommon.getValue('companyCd')};
		NCI.openMasterSearch('V_SPLR_MST', 'FOR_POPUP_ORD', callbackSearchSplrCd, conds);
	})
	// 取引先のクリアボタン
	.on('click', '#splrCdSettings>.btn-default', function() {
		MstCommon.setValue('splrCd', '');
		MstCommon.setValue('splrNmKj', '');
		MstCommon.setValue('splrNmKn', '');
	})
	;

	callbackSearchUser = function (org, trigger) {
		if (org) {
			MstCommon.setValue('usrCd', org.userCode);
			MstCommon.setValue('usrNm', org.userName);
		}
	};

	callbackSearchBankAcc = function (condition) {
		if (condition) {
			MstCommon.setValue('bnkaccCd', condition["BNKACC_CD"]);
			MstCommon.setValue('bnkaccNm', condition["BNKACC_NM"]);
		}
	};

	callbackSearchSplrCd = function (condition) {
		if (condition) {
			MstCommon.setValue('splrCd', condition["SPLR_CD"]);
			MstCommon.setValue('splrNmKj', condition["SPLR_NM_KJ"]);
			MstCommon.setValue('splrNmKn', condition["SPLR_NM_KN"]);
		}
	};
});

// 画面固有JS
var mg0261 = {

	// 検索キー
	searchParam : {},

	// 初期化処理
	init : function () {
		NCI.init("/mg0261/init", mg0261.searchParam).done(function(res, textStatus, jqXHR) {
			if (res && res.success) {

				// 基本情報設定
				MstCommon.baseRenderInfo = res;

				// フィールド初期化
				MstCommon.createField(mg0261.partsInfo, res.entity, displayMode);

				// ボタン表示
				if (displayMode == '0') {
					$('#btnUpdate').hide();
					$('#btnChangeMode').show();
					$('#btnRegister').hide();
					$('#usrCdSettings').hide();
					$('#bnkaccCdSettings').hide();
					$('#splrCdSettings').hide();
				// 編集モード
				} else if(displayMode == '1') {
					$('#btnUpdate').show();
					$('#btnChangeMode').hide();
					$('#btnRegister').hide();
					$('#companyCd select').prop('disabled', true);
					$('#usrCdSettings').hide();
					$('#bnkaccCdSettings').show();
					$('#splrCdSettings').hide();

				// 登録モード
				} else {
					$('#btnUpdate').hide();
					$('#btnChangeMode').hide();
					$('#btnRegister').show();
					$('#dltFg input:first').prop('checked', true);
					$('#dltFg input').prop('disabled', true);
					$('#companyCd select').prop('disabled', false);
					$('#usrCdSettings').show();
					$('#bnkaccCdSettings').show();
					$('#splrCdSettings').show();
				}
			}
		});
	},

	// パーツ情報
	partsInfo : [
		{'selectorKey':'#companyCd', 'id':'companyCd', 'class':'form-control required', 'data-role':'dropdownCode', 'data-field':'companyCd', 'data-validate':'', 'readonly':false, 'disabled':false, 'listName':'companyItems', 'rows':'','checkLabel':'','style':''},
		{'selectorKey':'#usrCd', 'id':'usrCd', 'class':'form-control required', 'data-role':'text', 'data-field':'usrCd', 'data-validate':'{"maxlength":25, "pattern": "alphaNumber"}', 'readonly':true, 'disabled':false, 'listName':'', 'rows':'','checkLabel':'','style':''},
		{'selectorKey':'#usrNm', 'id':'usrNm', 'class':'form-control', 'data-role':'text', 'data-field':'usrNm', 'data-validate':'', 'readonly':true, 'disabled':false, 'listName':'', 'rows':'','checkLabel':'','style':''},
		{'selectorKey':'#crdCompanyNm', 'id':'crdCompanyNm required', 'class':'form-control required', 'data-role':'text', 'data-field':'crdCompanyNm', 'data-validate':'{"maxlength":20, "pattern": "halfWidthOnly"}', 'readonly':false, 'disabled':false, 'listName':'', 'rows':'','checkLabel':'','style':''},
		{'selectorKey':'#bnkaccCd', 'id':'bnkaccCd', 'class':'form-control required', 'data-role':'text', 'data-field':'bnkaccCd', 'data-validate':'{"maxlength":4, "pattern": "alphaNumber"}', 'readonly':true, 'disabled':false, 'listName':'', 'rows':'','checkLabel':'','style':''},
		{'selectorKey':'#bnkaccNm', 'id':'bnkaccNm', 'class':'form-control', 'data-role':'text', 'data-field':'bnkaccNm', 'data-validate':'', 'readonly':true, 'disabled':false, 'listName':'', 'rows':'','checkLabel':'','style':''},
		{'selectorKey':'#splrCd', 'id':'splrCd', 'class':'form-control required', 'data-role':'text', 'data-field':'splrCd', 'data-validate':'{"maxlength":20}', 'readonly':true, 'disabled':false, 'listName':'', 'rows':'','checkLabel':'','style':''},
		{'selectorKey':'#splrNmKj', 'id':'splrNmKj', 'class':'form-control', 'data-role':'text', 'data-field':'splrNmKj', 'data-validate':'', 'readonly':true, 'disabled':false, 'listName':'', 'rows':'','checkLabel':'','style':''},
		{'selectorKey':'#splrNmKn', 'id':'splrNmKn', 'class':'form-control', 'data-role':'text', 'data-field':'splrNmKn', 'data-validate':'', 'readonly':true, 'disabled':false, 'listName':'', 'rows':'','checkLabel':'','style':''},
		{'selectorKey':'#bnkaccChrgDt', 'id':'bnkaccChrgDt', 'class':'form-control', 'data-role':'dropdownCode', 'data-field':'bnkaccChrgDt', 'data-validate':'{"maxlength":2, "pattern": "numberOnly", "max": 31}', 'readonly':false, 'disabled':false, 'listName':'bnkaccChrgDts', 'rows':'','checkLabel':'','style':''},
		{'selectorKey':'#dltFg', 'id':'dltFg', 'class':'form-control required', 'data-role':'radioCode', 'data-field':'dltFg', 'data-validate':'', 'readonly':false, 'disabled':false, 'listName':'dltFg', 'rows':'','checkLabel':'','style':''}
	],

	// サブミット時の値取得
	getSubmitValue : function () {
		var obj = {
			'companyCd' : MstCommon.getValue('companyCd'),
			'crdCompanyNm' : MstCommon.getValue('crdCompanyNm'),
			'usrCd' : MstCommon.getValue('usrCd'),
			'bnkaccCd' : MstCommon.getValue('bnkaccCd'),
			'splrCd' : MstCommon.getValue('splrCd'),
			'bnkaccChrgDt' : MstCommon.getValue('bnkaccChrgDt'),
			'dltFg' : MstCommon.getValue('dltFg')
		};
		return obj;
	},
};