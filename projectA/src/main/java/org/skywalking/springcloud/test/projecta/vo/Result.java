package org.skywalking.springcloud.test.projecta.vo;

public class Result {
    private String traceId;

    public Result(String traceId) {
        this.traceId = traceId;
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }
}
