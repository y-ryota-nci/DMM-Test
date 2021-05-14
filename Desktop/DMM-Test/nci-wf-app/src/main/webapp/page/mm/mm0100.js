$(function() {
	/** 検索実行 */
	var search = function(pageNo, keepMessage) {
		var cond = createCondition(pageNo);
		pager.search(cond, keepMessage);
		$('#btnDelete').prop('disabled', true);
		$('#searchResult').removeClass('hide');
	};

	/** 画面入力内容から検索条件を生成 */
	var createCondition = function(pageNo) {
		var $elements = $('div.searchConditionArea').find('input');
		var cond = pager.createCondition($elements, pageNo);
		// デフォルトソート条件
		if (!cond.sortColumn) {
			cond.sortColumn = 'ASSIGN_ROLE_CODE, SEQ_NO_INFO_SHARER_DEF';
			cond.sortAsc = true;
		}
		return cond;
	};

	var processDef = null;
	var pager = new Pager($('#searchResult'), '/mm0100/search', search).init();
	var informationSharerDefs = null;
	var $editInformationSharerDef = $('#editInformationSharerDef');

	// PagerのshowSearchResultをラップ
	pager.showSearchResult = function(res) {
		informationSharerDefs = res.informationSharerDefs;
		// 総行数・ページ数・ページ番号・ページあたりの行数
		this.fillPageInfo(res);
		// ページリンク
		this.fillPageLink(res);
		// テーブルへ検索結果を反映
		let results = res[this.resultPropName];
		this.fillTable(results);
		// テーブルのヘッダ列にソート機能を付与
		this.fillSortLink(res);
	};

	var corporationCode = NCI.getQueryString('corporationCode');
	var processDefCode = NCI.getQueryString('processDefCode');
	var processDefDetailCode = NCI.getQueryString('processDefDetailCode');

	let params = {
		corporationCode: corporationCode,
		processDefCode: processDefCode,
		processDefDetailCode: processDefDetailCode,
		messageCds : ['informationSharingPerson']
	};

	var init = function(res) {
		if (res.processDef) {
			$('input, select, textarea, span', 'div.searchConditionArea').filter('[data-field]').each(function(i, elem) {
				var val = res.processDef[$(elem).attr('data-field')];
				var tagName = elem.tagName, type = elem.type;
				if (tagName === 'INPUT' || tagName === 'SELECT' || tagName === 'TEXTAREA') {
					elem.value = val;
				} else {
					$(elem).text(val);
				}
			});
			processDef = res.processDef;
		}

		// 検索結果反映
		search(1);
		var checked = $('#tblInformationSharerDefs tbody input[type=checkbox]:checked').length === 0;
		$('#btnUpdate,#btnRemove').prop('disabled', checked);
	};

	NCI.init('/mm0100/init', params).done(function(res) {
		if (res && res.success) {
			NCI.createOptionTags($('#assignRoleCode'), res.assignRoles);
			NCI.createOptionTags($('#expressionDefCode'), res.expressionDefs);
			NCI.createOptionTags($('#expressionInfoSharerType'), res.expressionInfoSharerTypes);
			NCI.createOptionTags($('#informationSharerType'), res.informationSharerTypes);

			init(res);

			$(document)
			.on('change', '#tblInformationSharerDefs tbody input[type=checkbox]', function() {
				var checked = $('#tblInformationSharerDefs tbody input[type=checkbox]:checked').length === 0;
				$('#btnRemove').prop('disabled', checked);
			})
			.on('click', 'ul.pagination a', function(ev) {
				// ページ番号リンク押下
				var pageNo = this.getAttribute('data-pageNo');
				search(pageNo);
				return false;
			})
			// 検索結果のリンク押下
			.on('click', 'a.lnkOpenEdit', function(ev) {
				var $tr = $(this).closest('tr');
				var uniqueKey = $tr.find('span[data-field=uniqueKey]').text();
				var informationSharerDef = informationSharerDefs[uniqueKey];
				$('#uniqueKey').val(uniqueKey);
				$('#assignRoleCode').val(informationSharerDef.assignRoleCode);
				if (informationSharerDef.expressionDefCode) {
					$('#expressionDefCode').val(informationSharerDef.expressionDefCode);
					$('#expressionInfoSharerType').val(informationSharerDef.expressionInfoSharerType);
					$('#expressionInfoSharerType').closest('div').removeClass('hide');
				} else {
					$('#expressionDefCode').prop('selectedIndex', 0);
					$('#expressionInfoSharerType').prop('selectedIndex', 0);
					$('#expressionInfoSharerType').closest('div').addClass('hide');
				}
				$('#informationSharerType').val(informationSharerDef.informationSharerType);
				$('#displayFlag').prop('checked', informationSharerDef.displayFlag === '1');

				$editInformationSharerDef.modal();
			})
			.on('click', '#btnAdd', function(ev) {
				Popup.open("../mm/mm0101.html", callbackFromMm0101, {processDef: processDef}, this);
			})
			.on('click', '#btnRemove', function(ev) {
				var msg = NCI.getMessage('MSG0072', NCI.getMessage('informationSharingPerson'));
				NCI.confirm(msg, function() {
					var checked = $('#tblInformationSharerDefs tbody input[type=checkbox]:checked');
					var array = [];
					$.each(checked, function(i, e) {
						var $tr = $(e).closest('tr');
						var uniqueKey = $(e).val();
						array.push(informationSharerDefs[uniqueKey]);
					});
					var params = {
						informationSharerDefs: array
					};
					NCI.post('/mm0100/remove', params).done(function(res) {
						if (res && res.success) {
							search(1, true);
						}
					});
				});
			})
			.on('click', '#btnOK', function(e) {
				var uniqueKey = $('#uniqueKey').val();
				var $targets = $editInformationSharerDef.find('input,select');
				if (!Validator.validate($targets, true)) return false;
				var informationSharerDef = $.extend({}, informationSharerDefs[uniqueKey], NCI.toObjFromElements($editInformationSharerDef, []));

				if (!informationSharerDef.expressionDefCode) informationSharerDef.expressionInfoSharerType = '3';
				if (!informationSharerDef.displayFlag) informationSharerDef.displayFlag = '0';

				NCI.post('/mm0100/update', {informationSharerDef: informationSharerDef}).done(function(res) {
					if (res && res.success) {
						search(0, true);
						$editInformationSharerDef.modal('hide');
					}
				});
				return true;
			})
			.on('click', '#btnCancel', function(e) {
				$editInformationSharerDef.modal('hide');
			})
			;
			$editInformationSharerDef.on('hide.bs.modal', function() {
				Validator.hideBalloon();
				$('#assignRoleCode').prop('selectedIndex', 0);
				$('#expressionDefCode').prop('selectedIndex', 0);
				$('#expressionInfoSharerType').prop('selectedIndex', 0);
				$('#informationSharerType').prop('selectedIndex', 0);
			});
		}
	});

	/**
	 * 比較条件式定義作成及び遷移先設定コールバック
	 * ※iframeから呼び出すためグローバル化
	 */
	window.callbackFromMm0101 = function(res) {
		if (res && res.success) {
			NCI.addMessage('success', res.successes);
			search(1, true);
		}
	};

	// 戻るボタン
	$(document)
	.on('click', '#btnBack', function(ev) {
		NCI.redirect("./mm0300.html");
	});

});
