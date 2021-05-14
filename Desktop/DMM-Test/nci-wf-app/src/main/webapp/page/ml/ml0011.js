var previewMap = {};
$(function() {
	let params = {
			'fileId' : NCI.getQueryString('fileId'),
			'headerId' : NCI.getQueryString('headerId'),
			'version' : NCI.getQueryString('version'),
			'backToStandard' : NCI.getQueryString('backToStandard') || false,
			'messageCds' : ['MSG0149']
	};
	NCI.init('/ml0011/init', params).done(function(res) {
		if (res && res.success) {
			init(res);

			// 更新ボタン押下
			$('#btnUpdate').prop('disabled', false).click(update);
			// 標準へ戻すボタン押下
			$('#btnBackToStandard').prop('disabled', !res.canBackToStandard).click(backToStandard);
			// プレビューボタン押下
			$('#btnPreview').prop('disabled', false).click(preview);
			// メールテンプレートファイル名のリンク押下
			$('#mailTemplateFilename').click(redirectFile);

			// メール変数ボタン押下で現れるドロップダウンを選択
			$(document)
				.on('click', 'ul.dropdown-menu>li>a[data-code]', insertVariable);
		}
	});
	// 戻るボタン押下
	$('#btnBack').click(function(ev) {
		NCI.redirect('./ml0010.html');
	});

	/** カーソル位置にメール変数を挿入 */
	function insertVariable(ev) {
		// トリガーとなったリンク
		let $link = $(ev.target);
		// リンクが示す対象エレメントで、アクティブなタブ上にあるもの
		let dataField = $link.data('field');
		let $target = $('#tabContents div.tab-pane.active [data-field=' + dataField + ']');
		{
			// キャレット位置にテキストを挿入
			let target = $target[0];
			let str = '${' + $link.data('code') + '}';
			if (navigator.userAgent.match(/MSIE/)) {
				var r = document.selection.createRange();
				r.text = str;
				r.select();
			} else {
				var s = target.value;
				var p = target.selectionStart;
				var np = p + str.length;
				target.value = (s.substr(0, p) + str + s.substr(p));
				target.setSelectionRange(np, np);
			}
		}
		return false;
	}

	/** 初期化 */
	function init(res) {
		// データを反映
		NCI.toElementsFromObj(res.header, $('#frmHeader'));

		// タブ
		let $tabUI = $('#tabUI').empty();
		let $tabContents = $('#tabContents').empty();
		let $template = $('#tabTemplate>div.tab-pane');
		for (let i = 0; i < res.bodies.length; i++) {
			let body = res.bodies[i];
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
			$panel.find('input[data-field=mailSubject]').val(body.mailSubject);
			$panel.find('input[data-field=mailBody]').val(body.mailBody);
			$panel.appendTo($tabContents);
			NCI.toElementsFromObj(body, $panel);
		}

		// メール変数挿入用のボタン
		let $buttons = $('#buttonContents');
		let $dropdownContents = $('#dropdownContents');
		$.each(res.variables, function(i, v) {
			let $wrapper = $('<div class="dropdown"></div>');
			// ボタン
			$('<button class="btn btn-default btn-block dropdown-toggle" data-toggle="dropdown"></button>')
				.attr('data-code', v.value)
				.attr('title', "${" + v.value + "}")
				.append(v.label)
				.appendTo($wrapper);
			// ボタン内の選択肢(ポップアップ)
			$dropdownContents
				.clone(true)
				.removeAttr('id')
				.appendTo($wrapper)
				.find('a').attr('data-code', v.value);
			$buttons.append($wrapper);
		});
	}

	/** 更新処理 */
	function update(ev) {
		// バリデーション
		let $targets = $('input, textarea');
		if (!Validator.validate($targets, true)) {

			// タブ配下でエラーがあれば、そのタブをアクティブにする
			let $tabPane = $('.has-error').closest('div.tab-pane');
			let localeCode = $tabPane.find('span[data-field=localeCode]').text();
			$('a[href=\\#tab-' + localeCode + ']').click();
			return false;
		}

		// 確認メッセージ
		let msg = NCI.getMessage('MSG0069', NCI.getMessage('mailTemplate'));
		NCI.confirm(msg, function() {
			// 更新処理
			let header = NCI.toObjFromElements($('#frmHeader'));
			let bodies = [];
			$('#tabContents').find('div.tab-pane').each(function() {
				let $tabPane = $(this);
				let body = NCI.toObjFromElements($tabPane);
				bodies.push(body);
			});
			let params = {
				"header" : header,
				"bodies" : bodies
			};
			NCI.post('/ml0011/save', params).done(function(res) {
				if (res && res.success) {
					$('#btnBack').click();
				}
			})
		});
	}

	/** 標準へ戻す */
	function backToStandard(ev) {
		NCI.confirm(NCI.getMessage('MSG0149'), function() {
			NCI.redirect('./ml0011.html' +
					'?fileId=' + NCI.getQueryString('fileId') +
					'&headerId=' + NCI.getQueryString('headerId') +
					'&version=' + NCI.getQueryString('version') +
					'&backToStandard=true');
		});
	}

	function preview(ev) {
		let $button = $(ev.currentTarget);
		let $tabPane = $('div.tab-pane.active');
		let params = {
				"mailSubject" : $tabPane.find('input[data-field=mailSubject]').val(),
				"mailBody" : $tabPane.find('textarea[data-field=mailBody]').val(),
				"localeCode" : $tabPane.find('span[data-field=localeCode]').text()
		};
		NCI.post('/ml0011/preview', params).done(function(res) {
			if (res && res.success) {
				$('#divPreview').find('input[data-field=mailSubject]').val(res.mailSubject);
				$('#divPreview').find('textarea[data-field=mailBody]').val(res.mailBody);
				$('#divPreview').modal();
			}
		});
		return false;
	}

	function redirectFile(ev) {
		NCI.redirect('./ml0030.html'
				+ '?fileId=' + $('#mailTemplateFileId').val()
				+ '&fileVersion=' + $('#mailTemplateFileVersion').val()
				+ '&headerId=' + $('#mailTemplateHeaderId').val()
				+ '&headerVersion=' + $('#version').val());
	}
});
