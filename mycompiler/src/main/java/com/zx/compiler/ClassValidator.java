package com.zx.compiler;

import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;

/**
 * Description
 *
 * @version 1.0
 *          time 16:48 2016/2/29.
 * @auther zhangxiao
 */
public class ClassValidator {

    static boolean isPublic(Element element){
        return element.getModifiers().contains(Modifier.PUBLIC);
    }

    static boolean isAnnotation(Element element){
        return element.getModifiers().contains(Modifier.ABSTRACT);
    }
}
