$(function() {
	const params = NCI.flushScope(NCI.getQueryString(FLUSH_SCOPE_KEY));
	vd0034.destScreenId = params.destScreenId;
	vd0034.destContainerId = params.destContainerId;
	vd0034.destCondMap = params.destCondMap;
	vd0034.destCalcMap = params.destCalcMap;

	NCI.init('/vd0034/init', params).done(function(res) {
		// 初期化
		vd0034.init(res);
	});

	// コピー元画面
	$('#srcScreenId').change(vd0034.changeScreen)
	// コピーボタン押下
	$('#btnCopy').on('click', vd0034.copy);
	// 閉じるボタン押下
	$('#btnClose').on('click', vd0034.close);
	// 全選択／全解除
	$('button.btnSelectAll, button.btnCancelAll').click(vd0034.selectAll)
	$(document).on('click', 'input.condFlag, input.calcFlag', vd0034.toggleCheckbox);
});

const vd0034 = {
	/** テーブルレンダラー */
	responsiveTable : null,

	/** コピー先の画面ID */
	destScreenId : null,

	/** コピー先の画面のコンテナID */
	destContainerId : null,

	/** コピー先パーツ条件Map */
	destCondMap : null,

	/** コピー先計算式Map */
	destCalcMap : null,

	/** 初期化 */
	init : function(res) {
		// 画面一覧の選択肢
		NCI.createOptionTags($('#srcScreenId'), res.screenIds);

		// コピー元パーツ一覧
		vd0034.responsiveTable = new ResponsiveTable($('#srcParts'));
		vd0034.responsiveTable.modifyTR = vd0034.modifyTR;
		vd0034.responsiveTable.fillTable(res.results);
	},

	/** コピー元画面の変更時 */
	changeScreen : function(ev) {
		$('#srcParts').toggleClass('hide', !ev.target.value);
		$('#btnCopy').prop('disabled', true);

		const screenId = ev.target.value;
		if (screenId) {
			const params = { "srcScreenId" : screenId };
			NCI.post('vd0034/changeScreen', params).done(function(res) {
				// コピー元パーツ一覧を書き換え
				vd0034.responsiveTable.fillTable(res.results);
			});
		}
		else {
			// コピー元パーツ一覧をクリア
			vd0034.responsiveTable.fillTable([]);
		}
	},

	/** コピー実行 */
	copy : function(ev) {
		const msg = NCI.getMessage('');
		NCI.confirm(msg, function() {
			const params = {
				"srcScreenId" : $('#srcScreenId').val(),
				"srcParts" : NCI.toArrayFromTable($('#srcParts')),
				"destScreenId" : vd0034.destScreenId,
				"destContainerId" : vd0034.destContainerId,
				"destCondMap" : vd0034.destCondMap || {},
				"destCalcMap" : vd0034.destCalcMap || {}
			};
			NCI.post('vd0034/copy', params).done(function(res) {
				if (res && res.success) {
					Popup.close(res);
				}
			});
		});
	},

	close : function(ev) {
		Popup.close();
	},

	/** テーブル・レンダリング時の補正処理 */
	modifyTR : function($tr, rowIndex, entity, labels, ignores) {
		if (!entity.canCopyConds)
			$tr.find('input.condFlag').remove();
		if (!entity.canCopyCalcs)
			$tr.find('input.calcFlag').remove();
	},

	/** チェックボックスON/OFF切り替え処理 */
	toggleCheckbox : function(ev) {
		const len = $('tbody > tr > td > input[type=checkbox]:checked').length;
		$('#btnCopy').prop('disabled', len === 0);
	},

	/** 全選択／全解除 */
	selectAll : function(ev) {
		const $button = $(ev.target);
		const checked = $button.hasClass('btnSelectAll');
		const filter = $button.hasClass('condFlag') ? 'condFlag' : 'calcFlag';
		$('tbody > tr > td > input[type=checkbox].' + filter).prop('checked', checked);
		vd0034.toggleCheckbox();
	}
};
