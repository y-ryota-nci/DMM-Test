$(function() {
	// ワークリストは初期表示で自分のTODOリストを表示するので、初期表示＝検索である
	var pager = new Pager($('#seach-result'), '/wl0130/search', search).init();
	NCI.init("/wl0130/init").done(function(res) {
		if (res && res.success) {

			// サイドメニュー表示
			$("#sideMenuArea").empty();
			$("#sideMenuArea").append('<div class="contents_left">');

			$("div.contents_left").append('<ul class="lv1">');

			$("ul.lv1").append('<li id="workList" class="child_lv2">ワークリスト');
			$("#workList").append('<ul id="workList_child" class="lv2">')

			$("ul.lv1").append('<li id="ownList" class="child_lv2">自案件一覧');
			$("#ownList").append('<ul id="ownList_child" class="lv2">')

			$("ul.lv1").append('<li id="allList" class="child_lv2">汎用案件一覧');
			$("#allList").append('<ul id="allList_child" class="lv2">')

			res.userDisplayList.forEach(function(userDisp) {
				if ("1" == userDisp.screenType) {
					$("#workList_child").append('<li><a id=workListLink_' + userDisp.userDisplayId + ' class="workListLink">' + userDisp.userDisplayName + '</a></li>')
				} else if ("2" == userDisp.screenType) {
					$("#ownList_child").append('<li><a id=ownListLink_' + userDisp.userDisplayId + ' class="ownProcessListLink">' + userDisp.userDisplayName + '</a></li>')
				} else if ("3" == userDisp.screenType) {
					$("#allList_child").append('<li><a id=allListLink_' + userDisp.userDisplayId + ' class="allProcessListLink">' + userDisp.userDisplayName + '</a></li>')
				}
			});

			$("div.contents_left").append('<p><a href="#" class="sideUnderButton">検索条件 / 検索一覧</a></p>');

			// プロセス定義の選択肢
			NCI.createOptionTags($('#processDefCode'), res.processDefs);

			// 以前の検索条件を復元
			let initSearch = pager.loadCondition();

			$('#userDisplayId').val('-1');
			$('#workListLink_-1').click();

			if (initSearch) {
				search();
			}
		}
	});

	$(document).on('click', '#btnSearch', function() {
		// 検索ボタン押下
		search(1);
		return false;
	})
	.on('click', 'ul.pagination a', function() {
		// ページ番号リンク押下
		var pageNo = this.getAttribute('data-pageNo');
		search(pageNo);
		return false;
	})
	.on('click', '#btnReset', function() {
		$('#formCondition')[0].reset();
	})

	// 以下は仮で実装
	.on('click', '#sideMenuToggle', function() {
		var margin = 250;
		if(jQuery('#sideMenuArea').is(':visible')){
			margin = 0;
		}

		jQuery("#sideMenuArea").animate(
			{width: "toggle"},
			{duration: 0}
		);
		jQuery("#searchArea").animate(
			{marginLeft: margin + "px"},
			{duration: 0}
		);

		// サイドメニューの開閉状態の保存
		if (jQuery('#sideMenuArea').is(":visible")) {
			jQuery("#sideMenuState").val("1");

			$("#sideMenuToggle").css(
				{marginLeft:"0px"}
			);
		} else {
			jQuery("#sideMenuState").val("0");

			$("#sideMenuToggle").css(
				{marginLeft:"15px"}
			);
		}
	})
	.on('click', 'a.workListLink', function(ev) {
		$('#trayType').val('1');
		$('#userDisplayId').val(ev.target.id.substr(13));
		// 表示条件取得
		var params = { "trayType" : $('#trayType').val(), "userDisplayId" : $('#userDisplayId').val() };
		NCI.init("/wl0130/initUserDispInfo", params).done(function(res) {
			if (res && res.success) {
				// 検索条件
				$("#formCondition").empty();
				res.userDisplayConditionList.forEach(function(userDisplayCondition) {
					$("#formCondition").append('<div id="div_' + userDisplayCondition.attrName + '" class="form-group col-sm-6 searchConditionArea">');
					$("#div_" + userDisplayCondition.attrName).append('<label data-i18n="" class="searchConditionTh">' + userDisplayCondition.displayName + '</label>');
					$("#div_" + userDisplayCondition.attrName).append('<div id="serchCond_' + userDisplayCondition.attrName + '" class="searchConditionTd">');
					// テキスト検索
					$("#serchCond_" + userDisplayCondition.attrName).append('<input type="text" id="" class="form-control searchConditionText" />');
					// TODO:コンボボックス
					// <select id="processDefCode" class="form-control searchConditionSelect"></select>
				});

				// 一覧表示
				$('table thead th').remove();
				res.userDisplayColumnList.forEach(function(userDisplayColumn, j) {
					$('table thead tr').append('<th><a href="#" data-sort="' + userDisplayColumn.columnName + '">' + userDisplayColumn.displayName + '</a></th>');
				});
				$('table tfoot td').remove();
				res.userDisplayColumnList.forEach(function(userDisplayColumn, j) {
					if (userDisplayColumn.attrName == "businessProcessStatusName") {
						$('table tfoot tr').append('<td class="text-left"><a href="#" data-field="' + userDisplayColumn.attrName + '">#{' + userDisplayColumn.displayName + '}</a></td>');
					} else {
						$('table tfoot tr').append('<td class="text-left" data-field="' + userDisplayColumn.attrName + '">#{' + userDisplayColumn.displayName + '}</td>');
					}
				});
				$('table tfoot tr').append('<td class="hide" data-field="corporationCode"></td>');
				$('table tfoot tr').append('<td class="hide" data-field="processId"></td>');
				$('table tfoot tr').append('<td class="hide" data-field="activityId"></td>');
				$('table tfoot tr').append('<td class="hide" data-field="timestampUpdatedProcessLong"></td>');
			}
		});
		// 検索
		search(1);
	})
	.on('click', 'a.ownProcessListLink', function(ev) {
		$('#trayType').val('2');
		$('#userDisplayId').val(ev.target.id.substr(12));
		// 表示条件取得
		var params = { "trayType" : $('#trayType').val(), "userDisplayId" : $('#userDisplayId').val() };
		NCI.init("/wl0130/initUserDispInfo", params).done(function(res) {
			if (res && res.success) {
				// 検索条件
				$("#formCondition").empty();
				res.userDisplayConditionList.forEach(function(userDisplayCondition, j) {
					$("#formCondition").append('<div id="div_' + userDisplayCondition.attrName + '" class="form-group col-sm-6 searchConditionArea">');
					$("#div_" + userDisplayCondition.attrName).append('<label data-i18n="" class="searchConditionTh">' + userDisplayCondition.displayName + '</label>');
					$("#div_" + userDisplayCondition.attrName).append('<div id="serchCond_' + userDisplayCondition.attrName + '" class="searchConditionTd">');
					// テキスト検索
					$("#serchCond_" + userDisplayCondition.attrName).append('<input type="text" id="" class="form-control searchConditionText" />');
					// TODO:コンボボックス
					// <select id="processDefCode" class="form-control searchConditionSelect"></select>
				});

				// 一覧表示
				$('table thead th').remove();
				res.userDisplayColumnList.forEach(function(userDisplayColumn, j) {
					$('table thead tr').append('<th><a href="#" data-sort="' + userDisplayColumn.columnName + '">' + userDisplayColumn.displayName + '</a></th>');
				});
				$('table tfoot td').remove();
				res.userDisplayColumnList.forEach(function(userDisplayColumn, j) {
					if (userDisplayColumn.attrName == "businessProcessStatusName") {
						$('table tfoot tr').append('<td class="text-left"><a href="#" data-field="' + userDisplayColumn.attrName + '">#{' + userDisplayColumn.displayName + '}</a></td>');
					} else {
						$('table tfoot tr').append('<td class="text-left" data-field="' + userDisplayColumn.attrName + '">#{' + userDisplayColumn.displayName + '}</td>');
					}
				});
				$('table tfoot tr').append('<td class="hide" data-field="corporationCode"></td>');
				$('table tfoot tr').append('<td class="hide" data-field="processId"></td>');
				$('table tfoot tr').append('<td class="hide" data-field="activityId"></td>');
				$('table tfoot tr').append('<td class="hide" data-field="timestampUpdatedProcessLong"></td>');
			}
		});
		// 検索
		search(1);
	})
	.on('click', 'a.allProcessListLink', function(ev) {
		$('#trayType').val('3');
		$('#userDisplayId').val(ev.target.id.substr(12));
		// 表示条件取得
		var params = { "trayType" : $('#trayType').val(), "userDisplayId" : $('#userDisplayId').val() };
		NCI.init("/wl0130/initUserDispInfo", params).done(function(res) {
			if (res && res.success) {
				// 検索条件
				$("#formCondition").empty();
				res.userDisplayConditionList.forEach(function(userDisplayCondition, j) {
					$("#formCondition").append('<div id="div_' + userDisplayCondition.attrName + '" class="form-group col-sm-6 searchConditionArea">');
					$("#div_" + userDisplayCondition.attrName).append('<label data-i18n="" class="searchConditionTh">' + userDisplayCondition.displayName + '</label>');
					$("#div_" + userDisplayCondition.attrName).append('<div id="serchCond_' + userDisplayCondition.attrName + '" class="searchConditionTd">');
					// テキスト検索
					$("#serchCond_" + userDisplayCondition.attrName).append('<input type="text" id="" class="form-control searchConditionText" />');
					// TODO:コンボボックス
					// <select id="processDefCode" class="form-control searchConditionSelect"></select>
				});

				// 一覧表示
				$('table thead th').remove();
				res.userDisplayColumnList.forEach(function(userDisplayColumn, j) {
					$('table thead tr').append('<th><a href="#" data-sort="' + userDisplayColumn.columnName + '">' + userDisplayColumn.displayName + '</a></th>');
				});
				$('table tfoot td').remove();
				res.userDisplayColumnList.forEach(function(userDisplayColumn, j) {
					if (userDisplayColumn.attrName == "businessProcessStatusName") {
						$('table tfoot tr').append('<td class="text-left"><a href="#" data-field="' + userDisplayColumn.attrName + '">#{' + userDisplayColumn.displayName + '}</a></td>');
					} else {
						$('table tfoot tr').append('<td class="text-left" data-field="' + userDisplayColumn.attrName + '">#{' + userDisplayColumn.displayName + '}</td>');
					}
				});
				$('table tfoot tr').append('<td class="hide" data-field="corporationCode"></td>');
				$('table tfoot tr').append('<td class="hide" data-field="processId"></td>');
				$('table tfoot tr').append('<td class="hide" data-field="activityId"></td>');
				$('table tfoot tr').append('<td class="hide" data-field="timestampUpdatedProcess"></td>');
			}
		});
		// 検索
		search(1);
	})

	// サイドメニュー
	.on('click', '.child_lv2', function(){
		var obj = $(this);
		$('#sideMenuArea').find('.child_lv2').each(function(i, elem) {
			if (!obj.is($(elem))) {
				if ($(elem).hasClass('on')) {
					$(elem).children('.lv2').slideToggle(300);
					$(elem).removeClass('on');
				}
				$(elem).children('.lv2').children('li').children('a').removeClass('active');
			}
		});

		if (!$(this).hasClass('on')) {
			$(this).children('.lv2').slideToggle(300);
			$(this).toggleClass('on');
		}
	})
	.on('click', '.lv2 li', function(){
		$(this).children('a').toggleClass('active');
	})
	.on('click', '.toggleBtn .open', function(){
		$('.contents_left').addClass('fadeIn');
		$('.contents_left').removeClass('fadeOut');
		$('.contents_right').css('margin-left','200px');
	})
	.on('click', '.toggleBtn .close', function(){
		$('.contents_left').removeClass('fadeIn');
		$('.contents_left').addClass('fadeOut');
		$('.contents_right').css('margin-left','0');
	})
	// 検索結果のリンク押下
	.on('click', 'a[data-field]', function(ev) {
		var $tr = $(this).closest('tr');
		var corporationCode = $tr.find('[data-field=corporationCode]').text();
		var processId = $tr.find('[data-field=processId]').text();
		var activityId = $tr.find('[data-field=activityId]').text();
		var timestampUpdated = $tr.find('[data-field=timestampUpdatedProcess]').text();
		var trayType = 'WORKLIST';
		// 自案件検索
		if ($('#trayType').val() == '2') {
			trayType = 'OWN';
		} else if ($('#trayType').val() == '3') {
			trayType = 'ALL';
		}
		openDetail(corporationCode, processId, activityId, timestampUpdated, trayType);
	})
	;

	/** 検索実行 */
	function search(pageNo) {
		var cond = createCondition(pageNo);
		pager.search(cond);
	}

	/** 画面入力内容から検索条件を生成 */
	function createCondition(pageNo) {
		var $elements = $('#formCondition, .searchStyle').find('input[type=text], select, textarea, input[type=hidden]');
		var cond = pager.createCondition($elements, pageNo);
		// デフォルトソート条件
		if (!cond.sortColumn) {
			// 自案件検索
			if ($('#trayType').val() == '2') {
				cond.sortColumn = 'PROCESS_ID';
				cond.sortAsc = true;
			// 汎用案件検索
			} else if ($('#trayType').val() == '3') {
				cond.sortColumn = 'PROCESS_ID';
				cond.sortAsc = false;
			// ワークリスト
			} else {
				cond.sortColumn = 'PROCESS_BUSINESS_INFO_001, PROCESS_ID';
				cond.sortAsc = true;
			}
		}
		return cond;
	}

	/** 申請・承認画面を開く */
	function openDetail(corporationCode, processId, activityId, timestampUpdatedLong, trayType) {
		if (activityId) {
			redirectVd0310(corporationCode, processId, activityId, timestampUpdatedLong, trayType);
		}
		else {
			// 汎用案件検索ではプロセスベースなので、アクティビティが取得できない（パフォーマンス的な都合で）。
			// アクティビティIDが取得できていなければ、操作者のアクセス可能な最新のアクティビティを別途求める。
			let params = { "corporationCode" : corporationCode, "processId" : processId, "trayType" : "ALL" };
			NCI.post('/cm0060/getAccessibleActivity', params).done(function(res, textStatus, jqXHR) {
				if (!res || !res.activityId) {
					throw new Error("アクセス可能なアクティビティがありません。");
				}
				redirectVd0310(corporationCode, processId, res.activityId, timestampUpdatedLong, trayType);
			});
		}
	}

	/** 申請・承認画面へリダイレクト（openDetail()で事前に正規化してから呼び出すこと） */
	function redirectVd0310(corporationCode, processId, activityId, timestampUpdatedLong, trayType) {
		NCI.redirect("../vd/vd0310.html?corporationCode=" + corporationCode +
				"&processId=" + processId +
				"&activityId=" + activityId +
				"&trayType=" + trayType +
				"&timestampUpdated=" + timestampUpdatedLong);
	}
});
