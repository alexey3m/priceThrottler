package com.price.processor.common;

import com.price.processor.PriceProcessor;

import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Class for generate update event for every subscriber. Each publisher has own executor service.
 */
public class UpdatePublisher {

    private final PriceProcessor subscriber;
    private final ConcurrentMap<String, Currency> actualCurrencies;
    private final ExecutorService executorService;

    public UpdatePublisher(PriceProcessor subscriber, ConcurrentMap<String, Currency> actualCurrencies) {
        this.subscriber = subscriber;
        this.actualCurrencies = actualCurrencies;
        this.executorService = Executors.newSingleThreadExecutor();
    }

    public void update(String ccyPair) {
        executorService.submit(() -> {
            Currency actualCurrency = actualCurrencies.get(ccyPair);
            subscriber.onPrice(actualCurrency.getCurrency(), actualCurrency.getRate());
        });
    }

    public void closeUpdate() {
        executorService.shutdown();
    }
}
