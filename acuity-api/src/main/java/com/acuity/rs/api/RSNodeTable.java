package com.acuity.rs.api;


//Generated by the injector on run.

public interface RSNodeTable {

    RSNodeQueue getQueue();

    RSHashTable getTable();

    RSCacheableNode invokeGet(long var0);

    void invokePut(RSCacheableNode var0, long var1);

    void invokeRemove(long var0);

    void invokeReset();
}
