package com.price.processor.impl;

import com.price.processor.PriceProcessor;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Class for test purpose only.
 */
public class PriceSubscriber implements PriceProcessor {

    private final int id;
    private final long delay;
    private final AtomicInteger count;

    public PriceSubscriber(int id, long delay) {
        this.delay = delay;
        this.id = id;
        this.count = new AtomicInteger();
    }

    @Override
    public void onPrice(String ccyPair, double rate) {
        try {
            Thread.sleep(delay);
            count.incrementAndGet();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Subscriber id = " + id + " : currency " + ccyPair + " with rate = " + rate + " has been updated.");
    }

    @Override
    public void subscribe(PriceProcessor priceProcessor) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void unsubscribe(PriceProcessor priceProcessor) {
        throw new UnsupportedOperationException();
    }

    public AtomicInteger getCount() {
        return count;
    }
}
