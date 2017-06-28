package org.owen.survey;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.log4j.Logger;
import org.owen.helper.DatabaseConnectionHelper;

public class Question {

	private int questionId;
	private String questionText;
	private int sectionId;
	private QuestionType questionType;
	private int mandatory;

	public int getQuestionId() {
		return questionId;
	}

	public void setQuestionId(int questionId) {
		this.questionId = questionId;
	}

	public String getQuestionText() {
		return questionText;
	}

	public void setQuestionText(String questionText) {
		this.questionText = questionText;
	}

	public int getSectionId() {
		return sectionId;
	}

	public void setSectionId(int sectionId) {
		this.sectionId = sectionId;
	}

	public QuestionType getQuestionType() {
		return questionType;
	}

	public void setQuestionType(QuestionType questionType) {
		this.questionType = questionType;
	}

	public int getMandatory() {
		return mandatory;
	}

	public void setMandatory(int mandatory) {
		this.mandatory = mandatory;
	}

	public List<Question> getQuestionList() {
		DatabaseConnectionHelper dch = DatabaseConnectionHelper.getDBHelper();
		List<Question> questionList = new ArrayList<>();
		try (CallableStatement cstmt = dch.masterDS.getConnection().prepareCall("{call getQuestion()}")) {
			try (ResultSet rs = cstmt.executeQuery()) {
				while (rs.next()) {
					Question q = new Question();
					q.setQuestionId(rs.getInt("que_id"));
					q.setQuestionText(rs.getString("question"));
					q.setSectionId(rs.getInt("que_sec"));
					q.setQuestionType(QuestionType.get(rs.getInt("que_type")));
					q.setMandatory(rs.getInt("m_flag"));
					questionList.add(q);
				}
			}
			Logger.getLogger(Question.class).debug("Fetched " + questionList.size() + " questions.");

		} catch (SQLException e) {
			Logger.getLogger(Question.class).error("Exception while retrieving the questionList", e);
		}
		return questionList;
	}

	public Map<Integer, Map<Integer, String>> getOptionsList() {
		Map<Integer, Map<Integer, String>> result = new HashMap<>();
		DatabaseConnectionHelper dch = DatabaseConnectionHelper.getDBHelper();
		try (CallableStatement cstmt = dch.masterDS.getConnection().prepareCall("{call getOptions()}")) {
			try (ResultSet rs = cstmt.executeQuery()) {
				while (rs.next()) {
					int questionId = rs.getInt("que_id");
					if (result.containsKey(questionId)) {
						Map<Integer, String> oMap = result.get(questionId);
						oMap.put(rs.getInt("option_id"), rs.getString("option_text_display"));
						result.put(questionId, oMap);
					} else {
						Map<Integer, String> oMap = new TreeMap<>();
						oMap.put(rs.getInt("option_id"), rs.getString("option_text_display"));
						result.put(questionId, oMap);
					}
				}
			}

		} catch (SQLException e) {
			Logger.getLogger(Question.class).error("Exception while retrieving the option list", e);
		}

		return result;
	}
}
