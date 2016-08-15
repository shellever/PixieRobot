package com.shellever.pixierobot.bean;

/**
 * Created by linuxfor on 8/14/2016.
 */
public class CommonException extends RuntimeException {
    public CommonException() {
        super();
    }

    public CommonException(String detailMessage) {
        super(detailMessage);
    }

    public CommonException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public CommonException(Throwable throwable) {
        super(throwable);
    }
}
