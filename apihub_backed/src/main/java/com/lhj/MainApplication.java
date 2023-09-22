package com.lhj;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.File;

/**
 * 主类（项目启动入口）
 *
 * @author <a href="https://github.com/liyupi">程序员鱼皮</a>
 * @from <a href="https://yupi.icu">编程导航知识星球</a>
 */
// todo 如需开启 Redis，须移除 exclude 中的内容
@SpringBootApplication(exclude = {RedisAutoConfiguration.class})
@MapperScan("com.lhj.mapper")
@EnableScheduling
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
@EnableDubbo
public class MainApplication {

    public static void main(String[] args) {
//        ApplicationHome home = new ApplicationHome(MainApplication.class);
//        File jarFile = home.getSource();
//        String dirPath = jarFile.getParentFile().toString();
//        String filePath = dirPath + File.separator + ".dubbo";
//        System.out.println(filePath);
//        System.setProperty("dubbo.meta.cache.filePath", filePath);
//        System.setProperty("dubbo.mapping.cache.filePath",filePath);
        SpringApplication.run(MainApplication.class, args);
    }

}
