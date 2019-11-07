package kr.co.nexsys.mcp.homemanager.filter;


import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

/*@Component
@WebFilter(urlPatterns = {"/mms/**", "/entity/**"})*/
public class ServletFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
/*        ServletWrapper servletWrapper = new ServletWrapper((HttpServletRequest) request);
        HttpServletResponseWrapper httpServletResponse = new HttpServletResponseWrapper((HttpServletResponse) response);

        System.out.println("MMS-MRN:{}" + ((HttpServletRequest) request).getHeader("MMS-MRN"));
        System.out.println(("certificate:{}" + ((HttpServletRequest) request).getHeader("certificate")));

        try {
            if (servletWrapper.authenticationMMS((HttpServletRequest) request)) {
                chain.doFilter(servletWrapper, response);
            } else {
                throw new BusinessException("HM10001R");
            }
        } catch (BusinessException e) {
            response.setContentType("application/json");
            ((HttpServletResponse) response).sendError(HttpStatus.UNAUTHORIZED.value());
        }*/
    }

}
