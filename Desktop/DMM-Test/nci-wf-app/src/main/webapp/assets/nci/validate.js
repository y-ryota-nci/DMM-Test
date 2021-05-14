$(function() {
	//--------------------------------------------------------------
	// カスタムイベント「validate」の定義
	// @param ev イベント
	// @param isRequired 必須チェックするか。通常のフォーカス移動等のイベントではfalse、Validator.validate()からキックされるとtrueの場合あり
	//--------------------------------------------------------------
	$(document).on('validate','input,textarea,select' , function(ev, isRequired) {

		let $target;
		if (this.type === 'radio') {
			$target = $('input[name="' + this.name + '"]').eq(0);	// ラジオはエラー表示の都合上、必ず先頭要素を使う
		} else {
			$target = $(this);
		}

		// エラーバルーンとメッセージ削除
		Validator.hideBalloon($target);

		// ラベルまたは無効化されていたらバリデーション不要
		if ($target.is(Validator.IGNORE_SELECTOR)) {
			return;
		}

		// JSON書式の[data-validate]の値を、Objectとして取得
		const option = $target.data('validate');
		if (typeof option === 'string') {
			// Objectではなく文字列になっている場合、正しくないJSONであるからと思われる。
			// 間違えやすいのは下記（RFC 4627で定められたJSONの仕様）。
			// 　①文字列をダブルクォートで囲む（シングルクォートは不可）
			// 　②プロパティもダブルクォートで囲む（{"key": 123} は正しいが {'key':123}は間違っている）
			// 　③配列やプロパティの要素の末尾にカンマをつけない（ [1,2,3,]は不可）
			throw new Error('エレメント"' + (this.id || this.name) + '"の属性[data-validate]は、正しくないJSON書式になっています。プロパティ名のシングルクォートとか、要素の末尾のカンマとか。');
		}

		// 必須チェックする項目であり、かつ必須チェック判定が要求されていたらチェックを行う。
		// (例えば仮保存のときは必須チェック不要なので、切り分けが出来るようにしたい)
		if(isRequired && $target.hasClass('required')) {
			Validator.execute($target, Validator._required, option);
		}

		if (option) {
			if (option.pattern){
				// 型チェック
				switch(option.pattern){
				//日付型
				case 'date' :
					Validator.execute($target, Validator._date, option);
					break;
				//年月
				case 'ym' :
					Validator.execute($target, Validator._ym, option);
					break;
				//時刻
				case 'time' :
					Validator.execute($target, Validator._time, option);
					break;
				//郵便番号
				case 'postCode' :
					Validator.execute($target, Validator._postCode, option);
					break;
				//IPアドレス
				case 'ipAddr' :
					Validator.execute($target, Validator._ipAddr, option);
					break;
				//電話番号
				case 'tel' :
					Validator.execute($target, Validator._tel, option);
					break;
				//メールアドレス
				case 'mail' :
					Validator.execute($target, Validator._mailFormat, option);
					break;
				//数値型
				case 'numeric' :
					Validator.execute($target, Validator._numeric, option);
					break;
				//整数型
				case 'integer' :
					Validator.execute($target, Validator._integer, option);
					break;
				//数字（0-9のみ）
				case 'numberOnly' :
					Validator.execute($target, Validator._strNumberOnly, option);
					break;
				//アルファベット
				case 'alpha' :
					Validator.execute($target, Validator._alphabetOnly, option);
					break;
				//アルファベット＋数字
				case 'alphaNumber' :
					Validator.execute($target, Validator._alphabetNumber, option);
					break;
				//アルファベット＋記号
				case 'alphaSymbol' :
					Validator.execute($target, Validator._alphabetSymbol, option);
					break;
				//アルファベット＋数字＋記号
				case 'alphaSymbolNumber' :
					Validator.execute($target, Validator._alphabetSymbolNumber, option);
					break;
					//アルファベット＋数字＋記号＋改行
				case 'alphaSymbolNumberLf' :
					Validator.execute($target, Validator._alphabetSymbolNumberLf, option);
					break;
				//アルファベット＋数字＋アンダースコア
				case 'alphaNumberUnderscore' :
					Validator.execute($target, Validator._alphaNumUnderscore, option);
					break;
				//全角のみ
				case 'fullWidthOnly' :
					Validator.execute($target, Validator._fullWidthOnly, option);
					break;
				//半角のみ
				case 'halfWidthOnly' :
					Validator.execute($target, Validator._halfWidthOnly, option);
					break;
				//半角カナのみ
				case 'halfKanaOnly' :
					Validator.execute($target, Validator._halfKanaOnly, option);
					break;
				//全角カナのみ
				case 'fullKanaOnly' :
					Validator.execute($target, Validator._fullKanaOnly, option);
					break;
				//正規表現（ /^[A-Z]*$/ ）
				case 'regExp' :
					Validator.execute($target, Validator._regExp, option);
					break;
				// ファイル名
				case 'fileName' :
					Validator.execute($target, Validator._fileName, option);
					break;
				}
			}

			// 最大バイト数・桁数チェック
			if (option.maxByteSjis)
				Validator.execute($target, Validator._sjisByteCount, option);
			else if (option.maxByteUtf8)
				Validator.execute($target, Validator._utf8ByteCount, option);
			else {
				// 最大桁数は無指定でもデフォルト最大桁数が適用されるので、無条件に実施。抑制したければoption.maxLengthに極大値を設定して事実上無効化すること。
				Validator.execute($target, Validator._maxLength, option);
			}
			// 最小桁数チェック
			if (option.minLength || option.minlength) {
				Validator.execute($target, Validator._minLength, option);
			}
		}
	})

	/**
	 * blur時のバリデート
	 */
	.on('blur', 'input,textarea,select', function(e){
		$(this).trigger('validate', [ false ]);
	})

	/**
	 * blur時の入力補助
	 */
	.on('blur','input, textarea', function() {
		NCI.fillFormat( $(this) );
	})

	/**
	 * 入力補助イベントの定義(フォーカス想定)
	 */
	.on('focus','input' , function() {
		NCI.removeFormat( $(this) );
	});
});

