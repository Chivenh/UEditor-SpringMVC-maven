package com.cargo.hjs.controller;

import com.baidu.ueditor.ActionEnter;
import com.baidu.ueditor.ConfigManager;
import com.cargo.file.entity.AttachmentList;
import com.cargo.hjs.properties.ApplicationEventPublisher;
import com.cargo.hjs.service.UeditorService;
import com.cargo.util.web.WebContext;
import com.carogo.utils.LoadImageUtil;
import com.carogo.utils.Tutil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Map;

/**
 * UEditor相关配置
 *
 * @author LFH
 * @date 2018年10月08日 17:41
 */
@Controller
@RequestMapping("/ueditorConfig/")
public class UeditorConfigController {
	@Value("${uploader.file.root:D:/hjs-files/}${uploader.file.upload:uploadFiles/}")
	private String uploadBase;
	//ueditor配置,在当前bean初始化时初始化一个configManager,后面都使用这个configManager,不再每次请求时都去读配置文件
	private static ConfigManager ueditorConfigManager;

	@Resource
	private UeditorService ueditorService;

	@Resource
	private ApplicationEventPublisher remoteUploaderPublisher;

	/**
	 * UEditor 的配置服务
	 * @param request
	 * @param response
	 * @param action
	 * @throws Exception
	 */
	@RequestMapping("ueditor.do")
	public void config(HttpServletRequest request, HttpServletResponse response,String action)throws  Exception{
		PrintWriter writer = response.getWriter();
		String exec=null;
//		String rootPath = request.getSession().getServletContext().getRealPath("/");
		String rootPath = WebContext.getRealPath("/");
		request.setCharacterEncoding( "utf-8" );
		//全部改成text/html,以防止 IE把json当文件处理
		response.setContentType("text/html");
		switch (action){
		/*基本配置*/
		case "config":
//			response.setContentType("text/html");
			exec = new ActionEnter(request, rootPath,ueditorConfigManager).exec();
			break;
		/*各种命令的执行*/
		default:
//			response.setContentType("application/json");
			exec = new ActionEnter(request, rootPath,ueditorConfigManager).exec(state->{
				if(!state.isSuccess()){
					return;
				}
				//这里主要是获取了上传图片的回传参数,后期可能有用.
				Map infoMap=state.getField("infoMap");
				String fileFath=uploadBase+Tutil.getStr( infoMap.get("url")).substring(1);
				AttachmentList file=new AttachmentList();
				file.setFileType(Tutil.getStr( infoMap.get("type")));
				//上传后服务器上存储的真实名称
				file.setRealName(Tutil.getStr( infoMap.get("title")));
				file.setFileName(Tutil.getStr( infoMap.get("original")));
				file.setLocalFilePath(fileFath.replace(file.getRealName(),""));
				file= ueditorService.uploadInfo(file);
				state.putInfo("url",file.getId());//页面回显前缀后跟的路径
				remoteUploaderPublisher.triggerRemoteUploader(file);
//				if(infoMap.isEmpty()){
//					List<State> infos=state.getField("stateList");
//				}
			});
			/*exec = new ActionEnter(request, rootPath,configBase,uploadBase).exec();*/
			break;
		}
		writer.write(exec);
		writer.flush();
		writer.close();
	}

	/**
	 * 显示模板中的图片
	 * @param url
	 * @param response
	 */
	@RequestMapping("showImg.do")
	public void showImg(@RequestParam String url, HttpServletResponse response){
		LoadImageUtil.loadImageByPath(response,uploadBase+ url);
	}
	@PostConstruct
	public void initUeditorConfigManager(){
		String configBase="static/lib/ueditor";
		String rootPath= WebContext.getRealPath("/");
		ueditorConfigManager= ConfigManager.getInstance(rootPath, "", configBase, uploadBase);
	}
}
