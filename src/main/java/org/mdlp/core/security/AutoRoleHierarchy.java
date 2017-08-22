package org.mdlp.core.security;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.Map.Entry;

@Component
public class AutoRoleHierarchy implements RoleHierarchy, InitializingBean {

    private static final GrantedAuthority ROLE_ANONYMOUS = new SimpleGrantedAuthority("ROLE_ANONYMOUS");

    private boolean doAddAnonymousRole = true;

    private boolean doAddAnonymousRoleFrozen = doAddAnonymousRole;

    private final Map<GrantedAuthority, Set<GrantedAuthority>> hierarchy = new HashMap<>();

    @Autowired(required = false)
    public void setMappings(@Nullable Collection<RoleHierarchyMapping> mappings) {
        if (null == mappings) return;

        Map<GrantedAuthority, Set<GrantedAuthority>> reverseHierarchy = new HashMap<>();
        mappings.stream()
            .map(RoleHierarchyMapping::getReachableGrantedAuthorities)
            .filter(Objects::nonNull)
            .map(Map::entrySet)
            .flatMap(Set::stream)
            .forEach(entry -> {
                GrantedAuthority authority = entry.getKey();
                entry.getValue().forEach(reachableAuthority -> reverseHierarchy.computeIfAbsent(reachableAuthority, $ -> new HashSet<>()).add(authority));
            });

        for (int counter = 0; counter < reverseHierarchy.size(); ++counter) {
            boolean isModified = false;
            for (Entry<GrantedAuthority, Set<GrantedAuthority>> entry : reverseHierarchy.entrySet()) {
                Set<GrantedAuthority> reachableAuthorities = entry.getValue();
                for (GrantedAuthority authority : new HashSet<>(reachableAuthorities)) {
                    Set<GrantedAuthority> oneStepReachableAuthorities = reverseHierarchy.get(authority);
                    if (null != oneStepReachableAuthorities) isModified |= reachableAuthorities.addAll(oneStepReachableAuthorities);
                }
            }
            if (!isModified) break;
        }

        reverseHierarchy.forEach((reachableAuthority, authorities) -> {
            authorities.forEach(authority -> hierarchy.computeIfAbsent(authority, $ -> new HashSet<>()).add(reachableAuthority));
        });
    }

    public boolean isDoAddAnonymousRole() {
        return doAddAnonymousRole;
    }

    public void setDoAddAnonymousRole(boolean doAddAnonymousRole) {
        this.doAddAnonymousRole = doAddAnonymousRole;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (doAddAnonymousRoleFrozen = isDoAddAnonymousRole()) {
            hierarchy.values().forEach(reachableAuthorities -> reachableAuthorities.add(ROLE_ANONYMOUS));
        }
    }

    @NotNull
    @Override
    public Collection<? extends GrantedAuthority> getReachableGrantedAuthorities(@NotNull Collection<? extends GrantedAuthority> authorities) {
        Objects.requireNonNull(authorities, "authorities");

        if (hierarchy.isEmpty()) {
            Set<GrantedAuthority> reachableRoles = new HashSet<>(authorities);
            if (doAddAnonymousRoleFrozen) reachableRoles.add(ROLE_ANONYMOUS);
            return reachableRoles;
        }

        Set<GrantedAuthority> result = new HashSet<>(authorities);
        authorities.forEach(authority -> {
            Set<GrantedAuthority> reachableRoles = hierarchy.get(authority);
            if (null != reachableRoles) {
                result.addAll(reachableRoles);
            } else {
                if (doAddAnonymousRoleFrozen) result.add(ROLE_ANONYMOUS);
            }
        });
        return result;
    }

}
