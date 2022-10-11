package com.vijacdblz.redistester.app;

public class Partitioner {

    public int getPartition(String randString) {
        char idx = randString.charAt(0);

        switch (idx) {
            case '0':
                return 0;
            case '1':
                return 1;
            case '2':
                return 2;
            case '3':
                return 3;

        }

        return -1;

    }

}
