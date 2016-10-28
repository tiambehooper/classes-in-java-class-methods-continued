package com.theironyard;

import com.github.javaparser.ParseException;
import net.doughughes.testifier.annotation.Testifier;
import net.doughughes.testifier.matcher.RegexMatcher;
import net.doughughes.testifier.output.OutputStreamInterceptor;
import net.doughughes.testifier.util.SourceCodeExtractor;
import net.doughughes.testifier.util.TestifierAnnotationReader;
import net.doughughes.testifier.watcher.NotifyingWatcher;
import net.doughughes.testifier.watcher.OutputWatcher;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.number.IsCloseTo.closeTo;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@Testifier(sourcePath = "./src/main/java/com/theironyard/Example.java", clazz = Example.class)
public class ExampleTest {

    @Rule
    public NotifyingWatcher notifyingWatcher = new NotifyingWatcher("https://tiy-testifier-webapp.herokuapp.com/notify");

    @Rule
    public OutputWatcher outputWatcher = new OutputWatcher();

    @Test
    @Testifier(method = "not", args = {boolean.class})
    public void notShouldInvertValueTest() {
        /* arrange */
        Example example = new Example();

        /* act */
        boolean notTrue = example.not(true);
        boolean notFalse = example.not(false);

        /* assert */
        assertFalse("not(true) should be false", notTrue);
        assertTrue("not(false) should be true", notFalse);
    }

    @Test
    @Testifier(method = "doublePlusOne", args = {int.class})
    public void doublePlusOneTest() {
        /* arrange */
        Example example = new Example();

        /* act */
        int result = example.doublePlusOne(5);

        /* assert */
        assertThat("5 doubled plus 1 should be 11",
                result, equalTo(11));
    }

    @Test
    @Testifier(method = "averageTwoNumbers", args = {double.class, double.class})
    public void averageTwoNumbersTest() {
        /* arrange */
        Example example = new Example();

        /* act */
        double result = example.averageTwoNumbers(333.333, 555.555);

        /* assert */
        assertThat("Average of 333.333 and 555.555 should be (very close to) 444.444",
                result, closeTo(444.444, 0.001));
    }

    @Test
    @Testifier(method = "getGreeting", args = {String.class})
    public void getGreetingTest() {
        /* arrange */
        Example example = new Example();

        /* act */
        String result = example.getGreeting("Tracy Kerry");

        /* assert */
        assertThat("The greeting for 'Tracy Kerry' should be 'Hello, Tracy Kerry!'",
                result, equalTo("Hello, Tracy Kerry!"));
    }

    @Test
    @Testifier(method = "sayHello", args = {String.class})
    public void sayHelloTest() {
        /* arrange */
        Example example = new Example();

        /* act */
        example.sayHello("Kerry Tracy");

        /* assert */
        OutputStreamInterceptor out = (OutputStreamInterceptor) System.out;
        assertThat("The sayHello() method should have printed output to the console.",
                out.getPrinted().size(), equalTo(1));
        assertThat("Saying hello to 'Kerry Tracy' should print 'Hello, Kerry Tracy!' to the console.",
                out.getPrinted().get(0), equalTo("Hello, Kerry Tracy!"));
    }

    @Test
    @Testifier(method = "sayHello", args = {String.class})
    public void sayHelloShouldNotReturnAnythingTest() throws IOException, ParseException, NoSuchMethodException {
        /* arrange */
        Example example = new Example();

        /* act */
        example.sayHello("Kerry Tracy");

        /* assert */
        TestifierAnnotationReader reader = new TestifierAnnotationReader(this);
        String source = new SourceCodeExtractor(reader.getSourcePath()).getMethodDescription(reader.getMethod(), reader.getArgs());

        Assert.assertThat("The sayHello() method should not return any value.",
                source, RegexMatcher.matches("^(?!.*?ReturnStmt).*?$"));

    }
}