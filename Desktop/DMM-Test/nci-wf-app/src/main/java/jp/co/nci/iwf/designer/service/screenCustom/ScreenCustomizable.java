package jp.co.nci.iwf.designer.service.screenCustom;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Stereotype;

/**
 * 画面カスタムクラスであることを示すアノテーション
 */
@Stereotype
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.FIELD})
@RequestScoped
public @interface ScreenCustomizable {
}
