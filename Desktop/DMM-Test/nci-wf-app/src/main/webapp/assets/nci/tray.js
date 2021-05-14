/** 連続処理用データのフラッシュスコープ用キー */
const CONTINUOUS_PROCESSING = 'continuousProcessing';

/**
 * ワークリスト／自案件／汎用案件用の共通クラス
 * @param searchURL 検索Endpoint用URL
 * @param $conditionArea 検索条件エリアのルート要素
 * @param $resultArea 検索結果エリアのルート要素
 * @param searchFunction 検索実行するfunction
 *  */
const TRAY = function(searchURL, $conditionArea, $resultArea, $templateArea, searchFunction) {
	/**
	 * 初期化
	 * @param res NCI.init()のレスポンス
	 */
	this.init = function(res) {
		const self = this;
		self.trayType = res.trayType;
		self.trayConfigId = res.config.trayConfigId;
		self.configConditions = res.configConditions;
		self.configResults = res.configResults;

		// ページあたりの行数
		if (res.config.pageSize) {
			$('select.pageSize', self.pager.$root).val(res.config.pageSize);
		}
		$('#trayConfigName').text(res.config.trayConfigName);

		// 初期ソートを記録
		const len = res.configResults.length;
		for (let i = 0; i < len; i++) {
			const r = res.configResults[i];
			if (r.initialSortFlag === '1') {
				self.initSortColumnName = r.businessInfoCode
					+ (self.trayType === 'DETAIL_SEARCH' ? ', DOC_ID' : ', PROCESS_ID');
				self.initialSortAsc = (r.initialSortDescFlag !== '1');
				break;
			}
		}

		let defer = new $.Deferred();
		{
			// 検索条件のテンプレートを読み込む
			$templateArea
				.html(res.trayTemplateHtml)
				.find('[data-i18n]').each(function(i, elem) {
					// 多言語対応
					$(elem).text(NCI.getMessage(elem.getAttribute('data-i18n')));
				});

			// 選択肢の生成（必ずテンプレートのロード後かつ検索条件エリア生成前に行うこと）
			self._createOptionTags(res);

			// 検索条件エリアを生成
			self._setupConditionArea(res.configConditions, $conditionArea);
			// 検索結果エリアを生成
			self._setupResultArea(res.configResults, $resultArea);

			// イベント定義
			$(document)
				.on('click', '.btnSelectUser', TrayHelper.userLink_onClick)
				.on('click', '.btnClearUser', TrayHelper.userLink_clear)
				.on('click', '.btnSelectOrg', TrayHelper.orgLink_onClick)
				.on('click', '.btnClearOrg', TrayHelper.orgLink_clear)
				.on('click', '.btnSelectPost', TrayHelper.postLink_onClick)
				.on('click', '.btnClearPost', TrayHelper.postLink_clear)
				.on('click', '.btnSelectCorporation', TrayHelper.corporationLink_onClick)
				.on('click', '.btnClearCorporation', TrayHelper.corporationLink_clear)
				;

			// 処理の完了
			defer.resolve();
		}
		return defer.promise();
	};

	/** 選択肢の生成 */
	this._createOptionTags = function(res) {
		// トレイ設定の選択肢
		NCI.createOptionTags($('#trayConfigId'), res.trayConfigs);
		// プロセス定義の選択肢
		NCI.createOptionTags($('select.processDefCode'), res.processDefCodes);
		// 画面の選択肢
		NCI.createOptionTags($('select.screenCode'), res.selectableScreenCodes);
		// 画面プロセスの選択肢
		NCI.createOptionTags($('select.screenProcessCode'), res.selectableScreenProcesses);
		// 文書ステータス／業務ステータスの選択肢
		NCI.createOptionTags($('select.processBusinessStatus'), res.businessStatuses);
		// プロセス状態の選択肢
		NCI.createOptionTags($('select.processStatus'), res.processStatuses);
		// 代理元ユーザの選択肢
		NCI.createOptionTags($('select.proxyUser'), res.proxyUsers);

		// コンテンツ種別（文書管理用）の選択肢
		NCI.createOptionTags($('select.contentsTypes'), res.contentsTypes);
		// 公開／非公開の選択肢
		NCI.createOptionTags($('select.publishFlags'), res.publishFlags);
		// 保存期間区分の選択肢
		NCI.createOptionTags($('select.retentionTermTypes'), res.retentionTermTypes);
		// DMM向け支払方法
		if (res.payMthOptionItems && res.payMthOptionItems.length > 0) {
			$('div.payMthOptionItems').empty();
			const payMthOptionItems = res.payMthOptionItems;
			const len = payMthOptionItems.length;
			const df = document.createDocumentFragment();
			for (let i = 0; i < len; i++) {
				let label, radio, span, item;
				item = payMthOptionItems[i];

				label = document.createElement('label');
				label.setAttribute('class', 'radio-inline');

				radio = document.createElement('input');
				radio.type = 'radio';
				radio.name = 'processBusinessInfo012';
				radio.value = item.value;
				label.appendChild(radio);

				span = document.createElement('span');
				span.appendChild(document.createTextNode(item.label));

				label.appendChild(span);

				df.appendChild(label);
			}
			$('div.payMthOptionItems')[0].appendChild(df);
		}
	};

	/** 検索結果エリアを生成 */
	this._setupResultArea = function(results, $resultArea) {
		const self = this;
		const $table = $resultArea.find('table.responsive');
		const len = results.length;
		// colgroupタグ
		const $colgroup = $table.find('colgroup').empty();
		if (self.trayType === 'WORKLIST' || self.trayType === 'BATCH')
			$colgroup.append('<col style="width:35px" />');
		for (let i = 0; i < len; i++) {
			const r = results[i];
			if (self.ignoreBusinessInfoCodes.indexOf(r.businessInfoCode) >= 0) {
				continue;
			}
			$('<col style="width:' + r.colWidth + 'px" />')
				.toggleClass('text-left', r.alignType === 'L')
				.toggleClass('text-center', r.alignType === 'C')
				.toggleClass('text-right', r.alignType === 'R')
				.appendTo($colgroup);
		}
		// theadタグ
		const $theadTR = $table.find('thead>tr').empty();

		// ワークリストは選択用チェックボックスを設ける
		if (self.trayType === 'WORKLIST' || self.trayType === 'BATCH')
			$('<th>&nbsp;</th>').appendTo($theadTR)

		for (let i = 0; i < len; i++) {
			const r = results[i], businessInfoCode = r.businessInfoCode;
			if (self.ignoreBusinessInfoCodes.indexOf(businessInfoCode) >= 0) {
				continue;
			}
			const $link = $('<a href="#">')
				.attr('data-sort', businessInfoCode
						+ (self.trayType === 'DETAIL_SEARCH' ? ', DOC_ID' : ', PROCESS_ID'))
				.text(r.businessInfoName);
			const $th = $('<th>').append($link).appendTo($theadTR)
				;
			// 初期ソートのアイコン
			if (r.initialSortFlag === '1') {
				if (r.initialSortDescFlag === '1')
					$('<i class="fa fa-long-arrow-down"></i>').prependTo($link);
				else
					$('<i class="fa fa-long-arrow-up"></i>').prependTo($link);
			}
		}
		// tbodyタグ
		const $tbody = $table.find('tbody').empty();
		// tfootタグ
		const $tfootTR = $table.find('tfoot>tr').empty();
		// ワークリストは選択用チェックボックスを設ける
		if (self.trayType === 'WORKLIST' || self.trayType === 'BATCH') {
			$('<td style="text-align:center"><input type="checkbox" class="selectable" />').appendTo($tfootTR);
		}
		for (let i = 0; i < len; i++) {
			const r = results[i], businessInfoCode = r.businessInfoCode;
			if (self.ignoreBusinessInfoCodes.indexOf(businessInfoCode) >= 0) {
				continue;
			}
			const fieldName = NCI.toCamelCase(r.businessInfoCode);

			const $td = $('<td>').attr('data-label', r.businessInfoName)
				.appendTo($tfootTR);
			// classのtext-left,center,rightはoriginal.cssで上書きされてしまうため、styleに対してalignを指定する
			if (r.alignType === 'L') {
				$td.css('text-align', 'left');
			}
			if (r.alignType === 'C') {
				$td.css('text-align', 'center');
			}
			if (r.alignType === 'R') {
				$td.css('text-align', 'right');
			}

			// 型を data-validateに変換
			let option;
			if (r.dataType === 'int' && r.businessInfoCode !== "PROCESS_ID" && r.businessInfoCode !== "DOC_ID")
				option = '{"pattern" : "numeric" }';
			else if (r.dataType === 'date')
				option = '{"pattern" : "date" }';

			// リンク or ラベル
			if (r.linkFlag === '1')
				$('<a href="#" data-field="' + fieldName + '" class="inline-block"></a>')
				.attr('data-validate', option)
				.appendTo($td);
			else
				$('<span data-field="' + fieldName + '" class="inline-block"></span>')
				.attr('data-validate', option)
				.appendTo($td);
			// キー情報
			if (i === 0) {
				const fields = ['corporationCode', 'processId', 'activityId', 'timestampUpdatedProcessLong'];
				for (let n = 0; n < fields.length; n++) {
					$('<span data-field="' + fields[n] + '" class="hide"></span>').appendTo($td);
				}
			}
		}
	};

	/** 検索結果には使用しない */
	this.ignoreBusinessInfoCodes = ['PROXY_USER'];

	/** 検索条件エリアを生成 */
	this._setupConditionArea = function(conditions, $conditionArea) {
		const self = this;
		const len = conditions.length;
		let $tr = null;
		for (let i = 0; i < len; i++) {
			const condition = conditions[i];
			// 汎用案件・強制変更画面では代理者とアクティビティ定義をレンダリングしない
			if (('ACTIVITY_DEF_CODE' === condition.businessInfoCode || 'PROXY_USER' === condition.businessInfoCode) &&
					['WORKLIST', 'OWN'].indexOf(self.trayType) < 0) {
				this.ignoreBusinessInfoCodes.push('ACTIVITY_DEF_CODE');
				continue;
			}
			// 数値として扱うフィールド(の配列)追加
			if (condition.dataType === "int") {
				BusinessInfoCodes.NUMERICS.push(condition.businessInfoCode);
			}
			// 日付として扱うフィールド(の配列)追加
			if (condition.dataType === "date") {
				BusinessInfoCodes.DATES.push(condition.businessInfoCode);
			}

			if ($tr === null || i % 2 === 0) {
				$tr = $('<div class="tr">').appendTo($conditionArea);
			}
			// TH
			$('<div class="col-lg-2 col-md-2 col-sm-2 th">')
				.text(condition.businessInfoName)
				.appendTo($tr);
			// TD
			$('<div class="col-lg-4 col-md-4 col-sm-4 td">')
				.append( self._toTrayCondition(condition).html() )
				.appendTo($tr);
		}
		// ここで検索条件(loadCondition)を取得
		// 検索条件があればそこから該当する検索条件値を取得し条件値を設定する
		// なお検索条件は値(入力値)がないと検索条件に含まれないため（NCI.Pager.createCondition参照）
		// 検索条件はあるが値が取得できないパターンは条件値は未入力として扱うため「loadCondition[id] || ''」としている
		const loadCondition = self.pager.getCondition();
		if (loadCondition) {
			$('[id]', $conditionArea).each(function(i, elem) {
				NCI.setElementFromObj(elem, (loadCondition[elem.id] || ''));
			});
		}
		NCI.ymdPicker($conditionArea.find('input.ymdPicker'));
		NCI.ymPicker($conditionArea.find('input.ymPicker'));

		// 氏名
		$conditionArea.find('input.userCode').each(function() {
			const corporationCode = $(this).closest('div.input-group').find('input.corporationCode').val();
			const $userName = $(this).closest('div.input-group').find('input.userName');
			TrayHelper.fillUserName(corporationCode, this.value, $userName);
		});
		// 組織名
		$conditionArea.find('input.organizationCode').each(function() {
			const corporationCode = $(this).closest('div.input-group').find('input.corporationCode').val();
			const $organizationName = $(this).closest('div.input-group').find('input.organizationName');
			TrayHelper.fillOrganizationName(corporationCode, this.value, $organizationName);
		});
		// 役職名
		$conditionArea.find('input.postCode').each(function() {
			const corporationCode = $(this).closest('div.input-group').find('input.corporationCode').val();
			const $postName = $(this).closest('div.input-group').find('input.postName');
			TrayHelper.fillPostName(corporationCode, this.value, $postName);
		});
		// 企業名
		$conditionArea.find('input.corporationCode').each(function() {
			const $corporationName = $(this).closest('div.input-group').find('input.corporationName');
			TrayHelper.fillCorporationName(this.value, $corporationName);
		});
	};

	/** トレイ設定検索条件をもとに、検索条件エレメントを生成 */
	this._toTrayCondition = function(c) {
		const self = this, RANGE = "4";
		const businessInfoCode = c.businessInfoCode, type = c.conditionMatchType;
		const id = NCI.toCamelCase(businessInfoCode);

		let $article = null;
		if ('PROCESS_DEF_CODE' === businessInfoCode) {
			// 文書種別
			$article = $($templateArea.find('article.template-processDefCode')[0].cloneNode(true));
			$article.find('select.form-control').attr('id', id);
			$article.find('select.form-control>option[value=' + c.trayInitValue1 + ']').attr('selected', 'selected');	// defaultValue
		}
		else if ('SCREEN_CODE' === businessInfoCode) {
			// 画面
			$article = $($templateArea.find('article.template-screenCode')[0].cloneNode(true));
			$article.find('select.form-control').attr('id', id);
			$article.find('select.form-control>option[value=' + c.trayInitValue1 + ']').attr('selected', 'selected');	// defaultValue
		}
		else if ('SCREEN_PROCESS_CODE' === businessInfoCode) {
			// 画面プロセス
			$article = $($templateArea.find('article.template-screenProcessCode')[0].cloneNode(true));
			$article.find('select.form-control').attr('id', id);
			$article.find('select.form-control>option[value=' + c.trayInitValue1 + ']').attr('selected', 'selected');	// defaultValue
		}
		else if ('CONTENTS_TYPE' === businessInfoCode) {
			// コンテンツ種別（文書管理用）
			$article = $($templateArea.find('article.template-contentsType')[0].cloneNode(true));
			$article.find('select.form-control').attr('id', id);
			$article.find('select.form-control>option[value=' + c.trayInitValue1 + ']').attr('selected', 'selected');	// defaultValue
		}
		else if ('RETENTION_TERM_TYPE' === businessInfoCode) {
			// 保存期間区分
			$article = $($templateArea.find('article.template-retentionTermType')[0].cloneNode(true));
			$article.find('select.form-control').attr('id', id);
			$article.find('select.form-control>option[value=' + c.trayInitValue1 + ']').attr('selected', 'selected');	// defaultValue
		}
		else if ('PUBLISH_FLAG' === businessInfoCode) {
			// 公開フラグ
			$article = $($templateArea.find('article.template-publishFlag')[0].cloneNode(true));
			$article.find('select.form-control').attr('id', id);
			$article.find('select.form-control>option[value=' + c.trayInitValue1 + ']').attr('selected', 'selected');	// defaultValue
		}
		else if ('PROCESS_STATUS' === businessInfoCode) {
			// プロセスステータス
			$article = $($templateArea.find('article.template-processStatus')[0].cloneNode(true));
			$article.find('select.form-control').attr('id', id);
			$article.find('select.form-control>option[value=' + c.trayInitValue1 + ']').attr('selected', 'selected');	// defaultValue
		}
		else if ('BUSINESS_PROCESS_STATUS' === businessInfoCode) {
			// 業務ステータス
			$article = $($templateArea.find('article.template-processBusinessStatus')[0].cloneNode(true));
			$article.find('select.form-control').attr('id', id);
			$article.find('select.form-control>option[value=' + c.trayInitValue1 + ']').attr('selected', 'selected');	// defaultValue
			// 差戻しチェックボックス
			$article.find('input.sendback').attr('id', 'sendbackStatus');
			if (c.trayInitValue2 == '1') {
				$article.find('input.sendback').attr('checked', 'checked');
			}
			// 引き戻しチェックボックス
			$article.find('input.pullback').attr('id', 'pullbackStatus')
			if (c.trayInitValue3 == '1') {
				$article.find('input.pullback').attr('checked', 'checked');
			}
		}
		else if ('PROCESS_BUSINESS_INFO_012' === businessInfoCode) {
			// PROCESS_BUSINESS_INFO_012(支払方法)
			$article = $($templateArea.find('article.template-payMth')[0].cloneNode(true));
		}
		else if (businessInfoCode.indexOf('USER_CODE') >= 0) {
			// ユーザ選択
			$article = $($templateArea.find('article.template-user')[0].cloneNode(true));
			$article.find('input.userCode').attr({'id': id, 'value': c.trayInitValue1});
			$article.find('input.corporationCode').attr({'id': id.replace('Code', 'CorpCode'), 'value': c.trayInitValue2});
		}
		else if (businessInfoCode.indexOf('ORGANIZATION_CODE') >= 0) {
			// 組織選択
			$article = $($templateArea.find('article.template-org')[0].cloneNode(true));
			$article.find('input.organizationCode').attr({'id': id, 'value': c.trayInitValue1});
			$article.find('input.corporationCode').attr({'id': id.replace('Code', 'CorpCode'), 'value': c.trayInitValue2});
		}
		else if (businessInfoCode.indexOf('POST_CODE') >= 0) {
			// 役職選択
			$article = $($templateArea.find('article.template-post')[0].cloneNode(true));
			$article.find('input.postCode').attr({'id': id, 'value': c.trayInitValue1});
			$article.find('input.corporationCode').attr({'id': id.replace('Code', 'CorpCode'), 'value': c.trayInitValue2});
		}
		else if (businessInfoCode.indexOf('CORPORATION_CODE') >= 0) {
			// 企業選択
			$article = $($templateArea.find('article.template-corporation')[0].cloneNode(true));
			$article.find('input.corporationCode').attr({'id': id, 'value': c.trayInitValue1});
		}
		else if (businessInfoCode === 'PROXY_USER') {
			// 代理者
			$article = $($templateArea.find('article.template-proxyUser')[0].cloneNode(true));
			$article.find('select.form-control').attr('id', 'proxyUser');
		}
		else if (businessInfoCode === 'PROCESS_BUSINESS_INFO_016') {
			// DMM：費用計上月
			// 範囲
			$article = $($templateArea.find('article.template-range')[0].cloneNode(true));
			const idFrom = id + 'From', idTo = id + 'To';
			const $from = $article.find('input.from').attr({ 'id' : idFrom, 'value' : c.trayInitValue1 });
			const $to = $article.find('input.to').attr({ 'id' : idTo, 'value': c.trayInitValue2 });

			// 年月範囲
			$from.addClass('ymPicker').attr('data-validate', JSON.stringify({ "pattern" : "ym", "to": '#' + idTo }))
				.parent().append('<i class="glyphicon glyphicon-calendar form-control-feedback"></i>');
			$to.addClass('ymPicker').attr('data-validate', JSON.stringify({ "pattern" : "ym", "from": '#' + idFrom }))
				.parent().append('<i class="glyphicon glyphicon-calendar form-control-feedback"></i>');
		}
		else if (BusinessInfoCodes.FLAGS.indexOf(businessInfoCode) >= 0) {
			// フラグ(0:OFF、1:ON)
			$article = $($templateArea.find('article.template-flag')[0].cloneNode(true));
			const $checkbox = $article.find('input[type=checkbox]').attr('id', id);
			if (c.trayInitValue1 === '1')
				$checkbox.attr('checked', 'checked');
		}
		else if (BusinessInfoCodes.STATUSES.indexOf(businessInfoCode) >= 0) {
			// ステータス(1:OFF、2:ON)
			$article = $($templateArea.find('article.template-status')[0].cloneNode(true));
			const $checkbox = $article.find('input[type=checkbox]').attr('id', id);
			if (c.trayInitValue1 === '2')
				$checkbox.attr('checked', 'checked');
		}
		else if (RANGE === type){
			// 範囲
			$article = $($templateArea.find('article.template-range')[0].cloneNode(true));
			const idFrom = id + 'From', idTo = id + 'To';
			const $from = $article.find('input.from').attr({ 'id' : idFrom, 'value' : c.trayInitValue1 });
			const $to = $article.find('input.to').attr({ 'id' : idTo, 'value': c.trayInitValue2 });

			if (BusinessInfoCodes.DATES.indexOf(businessInfoCode) >= 0) {
				// 日付範囲
				$from.addClass('ymdPicker').attr('data-validate', JSON.stringify({ "pattern" : "date", "to": '#' + idTo }))
					.parent().append('<i class="glyphicon glyphicon-calendar form-control-feedback"></i>');
				$to.addClass('ymdPicker').attr('data-validate', JSON.stringify({ "pattern" : "date", "from": '#' + idFrom }))
					.parent().append('<i class="glyphicon glyphicon-calendar form-control-feedback"></i>');
			}
			else if (BusinessInfoCodes.NUMERICS.indexOf(businessInfoCode) >= 0) {
				// 数値範囲
				$from.attr('data-validate', JSON.stringify({ "pattern" : "numeric", "to": '#' + idTo }));
				$to.attr('data-validate', JSON.stringify({ "pattern" : "numeric", "from": '#' + idFrom }));
			}
		}
		else {
			// その他は一致区分ごとに表示を切り替え
			$article = $($templateArea.find('article.template-normal')[0].cloneNode(true));
			const $input = $article.find('input').attr({ 'id' : id, 'value' : c.trayInitValue1 });

			if (BusinessInfoCodes.DATES.indexOf(businessInfoCode) >= 0) {
				// 日付範囲
				$input.addClass('ymdPicker').attr('data-validate', JSON.stringify({ "pattern" : "date" }))
					.parent().append('<i class="glyphicon gl-sizeendar form-control-feedback"></i>');
			}
			else if (BusinessInfoCodes.NUMERICS.indexOf(businessInfoCode) >= 0) {
				// 数値範囲
				$input.attr('data-validate', JSON.stringify({ "pattern" : "numeric" }));
			}
		}
		return $article;
	};

	/**
	 * 申請・承認画面を開く
	 * @param ev イベント
	 * @param $tr 検索結果の対象行
	 * @param processTargets 連続処理用の選択プロセス一覧
	 **/
	this.openDetail = function(ev, $tr, processTargets) {
		const corporationCode = $tr.find('[data-field=corporationCode]').first().text();
		const processId = $tr.find('[data-field=processId]').first().text();
		const activityId = $tr.find('[data-field=activityId]').first().text();
		const timestampUpdatedLong = $tr.find('[data-field=timestampUpdatedProcessLong]').first().text();
		const self = this;
		const proxyUser = self.pager.condition.proxyUser;
		const trayType = self.trayType;
		// Ctrlキーが押下されているか
		const isCtrlKey = (ev != null && ev.ctrlKey);

		if (activityId) {
			if (isCtrlKey)
				openVd0310(corporationCode, processId, activityId, timestampUpdatedLong, proxyUser, trayType, processTargets);
			else
				redirectVd0310(corporationCode, processId, activityId, timestampUpdatedLong, proxyUser, trayType, processTargets);
		}
		else {
			// 汎用案件検索ではプロセスベースなので、アクティビティが取得できない（パフォーマンス的な都合で）。
			// よってAJAXで操作者のアクセス可能な最新のアクティビティを別途求めてから申請画面を開く
			const params = { "corporationCode" : corporationCode, "processId" : processId, "trayType" : self.trayType };
			NCI.post('/cm0060/getAccessibleActivity', params).done(function(res, textStatus, jqXHR) {
				if (!res || !res.length) {
					throw new Error("アクセス可能なアクティビティがありません。");
				} else {
					// DMM仕様。複数アクティビティにアクセスできても、常に先頭のアクティビティに遷移させる
					if (isCtrlKey)
						openVd0310(corporationCode, processId, res[0].activityId, timestampUpdatedLong, proxyUser, trayType, processTargets);
					else
						redirectVd0310(corporationCode, processId, res[0].activityId, timestampUpdatedLong, proxyUser, trayType, processTargets);
				}

			});
		}
	};

	/** 遷移先アクティビティ選択画面からのコールバック関数 */
	this.fromCm0190 = function(activity, self) {
		if (activity) {
			redirectVd0310(activity.corporationCode, activity.processId,
					activity.activityId, activity.timestampUpdatedProcessLong, null, self.trayType);
		}
	};

	/** 連続処理として申請画面を開く */
	this.continuousProcessing = function(ev) {
		// 選択行を吸い上げる
		const targets = [], self = this;
		let $firstTR = null;
		$('#tblSearchResult>tbody input.selectable:checked').each(function() {
			const $tr = $(this).closest('tr');
			targets.push({
				'corporationCode' : $tr.find('[data-field=corporationCode]').first().text(),
				'processId' : $tr.find('[data-field=processId]').first().text(),
				'activityId' : $tr.find('[data-field=activityId]').first().text(),
				'timestampUpdated' : $tr.find('[data-field=timestampUpdatedProcessLong]').first().text(),
				'proxyUser' : self.pager.condition.proxyUser,
				'trayType' : self.trayType
			});
			if ($firstTR === null)
				$firstTR = $tr;
		});
		// 選択行をフラッシュスコープで申請画面へ送ったうえで、先頭行を申請画面で開く
		if (targets.length && $firstTR && $firstTR.length) {
			// 連続処理はCtrlキー押下による別ウィンドウで開く処理は無効とするため第一引数のevはNULLで渡す
			self.openDetail(null, $firstTR, targets);
		} else {
			NCI.addMessage('danger', NCI.getMessage('MSG0135', 1));
		}
	};

	/** ページングコントローラー */
	this.pager = new Pager($resultArea, searchURL, searchFunction).init(true);
	/** 初期ソートのカラム名 */
	this.initSortColumnName = 'PROCESS_ID';
	/** 初期ソート昇順 */
	this.initialSortAsc = false;
	/** トレイタイプ */
	this.trayType = '';	// tray.init()でもらう
	/** トレイ設定検索条件 */
	this.configConditions = null;
	/** トレイ設定検索結果 */
	this.configResults = null;
};

