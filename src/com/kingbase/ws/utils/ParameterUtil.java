package com.kingbase.ws.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.kingbase.ws.bean.BindingBean;
import com.kingbase.ws.bean.OperationBean;
import com.kingbase.ws.bean.ParameterBean;
import com.kingbase.ws.bean.ParameterTypeBean;
import com.kingbase.ws.bean.ParameterTypeBean.BasicTypeBean;
import com.kingbase.ws.bean.ServiceBean;

public class ParameterUtil {

	/**
	 * 获取服务、方法、参数详情
	 * @param serviceBean
	 * @return
	 */
	public static String getServiceMessage(ServiceBean serviceBean){
		if(serviceBean==null){
			return "[]";
		}
		
		List<BindingBean> bindingBeans = serviceBean.getBindingBeans();
		if(bindingBeans==null||bindingBeans.size()==0){
			throw new IllegalArgumentException("WSDL解析异常");
		}
		String operationsMessage = getOperationsMessage(serviceBean);
		
		//获取文档的注释
		String documentation = serviceBean.getDocumentation();
		try {
			if(documentation!=null&&!"".equals(documentation)){
				documentation=URLEncoder.encode(documentation,"UTF-8");
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		StringBuilder serviceBuilder=new StringBuilder("[");
		//遍历 binding
		Iterator<BindingBean> iterator = bindingBeans.iterator();
		while(iterator.hasNext()){
			BindingBean bindingBean = iterator.next();
			serviceBuilder.append("{\"text\":\""+bindingBean.getBindingName()+"\",\"doc\":\""+documentation+"\",\"children\":"+operationsMessage+",\"state\":\"closed\"}");
			if(iterator.hasNext()){
				serviceBuilder.append(",");
			}
		}
		serviceBuilder.append("]");
		return serviceBuilder.toString();
	}
	
	/**
	 * 获取方法的 json 数据
	 * @param serviceBean
	 * @return
	 */
	public static String getOperationsMessage(ServiceBean serviceBean){
		//获取方法 参数
		List<OperationBean> operationBeans = serviceBean.getOperationBeans();
		if(operationBeans==null||operationBeans.size()==0){
			return "[]";
		}
		StringBuilder operationBuilder=new StringBuilder("[");
		try {
			Iterator<OperationBean> iterator = operationBeans.iterator();
			while(iterator.hasNext()){
				OperationBean operationBean = iterator.next();
				
				//获取方法的注释
				String documentation = operationBean.getDocumentation();
				if(documentation!=null&&!"".equals(documentation)){
					documentation=URLEncoder.encode(documentation,"UTF-8");
				}
				//获取方法的输入参数
				String inParameters=getParametesMessage(operationBean.getInParameters());
				
				//获取方法的输出参数
				String outParameters=getParametesMessage(operationBean.getOutParameters());
				
				if("[]".equals(inParameters)){
					operationBuilder.append("{\"text\":\""+operationBean.getOperationName()+"\",\"doc\":\""+documentation+"\",\"outParams\":"+outParameters+",\"children\":"+inParameters+"}");
				}else{
					operationBuilder.append("{\"text\":\""+operationBean.getOperationName()+"\",\"doc\":\""+documentation+"\",\"outParams\":"+outParameters+",\"children\":"+inParameters+",\"state\":\"closed\"}");
				}
				if(iterator.hasNext()){
					operationBuilder.append(",");
				}
			}
			operationBuilder.append("]");
		}catch(Exception e){
			e.printStackTrace();
		}	
		return operationBuilder.toString();
	}
	
	/**
	 * 获取方法的输入参数
	 * @param operationBean 方法实体对象
	 * @return
	 */
	private static String getParametesMessage(List<ParameterBean> parameters) {
		if(parameters==null||parameters.size()==0){
			return "[]";
		}
		
		StringBuilder builder=new StringBuilder("[");
		//遍历参数
		Iterator<ParameterBean> iterator = parameters.iterator();
		while(iterator.hasNext()){
			ParameterBean parameterBean = iterator.next();
			builder.append("{\"text\":\""+parameterBean.getParameterName()+"\",\"type\":\""+parameterBean.getParameterType()+"\"}");
			if(iterator.hasNext()){
				builder.append(",");
			}
		}
		builder.append("]");
		
		return builder.toString();
	}

	/**
	 * 打印多个services
	 * @param serviceBeans
	 * @return
	 */
	public static String printServices(List<ServiceBean> serviceBeans){
		if(serviceBeans==null||serviceBeans.size()==0){
			return "[]";
		}
		StringBuilder serviceBuilder=new StringBuilder("[");
		Iterator<ServiceBean> iterator = serviceBeans.iterator();
		while(iterator.hasNext()){
			ServiceBean serviceBean = iterator.next();
			
			String printService = printService(serviceBean);
			
			serviceBuilder.append(printService);
		    if(iterator.hasNext()){
		    	serviceBuilder.append(",");
		    }
		}
		serviceBuilder.append("]");
		return serviceBuilder.toString();
	}
	
	/**
	 * 打印单个服务
	 * @param serviceBean
	 * @return
	 */
	public static String printService(ServiceBean serviceBean){
		//选择 binding
		List<OperationBean> operations = serviceBean.getOperationBeans();
		
		if(operations==null||operations.size()==0){
			return "[]";
		}
		StringBuilder operationBuilder=new StringBuilder("[");
		
		Iterator<OperationBean> iterator = operations.iterator();
		String operationJSON="";
		try {
			while(iterator.hasNext()){
				OperationBean operationBean = iterator.next();
				
				String documentation = operationBean.getDocumentation();
				if(documentation!=null&&!"".equals(documentation)){
					documentation=URLEncoder.encode(documentation,"UTF-8");
				}
				operationBuilder.append("{\"text\":\""+operationBean.getOperationName()+"\",\"parentName\":\""+serviceBean.getServiceName()+"\",\"doc\":\""+documentation+"\"}");
				if(iterator.hasNext()){
					operationBuilder.append(",");
				}
			}
			operationBuilder.append("]");
			
			String serviceDoc = serviceBean.getDocumentation();
			if(serviceDoc!=null&&!"".equals(serviceDoc)){
				serviceDoc=URLEncoder.encode(serviceDoc,"UTF-8");
			}
			operationJSON="{\"text\":\""+serviceBean.getServiceName()+"\",\"doc\":\""+serviceDoc+"\",\"children\":"+operationBuilder.toString()+"}";
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return operationJSON;
	}
	
	
	/**
	 * 获取方法的输入参数
	 * @param serviceBean
	 * @param methodName
	 * @return
	 */
	public static String getInParameter(ServiceBean serviceBean,String methodName,Map<String,Object> parameterMap){
		if(serviceBean==null||methodName==null||"".equals(methodName)){
			throw new IllegalArgumentException();
		}
		if(parameterMap==null){
			parameterMap=new HashMap<String,Object>();
		}
		StringBuilder parameterBuilder=new StringBuilder();
		
		OperationBean operation = getOperation(serviceBean, methodName);
		if(operation==null){
			throw new IllegalArgumentException("方法【"+methodName+"】不存在");
		}

		String xmlns="xmlns";
		if("xsd".equalsIgnoreCase(serviceBean.getWsdlType())){
			methodName="xsd:"+methodName;
			xmlns="xmlns:xsd";
		}
		
		parameterBuilder.append("<"+methodName+" "+xmlns+"=\""+serviceBean.getTargetNamespace()+"\">");
		//遍历 输入参数
		List<ParameterBean> inParameters = operation.getInParameters();
		if(inParameters!=null){
			for (ParameterBean parameterBean : inParameters) {
				String parameterType = parameterBean.getParameterType();
				String parameterName = parameterBean.getParameterName();
				
				recursionParameter(serviceBean,parameterName,parameterMap,parameterType,parameterBuilder);
			}
		}
		
		parameterBuilder.append("</"+methodName+">");
		return parameterBuilder.toString();
	}
	
	/**
	 * 递归 获取参数
	 * @param serviceBean
	 * @param parameterName
	 * @param parameterType
	 * @param parameterBuilder
	 */
	private static void recursionParameter(ServiceBean serviceBean, String parameterName,Map<String,Object> parameterMap,
			String parameterType, StringBuilder parameterBuilder) {
		//基本类型
		if(ParameterTypeBean.basicTypes.contains(parameterType)){
			Object parameterValue=parameterMap.get(parameterName);
			if(parameterValue==null||"".equals(parameterValue)){
				parameterValue="?";
			}
			parameterBuilder.append("<"+parameterName+">"+parameterValue+"</"+parameterName+">");
		}
		//用户自定义的对象 类型
		else{
			List<BasicTypeBean> basicTypeBeans = getBasicTypeBean(serviceBean, parameterType);
			if(basicTypeBeans==null){
				throw new IllegalArgumentException("解析出错,不存在类型【"+parameterType+"】");
			}
			parameterBuilder.append("<"+parameterName+">");
			//遍历类型
			for (BasicTypeBean basicTypeBean : basicTypeBeans) {
				String basicTypeName = basicTypeBean.getBasicTypeName();
				
				if(basicTypeBean.getBasicType().equals(parameterType)){
					Object basicTypeValue=parameterMap.get(basicTypeName);
					if(basicTypeValue==null||"".equals(basicTypeValue)){
						basicTypeValue="?";
					}
					parameterBuilder.append("<"+basicTypeName+">"+basicTypeValue+"</"+basicTypeName+">");
					continue;
				}
				recursionParameter(serviceBean, basicTypeName,parameterMap, basicTypeBean.getBasicType(), parameterBuilder);
			}
			parameterBuilder.append("</"+parameterName+">");
		}
	}

	/**
	 * 找到调用的方法
	 * @param operations
	 * @param methodName
	 * @return
	 */
	public static OperationBean getOperation(ServiceBean serviceBean,String methodName){
		//获取方法
		List<OperationBean> operationBeans = serviceBean.getOperationBeans();
		
		for (OperationBean operationBean : operationBeans) {
			if(operationBean.getOperationName().equals(methodName)){
				return operationBean;
			}
		}
		return null;
	}
	
	/**
	 * 自定义类型 下的类型
	 * @param serviceBean
	 * @param parameterType
	 * @return
	 */
	public static List<BasicTypeBean> getBasicTypeBean(ServiceBean serviceBean,String parameterType){
		//获取参数实体
		List<ParameterTypeBean> parameterTypes = serviceBean.getParameterTypes();
		for (ParameterTypeBean typeBean : parameterTypes) {
			//找到参数类型
			if(parameterType.equals(typeBean.getTypeName())){
				return typeBean.getBasicTypeBeans();
			}
		}
		return null;
	}

}
