package com.leonardom011.socio.users.repository;

import com.leonardom011.socio.users.entity.Role;
import com.leonardom011.socio.users.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;


@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class UserRepositoryTest {


    @Autowired
    private UserRepository userRepository;

    private LocalDateTime timeNow = LocalDateTime.now();

    @Test
    void itShouldCheckWhenUserByUsernameExists() {
        // given
        String username = "Leca";
        User user = User.builder()
                .id(UUID.fromString("123456"))
                .username(username)
                .password("1234")
                .email("Leonardo@gmail.com")
                .firstName("Leonardo")
                .lastName("Marinovic")
                .createdAt(timeNow)
                .lastLogin(timeNow)
                .lastChange(timeNow)
                .role(Role.USER)
                .build();

        userRepository.save(user);

        // when
        Optional<User> expected = userRepository.findByUsername(username);

        // then
        assertThat(expected)
                .isNotNull()
                .isNotEmpty();

        User expectedUs = expected.orElseThrow();
        assertThat(expectedUs).isEqualTo(user);
    }

    /*@Test
    void itShouldCheckWhenUserDoesNotExistsByUsername() {
        // given
        String username = "Leca";
        User user = User.builder()
                .id(1L)
                .username("Vuka")
                .password("1234")
                .email("Leonardo@gmail.com")
                .firstName("Leonardo")
                .lastName("Marinovic")
                .createdAt(timeNow)
                .lastLogin(timeNow)
                .lastChange(timeNow)
                .role(Role.USER)
                .build();

        userRepository.save(user);

        // when
        Optional<User> expected = userRepository.findByUsername(username);

        // then
        assertThat(expected)
                .isNotNull()
                .isEmpty();
    }*/
}