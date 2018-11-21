/**
 * Project Name:rule-engine-core
 * File Name:FunctionTest.java
 * Package Name:com.foxconn.core.pro.server.rule.engine.core.test
 * Date:2018年10月17日上午8:38:37
 * Copyright (c) 2018, Foxconn All Rights Reserved.
 *
*/

package com.foxconn.core.pro.server.rule.engine.core.test;

import java.security.MessageDigest;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.Assert;

import com.alibaba.fastjson.JSONObject;
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
import com.ql.util.express.DefaultContext;
import com.ql.util.express.ExpressRunner;
import com.ql.util.express.IExpressContext;

/**
 * ClassName:FunctionTest <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2018年10月17日 上午8:38:37 <br/>
 * 
 * @author liupingan
 * @version
 * @since JDK 1.8
 * @see
 */
public class FunctionTest
{

	@org.junit.Test
	public void abs() throws Exception
	{
		String express = "abs(-99)";
		ExpressRunner runner = new ExpressRunner(false, true); // 显示执行编译过程
		runner.addFunction("abs", new OperatorAbs("abs"));
		Object objInt = runner.execute(express, null, null, false, true); // 显示指令执行过程

		Object objDouble = runner.execute("abs(99.01)", null, null, false, true); // 显示指令执行过程

		Object objLong = runner.execute("abs(-9999L)", null, null, false, true); // 显示指令执行过程
		Assert.assertTrue("表达式计算 Int", objInt.toString().equalsIgnoreCase(String.valueOf(Math.abs(-99))));

		Assert.assertTrue("abs 表达式计算 Double", objDouble.toString().equalsIgnoreCase(String.valueOf(Math.abs(99.01))));
		Assert.assertTrue("abs 表达式计算 Long", objLong.toString().equalsIgnoreCase(String.valueOf(Math.abs(-9999L))));
		System.out.println("abs 表达式计算 Int：" + express + " = " + objInt);
		System.out.println("abs 表达式计算 Double：" + "abs(99.01)" + " = " + objDouble);
		System.out.println("abs 表达式计算 Long：" + "abs(-9999L)" + " = " + objLong);
	}

	@org.junit.Test
	public void asin() throws Exception
	{
		String express = "asin(1)";
		ExpressRunner runner = new ExpressRunner(false, true); // 显示执行编译过程
		runner.addFunction("asin", new OperatorAsin("asin"));

		Object objInt = runner.execute(express, null, null, false, true); // 显示指令执行过程

		Object objDouble = runner.execute("asin(0.8)", null, null, false, true); // 显示指令执行过程

		Object objLong = runner.execute("asin(1L)", null, null, false, true); // 显示指令执行过程

		Assert.assertTrue("asin 表达式计算 Int",
				objInt.toString().equalsIgnoreCase(String.valueOf(Math.toDegrees(Math.asin(1)))));

		Assert.assertTrue("asin 表达式计算 Double",
				objDouble.toString().equalsIgnoreCase(String.valueOf(Math.toDegrees(Math.asin(0.8)))));
		Assert.assertTrue("asin 表达式计算 Long",
				objLong.toString().equalsIgnoreCase(String.valueOf(Math.toDegrees(Math.asin(1L)))));

		System.out.println("asin 表达式计算 Int：" + express + " = " + objInt);
		System.out.println("asin 表达式计算 Double：" + "asin(0.8)" + " = " + objDouble);
		System.out.println("asin 表达式计算 Long：" + "asin(1L)" + " = " + objLong);
	}

