/**
 * 
 */
package net.willkeung.word;

/**
 * @author willkeung
 *
 */
public class WordCountControllerException extends RuntimeException {

	private static final long serialVersionUID = 4863998388540665319L;

	/**
	 * @param message
	 */
	public WordCountControllerException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public WordCountControllerException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public WordCountControllerException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace
	 */
	public WordCountControllerException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
