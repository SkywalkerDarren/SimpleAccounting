package io.github.skywalkerdarren.simpleaccounting.util;

import java.util.concurrent.Executor;

public class SingleExecutors extends AppExecutors {
    private static Executor instant = Runnable::run;

    public SingleExecutors() {
        super(instant, instant, instant);
    }
}
