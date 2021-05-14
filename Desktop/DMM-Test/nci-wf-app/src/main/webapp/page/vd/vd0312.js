let parentParams = NCI.flushScope( NCI.getQueryString(FLUSH_SCOPE_KEY) );
$(function() {
	var params = {
			corporationCode: parentParams.contents.corporationCode,
			processId: parentParams.contents.processId,
			processDefCode: parentParams.contents.processDefCode,
			processDefDetailCode: parentParams.contents.processDefDetailCode,
			startUserInfo: parentParams.startUserInfo,	// 最新の内容
			processUserInfo: parentParams.contents.processUserInfo,
			contents : parentParams.contents,			// 画面ロード直後の内容
			runtimeMap : parentParams.runtimeMap,		// 最新の内容
			messageCds: ['processUserInfo', 'startUserInfo', 'name', 'organization', 'post', 'proxyUser'
						,'step', 'approver', 'proxyUser3']
	};
	// 初期化処理
	NCI.init("/vd0312/init", params).done(function(res) {
		if (res && res.success && res.routeList) {
			// ボタン名を設定
			$('#btnMove').find('span').text(parentParams.actionInfo.actionName);

			// 承認者一覧の生成
			let routeList = res.routeList;
			let $root = $('#vd0312');
			let $tbl = $('#tblRoute');
			let responsiveTable = new ResponsiveTable($root);
			let templates = responsiveTable.getTemplate();
			let $tbody = $('tbody', $tbl);
			$.each(routeList, function(idx, route) {
				let $trs = vd0312.createRow(idx, route, true, responsiveTable, templates);
				$.each($trs, function(idx, $tr) {
					$tbody.append($tr);
				});
			});

			// 各種ボタン、リンク押下時処理
			$(document)
				// ステップ追加ボタン押下
				.on('click', '#btnAddStep', function(ev) {
					let $rdo = $('input[name="selected"]:checked', $('#tblRoute'));
					let $tr  = $rdo.closest('tr');
					if ($tr.length > 0) {
						// ユーザ選択画面ポップアップ
						Popup.open("../cm/cm0040.html", addActivity, null);
					}
				})
				// ステップ削除ボタン押下
				.on('click', '#btnDelStep', function(ev) {
					let $rdo = $('input[name="selected"]:checked', $('#tblRoute'));
					let $tr  = $rdo.closest('tr');
					if ($tr.length > 0) {
						let index = $tr.index();
						let rowspan = $('th', $tr).prop('rowspan');
						// 変更になったアクティビティの行を削除
						// ※参加者が複数いる場合、複数行分削除する必要がある
						$('tr', $tbody)
							.filter(function(idx){ return (idx >= index && idx < index + rowspan); })
							.each(function() { $(this).remove(); });
					}
				})
				// ラジオボタン切替
				.on('click', 'input[type="radio"]', function() {
					$('#btnAddStep').prop('disabled', false);
					$('#btnDelStep').prop('disabled', $(this).hasClass('undeletable'));
				})
				// 承認者変更
				.on('click', 'tr.normal a[data-field=activityDefName]', function() {
					let $tr = $(this).closest('tbody tr');
					let flag = $('[data-field=existChangeDefFlag]', $tr);

					if (flag.val() == "true") {
						// 押下された場所を特定し、コールバックされる際の戻り値として呼出先から戻してもらう
						let index = $tr.index();
						let params = {callbackParam : index};
						// ユーザ選択画面ポップアップ
						let str = "?corporationCode="	 + parentParams.contents.corporationCode;
						str += "&processDefCode="		 + parentParams.contents.processDefCode;
						str += "&processDefDetailCode="	 + parentParams.contents.processDefDetailCode;
						str += "&activityDefCode="		 + $('[data-field=activityDefCode]', $tr).val();
						str += "&organizationCodeStart=" + parentParams.startUserInfo.organizationCode;
						Popup.open("../cm/cm0100.html" + str, changeAssignedUsers, params);
					} else {
						// 押下された場所を特定し、コールバックされる際の戻り値として呼出先から戻してもらう
						let index = $tr.index();
						let params = {callbackParam : index};
						// ユーザ選択画面ポップアップ
						Popup.open("../cm/cm0040.html", changeAssignedUsers, params);
					}
				})
				// 平行承認
				.on('click', 'tr.parallel a[data-field=activityDefName]', function() {
					let $tr = $(this).closest('tr');
					vd0312.onClickParallelLink($tr, $tbody, false);
				})
				// 閉じるボタン押下
				.on('click', '#btnClose', function(ev) {
					Popup.close();
				})
				// 申請ボタン
				.on('click', '#btnMove', function() {
					let msg = parent.vd0310.getConfirmMsg(parentParams.actionInfo);
					NCI.confirm(msg, function() {
						// 承認ルート情報をかき集める
						let changeRouteList = [];
						let rowIndex = 0;
						$('tr', $tbody).each(function() {
							let $tr = $(this);
							let index = $tr.index();
							if (index >= rowIndex) {
								let route = vd0312.toRouteFromElements($tr, $tr.index(), $tbody);
								changeRouteList.push(route);
								rowIndex = $tr.index() + route.assignedUserList.length;
							}
						});
						parentParams.changeRouteList = changeRouteList;

						// コールバック関数の呼び出し
						Popup.close(parentParams);
					});
				}
			);
		}
	});
});

