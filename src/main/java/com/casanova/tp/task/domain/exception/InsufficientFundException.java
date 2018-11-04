package com.casanova.tp.task.domain.exception;

import java.math.BigDecimal;
import java.text.MessageFormat;

public class InsufficientFundException extends RuntimeException {
    private static final String MSG = "Insufficient fund!! Trying to withdraw [{0}] with Balance [{1}].";

    public InsufficientFundException(final BigDecimal withdraw, final BigDecimal currentBalance) {
        super(MessageFormat.format(MSG, String.format("%.2f",withdraw), String.format("%.2f",currentBalance)));
    }
}
