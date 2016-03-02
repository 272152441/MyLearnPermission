package com.zx.compiler;

import com.google.auto.service.AutoService;
import com.zx.permissionannomation.MyPermissionDenied;
import com.zx.permissionannomation.MyPermissionGrant;

import java.io.IOException;
import java.io.Writer;
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
import javax.tools.JavaFileObject;

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

            ProxyInfo proxyInfo = proxyInfoMap.get(fqClassName);

            if(proxyInfo==null){
                proxyInfo = new ProxyInfo(packageName,className);
                proxyInfoMap.put(fqClassName,proxyInfo);
                proxyInfo.setTypeElement(classElement);
            }

            MyPermissionGrant grant = annotationMethod.getAnnotation(MyPermissionGrant.class);
            int requestCode = grant.value();
            proxyInfo.getGrantMethodMap().put(requestCode,annotationMethod.getSimpleName().toString());
        }


        for(Element item: roundEnv.getElementsAnnotatedWith(MyPermissionDenied.class)){

            ExecutableElement annotationMethod = (ExecutableElement)item;


            TypeElement classElement = (TypeElement)annotationMethod.getEnclosingElement();
            //full class name
            String fqClassName = classElement.getQualifiedName().toString();
            PackageElement packageElement = elements.getPackageOf(classElement);
            String packageName = packageElement.getQualifiedName().toString();
            String className = getClassName(classElement,packageName);

            messager.printMessage(Diagnostic.Kind.NOTE,"class name is "+ className);

            ProxyInfo proxyInfo = proxyInfoMap.get(fqClassName);

            if(proxyInfo==null){
                proxyInfo = new ProxyInfo(packageName,className);
                proxyInfoMap.put(fqClassName,proxyInfo);
                proxyInfo.setTypeElement(classElement);
            }

            MyPermissionDenied grant = annotationMethod.getAnnotation(MyPermissionDenied.class);
            int requestCode = grant.value();
            proxyInfo.getDeniedMethodMap().put(requestCode,annotationMethod.getSimpleName().toString());
        }

        for(ProxyInfo info :proxyInfoMap.values()){


            messager.printMessage(Diagnostic.Kind.NOTE,"proxy full name "+ info.getProxyClassFullName());
            try {
                JavaFileObject javaFileObject = processingEnv.getFiler()
                        .createSourceFile(info.getProxyClassFullName(),info.getTypeElement());
                Writer writer = javaFileObject.openWriter();
                writer.write(info.generateJavaCode());
                writer.flush();
                writer.close();

            } catch (IOException e) {
                e.printStackTrace();
                note(info.getTypeElement(),
                        "Unable to write injector for type %s: %s",
                        info.getTypeElement(), e.getMessage());
            }

        }


        return true;
    }


    private String getClassName(TypeElement typeElement,String packageName){
        int packageLength = packageName.length()+1;

        return typeElement.getQualifiedName().toString().substring(packageLength).replace('.','$');
    }

    private void note(Element element, String message,Object ... objects){
        if(objects.length>0){
            message = String.format(message,objects);
        }
        messager.printMessage(Diagnostic.Kind.NOTE,message);
    }

}
