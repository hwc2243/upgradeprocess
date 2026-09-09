package com.github.hwc2243.upgrade.dto;

import com.github.hwc2243.upgrade.dto.base.BaseUpgradeDTO;

public class UpgradeDTO extends BaseUpgradeDTO
{
  private static final long serialVersionUID = 1L;

  public UpgradeDTO () {
  }
  
  public UpgradeDTO (Builder builder) {
    super(builder);
  }
  
  public static class Builder extends BaseUpgradeDTO.Builder {
    public UpgradeDTO build() {
      return new UpgradeDTO(this);
    }
  }
}