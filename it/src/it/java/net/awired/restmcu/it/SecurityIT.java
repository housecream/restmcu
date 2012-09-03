package net.awired.restmcu.it;

import net.awired.ajsl.test.RestServerRule;
import net.awired.restmcu.api.filter.RestMcuSecurityServerFilter;
import org.junit.Rule;
import org.junit.Test;

public class SecurityIT {

    @Rule
    public RestMcuTestRule restmcu = new RestMcuTestRule();

    @Rule
    public RestServerRule notifyRule = new RestServerRule("http://0.0.0.0:5879", TestBoard.class)
            .addProvider(new RestMcuSecurityServerFilter(new RestMcuTestSecurityKey()));

    @Test
    public void should_secure_transaction() {
        restmcu.getBoardResource().getBoard();
    }

}