const BusinessInfoCodes = {
	/** 日付として扱うフィールド(の配列) */
	DATES : [],
	/** 数値として扱うフィールド(の配列) */
	NUMERICS : [],
	/** フラグ(チェックボックス)として扱うフィールド(の配列) */
	FLAGS : ['DESCRIPTION_FLAG', 'BATCH_PROCESSING_FLAG', 'LOCK_FLAG'],
	/** ステータス(チェックボックス)として扱うフィールド(の配列) */
	STATUSES : ['SENDBACK_STATUS', 'PULLBACK_STATUS', 'PULLFORWARD_STATUS'
		, 'APPLICATION_STATUS', 'APPROVAL_STATUS'],
	/** 完全一致のみとして扱うフィールド(の配列) */
	FULL_MATCHES : ['PROCESS_ID', 'PROCESS_DEF_CODE', 'BUSINESS_PROCESS_STATUS'
		, 'PROCESS_STATUS', 'SENDBACK_STATUS', 'PULLBACK_STATUS', 'PULLFORWARD_STATUS'
		, 'APPLICATION_STATUS', 'APPROVAL_STATUS', 'DESCRIPTION_FLAG'
		, 'BATCH_PROCESSING_FLAG', 'CORPORATION_CODE_PROCESS'
		, 'USER_CODE_OPERATION_PROCESS', 'ORGANIZATION_CODE_PROCESS'
		, 'POST_CODE_PROCESS', 'PROXY_USER', 'OWNER_USER_CODE'
		, 'PUBLISH_FLAG', 'LOCK_FLAG', 'LOCK_CORPORATION_CODE'
		, 'LOCK_USER_CODE', 'RETENTION_TERM_TYPE'],
	/** 後ろに 'Name'を付与するフィールド（"業務ステータス" に対する "業務ステータス名称"） */
	SUFIX_NAMES : ['BUSINESS_PROCESS_STATUS', 'PROCESS_STATUS', 'APPLICATION_STATUS'
		, 'APPROVAL_STATUS', 'SENDBACK_STATUS', 'PULLBACK_STATUS'
		, 'PULLFORWARD_STATUS', 'CONTENTS_TYPE', 'RETENTION_TERM_TYPE', 'PUBLISH_FLAG']
};

