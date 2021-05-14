var pager = null;
var displayMode = '0';

$(function() {

	// ページング情報(口座情報)
	pager = new Pager($('#seach-result'), '/sp0011/getAccountList', sp0011.getAccountList).init();
	pager['resultPropName'] = 'accList';

	// 検索キー
	sp0011.searchParam = {
		"companyCd" : NCI.getQueryString("companyCd"),
		"splrCd" : NCI.getQueryString("splrCd")
	};

	// 初期化処理開始
	sp0011.init();

	// イベント
	$(document)
	// 編集ボタン
	.on('click', '#btnChangeMode', function() {
		displayMode = '1';
		sp0011.init();
	})
	// 戻るボタン
	.on('click', '#btnBack', function() {
		NCI.redirect("./sp0010.html");
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
			var params = sp0011.getSubmitValue();

			// 初期化処理開始
			NCI.init("/sp0011/update", params).done(function(res, textStatus, jqXHR) {
				if (res && res.success) {
					// 完了メッセージ
					var msg = '登録内容の更新が完了しました。';
					$('#sp0011InformationContents').html(msg);
					$('#sp0011Information').modal({show: true});
				}
			});
		});
	});
});

// 画面固有JS
var sp0011 = {

	// 検索キー
	searchParam : {},

	// 初期化処理
	init : function () {
		NCI.init("/sp0011/init", sp0011.searchParam).done(function(res, textStatus, jqXHR) {
			if (res && res.success) {

				// HD表示切替
				if (NCI.loginInfo.corporationCode == '00053') {
					$('#seach-result .section').remove();
					$('#seach-result .hd').show();
				} else {
					$('#seach-result .hd').remove();
					$('#seach-result .section').show();
				}

				// 基本情報設定
				MstCommon.baseRenderInfo = res;

				// フィールド初期化
				MstCommon.createField(sp0011.partsInfo, res.entity, displayMode);

				// 口座情報取得
				var cond = pager.createCondition(null, 1);

				cond['companyCd'] =res.entity['companyCd'];
				cond['splrCd'] = res.entity['splrCd'];

				pager.search(cond).done(function(accRes) {

					// フィールド初期化
					MstCommon.createField(sp0011.gridPartsInfo, accRes.accList, displayMode);

					// 表示
					$('#seach-result').removeClass('hide');
				});

				// カレンダー（年月日）
				NCI.ymdPicker($('input.ymdPicker'));

				//日付フォーマットを修正
				$.each($('input.ymdPicker'), function(index, value) {
					value.value = value.value.slice(0, 10);
				});

				$(document).on('click', 'ul.pagination a', function(ev) {
					// ページ番号リンク押下
					const pageNo = this.getAttribute('data-pageNo');
					sp0011.getAccountList(pageNo);
					return false;
				})

				// ボタン表示
				if (displayMode == '0') {
					$('#payCondSettings').hide();
					$('#btnUpdate').hide();
					$('#btnChangeMode').hide();
				} else {
					$('#payCondSettings').show();
					$('#btnUpdate').show();
					$('#btnChangeMode').hide();
				}
			}
		});
	},

	// パーツ情報（取引先情報）
	partsInfo : [
		{'selectorKey':'#companyCd', 'id':'companyCd', 'class':'', 'data-role':'hidden', 'data-field':'companyCd', 'data-validate':'', 'readonly':false, 'disabled':false, 'listName':'', 'rows':'','checkLabel':'','style':''},
		{'selectorKey':'#companyAddedInfo', 'id':'companyAddedInfo', 'class':'form-control', 'data-role':'text', 'data-field':'companyAddedInfo', 'data-validate':'', 'readonly':true, 'disabled':false, 'listName':'', 'rows':'','checkLabel':'','style':''},
		{'selectorKey':'#companyNm', 'id':'companyNm', 'class':'form-control', 'data-role':'text', 'data-field':'companyNm', 'data-validate':'', 'readonly':true, 'disabled':false, 'listName':'', 'rows':'','checkLabel':'','style':''},
		{'selectorKey':'#splrCd', 'id':'splrCd', 'class':'form-control', 'data-role':'text', 'data-field':'splrCd', 'data-validate':'', 'readonly':true, 'disabled':false, 'listName':'', 'rows':'','checkLabel':'','style':''},
		{'selectorKey':'#crpPrsTp', 'id':'crpPrsTp', 'class':'', 'data-role':'radioCode', 'data-field':'crpPrsTp', 'data-validate':'', 'readonly':false, 'disabled':false, 'listName':'crpPrsTps', 'rows':'','checkLabel':'','style':''},
		{'selectorKey':'#dmsAbrTp', 'id':'dmsAbrTp', 'class':'', 'data-role':'radioCode', 'data-field':'dmsAbrTp', 'data-validate':'', 'readonly':false, 'disabled':false, 'listName':'dmsAbrTps', 'rows':'','checkLabel':'','style':''},
		{'selectorKey':'#lndNm', 'id':'lndNm', 'class':'form-control', 'data-role':'text', 'data-field':'lndNm', 'data-validate':'{"maxlength":100,"pattern":"all"}', 'readonly':false, 'disabled':false, 'listName':'', 'rows':'','checkLabel':'','style':''},
		{'selectorKey':'#crpNo', 'id':'crpNo', 'class':'form-control', 'data-role':'text', 'data-field':'crpNo', 'data-validate':'{"maxlength":13,"pattern":"numberOnly"}', 'readonly':false, 'disabled':false, 'listName':'', 'rows':'','checkLabel':'','style':''},
		{'selectorKey':'#splrNmKj', 'id':'splrNmKj', 'class':'form-control', 'data-role':'text', 'data-field':'splrNmKj', 'data-validate':'{"maxlength":50,"pattern":"all"}', 'readonly':false, 'disabled':false, 'listName':'', 'rows':'','checkLabel':'','style':''},
		{'selectorKey':'#splrNmKn', 'id':'splrNmKn', 'class':'form-control', 'data-role':'text', 'data-field':'splrNmKn', 'data-validate':'{"maxlength":150,"pattern":"halfWidthOnly"}', 'readonly':false, 'disabled':false, 'listName':'', 'rows':'','checkLabel':'','style':''},
		{'selectorKey':'#splrNmS', 'id':'splrNmS', 'class':'form-control', 'data-role':'text', 'data-field':'splrNmS', 'data-validate':'{"maxlength":30,"pattern":"all"}', 'readonly':false, 'disabled':false, 'listName':'', 'rows':'','checkLabel':'','style':''},
		{'selectorKey':'#splrNmE', 'id':'splrNmE', 'class':'form-control', 'data-role':'text', 'data-field':'splrNmE', 'data-validate':'{"maxlength":200,"pattern":"alphaSymbolNumber"}', 'readonly':false, 'disabled':false, 'listName':'', 'rows':'','checkLabel':'','style':''},
		{'selectorKey':'#zipCd', 'id':'zipCd', 'class':'form-control', 'data-role':'text', 'data-field':'zipCd', 'data-validate':'{"maxlength":10,"pattern":"postCode"}', 'readonly':false, 'disabled':false, 'listName':'', 'rows':'','checkLabel':'','style':''},
		{'selectorKey':'#adrPrfCd', 'id':'adrPrfCd', 'class':'', 'data-role':'dropdownCode', 'data-field':'adrPrfCd', 'data-validate':'', 'readonly':false, 'disabled':false, 'listName':'adrPrfCds', 'rows':'','checkLabel':'','style':''},
		{'selectorKey':'#adr1', 'id':'adr1', 'class':'form-control', 'data-role':'text', 'data-field':'adr1', 'data-validate':'{"maxlength":30,"pattern":"all"}', 'readonly':false, 'disabled':false, 'listName':'', 'rows':'','checkLabel':'','style':''},
		{'selectorKey':'#adr2', 'id':'adr2', 'class':'form-control', 'data-role':'text', 'data-field':'adr2', 'data-validate':'{"maxlength":50,"pattern":"all"}', 'readonly':false, 'disabled':false, 'listName':'', 'rows':'','checkLabel':'','style':''},
		{'selectorKey':'#adr3', 'id':'adr3', 'class':'form-control', 'data-role':'text', 'data-field':'adr3', 'data-validate':'{"maxlength":30,"pattern":"all"}', 'readonly':false, 'disabled':false, 'listName':'', 'rows':'','checkLabel':'','style':''},
		{'selectorKey':'#telNo', 'id':'telNo', 'class':'form-control', 'data-role':'text', 'data-field':'telNo', 'data-validate':'{"maxlength":15,"pattern":"tel"}', 'readonly':false, 'disabled':false, 'listName':'', 'rows':'','checkLabel':'','style':''},
		{'selectorKey':'#faxNo', 'id':'faxNo', 'class':'form-control', 'data-role':'text', 'data-field':'faxNo', 'data-validate':'{"maxlength":15,"pattern":"tel"}', 'readonly':false, 'disabled':false, 'listName':'', 'rows':'','checkLabel':'','style':''},
		{'selectorKey':'#affcmpTp', 'id':'affcmpTp', 'class':'form-control', 'data-role':'radioCode', 'data-field':'affcmpTp', 'data-validate':'', 'readonly':false, 'disabled':false, 'listName':'affcmpTps', 'rows':'','checkLabel':'','style':''},
		{'selectorKey':'#trdStsTp', 'id':'trdStsTp', 'class':'', 'data-role':'radioCode', 'data-field':'trdStsTp', 'data-validate':'', 'readonly':false, 'disabled':false, 'listName':'trdStsTps', 'rows':'','checkLabel':'','style':''},
		{'selectorKey':'#vdDtS', 'id':'vdDtS', 'class':'form-control ymdPicker', 'data-role':'text', 'data-field':'vdDtS', 'data-validate':'{"pattern":"date"}', 'readonly':false, 'disabled':false, 'listName':'', 'rows':'','checkLabel':'','style':''},
		{'selectorKey':'#vdDtE', 'id':'vdDtE', 'class':'form-control ymdPicker', 'data-role':'text', 'data-field':'vdDtE', 'data-validate':'{"pattern":"date"}', 'readonly':false, 'disabled':false, 'listName':'', 'rows':'','checkLabel':'','style':''},
		{'selectorKey':'#rmk', 'id':'rmk', 'class':'form-control', 'data-role':'text', 'data-field':'rmk', 'data-validate':'{"maxlength":1000,"pattern":"all"}', 'readonly':false, 'disabled':false, 'listName':'', 'rows':'','checkLabel':'','style':''},
		{'selectorKey':'#bumonCd', 'id':'bumonCd', 'class':'form-control', 'data-role':'text', 'data-field':'bumonCd', 'data-validate':'{"maxlength":14,"pattern":"alphaNumber"}', 'readonly':false, 'disabled':false, 'listName':'', 'rows':'','checkLabel':'','style':''},
		{'selectorKey':'#bumonNm', 'id':'bumonNm', 'class':'form-control', 'data-role':'text', 'data-field':'bumonNm', 'data-validate':'', 'readonly':false, 'disabled':false, 'listName':'', 'rows':'','checkLabel':'','style':''},
		{'selectorKey':'#cmptEtrNo', 'id':'cmptEtrNo', 'class':'form-control', 'data-role':'text', 'data-field':'cmptEtrNo', 'data-validate':'{"pattern":"alphaNumber","maxByteUtf8":50}', 'readonly':false, 'disabled':false, 'listName':'', 'rows':'','checkLabel':'','style':''},
		{'selectorKey':'#brthDt', 'id':'brthDt', 'class':'form-control ymdPicker', 'data-role':'text', 'data-field':'brthDt', 'data-validate':'{"pattern":"date"}', 'readonly':false, 'disabled':false, 'listName':'', 'rows':'','checkLabel':'','style':''}
	],

	// パーツ情報（口座情報）
	gridPartsInfo : [
		{'selectorKey':'#results .no', 'id':'', 'class':'form-control', 'data-role':'text', 'data-field':'no', 'data-validate':'{"pattern":"numberOnly"}', 'readonly':true, 'disabled':false, 'listName':'', 'rows':'','checkLabel':'', 'style':''},
		{'selectorKey':'#results .companyNm', 'id':'', 'class':'form-control', 'data-role':'text', 'data-field':'companyNm', 'data-validate':'{"maxlength":100,"pattern":"all"}', 'readonly':true, 'disabled':false, 'listName':'', 'rows':'','checkLabel':'', 'style':''},
		{'selectorKey':'#results .bnkCd', 'id':'', 'class':'form-control', 'data-role':'text', 'data-field':'bnkCd', 'data-validate':'{"maxlength":4,"pattern":"numberOnly"}', 'readonly':true, 'disabled':false, 'listName':'', 'rows':'','checkLabel':'', 'style':''},
		{'selectorKey':'#results .bnkNm', 'id':'', 'class':'form-control', 'data-role':'text', 'data-field':'bnkNm', 'data-validate':'{"maxlength":30,"pattern":"all"}', 'readonly':true, 'disabled':false, 'listName':'', 'rows':'','checkLabel':'', 'style':''},
		{'selectorKey':'#results .bnkbrcCd', 'id':'', 'class':'form-control', 'data-role':'text', 'data-field':'bnkbrcCd', 'data-validate':'{"maxlength":3,"pattern":"alphaNumber"}', 'readonly':true, 'disabled':false, 'listName':'', 'rows':'','checkLabel':'', 'style':''},
		{'selectorKey':'#results .bnkbrcNm', 'id':'', 'class':'form-control', 'data-role':'text', 'data-field':'bnkbrcNm', 'data-validate':'{"maxlength":10,"pattern":"all"}', 'readonly':true, 'disabled':false, 'listName':'', 'rows':'','checkLabel':'', 'style':''},
		{'selectorKey':'#results .bnkaccTp', 'id':'', 'class':'', 'data-role':'radioCode', 'data-field':'bnkaccTp', 'data-validate':'', 'readonly':false, 'disabled':true, 'listName':'bnkaccTps', 'rows':'','checkLabel':'', 'style':''},
		{'selectorKey':'#results .bnkaccNo', 'id':'', 'class':'form-control', 'data-role':'text', 'data-field':'bnkaccNo', 'data-validate':'{"maxlength":7,"pattern":"numberOnly"}', 'readonly':true, 'disabled':false, 'listName':'', 'rows':'','checkLabel':'', 'style':''},
		{'selectorKey':'#results .bnkaccNm', 'id':'', 'class':'form-control', 'data-role':'text', 'data-field':'bnkaccNm', 'data-validate':'{"maxlength":40,"pattern":"all"}', 'readonly':true, 'disabled':false, 'listName':'', 'rows':'','checkLabel':'', 'style':''},
		{'selectorKey':'#results .bnkaccNmKn', 'id':'', 'class':'form-control', 'data-role':'text', 'data-field':'bnkaccNmKn', 'data-validate':'{"maxlength":30,"pattern":"halfWidthOnly"}', 'readonly':true, 'disabled':false, 'listName':'', 'rows':'','checkLabel':'', 'style':''},
		{'selectorKey':'#results .payCmmOblTp', 'id':'', 'class':'', 'data-role':'radioCode', 'data-field':'payCmmOblTp', 'data-validate':'', 'readonly':false, 'disabled':false, 'listName':'payCmmOblTps', 'rows':'','checkLabel':'','style':''},
		{'selectorKey':'#results .hldTrtTp', 'id':'', 'class':'', 'data-role':'radioCode', 'data-field':'hldTrtTp', 'data-validate':'', 'readonly':false, 'disabled':false, 'listName':'hldTrtTps', 'rows':'','checkLabel':'','style':''},
		{'selectorKey':'#results .payeeBnkaccCd', 'id':'', 'class':'', 'data-role':'text', 'data-field':'payeeBnkaccCd', 'data-validate':'', 'readonly':true, 'disabled':false, 'listName':'', 'rows':'','checkLabel':'','style':'width:200px;'},
		{'selectorKey':'#results .payeeBnkaccCdSs', 'id':'', 'class':'', 'data-role':'text', 'data-field':'payeeBnkaccCdSs', 'data-validate':'', 'readonly':true, 'disabled':false, 'listName':'', 'rows':'','checkLabel':'','style':'width:200px;'},
		{'selectorKey':'#results .srcBnkaccCd', 'id':'', 'class':'', 'data-role':'text', 'data-field':'srcBnkaccCd', 'data-validate':'{"maxlength":4,"pattern":"numberOnly"}', 'readonly':true, 'disabled':false, 'listName':'', 'rows':'','checkLabel':'','style':''},
		{'selectorKey':'#results .srcBnkaccNm', 'id':'', 'class':'', 'data-role':'text', 'data-field':'srcBnkaccNm', 'data-validate':'', 'readonly':true, 'disabled':false, 'listName':'', 'rows':'','checkLabel':'','style':''}
	],

	// 口座情報検索
	getAccountList : function (pageNo, keepMessage) {
		var cond = pager.createCondition(null, pageNo);

		cond['companyCd'] = MstCommon.getValue('companyCd');
		cond['splrCd'] = MstCommon.getValue('splrCd');

		pager.search(cond, keepMessage).done(function(accRes) {

			// フィールド初期化
			MstCommon.createField(sp0011.gridPartsInfo, accRes.accList, displayMode);

			$('#seach-result').removeClass('hide');
		});
	},

	// サブミット時の値取得
	getSubmitValue : function () {

		var obj = {
			'companyCd' : MstCommon.getValue('companyCd'),
			'splrCd' : MstCommon.getValue('splrCd'),
			'crpPrsTp' : MstCommon.getValue('crpPrsTp'),
			'dmsAbrTp' : MstCommon.getValue('dmsAbrTp'),
			'lndNm' : MstCommon.getValue('lndNm'),
			'crpNo' : MstCommon.getValue('crpNo'),
			'splrNmKj' : MstCommon.getValue('splrNmKj'),
			'splrNmKn' : MstCommon.getValue('splrNmKn'),
			'splrNmS' : MstCommon.getValue('splrNmS'),
			'splrNmE' : MstCommon.getValue('splrNmE'),
			'zipCd' : MstCommon.getValue('zipCd'),
			'adrPrfCd' : MstCommon.getValue('adrPrfCd'),
			'adr1' : MstCommon.getValue('adr1'),
			'adr2' : MstCommon.getValue('adr2'),
			'adr3' : MstCommon.getValue('adr3'),
			'telNo' : MstCommon.getValue('telNo'),
			'faxNo' : MstCommon.getValue('faxNo'),
			'affcmpTp' : MstCommon.getValue('affcmpTp'),
			'mladr1' : MstCommon.getValue('mladr1'),
			'mladr2' : MstCommon.getValue('mladr2'),
			'cptl' : MstCommon.getValue('cptl'),
			'trdStsTp' : MstCommon.getValue('trdStsTp'),
			'vdDtS' : MstCommon.getValue('vdDtS'),
			'vdDtE' : MstCommon.getValue('vdDtE'),
			'rmk' : MstCommon.getValue('rmk'),
			'abstIn' : MstCommon.getValue('abstIn'),
			'bumonCd' : MstCommon.getValue('bumonCd'),
			'bumonNm' : MstCommon.getValue('bumonNm'),
			'cmptEtrNo' : MstCommon.getValue('cmptEtrNo'),
			'brthDt' : MstCommon.getValue('brthDt')
		};
		return obj;
	}
};

