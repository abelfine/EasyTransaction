package com.yiqiniu.easytrans.core;

import com.yiqiniu.easytrans.context.LogProcessContext;
import com.yiqiniu.easytrans.protocol.EasyTransRequest;

public class RpcCallRequest<P extends EasyTransRequest> {
    private String appId;
    private String busCode;
    private Integer callSeq;
    private String innerMethod;
    private String key;
    private P params;
    private LogProcessContext logContext;

    public RpcCallRequest(String appId, String busCode, Integer callSeq, String innerMethod, String key, P params, LogProcessContext logContext) {
        this.appId = appId;
        this.busCode = busCode;
        this.callSeq = callSeq;
        this.innerMethod = innerMethod;
        this.key = key;
        this.params = params;
        this.logContext = logContext;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getBusCode() {
        return busCode;
    }

    public void setBusCode(String busCode) {
        this.busCode = busCode;
    }

    public Integer getCallSeq() {
        return callSeq;
    }

    public void setCallSeq(Integer callSeq) {
        this.callSeq = callSeq;
    }

    public String getInnerMethod() {
        return innerMethod;
    }

    public void setInnerMethod(String innerMethod) {
        this.innerMethod = innerMethod;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public P getParams() {
        return params;
    }

    public void setParams(P params) {
        this.params = params;
    }

    public LogProcessContext getLogContext() {
        return logContext;
    }

    public void setLogContext(LogProcessContext logContext) {
        this.logContext = logContext;
    }
}
