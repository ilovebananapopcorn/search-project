package com.sy.keyword.common.web.filter;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import java.io.IOException;
import java.util.UUID;

@Slf4j
@Component
public class MDCFilter implements Filter {

    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        MDC.put("uuid", UUID.randomUUID().toString().substring(0, 25));
        chain.doFilter(req, res);
        MDC.clear();
    }

}
