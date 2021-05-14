// 承認状況
$(function() {
	// VD0310から呼び出される前提なので、Vd0310Contentsが必須である
	let parameters = NCI.flushScope(NCI.getQueryString(FLUSH_SCOPE_KEY));
	parameters.messageCds = ['parallelStart', 'parallelEnd', 'skip', 'abort'];
	NCI.init('/vd0170/init', parameters).done(function(res) {
		if (res && res.success && res.activityList) {
			$('#processDefName').text(
					parameters.contents.processDefCode
					+ '-' + parameters.contents.processDefDetailCode
					+ ' : ' + parameters.contents.processDefName);

			// HTML描画
			renderHtml(res.activityList);

			// 矢印の描画
			drawConnectors();

			// 画面リサイズ用
			$(window).resize(function() {
				jsPlumb.repaintEverything();
			});
		}
	});

	$('#btnClose').click(function() {
		Popup.close();
	});

	/** HTMLを生成 */
	function renderHtml(activityList) {
		let len = activityList.length;
		let $route = $('#route');

		// 最上位のルート情報を描画
		for (let i = 0; i < len; i++) {
			let activity = activityList[i];
			let activityType = activity.activityType || '';
			let $activity = null;
			if (activityType == 'parallel_s') {
				// 並行分岐の開始
				$activity = createParallel(activity);
			} else if (activityType == 'parallel_e') {
				// 並行分岐の終了
				continue;
			} else {
				// 単独のアクティビティ
				$activity = createActivity(activity);
			}

			if ($activity != null)
				$route.append($activity);
		}
	}

	/** 並行分岐アクティビティの開始～終了までを生成 */
	function createParallel(activity) {
		let len = activity.parallels.length;
		let $table = $('<table class="parallel">');

		// 並行分岐の開始
		let $trHead = $('<tr>').appendTo($table)
				.attr('data-activity-def-code', activity.activityDefCode);
		if (activity.activityId > 0)
			$trHead.attr('data-activity-id', activity.activityId);
		$('<th>')
			.attr('colspan', len)
			.text(NCI.getMessage('parallelStart'))
			.appendTo($trHead);

		// 並行分岐内のルートリスト
		let startActivityDefCodeTransits = activity.activityDefCodeTransits;
		let $trBody = $('<tr>').appendTo($table);
		let $td = null;
		for (let i = 0; i < len; i++) {
			// 並行分岐内の起点アクティビティなら、列を分ける
			let childS = activity.parallels[i];
			if (startActivityDefCodeTransits.indexOf(childS.activityDefCode) >= 0) {
				$td = $('<td>').appendTo($trBody);
			}

			let activityType = childS.activityType;
			let $contents = null;
			if (activityType == 'parallel_s') {
				let childE = findActivityParallelEnd(activity.parallels, i + 1);
				$contents = createParallel(childS, childE);
			}
			else if (activityType == 'parallel_e') {
				continue;
			}
			else {
				$contents = createActivity(childS);
			}

			if ($contents != null) {
				$td.append($contents);
			}
		}

		// 並行分岐の終了
		let $trFoot = $('<tr>').appendTo($table)
				.attr('data-activity-def-code', activity.endActivityDefCode);
		if (activity.endActivityId > 0)
			$trFoot.attr('data-activity-id', activity.endActivityId);
		if (activity.endActivityIdNext > 0)
			$trFoot.attr('data-activity-id-next', activity.endActivityIdNext);
		if (activity.endActivityDefCodeTransits && activity.endActivityDefCodeTransits.length)
			$trFoot.attr('activity-def-code-transits', JSON.stringify(activity.endActivityDefCodeTransits));
		$('<th>')
			.attr('colspan', len)
			.text(NCI.getMessage('parallelEnd'))
			.appendTo($trFoot);
		return $table;
	}

	/** 「並行分岐の終了」アクティビティを返す */
	function findActivityParallelEnd(activityList, startIndex) {
		// アクティビティリストの開始INDEX(＝並行分岐の開始アクティビティの直後)からスキャンして
		// 最初の「並行分岐の終了」
		for (let i = startIndex; i < activityList.length; i++) {
			let activity = activityList[i];
			if (activity.activityType === 'parallel_e')
				return activity;
		}
		return null;
	}

	/** 分岐でないアクティビティを生成 */
	function createActivity(activity) {
		// 参加者名と承認日
		let activityDefName = activity.activityDefName;
		let activityStatus = activity.activityStatus || '';
		let userName = '', approvedDate = '';
		if (activity.assignedUsers && activity.assignedUsers.length) {
			// 承認者
			userName = activity.assignedUsers[0].userName;
			approvedDate = activity.assignedUsers[0].approvedDate;
		}
		else if (activityStatus.indexOf('skip') >= 0) {
			// スキップ
			userName = '(' + NCI.getMessage('skip') + ')';
		}
		else if (activityStatus.indexOf('abort') >= 0) {
			// 中断
			userName = '(' + NCI.getMessage('abort') + ')';
		}

		// アクティビティのアクティビティ定義コードとアクティビティID（コネクタ接続情報）
		let $activity = $('<article class="activity">')
			.attr('data-activity-def-code', activity.activityDefCode);
		if (activity.activityId > 0)
			$activity.attr('data-activity-id', activity.activityId);
		if (activity.activityIdNext > 0)
			$activity.attr('data-activity-id-next', activity.activityIdNext);
		if (activity.activityDefCodeTransits && activity.activityDefCodeTransits.length)
			$activity.attr('activity-def-code-transits', JSON.stringify(activity.activityDefCodeTransits));

		// アクティビティの状態
		if (activity.closeActivity)
			$activity.addClass('closed');
		else if (activity.currentActivity)
			$activity.addClass('current');

		// アクティビティの要素
		$('<div class="activity-name">').text(activityDefName).appendTo($activity);
		$('<div class="assigned-user">').text(userName || ' ').appendTo($activity);
		$('<div class="execution-date">').text(approvedDate || ' ').appendTo($activity);

		return $activity;
	}

	/** コネクタの描画 */
	function drawConnectors() {
		jsPlumb.detachEveryConnection();
		jsPlumb.deleteEveryEndpoint();
		// 矢印コネクタのスタイル
		let connectorStyle = {
				connector:["Flowchart", {}],
				paintStyle:{lineWidth:3,strokeStyle:"#0000ff",outlineWidth:1},
				hoverPaintStyle:{strokeStyle:"#FF0000"},
				endpoint:"Blank",
				endpointStyle:{ fillStyle:"#FFCC99",lineWidth:1 },
				anchor:["Bottom", "Top"],
				overlays: [
					['Arrow', {location: 1, visible: true, width: 15, length: 12, id: 'arrow'}],
					['Label', {location: 0.7, id: 'label'}]
				]
		};
		// 点線コネクタのスタイル
		let dashConnectorStyle = {
				connector:["Flowchart", {}],
				paintStyle:{lineWidth:2,strokeStyle:"#0000ff",outlineWidth:1, dashstyle : "1 1"},
				hoverPaintStyle:{strokeStyle:"#FF0000"},
				endpoint:"Blank",
				endpointStyle:{ fillStyle:"#FFCC99",lineWidth:1 },
				anchor:["Bottom", "Top"],
				overlays: [
					['Arrow', {location: 1, visible: true, width: 15, length: 12, id: 'arrow'}],
					['Label', {location: 0.7, id: 'label'}]
				]
		};

		// コネクタを描画
	    $('[data-activity-id], [data-activity-def-code]').each(function(i, from) {
	    	let activityIdNext = from.getAttribute('data-activity-id-next');
    		let activityDefCodeTransits = JSON.parse($(from).attr('activity-def-code-transits') || null);
	    	if (activityIdNext) {
		    	// アクティビティIDが遷移元／遷移先であれば処理済みなので、実線でコネクタを描画
		    	let to = $('[data-activity-id=' + activityIdNext + ']')[0];
	    		jsPlumb.connect({ source: from, target: to}, connectorStyle);
	    	}
	    	else if (activityDefCodeTransits) {
	    		// 未処理の場合はルート定義上の遷移先／遷移元のアクティビティ定義コードを使って、点線でコネクタを描画
    			for (let i = 0; i < activityDefCodeTransits.length; i++) {
    				let activityDefCodeTransit = activityDefCodeTransits[i];
					let to = $('[data-activity-def-code=' + activityDefCodeTransit + ']');
					if (to && to.length) {
						jsPlumb.connect({ source: from, target: to}, dashConnectorStyle);
					}
    			}
	    	}
	    });
	}
});
