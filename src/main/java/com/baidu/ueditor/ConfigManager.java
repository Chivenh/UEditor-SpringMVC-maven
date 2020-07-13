package com.baidu.ueditor;

import com.baidu.ueditor.define.ActionMap;
import com.baidu.ueditor.define.ConfigPathSupplier;
import com.baidu.ueditor.jacksonextend.JsonMapper;
import com.fasterxml.jackson.databind.JsonNode;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * 配置管理器
 * 
 * @author hancong03@baidu.com
 *
 */
public final class ConfigManager {

	private static final JsonMapper JSON_MAPPER;

	static {
		JSON_MAPPER = new JsonMapper();
	}

	/**
	 * 配置文件名称
	 */
	private static final String CONFIG_FILE_NAME = "config.json";
	/**
	 * 默认上传文件目录
	 */
	private static final String UPLOAD_PATH = "upload/";
	/**
	 * 默认配置数据获取方法
	 */
	private static final ConfigPathSupplier DEFAULT_CONFIG_SUPPLIER;
	/**
	 * 各对应操作参数匹配集
	 */
	private static final Map<Integer, Function<ConfigManager, Map<String, Object>>> CONFIG_GETTERS;

	/**
	 * 运行根路径
	 */
	private final String rootPath;
	/**
	 * ueditor资源根路径
	 */
	private final String parentPath;
	/**
	 * 上传文件目录
	 */
	private final String uploadBase;
	/**
	 * 配置文件路径
	 */
	private String configPath;

	/**
	 * 配置原文
	 */
	private JsonNode jsonConfig = null;
	/**
	 * 涂鸦上传filename定义
	 */
	private final static String SCRAWL_FILE_NAME = "scrawl";
	/**
	 * 远程图片抓取filename定义
	 */
	private final static String REMOTE_FILE_NAME = "remote";

	private ConfigManager(String rootPath, final String contextPath, final String ueditorBase, final String uploadBase,
			ConfigPathSupplier configPathSupplier) throws IOException {

		rootPath = rootPath.replace("\\", "/");

		this.rootPath = rootPath;
		if (StringUtils.isNotBlank(contextPath)) {
			this.parentPath = this.rootPath + ueditorBase.replaceFirst("^" + contextPath.trim(), "");
		} else {
			this.parentPath = this.rootPath + ueditorBase;
		}

		this.uploadBase = StringUtils.isBlank(uploadBase) ? (this.parentPath + File.separator + UPLOAD_PATH)
				: uploadBase.trim();

		this.configPath = configPathSupplier.get(this.rootPath, this.parentPath);

		this.initEnv();
	}

	/**
	 * 通过一个给定的路径构建一个配置管理器， 该管理器要求地址路径所在目录下必须存在config.properties文件
	 */
	private ConfigManager(String rootPath, final String contextPath, final String ueditorBase,
			ConfigPathSupplier configPathSupplier) throws IOException {

		this(rootPath, contextPath, ueditorBase, null, configPathSupplier);

	}

	/**
	 * 配置管理器构造工厂
	 * 
	 * @param rootPath 服务器根路径
	 * @param contextPath 服务器所在项目路径
	 * @param ueditorBase ueditor资源根目录
	 * @return 配置管理器实例或者null
	 */
	public static ConfigManager getInstance(String rootPath, String contextPath, String ueditorBase) {

		try {
			return new ConfigManager(rootPath, contextPath, ueditorBase, DEFAULT_CONFIG_SUPPLIER);
		} catch (Exception e) {
			return null;
		}

	}

	/**
	 * 配置管理器构造工厂
	 * 
	 * @param rootPath 服务器根路径
	 * @param contextPath 服务器所在项目路径
	 * @param ueditorBase ueditor资源根目录
	 * @param uploadBase  上传文件根目录
	 * @return 配置管理器实例或者null
	 */
	public static ConfigManager getInstance(String rootPath, String contextPath, String ueditorBase,
			String uploadBase) {

		try {
			return new ConfigManager(rootPath, contextPath, ueditorBase, uploadBase, DEFAULT_CONFIG_SUPPLIER);
		} catch (Exception e) {
			return null;
		}

	}

