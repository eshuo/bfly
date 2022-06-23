package info.bfly.p2p.message.service.impl;

public class SendSmsResult extends ApiResultBase {
    private static final long serialVersionUID = 5568564484296402576L;
    private SendInfo          result;

    public SendInfo getResult() {
        return result;
    }

    public void setResult(SendInfo result) {
        this.result = result;
    }
}
