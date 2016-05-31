package com.kingbase.service.manager;

import java.util.ArrayList;
import java.util.List;
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
	
	@WebMethod(operationName = "getAll")
	public List<DataManager> getAll(){
		return new ArrayList<>(map.values());
	}
	
	@WebMethod(operationName = "add")
	public boolean add(String id,List<DataManager> dataManagers){
		if(dataManagers==null){
			throw new IllegalArgumentException("数据不能为空");
		}
		for (DataManager dataManager : dataManagers) {
			map.put(id, dataManager);
		}
		
		return true;
	}
	
	public static void main(String[] args) {
		Endpoint.publish("http://192.168.8.144:9002/serices/dataService", new DataService());
	}
}
