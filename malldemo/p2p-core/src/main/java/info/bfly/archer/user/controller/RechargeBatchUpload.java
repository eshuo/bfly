package info.bfly.archer.user.controller;

import info.bfly.archer.user.UserBillConstants.OperatorInfo;
import info.bfly.archer.user.model.User;
import info.bfly.archer.user.service.UserBillService;
import info.bfly.core.annotations.Log;
import info.bfly.core.annotations.ScopeType;
import info.bfly.core.jsf.util.FacesUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import org.slf4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import java.io.InputStream;
import java.io.Serializable;

@Component
@Scope(ScopeType.VIEW)
public class RechargeBatchUpload implements Serializable {
    private static final long serialVersionUID = 4497911706946758474L;
    @Log
    private static Logger log;
    @Resource
    private        UserBillService                ubs;
    @Resource
    private        HibernateTemplate              ht;
    /*
     * public static void main(String[] args) {
     * batchImport("d:\\1\\rechargeList.xls"); }
     */
    private        String                         fileName;
    private        UploadedFile                   file;

    public String batchImport() {
        if (file == null) {
            FacesUtil.addErrorMessage("未发现上传的文件！请先上传文件后，在导入表格！");
            return null;
        }
        int i = 1;
        try {
            InputStream fis = file.getInputstream();
            HSSFWorkbook wbs = new HSSFWorkbook(fis);
            HSSFSheet childSheet = wbs.getSheetAt(0);
            /*
             * if(log.isDebugEnabled()){ log.debug("总行数:" +
             * childSheet.getLastRowNum());
             *
             * }
             */
            // for(int i = 1;i<=childSheet.getLastRowNum();i++){
            HSSFRow row = childSheet.getRow(i);
            while (row != null) {
                row = childSheet.getRow(i);
                // System.out.println("列数:" + row.getPhysicalNumberOfCells());
                if (RechargeBatchUpload.log.isDebugEnabled()) {
                    RechargeBatchUpload.log.debug("当前行:" + row.getRowNum());
                }
                if (null != row) {
                    // FIXME: 判断用户是否存在
                    final String userId = StringUtils.trim(row.getCell(0).getStringCellValue());
                    if (StringUtils.isEmpty(userId)) {
                        break;
                    }
                    if (ht.get(User.class, userId) == null) {
                        FacesUtil.addInfoMessage("批量导入，导入第" + i + "行失败！失败原因：用户名不存在。请检查上传结果！");
                        return null;
                    }
                    try {
                        ubs.transferIntoBalance(StringUtils.trim(row.getCell(0).getStringCellValue()), row.getCell(1).getNumericCellValue(), OperatorInfo.ADMIN_OPERATION, row.getCell(2)
                                .getStringCellValue());
                    } catch (Exception e) {
                        RechargeBatchUpload.log.error("导入失败" + row.getRowNum(), e);
                        FacesUtil.addInfoMessage("批量导入失败！请检查上传结果！" + "失败行数第：" + i + "行。");
                        log.debug(e.getMessage());
                        return null;
                        // break ;
                    }
                    i++;
                }
            }
            // }
        } catch (Exception e) {
            log.debug(e.getMessage());
        }
        FacesUtil.addInfoMessage("文件上传成功！共导入" + i + "行。请检查上传结果！");
        // FIXME: 删除源文件
        return null;
    }

    public UploadedFile getFile() {
        return file;
    }

    public String getFileName() {
        return fileName;
    }

    public void handleFileUpload(FileUploadEvent event) {
        FacesMessage message = new FacesMessage("Succesful", event.getFile().getFileName() + " is uploaded.");
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
