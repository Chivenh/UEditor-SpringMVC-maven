package com.baidu.ueditor.template;

import java.io.PrintWriter;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.baidu.ueditor.ExecCall;
import com.baidu.ueditor.define.ActionMap;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;

import com.baidu.ueditor.ActionEnter;
import com.baidu.ueditor.ConfigManager;

/**
 * BaseUeditorController
 * UEditor相关配置
 * @apiNote
 * 引用项目中，继承{@link BaseUeditorController},并实现getRootPath方法，即可使用基本功能版的UEditor
 * 如果有定制需求，覆写action方法，使用{@link BaseUeditorService#registerExecCall(String, ExecCall)}方法来注册非config外的action回调操作，
 * 然后在{@link #action(HttpServletRequest, String, String)}方法中，使用{@link BaseUeditorService#acquireInvokeStateCall(String)}来在调用对应回调。
 * @author LFH
 * @since  2018年10月08日 17:41
 */
public abstract class BaseUeditorController {
	@Value("${uploader.file.root:D:/}${uploader.file.upload:uploadFiles/}ue/")
	private String uploadBase;

	@Value("${ueditor.config.base:static/lib/ueditor}")
	private String configBase;

	/*ueditor配置*/
	private ConfigManager ueditorConfigManager;

	protected String getUploadBase() {
		return uploadBase;
	}

	protected String getConfigBase() {
		return configBase;
	}

	/**
	 * 获取项目根路径
	 * @return 获取项目根路径
	 */
	protected abstract String getRootPath() ;

	protected ConfigManager getUeditorConfigManager() {
		return ueditorConfigManager;
	}

	/**
	 * UEditor 的配置服务
	 * @param request -
	 * @param response -
	 * @param action -
	 * @throws Exception
	 */
	@RequestMapping("ueditor.do")
	public void handle(HttpServletRequest request, HttpServletResponse response,String action)throws  Exception{
		PrintWriter writer = response.getWriter();
		String exec;

		String rootPath = this.getRootPath();
		request.setCharacterEncoding( "utf-8" );
		/*全部改成text/html,以防止 IE把json当文件处理*/
		response.setContentType("text/html");
		if(ActionMap.CONFIG_ACTION.equals(action)){
			/*基本配置*/
			/*response.setContentType("text/html");*/
				exec = new ActionEnter(request, rootPath, this.getUeditorConfigManager()).exec();
		}else{
			/*response.setContentType("application/json");*/
			exec=this.action(request,rootPath,action);
		}
		writer.write(exec);
		writer.flush();
		writer.close();
	}

	protected String action(HttpServletRequest request,String rootPath,String action){
		 return new ActionEnter(request, rootPath, this.getUeditorConfigManager()).exec();
	}

	@PostConstruct
	public void initUeditorConfigManager(){
		ueditorConfigManager = ConfigManager.getInstance(this.getRootPath(), "", this.getConfigBase(), this.getUploadBase());
	}
}
