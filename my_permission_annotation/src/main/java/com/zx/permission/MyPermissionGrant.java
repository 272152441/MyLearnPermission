package com.zx.permission;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Description
 *
 * @version 1.0
 *          time 11:28 2016/2/29.
 * @auther zhangxiao
 */
@Target(ElementType.METHOD)
public @interface MyPermissionGrant {
    int value();
}
