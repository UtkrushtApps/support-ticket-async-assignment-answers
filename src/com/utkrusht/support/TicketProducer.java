package com.utkrusht.support;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class TicketProducer implements Runnable {
    private final BlockingQueue<Ticket> ticketQueue;
    private final AtomicBoolean running;
    private int ticketCounter = 1;

    public TicketProducer(BlockingQueue<Ticket> ticketQueue, AtomicBoolean running) {
        this.ticketQueue = ticketQueue;
        this.running = running;
    }

    @Override
    public void run() {
        try {
            while (running.get()) {
                Ticket ticket = new Ticket(ticketCounter++, "Issue description for ticket #" + (ticketCounter-1));
                ticketQueue.put(ticket);
                System.out.println("[Producer] Enqueued: " + ticket);
                Thread.sleep(300); // Simulate incoming tickets every 300ms
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("[Producer] Interrupted. Stopping ticket generation.");
        }
    }
}
