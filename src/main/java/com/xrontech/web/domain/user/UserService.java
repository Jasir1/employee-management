package com.xrontech.web.domain.user;

import com.xrontech.web.domain.security.domain.UserData;
import com.xrontech.web.domain.security.entity.User;
import com.xrontech.web.domain.security.repos.UserRepository;
import com.xrontech.web.dto.ApplicationResponseDTO;
import com.xrontech.web.exception.ApplicationCustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;

    public User getProfile(){
        return getCurrentUser();
    }

    public ApplicationResponseDTO updateUser(UserUpdateDTO userUpdateDTO) {
        User user = userRepository.findByUsername(getCurrentUser().getUsername())
                .orElseThrow(() -> new ApplicationCustomException(HttpStatus.BAD_REQUEST, "USER_NOT_FOUND", "User Not Found"));

        user.setName(userUpdateDTO.getName());

        userRepository.findByMobile(userUpdateDTO.getMobile()).ifPresent(existingUser -> {
            if (!user.getMobile().equals(userUpdateDTO.getMobile())) {
                throw new ApplicationCustomException(HttpStatus.BAD_REQUEST, "MOBILE_ALREADY_EXISTS", "Mobile Already Exists");
            }
        });

        user.setMobile(userUpdateDTO.getMobile());

        user.setDateOfBirth(userUpdateDTO.getDateOfBirth());

        userRepository.save(user);

        return new ApplicationResponseDTO(HttpStatus.OK, "USER_UPDATED_SUCCESSFULLY!", "User Updated Successfully!");
    }

    public ApplicationResponseDTO updateProfilePic(MultipartFile file) {
        if (file.isEmpty()) {
            throw new ApplicationCustomException(HttpStatus.NOT_FOUND, "FILE_NOT_SELECTED", "File not Selected");
        } else {
            try {
                String projectRoot = System.getProperty("user.dir");
                String originalFilename = file.getOriginalFilename();
                if (originalFilename != null) {
                    String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));

                    if (!(fileExtension.equalsIgnoreCase(".jpg") || fileExtension.equalsIgnoreCase(".jpeg") || fileExtension.equalsIgnoreCase(".png"))) {
                        throw new ApplicationCustomException(HttpStatus.BAD_REQUEST, "INVALID_FILE_TYPE", "Invalid file type. Only JPG, JPEG, and PNG are allowed.");
                    }

                    String newFileName = UUID.randomUUID() + fileExtension;
                    String imagePath = "/uploads/" + newFileName;
                    Path path = Paths.get(projectRoot + imagePath);
                    File saveFile = new File(String.valueOf(path));
                    file.transferTo(saveFile);
                    User user = getCurrentUser();
                    user.setImageURL(newFileName);
                    userRepository.save(user);
                    return new ApplicationResponseDTO(HttpStatus.CREATED, "IMAGE_UPLOADED_SUCCESSFULLY", "Image Uploaded Successfully!");
                } else {
                    throw new ApplicationCustomException(HttpStatus.NOT_FOUND, "ORIGINAL_FILE_NAME_NOT_FOUND", "Original File Name Not Found");
                }
            } catch (IOException e) {
                throw new ApplicationCustomException(HttpStatus.BAD_REQUEST, "FILE_NOT_SAVED", "File not Saved");
            }
        }
    }

    public User getCurrentUser() {
        try {
            SecurityContext securityContext = SecurityContextHolder.getContext();
            if (securityContext != null && securityContext.getAuthentication() != null) {
                Object principal = securityContext.getAuthentication().getPrincipal();
                if (principal instanceof UserData userData) {
                    String username = userData.getUsername();
                    Optional<User> optionalUser = userRepository.findByUsername(username);
                    if (optionalUser.isPresent()) {
                        return optionalUser.get();
                    } else {
                        throw new ApplicationCustomException(HttpStatus.UNAUTHORIZED, "USER_NOT_FOUND", "User not found");
                    }
                } else {
                    throw new ApplicationCustomException(HttpStatus.UNAUTHORIZED, "INVALID_PRINCIPAL", "Invalid Principal");
                }
            } else {
                throw new ApplicationCustomException(HttpStatus.UNAUTHORIZED, "SECURITY_CONTEXT_IS_NULL", "Security Context is Null");
            }
        } catch (Exception e) {
            throw new ApplicationCustomException(HttpStatus.UNAUTHORIZED, "INVALID_USER", e.getMessage());
        }
    }

    public User findByUsername(String username) {
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isPresent()) {
            return optionalUser.get();
        } else {
            throw new ApplicationCustomException(HttpStatus.UNAUTHORIZED, "USER_NOT_FOUND", "User not found");
        }
    }

}