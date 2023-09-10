package dbs.bankingsystem.backend.config;

import dbs.bankingsystem.backend.entity.User;
import dbs.bankingsystem.backend.security.ActiveUserStore;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

/**
 * @author: ANG KUO SHENG CLEMENT
 * @date: 9-Sep-2023
 */

public class customisedAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    protected Log logger = LogFactory.getLog(this.getClass());
    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
    ActiveUserStore activeUserStore;
    @Autowired
    private Environment env;

    public customisedAuthenticationSuccessHandler() {
    }

    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        this.handle(request, response, authentication);
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.setMaxInactiveInterval(1800);
            String username;
            if (authentication.getPrincipal() instanceof User) {
                username = ((User)authentication.getPrincipal()).getEmail();
            } else {
                username = ((User)authentication.getPrincipal()).getUsername();
            }

            System.out.print("username:" + username);
            session.setAttribute("user", username);
        }

        this.clearAuthenticationAttributes(request);
    }

    protected void handle(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        this.addWelcomeCookie(this.getUserName(authentication), response);
        String targetUrl = this.determineTargetUrl(authentication);
        if (response.isCommitted()) {
            this.logger.debug("Response has already been committed. Unable to redirect to " + targetUrl);
        } else {
            this.redirectStrategy.sendRedirect(request, response, targetUrl);
        }
    }

    protected String determineTargetUrl(final Authentication authentication) {
        boolean isUser = false;
        boolean isAdmin = false;
        Map<String, String> roleTargetUrlMap = new HashMap();
        roleTargetUrlMap.put("ROLE_USER", "/flights/addFlights");
        roleTargetUrlMap.put("ROLE_ADMIN", "/flights/showFlights");
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator var6 = authorities.iterator();

        while(var6.hasNext()) {
            GrantedAuthority grantedAuthority = (GrantedAuthority)var6.next();
            String authorityName = grantedAuthority.getAuthority();
            if (roleTargetUrlMap.containsKey(authorityName)) {
                return (String)roleTargetUrlMap.get(authorityName);
            }

            if (grantedAuthority.getAuthority().equals("READ_PRIVILEGE")) {
                isUser = true;
            } else if (grantedAuthority.getAuthority().equals("WRITE_PRIVILEGE")) {
                isAdmin = true;
                isUser = false;
                break;
            }
        }

        if (isUser) {
            String username;
            if (authentication.getPrincipal() instanceof User) {
                username = ((User)authentication.getPrincipal()).getEmail();
            } else {
                username = authentication.getName();
            }

            return "/flights/addFlights";
        } else if (isAdmin) {
            return "/flights/showFlights";
        } else {
            throw new IllegalStateException();
        }
    }

    protected void clearAuthenticationAttributes(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.removeAttribute("SPRING_SECURITY_LAST_EXCEPTION");
        }
    }

    public void setRedirectStrategy(final RedirectStrategy redirectStrategy) {
        this.redirectStrategy = redirectStrategy;
    }

    protected RedirectStrategy getRedirectStrategy() {
        return this.redirectStrategy;
    }

    private String getUserName(final Authentication authentication) {
        return ((User)authentication.getPrincipal()).getEmail();
    }

    private void addWelcomeCookie(final String user, final HttpServletResponse response) {
        Cookie welcomeCookie = this.getWelcomeCookie(user);
        response.addCookie(welcomeCookie);
    }

    private Cookie getWelcomeCookie(final String user) {
        Cookie welcomeCookie = new Cookie("welcome", user);
        welcomeCookie.setMaxAge(2592000);
        return welcomeCookie;
    }
}
