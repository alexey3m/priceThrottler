package com.price.processor.impl;

import com.price.processor.PriceProcessor;
import org.junit.Assert;
import org.junit.Test;

public class PriceThrottleTest {

    @Test
    public void onPriceTest() throws InterruptedException {
        PriceSubscriber subscriber1 = new PriceSubscriber(1, 0);
        PriceProcessor priceThrottle = new PriceThrottle();
        priceThrottle.subscribe(subscriber1);
        priceThrottle.onPrice("EURUSD", 1.4);
        priceThrottle.onPrice("USDRUR", 6);
        Thread.sleep(100);

        Assert.assertEquals(2, subscriber1.getCount().get());
    }

    @Test
    public void onPriceRepeatCurrencyTest() throws InterruptedException {
        PriceProcessor priceThrottle = new PriceThrottle();
        priceThrottle.onPrice("EURUSD", 1.1);
        Thread.sleep(100);
        PriceSubscriber subscriber1 = new PriceSubscriber(1, 0);
        priceThrottle.subscribe(subscriber1);
        priceThrottle.onPrice("EURUSD", 1.6);
        priceThrottle.unsubscribe(subscriber1);
        Thread.sleep(5000);

        Assert.assertEquals(1, subscriber1.getCount().get());
    }

    @Test(expected = RuntimeException.class)
    public void subscribeMaxTest() {
        PriceProcessor priceThrottle = new PriceThrottle();
        for (int i = 1; i <= 201; i++) {
            priceThrottle.subscribe(new PriceSubscriber(i, 0));
        }
    }
}