var MstCommon = {

	// 基本情報
	baseRenderInfo : null,

	// パーツ要素
	partsElements : {},

	// パーツ作成
	createField : function (partsArray, entityObj, displayMode) {
		if ($.isArray(entityObj)) {
			$.each(entityObj, function(i, obj) {
				MstCommon.createParts(partsArray, MstCommon.baseRenderInfo, obj, i, displayMode);
			});
		} else {
			MstCommon.createParts(partsArray, MstCommon.baseRenderInfo, entityObj, -1, displayMode);
		}
	},

	// パーツ作成
	createParts : function (partsArray, listObj, entityObj, gridIndex, displayMode) {

		$.each(partsArray, function(i, parts) {
			//作成対象要素取得
			var $targetObj = $(parts['selectorKey']);

			if ($targetObj.size() > 1) {
				if ($targetObj.size() > gridIndex) {
					$targetObj = $($targetObj.get(gridIndex));
				} else {
					$targetObj = null;
				}
			}

			if ($targetObj) {
				var id = parts['id'];
				var className = parts['class'];
				var dataRole = parts['data-role'];
				var dataField = parts['data-field'];
				var dataValidate = parts['data-validate'];
				var option = null;

				if (dataValidate) {
					option = JSON.parse(dataValidate);
				}

				var readonly = parts['readonly'];
				var disabled = parts['disabled'];
				var listName = parts['listName'];
				var rows = parts['rows'];
				var style= parts['style'];

				if (displayMode == '0') {
					readonly = false;
					disabled = true;
				}

				var targetClass = '';
				var targetDataValidate = '';
				var targetMaxLength = '';
				var targetReadOnly = '';
				var targetDisabled = '';
				var targetStyle = '';
				var targetPattern = '';

				if (className) {
					if (displayMode == '0') {
						targetClass = 'class="' + className + ' displayReadonly" ';
					} else {
						targetClass = 'class="' + className + '" ';
					}
				} else if (displayMode == '0') {
					targetClass = 'class="displayReadonly" ';
				}

				if (dataValidate) {
					targetDataValidate = "data-validate=\'" + dataValidate + "\' ";

					if (option['maxlength']) {
						targetMaxLength = 'maxlength="' + option['maxlength'] + '" ';
					}
					if (option['pattern']) {
						targetPattern = option['pattern'];
					}
				}
				if (readonly || dataRole == 'numbering') {
					targetReadOnly = 'readonly="readonly" ';
				}
				if (disabled) {
					targetDisabled = 'disabled="disabled" ';
				}

				if (style) {
					targetStyle = 'style="' + style + '" ';
				}

				var html = '';

				// テキスト
				if (dataRole == 'text' || dataRole == 'numbering') {

					// テキストエリア
					if (rows) {
						// 値
						var targetVal = '';
						if (entityObj[dataField]) {
							targetVal = entityObj[dataField];
						}
						html = '<textarea rows="' + rows + '" ' + targetClass + targetDataValidate + targetMaxLength + ' data-role="' + dataRole + '" ' + targetReadOnly + targetDisabled + targetStyle + '>' + targetVal + '</textarea>';
						dataRole = 'textarea';

					// テキスト
					} else {
						// 値
						var targetVal = '';
						if (entityObj[dataField]) {
							targetVal = 'value="' + entityObj[dataField] + '" ';
						}
						html = '<input type="text" ' + targetClass + targetDataValidate + targetMaxLength + ' data-role="' + dataRole + '" ' + targetReadOnly + targetDisabled + targetVal + targetStyle + '/>';

						if (displayMode != '0' && option && option['pattern'] == 'date' && targetClass && targetClass.indexOf('ymdPicker') != -1) {
							html += '<i class="glyphicon glyphicon-calendar form-control-feedback"></i>';
						}
					}
					$targetObj.html(html);

				// ラジオボタン
				} else if (dataRole == 'radioCode') {
					// 値
					var targetVal = '';
					if (entityObj[dataField]) {
						targetVal = entityObj[dataField];
					}
					html = MstCommon.getRadioTag(dataField, listObj[listName], targetVal, disabled, gridIndex, targetStyle);
					$targetObj.html(html);

				// ドロップダウン
				} else if (dataRole == 'dropdownCode') {
					// 値
					var targetVal = '';
					if (entityObj[dataField]) {
						targetVal = entityObj[dataField];
					}
					html = '<select ' + targetStyle + '></select>';
					if (displayMode == '0') {
						var dispVal = MstCommon.getDropdownDispValue(listObj[listName], targetVal);
						html = '<span class="form-control displayReadonly" ' + targetStyle + '>' + dispVal + '</span>';
						$targetObj.html(html);

					} else {
						$targetObj.html(html);

						NCI.createOptionTags($(' select', $targetObj), listObj[listName]).val(targetVal);

						if (disabled) {
							$(' select', $targetObj).attr('disabled','disabled');

						} else if (readonly) {
							$(' select>option', $targetObj).attr('readonly','readonly');
						}
					}

				// チェックボックス
				} else if (dataRole == 'check') {
					// 値
					var targetVal = '';
					if (entityObj[dataField] == '1') {
						targetVal = 'checked="checked" ';
					}

					var targetLabel = '';
					var checkLabel = parts['checkLabel'];
					if (checkLabel) {
						targetLabel = checkLabel;
					}

					html = '<label class="form-control-static"><input type="checkbox" data-role="check" ' + targetVal + ' ' + targetDisabled + targetStyle + '/>' + targetLabel + '</label>';

					$targetObj.html(html);

				// 隠しフィールド
				} else if (dataRole == 'hidden') {
					// 値
					var targetVal = '';
					if (entityObj[dataField]) {
						targetVal = entityObj[dataField];
					}
					$targetObj.val(targetVal);
				}

				if (!MstCommon.partsElements[dataField]) {
					MstCommon.partsElements[dataField] = new Array();
				}

				MstCommon.partsElements[dataField].push({'element':$targetObj, 'dataRole':dataRole, 'pattern':targetPattern});
			}
		});
	},

	// ラジオボタン生成
	getRadioTag : function (dataField, list, value, disabled, gridIndex, targetStyle) {
		var sql = '<div class="form-control-static">';
		$.each(list, function(i, item) {
			var checked = '';
			if (item.value == value) {
				checked = 'checked';
			}

			var targetName = '';

			if (gridIndex != -1) {
				targetName = 'name="' + dataField + '-' + (gridIndex+1) + '" ';
			} else {
				targetName = 'name="' + dataField + '" ';
			}

			sql +=
				'<label class="radio-inline">' +
				' <input type="radio" ' + targetName + 'data-field="' + dataField + '" value="' + item.value + '" data-role="radioCode" ' + checked + ' ' + (disabled ? 'disabled' : '') + ' ' + targetStyle + '/>' +
				' <span>' + item.label + '</span>' +
				'</label>';

		});
		sql += '</div>';
		return sql;
	},

	// 値取得
	getValue : function (dataField, gridIndex) {

		if (MstCommon.partsElements[dataField]) {
			var objArray = MstCommon.partsElements[dataField];
			var $targetObj = null;

			if (gridIndex != -1 && gridIndex != undefined) {
				$targetObj = objArray[gridIndex];
			} else {
				$targetObj = objArray[0];
			}

			if ($targetObj) {
				var $targetElement = $targetObj['element'];
				var dataRole = $targetObj['dataRole'];
				var pattern = $targetObj['pattern'];

				// テキスト
				if (dataRole == 'text' || dataRole == 'numbering') {
					if (pattern == 'integer') {
						var val = $('>input', $targetElement).val();
						return val.replace(/,/g, '');

					} else if (pattern == 'ym') {
						var val = $('>input', $targetElement).val();
						return val.replace('/', '');

					} else {
						return $('>input', $targetElement).val();
					}

				// テキストエリア
				} else if (dataRole == 'textarea') {
					return $('>textarea', $targetElement).val();

				// ラジオボタン
				} else if (dataRole == 'radioCode') {
					return MstCommon.getRadioValue($targetElement);

				// ドロップダウン
				} else if (dataRole == 'dropdownCode') {
					if (displayMode == '1') {
						return $('>select', $targetElement).val();
					} else {
						return $('>span', $targetElement).html();
					}

				// チェックボックス
				} else if (dataRole == 'check') {
					return $(' input', $targetElement).get(0).checked ? '1' : '0';

				// 隠しフィールド
				} else if (dataRole == 'hidden') {
					return $targetElement.val();
				}
			}
		}
		return '';
	},

	// ラジオボタンの値取得
	getRadioValue : function ($targetObj) {
		if ($(' input', $targetObj).size() != 0) {
			for (var i=0; i<$(' input', $targetObj).size(); i++) {
				var item = $(' input', $targetObj).get(i);
				if (item.checked) {
					return $(item).val();
				}
			}
		}
		return '';
	},

	// 値設定
	setValue : function (dataField, value, gridIndex) {

		if (MstCommon.partsElements[dataField]) {
			var objArray = MstCommon.partsElements[dataField];
			var $targetObj = null;

			if (gridIndex != -1 && gridIndex != undefined) {
				$targetObj = objArray[gridIndex];
			} else {
				$targetObj = objArray[0];
			}

			if ($targetObj) {
				var $targetElement = $targetObj['element'];
				var dataRole = $targetObj['dataRole'];
				var pattern = $targetObj['pattern'];

				// テキスト
				if (dataRole == 'text' || dataRole == 'numbering') {
					if (pattern == 'integer') {
						$('>input', $targetElement).val(value);
						$('>input', $targetElement).blur();

					} else if (pattern == 'ym') {
						if (value && value.length > 4) {
							value = value.substring(0, 4) + '/' + value.substring(4);
						}
						$('>input', $targetElement).val(value);

					} else {
						$('>input', $targetElement).val(value);
					}

				// テキストエリア
				} else if (dataRole == 'textarea') {
					$('>textarea', $targetElement).val(value);

				// ラジオボタン
				} else if (dataRole == 'radioCode') {
					return MstCommon.setRadioValue($targetElement, value);

				// ドロップダウン
				} else if (dataRole == 'dropdownCode') {

					if (displayMode == '1') {
						$('>select', $targetElement).val(value);
					} else {
						$('>span', $targetElement).html(value);
					}

				// チェックボックス
				} else if (dataRole == 'check') {
					$(' input', $targetElement).get(0).checked = (value == '1');

				// 隠しフィールド
				} else if (dataRole == 'hidden') {
					$targetElement.val(value);
				}
				return $targetElement;
			}
		}
		return null;
	},

	// ラジオボタンの値設定
	setRadioValue : function ($targetObj, value) {
		if ($(' input', $targetObj).size() != 0) {
			for (var i=0; i<$(' input', $targetObj).size(); i++) {
				var item = $(' input', $targetObj).get(i);
				item.checked = $(item).val() == value;
			}
		}
	},

	// ドロップダウン名称取得
	getDropdownDispValue : function(list, value) {
		if (list && list.length > 0) {
			for (var i=0; i<list.length; i++) {
				var item = list[i];
				if (item.value == value) {
					return item.label;
				}
			}
		}
		return '';
	},

	// 年月フォーマット
	formatYearMonth : function (id) {
		var val = $('#' + id).val();

		if (val && val.indexOf('/') == -1 && val.length > 4) {
			var left = val.substring(0, 4);
			var right = val.substring(4);

			if (right.length == 1) {
				right = '0' + right;
			}

			$('#' + id).val(left + '/' + right);
		}
	}
};