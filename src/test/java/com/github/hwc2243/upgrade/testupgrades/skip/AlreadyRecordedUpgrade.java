package com.github.hwc2243.upgrade.testupgrades.skip;

import com.github.hwc2243.upgrade.UpgradeAction;

public class AlreadyRecordedUpgrade implements UpgradeAction {
    public static int executions;

    @Override
    public String getVersion() {
        return "1.0.0";
    }

    @Override
    public void execute() {
        executions++;
    }
}
