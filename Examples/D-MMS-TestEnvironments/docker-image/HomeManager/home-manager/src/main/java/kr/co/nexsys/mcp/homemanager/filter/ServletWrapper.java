package kr.co.nexsys.mcp.homemanager.filter;

import kr.co.nexsys.mcp.homemanager.authentication.ClientVerifier;

import javax.servlet.ReadListener;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.CharBuffer;


public class ServletWrapper extends HttpServletRequestWrapper {
    private final String body;

    private final ClientVerifier clientVerifier = new ClientVerifier();

    public ServletWrapper(HttpServletRequest request) throws IOException, ServletException {
        super(request);

        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader bufferedReader = null;
        try {
            InputStream inputStream = request.getInputStream();
            if (inputStream != null) {
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                CharBuffer charBuffer = CharBuffer.allocate(128);
                int bytesRead = -1;


                while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
                    stringBuilder.append(charBuffer, 0, bytesRead);
                }
            } else {
                stringBuilder.append("");
            }
        } catch (IOException e) {
            throw e;
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    throw e;
                }
            }
        }
        body = stringBuilder.toString();
    }

    public static boolean authenticationMMS(HttpServletRequest request) {
    /*    if (request.getMethod().equals("POST") || request.getMethod().equals("PUT") || request.getMethod().equals("DELETE")) {
            if (clientVerifier.verifyClient(request.getHeader("MMS-MRN"), request.getHeader("certificate"))) {
                return true;
            }
            return false;
        } else {
            return true;
        }*/

        return true;
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(body.getBytes());
        ServletInputStream servletInputStream = new ServletInputStream() {
            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setReadListener(ReadListener listener) {

            }

            @Override
            public int read() throws IOException {
                return byteArrayInputStream.read();
            }
        };
        return servletInputStream;
    }

    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(this.getInputStream()));
    }

    //Use this method to read the request body
    public String getBody() {
        return this.body;
    }
}
