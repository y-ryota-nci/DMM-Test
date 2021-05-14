const MM0004_SELECTED = "#MM0004_SELECTED#";

$(function() {

	NCI.init('/mm0004/init').done(function(res) {		// 権限判定のみ
		if (res && res.success) {

			// 主務兼務の選択肢
			if (res.jobTypeList) {
				NCI.createOptionTags($('#jobType'), res.jobTypeList);
			}

			// 遷移元からの受信パラメータ
			const belong = NCI.flushScope(NCI.getQueryString(FLUSH_SCOPE_KEY));
			if (belong) {
				$('#belong').find('input[id], select[id], span[id]').each(function() {
					var propName = this.id;
					var val = belong[propName];
					if (this.tagName === 'SPAN')
						$(this).text(val == null ? '' : val);
					else if (this.type === 'checkbox')
						this.checked = (val == '1');	// immediateManagerFlag
					else
						this.value = (val == null ? '' : val);
				});
			}
			$('#btnInput').prop('disabled', false);
		}

		// カレンダー（年月日）
		//	初期状態だとフォーカスアウト時にblankを保存してしまうため位置を修正
		NCI.ymdPicker($('input.ymdPicker'));

	});

	$(document)
	// 入力ボタン
	.on('click', '#btnInput', function() {
		if (!Validator.validate($('input, select'), true)) {
			return false;
		}

		// 選択内容をFlushScopeへ保存
		var belong = {}
		$('#belong').find('input[id], select[id], span[id]').each(function() {
			if (this.tagName === 'SPAN')
				belong[this.id] = $(this).text();
			else if (this.type === 'checkbox')
				belong[this.id] = this.checked ? '1' : '0';
			else
				belong[this.id] = this.value;
		});
		Popup.close(belong);
	})
	// 戻るボタン
	.on('click', '#btnClose', function() {
		Popup.close();
	})
	// 組織選択ボタン
	.on('click', '#btnSelectOrg:enabled', function() {
		var params = null;
		Popup.open("../cm/cm0020.html?corporationCode=" + $("#corporationCode").val(), callbackFromCm0020, params, this);
	})
	// 役職ボタン
	.on('click', '#btnSelectPost:enabled', function() {
		var params = null;
		Popup.open("../cm/cm0030.html?corporationCode=" + $("#corporationCode").val(), callbackFromCm0030, params, this);
	})
	// クリアボタン
	.on('click', '.clear-input-group:enabled', function() {
		$(this).closest('.input-group').find('input[type=text],span').val('').text('');
	})
});

/** 組織選択画面からのコールバック関数 */
function callbackFromCm0020(org, trigger) {
	if (org) {
		$('#organizationCode').val(org.organizationCode).trigger('validate');
		$('#organizationName').val(org.organizationName).trigger('validate');
	}
}

/** 役職選択画面からのコールバック関数 */
function callbackFromCm0030(post, trigger) {
	if (post) {
		$('#postCode').val(post.postCode).trigger('validate');
		$('#postName').val(post.postName).trigger('validate');
	}
}
