package com.github.hwc2243.upgrade.service;



import com.github.hwc2243.upgrade.dto.UpgradeDTO;
import com.github.hwc2243.upgrade.entity.UpgradeEntity;
import java.util.List;
import java.util.Set;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface UpgradeMapper {

  @Named("upgradeDefault")
  UpgradeDTO toDto(UpgradeEntity entity);

  @Named("upgradeShallow")
  UpgradeDTO toDtoShallow(UpgradeEntity entity);



  UpgradeEntity toEntity(UpgradeDTO dto);

  @IterableMapping(qualifiedByName = "upgradeDefault")
  List<UpgradeDTO> toDtos(List<UpgradeEntity> entities);

  @IterableMapping(qualifiedByName = "upgradeShallow")
  List<UpgradeDTO> toDtosShallow(List<UpgradeEntity> entities);

  List<UpgradeEntity> toEntities(List<UpgradeDTO> dtos);
}