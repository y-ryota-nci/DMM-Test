$(function() {
	let params = { messageCds : ['MSG0069'] };
	NCI.init('/mm0020/init', params).done(function(res) {
		if (res && res.success) {
			NCI.createOptionTags($('#containerId'), res.containers);
			NCI.createOptionTags($('select.dcType'), res.dcTypes);

			// 表示条件チェックボックス
			$('#dcList').on('click', 'input[type=checkbox].dcId', function() {
				let dcId = this.value, selected = this.checked;
				displayColumns(dcId, selected);
			});

			// 表示条件名の変更時
			$('input.dcName').on('change', function(ev) {
				var $root = $(this).closest('label.dcId[data-field]');
				var dcId = $root.attr('data-field');
				$('#tblDC thead th.' + dcId + ' span.dcName').text(this.value);
			});

			// コンテナ検索ボタン
			$('#btnSearch').on('click', function(ev) {
				search();
			});

			// 一括設定
			$('#btnBatchDcType').on('click', function(ev) {
				batchSetting();
			});

			// 更新ボタン押下
			$('#btnUpdate').on('click', function(ev) {
				register();
			});
		}
	});
});

/**
 * 選択された表示条件欄だけを表示
 * @param dcId 表示条件ID
 * @param selected 選択されてればtrue
 * @returns
 */
function displayColumns(dcId, selected) {
	const $tbl = $('#tblDC');

	// 選択されているテーブルヘッダの表示条件欄のみを表示
	$tbl.find('thead').find('th.dcId' + dcId).each(function(i, th) {
		const $th = $(th).toggleClass('hide', !selected);
		const $radio = $th.find('input[type=radio][name=batch]');

		// 現在が選択中で非選択になる場合、見えている最後のラジオを有効にしてやる
		if (!selected && $radio.prop('checked')) {
			$tbl.find('thead input[type=radio][name=batch]:visible:last').prop('checked', true);
		}
		$radio.prop('checked', selected);
	});
	// 選択されているテーブルデータ行の表示条件欄のみを表示
	$tbl.find('tbody').find('td.dcId' + dcId).each(function(i, elem) {
		const $td = $(elem).toggleClass('hide', !selected);
		const $select = $td.find('select.dcType').toggleClass('hide', !selected);
		if (!selected) {
			$select.val('');
		}
	});
}

/** 検索 */
function search() {
	// バリデーション
	const $targets = $('#containerId');
	if (!Validator.validate($targets, true)) {
		return false;
	}

	// コンテナの表示条件を反映
	let params = { "containerId" : $('#containerId').val() };
	NCI.post('/mm0020/search', params).done(function(res) {
		if (res && res.success) {
			const $dcList = $('#dcList');
			res.cols.forEach(function(dc, i, list) {
				// 表示条件チェックボックス
				let $label = $dcList.find('label.dcId[data-field=dcId' + dc.dcId + ']');
				$label.find('input[type=checkbox].dcId')
					.attr('data-field', 'dcId' + dc.dcId)
					.attr('value', dc.dcId)
					.prop('checked', dc.selected);
				$label.find('input.dcName').val(dc.dcName);

				// 表示条件テーブルのヘッダ
				$('#tblDC thead th.dcId' + dc.dcId + ' span.dcName').text(dc.dcName);
			});

			// 検索結果反映
			new Pager( $('#searchResult') ).fillTable(res.rows);

			// 列の表示可否
			$dcList.find('input[type=checkbox].dcId').each(function() {
				let dcId = this.value, selected = this.checked;
				displayColumns(dcId, selected);
			});

			// 検索時のコンテナIDを保存
			$('#tblDC').data('containerId', params.containerId);
		}
	});

	$('#searchResult').removeClass('hide');
	$('#btnUpdate').prop('disabled', false);
}

// 一括設定
function batchSetting() {
	const dcId = $('input[name=batch]:checked').val();
	const dcType = $('#batchDcType').val()
	$('#tblDC tbody').find('td.dcId' + dcId + ' select.dcType').val(dcType);
	Validator.hideBalloon();
}

// 登録処理
function register() {
	const $targets = getValidationTargets();
	if (!Validator.validate($targets, true)) {
		return false;
	}

	const msg = NCI.getMessage('MSG0071', NCI.getMessage('displayCondition'));
	NCI.confirm(msg, function() {
		// 表示条件名
		const dcList = [];
		$('#dcList').find('label.dcId').each(function(i, label) {
			let $label = $(label);
			let fName = $label.attr('data-field');
			let dcId = +fName.substring('dcId'.length);
			let dcName = $label.find('input.dcName').val();
			dcList.push({ "dcId" : dcId, "dcName" : dcName });
		});

		// パーツ表示条件
		let partsDcList = [];
		$('#tblDC tbody tr').each(function(i, tr) {
			const $tr = $(tr);
			const partsId = +$tr.find('span[data-field=partsId]').text();

			$tr.find('select.dcType').each(function(i, select) {
				if (select.value == '') return true;
				const fName = $(select).attr('data-field');
				const dcId = +fName.substring('dcType'.length);
				const dcType = select.value;
				partsDcList.push({
					"partsId" : partsId,
					"dcId" : dcId,
					"dcType" : dcType
				});
			});
		});

		// 更新処理
		const params = {
				"containerId" : $('#tblDC').data('containerId'),
				"dcList" : dcList,
				"partsDcList" : partsDcList
		};
		NCI.post('/mm0020/save', params).done(function(res) {
			// 画面をクリア
			$('#searchResult').addClass('hide');
			$('#btnUpdate').prop('disabled', true);
			$('#containerId').val('');
		});
	});
}

// バリデーション対象一覧を返す
function getValidationTargets() {
	let $targets = $('#dcList').find('input');
	$targets = $('#tblDC > tbody').find('input, select').add($targets);
	return $targets;
}