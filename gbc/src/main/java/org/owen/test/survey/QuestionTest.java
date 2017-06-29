package org.owen.test.survey;

import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.owen.survey.Question;

public class QuestionTest {

	@Test
	public void testGetQuestionList() {
		Question q = new Question();
		Map<Integer, List<Question>> qList = q.getQuestionList();
		Assert.assertNotNull(qList);		
	}
}
