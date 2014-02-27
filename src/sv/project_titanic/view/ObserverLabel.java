package sv.project_titanic.view;

import java.util.Observable;
import java.util.Observer;
import javax.swing.JLabel;

public class ObserverLabel extends JLabel implements Observer {
	public ObserverLabel(String text) {
		super(text);
	}

	public void update(Observable o, Object arg) {
		setText((String)arg);
	}
}

