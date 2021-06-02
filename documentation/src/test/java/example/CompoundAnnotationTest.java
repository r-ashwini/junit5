package example;

import org.junit.jupiter.api.*;

public class CompoundAnnotationTest
{
    @SanityTest
    static void SATest1()
    {
        System.out.println("This is a sanity test case");
    }
    @SmokeTest
    static void SMtest1()
    {
        System.out.println("This is a smoke test case");
    }
    @UnitTest
    void UTest1()
    {
        System.out.println("This is a unit test1");
    }
    @UnitTest
    void UTest2()
    {
        System.out.println("This is a unit test2");
    }
    @SystemTest
    void STest1()
    {
        System.out.println("This is a system test1");
    }
    @SystemTest
    void STest2()
    {
        System.out.println("This is a system test2");
    }
    @IntegrationTest
    void ITest1()
    {
        System.out.println("This is a integration test1");
    }
    @IntegrationTest
    void ITest2()
    {
        System.out.println("This is a integration test2");
    }
}
