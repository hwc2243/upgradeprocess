package com.github.hwc2243.upgrade.service.base;

import com.github.hwc2243.upgrade.dto.base.BaseUpgradeDTO;
import com.github.hwc2243.upgrade.dto.UpgradeDTO;
import com.github.hwc2243.upgrade.entity.base.BaseUpgradeEntity;
import com.github.hwc2243.upgrade.entity.UpgradeEntity;
import com.github.hwc2243.upgrade.persistence.base.BaseUpgradePersistence;
import com.github.hwc2243.upgrade.persistence.UpgradePersistence;
import com.github.hwc2243.upgrade.service.ServiceException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class BaseUpgradeServiceImpl<D extends UpgradeDTO, E extends UpgradeEntity, ID>
  implements BaseUpgradeService<D, ID> {

  @Autowired
  private BaseUpgradePersistence<E, ID> baseUpgradePersistence;
  
  @Autowired
  protected UpgradePersistence upgradePersistence;

    @Override
  public D create (D dto) throws ServiceException
  {
    E entity = toEntity(dto);
    E saved = baseUpgradePersistence.save(entity);
    return toDto(saved);
  }
  
  @Override
  public void delete (ID id) throws ServiceException
  {
    baseUpgradePersistence.deleteById(id);
  }
  
  @Override
  public List<D> findAll () throws ServiceException
  {
    List<E> entities = baseUpgradePersistence.findAll();
    return toDtos(entities);
  }

  @Override
  public D fetchByClassName (String className)
  {
	return toDto(baseUpgradePersistence.findFirstByClassName(className));
  }

  @Override
  public D get (ID id) throws ServiceException
  {
    Optional<E> optional = baseUpgradePersistence.findById(id);

    return optional.isEmpty() ? null : toDto(optional.get());
  }
  
  @Override
  public D update (D dto) throws ServiceException
  {
    E entity = toEntity(dto);
    E saved = baseUpgradePersistence.save(entity);
    return toDto(saved);
  }
  
  protected abstract E toEntity (D dto);
  protected abstract List<E> toEntities (List<D> dtos);

  protected abstract D toDto (E entity);
  protected abstract List<D> toDtos (List<E> entities);
}
