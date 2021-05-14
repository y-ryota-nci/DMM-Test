$(function() {

	let screenId = NCI.getQueryString('screenId');
	let version = NCI.getQueryString('version');
	let params = {
			'screenId' : screenId,
			'version' : version,
			'messageCds' : [ 'screenInfo', 'MSG0069' ]
	};
	NCI.init('/vd0031/init', params).done(function(res) {
		if (res && res.success) {
			// 有効条件および計算式ボタンの活性化条件
			// 画面IDがあり、かつスクラッチ開発でないこと
			let enabled = (screenId != null && res.screen.scratchFlag == '0');
			// 画面に紐づく有効条件、計算式をDATA属性として保持
			$('#conds').data('conds', res.condMap);
			$('#calcs').data('calcs', res.calcMap);
			$('#scripts').data('scripts', res.scripts);

			// 選択肢
			NCI.createOptionTags($('#scratchFlag'), res.scratchFlags);
			NCI.createOptionTags($('#containerId'), res.containers);
			NCI.createOptionTags($('#trayType'), res.trayTypes).val('NEW');
			displayDcList(res.dcList);

			// データを反映
			NCI.toElementsFromObj(res.screen);

			// 更新ボタン押下
			$('#btnUpdate')
				.prop('disabled', false)
				.click(update);

			// プレビューボタン押下
			$('#btnPreview').click(preview);

			// パーツ属性のコピーボタン押下
			$('#btnCopyProperty')
				.click(openCopyPartsProperty)
				.prop('disabled', !enabled);

			// スクラッチ区分変更
			$('#scratchFlag')
				.change(switchScratchFlag)
				.change()
				.prop('disabled', (screenId != null));

			// コンテナIDの変更
			$('#containerId')
				.change(changeContainerId)
				.change();

			// 有効条件ボタン押下
			$('#btnEC')
				.click(function() { displayPartsTree(1);})
				.prop('disabled', !enabled);

			// 計算式ボタン押下
			$('#btnCalc')
				.click(function() { displayPartsTree(2);})
				.prop('disabled', !enabled);

			// 外部Javascriptボタン
			$('#btnOutsideJavascript')
				.click(openScreenJavascript)
				.prop('disabled', screenId == null);
		}

		$('#btnBack').click(function(ev) {
			NCI.redirect('./vd0030.html');
		});
	});

	/** 更新処理 */
	function update(ev) {
		// バリデーション
		let $root = $('#inputed');
		let $targets = $root.find('input, select');
		if (!Validator.validate($targets, true)) {
			return false;
		}

		// 確認メッセージ
		let msg = NCI.getMessage('MSG0069', NCI.getMessage('screenInfo'));
		NCI.confirm(msg, function() {
			// 更新処理
			let params = {
					'screen' : NCI.toObjFromElements($root, ['trayType', 'dcId']),
					'condMap'  : $('#conds').data('conds') || {},
					'calcMap': $('#calcs').data('calcs') || {},
					'scripts' : $('#scripts').data('scripts') || []
			};
			NCI.post('/vd0031/save', params).done(function(res) {
				if (res && res.success) {
					$('#btnBack').click();
				}
			})
		});
	}

	/** スクラッチ区分の変更 */
	function switchScratchFlag(ev) {
		let isScratch = $('#scratchFlag').val() === '1';
		let $containerId = $('#containerId');
		if (isScratch) {
			$containerId.val('').prop('disabled', true).change();
		} else {
			$containerId.prop('disabled', false).change();
		}
	}

	/** コンテナID変更 */
	function changeContainerId() {
		// コンテナに合わせて表示条件の選択肢を切り替え
		let containerId = $('#containerId').val();
		let params = {
				'containerId' : containerId
		};
		NCI.post('/vd0031/getDcList', params).done(function(res) {
			if (res && res.success) {
				displayDcList(res.dcList);

				// プレビューボタン
				let disabled = (containerId == null || containerId === '');
				$('#btnPreview').prop('disabled', disabled);
			}
		});
	}

	function preview() {
		// コンテナに合わせて表示条件の選択肢を切り替え
		let screenId = $('#screenId').val();
		let containerId = $('#containerId').val();
		let dcId = $('#dcId').val();
		let trayType = $('#trayType').val();
		let condMap = $('#conds').data('conds');
		let calcMap = $('#calcs').data('calcs');
		let scripts = $('#scripts').data('scripts');
		let params = {
				'screenId' : screenId,
				'containerId' : containerId,
				'dcId' : dcId,
				'condMap': condMap,
				'calcMap': calcMap,
				'scripts': scripts,
				'trayType' : trayType,
				'screenCustomClass' : $('#screenCustomClass').val()
			};
		NCI.post('/vd0031/preparePreview', params).done(function(res) {
			if (res && res.success) {
				let params = {
						'ctx' : res.ctx,
						'dcId' : dcId,
						'trayType' : trayType
				};
				let id = "vd0115";
				NCI.flushScope(id, params);
				window.open('./vd0115.html?' + FLUSH_SCOPE_KEY + '=' + id, "preview");
			}
		});
		return false;
	}

	/** 表示条件の選択肢を表示 */
	function displayDcList(dcList) {
		let $dcId = $('#dcId');
		NCI.createOptionTags($dcId, dcList);

		// 表示条件の選択肢の初期値
		let dcLength = $dcId[0].options.length;
		if (dcLength <= 1)
			$dcId[0].selectedIndex = dcLength - 1;
		else
			$dcId[0].selectedIndex = 1;
	}

	function displayPartsTree(flg) {
		let containerId = $('#containerId').val();
		let conds = $('#conds').data('conds');
		let calcs = $('#calcs').data('calcs');
		let params = { 'containerId':containerId, 'conds':conds, 'calcs':calcs, 'type':flg }
		Popup.open("../vd/vd0032.html", fromVd0032, params);
	}

	function openScreenJavascript(ev) {
		let params = {
				'scripts' : $('#scripts').data('scripts')
		}
		Popup.open('../vd/vd0033.html', fromVd0033, params);
	}

	// パーツ属性のコピーボタン押下
	function openCopyPartsProperty(ev) {
		const conds = $('#conds').data('conds');
		const calcs = $('#calcs').data('calcs');
		const params = {
				'destScreenId' : $('#screenId').val(),
				'destContainerId':$('#containerId').val(),
				'destCondMap' : $('#conds').data('conds'),
				'destCalcMap' : $('#calcs').data('calcs')
		}
		Popup.open("../vd/vd0034.html", fromVd0034, params);
	}
});

function fromVd0032(result) {
	if (result) {
		// 更新された有効条件、計算式を再設定
		$('#conds').data('conds', result.conds);
		$('#calcs').data('calcs', result.calcs);
	}
}

function fromVd0033(scripts) {
	if (scripts) {
		// 更新されたJavascriptを再設定
		$('#scripts').data('scripts', scripts)
	}
}

// パーツ属性コピー画面からのコールバック
function fromVd0034(result) {
	if (result) {
		// 更新された有効条件、計算式を再設定
		$('#conds').data('conds', result.destCondMap);
		$('#calcs').data('calcs', result.destCalcMap);
	}
}
