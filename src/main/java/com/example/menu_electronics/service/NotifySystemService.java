package com.example.menu_electronics.service;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.example.menu_electronics.entity.NotifySystem;
import com.example.menu_electronics.exception.AppException;
import com.example.menu_electronics.exception.ErrorCode;
import com.example.menu_electronics.repository.NotifySystemRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class NotifySystemService {
    NotifySystemRepository notifySystemRepository;

    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public List<NotifySystem> getAllNotifySystems() {
        return notifySystemRepository.findAll();
    }

    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public List<NotifySystem> getAllNotifySystemsTotal() {
        return notifySystemRepository.findAllByReadNotifyEquals(false);
    }

    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public NotifySystem readNotifySystem(Long id) {
        NotifySystem notifySystem = notifySystemRepository
                .findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION));
        if (notifySystem.getReadNotify()) {
            return null;
        }
        notifySystem.setReadNotify(true);
        notifySystem = notifySystemRepository.save(notifySystem);
        return notifySystem;
    }
}
