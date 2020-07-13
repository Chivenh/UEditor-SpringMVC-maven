package com.baidu.ueditor.define;

import java.util.HashMap;
import java.util.Map;

/**
 * 定义请求action类型
 * @author hancong03@baidu.com
 *
 */
@SuppressWarnings("serial")
public final class ActionMap {

	public static final Map<String, Integer> MAPPING;
	/**
	 * 获取配置请求
	 */
	public static final int CONFIG = 0;
	public static final int UPLOAD_IMAGE = 1;
	public static final int UPLOAD_SCRAWL = 2;
	public static final int UPLOAD_VIDEO = 3;
	public static final int UPLOAD_FILE = 4;
	public static final int CATCH_IMAGE = 5;
	public static final int LIST_FILE = 6;
	public static final int LIST_IMAGE = 7;
	public static final String CONFIG_ACTION = "config";
	public static final String UPLOAD_IMAGE_ACTION = "uploadimage";
	public static final String UPLOAD_SCRAWL_ACTION = "uploadscrawl";
	public static final String UPLOAD_VIDEO_ACTION = "uploadvideo";
	public static final String UPLOAD_FILE_ACTION = "uploadfile";
	public static final String CATCH_IMAGE_ACTION = "catchimage";
	public static final String LIST_FILE_ACTION = "listfile";
	public static final String LIST_IMAGE_ACTION = "listimage";

	static {
		MAPPING = new HashMap<>(8);
		MAPPING.put( CONFIG_ACTION, ActionMap.CONFIG );
		MAPPING.put( UPLOAD_IMAGE_ACTION, ActionMap.UPLOAD_IMAGE );
		MAPPING.put( UPLOAD_SCRAWL_ACTION, ActionMap.UPLOAD_SCRAWL );
		MAPPING.put( UPLOAD_VIDEO_ACTION, ActionMap.UPLOAD_VIDEO );
		MAPPING.put( UPLOAD_FILE_ACTION, ActionMap.UPLOAD_FILE );
		MAPPING.put( CATCH_IMAGE_ACTION, ActionMap.CATCH_IMAGE );
		MAPPING.put( LIST_FILE_ACTION, ActionMap.LIST_FILE );
		MAPPING.put( LIST_IMAGE_ACTION, ActionMap.LIST_IMAGE );
	}
	
	public static int getType ( String key ) {
		return ActionMap.MAPPING.get( key );
	}
	
}
