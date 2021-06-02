package example.timing;

import java.lang.reflect.Method;
import java.util.*;

import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ExtensionContext.Namespace;
import org.junit.jupiter.api.extension.ExtensionContext.Store;
import org.junit.jupiter.api.extension.AfterAllCallback;

public class TimingExtensionDemo implements BeforeTestExecutionCallback, AfterTestExecutionCallback,AfterAllCallback {
    public static <String, Long extends Comparable<Long> > Map<String, Long> valueSort(final Map<String, Long> map) {
        // Static Method with return type Map and extending comparator class which compares values associated with two keys
        Comparator<String> valueComparator = new Comparator<String>(){
            // return comparison results of values of two keys
            @Override
            public int compare(String k1, String k2)
            {
                return map.get(k2).compareTo(map.get(k1));
            }
        };
        // SortedMap created using the comparator
        Map<String, Long> sorted = new TreeMap<String, Long>(valueComparator);
        sorted.putAll(map);
        return sorted;
    }
    Map<String,Long> timeRecorderTreeMap = new TreeMap<>();
    private static final String START_TIME = "start time";
    @Override
    public void beforeTestExecution(ExtensionContext context) throws Exception {
        getStore(context).put(START_TIME, System.currentTimeMillis());
    }
    @Override
    public void afterTestExecution(ExtensionContext context) throws Exception {
        Method testMethod = context.getRequiredTestMethod();
        long startTime = getStore(context).remove(START_TIME, long.class);
        long duration = System.currentTimeMillis() - startTime;
        timeRecorderTreeMap.put(testMethod.getName(),duration);
    }
    @Override
    public void afterAll(ExtensionContext context) throws Exception{
        // Calling the method valueSort
        Map<String,Long> sortedMap = valueSort(timeRecorderTreeMap);
        for(Map.Entry<String, Long> entry : sortedMap.entrySet()){
            System.out.println( entry.getKey() + "=>" + entry.getValue() );
        }
    }
    private Store getStore(ExtensionContext context) {
        return context.getStore(Namespace.create(getClass(), context.getRequiredTestMethod()));
    }
}
