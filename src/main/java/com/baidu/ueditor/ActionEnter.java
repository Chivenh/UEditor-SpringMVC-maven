package com.baidu.ueditor;

import com.baidu.ueditor.define.ActionMap;
import com.baidu.ueditor.define.AppInfo;
import com.baidu.ueditor.define.BaseState;
import com.baidu.ueditor.define.State;
import com.baidu.ueditor.hunter.FileManager;
import com.baidu.ueditor.hunter.ImageHunter;
import com.baidu.ueditor.upload.Uploader;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public class ActionEnter {
	
	private HttpServletRequest request = null;
	
	private String rootPath = null;
	private String contextPath = null;

	private String actionType = null;
	
	private ConfigManager configManager = null;

	public ActionEnter ( HttpServletRequest request, String rootPath ,String configBase) {
		this(request,rootPath,configBase,null);
	}

	public ActionEnter ( HttpServletRequest request, String rootPath,String configBase ,String uploadBase) {

		this.request = request;
		this.rootPath = rootPath;
		this.actionType = request.getParameter( "action" );
		this.contextPath = request.getContextPath();
		this.configManager = ConfigManager.getInstance( this.rootPath, this.contextPath, configBase,uploadBase );

	}

	/**
	 * 通过传入一个配置对象来初始化操作对象,而不是每次都去请求配置文件
	 * @param request
	 * @param rootPath
	 * @param configManager
	 */
	public ActionEnter (HttpServletRequest request, String rootPath,ConfigManager configManager){
		this.request = request;
		this.rootPath = rootPath;
		this.actionType = request.getParameter( "action" );
		this.contextPath = request.getContextPath();
		this.configManager=configManager;
	}
	
	public String exec () {
		
		String callbackName = this.request.getParameter("callback");
		
		if ( callbackName != null ) {

			if ( !validCallbackName( callbackName ) ) {
				return new BaseState( false, AppInfo.ILLEGAL ).toJsonString();
			}
			
			return callbackName+"("+this.invoke(null)+");";
			
		} else {
			return this.invoke(null);
		}

	}
	public String exec (ExecCall execCall) {

		String callbackName = this.request.getParameter("callback");

		if ( callbackName != null ) {

			if ( !validCallbackName( callbackName ) ) {
				return new BaseState( false, AppInfo.ILLEGAL ).toJsonString();
			}

			return callbackName+"("+this.invoke(execCall)+");";

		} else {
			return this.invoke(execCall);
		}

	}
	public String invoke(ExecCall execCall) {

		if ( actionType == null || !ActionMap.MAPPING.containsKey( actionType ) ) {
			return new BaseState( false, AppInfo.INVALID_ACTION ).toJsonString();
		}

		if ( this.configManager == null || !this.configManager.valid() ) {
			return new BaseState( false, AppInfo.CONFIG_ERROR ).toJsonString();
		}

		State state;

		int actionCode = ActionMap.getType( this.actionType );

		Map<String, Object> conf;

		switch ( actionCode ) {

			case ActionMap.CONFIG:
				return this.configManager.getAllConfig().toString();

			case ActionMap.UPLOAD_IMAGE:
			case ActionMap.UPLOAD_SCRAWL:
			case ActionMap.UPLOAD_VIDEO:
			case ActionMap.UPLOAD_FILE:
				conf = this.configManager.getConfig( actionCode );
				state = new Uploader( request, conf ).doExec();
				break;

			case ActionMap.CATCH_IMAGE:
				conf = configManager.getConfig( actionCode );
				String[] list = this.request.getParameterValues( (String)conf.get( "fieldName" ) );
				state = new ImageHunter( conf ).capture( list );
				break;

			case ActionMap.LIST_IMAGE:
			case ActionMap.LIST_FILE:
				conf = configManager.getConfig( actionCode );
				int start = this.getStartIndex();
				state = new FileManager( conf ).listFile( start );
				break;

			default:
				return "";
		}
		if(execCall!=null){
			execCall.work(state);
		}
		return state.toJsonString();

	}

	public int getStartIndex () {
		
		String start = this.request.getParameter( "start" );
		
		try {
			return Integer.parseInt( start );
		} catch ( Exception e ) {
			return 0;
		}
		
	}
	
	/**
	 * callback参数验证
	 */
	public boolean validCallbackName ( String name ) {

		return name.matches( "^[a-zA-Z_]+[\\w0-9_]*$" );

	}
	
}