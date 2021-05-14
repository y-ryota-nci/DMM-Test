var pager = null;
var displayMode = '0';

$(function() {

	// 検索キー
	mg0141.searchParam = {
		"companyCd" : NCI.getQueryString("companyCd"),
		"accCd" : NCI.getQueryString("accCd"),
		"accBrkdwnCd" : NCI.getQueryString("accBrkdwnCd"),
		"sqno" : NCI.getQueryString("sqno")
	};

	if(NCI.getQueryString("insertFlg")){
		// 登録モード
		displayMode = '2';
	}

	// 初期化処理開始
	mg0141.init();

	/** 勘定科目選択ポップアップからのコールバック */
	function callbackFromSelectAcc(acc) {
		if (acc) {
			$('#accCd input').val(acc['ACC_CD']).trigger('validate');
			$('#accNm input').val(acc['ACC_NM']);
			$('#vdDtS input').val(acc['VD_DT_S']);
			$('#vdDtE input').val(acc['VD_DT_E']);
		}
	}

	/** 勘定科目選択ポップアップをクリア */
	function clearForAcc() {
		$('#accCd input').val('').trigger('validate');
		$('#accNm input').val('');
	}

	// イベント
	$(document)
	// 編集ボタン
	.on('click', '#btnChangeMode', function() {
		// 編集モード
		displayMode = '1';
		mg0141.searchParam['needMenuHtml'] = false;
		mg0141.searchParam['needFooterHtml'] = false;
		mg0141.init();
	})
	// 戻るボタン
	.on('click', '#btnBack', function() {
		NCI.redirect("./mg0140.html");
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
			"accBrkdwnCd" : $("#accBrkdwnCd input").val(),
			"vdDtS" : $("#vdDtS input").val(),
			"vdDtE" : $("#vdDtE input").val(),
			"sqno" : $("#sqno input").val(),
			"needMenuHtml": false,
			"needFooterHtml": false
		};

		// 更新内容をチェックする
		NCI.init("/mg0141/updateCheck", paramsForCheck).done(function(res) {
			// 同一の銀行コードがすでに登録されていた場合
			if (res) {
				var msg = '勘定科目補助は有効期間内に含まれるものが既に登録されています。<br/>或いは有効期限内に該当勘定科目が登録されていない。';
				$('#checkErrorAlertBody').html(msg);
				$('#checkErrorAlert').modal({show: true});
			} else {
				var messege = '登録内容を更新します。よろしいですか？';

				NCI.confirm(messege, function() {

					// 更新処理
					var params = mg0141.getSubmitValue();
					params['needMenuHtml'] = false;
					params['needFooterHtml'] = false;

					// 初期化処理開始
					NCI.init("/mg0141/update", params).done(function(res, textStatus, jqXHR) {
						if (res && res.success) {
							// 完了メッセージ
							var msg = '登録内容の更新が完了しました。';
							$('#mg0141InformationContents').html(msg);
							$('#mg0141Information').modal({show: true});
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
			"accBrkdwnCd" : $("#accBrkdwnCd input").val(),
			"vdDtS" : $("#vdDtS input").val(),
			"vdDtE" : $("#vdDtE input").val(),
			"needMenuHtml": false,
			"needFooterHtml": false
		};

		// 登録内容をチェックする
		NCI.init("/mg0141/insertCheck", paramsForCheck).done(function(res) {
			// 同一の銀行コードがすでに登録されていた場合
			if (res) {
				var msg = '勘定科目補助は有効期間内に含まれるものが既に登録されています。<br/>或いは有効期限内に該当勘定科目が登録されていない。';
				$('#checkErrorAlertBody').html(msg);
				$('#checkErrorAlert').modal({show: true});
			} else {
				var messege = '登録します。よろしいですか？';

				NCI.confirm(messege, function() {

					// 更新処理
					var params = mg0141.getSubmitValue();
					params['needMenuHtml'] = false;
					params['needFooterHtml'] = false;

					// 初期化処理開始
					NCI.init("/mg0141/insert", params).done(function(res, textStatus, jqXHR) {
						if (res && res.success) {
							// 完了メッセージ
							var msg = '登録が完了しました。';
							$('#mg0141InformationContents').html(msg);
							$('#mg0141Information').modal({show: true});
							displayMode = '1';
							mg0141.searchParam['companyCd'] = res.entity.companyCd;
							mg0141.searchParam['accCd'] = res.entity.accCd;
							mg0141.searchParam['accBrkdwnCd'] = res.entity.accBrkdwnCd;
							mg0141.searchParam['sqno'] = res.entity.sqno;
							mg0141.searchParam['needMenuHtml'] = false;
							mg0141.searchParam['needFooterHtml'] = false;
							mg0141.init();
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
	// 勘定科目の選択ポップアップ起動
	.on('click', '#btnAccCd', function() {

		//入力値チェック
		if($('#companyCd select').val() == ''){
			var msg = '会社コードを選択してください。';
			$('#checkErrorAlertBody').html(msg);
			$('#checkErrorAlert').modal({show: true});
		}else{
			// 勘定科目の選択ポップアップ起動
			const conds = {'COMPANY_CD':$('#companyCd select').val()};
			NCI.openMasterSearch('ACC_MST', 'FOR_SEARCH_ACC_MST', callbackFromSelectAcc, conds);
		}
	})
	// 勘定科目のクリアボタン
	.on('click', '#btnClearAcc', function() {
		clearForBnk();
	})
	//エラーアラート用
	.on('click', '#checkErrorAlertButtonArea>.btn-primary', function() {
		$('#checkErrorAlert').modal("hide");
	})
});

// 画面固有JS
var mg0141 = {

	// 検索キー
	searchParam : {},

	// 初期化処理
	init : function () {
		NCI.init("/mg0141/init", mg0141.searchParam).done(function(res, textStatus, jqXHR) {
			if (res && res.success) {

				// 基本情報設定
				MstCommon.baseRenderInfo = res;

				// フィールド初期化
				MstCommon.createField(mg0141.partsInfo, res.entity, displayMode);

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
					$('#btnAccCdContainer').show();
					$('#btnUpdate').hide();
					$('#btnChangeMode').hide();
					$('#btnRegister').show();
					$('#accBrkdwnCd input').removeAttr('readonly');
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

	// パーツ情報（勘定科目補助マスタ）
	partsInfo : [
		{'selectorKey':'#companyCd', 'id':'companyCd', 'class':'form-control required', 'data-role':'dropdownCode', 'data-field':'companyCd', 'data-validate':'', 'readonly':false, 'disabled':false, 'listName':'companyItems', 'rows':'','checkLabel':'','style':''},
		{'selectorKey':'#accCd', 'id':'accCd', 'class':'form-control required', 'data-role':'text', 'data-field':'accCd', 'data-validate':'{"maxlength":4}', 'readonly':true, 'disabled':false, 'listName':'', 'rows':'','checkLabel':'','style':''},
		{'selectorKey':'#accNm', 'id':'accNm', 'class':'form-control', 'data-role':'text', 'data-field':'accNm', 'data-validate':'', 'readonly':true, 'disabled':false, 'listName':'', 'rows':'','checkLabel':'','style':''},
		{'selectorKey':'#accBrkdwnCd', 'id':'accBrkdwnCd', 'class':'form-control required', 'data-role':'text', 'data-field':'accBrkdwnCd', 'data-validate':'{"maxlength":10, "pattern": "alphaNumber"}', 'readonly':true, 'disabled':false, 'listName':'', 'rows':'','checkLabel':'','style':''},
		{'selectorKey':'#sqno', 'id':'sqno', 'class':'form-control', 'data-role':'text', 'data-field':'sqno', 'data-validate':'', 'readonly':true, 'disabled':false, 'listName':'', 'rows':'','checkLabel':'','style':''},
		{'selectorKey':'#accBrkdwnNm', 'id':'accBrkdwnNm', 'class':'form-control required', 'data-role':'text', 'data-field':'accBrkdwnNm', 'data-validate':'{"maxlength":60}', 'readonly':false, 'disabled':false, 'listName':'', 'rows':'','checkLabel':'','style':''},
		{'selectorKey':'#accBrkdwnNmS', 'id':'accBrkdwnNmS', 'class':'form-control required', 'data-role':'text', 'data-field':'accBrkdwnNmS', 'data-validate':'{"maxlength":30}', 'readonly':false, 'disabled':false, 'listName':'', 'rows':'','checkLabel':'','style':''},
		{'selectorKey':'#vdDtS', 'id':'vdDtS', 'class':'form-control ymdPicker required', 'data-role':'text', 'data-field':'vdDtS', 'data-validate':'{"pattern":"date"}', 'readonly':false, 'disabled':false, 'listName':'', 'rows':'','checkLabel':'','style':''},
		{'selectorKey':'#vdDtE', 'id':'vdDtE', 'class':'form-control ymdPicker required', 'data-role':'text', 'data-field':'vdDtE', 'data-validate':'{"pattern":"date"}', 'readonly':false, 'disabled':false, 'listName':'', 'rows':'','checkLabel':'','style':''},
		{'selectorKey':'#dltFg', 'id':'dltFg', 'class':'form-control required', 'data-role':'radioCode', 'data-field':'dltFg', 'data-validate':'', 'readonly':false, 'disabled':false, 'listName':'dltFgNm', 'rows':'','checkLabel':'','style':''}
	],

	// サブミット時の値取得
	getSubmitValue : function () {
		var obj = {
			'companyCd' : MstCommon.getValue('companyCd'),
			'accCd' : MstCommon.getValue('accCd'),
			'accBrkdwnCd' : MstCommon.getValue('accBrkdwnCd'),
			'sqno' : MstCommon.getValue('sqno'),
			'accBrkdwnNm' : MstCommon.getValue('accBrkdwnNm'),
			'accBrkdwnNmS' : MstCommon.getValue('accBrkdwnNmS'),
			'vdDtS' : MstCommon.getValue('vdDtS'),
			'vdDtE' : MstCommon.getValue('vdDtE'),
			'dltFg' : MstCommon.getValue('dltFg')
		};
		return obj;
	},
};