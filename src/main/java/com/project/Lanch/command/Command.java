package com.project.Lanch.command;

/**
 * The Command interface represents an action or operation
 * that can be executed, returning a result of type T.
 */

public interface Command<T> {
    T execute();
}