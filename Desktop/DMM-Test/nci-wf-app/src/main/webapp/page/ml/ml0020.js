var previewMap = {};
$(function() {
	let params = {};
	NCI.init('/ml0020/init', params).done(function(res) {
		if (res && res.success) {
			init(res);

			// 更新ボタン押下
			$('#btnUpdate').prop('disabled', false).click(update);
			// SMTP認証による有効/無効切り替え
			$('[name=' + NCI.escapeSelector('mail.smtp.auth') + ']').click(enableBySmtpAuth);
			// メールの文字エンコードによるメール符号化方式の切り替え
			$('[name=' + NCI.escapeSelector('mail.encode') + ']').click(enableByEncode);

			enableBySmtpAuth();
			enableByEncode();
		}
	});

	/** メールの文字エンコードによるメール符号化方式の切り替え */
	function enableByEncode() {
		// utf-8ならbase64エンコード、iso-2022-jpなら7bitでエンコード。これ以外の組み合わせは許容しない
		let encode = $('[name=' + NCI.escapeSelector('mail.encode') + ']:checked').val();
		$('[name=' + NCI.escapeSelector('mail.contentTransferEncoding') + ']').each(function(i, elem) {
			let $elem = $(elem);
			let contentTransferEncoding = $elem.val();
			if (encode === 'utf-8' && contentTransferEncoding === 'base64')
				$elem.prop('disabled', false).prop('checked', true);
			else if (encode === 'iso-2022-jp' && contentTransferEncoding === '7bit')
				$elem.prop('disabled', false).prop('checked', true);
			else
				$elem.prop('disabled', true);
		});
	}

	/** SMTP認証による有効/無効切り替え */
	function enableBySmtpAuth() {
		let smtpAuth = $('[name=' + NCI.escapeSelector('mail.smtp.auth') + ']:checked').val();
		let $objects = $('.smtpAuth').prop('disabled', smtpAuth === 'FALSE');
		Validator.hideBalloon($objects);
	}

	/** 初期化 */
	function init(res) {
		// データを反映
		let $root = $('#inputs');
		$.each(res.configs, function(i, r) {
			let prop = NCI.escapeSelector(r.configCode);
			let $targets = $('[id=' + prop + '], [name=' + prop + '], [data-field=' + prop + ']', $root);
			$targets.each(function(i, elem) {
				NCI.setElementFromObj(elem, r.configValue);
				$(elem).data({
					'version' : r.version,
					'mailConfigId': r.mailConfigId
				});
			});
			if (r.remarks) {
				$(document.createElement('div'))
					.addClass('text-info')
					.text(r.remarks)
					.appendTo($targets.closest('dd'));
			}
		});
	}

	/** 更新処理 */
	function update(ev) {
		// バリデーション
		let $root = $('#inputs');
		let $targets = $root.find('input, select');
		if (!Validator.validate($targets, true)) {
			return false;
		}

		// 確認メッセージ
		let msg = NCI.getMessage('MSG0071', NCI.getMessage('mailConfig'));
		NCI.confirm(msg, function() {
			// 更新処理
			let inputs = [];
			$root.find('dd').each(function(i, dd) {
				let $dd = $(dd);
				let $elem = $dd.find('[data-field]:first, [type=radio][name]');	// ラジオボタンは複数あるので、先頭を代表とする
				let configValue = null;
				if ($elem.is('[type=radio]'))
					configValue = $dd.find('input[name=' + NCI.escapeSelector($elem.prop('name')) + ']:checked').val();
				else
					configValue = $elem.val();

				inputs.push({
					"mailConfigId" : $elem.data('mailConfigId'),
					"configCode" : $elem.data('field') || $elem.prop('name'),
					"configValue" : configValue,
					"version" : $elem.data('version')
				});
			});
			let params = { "inputs" : inputs };
			NCI.post('/ml0020/save', params).done(function(res) {
				if (res && res.success) {
				}
			})
		});
	}
});
