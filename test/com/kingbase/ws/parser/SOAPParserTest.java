package com.kingbase.ws.parser;

import java.util.List;

import com.kingbase.ws.bean.BindingBean;
import com.kingbase.ws.bean.OperationBean;
import com.kingbase.ws.bean.ParameterTypeBean;
import com.kingbase.ws.bean.PortTypeBean;
import com.kingbase.ws.bean.ServiceBean;
import com.kingbase.ws.utils.ParameterUtil;

import junit.framework.TestCase;

public class SOAPParserTest extends TestCase{
	String url="";
	
	public void testParse(){
		//url="http://www.webxml.com.cn/webservices/ChinaStockSmallImageWS.asmx?wsdl";
		//url="http://localhost:9000/serices/HelloWorld?wsdl";
		//url="http://localhost:9001/serices/complex?wsdl";
		//url="http://192.168.1.36:8080/default/orgbizService?wsdl";
		//url="http://localhost:8080/axis2/services/Version?wsdl";
		url="http://192.168.8.144:8000/default/WSWorklistQueryManagerServiceMock?wsdl";
		
		SOAPParser parser=new SOAPParser();
		
		ServiceBean serviceBean = parser.parser(url);
		System.out.println(serviceBean);
		
		System.out.println("...............................");
		
		List<BindingBean> bindingBeans = serviceBean.getBindingBeans();
		for (BindingBean bindingBean : bindingBeans) {
			System.out.println(bindingBean);
		}
		System.out.println("...............................");
		
		List<PortTypeBean> portTypeBeans = serviceBean.getPortTypeBeans();
		for (PortTypeBean portTypeBean : portTypeBeans) {
			System.out.println(portTypeBean);
		}
		System.out.println("...............................");
		
		List<OperationBean> operationBeans = serviceBean.getOperationBeans();
		for (OperationBean operationBean : operationBeans) {
			System.out.println(operationBean);
		}
		System.out.println("...............................");
		
		List<ParameterTypeBean> parameterTypes = serviceBean.getParameterTypes();
		for (ParameterTypeBean parameterTypeBean : parameterTypes) {
			System.out.println(parameterTypeBean);
		}
		System.out.println("...............................");
		
		List<String> importWSDL = serviceBean.getImportWSDL();
		for (String wsdl : importWSDL) {
			System.out.println(wsdl);
		}
		
		String serviceMessage = ParameterUtil.getServiceMessage(serviceBean);
		System.out.println(serviceMessage);
	}
}
