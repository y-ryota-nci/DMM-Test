const MM0003_INPUTED = "#MM0003_INPUTED#";
const MM0004_SELECTED = "#MM0004_SELECTED#";

$(function() {

	// 遷移元からの受信パラメータ
	const params = {
		"corporationCode" : NCI.getQueryString("corporationCode"),
		"userCode" : NCI.getQueryString("userCode"),
		"timestampUpdated" : NCI.getQueryString("timestampUpdated"),
		"messageCds" : ['user']
	};
	NCI.init('/mm0003/init', params).done(function(res) {
		if (res && res.success) {
			// 言語の選択肢
			NCI.createOptionTags($('#defaultLocaleCode'), res.localeCodes);

			// 別画面から戻ってきた場合は、直前の入力内容を復元
			const inputed = loadInputed();
			if (inputed
					&& inputed.user.corporationCode === params.corporationCode
					&& inputed.user.userCode === params.userCode
			) {
				setResults(inputed);

				// ユーザ所属編集画面から戻ってきたときは、ユーザ所属を反映
				const mm0004inputed = NCI.flushScope(MM0004_SELECTED);
				if (mm0004inputed
						&& mm0004inputed.corporationCode === params.corporationCode
						&& mm0004inputed.userCode === params.userCode
				) {
					callbackFromMm0004(mm0004inputed);
				}
			} else {
				setResults(res);
			}
		}

		// カレンダー（年月日）
		//	初期状態だとフォーカスアウト時にblankを保存してしまうため位置を修正
		NCI.ymdPicker($('input.ymdPicker'));

	});

	$(document)
		// 戻るボタン押下
		.on('click', '#btnBack', function() {
			let url = "";
			if (NCI.getQueryString('displayValidOnly')) {
				// 「有効データのみ表示」が初期パラメータならプロファイル管理画面へ戻る
				url += "./mm0000.html?displayValidOnly=" + NCI.getQueryString('displayValidOnly');
				url += "&corporationCode=" + NCI.getQueryString('corporationCode');
				url += "&organizationCode=" + NCI.getQueryString('organizationCode');
				url += "&baseDate=" + NCI.getQueryString('baseDate');
			} else {
				// その他はユーザ一覧へ戻る
				url += "./mm0440.html";
			}
			NCI.redirect(url);
		})
		// 所属の行選択チェックボックス
		.on('change', 'input[type=checkbox].selectable', function() {
			const $checked = $('#userBelongInfo').find('input[type=checkbox].selectable:checked');
			$('#btnRemoveBelong').prop('disabled', $checked.length === 0);
		})
		// 所属の主務ラジオボタン
		.on('change', 'input[type=radio][name=jobType]', function() {
			// 主務は削除できないので、そのチェックボックスを無効化
			$('input[type=radio][name=jobType]').each(function() {
				const $checkbox = $(this).parent().parent().find('input[type=checkbox].selectable');
				if (this.checked)
					$checkbox.prop('disabled', true).prop('checked', false);
				else
					$checkbox.prop('disabled', false);
			});
		})
		// 更新ボタン押下
		.on('click', '#btnUpdateUser', function(e) {
			if (!validate()) {
				return false;
			}

			NCI.confirm(NCI.getMessage('MSG0071', NCI.getMessage('user')), function() {
				var inputs = toObjFromInputed();
				NCI.post('/mm0003/update', inputs).done(function(res) {
					if (res && res.success) {
						setResults(res);
					}
				});
			});
		})
		// 削除ボタン押下
		.on('click', '#btnRemoveUser', function() {
			NCI.confirm(NCI.getMessage('MSG0072', NCI.getMessage('user')), function() {
				var inputs = toObjFromInputed();
				NCI.post("/mm0003/delete", inputs).done(function(res) {
					if (res && res.success) {
						setResults(res);
					}
				});
			});
			return false;
		})
		// ユーザ所属リンク
		.on('click', 'a[data-field=seqNoUserBelong], a[data-field=organizationName], a[data-field=postName]', function() {
			// 入力内容を保存
			saveInputed();

			// 遷移先で参照するため、選択内容を保存
			const $tr = $(this).parent().parent();
			const belong = NCI.toObjFromElements($tr);
			belong.jobType = belong.jobType || '2';	// jobTypeが未チェックだと nullになってしまうので、その補正
			Popup.open("./mm0004.html", callbackFromMm0004, belong);
			return false;
		})
		// 所属の追加ボタン押下
		.on('click', '#btnAddBelong', function() {
			// 入力内容を保存
			saveInputed();

			let min = 0;
			$('#userBelongInfo').find('table.responsive tbody tr').each(function(i, tr) {
				const $tr = $(tr);
				const seqNoUserBelong = +($tr.find('a[data-field=seqNoUserBelong]').text());
				if (seqNoUserBelong < min) {
					min = seqNoUserBelong;
				}
			});
			// 遷移先で参照するため、初期内容を保存
			const belong = {
				  "corporationCode": $('#corporationCode').val()
				, "userCode": $('#userCode').val()
				, "userName" : $('#userName').val()
				, "seqNoUserBelong" : (min - 1)
				, "immediateManagerFlag": "0"
				, "jobType" : "2"
				, "validStartDate": NCI.today()
				, "validEndDate": "2099/12/31"
			};
			Popup.open("./mm0004.html", callbackFromMm0004, belong);
			return false;
		})
		// 所属の削除ボタン押下
		.on('click', '#btnRemoveBelong', function() {
			const $checked = $('#userBelongInfo').find('input.selectable:checked');
			$checked.each(function() {
				// メモリ上から削除するのみ、実際の削除は更新ボタン押下時だ
				const $tr = $(this).parent().parent();
				$tr.remove();
			});
		})
	;

	/** バリデーション実行 */
	function validate() {
		const result = Validator.validate($('input, textarea, select'), true);
		return result;
	}

	/** 検索結果を反映 */
	function setResults(res) {
		// ユーザ情報
		if (res.user) {
			$.each(res.user, function(prop, value) {
				$('#' + prop).val(res.user[prop]);
			});
		}
		// ユーザ所属
		const $belong = $('#userBelongInfo');
		const ubPager = new Pager( $belong );
		ubPager.fillTable(res.belongs);
		// 主務なら削除できなくする
		makeUnselectableIfJobTypeMain();

		// 所有予定のロール
		const mrPager = new Pager( $('#menuRoleInfo') );
		mrPager.fillTable(res.menuRoles);

		// 代理しているユーザ
		const atPager = new Pager( $('#authTransferInfo') );
		atPager.fillTable(res.authTransfers);

		// 代理してもらっているユーザ
		const ratPager = new Pager( $('#reverseAuthTransferInfo') );
		ratPager.fillTable(res.reverseAuthTransfers);

		// ボタン制御
		$('button.default-enabled').prop('disabled', false);
		$('button.default-disabled').prop('disabled', true);
	}
});

