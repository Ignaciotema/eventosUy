package logica.models;

import logica.controllers.IControllerEvento;
import logica.controllers.IControllerUsuario;

public class Factory {
	
	private static Factory instance = null;
	
	public static Factory getInstance() {
		if (instance == null) {
			instance = new Factory();
		}
		return instance;
	}
	
	private Factory() {};
	
	public IControllerUsuario getControllerUsuario() {
		return new ControllerUsuario();
	}

	public IControllerEvento getControllerEvento() {
		return new ControllerEvento();
	}

}
