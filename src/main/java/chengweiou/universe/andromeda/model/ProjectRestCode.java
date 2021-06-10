package chengweiou.universe.andromeda.model;

import chengweiou.universe.blackhole.model.RestCode;

public enum ProjectRestCode implements RestCode {
    USERNAME_PASSWORD_MISMATCH, ACCOUNT_INACTIVE,
    TWOFA_WAITING, TWOFA_CODE_NOT_MATCH, TWOFA_EXPIRED,
    PHONE_MSG_TOO_MANY,
    ;
}
