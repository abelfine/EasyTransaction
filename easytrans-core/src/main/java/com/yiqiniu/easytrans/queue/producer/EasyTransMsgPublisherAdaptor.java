package com.yiqiniu.easytrans.queue.producer;

import com.yiqiniu.easytrans.protocol.EasyTransRequest;
import com.yiqiniu.easytrans.rpc.EasyTransRpcConsumer;
import com.yiqiniu.easytrans.serialization.ObjectSerializer;

import java.io.Serializable;
import java.util.Map;

public class EasyTransMsgPublisherAdaptor implements EasyTransRpcConsumer {
    private EasyTransMsgPublisher publisher;
    private ObjectSerializer serializer;

    public EasyTransMsgPublisherAdaptor(EasyTransMsgPublisher publisher, ObjectSerializer serializer) {
        this.publisher = publisher;
        this.serializer = serializer;
    }

    @Override
    public <P extends EasyTransRequest<R, ?>, R extends Serializable> R call(String appId, String busCode, String innerMethod, Map<String, Object> header, P params) {
        return (R) publisher.publish(appId, busCode, innerMethod, header, serializer.serialization(params));
    }

    @Override
    public <P extends EasyTransRequest<R, ?>, R extends Serializable> void callWithNoReturn(String appId, String busCode, String innerMethod, Map<String, Object> header, P params) {
        throw new UnsupportedOperationException();
    }
}
