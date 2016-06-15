package com.kingbase.ws.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.dom4j.Element;

import com.kingbase.ws.bean.OperationBean;
import com.kingbase.ws.bean.ParameterBean;
import com.kingbase.ws.bean.ServiceBean;
import com.kingbase.ws.exceptions.WSDLServiceException;
import com.kingbase.ws.utils.ElementUtil;

/**
 * 解析wsdl 的message标签
 * @author ganliang
 *
 */
public class MessageParse {

	/**
	 * 解析message
	 * @param element 根元素节点
	 * @param serviceBean 服务实体对象
	 */
	public static void parse(Element element,ServiceBean serviceBean){
		if(element==null){
			throw new WSDLServiceException("节点不能为空");
		}
		if(!element.isRootElement()){
			throw new WSDLServiceException("types节点存在于根节点");
		}
		//找到所有的message节点
		List<Element> messageElements = ElementUtil.findElements(element, "message");
		
		Map<String,List<ParameterBean>> operationMap=new HashMap<String,List<ParameterBean>>();
		
		//遍历每一个message节点
		for (Element messageElement : messageElements) {
			List<ParameterBean> parameters=new ArrayList<ParameterBean>();
			
			//获取方法
			String method=messageElement.attributeValue("name");
			
			//找到参数元数节点
			List<Element> partElements = ElementUtil.findElements(messageElement, "part");
			
			for (Element partElement : partElements) {
				String parameterName=partElement.attributeValue("name");
				String parameterType=partElement.attributeValue("type");
				if(parameterType!=null){
					String[] strings = parameterType.split(":");
					if(strings.length>1){
						parameterType=strings[1];
					}
				}
				
				ParameterBean parameterBean=new ParameterBean();
				parameterBean.setParameterName(parameterName);
				parameterBean.setParameterType(parameterType);
				
				parameters.add(parameterBean);
			}
			
			operationMap.put(method, parameters);
		}
		
		List<OperationBean> operationBeans=new ArrayList<OperationBean>();
		//遍历方法
		for (Entry<String, List<ParameterBean>> entry : operationMap.entrySet()) {
			String operationName=entry.getKey();
			if(!operationName.contains("Response")){
				
				OperationBean operationBean=new OperationBean();
				operationBean.setOperationName(operationName);
				
				operationBean.setInParameters(entry.getValue());
				
				List<ParameterBean> outParameters = operationMap.get(operationName+"Response");
				operationBean.setOutParameters(outParameters);
				
				operationBeans.add(operationBean);
			}
		}
		serviceBean.setOperationBeans(operationBeans);
	}
}
