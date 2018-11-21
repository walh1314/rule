package com.foxconn.core.pro.server.rule.engine.front.common.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.Enumeration;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import com.foxconn.core.pro.server.rule.engine.front.common.constant.CommonConstant;
import com.foxconn.core.pro.server.rule.engine.core.thirdparty.common.service.CoreproCommonService;
import com.foxconn.core.pro.server.rule.engine.core.thirdparty.entity.UserDataBaseBean;
import com.foxconn.core.pro.server.rule.engine.front.dto.InputMap;
import com.foxconn.core.pro.server.rule.engine.front.entity.UserInfo;
import lombok.extern.slf4j.Slf4j;

/**
 * 系统工具类 ClassName: SystemUtil <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON(可选). <br/>
 * date: 2018年8月31日 下午2:24:41 <br/>
 *
 * @author liupingan
 * @version
 * @since JDK 1.8
 */
@Component
@Slf4j
public class SystemUtil
{

	@Autowired
	private CoreproCommonService coreproCommonService;

	public UserInfo getCurrentUser()
	{

		ServletRequestAttributes requestAttr = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes());
		if (requestAttr == null)
		{
			return null;
		}
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
				.getRequest();
		if (request != null)
		{
			return (UserInfo) request.getAttribute(CommonConstant.CURRENT_USER);
		}
		return null;
	}

	public void setCurrentUser(InputMap<? extends Object> inputMap)
	{

		ServletRequestAttributes requestAttr = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes());
		if (requestAttr == null)
		{
			return;
		}
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
				.getRequest();
		if (request != null)
		{
			request.setAttribute(CommonConstant.CURRENT_USER, inputMap.getConfig());
		}
	}

	public void setUserDataBaseBean(UserDataBaseBean inputMap)
	{

		ServletRequestAttributes requestAttr = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes());
		if (requestAttr == null)
		{
			return;
		}
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
				.getRequest();
		if (request != null)
		{
			if (inputMap != null)
			{
				// 获取第一个值
				request.setAttribute(CommonConstant.TENANT_DB, inputMap);
			} else
			{
				request.setAttribute(CommonConstant.TENANT_DB, null);
			}
		}
	}
	
	/*public void setTenantDb(UserDataBaseBean bean)
	{

		ServletRequestAttributes requestAttr = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes());
		if (requestAttr == null)
		{
			return;
		}
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
				.getRequest();
		if (request != null)
		{
			request.setAttribute("tenantDb", bean);
		}
	}*/

	public void setUserDataBaseBean(String userId)
	{
		UserDataBaseBean result = coreproCommonService.getUserDataBase(userId);
		// CloundMap<TenantDb> result =
		// accountDbService.getTenantDb(userId,requestMap);
		// 如果为成功，则进行处理
		if (result != null)
		{
			//设备所有者就是当前用户
			result.setDeviceOwner(userId);
			setUserDataBaseBean(result);
		}
	}

	public void setUserDataBaseBean(UserInfo userInfo)
	{

		if (userInfo != null && userInfo.getUserId() != null && StringUtils.isNotBlank(userInfo.getUserId()))
		{
			String userId = userInfo.getUserId().trim();
			setUserDataBaseBean(userId);
		}
	}

	public UserDataBaseBean getUserDataBaseBean()
	{

		ServletRequestAttributes requestAttr = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes());
		if (requestAttr == null)
		{
			return null;
		}
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
				.getRequest();
		if (request != null)
		{
			return (UserDataBaseBean) request.getAttribute(CommonConstant.TENANT_DB);
		}
		return null;
	}

	/**
	 * 设置创建人和修改人
	 * 
	 * @param record
	 * @param isSetCreate
	 */
	public <T> void setCreaterAndModifier(T record, UserInfo currentUser, boolean isSetCreate)
	{
		// UserInfo currentUser = this.getCurrentUser();
		Method method = null;
		try
		{
			if (record != null)
			{
				if (currentUser != null)
				{

					method = record.getClass().getMethod(CommonConstant.SET_MODIFIER, String.class);

					if (method != null)
					{
						method.invoke(record, currentUser.getUserId());
					}
					if (isSetCreate)
					{
						method = record.getClass().getMethod(CommonConstant.SET_CREATOR, String.class);
						if (method != null)
						{
							method.invoke(record, currentUser.getUserId());
						}

						method = record.getClass().getMethod(CommonConstant.SET_CREATETIME, Date.class);
						if (method != null)
						{
							method.invoke(record, new Date());
						}
					}

				}
				method = record.getClass().getMethod(CommonConstant.SET_MODIFYTIME, Date.class);
				if (method != null)
				{
					method.invoke(record, new Date());
				}
			}
		} catch (NoSuchMethodException e)
		{
			log.error(e.getMessage());
		} catch (SecurityException e)
		{
			log.error(e.getMessage());
		} catch (IllegalAccessException e)
		{
			log.error(e.getMessage());
		} catch (IllegalArgumentException e)
		{
			log.error(e.getMessage());
		} catch (InvocationTargetException e)
		{
			log.error(e.getMessage());
		}
	}

	/**
	 * 获取服务器IP地址
	 * 
	 * @return
	 */
	public static String getServerIp()
	{
		String serviceIp = null;
		try
		{
			Enumeration<NetworkInterface> netInterfaces = NetworkInterface.getNetworkInterfaces();
			InetAddress ip = null;
			while (netInterfaces.hasMoreElements())
			{
				NetworkInterface ni = (NetworkInterface) netInterfaces.nextElement();
				ip = (InetAddress) ni.getInetAddresses().nextElement();
				serviceIp = ip.getHostAddress();
				if (!ip.isSiteLocalAddress() && !ip.isLoopbackAddress() && ip.getHostAddress().indexOf(":") == -1)
				{
					serviceIp = ip.getHostAddress();
					break;
				} else
				{
					ip = null;
				}
			}
		} catch (SocketException e)
		{
			log.error(e.getMessage());
		}
		return serviceIp;
	}

	public static String getServerLocalIp()
	{
		String serviceIp = null;
		InetAddress address;
		try
		{
			address = InetAddress.getLocalHost();// 获取的是本地的IP地址
													// //PC-20140317PXKX/192.168.0.121
			serviceIp = address.getHostAddress();// 192.168.0.121
		} catch (UnknownHostException e)
		{
			log.error(e.getMessage());
		}

		return serviceIp;
	}
}
