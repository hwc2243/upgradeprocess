package com.github.hwc2243.upgrade.testupgrades.outoforder;

import com.github.hwc2243.upgrade.UpgradeAction;

public class MissingOlderUpgrade implements UpgradeAction {
    @Override
    public String getVersion() {
        return "1.0.0";
    }

    @Override
    public void execute() {
        throw new AssertionError("An out-of-order upgrade must not execute");
    }
}
