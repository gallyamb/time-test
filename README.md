# time-test
This repo contains a handy tooling to enable you to test your time sensitive code. Forget about:
- using `Thread.sleep` and wasting you CPU budget for waitings within your tests
- making a Spring bean from `java.time.Clock` and injecting it everywhere
- manually mocking `java.time.Clock` with some mock library (with more verbosity)

Instead you can use developer friendly API for managing time. See [Getting started](https://github.com/gallyamb/time-test/edit/main/README.md#getting-started)

# Getting started
All artifacts use the group `io.github.gallyamb.time`. Replace `<version>` with a
verified published release. The current development version is `0.1.1-SNAPSHOT`;
a local staging publication is not a Maven Central release. See [Publishing](docs/publishing.md).

The easiest way to integrate this library is to include `io.github.gallyamb.time:time-test-core:<version>`
and `io.github.gallyamb.time:time-test-mockito:<version>` in your tests classpath

## Example

```java
// just fixed the time at specififc moment
NowTest.withMoment(OffsetDateTime.parse("2020-05-12T11:23:55-04:00"), () -> {
  ServiceTime serviceTime = ... // a schedule when some service is available
  ZoneId zoneId = ... // some time zone where we will do our calculations
  // imagine, that such method determines current time internally via OffsetDateTime.now()
  OffsetDateTime result = timingService.calculateNearestServisedTime(serviceTime, zoneId);

  // cause we expect this time to be within service time
  Assertions.assertEquals(OffsetDateTime.parse("2020-05-22T11:23:55-04:00"), result);

  NowTest.tick(Duration.parse("P5D"));

  OffsetDateTime result = timingService.calculateNearestServisedTime(serviceTime, zoneId);

  // cause we expect at this time service is unavailable. And nearest service time was at 19:00 of previous day
  Assertions.assertEquals(OffsetDateTime.parse("2020-05-16T19:00:00-04:00"), result);
})
```

# Testing concurrent code

The above approach will work. Even library code will reflect time changes. But of course there will be some limitations:
clock changes will be visilbe only within current thread. This is often enough

If you want for some reason to test concurrent code, that works with time, you have another
option - `io.github.gallyamb.time:time-core:<version>` and `io.github.gallyamb.time:time-test-plain:<version>`!

Adding `io.github.gallyamb.time:time-core:<version>` to your production classpath allows you to obtain current time
from `Now.offsetDateTime()` and other methods (there are a lot in `io.github.gallyamb.time.Now`). And
adding `io.github.gallyamb.time:time-test-plain:<version>` allows you to change time not only within one thread, but
all threads at once. Of course, it'll work only if you obtain time instances from `io.github.gallyamb.time.Now`. But it's not such a
big deal, if you want to get testable code

## Example

```java
// MyService.java
public class MyService {
  public OffsetDateTime getTimeBasedResult() {
    return Now.offsetDateTime();
  }
}

// MyServiceTest.java
public class MyServiceTest {

  @Test
  public void testMyService_Concurrent() {
    var myService = new MyService();

    // just fixed the time at specififc moment
    var time = NowTest.withMoment(OffsetDateTime.parse("2020-05-12T11:23:55-04:00"), () -> {
      var result = new AtomicReference<OffsetDateTime>();
      new Thread(() -> {
        result.set(myService.getTimeBasedResult());
      }).start();
      while (result.get() == null) {
        Thread.sleep(100);
      }
      return result.get();
    }

    Assertions.assertEqual(OffsetDateTime.parse("2020-05-12T11:23:55-04:00"), time)
  }
}
```

# JUnit5 integration

There are JUnit5 integration available. With this you can write more obviuous tests. Just
include `io.github.gallyamb.time:time-test-junit5-integration:<version>`

## Example

```java
// will fix the time at the test start moment for every test
@FixedTime
public class MyTest {

  @Test
  public void testOffsetDateTimesDifference() {
    var start = OffsetDateTime.now();
    var end = OffsetDateTime.now().plusHours(5);

    // This will never become flaky, because time is fixed
    Assertions.assertEquals(Duration.parse("PT5H"), Duration.between(start, end));
  }

  @Test
  @FixedTime("2020-05-12T11:23:55-04:00")
  public void testAtSpecificTime() {
    // you can write test assuming that time is always fixed at specific moment
  }
}
```

## Clock lifecycle integrations

`Now.clock()` exposes the active clock for APIs that accept `java.time.Clock`.
`NowTest.withClock(clock, job)` temporarily uses an arbitrary clock (including a
running clock with a negative offset) and restores the previous state on exit.
For separate setup/teardown callbacks, retain the result of
`NowTest.useClock(clock)` and close it in teardown. Close nested scopes in reverse
order. A scope's `close()` is idempotent. Obtain `Now.clock()` again after changing
the clock; previously returned clocks do not track future replacements.

The plain implementation changes time globally, so independent tests which
mutate time must run sequentially. Both JPMS and ordinary classpath service loading
are tested by `./gradlew clean build`.
