$(function() {

	let screenProcessId = NCI.getQueryString('screenProcessId');
	let version = NCI.getQueryString('version');
	let params = {
			'screenProcessId' : screenProcessId,
			'version' : version,
			'messageCds' : [ 'screenProcessInfo', 'MSG0069' ]
	};
	NCI.init('/vd0041/init', params).done(function(res) {
		if (res && res.success) {
			// 登録時
			if (!res.entity.version) {
				$('#btnRegister').prop('disabled', false);
				$('#btnUpdate').hide();
			// 更新時
			} else {
				$('#btnRegister').hide();
				$('#btnUpdate').prop('disabled', false);
			}
			// 選択肢
			NCI.createOptionTags($('#processDefCode'), res.processDefCodes);
			NCI.createOptionTags($('#processDefDetailCode'), res.processDefDetailCodes);
			NCI.createOptionTags($('#screenId'), res.screenIds);

			// データを反映
			NCI.toElementsFromObj(res.entity);

			// プロセス定義の変更
			$('#processDefCode').change(changeProcessDefCode);

			// 登録ボタン押下
			$('#btnRegister')
				.prop('disabled', false)
				.click(save);
			// 更新ボタン押下
			$('#btnUpdate')
				.prop('disabled', false)
				.click(save);
		}

		$('#btnBack').click(function(ev) {
			NCI.redirect('./vd0040.html');
		});

		// カレンダー（年月日）
		//	初期状態だとフォーカスアウト時にblankを保存してしまうため位置を修正
		NCI.ymdPicker($('input.ymdPicker'));


	});

	/** 保存処理 */
	function save(ev) {
		// バリデーション
		let $root = $('#inputed');
		let $targets = $root.find('input, select');
		if (!Validator.validate($targets, true)) {
			return false;
		}
		let msg;
		// 確認メッセージ
		if ($('#btnRegister').css('display') === 'none') {
			msg = NCI.getMessage('MSG0071', NCI.getMessage('screenProcessInfo'));
		} else {
			msg = NCI.getMessage('MSG0069', NCI.getMessage('screenProcessInfo'));
		}
		NCI.confirm(msg, function() {
			let params = {
					'entity' : NCI.toObjFromElements($root)
			};
			NCI.post('/vd0041/save', params).done(function(res) {
				if (res && res.success) {
					$('#btnBack').click();
				}
			})
		});
	}

	/** プロセス定義コード変更時 */
	function changeProcessDefCode(ev) {
		// プロセス定義コードに連動させてプロセス定義明細コードの選択肢を切り替え
		let params = { "processDefCode" : $('#processDefCode').val() };
		NCI.post('/vd0041/getProcessDefDetails', params).done(function(res) {
			NCI.createOptionTags($('#processDefDetailCode'), res.processDefDetailCodes);
		});
	}
});
