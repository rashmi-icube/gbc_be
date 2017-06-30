package org.owen.test.survey;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.owen.survey.Response;
import org.owen.survey.ResponseHelper;

public class ResponseHelperTest {

	@Test
	public void testSaveAllResponses() {
		List<Response> respList = new ArrayList<>();

		Response rObj1 = new Response();
		rObj1.setQuestionId(1);
		rObj1.setOptionId(1);
		rObj1.setComment("test comment");
		respList.add(rObj1);

		Response rObj2 = new Response();
		rObj2.setQuestionId(2);
		rObj2.setOptionId(2);
		rObj2.setComment("");
		respList.add(rObj2);

		Response rObj3 = new Response();
		rObj3.setQuestionId(27);
		rObj3.setOptionId(3);
		rObj3.setComment("");
		respList.add(rObj3);

		Response rObj4 = new Response();
		rObj4.setQuestionId(52);
		rObj4.setOptionId(0);
		rObj4.setComment("test comment");
		respList.add(rObj4);

		Response rObj5 = new Response();
		rObj5.setQuestionId(54);
		rObj5.setOptionId(1);
		rObj5.setComment("");
		respList.add(rObj5);

		ResponseHelper rh = new ResponseHelper();
		Assert.assertTrue(rh.saveResponses(respList));

	}

}
