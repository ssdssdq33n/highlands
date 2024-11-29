package com.example.menu_electronics.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.menu_electronics.dto.request.TableNameRequest;
import com.example.menu_electronics.dto.response.TableNameResponse;
import com.example.menu_electronics.entity.TableName;
import com.example.menu_electronics.exception.AppException;
import com.example.menu_electronics.exception.ErrorCode;
import com.example.menu_electronics.mapper.TableNameMapper;
import com.example.menu_electronics.repository.TableNameRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TableNameService {
    TableNameRepository tableNameRepository;
    TableNameMapper tableNameMapper;

    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @Transactional
    @CacheEvict(value = "tables", allEntries = true)
    public TableNameResponse createTableName(TableNameRequest tableNameRequest) {
        Optional<TableName> tableNameCheck = tableNameRepository.findByName(tableNameRequest.getName());
        if (tableNameCheck.isPresent()) throw new AppException(ErrorCode.TABLE_EXISTED);
        TableName tableName = tableNameMapper.toTableName(tableNameRequest);
        LocalDateTime orderTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        tableName.setCreated(orderTime.format(formatter));
        tableName.setUpdated(orderTime.format(formatter));
        tableName = tableNameRepository.save(tableName);
        return tableNameMapper.toTableNameResponse(tableName);
    }

    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @Transactional
    @CacheEvict(value = "tables", allEntries = true)
    public TableNameResponse updateTableName(TableNameRequest tableNameRequest, Long id) {
        TableName tableName =
                tableNameRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.TABLE_NOT_EXISTED));
        LocalDateTime orderTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        tableName.setUpdated(orderTime.format(formatter));
        tableName.setName(tableNameRequest.getName());
        tableName = tableNameRepository.save(tableName);
        return tableNameMapper.toTableNameResponse(tableName);
    }

    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @Transactional
    @CacheEvict(value = "tables", allEntries = true)
    public String deleteTableName(Long id) {
        TableName tableName =
                tableNameRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.TABLE_NOT_EXISTED));
        tableNameRepository.delete(tableName);
        return "Deleted successfully";
    }

    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @Cacheable(value = "tables", key = "#id")
    public TableNameResponse getTableName(Long id) {
        TableName tableName =
                tableNameRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.TABLE_NOT_EXISTED));
        return tableNameMapper.toTableNameResponse(tableName);
    }

    public List<TableNameResponse> getAllTableName() {
        List<TableName> tableNames = tableNameRepository.findAll();
        return tableNames.stream().map(tableNameMapper::toTableNameResponse).toList();
    }
}