	@org.junit.Test
	public void concat_ws() throws Exception
	{
		String express = "concat_ws('$$',99,'str000999')";
		ExpressRunner runner = new ExpressRunner(false, true); // 显示执行编译过程
		runner.addFunction("concat_ws", new OperatorConcatWs("concat_ws"));
		Object objInt = runner.execute(express, null, null, false, true); // 显示指令执行过程

		Object objDouble = runner.execute("concat_ws('$$','7777',999.01)", null, null, false, true); // 显示指令执行过程

		Object objLong = runner.execute("concat_ws(88,99,99723242)", null, null, false, true); // 显示指令执行过程

		Assert.assertTrue("concat_ws 表达式计算 Int",
				objInt.toString().equalsIgnoreCase(String.valueOf("99$$str000999")));

		Assert.assertTrue("concat_ws 表达式计算 Double",
				objDouble.toString().equalsIgnoreCase(String.valueOf("7777$$999.01")));
		Assert.assertTrue("concat_ws 表达式计算 Long",
				objLong.toString().equalsIgnoreCase(String.valueOf("998899723242")));

		System.out.println("concat_ws 表达式计算 Int：" + express + " = " + objInt);
		System.out.println("concat_ws 表达式计算 Double：" + "concat_ws('$$','7777',999.01)" + " = " + objDouble);
		System.out.println("concat_ws 表达式计算 Long：" + "concat_ws(88,99,99723242)" + " = " + objLong);
	}

	@org.junit.Test
	public void concat() throws Exception
	{
		String express = "concat(99,'str000999')";
		ExpressRunner runner = new ExpressRunner(false, true); // 显示执行编译过程
		runner.addFunction("concat", new OperatorConcat("concat"));
		Object objInt = runner.execute(express, null, null, false, true); // 显示指令执行过程

		String express2 = "concat(99,888L)";
		Object objDouble = runner.execute(express2, null, null, false, true); // 显示指令执行过程

		String express3 = "concat(99,8.991)";
		Object objLong = runner.execute(express3, null, null, false, true); // 显示指令执行过程

		Assert.assertTrue("concat 表达式计算 Int",
				objInt.toString().equalsIgnoreCase(String.valueOf("99str000999")));

		Assert.assertTrue("concat 表达式计算 Double",
				objDouble.toString().equalsIgnoreCase(String.valueOf("99888")));
		Assert.assertTrue("concat 表达式计算 Long",
				objLong.toString().equalsIgnoreCase(String.valueOf("998.991")));

		System.out.println("concat 表达式计算 Int：" + express + " = " + objInt);
		System.out.println("concat 表达式计算 Double：" + express2 + " = " + objDouble);
		System.out.println("concat 表达式计算 Long：" + express3 + " = " + objLong);
	}

	@org.junit.Test
	public void cos() throws Exception
	{
		String express = "cos(279)";
		ExpressRunner runner = new ExpressRunner(false, true); // 显示执行编译过程
		runner.addFunction("cos", new OperatorCos("cos"));
		Object objInt = runner.execute(express, null, null, false, true); // 显示指令执行过程

		String express2 = "cos(180)";
		Object objDouble = runner.execute(express2, null, null, false, true); // 显示指令执行过程

		String express3 = "cos(99.01)";
		Object objLong = runner.execute(express3, null, null, false, true); // 显示指令执行过程

		Assert.assertTrue("cos 表达式计算 1",
				objInt.toString().equalsIgnoreCase(String.valueOf(Math.cos(279))));

		Assert.assertTrue("cos 表达式计算 2",
				objDouble.toString().equalsIgnoreCase(String.valueOf(Math.cos(180))));
		Assert.assertTrue("cos 表达式计算3",
				objLong.toString().equalsIgnoreCase(String.valueOf(Math.cos(99.01))));

		System.out.println("cos 表达式计算 1：" + express + " = " + objInt);
		System.out.println("cos 表达式计算 2：" + express2 + " = " + objDouble);
		System.out.println("cos 表达式计算 3：" + express3 + " = " + objLong);
	}

	@org.junit.Test
	public void cosh() throws Exception
	{
		String express = "cosh(10.1)";
		ExpressRunner runner = new ExpressRunner(false, true); // 显示执行编译过程
		runner.addFunction("cosh", new OperatorCosh("cosh"));
		Object objInt = runner.execute(express, null, null, false, true); // 显示指令执行过程

		String express2 = "cosh(20)";
		Object objDouble = runner.execute(express2, null, null, false, true); // 显示指令执行过程

		String express3 = "cosh(30)";
		Object objLong = runner.execute(express3, null, null, false, true); // 显示指令执行过程

		Assert.assertTrue("cosh 表达式计算 1",
				objInt.toString().equalsIgnoreCase(String.valueOf(Math.cosh(10.1))));

		Assert.assertTrue("cosh 表达式计算 2",
				objDouble.toString().equalsIgnoreCase(String.valueOf(Math.cosh(20))));
		Assert.assertTrue("cosh",
				objLong.toString().equalsIgnoreCase(String.valueOf(Math.cosh(30))));

		System.out.println("cosh 表达式计算 1：" + express + " = " + objInt);
		System.out.println("cosh 表达式计算 2：" + express2 + " = " + objDouble);
		System.out.println("cosh 表达式计算 3：" + express3 + " = " + objLong);
	}

