
/** パスワード強度を計算＆表示 */
function showPasswordStrength(str) {
	// パスワード強度
	var strength = score(str);
	if (console) console.log('score("' + str + '") = ' + strength);

	// 「数字＋大英字＋小英字」の組み合わせだと一字あたりのパスワード強度=5。
	// これを基準に、パスワード強度を色付け
	var bgColor = "red", val = NCI.getMessage('danger'), color = "black";

	if (strength >= 40)
		bgColor = "green", val = NCI.getMessage('strong'), color = "white";
	else if (strength > 30)
		bgColor = 'yellowgreen', val = NCI.getMessage('soft');
	else if (strength >= 24)
		bgColor = "gold", val = NCI.getMessage('weak');
	else if (strength >= 21)
		bgColor = "orange", val = NCI.getMessage('weak');
	else if (strength >= 18)
		bgColor = "darkorange";
	else if (strength >= 15)
		bgColor = "orangered";
	else if (strength == 0)
		bgColor = '', val = NCI.getMessage('passwordStrength'), color = 'black';

	var css = { 'color' : color, 'background-color' : bgColor };
	$('#password-strength').css(css).text(val);
}

/** パスワード強度の計算 */
function score(str) {
	// 文字種のビットフラグ
	var NUMBER = 1, LOWER = 2, UPPER = 4, MARK = 8;
	// ビットフラグ
	var flags = 0;
	// 最大値＝全文字種が入力された
	var max = NUMBER | LOWER | UPPER | MARK;
	for (var i = 0; i < str.length; i++) {
		var s = str[i];
		// ビット単位のORで文字種を求める
		if ('a' <= s && s <= 'z') flags |= LOWER;
		else if ('A' <= s && s <= 'Z') flags |= UPPER;
		else if ('0' <= s && s <= '9') flags |= NUMBER;
		else flags |= MARK

		if (flags >= max) break;
	}
	// 文字種ごとに多様性を計上
	var variety = 0, cnt = 0;
	if ((flags & NUMBER) != 0) { variety += 1; cnt++; }
	if ((flags & LOWER) != 0) { variety += 2; cnt++; }
	if ((flags & UPPER) != 0) { variety += 2; cnt++; }
	if ((flags & MARK) != 0) { variety += 3; cnt++; }

	// 強度＝文字数＊多様性
	return str.length * variety;
}
