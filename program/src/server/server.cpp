#include "server.h"

uint16_t startResponseHeader(char **buf, const prog_char *codeMsg) {
//    *buf = &((*buf)[TCP_CHECKSUM_L_P + 3]);
    uint16_t plen;
    plen = addToBufferTCP_P(*buf, 0, HEADER_HTTP);
    plen = addToBufferTCP_P(*buf, plen, codeMsg);
    plen = addToBufferTCP_P(*buf, plen, PSTR("\r\n"));
#ifdef HMAC
    plen = addSecurityToBuffer(*buf, plen);
#endif
    plen = addToBufferTCP_P(*buf, plen, HEADER_CONTENT);
    plen = addToBufferTCP_P(*buf, plen, DOUBLE_ENDL);
    return plen;
}

uint16_t appendErrorMsg_P(char *buf, uint16_t plen, const prog_char *type, const prog_char *msg) {
    plen = addToBufferTCP_P(buf, plen, PSTR("{\"errorClass\":\"net.awired.ajsl.core.lang.exception."));
    plen = addToBufferTCP_P(buf, plen, type);
    plen = addToBufferTCP_P(buf, plen, PSTR("\",\"message\":\""));
    plen = addToBufferTCP_P(buf, plen, msg);
    plen = addToBufferTCP_P(buf, plen, JSON_STR_END);
    return plen;
}

t_webRequest currentWebRequest = {0, 0};

static uint16_t commonCheck(char *buf, uint16_t dataPointer, uint16_t dataLen) {
    uint16_t plen;
    if (dataLen >= 999) {
        plen = startResponseHeader(&buf, HEADER_413);
        plen = appendErrorMsg_P(buf, plen, ERROR_MSG_UPDATE, PSTR("Too big"));
        return plen;
    }
    return 0;
}

uint16_t parseHeaders(char *buf, uint16_t dataPointer, uint16_t dataLen) {
    uint16_t plen;

    prog_char *methodPos;
    int i = 0;
    for (; (methodPos = (prog_char *) pgm_read_word(&resources[i].method)); i++) {
        uint16_t querylen = strlen_P((prog_char *) pgm_read_word(&resources[i].query));
        prog_char *currentQueryPos = (prog_char *) pgm_read_word(&resources[i].query);
        if (strncmp_P((char *) &(buf[dataPointer]), methodPos, 4) == 0
                && strncmp_P((char *) & (buf[dataPointer + 4]), currentQueryPos, querylen) == 0) {
            prog_char *suffixPos = (prog_char *) pgm_read_word(&resources[i].suffix);
            if (' ' == pgm_read_byte(&currentQueryPos[querylen - 1])) {
               currentWebRequest.resource = &resources[i];
               break;
            } else {
                int8_t currentPinId = atoi(&buf[dataPointer + 4 + querylen]);
                int8_t idx = configGetInputPinIdx(currentPinId);
                if (idx != -1) {
                    currentWebRequest.pinIdx = idx;
                } else {
                    idx = configGetOutputPinIdx(currentPinId);
                    if (idx == -1) {
                        plen = startResponseHeader(&buf, HEADER_400);
                        plen = appendErrorMsg_P(buf, plen, ERROR_MSG_NOT_FOUND, PSTR("Pin not found"));
                        return plen;
                    }
                    currentWebRequest.pinIdx = pinInputSize + idx;
                }

                uint8_t j = dataPointer + 4 + querylen;
                for (; buf[j] >= '0' && buf[j] <= '9'; j++);
                if (suffixPos == 0 && (buf[j] == ' ' || (buf[j] == '/' && buf[j + 1] == ' '))) {
                    currentWebRequest.resource = &resources[i];
                    break;
                } else if (strncmp_P((char *) & (buf[j]), suffixPos, strlen_P(suffixPos)) == 0) {
                    currentWebRequest.resource = &resources[i];
                    break;
                }
            }
        }
    }
    return 0;
}

uint16_t handleWebRequest(char *buf, uint16_t dataPointer, uint16_t dataLen) {
    uint16_t plen = commonCheck(buf, dataPointer, dataLen);
    if (plen != 0) {
        return plen;
    }

    plen = parseHeaders(buf, dataPointer, dataLen);
    if (plen != 0) {
        return plen;
    }

    if (!currentWebRequest.resource) {
        plen = startResponseHeader(&buf, HEADER_404);
        plen = appendErrorMsg_P(buf, plen, ERROR_MSG_NOT_FOUND, PSTR("No resource for this method & url"));
    } else {
        ResourceFunc currentFunc = (ResourceFunc) pgm_read_word(&currentWebRequest.resource->resourceFunc);
        if ((prog_char *)pgm_read_word(&currentWebRequest.resource->method) == GET) { // GET do not need data, calling func directly
            plen = currentFunc((char*)buf, 0, dataLen, &currentWebRequest);
        } else {
            uint16_t endPos = strstrpos_P(&buf[dataPointer], DOUBLE_ENDL);
            if (endPos == -1) {
                plen = startResponseHeader(&buf, HEADER_400);
                plen = appendErrorMsg_P(buf, plen, ERROR_MSG_NOT_FOUND, PSTR("Double endl not found"));
            } else {
                uint16_t dataStartPos = dataPointer + endPos + 4;
				plen = currentFunc((char *)buf, dataStartPos, dataLen, &currentWebRequest);
            }
        }
    }
    return plen;
}
