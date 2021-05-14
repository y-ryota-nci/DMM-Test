package jp.co.nci.iwf.jersey.base;

import jp.co.nci.iwf.component.CodeBook;
import jp.co.nci.iwf.util.MiscUtils;

/**
 * EndPointの基底クラス
 */
public abstract class BaseEndpoint<REQ extends IRequest> extends MiscUtils implements IEndpoint<REQ>, CodeBook {
}
