package org.owen.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ org.owen.test.survey.QuestionTest.class, org.owen.test.survey.ResponseHelperTest.class })
public class AllTests {

}
