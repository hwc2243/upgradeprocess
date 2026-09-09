package com.github.hwc2243.upgrade;

public interface UpgradeAction {

    /**
     * Version string following Semantic Versioning (e.g., "1.0.0").
     */
    String getVersion();

    /**
     * Executes the upgrade logic within a transactional boundary.
     */
    void execute() throws Exception;
}
