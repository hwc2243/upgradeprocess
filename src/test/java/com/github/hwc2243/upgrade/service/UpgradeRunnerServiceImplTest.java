package com.github.hwc2243.upgrade.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.github.hwc2243.upgrade.dto.UpgradeDTO;
import com.github.hwc2243.upgrade.testupgrades.failure.FailingUpgrade;
import com.github.hwc2243.upgrade.testupgrades.order.FirstUpgrade;
import com.github.hwc2243.upgrade.testupgrades.order.SecondUpgrade;
import com.github.hwc2243.upgrade.testupgrades.skip.AlreadyRecordedUpgrade;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

class UpgradeRunnerServiceImplTest {

    private final RecordingUpgradeService upgradeService = new RecordingUpgradeService();
    private UpgradeRunnerServiceImpl runner;

    @BeforeEach
    void setUp() {
        upgradeService.executed.clear();
        runner = new UpgradeRunnerServiceImpl();
        ReflectionTestUtils.setField(runner, "upgradeService", upgradeService);
        FirstUpgrade.executionOrder = 0;
        SecondUpgrade.executionOrder = 0;
        AlreadyRecordedUpgrade.executions = 0;
        FailingUpgrade.executions = 0;
    }

    @Test
    void rejectsMissingPackageToScanDuringInitialization() {
        assertThatThrownBy(() -> runner.validateConfiguration())
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("The required property 'upgrade.package-to-scan' must be configured.");
    }

    @Test
    void rejectsBlankPackageToScanDuringInitialization() {
        ReflectionTestUtils.setField(runner, "packageToScan", "  ");

        assertThatThrownBy(() -> runner.validateConfiguration())
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("The required property 'upgrade.package-to-scan' must be configured.");
    }

    @Test
    void executesDiscoveredUpgradesInVersionOrderAndRecordsEachAfterSuccess() throws Exception {
        ReflectionTestUtils.setField(runner, "packageToScan", "com.github.hwc2243.upgrade.testupgrades.order");

        runner.doUpgrade();

        assertThat(FirstUpgrade.executionOrder).isEqualTo(1);
        assertThat(SecondUpgrade.executionOrder).isEqualTo(2);
        assertThat(upgradeService.executed)
                .extracting(UpgradeDTO::getClassName, UpgradeDTO::getVersion)
                .containsExactly(
                        org.assertj.core.groups.Tuple.tuple("FirstUpgrade", "1.0.0"),
                        org.assertj.core.groups.Tuple.tuple("SecondUpgrade", "2.0.0"));
        assertThat(upgradeService.executed).allSatisfy(upgrade ->
                assertThat(upgrade.getExecutionDateTime()).isBeforeOrEqualTo(LocalDateTime.now()));
    }

    @Test
    void recordsAnUpgradeOnceAndSkipsItOnTheNextScan() throws Exception {
        ReflectionTestUtils.setField(runner, "packageToScan", "com.github.hwc2243.upgrade.testupgrades.skip");

        runner.doUpgrade();
        runner.doUpgrade();

        assertThat(AlreadyRecordedUpgrade.executions).isEqualTo(1);
        assertThat(upgradeService.executed).hasSize(1);
    }

    @Test
    void rejectsAnUnrecordedUpgradeOlderThanAnExecutedVersion() {
        upgradeService.executed.add(recorded("NewerUpgrade", "2.0.0"));
        ReflectionTestUtils.setField(runner, "packageToScan", "com.github.hwc2243.upgrade.testupgrades.outoforder");

        assertThatThrownBy(() -> runner.doUpgrade())
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Out-of-order upgrade detected")
                .hasMessageContaining("MissingOlderUpgrade")
                .hasMessageContaining("v2.0.0");

        assertThat(upgradeService.executed).hasSize(1);
    }

    @Test
    void doesNotRecordAnUpgradeWhenExecutionFails() {
        ReflectionTestUtils.setField(runner, "packageToScan", "com.github.hwc2243.upgrade.testupgrades.failure");

        assertThatThrownBy(() -> runner.doUpgrade())
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("expected test failure");

        assertThat(FailingUpgrade.executions).isEqualTo(1);
        assertThat(upgradeService.executed).isEmpty();
    }

    private UpgradeDTO recorded(String className, String version) {
        UpgradeDTO upgrade = new UpgradeDTO();
        upgrade.setClassName(className);
        upgrade.setVersion(version);
        return upgrade;
    }

    private static final class RecordingUpgradeService implements UpgradeService {
        private final List<UpgradeDTO> executed = new ArrayList<>();

        @Override
        public UpgradeDTO create(UpgradeDTO upgrade) {
            executed.add(upgrade);
            return upgrade;
        }

        @Override
        public List<UpgradeDTO> findAll() {
            return List.copyOf(executed);
        }

        @Override
        public UpgradeDTO fetchByClassName(String className) {
            return executed.stream()
                    .filter(upgrade -> className.equals(upgrade.getClassName()))
                    .findFirst()
                    .orElse(null);
        }

        @Override public void delete(Long id) { }
        @Override public UpgradeDTO get(Long id) { return null; }
        @Override public UpgradeDTO update(UpgradeDTO upgrade) { return upgrade; }
    }
}
