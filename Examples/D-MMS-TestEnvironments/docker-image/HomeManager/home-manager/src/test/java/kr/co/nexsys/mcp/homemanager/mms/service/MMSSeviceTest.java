package kr.co.nexsys.mcp.homemanager.mms.service;


import kr.co.nexsys.mcp.homemanager.mms.dao.MMSDao;
import kr.co.nexsys.mcp.homemanager.mms.dao.dvo.MMSDvo;
import kr.co.nexsys.mcp.homemanager.mms.service.vo.MMS;
import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MMSSeviceTest {
    @Mock
    private MMSDao mmsDao;

    @InjectMocks
    private MMSService mmsService;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    //전체 MMS 조회
    @Test
    public void shouldFindAllMMSs(){
        //given
        MMSDvo mmsDvo = MMSDvo.builder()
                              .mrn("urn:mrn:smart:test:test:test:test")
                              .url("http://www.test.com")
                              .createDate(LocalDateTime.now())
                              .build();
        when(mmsDao.findAll()).thenReturn(Lists.list(mmsDvo));
        //when
        List<MMS> list = mmsService.findAllMMSs();
        //then
        assertThat(list.get(0).getMrn(), equalTo(mmsDvo.getMrn()));
        assertThat(list.get(0).getUrl(), equalTo(mmsDvo.getUrl()));
    }

    //MMS조회
    @Test
    public void shouldFindOneMMSByMrn(){
        //given
        MMSDvo mmsDvo = MMSDvo.builder()
                              .mrn("urn:mrn:smart:test:test:test:test")
                              .url("http://www.test.com")
                              .createDate(LocalDateTime.now())
                              .build();
        when(mmsDao.findOneMMSByMrn(mmsDvo.getMrn())).thenReturn(mmsDvo);
        //when
        MMS mms = mmsService.findMMSByMrn(mmsDvo.getMrn());
        //then
        assertThat(mms.getMrn(), equalTo(mmsDvo.getMrn()));
        assertThat(mms.getUrl(), equalTo(mmsDvo.getUrl()));
    }

    //MMS생성
    @Test
    public void shouldCreateMMS(){
        //given
        MMSDvo mmsDvo = MMSDvo.builder()
                .mrn("urn:mrn:smart:test:test:test:test2")
                .url("http://www.test.com")
                .build();
        MMS mms = MMS.builder()
                .mrn("urn:mrn:smart:test:test:test:test2")
                .url("http://www.test.com")
                .build();

        when(mmsDao.saveAndFlush(mmsDvo)).thenReturn(mmsDvo);
        //when
        MMS result = mmsService.createMMS(mms);
        //then
        assertThat(result.getMrn(), equalTo(mmsDvo.getMrn()));
        assertThat(result.getUrl(), equalTo(mmsDvo.getUrl()));
    }

    //MMS 수정
    @Test
    public void shouldModifyMMS(){
        //given
        MMSDvo mmsDvo = MMSDvo.builder()
                .mrn("urn:mrn:smart:test:test:test:test2")
                .url("http://www.test.com")
                .build();
        MMS mms = MMS.builder()
                .mrn("urn:mrn:smart:test:test:test:test2")
                .url("http://www.test.com")
                .build();

        when(mmsDao.saveAndFlush(mmsDvo)).thenReturn(mmsDvo);
        when(mmsDao.findOneMMSByMrn(mms.getMrn())).thenReturn(mmsDvo);
        //when
        mms.setUrl("http://www.test222222.com");
        MMS result = mmsService.modifyMMS(mms.getMrn(),mms);
        //then
        assertThat(result.getMrn(), equalTo(mmsDvo.getMrn()));
        assertThat(result.getUrl(), equalTo(mmsDvo.getUrl()));
    }
}
