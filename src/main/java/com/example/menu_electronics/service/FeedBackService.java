package com.example.menu_electronics.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.menu_electronics.dto.request.FeedBackRequest;
import com.example.menu_electronics.dto.response.FeedBackResponse;
import com.example.menu_electronics.entity.FeedBack;
import com.example.menu_electronics.exception.AppException;
import com.example.menu_electronics.exception.ErrorCode;
import com.example.menu_electronics.mapper.FeedBackMapper;
import com.example.menu_electronics.repository.FeedBackRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FeedBackService {

    FeedBackRepository feedBackRepository;
    FeedBackMapper feedBackMapper;

    @Transactional
    @CacheEvict(value = "feedbacks", allEntries = true) // Xóa cache khi thêm mới
    public FeedBackResponse createFeedBack(FeedBackRequest feedBackRequest) {
        FeedBack feedBack = feedBackMapper.toFeedBack(feedBackRequest);
        LocalDateTime orderTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        feedBack.setCreated(orderTime.format(formatter));
        feedBack = feedBackRepository.save(feedBack);
        return feedBackMapper.toFeedBackResponse(feedBack);
    }

    @Cacheable(value = "feedbacks", key = "#id")
    public FeedBackResponse getFeedBack(Long id) {
        FeedBack feedBack =
                feedBackRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.FEEDBACK_NOT_EXISTED));
        return feedBackMapper.toFeedBackResponse(feedBack);
    }

    @Transactional
    @CacheEvict(value = "feedbacks", allEntries = true) // Xóa cache khi thêm mới
    public String deleteFeedBack(Long id) {
        FeedBack feedBack =
                feedBackRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.FEEDBACK_NOT_EXISTED));
        feedBackRepository.delete(feedBack);
        return "Deleted Successfully";
    }

    public List<FeedBackResponse> getAllFeedBack() {
        List<FeedBack> feedBackList = feedBackRepository.findAll();
        return feedBackList.stream().map(feedBackMapper::toFeedBackResponse).toList();
    }
}
