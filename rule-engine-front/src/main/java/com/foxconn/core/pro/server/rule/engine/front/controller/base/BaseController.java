package com.foxconn.core.pro.server.rule.engine.front.controller.base;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import com.foxconn.core.pro.server.rule.engine.front.common.constant.CommonConstant;
import com.foxconn.core.pro.server.rule.engine.front.common.entity.ResultMap;
import org.apache.commons.lang.StringUtils;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class BaseController
{
	private MessageSource messageSource;

	@Value(CommonConstant.SPRING_MESSAGES_BASENAME)
	private String basename;

	@Value(CommonConstant.SPRING_MESSAGES_CACHE_DURATION)
	private int cacheSeconds;

	@Value(CommonConstant.SPRING_MESSAGES_ENCODING)
	private String encoding;

	/**
	 * 初始化
	 * 
	 * @return
	 */
	private MessageSource initMessageSource()
	{
		ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
		messageSource.setBasename(basename);
		messageSource.setDefaultEncoding(encoding);
		messageSource.setCacheSeconds(cacheSeconds);
		return messageSource;
	}

	/**
	 * 设置当前的返回信息
	 * 
	 * @param request
	 * @param code
	 * @return
	 */
	public ResultMap<? extends Object> getMessage(HttpServletRequest request, ResultMap<? extends Object> result)
	{
		if (messageSource == null)
		{
			messageSource = initMessageSource();
		}
		String acceptLanguage = request.getHeader(CommonConstant.ACCEPT_LANGUAGE);
		// 默认没有就是请求地区的语言
		Locale locale = null;

		locale = getLauage(acceptLanguage);
		if (locale == null)
		{
			locale = request.getLocale();
		}
		if (result != null)
		{
			List<String> args = null;
			try
			{
				if (result.getArgs() != null)
				{
					args = new ArrayList<>(result.getArgs().length);
					for (int i = 0; i < result.getArgs().length; i++)
					{
						// args 国际化
						args.add(messageSource.getMessage(result.getArgs()[i], null, locale));
					}
				}

				if (StringUtils.isBlank(result.getMsg()))
				{
					if (!StringUtils.isBlank(result.getCode()))
					{
						result.setMsg(messageSource.getMessage(result.getCode(), args.toArray(), locale));
					}
				} else
				{
					result.setMsg(
							messageSource.getMessage(result.getMsg(), (args == null || args.size() == 0) ? new String[]
							{} : args.toArray(), locale));
				}
			} catch (NoSuchMessageException e)
			{
				log.error(e.getMessage());
			}
		}
		return result;
	}

	/**
	 * 设置当前的返回信息
	 * 
	 * @param request
	 * @param code
	 * @return
	 */
	public ResultMap<? extends Object> getMessage(ResultMap<? extends Object> result)
	{
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
				.getRequest();
		return getMessage(request, result);

	}

	/**
	 * @param acceptLanguage
	 * @return
	 */
	private Locale getLauage(String acceptLanguage)
	{
		Locale result = null;
		if (StringUtils.isBlank(acceptLanguage))
		{
			return result;
		} else
		{
			String[] languages = acceptLanguage.split(CommonConstant.REGULAR_EXPRESSION);
			String[] locales = null;
			for (int i = 0; i < languages.length; i++)
			{
				if (StringUtils.isBlank(languages[i]))
				{
					continue;
				}
				locales = languages[i].split(CommonConstant.LINE);

				result = new Locale(locales[0].trim(), locales.length > 1 ? locales[1].trim() : "",
						locales.length > 2 ? locales[2].trim() : "");
			}
		}
		return result;
	}
}
