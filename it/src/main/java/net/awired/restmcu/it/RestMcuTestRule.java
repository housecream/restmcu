package net.awired.restmcu.it;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import net.awired.ajsl.test.RestContext;
import net.awired.restmcu.api.domain.board.RestMcuBoard;
import net.awired.restmcu.api.domain.pin.RestMcuPin;
import net.awired.restmcu.api.filter.RestMcuSecurityFilter;
import net.awired.restmcu.api.resource.client.RestMcuBoardResource;
import net.awired.restmcu.api.resource.client.RestMcuPinResource;
import net.awired.restmcu.api.resource.test.RestMcuDebugResource;
import org.junit.rules.ExternalResource;

public class RestMcuTestRule extends ExternalResource {

    private RestMcuBoardResource boardResource;

    private RestMcuPinResource pinResource;

    private RestMcuDebugResource debugResource;

    private RestMcuBoard board;

    private Map<Integer, RestMcuPin> pins = new HashMap<Integer, RestMcuPin>();

    public RestMcuTestRule() {
        this(RestMcuTestContext.getUrl());
    }

    public RestMcuTestRule(String url) {

        RestContext context = new RestContext(Arrays.asList(new RestMcuSecurityFilter(new RestMcuTestSecurityKey())));

        pinResource = context.prepareClient(RestMcuPinResource.class, url, null, true);
        boardResource = context.prepareClient(RestMcuBoardResource.class, url, null, true);
        debugResource = context.prepareClient(RestMcuDebugResource.class, url, null, true);
    }

    @Override
    public void before() throws Throwable {
        reset();
    }

    public void reset() throws Exception {
        //        HccBoard deviceInfo = DefaultITDomainHelper.buildDefaultDevice();
        //        boardResource.setBoard(deviceInfo);

        //        for (int i = 0; i < deviceInfo.getNumberOfPin() - 1; i++) {
        //            HccPinInfo info = DefaultITDomainHelper.buildDefaultPin(i).getInfo();
        //            if (info != null) {
        //                pinResource.setPinInfo(i, info);
        //            }
        //        }
        //
        //        for (int i = 0; i < deviceInfo.getNumberOfPin() - 1; i++) {
        //            HccPin pin = DefaultITDomainHelper.buildDefaultPin(i);
        //            if (pin.getValue() != null) {
        //                debugResource.setDebugValue(i, pin.getValue());
        //            }
        //        }
    }

    public RestMcuPinResource getPinResource() {
        return pinResource;
    }

    public RestMcuBoardResource getBoardResource() {
        return boardResource;
    }

    public RestMcuDebugResource getDebugResource() {
        return debugResource;
    }

}
