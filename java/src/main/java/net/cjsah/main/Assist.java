package net.cjsah.main;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.quartz.SchedulerException;

@Slf4j
public class Assist {
    @SneakyThrows
    public static void main(String[] args) throws SchedulerException, InterruptedException {
        ClassPool.getDefault().insertClassPath("/home/cjsah/project/test-aio/tmp/aspose-words-24.7-jdk17.jar");
        CtClass ctClass;CtMethod[] methods;

        ctClass = ClassPool.getDefault().getCtClass("com.aspose.words.zzA1");
        methods = ctClass.getDeclaredMethods();
        for (CtMethod method : methods) {
            CtClass[] types = method.getParameterTypes();
            if (types.length == 0 && "zzVUb".equals(method.getName())) {
                method.setBody("return 256;");
                break;
            }
        }
        ctClass.writeFile();

        ctClass = ClassPool.getDefault().getCtClass("com.aspose.words.zzZN");
        methods = ctClass.getDeclaredMethods();
        for (CtMethod method : methods) {
            CtClass[] types = method.getParameterTypes();
            if (types.length == 0 && "zzYFB".equals(method.getName())) {
                method.setBody("{if (zzYLc == 0L) { zzYLc ^= zzZlz; } return com.aspose.words.zzX6d.zzYW8;}");
                break;
            }
        }
        ctClass.writeFile();

    }

}
