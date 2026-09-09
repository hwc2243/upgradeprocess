package com.github.hwc2243.upgrade.testupgrades.order;

import com.github.hwc2243.upgrade.UpgradeAction;

public class FirstUpgrade implements UpgradeAction {
    public static int executionOrder;

    @Override
    public String getVersion() {
        return "1.0.0";
    }

    @Override
    public void execute() {
        executionOrder = 1;
    }
}
