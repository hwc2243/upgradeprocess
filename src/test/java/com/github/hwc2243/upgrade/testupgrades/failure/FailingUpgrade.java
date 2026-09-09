package com.github.hwc2243.upgrade.testupgrades.failure;

import com.github.hwc2243.upgrade.UpgradeAction;

public class FailingUpgrade implements UpgradeAction {
    public static int executions;

    @Override
    public String getVersion() {
        return "1.0.0";
    }

    @Override
    public void execute() {
        executions++;
        throw new IllegalStateException("expected test failure");
    }
}
