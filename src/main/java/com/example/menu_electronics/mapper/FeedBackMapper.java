package com.example.menu_electronics.mapper;

import org.mapstruct.Mapper;

import com.example.menu_electronics.dto.request.FeedBackRequest;
import com.example.menu_electronics.dto.response.FeedBackResponse;
import com.example.menu_electronics.entity.FeedBack;

@Mapper(componentModel = "spring")
public interface FeedBackMapper {
    FeedBack toFeedBack(FeedBackRequest feedBackRequest);

    FeedBackResponse toFeedBackResponse(FeedBack feedBack);
}
