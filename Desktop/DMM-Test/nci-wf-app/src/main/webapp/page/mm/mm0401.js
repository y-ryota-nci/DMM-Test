$(function() {
	const params = {
		"corporationGroupCode" : NCI.getQueryString('corporationGroupCode'),
		"timestampUpdated" : NCI.getQueryString('timestampUpdated'),
		"messageCds" : ['MSG0072', 'MSG0196']
	};
	NCI.init('/mm0401/init', params).done(function(res) {
		init(res);

		// 登録ボタン
		$('#btnRegister').click(doSave).prop('disabled', false);
		// 更新ボタン
		$('#btnUpdate').click(doSave).prop('disabled', false);
		// 削除ボタン
		$('#btnDelete').click(doDelete).prop('disabled', false);
	});
	// 戻るボタン押下
	$('#btnBack').click(function() { NCI.redirect('./mm0400.html'); });

	/** 初期化 */
	function init(res) {
		NCI.toElementsFromObj(res.corporationGroup);
		// 登録時
		if (!res.corporationGroup.id) {
			$('#btnRegister').prop('disabled', false);
			$('#btnUpdate').css('display', 'none');
		// 更新時
		} else {
			$('#btnRegister').hide();
			$('#btnUpdate').prop('disabled', false);
		}

		$('#corporationGroupCode').prop('disabled', res.corporationGroup.corporationGroupCode !== null);
	}

	/** 更新処理 */
	function doSave(ev) {
		const $root = $('#inputed');
		const $targets = $root.find('input, select, textarea');
		if (!Validator.validate($targets, true))
			return false;

		let msgCd;
		// 確認メッセージ
		if ($('#btnRegister').css('display') === 'none') {
			msgCd = 'MSG0071';
		} else {
			msgCd = 'MSG0069';
		}
		let msg = NCI.getMessage(msgCd, NCI.getMessage('corporationGroup'));

		NCI.confirm(msg, function() {
			const params = { "corporationGroup" : NCI.toObjFromElements($root) };
			NCI.post('/mm0401/save', params).done(function(res) {
				if (res && res.success) {
					init(res);
					$('#btnRegister').hide();
					$('#btnUpdate').css('display', '');
				}
			});
		});
		return false;
	}

	/** 削除処理 */
	function doDelete(ev) {
		const $root = $('#inputed');
		const $targets = $root.find('input, select, textarea');
		if (!Validator.validate($targets, true))
			return false;

		const msg = NCI.getMessage("MSG0196");
		if (NCI.confirm(msg, function() {
			const params = { "corporationGroup" : NCI.toObjFromElements($root) };
			NCI.post("/mm0401/delete", params).done(function(res) {
				if (res && res.success)
					init(res);
			});
		}));
	}
});
