import com.github.javaparser.ParseException;
import net.doughughes.testifier.exception.CannotAccessMethodException;
import net.doughughes.testifier.exception.CannotFindMethodException;
import net.doughughes.testifier.exception.CannotInvokeMethodException;
import net.doughughes.testifier.matcher.RegexMatcher;
import net.doughughes.testifier.output.OutputStreamInterceptor;
import net.doughughes.testifier.test.TestifierTest;
import net.doughughes.testifier.util.Invoker;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.number.IsCloseTo.closeTo;
import static org.junit.Assert.*;

public class ExampleTest extends TestifierTest {

    @Test
    public void notShouldInvertValueTest() {
        /* arrange */
        Example example = new Example();

        try {
            /* act */
            boolean notTrue = (boolean) Invoker.invoke(example, "not", true);
            boolean notFalse = (boolean) Invoker.invoke(example, "not", false);

            /* assert */
            assertFalse("not(true) should be false", notTrue);
            assertTrue("not(false) should be true", notFalse);
        } catch (CannotInvokeMethodException | CannotFindMethodException | CannotAccessMethodException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void doublePlusOneTest() {
        /* arrange */
        Example example = new Example();

        try {
            /* act */
            int result = (int) Invoker.invoke(example, "doublePlusOne", 5);

            /* assert */
            assertThat("5 doubled plus 1 should be 11",
                    result, equalTo(11));
        } catch (CannotInvokeMethodException | CannotFindMethodException | CannotAccessMethodException e) {
            fail(e.getMessage());
        }

    }

    @Test
    public void averageTwoNumbersTest() {
        /* arrange */
        Example example = new Example();

        try {
            /* act */
            double result = (double) Invoker.invoke(example, "averageTwoNumbers", 333.333, 555.555);

            /* assert */
            assertThat("Average of 333.333 and 555.555 should be (very close to) 444.444",
                    result, closeTo(444.444, 0.001));
        } catch (CannotInvokeMethodException | CannotFindMethodException | CannotAccessMethodException e) {
            fail(e.getMessage());
        }

    }

    @Test
    public void getGreetingTest() {
        /* arrange */
        Example example = new Example();

        try {
            /* act */
            String result = (String) Invoker.invoke(example, "getGreeting", "Tracy Kerry");

            /* assert */
            assertThat("The greeting for 'Tracy Kerry' should be 'Hello, Tracy Kerry!'",
                    result, equalTo("Hello, Tracy Kerry!"));
        } catch (CannotInvokeMethodException | CannotFindMethodException | CannotAccessMethodException e) {
            fail(e.getMessage());
        }

    }

    @Test
    public void sayHelloTest() {
        /* arrange */
        Example example = new Example();

        try {
            /* act */
            Invoker.invoke(example, "sayHello", "Kerry Tracy");

            /* assert */
            OutputStreamInterceptor out = (OutputStreamInterceptor) System.out;
            assertThat("The sayHello() method should have printed output to the console.",
                    out.getPrinted().size(), greaterThanOrEqualTo(1));
            assertThat("Saying hello to 'Kerry Tracy' should print 'Hello, Kerry Tracy!' to the console.",
                    out.getPrinted().get(0), equalTo("Hello, Kerry Tracy!"));
        } catch (CannotInvokeMethodException | CannotFindMethodException | CannotAccessMethodException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void sayHelloShouldNotReturnAnythingTest() throws IOException, ParseException, NoSuchMethodException {
        /* arrange */
        Example example = new Example();

        /* act */

        /* assert */
        String source = null;
        try {
            source = codeWatcher.getMainSourceCodeService().getDescriptionOfMethod("sayHello", String.class);
        } catch (CannotFindMethodException e) {
            fail(e.getMessage());
        }

        Assert.assertThat("The sayHello() method should not return any value.",
                source, RegexMatcher.matches("^(?!.*?ReturnStmt).*?$"));

    }
}