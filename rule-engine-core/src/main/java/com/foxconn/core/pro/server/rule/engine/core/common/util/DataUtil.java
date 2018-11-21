package com.foxconn.core.pro.server.rule.engine.core.common.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

import com.foxconn.core.pro.server.rule.engine.core.constant.CommonConstant;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * ClassName: DataUtil <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON(可选). <br/>
 * date: 2018年8月23日 下午6:37:07 <br/>
 *
 * @author liupingan
 * @version
 * @since JDK 1.8
 */
@Slf4j
public class DataUtil
{

	/**
	 * Object field switch
	 * 
	 * @param source
	 * @param target
	 * @return
	 */
	public static Object objectInterconversion(Object source, Class<? extends Object> target)
	{
		if (!(source instanceof Object))
		{
			return null;
		}
		Object result = null;
		try
		{
			result = target.getConstructor().newInstance();

			Field[] fields = target.getDeclaredFields();
			Field targetField = null;
			Field sourceField = null;
			for (int i = 0; i < fields.length; i++)
			{// 遍历

				try
				{
					// 得到属性
					targetField = fields[i];
					// 打开私有访问
					targetField.setAccessible(true);
					// 获取属性
					String name = targetField.getName();
					if (CommonConstant.SERIAL_VERSION_UID.equals(name))
					{
						continue;
					}
					sourceField = source.getClass().getDeclaredField(name);
					if (sourceField != null && targetField.getType().equals(sourceField.getType()))
					{
						sourceField.setAccessible(true);
						targetField.set(result, sourceField.get(source));
					}
				} catch (IllegalAccessException e)
				{
					log.error(e.getMessage());
				} catch (NoSuchFieldException e)
				{
					log.error(e.getMessage());
				}
			}
		} catch (NoSuchMethodException e)
		{
			log.error(e.getMessage());
		} catch (SecurityException e)
		{
			log.error(e.getMessage());
		} catch (InstantiationException e1)
		{
			log.error(e1.getMessage());
		} catch (IllegalAccessException e1)
		{
			log.error(e1.getMessage());
		} catch (IllegalArgumentException e1)
		{
			log.error(e1.getMessage());
		} catch (InvocationTargetException e1)
		{
			log.error(e1.getMessage());
		}
		return result;
	}

	/**
	 * @param obj
	 *            要克隆的对象
	 * @param <T>
	 *            泛型
	 * @return 克隆的对象
	 * @throws Exception
	 *             如果没有实现seriablizable接口将会抛出异常
	 */
	@SuppressWarnings(CommonConstant.UNCHECKED)
	public static <T> T copyImplSerializable(T obj) throws Exception
	{
		ByteArrayOutputStream baos = null;
		ObjectOutputStream oos = null;

		ByteArrayInputStream bais = null;
		ObjectInputStream ois = null;

		Object o;
		// 如果子类没有继承该接口，这一步会报错
		try
		{
			baos = new ByteArrayOutputStream();
			oos = new ObjectOutputStream(baos);
			oos.writeObject(obj);
			bais = new ByteArrayInputStream(baos.toByteArray());
			ois = new ObjectInputStream(bais);

			o = ois.readObject();
			return (T) o;
		} catch (Exception e)
		{
			throw new Exception(CommonConstant.STRING_STR);
		} finally
		{
			try
			{
				baos.close();
				oos.close();
				bais.close();
				ois.close();
			} catch (Exception e2)
			{
				// 这里报错不需要处理
			}
		}
	}

}
