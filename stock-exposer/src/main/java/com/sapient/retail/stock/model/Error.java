package com.sapient.retail.stock.model;

import com.sapient.retail.stock.common.builder.GenericBuilder;
import org.springframework.util.StringUtils;

public class Error {
    private int code = 200;
    private String message;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static Error stockNotFound() {
        return GenericBuilder.of(Error::new)
                .with(Error::setCode, 404)
                .with(Error::setMessage, "stock not found")
                .build();
    }

    public static Error genericError() {
        return GenericBuilder.of(Error::new)
                .with(Error::setCode, 500)
                .with(Error::setMessage, "something went wrong, please try again in some time.")
                .build();
    }

    public static Error badInput(final String propertyName) {
        String message = "";
        if(StringUtils.hasText(propertyName))
            message = " Invalid input for field: " + propertyName;
        return GenericBuilder.of(Error::new)
                .with(Error::setCode, 400)
                .with(Error::setMessage, "Bad request." + message)
                .build();
    }
}
