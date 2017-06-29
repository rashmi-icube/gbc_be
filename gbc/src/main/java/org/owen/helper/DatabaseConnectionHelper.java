package org.owen.helper;

import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.apache.tomcat.jdbc.pool.PoolProperties;

public class DatabaseConnectionHelper {

	static DatabaseConnectionHelper dch;

	static public DatabaseConnectionHelper getDBHelper() {
		if (dch == null) {
			dch = new DatabaseConnectionHelper();
		}
		return dch;
	}

	public DataSource masterDS;

	private final static String MASTER_URL = UtilHelper.getConfigProperty("master_sql_url");
	private final static String MASTER_USER = UtilHelper.getConfigProperty("master_sql_user");
	private final static String MASTER_PASSWORD = UtilHelper.getConfigProperty("master_sql_password");

	public DatabaseConnectionHelper() {

		PoolProperties p = new PoolProperties();
		p.setUrl(MASTER_URL);
		p.setDriverClassName("com.mysql.jdbc.Driver");
		p.setUsername(MASTER_USER);
		p.setPassword(MASTER_PASSWORD);
		p.setJmxEnabled(true);
		p.setTestWhileIdle(false);
		p.setTestOnBorrow(true);
		p.setValidationQuery("SELECT 1");
		p.setTestOnReturn(false);
		p.setValidationInterval(Integer.valueOf(UtilHelper.getConfigProperty("validationInterval")));
		p.setTimeBetweenEvictionRunsMillis(Integer.valueOf(UtilHelper.getConfigProperty("timeBetweenEvictionRunsMillis")));
		p.setMaxActive(Integer.valueOf(UtilHelper.getConfigProperty("maxActive")));
		p.setInitialSize(Integer.valueOf(UtilHelper.getConfigProperty("initialSize")));
		p.setMaxWait(Integer.valueOf(UtilHelper.getConfigProperty("maxWait")));
		p.setRemoveAbandonedTimeout(Integer.valueOf(UtilHelper.getConfigProperty("removeAbandonedTimeout")));
		p.setMinEvictableIdleTimeMillis(Integer.valueOf(UtilHelper.getConfigProperty("minEvictableIdleTimeMillis")));
		p.setMinIdle(Integer.valueOf(UtilHelper.getConfigProperty("minIdle")));
		p.setLogAbandoned(true);
		p.setConnectionProperties("connectionTimeout=\"300000\"");
		p.setRemoveAbandoned(true);
		p.setMaxIdle(Integer.valueOf(UtilHelper.getConfigProperty("maxIdle")));
		p.setJdbcInterceptors("org.apache.tomcat.jdbc.pool.interceptor.ConnectionState;"
				+ "org.apache.tomcat.jdbc.pool.interceptor.ResetAbandonedTimer");
		masterDS = new DataSource();
		masterDS.setPoolProperties(p);

	}

	@Override
	public void finalize() {
		Logger.getLogger(DatabaseConnectionHelper.class).debug("Shutting down databases ...");
		try {
			if (!masterDS.getConnection().isClosed()) {
				try {
					masterDS.getConnection().close();
					masterDS.close();
					Logger.getLogger(DatabaseConnectionHelper.class).debug("Connection to master database closed!!!!");
				} catch (SQLException e) {
					Logger.getLogger(DatabaseConnectionHelper.class).error("An error occurred while closing the mysql connection", e);
				}
			}

		} catch (SQLException e) {
			Logger.getLogger(DatabaseConnectionHelper.class).error("An error occurred while attempting to close db connections", e);
		}
	}

}
