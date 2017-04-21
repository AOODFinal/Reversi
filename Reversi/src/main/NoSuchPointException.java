package main;
/**
 * Class that is used to tell ghostCheck that there is no valid point in line to place a ghost
 * @author Morgan
 *
 */
public class NoSuchPointException extends Exception {
	public NoSuchPointException(String string) {
		super(string);
	}

	public NoSuchPointException() {
		super();
	}

	private static final long serialVersionUID = 7064516393467927299L;
}
