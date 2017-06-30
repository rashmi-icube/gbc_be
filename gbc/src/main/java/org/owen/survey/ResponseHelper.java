package org.owen.survey;

import java.sql.CallableStatement;
import java.sql.Connection;

import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Types;
import java.util.List;

import org.apache.log4j.Logger;
import org.owen.helper.DatabaseConnectionHelper;

public class ResponseHelper {

	public boolean saveResponses(List<Response> responseList) {

		boolean allResponsesSaved = true;

		if (!responseList.isEmpty()) {
			try {
				Logger.getLogger(ResponseHelper.class).debug("Entering saveAllResponses");
				DatabaseConnectionHelper dch = DatabaseConnectionHelper.getDBHelper();
				Connection con = dch.masterDS.getConnection();
				Statement stmt = con.createStatement();
				String query = "";
				for (int i = 0; i < responseList.size(); i++) {
					Response respObj = responseList.get(i);
					String comment = respObj.getComment() == "" ? null : ("'" + respObj.getComment() + "'");
					query = "insert into response(que_id, option_id, commnet_text, response_time) values(" + respObj.getQuestionId() + ","
							+ (respObj.getOptionId() == 0 ? null : respObj.getOptionId()) + "," + comment + ", CURRENT_TIMESTAMP())";
					Logger.getLogger(ResponseHelper.class).debug("Query ::: " + query);
					stmt.addBatch(query);
				}
				con.setAutoCommit(false);
				/*ResultSet rs = stmt.executeQuery("select * from response");
				rs.last();
				System.out.println("rows before batch execution= " + rs.getRow());*/
				stmt.executeBatch();
				/*rs = stmt.executeQuery("select * from response");
				rs.last();
				System.out.println("rows after batch execution= " + rs.getRow());*/
				con.commit();

			} catch (Exception e) {
				allResponsesSaved = false;
				Logger.getLogger(ResponseHelper.class).error("Exception while saving the responses" , e);
			}
		}
		return allResponsesSaved;
	}

	public boolean saveAllResponses(List<Response> responseList) {
		boolean allResponsesSaved = true;

		if (!responseList.isEmpty()) {
			Logger.getLogger(ResponseHelper.class).debug("Entering saveAllResponses");

			for (int i = 0; i < responseList.size(); i++) {
				Response respObj = responseList.get(i);
				Logger.getLogger(ResponseHelper.class).debug("Entering saveAllResponses for question ID" + respObj.getQuestionId());
				boolean flag = saveTextResponse(respObj);
				allResponsesSaved = (allResponsesSaved || flag);
			}
		}
		return allResponsesSaved;
	}

	public boolean saveTextResponse(Response respObj) {
		boolean responseSaved = false;
		DatabaseConnectionHelper dch = DatabaseConnectionHelper.getDBHelper();
		int questionId = respObj.getQuestionId();
		try (CallableStatement cstmt = dch.masterDS.getConnection().prepareCall("{call saveResponse(?,?,?)}")) {
			cstmt.setInt("queId", questionId);
			if (respObj.getOptionId() == 0) {
				cstmt.setNull("optId", Types.INTEGER);
			} else {
				cstmt.setObject("optId", respObj.getOptionId());
			}
			if (respObj.getComment().isEmpty()) {
				cstmt.setNull("commTxt", Types.VARCHAR);
			} else {
				cstmt.setString("commTxt", respObj.getComment());
			}
			Logger.getLogger(ResponseHelper.class).debug("SQL statement for question : " + questionId + " : " + cstmt.toString());
			try (ResultSet rs = cstmt.executeQuery()) {
				if (rs.next()) {
					Logger.getLogger(ResponseHelper.class).debug("RS statement for question : " + questionId + " : " + rs.toString());
					responseSaved = true;
					Logger.getLogger(ResponseHelper.class).debug("Successfully saved the response for : " + questionId);
				}
			}

		} catch (Exception e) {
			Logger.getLogger(ResponseHelper.class).error("Exception while saving the response for question : " + questionId, e);
		}

		return responseSaved;
	}

}
