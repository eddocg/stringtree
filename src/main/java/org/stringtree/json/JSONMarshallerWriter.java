package org.stringtree.json;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.stringtree.json.JSONWriter;

public class JSONMarshallerWriter extends JSONWriter {

	@SuppressWarnings("finally") 
	public String write(Object object) {
		int countLevel = 5;
		try {
			return super.write(marshall(object, countLevel));
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			return "{}";
		}
	}

	
	private Object marshall(Object object, int countLevel) throws IllegalArgumentException, IllegalAccessException, InterruptedException {

		HashMap<String, Object> jsonObj = new HashMap<String, Object>();
		if(countLevel==0) throw new InterruptedException("Interrupted by count, make sure the tree level is less than: "+countLevel);
		countLevel--;
		Class itemData = object.getClass();
		if(object instanceof List) {
			List listOfObjects = new ArrayList();
			for(Object obj : (List)object) {
				listOfObjects.add(marshall(obj,5));
			}
			return listOfObjects;
		}
		Field[] itemDataFields = itemData.getDeclaredFields();
		
		for(Field field : itemDataFields) {

			field.setAccessible(true);
			if(field.get(object) instanceof java.lang.Byte) {
				jsonObj.put(field.getName(), field.getByte(object));
			} if(field.get(object) instanceof java.lang.Short) {
				jsonObj.put(field.getName(), field.getShort(object));
			} if(field.get(object) instanceof java.lang.Integer) {
				jsonObj.put(field.getName(), field.getInt(object));
			} else if(field.get(object) instanceof java.lang.Long) {
				jsonObj.put(field.getName(), field.getLong(object));
			} else if(field.get(object) instanceof java.lang.Float) {
				jsonObj.put(field.getName(), field.getFloat(object));
			} else if(field.get(object) instanceof java.lang.Double) {
				jsonObj.put(field.getName(), field.getDouble(object));
			} else if(field.get(object) instanceof java.lang.Character) {
				jsonObj.put(field.getName(), field.getChar(object));
			} else if(field.get(object) instanceof java.lang.Boolean) {
				jsonObj.put(field.getName(), field.getBoolean(object));
			} else if(field.getType().toString().equals("class java.lang.String")) {
				jsonObj.put(field.getName(), field.get(object));
			} else if(field.getType().toString().equals("interface java.util.List") || field.get(object) instanceof List) {
				List oList = (List)field.get(object);
				List newList = new ArrayList();
				for(Object oData : oList) {
					//jsonObj.put(field.getName(), ((JsonMarshalling)oData).toJson());
					newList.add(marshall(oData, countLevel));
				}
				jsonObj.put(field.getName(), newList);
			} else {
			}
		}

		return jsonObj;
	}


}
