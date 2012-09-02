package net.awired.restmcu.api.filter;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.Phase;

public class RestMcuSecurityOutInterceptor extends RestMcuInterceptor {
    public RestMcuSecurityOutInterceptor(RestMcuSecurityKey key) {
        super(Phase.WRITE, key);
    }

    @Override
    public void handleMessage(Message message) {
        long time = System.currentTimeMillis() / 1000L;
        Map<String, List<String>> headers = (Map<String, List<String>>) message.get(Message.PROTOCOL_HEADERS);
        if (headers == null) {
            headers = new TreeMap<String, List<String>>(String.CASE_INSENSITIVE_ORDER);
            message.put(Message.PROTOCOL_HEADERS, headers);
        }
        headers.put("Hmac-Time", Arrays.asList(String.valueOf(time)));
        headers.put("Hmac-Hash", Arrays.asList(buildHash(time, message)));
    }
}
