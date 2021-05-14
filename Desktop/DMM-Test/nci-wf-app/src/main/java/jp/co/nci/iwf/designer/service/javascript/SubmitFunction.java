package jp.co.nci.iwf.designer.service.javascript;

import java.io.Serializable;
import java.util.Objects;

import jp.co.nci.iwf.designer.parts.design.PartsDesignContainer;
import jp.co.nci.iwf.jpa.entity.ex.MwvScreen;
import jp.co.nci.iwf.jpa.entity.mw.MwmContainer;
import jp.co.nci.iwf.util.MiscUtils;

/**
 * パーツコンテナのサブミット時の呼び出し関数のJSON変換用クラス。
 */
public class SubmitFunction implements Serializable {
	/** 関数名 */
	public String funcName;
	/** パラメーター */
	public String param;

	/** コンストラクタ */
	public SubmitFunction() {}

	/** コンストラクタ */
	public SubmitFunction(MwvScreen s) {
		this.funcName = s.submitFuncName;
		this.param = s.submitFuncParam;
	}

	/** コンストラクタ */
	public SubmitFunction(MwmContainer c) {
		this.funcName = c.getSubmitFuncName();
		this.param = c.getSubmitFuncParam();
	}

	/** コンストラクタ */
	public SubmitFunction(PartsDesignContainer c) {
		this.funcName = c.submitFuncName;
		this.param = c.submitFuncParam;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof SubmitFunction) {
			final SubmitFunction sf = (SubmitFunction)o;
			return MiscUtils.eq(this.funcName, sf.funcName)
					&& MiscUtils.eq(this.param, sf.param);
		}
		return super.equals(o);
	}

	@Override
	public int hashCode() {
		if (funcName == null)
			return 17;
		return Objects.hash(this.funcName, this.param);
	}
}
