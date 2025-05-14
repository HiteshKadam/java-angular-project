package com.idpr.filter;

import com.idpr.service.CustomUserDetailsService;
import com.idpr.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JwtRequestFilterTest {

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private CustomUserDetailsService customUserDetailsService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain chain;

    @Mock
    private UserDetails userDetails;

    @InjectMocks
    private JwtRequestFilter jwtRequestFilter;

    @Test
    void doFilterInternal_withValidJwt_setsAuthentication() throws Exception {
        when(request.getHeader("Authorization")).thenReturn("Bearer validJwt");
        when(jwtUtil.extractUsername("validJwt")).thenReturn("testUser");
        when(jwtUtil.extractUserId("validJwt")).thenReturn(1L);
        when(jwtUtil.isTokenExpired("validJwt")).thenReturn(false);
        when(customUserDetailsService.loadUserByUsername("testUser")).thenReturn(userDetails);
        when(jwtUtil.validateToken("validJwt", null)).thenReturn(true);

        jwtRequestFilter.doFilterInternal(request, response, chain);

        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
        verify(chain).doFilter(request, response);
    }

    @Test
    void doFilterInternal_withExpiredJwt_returnsUnauthorized() throws Exception {
        when(request.getHeader("Authorization")).thenReturn("Bearer expiredJwt");
        when(jwtUtil.extractUsername("expiredJwt")).thenReturn("testUser");
        when(jwtUtil.isTokenExpired("expiredJwt")).thenReturn(true);

        jwtRequestFilter.doFilterInternal(request, response, chain);

        verify(response).sendError(HttpServletResponse.SC_UNAUTHORIZED, "JWT Token has expired");
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void doFilterInternal_withInvalidJwt_doesNotSetAuthentication() throws Exception {
        when(request.getHeader("Authorization")).thenReturn("Bearer invalidJwt");
        when(jwtUtil.extractUsername("invalidJwt")).thenReturn("testUser");
        when(jwtUtil.isTokenExpired("invalidJwt")).thenReturn(false);
        when(customUserDetailsService.loadUserByUsername("testUser")).thenReturn(userDetails);
        when(jwtUtil.validateToken("invalidJwt", null)).thenReturn(false);

        jwtRequestFilter.doFilterInternal(request, response, chain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(chain).doFilter(request, response);
    }

    @Test
    void doFilterInternal_withNoAuthorizationHeader_doesNotSetAuthentication() throws Exception {
        when(request.getHeader("Authorization")).thenReturn(null);

        jwtRequestFilter.doFilterInternal(request, response, chain);

        verify(chain).doFilter(request, response);
    }

    @Test
    void doFilterInternal_withMalformedAuthorizationHeader_doesNotSetAuthentication() throws Exception {
        when(request.getHeader("Authorization")).thenReturn("InvalidHeader");

        jwtRequestFilter.doFilterInternal(request, response, chain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(chain).doFilter(request, response);
    }
}