	@org.junit.Test
	public void crypto() throws Exception
	{
		String express = "crypto(123456,'md5')";
		ExpressRunner runner = new ExpressRunner(false, true); // 显示执行编译过程
		runner.addFunction("crypto", new OperatorDigest("crypto"));
		Object objInt = runner.execute(express, null, null, false, true); // 显示指令执行过程

		String express2 = "crypto('456sdwerrwer','SHA-384')";
		Object objDouble = runner.execute(express2, null, null, false, true); // 显示指令执行过程

		String express3 = "crypto('dg23sdfsfdf','md2')";
		Object objLong = runner.execute(express3, null, null, false, true); // 显示指令执行过程

		MessageDigest messageDigest= DigestUtils.getDigest("md5");
		;
		Assert.assertTrue("crypto 表达式计算 1",
				objInt.toString().equals(Hex.encodeHexString(messageDigest.digest("123456".getBytes()))));

		messageDigest= DigestUtils.getDigest("SHA-384");
		Assert.assertTrue("crypto 表达式计算 2",
				objDouble.toString().equals(Hex.encodeHexString(messageDigest.digest("456sdwerrwer".getBytes()))));
		messageDigest= DigestUtils.getDigest("md2");
		Assert.assertTrue("crypto",
				objLong.toString().equals(Hex.encodeHexString(messageDigest.digest("dg23sdfsfdf".getBytes()))));

		System.out.println("crypto 表达式计算 1：" + express + " = " + objInt);
		System.out.println("crypto 表达式计算 2：" + express2 + " = " + objDouble);
		System.out.println("crypto 表达式计算 3：" + express3 + " = " + objLong);
	}

	@org.junit.Test
	public void deviceName() throws Exception
	{
		String express = "import com.alibaba.fastjson.JSONObject;"
				+"import com.foxconn.core.pro.server.rule.engine.core.common.util.JSONObjectUtil;"
				+ ""
				+""
				+ "return deviceName();";
		IExpressContext<String, Object> expressContext = new DefaultContext<>();
		expressContext.put(CommonConstant.QL_DEVICE_NAME, "test_deviceName_0001");
		
		ExpressRunner runner = new ExpressRunner(false, true); // 显示执行编译过程

		runner.addFunction("deviceName", new OperatorDeviceName("deviceName"));

		Object r = runner.execute(express, expressContext, null, false, true); // 显示指令执行过程
		Assert.assertTrue("表达式计算", "test_deviceName_0001".toString().equals(r.toString()));
		System.out.println("表达式计算：" + express + " = " + r);
	}

	@org.junit.Test
	public void endswith() throws Exception
	{
		JSONObject root = new JSONObject();
		root.put("aaa","hgjhgjiii");
		String express = "import com.alibaba.fastjson.JSONObject;"
				+"import com.foxconn.core.pro.server.rule.engine.core.common.util.JSONObjectUtil;\r\n"
				+ "return endswith(deviceName(),'null');";
		IExpressContext<String, Object> expressContext = new DefaultContext<>();
		//expressContext.put("root", "root");
		expressContext.put("aaa", "hgjhgjiii");
		expressContext.put(CommonConstant.QL_DEVICE_NAME, 1111);
		
		ExpressRunner runner = new ExpressRunner(false, true); // 显示执行编译过程
		runner.addFunction("endswith", new OperatorEndWith("endswith"));
		runner.addFunction("deviceName", new OperatorDeviceName("deviceName"));
		Object r = runner.execute(express, expressContext, null, false, true); // 显示指令执行过程
		// Assert.assertTrue("表达式计算", r.toString().equalsIgnoreCase("117"));
		System.out.println("表达式计算：" + express + " = " + r);
	}

