package com.zx.compiler;

import java.util.HashMap;
import java.util.Map;

import javax.lang.model.element.TypeElement;

/**
 * Description
 *
 * @version 1.0
 *          time 16:58 2016/2/29.
 * @auther zhangxiao
 */
public class ProxyInfo {
    private String packageName;
    private String targetClassName;
    private String proxyClassName;
    private TypeElement typeElement;

    private Map<Integer, String> grantMethodMap = new HashMap<>();
    private Map<Integer, String> deniedMethodMap = new HashMap<>();

    private final static String PROXY = "PermissionProxy";

    public ProxyInfo(String packageName, String className) {
        this.packageName = packageName;
        targetClassName = className;
        proxyClassName = className + "$$" + PROXY;
    }

    public String getProxyClassFullName() {
        return packageName + "." + proxyClassName;
    }

    public TypeElement getTypeElement() {
        return typeElement;
    }

    public void setTypeElement(TypeElement typeElement) {
        this.typeElement = typeElement;
    }

    public String getTargetClassName() {
        return targetClassName.replace("$", ".");
    }


    public String generateJavaCode() {

        StringBuilder builder = new StringBuilder();
        builder.append("// this is auto generate class , do not modify .\n");
        builder.append("package ").append(packageName).append(";\n\n");
        builder.append("import com.zx.mypermission.*;\n");
        builder.append("\n");
        builder.append("public class ").append(proxyClassName).append(" implements ").append(PROXY).append("<")
                .append(typeElement.getSimpleName()).append(">{ \n");
        generateInjectJavaCode(builder);
        builder.append("\n }");
        return builder.toString();

    }

    private void generateInjectJavaCode(StringBuilder builder) {
        builder.append("@Override\n");
        builder.append("public void grant(").append(typeElement.getSimpleName()).append(" source,int requestCode){\n");
        builder.append("switch(requestCode){");
        for (int key : grantMethodMap.keySet()) {

            builder.append("case ").append(key).append(":");
            builder.append("source.").append(grantMethodMap.get(key)).append("();");
            builder.append("break;");
        }
        builder.append("}}\n");

        builder.append("@Override\n");
        builder.append("public void denied(").append(typeElement.getSimpleName()).append(" source,int requestCode){\n");
        builder.append("switch(requestCode){");
        for (int key : deniedMethodMap.keySet()) {

            builder.append("case ").append(key).append(":");
            builder.append("source.").append(deniedMethodMap.get(key)).append("();");
            builder.append("break;");
        }
        builder.append("}}\n");

    }

    public Map<Integer, String> getGrantMethodMap() {
        return grantMethodMap;
    }

    public void setGrantMethodMap(Map<Integer, String> grantMethodMap) {
        this.grantMethodMap = grantMethodMap;
    }

    public Map<Integer, String> getDeniedMethodMap() {
        return deniedMethodMap;
    }

    public void setDeniedMethodMap(Map<Integer, String> deniedMethodMap) {
        this.deniedMethodMap = deniedMethodMap;
    }
}