/** ステップ追加のコールバック処理 */
function addActivity(user) {
	if (user) {
		let users = user;
		// 承認者変更を行った行を特定
		let $tbl = $('#tblRoute');
		let $tbody = $('tbody', $tbl);
		let $rdo = $('input[name="selected"]:checked', $tbl);
		let $target = $rdo.closest('tr');
		let index = $target.index();
		if ($target) {
			let route = vd0312.toActivityFromElements($target);
			let req = vd0312.createReq(route, users);
			NCI.post("/vd0312/add", req).done(function(res) {
				if (res && res.success && res.route) {
					let $root = $('#vd0312');
					let responsiveTable = new ResponsiveTable($root);
					let templates = responsiveTable.getTemplate();
					// 行を生成
					let $trs = vd0312.createRow(index, res.route, false, responsiveTable, templates);
					// 追加する位置をきめる
					let parentKey = $('[data-field="parentKey"]', $target).val();
					let $insert = $('tr', $tbody)
									.filter(function(idx) { return (idx > index); })
									.filter(function() { return ($('[data-field="parentKey"]', $(this)).length > 0); })
									.filter(function() { return ($('[data-field="parentKey"]', $(this)).val() == parentKey || $('[data-field="parentKey"]', $(this)).val() == ''); })
									.first();
					// 行挿入
					if ($insert.length == 0) {
						$.each($trs, function(idx, $tr) {
							$tbody.append($tr);
						});
					} else {
						$.each($trs, function(idx, $tr) {
							$insert.before($tr);
						});
					}
				}
			});
		}
	}
}

/** 参加者変更時のコールバック処理. */
function changeAssignedUsers(result) {
	if (result) {
		let users = result.users;
		let index = result.callbackParam;
		// 承認者変更を行った行を特定
		let $tbl = $('#tblRoute');
		let $tbody = $('tbody', $tbl);
		let $target = $('tr', $tbody).eq(index);
		if ($target) {
			let route = vd0312.toActivityFromElements($target);
			let req = vd0312.createReq(route, users);
			NCI.post("/vd0312/change", req).done(function(res) {
				if (res && res.success && res.route && res.route.assignedUserList && res.route.assignedUserList.length > 0) {
					let $root = $('#vd0312');
					let responsiveTable = new ResponsiveTable($root);
					let templates = responsiveTable.getTemplate();
					let rowspan = $('th', $target).prop('rowspan');
					// 変更になったアクティビティの行を削除
					// ※参加者が複数いる場合、複数行分削除する必要がある
					$('tr', $tbody).filter(function(idx){ return (idx > index && idx < index + rowspan); }).each(function() { $(this).remove(); });
					// 行を生成
					let $trs = vd0312.createRow(index, res.route, false, responsiveTable, templates);
					$target.replaceWith($trs);
				}
			});
		}
	}
}

