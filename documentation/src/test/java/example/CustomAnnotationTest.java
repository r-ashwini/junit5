package example;

import org.junit.jupiter.api.Benchmark;
import org.junit.jupiter.api.IntegrationTest;
import org.junit.jupiter.api.SystemTest;
import org.junit.jupiter.api.UnitTest;
/*Benchmarking the test Container*/
@Benchmark
public class CustomAnnotationTest
{
    @UnitTest
    void UTest()
    {
    }
    @IntegrationTest
    void ITest()
    {
    }
    @SystemTest
    void STest()
    {
    }
}
