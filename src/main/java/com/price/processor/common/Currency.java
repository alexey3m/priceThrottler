package com.price.processor.common;

import java.util.Date;

/**
 * Wrapper for store currency name, rate and last update time
 */
public class Currency {

    private final String currency;
    private final double rate;
    private final Date lastTimeUpdate;

    public Currency(String currency, double rate) {
        this.currency = currency;
        this.rate = rate;
        this.lastTimeUpdate = new Date();
    }

    public String getCurrency() {
        return currency;
    }

    public double getRate() {
        return rate;
    }

    public Date getLastTimeUpdate() {
        return lastTimeUpdate;
    }
}
