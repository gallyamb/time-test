module org.time.core {
    uses org.time.ClockHandler;
    uses org.time.ClockStateListener;

    exports org.time.internal to org.time.test.spring, org.time.test.core;
    exports org.time;
}
