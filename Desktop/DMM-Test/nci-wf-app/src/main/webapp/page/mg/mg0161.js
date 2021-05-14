var pager = null;
var displayMode = '0';

$(function() {

	// 検索キー
	mg0161.searchParam = {
		"companyCd" : NCI.getQueryString("companyCd"),
		"taxCd" : NCI.getQueryString("taxCd"),
		"sqno" : NCI.getQueryString("sqno")
	};

	if(NCI.getQueryString("insertFlg")){
		// 登録モード
		displayMode = '2';
	}

	// 初期化処理開始
	mg0161.init();

	// イベント
	$(document)
	// 編集ボタン
	.on('click', '#btnChangeMode', function() {
		displayMode = '1';
		mg0161.searchParam['needMenuHtml'] = false;
		mg0161.searchParam['needFooterHtml'] = false;
		mg0161.init();
	})
	// 戻るボタン
	.on('click', '#btnBack', function() {
		NCI.redirect("./mg0160.html");
	})
	// 更新ボタン
	.on('click', '#btnUpdate', function() {

		const $targets = $('#editArea').find('input, select, text');
		if (!Validator.validate($targets, true)) {
			return false;
		}

		var paramsForCheck =  {
			"companyCd" : $("#companyCd select").val(),
			"taxCd" : $("#taxCd input").val(),
			"sqno" : $("#sqno input").val(),
			"vdDtS" : $("#vdDtS input").val(),
			"vdDtE" : $("#vdDtE input").val(),
			"needMenuHtml": false,
			"needFooterHtml": false
		};

		// 登録内容をチェックする
		NCI.init("/mg0161/updateCheck", paramsForCheck).done(function(res) {

			// 同一の銀行コードがすでに登録されていた場合
			if (res) {
				var msg = '消費税コードは有効期間内に含まれるものが既に登録されています。';
				$('#checkErrorAlertBody').html(msg);
				$('#checkErrorAlert').modal({show: true});
			} else {
				var messege = '登録内容を更新します。よろしいですか？';

				NCI.confirm(messege, function() {

					// 更新処理
					var params = mg0161.getSubmitValue();
					params['needMenuHtml'] = false;
					params['needFooterHtml'] = false;

					// 初期化処理開始
					NCI.init("/mg0161/update", params).done(function(res, textStatus, jqXHR) {
						if (res && res.success) {
							// 完了メッセージ
							var msg = '登録内容の更新が完了しました。';
							$('#mg0161InformationContents').html(msg);
							$('#mg0161Information').modal({show: true});
						}
					});
				});
			}
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
			"taxCd" : $("#taxCd input").val(),
			"vdDtS" : $("#vdDtS input").val(),
			"vdDtE" : $("#vdDtE input").val(),
			"needMenuHtml": false,
			"needFooterHtml": false
		};

		// 登録内容をチェックする
		NCI.init("/mg0161/insertCheck", paramsForCheck).done(function(res) {

			// 同一の銀行コードがすでに登録されていた場合
			if (res) {
				var msg = '消費税コードは有効期間内に含まれるものが既に登録されています。';
				$('#checkErrorAlertBody').html(msg);
				$('#checkErrorAlert').modal({show: true});
			} else {
				var messege = '登録します。よろしいですか？';

				NCI.confirm(messege, function() {

					// 更新処理
					var params = mg0161.getSubmitValue();
					params['needMenuHtml'] = false;
					params['needFooterHtml'] = false;

					// 初期化処理開始
					NCI.init("/mg0161/insert", params).done(function(res, textStatus, jqXHR) {
						if (res && res.success) {
							// 完了メッセージ
							var msg = '登録が完了しました。';
							$('#mg0161InformationContents').html(msg);
							$('#mg0161Information').modal({show: true});
							displayMode = '1';
							mg0161.searchParam['companyCd'] = res.entity.companyCd;
							mg0161.searchParam['taxCd'] = res.entity.taxCd;
							mg0161.searchParam['sqno'] = res.entity.sqno;
							mg0161.searchParam['needMenuHtml'] = false;
							mg0161.searchParam['needFooterHtml'] = false;
							mg0161.init();
						}
					});
				});
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
	// 勘定科目検索ボタン
	.on('click', '#accCdSettings>.btn-primary', function() {
		// 選択ポップアップ起動
		const conds = {"COMPANY_CD":MstCommon.getValue('companyCd')};
		NCI.openMasterSearch('ACC_MST', 'FOR_SEARCH_ACC_MST', mg0161.afterAccCdCondition, conds);
	})
	// 勘定科目のクリアボタン
	.on('click', '#accCdSettings>.btn-default', function() {
		MstCommon.setValue('accCd', '');
		MstCommon.setValue('accNm', '');
		MstCommon.setValue('accBrkdwnCd', '');
		MstCommon.setValue('accBrkdwnNm', '');
	})
	// 勘定科目の変更
	.on('change', '#accCd input', function() {
		MstCommon.setValue('accBrkdwnCd', '');
		MstCommon.setValue('accBrkdwnNm', '');
	})
	// 勘定科目補助検索ボタン
	.on('click', '#accBrkdwnCdSettings>.btn-primary', function() {
		//現在、汎用マスタ登録ができない
		if (MstCommon.getValue('accCd') != '') {
			const conds = {"COMPANY_CD":MstCommon.getValue('companyCd'), "ACC_CD": $('#accCd input').val()};
			NCI.openMasterSearch('ACC_BRKDWN_MST', 'FOR_SEARCH_ACC_BRKDWN_MST', mg0161.afterAccBrkdwnCdCondition, conds);
		}
	})
	// 勘定科目補助のクリアボタン
	.on('click', '#accBrkdwnCdSettings>.btn-default', function() {
		MstCommon.setValue('accBrkdwnCd', '');
		MstCommon.setValue('accBrkdwnNm', '');
	})
	//エラーアラート用
	.on('click', '#checkErrorAlertButtonArea>.btn-primary', function() {
		$('#checkErrorAlert').modal("hide");
	});
});

// 画面固有JS
var mg0161 = {

	// 勘定科目選択時のコールバック
	afterAccCdCondition : function (condition) {
		if (!condition) return;
		MstCommon.setValue('accCd', condition['ACC_CD']);
		MstCommon.setValue('accNm', condition['ACC_NM']);
		$('#accCd input').trigger('change');
	},

	// 勘定科目補助選択時のコールバック
	afterAccBrkdwnCdCondition : function (condition) {
		if (!condition) return;
		MstCommon.setValue('accBrkdwnCd', condition['ACC_BRKDWN_CD']);
		MstCommon.setValue('accBrkdwnNm', condition['ACC_BRKDWN_NM']);
	},

	// 検索キー
	searchParam : {},

	// 初期化処理
	init : function () {
		NCI.init("/mg0161/init", mg0161.searchParam).done(function(res, textStatus, jqXHR) {
			if (res && res.success) {

				// 基本情報設定
				MstCommon.baseRenderInfo = res;

				// フィールド初期化
				MstCommon.createField(mg0161.partsInfo, res.entity, displayMode);

				//日付フォーマットを修正
				$.each($('input.ymdPicker'), function(index, value) {
					value.value = value.value.slice(0, 10);
				});

				// ボタン表示
				if (displayMode == '0') {
					$('#btnUpdate').hide();
					$('#btnChangeMode').show();
					$('#btnRegister').hide();
					$('#accCdSettings').hide();
					$('#accBrkdwnCdSettings').hide();

				// 編集モード
				} else if(displayMode == '1') {
					$('#btnUpdate').show();
					$('#btnChangeMode').hide();
					$('#btnRegister').hide();
					$('#accCdSettings').show();
					$('#accBrkdwnCdSettings').show();
					$('#companyCd select').prop('disabled', true);

				// 登録モード
				} else {
					$('#btnUpdate').hide();
					$('#btnChangeMode').hide();
					$('#btnRegister').show();
					$('#taxCd input').removeAttr('readonly');
					$('#dltFg input:first').prop('checked', true);
					$('#dltFg input').prop('disabled', true);
					$('#accCdSettings').show();
					$('#accBrkdwnCdSettings').show();
					$('#companyCd select').prop('disabled', false);
				}
			}

			// カレンダー（年月日）
			//	初期状態だとフォーカスアウト時に現在日付を保存してしまうため位置を修正
			NCI.ymdPicker($('input.ymdPicker'));

		});
	},

	// パーツ情報（消費税マスタ）
	partsInfo : [
		{'selectorKey':'#companyCd', 'id':'companyCd', 'class':'form-control required', 'data-role':'dropdownCode', 'data-field':'companyCd', 'data-validate':'', 'readonly':false, 'disabled':false, 'listName':'companyItems', 'rows':'','checkLabel':'','style':''},
		{'selectorKey':'#taxCd', 'id':'taxCd', 'class':'form-control required', 'data-role':'text', 'data-field':'taxCd', 'data-validate':'{"maxlength":3, "pattern": "alphaNumber"}', 'readonly':true, 'disabled':false, 'listName':'', 'rows':'','checkLabel':'','style':''},
		{'selectorKey':'#sqno', 'id':'sqno', 'class':'form-control', 'data-role':'text', 'data-field':'sqno', 'data-validate':'{"maxlength":3, "pattern": "numberOnly"}', 'readonly':false, 'disabled':false, 'listName':'', 'rows':'','checkLabel':'','style':'display:none;'},
		{'selectorKey':'#taxNm', 'id':'taxNm', 'class':'form-control required', 'data-role':'text', 'data-field':'taxNm', 'data-validate':'{"maxlength":20}', 'readonly':false, 'disabled':false, 'listName':'', 'rows':'','checkLabel':'','style':''},
		{'selectorKey':'#taxNmS', 'id':'taxNmS', 'class':'form-control', 'data-role':'text', 'data-field':'taxNmS', 'data-validate':'{"maxlength":10}', 'readonly':false, 'disabled':false, 'listName':'', 'rows':'','checkLabel':'','style':''},
		{'selectorKey':'#taxRto', 'id':'taxRto', 'class':'form-control required', 'data-role':'text', 'data-field':'taxRto', 'data-validate':'{"maxlength":8,"pattern":"numeric"}', 'readonly':false, 'disabled':false, 'listName':'', 'rows':'','checkLabel':'','style':''},
		{'selectorKey':'#taxTp', 'id':'taxTp', 'class':'form-control required', 'data-role':'radioCode', 'data-field':'taxTp', 'data-validate':'', 'readonly':false, 'disabled':false, 'listName':'taxTps', 'rows':'','checkLabel':'','style':''},
		{'selectorKey':'#taxCdSs', 'id':'taxCdSs', 'class':'form-control', 'data-role':'text', 'data-field':'taxCdSs', 'data-validate':'{"maxlength":4, "pattern": "alphaNumber"}', 'readonly':false, 'disabled':false, 'listName':'', 'rows':'','checkLabel':'','style':''},
		{'selectorKey':'#frcUnt', 'id':'frcUnt', 'class':'form-control', 'data-role':'text', 'data-field':'frcUnt', 'data-validate':'{"maxlength":1, "pattern": "alphaNumber"}', 'readonly':false, 'disabled':false, 'listName':'', 'rows':'','checkLabel':'','style':''},
		{'selectorKey':'#frcTp', 'id':'frcTp', 'class':'', 'data-role':'radioCode', 'data-field':'frcTp', 'data-validate':'', 'readonly':false, 'disabled':false, 'listName':'frcTps', 'rows':'','checkLabel':'','style':''},
		{'selectorKey':'#accCd', 'id':'accCd', 'class':'form-control', 'data-role':'text', 'data-field':'accCd', 'data-validate':'{"maxlength":4}', 'readonly':true, 'disabled':false, 'listName':'', 'rows':'','checkLabel':'','style':''},
		{'selectorKey':'#accNm', 'id':'accNm', 'class':'form-control', 'data-role':'text', 'data-field':'accNm', 'data-validate':'{"maxlength":200}', 'readonly':true, 'disabled':false, 'listName':'', 'rows':'','checkLabel':'','style':''},
		{'selectorKey':'#accBrkdwnCd', 'id':'accBrkdwnCd', 'class':'form-control', 'data-role':'text', 'data-field':'accBrkdwnCd', 'data-validate':'{"maxlength":10}', 'readonly':true, 'disabled':false, 'listName':'', 'rows':'','checkLabel':'','style':''},
		{'selectorKey':'#accBrkdwnNm', 'id':'accBrkdwnNm', 'class':'form-control', 'data-role':'text', 'data-field':'accBrkdwnNm', 'data-validate':'{"maxlength":200}', 'readonly':true, 'disabled':false, 'listName':'', 'rows':'','checkLabel':'','style':''},
		{'selectorKey':'#dcTp', 'id':'dcTp', 'class':'', 'data-role':'radioCode', 'data-field':'dcTp', 'data-validate':'', 'readonly':false, 'disabled':false, 'listName':'dcTps', 'rows':'','checkLabel':'','style':''},
		{'selectorKey':'#vdDtS', 'id':'vdDtS', 'class':'form-control ymdPicker', 'data-role':'text', 'data-field':'vdDtS', 'data-validate':'{"pattern":"date"}', 'readonly':false, 'disabled':false, 'listName':'', 'rows':'','checkLabel':'','style':''},
		{'selectorKey':'#vdDtE', 'id':'vdDtE', 'class':'form-control ymdPicker', 'data-role':'text', 'data-field':'vdDtE', 'data-validate':'{"pattern":"date"}', 'readonly':false, 'disabled':false, 'listName':'', 'rows':'','checkLabel':'','style':''},
		{'selectorKey':'#dltFg', 'id':'dltFg', 'class':'form-control required', 'data-role':'radioCode', 'data-field':'dltFg', 'data-validate':'', 'readonly':false, 'disabled':false, 'listName':'dltFg', 'rows':'','checkLabel':'','style':''}
	],

	// サブミット時の値取得
	getSubmitValue : function () {
		var obj = {
			'companyCd' : MstCommon.getValue('companyCd'),
			'taxCd' : MstCommon.getValue('taxCd'),
			'sqno' : MstCommon.getValue('sqno'),
			'taxNm' : MstCommon.getValue('taxNm'),
			'taxNmS' : MstCommon.getValue('taxNmS'),
			'taxRto' : MstCommon.getValue('taxRto'),
			'taxTp' : MstCommon.getValue('taxTp'),
			'taxCdSs' : MstCommon.getValue('taxCdSs'),
			'frcUnt' : MstCommon.getValue('frcUnt'),
			'frcTp' : MstCommon.getValue('frcTp'),
			'accCd' : MstCommon.getValue('accCd'),
			'accBrkdwnCd' : MstCommon.getValue('accBrkdwnCd'),
			'dcTp' : MstCommon.getValue('dcTp'),
			'vdDtS' : MstCommon.getValue('vdDtS'),
			'vdDtE' : MstCommon.getValue('vdDtE'),
			'dltFg' : MstCommon.getValue('dltFg')
		};
		return obj;
	},
};