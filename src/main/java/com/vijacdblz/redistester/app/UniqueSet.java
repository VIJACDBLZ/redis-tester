package com.vijacdblz.redistester.app;

import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;

public class UniqueSet<E> extends HashSet<E> {

    private int batchSize = 1000;

    @Autowired
    private DupCheck dupCheck;

    public boolean add(E e, String partition) {
        if (super.add(e))
            throw new IllegalArgumentException("Element already exist in set" + e);

        if (super.size() >= batchSize) {
            dupCheck.checkAndPersist(this);
            this.clear();
        }

        return true;
    }

}
