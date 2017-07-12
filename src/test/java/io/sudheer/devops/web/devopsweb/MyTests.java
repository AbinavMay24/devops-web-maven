package io.sudheer.devops.web.devopsweb;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import io.sudheer.devops.web.devopsweb.MyClass;

public class MyTests {

    @Test
    public void multiplyByZeros() {
        MyClass tester = new MyClass(); // MyClass is tested

        // assert statements
        assertEquals("10 x 0 ", 0, tester.multiplyByZero(10));
        assertEquals("5 x 0 ", 0, tester.multiplyByZero(5));
        assertEquals("8 x 0 ", 0, tester.multiplyByZero(8));
    }
    
    @Test
    public void multiplyByNonZero() {
        MyClass tester = new MyClass(); // MyClass is tested

        // assert statements
        assertEquals("10 x N(>0) ", 80, tester.multiplyByNonZero(10));
        assertEquals("5 x N(>0) ", 40, tester.multiplyByNonZero(5));
        assertEquals("8 x N(>0) ", 64, tester.multiplyByNonZero(8));
    }
}
