package ar.vicria.subte.client.autoconfigure.properties;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class SubteClientPropertiesTest {

    @Test
    void throwsExceptionWhenEnabledWithoutUrl() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> new SubteClientProperties(true, null));
        Assertions.assertThrows(IllegalArgumentException.class, () -> new SubteClientProperties(true, ""));
        Assertions.assertThrows(IllegalArgumentException.class, () -> new SubteClientProperties(true, "    "));
    }
}