package sv.project_titanic.connection;
import java.awt.event.ActionEvent;

public class ClientActionEvent extends ActionEvent {

	/**
	 * Not really sure what this is used for.
	 */
	private static final long serialVersionUID = 1L;
	Object recieveObject;
	
	public ClientActionEvent(Object source, Object recieveObject, int id, String command)
	{
		super(source, id, command);
		this.recieveObject = recieveObject;
	}
	
	public Object getRecieveObject() { return recieveObject; }
}	
