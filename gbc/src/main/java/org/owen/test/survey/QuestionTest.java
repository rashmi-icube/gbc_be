package org.owen.test.survey;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.owen.survey.Question;

public class QuestionTest {

	@Test
	public void testGetQuestionList() {
		Question q = new Question();
		List<Question> qList = q.getQuestionList();
		Assert.assertNotNull(qList);
		Assert.assertNotNull(qList.get(0).getQuestionText());
		Assert.assertNotNull(qList.get(0).getMandatory());
		Assert.assertNotNull(qList.get(0).getOptionsList());
		Assert.assertNotNull(qList.get(0).getQuestionId());
		Assert.assertNotNull(qList.get(0).getQuestionType());
		Assert.assertNotNull(qList.get(0).getSectionId());
		
	}
}
