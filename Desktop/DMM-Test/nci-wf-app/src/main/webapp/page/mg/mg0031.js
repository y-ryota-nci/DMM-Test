var pager = null;
var displayMode = '0';

$(function() {

	// 検索キー
	mg0031.searchParam = {
		"companyCd" : NCI.getQueryString("companyCd"),
		"orgnzCd" : unescape(NCI.getQueryString("orgnzCd")),
		"itmExpsCd1" : NCI.getQueryString("itmExpsCd1"),
		"itmExpsCd2" : NCI.getQueryString("itmExpsCd2")
	};

	if(NCI.getQueryString("insertFlg")){
		// 登録モード
		displayMode = '2';
	}

	// 初期化処理開始
	mg0031.init();

	// イベント
	$(document)
	// 編集ボタン
	.on('click', '#btnChangeMode', function() {
		// 編集モード
		displayMode = '1';
		mg0031.searchParam['needMenuHtml'] = false;
		mg0031.searchParam['needFooterHtml'] = false;
		mg0031.init();
	})
	// 戻るボタン
	.on('click', '#btnBack', function() {
		NCI.redirect("./mg0030.html");
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
			var params = mg0031.getSubmitValue();
			params['needMenuHtml'] = false;
			params['needFooterHtml'] = false;

			// 初期化処理開始
			NCI.init("/mg0031/update", params).done(function(res, textStatus, jqXHR) {
				if (res && res.success) {
					// 完了メッセージ
					var msg = '登録内容の更新が完了しました。';
					$('#mg0031InformationContents').html(msg);
					$('#mg0031Information').modal({show: true});
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
				"orgnzCd" : $("#orgnzCd input").val(),
				"itmExpsCd1" : $("#itmExpsCd1 input").val(),
				"itmExpsCd2" : $("#itmExpsCd2 input").val(),
				"needMenuHtml": false,
				"needFooterHtml": false
			};

		// 登録内容をチェックする
		NCI.init("/mg0031/insertCheck", paramsForCheck).done(function(res) {

			// 同一の銀行コードがすでに登録されていた場合
			if (res) {
				var msg = '費目関連コードはすでに登録済みです。';
				$('#checkErrorAlertBody').html(msg);
				$('#checkErrorAlert').modal({show: true});
			} else {
				var messege = '登録します。よろしいですか？';

				NCI.confirm(messege, function() {

					// 更新処理
					var params = mg0031.getSubmitValue();
					params['needMenuHtml'] = false;
					params['needFooterHtml'] = false;

					// 初期化処理開始
					NCI.init("/mg0031/insert", params).done(function(res, textStatus, jqXHR) {
						if (res && res.success) {
							// 完了メッセージ
							var msg = '登録が完了しました。';
							$('#mg0031InformationContents').html(msg);
							$('#mg0031Information').modal({show: true});
							displayMode = '1';
							mg0031.searchParam['companyCd'] = params.companyCd;
							mg0031.searchParam['orgnzCd'] = params.orgnzCd;
							mg0031.searchParam['itmExpsCd1'] = params.itmExpsCd1;
							mg0031.searchParam['itmExpsCd2'] = params.itmExpsCd2;
							mg0031.searchParam['needMenuHtml'] = false;
							mg0031.searchParam['needFooterHtml'] = false;
							mg0031.init();
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

	// 費目コード（１）検索ボタン
	.on('click', '#itmExpsCd1Settings>.btn-primary', function() {
		const conds = { "COMPANY_CD" : $('#companyCd select').val(), "ITMEXPS_LEVEL":"1" };
		NCI.openMasterSearch('ITMEXPS_MST', 'FOR_ITMEXPS_POPUP_SEARCH', callbackSearchItmExpsCd1, conds);
	})
	// 費目コード（１）のクリアボタン
	.on('click', '#itmExpsCd1Settings>.btn-default', function() {
		MstCommon.setValue('itmExpsCd1', '');
		MstCommon.setValue('itmExpsNm1', '');
	})

	// 費目コード（２）検索ボタン
	.on('click', '#itmExpsCd2Settings>.btn-primary', function() {
		const conds = { "COMPANY_CD" : $('#companyCd select').val(), "ITMEXPS_LEVEL":"2" };
		NCI.openMasterSearch('ITMEXPS_MST', 'FOR_ITMEXPS_POPUP_SEARCH', callbackSearchItmExpsCd2, conds);
	})
	// 費目コード（２）のクリアボタン
	.on('click', '#itmExpsCd2Settings>.btn-default', function() {
		MstCommon.setValue('itmExpsCd2', '');
		MstCommon.setValue('itmExpsNm2', '');
	})

	// 仕訳コード検索ボタン
	.on('click', '#jrnCdSettings>.btn-primary', function() {
		const conds = {};
		NCI.openMasterSearch('JRN_MST', 'FOR_JRN_POPUP_SEARCH', callbackSearchJrnCd, conds);
	})
	// 仕訳コードのクリアボタン
	.on('click', '#jrnCdSettings>.btn-default', function() {
		MstCommon.setValue('jrnCd', '');
		MstCommon.setValue('jrnNm', '');
	})

	// 勘定科目検索ボタン
	.on('click', '#accCdSettings>.btn-primary', function() {
		const conds = {"COMPANY_CD": $('#companyCd select').val()};
		NCI.openMasterSearch('ACC_MST', 'FOR_SEARCH_ACC_MST', callbackSearchAccCd, conds);
	})
	// 勘定科目のクリアボタン
	.on('click', '#accCdSettings>.btn-default', function() {
		MstCommon.setValue('accCd', '');
		MstCommon.setValue('accNm', '');
		MstCommon.setValue('accBrkDwnCd', '');
		MstCommon.setValue('accBrkDwnNm', '');
	})
	// 勘定科目の変更
	.on('change', '#accCd input', function() {
		MstCommon.setValue('accBrkDwnCd', '');
		MstCommon.setValue('accBrkDwnNm', '');
	})

	// 勘定科目補助検索ボタン
	.on('click', '#accBrkDwnCdSettings>.btn-primary', function() {
		if (MstCommon.getValue('accCd') != '') {
			const conds = {"ACC_CD": $('#accCd input').val()};
			NCI.openMasterSearch('ACC_BRKDWN_MST', 'FOR_SEARCH_ACC_BRKDWN_MST', callbackSearchAccBrkDwnCd, conds);
		}
	})
	// 勘定科目補助のクリアボタン
	.on('click', '#accBrkDwnCdSettings>.btn-default', function() {
		MstCommon.setValue('accBrkDwnCd', '');
		MstCommon.setValue('accBrkDwnNm', '');
	})

	// 予算科目検索ボタン
	.on('click', '#bdgtAccCdSettings>.btn-primary', function() {
		const conds = { "COMPANY_CD" : $('#companyCd select').val() };
		NCI.openMasterSearch('BGT_ITM_MST', 'FOR_SEARCH_BGT_ITM_MST', callbackSearchBdgtAccCd, conds);
	})

	// 予算科目のクリアボタン
	.on('click', '#bdgtAccCdSettings>.btn-default', function() {
		MstCommon.setValue('bdgtAccCd', '');
		MstCommon.setValue('bdgtAccNm', '');
	})

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
	//組織区分：
	.on('click', '#asstTp1', function() {
		//##########に設定
		MstCommon.setValue('orgnzCd', '##########');
		$('#orgnzCdSettings').hide();
	})
	.on('click', '#asstTp2', function() {
		MstCommon.setValue('orgnzCd', '');
		$('#orgnzCdSettings').show();
	})

	;

	callbackSearchOrgnz = function (org, trigger) {
		if (org) {
			MstCommon.setValue('orgnzCd', org.organizationCode);
			MstCommon.setValue('orgnzNm', org.organizationName);
		}
	};

	callbackSearchItmExpsCd1 = function (condition) {
		if (condition) {
			MstCommon.setValue('itmExpsCd1', condition["ITMEXPS_CD"]);
			MstCommon.setValue('itmExpsNm1', condition["ITMEXPS_NM"]);
		}
	};

	callbackSearchItmExpsCd2 = function (condition) {
		if (condition) {
			MstCommon.setValue('itmExpsCd2', condition["ITMEXPS_CD"]);
			MstCommon.setValue('itmExpsNm2', condition["ITMEXPS_NM"]);
		}
	};

	callbackSearchJrnCd = function (condition) {
		if (condition) {
			MstCommon.setValue('jrnCd', condition["JRN_CD"]);
			MstCommon.setValue('jrnNm', condition["JRN_NM"]);
		}
	};

	callbackSearchAccCd = function (condition) {
		if (condition) {
			MstCommon.setValue('accCd', condition["ACC_CD"]);
			MstCommon.setValue('accNm', condition["ACC_NM"]);
			$('#accCd input').trigger('change');
		}
	};

	callbackSearchAccBrkDwnCd = function (condition) {
		if (condition) {
			MstCommon.setValue('accBrkDwnCd', condition["ACC_BRKDWN_CD"]);
			MstCommon.setValue('accBrkDwnNm', condition["ACC_BRKDWN_NM"]);
		}
	};

	callbackSearchBdgtAccCd = function (condition) {
		if (condition) {
			MstCommon.setValue('bdgtAccCd', condition["BGT_ITM_CD"]);
			MstCommon.setValue('bdgtAccNm', condition["BGT_ITM_NM"]);
		}
	};

	callbackSearchTaxCd = function (condition) {
		if (condition) {
			MstCommon.setValue('taxCd', condition["TAX_CD"]);
			MstCommon.setValue('taxNm', condition["TAX_NM"]);
		}
	};
});

// 画面固有JS
var mg0031 = {

	// 検索キー
	searchParam : {},

	// 初期化処理
	init : function () {
		NCI.init("/mg0031/init", mg0031.searchParam).done(function(res, textStatus, jqXHR) {
			if (res && res.success) {

				// 基本情報設定
				MstCommon.baseRenderInfo = res;

				// フィールド初期化
				MstCommon.createField(mg0031.partsInfo, res.entity, displayMode);

				// ボタン表示
				// 閲覧モード
				if (displayMode == '0') {
					$('#btnUpdate').hide();
					$('#btnChangeMode').show();
					$('#btnRegister').hide();
					$('#orgnzCdSettings').hide();
					$('#itmExpsCd1Settings').hide();
					$('#itmExpsCd2Settings').hide();
					$('#jrnCdSettings').hide();
					$('#accCdSettings').hide();
					$('#accBrkDwnCdSettings').hide();
					$('#bdgtAccCdSettings').hide();
					$('#taxCdSettings').hide();
				// 編集モード
				} else if(displayMode == '1') {
					$('#btnUpdate').show();
					$('#btnChangeMode').hide();
					$('#btnRegister').hide();
					$('#companyCd select').prop('disabled', true);
					$('#orgnzCdSettings').hide();
					$('#itmExpsCd1Settings').hide();
					$('#itmExpsCd2Settings').hide();
					$('#jrnCdSettings').show();
					$('#accCdSettings').show();
					$('#accBrkDwnCdSettings').show();
					$('#bdgtAccCdSettings').show();
					$('#taxCdSettings').show();
					$('#asstTp1').prop('disabled', true);
					$('#asstTp2').prop('disabled', true);

				// 登録モード
				} else {
					$('#btnUpdate').hide();
					$('#btnChangeMode').hide();
					$('#btnRegister').show();
					$('#dltFg input:first').prop('checked', true);
					$('#dltFg input').prop('disabled', true);
					$('#companyCd select').prop('disabled', false);
					$('#orgnzCdSettings').show();
					$('#itmExpsCd1Settings').show();
					$('#itmExpsCd2Settings').show();
					$('#jrnCdSettings').show();
					$('#accCdSettings').show();
					$('#accBrkDwnCdSettings').show();
					$('#bdgtAccCdSettings').show();
					$('#taxCdSettings').show();
					$('#asstTp input:first').prop('checked', true);
				}
			}

			// カレンダー（年月日）
			//	初期状態だとフォーカスアウト時に現在日付を保存してしまうため位置を修正
			NCI.ymdPicker($('input.ymdPicker'));

		});
	},

	// パーツ情報（費目関連マスタ）
	partsInfo : [
		{'selectorKey':'#companyCd', 'id':'companyCd', 'class':'form-control required', 'data-role':'dropdownCode', 'data-field':'companyCd', 'data-validate':'', 'readonly':false, 'disabled':false, 'listName':'companyItems', 'rows':'','checkLabel':'','style':''},
		{'selectorKey':'#orgnzCd', 'id':'orgnzCd', 'class':'form-control required', 'data-role':'text', 'data-field':'orgnzCd', 'data-validate':'{"maxlength":10}', 'readonly':true, 'disabled':false, 'listName':'', 'rows':'','checkLabel':'','style':''},
		{'selectorKey':'#orgnzNm', 'id':'orgnzNm', 'class':'form-control', 'data-role':'text', 'data-field':'orgnzNm', 'data-validate':'', 'readonly':true, 'disabled':false, 'listName':'', 'rows':'','checkLabel':'','style':''},
		{'selectorKey':'#itmExpsCd1', 'id':'itmExpsCd1', 'class':'form-control required', 'data-role':'text', 'data-field':'itmExpsCd1', 'data-validate':'{"maxlength":8}', 'readonly':true, 'disabled':false, 'listName':'', 'rows':'','checkLabel':'','style':''},
		{'selectorKey':'#itmExpsNm1', 'id':'itmExpsNm1', 'class':'form-control', 'data-role':'text', 'data-field':'itmExpsNm1', 'data-validate':'', 'readonly':true, 'disabled':false, 'listName':'', 'rows':'','checkLabel':'','style':''},
		{'selectorKey':'#itmExpsCd2', 'id':'itmExpsCd2', 'class':'form-control required', 'data-role':'text', 'data-field':'itmExpsCd2', 'data-validate':'{"maxlength":8}', 'readonly':true, 'disabled':false, 'listName':'', 'rows':'','checkLabel':'','style':''},
		{'selectorKey':'#itmExpsNm2', 'id':'itmExpsNm2', 'class':'form-control', 'data-role':'text', 'data-field':'itmExpsNm2', 'data-validate':'', 'readonly':true, 'disabled':false, 'listName':'', 'rows':'','checkLabel':'','style':''},
		{'selectorKey':'#jrnCd', 'id':'jrnCd', 'class':'form-control', 'data-role':'text', 'data-field':'jrnCd', 'data-validate':'{"maxlength":3}', 'readonly':true, 'disabled':false, 'listName':'', 'rows':'','checkLabel':'','style':''},
		{'selectorKey':'#jrnNm', 'id':'jrnNm', 'class':'form-control', 'data-role':'text', 'data-field':'jrnNm', 'data-validate':'', 'readonly':true, 'disabled':false, 'listName':'', 'rows':'','checkLabel':'','style':''},
		{'selectorKey':'#accCd', 'id':'accCd', 'class':'form-control required', 'data-role':'text', 'data-field':'accCd', 'data-validate':'{"maxlength":4}', 'readonly':true, 'disabled':false, 'listName':'', 'rows':'','checkLabel':'','style':''},
		{'selectorKey':'#accNm', 'id':'accNm', 'class':'form-control', 'data-role':'text', 'data-field':'accNm', 'data-validate':'', 'readonly':true, 'disabled':false, 'listName':'', 'rows':'','checkLabel':'','style':''},
		{'selectorKey':'#accBrkDwnCd', 'id':'accBrkDwnCd', 'class':'form-control', 'data-role':'text', 'data-field':'accBrkDwnCd', 'data-validate':'{"maxlength":10}', 'readonly':true, 'disabled':false, 'listName':'', 'rows':'','checkLabel':'','style':''},
		{'selectorKey':'#accBrkDwnNm', 'id':'accBrkDwnNm', 'class':'form-control', 'data-role':'text', 'data-field':'accBrkDwnNm', 'data-validate':'', 'readonly':true, 'disabled':false, 'listName':'', 'rows':'','checkLabel':'','style':''},
		{'selectorKey':'#mngAccCd', 'id':'mngAccCd', 'class':'form-control', 'data-role':'text', 'data-field':'mngAccCd', 'data-validate':'{"maxlength":5, "pattern":"alphaNumberUnderscore"}', 'readonly':false, 'disabled':false, 'listName':'', 'rows':'','checkLabel':'','style':''},
		{'selectorKey':'#mngAccBrkDwnCd', 'id':'mngAccBrkDwnCd', 'class':'form-control', 'data-role':'text', 'data-field':'mngAccBrkDwnCd', 'data-validate':'{"maxlength":10, "pattern":"alphaNumberUnderscore"}', 'readonly':false, 'disabled':false, 'listName':'', 'rows':'','checkLabel':'','style':''},
		{'selectorKey':'#bdgtAccCd', 'id':'bdgtAccCd', 'class':'form-control', 'data-role':'text', 'data-field':'bdgtAccCd', 'data-validate':'{"maxlength":10}', 'readonly':true, 'disabled':false, 'listName':'', 'rows':'','checkLabel':'','style':''},
		{'selectorKey':'#bdgtAccNm', 'id':'bdgtAccNm', 'class':'form-control', 'data-role':'text', 'data-field':'bdgtAccNm', 'data-validate':'', 'readonly':true, 'disabled':false, 'listName':'', 'rows':'','checkLabel':'','style':''},
		{'selectorKey':'#asstTp', 'id':'asstTp', 'class':'form-control required', 'data-role':'radioCode', 'data-field':'asstTp', 'data-validate':'', 'readonly':false, 'disabled':false, 'listName':'asstTps', 'rows':'','checkLabel':'','style':''},
		{'selectorKey':'#taxCd', 'id':'taxCd', 'class':'form-control required', 'data-role':'text', 'data-field':'taxCd', 'data-validate':'{"maxlength":3}', 'readonly':true, 'disabled':false, 'listName':'', 'rows':'','checkLabel':'','style':''},
		{'selectorKey':'#taxNm', 'id':'taxNm', 'class':'form-control', 'data-role':'text', 'data-field':'taxNm', 'data-validate':'', 'readonly':true, 'disabled':false, 'listName':'', 'rows':'','checkLabel':'','style':''},
		{'selectorKey':'#slpGrpGl', 'id':'slpGrpGl', 'class':'form-control', 'data-role':'text', 'data-field':'slpGrpGl', 'data-validate':'{"maxlength":3, "pattern":"numberOnly"}', 'readonly':false, 'disabled':false, 'listName':'', 'rows':'','checkLabel':'','style':''},
		{'selectorKey':'#cstTp', 'id':'cstTp', 'class':'form-control', 'data-role':'radioCode', 'data-field':'cstTp', 'data-validate':'', 'readonly':false, 'disabled':false, 'listName':'cstTps', 'rows':'','checkLabel':'','style':''},
		{'selectorKey':'#taxSbjTp', 'id':'taxSbjTp', 'class':'form-control required', 'data-role':'radioCode', 'data-field':'taxSbjTp', 'data-validate':'', 'readonly':false, 'disabled':false, 'listName':'taxSbjTps', 'rows':'','checkLabel':'','style':''},
		{'selectorKey':'#dltFg', 'id':'dltFg', 'class':'form-control required', 'data-role':'radioCode', 'data-field':'dltFg', 'data-validate':'', 'readonly':false, 'disabled':false, 'listName':'dltFgNm', 'rows':'','checkLabel':'','style':''}
	],

	// サブミット時の値取得
	getSubmitValue : function () {
		var obj = {
			'companyCd' : MstCommon.getValue('companyCd'),
			'orgnzCd' : MstCommon.getValue('orgnzCd'),
			'itmExpsCd1' : MstCommon.getValue('itmExpsCd1'),
			'itmExpsCd2' : MstCommon.getValue('itmExpsCd2'),
			'jrnCd' : MstCommon.getValue('jrnCd'),
			'accCd' : MstCommon.getValue('accCd'),
			'accBrkDwnCd' : MstCommon.getValue('accBrkDwnCd'),
			'mngAccCd' : MstCommon.getValue('mngAccCd'),
			'mngAccBrkDwnCd' : MstCommon.getValue('mngAccBrkDwnCd'),
			'bdgtAccCd' : MstCommon.getValue('bdgtAccCd'),
			'asstTp' : MstCommon.getValue('asstTp'),
			'taxCd' : MstCommon.getValue('taxCd'),
			'slpGrpGl' : MstCommon.getValue('slpGrpGl'),
			'cstTp' : MstCommon.getValue('cstTp'),
			'taxSbjTp' : MstCommon.getValue('taxSbjTp'),
			'dltFg' : MstCommon.getValue('dltFg')
		};
		return obj;
	},
};