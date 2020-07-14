package com.baidu.ueditor.template;

import java.io.PrintWriter;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;

import com.baidu.ueditor.ActionEnter;
import com.baidu.ueditor.ConfigManager;
import com.baidu.ueditor.ExecCall;
import com.baidu.ueditor.define.ActionMap;

/**
 * BaseUeditorController
 * UEditor相关配置
 * @apiNote
 * 引用项目中，继承{@link BaseUeditorController},并实现getRootPath方法，即可使用基本功能版的UEditor
 * 如果有定制需求，覆写action方法，使用{@link BaseUeditorService#registerExecCall(String, ExecCall)}方法来注册非config外的action回调操作，
 * 然后在{@link #action(HttpServletRequest, String)}方法中，使用{@link BaseUeditorService#acquireInvokeStateCall(String)}来在调用对应回调。
 * @author LFH
 * @since  2018年10月08日 17:41
 */
public abstract class BaseUeditorController {
	@Value("${uploader.file.root:D:/}${uploader.file.upload:uploadFiles/}ue/")
	private String uploadBase;

	@Value("${ueditor.config.base:static/lib/ueditor}")
	private String ueditorBase;

	/**
	 * ueditor配置对象
	 */
	private ConfigManager ueditorConfigManager;

	/**
	 * 获取项目根路径
	 * @return 获取项目根路径
	 */
	protected abstract String getRootPath() ;

	protected String getUploadBase() {
		return uploadBase;
	}

	protected String getUeditorBase() {
		return ueditorBase;
	}

	/**
	 * @deprecated {@link #getUeditorBase()}
	 * @return -
	 */
	@Deprecated
	protected String getConfigBase(){
		return this.getUeditorBase();
	}

	protected ConfigManager getUeditorConfigManager() {
		return ueditorConfigManager;
	}

	protected void setUeditorConfigManager(ConfigManager ueditorConfigManager) {
		this.ueditorConfigManager = ueditorConfigManager;
	}

	/**
	 * 配置ueditorService
	 * @return -
	 */
	protected BaseUeditorService getUeditorService(){
		return null;
	}

	/**
	 * UEditor 的配置服务
	 * @param request -
	 * @param response -
	 * @param action -
	 * @throws Exception -
	 */
	@RequestMapping("ueditor.do")
	public void handle(HttpServletRequest request, HttpServletResponse response,String action)throws  Exception{
		PrintWriter writer = response.getWriter();
		String exec;

		request.setCharacterEncoding( "utf-8" );
		/*全部改成text/html,以防止 IE把json当文件处理*/
		response.setContentType("text/html");
		if(ActionMap.CONFIG_ACTION.equals(action)){
			/*基本配置*/
			/*response.setContentType("text/html");*/
			exec = new ActionEnter(request, this.getUeditorConfigManager()).exec();
		}else{
			/*response.setContentType("application/json");*/
			exec=this.action(request,action);
		}
		writer.write(exec);
		writer.flush();
		writer.close();
	}

	/**
	 * 非配置操作外的其它操作
	 * @param request -
	 * @param action -
	 * @return -
	 */
	protected String action(HttpServletRequest request,String action){
		ExecCall execCall=null;
		if(this.getUeditorService()!=null){
			/*如果定义了ueditorService,则自动添加回调*/
			execCall=this.getUeditorService().acquireInvokeStateCall(action);
		}
		return new ActionEnter(request, this.getUeditorConfigManager()).exec(execCall);
	}

	@PostConstruct
	public void initUeditorConfigManager(){
		this.setUeditorConfigManager( ConfigManager.getInstance(this.getRootPath(), "", this.getUeditorBase(), this.getUploadBase()));
	}
}
