package com.gao.msg;

/**
 * 请求
 */
public class Request {
    // 请求id
    private String requestId;
    // 返回结果
    private Object result;

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }
}
