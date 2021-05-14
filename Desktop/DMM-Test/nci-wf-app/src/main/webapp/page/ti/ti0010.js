$(function() {
	let params = { "messageCds" : ['MSG0068', 'importMaster'] };
	NCI.init('/ti0010/init', params).done(function(res) {
		// 取込テーブル一覧と全テーブル一覧を反映
		redraw(res);
		// カテゴリを反映
		$('input[name=categoryId][value=' + res.categoryId + ']').prop('checked', true);

		// カテゴリ再選択
		$('#tblCategory').on('click', 'input[type=radio][name=categoryId]', function() {
			refresh(this.value);
		});
		// 更新ボタン押下
		$('button.btnUpdate').click(update);
		// カテゴリ編集
		$('#btnEditCategory').click(editCategory);
	});

	/** カテゴリ編集を開く */
	function editCategory() {
		Popup.open('./ti0011.html', fromTi0011);
	}

	/** バリデーション */
	function validate() {
		let $categoryId = $('input[name=categoryId]:checked');
		if (!$categoryId.val()) {
			Validator.showBalloon($categoryId, NCI.getMessage('MSG0003', NCI.getMessage('category')));
			return false;
		}
		return true;
	}

	/** 更新処理 */
	function update() {
		if (!validate)
			return false;

		let msg = NCI.getMessage('MSG0068', NCI.getMessage('importMaster'));
		if (NCI.confirm(msg, function() {
			let inputs = [];
			$('input[data-field=tableName]:checked').each(function(i, checkbox) {
				inputs.push({
					"tableName" : checkbox.value,
					"comment" : $(checkbox).closest('tr').find('input[data-field=comment]').val()
				});
			});
			let params = {
				"categoryId" : $('input[name=categoryId]:checked').val(),
				"entityType" : null,	/* 仕様が未定だ */
				"inputs" : inputs
			};
			NCI.post('/ti0010/save', params).done(function(res) {
				if (res && res.success) {
					// nothing to do
				}
			});
		}));
	}

});
/** テーブル一覧を反映 */
function redraw(res) {
	if (res.categories)
		new ResponsiveTable($('#divCategory')).fillTable(res.categories);
	if (res.allTables) {
		// テーブル一覧をレンダリング
		new ResponsiveTable($('#divAll')).fillTable(res.allTables);
		// レンダリングしただけではCHECKBOXの値が設定されていないので、別途設定してやる
		$('#tblAll>tbody').find('input[data-field=tableName]').each(function(i, elem) {
			elem.checked = res.allTables[i].selected;
		});
	}
	$('#btnEditCategory, button.btnUpdate').prop('disabled', false);
}

/** テーブルカテゴリIDに紐付く取込テーブル一覧を読み直し */
function refresh(categoryId) {
	let params = { "categoryId" : categoryId };
	NCI.post('/ti0010/refresh', params).done(function(res) {
		redraw(res);
	});
}
/** カテゴリ編集画面からのCallback関数 */
function fromTi0011(categories) {
	if (categories) {
		// 選択されているカテゴリIDを退避
		let categoryId = $('input[name=categoryId]:checked').val();

		// 更新されたカテゴリを反映
		new ResponsiveTable($('#divCategory')).fillTable(categories);

		$target = $('input[name=categoryId][value=' + categoryId + ']');
		if ($target.length === 0) {
			$target = $('input[name=categoryId]').first();
		}
		$target.prop('checked', true);

		// テーブル一覧を読み直し
		categoryId = $target.val();
		refresh(categoryId);
	}
}