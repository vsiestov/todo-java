package com.vsiestov.shared.core;

public interface UseCaseClass<T, V> {
    public T execute(V request);
}
