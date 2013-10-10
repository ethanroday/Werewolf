package edu.wm.werewolf;

public class JSONResponse {

	private String status;
	private Object object;
	private Class<?> objClass;
	
	public JSONResponse() {}
	
	public JSONResponse(String status, Object object) {
		this.status = status;
		this.object = object;
		if (object == null) {
			this.objClass = null;
		} else {
			this.objClass = object.getClass();
		}
	}

	public Class<?> getObjClass() {
		return objClass;
	}

	public void setObjClass(Class<?> objClass) {
		this.objClass = objClass;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Object getObject() {
		return object;
	}

	public void setObject(Object object) {
		this.object = object;
	}
	
}
