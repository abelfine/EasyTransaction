package com.yiqiniu.easytrans.core;

import com.yiqiniu.easytrans.executor.EasyTransExecutor;
import com.yiqiniu.easytrans.protocol.EasyTransRequest;
import com.yiqiniu.easytrans.util.ReflectUtil;
import org.springframework.context.ApplicationContext;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class ExecutorHelper {
    private ConcurrentHashMap<Class<?>,EasyTransExecutor> mapExecutors = new ConcurrentHashMap<Class<?>, EasyTransExecutor>();
    private ApplicationContext ctx;

    public ExecutorHelper(ApplicationContext ctx) {
        super();
        this.ctx = ctx;
    }

    public EasyTransExecutor getExecutor(@SuppressWarnings("rawtypes") Class<? extends EasyTransRequest> clazz){

        EasyTransExecutor easyTransExecutor = mapExecutors.get(clazz);
        if(easyTransExecutor == null){
            Class<?> executorClazz = findEasyTransExecutor(clazz);
            easyTransExecutor = (EasyTransExecutor) ctx.getBean(executorClazz);
            mapExecutors.put(clazz,easyTransExecutor);
        }
        return easyTransExecutor;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private Class<?> findEasyTransExecutor(Class clazz) {
        List<Class<?>> typeArguments = ReflectUtil.getTypeArguments(EasyTransRequest.class, clazz);

        if(typeArguments != null && typeArguments.size() >= 2){
            return (Class<?>) typeArguments.get(1);
        }else{
            return null;
        }
    }
}