	@org.junit.Test
	public void exp() throws Exception
	{
		String express = "exp(99)";
		ExpressRunner runner = new ExpressRunner(false, true); // 显示执行编译过程
		runner.addFunction("exp", new OperatorExp("exp"));
		Object r = runner.execute(express, null, null, false, true); // 显示指令执行过程
		Assert.assertTrue("表达式计算", r.toString().equals(String.valueOf(Math.exp(99))));
		System.out.println("表达式计算：" + express + " = " + r);
	}

	@org.junit.Test
	public void floor() throws Exception
	{
		String express = "floor(99)";
		ExpressRunner runner = new ExpressRunner(false, true); // 显示执行编译过程

		runner.addFunction("floor", new OperatorFloor("floor"));
		Object r = runner.execute(express, null, null, false, true); // 显示指令执行过程
		Assert.assertTrue("表达式计算", r.toString().equals(String.valueOf(Math.floor(99))));
		System.out.println("表达式计算：" + express + " = " + r);
	}

	@org.junit.Test
	public void log() throws Exception
	{
		String express = "log(99)";
		ExpressRunner runner = new ExpressRunner(false, true); // 显示执行编译过程

		runner.addFunction("log", new OperatorLog("log"));
		Object r = runner.execute(express, null, null, false, true); // 显示指令执行过程
		Assert.assertTrue("表达式计算 1", r.toString().equals(String.valueOf(Math.log(99))));
		System.out.println("表达式计算 1：" + express + " = " + r);
		
		String express2 = "log(99,10)";

		Object result2 = runner.execute(express2, null, null, false, true); // 显示指令执行过程
		Assert.assertTrue("表达式计算 1", result2.toString().equals(String.valueOf(Math.log(99)/Math.log(10))));
		System.out.println("表达式计算 1：" + express2 + " = " + result2);
	}

	@org.junit.Test
	public void lower() throws Exception
	{
		String express = "lower(\"UUUUioittOOOO\")";
		ExpressRunner runner = new ExpressRunner(false, true); // 显示执行编译过程
		runner.addFunction("lower", new OperatorLower("lower"));
		Object r = runner.execute(express, null, null, false, true); // 显示指令执行过程
		Assert.assertTrue("表达式计算", r.toString().equals("UUUUioittOOOO".toLowerCase()));
		System.out.println("表达式计算：" + express + " = " + r);
	}

	@org.junit.Test
	public void modulo() throws Exception
	{
		String express = "modulo(99,7)";
		ExpressRunner runner = new ExpressRunner(false, true); // 显示执行编译过程
		runner.addFunction("modulo", new OperatorMod("modulo"));
		Object r = runner.execute(express, null, null, false, true); // 显示指令执行过程
		Assert.assertTrue("表达式计算", r.toString().equalsIgnoreCase(String.valueOf(Math.floorMod(99, 7))));
		System.out.println("表达式计算：" + express + " = " + r);
	}

	@org.junit.Test
	public void nanvl() throws Exception
	{
		
		String express = "import com.alibaba.fastjson.JSONObject;"
				+"import com.foxconn.core.pro.server.rule.engine.core.common.util.JSONObjectUtil;\r\n"
				+ "return nanvl(deviceName(),'default');";
		IExpressContext<String, Object> expressContext = new DefaultContext<>();
		expressContext.put("aaa", "hgjhgjiii");
		expressContext.put(CommonConstant.QL_DEVICE_NAME, 1111);
		
		ExpressRunner runner = new ExpressRunner(false, true); // 显示执行编译过程
		runner.addFunction("nanvl", new OperatorNanvl("nanvl"));
		runner.addFunction("deviceName", new OperatorDeviceName("deviceName"));
		
		 String express2 = "import com.alibaba.fastjson.JSONObject;"
					+"import com.foxconn.core.pro.server.rule.engine.core.common.util.JSONObjectUtil;\r\n"
					+ "return nanvl(aaaa,'default2');";
		Object object1 = runner.execute(express, expressContext, null, false, true); // 显示指令执行过程
		
		Object object2 = runner.execute(express2, expressContext, null, false, true); // 显示指令执行过程
		Assert.assertTrue("表达式计算1", object1.toString().equals("1111"));
		Assert.assertTrue("表达式计算2", object2.toString().equals("default2"));
		System.out.println("表达式计算1：" + express + " = " + object1);
		System.out.println("表达式计算2：" + express2 + " = " + object2);
	}

