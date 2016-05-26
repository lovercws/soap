package com.kingbase.web.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.kingbase.ws.bean.ServiceBean;
import com.kingbase.ws.caller.HttpCaller;
import com.kingbase.ws.parser.SOAPParser;
import com.kingbase.ws.utils.ParameterUtil;
import com.kingbase.ws.utils.XMLUtil;

@WebServlet(urlPatterns={"/ServiceCallServlet"})
public class ServiceCallServlet extends HttpServlet{

	private static final long serialVersionUID = -994122804849307179L;

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setCharacterEncoding("utf-8");
		String type=request.getParameter("type");
		String wsdllocation=request.getParameter("wsdlLocation");//wsdl url
		String methodName=request.getParameter("methodName"); //方法名称
		String json="";
		//获取方法的参数
		if("getParameter".equals(type)){
			SOAPParser soapParser=new SOAPParser();
			try {
				//解析wsdl
				ServiceBean serviceBean = soapParser.parser(wsdllocation);
				//获取参数
				json = ParameterUtil.getInParameter(serviceBean, methodName);
				json=XMLUtil.printXML(json);
				//获取 参数xml
			} catch (Exception e) {
				json="{\"success\":false,msg:\""+e.getLocalizedMessage()+"\"}";
			}
		}
		//方法调用
		else if("methodCall".equals(type)){
			String parameterXML=request.getParameter("parameterXML");
			SOAPParser soapParser=new SOAPParser();
			HttpCaller httpCaller=new HttpCaller();
			try {
				//解析wsdl
				ServiceBean serviceBean = soapParser.parser(wsdllocation);
				
				parameterXML=parameterXML.replaceAll("\r", "");
				parameterXML=parameterXML.replaceAll("\n", "");
				parameterXML = parameterXML.replaceAll("> +", ">");
				
				//调用 返回结果
				json = httpCaller.caller(wsdllocation, serviceBean.getTargetNamespace(), serviceBean.getWsdlType(), methodName, parameterXML);
				
			}catch(Exception e){
				json="{\"success\":false,msg:\""+e.getLocalizedMessage()+"\"}";
			}
		}
		response.getWriter().print(json);
	}
}
