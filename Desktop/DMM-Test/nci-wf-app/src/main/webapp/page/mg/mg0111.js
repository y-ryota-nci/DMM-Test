var pager = null;
var displayMode = '0';

$(function() {

	// 検索キー
	mg0111.searchParam = {
		"companyCd" : NCI.getQueryString("companyCd"),
		"bnkCd" : NCI.getQueryString("bnkCd"),
		"bnkbrcCd" : NCI.getQueryString("bnkbrcCd")
	};

	if(NCI.getQueryString("insertFlg")){
		// 登録モード
		displayMode = '2';
	}

	// 初期化処理開始
	mg0111.init();

	/** 銀行選択ポップアップからのコールバック */
	function callbackFromSelectBnk(bnk) {
		if (bnk) {
			$('#bnkCd input').val(bnk['BNK_CD']).trigger('validate');
			$('#bnkNm input').val(bnk['BNK_NM']);
		}
	}

	/** 銀行コード選択ポップアップをクリア */
	function clearForBnk() {
		$('#bnkCd input').val('').trigger('validate');
		$('#bnkNm input').val('');
	}

	// イベント
	$(document)
	// 編集ボタン
	.on('click', '#btnChangeMode', function() {
		displayMode = '1';
		mg0111.searchParam['needMenuHtml'] = false;
		mg0111.searchParam['needFooterHtml'] = false;
		mg0111.init();
	})
	// 戻るボタン
	.on('click', '#btnBack', function() {
		NCI.redirect("./mg0110.html");
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
			var params = mg0111.getSubmitValue();
			params['needMenuHtml'] = false;
			params['needFooterHtml'] = false;

			// 初期化処理開始
			NCI.init("/mg0111/update", params).done(function(res, textStatus, jqXHR) {
				if (res && res.success) {
					// 完了メッセージ
					var msg = '登録内容の更新が完了しました。';
					$('#mg0111InformationContents').html(msg);
					$('#mg0111Information').modal({show: true});
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
				"bnkCd" : $("#bnkCd input").val(),
				"bnkbrcCd" : $("#bnkbrcCd input").val(),
				"needMenuHtml": false,
				"needFooterHtml": false
			};

		// 登録内容をチェックする
		NCI.init("/mg0111/insertCheck", paramsForCheck).done(function(res) {

			// 同一の銀行支店コードがすでに登録されていた場合
			if (res) {
				var msg = '銀行支店コードはすでに登録済みです。';
				$('#checkErrorAlertBody').html(msg);
				$('#checkErrorAlert').modal({show: true});
			} else {
				var messege = '登録します。よろしいですか？';

				NCI.confirm(messege, function() {

					// 更新処理
					var params = mg0111.getSubmitValue();
					params['needMenuHtml'] = false;
					params['needFooterHtml'] = false;

					// 初期化処理開始
					NCI.init("/mg0111/insert", params).done(function(res, textStatus, jqXHR) {
						if (res && res.success) {
							// 完了メッセージ
							var msg = '登録が完了しました。';
							$('#mg0111InformationContents').html(msg);
							$('#mg0111Information').modal({show: true});
							displayMode = '1';
							mg0111.searchParam['companyCd'] = params.companyCd;
							mg0111.searchParam['bnkCd'] = params.bnkCd;
							mg0111.searchParam['bnkbrcCd'] = params.bnkbrcCd;
							mg0111.searchParam['needMenuHtml'] = false;
							mg0111.searchParam['needFooterHtml'] = false;
							mg0111.init();
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
	// 銀行コードの選択ポップアップ起動
	.on('click', '#btnBnkCd', function() {

		//入力値チェック
		if($('#companyCd select').val() == ''){
			var msg = '会社コードを選択してください。';
			$('#checkErrorAlertBody').html(msg);
			$('#checkErrorAlert').modal({show: true});
		}else{
			// 銀行コードの選択ポップアップ起動
			const conds = {'COMPANY_CD':$('#companyCd select').val()};
			NCI.openMasterSearch('BNK_MST', 'FOR_SEARCH_BNK_MST', callbackFromSelectBnk, conds);
		}
	})
	// 銀行コードのクリアボタン
	.on('click', '#btnClearBnk', function() {
		clearForBnk();
	})
	//エラーアラート用
	.on('click', '#checkErrorAlertButtonArea>.btn-primary', function() {
		$('#checkErrorAlert').modal("hide");
	});
});

// 画面固有JS
var mg0111 = {

	// 検索キー
	searchParam : {},

	// 初期化処理
	init : function () {
		NCI.init("/mg0111/init", mg0111.searchParam).done(function(res, textStatus, jqXHR) {
			if (res && res.success) {

				// 基本情報設定
				MstCommon.baseRenderInfo = res;

				// フィールド初期化
				MstCommon.createField(mg0111.partsInfo, res.entity, displayMode);

				//日付フォーマットを修正
				$.each($('input.ymdPicker'), function(index, value) {
					value.value = value.value.slice(0, 10);
				});

				// ボタン表示
				if (displayMode == '0') {
					$('#btnBnkCdContainer').hide();
					$('#btnUpdate').hide();
					$('#btnChangeMode').show();
					$('#btnRegister').hide();
				// 編集モード
				} else if(displayMode == '1') {
					$('#btnBnkCdContainer').hide();
					$('#btnUpdate').show();
					$('#btnChangeMode').hide();
					$('#btnRegister').hide();
					$('#companyCd select').prop('disabled', true);

				// 登録モード
				} else {
					$('#btnBnkCdContainer').show();
					$('#btnUpdate').hide();
					$('#btnChangeMode').hide();
					$('#btnRegister').show();
					$('#bnkbrcCd input').removeAttr('readonly');
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

	// パーツ情報（銀行支店マスタ）
	partsInfo : [
		{'selectorKey':'#companyCd', 'id':'companyCd', 'class':'form-control required', 'data-role':'dropdownCode', 'data-field':'companyCd', 'data-validate':'', 'readonly':false, 'disabled':false, 'listName':'companyItems', 'rows':'','checkLabel':'','style':''},
		{'selectorKey':'#bnkCd', 'id':'bnkCd', 'class':'form-control required', 'data-role':'text', 'data-field':'bnkCd', 'data-validate':'', 'readonly':true, 'disabled':false, 'listName':'', 'rows':'','checkLabel':'','style':''},
		{'selectorKey':'#bnkNm', 'id':'bnkNm', 'class':'form-control', 'data-role':'text', 'data-field':'bnkNm', 'data-validate':'', 'readonly':true, 'disabled':false, 'listName':'', 'rows':'','checkLabel':'','style':''},
		{'selectorKey':'#bnkbrcCd', 'id':'bnkbrcCd', 'class':'form-control required', 'data-role':'text', 'data-field':'bnkbrcCd', 'data-validate':'{"maxlength":3, "pattern": "numberOnly"}', 'readonly':true, 'disabled':false, 'listName':'', 'rows':'','checkLabel':'','style':''},
		{'selectorKey':'#bnkbrcNm', 'id':'bnkbrcNm', 'class':'form-control required', 'data-role':'text', 'data-field':'bnkbrcNm', 'data-validate':'{"maxlength":40}', 'readonly':false, 'disabled':false, 'listName':'', 'rows':'','checkLabel':'','style':''},
		{'selectorKey':'#bnkbrcNmS', 'id':'bnkbrcNmS', 'class':'form-control required', 'data-role':'text', 'data-field':'bnkbrcNmS', 'data-validate':'{"maxlength":20}', 'readonly':false, 'disabled':false, 'listName':'', 'rows':'','checkLabel':'','style':''},
		{'selectorKey':'#bnkbrcNmKn', 'id':'bnkbrcNmKn', 'class':'form-control required', 'data-role':'text', 'data-field':'bnkbrcNmKn', 'data-validate':'{"maxlength":20,"pattern":"halfWidthOnly"}', 'readonly':false, 'disabled':false, 'listName':'', 'rows':'','checkLabel':'','style':''},
		{'selectorKey':'#vdDtS', 'id':'vdDtS', 'class':'form-control ymdPicker required', 'data-role':'text', 'data-field':'vdDtS', 'data-validate':'{"pattern":"date"}', 'readonly':false, 'disabled':false, 'listName':'', 'rows':'','checkLabel':'','style':''},
		{'selectorKey':'#vdDtE', 'id':'vdDtE', 'class':'form-control ymdPicker required', 'data-role':'text', 'data-field':'vdDtE', 'data-validate':'{"pattern":"date"}', 'readonly':false, 'disabled':false, 'listName':'', 'rows':'','checkLabel':'','style':''},
		{'selectorKey':'#dltFg', 'id':'dltFg', 'class':'form-control required', 'data-role':'radioCode', 'data-field':'dltFg', 'data-validate':'', 'readonly':false, 'disabled':false, 'listName':'dltFg', 'rows':'','checkLabel':'','style':''}
	],

	// サブミット時の値取得
	getSubmitValue : function () {
		var obj = {
			'companyCd' : MstCommon.getValue('companyCd'),
			'bnkCd' : MstCommon.getValue('bnkCd'),
			'bnkbrcCd' : MstCommon.getValue('bnkbrcCd'),
			'bnkbrcNm' : MstCommon.getValue('bnkbrcNm'),
			'bnkbrcNmS' : MstCommon.getValue('bnkbrcNmS'),
			'bnkbrcNmKn' : MstCommon.getValue('bnkbrcNmKn'),
			'vdDtS' : MstCommon.getValue('vdDtS'),
			'vdDtE' : MstCommon.getValue('vdDtE'),
			'dltFg' : MstCommon.getValue('dltFg')
		};
		return obj;
	},
};