/** 有効条件評価 */
var Evaluate = {

	/** 有効条件判定処理. */
	execute : function(formula, args) {
		var result = false;
		try {
			// 入力値のエスケープ処理
			for (var k in args) {
				args[k] = ('' + args[k]).replace("\\", "\\\\").replace("'", "\\'");
			}
			var func = new Function("return (" + NCI.format(formula, args) + ")");
			result = func();
		} catch (e) {
		}
		return result;
	},

	toNum : function(val) {
		if (val === '' || isNaN(val)) {
			return null;
		}
		return +val;
	},

	_eq : function(v1, v2, flg) {
		if (flg) {
			v1 = Evaluate.toNum(v1);
			v2 = Evaluate.toNum(v2);
		}
		return v1 === v2;
	},

	_ne : function(v1, v2, flg) {
		if (flg) {
			v1 = Evaluate.toNum(v1);
			v2 = Evaluate.toNum(v2);
		}
		return v1 !== v2;
	},

	_gt : function(v1, v2, flg) {
		if (flg) {
			v1 = Evaluate.toNum(v1);
			v2 = Evaluate.toNum(v2);
		}
		if (v1 == null)
			return false;
		else if (v2 == null)
			return true;
		return v1 > v2;
	},

	_ge : function(v1, v2, flg) {
		if (flg) {
			v1 = Evaluate.toNum(v1);
			v2 = Evaluate.toNum(v2);
		}
		if (v1 == null)
			return (v2 == null);
		return v1 >= v2;
	},

	_lt : function(v1, v2, flg) {
		if (flg) {
			v1 = Evaluate.toNum(v1);
			v2 = Evaluate.toNum(v2);
		}
		if (v1 == null)
			return false;
		else if (v2 == null)
			return true;
		return v1 < v2;
	},

	_le : function(v1, v2, flg) {
		if (flg) {
			v1 = Evaluate.toNum(v1);
			v2 = Evaluate.toNum(v2);
		}
		if (v1 == null)
			return (v2 == null)
		return v1 <= v2;
	}
};