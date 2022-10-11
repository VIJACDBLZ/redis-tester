package com.vijacdblz.redistester.app;

import java.util.Set;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class DupCheck {

    @Async
    public int checkAndPersist(Set uniqueSet) {
        return 0;

    }

}
