package com.kingbase.service.manager;

import java.util.concurrent.ConcurrentHashMap;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.ws.Endpoint;

@WebService(serviceName = "DataService")
public class DataService {

	private static final ConcurrentHashMap<String, DataManager> map = new ConcurrentHashMap<String, DataManager>();

	@WebMethod(operationName = "get")
	public DataManager get(@WebParam(name = "id") String id) {
		DataManager dataManager = map.get(id);
		if (dataManager == null) {
			throw new IllegalArgumentException("查询资源不存在");
		}
		return dataManager;
	}

	@WebMethod(operationName = "update")
	public boolean update(@WebParam(name = "id") String id, @WebParam(name = "dataManager") DataManager dataManager) {
		if (dataManager == null) {
			return false;
		}

		map.put(id, dataManager);
		return true;
	}

	@WebMethod(operationName = "delete")
	public DataManager delete(@WebParam(name = "id") String id) {
		return map.remove(id);
	}
	
	public static void main(String[] args) {
		Endpoint.publish("http://localhost:9002/serices/dataService", new DataService());
	}
}
