package kr.co.nexsys.mcp.homemanager.mms.controller;

import kr.co.nexsys.mcp.homemanager.authentication.ClientVerifier;
import kr.co.nexsys.mcp.homemanager.exception.BusinessException;
import kr.co.nexsys.mcp.homemanager.exception.ErrorCode;
import kr.co.nexsys.mcp.homemanager.exception.ErrorCodeDto;
import kr.co.nexsys.mcp.homemanager.mms.controller.dto.*;
import kr.co.nexsys.mcp.homemanager.mms.service.MMSService;
import kr.co.nexsys.mcp.homemanager.mms.service.vo.MMS;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/mms")
public class MMSController {

    @Autowired
    private MMSService mmsService;

    @Autowired
    private ClientVerifier clientVerifier;

    @GetMapping
    public ResponseEntity<MMSFindAllResDto> findAllMMSs(){
        log.debug("mmsList all find");

        return ResponseEntity.ok(MMSFindAllResDto.builder()
                                    .MMSList(mmsService.findAllMMSs().stream()
                                        .map(MMSController::valueOf)
                                        .collect(Collectors.toList())).build());
    }

    @GetMapping(value = "/{mrn}")
    public ResponseEntity<MMSFindOneResDto> findMMS(@PathVariable("mrn") String mrn){
        log.debug("find mms, mrn :" + mrn);

        return ResponseEntity.ok(MMSFindOneResDto.builder()
                                    .mmsDto(MMSController.valueOf(mmsService.findMMSByMrn(mrn)))
                                    .build());
    }

    @PostMapping
    public ResponseEntity<MMSCreateResDto> createMMS(@RequestHeader(value = "MMS-MRN") String MMSMrn,
                                                     @RequestBody MMSCreateReqDto mmsCreateReqDto){
        log.debug("insert Data :" + mmsCreateReqDto.toString());

        String url = clientVerifier.getAutenticationUrl(MMSMrn,mmsCreateReqDto.getCertificate());
        return ResponseEntity.ok(MMSCreateResDto.builder()
                    .mmsDto(MMSController.valueOf(mmsService.createMMS(MMSController.valueOf(MMSMrn,url))))
                    .build());

    }

    @PutMapping(value = "/{mrn}")
    public ResponseEntity<MMSModifyResDto> modifyMMS(@PathVariable("mrn") String mrn,
                                                     @RequestHeader(value = "MMS-MRN") String MMSMrn,
                                                     @RequestBody MMSModifyReqDto mmsModifyReqDto){
        log.debug("update Data :" + mmsModifyReqDto +", mrn :" + mrn);

        String url = clientVerifier.getAutenticationUrl(MMSMrn,mmsModifyReqDto.getCertificate());
        return ResponseEntity.ok(MMSModifyResDto.builder()
                        .mmsDto(MMSController.valueOf(mmsService.modifyMMS(MMSMrn,mrn,MMSController.valueOf(url))))
                        .build());

    }

    @DeleteMapping(value = "/{mrn}")
    public ResponseEntity<?> removeMMS(@PathVariable("mrn") String mrn,
                                       @RequestHeader(value = "MMS-MRN")String MMSMrn,
                                       @RequestBody MMSDeleteReqDto mmsDeleteReqDto){
        log.debug("delete mrn :" + mrn);

        mmsService.removeMMS(MMSMrn,mrn);
        return ResponseEntity.ok("OK");

    }


    private static MMS valueOf(String MMSMrn, String url){
        return MMS.builder()
                .mrn(MMSMrn)
                .url(url)
                .build();
    }
    private static MMS valueOf(String url){
        return MMS.builder()
                .url(url)
                .build();
    }

    private static MMSDto valueOf(MMS mms){
        return MMSDto.builder()
                .mrn(mms.getMrn())
                .url(mms.getUrl())
                .build();
    }

}
