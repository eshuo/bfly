package info.bfly.core.bean.factory.config;

import info.bfly.core.annotations.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.util.ReflectionUtils;

public class LogBeanPostProcessor implements BeanPostProcessor {
    private void initializeLog(final Object bean, final Class<? extends Object> clazz) {
        ReflectionUtils.doWithFields(clazz, field -> {
            // 获取是否可以方法的属性
            boolean visable = field.isAccessible();
            try {
                // 设置可以属性为可以访问
                field.setAccessible(true);
                if (field.get(bean) == null) {
                    field.set(bean, LoggerFactory.getLogger(clazz));
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new BeanInitializationException(String.format("初始化logger失败!bean=%s;field=%s", bean, field));
            } finally {
                // 恢复原来的访问修饰
                field.setAccessible(visable);
            }
        }, field -> {
            if (field.getAnnotation(Log.class) == null) {
                return false;
            }
            return field.getType().isAssignableFrom(Logger.class);
        });
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {

        return bean;
    }

    @Override
    @SuppressWarnings({"rawtypes", "unchecked"})
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Class clazz = bean.getClass();
        initializeLog(bean, clazz);
        return bean;
    }

}
