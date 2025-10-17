package com.utkrusht.support;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class TicketingSystemMain {
    public static void main(String[] args) {
        final int NUM_AGENTS = 3;
        final int QUEUE_CAPACITY = 10;
        
        BlockingQueue<Ticket> ticketQueue = new ArrayBlockingQueue<>(QUEUE_CAPACITY);
        AtomicBoolean running = new AtomicBoolean(true);
        
        // Start producer
        Thread producerThread = new Thread(new TicketProducer(ticketQueue, running), "TicketProducer");
        producerThread.start();

        // Start agents
        List<Thread> agentThreads = new ArrayList<>();
        for (int i = 1; i <= NUM_AGENTS; i++) {
            Thread t = new Thread(new TicketAgent("Agent-" + i, ticketQueue, running), "Agent-" + i);
            agentThreads.add(t);
            t.start();
        }

        // Let system run for 6 seconds
        try {
            Thread.sleep(6000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            System.out.println("[Main] Initiating shutdown...");
            running.set(false);
        }

        // Wait for producer to finish
        try {
            producerThread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        // Wait for all agent threads to finish
        for (Thread t : agentThreads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        System.out.println("[Main] Ticketing system shut down gracefully.");
    }
}
