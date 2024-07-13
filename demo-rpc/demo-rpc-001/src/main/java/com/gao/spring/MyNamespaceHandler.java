package com.gao.spring;

import com.gao.spring.bean.ConsumerBean;
import com.gao.spring.bean.ProviderBean;
import com.gao.spring.bean.ServerBean;
import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

/**
 * 自定义XMl
 */

public class MyNamespaceHandler extends NamespaceHandlerSupport {
    @Override
    public void init() {
        registerBeanDefinitionParser("consumer", new MyBeanDefinitionParser(ConsumerBean.class));
        registerBeanDefinitionParser("provider", new MyBeanDefinitionParser(ProviderBean.class));
        registerBeanDefinitionParser("server", new MyBeanDefinitionParser(ServerBean.class));
    }
}
