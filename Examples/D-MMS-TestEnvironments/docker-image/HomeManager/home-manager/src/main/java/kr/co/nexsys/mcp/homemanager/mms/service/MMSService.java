package kr.co.nexsys.mcp.homemanager.mms.service;



import kr.co.nexsys.mcp.homemanager.exception.BusinessException;
import kr.co.nexsys.mcp.homemanager.mms.dao.MMSDao;
import kr.co.nexsys.mcp.homemanager.mms.dao.dvo.MMSDvo;
import kr.co.nexsys.mcp.homemanager.mms.service.vo.MMS;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
public class MMSService {

    private MMSDao mmsDao;

    @Autowired
    public MMSService(MMSDao mmsDao) {this.mmsDao = mmsDao;}

    //MMS 전체 조회
    @Transactional(readOnly = true)
    public List<MMS> findAllMMSs() {
        try {
            List<MMS> result = mmsDao.findAll().stream()
                                               .map(MMSService::valueOf)
                                               .collect(Collectors.toList());

            if(result.isEmpty()){
                throw new BusinessException("HM03001N");
            }else{
                return result;
            }
        }catch(BusinessException n){
            throw new BusinessException("HM03001N");
        }catch(Exception e){
            e.printStackTrace();
            throw new BusinessException("HM99001R");
        }
    }

    //MMS 조회
    @Transactional(readOnly = true)
    public MMS findMMSByMrn(String mrn) {
        try {
            return MMSService.valueOf(mmsDao.findOneMMSByMrn(mrn));
        }catch(NullPointerException n) {
            throw new BusinessException("HM03001N");
        }catch(Exception e){
            throw new BusinessException("HM99001R");
        }
    }

    //MMS 생성
    public MMS createMMS(MMS mms) {
        try {
            if(mmsDao.findOneMMSByMrn(mms.getMrn()) !=null){
                throw new BusinessException("HM02003R");
            }else {
                MMS result = MMSService.valueOf(mmsDao.saveAndFlush(MMSService.valueOf(mms)));
                return MMS.builder().mrn(result.getMrn())
                        .url(result.getUrl()).build();
            }
        }catch(BusinessException a){
            throw new BusinessException("HM02003R");
        }catch(Exception e){
            throw new BusinessException("HM99001R");
        }
    }

    //MMS 수정
    public MMS modifyMMS(String MMSMrn, String mrn, MMS mms) {
        try {
            if (MMSMrn.equals(mrn)) {
                //mrn에 해당하는 mms 조회
                MMS result = MMSService.valueOf(mmsDao.findOneMMSByMrn(mrn));

                result.setUrl(mms.getUrl());
                return MMSService.valueOf(mmsDao.saveAndFlush(MMSService.valueOf(result)));
            } else {
                throw new BusinessException("HM10002R");
            }
        }catch(BusinessException e){
            throw new BusinessException("HM10002R");
        }catch(NullPointerException n) {
            throw new BusinessException("HM03002N");
        }catch(Exception e){
            throw new BusinessException("HM99001R");
        }
    }

    //MMS 삭제
    public void removeMMS(String MMSMrn,String mrn) {
        try {
            if (MMSMrn.equals(mrn)) {
                //mrn에 해당하는 mms 조회
                MMS result = MMSService.valueOf(mmsDao.findOneMMSByMrn(mrn));
                mmsDao.delete(MMSService.valueOf(result));
            } else {
                throw new BusinessException("HM10002R");
            }
        }catch(BusinessException b){
            throw new BusinessException("HM10002R");
        }catch(NullPointerException n) {
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

    private static MMSDvo valueOf(MMS mms){
        return MMSDvo.builder()
                .mrn(mms.getMrn())
                .url(mms.getUrl())
                .build();
    }
}
