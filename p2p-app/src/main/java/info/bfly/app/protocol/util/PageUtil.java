package info.bfly.app.protocol.util;

import info.bfly.app.protocol.model.response.ResponsePage;

public class PageUtil {
    public static ResponsePage getPage(Integer curPage, Integer size, Integer count){
        ResponsePage responsePage =new ResponsePage();
        if(size!=null&&size>0){
            responsePage.setSize(size);
        }
        if(curPage!=null&&curPage>0){
            responsePage.setCurrentPage(curPage);
        }
        responsePage.setMaxSize(count);
        responsePage.init();
        return responsePage;
    }
}
