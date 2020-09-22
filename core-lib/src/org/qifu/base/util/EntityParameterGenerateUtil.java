/* 
 * Copyright 2019-2021 qifu of copyright Chen Xin Nien
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * -----------------------------------------------------------------------
 * 
 * author: 	Chen Xin Nien
 * contact: chen.xin.nien@gmail.com
 * 
 */
package org.qifu.base.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.qifu.base.model.CreateDateField;
import org.qifu.base.model.CreateField;
import org.qifu.base.model.CreateUserField;
import org.qifu.base.model.EntityPK;
import org.qifu.base.model.EntityUK;
import org.qifu.base.model.UpdateDateField;
import org.qifu.base.model.UpdateField;
import org.qifu.base.model.UpdateUserField;

public class EntityParameterGenerateUtil {
	
	public static Map<String, Object> createParamMap(String paramName, String value) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put(paramName, value);
		return paramMap;
	}
	
	public static boolean foundUniqueKey(Object entityObject) {
		Method[] methods = entityObject.getClass().getMethods();
		if (methods==null) {
			return false;
		}
		boolean found = false;
		for (int ix = 0; ix < methods.length && !found; ix++) {
			Annotation[] annotations = methods[ix].getDeclaredAnnotations();
			if (annotations == null) {
				continue;
			}
			for(Annotation annotation : annotations) {
				if(annotation instanceof EntityUK) {
					found = true;
				}
			}
		}
		return found;
	}
	
	public static Map<String, Object> getUKParameter(Object entityObject) {
		Method[] methods=entityObject.getClass().getMethods();
		if (methods==null) {
			return null;
		}
		Map<String, Object> ukMap=new HashMap<String, Object>();
		for (int ix=0; ix<methods.length; ix++) {
			Annotation[] annotations=methods[ix].getDeclaredAnnotations();
			if (annotations==null) {
				continue;
			}
			for(Annotation annotation : annotations) {
				if(annotation instanceof EntityUK) {
					if (methods[ix].getName().indexOf("get")==0) {
						for (int nx=0; nx<annotations.length; nx++) {
							if (annotations[nx] instanceof EntityUK) {
								try {
									ukMap.put(((EntityUK)annotations[nx]).name(), methods[ix].invoke(entityObject));
									nx=annotations.length;
								} catch (IllegalArgumentException e) {
									e.printStackTrace();
								} catch (IllegalAccessException e) {
									e.printStackTrace();
								} catch (InvocationTargetException e) {
									e.printStackTrace();
								}								
							}
						}					
					}					
				}
			}
		}		
		return ukMap;
	}	
	
	public static Map<String, Object> getPKParameter(Object entityObject) {
		Method[] methods=entityObject.getClass().getMethods();
		if (methods==null) {
			return null;
		}
		Map<String, Object> ukMap=new HashMap<String, Object>();
		for (int ix=0; ix<methods.length; ix++) {
			Annotation[] annotations=methods[ix].getDeclaredAnnotations();
			if (annotations==null) {
				continue;
			}
			for(Annotation annotation : annotations) {
				if(annotation instanceof EntityPK) {
					if (methods[ix].getName().indexOf("get")==0) {
						for (int nx=0; nx<annotations.length; nx++) {
							if (annotations[nx] instanceof EntityPK) {
								try {
									ukMap.put(((EntityPK)annotations[nx]).name(), methods[ix].invoke(entityObject));
									nx=annotations.length;
								} catch (IllegalArgumentException e) {
									e.printStackTrace();
								} catch (IllegalAccessException e) {
									e.printStackTrace();
								} catch (InvocationTargetException e) {
									e.printStackTrace();
								}								
							}							
						}					
					}					
				}
			}
		}		
		return ukMap;
	}	
	
	public static EntityPK getPrimaryKeyField(Object entityObject) {
		Method[] methods=entityObject.getClass().getMethods();
		if (methods==null) {
			return null;
		}
		EntityPK field = null;
		for (int ix=0; ix<methods.length; ix++) {
			Annotation[] annotations=methods[ix].getDeclaredAnnotations();
			if (annotations==null) {
				continue;
			}
			for(Annotation annotation : annotations) {
				if(annotation instanceof EntityPK) {
					field = ((EntityPK)annotation);			
				}
			}
		}
		return field;
	}		
	
	public static UpdateField getUpdateField(Object entityObject) {
		Method[] methods=entityObject.getClass().getMethods();
		if (methods==null) {
			return null;
		}
		UpdateField field = new UpdateField();
		for (int ix=0; ix<methods.length; ix++) {
			Annotation[] annotations=methods[ix].getDeclaredAnnotations();
			if (annotations==null) {
				continue;
			}
			for(Annotation annotation : annotations) {
				if (annotation instanceof UpdateUserField) {
					field.setUpdateUserField( (UpdateUserField)annotation );					
				}
				if (annotation instanceof UpdateDateField) {
					field.setUpdateDateField( (UpdateDateField)annotation );
				}
			}
		}
		return field;
	}	
	
	public static CreateField getCreateField(Object entityObject) {
		Method[] methods=entityObject.getClass().getMethods();
		if (methods==null) {
			return null;
		}
		CreateField field = new CreateField();
		for (int ix=0; ix<methods.length; ix++) {
			Annotation[] annotations=methods[ix].getDeclaredAnnotations();
			if (annotations==null) {
				continue;
			}
			for(Annotation annotation : annotations) {
				if (annotation instanceof CreateUserField) {
					field.setCreateUserField( (CreateUserField)annotation );		
				}
				if (annotation instanceof CreateDateField) {
					field.setCreateDateField( (CreateDateField)annotation );
				}
			}
		}
		return field;		
	}
	
}
