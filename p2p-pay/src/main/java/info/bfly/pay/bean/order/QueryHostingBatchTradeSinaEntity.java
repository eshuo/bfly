package info.bfly.pay.bean.order;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 托管交易批次查询 on 2017/2/17 0017.
 */

public class QueryHostingBatchTradeSinaEntity extends OrderSinaEntity implements Serializable {

    private static final long serialVersionUID = 7384417185686802374L;
    /**
     * 交易批次号
     */
    @NotNull
    private String out_batch_no;
    /**
     * 页号
     */
    private Integer page_no;
    /**
     * 每页大小
     */
    private Integer page_size;

    public String getOut_batch_no() {
        return out_batch_no;
    }

    public void setOut_batch_no(String out_batch_no) {
        this.out_batch_no = out_batch_no;
    }

    public Integer getPage_no() {
        return page_no;
    }

    public void setPage_no(Integer page_no) {
        this.page_no = page_no;
    }

    public Integer getPage_size() {
        return page_size;
    }

    public void setPage_size(Integer page_size) {
        this.page_size = page_size;
    }

    @Override
    public String toString() {
        return "QueryHostingBatchTradeSinaEntity{" +
                "out_batch_no='" + out_batch_no + '\'' +
                ", page_no=" + page_no +
                ", page_size=" + page_size +
                "} " + super.toString();
    }
}
