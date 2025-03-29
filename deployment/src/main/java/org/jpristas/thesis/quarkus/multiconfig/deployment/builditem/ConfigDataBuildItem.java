package org.jpristas.thesis.quarkus.multiconfig.deployment.builditem;

import io.quarkus.builder.item.SimpleBuildItem;

public final class ConfigDataBuildItem extends SimpleBuildItem {
    private final String fileContent;

    public ConfigDataBuildItem(String fileContent) {
        this.fileContent = fileContent;
    }

    public String getFileContent() {
        return fileContent;
    }
}
