package kr.co.nexsys.mcp.homemanager.entity.dao;


import kr.co.nexsys.mcp.homemanager.entity.dao.dvo.HomeMMSDvo;
import kr.co.nexsys.mcp.homemanager.mms.dao.MMSDao;
import kr.co.nexsys.mcp.homemanager.mms.dao.dvo.MMSDvo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
public class HomeMMSDaoTest {

    @Autowired
    private HomeMMSDao homeMMSDao;

    @Autowired
    private MMSDao mmsDao;

    //HomeMMS 조회
    @Test
    public void shouldFind(){
        //given
        HomeMMSDvo homeMMSDvo = HomeMMSDvo.builder()
                                    .mrn_mms("urn:mrn:smart:test:test:test:mmss")
                                    .mrn("urn:mrn:smart:test:test:test:entity")
                                    .build();
        MMSDvo mmsDvo = MMSDvo.builder()
                            .mrn("urn:mrn:smart:test:test:test:mmss")
                            .url("http://www.test.com")
                            .build();
        MMSDvo mmsGigven = mmsDao.saveAndFlush(mmsDvo);
        HomeMMSDvo given = homeMMSDao.saveAndFlush(homeMMSDvo);
        //when
        HomeMMSDvo result = homeMMSDao.findOneHomeMMSByMrn("urn:mrn:smart:test:test:test:entity");
        //then
        assertThat(result.getMrn_mms(),equalTo(given.getMrn_mms()));
        assertThat(result.getMrn(),equalTo(given.getMrn()));
    }

    //HomeMMS 생성
    @Test
    public void shouldSave(){
        //given
        HomeMMSDvo homeMMSDvo = HomeMMSDvo.builder()
                .mrn_mms("urn:mrn:smart:test:test:test:mmss")
                .mrn("urn:mrn:smart:test:test:test:entity")
                .build();
        MMSDvo mmsDvo = MMSDvo.builder()
                .mrn("urn:mrn:smart:test:test:test:mmss")
                .url("http://www.test.com")
                .build();
        MMSDvo mmsGigven = mmsDao.saveAndFlush(mmsDvo);
        //when
        HomeMMSDvo result = homeMMSDao.saveAndFlush(homeMMSDvo);
        //then
        assertThat(result.getMrn(),notNullValue());
        assertThat(result.getMrn_mms(),notNullValue());
    }

    //HomeMMS 수정
    @Test
    public void shouldModify(){
        //given
        HomeMMSDvo homeMMSDvo = HomeMMSDvo.builder()
                .mrn_mms("urn:mrn:smart:test:test:test:mmss")
                .mrn("urn:mrn:smart:test:test:test:entity")
                .build();
        MMSDvo mmsDvo = MMSDvo.builder()
                .mrn("urn:mrn:smart:test:test:test:mmss")
                .url("http://www.test.com")
                .build();
        MMSDvo mmsDvoModified = MMSDvo.builder()
                .mrn("urn:mrn:smart:test:test:test:modified")
                .url("http://www.test22222222222.com")
                .build();
        mmsDao.saveAndFlush(mmsDvo);
        MMSDvo mmsGigven = mmsDao.saveAndFlush(mmsDvoModified);
        HomeMMSDvo given1 = homeMMSDao.saveAndFlush(homeMMSDvo);
        HomeMMSDvo given2 = homeMMSDao.saveAndFlush(homeMMSDvo);
        //when
        given1.setMrn_mms("urn:mrn:smart:test:test:test:modified");
        HomeMMSDvo result = homeMMSDao.saveAndFlush(given1);
        //then
        assertThat(result.getMrn_mms(),equalTo(given2.getMrn_mms()));
    }

    //HomeMMS 삭제
    @Test
    public void shouldDelete(){
        //given
        HomeMMSDvo homeMMSDvo = HomeMMSDvo.builder()
                .mrn_mms("urn:mrn:smart:test:test:test:mmss")
                .mrn("urn:mrn:smart:test:test:test:entity")
                .build();
        MMSDvo mmsDvo = MMSDvo.builder()
                .mrn("urn:mrn:smart:test:test:test:mmss")
                .url("http://www.test.com")
                .build();
        mmsDao.saveAndFlush(mmsDvo);
        HomeMMSDvo given = homeMMSDao.saveAndFlush(homeMMSDvo);
        //when
        homeMMSDao.delete(given);
        //then
        assertThat(homeMMSDao.findOneHomeMMSByMrn(given.getMrn()),nullValue());
    }

}
