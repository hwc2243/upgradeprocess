package com.github.hwc2243.upgrade.entity;


import com.github.hwc2243.upgrade.entity.base.BaseUpgradeEntity;
import com.github.hwc2243.upgrade.model.Upgrade;
import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity(name="Upgrade")
@Table(name="upgrade")
public class UpgradeEntity
    extends BaseUpgradeEntity<UpgradeEntity>
    implements Upgrade, Serializable
{
	public UpgradeEntity ()
	{
		super();
	}
	
    // Private constructor to force the use of the Builder
    private UpgradeEntity (Builder builder)
    {
        this.className = builder.className;
        this.version = builder.version;
        this.executionDateTime = builder.executionDateTime;
    }

    public static class Builder {

        private String className = null;
        private String version = null;
        private LocalDateTime executionDateTime = null;

        public Builder className(String className) {
            this.className = className;
            return this;
        }

        public Builder version(String version) {
            this.version = version;
            return this;
        }

        public Builder executionDateTime(LocalDateTime executionDateTime) {
            this.executionDateTime = executionDateTime;
            return this;
        }

        /**
         * The build method creates and returns the immutable Entity object.
         */
        public UpgradeEntity build() {
            return new UpgradeEntity(this);
        }
    }
}