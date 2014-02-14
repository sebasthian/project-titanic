package sv.project_titanic.connection;

import java.awt.event.ActionEvent;

public class ClientActionEvent extends ActionEvent {

	Object recieveObject;
	public ClientActionEvent(Object recieveObject ,Object arg0, int arg1, String arg2)
	{
		super(arg0, arg1, arg2);
		this.recieveObject = recieveObject;
	}
	
	Object getRecieveObject() { return recieveObject; }
}
