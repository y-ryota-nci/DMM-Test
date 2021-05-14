var pager = null;
var displayMode = '0';

$(function() {

	// 検索キー
	mg0181.searchParam = {
		"companyCd" : NCI.getQueryString("companyCd"),
		"mnyCd" : NCI.getQueryString("mnyCd"),
		"sqno" : NCI.getQueryString("sqno")
	};

	if(NCI.getQueryString("insertFlg")){
		// 登録モード
		displayMode = '2';
	}

	// 初期化処理開始
	mg0181.init();

	// イベント
	$(document)
	// 編集ボタン
	.on('click', '#btnChangeMode', function() {
		displayMode = '1';
		mg0181.searchParam['needMenuHtml'] = false;
		mg0181.searchParam['needFooterHtml'] = false;
		mg0181.init();
	})
	// 戻るボタン
	.on('click', '#btnBack', function() {
		NCI.redirect("./mg0180.html");
	})
	// 更新ボタン
	.on('click', '#btnUpdate', function() {

		const $targets = $('#editArea').find('input, select, text');
		if (!Validator.validate($targets, true)) {
			return false;
		}

		var paramsForCheck =  {
			"companyCd" : $("#companyCd select").val(),
			"mnyCd" : $("#mnyCd select").val(),
			"vdDtS" : $("#vdDtS input").val(),
			"vdDtE" : $("#vdDtE input").val(),
			"sqno" : $("#sqno input").val(),
			"needMenuHtml": false,
			"needFooterHtml": false
		};

		// 登録内容をチェックする
		NCI.init("/mg0181/updateCheck", paramsForCheck).done(function(res) {

			// 同一の銀行コードがすでに登録されていた場合
			if (res) {
				var msg = '社内レートは有効期間内に含まれるものが既に登録されています。';
				$('#checkErrorAlertBody').html(msg);
				$('#checkErrorAlert').modal({show: true});
			} else {
				var messege = '登録内容を更新します。よろしいですか？';

				NCI.confirm(messege, function() {

					// 更新処理
					var inputs = mg0181.getSubmitValue();
					var params = {
							"inputs"	: inputs
						};
					params['needMenuHtml'] = false;
					params['needFooterHtml'] = false;

					// 初期化処理開始
					NCI.init("/mg0181/update", params).done(function(res, textStatus, jqXHR) {
						if (res && res.success) {
							// 完了メッセージ
							var msg = '登録内容の更新が完了しました。';
							$('#mg0181InformationContents').html(msg);
							$('#mg0181Information').modal({show: true});
							mg0181.searchParam['sqno'] = res.entity.sqno;
							mg0181.searchParam['needMenuHtml'] = false;
							mg0181.searchParam['needFooterHtml'] = false;
							mg0181.init();
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
			"mnyCd" : $("#mnyCd select").val(),
			"vdDtS" : $("#vdDtS input").val(),
			"vdDtE" : $("#vdDtE input").val(),
			"needMenuHtml": false,
			"needFooterHtml": false
		};

		// 登録内容をチェックする
		NCI.init("/mg0181/insertCheck", paramsForCheck).done(function(res) {

			// 同一の銀行コードがすでに登録されていた場合
			if (res) {
				var msg = '社内レートは有効期間内に含まれるものが既に登録されています。';
				$('#checkErrorAlertBody').html(msg);
				$('#checkErrorAlert').modal({show: true});
			} else {
				var messege = '登録します。よろしいですか？';

				NCI.confirm(messege, function() {

					/*
					 * 登録処理
					 */
					//入力値を取得
					var inputs = mg0181.getSubmitValue();
					var params = {
						"inputs"	: inputs
					};
					params['needMenuHtml'] = false;
					params['needFooterHtml'] = false;

					//登録処理開始
					NCI.init("/mg0181/insert", params).done(function(res, textStatus, jqXHR) {
						if (res && res.success) {
							// 完了メッセージ
							var msg = '登録が完了しました。';
							$('#mg0181InformationContents').html(msg);
							$('#mg0181Information').modal({show: true});
							displayMode = '1';
							mg0181.searchParam['companyCd'] = res.entity.companyCd;
							mg0181.searchParam['mnyCd'] = res.entity.mnyCd;
							mg0181.searchParam['sqno'] = res.entity.sqno;
							mg0181.searchParam['needMenuHtml'] = false;
							mg0181.searchParam['needFooterHtml'] = false;
							mg0181.init();
						}
					});
				});
			}
		});
	})
	//エラーアラート用
	.on('click', '#checkErrorAlertButtonArea>.btn-primary', function() {
		$('#checkErrorAlert').modal("hide");
	});
});

// 画面固有JS
var mg0181 = {

	// 検索キー
	searchParam : {},

	// 初期化処理
	init : function () {
		NCI.init("/mg0181/init", mg0181.searchParam).done(function(res, textStatus, jqXHR) {
			if (res && res.success) {

				// 基本情報設定
				MstCommon.baseRenderInfo = res;

				// フィールド初期化
				MstCommon.createField(mg0181.partsInfo, res.entity, displayMode);

				// ボタン表示
				if (displayMode == '0') {
					$('#btnUpdate').hide();
					$('#btnChangeMode').show();
					$('#btnRegister').hide();
				// 編集モード
				} else if(displayMode == '1') {
					$('#dltFg input').prop('disabled', !($('#sqno input').val() == $('#lstSqno input').val()));
					$('#btnUpdate').show();
					$('#btnChangeMode').hide();
					$('#btnRegister').hide();
					$('#companyCd select').prop('disabled', true);
				// 登録モード
				} else {
					$('#btnCompanyCdContainer').show();
					$('#btnMnyCdContainer').show();
					$('#btnUpdate').hide();
					$('#btnChangeMode').hide();
					$('#btnRegister').show();
					$('#sqno input').val('1');
					$('#rtoTp input').val('10');
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

	// パーツ情報（社内レートマスタ）
	partsInfo : [
		{'selectorKey':'#companyCd',        'id':'companyCd',        'class':'form-control required',            'data-role':'dropdownCode', 'data-field':'companyCd',        'data-validate':'',                                    'readonly':false, 'disabled':false, 'listName':'companyItems', 'rows':'','checkLabel':'','style':''},
		{'selectorKey':'#mnyCd',            'id':'mnyCd',            'class':'form-control required',            'data-role':'dropdownCode', 'data-field':'mnyCd',            'data-validate':'',                                    'readonly':true,  'disabled':false, 'listName':'mnyCds',       'rows':'','checkLabel':'','style':''},
		{'selectorKey':'#mnyNm',            'id':'mnyNm',            'class':'form-control required',            'data-role':'text',         'data-field':'mnyNm',            'data-validate':'',                                    'readonly':true,  'disabled':false, 'listName':'',             'rows':'','checkLabel':'','style':''},
		{'selectorKey':'#sqno',             'id':'sqno',             'class':'form-control text-right required', 'data-role':'text',         'data-field':'sqno',             'data-validate':'{"maxlength":3}',                     'readonly':true,  'disabled':false, 'listName':'',             'rows':'','checkLabel':'','style':''},
		{'selectorKey':'#inRto',            'id':'inRto',            'class':'form-control text-right required', 'data-role':'text',         'data-field':'inRto',            'data-validate':'{"maxlength":9, "pattern":"numeric", "min": 0, "max" : 9999.9999}', 'readonly':false, 'disabled':false, 'listName':'',             'rows':'','checkLabel':'','style':''},
		{'selectorKey':'#rtoTp',            'id':'rtoTp',            'class':'form-control required',            'data-role':'text',         'data-field':'rtoTp',            'data-validate':'{"maxlength":2}',                     'readonly':false, 'disabled':false, 'listName':'',             'rows':'','checkLabel':'','style':''},
		{'selectorKey':'#vdDtS',            'id':'vdDtS',            'class':'form-control ymdPicker required',  'data-role':'text',         'data-field':'vdDtS',            'data-validate':'{"pattern":"date"}',                  'readonly':false, 'disabled':false, 'listName':'',             'rows':'','checkLabel':'','style':''},
		{'selectorKey':'#vdDtE',            'id':'vdDtE',            'class':'form-control ymdPicker required',  'data-role':'text',         'data-field':'vdDtE',            'data-validate':'{"pattern":"date"}',                  'readonly':false, 'disabled':false, 'listName':'',             'rows':'','checkLabel':'','style':''},
		{'selectorKey':'#dltFg',            'id':'dltFg',            'class':'form-control required',            'data-role':'radioCode',    'data-field':'dltFg',            'data-validate':'',                                    'readonly':false, 'disabled':false, 'listName':'dltFg',        'rows':'','checkLabel':'','style':''},
		{'selectorKey':'#timestampUpdated', 'id':'timestampUpdated', 'class':'form-control hide',                'data-role':'text',         'data-field':'timestampUpdated', 'data-validate':'',                                    'readonly':true,  'disabled':false, 'listName':'',             'rows':'','checkLabel':'','style':''},
		{'selectorKey':'#lstSqno',          'id':'lstSqno',          'class':'form-control hide',                'data-role':'text',         'data-field':'lstSqno',          'data-validate':'{"maxlength":3}',                     'readonly':true,  'disabled':false, 'listName':'',             'rows':'','checkLabel':'','style':''}
	],

	// サブミット時の値取得
	getSubmitValue : function () {
		var obj = {
			'companyCd'        : MstCommon.getValue('companyCd'),
			'mnyCd'            : MstCommon.getValue('mnyCd'),
			'sqno'             : MstCommon.getValue('sqno'),
			'inRto'            : MstCommon.getValue('inRto').replace(',',''),
			'rtoTp'            : MstCommon.getValue('rtoTp'),
			'vdDtS'            : MstCommon.getValue('vdDtS'),
			'vdDtE'            : MstCommon.getValue('vdDtE'),
			'dltFg'            : MstCommon.getValue('dltFg'),
			'timestampUpdated' : MstCommon.getValue('timestampUpdated'),
			'lstSqno'          : MstCommon.getValue('lstSqno'),
		};
		return obj;
	},
};