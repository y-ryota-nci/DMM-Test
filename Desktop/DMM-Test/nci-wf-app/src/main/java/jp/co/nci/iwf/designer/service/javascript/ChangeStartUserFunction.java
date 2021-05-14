package jp.co.nci.iwf.designer.service.javascript;

import java.io.Serializable;
import java.util.Objects;

import jp.co.nci.iwf.jpa.entity.ex.MwvScreen;
import jp.co.nci.iwf.jpa.entity.mw.MwmContainer;
import jp.co.nci.iwf.util.MiscUtils;

/**
 * 起案担当者変更時の呼び出し関数のJSON変換用クラス。
 */
public class ChangeStartUserFunction implements Serializable {


	/** 関数名 */
	public String funcName;
	/** パラメーター */
	public String param;

	/** コンストラクタ */
	public ChangeStartUserFunction() {}

	/** コンストラクタ */
	public ChangeStartUserFunction(MwmContainer c) {
		this.funcName = c.getChangeStartUserFuncName();
		this.param = c.getChangeStartUserFuncParam();
	}

	/** コンストラクタ */
	public ChangeStartUserFunction(MwvScreen spi) {
		this.funcName = spi.changeStartUserFuncName;
		this.param = spi.changeStartUserFuncParam;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof ChangeStartUserFunction) {
			final ChangeStartUserFunction f = (ChangeStartUserFunction)o;
			return MiscUtils.eq(this.funcName, f.funcName)
					&& MiscUtils.eq(this.param, f.param);
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
