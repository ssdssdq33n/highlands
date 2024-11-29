package com.example.menu_electronics.mapper;

import org.mapstruct.Mapper;

import com.example.menu_electronics.dto.request.SelectionRequest;
import com.example.menu_electronics.dto.response.SelectionResponse;
import com.example.menu_electronics.entity.Selection;

@Mapper(componentModel = "spring")
public interface SelectionMapper {
    Selection toSelection(SelectionRequest selectionRequest);

    SelectionResponse toSelectionResponse(Selection selection);
}
