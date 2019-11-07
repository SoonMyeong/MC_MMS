package kr.co.nexsys.mcp.homemanager.entity.controller;


import kr.co.nexsys.mcp.homemanager.authentication.ClientVerifier;
import kr.co.nexsys.mcp.homemanager.entity.controller.dto.*;
import kr.co.nexsys.mcp.homemanager.entity.service.HomeMMSService;
import kr.co.nexsys.mcp.homemanager.entity.service.vo.HomeMMS;
import kr.co.nexsys.mcp.homemanager.mms.controller.dto.MMSDto;
import kr.co.nexsys.mcp.homemanager.mms.service.vo.MMS;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/entity")
public class HomeMMSController {

    @Autowired
    private HomeMMSService homeMMSService;

    @Autowired
    private ClientVerifier clientVerifier;

    @GetMapping("/{mrn}/home-mms")
    public ResponseEntity<HomeMMSFindResDto> findHomeMMS(@PathVariable("mrn") String mrn){

        return ResponseEntity.ok(HomeMMSFindResDto.builder()
                                    .mrn(mrn)
                                    .homeMmsDto(HomeMMSController.valueOf(homeMMSService.findHomeMMS(mrn)))
                                    .build());
    }

    @PostMapping("/home-mms")
    public ResponseEntity<HomeMMSCreateResDto> createHomeMMS(@RequestHeader(value = "MMS-MRN") String MmsMrn,
                                                             @RequestBody @Valid HomeMMSCreateReqDto homeMMSCreateReqDto){

        return ResponseEntity.ok(HomeMMSCreateResDto.builder()
                    .homeMmsDto(HomeMMSController.valueOf(homeMMSService.createHomeMMS(HomeMMSController.valueOf(MmsMrn,homeMMSCreateReqDto))))
                    .build());

    }

   /* @PutMapping("/{mrn}/home-mms")
    public ResponseEntity<HomeMMSModifyResDto> modifyHomeMMS(@PathVariable("mrn") String mrn,
                                                             @RequestBody @Valid HomeMMSModifyReqDto homeMMSModifyReqDto){

        return ResponseEntity.ok(HomeMMSModifyResDto.builder()
                                    .homeMmsDto(HomeMMSController.valueOf(homeMMSService.modifyHomeMMS(mrn,HomeMMSController.valueOf(homeMMSModifyReqDto))))
                                    .build());
    }*/

    @DeleteMapping("/{mrn}/home-mms")
    public ResponseEntity<?> deleteHomeMMS(@PathVariable("mrn") String mrn,
                                           @RequestHeader(value = "MMS-MRN") String MmsMrn,
                                           @RequestBody HomeMMSDeleteReqDto homeMMSDeleteReqDto){

        homeMMSService.deleteHomeMMS(mrn,MmsMrn);
        return ResponseEntity.ok("DELETE OK");

    }

    private static MMSDto valueOf(MMS mms){
        return MMSDto.builder()
                .mrn(mms.getMrn())
                .url(mms.getUrl())
                .build();
    }

    private static HomeMMSDto valueOf(HomeMMS homeMMS){
        return HomeMMSDto.builder()
                    .mrn(homeMMS.getMrn())
                    .homeMmsMrn(homeMMS.getHomeMmsMrn())
                    .build();
    }
    private static HomeMMS valueOf(String MmsMrn, HomeMMSCreateReqDto homeMMSCreateReqDto){
        return HomeMMS.builder()
                    .mrn(homeMMSCreateReqDto.getMrn())
                    .homeMmsMrn(MmsMrn)
                    .type(homeMMSCreateReqDto.getType())
                    .build();
    }
    private static HomeMMS valueOf(HomeMMSModifyReqDto homeMMSModifyReqDto){
        return HomeMMS.builder()
                .homeMmsMrn(homeMMSModifyReqDto.getHomeMmsMrn())
                .build();
    }

}
