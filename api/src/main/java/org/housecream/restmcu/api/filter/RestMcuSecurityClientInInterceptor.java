/**
 *
 *     Copyright (C) Housecream.org
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>
 */
package org.housecream.restmcu.api.filter;

import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;
import fr.norad.jaxrs.client.server.resource.filter.FilterUtils;

public class RestMcuSecurityClientInInterceptor extends AbstractPhaseInterceptor<Message> {

    private RestMcuSecurityContext restMcuSecurityContext;

    public RestMcuSecurityClientInInterceptor(RestMcuSecurityKey key) {
        super(Phase.INVOKE);
        restMcuSecurityContext = new RestMcuSecurityContext(key);
    }

    @Override
    public void handleMessage(Message message) {
        try {
            restMcuSecurityContext.checkSecurity(message);
        } catch (SecurityException e) {
            FilterUtils.replaceCurrentPayloadWithError(message, e);
        }

    }

}
