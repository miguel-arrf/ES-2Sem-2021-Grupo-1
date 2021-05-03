package g1.ISCTE;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FontTypeTest {


    @Test
    void valueOf() {
        assertEquals(FontType.valueOf("ROUNDED_SEMI_BOLD"), FontType.ROUNDED_SEMI_BOLD);
    }
}