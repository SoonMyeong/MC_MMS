package kr.co.nexsys.mcp.homemanager.entity.service;

import kr.co.nexsys.mcp.homemanager.entity.dao.HomeMMSDao;
import kr.co.nexsys.mcp.homemanager.entity.dao.dvo.HomeMMSDvo;
import kr.co.nexsys.mcp.homemanager.entity.service.vo.HomeMMS;
import kr.co.nexsys.mcp.homemanager.mms.dao.MMSDao;
import kr.co.nexsys.mcp.homemanager.mms.dao.dvo.MMSDvo;
import kr.co.nexsys.mcp.homemanager.mms.service.vo.MMS;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;


import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class HomeMMSServiceTest {

    @Mock
    private HomeMMSDao homeMMSDao;

    @Mock
    private MMSDao mmsDao;

    @InjectMocks
    private HomeMMSService homeMMSService;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    //HomeMMS 조회
    @Test
    public void shouldFindHomeMMS(){
        //given
        String mrn = "urn:mrn:smart:test:test:test:entity";
        HomeMMSDvo homeMMSDvo = HomeMMSDvo.builder()
                                .mrn("urn:mrn:smart:test:test:test:entity")
                                .mrn_mms("urn:mrn:smart:test:test:test:mmss")
                                .build();
        MMSDvo mmsDvo = MMSDvo.builder()
                                .mrn("urn:mrn:smart:test:test:test:mmss")
                                .url("http://www.test.com")
                                .build();
        when(homeMMSDao.findOneHomeMMSByMrn(mrn)).thenReturn(homeMMSDvo);
        when(mmsDao.findOneMMSByMrn(homeMMSDvo.getMrn_mms())).thenReturn(mmsDvo);
        //when
        MMS mms = homeMMSService.findHomeMMS(homeMMSDvo.getMrn());
        //then
        assertThat(mms.getMrn(), equalTo(mmsDvo.getMrn()));
        assertThat(mms.getUrl(), equalTo(mmsDvo.getUrl()));
    }

    //HomeMMS 생성
    @Test
    public void shoudCreateHomeMMS(){
        //given
        HomeMMSDvo homeMMSDvo = HomeMMSDvo.builder()
                .mrn("urn:mrn:smart:test:test:test:entity")
                .mrn_mms("urn:mrn:smart:test:test:test:mmss")
                .build();
        HomeMMS homeMMS = HomeMMS.builder()
                .mrn("urn:mrn:smart:test:test:test:entity")
                .homeMmsMrn("urn:mrn:smart:test:test:test:mmss")
                .build();
        when(homeMMSDao.saveAndFlush(homeMMSDvo)).thenReturn(homeMMSDvo);
        //when
        HomeMMS result =homeMMSService.createHomeMMS(homeMMS);
        //then
        assertThat(result.getMrn(),equalTo(homeMMSDvo.getMrn()));
        assertThat(result.getHomeMmsMrn(),equalTo(homeMMSDvo.getMrn_mms()));
    }

    //HomeMMS 수정
    @Test
    public void shouldModifyHomeMMS(){
        //given
        String mrn = "urn:mrn:smart:test:test:test:entity";
        HomeMMSDvo homeMMSDvo = HomeMMSDvo.builder()
                .mrn("urn:mrn:smart:test:test:test:entity")
                .mrn_mms("urn:mrn:smart:test:test:test:mmss")
                .build();
        HomeMMS homeMMSModify = HomeMMS.builder()
                .homeMmsMrn("urn:mrn:smart:test:test:test:modified")
                .build();
        MMSDvo mmsDvo = MMSDvo.builder()
                .mrn("urn:mrn:smart:test:test:test:modified")
                .url("http://www.test.com")
                .build();
        when(mmsDao.findOneMMSByMrn(homeMMSModify.getHomeMmsMrn())).thenReturn(mmsDvo);
        when(homeMMSDao.findOneHomeMMSByMrn(mrn)).thenReturn(homeMMSDvo);
        homeMMSDvo.setMrn_mms("urn:mrn:smart:test:test:test:modified");
        when(homeMMSDao.saveAndFlush(homeMMSDvo)).thenReturn(homeMMSDvo);
        //when
        HomeMMS result = homeMMSService.modifyHomeMMS(mrn,homeMMSModify);
        //then
        assertThat(result.getHomeMmsMrn(),equalTo(homeMMSDvo.getMrn_mms()));
    }

}
