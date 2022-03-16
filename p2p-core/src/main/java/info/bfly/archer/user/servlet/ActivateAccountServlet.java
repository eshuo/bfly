package info.bfly.archer.user.servlet;

import info.bfly.archer.common.exception.*;
import info.bfly.archer.user.exception.UserNotFoundException;
import info.bfly.archer.user.service.UserService;
import info.bfly.core.annotations.Log;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 邮件激活账号
 *
 * @author yinjunlu
 *
 */
@Component
public class ActivateAccountServlet extends HttpServlet {
    private static final long serialVersionUID = -6074598131359403903L;
    @Log
    static Logger log;
    @Resource
    UserService userService;

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String code = request.getParameter("activeCode");
        if (StringUtils.isNotEmpty(code)) {
            try {
                userService.activateUserByEmailActiveCode(code);
                response.sendRedirect(request.getContextPath() + "/regSuccess");
            } catch (AuthCodeNotMatchException|AuthInfoOutOfDateException | AuthInfoOutOfTimesException | NoMatchingObjectsException e) {
                response.sendRedirect(request.getContextPath() + "/activefail");
            } catch (UserNotFoundException e) {
                response.sendRedirect(request.getContextPath() + "/activefail");
            } catch (AuthInfoAlreadyActivedException e) {
                response.sendRedirect(request.getContextPath() + "/regSuccess");
            }
        } else {
            response.sendRedirect(request.getContextPath() + "/activefail");
        }
    }

    @Override
    public void init() throws ServletException {
        // Put your code here
    }
}
