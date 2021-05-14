var pager = null;
var displayMode = '0';

$(function() {

	// 検索キー
	mg0131.searchParam = {
		"companyCd" : NCI.getQueryString("companyCd"),
		"accCd" : NCI.getQueryString("accCd"),
		"sqno" : NCI.getQueryString("sqno")
	};

	if(NCI.getQueryString("insertFlg")){
		// 登録モード
		displayMode = '2';
	}

	// 初期化処理開始
	mg0131.init();

	// イベント
	$(document)
	// 編集ボタン
	.on('click', '#btnChangeMode', function() {
		// 編集モード
		displayMode = '1';
		mg0131.searchParam['needMenuHtml'] = false;
		mg0131.searchParam['needFooterHtml'] = false;
		mg0131.init();
	})
	// 戻るボタン
	.on('click', '#btnBack', function() {
		NCI.redirect("./mg0130.html");
	})
	// 更新ボタン
	.on('click', '#btnUpdate', function() {

		const $targets = $('#editArea').find('input, select, text');
		if (!Validator.validate($targets, true)) {
			return false;
		}

		var paramsForCheck =  {
			"companyCd" : $("#companyCd select").val(),
			"accCd" : $("#accCd input").val(),
			"vdDtS" : $("#vdDtS input").val(),
			"vdDtE" : $("#vdDtE input").val(),
			"sqno" : $("#sqno input").val(),
			"needMenuHtml": false,
			"needFooterHtml": false
		};

		// 登録内容をチェックする
		NCI.init("/mg0131/updateCheck", paramsForCheck).done(function(res) {
			// 同一の銀行コードがすでに登録されていた場合
			if (res) {
				var msg = '勘定科目は有効期間内に含まれるものが既に登録されています。';
				$('#checkErrorAlertBody').html(msg);
				$('#checkErrorAlert').modal({show: true});
			} else {
				var messege = '登録内容を更新します。よろしいですか？';

				NCI.confirm(messege, function() {

					// 更新処理
					var params = mg0131.getSubmitValue();
					params['needMenuHtml'] = false;
					params['needFooterHtml'] = false;

					// 初期化処理開始
					NCI.init("/mg0131/update", params).done(function(res, textStatus, jqXHR) {
						if (res && res.success) {
							// 完了メッセージ
							var msg = '登録内容の更新が完了しました。';
							$('#mg0131InformationContents').html(msg);
							$('#mg0131Information').modal({show: true});
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
			"accCd" : $("#accCd input").val(),
			"vdDtS" : $("#vdDtS input").val(),
			"vdDtE" : $("#vdDtE input").val(),
			"needMenuHtml": false,
			"needFooterHtml": false
		};

		// 登録内容をチェックする
		NCI.init("/mg0131/insertCheck", paramsForCheck).done(function(res) {
			// 同一の銀行コードがすでに登録されていた場合
			if (res) {
				var msg = '勘定科目は有効期間内に含まれるものが既に登録されています。';
				$('#checkErrorAlertBody').html(msg);
				$('#checkErrorAlert').modal({show: true});
			} else {
				var messege = '登録します。よろしいですか？';

				NCI.confirm(messege, function() {

					// 更新処理
					var params = mg0131.getSubmitValue();
					params['needMenuHtml'] = false;
					params['needFooterHtml'] = false;

					// 初期化処理開始
					NCI.init("/mg0131/insert", params).done(function(res, textStatus, jqXHR) {
						if (res && res.success) {
							// 完了メッセージ
							var msg = '登録が完了しました。';
							$('#mg0131InformationContents').html(msg);
							$('#mg0131Information').modal({show: true});
							displayMode = '1';
							mg0131.searchParam['companyCd'] = res.entity.companyCd;
							mg0131.searchParam['accCd'] = res.entity.accCd;
							mg0131.searchParam['sqno'] = res.entity.sqno;
							mg0131.searchParam['needMenuHtml'] = false;
							mg0131.searchParam['needFooterHtml'] = false;
							mg0131.init();
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
	//エラーアラート用
	.on('click', '#checkErrorAlertButtonArea>.btn-primary', function() {
		$('#checkErrorAlert').modal("hide");
	})
});

// 画面固有JS
var mg0131 = {

	// 検索キー
	searchParam : {},

	// 初期化処理
	init : function () {
		NCI.init("/mg0131/init", mg0131.searchParam).done(function(res, textStatus, jqXHR) {
			if (res && res.success) {

				// 基本情報設定
				MstCommon.baseRenderInfo = res;

				// フィールド初期化
				MstCommon.createField(mg0131.partsInfo, res.entity, displayMode);

				//日付フォーマットを修正
				$.each($('input.ymdPicker'), function(index, value) {
					value.value = value.value.slice(0, 10);
				});

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
					$('#accCd input').removeAttr('readonly');
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

	// パーツ情報（銀行口座マスタ）
	partsInfo : [
		{'selectorKey':'#companyCd', 'id':'companyCd', 'class':'form-control required', 'data-role':'dropdownCode', 'data-field':'companyCd', 'data-validate':'', 'readonly':false, 'disabled':false, 'listName':'companyItems', 'rows':'','checkLabel':'','style':''},
		{'selectorKey':'#accCd', 'id':'accCd', 'class':'form-control required', 'data-role':'text', 'data-field':'accCd', 'data-validate':'{"maxlength":4, "pattern": "alphaNumber"}', 'readonly':true, 'disabled':false, 'listName':'', 'rows':'','checkLabel':'','style':''},
		{'selectorKey':'#sqno', 'id':'sqno', 'class':'form-control', 'data-role':'text', 'data-field':'sqno', 'data-validate':'{"maxlength":3, "pattern": "numberOnly"}', 'readonly':true, 'disabled':false, 'listName':'', 'rows':'','checkLabel':'','style':''},
		{'selectorKey':'#accNm', 'id':'accNm', 'class':'form-control required', 'data-role':'text', 'data-field':'accNm', 'data-validate':'{"maxlength":60}', 'readonly':false, 'disabled':false, 'listName':'', 'rows':'','checkLabel':'','style':''},
		{'selectorKey':'#accNmS', 'id':'accNmS', 'class':'form-control', 'data-role':'text', 'data-field':'accNmS', 'data-validate':'{"maxlength":30}', 'readonly':false, 'disabled':false, 'listName':'', 'rows':'','checkLabel':'','style':''},
		{'selectorKey':'#dcTp', 'id':'dcTp', 'class':'form-control required', 'data-role':'radioCode', 'data-field':'dcTp', 'data-validate':'', 'readonly':true, 'disabled':false, 'listName':'dcTpNm', 'rows':'','checkLabel':'','style':''},
		{'selectorKey':'#accBrkdwnTp', 'id':'accBrkdwnTp', 'class':'form-control', 'data-role':'radioCode', 'data-field':'accBrkdwnTp', 'data-validate':'', 'readonly':true, 'disabled':false, 'listName':'accBrkdwnTpNm', 'rows':'','checkLabel':'','style':''},
		{'selectorKey':'#taxCdSs', 'id':'taxCdSs', 'class':'form-control required', 'data-role':'text', 'data-field':'taxCdSs', 'data-validate':'{"maxlength":4, "pattern": "alphaNumber"}', 'readonly':false, 'disabled':false, 'listName':'', 'rows':'','checkLabel':'','style':''},
		{'selectorKey':'#taxIptTp', 'id':'taxIptTp', 'class':'form-control required', 'data-role':'radioCode', 'data-field':'taxIptTp', 'data-validate':'', 'readonly':false, 'disabled':false, 'listName':'taxIptTpNm', 'rows':'','checkLabel':'','style':''},
		{'selectorKey':'#vdDtS', 'id':'vdDtS', 'class':'form-control ymdPicker required', 'data-role':'text', 'data-field':'vdDtS', 'data-validate':'{"pattern":"date"}', 'readonly':false, 'disabled':false, 'listName':'', 'rows':'','checkLabel':'','style':''},
		{'selectorKey':'#vdDtE', 'id':'vdDtE', 'class':'form-control ymdPicker required', 'data-role':'text', 'data-field':'vdDtE', 'data-validate':'{"pattern":"date"}', 'readonly':false, 'disabled':false, 'listName':'', 'rows':'','checkLabel':'','style':''},
		{'selectorKey':'#dltFg', 'id':'dltFg', 'class':'form-control required', 'data-role':'radioCode', 'data-field':'dltFg', 'data-validate':'', 'readonly':false, 'disabled':false, 'listName':'dltFgNm', 'rows':'','checkLabel':'','style':''}
	],

	// サブミット時の値取得
	getSubmitValue : function () {
		var obj = {
			'companyCd' : MstCommon.getValue('companyCd'),
			'accCd' : MstCommon.getValue('accCd'),
			'sqno' : MstCommon.getValue('sqno'),
			'accNm' : MstCommon.getValue('accNm'),
			'accNmS' : MstCommon.getValue('accNmS'),
			'dcTp' : MstCommon.getValue('dcTp'),
			'accBrkdwnTp' : MstCommon.getValue('accBrkdwnTp'),
			'taxCdSs' : MstCommon.getValue('taxCdSs'),
			'taxIptTp' : MstCommon.getValue('taxIptTp'),
			'vdDtS' : MstCommon.getValue('vdDtS'),
			'vdDtE' : MstCommon.getValue('vdDtE'),
			'dltFg' : MstCommon.getValue('dltFg')
		};
		return obj;
	},
};