$(function() {
	const params = { messageCds : ['MSG0069', 'MSG0072', 'MSG0280', 'MSG0281', 'blockDisplaySettting'] };
	NCI.init('/mm0060/init', params).done(function(res) {
		if (res && res.success) {
			NCI.createOptionTags($('#screenProcessId'), res.screenProcesses);
		}
	});

	// ソート機能を付与
	$('#tblDC tbody').sortable({
		handle: 'i.glyphicon.glyphicon-align-justify',
		helper: MM0060.fixPlaceHolderWidth,
		stop: MM0060.stop,
		update: MM0060.resetSortOrder
	});

	$(document)
	// 表示条件チェックボックス
	.on('click', '#dcList input[type=checkbox].dcId', function(ev) {
		const dcId = this.value, selected = this.checked;
		MM0060.displayColumns(dcId, selected);
	})
	// 表示条件名の変更時
	.on('change', 'input.dcName', function(ev) {
		const $root = $(this).closest('label.dcId[data-field]');
		const dcId = $root.attr('data-field');
		$('#tblDC thead th.' + dcId + ' span.dcName').text(this.value);
	})
	// 画面プロセス名の検索ボタン
	.on('click', '#btnSearch', function(ev) { MM0060.search(false); })
	// 更新ボタン押下
	.on('click', '#btnUpdate', MM0060.update)
	// デフォルトをコピーボタン押下
	.on('click', '#btnCopyDefault', MM0060.copyDefault)
	// 削除ボタン
	.on('click', '#btnDelete', MM0060.deleteBlockDisplay)
	// 全選択／全解除ボタン押下
	.on('click', '#btnSelectAll,#btnCancelAll', function(ev) {
		const $tr = $('#tblDC tbody tr input[type=radio]:checked').parents('tr');
		$tr.find('input[type=checkbox]').prop('checked', $(this).attr('id') === 'btnSelectAll');
	});
});
var MM0060 = {
	/** デフォルトのブロック表示順 */
	DEFAULT_SCREEN_PROCESS_ID : '-1',

	/** セル幅を記録しておくための配列 */
	width : [],

	/**
	 * 選択された表示条件欄だけを表示
	 * @param {*} dcId 表示条件ID
	 * @param {*} selected 選択されてればtrue
	 */
	displayColumns : function(dcId, selected) {
		const $tbl = $('#tblDC');

		// 選択されているテーブルヘッダの表示条件欄のみを表示
		$tbl.find('thead').find('th.dcId' + dcId).each(function(i, th) {$(th).toggleClass('hide', !selected);});

		// 選択されているテーブルデータ行の表示条件欄のみを表示
		$tbl.find('tbody').find('td.dcId' + dcId).each(function(i, elem) {
			let $td = $(elem).toggleClass('hide', !selected);
			if (!selected) {
				$td.find('input[type=checkbox]').prop('checked', false);
			}
		});
		$tbl.find('thead tr th').css('width', 'auto').css('width', '');
		$tbl.find('tbody tr td').css('width', 'auto').css('width', '');
	},

	/**
	 * ソート順をリセット
	 */
	resetSortOrder : function(ev, ui) {
		$('#tblDC tbody').find('input[data-field=sortOrder]').each(function(i, elem) {$(elem).val(i);});
	},

	/** 幅調整 */
	fixPlaceHolderWidth : function(event, ui){
		MM0060.width = [];
		ui.children().each(function() {
			MM0060.width.push($(this).width());
			$(this).width($(this).width());
		});
		$('#tblDC thead tr').children().each(function(i, e) {
			$(this).width(MM0060.width[i]);
		});
		return ui;
	},

	/**
	 * ソートストップ処理
	 */
	stop : function() {
		$('#tblDC thead tr').children().each(function(i, e) {$(this).width(MM0060.width[i]);});
	},

	/**
	 * 検索処理
	 */
	search : function(keepMessage) {
		// バリデーション
		const $targets = $('#screenProcessId');
		if (!Validator.validate($targets, true)) return false;

		// 画面プロセス定義の表示条件を反映
		const params = {screenProcessId: $('#screenProcessId').val()};
		NCI.post('/mm0060/search', params, keepMessage).done(function(res) {
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
				const responsiveTable = new ResponsiveTable( $('#searchResult'), NCI.getMessage('MSG0281', NCI.getMessage('blockDisplaySettting')) );
				responsiveTable.modifyTR = function($tr, rowIndex, entity, labels, ignores) {
					$tr.find('input[type=radio]').prop('disabled', false);
				};
				responsiveTable.fillTable(res.rows);

				// 列の表示可否
				$dcList.find('input[type=checkbox].dcId').each(function() {
					const dcId = this.value, selected = this.checked;
					MM0060.displayColumns(dcId, selected);
				});

				// 最初のラジオボタンを選択
				$('#tblDC tbody tr input[type=radio]:first').prop('checked', true);

				// 検索時の画面プロセス定義IDを保存
				$('#tblDC').data('screenProcessId', params.screenProcessId);

				// ボタン制御
				const isDefaultScreenProcess = params.screenProcessId === MM0060.DEFAULT_SCREEN_PROCESS_ID;
				$('#displayCondition, #searchResult').removeClass('hide');
				$('#divSelectButton').toggleClass('hide', res.rows.length === 0);
				$('#btnUpdate').prop('disabled', res.rows.length === 0);
				$('#btnDelete').prop('disabled', res.rows.length === 0 || isDefaultScreenProcess);
				$('#btnCopyDefault').prop('disabled', isDefaultScreenProcess);
			}
		});
	},

	/**
	 * 更新処理
	 */
	update : function(ev) {
		const $targets = $('input, select');
		if (!Validator.validate($targets, true)) {
			return false;
		}

		const msg = NCI.getMessage('MSG0071', NCI.getMessage('blockDisplaySettting'));
		NCI.confirm(msg, function() {
			// 表示条件名
			const dcs = [];
			$('#dcList').find('label.dcId').each(function(i, label) {
				let $label = $(label);
				let fName = $label.attr('data-field');
				let dcId = fName.substring('dcId'.length);
				let dcName = $label.find('input.dcName').val();
				dcs.push({dcId : dcId, dcName: dcName });
			});

			// ブロック表示順
			const blockDisplays = [];
			$('#tblDC tbody tr').each(function(i, tr) {
				let $tr = $(tr);
				let blockId = $tr.find('span[data-field=blockId]').text();
				let sortOrder = $tr.find('input[data-field=sortOrder]').val();

				$tr.find('input.displayFlag').each(function(i, input) {
					if ($(input).parent().parent().hasClass('hide')) return;
					const fName = $(input).attr('data-field');
					const dcId = fName.substring('displayFlag'.length);
					const displayFlag = $(input).prop('checked') ? '1' : '0';
					const expansionFlag = $(input).parent().parent().find('input.expansionFlag').prop('checked') ? '1' : '0';
					blockDisplays.push({
						blockId: blockId,
						dcId: dcId,
						displayFlag: displayFlag,
						expansionFlag: expansionFlag,
						sortOrder: sortOrder
					});
				});
			});

			// 更新処理
			const params = {
					screenProcessId: $('#tblDC').data('screenProcessId'),
					dcs: dcs,
					blockDisplays: blockDisplays
			};
			NCI.post('/mm0060/save', params).done(function(res) {
				// 画面をクリア
//				$('#displayCondition').addClass('hide');
//				$('#searchResult').addClass('hide');
//				$('#btnUpdate, #btnCopyDefault, #btnDelete').prop('disabled', true);
//				$('#screenProcessId').prop('selectedIndex', 0);
				MM0060.search(true);
			});
		});
	},

	/**
	 * デフォルトをコピー処理
	 */
	copyDefault : function(ev) {
		const $targets = $('input, select');
		if (!Validator.validate($targets, true)) {
			return false;
		}

		const msg = NCI.getMessage('MSG0280', NCI.getMessage('blockDisplaySettting'));
		NCI.confirm(msg, function() {
			// 表示条件名
			const dcs = [];
			$('#dcList').find('label.dcId').each(function(i, label) {
				const $label = $(label);
				const fName = $label.attr('data-field');
				const dcId = fName.substring('dcId'.length);
				const dcName = $label.find('input.dcName').val();
				dcs.push({dcId : dcId, dcName: dcName });
			});
			// 更新処理
			const params = {
					screenProcessId: $('#tblDC').data('screenProcessId'),
					dcs: dcs,
			};
			NCI.post('/mm0060/copyDefault', params).done(function(res) {
				// 画面をクリア
				MM0060.search(true);
			});
		});
	},

	/**
	 * 削除処理
	 */
	deleteBlockDisplay : function() {
		const $targets = $('input, select');
		if (!Validator.validate($targets, true)) {
			return false;
		}

		const msg = NCI.getMessage('MSG0072', NCI.getMessage('blockDisplaySettting'));
		NCI.confirm(msg, function() {
			// 表示条件名
			const dcs = [];
			$('#dcList').find('label.dcId').each(function(i, label) {
				const $label = $(label);
				const fName = $label.attr('data-field');
				const dcId = fName.substring('dcId'.length);
				const dcName = $label.find('input.dcName').val();
				dcs.push({dcId : dcId, dcName: dcName });
			});
			// 更新処理
			const params = {
					screenProcessId: $('#tblDC').data('screenProcessId'),
					dcs: dcs,
			};
			NCI.post('/mm0060/delete', params).done(function(res) {
				// 画面をクリア
				MM0060.search(true);
			});
		});
	}
}
