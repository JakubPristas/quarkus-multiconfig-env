package org.jpristas.thesis.quarkus.multiconfig.deployment.propdoc.impl.writer;

//import org.apache.commons.lang.StringUtils;

public class ProdProperty {
    private String originalName;
    private String name;
    private String originalValue;
    private String prodValue;
    private String description;
    private boolean required;

    public ProdProperty(String originalName, String originalValue, String prodValue,
                        String description, boolean required) {
        this.originalName = originalName;
        this.name = originalName.replaceAll("\\W", "_").toUpperCase();
        this.originalValue = originalValue;
        this.prodValue = prodValue;
//        this.description = StringUtils.isBlank(description) ? "" : description;
        this.description = (description == null || description.trim().isEmpty()) ? "" : description;
        this.required = required;
    }

    @Override
    public String toString() {
        return "ProdProperty [originalName=" + originalName + ", name=" + name + ", originalValue="
                + originalValue + ", prodValue=" + prodValue + ", description=" + description + "]";
    }

    public String getOriginalName() {
        return originalName;
    }

    public void setOriginalName(String originalName) {
        this.originalName = originalName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOriginalValue() {
        return originalValue;
    }

    public void setOriginalValue(String originalValue) {
        this.originalValue = originalValue;
    }

    public String getProdValue() {
        return prodValue;
    }

    public void setProdValue(String prodValue) {
        this.prodValue = prodValue;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isRequired() {
        return required;
    }
}
