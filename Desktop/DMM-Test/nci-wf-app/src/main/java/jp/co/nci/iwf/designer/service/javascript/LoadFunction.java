package jp.co.nci.iwf.designer.service.javascript;

import java.io.Serializable;
import java.util.Objects;

import jp.co.nci.iwf.designer.parts.design.PartsDesignContainer;
import jp.co.nci.iwf.jpa.entity.ex.MwvScreen;
import jp.co.nci.iwf.jpa.entity.mw.MwmContainer;
import jp.co.nci.iwf.util.MiscUtils;

/**
 * パーツコンテナのロード時の呼び出し関数のJSON変換用クラス。
 */
public class LoadFunction implements Serializable {

	/** 関数名 */
	public String funcName;
	/** パラメーター */
	public String param;

	/** コンストラクタ */
	public LoadFunction() {}

	/** コンストラクタ */
	public LoadFunction(PartsDesignContainer d) {
		this.funcName = d.loadFuncName;
		this.param = d.loadFuncParam;
	}

	/** コンストラクタ */
	public LoadFunction(MwmContainer c) {
		this.funcName = c.getLoadFuncName();
		this.param = c.getLoadFuncParam();
	}

	/** コンストラクタ */
	public LoadFunction(MwvScreen spi) {
		this.funcName = spi.loadFuncName;
		this.param = spi.loadFuncParam;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof LoadFunction) {
			final LoadFunction sf = (LoadFunction)o;
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
