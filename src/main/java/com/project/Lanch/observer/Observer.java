package com.project.Lanch.observer;


/**
 * Observer defines the interface for objects that should be notified of changes.
 * - Provides a single method, `update`, which receives a message describing the change.
 * - Designed for use in the Observer pattern to decouple event generators from listeners.
 */



public interface Observer {
    void update(String message);
}
