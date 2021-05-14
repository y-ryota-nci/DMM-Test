package jp.co.nci.iwf.cdi.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Stereotype;
import javax.inject.Named;

/**
 * ビジネスロジックを表すアノテーション。
 * ApplicationScopeなので、扱いには注意すること。
 */
@Stereotype
@Named
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@ApplicationScoped
public @interface BizLogic {}
