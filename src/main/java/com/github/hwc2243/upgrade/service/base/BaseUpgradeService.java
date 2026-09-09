package com.github.hwc2243.upgrade.service.base;


import com.github.hwc2243.upgrade.dto.base.BaseUpgradeDTO;
import java.time.LocalDateTime;
import java.util.List;

public interface BaseUpgradeService<D extends BaseUpgradeDTO, ID> extends EntityService<D, ID> {

	public D fetchByClassName (String className);
}