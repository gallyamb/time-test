# time-test
This repo contains a handy tooling to enable you to test your time sensitive code. Forget about:
- making a Spring bean from `java.time.Clock` and injecting it everywhere
- manually mocking `java.time.Clock` with some mock library

# Getting started
There are two different approaches to start integration with this library:
1. when you have a lot of legacy code
2. when you just start your project from scratch

In both cases you can just include `org.time.test:time-test-core:0.1.0` and `org.time.test:time-test-mockito:0.1.0` in your tests classpath and write code like
```java
// just fixed the time at specififc moment
NowTest.withMoment(OffsetDateTime.parse("2020-05-12T11:23:55T-04:00"), () -> {
  ServiceTime serviceTime = ... // a schedule when some service is available
  ZoneId zoneId = ... // some time zone where we will do our calculations
  // imagine, that such method determines current time internally via OffsetDateTime.now()
  OffsetDateTime result = timingService.calculateNearestServisedTime(serviceTime, zoneId);

  // cause we expect this time to be within service time
  Assertions.assertEquals(OffsetDateTime.parse("2020-05-22T11:23:55T-04:00"), result);

  Now.tick(Duration.parse("P5D"));

  OffsetDateTime result = timingService.calculateNearestServisedTime(serviceTime, zoneId);

  // cause we expect at this time service is unavailable. And nearest service time was at 19:00 of previous day
  Assertions.assertEquals(OffsetDateTime.parse("2020-05-16T19:00:00T-04:00"), result);
})
```

This approach will work. Even library code will reflect time changes. But of course there will be some limitations: clock changes will be visilbe only within current thread. This is often enough

If you want for some reason test concurrent code, that works with time, you have another option - `org.time:time-core:0.1.0` and `org.time.test:time-test-plain:0.1.0`!

Adding `org.time:time-core:0.1.0` to your production classpath allows you to obtain current time from `Now.offsetDateTime()` and other methods (there are a lot in `org.time.Now`). And adding `org.time.test:time-test-plain:0.1.0` allows you to change time not only within one thread, but all threads at once. Of course, it'll work only if you obtain time instances from `org.time.Now`. But it's not such a big deal, if you want to get testable code
