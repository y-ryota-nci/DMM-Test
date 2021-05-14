var pager = null;
var displayMode = '0';

$(function() {

	// 検索キー
	mg0011.searchParam = {
		"companyCd" : NCI.getQueryString("companyCd"),
		"orgnzCd" : unescape(NCI.getQueryString("orgnzCd")),
		"itmCd" : NCI.getQueryString("itmCd"),
		"sqno" : NCI.getQueryString("sqno"),
		"messageCds": ['MSG0208', 'picture']
	};

	if(NCI.getQueryString("insertFlg")){
		// 登録モード
		displayMode = '2';
	}

	// 初期化処理開始
	mg0011.init();

	// イベント
	$(document)
	// 編集ボタン
	.on('click', '#btnChangeMode', function() {
		displayMode = '1';
		mg0011.searchParam['needMenuHtml'] = false;
		mg0011.searchParam['needFooterHtml'] = false;
		mg0011.init();
	})
	// 戻るボタン
	.on('click', '#btnBack', function() {
		NCI.redirect("./mg0010.html");
	})
	// 更新ボタン
	.on('click', '#btnUpdate', function() {

		const $targets = $('#editArea').find('input, select, text');
		if (!Validator.validate($targets, true)) {
			return false;
		}

		var paramsForCheck =  {
			"companyCd" : $("#companyCd select").val(),
			"orgnzCd" : $("#orgnzCd input").val(),
			"itmCd" : $("#itmCd input").val(),
			"sqno" : $("#sqno input").val(),
			"vdDtS" : $("#vdDtS input").val(),
			"vdDtE" : $("#vdDtE input").val(),
			"needMenuHtml": false,
			"needFooterHtml": false
		};

		// 登録内容をチェックする
		NCI.init("/mg0011/updateCheck", paramsForCheck).done(function(res) {

			// 同一の銀行コードがすでに登録されていた場合
			if (res) {
				var msg = '品目は有効期間内に含まれるものが既に登録されています。';
				$('#checkErrorAlertBody').html(msg);
				$('#checkErrorAlert').modal({show: true});
			} else {
				var messege = '登録内容を更新します。よろしいですか？';

				NCI.confirm(messege, function() {

					// 更新処理
					var params = mg0011.getSubmitValue();
					params['needMenuHtml'] = false;
					params['needFooterHtml'] = false;


					// 初期化処理開始
					NCI.init("/mg0011/update", params).done(function(res, textStatus, jqXHR) {
						if (res && res.success) {
							// 完了メッセージ
							var msg = '登録内容の更新が完了しました。';
							$('#mg0011InformationContents').html(msg);
							$('#mg0011Information').modal({show: true});
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
			"orgnzCd" : $("#orgnzCd input").val(),
			"itmCd" : $("#itmCd input").val(),
			"vdDtS" : $("#vdDtS input").val(),
			"vdDtE" : $("#vdDtE input").val(),
			"needMenuHtml": false,
			"needFooterHtml": false
		};

		// 登録内容をチェックする
		NCI.init("/mg0011/insertCheck", paramsForCheck).done(function(res) {

			// 同一の銀行コードがすでに登録されていた場合
			if (res) {
				var msg = '品目は有効期間内に含まれるものが既に登録されています。';
				$('#checkErrorAlertBody').html(msg);
				$('#checkErrorAlert').modal({show: true});
			} else {
				var messege = '登録します。よろしいですか？';

				NCI.confirm(messege, function() {

					// 更新処理
					var params = mg0011.getSubmitValue();
					params['needMenuHtml'] = false;
					params['needFooterHtml'] = false;

					// 初期化処理開始
					NCI.init("/mg0011/insert", params).done(function(res, textStatus, jqXHR) {
						if (res && res.success) {
							// 完了メッセージ
							var msg = '登録が完了しました。';
							$('#mg0011InformationContents').html(msg);
							$('#mg0011Information').modal({show: true});
							displayMode = '1';
							mg0011.searchParam['companyCd'] = res.entity.companyCd;
							mg0011.searchParam['orgnzCd'] = res.entity.orgnzCd;
							mg0011.searchParam['itmCd'] = res.entity.itmCd;
							mg0011.searchParam['sqno'] = res.entity.sqno;
							mg0011.searchParam['needMenuHtml'] = false;
							mg0011.searchParam['needFooterHtml'] = false;
							mg0011.init();
						}
					});
				});
			}
		});
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

	// 取引先検索ボタン
	.on('click', '#splrCdSettings>.btn-primary', function() {
		const conds = {"COMPANY_CD" : MstCommon.getValue('companyCd')};
		NCI.openMasterSearch('V_SPLR_MST', 'FOR_POPUP_ORD', callbackSearchSplrCd, conds);
	})
	// 取引先のクリアボタン
	.on('click', '#splrCdSettings>.btn-default', function() {
		MstCommon.setValue('splrCd', '');
		MstCommon.setValue('splrNmKj', '');
		MstCommon.setValue('splrNmKn', '');
	})

	// 組織検索ボタン
	.on('click', '#orgnzCdSettings>.btn-primary', function() {
		// 選択ポップアップ起動
		// 組織選択（企業を選択済みならその企業の）
		const params = null, corporationCode = MstCommon.getValue('companyCd');
		let url = "../cm/cm0020.html?initSearch=true";
		if (corporationCode)
			url += "&corporationCode=" + corporationCode + "&organizationLevel=3";
		Popup.open(url, mg0011.callbackFromCm0020, params, this);
	})
	// 組織のクリアボタン
	.on('click', '#orgnzCdSettings>.btn-default', function() {
		MstCommon.setValue('orgnzCd', '');
		MstCommon.setValue('orgnzNm', '');
	})
	//エラーアラート用
	.on('click', '#checkErrorAlertButtonArea>.btn-primary', function() {
		$('#checkErrorAlert').modal("hide");
	})
	.on('click', '#updFile', function() {
			$('#tenpu').find('input[type=file]').click();
	});

	//ドラッグ＆ドロップによるファイルアップロード
	FileUploader.setup("div.dragZone, input[type=file]", "/mg0011/upload", false, function(itemImage) {
		if (itemImage) {
			const fileName = itemImage.fileName, pos = fileName.indexOf('.');
			const ext = (pos < 0 ? '' : fileName.substring(pos + 1));
			if (!/PNG|GIF|JPG|JPEG/i.test(ext)) {
				NCI.alert(NCI.getMessage('MSG0208', NCI.getMessage('picture')));
				return false;
			}
			mg0011.drawImage(itemImage.itmImgId);
		}
	});

	callbackSearchTaxCd = function (condition) {
		if (condition) {
			MstCommon.setValue('taxCd', condition["TAX_CD"]);
			MstCommon.setValue('taxNm', condition["TAX_NM"]);
		}
	};

	callbackSearchSplrCd = function (condition) {
		if (condition) {
			MstCommon.setValue('splrCd', condition["SPLR_CD"]);
			MstCommon.setValue('splrNmKj', condition["SPLR_NM_KJ"]);
			MstCommon.setValue('splrNmKn', condition["SPLR_NM_KN"]);
		}
	};


});

// 画面固有JS
var mg0011 = {

	callbackFromCm0020 : function (org, trigger) {
		if (org) {
			MstCommon.setValue('orgnzCd', org.organizationCode);
			MstCommon.setValue('orgnzNm', org.organizationName);
		}
	},

	// 検索キー
	searchParam : {},

	// 初期化処理
	init : function () {
		NCI.init("/mg0011/init", mg0011.searchParam).done(function(res, textStatus, jqXHR) {
			if (res && res.success) {

				// 基本情報設定
				MstCommon.baseRenderInfo = res;

				// フィールド初期化
				MstCommon.createField(mg0011.partsInfo, res.entity, displayMode);

				//日付フォーマットを修正
				$.each($('input.ymdPicker'), function(index, value) {
					value.value = value.value.slice(0, 10);
				});

				// ボタン表示
				if (displayMode == '0') {
					$('#btnUpdate').hide();
					$('#btnChangeMode').show();
					$('#btnRegister').hide();
					$('#orgnzCdSettings').hide();
					$('#itmImgDragAndDrop').hide();
					$('#updFile').hide();
					$('#taxCdSettings').hide();
					$('#splrCdSettings').hide();

				// 編集モード
				} else if(displayMode == '1') {
					$('#btnUpdate').show();
					$('#btnChangeMode').hide();
					$('#btnRegister').hide();
					$('#orgnzCdSettings').hide();
					$('#companyCd select').prop('disabled', true);
					$('#itmImgDragAndDrop').show();
					$('#updFile').show();
					$('#taxCdSettings').show();
					$('#splrCdSettings').show();

				// 登録モード
				} else {
					$('#btnUpdate').hide();
					$('#btnChangeMode').hide();
					$('#btnRegister').show();
					$('#itmCd input').removeAttr('readonly');
					$('#stckTp input:first').prop('checked', true);
					$('#prcFldTp input:first').prop('checked', true);
					$('#dltFg input:first').prop('checked', true);
					$('#dltFg input').prop('disabled', false);
					$('#orgnzCdSettings').show();
					$('#companyCd select').prop('disabled', false);
					$('#itmImgDragAndDrop').show();
					$('#updFile').show();
					MstCommon.setValue('sqno', '1');
					$('#taxCdSettings').show();
					$('#splrCdSettings').show();
				}

				// 画像をロードする
				if (res.entity && res.entity.itmImgId) {
					mg0011.drawImage(res.entity.itmImgId);
				}

				// カレンダー（年月日）
				//	初期状態だとフォーカスアウト時に現在日付を保存してしまうため位置を修正
				NCI.ymdPicker($('input.ymdPicker'));

			}
		});
	},

	// パーツ情報（品目マスタ）
	partsInfo : [
		{'selectorKey':'#companyCd', 'id':'companyCd', 'class':'form-control required', 'data-role':'dropdownCode', 'data-field':'companyCd', 'data-validate':'', 'readonly':true, 'disabled':false, 'listName':'companyItems', 'rows':'','checkLabel':'','style':''},
		{'selectorKey':'#orgnzCd', 'id':'orgnzCd', 'class':'form-control required', 'data-role':'text', 'data-field':'orgnzCd', 'data-validate':'{"maxlength":10}', 'readonly':true, 'disabled':false, 'listName':'', 'rows':'','checkLabel':'','style':''},
		{'selectorKey':'#orgnzNm', 'id':'orgnzNm', 'class':'form-control', 'data-role':'text', 'data-field':'orgnzNm', 'data-validate':'', 'readonly':true, 'disabled':false, 'listName':'', 'rows':'','checkLabel':'','style':''},
		{'selectorKey':'#itmCd', 'id':'itmCd', 'class':'form-control required', 'data-role':'text', 'data-field':'itmCd', 'data-validate':'{"maxlength":20}', 'readonly':true, 'disabled':false, 'listName':'', 'rows':'','checkLabel':'','style':''},
		{'selectorKey':'#sqno', 'id':'sqno', 'class':'form-control', 'data-role':'text', 'data-field':'sqno', 'data-validate':'{}', 'readonly':true, 'disabled':false, 'listName':'', 'rows':'','checkLabel':'','style':''},
		{'selectorKey':'#itmNm', 'id':'itmNm', 'class':'form-control', 'data-role':'text', 'data-field':'itmNm', 'data-validate':'{"maxlength":30}', 'readonly':false, 'disabled':false, 'listName':'', 'rows':'','checkLabel':'','style':''},
		{'selectorKey':'#stckTp', 'id':'stckTp', 'class':'form-control required', 'data-role':'radioCode', 'data-field':'stckTp', 'data-validate':'', 'readonly':false, 'disabled':false, 'listName':'stckTps', 'rows':'','checkLabel':'','style':''},
		{'selectorKey':'#untCd', 'id':'untCd', 'class':'form-control required', 'data-role':'dropdownCode', 'data-field':'untCd', 'data-validate':'', 'readonly':false, 'disabled':false, 'listName':'untCds', 'rows':'','checkLabel':'','style':''},
		{'selectorKey':'#amt', 'id':'amt', 'class':'form-control required', 'data-role':'text', 'data-field':'amt', 'data-validate':'{"min":0,"max":999999999999,"pattern":"integer"}', 'readonly':false, 'disabled':false, 'listName':'', 'rows':'','checkLabel':'','style':''},
		{'selectorKey':'#taxCd', 'id':'taxCd', 'class':'form-control', 'data-role':'text', 'data-field':'taxCd', 'data-validate':'{"maxlength":3}', 'readonly':true, 'disabled':false, 'listName':'', 'rows':'','checkLabel':'','style':''},
		{'selectorKey':'#taxNm', 'id':'taxNm', 'class':'form-control', 'data-role':'text', 'data-field':'taxNm', 'data-validate':'', 'readonly':true, 'disabled':false, 'listName':'', 'rows':'','checkLabel':'','style':''},
		{'selectorKey':'#splrCd', 'id':'splrCd', 'class':'form-control', 'data-role':'text', 'data-field':'splrCd', 'data-validate':'{"maxlength":20}', 'readonly':true, 'disabled':false, 'listName':'', 'rows':'','checkLabel':'','style':''},
		{'selectorKey':'#splrNmKj', 'id':'splrNmKj', 'class':'form-control', 'data-role':'text', 'data-field':'splrNmKj', 'data-validate':'', 'readonly':true, 'disabled':false, 'listName':'', 'rows':'','checkLabel':'','style':''},
		{'selectorKey':'#splrNmKn', 'id':'splrNmKn', 'class':'form-control', 'data-role':'text', 'data-field':'splrNmKn', 'data-validate':'', 'readonly':true, 'disabled':false, 'listName':'', 'rows':'','checkLabel':'','style':''},
		{'selectorKey':'#makerNm', 'id':'makerNm', 'class':'form-control', 'data-role':'text', 'data-field':'makerNm', 'data-validate':'{"maxlength":30}', 'readonly':false, 'disabled':false, 'listName':'', 'rows':'','checkLabel':'','style':''},
		{'selectorKey':'#makerMdlNo', 'id':'makerMdlNo', 'class':'form-control', 'data-role':'text', 'data-field':'makerMdlNo', 'data-validate':'{"maxlength":90, "pattern":"alphaSymbolNumber"}', 'readonly':false, 'disabled':false, 'listName':'', 'rows':'','checkLabel':'','style':''},
		{'selectorKey':'#itmRmk', 'id':'itmRmk', 'class':'form-control', 'data-role':'text', 'data-field':'itmRmk', 'data-validate':'{"maxlength":1000}', 'readonly':false, 'disabled':false, 'listName':'', 'rows':'','checkLabel':'','style':''},
		{'selectorKey':'#ctgryCd', 'id':'ctgryCd', 'class':'form-control', 'data-role':'text', 'data-field':'ctgryCd', 'data-validate':'{"maxlength":10, "pattern": "alphaNumber"}', 'readonly':false, 'disabled':false, 'listName':'', 'rows':'','checkLabel':'','style':''},
		{'selectorKey':'#itmVrsn', 'id':'itmVrsn', 'class':'form-control', 'data-role':'text', 'data-field':'itmVrsn', 'data-validate':'{"maxlength":18, "pattern": "integer"}', 'readonly':false, 'disabled':false, 'listName':'', 'rows':'','checkLabel':'','style':''},
		{'selectorKey':'#prcFldTp', 'id':'prcFldTp', 'class':'form-control required', 'data-role':'radioCode', 'data-field':'prcFldTp', 'data-validate':'', 'readonly':false, 'disabled':false, 'listName':'prcFldTps', 'rows':'','checkLabel':'','style':''},
		{'selectorKey':'#vdDtS', 'id':'vdDtS', 'class':'form-control ymdPicker', 'data-role':'text', 'data-field':'vdDtS', 'data-validate':'{"pattern":"date"}', 'readonly':false, 'disabled':false, 'listName':'', 'rows':'','checkLabel':'','style':''},
		{'selectorKey':'#vdDtE', 'id':'vdDtE', 'class':'form-control ymdPicker', 'data-role':'text', 'data-field':'vdDtE', 'data-validate':'{"pattern":"date"}', 'readonly':false, 'disabled':false, 'listName':'', 'rows':'','checkLabel':'','style':''},
		{'selectorKey':'#dltFg', 'id':'dltFg', 'class':'form-control required', 'data-role':'radioCode', 'data-field':'dltFg', 'data-validate':'', 'readonly':false, 'disabled':false, 'listName':'dltFgNm', 'rows':'','checkLabel':'','style':''},
		{'selectorKey':'#itmImgId', 'id':'itmImgId', 'class':'form-control', 'data-role':'text', 'data-field':'itmImgId', 'data-validate':'', 'readonly':false, 'disabled':false, 'listName':'', 'rows':'','checkLabel':'','style':''}
	],

	// サブミット時の値取得
	getSubmitValue : function () {
		var obj = {
			'companyCd' : MstCommon.getValue('companyCd'),
			'orgnzCd' : MstCommon.getValue('orgnzCd'),
			'itmCd' : MstCommon.getValue('itmCd'),
			'sqno' : MstCommon.getValue('sqno'),
			'itmNm' : MstCommon.getValue('itmNm'),
			'ctgryCd' : MstCommon.getValue('ctgryCd'),
			'stckTp' : MstCommon.getValue('stckTp'),
			'untCd' : MstCommon.getValue('untCd'),
			'amt' : MstCommon.getValue('amt'),
			'taxCd' : MstCommon.getValue('taxCd'),
			'splrCd' : MstCommon.getValue('splrCd'),
			'splrNmKj' : MstCommon.getValue('splrNmKj'),
			'splrNmKn' : MstCommon.getValue('splrNmKn'),
			'makerNm' : MstCommon.getValue('makerNm'),
			'makerMdlNo' : MstCommon.getValue('makerMdlNo'),
			'itmRmk' : MstCommon.getValue('itmRmk'),
			'itmVrsn' : MstCommon.getValue('itmVrsn'),
			'prcFldTp' : MstCommon.getValue('prcFldTp'),
			'vdDtS' : MstCommon.getValue('vdDtS'),
			'vdDtE' : MstCommon.getValue('vdDtE'),
			'dltFg' : MstCommon.getValue('dltFg'),
			'itmImgId' : MstCommon.getValue('itmImgId')
		};
		return obj;
	},

	/** アップロード結果を表示 */
	drawImage : function(itmImgId) {
		var $imageArea = $('#imageArea');
		$imageArea.find('img').remove();

		var url = '../../assets/nci/images/noImage.png';
		if (itmImgId) {
			url = '../../endpoint/mg0011/download/itemImage?itmImgId=' + itmImgId;
			url += '&_tm=' + +new Date();
		}
		$imageArea.append('<img src="' + url + '" class="img-responsive img-thumbnail col-lg-6 col-md-6 col-sm-9 col-xs-12 ">');
//		$('#itmImgId').val(itmImgId);
		MstCommon.setValue('itmImgId', itmImgId);
	},
};
