package com.price.processor.impl;

import com.price.processor.PriceProcessor;
import com.price.processor.common.Currency;
import com.price.processor.common.UpdatePublisher;

import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Main class for price throttle.
 * WAIT_TIME - constriction for skip unnecessary updates
 * With using ConcurrentMap we are delimited access to put or get current currency.
 */
public class PriceThrottle implements PriceProcessor {

    private final static long WAIT_TIME = 1000;
    private final static int MAX_SUBSCRIBERS = 200;
    private final ConcurrentMap<String, Currency> currencies = new ConcurrentHashMap<>();
    private final ConcurrentMap<PriceProcessor, UpdatePublisher> subscribers = new ConcurrentHashMap<>();

    @Override
    public void onPrice(String ccyPair, double rate) {
        if (currencies.containsKey(ccyPair)) {
            Currency actualCurrency = currencies.get(ccyPair);
            if (new Date().getTime() - actualCurrency.getLastTimeUpdate().getTime() > WAIT_TIME) {
                updateOnPrice(ccyPair, rate);
            }
        } else {
            updateOnPrice(ccyPair, rate);
        }
    }

    @Override
    public void subscribe(PriceProcessor priceProcessor) {
        if (subscribers.size() < MAX_SUBSCRIBERS) {
            subscribers.put(priceProcessor, new UpdatePublisher(priceProcessor, currencies));
        } else {
            throw new RuntimeException("Maximum amount of subscribers has been reached. Please wait free slot.");
        }
    }

    @Override
    public void unsubscribe(PriceProcessor priceProcessor) {
        subscribers.get(priceProcessor).closeUpdate();
        subscribers.remove(priceProcessor);
    }

    private void updateOnPrice(String ccyPair, double rate) {
        currencies.put(ccyPair, new Currency(ccyPair, rate));
        this.subscribers.values().parallelStream().forEach(updatePublisher -> updatePublisher.update(ccyPair));
    }
}