var TrayHelper = {
	/** ユーザ選択ポップアップの起動 */
	userLink_onClick : function(ev) {
		let params = null;
		Popup.open("../cm/cm0040.html", TrayHelper.userLink_callback, params, this);
		return false;
	},

	/** ユーザ選択ポップアップからのコールバック */
	userLink_callback : function(user, trigger) {
		if (user) {
			const $root = $(trigger).closest('div.input-group');
			$root.find('input.corporationCode').val(user.corporationCode);
			$root.find('input.userCode').val(user.userCode);
			$root.find('input.userName').val(user.userName);
			$root.find('input.userAddedInfo').val(user.userAddedInfo);
		}
	},

	/** ユーザ選択クリア */
	userLink_clear : function(ev) {
		const $root = $(ev.currentTarget).closest('div.input-group');
		$root.find('input').val('');
	},

	/** 組織選択ポップアップの起動 */
	orgLink_onClick : function(ev) {
		let params = null;
		Popup.open("../cm/cm0020.html", TrayHelper.orgLink_callback, params, this);
		return false;
	},

	/** 組織選択ポップアップからのコールバック */
	orgLink_callback : function(org, trigger) {
		if (org) {
			const $root = $(trigger).closest('div.input-group');
			$root.find('input.corporationCode').val(org.corporationCode);
			$root.find('input.organizationCode').val(org.organizationCode);
			$root.find('input.organizationName').val(org.organizationName);
			$root.find('input.organizationAddedInfo').val(org.organizationAddedInfo);
		}
	},

	/** 組織選択クリア */
	orgLink_clear : function(ev) {
		const $root = $(ev.currentTarget).closest('div.input-group');
		$root.find('input').val('');
	},

	/** 役職選択ポップアップの起動 */
	postLink_onClick : function(ev) {
		let params = null;
		Popup.open("../cm/cm0030.html", TrayHelper.postLink_callback, params, this);
		return false;
	},

	/** 役職選択ポップアップからのコールバック */
	postLink_callback : function(post, trigger) {
		if (post) {
			const $root = $(trigger).closest('div.input-group');
			$root.find('input.corporationCode').val(post.corporationCode);
			$root.find('input.postCode').val(post.postCode);
			$root.find('input.postName').val(post.postName);
			$root.find('input.postAddedInfo').val(post.postAddedInfo);
		}
	},

	/** 役職選択クリア */
	postLink_clear : function(ev) {
		const $root = $(ev.currentTarget).closest('div.input-group');
		$root.find('input').val('');
	},

	/** 企業選択ポップアップの起動 */
	corporationLink_onClick : function(ev) {
		let params = null;
		Popup.open("../cm/cm0010.html?initSearch=true", TrayHelper.corporationLink_callback, params, this);
		return false;
	},

	/** 企業選択ポップアップからのコールバック */
	corporationLink_callback : function(corp, trigger) {
		if (corp) {
			const $root = $(trigger).closest('div.input-group');
			$root.find('input.corporationCode').val(corp.corporationCode);
			$root.find('input.corporationName').val(corp.corporationName);
		}
	},

	/** 企業選択クリア */
	corporationLink_clear : function(ev) {
		const $root = $(ev.currentTarget).closest('div.input-group');
		$root.find('input').val('');
	},

	/** 企業名を取得 */
	fillCorporationName : function(corporationCode, $coporationName) {
		if ($coporationName && $coporationName.length > 0) {
			const params = {'corporationCode': corporationCode };
			NCI.post('trayAjax/getWfmCorporation', params).done(function(res) {
				if (res && res.results && res.results.length)
					$coporationName.attr('value', res.results[0].corporationName);
				else
					$coporationName.attr('value', '');
			});
		}
	},

	/** ユーザ名を取得 */
	fillUserName : function(corporationCode, userCode, $userName) {
		if ($userName && $userName.length > 0) {
			const params = {'corporationCode': corporationCode, 'userCode': userCode};
			NCI.post('trayAjax/getWfmUser', params).done(function(res) {
				if (res && res.results && res.results.length)
					$userName.attr('value', res.results[0].userName);
				else
					$userName.attr('value', '');
			});
		}
	},

	/** 組織名を取得 */
	fillOrganizationName : function(corporationCode, organizationCode, $organizationName) {
		if ($organizationName && $organizationName.length > 0) {
			const params = {'corporationCode': corporationCode, 'organizationCode': organizationCode};
			NCI.post('trayAjax/getWfmOrganization', params).done(function(res) {
				if (res && res.results && res.results.length)
					$organizationName.attr('value', res.results[0].organizationName);
				else
					$organizationName.attr('value', '');
			});
		}
	},

	/** 役職名を取得 */
	fillPostName : function(corporationCode, postCode, $postName) {
		if ($postName && $postName.length > 0) {
			const params = {'corporationCode': corporationCode, 'postCode': postCode};
			NCI.post('trayAjax/getWfmPost', params).done(function(res) {
				if (res && res.results && res.results.length)
					$postName.attr('value', res.results[0].postName);
				else
					$postName.attr('value', '');
			});
		}
	},

	/** トレイタイプに従って、トレイ系画面へ戻るする */
	backToTray : function(trayType) {
		if (trayType === 'NEW')
//			NCI.redirect('../na/na0010.html');
			NCI.redirect('../wl/wl0030.html');	// DMMは新規申請画面がないので、ワークリストへ戻る
		else if (trayType === 'WORKLIST')
			NCI.redirect('../wl/wl0030.html');
		else if (trayType === 'OWN')
			NCI.redirect('../wl/wl0032.html');
		else if (trayType === 'ALL')
			NCI.redirect('../wl/wl0033.html');
		else if (trayType === 'FORCE')
			NCI.redirect('../wl/wl0031.html');
		else if (trayType === 'BATCH')
			NCI.redirect('../wl/wl0037.html');
		else if (trayType === 'DETAIL_SEARCH')	// 文書管理の詳細検索
			NCI.redirect('../dc/dc0022.html');
		else
			throw new Error("不明なトレイタイプです。trayType=" + trayType);
	}
};

