package info.bfly.app.protocol.model.request;

/**
 * Created by Administrator on 2017/1/9 0009.
 */

/**
 * 意见反馈
 */
public class FeedbackValue {


    private String mobileNumber;

    private String content;

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
