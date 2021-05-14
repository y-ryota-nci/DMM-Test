var pager = null;
var displayMode = '0';

$(function() {

	// 検索キー
	mg0211.searchParam = {
		"companyCd" : NCI.getQueryString("companyCd"),
		"hldtaxTp" : NCI.getQueryString("hldtaxTp")
	};

	if(NCI.getQueryString("insertFlg")){
		// 登録モード
		displayMode = '2';
	}

	// 初期化処理開始
	mg0211.init();

	/** 会社選択ポップアップからのコールバック */
	function callbackFromSelectCompany(company) {
		if (company) {
			$('#companyCd input').val(company['CORPORATION_CODE']).trigger('validate');
			$('#companyAddedInfo input').val(company['CORPORATION_ADDED_INFO']);
		}
	}
/*
	function callbackFromSelectCompany(entity, trigger) {
		if (entity) {
			$('#companyCd input').val(entity.corporationCode).trigger('validate');
			$('#companyNm input').val(entity.corporationName);
		}
	}
*/
	/** 会社コード選択ポップアップをクリア */
	function clearForCompany() {
		$('#companyCd input').val('').trigger('validate');
		$('#companyAddedInfo input').val('');
	}

	// イベント
	$(document)
	// 編集ボタン
	.on('click', '#btnChangeMode', function() {
		displayMode = '1';
		mg0211.searchParam['needMenuHtml'] = false;
		mg0211.searchParam['needFooterHtml'] = false;
		mg0211.init();
	})
	// 戻るボタン
	.on('click', '#btnBack', function() {
		NCI.redirect("./mg0210.html");
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
			var inputs = mg0211.getSubmitValue();
			var params = {
					"inputs"	: inputs
				};
			params['needMenuHtml'] = false;
			params['needFooterHtml'] = false;

			// 初期化処理開始
			NCI.init("/mg0211/update", params).done(function(res, textStatus, jqXHR) {
				if (res && res.success) {
					// 完了メッセージ
					var msg = '登録内容の更新が完了しました。';
					$('#mg0211InformationContents').html(msg);
					$('#mg0211Information').modal({show: true});
					mg0211.searchParam['companyCd'] = res.entity.companyCd;
					mg0211.searchParam['hldtaxTp'] = res.entity.hldtaxTp;
					mg0211.init();
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
				"companyCd" : $("#companyCd input").val(),
				"hldtaxTp" : $("#hldtaxTp input").val(),
				"needMenuHtml": false,
				"needFooterHtml": false
			};

		// 登録内容をチェックする
		NCI.init("/mg0211/insertCheck", paramsForCheck).done(function(res) {

			// 同一の源泉税区分がすでに登録されていた場合
			if (res) {
				var msg = '源泉税区分はすでに登録済みです。';
				$('#checkErrorAlertBody').html(msg);
				$('#checkErrorAlert').modal({show: true});
			} else {
				var messege = '登録します。よろしいですか？';

				NCI.confirm(messege, function() {

					/*
					 * 登録処理
					 */
					//入力値を取得
					var inputs = mg0211.getSubmitValue();
					var params = {
							"inputs"	: inputs
						};
					params['needMenuHtml'] = false;
					params['needFooterHtml'] = false;

					//登録処理開始
					NCI.init("/mg0211/insert", params).done(function(res, textStatus, jqXHR) {
						if (res && res.success) {
							// 完了メッセージ
							var msg = '登録が完了しました。';
							$('#mg0211InformationContents').html(msg);
							$('#mg0211Information').modal({show: true});
							displayMode = '1';
							mg0211.searchParam['companyCd'] = res.entity.companyCd;
							mg0211.searchParam['hldtaxTp'] = res.entity.hldtaxTp;
							mg0211.searchParam['needMenuHtml'] = false;
							mg0211.searchParam['needFooterHtml'] = false;
							mg0211.init();
						}
					});
				})
			}
		});
	})
	// 勘定科目検索ボタン
	.on('click', '#accCdSettings>.btn-primary', function() {
		// 選択ポップアップ起動
		if (MstCommon.getValue('companyCd') && MstCommon.getValue('companyCd') != '') {
			const conds = {"COMPANY_CD":MstCommon.getValue('companyCd')};
			NCI.openMasterSearch('ACC_MST', 'FOR_SEARCH_ACC_MST', mg0211.afterAccCdCondition, conds);
		}
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
			NCI.openMasterSearch('ACC_BRKDWN_MST', 'FOR_SEARCH_ACC_BRKDWN_MST', mg0211.afterAccBrkdwnCdCondition, conds);
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
var mg0211 = {

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
		NCI.init("/mg0211/init", mg0211.searchParam).done(function(res, textStatus, jqXHR) {
			if (res && res.success) {

				// 基本情報設定
				MstCommon.baseRenderInfo = res;

				// フィールド初期化
				MstCommon.createField(mg0211.partsInfo, res.entity, displayMode);

				// ボタン表示
				if (displayMode == '0') {
					$('#btnUpdate').hide();
					$('#btnChangeMode').show();
					$('#btnRegister').hide();
					$('#accCdSettings').hide();
					$('#accBrkdwnCdSettings').hide();
				// 編集モード
				} else if(displayMode == '1') {
					$('#dltFg input').prop('disabled', !($('#sqno input').val() == $('#lstSqno input').val()));
					$('#btnUpdate').show();
					$('#btnChangeMode').hide();
					$('#btnRegister').hide();
					$('#companyCd select').prop('disabled', true);
					$('#accCdSettings').show();
					$('#accBrkdwnCdSettings').show();
				// 登録モード
				} else {
					$('#btnCompanyCdContainer').show();
					$('#btnMnyCdContainer').show();
					$('#btnUpdate').hide();
					$('#btnChangeMode').hide();
					$('#btnRegister').show();
					$('#hldtaxTp input').removeAttr('readonly');
					$('#dltFg input:first').prop('checked', true);
					$('#dltFg input').prop('disabled', true);
					$('#companyCd select').prop('disabled', false);
					$('#accCdSettings').show();
					$('#accBrkdwnCdSettings').show();
				}
			}

			// カレンダー（年月日）
			//	初期状態だとフォーカスアウト時に現在日付を保存してしまうため位置を修正
			NCI.ymdPicker($('input.ymdPicker'));

		});
	},

	// パーツ情報（社内レートマスタ）
	partsInfo : [
		{'selectorKey':'#companyCd',        'id':'companyCd',        'class':'form-control required',            'data-role':'dropdownCode', 'data-field':'companyCd',        'data-validate':'',                                    'readonly':false, 'disabled':false, 'listName':'companyItems', 'rows':'','checkLabel':'','style':''},
		{'selectorKey':'#hldtaxTp',         'id':'hldtaxTp',         'class':'form-control required',            'data-role':'text',         'data-field':'hldtaxTp',         'data-validate':'{"maxlength":3, "pattern":"alphaNumber"}', 'readonly':true,  'disabled':false, 'listName':'',             'rows':'','checkLabel':'','style':''},
		{'selectorKey':'#hldtaxNm',         'id':'hldtaxNm',         'class':'form-control required',            'data-role':'text',         'data-field':'hldtaxNm',         'data-validate':'{"maxlength":100}',                   'readonly':false, 'disabled':false, 'listName':'',             'rows':'','checkLabel':'','style':''},
		{'selectorKey':'#hldtaxRto1',       'id':'hldtaxRto1',       'class':'form-control text-right required', 'data-role':'text',         'data-field':'hldtaxRto1',       'data-validate':'{"maxlength":5, "pattern":"numeric", "min":0, "max":99.99}', 'readonly':false, 'disabled':false, 'listName':'',             'rows':'','checkLabel':'','style':''},
		{'selectorKey':'#hldtaxRto2',       'id':'hldtaxRto2',       'class':'form-control text-right required', 'data-role':'text',         'data-field':'hldtaxRto2',       'data-validate':'{"maxlength":5, "pattern":"numeric", "min":0, "max":99.99}', 'readonly':false, 'disabled':false, 'listName':'',             'rows':'','checkLabel':'','style':''},
		{'selectorKey':'#accCd',            'id':'accCd',            'class':'form-control required',            'data-role':'text',         'data-field':'accCd',            'data-validate':'{"maxlength":4}', 'readonly':true, 'disabled':false, 'listName':'', 'rows':'','checkLabel':'','style':''},
		{'selectorKey':'#accNm',            'id':'accNm',            'class':'form-control',                     'data-role':'text',         'data-field':'accNm',            'data-validate':'{"maxlength":200}', 'readonly':true, 'disabled':false, 'listName':'', 'rows':'','checkLabel':'','style':''},
		{'selectorKey':'#accBrkdwnCd',      'id':'accBrkdwnCd',      'class':'form-control required',            'data-role':'text',         'data-field':'accBrkdwnCd',      'data-validate':'{"maxlength":10}', 'readonly':true, 'disabled':false, 'listName':'', 'rows':'','checkLabel':'','style':''},
		{'selectorKey':'#accBrkdwnNm',      'id':'accBrkdwnNm',      'class':'form-control',                     'data-role':'text',         'data-field':'accBrkdwnNm',      'data-validate':'{"maxlength":200}', 'readonly':true, 'disabled':false, 'listName':'', 'rows':'','checkLabel':'','style':''},
		{'selectorKey':'#vdDtS',            'id':'vdDtS',            'class':'form-control ymdPicker required',  'data-role':'text',         'data-field':'vdDtS',            'data-validate':'{"pattern":"date"}',                  'readonly':false, 'disabled':false, 'listName':'',             'rows':'','checkLabel':'','style':''},
		{'selectorKey':'#vdDtE',            'id':'vdDtE',            'class':'form-control ymdPicker required',  'data-role':'text',         'data-field':'vdDtE',            'data-validate':'{"pattern":"date"}',                  'readonly':false, 'disabled':false, 'listName':'',             'rows':'','checkLabel':'','style':''},
		{'selectorKey':'#dltFg',            'id':'dltFg',            'class':'form-control required',            'data-role':'radioCode',    'data-field':'dltFg',            'data-validate':'',                                    'readonly':false, 'disabled':false, 'listName':'dltFg',        'rows':'','checkLabel':'','style':''},
		{'selectorKey':'#timestampUpdated', 'id':'timestampUpdated', 'class':'form-control hide',                'data-role':'text',         'data-field':'timestampUpdated', 'data-validate':'',                                    'readonly':true,  'disabled':false, 'listName':'',             'rows':'','checkLabel':'','style':''},
		{'selectorKey':'#sortOrder',        'id':'sortOrder',        'class':'form-control text-right', 'data-role':'text',         'data-field':'sortOrder',        'data-validate':'{"maxlength":5, "pattern":"integer", "min":0}', 'readonly':false, 'disabled':false, 'listName':'',   'rows':'','checkLabel':'','style':''}
	],

	// サブミット時の値取得
	getSubmitValue : function () {
		var obj = {
			'companyCd'        : MstCommon.getValue('companyCd'),
			'hldtaxTp'         : MstCommon.getValue('hldtaxTp'),
			'hldtaxNm'         : MstCommon.getValue('hldtaxNm'),
			'hldtaxRto1'       : MstCommon.getValue('hldtaxRto1'),
			'hldtaxRto2'       : MstCommon.getValue('hldtaxRto2'),
			'accCd'            : MstCommon.getValue('accCd'),
			'accBrkdwnCd'      : MstCommon.getValue('accBrkdwnCd'),
			'vdDtS'            : MstCommon.getValue('vdDtS'),
			'vdDtE'            : MstCommon.getValue('vdDtE'),
			'dltFg'            : MstCommon.getValue('dltFg'),
			'timestampUpdated' : MstCommon.getValue('timestampUpdated'),
			'sortOrder' : MstCommon.getValue('sortOrder')
		};
		return obj;
	},
};