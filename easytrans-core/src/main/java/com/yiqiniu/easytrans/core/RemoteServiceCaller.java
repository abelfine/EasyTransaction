package com.yiqiniu.easytrans.core;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.yiqiniu.easytrans.context.LogProcessContext;
import com.yiqiniu.easytrans.executor.DeliveryMode;
import com.yiqiniu.easytrans.executor.EasyTransExecutor;
import com.yiqiniu.easytrans.executor.ServiceType;
import com.yiqiniu.easytrans.protocol.EasyTransRequest;
import com.yiqiniu.easytrans.queue.producer.EasyTransMsgPublishResult;
import com.yiqiniu.easytrans.queue.producer.EasyTransMsgPublisher;
import com.yiqiniu.easytrans.queue.producer.EasyTransMsgPublisherAdaptor;
import com.yiqiniu.easytrans.rpc.EasyTransRpcConsumer;
import com.yiqiniu.easytrans.serialization.ObjectSerializer;

public class RemoteServiceCaller {

    private EasyTransRpcConsumer consumer;
    private ExecutorHelper helper;
    private ObjectSerializer serializer;

    private ConcurrentMap<Class, ServiceType> typeMap = new ConcurrentHashMap<Class, ServiceType>();

    public RemoteServiceCaller(EasyTransRpcConsumer consumer, EasyTransMsgPublisher publisher, ExecutorHelper helper,
                               ObjectSerializer serializer) {
        super();
        this.consumer = new EasyTransConsumerExecutor(consumer, new EasyTransMsgPublisherAdaptor(publisher,serializer));
        this.helper = helper;
    }

    public <P extends EasyTransRequest<R, ?>, R extends Serializable> R call(String appId, String busCode, Integer callSeq, String innerMethod, P params, LogProcessContext logContext) {
        return consumer.call(appId, busCode, innerMethod, HeaderUtils.initEasyTransRequestHeader(callSeq, logContext), params);
    }

    public <P extends EasyTransRequest<R, ?>, R extends Serializable> void callWithNoReturn(String appId, String busCode, Integer callSeq, String innerMethod, P params, LogProcessContext logContext) {
        consumer.callWithNoReturn(appId, busCode, innerMethod, HeaderUtils.initEasyTransRequestHeader(callSeq, logContext), params);
    }


    /*public EasyTransMsgPublishResult publish(String topic, String tag, Integer callSeq, String key, EasyTransRequest<?, ?> request, LogProcessContext logContext) {
        return publisher.publish(topic, tag, key, initEasyTransRequestHeader(callSeq, logContext), serializer.serialization(request));
    }*/


    private class EasyTransConsumerExecutor implements EasyTransRpcConsumer{
        private EasyTransRpcConsumer consumer;
        private EasyTransRpcConsumer publisher;
        private ConcurrentMap<Class, ServiceType> type = new ConcurrentHashMap<Class, ServiceType>();

        public EasyTransConsumerExecutor(EasyTransRpcConsumer consumer, EasyTransRpcConsumer publisher) {
            this.consumer = consumer;
            this.publisher = publisher;
        }

        @Override
        public <P extends EasyTransRequest<R, ?>, R extends Serializable> R call(String appId, String busCode, String innerMethod, Map<String, Object> header, P params) {
            switch (get(params.getClass())){
                case RPC:
                    return consumer.call(appId,busCode,innerMethod,header,params);
                case MESSAGE:
                    return publisher.call(appId,busCode,innerMethod,header,params);
            }
            return null;
        }

        @Override
        public <P extends EasyTransRequest<R, ?>, R extends Serializable> void callWithNoReturn(String appId, String busCode, String innerMethod, Map<String, Object> header, P params) {
            switch (get(params.getClass())){
                case RPC:
                    consumer.call(appId,busCode,innerMethod,header,params);
                    break;
                case MESSAGE:
                    publisher.call(appId,busCode,innerMethod,header,params);
                    break;
            }
        }

        private ServiceType get(Class<? extends EasyTransRequest> clazz) {
            ServiceType serviceType = type.get(clazz);
            if (serviceType == null) {
                synchronized (this) {
                    serviceType = type.get(clazz);
                    if (serviceType == null) {
                        EasyTransExecutor executor = helper.getExecutor(clazz);
                        assert executor != null;
                        DeliveryMode mode = executor.getClass().getAnnotation(DeliveryMode.class);
                        if (mode == null) {
                            type.putIfAbsent(clazz, ServiceType.RPC);
                        } else {
                            type.putIfAbsent(clazz, mode.value());
                        }
                        return type.get(clazz);
                    } else {
                        return serviceType;
                    }
                }
            }
            return serviceType;
        }
    }
}
