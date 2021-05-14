package jp.co.nci.iwf.cdi.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Stereotype;
import javax.inject.Named;

/**
 * Jerseyリクエストを扱うクラスであることを示すアノテーション
 */
@Stereotype
@Named
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@RequestScoped
public @interface Endpoint {

}