	@org.junit.Test
	public void newuuid() throws Exception
	{
		String express = "newuuid()";
		ExpressRunner runner = new ExpressRunner(false, true); // 显示执行编译过程
		runner.addFunction("newuuid", new OperatorNewuuid("newuuid"));
		Object object = runner.execute(express, null, null, false, true); // 显示指令执行过程
		Assert.assertTrue("表达式计算", object != null && object.toString().length()==32);
		System.out.println("表达式计算：" + express + " = " + object);
	}

	@org.junit.Test
	public void power() throws Exception
	{
		String express = "power(9,2)";
		ExpressRunner runner = new ExpressRunner(false, true); // 显示执行编译过程

		runner.addFunction("power", new OperatorPower("power"));
		Object object1 = runner.execute(express, null, null, false, true); // 显示指令执行过程
		
		String express2 = "power(9.9,5.6)";
		Object object2 = runner.execute(express2, null, null, false, true); // 显示指令执行过程
		Assert.assertTrue("表达式计算1", object1.toString().equals(String.valueOf(Math.pow(9, 2))));
		Assert.assertTrue("表达式计算2", object2.toString().equals(String.valueOf(Math.pow(9.9, 5.6))));
		System.out.println("表达式计算1：" + express + " = " + object1);
		System.out.println("表达式计算2：" + express2 + " = " + object2);
	}

	@org.junit.Test
	public void productId() throws Exception
	{
		String express = "import com.alibaba.fastjson.JSONObject;"
				+"import com.foxconn.core.pro.server.rule.engine.core.common.util.JSONObjectUtil;\r\n"
				+ "return productId();";
		IExpressContext<String, Object> expressContext = new DefaultContext<>();
		expressContext.put(CommonConstant.QL_PRODUCT_KEY, "productId_001");
		ExpressRunner runner = new ExpressRunner(false, true); // 显示执行编译过程
		runner.addFunction("productId", new OperatorProductId("productId"));
		Object object1 = runner.execute(express, expressContext, null, false, true); // 显示指令执行过程
		
		Assert.assertTrue("表达式计算1", object1.toString().equals("productId_001"));
		System.out.println("表达式计算1：" + express + " = " + object1);
	}

	@org.junit.Test
	public void random() throws Exception
	{
		String express = "random()";
		ExpressRunner runner = new ExpressRunner(false, true); // 显示执行编译过程
		runner.addFunction("random", new OperatorRandom("random"));
		Object r = runner.execute(express, null, null, false, true); // 显示指令执行过程
		System.out.println("表达式计算：" + express + " = " + r);
	}

	@org.junit.Test
	public void randint() throws Exception
	{
		String express = "randint(10,99)";
		ExpressRunner runner = new ExpressRunner(false, true); // 显示执行编译过程
		runner.addFunction("randint", new OperatorRandInt("randint"));
		Object r = runner.execute(express, null, null, false, true); // 显示指令执行过程
		System.out.println("表达式计算：" + express + " = " + r);
	}

	@org.junit.Test
	public void replace() throws Exception
	{
		String express = "return replace('iijjsdf88123puuidpoosdfsd','uuid','=====');";
		ExpressRunner runner = new ExpressRunner(false, true); // 显示执行编译过程
		runner.addFunction("replace", new OperatorReplace("replace"));

		Object r = runner.execute(express, null, null, false, true); // 显示指令执行过程
		Assert.assertTrue("表达式计算", r.toString().equals("iijjsdf88123p=====poosdfsd"));
		System.out.println("表达式计算：" + express + " = " + r);
	}

	@org.junit.Test
	public void sin() throws Exception
	{
		String express = "sin(99)";
		ExpressRunner runner = new ExpressRunner(false, true); // 显示执行编译过程

		runner.addFunction("sin", new OperatorSin("sin"));
		Object r = runner.execute(express, null, null, false, true); // 显示指令执行过程
		Assert.assertTrue("表达式计算", r.toString().equals(String.valueOf(Math.sin(99))));
		System.out.println("表达式计算：" + express + " = " + r);
	}

