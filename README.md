# 介绍
> link-trace-starter 实现简易的线程、Spring Cloud 分布式链路跟踪，日志输出traceId

# 支持
- [x] 异步/多线程traceId链路跟踪
- [x] Feign traceId链路跟踪
- [x] Feign开启Hystrix traceId链路跟踪
- [x] HttpClient traceId链路跟踪
- [x] OKHttp traceId链路跟踪
- [x] RocketMQ traceId链路跟踪
- [x] RabbitMQ traceId链路跟踪
- [x] 定时任务 traceId链路跟踪
- [x] 基于注解 traceId链路跟踪（在方法上添加@TraceIdAspect注解即可）

# 使用
- 引入依赖
```
<dependency>
    <groupId>com.gitee.osinn.framework</groupId>
    <artifactId>link-trace-starter</artifactId>
    <version>1.0</version>
</dependency>
# RocketMQ 测试版本
<dependency>
    <groupId>org.apache.rocketmq</groupId>
    <artifactId>rocketmq-spring-boot-starter</artifactId>
    <version>2.2.1</version>
</dependency>
# RabbitMQ 测试版本
<dependency>
<groupId>org.springframework.boot</groupId>
<artifactId>spring-boot-starter-amqp</artifactId>
<version>2.4.2</version>
</dependency>
```
# 配置
```
trace:
  # 开启自动配置
  enabled: true
  task-execution:
    pool:
      # 以下参数是否Spring 线程池配置参数
      queue-capacity: 1000
      keep-alive-seconds: 300
      max-pool-size: 200
      core-pool-size: 50
  scheduled:
    # 开启定时任务traceId链路跟踪
    enabled: true
  # Spring cloud Feign、Hystrix链路跟踪自动配置
  cloud:
    enabled-feign: true # 开启Feign，默认关闭，如果开启需要自行引入Feign相关依赖
    enabled-hystrix: true # 开启Hystrix，默认关闭，如果开启需要自行引入Hystrix相关依赖
  # 开启RocketMQ链路跟踪
  enabled-rocket-mq: true
  # 开启RabbitMQ链路跟踪
  enabled-rabbitmq: true
  # 表示注入HandlerTraceIdAspect，之后可以在方法上使用@HandlerTraceIdAspect注解实现traceId链路跟踪
  injection-aop: true
```
# 异步/多线程支持
- 启动类上添加`@EnabledExecutor`注解，默认已启用异步，可配置线程池参数如下

```
trace:
  task-execution:
    pool:
      # 以下参数是否Spring 线程池配置参数
      queue-capacity: 1000
      keep-alive-seconds: 300
      max-pool-size: 200
      core-pool-size: 50
```
# 定时任务 traceId跟踪
- 配置

```
trace:
  scheduled:
    # 开启定时任务traceId跟踪
    enabled: true
```

- 如果不开启内置的线程池配置，则需要自行创建连接池
- 关键对象`Executor`、`TaskScheduler`
- 参考以下代码
```
@Bean
public Executor taskExecutor(TraceTaskExecutionProperties traceTaskExecutionProperties) {
    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    executor.setMaxPoolSize(traceTaskExecutionProperties.getMaxPoolSize());
    executor.setCorePoolSize(traceTaskExecutionProperties.getCorePoolSize());
    executor.setQueueCapacity(traceTaskExecutionProperties.getQueueCapacity());
    executor.setKeepAliveSeconds(traceTaskExecutionProperties.getKeepAliveSeconds());
    executor.setAllowCoreThreadTimeOut(traceTaskExecutionProperties.isAllowCoreThreadTimeout());
    // 关键
    executor.setTaskDecorator(new MdcTaskDecorator());
    executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
    executor.setThreadNamePrefix(traceTaskExecutionProperties.getThreadNamePrefix());
    executor.setWaitForTasksToCompleteOnShutdown(true);
    executor.initialize();
    return executor;
}

@Bean
public TaskScheduler taskScheduler(TraceTaskExecutionProperties traceTaskExecutionProperties) {
    // 必须创建MdcThreadPoolTaskScheduler对象
    MdcThreadPoolTaskScheduler threadPoolTaskScheduler = new MdcThreadPoolTaskScheduler();
    threadPoolTaskScheduler.setPoolSize(traceTaskExecutionProperties.getCorePoolSize());
    threadPoolTaskScheduler.setThreadNamePrefix("my-ThreadPoolTaskScheduler");
    // 错误处理
    threadPoolTaskScheduler.setErrorHandler(throwable -> {
        log.error(throwable.getMessage(), throwable);
        ThreadMdcUtil.clear();
    });
    return threadPoolTaskScheduler;
}
```

# HttpClient例子
- 首先自己创建一个 httpClient, 然后将`HttpClientTraceIdInterceptor`拦截器塞到自己创建的`httpClient`中
- `OKHttp`亦是如此

```
public class HttpClientUtil {
    private static final CloseableHttpClient HTTP_CLIENT = HttpClientBuilder.create()
            .addInterceptorFirst(new HttpClientTraceIdInterceptor()) // 关键设置拦截器
            .build();

    public static String doGet(String url) {
        HttpGet httpGet = new HttpGet(url);
        CloseableHttpResponse response = null;
        try {
            response = HTTP_CLIENT.execute(httpGet);
            HttpEntity resEntity = response.getEntity();
            return EntityUtils.toString(resEntity);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
```
# OKHttp例子
```
public class OkHttpUtil {

    private static final OkHttpClient OK_HTTP_CLIENT = new OkHttpClient().newBuilder()
            .addNetworkInterceptor(new OkHttpTraceIdInterceptor()) // 关键设置拦截器
            .build();

    public static String doGet(String url) {
        String result = null;
        Request request = new Request.Builder().url(url).build();
        try {
            Response response = OK_HTTP_CLIENT.newCall(request).execute();
            result = Objects.requireNonNull(response.body()).string();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
```
# 自定义`traceId`值
- 继承`ITraceService`接口，并注入实现bean，示例如下

```
@Service
public class TraceServiceImpl implements ITraceService {

    @Override
    public String generateTraceId() {
        String traceId = "自定义traceId值";
        return traceId;
    }
}
```
# FAQ
- RocketMQ 发送消息回调方式发送队列消息会丢失traceId,原因是发送消息时获取不到traceId值,解决方案如下
```
// 需要发送的消息
Message<?> msg = MessageBuilder.withPayload("测试链路跟踪")
        // 手动往消息头中添加traceId
        .setHeader(TraceConstant.TRACE_ID_MDC_FIELD, ThreadMdcUtil.getTraceId())
        .build();
// 发送消息
rocketMQTemplate.asyncSend("springboot-topic", msg, new SendCallback() {
    @Override
    public void onSuccess(SendResult sendResult) {
        //发送成功
    }
    @Override
    public void onException(Throwable throwable) {
        //发送失败
    }
});
```