/**
 * バリデーションライブラリ
 */
const Validator = {

	/** yyyyMMdd形式での有効な正規表現パターン */
	YYYYMMDD_PATTERNS : [
		 /^(\d{4})\/(\d{2})\/(\d{2})$/
		,/^(\d{2})\/(\d{2})\/(\d{2})$/
		,/^(\d{4})(\d{2})(\d{2})$/
		,/^(\d{2})(\d{2})(\d{2})$/
	]

	/** yyyyMM形式での有効な正規表現パターン */
	, YYYYMM_PATTERNS : [
		 /^(\d{4})\/(\d{2})$/
		,/^(\d{2})\/(\d{2})$/
		,/^(\d{4})(\d{2})$/
		,/^(\d{2})(\d{2})$/
	]

	/** バリデーションチェック対象外を示すセレクタ文字列 */
	, IGNORE_SELECTOR : '[type=submit], [type=button], [disabled], [type=hidden]'

	/** デフォルトのMAX桁数 */
	, DEFAULT_MAX_LENGTH : 1000

	/**
	 * バリデーション実行
	 * @param $targets 対象Jqueryオブジェクト
	 * @param isRequired 必須チェックを行うならtrue（省略時はtrue扱い）
	 */
	, validate : function($targets, isRequired) {
		const required = (isRequired || isRequired == undefined);
		// エラークリア
		Validator.hideBalloon($targets);
		// ボタン類、無効化、ラベル化された項目は除外
		$targets.not(Validator.IGNORE_SELECTOR).each(function() {
			const $target = $(this);
			if (required && $target.hasClass('required')) {
				// 必須
				$target.trigger('validate', [ true ]);
			}
			else if (NCI.getPureValue(Validator.getValue($target), $target.data('validate')) != '') {
				// 非必須なら値が入力されていれば実施
				$target.trigger('validate', [ false ]);
			}
		});
		// エラーなし？
		const $errors = $targets.filter('.has-error');
		if ($errors.length === 0) {
			return true;
		}
		// エラーがあっても、エレメント自体が見えない状態であれば扱いが変わる
		const $hiddens = $errors.filter(':hidden');
		if ($hiddens.length === 0){
			// エラー対象がすべて表示されているなら、単純に最初のエラーを表示する
			$errors.eq(0).focus();
			return false;
		}
		else if (($errors.length - $hiddens.length) > 0) {
			// バルーンの表示対象が見えていれば、その最初のエラーを表示
			$errors.eq(0).focus();
			return false;
		}
		else {
			// 見えないエラーには、大きく分けて下記２パターンがあるので、切り分ける
			// ・非表示項目のためエラーチェックの対象外にしたいもの
			// ・サブタイトルやブロックが折り畳まれていて一時的に見えないが、エラーチェック対象にしたいもの
			let errorUnderBlock = false;
			$hiddens.each(function(i, elem) {
				// 「エラー項目を包含しているサブタイトルやブロック」が折り畳まれているなら警告（＝エラー扱い）
				if (typeof(window.loadfunc) === 'undefined') {
					// 旧デザイン
					const $block = $(elem).closest('.ac-contents');
					const $checkbox = $block.prevUntil('input[type=checkbox].ac-trigger');
					if ($block.length && $checkbox.length && !$checkbox.checked) {
						NCI.alert(NCI.getMessage('MSG0000'), function() {
							NCI.scrollTo($block.closest(':visible'));
						});
						errorUnderBlock = true;
						return false;	// break扱い
					}
				} else {
					// 新デザイン
					const $block = $(elem).closest('.collapse');
					if ($block.length && !$block.hasClass('in')) {
						NCI.alert(NCI.getMessage('MSG0000'), function() {
							NCI.scrollTo($block.closest(':visible'));
						});
						errorUnderBlock = true;
						return false;	// break扱い
					}
				}
			});
			return !errorUnderBlock;	// ブロック/サブタイトルが畳まれていることで見えなくなっているとfalse
		}
	}

	/**
	 * エラーバルーンを表示中か
	 * @param $targets 対象Jqueryオブジェクト
	 */
	, hasError : function($targets) {
		const $errors = $('.has-error');
		return ($errors.length > 0);
	}

	/**
	 * バリデーションを実行し、エラーの場合、エラーメッセージを格納
	 * @param $target 対象jQueryオブジェクト
	 * @param func 実行するバリデーション関数(引数：$target, objValue)
	 * @param option オプション
	 */
	, execute : function($target, func, option) {
		// 値（書式設定を除去した値）
		const val = NCI.getPureValue(Validator.getValue($target), option);
		// バリデーション関数を実行
		const msg = func(val, option);
		if (msg) {
			// メッセージセット
			Validator.showBalloon($target, msg);

			return msg;
		}
		return null;
	}

	/**
	 * 対象jQueryオブジェクトに、エラーメッセージをセット
	 * errorMsgが無い場合、初期化
	 * @param $targets : 対象jQueryオブジェクト
	 * @param msg : エラーメッセージ
	 */
	, showBalloon : function($targets, msg) {
		$targets.addClass('has-error').each(function() {
			const $target = $(this);
			if ($target.is(':visible')) {
				let $div = $target.data('div-balloon');

				// バルーンの実体がなければ生成
				if (!$div || $div.length === 0) {
					$div = $(document.createElement('div'))
							.addClass('balloon')
							.css('position', 'absolute')
							.appendTo('body');
					$target.data('div-balloon', $div);	// あとでバルーンを参照するため
				}
				const zIndex = 1040 + (10 * $('div.modal:visible').length);	// 現存する.modal() の z-indexよりちょっと上
				$div.css('z-index', zIndex);

				// メッセージを追加し、表示位置を調整。
				// .position()は jqueryの標準メソッドをjquery-uiで上書きしている前提なので、
				// jquery-ui.jsがないと正しく座標計算が行われない。
				// また、.position()はshow()が完了した後にやらないと座標がずれる。
				// （アニメーションさせているとshow()の完了までに遅延時間がある）。
				let html = $div.html() || "";
				html += NCI.escapeHtml(msg) + "<br />";
				$div.html(html).removeClass('hide').position({
					  my : "left top"
					, at : "left+5 bottom+10"
					, of : $target
					, collision : "fit none"	// 垂直方向にはcollision無効
				});
			}
		});
	}

	/**
	 * 対象jQueryオブジェクトからエラーメッセージを除去
	 * @param $targets : 対象jQueryオブジェクト。未指定ならすべてからエラーメッセージを除去
	 */
	, hideBalloon : function($targets) {
		if ($targets) {
			$targets.each(function() {
				// .data("div-balloon")にバルーン表示用のDIVタグが保存されているので、
				// これに対して非表示処理を行う。
				let $target = $(this);
				let $div = $target
					.removeClass("has-error")
					.data("div-balloon");
				if ($div) {	// 高速化のためリフローさせない
					$div.addClass('hide').empty();
				}
			});
		}
		else {
			// 要素が無ければ全部クリア
			Validator.hideBalloon($(".has-error"));
		}
	}

	/**
	 * 対象の値を取得（単一エレメントが前提）
	 */
	, getValue : function($target) {
		/**********************************************
		 * 注意！
		 * このメソッドはパフォーマンスに多大な
		 * 影響を及ぼすので扱いに注意すること！
		 *********************************************/
		if ($target.length == 0) {
			return '';
		}
		// 速度維持のため、jQueryを使わずPure JavaScriptで実装
		const element = $target[0];
		const type = element.type;
		let value = '';
		if (type === 'radio') {
			// ラジオボタンは選択されているものの値、未選択なら空文字
			const radios = document.getElementsByName(element.name);
			const len = radios.length;
			for (let i = 0; i < len; i++) {
				const radio = radios[i];
				if (radio.checked) {
					value = radio.value;
					break;
				}
			}
		}
		else if (type === 'checkbox') {
			// チェックボックスは選択されていればその値、未選択なら空文字
			if (element.checked) {
				value = element.value;
			}
		}
		else {
			value = element.value;
		}
		return value;	// 該当要素なしなら空文字列
	}

	/** 必須チェック */
	, _required : function(value, option) {
		return (value.trim().length > 0 ? '' : NCI.getMessage('MSG0074'));
	}

	/** UTF8換算でのMaxバイトチェック */
	, _utf8ByteCount : function(value, option) {
		if (value > '') {
			const count = Validator._getUtf8ByteCount(value);
			if (!isNaN(option.maxByteUtf8) && count > option.maxByteUtf8) {
				return NCI.getMessage('MSG0093', option.maxByteUtf8);
			}
		}
	}

	/** UTF8換算でのバイト数をカウント */
	, _getUtf8ByteCount : function(value) {
	    return(encodeURIComponent(value).replace(/%../g,"x").length);
	}

	/** Shift-JIS換算でのMaxバイトチェック */
	, _sjisByteCount : function(value, option) {
		if (value > '') {
			const count = Validator._getSjisByteCount(value);
			if (!isNaN(option.maxByteSjis) && count > option.maxByteSjis) {
				return NCI.getMessage('MSG0093', option.maxByteSjis);
			}
		}
	}

	/** Shift-JIS換算でのバイト数をカウント */
	, _getSjisByteCount : function(value) {
		let count = 0;
		for (let i = 0; i < value.length; i++) {
			let s = value.charAt(i);
			count += Validator._isSjis1byte(s) ? 1 : 2;
		}
		return count;
	}

	/** 先頭文字がShift-JIS換算で1バイトであるか。1バイトならtrue */
	, _isSjis1byte : function(s) {
		if (s > '') {
			// Shift_JIS: 0x0 ～ 0x80, 0xa0 , 0xa1 ～ 0xdf , 0xfd ～ 0xff
			// Unicode : 0x0 ～ 0x80, 0xf8f0, 0xff61 ～ 0xff9f, 0xf8f1 ～ 0xf8f3
			const c = s.charCodeAt(0);
			return ( (c >= 0x0 && c < 0x81) || (c == 0xf8f0) || (c >= 0xff61 && c < 0xffa0) || (c >= 0xf8f1 && c < 0xf8f4));
		}
		return true;	// nullとか長さ０文字列はtrue扱いする。null判定ぐらいは呼び元でやってくれ。
	}

	/** 文字数チェック(最大桁数) */
	, _maxLength : function(value, option) {
		if (value > '' && option) {	// 入力されていたらチェック、入力有無自体は requiredでやること
			const max = option.maxLength || option.maxlength	// optionでの指定が最優先(大文字小文字を正規化)
					|| this.maxlength || this.maxLength			// エレメントのmaxlengthが次点(大文字小文字を正規化)
					|| Validator.DEFAULT_MAX_LENGTH				// 何も指定がなければシステム標準の最大桁数（無指定でも必ず最大桁数チェックを実施させるため）
			if (max > 0 && value.length > max) {
				return NCI.getMessage('MSG0092',max);
			}
		}
	}

	/** 文字数チェック(最小桁数) */
	, _minLength : function(value, option) {
		if (value > '' && option) {	// 入力されていたらチェック、入力有無自体は requiredでやること
			const min = option.minLength || option.minlength;		// "minLength"と"minlength"の両方に対応
			if (min > 0 && value.length < min) {
				return NCI.getMessage('MSG0097',min);
			}
		}
	}

	/** 日付形式チェック */
	, _date:	function(value, option) {
		if (value > '') {
			// 型チェック
			const vDt = Validator.toDate(value);
			if (!vDt) {
				return NCI.getMessage('MSG0075');
			}
			// 日付範囲チェック対応 入力される日付を
			// option.minで指定された日付または1900/01/01から
			// option.maxで指定された日付2099/12/31に制限する
			const outRange = Validator._rangeDate(vDt , option);
			// outRangeにメッセージが入っていたら＝エラーだったらリターン
			if(outRange > ''){
				return outRange;
			}
			// objectのvalueは、比較対象のid
			if(option.from){
				// objectのvalueは、比較対象のid
				const from = NCI.getPureValue(Validator.getValue($(option.from)));
				return Validator._dateTo(value, from);

			} else if (option.to){
				// objectのvalueは、比較対象のid
				const to = NCI.getPureValue(Validator.getValue($(option.to)));
				return Validator._dateFrom(value, to);

			}


		}
	}

	/** 年月形式チェック */
	, _ym:	function(value, option) {
		if (value > '') {
			// 型チェック
			const vDt = Validator._toYm(value);
			if (!vDt) {
				return NCI.getMessage('MSG0100');
			}
			// From To チェック
			if(option.from){
				// objectのvalueは、比較対象のid
				const from = NCI.getPureValue(Validator.getValue($(option.from)));
				return Validator._dateTo(value, from);

			} else if (option.to){
				// objectのvalueは、比較対象のid
				const to = NCI.getPureValue(Validator.getValue($(option.to)));
				return Validator._dateFrom(value, to);

			}
		}
	}

	/** 時刻形式チェック */
	, _time : function(value, option) {
		if (value > '') {
			const time = Validator._toTime(value);
			if (!time) {
				return NCI.getMessage('MSG0131');
			}
			// From To チェック
			if (option.from){
				// objectのvalueは、比較対象のid
				const from = NCI.getPureValue(Validator.getValue($(option.from)));
				return Validator._timeTo(value, from);

			} else if (option.to){
				// objectのvalueは、比較対象のid
				const to = NCI.getPureValue(Validator.getValue($(option.to)));
				return Validator._timeFrom(value, to);
			}
		}
	}

	/**
	 * (String -> Date)日付変換(有効な書式は yyyy/MM, yy/MM, yyyyMM, yyMM)
	 * @return 日付、変換できない時は ''
	 */
	, _toYm : function(value) {
		if (value > '') {
			for (let i = 0; i < Validator.YYYYMM_PATTERNS.length; i++) {
				const result = value.match( Validator.YYYYMM_PATTERNS[i] );
				if (result && result.length === 3) {
					const y = parseInt(result[1], 10);
					const yyyy = y + (y > 99 ? 0 : (y >= 70 ? 1900 : 2000));	// 西暦4桁化（西暦2桁で70未満なら2000年代、70以上であれば1900年代とする）
					const mm = parseInt(result[2], 10) - 1;
					const d = new Date(yyyy, mm, 1);
					if (!isNaN(d) && d.getFullYear() == yyyy && d.getMonth() == mm) {
						return d;
					}
				}
			}
		}
		return null;
	}

	/**
	 * (String -> Date)日付変換(有効な書式は yyyy/MM/dd, yy/MM/dd, yyyyMMdd, yyMMdd)
	 * @return 日付、変換できない時は ''
	 */
	, toDate : function(value) {
		if (value > '') {
			for (let i = 0; i < Validator.YYYYMMDD_PATTERNS.length; i++) {
				let result = value.match( Validator.YYYYMMDD_PATTERNS[i] );
				if (result && result.length === 4) {
					const y = parseInt(result[1], 10);
					const yyyy = y + (y > 99 ? 0 : (y >= 70 ? 1900 : 2000));	// 西暦4桁化（西暦2桁で70未満なら2000年代、70以上であれば1900年代とする）
					const mm = parseInt(result[2], 10) - 1;
					const dd = parseInt(result[3], 10);
					const d = new Date(yyyy, mm, dd);
					if (!isNaN(d) && d.getFullYear() == yyyy && d.getMonth() == mm && d.getDate() == dd) {
						return d;
					}
				}
			}
			for (let i = 0; i < Validator.YYYYMM_PATTERNS.length; i++) {
				let result = value.match( Validator.YYYYMM_PATTERNS[i] );
				if (result && result.length === 3) {
					const y = parseInt(result[1], 10);
					const yyyy = y + (y > 99 ? 0 : (y >= 70 ? 1900 : 2000));	// 西暦4桁化（西暦2桁で70未満なら2000年代、70以上であれば1900年代とする）
					const mm = parseInt(result[2], 10) - 1;
					const d = new Date(yyyy, mm, 1);
					if (!isNaN(d) && d.getFullYear() == yyyy && d.getMonth() == mm) {
						return d;
					}
				}
			}
		}
		return null;
	}

	/**
	 * 時刻変換、正しい時刻であれば
	 */
	, _toTime : function(value) {
		if (value > '') {
			// 型チェック
			const patterns = [
				  /^(\d{2})\:(\d{2})$/
				, /^(\d{2})(\d{2})$/
			];
			for (let i = 0; i < patterns.length; i++) {
				const result = value.match( patterns[i] );
				if (result) {
					let hh = parseInt(result[1] , 10), mm = parseInt(result[2] , 10);
					if ((0 <= hh && hh <= 23) && (0 <= mm && mm <= 59)) {
						if (hh < 10)
							hh = ('0' + hh);
						if (mm < 10)
							mm = ('0' + mm);
						const time = hh + ':' + mm;
						return time;
					}
				}
			}
			return null;
		}
	}

	/**
	 * 日付From 整合性チェック
	 * @param option : 日付Toのid
	 */
	, _dateFrom : function(from, to) {
		if (from <= '' || to <= '') {
			return '';
		}
		// 型チェック
		const vDtFrom = Validator.toDate(from);
		const vDtTo = Validator.toDate(to);
		if (!vDtFrom || !vDtTo) {
			return '';
		}
		// 日付大小チェック
		return (vDtFrom > vDtTo ? NCI.getMessage('MSG0107') : '');
	}

	/**
	 * 日付To 整合性チェック
	 * @param option : 日付Fromのid
	 */
	, _dateTo : function(to, from) {
		if (from <= '' || to <= '') {
			return '';
		}
		// 型チェック
		const vDtFrom = Validator.toDate(from);
		const vDtTo = Validator.toDate(to);
		if (!vDtFrom || !vDtTo) {
			return '';
		}
		// 日付大小チェック
		return (vDtFrom > vDtTo ? NCI.getMessage('MSG0107') : '');
	}

	/**
	 * 時刻From 整合性チェック
	 * @param option : 日付Toのid
	 */
	, _timeFrom : function(from, to) {
		if (from <= '' || to <= '') {
			return '';
		}
		// 型チェック
		const vTmFrom = Validator._toTime(from);
		const vTmTo = Validator._toTime(to);
		if (!vTmFrom || !vTmTo) {
			return '';
		}
		// 時刻大小チェック
		return (vTmFrom > vTmTo ? NCI.getMessage('MSG0107') : '');
	}

	/**
	 * 時刻To 整合性チェック
	 * @param option : 日付Fromのid
	 */
	, _timeTo : function(to, from) {
		if (from <= '' || to <= '') {
			return '';
		}
		// 型チェック
		const vTmFrom = Validator.toDate(from);
		const vTmTo = Validator.toDate(to);
		if (!vTmFrom || !vTmTo) {
			return '';
		}
		// 時刻大小チェック
		return (vTmFrom > vTmTo ? NCI.getMessage('MSG0107') : '');
	}

	/** 全角カナのみで入力されているか */
	, _fullKanaOnly : function(value, option) {
		if (value > '' && !value.match(/^[ァ-ヶー 　]*$/)) {
			return NCI.getMessage('MSG0091');
		}
	}

	/** 半角カナのみで入力されているか */
	, _halfKanaOnly : function(value, option) {
		if (value > '' && !value.match(/^[｡-ﾟ ]*$/)) {
			return  NCI.getMessage('MSG0079');
		}
	}

	/** 半角のみ（半角英数字＋半角カナ）で入力されているか */
	, _halfWidthOnly : function(value, option) {
		if (value > '' && !value.match(/^[ -~｡-ﾟ]*$/)) {
			return  NCI.getMessage('MSG0080');
		}
	}

	/** 全角のみで入力されているか */
	, _fullWidthOnly : function(value, option) {
		if (value > '' && !value.match(/^[^ -~｡-ﾟ]*$/)) {
		//if (value > '' && !value.match(/^[^ -~｡-ﾟ]+$/)) {
			return NCI.getMessage('MSG0078');
		}
	}

	/** メールアドレス形式 */
	, _mailFormat : function(value, option) {
		if (value > '') {
			const values = value.split(/[,\s]+/);
			const len = values.length;
			for (let i = 0; i < len; i++) {
				const address = values[i];
				if (address > '' && !address.match(/^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9\-]{2,})+$/)) {
					return NCI.getMessage('MSG0090');
				}
			}
		}
	}

	/** 電話番号形式 */
	, _tel : function(value, option) {
		if (value > '' && !value.match(/^[0-9][0-9-]{1,19}$/)) {
			return NCI.getMessage('MSG0089');
		}
	}

	/** IPアドレス形式(IPv4) */
	, _ipAddr : function(value, option) {
		if (value > '' && !value.match(/^\d{1,3}\.\d{1,3}\.\d{1,3}\.\d{1,3}$/)) {
			return NCI.getMessage('MSG0088');
		}
	}

	/** 郵便番号形式 */
	, _postCode : function(value, option) {
		if (value > '' && !value.match(/^(\d{3}-\d{4}|\d{7})$/)) {
			return NCI.getMessage('MSG0087');
		}
	}

	/** アルファベット（大文字小文字を区別しない） */
	, _alphabetOnly : function(value, option) {
		if (value > '' && !value.match(/^[a-zA-Z]*$/)) {
			return NCI.getMessage('MSG0076');
		}
	}
	/** アルファベット＋数値 */
	, _alphabetNumber : function(value, option) {
		if (value > '' && !value.match(/^[a-zA-Z0-9]*$/)) {
			return NCI.getMessage('MSG0082');
		}
	}
	/** アルファベット＋記号 */
	, _alphabetSymbol : function(value, option) {
		if (value > '' && !value.match(/^[ -/:-~]*$/)) {
			return NCI.getMessage('MSG0083');
		}
	}
	/** アルファベット＋数字＋記号 */
	, _alphabetSymbolNumber : function(value, option) {
		if (value > '' && !value.match(/^[ -~]*$/)) {
			return NCI.getMessage('MSG0084');
		}
	}

	/** アルファベット＋数字＋記号＋改行 */
	, _alphabetSymbolNumberLf : function(value, option) {
		if (value > '' && !value.match(/^[ -~/\n]*$/)) {
			return NCI.getMessage('MSG0086');
		}
	}

	/** 数字（0-9のみ、マイナス／小数点が必要なら _numeric()を使うべし）*/
	, _strNumberOnly : function(value, option) {
		if (value > '' && !value.match(/^[0-9]*$/)) {
			return NCI.getMessage('MSG0077');
		}
	}

	/** 数値＋アルファベット＋アンダースコア */
	, _alphaNumUnderscore :  function(value, option) {
		if (value > '' && !value.match(/^[a-zA-Z0-9_]*$/)) {
			return NCI.getMessage('MSG0085');
		}
	}

	/** 数値書式チェック */
	, _numeric : function(value, option) {
		if (value > '') {
			// 値が入力済かつ非数値ならエラー
			if (isNaN(value)) {
				return NCI.getMessage('MSG0190');
			}
			else {
				// 端数処理と小数点以下ゼロ埋めしたうえで、書式チェック
				value = Validator._formatDecimalPoint(value, option);
				// 最大最小の範囲チェック
				let msg = Validator._rangeNumeric(value, option);
				if (msg) {
					return msg;
				}
				// 小数点桁数の超過チェック
				msg = Validator._maxDecimalPoint(value, option);
				if (msg) {
					return msg;
				}
			}
		}
	}

	/** 整数書式チェック */
	, _integer : function(value, option) {
		if (value > '') {
			// 値が入力済かつ「非数値またはドットが含まれて」いればエラー
			if (isNaN(value) || value.match(/[.]/i)) {
				return NCI.getMessage('MSG0081');
			}
			else {
				// 端数処理と小数点以下ゼロ埋めしたうえで、書式チェック
				value = Validator._formatDecimalPoint(value, option);
				// 最大最小の範囲チェック
				const msg = Validator._rangeNumeric(value, option);
				if (msg) {
					return msg;
				}
			}
		}
	}

	// 日付範囲チェック対応 入力される日付を
	// option.minで指定された日付または1900/01/01から
	// option.maxで指定された日付2099/12/31に制限する
	/** 範囲（日付、startDate / endDate） */
	, _rangeDate : function(value , option) {
			const startDateStr  = (option.min) ? option.min : "1900/01/01" ;
			const endDateStr = (option.max) ? option.max :"2099/12/31";
			const startDate = new Date(startDateStr), endDate = new Date(endDateStr);
			if ((value < startDate || endDate < value)) {
				// {0}は「{1}～{2}」の範囲内で入力してください。
				return NCI.getMessage("MSG0005", [NCI.addComma("日付", option),NCI.addComma(startDateStr, option), NCI.addComma(endDateStr, option)]);
			}
			// メッセージなしで返す
			return '';
	}

	/** 範囲（数値/整数、min / max） */
	, _rangeNumeric : function(value, option) {
		if (value > '' && !isNaN(value) && option) {
			const v = +value, min = +option.min, max = +option.max;
			if (!isNaN(min) && !isNaN(max) && (v < min || max < v)) {
				return NCI.getMessage("MSG0104", [NCI.addComma(min, option), NCI.addComma(max, option)]);
			}
			if (!isNaN(min) && v < min) {
				return NCI.getMessage("MSG0105", NCI.addComma(min, option));
			}
			if (!isNaN(max) && max < v) {
				return NCI.getMessage("MSG0106", NCI.addComma(max, option));
			}
		}
	}
	/**
	 * 正規表現
	 * @param value 値
	 * @param option オプション。例えば正規表現が /^[0-9]$/ なら option.regPatternは'0-9'となる
	 */
	, _regExp : function(value, option) {
		if (value > '' && option.regPattern && !value.match('^[' + option.regPattern + ']+$')) {
			const c = value.match('[^' + option.regPattern + ']+');
			return NCI.getMessage('MSG0103', c);
		}
	}

	/** 小数点桁数の超過チェック */
	, _maxDecimalPoint : function(value, option) {
		if (value > '' && option && option.decimalPlaces) {
			// 有効な小数点桁数
			const decimalPoint = +option.decimalPlaces || 0;
			if (decimalPoint && !isNaN(value)) {
				// 小数点以下の値
				const decimals = value.replace(/^([+-]?\d*)(.)(\d*)/,'$3');
				if (decimals && decimals.length > decimalPoint) {
					return NCI.getMessage('MSG0096', decimalPoint);
				}
			}
		}
		return '';
	}

	/** 小数点以下の書式設定（端数の丸め処理＋小数点以下の0埋め） */
	, _formatDecimalPoint : function(value, option) {
		let val = value;
		if (!isNaN(val) && val > '' && option) {
			let roundType = option.roundType, decimalPlaces = option.decimalPlaces;

			// 端数の丸め処理
			if (roundType != null && decimalPlaces != null) {
				const ROUND_UP = 0, ROUND_DOWN = 1, ROUND_HALF_UP = 4;	// JavaのBigDecimalの定数に等しい
				const _pow = Math.pow( 10, decimalPlaces ) ;
				// 端数処理を修正したら、忘れずに PartsRendererTextbox.toDecimalFormat()も確認すること！
				switch (roundType) {
				case 'ROUND_UP':
				case ROUND_UP:
					val = Math.ceil(val * _pow) / _pow;
					break;
				case 'ROUND_DOWN':
				case ROUND_DOWN:
					val = Math.floor(val * _pow) / _pow;
					break;
				case 'ROUND_HALF_UP':
				case ROUND_HALF_UP:
					val = Math.round(val * _pow) / _pow;
					break;
				}
			}
			// フォーマット
			const conf = {
					// 最小小数点桁数：ゼロ埋めするなら最大小数点桁数に等しい、ゼロ埋めしないなら最小小数点桁数は0
					minimumFractionDigits : option.zeroPadRight ? option.decimalPlaces : 0,
					// 最大小数点桁数：端数処理するならオプションの小数点桁数のまま、端数処理しないなら小数点桁数の最大値である9
					maximumFractionDigits : roundType != null ? option.decimalPlaces : 9,
					// カンマ区切り：別途 NCI.addComma()にて行うので、ここでは実施しない
					useGrouping : false
			};
			val = new Intl.NumberFormat("ja-jp", conf).format(val)
		}
		return val;
	}

	/** ファイル名 */
	, _fileName : function(value, option) {
		const pattern =
			"[\\x00-\\x1f<>:\"/\\\\|?*]" +
        	"|^(CON|PRN|AUX|NUL|COM[0-9]|LPT[0-9]|CLOCK\\$)(\\.|$)" +
        	"|^[\\. ]$";
		if (value > '' && value.match(pattern)) {
			const char = value.match(pattern);
			return NCI.getMessage('MSG0103', char);
		}
	}
    /****************************************************************************************/

};
