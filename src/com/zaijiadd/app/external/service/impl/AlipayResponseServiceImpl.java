package com.zaijiadd.app.external.service.impl;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.zaijiadd.app.common.utils.Md5Utils;
import com.zaijiadd.app.external.service.AlipayResponseService;
import com.zaijiadd.app.external.utils.AlipayConfig;
import com.zaijiadd.app.external.utils.AlipayCore;
import com.zaijiadd.app.external.utils.RSA;
import com.zaijiadd.app.utils.constants.ConstantsForAccount;

public class AlipayResponseServiceImpl implements AlipayResponseService {

	/**
	 * 支付宝消息验证地址
	 */
	private static final String HTTPS_VERIFY_URL = "https://mapi.alipay.com/gateway.do?service=notify_verify&";

	@Override
	public Boolean verify( Map<String, String> params ) {

		// 判断responsetTxt是否为true，isSign是否为true
		// responsetTxt的结果不是true，与服务器设置问题、合作身份者ID、notify_id一分钟失效有关
		// isSign不是true，与安全校验码、请求时的参数格式（如：带自定义参数等）、编码格式有关
		String responseTxt = "false";
		if ( params.get( "notify_id" ) != null ) {
			String notify_id = params.get( "notify_id" );
			responseTxt = verifyResponse( notify_id );
		}
		String sign = "";
		if ( params.get( "sign" ) != null ) {
			sign = params.get( "sign" );
		}
		boolean isSign = getSignVeryfy( params, sign );

		// 写日志记录（若要调试，请取消下面两行注释）
		// String sWord = "responseTxt=" + responseTxt + "\n isSign=" + isSign +
		// "\n 返回回来的参数：" + AlipayCore.createLinkString(params);
		// AlipayCore.logResult(sWord);

		if ( isSign && responseTxt.equals( "true" ) ) {
			return true;
		} else {
			return false;
		}

	}

	/**
	 * 获取远程服务器ATN结果,验证返回URL
	 * 
	 * @param notify_id
	 *            通知校验ID
	 * @return 服务器ATN结果 验证结果集： invalid命令参数不对 出现这个错误，请检测返回处理中partner和key是否为空 true
	 *         返回正确信息 false 请检查防火墙或者是服务器阻止端口问题以及验证时间是否超过一分钟
	 */
	private static String verifyResponse( String notify_id ) {
		// 获取远程服务器ATN结果，验证是否是支付宝服务器发来的请求

		String partner = AlipayConfig.partner;
		String veryfy_url = HTTPS_VERIFY_URL + "partner=" + partner
				+ "&notify_id=" + notify_id;

		return checkUrl( veryfy_url );
	}

	/**
	 * 根据反馈回来的信息，生成签名结果
	 * 
	 * @param Params
	 *            通知返回来的参数数组
	 * @param sign
	 *            比对的签名结果
	 * @return 生成的签名结果
	 */
	private static boolean getSignVeryfy( Map<String, String> Params,
			String sign ) {
		// 过滤空值、sign与sign_type参数
		Map<String, String> sParaNew = AlipayCore.paraFilter( Params );
		// 获取待签名字符串
		String preSignStr = AlipayCore.createLinkString( sParaNew );
		// 获得签名验证结果
		boolean isSign = false;
		if ( AlipayConfig.sign_type.equals( "RSA" ) ) {
			isSign = RSA.verify( preSignStr, sign, AlipayConfig.ali_public_key,
					AlipayConfig.input_charset );
		}
		return isSign;
	}

	/**
	 * 获取远程服务器ATN结果
	 * 
	 * @param urlvalue
	 *            指定URL路径地址
	 * @return 服务器ATN结果 验证结果集： invalid命令参数不对 出现这个错误，请检测返回处理中partner和key是否为空 true
	 *         返回正确信息 false 请检查防火墙或者是服务器阻止端口问题以及验证时间是否超过一分钟
	 */
	private static String checkUrl( String urlvalue ) {
		String inputLine = "";

		try {
			URL url = new URL( urlvalue );
			HttpURLConnection urlConnection = ( HttpURLConnection ) url
					.openConnection();
			BufferedReader in = new BufferedReader( new InputStreamReader(
					urlConnection.getInputStream() ) );
			inputLine = in.readLine().toString();
		} catch ( Exception e ) {
			e.printStackTrace();
			inputLine = "";
		}

		return inputLine;
	}

	@Override
	public String getSign( String transAmount, String orderId, String subject ) {
		
		StringBuffer params = new StringBuffer();
		

		params.append( "_input_charset=" + ConstantsForAccount.ALIPAY_INPUT_CHARSET );
		params.append( "&notify_url=" + ConstantsForAccount.ALIPAY_SIGN_NOTIFY_URL );
		params.append( "&out_trade_no=" + orderId );
		params.append( "&partner=" + ConstantsForAccount.ALIPAY_SIGN_PARTNER );
		params.append( "&payment_type=" + ConstantsForAccount.ALIPAY_SIGN_PAYMENT_TYPE );
		params.append( "&return_url=" + ConstantsForAccount.ALIPAY_SIGN_RETURN_URL );
		params.append( "&seller_id=" + ConstantsForAccount.ALIPAY_SIGN_SELLER_ID );
		params.append( "&service=" + ConstantsForAccount.ALIPAY_SIGN_SERVICE );
		params.append( "&subject=" + subject );

		params.append( "&total_fee=" + transAmount );
		
		System.out.println( params.toString() );
		
		String sign = Md5Utils.MD5( params.toString() + ConstantsForAccount.ALIPAY_SIGN_KEY );
		
		return sign;
		
	}
	
	public static void main( String[] args ) {
		
		AlipayResponseServiceImpl i = new AlipayResponseServiceImpl();
		String sign = i.getSign( "12229.06", "123456789", "Fuck" );
		System.out.println( sign );
		
	}

}