package com.github.hwc2243.upgrade.model.base;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public interface BaseUpgrade
 extends Serializable
{ 
  public Long getId ();
  public void setId (Long id);


  public String getClassName ();
  public void setClassName (String className);
  

  public String getVersion ();
  public void setVersion (String version);
  

  public LocalDateTime getExecutionDateTime ();
  public void setExecutionDateTime (LocalDateTime executionDateTime);
  

}
