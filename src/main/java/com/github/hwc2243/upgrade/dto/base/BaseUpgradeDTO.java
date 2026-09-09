package com.github.hwc2243.upgrade.dto.base;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.github.hwc2243.upgrade.dto.UpgradeDTO;
import com.github.hwc2243.upgrade.model.base.BaseUpgrade;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public abstract class BaseUpgradeDTO
implements BaseUpgrade,  Serializable
{
  protected Long id = null;

  protected String className = null;
  
  protected String version = null;
  
@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
  protected LocalDateTime executionDateTime = null;
  

  protected BaseUpgradeDTO () {
  }
  
  // Private constructor to force the use of the Builder
  protected BaseUpgradeDTO (Builder builder)
  {
    this.id = builder.id;
    this.className = builder.className;
    this.version = builder.version;
    this.executionDateTime = builder.executionDateTime;
  }

  public Long getId ()
  {
    return this.id;
  }
  
  public void setId (Long id)
  {
    this.id = id;
  }


  public String getClassName ()
  {
    return this.className;
  }
  
  public void setClassName (String className)
  {
    this.className = className;
  }
  

  public String getVersion ()
  {
    return this.version;
  }
  
  public void setVersion (String version)
  {
    this.version = version;
  }
  

  public LocalDateTime getExecutionDateTime ()
  {
    return this.executionDateTime;
  }
  
  public void setExecutionDateTime (LocalDateTime executionDateTime)
  {
    this.executionDateTime = executionDateTime;
  }
  


    @Override
	public int hashCode() {
		return Objects.hash(id);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
			
		BaseUpgradeDTO other = (BaseUpgradeDTO) obj;
		return id == other.id;
	}

  public abstract static class Builder {

  private Long id = null;

  private String className = null;
  
  private String version = null;
  
  private LocalDateTime executionDateTime = null;
  


    public Builder id(Long id) {
      this.id = id;
      return this;
    }
    
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
    public abstract UpgradeDTO build();
  }
}