	/**
	 * 配置管理器构造工厂
	 * 
	 * @param rootPath 服务器根路径
	 * @param contextPath 服务器所在项目路径
	 * @param ueditorBase ueditor资源根目录
	 * @param uploadBase     上传文件根目录
	 * @param configSupplier 配置文件路径提供
	 * @return 配置管理器实例或者null
	 */
	public static ConfigManager getInstance(String rootPath, String contextPath, String ueditorBase, String uploadBase,
			ConfigPathSupplier configSupplier) {

		try {
			return new ConfigManager(rootPath, contextPath, ueditorBase, uploadBase, configSupplier);
		} catch (Exception e) {
			return null;
		}

	}

	/**
	 * 配置管理器构造工厂
	 * 
	 * @param rootPath 服务器根路径
	 * @param contextPath 服务器所在项目路径
	 * @param ueditorBase ueditor资源根目录
	 * @param configSupplier 配置文件路径提供
	 * @return 配置管理器实例或者null
	 */
	public static ConfigManager getInstance(String rootPath, String contextPath, String ueditorBase,
			ConfigPathSupplier configSupplier) {

		try {
			return new ConfigManager(rootPath, contextPath, ueditorBase, configSupplier);
		} catch (Exception e) {
			return null;
		}

	}

	/**
	 * 验证配置文件加载是否正确
	 * @return -
	 */
	public boolean valid() {
		return this.jsonConfig != null;
	}

	public JsonNode getAllConfig() {

		return this.jsonConfig;

	}

	public Map<String, Object> getConfig(int type) {
		return CONFIG_GETTERS.get(type).apply(this);
	}

	private void initEnv() throws IOException {

		String configContent = this.readFile(this.configPath);

		try {
			JsonNode jsonConfig = JSON_MAPPER.readTree(configContent);
			this.jsonConfig = jsonConfig;
		} catch (Exception e) {
			this.jsonConfig = null;
		}

	}

	private String[] getArray(String key) {

		JsonNode jsonArray = this.jsonConfig.path(key);
		if (jsonArray.isArray()) {
			String[] result = new String[jsonArray.size()];

			for (int i = 0, len = jsonArray.size(); i < len; i++) {
				result[i] = jsonArray.path(i).textValue();
			}

			return result;
		}

		return null;

	}

	private String readFile(String path) throws IOException {

		StringBuilder builder = new StringBuilder();

		try {

			InputStreamReader reader = new InputStreamReader(new FileInputStream(path), StandardCharsets.UTF_8);
			BufferedReader bfReader = new BufferedReader(reader);

			String tmpContent = null;

			while ((tmpContent = bfReader.readLine()) != null) {
				builder.append(tmpContent);
			}

			bfReader.close();

		} catch (UnsupportedEncodingException e) {
			// 忽略
		}

		return this.filter(builder.toString());

	}

	// 过滤输入字符串, 剔除多行注释以及替换掉反斜杠
	private String filter(String input) {

		return input.replaceAll("/\\*[\\s\\S]*?\\*/", "");

	}

