package com.baidu.ueditor.template;

import java.io.PrintWriter;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;

import com.baidu.ueditor.ActionEnter;
import com.baidu.ueditor.ConfigManager;
import org.springframework.web.context.WebApplicationContext;

/**
 * BaseUeditorController
 * UEditor相关配置
 *
 * @author LFH
 * @since  2018年10月08日 17:41
 */
public abstract class BaseUeditorController {
	@Value("${uploader.file.root:D:/}${uploader.file.upload:uploadFiles/}ue/")
	private String uploadBase;

	@Value("${ueditor.config.base:static/lib/ueditor}")
	private String configBase;

	@Autowired
	private WebApplicationContext applicationContext;

	private String rootPath;

	/*ueditor配置*/
	private ConfigManager ueditorConfigManager;

	public String getUploadBase() {
		return uploadBase;
	}

	public String getConfigBase() {
		return configBase;
	}

	public String getRootPath() {
		return rootPath;
	}

	public ConfigManager getUeditorConfigManager() {
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

		request.setCharacterEncoding( "utf-8" );
		/*全部改成text/html,以防止 IE把json当文件处理*/
		response.setContentType("text/html");
		if("config".equals(action)){
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
		ServletContext context = applicationContext.getServletContext();
		Assert.notNull(context,"ServletContext 为空，不支持使用ueditor");
		this.rootPath= context.getRealPath("/");
		ueditorConfigManager = ConfigManager.getInstance(rootPath, "", this.getConfigBase(), this.getUploadBase());
	}
}
