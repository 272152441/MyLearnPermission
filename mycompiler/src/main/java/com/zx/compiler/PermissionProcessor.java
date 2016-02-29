package com.zx.compiler;

import com.google.auto.service.AutoService;
import com.zx.permission.MyPermissionDenied;
import com.zx.permission.MyPermissionGrant;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;

import static javax.lang.model.SourceVersion.latestSupported;

/**
 * Description
 *
 * @version 1.0
 *          time 16:52 2016/2/29.
 * @auther zhangxiao
 */

@AutoService(Processor.class)
public class PermissionProcessor extends AbstractProcessor{
    private Messager messager;
    private Elements elements;
    private Map<String , ProxyInfo> proxyInfoMap = new HashMap<>();


    @Override
    public Set<String> getSupportedAnnotationTypes() {

        HashSet<String> supportType = new LinkedHashSet<>();
        supportType.add(MyPermissionDenied.class.getCanonicalName());
        supportType.add(MyPermissionGrant.class.getCanonicalName());

        return supportType;
    }

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);

        messager = processingEnv.getMessager();
        elements = processingEnv.getElementUtils();
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return latestSupported();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {

        messager.printMessage(Diagnostic.Kind.NOTE,"process ...");

        for(Element item: roundEnv.getElementsAnnotatedWith(MyPermissionGrant.class)){

            ExecutableElement annotationMethod = (ExecutableElement)item;


            TypeElement classElement = (TypeElement)annotationMethod.getEnclosingElement();
            //full class name
            String fqClassName = classElement.getQualifiedName().toString();
            PackageElement packageElement = elements.getPackageOf(classElement);
            String packageName = packageElement.getQualifiedName().toString();
            String className = getClassName(classElement,packageName);

            messager.printMessage(Diagnostic.Kind.NOTE,"class name is "+ className);

            ProxyInfo proxyInfo = proxyInfoMap.get(className);
        }




        return false;
    }


    private String getClassName(TypeElement typeElement,String packageName){
        int packageLength = packageName.length()+1;

        return typeElement.getQualifiedName().toString().substring(packageLength).replace('.','$');
    }

}
