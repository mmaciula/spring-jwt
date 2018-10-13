package com.github.springjwt.security.jwt;

import com.github.springjwt.security.PrincipalUser;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Date;

@Component
public class TokenManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(TokenManager.class);
    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.expires.days}")
    private int expiresInDays;
    @Autowired
    private UserDetailsService userDetailsService;

    public SignedJWT createNewToken(final PrincipalUser principalUser){
        try{
            Date now = new Date();
            Date expires = DateUtils.addDays(now, expiresInDays);

            // time to midnight
            expires = DateUtils.setHours(expires, 0);
            expires = DateUtils.setMinutes(expires, 0);
            expires = DateUtils.setSeconds(expires, 0);
            expires = DateUtils.setMilliseconds(expires, 0);

            JWTClaimsSet claimsSet = new JWTClaimsSet();
            claimsSet.setSubject(principalUser.getUsername());
            claimsSet.setIssueTime(now);
            claimsSet.setNotBeforeTime(now);
            claimsSet.setExpirationTime(expires);

            SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.ES256), claimsSet);

            JWSSigner signer = new MACSigner(secret);
            signedJWT.sign(signer);

            return signedJWT;

        } catch (JOSEException exception) {
            LOGGER.error("Could not generate token for user: " + principalUser.getUsername(), exception);
        }

        return null;
    }

    public PrincipalUser getUserDetails(final String token){
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            JWSVerifier verifier = new MACVerifier(secret);

            if (signedJWT.verify(verifier)){
                Date now = new Date();
                boolean isNotExpired = now.before(signedJWT.getJWTClaimsSet().getExpirationTime());

                if (isNotExpired){
                    String email = signedJWT.getJWTClaimsSet().getSubject();
                    try{
                        return (PrincipalUser) userDetailsService.loadUserByUsername(email);
                    } catch (UsernameNotFoundException usernameNotFound){
                        LOGGER.debug("Username not found!");
                        return null;
                    }
                }
            }
        } catch (ParseException parseException){
            LOGGER.debug("Can not parse token \"" + token + "\"");
        } catch (JOSEException joseException){
            LOGGER.debug("Can not verify token \"" + token + "\"");
        }
        return null;
    }
}