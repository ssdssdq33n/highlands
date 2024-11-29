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

import com.example.menu_electronics.dto.request.SelectionRequest;
import com.example.menu_electronics.dto.response.SelectionResponse;
import com.example.menu_electronics.entity.Selection;
import com.example.menu_electronics.exception.AppException;
import com.example.menu_electronics.exception.ErrorCode;
import com.example.menu_electronics.mapper.SelectionMapper;
import com.example.menu_electronics.repository.SelectionRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SelectionService {

    SelectionRepository selectionRepository;
    SelectionMapper selectionMapper;

    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @Transactional
    @CacheEvict(value = "selections", allEntries = true) // Xóa cache khi thêm mới
    public SelectionResponse createSelection(SelectionRequest selectionRequest) {
        Optional<Selection> selection = selectionRepository.findByName(selectionRequest.getName());
        if (selection.isPresent()) throw new AppException(ErrorCode.SELECTION_EXISTED);
        LocalDateTime orderTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        Selection selectionEntity = selectionMapper.toSelection(selectionRequest);
        selectionEntity.setCreated(orderTime.format(formatter));
        selectionEntity.setUpdated(orderTime.format(formatter));
        selectionEntity = selectionRepository.save(selectionEntity);
        return selectionMapper.toSelectionResponse(selectionEntity);
    }

    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @Transactional
    @CacheEvict(value = "selections", allEntries = true) // Xóa cache khi thêm mới
    public SelectionResponse updateSelection(SelectionRequest selectionRequest, Long id) {
        Selection selection =
                selectionRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.SELECTION_NOT_EXISTED));
        LocalDateTime orderTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        selection.setName(selectionRequest.getName());
        selection.setUpdated(orderTime.format(formatter));
        selection = selectionRepository.save(selection);
        return selectionMapper.toSelectionResponse(selection);
    }

    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @Transactional
    @CacheEvict(value = "selections", allEntries = true) // Xóa cache khi thêm mới
    public String deleteSelection(Long id) {
        Selection selection =
                selectionRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.SELECTION_NOT_EXISTED));
        selectionRepository.delete(selection);
        return "Deleted Successfully";
    }

    //    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public List<SelectionResponse> getAllSelections() {
        List<Selection> selections = selectionRepository.findAll();
        return selections.stream().map(selectionMapper::toSelectionResponse).toList();
    }

    //    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @Cacheable(value = "selections", key = "#id")
    public SelectionResponse getSelection(Long id) {
        Selection selection =
                selectionRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.SELECTION_NOT_EXISTED));
        return selectionMapper.toSelectionResponse(selection);
    }
}
