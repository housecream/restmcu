/**
 *
 *     Copyright (C) norad.fr
 *
 *     Licensed under the Apache License, Version 2.0 (the "License");
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *             http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 */
package org.housecream.restmcu.it.resource;

import static org.housecream.restmcu.it.builder.NotifBuilder.notif;
import org.housecream.restmcu.api.domain.line.RestMcuLineNotification;
import fr.norad.core.lang.exception.NotFoundException;
import fr.norad.core.lang.exception.UpdateException;

public class EmulatorLineResource extends LatchLineResource {

    private final EmulatorBoardResource boardResource;

    public EmulatorLineResource(EmulatorBoardResource boardResource) {
        this.boardResource = boardResource;
    }

    @Override
    public void setLineValue(Integer lineId, Float value) throws NotFoundException, UpdateException {
        Float oldValue = lineInfo(lineId).getValue();
        super.setLineValue(lineId, value);
        notifyChange(lineId, oldValue);
    }

    private void notifyChange(Integer lineId, Float oldValue) {
        String source = boardResource.getBoardSettings().getIp() + ":" + boardResource.getBoardSettings().getPort();
        RestMcuLineNotification notif = notif().lineId(lineId).oldVal(oldValue).val(lineInfo(lineId).getValue())
                .build();
        notif.setSource(source);
        boardResource.sendNotif(notif);
    }
}
