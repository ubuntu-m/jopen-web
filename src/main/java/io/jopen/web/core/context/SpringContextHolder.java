package io.jopen.web.core.context;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 */
@Component
public class SpringContextHolder implements ApplicationContextAware {

    private static ApplicationContext applicationHolder;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        applicationHolder = applicationContext;
    }

    public static <T> T getBean(Class<T> clazz) {
        return applicationHolder.getBean(clazz);
    }
}
