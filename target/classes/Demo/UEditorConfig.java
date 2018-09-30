
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.text.html.Option;

import com.baidu.ueditor.define.State;
import com.cargo.tpy.utils.LoadImageUtil;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.baidu.ueditor.ActionEnter;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 富文本编辑器相关控制器
 *
 * @author LFH
 * @date 2018年09月18日 21:28
 */
@Controller
@RequestMapping("/data/")
public class EmailDataController {
	@Value("${uploader.file.root:D:/tpy-files/}${uploader.file.upload:uploadFiles/}")
	private String uploadBase;

	/**
	 * UEditor 的配置服务
	 * @param request
	 * @param response
	 * @param action
	 * @throws Exception
	 */
	@RequestMapping("ueditor.do")
	public void config(HttpServletRequest request,HttpServletResponse response,String action)throws  Exception{
		PrintWriter writer = response.getWriter();
		String exec=null;
		String configBase="static/lib/ueditor";
		String rootPath = request.getSession().getServletContext().getRealPath("/");
		request.setCharacterEncoding( "utf-8" );
		switch (action){
		/*基本配置*/
		case "config":
			response.setContentType("text/html");
			exec = new ActionEnter(request, rootPath,configBase).exec();
			break;
		/*各种命令的执行*/
		default:
			response.setContentType("application/json");
			/*exec = new ActionEnter(request, rootPath,configBase,uploadBase).exec(state->{
				//这里主要是获取了上传图片的回传参数,后期可能有用.
//				Map infoMap=state.getField("infoMap");
//				if(infoMap.isEmpty()){
//					List<State> infos=state.getField("stateList");
//				}
			});*/
			exec = new ActionEnter(request, rootPath,configBase,uploadBase).exec();
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
	public void showImg(@RequestParam String url,HttpServletResponse response){
		LoadImageUtil.loadImageByPath(response,uploadBase+ url);
	}

}
