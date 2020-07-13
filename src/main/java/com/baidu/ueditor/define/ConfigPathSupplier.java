package com.baidu.ueditor.define;

/**
 * ConfigSupplier
 *
 * @author LFH
 * @since 2020年07月13日 10:27
 */
@SuppressWarnings({ "unused" })
@FunctionalInterface
public interface ConfigPathSupplier {

	/**
	 * @param rootPath 运行根路径
	 * @param parentPath ueditor根路径
	 * @return 配置文件路径
	 */
	String get(String rootPath,String parentPath);
}
