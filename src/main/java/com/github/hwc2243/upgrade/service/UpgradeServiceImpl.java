package com.github.hwc2243.upgrade.service;

import com.github.hwc2243.upgrade.dto.UpgradeDTO;
import com.github.hwc2243.upgrade.entity.UpgradeEntity;
import com.github.hwc2243.upgrade.service.base.BaseUpgradeServiceImpl;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UpgradeServiceImpl
  extends BaseUpgradeServiceImpl<UpgradeDTO, UpgradeEntity,Long>
  implements UpgradeService
{
  
  @Autowired
  protected UpgradeMapper upgradeMapper;
  
  protected UpgradeEntity toEntity (UpgradeDTO dto) {
    return upgradeMapper.toEntity(dto);
  }
  
  protected List<UpgradeEntity> toEntities (List<UpgradeDTO> dtos) {
    return upgradeMapper.toEntities(dtos);
  }

  protected UpgradeDTO toDto (UpgradeEntity entity) {
    return upgradeMapper.toDto(entity);
  }
  
  protected List<UpgradeDTO> toDtos (List<UpgradeEntity> entities) {
    return upgradeMapper.toDtos(entities);
  }
}