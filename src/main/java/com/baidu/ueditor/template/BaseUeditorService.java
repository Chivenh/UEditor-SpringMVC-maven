package com.baidu.ueditor.template;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.baidu.ueditor.ExecCall;

/**
 * BaseUeditorService
 * @apiNote
 * 引用项目中，继承{@link BaseUeditorService}并调用register相关方法，注册各action对应回调，供{@link #acquireInvokeStateCall(String)}调用
 * @author LFH
 * @since 2020年06月28日 10:16
 */
@SuppressWarnings({ "unused" })

public abstract class BaseUeditorService {

	public BaseUeditorService() {
		this.stateHandlers=new ConcurrentHashMap<>();
	}

	private final Map<String, ExecCall> stateHandlers;

	private static final ExecCall VOID_CALL=state->{};

	protected boolean registerExecCall(String stateKey, ExecCall stateCall){
		return this.stateHandlers.put(stateKey,stateCall)!=null;
	}

	protected boolean registerExecCall(ExecCall stateCall,String... stateKeys){

		if(stateKeys!=null&&stateKeys.length>0){
			for (String stateKey : stateKeys) {
				this.registerExecCall(stateKey,stateCall);
			}
		}

		return true;
	}

	protected boolean unregisterExecCall(String stateKey){
		return this.stateHandlers.remove(stateKey)!=null;
	}

	public ExecCall acquireInvokeStateCall(String stateKey){
		return this.stateHandlers.getOrDefault(stateKey,VOID_CALL);
	}

}
