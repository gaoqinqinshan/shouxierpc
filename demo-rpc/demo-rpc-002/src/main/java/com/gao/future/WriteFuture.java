package com.gao.future;

import com.gao.msg.Response;

import java.util.concurrent.Future;

public interface WriteFuture<T> extends Future<T> {

    // 返回写操作失败的原因，即抛出的异常
    Throwable cause();
    // 设置写操作失败的原因
    void setCause(Throwable cause);
    // 检查写操作是否成功完成
    boolean isWriteSuccess();
    // 设置写操作是否成功完成
    void setWriteResult(boolean result);
    // 获取请求ID
    String requestId();
    // 设置请求ID
    T response();
    // 设置写操作的结果
    void setResponse(Response response);
    // 检查写操作是否超时
    boolean isTimeout();
}