/** 連続処理用リスト */
var ProcessList = function(corporationCode, processId, activityId, targets) {
	/** 処理対象のプロセス一覧 */
	this.targets = targets;
	this.corporationCode = corporationCode;
	this.processId = processId;
	this.activityId = activityId;

	/** 連続処理の対象行で、現在処理中の行INDEXを返す。連続処理でなく単独で申請画面を開いているときは常に -1を返す */
	this.getCurrentIndex = function() {
		if (this.targets && this.targets.length) {
			const c = this.corporationCode;
			const p = this.processId;
			const a = this.activityId;
			for (let i = 0; i < targets.length; i++) {
				const t = this.targets[i];
				if (t.corporationCode === c && t.processId === p && t.activityId === a) {
					return i;
				}
			}
		}
		return -1;
	};

	/** 連続処理の対象行で、現在処理中のプロセス情報を返す。連続処理でなく単独で申請画面を開いているときは常に nullを返す */
	this.getCurrent = function() {
		const i = this.getCurrentIndex();
		return (i < 0 ? null : this.targets[i]);
	};

	/** 「連続処理中の行」より後に行があるか */
	this.hasNext = function() {
		const i = this.getCurrentIndex(), targets = this.targets;
		return (targets && targets.length && i < targets.length - 1);
	};

	/** 「連続処理中の行」より前に行があるか */
	this.hasPrev = function() {
		const i = this.getCurrentIndex(), targets = this.targets;
		return (targets && targets.length && (i > 0));
	};

	/** 「連続処理中の行」を削除し、次に処理すべき行を返す */
	this.removeCurrent = function() {
		const i = this.getCurrentIndex(), targets = this.targets;
		targets.splice(i, 1);
		if (i < targets.length) {
			// 処理後の同位置にある行を次とする
			return targets[i];
		} else if (targets.length) {
			// 同じ位置に行がなければ、末尾を次とする
			return targets[targets.length - 1];
		}
		// 対象行がもうない
		return null;
	};

	/** 「連続処理中の行」の先頭行を返す */
	this.getFirst = function() {
		if (this.hasPrev()) {
			return this.targets[0];
		}
		return null;
	};

	/** 「連続処理中の行」の前行を返す */
	this.getPrev = function() {
		if (this.hasPrev()) {
			const i = this.getCurrentIndex(), targets = this.targets;
			return this.targets[i - 1];
		}
		return null;
	};

	/** 「連続処理中の行」の次行を返す */
	this.getNext = function() {
		if (this.hasNext()) {
			const i = this.getCurrentIndex(), targets = this.targets;
			return this.targets[i + 1];
		}
		return null;
	};

	/** 「連続処理中の行」の最終行を返す */
	this.getLast = function() {
		if (this.hasNext()) {
			return this.targets[targets.length - 1];
		}
		return null;
	};

	/** 連続処理リストの指定行へ遷移 */
	this.move = function(t, trayType) {
		if (t) {
			redirectVd0310(t.corporationCode, t.processId, t.activityId
					, t.timestampUpdated, t.proxyUser, trayType, this.targets);
		}
	};

	/** 連続処理の位置情報を返す */
	this.getPositionInfo = function() {
		if (!this.targets || !this.targets.length)
			return '';
		const i = this.getCurrentIndex(), targets = this.targets;
		return (i + 1) + '/' + this.targets.length;
	};
};

