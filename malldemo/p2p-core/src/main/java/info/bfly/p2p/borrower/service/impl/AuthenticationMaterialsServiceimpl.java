package info.bfly.p2p.borrower.service.impl;

import info.bfly.archer.picture.model.AutcMtrPicture;
import info.bfly.archer.picture.model.AutcMtrType;
import info.bfly.archer.picture.model.AuthenticationMaterials;
import info.bfly.core.util.IdGenerator;
import info.bfly.p2p.borrower.service.AuthenticationMaterialsService;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * Created by Administrator on 2017/6/2 0002.
 */
@Service("authenticationMaterialsService")
public class AuthenticationMaterialsServiceimpl implements AuthenticationMaterialsService {

    @Resource
    private HibernateTemplate ht;


    @Override
    @Transactional(readOnly = false)
    public AuthenticationMaterials initAutcMtr(String autcMtrTypeId) {
        AuthenticationMaterials autcMtr = new AuthenticationMaterials();
        autcMtr.setId(IdGenerator.randomUUID());
        autcMtr.setType(new AutcMtrType(autcMtrTypeId));
        return ht.merge(autcMtr);
    }

    @Override
    public void handleAutcMtrUpload(AuthenticationMaterials mutcMtr, String picturePath) {

        // 判断最大数量
        if (mutcMtr.getPictures().size() < mutcMtr.getType().getMaxNumber()) {
            // 保存上传图片
            AutcMtrPicture amPic = new AutcMtrPicture();
            amPic.setId(IdGenerator.randomUUID());
            amPic.setAutcMtr(mutcMtr);
            amPic.setPicture(picturePath);
            amPic.setSeqNum(mutcMtr.getPictures().size() + 1);
            mutcMtr.getPictures().add(amPic);
           ht.update(mutcMtr);
        } else {
            // 超出允许图片数量的上限。
//            throw new AnalyticParamExection("照片picture");
        }
    }
}
