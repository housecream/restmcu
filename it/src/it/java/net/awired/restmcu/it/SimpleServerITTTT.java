package net.awired.restmcu.it;

import net.awired.ajsl.core.io.NetworkUtils;
import net.awired.ajsl.test.RestServerRule;
import net.awired.restmcu.api.domain.board.RestMcuBoardSettings;
import net.awired.restmcu.api.filter.RestMcuSecurityInInterceptor;
import net.awired.restmcu.api.filter.RestMcuSecurityOutInterceptor;
import org.junit.Rule;
import org.junit.Test;

public class SimpleServerITTTT {
    @Rule
    public RestMcuTestRule restmcu = new RestMcuTestRule();

    @Rule
    public RestServerRule notifyRule = new RestServerRule("http://0.0.0.0:5879", NotifyResource.class)
            .addInInterceptor(new RestMcuSecurityInInterceptor(new RestMcuTestSecurityKey())).addOutInterceptor(
                    new RestMcuSecurityOutInterceptor(new RestMcuTestSecurityKey()));

    @Test
    public void should() throws Exception {
        RestMcuBoardSettings boardSettings = new RestMcuBoardSettings();
        boardSettings.setNotifyUrl("http://" + NetworkUtils.getFirstNonWifiIp() + ":5879/");
        //        restmcu.getBoardResource().setBoardSettings(boardSettings);
        Thread.sleep(10000000);
    }

}
