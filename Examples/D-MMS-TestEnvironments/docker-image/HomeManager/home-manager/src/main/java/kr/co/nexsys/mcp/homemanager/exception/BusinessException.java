package kr.co.nexsys.mcp.homemanager.exception;

import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {
    private String code;

    public BusinessException(String code){
        this.code = code;
    }

}
