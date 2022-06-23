package info.bfly.archer.user.controller;

import info.bfly.archer.common.controller.EntityQuery;
import info.bfly.archer.system.controller.DictUtil;
import info.bfly.archer.user.model.User;
import info.bfly.archer.user.model.UserBill;
import info.bfly.core.annotations.Log;
import info.bfly.core.annotations.ScopeType;
import info.bfly.core.jsf.util.FacesUtil;
import info.bfly.core.util.DateUtil;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Component
@Scope(ScopeType.VIEW)
public class UserBillList<E extends UserBill> extends EntityQuery<UserBill> implements Serializable {
    private Date     startTime;
    private Date     endTime;
    @Log
    Logger log;
    @Autowired
    private DictUtil dictUtil;

    public UserBillList() {
        final String[] RESTRICTIONS = { "id like #{userBillList.example.id}", "user.id like #{userBillList.example.user.id}", "type like #{userBillList.example.type}",
                "typeInfo like #{userBillList.example.typeInfo}", "time >= #{userBillList.startTime}", "time <= #{userBillList.endTime}" };
        setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
        // addOrder("time", super.DIR_DESC);
    }

    /**
     * 导出
     */
    public void export() {
        UserBill userBill = getExample();
        StringBuilder hql = new StringBuilder("from UserBill where 1=1");
        if (userBill.getUser().getId() != null && userBill.getUser().getId().length() > 0) {
            hql.append(" and user.id='").append(userBill.getUser().getId()).append("'");
        }
        if (userBill.getTypeInfo() != null && userBill.getTypeInfo().length() > 0) {
            hql.append(" and typeInfo='").append(userBill.getTypeInfo()).append("'");
        }
        if (startTime != null && endTime != null) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            hql.append(" and time between ").append(format.format(startTime)).append(" and ").append(format.format(endTime));
        }
        List<UserBill> list = (List<UserBill>) getHt().find(hql.toString());
        if (list != null && list.size() > 0) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            DecimalFormat numberFormat = new DecimalFormat("0.00");
            String[] excelHeader = { "时间", "类型|明细", "金额", "可用金额", "冻结金额", "备注" };
            HSSFWorkbook wb = new HSSFWorkbook();
            HSSFSheet sheet = wb.createSheet("user_bill");
            HSSFRow row = sheet.createRow(0);
            for (int i = 0; i < excelHeader.length; i++) {
                HSSFCell cell = row.createCell(i);
                cell.setCellValue(excelHeader[i]);
                sheet.autoSizeColumn(i);
            }
            for (int i = 0; i < list.size(); i++) {
                UserBill bill = list.get(i);
                row = sheet.createRow(i + 1);
                row.createCell(0).setCellValue(format.format(bill.getTime()));
                row.createCell(1).setCellValue(dictUtil.getValue("bill_operator", bill.getTypeInfo()));
                row.createCell(2).setCellValue(numberFormat.format(bill.getMoney()));
                row.createCell(3).setCellValue(numberFormat.format(bill.getBalance()));
                row.createCell(4).setCellValue(numberFormat.format(bill.getFrozenMoney()));
                row.createCell(5).setCellValue(bill.getDetail());
            }
            HttpServletResponse response = FacesUtil.getHttpServletResponse();
            response.setContentType("application/vnd.ms-excel");
            OutputStream stream = null;
            try {
                String filename = "账户流水_" + userBill.getUser().getId() + ".xls";
                String agent = FacesUtil.getHttpServletRequest().getHeader("USER-AGENT");
                if (null != agent && -1 != agent.indexOf("MSIE")) {
                    filename = URLEncoder.encode(filename, "utf-8");
                }
                else {
                    filename = new String(filename.getBytes("utf-8"), "iso8859-1");
                }
                response.setHeader("Content-disposition", "attachment;filename=" + filename);
                stream = response.getOutputStream();
                wb.write(stream);
                stream.flush();
                stream.close();
                stream = null;
                response.flushBuffer();
                FacesUtil.getCurrentInstance().responseComplete();
            }
            catch (IOException e) {
                log.debug(e.getMessage());
                log.error(e.getMessage());
            }
            finally {
                if (stream != null) {
                    try {
                        stream.close();
                    }
                    catch (IOException e) {
                        log.debug(e.getMessage());
                    }
                }
            }
        }
    }

    public Date getEndTime() {
        return endTime;
    }

    public Date getSearchcommitMaxTime() {
        return endTime;
    }

    public Date getSearchcommitMinTime() {
        return startTime;
    }

    public Date getStartTime() {
        return startTime;
    }

    @Override
    protected void initExample() {
        UserBill example = new UserBill();
        example.setUser(new User());
        setExample(example);
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public void setSearchcommitMaxTime(Date searchcommitMaxTime) {
        if (searchcommitMaxTime != null) {
            searchcommitMaxTime = DateUtil.addDay(searchcommitMaxTime, 1);
        }
        endTime = searchcommitMaxTime;
    }

    public void setSearchcommitMinTime(Date searchcommitMinTime) {
        startTime = searchcommitMinTime;
    }

    /**
     * 设置查询的起始和结束时间
     */
    public void setSearchStartEndTime(Date startTime, Date endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }
}
