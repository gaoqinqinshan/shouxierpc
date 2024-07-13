package com.gao;

public class ConsumerConfig<T> {
    private String nozzle; //接口
    private String alias;  //别名

    // 动态代理(synchronized 关键字确保了方法在同一时间只能被一个线程访问)
    protected synchronized T refer() {
        System.out.format("消费者信息 => [接口：%s, 别名：%s] ", nozzle, alias);
        return null;
    }
}