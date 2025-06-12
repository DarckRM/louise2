package akarin.bot.louise2;

import akarin.bot.louise2.annotation.features.FeatureAuth;
import akarin.bot.louise2.annotation.features.LouiseFeature;
import akarin.bot.louise2.annotation.features.OnCommand;
import akarin.bot.louise2.features.common.FeatureInterface;
import akarin.bot.louise2.features.common.FeatureManager;
import akarin.bot.louise2.features.common.FeatureMethod;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@SpringBootApplication
@MapperScan("akarin.bot.louise2.mapper")
@Slf4j
public class Louise2Application implements ApplicationRunner {

    @Resource
    ApplicationContext context;

    public static void main(String[] args) {
        SpringApplication.run(Louise2Application.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // 扫描所有 LouiseFeature 注解的类
        String resources = "akarin.bot.louise2.features";
        try {
            URL url = this.getClass().getClassLoader().getResource(resources.replace('.', '/'));
            assert url != null;
            File root = new File(url.toURI());

            for (File file : Objects.requireNonNull(root.listFiles())) {
                if (file.isDirectory())
                    continue;

                Class<?> clazz = Class.forName(resources + "." + file.getName().replace(".class", ""));
                // 跳过内部类和匿名类
                if (clazz.isAnonymousClass() || clazz.isLocalClass() || clazz.isMemberClass())
                    continue;

                FeatureInterface feature;
                // 构造器依赖注入
                if (clazz.getDeclaredConstructors().length == 0) {
                    feature = (FeatureInterface) clazz.getConstructor().newInstance();
                } else if (clazz.getDeclaredConstructors().length == 1) {
                    Constructor<?> constructor = clazz.getDeclaredConstructors()[0];
                    List<Object> parameters = new ArrayList<>();
                    for (Class<?> paramType : constructor.getParameterTypes())
                        parameters.add(context.getBean(paramType));
                    feature = (FeatureInterface) constructor.newInstance(parameters.toArray());
                } else {
                    throw new IllegalArgumentException("功能模块 " + clazz.getName() + " 的有参构造器数量不正确");
                }

                // 获取类注解
                String prefix = "";
                for (Annotation annotation : clazz.getDeclaredAnnotations()) {
                    if (annotation instanceof LouiseFeature louiseAnno) {
                        prefix = louiseAnno.prefix();
                        feature.setName(louiseAnno.name());
                    }
                }

                // 获取注解方法
                for (Method method : clazz.getDeclaredMethods()) {
                    FeatureMethod featureMethod = new FeatureMethod(feature, method);
                    featureMethod.getParameterSignatures()
                            .addAll(Arrays.stream(method.getParameterTypes()).toList());
                    for (Annotation methodAnno : method.getDeclaredAnnotations()) {
                        // 为方法处理权限控制注解
                        if (methodAnno instanceof FeatureAuth auth) {
                            long cooldown = Long.parseLong(auth.cooldown());
                            if (cooldown < 0) {
                                log.warn("插件 {} 的命令 {} 的冷却时间小于 0, 已忽略", feature.getName(), method.getName());
                                cooldown = 0;
                            }
                            featureMethod.setCooldown(cooldown * 1000);

                            String methodName = auth.name();
                            if (methodName.isEmpty())
                                methodName = method.getName();
                            featureMethod.setMethodName(methodName);
                        }

                        if (methodAnno instanceof OnCommand commands) {
                            if (prefix.isEmpty()) {
                                log.warn("插件 {} 的命令前缀为空, 跳过注入 {}", feature.getName(), commands.value());
                                continue;
                            }
                            for (String command : commands.value())
                                feature.addCommandMethod(prefix + command, featureMethod);
                            feature.getMethods().add(featureMethod);
                        }
                    }
                }

                FeatureManager.registerFeature(feature);
            }

        } catch (Exception e) {
            log.error("扫描功能模块失败: ", e);
        }
    }
}
