const FLUSH_SCOPE_KEY = "__flushScopeKey__"
const PAGE_SIZE_ARRAY = [2, 5, 10, 20, 50, 100];

$(function() {
	// 高速化のため、ブロックUIを事前生成しておく
	NCI.createBlockUI();

	// 日付（bootstrap-datepicker.js）の言語リソース
	// 言語を足すときは /src/main/webapp/assets/bootstrap-datepicker/locales/*.jsを参照のこと。
	if ($.fn.datepicker && $.fn.datepicker.dates) {
		$.fn.datepicker.dates.en['format'] = "yyyy/mm/dd";
		$.fn.datepicker.dates.ja={days:["日曜","月曜","火曜","水曜","木曜","金曜","土曜"],daysShort:["日","月","火","水","木","金","土"],daysMin:["日","月","火","水","木","金","土"],months:["1月","2月","3月","4月","5月","6月","7月","8月","9月","10月","11月","12月"],monthsShort:["1月","2月","3月","4月","5月","6月","7月","8月","9月","10月","11月","12月"],today:"今日",format:"yyyy/mm/dd",titleFormat:"yyyy年mm月",clear:"クリア"};
		$.fn.datepicker.dates.zh={days:["星期日","星期一","星期二","星期三","星期四","星期五","星期六"],daysShort:["周日","周一","周二","周三","周四","周五","周六"],daysMin:["日","一","二","三","四","五","六"],months:["一月","二月","三月","四月","五月","六月","七月","八月","九月","十月","十一月","十二月"],monthsShort:["1月","2月","3月","4月","5月","6月","7月","8月","9月","10月","11月","12月"],today:"今日",clear:"清除",format:"yyyy/mm/dd",titleFormat:"yyyy年mm月",weekStart:1};
	}

	// Windowリサイズ（含むデバイスの縦横持ち替え）
	$(window).on("resize", function() {
		// 連続したリサイズイベントの負荷を最小にするための遅延実行
		NCI.doLater(function() {
			// バルーンは位置がずれるのでいったんクリア
			if (typeof(Validator) !== 'undefined') {
				Validator.hideBalloon();
			}
		}, 300);
	});

	// キーボードイベントのフック
	$(document).on('keydown', function(ev) {
		const BACKSPACE = 8, ESC = 27, ENTER = 13, F1=112;
		const target = ev.target, tagName = target.tagName, type = target.type;
		switch (ev.which) {
		case BACKSPACE:		// INPUT/TEXTAREA以外でのBackSpaceをキャンセル
			// 「IE限定」読取専用Textbox上でBackSpace押下すると「戻る」ので、読取専用時も除外
			return /^INPUT|TEXTAREA$/.test(tagName) && !target.readOnly;
		case ESC: case F1:			// エスケープキー/F1キーは常にキャンセル
			return false;
		};
	})
	// サブタイトル等のクリック
	.on('click', ".collapse_btn", function(ev){
		// 矢印アイコンの上下反転
		const $btn = $(this), $icon = $btn.find('i.fa').first();
		const up = $icon.hasClass('fa-chevron-up');
		$icon.toggleClass('fa-chevron-down', up).toggleClass('fa-chevron-up', !up);

		// サブタイトルのクリックにより、その内部コンテンツの表示／非表示が切り替わるので、
		// バルーンもそれに追随させる
		const $targets = $($btn.data('target')).find('input, select, textarea');
		if (up)
			Validator.validate($targets, false);
		else
			Validator.hideBalloon($targets);
	});
});


