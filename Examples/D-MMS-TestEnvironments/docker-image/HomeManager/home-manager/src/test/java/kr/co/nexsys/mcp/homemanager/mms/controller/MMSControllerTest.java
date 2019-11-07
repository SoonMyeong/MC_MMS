package kr.co.nexsys.mcp.homemanager.mms.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.nexsys.mcp.homemanager.mms.controller.dto.MMSDto;
import kr.co.nexsys.mcp.homemanager.mms.controller.dto.MMSModifyReqDto;
import kr.co.nexsys.mcp.homemanager.mms.service.MMSService;
import kr.co.nexsys.mcp.homemanager.mms.service.vo.MMS;
import org.assertj.core.util.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.test.web.servlet.ResultActions;



import static org.hamcrest.Matchers.equalTo;
import static org.mockito.BDDMockito.given;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(MMSController.class)
public class MMSControllerTest {

    @Autowired
    MockMvc mockMvc;
    @MockBean
    MMSService mmsService;
    @Autowired
    ObjectMapper objectMapper;

    //전체 MMS 조회
    @Test
    public void findAllMMSs() throws Exception{
        //given
        MMS mms = MMS.builder()
                .mrn("urn:home:mms:test:test-0.0.1v")
                .url("http://www.test.com")
                .build();
        given(mmsService.findAllMMSs()).willReturn(Lists.list(mms));
        //when
        ResultActions resultActions = mockMvc.perform(get("/mms")).andDo(print());
        //then
        resultActions.andExpect(status().isOk())
                     .andExpect(jsonPath("$.mmslist.[0].mrn", equalTo(mms.getMrn())))
                     .andExpect(jsonPath("$.mmslist.[0].ip", equalTo(mms.getUrl())));
    }

    //MRN으로부터 MMS 조회
    @Test
    public void findMMS() throws Exception{
        //given
        String mrn = "urn:home:mms:test:test:test:test";
        MMS mms = MMS.builder()
                .mrn(mrn)
                .url("http://www.test.com")
                .build();
        given(mmsService.findMMSByMrn(mrn)).willReturn(mms);
        //when
        ResultActions resultActions = mockMvc.perform(get("/mms/"+mrn)
                                             .contentType(MediaType.APPLICATION_JSON_UTF8))
                                             .andDo(print());
        //then
        resultActions.andExpect(status().isOk())
                     .andExpect(jsonPath("$.mmsDto.mrn", equalTo(mms.getMrn())))
                     .andExpect(jsonPath("$.mmsDto.ip", equalTo(mms.getUrl())));
    }

    //MMS 생성
    @Test
    public void createMMS() throws Exception{
        //given
        MMS mms = MMS.builder()
                .mrn("urn:mrn:smart:test:test:test:test")
                .url("http://www.test.com")
                .build();
        given(mmsService.createMMS(mms)).willReturn(mms);
        //when
        ResultActions resultActions = mockMvc.perform(post("/mms")
                                             .contentType(MediaType.APPLICATION_JSON_UTF8)
                                             .content(objectMapper.writeValueAsString(mms)))
                                             .andDo(print());
        //then
        resultActions.andExpect(status().isOk())
                     .andExpect(jsonPath("$.mmsDto.mrn", equalTo(mms.getMrn())))
                     .andExpect(jsonPath("$.mmsDto.ip", equalTo(mms.getUrl())));

    }

    //MMS 수정
    @Test
    public void modifyMMS() throws Exception{
        //given
        String mrn = "urn:home:mms:test:test-0.0.2v";
        MMS mms = MMS.builder()
                .url("http://www.test.com")
                .build();
        //when
        ResultActions resultActions = mockMvc.perform(put("/mms/"+mrn)
                                             .contentType(MediaType.APPLICATION_JSON_UTF8)
                                             .content(objectMapper.writeValueAsString(mms)))
                                             .andDo(print());
        //then
        resultActions.andExpect(status().isOk())
                    .andExpect(jsonPath("$.mmsDto.mrn", equalTo(mms.getMrn())))
                    .andExpect(jsonPath("$.mmsDto.ip", equalTo(mms.getUrl())));
    }

    //삭제
    @Test
    public void removeMMS() throws Exception{
        //given
        String mrn = "urn:home:mms:test:test-0.0.2v";
        //when
        ResultActions resultActions = mockMvc.perform(delete("/mms/"+mrn)).andDo(print());
        //then
        resultActions.andExpect(status().isOk());
    }
}