	static {
		DEFAULT_CONFIG_SUPPLIER = (rootPath, parentPath) -> parentPath + File.separator
				+ ConfigManager.CONFIG_FILE_NAME;
		CONFIG_GETTERS = new HashMap<>(7);

		CONFIG_GETTERS.put(ActionMap.UPLOAD_FILE, (m) -> {
			Map<String, Object> conf = new HashMap<>(10);
			conf.put("isBase64", "false");
			conf.put("maxSize", m.jsonConfig.path("fileMaxSize").longValue());
			conf.put("allowFiles", m.getArray("fileAllowFiles"));
			conf.put("fieldName", m.jsonConfig.path("fileFieldName").textValue());

			conf.put("savePath", m.jsonConfig.path("filePathFormat").textValue());
			conf.put("uploadBase", m.uploadBase);
			conf.put("rootPath", m.rootPath);

			return conf;
		});

		CONFIG_GETTERS.put(ActionMap.UPLOAD_IMAGE, (m) -> {
			Map<String, Object> conf = new HashMap<>(10);
			conf.put("isBase64", "false");
			conf.put("maxSize", m.jsonConfig.path("imageMaxSize").longValue());
			conf.put("allowFiles", m.getArray("imageAllowFiles"));
			conf.put("fieldName", m.jsonConfig.path("imageFieldName").textValue());

			conf.put("savePath", m.jsonConfig.path("imagePathFormat").textValue());
			conf.put("uploadBase", m.uploadBase);
			conf.put("rootPath", m.rootPath);

			return conf;
		});

		CONFIG_GETTERS.put(ActionMap.UPLOAD_VIDEO, (m) -> {
			Map<String, Object> conf = new HashMap<>(10);
			conf.put("maxSize", m.jsonConfig.path("videoMaxSize").longValue());
			conf.put("allowFiles", m.getArray("videoAllowFiles"));
			conf.put("fieldName", m.jsonConfig.path("videoFieldName").textValue());

			conf.put("savePath", m.jsonConfig.path("videoPathFormat").textValue());
			conf.put("uploadBase", m.uploadBase);
			conf.put("rootPath", m.rootPath);

			return conf;
		});

		CONFIG_GETTERS.put(ActionMap.UPLOAD_SCRAWL, (m) -> {
			Map<String, Object> conf = new HashMap<>(10);
			conf.put("filename", ConfigManager.SCRAWL_FILE_NAME);
			conf.put("maxSize", m.jsonConfig.path("scrawlMaxSize").longValue());
			conf.put("fieldName", m.jsonConfig.path("scrawlFieldName").textValue());
			conf.put("isBase64", "true");

			conf.put("savePath", m.jsonConfig.path("scrawlPathFormat").textValue());
			conf.put("uploadBase", m.uploadBase);
			conf.put("rootPath", m.rootPath);

			return conf;
		});

		CONFIG_GETTERS.put(ActionMap.CATCH_IMAGE, (m) -> {
			Map<String, Object> conf = new HashMap<>(10);
			conf.put("filename", ConfigManager.REMOTE_FILE_NAME);
			conf.put("filter", m.getArray("catcherLocalDomain"));
			conf.put("maxSize", m.jsonConfig.path("catcherMaxSize").longValue());
			conf.put("allowFiles", m.getArray("catcherAllowFiles"));
			conf.put("fieldName", m.jsonConfig.path("catcherFieldName").textValue() + "[]");

			conf.put("savePath", m.jsonConfig.path("catcherPathFormat").textValue());
			conf.put("uploadBase", m.uploadBase);
			conf.put("rootPath", m.rootPath);

			return conf;
		});

		CONFIG_GETTERS.put(ActionMap.LIST_IMAGE, (m) -> {
			Map<String, Object> conf = new HashMap<>(5);
			conf.put("allowFiles", m.getArray("imageManagerAllowFiles"));
			conf.put("dir", m.jsonConfig.path("imageManagerListPath").textValue());
			conf.put("count", m.jsonConfig.path("imageManagerListSize").intValue());

			conf.put("uploadBase", m.uploadBase);
			conf.put("rootPath", m.rootPath);

			return conf;
		});

		CONFIG_GETTERS.put(ActionMap.LIST_FILE, (m) -> {
			Map<String, Object> conf = new HashMap<>(5);
			conf.put("allowFiles", m.getArray("fileManagerAllowFiles"));
			conf.put("dir", m.jsonConfig.path("fileManagerListPath").textValue());
			conf.put("count", m.jsonConfig.path("fileManagerListSize").intValue());

			conf.put("uploadBase", m.uploadBase);
			conf.put("rootPath", m.rootPath);

			return conf;
		});

	}

}
