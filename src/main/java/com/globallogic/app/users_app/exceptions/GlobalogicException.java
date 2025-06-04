package com.globallogic.app.users_app.exceptions;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;


@NoArgsConstructor
@AllArgsConstructor
@Getter
public class GlobalogicException  extends RuntimeException{
    private  String code;

    public GlobalogicException(String code, String message) {
        super(message);
        this.code = code;
    }

}
