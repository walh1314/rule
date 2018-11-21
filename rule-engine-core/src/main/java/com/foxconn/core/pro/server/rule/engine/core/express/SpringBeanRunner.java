/**
 * Project Name:rule-engine-core
 * File Name:SpringBeanRunner.java
 * Package Name:com.foxconn.core.pro.server.rule.engine.core.express
 * Date:2018年8月23日下午4:11:00
 * Copyright (c) 2018, Foxconn All Rights Reserved.
 *
*/

package com.foxconn.core.pro.server.rule.engine.core.express;

import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import com.foxconn.core.pro.server.rule.engine.core.constant.CommonConstant;
import com.foxconn.core.pro.server.rule.engine.core.express.op.digest.OperatorDigest;
import com.foxconn.core.pro.server.rule.engine.core.express.op.number.OperatorAbs;
import com.foxconn.core.pro.server.rule.engine.core.express.op.number.OperatorAsin;
import com.foxconn.core.pro.server.rule.engine.core.express.op.number.OperatorCos;
import com.foxconn.core.pro.server.rule.engine.core.express.op.number.OperatorCosh;
import com.foxconn.core.pro.server.rule.engine.core.express.op.number.OperatorExp;
import com.foxconn.core.pro.server.rule.engine.core.express.op.number.OperatorFloor;
import com.foxconn.core.pro.server.rule.engine.core.express.op.number.OperatorLog;
import com.foxconn.core.pro.server.rule.engine.core.express.op.number.OperatorMod;
import com.foxconn.core.pro.server.rule.engine.core.express.op.number.OperatorPower;
import com.foxconn.core.pro.server.rule.engine.core.express.op.number.OperatorRandInt;
import com.foxconn.core.pro.server.rule.engine.core.express.op.number.OperatorRandom;
import com.foxconn.core.pro.server.rule.engine.core.express.op.number.OperatorSin;
import com.foxconn.core.pro.server.rule.engine.core.express.op.number.OperatorSinh;
import com.foxconn.core.pro.server.rule.engine.core.express.op.number.OperatorTan;
import com.foxconn.core.pro.server.rule.engine.core.express.op.number.OperatorTanh;
import com.foxconn.core.pro.server.rule.engine.core.express.op.string.OperatorConcat;
import com.foxconn.core.pro.server.rule.engine.core.express.op.string.OperatorConcatWs;
import com.foxconn.core.pro.server.rule.engine.core.express.op.string.OperatorEndWith;
import com.foxconn.core.pro.server.rule.engine.core.express.op.string.OperatorLower;
import com.foxconn.core.pro.server.rule.engine.core.express.op.string.OperatorReplace;
import com.foxconn.core.pro.server.rule.engine.core.express.op.string.OperatorUpper;
import com.foxconn.core.pro.server.rule.engine.core.express.op.sys.OperatorDeviceName;
import com.foxconn.core.pro.server.rule.engine.core.express.op.sys.OperatorNanvl;
import com.foxconn.core.pro.server.rule.engine.core.express.op.sys.OperatorNewuuid;
import com.foxconn.core.pro.server.rule.engine.core.express.op.sys.OperatorProductId;
import com.foxconn.core.pro.server.rule.engine.core.express.op.sys.OperatorTimestamp;
import com.foxconn.core.pro.server.rule.engine.core.express.op.sys.OperatorTopic;
import com.ql.util.express.ExpressRunner;
import com.ql.util.express.IExpressContext;

import lombok.extern.slf4j.Slf4j;

/**
 * ClassName:SpringBeanRunner <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2018年8月23日 下午4:11:00 <br/>
 * 
 * @author liupingan
 * @version
 * @since JDK 1.8
 * @see
 */
@Service
@Slf4j
public class SpringBeanRunner implements ApplicationContextAware
{
	private ApplicationContext applicationContext;
	private ExpressRunner runner;

	@Override
	public void setApplicationContext(ApplicationContext context)
	{
		this.applicationContext = context;
		runner = new ExpressRunner();
		//初始化增加函数
		initRunner();
	}

	private void initRunner()
	{
		// runner.addFunction("crypto", new OperatorDigest("crypto"));
		runner.addFunction(CommonConstant.ABS, new OperatorAbs(CommonConstant.ABS));
		runner.addFunction(CommonConstant.ASIN, new OperatorAsin(CommonConstant.ASIN));

		runner.addFunction(CommonConstant.CONCAT_WS, new OperatorConcatWs(CommonConstant.CONCAT_WS));
		runner.addFunction(CommonConstant.CONCAT, new OperatorConcat(CommonConstant.CONCAT));
		runner.addFunction(CommonConstant.COS, new OperatorCos(CommonConstant.COS));
		runner.addFunction(CommonConstant.COSH, new OperatorCosh(CommonConstant.COSH));
		runner.addFunction(CommonConstant.CRYPTO, new OperatorDigest(CommonConstant.CRYPTO));

		runner.addFunction(CommonConstant.DEVICE_NAME, new OperatorDeviceName(CommonConstant.DEVICE_NAME));

		runner.addFunction(CommonConstant.ENDSWITH, new OperatorEndWith(CommonConstant.ENDSWITH));
		runner.addFunction(CommonConstant.EX_P, new OperatorExp(CommonConstant.EX_P));

		runner.addFunction(CommonConstant.FLOOR, new OperatorFloor(CommonConstant.FLOOR));

		runner.addFunction(CommonConstant.LOG, new OperatorLog(CommonConstant.LOG));
		runner.addFunction(CommonConstant.LOWER, new OperatorLower(CommonConstant.LOWER));

		runner.addFunction(CommonConstant.MODULO, new OperatorMod(CommonConstant.MODULO));

		runner.addFunction(CommonConstant.NANVL, new OperatorNanvl(CommonConstant.NANVL));
		runner.addFunction(CommonConstant.NEWUUID, new OperatorNewuuid(CommonConstant.NEWUUID));

		runner.addFunction(CommonConstant.POWER, new OperatorPower(CommonConstant.POWER));
		runner.addFunction(CommonConstant.PRODUCTID, new OperatorProductId(CommonConstant.PRODUCTID));

		runner.addFunction(CommonConstant.RANDOM, new OperatorRandom(CommonConstant.RANDOM));
		runner.addFunction(CommonConstant.RANDINT, new OperatorRandInt(CommonConstant.RANDINT));	
		runner.addFunction(CommonConstant.REPLACE, new OperatorReplace(CommonConstant.REPLACE));

		runner.addFunction(CommonConstant.SIN, new OperatorSin(CommonConstant.SIN));
		runner.addFunction(CommonConstant.SINH, new OperatorSinh(CommonConstant.SINH));

		runner.addFunction(CommonConstant.TAN, new OperatorTan(CommonConstant.TAN));
		runner.addFunction(CommonConstant.TANH, new OperatorTanh(CommonConstant.TANH));
		runner.addFunction(CommonConstant.TIMESTAMP, new OperatorTimestamp(CommonConstant.TIMESTAMP));
		runner.addFunction(CommonConstant.TOP_IC, new OperatorTopic(CommonConstant.TOP_IC));
		
		runner.addFunction(CommonConstant.UPPER, new OperatorUpper(CommonConstant.UPPER));
		
	}

	public Object executeExpress(String text, Map<String, Object> context)
	{
		IExpressContext<String, Object> expressContext = new SpringBeanContext(context, this.applicationContext);
		try
		{
			return runner.execute(text, expressContext, null, true, false);
		} catch (Exception e)
		{
			log.error("qlExpress运行出错！", e);
		}
		return null;

	}
}
