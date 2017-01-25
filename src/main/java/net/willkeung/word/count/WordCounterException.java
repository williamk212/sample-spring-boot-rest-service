/**
 * 
 */
package net.willkeung.word.count;

/**
 * @author willkeung
 *
 */
public class WordCounterException extends RuntimeException {

	private static final long serialVersionUID = -44553900375414108L;

	/**
	 * @param message
	 */
	public WordCounterException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public WordCounterException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public WordCounterException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace
	 */
	public WordCounterException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
