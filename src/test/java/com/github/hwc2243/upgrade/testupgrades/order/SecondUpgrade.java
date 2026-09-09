package com.github.hwc2243.upgrade.testupgrades.order;

import com.github.hwc2243.upgrade.UpgradeAction;

public class SecondUpgrade implements UpgradeAction {
    public static int executionOrder;

    @Override
    public String getVersion() {
        return "2.0.0";
    }

    @Override
    public void execute() {
        executionOrder = FirstUpgrade.executionOrder + 1;
    }
}
