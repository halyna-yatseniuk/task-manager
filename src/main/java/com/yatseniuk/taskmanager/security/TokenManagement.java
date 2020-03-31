package com.yatseniuk.taskmanager.security;

import com.yatseniuk.taskmanager.constants.ErrorMessages;
import com.yatseniuk.taskmanager.dto.token.JwtTokenDTO;
import com.yatseniuk.taskmanager.exceptions.InvalidTokenException;
import com.yatseniuk.taskmanager.exceptions.JWTAuthenticationException;
import com.yatseniuk.taskmanager.exceptions.RefreshTokenException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.impl.TextCodec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Objects;

import static com.yatseniuk.taskmanager.constants.ConstantValues.AUTHORIZATION_HEADER;
import static com.yatseniuk.taskmanager.constants.ConstantValues.AUTH_HEADER_PREFIX;

@Component
public class TokenManagement {
    private static final Logger LOG = LoggerFactory.getLogger(TokenManagement.class);
    @Value("${secretKey}")
    private String secretKey;
    @Value("${expireTimeAccessToken}")
    private String expireTimeAccessToken;
    @Value("${expireTimeRefreshToken}")
    private String expireTimeRefreshToken;
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    public TokenManagement(UserDetailsServiceImpl userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    public JwtTokenDTO generateAccessAndRefreshTokens(String email) {
        long nowMillis = System.currentTimeMillis();
        long expirationTime = Long.parseLong(expireTimeAccessToken);
        Date expirationDate = new Date(nowMillis + expirationTime);
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        byte[] decodeSecretKey = TextCodec.BASE64.decode(secretKey);

        String token = Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(expirationDate)
                .signWith(signatureAlgorithm, decodeSecretKey).compact();

        long expirationTimeRefresh = Long.parseLong(expireTimeRefreshToken);
        String refreshToken = Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(nowMillis + expirationTimeRefresh))
                .signWith(signatureAlgorithm, decodeSecretKey).compact();

        LOG.info("Tokens generated successfully");
        return new JwtTokenDTO(token, refreshToken);
    }

    public JwtTokenDTO refreshAccessAndRefreshTokens(String refreshToken) {
        if (refreshToken != null && validateRefreshToken(refreshToken)) {
            try {
                String email = getUserEmail(refreshToken);
                JwtTokenDTO jwtTokenDTO = generateAccessAndRefreshTokens(email);
                LOG.info("Tokens refreshed successfully");
                return jwtTokenDTO;

            } catch (JwtException e) {
                throw new RefreshTokenException(ErrorMessages.FAIL_TO_REFRESH_TOKEN.getMessage());
            }
        } else {
            throw new JWTAuthenticationException(ErrorMessages.FAIL_TO_AUTHENTICATE.getMessage());
        }
    }

    public boolean validateRefreshToken(String token) {
        boolean isValid = false;
        try {
            Jws<Claims> claimsJws = Jwts.parser().setSigningKey(TextCodec.BASE64.decode(secretKey)).parseClaimsJws(token);
            if (!claimsJws.getBody().getExpiration().before(new Date())) {
                isValid = true;
            }
        } catch (JwtException ex) {
            throw new InvalidTokenException(ErrorMessages.INVALID_TOKEN.getMessage());
        } catch (IllegalArgumentException ex) {
            throw new InvalidTokenException(ErrorMessages.CANNOT_RETRIEVE_USER_DATA.getMessage());
        }
        return isValid;
    }

    public String getUserEmail(String token) {
        return Jwts.parser().setSigningKey(TextCodec.BASE64.decode(secretKey))
                .parseClaimsJws(token).getBody().getSubject();
    }

    public UsernamePasswordAuthenticationToken getAuthentication(String token) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(getUserEmail(token));
        return new UsernamePasswordAuthenticationToken(userDetails,
                "", userDetails.getAuthorities());
    }

    public String resolveAccessToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER.getValue());
        if (Objects.nonNull(bearerToken) && bearerToken.startsWith(AUTH_HEADER_PREFIX.getValue())) {
            return bearerToken.substring(7);
        }
        return null;
    }
}