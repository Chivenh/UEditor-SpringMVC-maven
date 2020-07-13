package com.baidu.ueditor.define;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 处理状态接口
 * @author hancong03@baidu.com
 *
 */
public abstract class State {

	public static final String DEFAULT_INFO="infoMap";

	protected static final Map<String,Map<String,Field>> FIELD_CASHE=new HashMap<>(2);

	protected static final ReentrantLock CACHE_LOCK=new ReentrantLock(true);

	protected static void writeCache(Class<? extends State> stateClass){

		String stateClassName = stateClass.getName();

		if(FIELD_CASHE.containsKey(stateClassName)){
			return;
		}

		CACHE_LOCK.lock();

		try {

			Field[] fields = stateClass.getDeclaredFields();

			Map<String,Field> mapFields = new HashMap<>(fields.length);

			FIELD_CASHE.put(stateClassName,mapFields);

			if(fields.length>0){

				for (Field field : fields) {
					if(field!=null){

						if(Modifier.isStatic(field.getModifiers())){
							continue;
						}

						/*设置属性访问级别,获取private属性值.*/
						field.setAccessible(true);
						mapFields.put(field.getName(),field);
					}
				}

			}

		}finally {
			CACHE_LOCK.unlock();
		}

	}

	protected static Map<String,Field> getFieldCache(Class<? extends State> stateClass){
		return FIELD_CASHE.get(stateClass.getName());
	}

	protected static Field getFieldCache(Class<? extends State> stateClass,String name){
		return getFieldCache(stateClass).getOrDefault(name,getFieldCache(State.class).get(name));
	}

	protected boolean state = false;
	protected String info = null;
	protected Map<String, String> infoMap = new HashMap<>();

	public State() {
		writeCache(this.getClass());
	}

	/**
	 * 是否执行成功
	 * @return -
	 */
	 public abstract boolean isSuccess();

	/**
	 * 设置默认info
	 * @param name  名称
	 * @param val 值
	 */
	public abstract void putInfo(String name, String val);

	/**
	 * 设置默认info
	 * @param name  名称
	 * @param val 值
	 */
	public abstract void putInfo(String name, long val);

	/**
	 * json化
	 * @return -
	 */
	public abstract String toJsonString();

	 @SuppressWarnings("unchecked")
	public  <T> T getField(String name){
		try {
			Class stateClass = this.getClass();
			Field field=getFieldCache(stateClass,name);
			if(field!=null){
				return (T) field.get(this);
			}
		}catch (IllegalAccessException e){
			/**/
		}
		return (T)null;
	}

	public Object getInfo(String name){
		Map<String,Object> infoMap=this.getField(DEFAULT_INFO);
		return infoMap!=null? infoMap.get(name):null;
	}

	static {
	 	/*父级字段*/
	 	writeCache(State.class);
	}
}
