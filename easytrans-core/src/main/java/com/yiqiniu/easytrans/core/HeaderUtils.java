package com.yiqiniu.easytrans.core;

import com.yiqiniu.easytrans.context.LogProcessContext;

import java.util.HashMap;
import java.util.Map;

public class HeaderUtils {
    public static Map<String, Object> initEasyTransRequestHeader(Integer callSeq, LogProcessContext logContext) {
        HashMap<String, Object> header = new HashMap<String, Object>();
        header.put(EasytransConstant.CallHeadKeys.PARENT_TRX_ID_KEY, logContext.getTransactionId());
        header.put(EasytransConstant.CallHeadKeys.CALL_SEQ, callSeq);
        return header;
    }
}
