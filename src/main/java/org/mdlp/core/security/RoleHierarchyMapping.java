package org.mdlp.core.security;

import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Map;

public interface RoleHierarchyMapping {

    @NotNull Map<GrantedAuthority, Collection<GrantedAuthority>> getReachableGrantedAuthorities();

}