const NCI = {

	/** ログイン者情報：NCI.init()呼出し後に設定されます。 */
	loginInfo : null,

	/**
	 * 初期化リクエストを送信し、結果をJSONとして求める。
	 * また、画面のHTMLから 多言語対応する必要のあるエレメントからキーを抜き出し、
	 * 対応するメッセージを取得・エレメントへ反映までを一括で行う。
	 * @param uri リクエスト先URL(/endpointからの相対パス)
	 * @param params 送信パラメータ
	 * @param $root 省略可。多言語対応対応のエレメントを特定するためのルート要素。
	 */
	init : function(uri, params, $root) {
		if (!params)
			params = {};

		if (!params.messageCds)
			params.messageCds = [];

		// menu.htmlで使用するメッセージを追加
		let keys = params.messageCds || [];
		const defaults =
		[
			'userAddedInfo','userName','language','btnLogout', 'noRecord', 'btnTopPage',
			'prevPage', 'nextPage', 'yes', 'no', 'noPaging', 'announcementOfTheDay',
			'MSG0069','MSG0070','MSG0071','MSG0072','MSG0073', 'MSG0054', 'MSG0158',
			'MSG0055', 'MSG0056', 'MSG0057', 'btnConfirmChangeLangYes', 'sorryForTouble',
			'btnConfirmChangeLangNo', 'btnConfirmLogoutYes', 'btnConfirmLogoutNo',
			'MSG0184', 'MSG0185', 'MSG0186', 'MSG0204', 'btnOK', 'btnCancel',
			'MSG0003', 'targetRow', 'MSG0000', 'btnClose', 'MSG0220', 'MSG0224',
			'MSG0225', 'MSG0276',
			/** 必須入力です。*/
			'MSG0074',
			/** 日付形式(yyyy/mm/dd)で入力してください。*/
			'MSG0075',
			/** アルファベッドのみで入力してください。*/
			'MSG0076',
			/** 数字のみで入力してください。*/
			'MSG0077',
			/** 全角のみで入力してください。*/
			'MSG0078',
			/** 半角カナのみで入力してください。*/
			'MSG0079',
			/** 半角のみで入力してください。*/
			'MSG0080',
			/** 整数で入力してください。*/
			'MSG0081',
			/** アルファベットまたは数字のみで入力してください。*/
			'MSG0082',
			/** アルファベットまたは記号のみで入力してください。*/
			'MSG0083',
			/** アルファベット、記号、数字のみで入力してください。*/
			'MSG0084',
			/** アルファベット、数字、アンダースコア(_)のみで入力してください。*/
			'MSG0085',
			/** アルファベット、記号、数字、改行のみで入力してください。*/
			'MSG0086',
			/** 郵便番号の形式(****-***)で入力してください。*/
			'MSG0087',
			/** IPアドレスの形式で入力してください。*/
			'MSG0088',
			/** 電話番号の形式で入力してください。*/
			'MSG0089',
			/** メールアドレスの形式で入力してください。*/
			'MSG0090',
			/** 全角カナのみで入力してください。*/
			'MSG0091',
			/** {0}桁以下で入力してください。*/
			'MSG0092',
			/** {0}byte以下で入力してください。*/
			'MSG0093',
			/** 小数点以下は最大{0}桁で入力してください。 */
			'MSG0096',
			/** {0}桁以上で入力してください。 */
			'MSG0097',
			/** 年月形式(yyyy/mm)で入力してください。 */
			'MSG0100',
			/** 使用できない文字が含まれています ( {0} ) */
			'MSG0103',
			/** {0}以上、{1}以下にしてください。 */
			'MSG0104',
			/** {0}以上にしてください。 */
			'MSG0105',
			/** {0}以下にしてください。 */
			'MSG0106',
			/** 開始≦終了にしてください。 */
			'MSG0107',
			/** 正しい時刻で入力してください。*/
			'MSG0131',
			/**  */
			'MSG0141',
			/** 数値で入力してください。 */
			'MSG0190',
			//	日付範囲チェック対応
			/** {0}は「{1}～{2}」の範囲内で入力してください。*/
			'MSG0005',
		]
		keys = keys.concat(defaults);

		// 多言語対応を要するエレメントからメッセージCDを収集
		const $i18n = $('[data-i18n]', $root);
		$i18n.each(function(i, elem) {
			let key = $i18n.eq(i).data('i18n');
			if (key != null && key !== '') keys.push(key);
		});

		// 空でなく、キャッシュになく、重複もしないメッセージCDだけを抜き出し
		params.messageCds = keys.filter(function (key, i, self) {
			return key !== null
				&& key !== ''
				&& !NCI.isCachedMessage(key)	// キャッシュに無い
				&& self.indexOf(key) === i;		// 重複しない
		});

		$('span.screen-name').text('');

		// メニューHTMLが必要か
		if (params.needMenuHtml == null || typeof(params.needMenuHtml) === 'undefined') {
			params.needMenuHtml = ($('#nav').length !== 0);
		}
		// フッターHTMLが必要か
		if (params.needFooterHtml == null || typeof(params.needFooterHtml) === 'undefined') {
			params.needFooterHtml = !Popup.isPopup();
		}

		// 送信
		const jqXHR = NCI.post(uri, params, false).done(function(res, textStatus, jqXHR) {
			// messageCdsに対応したメッセージをキャッシュ
			if (res && res.messages && res.messages.length > 0) {
				const messages = res.messages;
				$.each(messages, function(i, p) {
					NCI.putMessage(p.messageCd, p.text)
				});
			}
			// ログイン情報
			if (res && res.loginInfo) {
				NCI.loginInfo = res.loginInfo;
			}
			// タイトル
			if (res && res.title) {
				$('h1.page-header, span.screen-name').text(res.title);	// 互換性用
			}
			// APP名
			if (res && res.applicationName) {
				document.title = res.applicationName;
			}

			// メニューの初期化
			if (params.needMenuHtml) {
				NCI.initMenu(res);
			}

			// フッターの初期化
			if (params.needFooterHtml) {
				$(res.footerHtml)
						.appendTo('body')
						.find('p.version').text(res.appVersion);
			}

			// サイト識別用の背景色と文字色
			if (res && (res.siteBgColor || res.siteFontColor)) {
				// CSSルールを新たに追加し、「サイトで共通的なCSSクラス」をそのルールで上書きする
				let css = '';
				css += '<style type="text/css">';
				// for 旧デザイン
				css += '.header-row-area { background-color : {siteBgColor}; color : {siteFontColor} }';
				css += '.header-button-area { background-color : {siteBgColor}; color : {siteFontColor} }';
				css += '.navbar-header select { color : black; }';
				css += '.sub-title { background-color : {siteBgColor}; color : {siteFontColor} }';
				// for 新デザイン
				css += 'nav > div.container-fluid { background-color : {siteBgColor}; color : {siteFontColor}; padding: 0px 25px; }';
				css += '.panel-default .panel-heading { border-left : 12px solid {siteBgColor}; }';
				css += '.navbar-header select { color : black; }';
				css += '</style>';
				const arguments = { "siteBgColor" : res.siteBgColor, "siteFontColor" : res.siteFontColor };
				css = NCI.format(css, arguments);
				$(css).appendTo($('head:first'));
			}

			// 検証環境用の背景画像を使うか
			$('body').toggleClass('test-env', res && res.useTestBgImage);

			// 本日のお知らせ（自社用）
			if (res && res.announcements && res.announcementHtml) {
				const $announcement = $(res.announcementHtml);
				const $tbl = new ResponsiveTable($announcement);
				$.each(res.announcements, function(i, row) {
					const $tr = $tbl.addRowResult(row);
					$tr.find('[data-field=contents]').html(NCI.escapeHtmlBR(row.contents));
					$tr.find('[data-field=linkTitle]').click(function() { window.open(row.linkUrl, '_blank'); });
				});
				$tbl.dispNoRecordMessage();
				$announcement.appendTo('body').modal({ keyboard : false, backdrop : "static" });
			}

			var corprationCode = '';

			if (res.loginInfo){
				// ログインユーザの会社コードを取得する。
				corprationCode = res.loginInfo.corporationCode;

				var cssChangeFlag = "false";
				var url = "../../getCssChangeFlagServlet";
				var request = "";
				var targetList = null;

				// セッション情報取得用のサーブレットを呼び出し、CSS切替フラグを取得する。
				$.ajax({
					type    : "GET",          //GET / POST
					url     : url ,  //送信先のServlet URL
					data    : request,        //リクエストJSON
					async   : false,           //true:非同期(デフォルト), false:同期
					success : function(data) {
						targetList = data["targetList"];
					},
					error : function(XMLHttpRequest, textStatus, errorThrown) {
					}
				});

				// css切り替え対象の会社か判定を行う。
				if (targetList.indexOf(corprationCode) >= 0){
					// linkタグの要素を取得する。
					var elements = document.getElementsByTagName('link');

					for (var i = 0; i < elements.length; i++){
						// linkタグの要素を配列から一つ取得する。
						var element = elements[i];

						// cssのフルパスを指定する。
						var fullPath = element.href;

						// cssが格納されているフォルダパスを取得する。
						var folderPath = fullPath.substring(fullPath.lastIndexOf('/assets') + 1, fullPath.lastIndexOf('/') + 1);

						// cssファイル名を取得する。
						var fileName = fullPath.substring(fullPath.lastIndexOf('/') + 1);

						// 切替対象のcssかを判定する。 TODO 判定文字列を固定にしているが、設定ファイルから取得するように変更する必要があるかも。
						if (fileName == 'original.css'){
							// 切替対象の場合、要素のhrefに切り替え後cssのパスを設定する。
							element.href = '../../' + folderPath + 'original_' + corprationCode + '.css';
						} else if(fileName == 'styles.css'){
							element.href = '../../' + folderPath + 'styles_' + corprationCode + '.css';
						}

					}
				}
			}
		})
		.always(function() {
			// メッセージをエレメントへ反映
			$i18n.each(function(i, elem) {
				const key = $i18n.eq(i).data('i18n');
				if (key != null && key !== '') {
					let value = NCI.getMessage(key);
					if (value != null) {
						$(elem).text(value);
					}
				}
			});
		});

		return jqXHR;
	},

	/**
	 * POST送信
	 * @param uri リクエスト先URL(/endpointからの相対パス)
	 * @param params 送信パラメータ
	 * @param successCallback [OPTION]成功時のコールバック関数
	 * @param errorCallback [OPTION]エラー時のコールバック関数。
	 * @param clearMessage メッセージをクリアしたくなければfalse、省略時はクリア
	 * @param hideBlockUI BlockUIが不要ならtrue。省略時はBlockUIを使う
	 */
	post: function(uri, parameters, keepMessage, hideBlockUI) {
		if (!keepMessage)
			NCI.clearMessage();

		let url = '../../endpoint';
		if (uri.indexOf('/') == 0)
			url += uri;
		else
			url += ('/' + uri);

		if (parameters == null || typeof(parameters) === 'undefined') {
			parameters = {};
		}

		// 表示しているデバイスのカテゴリ
		parameters.viewWidth = NCI.getViewWidth();

		// 操作者のセッション上のユーザ情報（クライアント側とサーバ側で同一ユーザが操作しているかの判定用）
		if (NCI.loginInfo) {
			parameters.clientCorporationCode = NCI.loginInfo.corporationCode;
			parameters.clientUserCode = NCI.loginInfo.userCode;
		}

		const settings = {
				"url" : url
				, "method" : 'POST'
				, "cache" : false
				, "contentType" : 'application/json'
				, "data" : JSON.stringify(parameters)
				, "dataType" : 'json'
				, "headers": {
					// CSRF対策 =================================================================================
					// iframe, imageなどからのリクエストではHTTPリクエストに独自のヘッダを付与することができない。
					// 独自のヘッダをつけるにはXMLHttpRequestを使うしかない。
					// そしてXMLHttpRequestを使う場合にはSame Origin Policyが適用されるため
					// 攻撃者のドメインからHTTPリクエストがくることはない。
					// つまり XMLHttpRequest経由で特定のヘッダ(ここでは"X-Requested-By")が付与されていれば、
					// サーバ側で正しいリクエストと判断できる。
					"X-Requested-By": "NCI WF Ver6"		// @see CsrfProtectionFilter.java
				}
		};

		// ブロックUIをアクティブ化
		if (!hideBlockUI)
			NCI.openBlockUI();

		const jqXHR = $.ajax(url, settings)
		.done(function(res, textStatus, jqXHR) {
			if (res == null && window.console) {
				window.console.warn('HTTPレスポンスが null です。APPサーバ側でレスポンスをリターンし忘れていませんか？');
			}
			// メッセージ
			if (res && res.successes && res.successes.length)
				NCI.addMessage('success', res.successes);
			if (res && res.warns && res.warns.length)
				NCI.addMessage('warning', res.warns);
			if (res && res.alerts && res.alerts.length)
				NCI.addMessage('danger', res.alerts);
		})
		.fail(function(jqXHR, textStatus, errorThrown) {
			NCI.defaultErrorCallback(jqXHR, textStatus, errorThrown, settings);
		})
		.always(function() {
			// ブロックUIを除去
			if (!hideBlockUI)
				NCI.closeBlockUI();
		});

		return jqXHR;
	},

	/**
	 * AJAXでGETリクエスト送信
	 * @param url リクエストURL
	 * @param params リクエストのパラメータ
	 */
	get : function(uri, parameters) {
		let url = '../../endpoint';
		if (uri.indexOf('/') == 0)
			url += uri;
		else
			url += ('/' + uri);

		const settings = {
				"url" : url
				, "method" : 'GET'
				, "cache" : false
				, "data" : parameters
				, "dataType" : 'json'
		};

		// ブロックUIをアクティブ化
		NCI.openBlockUI();

		const jqXHR = $.ajax(url, settings)
		.fail(function(jqXHR, textStatus, errorThrown) {
			NCI.defaultErrorCallback(jqXHR, textStatus, errorThrown, settings);
		})
		.always(function() {
			// ブロックUIを除去
			NCI.closeBlockUI();
		});
		return jqXHR;
	},

	/** 標準の通信エラーハンドラー */
	defaultErrorCallback : function(jqXHR, textStatus, errorThrown, settings) {
		const url = settings.url;
		const sts = jqXHR.status;
		const httpError = 'http ' + jqXHR.status + ' ' + settings.method + ' ' + url;

		// 検索条件不備によるエラーにより、同一画面を再表示したとき壊れた検索条件が復元されてしまうのを防ぐ
		const baseURL = '../../endpoint';
		if (url.indexOf(baseURL) === 0)
			window.sessionStorage.removeItem(url.substring(baseURL.length));
		else
			window.sessionStorage.removeItem(url);

		// コンソールへのログ
		if (window.console) {
			let msg = '';
			if (jqXHR.status && jqXHR.status != 200) {
				msg += httpError;
			} else {
				msg += (textStatus + ' ' + errorThrown + ' ' + url);
			}
			window.console.error(msg);
			if (jqXHR && (jqXHR.responseType === 'text' || jqXHR.responseType === '' || typeof(jqXHR.responseType) === 'undefined') && jqXHR.responseText)
				window.console.error(jqXHR.responseText);
			window.console.trace();
		}

		if (200 <= sts && sts <= 299) {
			// HTTP的には成功;
		} else if (300 <= sts && sts <= 399) {
			// 成功じゃないけど、REDIRECT等のためクライアントで出来ることは何もない;
		} else {
			let statusText = "";
			switch (sts) {
			case 0: statusText = textStatus; break;
			case 400:	statusText = "Bad Request"; break;
			case 401:	statusText = "Unauthenticated"; break;
			case 402:	statusText = "Payment Required"; break;
			case 403:	statusText = "Forbidden"; break;
			case 404:	statusText = "Not Found"; break;
			case 405:	statusText = "Method Not Allowed"; break;
			case 406:	statusText = "Not Acceptable"; break;
			case 407:	statusText = "Proxy Authentication Required"; break;
			case 408:	statusText = "Request Timeout"; break;
			case 409:	statusText = "Conflict"; break;
			case 410:	statusText = "Gone"; break;
			case 411:	statusText = "Length Required"; break;
			case 412:	statusText = "Precondition Failed"; break;
			case 413:	statusText = "Request Entity Too Large"; break;
			case 414:	statusText = "Request-URI Too Large"; break;
			case 415:	statusText = "Unsupported media type"; break;
			case 416:	statusText = "Requested range not satisfiable"; break;
			case 417:	statusText = "Expectation Failed"; break;
			case 500:	statusText = "Server Internal Error"; break;
			case 501:	statusText = "Not Implemented"; break;
			case 502:	statusText = "Bad Gateway"; break;
			case 503:	statusText = "Service Unavailable"; break;
			case 504:	statusText = "Gateway Timeout"; break;
			case 505:	statusText = "HTTP Version Not Supported"; break;
			default:	statusText = "HTTP" + sts + "; Unknown Error"; break;
			}
			const errorInfo = {
				"url" : url
				, "httpError" : httpError
				, "status" : jqXHR.status
				, "statusText" : statusText
				, "textStatus" : textStatus
				, "errorThrown" : errorThrown
			};
			// レスポンスがJSONエントリを持っていればパースして追加情報とする
			let json = null;
			if (jqXHR.responseType === 'arraybuffer') {
				// 帳票のダウンロード等ではエラー時のレスポンスもバイナリなので、バイナリを文字列化したうえでJSONパースする
				const utf8str = NCI.arraybufferToStr(jqXHR.response)
				try { json = JSON.parse(utf8str); }
				catch (e) { console.error(utf8str); };
			} else {
				json = jqXHR.responseJSON;
			}
			if (json) {
				$.each(json, function(name, value) {
					errorInfo[name] = value;
				});
				// リクエストされていたメッセージCDに対応する多言語リソースをローカルへキャッシュ
				if (json.messages && json.messages.length > 0) {
					const messages = json.messages;
					$.each(messages, function(i, p) {
						NCI.putMessage(p.messageCd, p.text)
					});
				}
			}

			// ユーザごとのトップページ
			if (NCI.loginInfo && NCI.loginInfo.topPageUrl)
				errorInfo.topPageUrl = NCI.loginInfo.topPageUrl;
			else if (json && json.loginInfo && json.loginInfo.topPageUrl)
				errorInfo.topPageUrl = json.loginInfo.topPageUrl;
			else
				errorInfo.topPageUrl = "unsecure/login.html";	// 何もなければ、仕方がない

			if (errorInfo.redirectUrl) {
				// リダイレクト
				NCI.redirect('../' + errorInfo.redirectUrl);
			}
			else if (errorInfo.alerts && errorInfo.alerts.length) {
				// エラーあり
				NCI.addMessage('danger', errorInfo.alerts);
			}
			else if (errorInfo.warnings && errorInfo.warnings.length) {
				// 警告あり
				NCI.addMessage('warning', errorInfo.warnings);
			}
			else {
				// エラー表示用HTMLをAJAXで取得し、当画面のエラーコンテンツとして流し込む
				const errorUrl = '../error/systemError.html';
				const timestamp = errorInfo.timestamp || new Date();
				$.get(errorUrl, null, function(errorHtml, status) {
					$('section').hide();
					$('body').append(errorHtml);

					// 読み込んだエラー表示用HTMLにエラーコンテンツを反映
					if (errorInfo.status !== 0)
						$('#___httpStatus___').text('HTTP' + errorInfo.status + '  ' + errorInfo.statusText);
					else
						$('#___httpStatus___').text('');
					$('#___url___').text(settings.method + ' ' + errorInfo.url);
					$('#___errorText___').text(errorInfo.statusText || errorInfo.textStatus || errorInfo.errorThrown);
					$('#___timestamp___').text(timestamp);
					$('#___topPageUrl___').text(errorInfo.topPageUrl);

					// エラー内容による画像や文言の切り替え
					if (textStatus === 'timeout') {		// タイムアウト
						$('#errorReason').text(NCI.getMessage('MSG0224'));
						$('#errImage').attr('src', '../../assets/nci/images/timeout.png');
						$('#sorryForTouble').hide();
					}
					else if (sts === 0) {				// サーバーへの接続エラー
						if (NCI.isCachedMessage('MSG0225'))
							$('#errorReason').text(NCI.getMessage('MSG0225'));
						else
							$('#errorReason').text('サーバーとの通信障害のため、ご要望の処理を続行できませんでした。');	// キャッシュされてないと多言語リソースが表示できないので仕方なくハードコード
						$('#errImage').attr('src', '../../assets/nci/images/power.png');
						$('#sorryForTouble').hide();
					}
					else if (sts === 403) {				// Forbidden（権限なし）
						$('#errorReason').text(NCI.getMessage('MSG0186'));
						$('#errImage').attr('src', '../../assets/nci/images/dont_enter.png');
						$('#sorryForTouble').hide();
					}
					else if (sts === 404) {				// Not found
						$('#errorReason').text(NCI.getMessage('MSG0185'));
						$('#errImage').attr('src', '../../assets/nci/images/not-found.png');
						$('#sorryForTouble').hide();
					}
					else if (sts === 409) {				// Conflict（クライアントとサーバでログイン者情報が異なる）
						$('#errorReason').text(NCI.getMessage('MSG0276'));
						$('#errImage').attr('src', '../../assets/nci/images/conflict.png');
						$('#sorryForTouble').hide();
					}
					else if (sts === 503) {	// Service Unavailable
						$('#errorReason').text(NCI.getMessage('MSG0184'));
					}
					else {					// Interna Server Error
						$('#errorReason').text(NCI.getMessage('MSG0158'));
					}
				});
			}
		}
		return jqXHR;
	},

	/** utf8のarraybufferを文字列化 */
	arraybufferToStr : function(ab) {
		const array = new Uint8Array(ab), len = array.length;
		let out = "", i = 0;
		while (i < len) {
			const c = array[i++];
			let char2, char3;
			switch (c >> 4) {
			case 0:
			case 1:
			case 2:
			case 3:
			case 4:
			case 5:
			case 6:
			case 7:
				// 0xxxxxxx
				out += String.fromCharCode(c);
				break;
			case 12:
			case 13:
				// 110x xxxx 10xx xxxx
				char2 = array[i++];
				out += String.fromCharCode(((c & 0x1F) << 6) | (char2 & 0x3F));
				break;
			case 14:
				// 1110 xxxx 10xx xxxx 10xx xxxx
				char2 = array[i++];
				char3 = array[i++];
				out += String.fromCharCode(((c & 0x0F) << 12)
						| ((char2 & 0x3F) << 6) | ((char3 & 0x3F) << 0));
				break;
			}
		}
		return out;
	},

	/**
	 * メッセージCDに対応したメッセージを返す。 キャッシュされたメッセージしか返さない仕様であるので、必要なメッセージがあるなら
	 * NCI.init(url, params)の paramsに属性 "messageCds"として、必要なメッセージCDを
	 * 文字配列として渡してください。それでメッセージ取得＆キャッシュされるようになります。
	 *
	 * @param messageCd
	 *            メッセージCD
	 * @param args
	 *            [省略可] メッセージCDに対応したメッセージをフォーマットする際に埋め込まれるパラメータ。詳細は
	 *            NCI.format()を参照
	 * @see NCI.format()
	 */
	getMessage : function(messageCd, args) {
		if (messageCd == null || messageCd === '')
			return null;
		const fmt = window.sessionStorage.getItem('MessageCd.' + messageCd);
		if (fmt == null && window.console) {
			window.console.log('messageCd [' + messageCd + ']はキャッシュされていません。必要なメッセージはあらかじめ NCI.init(url, params)の paramsに属性 "messageCds"として、必要なメッセージCDを文字配列として渡してください' )
			if (this.loginInfo)
				return '%{' + messageCd + '@' + this.loginInfo.localeCode + '}%';
			else
				return '%{' + messageCd + '@??}%';
		}
		if (!args || args.length === 0)
			return fmt;
		return NCI.format(fmt, args);
	},

	/**
	 * メッセージCDをキャッシュ
	 * @param messageCd キャッシュキーとなるメッセージCD
	 * @param text キャッシュ値である文言
	 */
	putMessage : function(messageCd, text) {
		window.sessionStorage.setItem('MessageCd.' + messageCd, text);
	},

	/**
	 * メッセージCDがキャッシュされているか
	 */
	isCachedMessage : function(messageCd) {
		const val = window.sessionStorage.getItem('MessageCd.' + messageCd);
		if (val == null)
			return false;
		const a =  !/^%{.+@.+}%$/.test(val);
		return !/^%{.+@.+}%$/.test(val);	// %{???@??}%形式の値はサーバ側で該当リソースなしを示すので、キャッシュ有に含めてはダメ
	},

	/**
	 * 文字列フォーマット(sprintf もどき)
	 *
	 * 　例1：NCI.format("{0} : {1} + {2} = {3}", "足し算", 8, 0.5, 8+0.5)
	 * 　　　　⇒ 足し算 : 8 + 0.5 = 8.5
	 * 　例2：NCI.format("{0} : {1} + {2} = {3}", ["足し算", 8, 0.5, 8+0.5])
	 * 　　　　⇒ 足し算 : 8 + 0.5 = 8.5
	 * 　例3：NCI.format("名前 : {name}, 年齢 : {age}", { "name":"山田", "age":128 } );
	 * 　　　　⇒ 名前 : 山田, 年齢 : 128
	 *
	 * @param fmt フォーマット
	 * @param params 可変長パラメータ（オブジェクト／配列）
	 */
	format : function(fmt, params) {
		if (fmt == null || typeof params == "undefined") {
			return fmt;
		}

		let fn = undefined;
		if (typeof params == "object") {
			// 引数 params が単一Object
			fn = function(m, k) { return params[k]; }
		}
		else {
			// 複数引数
			const args = arguments;
			fn = function(m, k) { return args[parseInt(k) + 1]; }
		}
		return fmt.replace(/\{(\w+)\}/g, fn);
	},

	/** リダイレクト */
	redirect : function(url) {
		NCI.openBlockUI();
		url += (url.indexOf('?') < 0 ? '?' : '&');
		url += '_tm=' + +new Date();	// キャッシュさせない

		if (Popup.isPopup()) {
			const $iframe = parent.$('div.nci-popup:last iframe.nci-popup:first');
			$iframe.attr('src', url);
		} else {
			window.location.replace(url);
		}
	},

	/**
	 * Dateオブジェクトを文字列化。
	 * @param date Dateオブジェクト
	 * @param format YYYY：年、MM：月。DD:日、hh:時、mm:分、ss:秒、S:ミリ秒。省略したら「YYYY/MM/DD」扱い
	 */
	formatDate : function(date, format) {
		let val = new String(format || 'YYYY/MM/DD');
		val = val.replace(/YYYY/g, date.getFullYear());
		val = val.replace(/MM/g, ('0' + (date.getMonth() + 1)).slice(-2));
		val = val.replace(/DD/g, ('0' + date.getDate()).slice(-2));
		val = val.replace(/hh/g, ('0' + date.getHours()).slice(-2));
		val = val.replace(/mm/g, ('0' + date.getMinutes()).slice(-2));
		val = val.replace(/ss/g, ('0' + date.getSeconds()).slice(-2));
		if (val.match(/S/g)) {
			const milliSeconds = ('00' + date.getMilliseconds()).slice(-3);
			const length = val.match(/S/g).length;
			for (var i = 0; i < length; i++) {
				val = val.replace(/S/, milliSeconds.substring(i, i + 1));
			}
		}
		return val;
	},

	/**
	 * SELECTタグの子要素(＝OPTIONタグ)を生成する
	 * @param $select 対象のSELECTタグ
	 * @param optionItems ラベルと値を同じにするなら単純配列、ラベルと値を変えるなら {'label':'ラベル', 'value':'値'}の配列。
	 * @param 新しい値。未設定なら以前の値
	 */
	createOptionTags : function($select, optionItems, newVal) {
		let enableTitle = false;
		$select.each(function(n, select) {
			// 処理前の選択内容を退避
			const oldIndex = select.selectedIndex;
			const oldValue = oldIndex < 0 ? '' : select.value;
			const df = document.createDocumentFragment();
			if (optionItems && optionItems.length > 0) {
				const len = optionItems.length;
				const type = typeof optionItems[0];
				let option, item;
				for (let i = 0; i < len; i++) {
					option = document.createElement('option');
					option.setAttribute('data-toggle', 'tooltip');	// bootstrapのツールチップ
					item = optionItems[i];
					if (type === 'string' || type === 'number'){
						// 単純な配列は値とラベルを同じにする
						option.value = item;
						option.textContent = item;
						if (item != null && item !== '') {
							option.title = item;
							enableTitle = true;
						}
					}
					else if (type === 'object') {
						// valueとlabelを属性として持つObject
						option.value = item.value;
						option.textContent = item.label;
						if (item.title != null && item.title !== '') {
							option.title = item.title;
							enableTitle = true;		// titleがあればツールチップを有効化
						}
					}
					// 初期値があるなら選択させる
					if (option.value === oldValue) {
						option.setAttribute("selected", "selected");
					}
					df.appendChild(option);
				}
			}
			// 古い選択肢をクリア→ 「select.innerHtml = ''」より下記のほうが高速だ
			if (typeof keepOld === 'undefined' || !keepOld) {
				let child;
				while (child = select.lastChild)
					select.removeChild(child);
			}
			select.appendChild(df);
		});

		// ツールチップ用イベント登録
		if (enableTitle) {
			const onChange = function(e) {
				// 選択した「選択肢のツールチップ」を「ドロップダウンリストのツールチップ」にする
				const select = e.target, i = select.selectedIndex;
				select.title = (i < 0 ? '' : select.options[i].title);
			};
			$select.off('change', onChange).on('change', onChange);	// イベントが重複しないよう既存を殺してから登録
		}
		return $select;
	},

	/** メニューの初期化 */
	initMenu : function(res) {
		// メニューHTML
		let $nav = $('#nav');
		if ($nav.length > 0 && res && res.menuHtml) {
			$nav.html(res.menuHtml);
			if (window.loadfunc)
				loadfunc();		// original.js
			$('header').removeClass('container-fluid');
		}

		if ($nav.length > 0 && res && res.loginInfo) {
			// ログイン者情報
			let loginInfo = res.loginInfo;
//			$('#menu-app-name').text(res.applicationName);
			$('#menu-user-id').text(loginInfo.userAddedInfo);	// TODO：新デザインに移行後は削除する
			$('#menu-user-name').text(loginInfo.userName);		// TODO：新デザインに移行後は削除する
			$('.menu-user-name').text(loginInfo.userAddedInfo + "    " + loginInfo.userName);

			// 言語リスト選択肢
			if (res && res.selectableLocales) {
				NCI.createOptionTags($('#menu-localeCode'), res.selectableLocales);
				$('#menu-localeCode').val(loginInfo.localeCode).data('origin', loginInfo.localeCode);
			} else {
				// 選択肢がないなら言語の切り替えを使用しないということだ
				$('#menu-localeCode').hide();
			}

			// ログイン可能な企業リストの選択肢
			if (res && res.loginableCorporations && res.loginableCorporations.length > 1) {
				NCI.createOptionTags($('#menu-corporationCode'), res.loginableCorporations).val(loginInfo.corporationCode);
			} else {
				// 選択肢が２つ以上なければ企業の切り替えをしないということだ
				$('#menu-corporationCode').hide();
			}

			// メニュー
			const $pcMenu = $('#pcMenu > ul').empty();
			const $spMenu = $('#spMenu > ul').empty();
			if (res.userMenus && res.userMenus.length) {
				// PC版メニュー
				$.each(res.userMenus, function(i, menu1) {
					const hasChild1 = menu1.children && menu1.children.length;
					const $link1 = $('<a>').attr('href', menu1.url).text(menu1.label);
					const $li1 = $('<li>').addClass('parent', hasChild1).append($link1).appendTo($pcMenu);
					if (hasChild1) {
						const $ul2 = $('<ul class="sub">').appendTo($li1);
						$.each(menu1.children, function(j, menu2) {
							const hasChild2 = menu2.children && menu2.children.length > 0;
							const $link2 = $('<a>').attr('href', menu2.url).text(menu2.label);
							const $li2 = $('<li>').addClass('sub_parent', hasChild2).append($link2).appendTo($ul2);
							if (hasChild2) {
								const $ul3 = $('<ul class="subsub">').appendTo($li2);
								$.each(menu2.children, function(k, menu3) {
									$('<li>').append($('<a>').attr('href', menu3.url).text(menu3.label)).appendTo($ul3);
								});
							}
						});
					}
				});
				// モバイル版メニュー
				let sub = 0, subsub = 0;
				$.each(res.userMenus, function(i, menu1) {
					const noChild1 = !menu1.children || menu1.children.length === 0;
					if (noChild1) {
						// 第一階層のみ
						const $link1 = $('<a>').attr('href', menu1.url).text(menu1.label);
						$('<li>').append($link1).appendTo($spMenu);
					} else {
						// 第二階層ありの第一階層
						const $li1 = $('<li class="parent">').appendTo($spMenu);
						const $span1 = $('<span data-toggle="collapse" class="icon pull-right" href="#sub-menu-' + (++sub) + '">')
								.append($('<i class="fa fa-plus"></i>'));
						const $link1 = $('<a data-toggle="collapse" href="#sub-menu-' + sub + '">')
							.text(menu1.label)
							.append($span1)
							.appendTo($li1);
						const $ul2 = $('<ul class="children collapse" id="sub-menu-' + sub + '">').appendTo($li1);
						$.each(menu1.children, function(i, menu2) {
							const noChild2 = !menu2.children || menu2.children.length === 0;
							if (noChild2) {
								// 第二階層のみ
								const $link2 = $('<a>').attr('href', menu2.url).text(menu2.label);
								$('<li>').append($link2).appendTo($ul2);
							} else {
								// 第三階層ありの第二階層
								const $li2 = $('<li class="parent">').appendTo($ul2);
								const $span2 = $('<span data-toggle="collapse" class="icon pull-right" href="#subsub-menu-' + (++subsub) + '">')
										.append($('<i class="fa fa-plus"></i>'));
								const $link2 = $('<a data-toggle="collapse" href="#subsub-menu-' + subsub + '">')
									.text(menu2.label)
									.append($span1)
									.appendTo($li1);
								const $ul3 = $('<ul class="children collapse" id="subsub-menu-' + sub + '">').appendTo($li2);
								$.each(menu2.children, function(i, menu3) {
									// 第三階層のみ
									const $link3 = $('<a>').attr('href', menu3.url).text('└' + menu3.label);
									$('<li>').append($link3).appendTo($ul3);
								});
							}
						});
					}
				});
				// モバイルのログアウトボタン
				const $btnLogout = $('<button id="btnLogout" class="btn btn-default margin">')
						.append($('<i class="fa fa-sign-out"></i>'))
						.append($('<span data-i18n="btnLogout"></span>').text(NCI.getMessage('btnLogout')));
				$('<li class="searchBtn">').append($btnLogout).appendTo($spMenu);
			}

			// メッセージをエレメントへ反映
			$nav.find('[data-i18n]').each(function(i, elem) {
				const $elem = $(elem);
				const key = $elem.data('i18n');
				if (key != null && key !== '') {
					let value = NCI.getMessage(key);
					if (value != null) {
						$elem.text(value);
					}
				}
			});
		}

		$(document).on('click', '#btnLogout, #lnkLogout', function() {
			// ログアウトの確認
			$('#confirmLogout').modal();
			NCI.confirm(NCI.getMessage('MSG0057'), function() {
				// ログアウトの確認でOK押下
				NCI.post('/login/logout').done(function(res) {
					if (res && res.success) {
						window.sessionStorage.clear();
						NCI.redirect('../' + res.redirectUrl);
					}
				});
			});
			return false;
		})
		.on('change', '#menu-localeCode', function() {
			// 言語の変更の確認
			NCI.confirm(NCI.getMessage('MSG0055'), function() {
				// 言語の変更の確認でOK押下
				const old = NCI.loginInfo.localeCode;
				NCI.post('/login/changeLocaleCode?newLocaleCode=' + $('#menu-localeCode').val()).done(function(res) {
					if (res && res.success) {
						// すべてのキャッシュをクリアする
						window.sessionStorage.clear();
						NCI.redirect('../' + res.redirectUrl);
					}
					else if (!res.success) {
						if (res.reason) alert(res.reason);
						this.value = old;
					}
				});
			}, function() {
				// 言語の変更の確認でCANCEL押下
				$('#menu-localeCode').val($('#menu-localeCode').data('origin'));
			});
			return false;
		})
		.on('change', '#menu-corporationCode', function() {
			// 企業の切り替えの確認
			const oldCorporationCode = NCI.loginInfo.corporationCode;
			const newCorporationCode = this.value;
			const newCorporationName = this.options[this.selectedIndex].label;
			NCI.confirm('入力途中のデータは破棄して、[' + newCorporationName + '] でログインしなおしますが、よろしいですか。', function() {
				const params = { "newCorporationCode" : newCorporationCode };
				NCI.post('/login/changeCorporation', params).done(function(res) {
					if (res && res.success) {
						// すべてのキャッシュをクリアする
						window.sessionStorage.clear();
						NCI.redirect('../' + res.redirectUrl);
					}
					else if (!res.success) {
						$('#menu-corporationCode').val(oldCorporationCode);
					}
				});
			});
		});
	},

	/**
	 * メッセージを追記
	 * @param cssClass 追記対象を特定するためのCSSクラス。{danger||success||warning}
	 * @param messages メッセージの配列
	 */
	addMessage : function(cssClass, messages) {
		if (cssClass && messages && messages.length) {
			const $header = $('header');
			if ($header.length === 0) {
				alert('<header>タグがないため、NCI.addMessage()出来ません。')
				return false;
			}
			// DIVタグ
			const id = "div-alert-" + cssClass;
			let $div = $('#' + id);
			if ($div.length === 0) {
				$div = $('<div id="' + id + '" class="container alert alert-' + cssClass + ' nci-messages">').insertAfter($header);
			}
			// ULタグ
			let $ul = $div.find('ul');
			if ($ul.length === 0) {
				$ul = $('<ul></ul>').appendTo($div);
			}
			// LIタグ（実際のメッセージ）
			const li = document.createElement('li');
			if (typeof messages === 'string') {
				$(li.cloneNode(false)).text(messages).appendTo($ul);
			}
			else if ($.isArray(messages)) {
				for (let i = 0; i < messages.length; i++) {
					$(li.cloneNode(false)).text(messages[i]).appendTo($ul);
				}
			}
			NCI.scrollTo($('body'));
		}
	},

	/**
	 * メッセージをクリア
	 * @param cssClass 追記対象を特定するためのCSSクラス。省略すると全種類をクリア。{alert||success||warning}
	 */
	clearMessage : function(cssClass) {
		// CSSクラスが指定されていれば特定のメッセージだけ、なければ全種類
		if (cssClass)
			selector = "#div-alert-" + cssClass;
		else
			selector = "div.nci-messages";
		$(selector).empty();
	},

	/**
	 * ダウンロード用にGETリクエストを送信
	 * @param uri URL
	 * @param parameters 送信パラメータ
	 * @param timeout ダウンロードモニターがタイムアウトするまでのミリ秒（リクエストタイムアウトとは異なるので注意）。デフォルトは10分。
	 */
	download : function(uri, parameters, timeout) {

		let url = '../../endpoint';
		if (uri.indexOf('/') === 0)
			url += uri;
		else
			url += ('/' + uri);

		// ダウンロードモニター用トークン
		const token = NCI.getUUID();
		url += (url.indexOf('?') > 0 ? '&' : '?') + '___token=' + token;

		if (typeof(timeout) === 'undefined')
			timeout = 10 * 60 * 1000;

		// トークン付でダウンロードモニター起動し、ブロックUI表示（ダウンロードモニター終了時に自動解除）
		const downloadMonitor = NCI.downloadMonitor(token, timeout);

		// JQuery.ajax()はバイナリデータを正しく扱えないので、XMLHttpRequestでリクエストを投げる
		const method = 'POST';
		const deferred = $.Deferred();
		const xhr = new XMLHttpRequest();
		xhr.open(method, url, true);
		xhr.timeout = timeout;
		xhr.responseType = 'arraybuffer';
		// CSRF対策 =================================================================================
		// iframe, imageなどからのリクエストではHTTPリクエストに独自のヘッダを付与することができない。
		// 独自のヘッダをつけるにはXMLHttpRequestを使うしかない。
		// そしてXMLHttpRequestを使う場合にはSame Origin Policyが適用されるため
		// 攻撃者のドメインからHTTPリクエストがくることはない。
		// つまり XMLHttpRequest経由で特定のヘッダ(ここでは"X-Requested-By")が付与されていれば、
		// サーバ側で正しいリクエストと判断できる。
		xhr.setRequestHeader("X-Requested-By", "NCI WF Ver6")	// @see CsrfProtectionFilter.java
		xhr.setRequestHeader('Content-type', 'application/json');
		xhr.addEventListener('load', function(){
			if (xhr.status === 200) {
				// Ajaxでバイナリデータを受け取り、それをBlobとして書き出す
		    	// ファイル名を求める
				let filename = "";
				const disposition = xhr.getResponseHeader('Content-Disposition');
				const attachment = disposition.indexOf('attachment') !== -1;
				if (disposition) {
					const matches = /filename[^;=\n]*=((['"]).*?\2|[^;\n]*)/.exec(disposition);
					if (matches != null && matches[1])
						filename = decodeURIComponent(matches[1].replace(/['"]/g, ''));
				}
				// Blobを生成
				const type = xhr.getResponseHeader('Content-Type');
				const blob = new Blob([ this.response ], { type : type });
				if (typeof window.navigator.msSaveOrOpenBlob !== 'undefined') {
					// IE専用
					window.navigator.msSaveOrOpenBlob(blob, filename);
				} else {
					const URL = window.URL || window.webkitURL;
					const downloadUrl = URL.createObjectURL(blob);
					if (filename && attachment) {
						const a = document.createElement("a");
						if (typeof a.download === 'undefined') {
							// Safariはダメっぽい？
							window.location = downloadUrl;
						} else {
							a.href = downloadUrl;
							a.download = filename;
							document.body.appendChild(a);
							a.click();
						}
					} else {
						window.location = downloadUrl;
					}
					// 使わなくなったURLオブジェクトを解放
					setTimeout(function() { URL.revokeObjectURL(downloadUrl); }, 100);
				}
				deferred.resolve(xhr.response);
			} else {
				downloadMonitor.abort();
				NCI.defaultErrorCallback(xhr, xhr.statusText, '', { 'url' : url, 'params' : parameters, 'method':method });
				deferred.reject("HTTP error: " + xhr.status);
			}
		});
		xhr.addEventListener('error', function() {
			// 例えばXHRのCross-Origin制約に引っかかった場合やネットワーク接続ができない場合など、サーバーとの接続ができなかった場合
			const msg = 'connect error';
			downloadMonitor.abort();
			NCI.defaultErrorCallback(xhr, msg, '', { 'url' : url, 'params' : parameters, 'method':method });
			deferred.reject(msg);
		});
		xhr.addEventListener('abort', function() {
			// 他から中断された
			const msg = 'abort';
			downloadMonitor.abort();
			NCI.defaultErrorCallback(xhr, msg, '', { 'url' : url, 'params' : parameters, 'method':method });
			deferred.reject(msg);
		});
		xhr.addEventListener('timeout', function(){
			// 時間切れ
			const msg = 'timeout';
			downloadMonitor.abort();
			NCI.defaultErrorCallback(xhr, msg, '', { 'url' : url, 'params' : parameters, 'method':method });
			deferred.reject(msg);
		});
		xhr.send(JSON.stringify(parameters));
		return deferred.promise();
	},

	/**
	 * ブロックUIを表示したうえでダウンロードモニターのリクエストを送信し、
	 * ダウンロード側リクエストを監視。監視完了するとモニター側のリクエスト完了時にブロックUIを解除
	 *
	 * @param token
	 *            ダウンロード側リクエストを識別するためのトークン。NCI.getUUID()等を使って生成すべし。
	 * @param timeout
	 *            タイムアウトまでの待機時間（ミリ秒）。省略時は10分。0ならタイムアウトしない
	 */
	downloadMonitor : function(token, timeout) {
		if (!timeout)
			timeout = 10 * 60 * 1000;

		// ブロックUIをアクティブ化
		NCI.openBlockUI();

		// ダウンロードモニター用リクエスト送信し、ダウンロード側リクエストを監視
		const params = { 'token': token, 'timeout': timeout };
		return $.get('../../endpoint/download-monitor/watch', params, function(result, status, jqXHR) {
			if (window.console) window.console.log('...downloadMonitor -> ' + result);
		}).always(function() {
			// ブロックUIを除去
			NCI.closeBlockUI();
		});
	},

	/** ブロックUIの表示要求カウンター */
	blockUiCount : 0,

	/** ブロックUIのアニメーション時間(ミリ秒) */
	blockUiTimer : 250,

	/** ブロックUIの生成 */
	createBlockUI : function() {
		$(
			'<div id="blockUI" tabindex="-1">' +
				'<div class="blockUI-backdrop"></div>' +
				'<span class="blockUI-content col-xs-12 col-sm-11">' +
//					'<img src="../../assets/nci/images/busy.gif" tabindex="-1" />' +
					'<span> Please wait for a while </span>' +
//					'<img src="../../assets/nci/images/busy.gif" tabindex="-1" />' +
				'</span>' +
			'</div>')
			.appendTo('body')
			.on('keydown keypress keyup select', function(ev) {
				if (ev.keyCode === 9) // タブ押下をキャンセル
					return false;
			});
	},

	/** ブロックUIをアクティブ化 */
	openBlockUI : function() {
		// 重複起動しないよう、ブロックUIの表示要求カウンター＝０のときだけ起動する
		if (NCI.blockUiCount++ === 0) {
			$('#blockUI').show();
		}
	},

	/**
	 * ブロックUIを除去
	 * @param force
	 * 	強制的にブロックUIを閉じる場合にtrue。省略時は false扱い。
	 * 	ただし非同期実行しているとブロックUIが閉じなくなってしまうので使い方には細心の注意を払うこと。
	 * 	副作用が強烈なので、frameworkのことがよく分かってないなら使わないほうがいい。
	 **/
	closeBlockUI : function(force) {
		// ブロックUIの非表示処理が前後しないよう、ブロックUIの表示要求カウンター＝０のときだけブロックUIを隠す
		if (--NCI.blockUiCount === 0 || force) {
			$('#blockUI').hide();
		}
	},

	/** トークン（UUID）を生成して返す */
	getUUID : function() {
		const h = ['0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f'];
		const k = ['x','x','x','x','x','x','x','x','-','x','x','x','x','-','4','x','x','x','-','y','x','x','x','-','x','x','x','x','x','x','x','x','x','x','x','x'];
		let u = '', i = 0, rb = Math.random() * 0xffffffff|0;
		while (i++ < 36) {
			const c = k[i - 1], r = rb&0xf, v = c == 'x' ? r : (r&0x3|0x8);
			u += (c == '-' || c == '4') ? c : h[v];
			rb = i % 8 == 0 ? Math.random() * 0xffffffff | 0 : rb >> 4;
		}
		return u;
	},

	/** クエリーストリングを返す、引数 keyがあればそのキーに対応した値を返す。同一キーで複数値があれば配列で返す。keyがなければ 未加工のクエリーストリングをそのまま返す。 */
	getQueryString : function(key) {
		const queryString = window.location.search.substring(1);
		if (key == null)
			return queryString;

		const queryStrings = queryString.split("&");
		const len = queryStrings.length;
		const values = [];
		for (let i = 0; i < len; i++) {
			const entry = queryStrings[i].split("=");
			if (entry[0] === key) {
				values.push(entry[1]);
			}
		};

		if (values.length === 0)
			return null;
		if (values.length === 1)
			return values[0];
		return values;
	},

	/** ダイアログのIDを自動採番するためのカウンター */
	dialogCount : 0,

	/**
	 * Yes/Noの確認ダイアログ表示
	 * @param msg メッセージ
	 * @param yesCallback Yesボタン押下したときのコールバック関数
	 * @param noCallback Noボタン押下したときのコールバック関数
	 **/
	confirm : function(msg, yesCallback, noCallback) {
		if (msg == null) {
			if (yesCallback)
				yesCallback();
			return false;
		}
		const textYes = NCI.getMessage('yes'), textNo = NCI.getMessage('no');

		const $div = $(
			'<div class="modal fade">' +
				'<div class="modal-dialog dialog-sm center-modal">' +
					'<div class="modal-content">' +
						'<div class="modal-body lead">' +
							'<i class="glyphicon glyphicon-question-sign orange"></i>' +
							'<span class="modal-body"></span>' +
						'</div>' +
						'<div class="modal-footer">' +
							'<button class="btn btn-primary button-yes">' +
								'<i class="glyphicon glyphicon-ok-sign"></i>' +
								'<span class="button-yes"></span>' +
							'</button>' +
							'<button class="btn btn-default button-no">' +
								'<i class="glyphicon glyphicon-remove-sign"></i>' +
								'<span class="button-no"></span>' +
							'</button>' +
						'</div>' +
					'</div>' +
				'</div>' +
			'</div>');
		$div.find('span.modal-body').text(msg);
		$div.find('span.button-yes').text(textYes);
		$div.find('button.button-yes').click(function(ev) {
			if (yesCallback) yesCallback(ev);
			$div.modal('hide');
		});
		$div.find('span.button-no').text(textNo);
		$div.find('button.button-no').click(function(ev) {
			if (noCallback) noCallback(ev);
			$div.modal('hide');
		});
		$div.on('hidden.bs.modal', function(e) {
			$(this).remove();
		})
		.on('shown.bs.modal', function (event) {
			// ポップアップの表示完了時
			$(this).find('button.button-yes').focus();

		})
		.appendTo('body').modal({
			keyboard : false,
			backdrop : "static"
		});

		// ダイアログを表示するたびにスクロールするのは目障りだ
//		NCI.scrollTo($div);
		return $div;
	},

	/**
	 * ダイアログ表示（OKボタンのみの）
	 * @param msg メッセージ
	 * @param callback Yesボタン押下したときのコールバック関数
	 **/
	alert : function(msg, callback) {
		if (msg == null) {
			if (callback)
				callback();
			return null;
		}
		else {
			const $div = $(
				'<div class="modal fade">' +
					'<div class="modal-dialog dialog-sm center-modal">' +
						'<div class="modal-content">' +
							'<div class="modal-body lead">' +
								'<i class="glyphicon glyphicon-info-sign blue"></i>' +
								'<span class="modal-body"></span>' +
							'</div>' +
							'<div class="modal-footer">' +
								'<button class="btn btn-primary button-ok">' +
									'<i class="glyphicon glyphicon-ok-sign"></i>' +
									'<span class="button-ok"></span>' +
								'</button>' +
							'</div>' +
						'</div>' +
					'</div>' +
				'</div>');
			$div.find('span.modal-body').text(msg);
			$div.find('span.button-ok').text("OK");
			$div.find('button.button-ok').click(function(ev) {
				if (callback)
					callback(ev);
				$div.modal('hide');
			});
			$div.on('hidden.bs.modal', function(e) {
				$(this).remove();
			}).appendTo('body').modal({
				keyboard : false,
				backdrop : "static"
			});
			return $div;
		}
	},

	/**
	 * flushScopeへの読み書き。
	 * ※読み込みアクセスは一度しかできない（読み込まれると削除される）。
	 * @param k キー
	 * @param value 値。省略時は etterアクセス(読み込み)、省略しなければ setterアクセス（書き込み）
	 */
	flushScope : function(k, value) {
		const key = "#FlushScope#" + k;
		if (typeof(value) === 'undefined') {
			// getter呼び出しなので値を返すが、返した値自体はストレージから削除
			// （flushScopeは、一度のみの読み込みしか出来ない）
			const val = NCI.localScope(key);
			window.localStorage.removeItem(key);
			return val;
		}
		// setter呼び出しなので、値をキーで保存
		return NCI.localScope(key, value);
	},

	/**
	 * window.sessionStorageへの読み書き（window.sessionStorage.setItem / getItemのラッパー）。値には文字列だけでなくObjectも扱える。
	 * @param key キー
	 * @param value 値（文字列だけでなくObjectでも可）。省略時は etterアクセス(読み込み)、省略しなければ setterアクセス（書き込み）
	 */
	sessionScope : function(key, value) {
		if (typeof(value) === 'undefined') {
			// getter呼び出しなので値を返す
			const val = window.sessionStorage.getItem(key);
			if (val === null) return val;
			try { return JSON.parse(val); }
			catch (e) { return val; }
		}
		// setter呼び出しなので、値をキーで保存
		window.sessionStorage.setItem(key, JSON.stringify(value));
		return value;
	},

	/**
	 * window.localStorageへの読み書き（window.localStorage.setItem / getItemのラッパー）。値には文字列だけでなくObjectも扱える。
	 * @param key キー
	 * @param value 値（文字列だけでなくObjectでも可）。省略時は etterアクセス(読み込み)、省略しなければ setterアクセス（書き込み）
	 */
	localScope : function(key, value) {
		if (typeof(value) === 'undefined') {
			// getter呼び出しなので値を返す
			const val = window.localStorage.getItem(key);
			try { return JSON.parse(val); }
			catch (e) { return val; }
		}
		// setter呼び出しなので、値をキーで保存
		window.localStorage.setItem(key, JSON.stringify(value));
		return value;
	},

	/** カレンダー（年月日） */
	ymdPicker : function($elements, params) {
		// bootstrap-datepicker.jsにて実装している
		const defaults = {
				clearBtn: true,
				language: NCI.loginInfo.localeCode,
				orientation: "auto",
				daysOfWeekHighlighted: "0,6",
				autoclose: true,
				todayHighlight: true,
				updateViewDate: true	// エレメント値を datepickerUI側へ反映する
		};
		const options = $.extend({}, defaults, params);
		const changeDate = function(e) {
			// 自分のバリデーション実行
			const $text = $(this).trigger('validate');
			const opt = $text.data('validate');
			// 自分と連動するFROM/TO日付エレメントがあれば、そちらもバリデーション実行
			if (opt && opt.to)
				$(opt.to).trigger('validate');
			if (opt && opt.from)
				$(opt.from).trigger('validate');
		}
		$elements
			.filter(function() {return !$(this).prop('readonly')})
			.datepicker(options)
			.off('changeDate', changeDate)
			.on('changeDate', changeDate);
	},

	/** カレンダー（年月） */
	ymPicker : function($elements, params) {
		// bootstrap-datepicker.jsにて実装している
		const defaults = {
			clearBtn: true,
			language: NCI.loginInfo.localeCode,
			orientation: "left",
			autoclose: true,
			format: "yyyy/mm",
			startView: 1,
			minViewMode: 1,
			updateViewDate: true	// エレメント値を datepickerUI側へ反映する
		};
		const options = $.extend({}, defaults, params);
		const changeDate = function(e) {
			// 自分のバリデーション実行
			const $text = $(this).trigger('validate');
			const opt = $text.data('validate');
			// 自分と連動するFROM/TO日付エレメントがあれば、そちらもバリデーション実行
			if (opt && opt.to)
				$(opt.to).trigger('validate');
			if (opt && opt.from)
				$(opt.from).trigger('validate');
		}
		$elements
			.datepicker(options)
			.off('changeDate', changeDate)
			.on('changeDate', changeDate);
	},

	/** 対象エレメントまでスクロール */
	scrollTo : function($target) {
		if ($target && $target.length) {
			const top = $target.eq(0).offset().top - $("#head_object").height();
			$("html,body").animate({ "scrollTop" : top });
		}
	},

	/** 文字列をHTMLエスケープ */
	escapeHtml : function(str) {
		return $(document.createElement('div')).text(str).html();
	},

	/** 文字列をHTMLエスケープし、かつ改行コードを<BR>タグへ置換 */
	escapeHtmlBR : function(str) {
		return NCI.escapeHtml(str).replace(/\n/g, "<br />");
	},

	setElementFromObj : function(elem, val) {
		const tagName = elem.tagName, type = elem.type, $elem = $(elem);

		// 値の正規化
		if (val == null)
			val = '';
		else
			val += '';

		// 対象エレメントごとの値設定方法
		if (/radio|checkbox/.test(type)) {
			// ラジオ/チェックボックスはエレメントに値が設定済みならチェック可否設定、エレメントに値が未設定なら値を付与
			const elValue = elem.getAttribute("value");		// elem.value だと 'on' になってしまうので、elem.getAttribute('value')でないとダメ
			if (elValue == null)
				elem.value = val;
			else
				elem.checked = (elValue === val);
		}
		else if (/SELECT/.test(tagName)) {
			elem.value = val;
		}
		else if (/INPUT|TEXTAREA/.test(tagName)) {
			elem.value = val;
			NCI.fillFormat($elem);	// フォーマットの適用
		}
		else {
			// SPAN等はHTMLエスケープ済みならHTMLとして設定。
			// 未エスケープならここでエスケープ。
			if ($elem.attr('data-html-escaped') != undefined)
				$elem.html(val);
			else
				$elem.text(val);
			NCI.fillFormat($elem);	// フォーマットの適用
		}
	},

	/**
	 * 対象Objectの属性名と一致するid/name/data-fieldをもつエレメントへ、Objectの値を設定。
	 * @param obj 対象Object
	 * @param $root 絞り込み要素となるルートエレメント。省略した場合は全エレメントが対象
	 */
	toElementsFromObj : function(obj, $root, ignores) {
		ignores = ignores || [];
		$.each(obj, function(prop, val) {
			prop = NCI.escapeSelector(prop);
			if (prop.indexOf('@class') >= 0)
				return true;
			if (ignores && ignores.length && ignores.indexOf(prop) >= 0)
				return true;

			let $targets = $('[id=' + prop + '], [name=' + prop + '], [data-field=' + prop + ']', $root);
			$targets.each(function(i, elem) {
				NCI.setElementFromObj(elem, val);
			});
		});
	},

	/**
	 * ルート要素配下の属性にid/name/data-fieldをもつエレメントを抜き出して
	 * その値（value/text)を収集し、オブジェクトとして返す
	 * @param $root ルート要素
	 * @param ignores 無視すべきカラム名の配列
	 */
	toObjFromElements : function($root, ignores) {
		ignores = ignores || [];
		const params = {};
		$root.find('input, select, textarea, td, a, span, label')
			.filter('[id],[name],[data-field]')
			.not("[type=reset], [type=submit], [type=button]")
			.each(function(i, elem) {
				let prop = elem.getAttribute("data-field") || elem.name || elem.id || '';
				if (prop.indexOf('@class') === 0)	// サーバ側クラス情報は無視
					return true;
				if (ignores && ignores.length && ignores.indexOf(prop) >= 0)
					return true;

				let tagName = elem.tagName, type = elem.type, value, $elem = $(elem);
				if (/radio|checkbox/.test(type)) {
					if (elem.checked) {
						value = elem.value;
					} else if (params[prop] == null) {
						value = '';		// チェックされてないということを示す
					}
				} else if (/INPUT|SELECT|TEXTAREA/.test(tagName)) {
					const v = $elem.data('validate');
					if (v && v.pattern && /integer|numeric/.test(v.pattern))
						value = NCI.getPureValue(elem.value, v);
					else
						value = $elem.val();
				} else {
					const v = $elem.data('validate');
					if (v && v.pattern && /integer|numeric/.test(v.pattern))
						value = NCI.getPureValue($elem.text(), v);
					else
						value = $elem.text();

				}

				if (value != null)
					params[prop] = value;
			});
		return params;
	},

	/**
	 * ルート要素の配下のtableから、属性id/name/data-fieldをもつエレメントを抜き出して
	 * その値(value/text)を収集し、配列として返す
	 * @param $root ルート要素
	 * @param ignores 無視すべきカラム名の配列
	 */
	toArrayFromTable : function($root, ignores) {
		ignores = ignores || [];
		const rows = [];
		$root.find('table tbody tr').each(function(i, tr) {
			let row = NCI.toObjFromElements($(tr), ignores);
			rows.push(row);

		});
		return rows;
	},


    /**
     * カンマ区切り
     */
    addComma : function(val, option) {
    	if (option && !!option.notAddComma) 	// カンマ区切りなしが指定されているか？
    		return val;
		if (val == null || val.length === 0)
			return val;
    	return ('' + val).replace(/^(-?[0-9]+)(?=\.|$)/, function(s){ return s.replace(/([0-9]+?)(?=(?:[0-9]{3})+$)/g, '$1,');});
    },

    /**
     * カンマ削除
     */
    removeComma : function(val) {
		if (val == null || val.length === 0)
			return val;
		return ('' + val).replace(/,/g, '');
    },

    /**
     * 日付にスラッシュ追加
     */
    addSlash : function(val) {
    	const date = Validator.toDate(val);
    	if (date && val.length >= 4) {
    		const yyyy = val.substr(0, 4);
    		const len = val.length;
    		if (len >= 6) {
    			const mm = val.substr(4, 2);
    			if (len === 6) {
        			return yyyy + '/' + mm;
    			}
    			if (len === 8) {
					dd = val.substr(6, 2);
					return yyyy + '/' + mm + '/' + dd;
				}
    		}
    	}
		return val;
    },

    /**
     * スラッシュ削除
     */
    removeSlash : function(val) {
    	return val.replace(/\//g,'');
    },

	/**
	 * 設定された書式を除去する（エレメントがフォーカスを得たときを想定）
	 */
	removeFormat : function($targets) {
		$targets.each(function(i, elem) {
			const $target = $(elem);
			const option = $target.data('validate');
			if (option) {
				let val = $target.val();
				if (val && val.length > 0) {
					switch(option.pattern){
//					case 'date' :		//日付[yyyy/mm/dd]型
//						val = NCI.removeSlash(val);	/* これを行うと datepickerを表示しながら手入力するとdatepicker側に型エラー扱いされる（datepickerはスラッシュありの前提だから） */
//						break;
					case 'integer' :	//整数型
					case 'numeric' :	//数値型
						// 接頭語・接尾語を除去
						val = NCI.removePrefixSuffix(val, option.prefix, option.suffix);
						// 桁区切りのカンマ除去
						val = NCI.removeComma(val);
						break;
					}
					$target.val(val);
				}
			}
		});
	},

	/**
	 * エレメントに設定された書式を付与する（エレメントがフォーカスを失った時を想定）
	 */
	fillFormat : function($targets) {
		$targets.each(function(i, elem) {
			const $target = $(elem);
			const option = $target.data('validate');
			if (!option)
				return true;
			const isInputable = /INPUT|TEXTAREA/.test(elem.tagName);
				let val = isInputable ? $target.val() : $target.text();
				if (val && val.length > 0) {
					switch(option.pattern){
					case 'date' :		//日付[yyyy/mm/dd]型
				case 'ym' :			//日付[yyyy/mm]型
						val = NCI.addSlash(val);
						break;
					case 'integer' :	//整数型
					case 'numeric' :	//数値型
						// 端数処理と小数点以下ゼロ埋めしたうえで、書式チェック
						val = NCI.removeComma(val);
						val = Validator._formatDecimalPoint(val, option);
						// マイナスなら赤字
						const flag = (!!option.redIfNegative && parseInt(val, 10) < 0);
						$target.toggleClass('red', flag);
						// 桁区切りのカンマ付与
						val = NCI.addComma(val, option);
						// 接頭語・接尾語を付与
						val = NCI.addPrefixSuffix(val, option.prefix, option.suffix);
						break;
					}
				if (isInputable)
					$target.val(val);
				else
					$target.text(val);
			}
		});
	},

	/**
	 * エレメントに設定されている書式を除去した値を返す（エレメントからDBへ設定すべき値を吸い上げるときを想定）
	 */
	getPureValue : function(val, option) {
		if (option && option.pattern && /integer|numeric/.test(option.pattern)) {
			// 接頭語・接尾語・カンマ桁区切りを除去
			val = NCI.removePrefixSuffix(val, option.prefix, option.suffix);
			val = NCI.removeComma(val);
		}
		return val;
	},

    /** 固定の小数点桁数用に、末尾にゼロを付与して返す */
    zeroPadRight : function(value, decimalPlaces) {
    	let val = '' + value;
		if (decimalPlaces > 0) {	// 固定桁数で小数点を表示する場合
			let decimal = 0, dot = val.indexOf('.');
			if (dot < 0)
				val += '.';
			else
				decimal = val.substring(dot + 1).length;
			const len = decimalPlaces - decimal;
			for (let i = 0; i < len; i++) {
				val += '0';
			}
		}
		return val;
    },

	/**
	 * 対象文字列から接頭語と接尾語を除去
	 * @param val 対象文字列
	 * @param prefix 接頭語
	 * @param suffix 接尾語
	 */
    removePrefixSuffix : function(val, prefix, suffix) {
		if (val != null) {
			val += '';	// 文字列変換
			if (prefix != null) {
				prefix += '';	// 文字列変換
				const pos = val.indexOf(prefix);
				if (pos >= 0)
					val = val.substring(pos + prefix.length);
			}
			if (suffix != null) {
				suffix += '';	// 文字列変換
				const pos = val.lastIndexOf(suffix);
				if (pos >= 0)
					val = val.substring(0, pos);
			}
		}
		return val;
	},

	/**
	 * 対象文字列へ接頭語と接尾語を付与、ただし対象文字列がNULL/ブランクなら何もしない
	 * @param val 対象文字列
	 * @param prefix 接頭語
	 * @param suffix 接尾語
	 */
    addPrefixSuffix : function(val, prefix, suffix) {
		if (val == null) {
			val = '';
		}
		else {
			// いったん接頭語・接尾語を除去
			val = NCI.removePrefixSuffix(val, prefix, suffix);

			if (prefix != null) {
				val = ('' + prefix + val);
			}
			if (suffix != null) {
				val += ('' + suffix);
			}
		}
		return val;
	},

	/** Timestamp型の文字列(yyyy/MM/dd hh:mi:ss.SSSSSS)をLong値へ変換 */
	timestampToLong : function(value) {
		if (value > '') {
			// IWF API経由でのTimestamp型はナノ秒まで保持しているが、JPA経由でのTimestampはミリ秒の範囲でしか値を保持してない。
			// ようは '.SSSSSS'部分の精度が3桁か6桁かが異なるので、どちらでも対応できるよう正規表現に工夫が必要だ
			const result = value.match(/^(\d{4})\/(\d{2})\/(\d{2}) (\d{2})\:(\d{2})\:(\d{2})\.(\d{1,3})(\d*)$/);
			if (result && (result.length === 8 || result.length === 9)) {
				const yyyy = parseInt(result[1], 10);
				const mm = parseInt(result[2], 10) - 1;
				const dd = parseInt(result[3], 10);
				const hh = parseInt(result[4], 10);
				const mi = parseInt(result[5], 10);
				const ss = parseInt(result[6], 10);
				const sss = NCI.padRight(result[7], 3, '0');
				const d = new Date(yyyy, mm, dd, hh, mi, ss, sss);

				if (!isNaN(d) && d.getFullYear() == yyyy && d.getMonth() == mm && d.getDate() == dd
						&& d.getHours() == hh && d.getMinutes() == mi && ﻿d.getSeconds() == ss && d.getMilliseconds() == sss) {
					return d.getTime();
				}
			}
		}
		return null;
	},

	/**
	 * 右端のパディング処理（右端を指定文字で埋める）
	 * @param value 文字列
	 * @param size 埋める文字数
	 * @param ch 埋め込む文字
	 */
	padRight : function(value, size, ch) {
		let s = ((value == null) ? '' : '' + value);
		for ( ; s.length < size; s += ch);
		return s;
	},

	/**
	 * 左端のパディング処理（左端を指定文字で埋める）
	 * @param value 文字列
	 * @param size 埋める文字数
	 * @param ch 埋め込む文字
	 */
	padLeft : function(value, size, ch) {
		let s = ((value == null) ? '' : '' + value);
		const len = size - s.length;
		for (let i = 0; i < len; i++)
			s = ch + s;
		return s;
	},

	/** jqueryのセレクタ用にエスケープ */
	escapeSelector : function(selector) {
		if (selector == null || selector === '')
			return selector;
		// jqueryのセレクタでエスケープが必要なのは「 !"#$%&'()*+,./:;<=>?@[\]^`{|}~」。
		// 「 」(半角スペース)や「>」などの「結合子」を含んでいることに留意せよ。
		return selector.replace(/[ !"#$%&'()*+,.\/:;<=>?@\[\\\]^`{|}~]/g, "\\$&");
	},

	/** スネークケース文字列をキャメルケース文字列に変換 */
	toCamelCase : function(str) {
		if (str == null || typeof str != 'string')
			return str;
		str = str.toLowerCase();
		return str.replace(/[-_](.)/g, function(match, group1) {
			return group1.toUpperCase();
		});
	},

	/** システム日付を yyyy/MM/dd形式で返す */
	today : function() {
		const dt = new Date();
		const y = dt.getFullYear();
		const m = ("00" + (dt.getMonth() + 1)).slice(-2);
		const d = ("00" + dt.getDate()).slice(-2);
		const result = y + "/" + m + "/" + d;
		return result;
	},

	/**
	 * 画面の表示幅を返す
	 * @return 'xs':モバイル、'sm':タブレット、'md':PC、'lg':PC(大型)
	 */
	getViewWidth : function() {
		let w = $(window).width();
		if (w === 0) {
			// ポップアップ画面で初期化前だと 幅が 0pxとなってしまうため、親画面のサイズを使う
			w = $(parent).width();
		}
		if (w < 768) return 'xs';	// モバイル
		if (w < 992) return 'sm';	// タブレット
		if (w < 1200) return 'md';		// PC
		else return 'lg';			// PC(大型)
	},

	/** doLater()用のタイマーリスト */
	timerIds : {},

	/**
	 * 関数の遅延実行。遅延中に何度呼び出されても一度しか実行しない
	 * @param callback 実行する関数
	 * @param wait 遅延するミリ秒
	 */
	doLater : function(callback, wait) {
		// 遅延実行が予約されているならキャンセル
		if (callback in NCI.timerIds) {
			window.clearTimeout(NCI.timerIds[callback]);
		}
		// 遅延実行を登録する
		NCI.timerIds[callback] = window.setTimeout(function() {
			callback.call();
			delete NCI.timerIds[callback];
		}, wait);
	},

	/**
	 * 汎用マスタの検索ポップアップ画面を開く
	 * @param tableName 汎用マスタのテーブル名
	 * @param tableSearchCode 汎用マスタのテーブル検索コード
	 * @param callback ポップアップ画面で選択後に呼び出されるコールバック関数(関数仕様：function(entity) {} )
	 * @param initConditions 初期検索条件Map
	 * @param trigger 何によって検索ポップアップが起動されたかを識別するためにコールバック関数に渡される引数。ボタンとか。
	 * @param corporationCode 企業コード。省略時は操作者企業コード
	 */
	openMasterSearch : function(tableName, tableSearchCode, callback, initConditions, trigger, corporationCode) {
		if (!tableName)
			throw new Error("汎用テーブル検索ポップアップのtableNameが指定されていません");
		if (!tableSearchCode)
			throw new Error("汎用テーブル検索ポップアップのtableSearchCodeが指定されていません");
		if (!callback)
			throw new Error("汎用テーブル検索ポップアップのcallbackが指定されていません");
		const params = {
			"tableName" : tableName,
			"corporationCode" : corporationCode || NCI.loginInfo.corporationCode,
			"tableSearchCode" : tableSearchCode,
			"initConditions" : initConditions || {}
		}
		Popup.open('../ti/ti0000.html', callback, params, trigger);
	}
};

/**
 * ページング付のtableレンダラークラス。ページングなしなら ResponsiveTableを使うべし。
 * @param $root (必須)起点エレメント（レンダリング対象のtableタグを含む）
 * @param url 検索処理を行うときのEndpointへのURL
 * @param 検索処理コールバック関数
 * @param msgNoRecord (任意)該当レコードなしときの個別メッセージ。未指定なら標準メッセージを使用。
 */
const Pager = function($root, url, searchFunction, msgNoRecord) {
	/** 検索EndpointへのURL */
	this.url = url;
	/** 検索結果を反映するルートエレメント(JQueryオブジェクト) */
	this.$root = $root;
	/** 呼び元の検索実行関数 */
	this.searchFunction = searchFunction;
	/** 検索条件 */
	this.condition = null;
	/** 検索結果のプロパティ名 */
	this.resultPropName = 'results';
	/** 検索結果反映用クラス */
	this.responsiveTable = new ResponsiveTable($root, msgNoRecord);

	/** 初期化オプションフラグ：ページあたりの行数の初期化を行うか */
	const CREATE_PAGE_SIZE = 1;

	/**
	 * 初期化、呼ばなくても良い
	 * @param flags 初期化オプションフラグ。初期化オプションフラグ定数と論理積をとって含まれていればそのオプションを実施。省略時はすべて実行
	 **/
	this.init = function(flags) {
		// ページあたりの行数
		if (!flags || flags & CREATE_PAGE_SIZE > 0) {
			const $pageSize = $('select.pageSize', this.$root);
			NCI.createOptionTags($pageSize, PAGE_SIZE_ARRAY).val("50");

			const self = this;
			if (this.searchFunction) {
				$pageSize.on('change', function(ev) {
					// ページサイズ上下同期
					$('select.pageSize', self.$root).val(this.value);
					// 検索
					self.searchFunction(1);
				});
			}
		}
		return this;
	}

	/**
	 * 検索リクエストを送信し、その結果を画面に反映
	 * @param cond 検索条件
	 * @param keepMessage 既存メッセージを保持するなら true
	 **/
	this.search = function(cond, keepMessage) {
		// 検索と検索結果の反映
		if (!cond['pageNo'] || cond['pageNo'] < 0)
			if (window.console) {
				window.console.warn('検索条件に pageNo がありません。ページ制御は行われません');
				window.console.dir(cond);
			}
		if (!cond['pageSize'] || cond['pageSize'] < 0)
			if (window.console) {
				window.console.warn('検索条件に pageSize がありません。ページ制御は行われません');
				window.console.dir(cond);
			}

		// 検索条件の保存
		this.condition = cond;
		this.saveCondition();

		// 検索
		const self = this;
		return NCI.post(self.url, self.condition, keepMessage).done(function(res) {
			if (res && res.success) {
				self.showSearchResult(res);
			}
		});
	};

	/** 検索条件を保存 */
	this.saveCondition = function() {
		const value = (this.condition == null ? '' : JSON.stringify(this.condition));
		window.sessionStorage.setItem(this.url, JSON.stringify(value));
	};

	/**
	 * 検索条件の取得
	 * ※下記のloadConditionと違い、sessionStorageから取得するだけ
	 * 　sessionStorageから削除(removeItem)は行わないので注意すること！
	 */
	this.getCondition = function() {
		const value = JSON.parse(window.sessionStorage.getItem(this.url));
		return (!value || !value.length) ? null : JSON.parse(value);
	};

	/**
	 * 検索条件を復元して画面に反映
	 * @return 復元できたら復元された検索条件を返す、復元できなければ null。
	 */
	this.loadCondition = function() {
		const value = JSON.parse(window.sessionStorage.getItem(this.url));
		window.sessionStorage.removeItem(this.url);
		let isLoaded = false
		if (!value || !value.length) {
			this.condition = { pageNo : 1, pageSize : 10 };	// ソート条件は不明なので、付与できない
		}
		else {
			this.condition = JSON.parse(value);
			// 復元内容をできるだけ画面へ書き戻す
			const ignores = ['pageNo', 'sortColumn', 'sortAsc', 'messageCds'];
			$.each(this.condition, function(name, val) {
				// 無視リストにいる項目は対象外
				if (ignores.indexOf(name) >= 0) {
					return true;
				}
				// ページあたりの行数
				if (name === 'pageSize') {
					$('select.' + name, this.$root).val(val);
					return true;
				}
				// その他
				const $id = $('#' + name);
				if ($id.length) {
					if ($id.prop('tagName') === 'INPUT' && $id.prop('type') === 'checkbox')
						$id.prop('checked', true);
					else
						$id.val(val);
					return true;
				}
				else {
					const $name = $('input[type="radio"][name="' + name + '"][value="' + val + '"]');
					if ($name.length) {
						$name.prop('checked', true);
						return true;
					}
				}
			});
			isLoaded = true;
		}
		return (isLoaded ? this.condition : null);
	}

	/** 検索と検索結果の反映 */
	this.showSearchResult = function(res) {
		// 総行数・ページ数・ページ番号・ページあたりの行数
		this.fillPageInfo(res);
		// ページリンク
		this.fillPageLink(res);
		// テーブルへ検索結果を反映
		const results = res[this.resultPropName];
		this.fillTable(results);
		// テーブルのヘッダ列にソート機能を付与
		this.fillSortLink(res);
	};

	/** テーブルのヘッダ列にソート機能を付与 */
	this.fillSortLink = function(res) {
		const self = this;
		const sortAsc = !!self.condition.sortAsc;
		const sortColumn = self.condition.sortColumn;

		$('table.responsive thead a[data-sort]', this.$root).each(function(i, link) {
			const $link = $(link);
			$link.find('i').remove();
			const dataSort = $link.attr('data-sort');
			if (dataSort === sortColumn) {
				// ソート方向を示す矢印アイコン
				$(document.createElement('i'))
					.addClass(sortAsc ? 'glyphicon glyphicon-arrow-up' : 'glyphicon glyphicon-arrow-down')
					.prependTo($link);
			}
			// ソートリンク押下時のイベント
			$link.off('click').on('click', function(e) {
				if (dataSort === self.condition.sortColumn)
					self.condition.sortAsc = !sortAsc;
				else
					self.condition.sortColumn = dataSort;

				// 新しいソート順で検索
				if (self.searchFunction)
					self.searchFunction();
			});
		});
	};

	/** 総行数・ページ数・ページ番号・ページあたりの行数を反映 */
	this.fillPageInfo = function(res) {
		// ページング情報（0件なら非表示）
		const results = res[this.resultPropName];
		const visible = results && results.length > 0;
		$('div.fixed-table-pagination', this.$root).toggleClass('hide', !visible);

		// 総行数・ページ数・ページ番号・ページあたりの行数
		const $pagination = $('div.fixed-table-pagination', this.$root);
		let $pagingInfo = $('span.pagination-info', $pagination);
		if ($pagingInfo.length === 0) {
			$pagingInfo = $('<span class="pagination-info">').appendTo($pagination);
		}
		$pagingInfo.empty().append(NCI.getMessage('MSG0204', [res.allCount, res.start, res.end]));

		// TODO：上記ですべて事足りるはずなので、デザインの反映が終わったら削除する
		$('.page-no', this.$root).text(res.allCount == 0 ? 0 : res.pageNo);
		$('.page-count', this.$root).text(res.pageCount);
		$('.all-count', this.$root).text(res.allCount);
		$('.start-row-no', this.$root).text(res.start);
		$('.end-row-no', this.$root).text(res.end);
	};

	/** ページリンクを反映 */
	this.fillPageLink = function(res) {
		// ページ番号リンク
		const $pagenation = this.$root.find('ul.pagination').empty();
		const results = res[this.resultPropName];
		if (!results || results.length === 0) {
			$pagenation.hide();
		}
		else {
			$pagenation.attr('data-currentPageNo', res.pageNo).show();
			const RANGE = 2, SPACE = RANGE + 1;
			const pageNo = res.pageNo, pageCount = res.pageCount;
			const dispStart = Math.max(1, pageNo - RANGE), dispEnd = Math.min(pageCount, pageNo + RANGE);
			const start = pageNo - SPACE, end = pageNo + SPACE;

			// 「前のページへ」リンク
			const cssPrev = pageNo > 1 ? "" : "invisible";
			const msgPrev = NCI.getMessage('prevPage');
			$('<li class="first ' + cssPrev + '"><a href="#" data-pageNo="1"><i class="glyphicon glyphicon-fast-backward"></i>&nbsp;</a></li>').appendTo($pagenation);
			$('<li class="prev ' + cssPrev + '"><a href="#" data-pageNo="' + (pageNo - 1) + '"><i class="glyphicon glyphicon-backward"></i> ' + msgPrev + '</a></li>').appendTo($pagenation);
			// ページリンク
			for (let n = start; n <= end; n++) {
				if (dispStart <= n && n <= dispEnd) {
					const activeCss = (n === pageNo) ? "active" : "";
					$('<li class="hidden-xs ' + activeCss + '"><a href="#" data-pageNo="' + n + '">' + n + '</a></li>').appendTo($pagenation);
				}
				else {
					// 連続してページリンクを押下し続けたとき、次へ／前へのリンク位置がずれるとユーザビリティが悪くなるから、ずれないようブランクを出力
					$('<li class="hidden-xs"><a class="invisible"> ' + n + '</a></li>').appendTo($pagenation);
				}
			}
			// 「次のページ」へリンク
			const cssNext = pageNo < pageCount ? "" : "invisible";
			const msgNext = NCI.getMessage('nextPage');
			$('<li class="next ' + cssNext + '"><a href="#" data-pageNo="' + (pageNo + 1) + '">' + msgNext + ' <i class="glyphicon glyphicon-forward"></i></a></li>').appendTo($pagenation);
			$('<li class="last ' + cssNext + '"><a href="#" data-pageNo="' + pageCount + '"><i class="glyphicon glyphicon-fast-forward"></i>&nbsp;</a></li>').appendTo($pagenation);
		}
	};

	/**
	 * 検索条件パラメータ生成
	 * @param $elements 検索条件のjqueryオブジェクト集合
	 * @param pageNo [省略可]ページ番号(1スタート)。省略すれば1
	 */
	this.createCondition = function ($elements, pageNo) {
		// ページ番号が指定されていればそのページを、無指定なら現ページ
		const old = this.condition;
		this.condition = {
			'pageSize' : +($('select.pageSize:first', this.$root).val()),
			'pageNo' : +(pageNo || $('ul.pagenation', this.$root).attr('data-currentPageNo') || (this.condition && this.condition.pageNo) || 1),
			'sortColumn' : old == null ? null : old.sortColumn,
			'sortAsc' : old == null ? false : old.sortAsc
		};

		if ($elements && $elements.length) {
			const self = this;
			$elements.each(function(i, elem) {
				// 属性として idもnameもないなら無視
				const id = elem.id || elem.name;
				if (!id) return true;

				const tagName = elem.tagName;
				if (tagName === 'INPUT') {
					const type = elem.type;
					switch (type) {
					case 'radio':
					case 'checkbox':
						if (elem.checked) self.condition[id] = elem.value;
						break
					case 'botton':
					case 'submit':
					case 'reset':
					case 'image':
						break;
					default:
						if (elem.value.length) {
							// 書式設定があれば除去して吸い上げる
							const option = elem.getAttribute('data-validate');
							if (option)
								self.condition[id] = NCI.getPureValue(elem.value, $.parseJSON(option));
							else
								self.condition[id] = elem.value;
						}
						break;
					}
				}
				else if (tagName === 'SELECT' || tagName === 'TEXTAREA') {
					if (elem.value.length)
						self.condition[id] = elem.value;
				}
			});
		}
		return this.condition;
	};

	/**
	 * テーブルへ検索結果を反映
	 * @param 検索結果の配列
	 **/
	this.fillTable = function(results) {
		this.responsiveTable.fillTable(results);
	};

	/**
	 * テーブルへ検索結果を１行分だけ反映
	 * @param $tr 結果を反映する対象
	 * @param rowIndex 行Index（0スタート）
	 * @param entity 検索結果
	 * @param labels ヘッダ行のラベル配列
	 **/
	this.fillRowResult = function($tr, rowIndex, entity, labels) {
		this.responsiveTable.fillRowResult($tr, rowIndex, entity, labels);
	},

	/** 新しく行を追加 */
	this.addRowResult = function(entity) {
		return this.responsiveTable.addRowResult(entity);
	},

	/** テンプレートを取得する */
	this.getTemplate = function() {
		return this.responsiveTable.getTemplate();
	},

	/** ヘッダ行のラベルを配列化 */
	this.getHeaderLabels = function() {
		return this.responsiveTable.getHeaderLabels();
	},

	/** テンプレートをコピーして、新しい行インスタンスを生成(ただし.append()はしてない) */
	this.toNewLine = function(template, entity) {
		return this.responsiveTable.toNewLine(template, entity);
	}

	/** 「該当レコードなし」の表示・非表示制御 */
	this.dispNoRecordMessage = function(results) {
		this.responsiveTable.dispNoRecordMessage();
	},

	/** テンプレートから生成した行レイアウトを、エンティティ内容で補正 */
	this.modifyTR = function($tr, rowIndex, entity, labels, ignores) {
		this.responsiveTable.modifyTR($tr, rowIndex, entity, labels, ignores);
	}
}

/**
 * 検索結果をTABLE.responsiveへ反映するクラス。
 * @param $root (必須)起点エレメント（レンダリング対象のtableタグを含む）
 * @param msgNoRecord (任意)該当レコードなしときの個別メッセージ。未指定なら標準メッセージを使用。
 */
const ResponsiveTable = function($root, msgNoRecord) {
	/** 検索結果を反映するルートエレメント(JQueryオブジェクト) */
	this.$root = $root;
	/** 該当レコードなしメッセージ */
	this.msgNoRecord = msgNoRecord;

	/**
	 * テーブルへ検索結果を反映
	 * @param results 検索結果の配列
	 * @param ignores 結果を反映しないフィールド名の配列（省略可）
	 **/
	this.fillTable = function(results, ignores) {
		const $tbody = $('table.responsive tbody', this.$root);
		const tbody = $tbody[0];
		if (tbody) {
			// 既存行の削除
			let child;
			while (child = tbody.lastChild) {
				tbody.removeChild(child);
			}
			if (results && results.length > 0) {
				// ヘッダ行のラベルを配列化
				const labels = this.getHeaderLabels();

				// テンプレート行
				const template = this.getTemplate();

				// 検索結果の反映
				const self = this;
				const df = document.createDocumentFragment();
				$.each(results, function(rowIndex, entity) {
					// テンプレートから新しい行エレメントを生成
					const $tr = self.toNewLine(template, entity);
					// 行エレメントにJSON内容を反映
					self.fillRowResult($tr, rowIndex, entity, labels, ignores);

					$tr.each(function(i, tr) {
						df.appendChild(tr);
					});
				});
				tbody.appendChild(df);
			}
		}
		// 「該当レコードなし」の表示コントロール
		this.dispNoRecordMessage();
	};

	/**
	 * テーブルへ検索結果を１行分だけ反映
	 * @param $tr 結果を反映する対象
	 * @param rowIndex 行Index（0スタート）
	 * @param entity 検索結果
	 * @param labels ヘッダ行のラベル配列(table.responsive用)
	 * @param ignores 結果を反映しないフィールド名の配列（省略可）
	 **/
	this.fillRowResult = function($tr, rowIndex, entity, labels, ignores) {
		// テンプレートから生成した行レイアウトを、エンティティ内容で補正
		this.modifyTR($tr, rowIndex, entity, labels, ignores);

		ignores = ignores || [];
		// テンプレート内に設定されているCSSクラスを元にして、CSSクラス名＝エンティティ.プロパティ名として
		// 内容を反映していく。
		$tr.find('[data-field]').each(function(i, elem) {
			// エレメントに定義された「フィールド名」で、エンティティから値を転写
			const fieldName = elem.getAttribute('data-field');
			if (fieldName && ignores.indexOf(fieldName) < 0) {
				NCI.setElementFromObj(elem, entity[fieldName]);
			}
		});
		// responsive対応として、ヘッダーの列名を 'data-label'として記録
		if (labels && labels.length) {
			let label;
			$tr.find('th, td').each(function(i, elem) {
				label = labels[i];
				if (label != null && label !== '')
					elem.setAttribute('data-label', label);
			});
		}
	},

	/** テンプレートから生成した行レイアウトを、エンティティ内容で補正 */
	this.modifyTR = function($tr, rowIndex, entity, labels, ignores) {
		return $tr;
	},

	/**
	 * テーブルへ検索結果を１行分だけ新しく追加
	 * @param $tr 結果を反映する対象
	 * @param rowIndex 行Index（0スタート）
	 * @param entity 検索結果
	 * @param labels ヘッダ行のラベル配列(table.responsive用)
	 **/
	this.addRowResult = function(entity) {
		// ヘッダ行のラベルを配列化
		const labels = this.getHeaderLabels();

		// テンプレート行
		const template = this.getTemplate();

		// 検索結果の反映
		const newRowIndex = $('table.responsive tbody tr', this.$root).length;
		const $tr = this.toNewLine(template, entity);
		this.fillRowResult($tr, newRowIndex, entity, labels);
		$('table.responsive>tbody', this.$root).append($tr);

		// 「該当レコードなし」の表示コントロール
		const results = [ entity ];
		this.dispNoRecordMessage();

		return $tr;
	},

	/** テンプレートを取得する */
	this.getTemplate = function() {
		let template;
		if (this.$root && this.$root.length)
			template = this.$root[0].querySelector('table.responsive>tfoot.template');
		else
			template = document.querySelector('table.responsive>tfoot.template');

		// TFOOTタグがテンプレートなので、その下のTRが新しい行
		// これをクローンして追加行とする
		if (!template) {
			throw new Error('テンプレートが見つかりません');
		}
		return template;
	},

	/** ヘッダ行のラベルを配列化 */
	this.getHeaderLabels = function() {
		const labels = [];
		$('table.responsive>thead th', this.$root).each(function(i, elem) {
			labels.push($.trim($(elem).text()));
		});
		return labels;
	},

	/** テンプレートをコピーして、新しい行インスタンスを生成(ただし.append()はしてない) */
	this.toNewLine = function(template, entity) {
		return $(template.cloneNode(true).children);
	}

	/** 「該当レコードなし」の表示・非表示制御 */
	this.dispNoRecordMessage = function() {
		// 検索結果０件のメッセージがなければCAPTIONとして生成
		const $table = $('table.responsive:first', this.$root)
		let $noRecord = $table.find('+label.noRecord');	// テーブルの直後にあるlabel
		if ($noRecord.length === 0) {
			$noRecord = $('<label class="noRecord" />')
				.text(this.msgNoRecord || NCI.getMessage('noRecord'))
				.insertAfter($table);
		}
		// 検索結果行があればコンテンツを表示、行が無ければレコードなしのメッセージを表示
		const $line = $table.find('>tbody>tr');
		const $tableContents = $table.find('>thead, >tbody, >tfoot, >colgroup');
		$table.toggle($line.length > 0);
		$noRecord.toggle($line.length === 0);
	},

	/** 結果行をクリア */
	this.empty = function() {
		$('table.responsive tbody', this.$root).empty();
	}
}

const FileUploader = {
	rowCount : 0,

	/**
	 * 対象エレメントをファイルアップロードのトリガーに設定。
	 * 対象エレメントが　<input type=file>でなければ、ドラッグ＆ドロップによる
	 * アップロードのトリガーとして設定。
	 * @param selector 対象エレメントを特定するセレクター（文字列）
	 * @param url アップロード先URL
	 * @param useMonitor デバッグ用のモニターを使用するならtrue。省略時はモニターを使わない
	 * @param callback 成功時のコールバック関数
	 * @param prepare アップロード時に追加パラメータを生成するためのコールバック関数（引数なし）
	 * @param アップロード時に追加するパラメータ
	 *  */
	setup : function(selector, url, useMonitor, callback, prepare) {
		if (typeof(selector) !== 'string') {
			alert('FileUploader.setup()の第一引数は String でなければなりません');
			return false;
		}
		// セットアップパラメータ
		const setupParams = {
			"url" : url, "useMonitor" : useMonitor, "callback" : callback, "prepare" : prepare
		};

		// 無効なドラッグ／ドラッグ中／ドロップのイベントをキャンセルするイベントハンドラー
		const cancelDragAndDrop = function(e) {
			e.stopPropagation();
			e.preventDefault();
			const $selector = $(selector);
			$selector.css('border-style', $selector.data('old-border-style'));
		};

		$(document)
			// ファイルアップロードコントロールでファイルが選択された
			.off('change', selector, FileUploader.onChangeFile)
			.on('change', selector, setupParams, FileUploader.onChangeFile)
			// ドラッグ＆ドロップ領域にカーソルが侵入
			.off('dragenter', selector, FileUploader.onDragEnter)
			.on('dragenter', selector, FileUploader.onDragEnter)
			// ドラッグ＆ドロップ領域をカーソルが移動中
			.off('dragover', selector, FileUploader.onDragOver)
			.on('dragover', selector, FileUploader.onDragOver)
			// ドラッグ＆ドロップ領域へドロップ
			.off('drop', selector, FileUploader.onDrop)
			.on('drop', selector, setupParams, FileUploader.onDrop);

		// ファイルがdivの外でドロップされた場合、ブラウザで開いてしまう。
		// これを避けるため、documentの「ドロップ」イベントを防ぐ
		$(document)
			.off('dragenter dragover drop', cancelDragAndDrop)
			.on('dragenter dragover drop', cancelDragAndDrop);
	},

	onChangeFile : function(e) {
		const $file = $(e.target);
		if ($file.is('input[type=file]:enabled')) {
			const files = e.target.files;
			if (files.length) {
				const url = e.data.url;
				const useMonitor = e.data.useMonitor;
				const callback = e.data.callback;
				// 追加パラメータは前準備コールバック関数に生成してもらう
				const exParams = (e.data.prepare) ? (e.data.prepare() || {}) : {};

				FileUploader._handleFileUpload(url, files, $file, useMonitor, exParams).done(function(res) {
					e.target.value = '';	// これを行うことで同一ファイル名を何度でもアップロードできる。これがないとchangeイベントが発生しない
					if (callback) {
						callback(res, exParams);
					}
				});
			}
		}
	},

	onDragEnter : function(e) {
		e.stopPropagation();
		e.preventDefault();
		const $elem = $(e.target);
		if (!$elem.is('input[type=file]')) {
			$elem
				.data('old-border-style', $elem.css('border-style'))	// 以前の枠を記録
				.css('border-style', 'solid');
		}
	},

	onDragOver : function(e) {
		e.stopPropagation();
		e.preventDefault();
	},

	onDrop : function(e) {
		e.preventDefault();

		const $elem = $(e.target);
		if (!$elem.is('input[type=file]')) {
			$elem.css('border-style', $elem.data('old-border-style'));

			const files = e.originalEvent.dataTransfer.files;
			if (files.length) {
				const url = e.data.url;
				const useMonitor = e.data.useMonitor;
				const callback = e.data.callback;

				// 追加パラメータは前準備コールバック関数に生成してもらう
				const exParams = (e.data.prepare) ? (e.data.prepare() || {}) : {};

				FileUploader._handleFileUpload(url, files, $elem, useMonitor, exParams).done(function(res) {
					if (callback) {
						callback(res, exParams);
					}
				});
			}
		}
	},

	/** ファイル送信処理 */
	_handleFileUpload : function (url, files, elem, useMonitor, exParams) {
		NCI.clearMessage();

		// ファイルデータをFormDataでラッピング
		const formData = new FormData();
		let monitor = null;
		for (let i = 0; i < files.length; i++) {
			// ファイルのエントリ
			formData.append('file', files[i]);
			// モニター
			if (useMonitor) {
				monitor = new FileUploader._createMonitorbar(elem); // Using this we can set progress.
				monitor.setFileNameSize(files[i].name, files[i].size);
			}
		}
		// 追加パラメータがあればformDataへ追加
		if (exParams) {
			$.each(exParams, function(prop, val) {
				formData.append(prop, val);
			});
		}
		// 送信処理
		return FileUploader._sendFileToServer(url, formData, monitor);
	},

	/** ファイル送信処理 */
	_sendFileToServer : function(url, formData, monitor) {
		// ブロックUIをアクティブ化
		NCI.openBlockUI();

		// JQuery.ajax()はバイナリデータを正しく扱えないので、XMLHttpRequestでリクエストを投げる
		const uri = '../../endpoint' + url;
		const method = 'POST';
		const deferred = $.Deferred();
		const xhr = new XMLHttpRequest();
		xhr.open(method, uri, true);
		xhr.timeout = 0;	// タイムアウトさせたいならサーバーサイドで行うこと。
		xhr.responseType = 'arraybuffer';
		// CSRF対策 =================================================================================
		// iframe, imageなどからのリクエストではHTTPリクエストに独自のヘッダを付与することができない。
		// 独自のヘッダをつけるにはXMLHttpRequestを使うしかない。
		// そしてXMLHttpRequestを使う場合にはSame Origin Policyが適用されるため
		// 攻撃者のドメインからHTTPリクエストがくることはない。
		// つまり XMLHttpRequest経由で特定のヘッダ(ここでは"X-Requested-By")が付与されていれば、
		// サーバ側で正しいリクエストと判断できる。
		xhr.setRequestHeader("X-Requested-By", "NCI WF Ver6")	// @see CsrfProtectionFilter.java
//		xhr.setRequestHeader('Content-type', 'multipart/form-data; boundary=---------------------------105742468821884\r\n');
		xhr.addEventListener('load', function(){
			const type = xhr.getResponseHeader('Content-Type');
			if (xhr.status === 200) {
				if (monitor) monitor.setProgress(100);

				if (type === 'application/json') {
					// 帳票のダウンロード等ではエラー時のレスポンスもバイナリなので、バイナリをUTF8文字列化したうえでJSONパースする
					const utf8str = NCI.arraybufferToStr(xhr.response)
					deferred.resolve(JSON.parse(utf8str));
				}
				else {
					// Ajaxでバイナリデータを受け取り、それをBlobとして書き出す
			    	// ファイル名を求める
					let filename = "";
					const disposition = xhr.getResponseHeader('Content-Disposition');
					const attachment = disposition.indexOf('attachment') !== -1;
					if (disposition) {
						const matches = /filename[^;=\n]*=((['"]).*?\2|[^;\n]*)/.exec(disposition);
						if (matches != null && matches[1])
							filename = decodeURIComponent(matches[1].replace(/['"]/g, ''));
					}
					// Blobを生成
					const blob = new Blob([ this.response ], { type : type });
					if (typeof window.navigator.msSaveOrOpenBlob !== 'undefined') {
						// IE専用
						window.navigator.msSaveOrOpenBlob(blob, filename);
					} else {
						const URL = window.URL || window.webkitURL;
						const downloadUrl = URL.createObjectURL(blob);
						if (filename && attachment) {
							const a = document.createElement("a");
							if (typeof a.download === 'undefined') {
								// Safariはダメっぽい？
								window.location = downloadUrl;
							} else {
								a.href = downloadUrl;
								a.download = filename;
								document.body.appendChild(a);
								a.click();
							}
						} else {
							window.location = downloadUrl;
						}
						// 使わなくなったURLオブジェクトを解放
						setTimeout(function() { URL.revokeObjectURL(downloadUrl); }, 100);
					}
					deferred.resolve(xhr.response);
				}
			} else if (xhr.status === 204) {	// サーバサイドがコンテンツを返さないのは、単純に受け取りしかしないからだと思われる
				deferred.resolve(null);
			} else {
				NCI.defaultErrorCallback(xhr, xhr.statusText, '', { 'url' : url, 'params' : formData, 'method':method });
				deferred.reject("HTTP error: " + xhr.status);
			}
		});
		xhr.addEventListener('error', function() {
			// 例えばXHRのCross-Origin制約に引っかかった場合やネットワーク接続ができない場合など、サーバーとの接続ができなかった場合
			const msg = 'connect error';
			NCI.defaultErrorCallback(xhr, msg, '', { 'url' : url, 'params' : formData, 'method':method });
			deferred.reject(msg);
		});
		xhr.addEventListener('abort', function() {
			// 他から中断された
			const msg = 'abort';
			NCI.defaultErrorCallback(xhr, msg, '', { 'url' : url, 'params' : formData, 'method':method });
			deferred.reject(msg);
		});
		xhr.addEventListener('timeout', function(){
			// 時間切れ
			const msg = 'timeout';
			NCI.defaultErrorCallback(xhr, msg, '', { 'url' : url, 'params' : formData, 'method':method });
			deferred.reject(msg);
		});
		xhr.addEventListener('loadend', function() {
			// ブロックUIを除去
			NCI.closeBlockUI();
		});
		xhr.send(formData);
		return deferred.promise();
	},

	/** デバッグ用モニターを生成 */
	_createMonitorbar : function(obj) {
		FileUploader.rowCount++;
		const row = (FileUploader.rowCount % 2 == 0) ? "even" : "odd";
		this.monitorbar = $("<div class='monitorbar " + row + "'></div>");
		this.filename = $("<div class='filename'></div>").appendTo(this.monitorbar);
		this.size = $("<div class='filesize'></div>").appendTo(this.monitorbar);
		this.progressBar = $("<div class='progressBar'><div></div></div>").appendTo(this.monitorbar);
		this.abort = $("<div class='abort'>Abort</div>").appendTo(this.monitorbar);
		obj.after(this.monitorbar);

		this.setFileNameSize = function(name, size) {
			let sizeStr = "";
			const sizeKB = size / 1024;
			if (parseInt(sizeKB) > 1024) {
				const sizeMB = sizeKB / 1024;
				sizeStr = sizeMB.toFixed(2) + " MB";
			} else {
				sizeStr = sizeKB.toFixed(2) + " KB";
			}

			this.filename.html(name);
			this.size.html(sizeStr);
		}
		this.setProgress = function(progress) {
			const progressBarWidth = progress * this.progressBar.width() / 100;
			this.progressBar.find('div').animate({
				width : progressBarWidth
			}, 10).html(progress + "% ");
			if (parseInt(progress) >= 100) {
				this.abort.hide();
			}
		}
		this.setAbort = function(jqxhr) {
			const sb = this.monitorbar;
			this.abort.click(function() {
				jqxhr.abort();
				sb.hide();
			});
		}
	}
};

const Popup = {
	_oBsModal : { oWinList: [], x: 0, y: 0, oDragItem: null },

	/**
	 * ポップアップ画面をモーダルで開く
	 * @param url
	 * 		ポップアップ画面のURL
	 * @param callback
	 * 		ポップアップ画面終了時のコールバック関数へのポインター。
	 * 		関数仕様は「function(result, trigger)」。
	 * 		第一引数は Popup.close(result)で渡された値。
	 * 		第二引数は Popup.open(url, callback, params, trigger)で渡された triggerオブジェクト）。
	 * @param params
	 * 		ポップアップ画面で受け取ることのできるパラメータObject。
	 * @param trigger
	 * 		コールバック関数に渡せるObject。
	 * 		通常はボタンなどポップアップを開くトリガーとなったObjectを渡し、コールバック関数内で処理対象を特定するために使用する。
	 */
	open : function(url, callback, params, trigger) {
		if (typeof(params) === "undefined")
			params = "";

		if (callback && typeof(callback) === 'string') {
			if (window.console) {
				window.console.error('Popup.open()で引数「callback」が文字列として渡されていますが、Functionとして渡してください。いまの実装ではポップアップを閉じたときに正しい動作をしません');
				window.console.trace();
			}
			alert('Popup.open()で引数「callback」が文字列として渡されていますが、Functionとして渡してください。いまの実装ではポップアップを閉じたときに正しい動作をしません');
			return false;
		}

		Popup._oBsModal.oWinList.push(window);
		const iIndex = Popup._oBsModal.oWinList.length;

		// ポップアップ先でパラメータが受け取れるよう、フラッシュスコープに保存し、URLにそのキーを追記
		const id = "popupContainer" + iIndex;
		NCI.flushScope(id, params);
		url += (url.indexOf("?") > 0 ? "&" : "?") + FLUSH_SCOPE_KEY + '=' + id;
		url += '&_popup=1';

		const iframeId = 'popupFrame' + iIndex;
		$("body").append('<div id="' + id + '" class="modal fade nci-popup" role="dialog">' +
			'<div id="popupDialog' + iIndex + '" class="modal-dialog">' +
				'<div class="modal-content" id="modal-content' + iIndex + '">' +
					'<div class="modal-body">' +
					'<iframe id="' + iframeId + '" class="nci-popup" onload="Popup._setPopupTitleBar(this,' + iIndex + ')" src="' + url + '" ' +
							'style="display:none; margin: 15px 8px; position: relative; z-index: 202; width:100%; height:100%;background-color:transparent;" scrolling="auto" frameborder="0" allowtransparency="true" width="100%" height="100%"></iframe>' +
					'</div>' +
				 '</div>' +
			'</div>' +
		'</div>');

		const iHeight = $(window).height() * 0.85;
		const $iframe = $("#" + iframeId).css({ height: iHeight });

		$("#" + id)
			// ポップアップ起動
			.modal()
			// ポップアップの表示完了時
			.on('shown.bs.modal', function (event) {
				const zIndex = 1040 + (10 * $('.modal:visible').length);
				$(this).css('z-index', zIndex);

				// エレメントにフォーカスを当てる。
				// これをやらなとポップアップのトリガーになったエレメントにフォーカスが残ってしまい、
				// その状態でENTERキーを押下すると何度もポップアップが起動してしまう
				$iframe.contents()
						.find('body').find('input,select,textarea,button,a')
						.filter(':not(input.ymdPicker)')	// 初期表示でカレンダーにフォーカスを当てるとUIが起動してすごく邪魔
						.filter(':visible:enabled')
						.first()
						.focus();

				setTimeout(function () {
						$('.modal-backdrop').not('.modal-stack').css('z-index', zIndex - 1).addClass('modal-stack');
				}, 0);
			})
			// ポップアップが非表示になった時
			.on('hidden.bs.modal', function () {
				// 不要になったポップアップを削除
				if (Popup._oBsModal.oWinList.length > 0)
					Popup._oBsModal.oWinList.length--;
				const $self = $(this);
				// 最初にresultを取得しておいてから自身をremoveする
				const result = $self.data('result');
				$self.remove();
				// 呼び元のコールバックを呼び出す
				if (typeof(callback) === 'function') {
					callback(result, trigger);
				}
			});

		Popup._diaMakeDragable($("#modal-content" + iIndex));
	},

	/**
	 * ポップアップを閉じる。
	 */
	close : function(result) {
		parent.$('div.nci-popup:last').addClass('hide').data('result', result).modal("hide");
		return this;
	},

	/** 今現在がポップアップとして起動されているか */
	isPopup : function() {
		try {
			return parent.$('div.nci-popup:last:not(.hide)').length > 0;
		} catch (e) {
			return false;
		}
	},

	//private functions =============================
	_setPopupTitleBar : function(oIframe, iIndex) {
		$("#idDiaLoading" + iIndex).hide();
		$("#popupFrame" + iIndex).show().modal("handleUpdate");
	},

	_diaMakeDragable : function(oBox) {
		oBox.addClass("Dragable");

		if (navigator.platform == "iPad" || navigator.platform == "iPhone" || /Android/i.test(navigator.userAgent)) {
			oBox[0].ontouchstart = function (e) { Popup._diaTouchStart(oBox, e) };
			oBox[0].ontouchmove = function (e) { Popup._diaTouchMove(oBox, e) };
		} else {
			oBox.on("mousemove", function (e) { Popup._diaDragMove(oBox, e) });
			oBox.on("mouseup", function (e) { Popup._diaDragStop(oBox, e) });
			oBox.on("mousedown", function (e) { Popup._diaDragStart(oBox, e); return false });
		}
	},

	_diaTouchStart : function(o, e) {
		const oPos = $(e.target).position();
		Popup._oBsModal.x = e.targetTouches[0].pageX - oPos.left;
		Popup._oBsModal.y = e.targetTouches[0].pageY - oPos.top;
	},

	_diaDragStart : function(o, e) {
		if (!e) var e = window.event;

		if (e.target && e.target.nodeName) {
			let sNode = e.target.nodeName;
			if (sNode == "IMG" || sNode == "BUTTON")
					return;
		}

		Popup._oBsModal.oDragItem = o;

		if (e.offsetX && e.target) {
			Popup._oBsModal.x = e.offsetX;
			Popup._oBsModal.y = e.offsetY;
		} else {
			const oPos = o.position();
			Popup._oBsModal.x = e.clientX - oPos.left;
			Popup._oBsModal.y = e.clientY - oPos.top;
		}

		let oOffset = Popup._getBrowserOffset()
		Popup._oBsModal.x += oOffset.x;
		Popup._oBsModal.y += oOffset.y;

		if (Popup._oBsModal.oDragItem.parent()[0] != document.body) {
			const o = $("#modal-content" + Popup._oBsModal.oWinList.length);
			o.css({ "width": o.width(), "height": o.height() });
		}

		if (o.setCapture) {
				o.setCapture();
		} else {
			window.addEventListener("mousemove", Popup._diaDragMove2, true);
			window.addEventListener("mouseup", Popup._diaDragStop2, true);
		}
	},

	_diaDragMove2 : function(e) {
		Popup._diaDragMove(Popup._oBsModal.oDragItem, e);
	},

	_diaDragStop2 : function(e) {
		Popup._diaDragStop(Popup._oBsModal.oDragItem, e);
	},

	_diaDragMove : function(o, e) {
		if (Popup._oBsModal.oDragItem == null) return;

		if (!e) var e = window.event;
		const x = e.clientX + document.body.scrollLeft - document.body.clientLeft - Popup._oBsModal.x;
		const y = e.clientY + document.body.scrollTop - document.body.clientTop - Popup._oBsModal.y;
		const yOffset = 30;

		$("#popupDialog" + Popup._oBsModal.oWinList.length).css("width", "inherit");

		Popup._oBsModal.oDragItem.css({
			position: "absolute",
			left: x,
			top: y - yOffset
		});
	},

	_diaTouchMove : function(o, e) {
		e.preventDefault();

		const oTitleBar = $("#popupFrame" + Popup._oBsModal.oWinList.length);
		oTitleBar.css("width", oTitleBar.width());

		o.css({
			position: "absolute",
			left: e.targetTouches[0].pageX - Popup._oBsModal.x,
			top:	e.targetTouches[0].pageY - Popup._oBsModal.y
		});
	},

	_diaDragStop : function(o, e) {
		if (Popup._oBsModal.oDragItem == null) return;

		if (o.releaseCapture) {
			o.releaseCapture();
		} else if (Popup._oBsModal.oDragItem) {
			window.removeEventListener("mousemove", Popup._diaDragMove2, true);
			window.removeEventListener("mouseup", Popup._diaDragStop2, true);
		}

		Popup._oBsModal.oDragItem = null;
	},

	_getBrowserOffset : function() {
		if (window.pageXOffset != null) {
				return { x: window.pageXOffset, y: window.pageYOffset };
		}

		const doc = window.document;
		if (document.compatMode === "CSS1Compat") {
				return {
						x: doc.documentElement.scrollLeft,
						y: doc.documentElement.scrollTop
				};
		}

		return {
				x: doc.body.scrollLeft,
				y: doc.body.scrollTop
		};
	}
}
