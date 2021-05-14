var pager = null;
var displayMode = '0';

$(function() {

	// 検索キー
	mg0191.searchParam = {
		"companyCd" : NCI.getQueryString("companyCd"),
		"bumonCd" : NCI.getQueryString("bumonCd")
	};

	if(NCI.getQueryString("insertFlg")){
		// 登録モード
		displayMode = '2';
	}

	// 初期化処理開始
	mg0191.init();

	/** 事業選択ポップアップからのコールバック */
	function callbackFromSelectEntrp(entrp) {
		if (entrp) {

			var code = entrp['LOOKUP_ID'];

			$('#entrpTpCd input').val(code.substr(0,3)).trigger('validate');
			$('#entrpCd input').val(code.substr(3,3));
			$('#entrpNm input').val(entrp['LOOKUP_NAME']);
		}
	}

	/** 地域選択ポップアップからのコールバック */
	function callbackFromSelectArea(area) {
		if (area) {
			$('#areaCd input').val(area['LOOKUP_ID']).trigger('validate');
			$('#areaNm input').val(area['LOOKUP_NAME']);
		}
	}

	/** タブ選択ポップアップからのコールバック */
	function callbackFromSelectTab(tab) {
		if (tab) {
			$('#tabCd input').val(tab['LOOKUP_ID']).trigger('validate');
			$('#tabNm input').val(tab['LOOKUP_NAME']);
		}
	}

	/** 事業選択ポップアップをクリア */
	function clearForEntrp() {
		$('#entrpTpCd input').val('').trigger('validate');
		$('#entrpCd input').val('');
		$('#entrpNm input').val('');
	}

	/** 地域選択ポップアップをクリア */
	function clearForArea() {
		$('#areaCd input').val('').trigger('validate');
		$('#areaNm input').val('');
	}

	/** タブ選択ポップアップをクリア */
	function clearForTab() {
		$('#tabCd input').val('').trigger('validate');
		$('#tabNm input').val('');
	}

	// イベント
	$(document)
	// 編集ボタン
	.on('click', '#btnChangeMode', function() {
		// 編集モード
		displayMode = '1';
		mg0191.searchParam['needMenuHtml'] = false;
		mg0191.searchParam['needFooterHtml'] = false;
		mg0191.init();
	})
	// 戻るボタン
	.on('click', '#btnBack', function() {
		NCI.redirect("./mg0190.html");
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
			var params = mg0191.getSubmitValue();
			params['needMenuHtml'] = false;
			params['needFooterHtml'] = false;

			// 初期化処理開始
			NCI.init("/mg0191/update", params).done(function(res, textStatus, jqXHR) {
				if (res && res.success) {
					// 完了メッセージ
					var msg = '登録内容の更新が完了しました。';
					$('#mg0191InformationContents').html(msg);
					$('#mg0191Information').modal({show: true});
				}
			});
		});
	})
	// 登録ボタン
	.on('click', '#btnRegister', function() {

		//部門コード設定
//		$("#bumonCd input").val($('#entrpTpCd input').val() + $('#entrpCd input').val()
//				+ $('#tabCd input').val() + $('#siteCd select').val()
//				+ $('#tpCd select').val() + $('#areaCd input').val());

		const $targets = $('#editArea').find('input, select, text');
		if (!Validator.validate($targets, true)) {
			return false;
		}

		var paramsForCheck =  {
				"companyCd" : $("#companyCd select").val(),
				"bumonCd" : $("#bumonCd input").val(),
				"needMenuHtml": false,
				"needFooterHtml": false
			};

		// 登録内容をチェックする
		NCI.init("/mg0191/insertCheck", paramsForCheck).done(function(res) {

			// 同一の部門コードがすでに登録されていた場合
			if (res) {
				var msg = '部門コードはすでに登録済みです。';
				$('#checkErrorAlertBody').html(msg);
				$('#checkErrorAlert').modal({show: true});
			} else {
				var messege = '登録します。よろしいですか？';

				NCI.confirm(messege, function() {

					// 更新処理
					var params = mg0191.getSubmitValue();
					params['needMenuHtml'] = false;
					params['needFooterHtml'] = false;

					// 初期化処理開始
					NCI.init("/mg0191/insert", params).done(function(res, textStatus, jqXHR) {
						if (res && res.success) {
							// 完了メッセージ
							var msg = '登録が完了しました。';
							$('#mg0191InformationContents').html(msg);
							$('#mg0191Information').modal({show: true});
							displayMode = '1';
							mg0191.searchParam['companyCd'] = params.companyCd;
							mg0191.searchParam['bumonCd'] = params.bumonCd;
							mg0191.searchParam['needMenuHtml'] = false;
							mg0191.searchParam['needFooterHtml'] = false;
							mg0191.init();
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
	// 事業選択ポップアップ起動
	.on('click', '#btnEntrpNm', function() {

		//入力値チェック
		if($('#companyCd select').val() == ''){
			var msg = '会社コードを選択してください。';
			$('#checkErrorAlertBody').html(msg);
			$('#checkErrorAlert').modal({show: true});
		}else{
			// 事業選択ポップアップ起動
			const conds = {
					'CORPORATION_CODE':$('#companyCd select').val(),
					'LOCALE_CODE':NCI.loginInfo.localeCode,
					'LOOKUP_GROUP_ID':'SS_KINO_CD_1'
					};
			NCI.openMasterSearch('MWM_LOOKUP', 'FOR_POPUP_LOOKUP', callbackFromSelectEntrp, conds);
		}
	})
	// 事業のクリアボタン
	.on('click', '#btnClearEntrpNm', function() {
		clearForEntrp();
	})
	// 地域選択ポップアップ起動
	.on('click', '#btnAreaNm', function() {

		//入力値チェック
		if($('#companyCd select').val() == ''){
			var msg = '会社コードを選択してください。';
			$('#checkErrorAlertBody').html(msg);
			$('#checkErrorAlert').modal({show: true});
		}else{
			// 地域選択ポップアップ起動
			const conds = {
					'CORPORATION_CODE':$('#companyCd select').val(),
					'LOCALE_CODE':NCI.loginInfo.localeCode,
					'LOOKUP_GROUP_ID':'SS_PROJECT_MST'
					};
			NCI.openMasterSearch('MWM_LOOKUP', 'FOR_POPUP_LOOKUP', callbackFromSelectArea, conds);
		}
	})
	// タブのクリアボタン
	.on('click', '#btnClearTabNm', function() {
		clearForTab();
	})
	// タブ選択ポップアップ起動
	.on('click', '#btnTabNm', function() {

		//入力値チェック
		if($('#companyCd select').val() == ''){
			var msg = '会社コードを選択してください。';
			$('#checkErrorAlertBody').html(msg);
			$('#checkErrorAlert').modal({show: true});
		}else{
			// タブ選択ポップアップ起動
			const conds = {
					'CORPORATION_CODE':$('#companyCd select').val(),
					'LOCALE_CODE':NCI.loginInfo.localeCode,
					'LOOKUP_GROUP_ID':'SS_KINO_CD_2'
					};
			NCI.openMasterSearch('MWM_LOOKUP', 'FOR_POPUP_LOOKUP', callbackFromSelectTab, conds);
		}
	})
	// タブのクリアボタン
	.on('click', '#btnClearTabNm', function() {
		clearForTab();
	})
	//エラーアラート用
	.on('click', '#checkErrorAlertButtonArea>.btn-primary', function() {
		$('#checkErrorAlert').modal("hide");
	})
});

// 画面固有JS
var mg0191 = {

	// 検索キー
	searchParam : {},

	// 初期化処理
	init : function () {
		NCI.init("/mg0191/init", mg0191.searchParam).done(function(res, textStatus, jqXHR) {
			if (res && res.success) {

				// 基本情報設定
				MstCommon.baseRenderInfo = res;

				// フィールド初期化
				MstCommon.createField(mg0191.partsInfo, res.entity, displayMode);

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
					$('#bumonCd input').prop('readonly', false);
					$('#btnUpdate').hide();
					$('#btnChangeMode').hide();
					$('#btnRegister').show();
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

	// パーツ情報（部門マスタ）
	partsInfo : [
		{'selectorKey':'#companyCd', 'id':'companyCd', 'class':'form-control required', 'data-role':'dropdownCode', 'data-field':'companyCd', 'data-validate':'', 'readonly':false, 'disabled':false, 'listName':'companyItems', 'rows':'','checkLabel':'','style':''},
		{'selectorKey':'#bumonCd', 'id':'bumonCd', 'class':'form-control required', 'data-role':'text', 'data-field':'bumonCd', 'data-validate':'{"minlength": 14, "maxlength": 14, "pattern": "alphaNumber"}', 'readonly':true, 'disabled':false, 'listName':'', 'rows':'','checkLabel':'','style':''},
		{'selectorKey':'#bumonNm', 'id':'bumonNm', 'class':'form-control required', 'data-role':'text', 'data-field':'bumonNm', 'data-validate':'{"maxlength": 100}', 'readonly':false, 'disabled':false, 'listName':'', 'rows':'','checkLabel':'','style':''},
		{'selectorKey':'#taxKndCd', 'id':'taxKndCd', 'class':'form-control required', 'data-role':'dropdownCode', 'data-field':'taxKndCd', 'data-validate':'{"maxlength":1, "pattern": "integer"}', 'readonly':false, 'disabled':false, 'listName':'taxKndCdItems', 'rows':'','checkLabel':'','style':''},
		{'selectorKey':'#dltFg', 'id':'dltFg', 'class':'form-control required', 'data-role':'radioCode', 'data-field':'dltFg', 'data-validate':'', 'readonly':false, 'disabled':false, 'listName':'dltFgNm', 'rows':'','checkLabel':'','style':''}
	],

	// サブミット時の値取得
	getSubmitValue : function () {
		var obj = {
			'companyCd' : MstCommon.getValue('companyCd'),
			'bumonCd' : MstCommon.getValue('bumonCd'),
			'bumonNm' : MstCommon.getValue('bumonNm'),
			'taxKndCd' : MstCommon.getValue('taxKndCd'),
			'dltFg' : MstCommon.getValue('dltFg')
		};
		return obj;
	},
};