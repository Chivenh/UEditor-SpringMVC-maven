package com.baidu.ueditor.define;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * 处理状态接口
 * @author hancong03@baidu.com
 *
 */
public interface State {
	
	 boolean isSuccess();

	 void putInfo(String name, String val);

	 void putInfo(String name, long val);

	 String toJSONString();

	default <T> T getField(String name){
		try {
		Class stateClass = this.getClass();;
			Field field= stateClass.getDeclaredField(name);
			if(field!=null){
				//设置属性访问级别,获取private属性值.
				field.setAccessible(true);
				return (T) field.get(this);
			}
		}catch (NoSuchFieldException|IllegalAccessException e){

		}
		return (T)null;
	}

	default Object getInfo(String name){
		Map<String,Object> infoMap=this.getField("infoMap");
		return infoMap!=null? infoMap.get(name):null;
	}
}
