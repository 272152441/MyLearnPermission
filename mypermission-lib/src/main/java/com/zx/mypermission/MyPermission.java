package com.zx.mypermission;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.Fragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Description
 *
 * @version 1.0
 *          time 14:31 2016/2/29.
 * @auther zhangxiao
 */
public class MyPermission {

    private static final String SUFFIX = "$$PermissionProxy";

    public static void requirePermission(Activity activity, int requireCode, String... permissions) {
        requireInnerPermission(activity,requireCode,permissions);
    }

    public static void requirePermission(Fragment fragment, int requireCode, String... permissions) {
        requireInnerPermission(fragment,requireCode,permissions);
    }

    @TargetApi(value = Build.VERSION_CODES.M)
    private static void requireInnerPermission(Object object, int requireCode, String... permissions) {
        if (!MyUtils.isOverMarshmallow()) {
            doExcuteSuccess(object, requireCode);
            return;
        }

        List<String> permissionDeniedList = MyUtils.findDeniedPermission(MyUtils.getActivity(object), permissions);

        if (permissionDeniedList.size() > 0) {
            if (object instanceof Activity) {
                ((Activity) object).requestPermissions(permissionDeniedList.toArray(new String[permissionDeniedList.size()]), requireCode);

            } else if (object instanceof Fragment) {
                ((Fragment) object).requestPermissions(permissionDeniedList.toArray(new String[permissionDeniedList.size()]), requireCode);
            } else {
                throw new IllegalArgumentException(object.getClass().getName() + "is not support");
            }
        } else {
            doExcuteSuccess(object, requireCode);
        }


    }

    private static void doExcuteSuccess(Object object, int requireCode) {
        Class clazz = object.getClass();

        try {
            Class proxyClass = Class.forName(clazz.getName() + SUFFIX);
            PermissionProxy proxy = (PermissionProxy) proxyClass.newInstance();
            proxy.grant(object, requireCode);

        } catch (ClassNotFoundException e) {

        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }

    private static void doExcuteFail(Object object, int requireCode) {
        Class clazz = object.getClass();

        try {
            Class proxyClass = Class.forName(clazz.getName() + SUFFIX);
            PermissionProxy proxy = (PermissionProxy) proxyClass.newInstance();
            proxy.denied(object, requireCode);

        } catch (ClassNotFoundException e) {

        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }

    public static void onRequestPermissionResult(Activity activity, int requestCode, String[] permissions, int[] grantPermission){
        requestPermissionResult(activity,requestCode,permissions,grantPermission);
    }

    public static void onRequestPermissionResult(Fragment fragment, int requestCode, String[] permissions, int[] grantPermission){
        requestPermissionResult(fragment,requestCode,permissions,grantPermission);
    }

    private static void requestPermissionResult(Object object,int requestCode, String[] permissions, int[] grantPermission){
        List<String> deniedList = new ArrayList<>();

        for(int i = 0 ; i < grantPermission.length ; i++){
            if(grantPermission[i] != PackageManager.PERMISSION_GRANTED){
                deniedList.add(permissions[i]);
            }

        }

        if(deniedList.size()>0){
            doExcuteFail(object,requestCode);
        }else{
            doExcuteSuccess(object,requestCode);
        }
    }

}
