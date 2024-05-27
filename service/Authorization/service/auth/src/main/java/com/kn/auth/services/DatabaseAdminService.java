package com.kn.auth.services;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.kn.auth.enums.ReturnPosition;
import com.kn.auth.enums.TableName;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DatabaseAdminService extends DatabaseService {

    public void deleteValueByValue(TableName tableName, String sourceColumn, String sourceValue)
            throws DataAccessException {
        jdbcTemplate.execute(
                String.format("DELETE FROM `%s` WHERE `%s` = '%s'", tableName.getValue(), sourceColumn, sourceValue));
    }

    public String getValueByValue(TableName tableName, String sourceColumn, String sourceValue, String fromColumn,
            ReturnPosition returnPosition) throws DataAccessException {
        return jdbcTemplate.queryForObject(
                String.format("SELECT `%s` FROM `%s` WHERE `%s` = '%s' %s",
                        fromColumn,
                        tableName.getValue(),
                        sourceColumn,
                        sourceValue,
                        String.format(returnPosition.getValue(), sourceColumn)),
                String.class);
    }
}
