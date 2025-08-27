package akarin.bot.louise2;

import akarin.bot.louise2.annotation.features.FeatureAuth;
import akarin.bot.louise2.annotation.features.LouiseFeature;
import akarin.bot.louise2.annotation.features.OnCommand;
import akarin.bot.louise2.config.LouiseConfig;
import akarin.bot.louise2.config.PluginConfig;
import akarin.bot.louise2.domain.common.LouiseContext;
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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

@SpringBootApplication
@MapperScan("akarin.bot.louise2.mapper")
@Slf4j
public class Louise2Application implements ApplicationRunner {

    @Resource
    private ApplicationContext context;

    @Resource
    private PluginConfig pluginConfig;

    @Resource
    private LouiseConfig louiseConfig;

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
                processFeature(clazz);
            }

        } catch (Exception e) {
            log.error("扫描功能模块失败: ", e);
        }
    }

    private void processFeature(Class<?> clazz) throws NoSuchMethodException, InvocationTargetException,
            InstantiationException, IllegalAccessException {
        // 跳过内部类和匿名类
        if (clazz.isAnonymousClass() || clazz.isLocalClass() || clazz.isMemberClass())
            return;

        FeatureInterface feature;
        // 构造器依赖注入
        if (clazz.getDeclaredConstructors().length == 0)
            feature = (FeatureInterface) clazz.getConstructor().newInstance();
        else if (clazz.getDeclaredConstructors().length == 1) {
            Constructor<?> constructor = clazz.getDeclaredConstructors()[0];
            List<Object> parameters = new ArrayList<>();
            for (Class<?> paramType : constructor.getParameterTypes())
                parameters.add(context.getBean(paramType));
            feature = (FeatureInterface) constructor.newInstance(parameters.toArray());
        } else
            throw new IllegalArgumentException("功能模块 " + clazz.getName() + " 的有参构造器数量不正确");

        // 获取类注解
        String prefix = "";
        for (Annotation annotation : clazz.getDeclaredAnnotations()) {
            if (annotation instanceof LouiseFeature louiseAnno) {
                prefix = louiseAnno.prefix();
                feature.setName(louiseAnno.name());
                feature.setCode(louiseAnno.code());
            }
        }

        // 获取注解方法
        Map<Class<?>, Map<String, List<FeatureMethod>>> annoMap = new HashMap<>();
        for (Method method : clazz.getDeclaredMethods()) {
            FeatureMethod featureMethod = new FeatureMethod(feature, method);
            // 为插件方法包装类添加参数签名
            featureMethod.getParameterSignatures().addAll(Arrays.stream(method.getParameterTypes()).toList());
            // 为方法设置默认信息
            processDefaultSetup(featureMethod, method, feature);
            for (Annotation methodAnno : method.getDeclaredAnnotations()) {
                // 为方法处理权限控制注解
                processAuthAnnotation(methodAnno, featureMethod, method, feature);
                // 为方法处理定时任务注解
                // 为方法处理处理器注解
                processHandlerAnnotation(methodAnno, featureMethod, method, feature, prefix)
                        .forEach((k, v) -> annoMap.computeIfAbsent(k, (e) -> new HashMap<>()).putAll(v));
            }
        }
        annoMap.getOrDefault(OnCommand.class, new HashMap<>()).forEach((k, v) ->
                v.forEach(featureMethod -> feature.addCommandMethod(k, featureMethod)));

        PluginConfig.Plugin plugin = pluginConfig.getPlugins().get(feature.getCode());
        feature.getMethods().forEach(f -> {
            if (plugin == null)
                return;
            PluginConfig.Feature fConfig = plugin.getFeatures().get(f.getMethodCode());
            if (fConfig == null)
                return;
            // 基础参数控制
            f.setCooldown(fConfig.getCooldown() * 1000L);
        });

        FeatureManager.registerFeature(feature);
    }

    private void processDefaultSetup(FeatureMethod featureMethod, Method method, FeatureInterface feature) {
        featureMethod.setMethodName(method.getName());
        featureMethod.setMethodCode(method.getName());
    }

    // 为方法处理权限控制注解
    private void processAuthAnnotation(Annotation methodAnno, FeatureMethod featureMethod, Method method,
                                       FeatureInterface feature) {
        // 为方法处理权限控制注解
        if (methodAnno instanceof FeatureAuth auth) {
            long cooldown = Long.parseLong(auth.cooldown());
            if (cooldown < 0) {
                log.warn("插件 {} 的命令 {} 的冷却时间小于 0, 已忽略", feature.getName(), method.getName());
                cooldown = 0;
            }
            featureMethod.setCooldown(cooldown * 1000);

            if (auth.name().isEmpty())
                featureMethod.setMethodName(method.getName());
            else
                featureMethod.setMethodName(auth.name());

            if (auth.code().isEmpty())
                featureMethod.setMethodCode(method.getName());
            else
                featureMethod.setMethodCode(auth.code());
        }
    }

    private Map<Class<?>, Map<String, List<FeatureMethod>>> processHandlerAnnotation(Annotation methodAnno,
                                                                                     FeatureMethod featureMethod,
                                                                                     Method method,
                                                                                     FeatureInterface feature,
                                                                                     String prefix) {
        PluginConfig.Plugin config = pluginConfig.getPlugins().getOrDefault(feature.getCode(), null);
        Map<Class<?>, Map<String, List<FeatureMethod>>> annoMap = new HashMap<>();

        if (methodAnno instanceof OnCommand commands) {
            if (prefix.isEmpty()) {
                log.warn("插件 {} 的命令前缀为空, 跳过注入 {}", feature.getName(), commands.value());
                return new HashMap<>();
            }
            Map<String, List<FeatureMethod>> handlers = new HashMap<>();
            List<String> commandList = new ArrayList<>();

            if (config != null && config.getFeatures().containsKey(featureMethod.getMethodCode())) {
                PluginConfig.Feature featureConfig = config.getFeatures().get(featureMethod.getMethodCode());
                if (!featureConfig.getEnable())
                    return new HashMap<>();
                commandList.addAll(featureConfig.getCommands());
            }
            if (commandList.isEmpty())
                commandList.addAll(List.of(commands.value()));
            commandList = commandList.stream().distinct().collect(Collectors.toList());

            for (String command : commandList)
                handlers.computeIfAbsent(prefix + command, e -> new ArrayList<>()).add(featureMethod);

            annoMap.put(OnCommand.class, handlers);
            feature.getMethods().add(featureMethod);
        }

        return annoMap;
    }
}
