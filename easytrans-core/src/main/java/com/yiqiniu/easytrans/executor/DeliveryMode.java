package com.yiqiniu.easytrans.executor;

public @interface DeliveryMode {
    ServiceType value() default ServiceType.RPC;
}
