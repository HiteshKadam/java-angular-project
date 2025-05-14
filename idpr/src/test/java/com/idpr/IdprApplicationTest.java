package com.idpr;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@ExtendWith(MockitoExtension.class)
class IdprApplicationTest {

    @Test
    void applicationContextLoadsSuccessfully() {
        assertDoesNotThrow(() -> IdprApplication.main(new String[]{}));
    }

}