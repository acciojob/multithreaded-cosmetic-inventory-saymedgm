package com.driver;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class CosmeticInventory {

    public final ConcurrentHashMap<String, AtomicInteger> cosmetics;
    private final AtomicInteger totalQuantity;

    public CosmeticInventory() {
        this.cosmetics = new ConcurrentHashMap<>();
        this.totalQuantity = new AtomicInteger(0);
    }

    public void updateQuantity(String cosmeticType, int quantityChange) {
        cosmetics.computeIfAbsent(cosmeticType, key -> new AtomicInteger(0)); // initialize if not present

        // use AtomicInteger for thread-safe increment/decrement
        cosmetics.get(cosmeticType).addAndGet(quantityChange);
        totalQuantity.addAndGet(quantityChange);
    }

    public int getTotalQuantity() {
        return totalQuantity.get(); // Atomic integer guarantees consistent read
    }

    public static void main(String[] args) {
        CosmeticInventory inventory = new CosmeticInventory();

        // Simulate concurrent updates from multiple threads
        new Thread(() -> inventory.updateQuantity("Lipstick", 10)).start();
        new Thread(() -> inventory.updateQuantity("Foundation", 15)).start();

        // Wait for updates to finish
        try {
            Thread.sleep(100); // Adjust sleep time based on actual thread execution
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Total Cosmetic Quantity: " + inventory.getTotalQuantity());

        // Additional updates for Sample Input 2
        new Thread(() -> inventory.updateQuantity("Eyeliner", 5)).start();
        new Thread(() -> inventory.updateQuantity("Lipstick", 20)).start();
        new Thread(() -> inventory.updateQuantity("Mascara", 15)).start();

        // Wait for updates and print final total
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Total Cosmetic Quantity: " + inventory.getTotalQuantity());
    }
}
