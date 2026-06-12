package com.itheima;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UUIDTest {

    @Test
    public void testUuidUniqueness() {
        Set<String> values = new HashSet<>();
        for (int i = 0; i < 1000; i++) {
            values.add(UUID.randomUUID().toString());
        }
        assertEquals(1000, values.size());
    }
}
