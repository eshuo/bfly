package info.bfly.archer.system.service;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public interface AppLocalFilter {
    void doFilter(ServletRequest request, ServletResponse response, FilterChain filter) throws IOException, ServletException;
}
