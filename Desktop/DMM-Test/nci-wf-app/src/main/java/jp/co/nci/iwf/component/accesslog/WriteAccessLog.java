package jp.co.nci.iwf.component.accesslog;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.interceptor.InterceptorBinding;

/**
 * アクセスログの記録対象であることをしめすアノテーション。
 * これを付与されているものに対して<code>AccessLogEndpointInterceptor</code>が実行される。
 */
@InterceptorBinding
@Target(value = { ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface WriteAccessLog {
}
