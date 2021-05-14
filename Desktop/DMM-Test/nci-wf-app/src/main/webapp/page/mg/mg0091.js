var pager = null;
var displayMode = '0';

$(function() {

	// 検索キー
	mg0091.searchParam = {
		"companyCd" : NCI.getQueryString("companyCd"),
		"bnkaccCd" : NCI.getQueryString("bnkaccCd"),
		"sqno" : NCI.getQueryString("sqno")
	};

	if(NCI.getQueryString("insertFlg")){
		// 登録モード
		displayMode = '2';
	}

	// 初期化処理開始
	mg0091.init();

	/** 銀行支店選択ポップアップからのコールバック */
	function callbackFromSelectBnk(bnk) {
		if (bnk) {
			$('#bnkCd input').val(bnk['BNK_CD']).trigger('validate').trigger('change');
			$('#bnkNm input').val(bnk['BNK_NM']);
		}
	}

	/** 銀行支店選択ポップアップからのコールバック */
	function callbackFromSelectBnkbrc(bnkbrc) {
		if (bnkbrc) {
			$('#bnkbrcCd input').val(bnkbrc['BNKBRC_CD']).trigger('validate');
			$('#bnkbrcNm input').val(bnkbrc['BNKBRC_NM']);
		}
	}

	/** 銀行コード選択ポップアップをクリア */
	function clearForBnk() {
		$('#bnkCd input').val('').trigger('validate');
		$('#bnkNm input').val('');
		$('#bnkbrcCd input').val('');
		$('#bnkbrcNm input').val('');
	}

	/** 銀行支店コード選択ポップアップをクリア */
	function clearForBnkbrc() {
		$('#bnkbrcCd input').val('').trigger('validate');
		$('#bnkbrcNm input').val('');
	}

	// イベント
	$(document)
	// 編集ボタン
	.on('click', '#btnChangeMode', function() {
		// 編集モード
		displayMode = '1';
		mg0091.searchParam['needMenuHtml'] = false;
		mg0091.searchParam['needFooterHtml'] = false;
		mg0091.init();
	})
	// 戻るボタン
	.on('click', '#btnBack', function() {
		NCI.redirect("./mg0090.html");
	})
	// 更新ボタン
	.on('click', '#btnUpdate', function() {

		const $targets = $('#editArea').find('input, select, text');
		if (!Validator.validate($targets, true)) {
			return false;
		}

		var paramsForCheck = {
			companyCd : $('#companyCd select').val(),
			bnkaccCd : $('#bnkaccCd input').val(),
			bnkCd : $('#bnkCd input').val(),
			bnkbrcCd : $('#bnkbrcCd input').val(),
			bnkaccNo : $('#bnkaccNo input').val(),
			vdDtS : $("#vdDtS input").val(),
			vdDtE : $("#vdDtE input").val(),
			sqno : $("#sqno input").val(),
			"needMenuHtml": false,
			"needFooterHtml": false
		};

		// 登録内容をチェックする
		NCI.init("/mg0091/updateCheck", paramsForCheck).done(function(res) {

			// すでに登録されていた場合
			if (res) {
				var msg = '銀行口座は有効期間内に含まれるものが既に登録されています。';
				$('#checkErrorAlertBody').html(msg);
				$('#checkErrorAlert').modal({show: true});
			} else {
				var messege = '登録内容を更新します。よろしいですか？';

				NCI.confirm(messege, function() {

					// 更新処理
					var params = mg0091.getSubmitValue();
					params['needMenuHtml'] = false;
					params['needFooterHtml'] = false;

					// 初期化処理開始
					NCI.init("/mg0091/update", params).done(function(res, textStatus, jqXHR) {
						if (res && res.success) {
							// 完了メッセージ
							var msg = '登録内容の更新が完了しました。';
							$('#mg0091InformationContents').html(msg);
							$('#mg0091Information').modal({show: true});
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
		var paramsForCheck = {
			companyCd : $('#companyCd select').val(),
			bnkaccCd : $('#bnkaccCd input').val(),
			bnkCd : $('#bnkCd input').val(),
			bnkbrcCd : $('#bnkbrcCd input').val(),
			bnkaccNo : $('#bnkaccNo input').val(),
			vdDtS : $("#vdDtS input").val(),
			vdDtE : $("#vdDtE input").val(),
			"needMenuHtml": false,
			"needFooterHtml": false
		};

		// 登録内容をチェックする
		NCI.init("/mg0091/insertCheck", paramsForCheck).done(function(res) {

			// すでに登録されていた場合
			if (res) {
				var msg = '銀行口座は有効期間内に含まれるものが既に登録されています。';
				$('#checkErrorAlertBody').html(msg);
				$('#checkErrorAlert').modal({show: true});
			} else {
				var messege = '登録します。よろしいですか？';

				NCI.confirm(messege, function() {

					// 更新処理
					var params = mg0091.getSubmitValue();
					params['needMenuHtml'] = false;
					params['needFooterHtml'] = false;

					// 初期化処理開始
					NCI.init("/mg0091/insert", params).done(function(res, textStatus, jqXHR) {
						if (res && res.success) {
							// 完了メッセージ
							var msg = '登録が完了しました。';
							$('#mg0091InformationContents').html(msg);
							$('#mg0091Information').modal({show: true});
							displayMode = '1';
							mg0091.searchParam['companyCd'] = res.entity.companyCd;
							mg0091.searchParam['bnkaccCd'] = res.entity.bnkaccCd;
							mg0091.searchParam['sqno'] = res.entity.sqno;
							mg0091.searchParam['needMenuHtml'] = false;
							mg0091.searchParam['needFooterHtml'] = false;
							mg0091.init();
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
	// 銀行の変更
	.on('change', '#bnkCd input', function() {
		clearForBnkbrc();
	})
	// 銀行コードのクリアボタン
	.on('click', '#btnClearBnk', function() {
		clearForBnk();
	})
	// 銀行口座コードの選択ポップアップ起動
	.on('click', '#btnBnkbrcCd', function() {
		//入力値チェック
		if($('#companyCd select').val() == ''){
			var msg = '会社コードを選択してください。';
			$('#checkErrorAlertBody').html(msg);
			$('#checkErrorAlert').modal({show: true});
		}else if($('#bnkCd input').val() == ''){
			var msg = '銀行を選択してください。';
			$('#checkErrorAlertBody').html(msg);
			$('#checkErrorAlert').modal({show: true});
		}else{
			// 銀行支店コードの選択ポップアップ起動
			const conds = {'COMPANY_CD':$('#companyCd select').val(),'BNK_CD':$('#bnkCd input').val()};
			NCI.openMasterSearch('BNKBRC_MST', 'FOR_SEARCH_BNKBRC_MST', callbackFromSelectBnkbrc, conds);
		}
	})
	// 銀行口座コードのクリアボタン
	.on('click', '#btnClearBnkbrc', function() {
		clearForBnkbrc();
	})
	//エラーアラート用
	.on('click', '#checkErrorAlertButtonArea>.btn-primary', function() {
		$('#checkErrorAlert').modal("hide");
	})
});

// 画面固有JS
var mg0091 = {

	// 検索キー
	searchParam : {},

	// 初期化処理
	init : function () {
		NCI.init("/mg0091/init", mg0091.searchParam).done(function(res, textStatus, jqXHR) {
			if (res && res.success) {

				// 基本情報設定
				MstCommon.baseRenderInfo = res;

				// フィールド初期化
				MstCommon.createField(mg0091.partsInfo, res.entity, displayMode);

				//日付フォーマットを修正
				$.each($('input.ymdPicker'), function(index, value) {
					value.value = value.value.slice(0, 10);
				});

				// ボタン表示
				// 閲覧モード
				if (displayMode == '0') {
					$('#btnBnkCdContainer').hide();
					$('#btnBnkbrcCdContainer').hide();
					$('#btnUpdate').hide();
					$('#btnChangeMode').show();
					$('#btnRegister').hide();
				// 編集モード
				} else if(displayMode == '1') {
					$('#btnBnkCdContainer').show();
					$('#btnBnkbrcCdContainer').show();
					$('#btnUpdate').show();
					$('#btnChangeMode').hide();
					$('#btnRegister').hide();
					$('#companyCd select').prop('disabled', true);
				// 登録モード
				} else {
					$('#btnBnkCdContainer').show();
					$('#btnBnkbrcCdContainer').show();
					$('#btnUpdate').hide();
					$('#btnChangeMode').hide();
					$('#btnRegister').show();
					$('#bnkaccCd input').removeAttr('readonly');
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
		{'selectorKey':'#bnkaccCd', 'id':'bnkaccCd', 'class':'form-control required', 'data-role':'text', 'data-field':'bnkaccCd', 'data-validate':'{"maxlength":4, "pattern":"numberOnly"}', 'readonly':true, 'disabled':false, 'listName':'', 'rows':'','checkLabel':'','style':''},
		{'selectorKey':'#sqno', 'id':'sqno', 'class':'form-control', 'data-role':'text', 'data-field':'sqno', 'data-validate':'{"maxlength":3, "pattern":"numberOnly"}', 'readonly':true, 'disabled':false, 'listName':'', 'rows':'','checkLabel':'','style':''},
		{'selectorKey':'#bnkCd', 'id':'bnkCd', 'class':'form-control required', 'data-role':'text', 'data-field':'bnkCd', 'data-validate':'', 'readonly':true, 'disabled':false, 'listName':'', 'rows':'','checkLabel':'','style':''},
		{'selectorKey':'#bnkNm', 'id':'bnkNm', 'class':'form-control', 'data-role':'text', 'data-field':'bnkNm', 'data-validate':'', 'readonly':true, 'disabled':false, 'listName':'', 'rows':'','checkLabel':'','style':''},
		{'selectorKey':'#bnkbrcCd', 'id':'bnkbrcCd', 'class':'form-control required', 'data-role':'text', 'data-field':'bnkbrcCd', 'data-validate':'', 'readonly':true, 'disabled':false, 'listName':'', 'rows':'','checkLabel':'','style':''},
		{'selectorKey':'#bnkbrcNm', 'id':'bnkbrcNm', 'class':'form-control', 'data-role':'text', 'data-field':'bnkbrcNm', 'data-validate':'', 'readonly':true, 'disabled':false, 'listName':'', 'rows':'','checkLabel':'','style':''},
		{'selectorKey':'#bnkaccTp', 'id':'bnkaccTp', 'class':'form-control required', 'data-role':'radioCode', 'data-field':'bnkaccTp', 'data-validate':'', 'readonly':false, 'disabled':false, 'listName':'bnkaccTpNm', 'rows':'','checkLabel':'','style':''},
		{'selectorKey':'#bnkaccNo', 'id':'bnkaccNo', 'class':'form-control required', 'data-role':'text', 'data-field':'bnkaccNo', 'data-validate':'{"maxlength":7, "pattern":"numberOnly"}', 'readonly':false, 'disabled':false, 'listName':'', 'rows':'','checkLabel':'','style':''},
		{'selectorKey':'#bnkaccNm', 'id':'bnkaccNm', 'class':'form-control required', 'data-role':'text', 'data-field':'bnkaccNm', 'data-validate':'{"maxlength":40}', 'readonly':false, 'disabled':false, 'listName':'', 'rows':'','checkLabel':'','style':''},
		{'selectorKey':'#bnkaccNmKn', 'id':'bnkaccNmKn', 'class':'form-control required', 'data-role':'text', 'data-field':'bnkaccNmKn', 'data-validate':'{"maxlength":30, "pattern": "halfWidthOnly"}', 'readonly':false, 'disabled':false, 'listName':'', 'rows':'','checkLabel':'','style':''},
		{'selectorKey':'#vdDtS', 'id':'vdDtS', 'class':'form-control ymdPicker required', 'data-role':'text', 'data-field':'vdDtS', 'data-validate':'{"pattern":"date"}', 'readonly':false, 'disabled':false, 'listName':'', 'rows':'','checkLabel':'','style':''},
		{'selectorKey':'#vdDtE', 'id':'vdDtE', 'class':'form-control ymdPicker required', 'data-role':'text', 'data-field':'vdDtE', 'data-validate':'{"pattern":"date"}', 'readonly':false, 'disabled':false, 'listName':'', 'rows':'','checkLabel':'','style':''},
		{'selectorKey':'#dltFg', 'id':'dltFg', 'class':'form-control required', 'data-role':'radioCode', 'data-field':'dltFg', 'data-validate':'', 'readonly':false, 'disabled':false, 'listName':'dltFgNm', 'rows':'','checkLabel':'','style':''}
	],

	// サブミット時の値取得
	getSubmitValue : function () {
		var obj = {
			'companyCd' : MstCommon.getValue('companyCd'),
			'bnkaccCd' : MstCommon.getValue('bnkaccCd'),
			'sqno' : MstCommon.getValue('sqno'),
			'bnkCd' : MstCommon.getValue('bnkCd'),
			'bnkbrcCd' : MstCommon.getValue('bnkbrcCd'),
			'bnkaccTp' : MstCommon.getValue('bnkaccTp'),
			'bnkaccNo' : MstCommon.getValue('bnkaccNo'),
			'bnkaccNm' : MstCommon.getValue('bnkaccNm'),
			'bnkaccNmKn' : MstCommon.getValue('bnkaccNmKn'),
			'vdDtS' : MstCommon.getValue('vdDtS'),
			'vdDtE' : MstCommon.getValue('vdDtE'),
			'dltFg' : MstCommon.getValue('dltFg')
		};
		return obj;
	},
};