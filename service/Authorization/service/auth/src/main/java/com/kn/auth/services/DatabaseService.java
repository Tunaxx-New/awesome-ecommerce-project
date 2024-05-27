package com.kn.auth.services;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.kn.auth.models.Authentication;

import jakarta.persistence.Table;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DatabaseService {
    @Autowired
    protected JdbcTemplate jdbcTemplate;

    public String contstraintColumn(String constraintName) throws SQLException {
        DatabaseMetaData metaData;
        metaData = jdbcTemplate.getDataSource().getConnection().getMetaData();
        Table tableAnnotation = Authentication.class.getAnnotation(Table.class);
        ResultSet uniqueKeys = metaData.getIndexInfo(null, null, tableAnnotation.name(), true, false);
        while (uniqueKeys.next()) {
            String columnName = uniqueKeys.getString("COLUMN_NAME");
            String constraint = uniqueKeys.getString("INDEX_NAME");
            System.out.println(constraint + " " + columnName + " " + constraintName);
            if (constraint.equals(constraintName)) {
                return columnName;
            }
        }
        return "";
    }
}