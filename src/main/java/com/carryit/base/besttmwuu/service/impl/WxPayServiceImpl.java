package com.carryit.base.besttmwuu.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.carryit.base.besttmwuu.service.WxPayService;
import com.util.PayCommonUtil;
import com.util.PropertyUtil;
import com.util.XMLUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

@Service("wxPayService")
public class WxPayServiceImpl implements WxPayService{

    Logger logger = LoggerFactory.getLogger(WxPayServiceImpl.class);


    @Override
    public JSONObject wxPay(String remoteAddrIP,String totalPrice) throws Exception{

        JSONObject jo = new JSONObject();
        /*
        应用ID	appid
        商户号	mch_id
        随机字符串	nonce_str
        签名	sign
        商品描述	body
        商户订单号	out_trade_no
        总金额	total_fee
        终端IP	spbill_create_ip
        通知地址	notify_url
        交易类型	trade_type
         */

        SortedMap<Object, Object> parameters = PayCommonUtil.getWXPrePayID(); // 获取预付单，此处已做封装，需要工具类

//        TravelFly travelFly = new TravelFly(); // 商品对象
//        travelFly.setId(orders.getProductId());
//        travelFly = travelFlyMapper.selectById(travelFly);
//        travelFly.setBusinesser(businesserMapper.selectByPrimaryKey(travelFly.getBusinesserId()));
//        orders.setTravelFly(travelFly);
//        parameters.put("body", "xxx产品-" + travelFly.getProductName());

        parameters.put("spbill_create_ip", remoteAddrIP);
        parameters.put("out_trade_no",  PayCommonUtil.getDateStr()); // 订单id这里我的订单id生成规则是订单id+时间
        parameters.put("total_fee", totalPrice); // 测试时，每次支付一分钱，微信支付所传的金额是以分为单位的，因此实际开发中需要x100
        // parameters.put("total_fee", orders.getOrderAmount()*100+""); // 上线后，将此代码放开

        parameters.put("body","小马商城支付订单");

        // 设置签名
        String sign = PayCommonUtil.createSign("UTF-8", parameters);
        parameters.put("sign", sign);
        // 封装请求参数结束
        String requestXML = PayCommonUtil.getRequestXml(parameters); // 获取xml结果
        logger.debug("封装请求参数是：" + requestXML);
        // 调用统一下单接口
        String result = PayCommonUtil.httpsRequest(PropertyUtil.getProperty("wxpay.payUrl"), "POST",requestXML);
        logger.debug("调用统一下单接口：" + result);
        SortedMap<Object, Object> parMap = PayCommonUtil.startWXPay(result);
        logger.debug("最终的map是：" + parMap.toString());
        if (parMap != null)
        {
            jo.put("code",200);
            jo.put("data",parameters);
            jo.put("msg","SUCCESS");
        } else
        {
            jo.put("code",-999);
            jo.put("msg","支付出现异常，请稍后重试!");
        }
        return jo;
    }
}