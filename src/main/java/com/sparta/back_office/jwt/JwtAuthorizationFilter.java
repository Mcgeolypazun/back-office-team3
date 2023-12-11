package com.sparta.back_office.jwt;

import com.sparta.back_office.dto.JwtAuthentication;
import com.sparta.back_office.dto.JwtUser;
import com.sparta.back_office.security.UserDetailsServiceImpl;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static com.sparta.back_office.jwt.JwtUtil.AUTHORIZATION_HEADER;
import static org.hibernate.usertype.DynamicParameterizedType.ACCESS_TYPE;
@Slf4j(topic = "JWT 검증 및 인가")
@Log4j2
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;

    public JwtAuthorizationFilter(JwtUtil jwtUtil, UserDetailsServiceImpl userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain filterChain) throws ServletException, IOException {

        String token = req.getHeader(AUTHORIZATION_HEADER);

        Optional<JwtUser> bearerToken = jwtUtil.getJwtUser(token, ACCESS_TYPE);
        log.info("bearerToken = " + bearerToken);
        // 유효한 엑세스 토큰인 경우 인가 처리
        bearerToken.ifPresent(this::setAuthentication);
        //filterChain.doFilter(req, res);

        String tokenValue = jwtUtil.getJwtFromHeader(req);

        if (StringUtils.hasText(tokenValue)) {

            if (!jwtUtil.validateToken(tokenValue)) {
                log.error("Token Error");
                return;
            }

            Claims info = jwtUtil.getUserInfoFromToken(tokenValue);

            try {
                log.info("info = "+info);
                setAuthentication(info.getSubject());
            } catch (Exception e) {
                log.error(e.getMessage());
                return;
            }
        }

        filterChain.doFilter(req, res);
    }


    public void setAuthentication(JwtUser user) {
        var authorities = List.of(new SimpleGrantedAuthority(user.role().name()));
        log.info("authorities = " + authorities);
        var authentication = new JwtAuthentication(user, authorities);
        log.info("authentication = " + authentication);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        log.info("SecurityContextHolder.getContext() = " + SecurityContextHolder.getContext());
    }
    public void setAuthentication(String username) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = createAuthentication(username);
        context.setAuthentication(authentication);

        SecurityContextHolder.setContext(context);
    }

    private Authentication createAuthentication(String username) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
}