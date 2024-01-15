package com.nikitin.roadmapfrontend.configuration.security;

import com.nimbusds.jose.shaded.gson.internal.LinkedTreeMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.oauth2.core.oidc.user.OidcUserAuthority;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Slf4j
@Component
public class KeycloakAuthoritiesMapper implements GrantedAuthoritiesMapper {

    private static final String REALM_ACCESS_CLAIM = "realm_access";
    private static final String ROLES_CLAIM = "roles";

    @Override
    public Collection<? extends GrantedAuthority> mapAuthorities(Collection<? extends GrantedAuthority> authorities) {
        Set<GrantedAuthority> mappedAuthorities = new HashSet<>();

        authorities.forEach(authority -> {
            if (authority instanceof OidcUserAuthority oidcAuth) {
                if (Objects.nonNull(oidcAuth.getIdToken().getClaim(REALM_ACCESS_CLAIM))) {
                    LinkedTreeMap<String, ArrayList<String>> realmAccess = (oidcAuth.getIdToken().getClaim(REALM_ACCESS_CLAIM));
                    var roles = (List<String>) realmAccess.get(ROLES_CLAIM);
                    roles.forEach(a -> mappedAuthorities.add(new SimpleGrantedAuthority(a)));
                }
            }
        });

        return mappedAuthorities;
    }
}
