$(function() {
	// 検索条件（一致区分に合わせてテンプレート生成内容を切り替えられるよう、デフォルト実装を置換）
	const conditionsTable = new ResponsiveTable($('#conditions'));
	conditionsTable.modifyTR = modifyTR;
	// 検索結果
	const resultsTable = new ResponsiveTable($('#results'));
	resultsTable.modifyTR = function($tr, rowIndex, entity, labels, ignores) {
		$tr.find('input[type=radio]').prop('disabled', false);
	};

	const params = {
			"docTrayConfigId": NCI.getQueryString('docTrayConfigId'),
			"version": NCI.getQueryString('version'),
			"from": NCI.getQueryString('from'),
			"messageCds" : ['trayDisplayConfig', 'MSG0071', 'MSG0072', 'MSG0229', 'WL0030', 'WL0032', 'sendback', 'pullback', 'pullforward']
	};
	NCI.init('/dc0220/init', params).done(function(res) {
		if (res && res.success) {
			bindEvent();

			$('#tray-template')
				.html(res.trayTemplateHtml)
				.find('[data-i18n]').each(function(i, elem) {
					// 多言語対応
					$(elem).text(NCI.getMessage(elem.getAttribute('data-i18n')));
				});

			// テンプレートを読み終わったので、初期化を開始
			init(res);
		}
		// 戻るボタン
		$('#btnBack').click(doBack);
		// イベント定義
		$(document)
			.on('click', '.btnSelectUser', TrayHelper.userLink_onClick)
			.on('click', '.btnClearUser', TrayHelper.userLink_clear)
			.on('click', '.btnSelectOrg', TrayHelper.orgLink_onClick)
			.on('click', '.btnClearOrg', TrayHelper.orgLink_clear)
			.on('click', '.btnSelectPost', TrayHelper.postLink_onClick)
			.on('click', '.btnClearPost', TrayHelper.postLink_clear)
			.on('click', '.btnSelectCorporation', TrayHelper.corporationLink_onClick)
			.on('click', '.btnClearCorporation', TrayHelper.corporationLink_clear)
			;
	});

	function init(res) {
		// ページあたりの表示条件
		NCI.createOptionTags($('#pageSize'), PAGE_SIZE_ARRAY);
		// 検索条件一致区分の選択肢
		NCI.createOptionTags($('select.conditionMatchType'), res.conditionMatchTypes);
		// コンテンツ種別（文書管理用）の選択肢
		NCI.createOptionTags($('select.contentsTypes'), res.contentsTypes);
		// 公開／非公開の選択肢
		NCI.createOptionTags($('select.publishFlags'), res.publishFlags);
		// 保存期間区分の選択肢
		NCI.createOptionTags($('select.retentionTermTypes'), res.retentionTermTypes);
		// 列幅の選択肢
		NCI.createOptionTags($('select.colWidth'), [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12]);
		// 表示位置揃えの選択肢
		NCI.createOptionTags($('select.alignType'), res.alignTypeOptions);

		// 文書業務管理項目名一覧ダイアログ
		new ResponsiveTable($('#fieldList')).fillTable(res.docBusinessInfoNames);

		// 現在選択されているトレイ表示条件設定の内容を反映
		fillTrayConfig(res);
	}

	function bindEvent() {
		// 検索条件
		$('#conditions').on('change', 'select[data-field=conditionMatchType]', change_matchType);
		// 検索条件の追加／削除ボタン
		$('#btnEditCondition').click(function(){ openFieldList('#table-conditions'); });
		$('#btnCallbackCondition').click(function(){ callbackFieldList('#table-conditions'); });
		// 検索結果の追加／削除ボタン
		$('#btnEditResult').click(function(){ openFieldList('#table-results'); });
		$('#btnCallbackResult').click(function(){ callbackFieldList('#table-results'); });
		// 更新／削除ボタン
		$('#btnUpdate').click(saveTrayConfig).prop('disabled', false);
		$('#btnDelete').click(deleteTrayConfig);
		// 全選択／全解除
		$('#btnSelectAll').click(function() { selectAll(true); });
		$('#btnCancelAll').click(function() { selectAll(false); });
		// 行のソート
		$('#table-conditions>tbody').sortable({"update" : function() { resetSortOrder('#table-conditions'); }});
		$('#table-results>tbody').sortable({"update" : function() { resetSortOrder('#table-results'); }});
	}

	/** 抽出結果を画面へ反映 */
	function fillTrayConfig(res) {
		// トレイ表示項目設定のヘッダ部
		NCI.toElementsFromObj(res.entity, $('#entity'));
		// 検索条件テーブル
		conditionsTable.fillTable(res.conditions);
		// 検索結果テーブル
		resultsTable.fillTable(res.results);

		// 新規レコード or システムフラグONなら削除不可
		const newRecord = (!res.entity || !res.entity.docTrayConfigId);
		const systemFlag = (res.entity && res.entity.systemFlag === '1');
		$('#btnDelete').prop('disabled', (newRecord || systemFlag));

		$('#docTrayConfigCode').prop('disabled', !!res.entity.docTrayConfigId);
	}

	/** 検索条件　一致区分をクリア */
	function change_matchType(ev) {
		// 既存エラーをクリア
		const $tr = $(ev.target).closest('tr');
		const $targets = $tr.find('input, select');
		Validator.hideBalloon($targets);

		// 現在の値を取得して、その内容で再レンダリング
		const entity = NCI.toObjFromElements($tr);
		conditionsTable.fillRowResult($tr, null, entity, conditionsTable.getHeaderLabels());
	}

	/** 業務管理項目一覧を開く */
	function openFieldList(tableId) {
		// 追加ボタン
		$('#btnCallbackCondition').toggle(tableId === '#table-conditions');
		$('#btnCallbackResult').toggle(tableId !== '#table-conditions');

		// 選択中の業務管理項目名を求めて、業務管理項目一覧へ反映
		let docBusinessInfoCodes = {};
		$(tableId).find('>tbody span[data-field=docBusinessInfoCode]').each(function(i, elem) {
			docBusinessInfoCodes[$(elem).text()] = 1;
		});
		$('#table-fieldList').find('>tbody input[data-field=docBusinessInfoCode]').each(function(i, elem) {
			elem.checked = (elem.value in docBusinessInfoCodes);
		});
		// 業務管理項目一覧を開く
		$('#fieldList').modal();
	}

	/** 業務管理項目一覧からのコールバック関数。選択内容を検索条件／検索結果へ反映する */
	function callbackFieldList(tableId) {
		Validator.hideBalloon();

		// 使用中の検索条件を求める
		let newIds = {};
		$('#table-fieldList>tbody input[data-field=docBusinessInfoCode]:checked').each(function(i, elem) {
			const docBusinessInfoCode = elem.value;
			newIds[docBusinessInfoCode] = NCI.toObjFromElements($(elem).closest('tr'));
		});
		// 不要な行を削除
		const $table = $(tableId);
		$table.find('>tbody span[data-field=docBusinessInfoCode]').each(function(i, elem) {
			const docBusinessInfoCode = $(elem).text();
			if (docBusinessInfoCode in newIds) {
				// 既存IDが「使用中の検索条件」にあれば、新規リストから消し込む
				delete newIds[docBusinessInfoCode];
			} else {
				// 既存IDが「使用中の検索条件」になければ、既存行を削除
				$(elem).closest('tr').remove();
			}
		});
		// 「使用中の検索条件」の残余が新規に選択されたもの
		const tbl = (tableId === '#table-conditions') ? conditionsTable : resultsTable;
		const defaults = { "colWidth" : 150, "conditionMatchType" : "1", "alignType" : "L" };
		for (let docBusinessInfoCode in newIds) {
			const entity = $.extend({}, defaults, newIds[docBusinessInfoCode]);
			tbl.addRowResult(entity);
		}
		tbl.dispNoRecordMessage();

		resetSortOrder(tableId);
	}

	/** 業務管理項目ごとにテンプレートを書き変えるメソッド（ResponsiveTableのデフォルト実装を上書きする想定）  */
	function modifyTR($tr, rowIndex, c, labels, ignores) {
		// いくつかの業務管理項目は固有の入力方法しかない
		const $td = $tr.find('td.contents').empty();
		const $template = $('#tray-template');
		const self = this, RANGE = "4", FULL_MATCH = "1";
		const docBusinessInfoCode = c.docBusinessInfoCode, type = c.conditionMatchType;
		const id = NCI.toCamelCase(docBusinessInfoCode);

		let $article = null;
		if ('CONTENTS_TYPE' === docBusinessInfoCode) {
			// コンテンツ種別（文書管理用）
			$article = $($template.find('article.template-contentsType')[0].cloneNode(true));
			$article.find('select.form-control').attr('data-field', 'trayInitValue1');
		}
		else if ('PUBLISH_FLAG' === docBusinessInfoCode) {
			// 公開／非公開の選択肢
			$article = $($template.find('article.template-publishFlag')[0].cloneNode(true));
			$article.find('select.form-control').attr('data-field', 'trayInitValue1');
		}
		else if ('RETENTION_TERM_TYPE' === docBusinessInfoCode) {
			// 保存期間区分の選択肢
			$article = $($template.find('article.template-retentionTermType')[0].cloneNode(true));
			$article.find('select.form-control').attr('data-field', 'trayInitValue1');
		}
		else if (docBusinessInfoCode.indexOf('CORPORATION_CODE') >= 0) {
			// 企業選択
			$article = $($template.find('article.template-corporation')[0].cloneNode(true));
			$article.find('input.corporationCode').attr('data-field', 'trayInitValue1');
		}
		else if (docBusinessInfoCode.indexOf('USER_CODE') >= 0) {
			// ユーザ選択
			$article = $($template.find('article.template-user')[0].cloneNode(true));
			$article.find('input.userCode').attr('data-field', 'trayInitValue1');
		}
		else if (docBusinessInfoCode.indexOf('ORGANIZATION_CODE') >= 0) {
			// 組織選択
			$article = $($template.find('article.template-org')[0].cloneNode(true));
			$article.find('input.organizationCode').attr('data-field', 'trayInitValue1');
		}
		else if (docBusinessInfoCode.indexOf('POST_CODE') >= 0) {
			// 役職選択
			$article = $($template.find('article.template-post')[0].cloneNode(true));
			$article.find('input.postCode').attr('data-field', 'trayInitValue1');
		}
		else if (docBusinessInfoCode === 'PROXY_USER') {
			// 代理者
			$article = $($template.find('article.template-proxyUser')[0].cloneNode(true));
			$article.find('select.form-control').attr('data-field', 'trayInitValue1');
		}
		else if (BusinessInfoCodes.FLAGS.indexOf(docBusinessInfoCode) >= 0) {
			// フラグ(0:OFF、1:ON)
			$article = $($template.find('article.template-flag')[0].cloneNode(true));
			$article.find('input[type=checkbox]').attr('data-field', 'trayInitValue1');
		}
		else if (BusinessInfoCodes.STATUSES.indexOf(docBusinessInfoCode) >= 0) {
			// ステータス(1:OFF、2:ON)
			$article = $($template.find('article.template-status')[0].cloneNode(true));
			$article.find('input[type=checkbox]').attr('data-field', 'trayInitValue1');
		}
		else if (RANGE === type){
			// 範囲
			$article = $($template.find('article.template-range')[0].cloneNode(true));
			const $from = $article.find('input.from').attr('data-field', 'trayInitValue1');
			const $to = $article.find('input.to').attr('data-field', 'trayInitValue2');
			if (BusinessInfoCodes.DATES.indexOf(docBusinessInfoCode) >= 0) {
				// 日付範囲
				$from.addClass('ymdPicker').attr('data-validate', JSON.stringify({ "pattern" : "date" }))
					.parent().append('<i class="glyphicon glyphicon-calendar form-control-feedback"></i>');
				NCI.ymdPicker($from);
				$to.addClass('ymdPicker').attr('data-validate', JSON.stringify({ "pattern" : "date" }))
					.parent().append('<i class="glyphicon glyphicon-calendar form-control-feedback"></i>');
				NCI.ymdPicker($to);
			}
			else if (BusinessInfoCodes.NUMERICS.indexOf(docBusinessInfoCode) >= 0) {
				// 数値範囲
				$from.attr('data-validate', JSON.stringify({ "pattern" : "numeric" }));
				$to.attr('data-validate', JSON.stringify({ "pattern" : "numeric" }));
			}
		}
		else {
			// その他は一致区分ごとに表示を切り替え
			$article = $($template.find('article.template-normal')[0].cloneNode(true));
			const $input = $article.find('input').attr('data-field', 'trayInitValue1');

			if (BusinessInfoCodes.DATES.indexOf(docBusinessInfoCode) >= 0) {
				// 日付範囲
				$input.addClass('ymdPicker').attr('data-validate', JSON.stringify({ "pattern" : "date" }))
					.parent().append('<i class="glyphicon glyphicon-calendar form-control-feedback"></i>');
				NCI.ymdPicker($input);
			}
			else if (BusinessInfoCodes.NUMERICS.indexOf(docBusinessInfoCode) >= 0) {
				// 数値範囲
				$input.attr('data-validate', JSON.stringify({ "pattern" : "numeric" }));
			}
		}
		if (BusinessInfoCodes.FULL_MATCHES.indexOf(docBusinessInfoCode) >= 0) {
			$tr.find('select.conditionMatchType>option').each(function(i, option) {
				if (FULL_MATCH != option.value) {
					$(option).remove();
				}
			});
		}

		return $td.append($article);
	}

	/** ソート順を画面でレイアウトされている順番で書き換え */
	function resetSortOrder(tableId) {
		$(tableId).find('>tbody span[data-field=sortOrder]').each(function(i, elem) {
			$(elem).text((i + 1) + '');
		});
	}

	/** 更新処理 */
	function saveTrayConfig(ev) {
		if (!Validator.validate($('input, select'), true))
			return false;

		let msg = NCI.getMessage("MSG0071", NCI.getMessage("trayDisplayConfig"));
		if (NCI.confirm(msg, function() {
			const params = {
					entity : NCI.toObjFromElements($('#entity')),
					conditions : NCI.toArrayFromTable($('#conditions'), ['docBusinessInfoName']),
					results : NCI.toArrayFromTable($('#results'), ['docBusinessInfoName'])
			};
			params.entity.docTrayConfigId = $('#docTrayConfigId').val();
			NCI.post('/dc0220/save', params).done(function(res) {
				if (res && res.success) {
					fillTrayConfig(res);
				}
			});
		}));
	}

	/** 削除処理 */
	function deleteTrayConfig(ev) {
		if (!Validator.validate($('input, select'), true))
			return false;

		let msg = NCI.getMessage("MSG0072", NCI.getMessage("trayDisplayConfig"));
		if (NCI.confirm(msg, function() {
			const params = {
					entity : NCI.toObjFromElements($('#entity'))
			};
			params.entity.docTrayConfigId = $('#docTrayConfigId').val();
			NCI.post('/dc0220/delete', params).done(function(res) {
				if (res && res.success) {
					doBack();
				}
			});
		}));
	}

	function selectAll(checked) {
		$('#table-fieldList').find('input[data-field=docBusinessInfoCode]').prop('checked', checked);
	}

	function doBack(ev) {
		const screenId = NCI.getQueryString('from');
		NCI.redirect('./' + screenId + '.html');
	}
});