var vd0312 = {
	/**  */
	ignores1 : ['selected', 'userName', 'organizationName', 'postName', 'proxyUsers', 'corporationCode', 'organizationCode', 'postCode', 'userCode'],
	/**  */
	ignores2 : ['selected', 'activityDefName', 'id', 'activityDefCode', 'parentKey', 'existChangeDefFlag', 'addedActivity', 'changeActivity'],

	/**
	 * ステップ追加、承認者変更の際のリクエスト生成.
	 * @param route ルート情報
	 * @param user 承認者(ユーザ)情報
	 */
	createReq: function(route, users) {
		var req = {
			corporationCode: parentParams.contents.corporationCode,
			processId: parentParams.contents.processId,
			processDefCode: parentParams.contents.processDefCode,
			processDefDetailCode: parentParams.contents.processDefDetailCode,
			target: route,
			addAssignedUsers: []
		};
		if ($.isArray(users)) {
			$.each(users, function(i, elem) {
				var userInfo = {
						corporationCode  : elem.corporationCode,
						organizationCode : null,
						organizationName : null,
						postCode         : null,
						postName         : null,
						userCode         : elem.userCode,
						userName         : elem.userName,
						userAddedInfo    : elem.userAddedInfo
					};
				req.addAssignedUsers.push(userInfo);
			});
		} else {
			var userInfo = {
					corporationCode  : users.corporationCode,
					organizationCode : null,
					organizationName : null,
					postCode         : null,
					postName         : null,
					userCode         : users.userCode,
					userName         : users.userName,
					userAddedInfo    : users.userAddedInfo
				};
			req.addAssignedUsers.push(userInfo);
		}
		return req;
	},

	/** 承認ルートのアクティビティ1つに対する行を生成 */
	createRow : function(rowIndex, route, init, responsiveTable, templates) {
		let $trs = [];

		// 並行承認・連携フローアクティビティの場合
		if (route.branchStartActivity) {
			let $tr = $(responsiveTable.toNewLine(templates)[0]);
			vd0312.fillRowResultActivity($tr, rowIndex, route, 0, init)
			$trs.push($tr);

			// 並行承認内のルート情報を再帰呼出
			if (route.children && route.children.length > 0) {
				$.each(route.children, function(idx, child) {
					let $ctrs = vd0312.createRow(++rowIndex, child, init, responsiveTable, templates);
					$.each($ctrs, function(idx, $tr) {
						$trs.push($tr);
					});
				});
			}
		}
		// 通常アクティビティの場合
		else {
			// 承認者の行数分作成
			$.each(route.assignedUserList, function(idx, usr) {
				let $tr = $(responsiveTable.toNewLine(templates)[1]);
				$tr.find('input[type=radio]').prop('disabled', false);
				vd0312.fillRowResultActivity($tr, rowIndex, route, idx, init)
				vd0312.fillRowResultAssigned($tr, usr, route)
				$trs.push($tr);
				rowIndex++;
			});
		}

		return $trs;
	},

	/**
	 * 承認ルートのアクティビティに対する情報を反映.
	 * @param $tr 行データ
	 * @param rowIndex 行番号
	 * @param route ルート情報
	 * @param idx １アクティビティ内の参加者に対する番号（0～）
	 * @param init 初期表示か
	 */
	fillRowResultActivity : function($tr, rowIndex, route, idx, init) {
		// 並行承認内にあるアクティビティには親キーを設定
		if (route.parentKey != null) {
			// 初期表示時は非表示に
			if (init) {
				$tr.addClass('hide ' + route.parentKey);
				$tr.find('input[type=radio]').prop('disabled', true);
			} else {
				$tr.addClass(route.parentKey);
			}
		}
		// 行の背景色の設定
		if (route.currentActivity) {
			$tr.addClass('current');
		} else if (route.closeActivity) {
			if (rowIndex%2 == 0) {
				$tr.addClass('closed-even');
			} else {
				$tr.addClass('closed-odd');
			}
		}
		// 行の最初のカラムを取得
		let $th = $tr.children().eq(0);
		if (idx == 0) {
			// アクティビティに関連する項目をセット
			NCI.toElementsFromObj(route, $tr, vd0312.ignores1);

			// ラジオボタンの制御
			// 終了していないアクティビティはラジオボタンを表示
			// 並行承認アクティビティは対象外（ver5で並行承認の後への追加はできなくなったようだ）
			if (!route.closeActivity && !route.branchStartActivity) {
				let $rdo = $th.find('input[name=selected]');
				$rdo.removeClass('invisible');
				// さらに削除可能なアクティビティは"undeletable"も除去
				if (!route.currentActivity && route.deletableActivity) {
					$rdo.removeClass('undeletable');
				}
			}
			// カレントアクティビティ、終了アクティビティ、承認者変更不可アクティビティ
			if (route.branchStartActivity) {
				// 何もしない
			} else if (route.currentActivity || route.closeActivity) { // TODO 鷲田さんの修正待ち || !route.changeableActivity) {
				$th.find('span[data-field=activityDefName]').removeClass('hide');
			} else {
				$th.find('a[data-field=activityDefName]').removeClass('hide');
			}
			// rowspanを設定
			let len = route.assignedUserList.length;
			if (len > 1) {
				$th.attr('rowspan', len);
			}
		} else {
			$th.remove();
		}
	},

	/**
	 * 参加者(承認者)に対する情報を反映.
	 * @param $tr 行データ
	 * @param usr 参加者(承認者)情報
	 * @param route ルート情報
	 */
	fillRowResultAssigned : function($tr, usr, route) {
		// 参加者に関連する項目を設定
		NCI.toElementsFromObj(usr, $tr, vd0312.ignores2);
		// 矢印の表示
		if (route.currentActivity && usr.showArrow) {
			$tr.find('span.arrow').removeClass('invisible');
		}
	},

	/**
	 * 行からアクティビティ、参加者情報を取得し、1つのルート情報を生成して返す.
	 * @param $tr 行データ
	 * @return
	 */
	toRouteFromElements : function($tr, index, $tbody) {
		let route = vd0312.toActivityFromElements($tr);
		let rowspan = 1;
		if ($('th', $tr).prop('rowspan')) {
			rowspan = $('th', $tr).prop('rowspan');
		}
		let assigns = [];
		$('tr', $tbody)
			.filter(function(idx) {
				return (idx >= index && idx < index + rowspan);
			})
			.each(function() {
				let assign = vd0312.toAssignsFromElements($(this));
				assigns.push(assign);
			});
		route.assignedUserList = assigns;
		return route;
	},

	/**
	 * 行からアクティビティ情報のみを抽出しオブジェクトにして返す
	 * @param $tr 行データ
	 * @return
	 */
	toActivityFromElements : function($tr) {
		// 無視すべきカラム名の配列
		return NCI.toObjFromElements($tr, vd0312.ignores1);
	},

	/**
	 * 行からアクティビティ情報のみを抽出しオブジェクトにして返す
	 * @param $tr 行データ
	 * @return
	 */
	toAssignsFromElements : function($tr) {
		return NCI.toObjFromElements($tr, vd0312.ignores2);
	},

	/**
	 * 並行承認リンク押下時処理.
	 * @param $tr
	 * @param $tbody
	 * @param flg 親の並行承認リンクの開閉フラグ trueなら閉じる
	 */
	onClickParallelLink : function($tr, $tbody, flg) {
		let key = $('[data-field=activityDefCode]', $tr).val();
		let selector = 'tr.' + key;
		$(selector, $tbody).each(function() {
			let $target = $(this);
			if (!flg && $target.hasClass('hide')) {
				$target.removeClass('hide');
				$target.find('input[type=radio]').prop('disabled', false);
			} else {
				$target.addClass('hide');
				$target.find('input[type=radio]').prop('disabled', true);
				if ($target.find('input[type=radio]').prop('checked')) {
					$target.find('input[type=radio]').prop('checked', false);
					$('#btnAddStep').prop('disabled', true);
					$('#btnDelStep').prop('disabled', true);
				}
				if ($target.hasClass('parallel')) {
					// 親が閉じた場合は子供は常に閉じるよ
					vd0312.onClickParallelLink($target, $tbody, true);
				}

			}
		});
	}
};
