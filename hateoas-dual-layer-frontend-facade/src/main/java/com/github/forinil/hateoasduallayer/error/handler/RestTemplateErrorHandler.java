package com.github.forinil.hateoasduallayer.error.handler;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.DefaultResponseErrorHandler;

@Component
public class RestTemplateErrorHandler extends DefaultResponseErrorHandler {

    @Override
    protected boolean hasError(HttpStatus statusCode) {
        return ((statusCode.series() == HttpStatus.Series.CLIENT_ERROR && !statusCode.equals(HttpStatus.NOT_FOUND)) ||
                statusCode.series() == HttpStatus.Series.SERVER_ERROR);
    }
}
