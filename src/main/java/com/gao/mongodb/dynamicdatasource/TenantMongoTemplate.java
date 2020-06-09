package com.gao.mongodb.dynamicdatasource;

import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.lang.reflect.Method;

/**
 * 租户数据源操作代理类
 * 所有的mongotemplate操作都会走这里代理，然后内部根据threadLocalMongoTemplate变量来决定
 * 使用默认数据源还是TenantAop动态加载的数据源
 */
public class TenantMongoTemplate implements MethodInterceptor {

    //跟当前线程唯一绑定
    private ThreadLocal<MongoTemplate> threadLocalMongoTemplate = new ThreadLocal<>();

    public Object getInstance(Object target, Class[] args, Object[] argsValue) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(target.getClass());
        enhancer.setCallback(this);
        return enhancer.create(args, argsValue);
    }

    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        Object result;
        Object template = getTemplate();
        //template为空代表方法没有被AOP拦截住，使用默认数据源
        if (template == null) {
            result = proxy.invokeSuper(obj, args);
        }
        //这里使用AOP动态set进来的template
        else {
            result = method.invoke(template, args);
        }
        return result;
    }

    public void setTemplate(MongoTemplate template) {
        threadLocalMongoTemplate.set(template);
    }

    public MongoTemplate getTemplate() {
        return threadLocalMongoTemplate.get();
    }
}