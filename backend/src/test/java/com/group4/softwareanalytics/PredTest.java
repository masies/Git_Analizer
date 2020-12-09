package com.group4.softwareanalytics;


import com.group4.softwareanalytics.commits.Commit;
import com.group4.softwareanalytics.commits.CommitDiff;
import com.group4.softwareanalytics.commits.CommitRepository;
import com.group4.softwareanalytics.metrics.ProjectMetric;
import com.group4.softwareanalytics.repository.RepoRepository;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import weka.core.Instance;
import weka.core.Instances;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
public class PredTest {

    @Autowired
    private AsyncService asyncService;

    
    public static int randInt(int min, int max) {

        int range = max - min + 1;

        // Math.random() function will return a random no between [0.0,1.0).
        int res = (int) ( Math.random()*range)+min;

        return res;

    }

    public boolean getRandomBoolean() {
        Random random = new Random();
        return random.nextBoolean();
    }

    @Test
    void testPredictor() throws Exception {

        List<CommitEntry> ces = new ArrayList<>();


        List<String> nullFields = Arrays.asList("setDate","setDeveloperMail","setCommitHash","setContibutions");

        Method[] publicMethods = CommitEntry.class.getMethods();
        CommitEntry testObj = new CommitEntry(0,0,0,0,0,0,0,0,0);

        for(int i = 0; i<40;i++) {
            testObj = new CommitEntry(0,0,0,0,0,0,0,0,0);
            for (Method aMethod : publicMethods) {
                if (aMethod.getName().startsWith("set") && aMethod.getParameterCount() == 1 && !nullFields.contains(aMethod.getName())) {
                    if(aMethod.getName().equals("setBuggy"))
                    {
                        Object[] parms = new Object[]{getRandomBoolean()};
                        aMethod.invoke(testObj, parms);
                    }
                    else {
                        Object[] parms = new Object[]{randInt(1, 5)};
                        aMethod.invoke(testObj, parms);
                    }
                }

            }
            ces.add(testObj);
        }

        Instances actualSet = Predictor.createArfFile(ces);

        assertNotNull(actualSet);

        assertEquals(40,actualSet.size());

        PredictorStats ps = Predictor.evaluate(actualSet);

        assertNotNull(ps.getAccuracy());
        assertNotNull(ps.getPrecision());
        assertNotNull(ps.getRecall());


        List<CommitEntry> lastTen = ces.subList(ces.size() - 10, ces.size());

        Instances preList = Predictor.createArfFile(lastTen);

        assertEquals(10,preList.size());
    }
}
