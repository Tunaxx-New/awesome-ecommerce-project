package com.kn.auth.enums;

import com.kn.auth.constants.TableNameConstants;

public enum TableName {
    AUTHENTICATION(TableNameConstants.AUTHENTICATION),
    AUTHENTICATION_TOKEN(TableNameConstants.AUTHENTICATION_TOKEN),
    AUTHENTICATION_ROLE(TableNameConstants.AUTHENTICATION_ROLE),
    BUYERS(TableNameConstants.BUYERS),
    SELLERS(TableNameConstants.SELLERS);

    private final String value;

    TableName(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