	@org.junit.Test
	public void sinh() throws Exception
	{
		String express = "sinh(99.8)";
		ExpressRunner runner = new ExpressRunner(false, true); // 显示执行编译过程
		runner.addFunction("sinh", new OperatorSinh("sinh"));
		Object r = runner.execute(express, null, null, false, true); // 显示指令执行过程
		Assert.assertTrue("表达式计算", r.toString().equals(String.valueOf(Math.sinh(99.8))));
		System.out.println("表达式计算：" + express + " = " + r);
	}

	@org.junit.Test
	public void tan() throws Exception
	{
		String express = "tan(6.7)";
		ExpressRunner runner = new ExpressRunner(false, true); // 显示执行编译过程

		runner.addFunction("tan", new OperatorTan("tan"));
		Object r = runner.execute(express, null, null, false, true); // 显示指令执行过程
		Assert.assertTrue("表达式计算", r.toString().equals(String.valueOf(Math.tan(6.7))));
		System.out.println("表达式计算：" + express + " = " + r);
	}

	@org.junit.Test
	public void tanh() throws Exception
	{
		String express = "tanh(6.7)";
		ExpressRunner runner = new ExpressRunner(false, true); // 显示执行编译过程
		runner.addFunction("tanh", new OperatorTanh("tanh"));
		Object r = runner.execute(express, null, null, false, true); // 显示指令执行过程
		Assert.assertTrue("表达式计算", r.toString().equals(String.valueOf(Math.tanh(6.7))));
		System.out.println("表达式计算：" + express + " = " + r);
	}

	@org.junit.Test
	public void timestamp() throws Exception
	{
		String express = "timestamp('yyyy-MM-dd HH:mm:ss.SSS')";
		ExpressRunner runner = new ExpressRunner(false, true); // 显示执行编译过程
		runner.addFunction("timestamp", new OperatorTimestamp("timestamp"));
		Object r = runner.execute(express, null, null, false, true); // 显示指令执行过程
		System.out.println("表达式计算：" + express + " = " + r);
	}

	@org.junit.Test
	public void topic() throws Exception
	{

		String express = "import com.alibaba.fastjson.JSONObject;"
				+"import com.foxconn.core.pro.server.rule.engine.core.common.util.JSONObjectUtil;"
				+ "return topic();";
		IExpressContext<String, Object> expressContext = new DefaultContext<>();
		expressContext.put(CommonConstant.QL_TOPIC, "/test_Topic_0001/002/003/004");
		
		ExpressRunner runner = new ExpressRunner(false, true); // 显示执行编译过程

		runner.addFunction("topic", new OperatorTopic("topic"));

		Object r = runner.execute(express, expressContext, null, false, true); // 显示指令执行过程
		
		
		
		express = "import com.alibaba.fastjson.JSONObject;"
				+"import com.foxconn.core.pro.server.rule.engine.core.common.util.JSONObjectUtil;"
				+ "return topic(3);";
		
		
		Object object2 = runner.execute(express, expressContext, null, false, true); // 显示指令执行过程
		Assert.assertTrue("表达式计算", "/test_Topic_0001/002/003/004".toString().equals(r.toString()));
		System.out.println("表达式计算：" + express + " = " + r);
		Assert.assertTrue("表达式计算2", "003".toString().equals(object2.toString()));
		System.out.println("表达式计算2：" + express + " = " + object2);
	}

	@org.junit.Test
	public void upper() throws Exception
	{
		String express = "upper('oooPPPbbbFFdddd981231231231yyyy')";
		ExpressRunner runner = new ExpressRunner(false, true); // 显示执行编译过程

		runner.addFunction("upper", new OperatorUpper("upper"));
		Object r = runner.execute(express, null, null, false, true); // 显示指令执行过程
		Assert.assertTrue("表达式计算", r.toString().equals("oooPPPbbbFFdddd981231231231yyyy".toUpperCase()));
		System.out.println("表达式计算：" + express + " = " + r);
	}

}
