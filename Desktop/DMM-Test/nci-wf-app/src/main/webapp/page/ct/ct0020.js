$(function() {
	// 遷移元からの受信パラメータ
	var params = {
		"corporationCode" : NCI.getQueryString("corporationCode"),
	};
	var pager = new Pager($('#seach-result'), '/ct0020/search', search).init();
	NCI.init("/ct0020/init").done(function(res) {
		if (res && res.success) {
			// 企業グループの選択肢
			NCI.createOptionTags($('#catalogCategoryId'), res.catalogCategories).val('');

			// 初期検索するか？
			const initSearch = NCI.getQueryString("initSearch");
			if (initSearch) {
				search(1);
			}

			$(document)
			.on('click', '#btnSearch', function() {
				// 検索ボタン押下
				search(1);
				return false;
			})
			.on('click', 'ul.pagination a', function() {
				// ページ番号リンク押下
				var pageNo = this.getAttribute('data-pageNo');
				search(pageNo);
				return false;
			})
			// 検索結果の選択ラジオボタン
			.on('change', 'input[type=radio][name=rdoSelect]', function() {
				$('#btnSelect').prop('disabled', false);
			})
			// 閉じるボタン
			.on('click', 'button.btnClose', function() {
				Popup.close();
			})
			// 選択ボタン
			.on('click', '#btnSelect', function() {
				var $tr = $('input[name=rdoSelect]:checked').parent().parent();
				var entity = {};
				$tr.find('[data-field]').each(function(i, elem) {
					var fieldName = elem.getAttribute('data-field');
					entity[fieldName] = $(elem).text();
				});

				// コールバック関数の呼び出し
				Popup.close(entity);
			})
			;
		}
	});

	/** 検索実行 */
	function search(pageNo) {
		var $targets = $('#formCondition').find('input, select');
		if (!Validator.validate($targets, true))
			return false;

		var cond = createCondition(pageNo);
		pager.search(cond).done(function(res) {
			$('#seach-result tbody span.catalogImageId').each(function(i, elem) {
				var $catalogImageId = $(elem);
				var $td = $catalogImageId.parent();
				$td.find('img').remove();
				var url = '../../assets/nci/images/noImage.png';
				if ($catalogImageId.text()) {
					url = '../../endpoint/ct0020/download/catalogImage?catalogImageId=' + $catalogImageId.text();
					url += '&_tm=' + +new Date();
				}
				$td.append('<img src="' + url + '" class="img-responsive img-thumbnail col-md-12">');
			});
		});
		$('#btnSelect').prop('disabled', true);
		$('#seach-result').removeClass('invisible');
	}

	/** 画面入力内容から検索条件を生成 */
	function createCondition(pageNo) {
		var $elements = $('#formCondition').find('input, select, textarea');
		var cond = pager.createCondition($elements, pageNo);
		// デフォルトソート条件
		if (!cond.sortColumn) {
			cond.sortColumn = 'C.CATALOG_CODE';
			cond.sortAsc = true;
		}
		return cond;
	}
});

