package ru.oculus.database.service.tablespace;

import java.math.BigDecimal;

public class TablespaceInfo {
    private String name;
    private BigDecimal totalSpace;
    private BigDecimal freeSpace;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getTotalSpace() {
        return totalSpace;
    }

    public void setTotalSpace(BigDecimal totalSpace) {
        this.totalSpace = totalSpace;
    }

    public BigDecimal getFreeSpace() {
        return freeSpace;
    }

    public void setFreeSpace(BigDecimal freeSpace) {
        this.freeSpace = freeSpace;
    }

}
