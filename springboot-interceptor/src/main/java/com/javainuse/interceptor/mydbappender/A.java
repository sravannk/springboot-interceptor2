package com.javainuse.interceptor.mydbappender;

import java.lang.reflect.Method;
import java.security.Timestamp;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import ch.qos.logback.classic.spi.CallerData;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.ThrowableProxyUtil;
import ch.qos.logback.core.db.DBAppenderBase;
import ch.qos.logback.core.db.DBHelper;

public class A extends DBAppenderBase<ILoggingEvent> {

	protected String insertSQL;

	// Indices of the fields of the table in which the logging information is stored
	/*
	 * private static final int EVENTTIME_INDEX = 1; private static final int
	 * MESSAGE_INDEX = 2; private static final int LOGGER_INDEX = 3; private static
	 * final int LEVEL_INDEX = 4; private static final int CALLER_CLASS_INDEX = 5;
	 * private static final int CALLER_METHOD_INDEX = 6; private static final int
	 * CALLER_LINE_INDEX = 7; private static final int TRACE_INDEX = 8;
	 */

	static final int requestid_INDEX = 1;
	static final int uri_INDEX = 2;
	static final int uritype_INDEX = 3;
	static final int operation_INDEX = 4;
	static final int status_INDEX = 5;
	static final int servicetype_INDEX = 6;
	static final int parent_INDEX = 7;
	static final int EVENT_KEY_INDEX = 8;

	static final StackTraceElement EMPTY_CALLER_DATA = CallerData.naInstance();

	@Override
	public void append(ILoggingEvent eventObject) {

		Connection connection = null;
		PreparedStatement insertStatement = null;
		try {
			connection = connectionSource.getConnection();
			connection.setAutoCommit(false);

			insertStatement = connection.prepareStatement(getInsertSQL());

			// Inserting the event in database
			synchronized (this) {
				subAppend(eventObject, connection, insertStatement);
			}
			secondarySubAppend(eventObject, connection, 1);

			connection.commit();

		} catch (Throwable sqle) {
			addError("problem appending event", sqle);
		} finally {
			DBHelper.closeStatement(insertStatement);
			DBHelper.closeConnection(connection);
		}
	}

	@Override
	protected Method getGeneratedKeysMethod() {
		return null;
	}

	@Override
	protected String getInsertSQL() {
		return insertSQL;
	}

	@Override
	protected void secondarySubAppend(ILoggingEvent eventObject, Connection connection, long eventId) throws Throwable {
	}

	@Override
	public void start() {
		insertSQL = A.buildInsertSQL();
		System.out.println(insertSQL);
		super.start();
	}

	@Override
	protected void subAppend(ILoggingEvent event, Connection connection, PreparedStatement insertStatement)
			throws Throwable {

		bindLoggingEventWithInsertStatement(insertStatement, event);
		bindCallerDataWithPreparedStatement(insertStatement, event.getCallerData());

		int updateCount = insertStatement.executeUpdate();
		if (updateCount != 1) {
			addWarn("Failed to insert loggingEvent");
		}
	}

	void bindCallerDataWithPreparedStatement(PreparedStatement stmt, StackTraceElement[] callerDataArray)
			throws SQLException {

		StackTraceElement caller = extractFirstCaller(callerDataArray);

		/*
		 * stmt.setString(CALLER_CLASS_INDEX, caller.getClassName());
		 * stmt.setString(CALLER_METHOD_INDEX, caller.getMethodName());
		 * stmt.setString(CALLER_LINE_INDEX, Integer.toString(caller.getLineNumber()));
		 */
	}

	private java.sql.Date getSqlDate() {
		Date now = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSX");
		String nowDateStr = sdf.format(now);
		System.out.println("nowDateStr = " + nowDateStr);

		java.sql.Date toDB = null;
		try {
			toDB = new java.sql.Date(sdf.parse(nowDateStr).getTime());
			System.out.println("toDB = " + toDB);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return toDB;
	}

	void bindLoggingEventWithInsertStatement(PreparedStatement stmt, ILoggingEvent event) throws SQLException {

		/*stmt.setTimestamp(EVENTTIME_INDEX, new Timestamp(event.getTimeStamp()));
		stmt.setString(MESSAGE_INDEX, event.getFormattedMessage());
		stmt.setString(LOGGER_INDEX, event.getLoggerName());
		stmt.setString(LEVEL_INDEX, event.getLevel().toString());
*/
		 stmt.setString(requestid_INDEX, "even"); 
		  stmt.setString(uri_INDEX, "url"); 
		  stmt.setString(uritype_INDEX, "server"); 
		  stmt.setString(operation_INDEX, "name"); 
		  stmt.setString(status_INDEX, "vani.getStatus()");
		  //stmt.setString(request_INDEX, event.getThreadName()); 
		  //stmt.setString(response_INDEX, event.getThreadName()); 
		  stmt.setString(servicetype_INDEX, "vani.getServername()"); 
		  stmt.setString(parent_INDEX, "even"); 
		
	/*	if (event.getThrowableProxy() != null && event.getThrowableProxy().getStackTraceElementProxyArray() != null)
			stmt.setString(TRACE_INDEX, ThrowableProxyUtil.asString(event.getThrowableProxy()));
		else
			stmt.setString(TRACE_INDEX, null);
	}
*/
	}

	private static String buildInsertSQL() {

		return "INSERT INTO IOT_TRANSACTION"
				+ " (requestid, uri, uritype, operation, status, servicetype, parent) " + "VALUES "
				+ "(?, ?, ? ,?, ?, ?, ?)";
	}

	private StackTraceElement extractFirstCaller(StackTraceElement[] callerDataArray) {

		StackTraceElement caller = EMPTY_CALLER_DATA;
		if (hasAtLeastOneNonNullElement(callerDataArray))
			caller = callerDataArray[0];

		return caller;
	}

	private boolean hasAtLeastOneNonNullElement(StackTraceElement[] callerDataArray) {
		return callerDataArray != null && callerDataArray.length > 0 && callerDataArray[0] != null;
	}
}
