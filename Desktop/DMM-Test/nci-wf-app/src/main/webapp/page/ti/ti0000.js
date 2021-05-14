

$(function() {
	// (tableSearchId) || (tableName && tableSearchCode) が必須パラメータ
	let initParams = NCI.flushScope(NCI.getQueryString(FLUSH_SCOPE_KEY));
	let params = {
		"tableSearchId" : initParams.tableSearchId,
		"tableName" : initParams.tableName,
		"corporationCode" : initParams.corporationCode,
		"tableSearchCode" : initParams.tableSearchCode,
		"initConditions" : initParams.initConditions
	};
	let pager = new Pager($('#seach-result'), '/ti0000/search', search).init();
	pager.responsiveTable.modifyTR = function($tr, rowIndex, entity, labels, ignores) {
		$tr.find('input[type=radio]').prop('disabled', false);
	};
	NCI.init('/ti0000/init', params).done(function(res) {
		if (res && res.success) {
			init(res);

			if (res.initSearch) {
				search();
			}

			// 検索ボタン押下
			$('#btnSearch').click(function(ev) { search(1); });
			// リセットボタン押下
			$('#btnReset').click(function(ev) { $('#formCondition')[0].reset(); });
			// 選択ボタン押下
			$('#btnSelect').click(execSelect);

			// ページ番号リンク押下
			$(document).on('click', 'ul.pagination a', function() {
				var pageNo = this.getAttribute('data-pageNo');
				search(pageNo);
				return false;
			})
			// 検索結果の選択ラジオボタン
			.on('change', 'input[type=radio][name=rdoSelect]', function() {
				$('#btnSelect').prop('disabled', false);
			});
		}
		// 閉じるボタン
		$('#btnClose, #btnClose2').click(function() { Popup.close(); });
	});

	// 検索実行
	function search(pageNo) {
		var $targets = $('#formCondition').find('input, select');
		if (!Validator.validate($targets, true)) {
			return false;
		}
		var cond = createCondition(pageNo);
		pager.search(cond).done(function() {
			// 3桁カンマ区切り
			$('#seach-result tbody tr').find('.dataType-number').each(function(i, elem) {
				elem.textContent = NCI.addComma(elem.textContent);
			});
		});
		$('#btnSelect').prop('disabled', true);
		$('#seach-result').removeClass('invisible');
	}

	// 画面入力内容から検索条件を生成
	function createCondition(pageNo) {
		var cond = pager.createCondition(null, pageNo);
		cond.tableSearchId = ti0000.tableSearchId;
		cond.tableName = ti0000.tableName;
		cond.conditionDefs = ti0000.conditionDefs;
		cond.resultDefs = ti0000.resultDefs;
		cond.conditions = NCI.toObjFromElements($('#formCondition'));
		// デフォルトソート条件
		cond.defaultSortColumns = ti0000.defaultSortColumns;
		cond.defaultSortDirections = ti0000.defaultSortDirections;

		return cond;
	}

	// 選択ボタン押下
	function execSelect(ev) {
		var $tr = $('input[name=rdoSelect]:checked').closest('tr');
		var entity = {};
		$tr.find('[data-field]').each(function(i, elem) {
			let colName = elem.getAttribute('data-field');
			entity[colName] = $(elem).text();
		});
		entity.htmlId = initParams.htmlId;

		// コールバック関数の呼び出し
		Popup.close(entity);
	}

	// 初期化
	function init(res) {
		// レスポンスを保存
		$.each(ti0000, function(prop, value) {
			if (prop in res) ti0000[prop] = res[prop];
		});

		// 検索条件の初期化
		ti0000.initCondition();
		// 検索結果のテンプレートを初期化
		ti0000.initResultTemplate();

		// カレンダー（年月日）
		NCI.ymdPicker($('input.ymdPicker'));
	}
});

