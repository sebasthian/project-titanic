package sv.project_titanic.connection;

import java.awt.event.ActionEvent;

public class ClientActionEvent extends ActionEvent {

	Object recieveObject;
	public ClientActionEvent(Object source, Object recieveObject, int id, String command)
	{
		super(source, id, command);
		this.recieveObject = recieveObject;
	}
	
	Object getRecieveObject() { return recieveObject; }
}	
