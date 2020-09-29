package com.internet.shop.web.filters;

import com.internet.shop.lib.Injector;
import com.internet.shop.model.Role;
import com.internet.shop.model.User;
import com.internet.shop.service.UserService;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AuthorizationFilter implements Filter {
    private static final String USER_ID = "user_id";
    private static final Injector injector = Injector.getInstance("com.internet.shop");
    private UserService userService = (UserService) injector.getInstance(UserService.class);
    private Map<String, Set<Role.RoleName>> protectedurls = new HashMap<>();

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        protectedurls.put("/users/all", Set.of(Role.RoleName.ADMIN));
        protectedurls.put("/products/admin", Set.of(Role.RoleName.ADMIN));
        protectedurls.put("/order/delete", Set.of(Role.RoleName.ADMIN));
        protectedurls.put("/orders/admin", Set.of(Role.RoleName.ADMIN));
        protectedurls.put("/users/delete", Set.of(Role.RoleName.ADMIN));
        protectedurls.put("/products/add", Set.of(Role.RoleName.ADMIN));
        protectedurls.put("/order/complete", Set.of(Role.RoleName.USER));
        protectedurls.put("/orders/by-user", Set.of(Role.RoleName.USER));
        protectedurls.put("/shopping-cart/products", Set.of(Role.RoleName.USER));
        protectedurls.put("/shopping-cart/products/add", Set.of(Role.RoleName.USER));
        protectedurls.put("/shopping-cart/products/delete", Set.of(Role.RoleName.USER));
        protectedurls.put("/products", Set.of(Role.RoleName.USER));
    }

    @Override
    public void doFilter(ServletRequest servletRequest,
                         ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse resp = (HttpServletResponse) servletResponse;
        String requestedUrl = req.getServletPath();
        if (protectedurls.get(requestedUrl) == null) {
            filterChain.doFilter(req, resp);
            return;
        }
        Long userId = (Long) req.getSession().getAttribute(USER_ID);
        User user = userService.get(userId);
        if (isAuthorized(user, protectedurls.get(requestedUrl))) {
            filterChain.doFilter(req, resp);
        } else {
            req.getRequestDispatcher("/WEB-INF/views/users/accessDenied.jsp").forward(req, resp);
        }
    }

    @Override
    public void destroy() {
    }

    private boolean isAuthorized(User user, Set<Role.RoleName> authorizedRoles) {
        for (Role.RoleName authRole : authorizedRoles) {
            for (Role userRole : user.getRoles()) {
                if (authRole.equals(userRole.getRoleName())) {
                    return true;
                }
            }
        }
        return false;
    }
}
