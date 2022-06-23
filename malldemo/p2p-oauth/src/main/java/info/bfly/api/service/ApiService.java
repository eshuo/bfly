package info.bfly.api.service;

import info.bfly.api.model.In;
import info.bfly.api.model.Out;
import info.bfly.archer.key.ResponseMsg;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.inject.Singleton;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

import static info.bfly.api.validate.Validate.isTrue;

/**
 * Created by XXSun on 2016/12/22.
 */
@Singleton
@Service("apiService")
public class ApiService {

    private final static Map<String, Long> MemoryDbData = new HashMap<String, Long>();

    @Value("#{refProperties['enableUniqueRequest'] eq 'true'}")
    private boolean enableUniqueRequest;

    private static final String REQUEST_IN = "requestIn";

    public In parseIn(HttpServletRequest request) {
        Object in = request.getAttribute(REQUEST_IN);
        if (in == null) {
            in = new In(request);
            if(enableUniqueRequest)
            isTrue(unique((In) in), ResponseMsg.REQUEST_UNIQUE, "requestId");
            request.setAttribute(REQUEST_IN, in);
        }
        return (In) in;
    }

    private boolean unique(In in) {
        if (MemoryDbData.containsKey(in.getDeviceId() + in.getRequestId()) && MemoryDbData.get(in.getDeviceId() + in.getRequestId()) > System.currentTimeMillis())
            return false;
        else {
            MemoryDbData.put(in.getDeviceId() + in.getRequestId(), System.currentTimeMillis() + 600000);
            return true;
        }

    }

    public Out parseOut(HttpServletRequest request) {
        return new Out(parseIn(request));
    }
    public Out parseOutSimple(HttpServletRequest request) {
        return new Out(request);
    }
    public Out parseOutwhitMessage(ResponseMsg message, HttpServletRequest request) {
        return new Out(message, request);
    }
}
