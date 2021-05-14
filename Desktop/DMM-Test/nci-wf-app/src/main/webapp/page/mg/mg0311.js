var pager = null;
var displayMode = '0';

$(function() {

	// 検索キー
	mg0311.searchParam = {
		"companyCd" : NCI.getQueryString("companyCd"),
		"zipCd" : NCI.getQueryString("zipCd"),
		"sqno" : NCI.getQueryString("sqno")
	};

	if(NCI.getQueryString("insertFlg")){
		// 登録モード
		displayMode = '2';
	}

	// 初期化処理開始
	mg0311.init();

	// イベント
	$(document)
	// 編集ボタン
	.on('click', '#btnChangeMode', function() {
		displayMode = '1';
		mg0311.searchParam['needMenuHtml'] = false;
		mg0311.searchParam['needFooterHtml'] = false;
		mg0311.init();
	})
	// 戻るボタン
	.on('click', '#btnBack', function() {
		NCI.redirect("./mg0310.html");
	})
	// 更新ボタン
	.on('click', '#btnUpdate', function() {

		const $targets = $('#editArea').find('input, select, text');
		if (!Validator.validate($targets, true)) {
			return false;
		}

		var paramsForCheck =  {
				"companyCd" : $("#companyCd select").val(),
				"zipCd" : $("#zipCd input").val(),
				"sqno" : $("#sqno input").val(),
				"adrPrf" : $("#adrPrf input").val(),
				"adr1" : $("#adr1 input").val(),
				"adr2" : $("#adr2 input").val(),
				"needMenuHtml": false,
				"needFooterHtml": false
			};

		// 更新内容をチェックする
		NCI.init("/mg0311/updateCheck", paramsForCheck).done(function(res) {

			// 同一の通貨コードがすでに登録されていた場合
			if (res) {
				var msg = '住所情報はすでに登録済みか有効でないか再度ご確認ください。';
				$('#checkErrorAlertBody').html(msg);
				$('#checkErrorAlert').modal({show: true});
			} else {

				var messege = '登録内容を更新します。よろしいですか？';

				NCI.confirm(messege, function() {

					// 更新処理
					var params = mg0311.getSubmitValue();
					params['needMenuHtml'] = false;
					params['needFooterHtml'] = false;

					// 初期化処理開始
					NCI.init("/mg0311/update", params).done(function(res, textStatus, jqXHR) {
						if (res && res.success) {
							// 完了メッセージ
							var msg = '登録内容の更新が完了しました。';
							$('#mg0311InformationContents').html(msg);
							$('#mg0311Information').modal({show: true});
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
				"zipCd" : $("#zipCd input").val(),
				"adrPrf" : $("#adrPrf input").val(),
				"adr1" : $("#adr1 input").val(),
				"adr2" : $("#adr2 input").val(),
				"needMenuHtml": false,
				"needFooterHtml": false
			};

		// 登録内容をチェックする
		NCI.init("/mg0311/insertCheck", paramsForCheck).done(function(res) {

			// 同一の通貨コードがすでに登録されていた場合
			if (res) {
				var msg = '住所情報はすでに登録済みか有効でないか再度ご確認ください。';
				$('#checkErrorAlertBody').html(msg);
				$('#checkErrorAlert').modal({show: true});
			} else {
				var messege = '登録します。よろしいですか？';

				NCI.confirm(messege, function() {

					// 更新処理
					var params = mg0311.getSubmitValue();
					params['needMenuHtml'] = false;
					params['needFooterHtml'] = false;

					// 初期化処理開始
					NCI.init("/mg0311/insert", params).done(function(res, textStatus, jqXHR) {
						if (res && res.success) {
							// 完了メッセージ
							var msg = '登録が完了しました。';
							$('#mg0311InformationContents').html(msg);
							$('#mg0311Information').modal({show: true});
							displayMode = '1';
							mg0311.searchParam['companyCd'] = params.companyCd;
							mg0311.searchParam['zipCd'] = params.zipCd;
							mg0311.searchParam['sqno'] = res.sqno;
							mg0311.searchParam['needMenuHtml'] = false;
							mg0311.searchParam['needFooterHtml'] = false;
							mg0311.init();
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
var mg0311 = {

	// 検索キー
	searchParam : {},

	// 初期化処理
	init : function () {
		NCI.init("/mg0311/init", mg0311.searchParam).done(function(res, textStatus, jqXHR) {
			if (res && res.success) {

				// 基本情報設定
				MstCommon.baseRenderInfo = res;

				// フィールド初期化
				MstCommon.createField(mg0311.partsInfo, res.entity, displayMode);

				// ボタン表示
				if (displayMode == '0') {
					$('#btnUpdate').hide();
					$('#btnChangeMode').show();
					$('#btnRegister').hide();
				// 編集モード
				} else if(displayMode == '1') {
					$('#btnUpdate').show();
					$('#btnChangeMode').hide();
					$('#btnRegister').hide();
					$('#zipCd input').prop('disabled', true);
					$('#sqno input').prop('disabled', true);

				// 登録モード
				} else {
					$('#btnUpdate').hide();
					$('#btnChangeMode').hide();
					$('#btnRegister').show();
					$('#zipCd input').prop('disabled', false);
					$('#sqno input').prop('disabled', true);
					$('#dltFg input:first').prop('checked', true);
					$('#dltFg input').prop('disabled', true);
				}
			}
		});
	},

	// パーツ情報
	partsInfo : [
		{'selectorKey':'#companyCd', 'id':'companyCd', 'class':'form-control required', 'data-role':'dropdownCode', 'data-field':'companyCd', 'data-validate':'', 'readonly':true, 'disabled':false, 'listName':'companyItems', 'rows':'','checkLabel':'','style':''},
		{'selectorKey':'#zipCd', 'id':'zipCd', 'class':'form-control required', 'data-role':'text', 'data-field':'zipCd', 'data-validate':'{"maxlength":7, "pattern": "numberOnly"}', 'readonly':false, 'disabled':false, 'listName':'', 'rows':'','checkLabel':'','style':''},
		{'selectorKey':'#sqno', 'id':'sqno', 'class':'form-control required', 'data-role':'text', 'data-field':'sqno', 'data-validate':'{"maxlength":3, "pattern": "numberOnly"}', 'readonly':true, 'disabled':false, 'listName':'', 'rows':'','checkLabel':'','style':''},
		{'selectorKey':'#adrPrfCd', 'id':'adrPrfCd', 'class':'form-control required', 'data-role':'text', 'data-field':'adrPrfCd', 'data-validate':'{"maxlength":5, "pattern": "numberOnly"}', 'readonly':false, 'disabled':false, 'listName':'', 'rows':'','checkLabel':'','style':''},
		{'selectorKey':'#adrPrf', 'id':'adrPrf', 'class':'form-control required', 'data-role':'text', 'data-field':'adrPrf', 'data-validate':'{"maxlength":30}', 'readonly':false, 'disabled':false, 'listName':'', 'rows':'','checkLabel':'','style':''},
		{'selectorKey':'#adrPrfKn', 'id':'adrPrfKn', 'class':'form-control required', 'data-role':'text', 'data-field':'adrPrfKn', 'data-validate':'{"maxlength":30, "pattern": "halfWidthOnly"}', 'readonly':false, 'disabled':false, 'listName':'', 'rows':'','checkLabel':'','style':''},
		{'selectorKey':'#adr1', 'id':'adr1', 'class':'form-control required', 'data-role':'text', 'data-field':'adr1', 'data-validate':'{"maxlength":60}', 'readonly':false, 'disabled':false, 'listName':'', 'rows':'','checkLabel':'','style':''},
		{'selectorKey':'#adr1Kn', 'id':'adr1Kn', 'class':'form-control required', 'data-role':'text', 'data-field':'adr1Kn', 'data-validate':'{"maxlength":100, "pattern": "halfWidthOnly"}', 'readonly':false, 'disabled':false, 'listName':'', 'rows':'','checkLabel':'','style':''},
		{'selectorKey':'#adr2', 'id':'adr2', 'class':'form-control required', 'data-role':'text', 'data-field':'adr2', 'data-validate':'{"maxlength":60}', 'readonly':false, 'disabled':false, 'listName':'', 'rows':'','checkLabel':'','style':''},
		{'selectorKey':'#adr2Kn', 'id':'adr2Kn', 'class':'form-control required', 'data-role':'text', 'data-field':'adr2Kn', 'data-validate':'{"maxlength":100, "pattern": "halfWidthOnly"}', 'readonly':false, 'disabled':false, 'listName':'', 'rows':'','checkLabel':'','style':''},
		{'selectorKey':'#dltFg', 'id':'dltFg', 'class':'form-control required', 'data-role':'radioCode', 'data-field':'dltFg', 'data-validate':'', 'readonly':false, 'disabled':false, 'listName':'dltFg', 'rows':'','checkLabel':'','style':''}
	],

	// サブミット時の値取得
	getSubmitValue : function () {
		var obj = {
			'companyCd' : MstCommon.getValue('companyCd'),
			'zipCd' : MstCommon.getValue('zipCd'),
			'sqno' : MstCommon.getValue('sqno'),
			'adrPrfCd' : MstCommon.getValue('adrPrfCd'),
			'adrPrf' : MstCommon.getValue('adrPrf'),
			'adrPrfKn' : MstCommon.getValue('adrPrfKn'),
			'adr1' : MstCommon.getValue('adr1'),
			'adr1Kn' : MstCommon.getValue('adr1Kn'),
			'adr2' : MstCommon.getValue('adr2'),
			'adr2Kn' : MstCommon.getValue('adr2Kn'),
			'dltFg' : MstCommon.getValue('dltFg')
		};
		return obj;
	},

};