var ti0000 = {
	// 汎用テーブル検索条件ID
	tableSearchId : null,
	// テーブル名
	tableName : null,
	// 初期検索の有無
	initSearch : null,
	// 検索条件カラム
	conditionDefs : null,
	// 検索結果カラム
	resultDefs : null,
	// デフォルトソートカラム
	defaultSortColumns : null,
	// デフォルトソート方向
	defaultSortDirections : null,
	// プライマリキーのカラム名
	pkColumnNames : null,

	// 検索条件の初期化
	initCondition : function() {
		const DATE = "D";
		const DISPLAY_TEXTBOX = "1", DISPLAY_DROPDOWN = "2", HIDDEN = "3";
		const RANGE = "4";
		const $formCondition = $('#formCondition');
		const $tableBox = $('#tableBox').empty();
		const th = document.createElement('div');
		th.className = "th col-sm-2";
		const td = document.createElement('div');
		td.className = "td col-sm-4";
		let $tr = null;
		const len = ti0000.conditionDefs.length;
		for (let i = 0; i < len; i++) {
			const c = ti0000.conditionDefs[i];

			// 検索条件2つごとに改行
			if (i % 2 === 0) {
				$tr = $('<div class="tr">').appendTo($tableBox);
			}

			// TH
			$(th.cloneNode(false))
				.toggleClass("hide", c.displayType === HIDDEN)
				.text(c.displayName)
				.appendTo($tr);
			// TD
			const $td = $(td.cloneNode(false))
				.toggleClass("hide", c.displayType === HIDDEN)
				.appendTo($tr);

			if (c.displayType == DISPLAY_TEXTBOX || c.displayType == HIDDEN) {
				// 範囲テキストボックス
				if (c.matchType == RANGE) {
					$(document.getElementById('cond-rangeText').cloneNode(true))
						.removeAttr('id')
						.appendTo($td);
					// テキストボックスFROM
					$td.find('input.form-control.from')
						.attr("id", c.columnName + "_FROM")
						.attr("data-validate", ti0000.getDataValidate(c, true))
						.attr("value", c.initValue1)
						.toggleClass("ymdPicker", c.columnType == DATE)
						.toggleClass("required", c.required);
					// テキストボックスTO
					$td.find('input.form-control.to')
						.attr("id", c.columnName + "_TO")
						.attr("data-validate", ti0000.getDataValidate(c))
						.attr("value", c.initValue2)
						.toggleClass("ymdPicker", c.columnType == DATE)
						.toggleClass("required", c.required);
				}
				// 単一テキストボックス
				else {
					$(document.getElementById('cond-singleText').cloneNode(true))
						.removeAttr('id')
						.appendTo($td);
					// テキストボックス
					$td.find('input.form-control')
						.attr("id", c.columnName)
						.attr("data-validate", ti0000.getDataValidate(c))
						.attr("value", c.initValue1)
						.toggleClass("ymdPicker", c.columnType == DATE)
						.toggleClass("required", c.required);
				}
			}
			// ドロップダウンリスト
			else if (c.displayType == DISPLAY_DROPDOWN) {
				// 範囲ドロップダウン
				if (c.matchType == RANGE) {
					$(document.getElementById('cond-rangeDropdown').cloneNode(true))
						.removeAttr('id')
						.appendTo($td);
					// ドロップダウンリストFROM
					let $ddl = $td.find('select.form-control.from')
						.attr("id", c.columnName + "_FROM")
						.toggleClass("required", c.required);
					NCI.createOptionTags($ddl, c.options).val(c.initValue1);
					// ドロップダウンリストTO
					$ddl = $td.find('select.form-control.to')
						.attr("id", c.columnName + "_TO")
						.toggleClass("required", c.required);
					NCI.createOptionTags($ddl, c.options).val(c.initValue2);
				}
				// 単一ドロップダウン
				else {
					$(document.getElementById('cond-singleDropdown').cloneNode(true))
						.removeAttr('id')
						.appendTo($td);
					// ドロップダウンリスト
					let $ddl = $td.find('select.form-control')
						.attr("id", c.columnName)
						.toggleClass("required", c.required);
					NCI.createOptionTags($ddl, c.options).val(c.initValue1);
				}
			}
		}
	},

	// data-validate属性用のJSONオブジェクトを生成
	getDataValidate : function(c, isRange) {
		const NUMBER = "N", DATE = "D", RANGE = "4";
		let validate = null;
		if (c.columnType == NUMBER) {
			validate = {
				"pattern" : "numeric",
				"min" : -9999999999,
				"max" : 9999999999
			};
		}
		else if (c.columnType == DATE) {
			validate = { "pattern" : "date" };
			if (c.matchType == RANGE) {
				validate.to = c.columnName + "_TO";
				validate.from = c.columnName + "_FROM";
			}
		}
		if (validate == null)
			return null;
		return JSON.stringify(validate);
	},

	// 検索結果のテンプレートを初期化
	initResultTemplate : function() {
		const $tblResult = $('#tblResult');
		const $colgroup = $tblResult.find('>colgroup');
		const $header = $tblResult.find('>thead>tr');
		const $template = $tblResult.find('>tfoot.template>tr');
		const len = ti0000.resultDefs.length;
		let th = document.createElement("th");
		let sortLink = document.createElement("a");
		th.appendChild(sortLink);
		let td = document.createElement("td");
		let span = document.createElement("span");
		let col = document.createElement("col");

		const DISPLAY = "1", HIDDEN = "2";
		for (let i = 0; i < len; i++) {
			const r = ti0000.resultDefs[i];

			// 列幅
			$(col.cloneNode(true))
				.toggleClass("col-sm-" + r.displayWidth, r.displayWidth != null)
				.toggleClass("hide", r.displayType == HIDDEN)
				.appendTo($colgroup);

			// ヘッダ
			const $th = $(th.cloneNode(true))
				.toggleClass("hide", r.displayType == HIDDEN)
				.appendTo($header);
			$th.find("a")
				.text(r.displayName)
				.attr("data-sort", r.sortColumnName);

			// テンプレート
			const $td = $(td.cloneNode(true)).toggleClass("hide", r.displayType == HIDDEN);

			// classのtext-left,center,rightはoriginal.cssで上書きされてしまうため、styleに対してalignを指定する
			if (r.alignType === "L") {
				$td.css('text-align', 'left');
			}
			if (r.alignType === "C") {
				$td.css('text-align', 'center');
			}
			if (r.alignType === "R") {
				$td.css('text-align', 'right');
			}

			// dataType-numberを追加することでNCI.jsの中で値を3桁区切りに設定している。
			// IDのみ数値でも3桁区切りの対象外とする
			$(span.cloneNode(true))
				.attr("data-field", r.columnName)
				.text("#{" + r.displayName + "}")
				.toggleClass("dataType-number", r.columnType === "N" && r.columnName !== "ID" && r.displayType != HIDDEN)
				.appendTo($td);
			$td.appendTo($template);
		}
	}
};
