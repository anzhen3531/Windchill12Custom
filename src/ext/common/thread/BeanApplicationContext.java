package ext.common.thread;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * Bean 应用程序上下文
 *
 * @author anzhen
 * @date 2024/12/28
 */
@Component
public class BeanApplicationContext implements ApplicationContextAware {

    public static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        BeanApplicationContext.applicationContext = context;
    }

    public static <T> T getBeanByClass(Class<T> clazz) {
        return getBean(clazz.getName());
    }

    public static <T> T getBean(String className) {
        if (applicationContext.containsBean(className)) {
            return (T) applicationContext.getBean(className);
        } else {
            return null;
        }
    }
}
