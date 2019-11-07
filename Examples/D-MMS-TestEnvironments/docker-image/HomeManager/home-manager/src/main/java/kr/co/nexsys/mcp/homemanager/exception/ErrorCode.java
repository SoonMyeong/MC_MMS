package kr.co.nexsys.mcp.homemanager.exception;

import lombok.Getter;


@Getter
public enum ErrorCode {

    NOT_FOUND_PARAMETER("HM02001R", "Required input is missing."),  //필수 입력 값이 누락 되었습니다.
    INVALID_PARAMETER("HM02002R", "The data format is incorrect."), //데이터 형식이 틀립니다.
    ALREADY_EXIST_DATA("HM02003R","The data already exists."), //이미 존재하는 데이터 입니다.

    NO_RESULT_DATA("HM03001N", "No data was queried."), //조회된 데이터가 없습니다.
    NO_MRN_DATA("HM03002N", "MRN does not exist."), //MRN이 존재하지 않습니다.
    NO_REIGSTER_DATA("HM03003N", "Unregistered MMS MRN"), // 등록되지 않은 MMS MRN 입니다.

    ERROR_AUTH("HM10001R","An authentication error has occurred."), //인증서 오류가 발생하였습니다.
    NO_PERMISSION("HM10002R","You do not have permission."), // 권한이 없습니다.

    ERROR_SYSTEM("HM99001R", "A system error has occurred."); //시스템 오류가 발생하였습니다.


    private final String code;
    private final String message;

    ErrorCode(String code, String message){
        this.code = code;
        this.message = message;
    }
}
