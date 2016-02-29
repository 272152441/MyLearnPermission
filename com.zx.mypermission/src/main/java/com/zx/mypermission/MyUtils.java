package com.zx.mypermission;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.Fragment;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Description
 *
 * @version 1.0
 *          time 15:36 2016/2/29.
 * @auther zhangxiao
 */
public class MyUtils {

    private MyUtils(){

    }

    public static boolean isOverMarshmallow(){
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    public static List<String> findDeniedPermission(Activity activity ,String ... permission){
        List<String> deniedList = new ArrayList<>();
        for(String item :permission){

            if(activity.checkSelfPermission(item)!= PackageManager.PERMISSION_GRANTED){
                deniedList.add(item);
            }
        }

        return deniedList;
    }

    public static List<Method> findAnnotationMethods(Class clazz, Class<? extends Annotation> annoClass){
        List<Method> methodList = new ArrayList<>();
        Method methods [] = clazz.getDeclaredMethods();
        for(Method item : methods){
            if(item.isAnnotationPresent(annoClass)){
                methodList.add(item);
            }
        }

        return methodList;
    }

    public static Activity getActivity(Object object){
        if(object instanceof  Activity){
            return (Activity)object;
        }else if(object instanceof Fragment){
            return ((Fragment)object).getActivity();
        }

        return null;
    }
}