/** ユーザ所属編集画面からのコールバック関数 */
function callbackFromMm0004(belong) {
	const ubPager = new Pager( $('#userBelongInfo') );
	if (belong) {
		// 既存ならユーザ所属連番が一致する行へ反映
		const labels = ubPager.getHeaderLabels();
		let found = false;
		$('#userBelongInfo').find('table.responsive tbody tr').each(function(i, tr) {
			const $tr = $(tr);
			if ($tr.find('a[data-field=seqNoUserBelong]').text() == belong.seqNoUserBelong) {
				ubPager.fillRowResult($tr, i, belong, labels);
				found = true;
				return false;
			}
		});
		// 該当するユーザ所属連番がなければ新規
		if (!found) {
			ubPager.addRowResult(belong);
		}

		// 主務なら削除できなくする
		makeUnselectableIfJobTypeMain();
	}
}

/** 入力内容をObject化 */
function toObjFromInputed() {
	const inputs = {
		"user" : NCI.toObjFromElements($('#userInfo')),
		"belongs": NCI.toArrayFromTable($('#userBelongInfo')),
		"baseDate" : NCI.getQueryString('baseDate')
	};
	return inputs;
}

/** URIをキーとして、入力内容を保存 */
function saveInputed() {
	const inputed = toObjFromInputed();
	NCI.flushScope(MM0003_INPUTED, inputed);
}

/** URIをキーとして、前回入力内容をロード */
function loadInputed() {
	return NCI.flushScope(MM0003_INPUTED);
}

/** 主務なら削除できなくする */
function makeUnselectableIfJobTypeMain() {
	const $belong = $('#userBelongInfo');
	$belong.find('input[name=jobType]').each(function(i, radio) {
		const cbox = $(radio).closest('tr').find('input.selectable');
		if (radio.checked)
			cbox.prop({'disabled':true, 'checked':false });
		else
			cbox.prop({'disabled':false });
	});
}