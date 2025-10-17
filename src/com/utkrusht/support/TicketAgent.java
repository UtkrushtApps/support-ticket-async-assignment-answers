package com.utkrusht.support;

import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class TicketAgent implements Runnable {
    private final BlockingQueue<Ticket> ticketQueue;
    private final AtomicBoolean running;
    private final String agentName;
    private final Random random = new Random();

    public TicketAgent(String agentName, BlockingQueue<Ticket> ticketQueue, AtomicBoolean running) {
        this.agentName = agentName;
        this.ticketQueue = ticketQueue;
        this.running = running;
    }

    @Override
    public void run() {
        while (running.get() || !ticketQueue.isEmpty()) {
            try {
                Ticket ticket = ticketQueue.poll();
                if (ticket == null) {
                    Thread.sleep(100); // No ticket right now
                    continue;
                }
                processTicket(ticket);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("[" + agentName + "] Interrupted. Shutting down.");
                break;
            }
        }
        System.out.println("[" + agentName + "] Agent stopped.");
    }

    private void processTicket(Ticket ticket) {
        System.out.println("[" + agentName + "] Processing " + ticket);
        try {
            // Simulate random processing time between 200-700ms
            Thread.sleep(200 + random.nextInt(500));
            // Simulate occasional error
            if (random.nextInt(20) == 0) { // ~5% failure chance
                throw new Exception("Processing failed for ticket " + ticket.getId());
            }
            System.out.println("[" + agentName + "] Successfully processed " + ticket);
        } catch (Exception e) {
            System.err.println("[" + agentName + "] Error processing " + ticket + ": " + e.getMessage());
            // Could implement retry or put failed tickets to dlq
        }
    }
}
