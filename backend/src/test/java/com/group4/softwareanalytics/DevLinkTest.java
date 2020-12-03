package com.group4.softwareanalytics;


import com.group4.softwareanalytics.developer.DeveloperExpertise;
import com.group4.softwareanalytics.developer.DeveloperPR;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class DevLinkTest {

    @Autowired
    private AsyncService asyncService;

    @Test
    void IssueDevLinkTest() throws InvocationTargetException, IllegalAccessException {

        String owner = "ManbangGroup";
        String name = "Phantom";
        String username = "shaobin0604";

        ArrayList<DeveloperPR> developerPRRatesList = new ArrayList<>();

        DeveloperPR actualDevPR = asyncService.linkIssueDev(owner, name, username, 21, developerPRRatesList);

        DeveloperPR ExpectedDevPR = new DeveloperPR(owner,name, username);

        ExpectedDevPR.setOpened(1);
        ExpectedDevPR.setAccepted_opened(1);
        ExpectedDevPR.setReviewed(1);
        ExpectedDevPR.setAccepted_reviewed(1);
        ExpectedDevPR.addPROpened(21);
        ExpectedDevPR.addPRreviewed(21);


        for (Method m : actualDevPR.getClass().getMethods()) {
            if (m.getName().startsWith("get") && m.getParameterTypes().length == 0) {
                final Object actMet = m.invoke(actualDevPR);
                final Object expMet = m.invoke(ExpectedDevPR);
                assertEquals(actMet,expMet);
            }
        }
    }


    @Test
    void CommitDevLinkTest() throws InvocationTargetException, IllegalAccessException {
        String owner = "ManbangGroup";
        String name = "Phantom";
        String email = "user@test.com";
        String commitHash = "123";


        ArrayList<DeveloperExpertise> developerExpertiseList = new ArrayList<>();

        DeveloperExpertise actualDexExpr = asyncService.linkCommitDev(owner,name,email,commitHash, developerExpertiseList, new CommitEntry(0,0,0,0,0,0,0,0,0), new Date());

        DeveloperExpertise expectedDevExpr = new DeveloperExpertise(owner,name,1,email);

        expectedDevExpr.addCommitHash("123");

        for (Method m : actualDexExpr.getClass().getMethods()) {
            if (m.getName().startsWith("get") && m.getParameterTypes().length == 0) {
                final Object actMet = m.invoke(actualDexExpr);
                final Object expMet = m.invoke(expectedDevExpr);
                assertEquals(actMet,expMet);
            }
        }

        String newCommitHash = "123";

        DeveloperExpertise oldDexExpr = asyncService.linkCommitDev(owner,name,email,newCommitHash, developerExpertiseList, new CommitEntry(0,0,0,0,0,0,0,0,0), new Date());


        assertTrue(oldDexExpr.getExpertise() == 2);
    }
}
