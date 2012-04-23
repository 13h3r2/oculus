package ru.oculus.database.service.scheme;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class SchemaInfo {
    private String name;

    /**
     * in GB
     */
    private BigDecimal size;

    private int connectionCount;

    private String lastPatch;

    private Boolean isImport = null;

    public String getLastPatch() {
        return lastPatch;
    }

    public void setLastPatch(String lastPatch) {
        this.lastPatch = lastPatch;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getSize() {
        return size;
    }

    public void setSize(BigDecimal size) {
        this.size = size;
    }

    public int getConnectionCount() {
        return connectionCount;
    }

    public void setConnectionCount(int connectionCount) {
        this.connectionCount = connectionCount;
    }

    public Boolean getImport() {
        return isImport;
    }

    public void setImport(Boolean anImport) {
        isImport = anImport;
    }
}
