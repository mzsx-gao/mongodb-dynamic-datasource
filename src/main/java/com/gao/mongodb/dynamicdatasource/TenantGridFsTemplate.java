package com.gao.mongodb.dynamicdatasource;

import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;

import java.lang.reflect.Method;

/**
 * 租户fs数据源代理类
 */
public class TenantGridFsTemplate implements MethodInterceptor {

    //跟线程唯一绑定
    private ThreadLocal<GridFsTemplate> threadLocalGridFsTemplate = new ThreadLocal<>();

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
        if (null == template) {
            result = proxy.invokeSuper(obj, args);
        } else {
            result = method.invoke(template, args);
        }
        return result;
    }

    public void setTemplate(GridFsTemplate template) {
        threadLocalGridFsTemplate.set(template);
    }

    public GridFsTemplate getTemplate() {
        return threadLocalGridFsTemplate.get();
    }
}
