package com.nikitin.roadmapsender.config.security;


import com.nimbusds.jose.shaded.gson.internal.LinkedTreeMap;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JwtGrantedAuthoritiesConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

    private static final String REALM_ACCESS_CLAIM = "realm_access";
    private static final String ROLES_CLAIM = "roles";

    @Override
    public Collection<GrantedAuthority> convert(@NonNull Jwt jwt) {
        if (Objects.isNull(jwt.getClaim(REALM_ACCESS_CLAIM))) {
            return Collections.emptyList();
        }
        LinkedTreeMap<String, ArrayList<String>> realmAccess = jwt.getClaim(REALM_ACCESS_CLAIM);
        var roles = (List<String>) realmAccess.get(ROLES_CLAIM);

        return roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }
}
