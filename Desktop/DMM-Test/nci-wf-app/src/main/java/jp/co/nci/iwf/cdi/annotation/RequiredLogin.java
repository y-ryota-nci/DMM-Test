package jp.co.nci.iwf.cdi.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.inject.Named;
import javax.ws.rs.NameBinding;

/**
 * ログイン済みでないとアクセスできないことを示すマーカー。
 * 当アノテーションを付与されたFilterとEndpointが対になって実現される。
 */
@Named
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.METHOD })
@NameBinding
public @interface RequiredLogin {
}
