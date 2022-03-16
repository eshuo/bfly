package info.bfly.p2p.borrower.service;

import info.bfly.archer.picture.model.AuthenticationMaterials;

/**
 * Created by Administrator on 2017/6/2 0002.
 */
public interface AuthenticationMaterialsService {



     AuthenticationMaterials initAutcMtr(String autcMtrTypeId);


    void handleAutcMtrUpload(AuthenticationMaterials mutcMtr, String picturePath);


}
