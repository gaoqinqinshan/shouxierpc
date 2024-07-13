package com.gao.spring;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * 目的：自定义Spring框架的BeanDefinitionParser
 * 作用：解析xml配置文件的bean并且把他转换为spring容器管理的 BeanDefinition
 */
public class MyBeanDefinitionParser implements BeanDefinitionParser {

    // 接受一个Class对象作为参数,这个Class对象表示要创建的类
    private final Class<?> beanClass;

    MyBeanDefinitionParser(Class<?> beanClass) {
        this.beanClass = beanClass;
    }

    // 解析XML元素,将解析结果封装到BeanDefinition对象中
    // 返回true表示支持解析该元素,返回false表示不支持解析该元素
    @Override
    public BeanDefinition parse(Element element, ParserContext parserContext) {

        // 创建一个用于定于bean的类一般用RootBeanDefinition
        RootBeanDefinition beanDefinition = new RootBeanDefinition();
        // 设置要实例化的bean类
        beanDefinition.setBeanClass(beanClass);
        // 设置bean是否被延时初始化
        beanDefinition.setLazyInit(false);
        // 从Element中提取属性值,并设置到beanName
        String beanName = element.getAttribute("id");
        // 注册
        parserContext.getRegistry().registerBeanDefinition(beanName, beanDefinition);

        for (Method method : beanClass.getMethods()) {
            // 如果对象不符合JavaBean规范的属性的setter或者getter方法
            if (!isProperty(method, beanClass)) continue;
            String name = method.getName();
            // 将名字去掉getter、setter方法名字 只保留属性的名字（并且设置成小写）
            String methodName = name.substring(3, 4).toLowerCase() + name.substring(4);
            // 从xml中提取methodName
            String vale = element.getAttribute(methodName);
            // 把methodName,value放到bean里面
            beanDefinition.getPropertyValues().addPropertyValue(methodName, vale);
        }

        return null;


    }

    //判断一个给定的 Method 对象是否是一个符合JavaBean规范的属性的setter或者getter方法
    private boolean isProperty(Method method, Class beanClass) {
        // 获取方法名
        String methodName = method.getName();
        // 检查该方法是否为setter方法
        boolean flag = methodName.length() > 3 && methodName.startsWith("set") && Modifier.isPublic(method.getModifiers()) && method.getParameterTypes().length == 1;
        // 设置getter为空（并且这个Method为JAvaApi的反射类通过反射就可以知道信息了）
        Method getter = null;
        // 如果这个方法不为getter，返回为false
        if (!flag) return false;
        // 获取method的第一个类型
        Class<?> type = method.getParameterTypes()[0];
        // 查找在 beanClass 类中查找一个名为 "get" + methodName.substring(3)的方法
        try {
            getter = beanClass.getMethod("get" + methodName.substring(3));
        } catch (NoSuchMethodException e) {

        }
        // 如果geeter为空则前面查找的"get" + methodName不存在，开始查找"is"+ methodName.substring(3)的方法
        if (getter == null) {
            try {
                getter = beanClass.getMethod("is" + methodName.substring(3));
            } catch (NoSuchMethodException e) {
            }
        }
        // 检查 getter 方法是否为 public，并且其返回类型是否与 type 变量相同。
        // 如果满足这两个条件，flag 变量将被设置为 true，否则将被设置为 false。
        flag = getter != null && Modifier.isPublic(getter.getModifiers()) && type.equals(getter.getReturnType());
        // 返回flag
        return flag;
    }
}
