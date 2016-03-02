package com.zx.mypermission;

/**
 * Description
 *
 * @version 1.0
 *          time 15:54 2016/2/29.
 * @auther zhangxiao
 */
public interface PermissionProxy <T>{

    void grant(T t , int requestCode);

    void denied(T t , int requestCode);
}