/**
 * 申請・承認画面へリダイレクト（openDetail()で事前に正規化してから呼び出すこと）
 * @param corporationCode 企業コード
 * @param processId プロセスID
 * @param activityId アクティビティID
 * @param timestampUpdated 最終更新日時
 * @param proxyUser 代理ユーザ情報
 * @param trayType トレイ情報
 * @param processTargets 連続処理用のプロセス一覧
 **/
function redirectVd0310(corporationCode, processId, activityId, timestampUpdatedLong, proxyUser, trayType, processTargets) {
	// 連続処理用のプロセス一覧をフラッシュスコープに退避
	NCI.flushScope(CONTINUOUS_PROCESSING, processTargets);

	NCI.redirect("../vd/vd0310.html?corporationCode=" + corporationCode +
			"&processId=" + processId +
			"&activityId=" + activityId +
			"&trayType=" + trayType +
			"&proxyUser=" + (proxyUser || '') +
			"&timestampUpdated=" + timestampUpdatedLong);
};

/**
 * 申請・承認画面を別ウィンドウ(タブ)にて表示（openDetail()で事前に正規化してから呼び出すこと）
 * @param corporationCode 企業コード
 * @param processId プロセスID
 * @param activityId アクティビティID
 * @param timestampUpdated 最終更新日時
 * @param proxyUser 代理ユーザ情報
 * @param trayType トレイ情報
 * @param processTargets 連続処理用のプロセス一覧
 **/
function openVd0310(corporationCode, processId, activityId, timestampUpdatedLong, proxyUser, trayType, processTargets) {
	// 連続処理用のプロセス一覧をフラッシュスコープに退避
	NCI.flushScope(CONTINUOUS_PROCESSING, processTargets);

	const url = "../vd/vd0310.html?corporationCode=" + corporationCode +
				"&processId=" + processId +
				"&activityId=" + activityId +
				"&trayType=" + trayType +
				"&proxyUser=" + (proxyUser || '') +
				"&timestampUpdated=" + timestampUpdatedLong
				"_tm=" + +new Date();		// キャッシュさせない

	window.open(url, '_blank');
};