package com.omnie.claimrouter.stream.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class ReflectionUtils {

	public static List<Field> getFields(Class<? extends Object> cls) {
		List<Field> fields=new ArrayList<Field>();
		for(Field field :  cls.getDeclaredFields()){
			fields.add(field);
		}
		if(!cls.getSuperclass().equals(Object.class)) {
			fields.addAll(getFields(cls.getSuperclass()));
		}
		return fields;
	}
	
	public static  void setField(Object instance, String fieldVal, String fieldName) {
		try {
			Field fieldInfo = null;
			try {
				fieldInfo = instance.getClass().getDeclaredField(fieldName);
			}catch (NoSuchFieldException e) {
			}
			if(fieldInfo==null) {
				fieldInfo=instance.getClass().getSuperclass().getDeclaredField(fieldName);
			}
			fieldInfo.setAccessible(true);
			fieldInfo.set(instance, fieldVal);
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			System.out.println(instance);
			e.printStackTrace();
		}
	}
	
	public static  Object getField(Object instance, String fieldName) {
		try {
			Field fieldInfo = null;
			try {
				fieldInfo = instance.getClass().getDeclaredField(fieldName);
			}catch (NoSuchFieldException e) {
			}
			if(fieldInfo==null) {
				fieldInfo=instance.getClass().getSuperclass().getDeclaredField(fieldName);
			}
			return  getField(instance, fieldInfo);
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException e) {
			System.out.println("instance="+instance);
			e.printStackTrace();
		}
		return null;
	}

	public static Object getField(Object instance, Field fieldInfo) {
		try {
			fieldInfo.setAccessible(true);
			return fieldInfo.get(instance);
		} catch (SecurityException | IllegalArgumentException | IllegalAccessException e) {
			System.out.println(instance);
			e.printStackTrace();
		}
		return null;
	}

}
