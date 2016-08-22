package com.example.stockmonitor.dataaccess;
@SuppressWarnings("serial")
/**
 * This class represents an error occurring while accessing stock information
 * @author tuandao
 *
 */
public class DataAccessException extends Exception{
	public DataAccessException() {
        // TODO Auto-generated constructor stub
    }

    public DataAccessException(String message) {
        super(message);
        // TODO Auto-generated constructor stub
    }

    public DataAccessException(Throwable cause) {
        super(cause);
        // TODO Auto-generated constructor stub
    }

    public DataAccessException(String message, Throwable cause) {
        super(message, cause);
        // TODO Auto-generated constructor stub
    }
}