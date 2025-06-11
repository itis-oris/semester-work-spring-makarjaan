package com.makarova.entity;

import lombok.Getter;

@Getter
public enum ApartmentStatus {
    SENT_FOR_VERIFICATION("Отправлено на проверку"),
    REFUSED("Отказано в публикации"),
    PUBLISHED("Опубликовано"),
    COMPLETED("Снято с публикации");

    private final String displayName;

    ApartmentStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
