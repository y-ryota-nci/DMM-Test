$(function() {
	var params = {
			corporationCode   : NCI.getQueryString("corporationCode"),
			seqNoAuthTransfer : NCI.getQueryString("seqNoAuthTransfer"),
			messageCds : [ 'MSG0064', 'MSG0066', 'MSG0069', 'MSG0071', 'MSG0072', 'proxyInfo' ]
		};

	NCI.init("/ws0011/init", params).done(function(res) {
		if (res && res.success) {

			if (res.target) {
				$('#corporationCode').val(res.target.corporationCode);
				$('#seqNoAuthTransfer').val(res.target.seqNoAuthTransfer);
				$('#userCode').val(res.target.userCode);
				// 文書種別
				$('#section1').find('[data-field=refProcessDefName]').text(res.target.processDefName).removeClass('hide');
				// 代理元ユーザ
				$('#section3').find('[data-field=refUserNameTransfer]').text(res.target.userNameTransfer).removeClass('hide');
				// 有効期間
				$('#validStartDate').val(res.target.validStartDateTransfer);
				$('#validEndDate').val(res.target.validEndDateTransfer);
				// 各ブロックを表示
				$('#section3, #section4').each(function() {
					$(this).removeClass('hide');
				});
				// ボタン有効化
				$('#btnUpdate, #btnDelete').each(function() {
					$(this).removeClass('hide').prop('disabled', false);
				});

			} else {
				// 文書種別の選択肢
				NCI.createOptionTags($('#processDefCode'), res.processDefs);
				// 最初の選択肢を選択状態にする
				$('#processDefCode').children().first().prop('selected', true);
				//
				$('#section1').find('div.form-group').removeClass('hide');
				$('#section3').find('div.form-group').removeClass('hide');
				// ボタン有効化
				$('#btnRegist').removeClass('hide');
			}
		}

		// カレンダー（年月日）
		//	初期状態だとフォーカスアウト時にblankを保存してしまうため位置を修正
		NCI.ymdPicker($('input.ymdPicker'));

	});

	$(document)
		// 文書種別追加ボタン押下
		.on('click', '#btnAddProcess', function(ev) {
			// 選択した文書種別のvalue,labelを取得
			var val = $('#processDefCode').val();
			var txt = $('#processDefCode option:selected').prop('label');
			var $tbl  = $('#tblProcess');
			// 全ての文書種別を選択しているか
			var all = (val == 'ALL');
			// 既に選択済みか
			var selected = false;
			$('tbody tr', $tbl).each(function() {
				if (all) {
					$(this).remove();
				} else {
					var processDefCode = $(this).find('[data-field=processDefCode]').text();
					if ('ALL' == processDefCode) {
						$(this).remove();
					} else if (val == processDefCode) {
						selected = true;
					}
				}
			});
			if (!selected) {
				var entity = {processDefName : txt, processDefCode : val};
				var $root = $('#section1');
				new ResponsiveTable($root).addRowResult(entity);
				$tbl.removeClass('hide');
			}
			// 代理元ユーザ選択欄を表示
			$('#section3').removeClass('hide');
			// 代理元ユーザが1人でも選択済みなら代理設定を有効にする期間欄も表示、登録ボタンを有効にする
			if ($('tbody tr', $('#tblUser')).length > 0) {
				$('#section4').removeClass('hide');
				disabledBtnRegist();
			}
			return false;
		})
		// 文書種別削除ボタン押下
		.on('click', 'button.btnDeleteProcess', function(ev) {
			// 画面上から削除するのみ
			var $tr = $(this).closest('tr');
			$tr.remove();
			// 削除後、1行もなくなれば決裁区分欄以下を非表示にする
			var $tbl  = $('#tblProcess');
			if ($('tbody tr', $tbl).length == 0) {
				$tbl.addClass('hide');
				$('#section3, #section4').each(function() {
					$(this).addClass('hide');
				});
				// 登録ボタンは無効化
				disabledBtnRegist();
			}
			return false;
		})
		// 代理先ユーザ追加ボタン押下
		.on('click', '#btnAddUser', function(ev) {
			// ユーザ選択画面ポップアップ
			var params = null;
			Popup.open("../cm/cm0040.html", callbackFromCm0040, params, this);
		})
		// 代理先ユーザ削除ボタン押下
		.on('click', 'button.btnDeleteUser', function(ev) {
			// 画面上から削除するのみ
			var $tr = $(this).closest('tr');
			$tr.remove();
			// 削除後、1行もなくなれば代理設定を有効にする期間欄を非表示にする
			var $tbl  = $('#tblUser');
			if ($('tbody tr', $tbl).length == 0) {
				$tbl.addClass('hide');
				$('#section4').addClass('hide');
				// 登録ボタンは無効化
				disabledBtnRegist();
			}
			return false;
		})
		// 登録ボタン押下
		.on('click', '#btnRegist', function(ev) {
			var $root = $('#section4');
			var $targets = $root.find('input');
			if (!Validator.validate($targets, true))
				return false;

			// 必要なデータを書きあつめる
			var processDefCods = [];
			$('tbody tr', $('#tblProcess')).each(function() {
				processDefCods.push( $(this).find('[data-field=processDefCode]').text() );
			});
			var userCodeTransfers = [];
			$('tbody tr', $('#tblUser')).each(function() {
				userCodeTransfers.push( $(this).find('[data-field=userCode]').text() );
			});

			var msg = NCI.getMessage("MSG0069", NCI.getMessage("proxyInfo"));
			if (NCI.confirm(msg, function() {
				var params = {
					 processDefCodes   : processDefCods
					,userCodeTransfers : userCodeTransfers
					,validStartDate    : $('#validStartDate').val()
					,validEndDate      : $('#validEndDate').val()
					,userCode          : $('#userCode').val()
				};
				NCI.post("/ws0011/regist", params).done(function(res) {
					if (res && res.success) {
						let msg = NCI.getMessage("MSG0066", NCI.getMessage("proxyInfo"));
						$('#infomsg').text(msg);
						$('#informDlg').modal();
					}
				});
			}));
		})
		// 更新ボタン押下
		.on('click', '#btnUpdate', function(ev) {
			var $root = $('#section4');
			var $targets = $root.find('input');
			if (!Validator.validate($targets, true))
				return false;

			var msg = NCI.getMessage("MSG0071", NCI.getMessage("proxyInfo"));
			if (NCI.confirm(msg, function() {
				var entry = $('#corporationCode').val()
					+ "\t" + $('#seqNoAuthTransfer').val()
					+ "\t" + $('#validStartDate').val()
					+ "\t" + $('#validEndDate').val();
				NCI.post("/ws0011/update", {entry : entry}).done(function(res) {
					if (res && res.succes) {
					}
				});
			}));
		})
		// 削除ボタン押下
		.on('click', '#btnDelete', function(ev) {
			var msg = NCI.getMessage("MSG0072", NCI.getMessage("proxyInfo"));
			if (NCI.confirm(msg, function() {
				var entry = $('#corporationCode').val() + "\t" + $('#seqNoAuthTransfer').val()
				NCI.post("/ws0011/delete", {entry : entry}).done(function(res) {
					if (res && res.success) {
						let msg = NCI.getMessage("MSG0064", NCI.getMessage("proxyInfo"));
						$('#infomsg').text(msg);
						$('#informDlg').modal();
					}
				});
			}));
		})
		// OKボタン、戻るボタン押下
		.on('click', '#btnBack, #btnInformDlgOK', function() {
			let url = "./ws0010.html";
			let corporationCode = $('#corporationCode').val();
			let userCode = $('#userCode').val();
			if (corporationCode && userCode) {
				url += "?corporationCode=" + corporationCode + "&userCode=" + userCode;
			}
			NCI.redirect(url);
		})
	;
});

/** ユーザ選択画面からのコールバック関数 */
function callbackFromCm0040(user, trigger) {
	if (user) {
		// 選択した文書種別のvalue,labelを取得
		var userCode = user.userCode;
		var userName = user.userName;
		var $tbl  = $('#tblUser');
		// 既に選択済みか
		var selected = false;
		$('tbody tr', $tbl).each(function() {
			if (userCode == $(this).find('[data-field=userCode]').text()) {
				selected = true;
				return;
			}
		});
		if (!selected) {
			var entity = {userCode : userCode, userName : userName};
			var $root = $('#section3');
			new ResponsiveTable($root).addRowResult(entity);
			$tbl.removeClass('hide');
		}
		// 代理設定を有効にする期間欄を表示
		$('#section4').removeClass('hide');
		// 登録ボタンを有効にする
		disabledBtnRegist();
		return false;
	}
}

function disabledBtnRegist() {
	let isProcessType = $('tbody tr', $('#tblProcess')).length > 0;
	let isUser = $('tbody tr', $('#tblUser')).length > 0;
	let disabled = !isProcessType || !isUser;
	$('#btnRegist').prop('disabled', disabled);
}