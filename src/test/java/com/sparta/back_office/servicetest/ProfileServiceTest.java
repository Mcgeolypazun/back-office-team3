package com.sparta.back_office.servicetest;

import com.sparta.back_office.dto.ProfileRequestDto;
import com.sparta.back_office.dto.ProfileResponseDto;
import com.sparta.back_office.entity.PasswordChecking;
import com.sparta.back_office.entity.User;
import com.sparta.back_office.entity.UserRoleEnum;
import com.sparta.back_office.repository.PasswordCheckingRepository;
import com.sparta.back_office.repository.UserRepository;
import com.sparta.back_office.service.ProfileService;
import com.sparta.back_office.util.IntegrationTest;
import com.sparta.back_office.util.utilTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;

import static com.sparta.back_office.entity.UserRoleEnum.USER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@DisplayName("프로필 테스트")
@Transactional
public class ProfileServiceTest extends utilTest {

    private UserRepository userRepository;
    private ProfileService profileService;
    private PasswordCheckingRepository passwordCheckingRepository;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        passwordCheckingRepository = mock(PasswordCheckingRepository.class);
        profileService = new ProfileService(userRepository, passwordCheckingRepository);
    }


    @Test
    @DisplayName("비밀번호 수정 테스트 성공")
    void testUpdateUserPassword(){
        // Given
        var mockUser = saveUser();
        Long userId = 1L;
        PasswordChecking passwordChecking = new PasswordChecking(mockUser.getPassword(),mockUser);

        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        when(passwordCheckingRepository.findByUser(mockUser)).thenReturn(passwordChecking);

        // When
        String newPassword1 = "PAssword45";
        String newPassword2 = "PAsSword85";
        String newPassword3 = "PAsSword81";
        String newPassword4 = "PAsSword82";

        profileService.updateUserPassword(userId, newPassword1);
        profileService.updateUserPassword(userId, newPassword2);
        profileService.updateUserPassword(userId, newPassword3);
        profileService.updateUserPassword(userId, newPassword4);

        // Then
        assertThat(mockUser.getPassword()).isEqualTo(newPassword4);
    }

    @Test
    @DisplayName("비밀번호 수정 테스트 실패")
    void test2(){
        // Given
        var mockUser = saveUser();
        Long userId = 1L;
        PasswordChecking passwordChecking = new PasswordChecking(mockUser.getPassword(),mockUser);

        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        when(passwordCheckingRepository.findByUser(mockUser)).thenReturn(passwordChecking);

        // When
        String newPassword1 = "PAssword45";
        String newPassword2 = "PAsSword85";
        String newPassword3 = "PAsSword81";
        String newPassword4 = "PAssword45";

        profileService.updateUserPassword(userId, newPassword1);
        profileService.updateUserPassword(userId, newPassword2);
        profileService.updateUserPassword(userId, newPassword3);

        // Then
        assertThrows(IllegalArgumentException.class,
                () -> profileService.updateUserPassword(userId, newPassword4));
    }


    @Test
    @DisplayName("프로필 조회")
    void testGetProfile_WhenProfileExists_ReturnsProfileResponseDto() {
        // Arrange
        Long userId = 1L;
        var mockUser = saveUser();

        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        // Act
        ProfileResponseDto result1 = profileService.getProfile(userId);
        // Assert
        assertEquals(mockUser.getUsername(), result1.getUsername());
        assertEquals(mockUser.getEmail(), result1.getEmail());
        assertEquals(mockUser.getIntro(), result1.getIntro());
    }
    @DisplayName("프로필 업데이트 테스트")
    @Test
    void test() {
        // Update profile
        var mockUser = saveUser();
        var userId  = mockUser.getId();

        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));

        ProfileRequestDto profileRequestDto = new ProfileRequestDto(mockUser.getUsername(),mockUser.getPassword(), mockUser.getIntro(), mockUser.getEmail());
        // Act - Get updated profile
        profileService.updateProfile(userId, profileRequestDto);
        ProfileResponseDto result2 = profileService.getProfile(userId);
        // Assert updated profile data
        assertEquals(mockUser.getUsername(), result2.getUsername());
        assertEquals(mockUser.getEmail(), result2.getEmail());
        assertEquals(mockUser.getIntro(), result2.getIntro());
    }


}
//assertThat(response.getTitle()).isEqualTo("title");
//        assertThat(response.getContent()).isEqualTo("content");
//        assertThat(response.getId()).isNotNull();
//        assertThat(response.getCreatedAt()).isNotNull();
//        assertThat(response.getActivatedAt()).isNotNull();