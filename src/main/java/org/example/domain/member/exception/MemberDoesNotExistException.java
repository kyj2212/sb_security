package org.example.domain.member.exception;

import org.example.global.error.ErrorCode;
import org.example.global.error.exception.BusinessException;

public class MemberDoesNotExistException extends BusinessException {
    public MemberDoesNotExistException() {
        super(ErrorCode.MEMBER_NOT_FOUND);
    }
}