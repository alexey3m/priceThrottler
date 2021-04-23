package com.price.processor.common;

import com.price.processor.impl.PriceSubscriber;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class UpdatePublisherTest {

    @Test
    public void updateTest() throws InterruptedException {
        Currency currencyEUR = new Currency("EURUSD", 1.1);
        Currency currencyUSD = new Currency("USDRUB", 0.005);
        ConcurrentMap<String, Currency> current = new ConcurrentHashMap<>();
        current.put("EURUSD", currencyEUR);
        current.put("USDRUB", currencyUSD);
        PriceSubscriber subscriber = new PriceSubscriber(1, 0);
        UpdatePublisher publisher = new UpdatePublisher(subscriber, current);

        publisher.update("EURUSD");
        publisher.update("USDRUB");
        Thread.sleep(100);

        Assert.assertEquals(2, subscriber.getCount().get());
    }

    @Test
    public void closeUpdateTest() {
        Currency currency = new Currency("EURUSD", 1.1);
        ConcurrentMap<String, Currency> current = new ConcurrentHashMap<>();
        current.put("EURUSD", currency);
        PriceSubscriber subscriber = new PriceSubscriber(1, 0);
        UpdatePublisher publisher = new UpdatePublisher(subscriber, current);
        publisher.closeUpdate();
    }
}
