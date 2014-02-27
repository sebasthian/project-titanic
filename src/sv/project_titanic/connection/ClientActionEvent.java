package sv.project_titanic.connection;
import java.awt.event.ActionEvent;

/**Event class used by the TCPClient when recieving data from the server.*/
public class ClientActionEvent extends ActionEvent {

	/**
	 * Not really sure what this is used for.
	 */
	private static final long serialVersionUID = 1L;
	Object recieveObject;
	
	/**Create a new event object.
	 *
	 * @param recieveObject data recieved from server.
	 *
	 * @see java.awt.event.ActionEvent
	 */
	public ClientActionEvent(Object source, Object recieveObject, int id, String command)
	{
		super(source, id, command);
		this.recieveObject = recieveObject;
	}
	
	/**@return the data recieved from server.*/
	public Object getRecieveObject() { return recieveObject; }
}	
