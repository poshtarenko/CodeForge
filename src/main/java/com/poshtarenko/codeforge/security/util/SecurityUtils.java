package com.poshtarenko.codeforge.security.util;

import com.poshtarenko.codeforge.entity.ERole;
import com.poshtarenko.codeforge.exception.NotEnoughRightsException;
import com.poshtarenko.codeforge.security.userdetails.UserDetailsImpl;
import lombok.experimental.UtilityClass;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@UtilityClass
public class SecurityUtils {

    public static long getUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return ((UserDetailsImpl) authentication.getPrincipal()).getId();
    }

    public static void checkUserRole(ERole requiredRole) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        ((UserDetailsImpl) authentication.getPrincipal())
                .getAuthorities()
                .stream()
                .filter(a -> a.getAuthority().equals(requiredRole.name()))
                .findFirst()
                .orElseThrow(() -> new NotEnoughRightsException(
                        "You do not have permission on this action. Required role : " + requiredRole.name(),
                        requiredRole)
                );
    }

}
