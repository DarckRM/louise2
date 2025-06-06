package akarin.bot.louise2;

import akarin.bot.louise2.annotation.features.LouiseFeature;
import akarin.bot.louise2.annotation.features.OnCommand;
import akarin.bot.louise2.features.common.FeatureInterface;
import akarin.bot.louise2.features.common.FeatureManager;
import akarin.bot.louise2.features.common.FeatureMethod;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.lang.annotation.Annotation;
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
                FeatureInterface feature = (FeatureInterface) clazz.newInstance();

                // 获取注解方法
                for (Method method : clazz.getDeclaredMethods()) {
                    for (Annotation methodAnno : method.getDeclaredAnnotations()) {
                        FeatureMethod featureMethod = new FeatureMethod(feature, method);
                        featureMethod.getParameterSignatures()
                                .addAll(Arrays.stream(method.getParameterTypes()).toList());

                        if (methodAnno instanceof OnCommand commands) {
                            for (String command : commands.value())
                                feature.addCommandMethod(command, featureMethod);
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
