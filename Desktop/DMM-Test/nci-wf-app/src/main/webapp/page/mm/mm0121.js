var previewMap = {};
$(function() {
	let params = {
			"announcementId" : NCI.getQueryString('announcementId'),
			"version" : NCI.getQueryString("version")
	};
	NCI.init('/mm0121/init', params).done(function(res) {
		if (res && res.success) {
			NCI.createOptionTags($('#corporationCode'), res.corporations);

			init(res);

			// 登録ボタン押下
			$('#btnRegister').prop('disabled', false).click(save);
			$('#btnUpdate').prop('disabled', false).click(save);
			// プレビューボタン押下
			$('#btnPreview').prop('disabled', false).click(preview);
		}

		// カレンダー（年月日）
		//	初期状態だとフォーカスアウト時にblankを保存してしまうため位置を修正
		NCI.ymdPicker($('input.ymdPicker'));

	});
	// 戻るボタン押下
	$('#btnBack').click(function(ev) {
		NCI.redirect('./mm0120.html');
	});

	/** 初期化 */
	function init(res) {
		// データを反映
		NCI.toElementsFromObj(res.entity, $('#inputedHeader'));
		// 登録時
		if (!res.entity.version) {
			$('#btnRegister').prop('disabled', false);
			$('#btnUpdate').hide();
		// 更新時
		} else {
			$('#btnRegister').hide();
			$('#btnUpdate').prop('disabled', false);
		}

		// タブ
		let $tabUI = $('#tabUI').empty();
		let $tabContents = $('#tabContents').empty();
		let $template = $('#tabTemplate>div.tab-pane');
		for (let i = 0; i < res.entity.locales.length; i++) {
			let body = res.entity.locales[i];
			// タブUI部分
			let $link = $('<a data-toggle="tab"></a>')
				.attr('href', '#tab-' + body.localeCode)
				.text(body.localeName);
			let $li = $('<li></li>')
				.toggleClass('active', i == 0)
				.append($link)
				.appendTo($tabUI);
			// タブのコンテンツ部分
			let $panel = $template.clone(true);
			$panel
				.attr('id', 'tab-' + body.localeCode)
				.toggleClass('active', i == 0);
			$panel.find('input[data-field=subject]').val(body.subject);
			$panel.find('input[data-field=contents]').val(body.contents);
			$panel.appendTo($tabContents);
			NCI.toElementsFromObj(body, $panel);
		}
		// 操作者の言語コードのタブを選択させる
		$('a[href=\\#tab-' + NCI.loginInfo.localeCode + ']').click();
	}

	/** 更新処理 */
	function save(ev) {
		// バリデーション
		let $targets = $('input, textarea');
		if (!Validator.validate($targets, true)) {

			// タブ配下でエラーがあれば、そのタブをアクティブにする
			let $tabPane = $('.has-error').closest('div.tab-pane');
			let localeCode = $tabPane.find('span[data-field=localeCode]').text();
			$('a[href=\\#tab-' + localeCode + ']').click();
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
			// 更新処理
			let entity = NCI.toObjFromElements($('#inputedHeader'));
			entity.locales = [];
			$('#tabContents').find('div.tab-pane').each(function() {
				let $tabPane = $(this);
				let tabContents = NCI.toObjFromElements($tabPane);
				entity.locales.push(tabContents);
			});
			let params = { "entity" : entity };
			NCI.post('/mm0121/save', params).done(function(res) {
				if (res && res.success) {
					$('#btnBack').click();
				}
			})
		});
	}

	function preview(ev) {
		let $button = $(ev.currentTarget);
		let $tabPane = $('div.tab-pane.active');
		let preview = NCI.toObjFromElements($tabPane);
		let $preview = $('#divPreview');
		$preview.find('[data-field=subject]')
				.text(preview.subject);
		$preview.find('[data-field=contents]')
				.html(NCI.escapeHtmlBR(preview.contents));
		$preview.find('a[data-field=linkTitle]')
				.text(preview.linkTitle || '')
				.attr('href', preview.linkUrl || '');
		$preview.modal();
		return false;
	}
});
