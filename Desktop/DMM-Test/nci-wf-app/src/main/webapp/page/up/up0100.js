$(function() {
	const params = {};
	NCI.init('/up0100/init', params).done(function(res) {
		if (res && res.success) {
			UP0100.init(res);
		}
	});

	$(document)
		// 検索ボタン押下
		.on('click', '#btnSearch', function() { UP0100.search(1); return false; })
		// ページ番号リンク押下
		.on('click', 'ul.pagination a', function() { UP0100.search(this.getAttribute('data-pageNo')); return false; })
		// リセットボタン押下
		.on('click', '#btnReset', function(ev) { $('#formCondition')[0].reset(); })
		// 検索結果のリンク押下
		.on('click', '.hyperlink', UP0100.openDetail)
		// 履歴押下
		.on('click', '.show-hisotry', UP0100.showHistory)
	;
});

var UP0100 = {
	/** 操作者のアクセス可能な画面IDの配列 */
	accessibleScreenIds : [],

	/** テーブルのレンダラー */
	pager : null,

	/** 初期化 */
	init : function(res) {
		UP0100.accessibleScreenIds = res.accessibleScreenIds;
		UP0100.pager = new Pager($('#search-result'), 'up0100/search', UP0100.search).init();
		UP0100.pager.responsiveTable.modifyTR = function($tr, rowIndex, entity, labels, ignores) {
			// 操作者のアクセス可能な画面であれば、リンクにする
			const linkEnable = (UP0100.accessibleScreenIds.indexOf(entity.uploadKind) >= 0);
			$tr.find('.linkable').toggleClass('hyperlink', linkEnable);
		}

		// カレンダー（年月日）
		NCI.ymdPicker($('input.ymdPicker'));

		// 選択肢
		NCI.createOptionTags($('#uploadKind'), res.uploadKinds);

		// 以前の検索条件を復元できれば再検索する
		if (UP0100.pager.loadCondition()) {
			UP0100.search();
		}
	},

	/** 検索 */
	search : function(pageNo, keepMessage) {
		const $targets = $('#formCondition').find('input, select, text');
		if (!Validator.validate($targets, true)) {
			return false;
		}
		const cond = UP0100.createCondition(pageNo);
		UP0100.pager.search(cond, keepMessage).done(function(res) {
			$('#search-result').removeClass('hide');
		});
	},

	/** 画面入力内容から検索条件を生成 */
	createCondition : function(pageNo) {
		const $elements = $('#formCondition').find('input, select, textarea');
		const cond = UP0100.pager.createCondition($elements, pageNo);
		// デフォルトソート条件
		if (!cond.sortColumn) {
			cond.sortColumn = 'UPLOAD_DATETIME, FILE_NAME, UPLOAD_FILE_ID';
			cond.sortAsc = false;
		}
		return cond;
	},

	/** 個々のアップロード画面を開く */
	openDetail : function(ev) {
		const $tr = $(ev.currentTarget).closest('tr');
		const uploadFileId = $tr.find('[data-field=uploadFileId]').text();
		const uploadKind = $tr.find('[data-field=uploadKind]').text();
		const url = './' + uploadKind.toLowerCase() + '.html?uploadFileId=' + uploadFileId;
		NCI.redirect(url);
	},

	/** アップロード登録履歴を開く */
	showHistory : function(ev) {
		const $tr = $(ev.currentTarget).closest('tr');
		const params = { 'uploadFileId': $tr.find('[data-field=uploadFileId]').text() };
		NCI.post('/up0100/getHistory', params).done(function(res) {
			if (res && res.success) {
				const $history = $('#history');
				new ResponsiveTable($history).fillTable(res.results);
				$history.modal();
			}
		});
	}
};
