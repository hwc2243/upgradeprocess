package com.github.hwc2243.upgrade.service;

import java.lang.reflect.Modifier;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AssignableTypeFilter;
import org.springframework.stereotype.Service;

import com.github.hwc2243.upgrade.UpgradeAction;
import com.github.hwc2243.upgrade.dto.UpgradeDTO;

@Service
public class UpgradeRunnerServiceImpl implements UpgradeRunnerService {
	private static final Logger log = LoggerFactory.getLogger(UpgradeRunnerServiceImpl.class);

    @Value("${upgrade.package-to-scan:#{null}}")
    private String packageToScan;
	
    @Autowired
    protected UpgradeService upgradeService;
    
	@Override
	public void doUpgrade() throws Exception {
	log.info("Starting database upgrade scan in package: {}", packageToScan);

    List<UpgradeAction> availableUpgrades = scanForUpgradeActions(packageToScan);
    
    // Sort upgrades by Semantic Version ascending
    availableUpgrades.sort(Comparator.comparing(
        UpgradeAction::getVersion, 
        this::compareVersions
    ));

    // Determine highest version previously recorded in the system
    String highestExecutedVersion = getHighestExecutedVersion();

    for (UpgradeAction upgrade : availableUpgrades) {
        String name = upgrade.getClass().getSimpleName();
        String version = upgrade.getVersion();

        UpgradeDTO existingExecution = upgradeService.fetchByClassName(name);

        if (existingExecution != null) {
            log.debug("Skipping already executed upgrade: {} (v{})", name, version);
            continue;
        }

        // Check if this unexecuted upgrade has a lower version than what has already been processed
        if (highestExecutedVersion != null && compareVersions(version, highestExecutedVersion) < 0) {
            throw new IllegalStateException(String.format(
                "Out-of-order upgrade detected! Upgrade '%s' (v%s) has not been run, " +
                "but a higher version (v%s) was already executed.",
                name, version, highestExecutedVersion
            ));
        }

        log.info("Executing upgrade: {} (v{})", name, version);
        
        try {
            upgrade.execute();
            
            // Record execution state
            UpgradeDTO newUpgrade = new UpgradeDTO();
            newUpgrade.setClassName(name);
            newUpgrade.setVersion(version);
            newUpgrade.setExecutionDateTime(LocalDateTime.now());
            
            UpgradeDTO update = upgradeService.create(newUpgrade);
            highestExecutedVersion = version;
            log.info("Successfully completed upgrade: {} (v{})", name, version);
        } catch (Exception e) {
            log.error("Failed executing upgrade: {} (v{})", name, version, e);
            throw e; // Abort startup on failure
        }
    }
}

private List<UpgradeAction> scanForUpgradeActions(String basePackage) throws Exception {
    List<UpgradeAction> actions = new ArrayList<>();
    
    ClassPathScanningCandidateComponentProvider scanner =
            new ClassPathScanningCandidateComponentProvider(false);
    scanner.addIncludeFilter(new AssignableTypeFilter(UpgradeAction.class));

    for (BeanDefinition bd : scanner.findCandidateComponents(basePackage)) {
        Class<?> clazz = Class.forName(bd.getBeanClassName());
        if (!clazz.isInterface() && !Modifier.isAbstract(clazz.getModifiers())) {
            UpgradeAction action = (UpgradeAction) clazz.getDeclaredConstructor().newInstance();
            actions.add(action);
        }
    }
    return actions;
}

private String getHighestExecutedVersion() throws ServiceException {
    return upgradeService.findAll().stream()
            .map(UpgradeDTO::getVersion)
            .max(this::compareVersions)
            .orElse(null);
}

/**
 * Standard Semantic Versioning comparison algorithm.
 */
private int compareVersions(String v1, String v2) {
    String[] parts1 = v1.split("\\.");
    String[] parts2 = v2.split("\\.");
    int length = Math.max(parts1.length, parts2.length);

    for (int i = 0; i < length; i++) {
        int p1 = i < parts1.length ? Integer.parseInt(parts1[i].replaceAll("\\D+", "")) : 0;
        int p2 = i < parts2.length ? Integer.parseInt(parts2[i].replaceAll("\\D+", "")) : 0;
        if (p1 != p2) {
            return Integer.compare(p1, p2);
        }
    }
    return 0;
}
}
