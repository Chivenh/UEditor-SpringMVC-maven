package com.baidu.ueditor;

import com.baidu.ueditor.define.State;

/**
 * 为ActionEnter 的 exec 添加后台回调.
 * @see	 ActionEnter#exec(ExecCall)
 * @author LFH
 * @date 2018年09月20日 14:07
 */
@FunctionalInterface
public interface ExecCall {
	void work(State state);
}
