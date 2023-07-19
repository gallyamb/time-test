module org.time.test.plain.test {
    requires org.time.test.core;
    requires org.time.test.plain;
    requires org.time.test.core.test;

    exports org.time.test.plain.test;

    uses org.time.ClockHandler;
}