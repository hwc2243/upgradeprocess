package com.github.hwc2243.upgrade.persistence.base;




import com.github.hwc2243.upgrade.entity.UpgradeEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface BaseUpgradePersistence<E extends UpgradeEntity, ID> extends JpaRepository<E, ID>
{


    public E findFirstByClassName(String className);

}
