package kr.co.nexsys.mcp.homemanager.entity.service;

import kr.co.nexsys.mcp.homemanager.exception.BusinessException;
import kr.co.nexsys.mcp.homemanager.entity.dao.HomeMMSDao;
import kr.co.nexsys.mcp.homemanager.entity.dao.dvo.HomeMMSDvo;
import kr.co.nexsys.mcp.homemanager.entity.service.vo.HomeMMS;
import kr.co.nexsys.mcp.homemanager.mms.dao.MMSDao;
import kr.co.nexsys.mcp.homemanager.mms.dao.dvo.MMSDvo;
import kr.co.nexsys.mcp.homemanager.mms.service.vo.MMS;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.BatchUpdateException;

@Slf4j
@Service
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
public class HomeMMSService {

    @Autowired
    private HomeMMSDao homeMMSDao;

    @Autowired
    private MMSDao mmsDao;

    public HomeMMSService(HomeMMSDao homeMMSDao) {this.homeMMSDao = homeMMSDao;}

    //homeMMS 조회
    public MMS findHomeMMS(String mrn){
        try{
             HomeMMSDvo homeMMSDvo =homeMMSDao.findOneHomeMMSByMrn(mrn);
             return HomeMMSService.valueOf(mmsDao.findOneMMSByMrn(homeMMSDvo.getMrn_mms()));
        }catch(NullPointerException n){
            throw new BusinessException("HM03002N");
        }catch(Exception e){
            throw new BusinessException("HM99001R");
        }
    }

    //homeMMS 생성
    public HomeMMS createHomeMMS(HomeMMS homeMMS){
       try{
           if(homeMMSDao.findOneHomeMMSByMrn(homeMMS.getMrn()) !=null){
               throw new BusinessException("HM02003R");
           }else{
             //  if(mmsDao.findOneMMSByMrn(homeMMS.getHomeMmsMrn()) ==null){
             //      throw new NullResultException("HM03003N");
             //  }else{
                   return HomeMMSService.valueOf(homeMMSDao.saveAndFlush(HomeMMSService.valueOf(homeMMS)));
             //  }
           }
       }catch(BusinessException a){
           throw new BusinessException("HM02003R");
       }catch(NullPointerException n){
           throw new BusinessException("HM03003N");
       }catch(Exception e){
           throw new BusinessException("HM99001R");
       }
    }

    //homeMMS 수정
    public HomeMMS modifyHomeMMS(String mrn, HomeMMS homeMMS){
        try{
            //MMSDvo mmsDvo =mmsDao.findOneMMSByMrn(homeMMS.getHomeMmsMrn());
            HomeMMSDvo homeMMSDvo =homeMMSDao.findOneHomeMMSByMrn(mrn);
            //entity mrn이나 mms mrn 중 둘중 하나라도 존재하지 않으면 HM03002N 에러 출력
            //if(mmsDvo==null || homeMMSDvo==null){
            if(homeMMSDvo == null){
                throw new BusinessException("HM03002N");
            }else{
                homeMMSDvo.setMrn_mms(homeMMS.getHomeMmsMrn());
                return HomeMMSService.valueOf(homeMMSDao.saveAndFlush(homeMMSDvo));
            }
        }catch(BusinessException n){
            throw new BusinessException("HM03002N");
        }catch(Exception e){
            throw new BusinessException("HM99001R");
        }
    }

    //homeMMS 삭제
    public void deleteHomeMMS(String mrn, String MmsMrn){
        try{
            if(MmsMrn.equals(mrn)){
                HomeMMS homeMMS = HomeMMSService.valueOf(homeMMSDao.findOneHomeMMSByMrn(mrn));
                homeMMSDao.delete(HomeMMSService.valueOf(homeMMS));
            }else{
                throw new BusinessException("HM10002R");
            }
        }catch(BusinessException b){
            throw new BusinessException("HM10002R");
        }catch(NullPointerException n){
            throw new BusinessException("HM03002N");
        }catch(Exception e){
            throw new BusinessException("HM99001R");
        }
    }

    private static MMS valueOf(MMSDvo mmsDvo){
        return MMS.builder()
                .mrn(mmsDvo.getMrn())
                .url(mmsDvo.getUrl())
                .build();
    }
    private static HomeMMSDvo valueOf(HomeMMS homeMMS){
        return HomeMMSDvo.builder()
                .mrn(homeMMS.getMrn())
                .mrn_mms(homeMMS.getHomeMmsMrn())
                .type(homeMMS.getType())
                .build();
    }
    private static HomeMMS valueOf(HomeMMSDvo homeMMSDvo){
        return HomeMMS.builder()
                .mrn(homeMMSDvo.getMrn())
                .homeMmsMrn(homeMMSDvo.getMrn_mms())
                .build();
    }

}
