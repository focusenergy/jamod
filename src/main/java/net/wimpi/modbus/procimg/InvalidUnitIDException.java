package net.wimpi.modbus.procimg;

/**
 * Exception class for requests to this modbus slave with the wrong unit ID.
 * If requests go to a process image with an invalid unit ID or a unit ID that
 * does not correspond to the slave, then it is expected that the process will
 * throw this exception.  This signifies to the request that no response should
 * be sent back to the requestor; as it is likely the message was destined to 
 * another node on the network.
 * <p>
 * @author Charles Hache
 *
 */
public class InvalidUnitIDException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2913820338232895833L;
	
	/**
	 * Creates a new InvalidUnitIDException.
	 */
	public InvalidUnitIDException() {
		super();
	}
	
	/** Creates a new InvalidUnitIDException with the given message.
	 * @param message A message for the exception.
	 */
	public InvalidUnitIDException(String message) {
		super(message);
	}

}
