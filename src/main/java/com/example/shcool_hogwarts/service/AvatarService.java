package com.example.shcool_hogwarts.service;

import com.example.shcool_hogwarts.model.Avatar;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface AvatarService {
    void uploadAvatar(Long studentId, MultipartFile avatar) throws IOException;

    Avatar findAvatar(Long id);

    Page<Avatar> getAllAvatars(Pageable pageable);
}
