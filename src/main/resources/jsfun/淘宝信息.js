

/**
* 这里是对自定义函数的详细说明
*
* @cnName 联系人电话是否出现在淘宝常用收货地址中
* @funType 淘宝
* @notation prefix
* @param data type:jsonObject cnName:data 淘宝原始信息
  @param phone type:jsonObject cnName:phone 联系人电话
* @return {boolean}
*/
function taobao_phone_exit(data,phone) {
  if (data === null || phone === null) {
    return false;
  }

   if(data.address_analysis.commonly_used_address.deliver_phone){
        var deliver_phone = data.address_analysis.commonly_used_address.deliver_phone;
        for(var key in deliver_phone){
            if(deliver_phone[key] == phone){
                return true
            }
        }
   }

  return false;
}

/**
* 这里是对自定义函数的详细说明
*
* @cnName 联系人是否出现在订单收货地址中
* @funType 淘宝
* @notation prefix
* @param data type:jsonObject cnName:data 淘宝原始信息
  @param phone type:jsonObject cnName:phone 联系人电话
* @return {boolean}
*/
function taobao_phone3_exit(data,phone) {
  if (data === null || phone === null) {
    return false;
  }

   if(data.recentdeliveraddress && data.recentdeliveraddress.length > 0){
        var recentdeliveraddress = data.recentdeliveraddress;
        for(var i = 0; i < recentdeliveraddress.length; i++){
            if(recentdeliveraddress[i].deliver_mobilephone == phone){
                return true
            }
        }
    }

  return false;
}

/**
* 这里是对自定义函数的详细说明
*
* @cnName 联系人是否出现在淘宝收货地址中
* @funType 淘宝
* @notation prefix
* @param data type:jsonObject cnName:data 淘宝原始信息
  @param phone type:jsonObject cnName:phone 联系人电话
* @return {jsonObject}
*/
function taobao_phone2_exit(data,phone) {
  if (data === null || phone === null) {
    return false;
  }

   if(data.deliveraddress && data.deliveraddress.length > 0){
        var deliveraddress = data.deliveraddress;
        for(var i = 0; i < deliveraddress.length; i++){
            if(deliveraddress[i].phone_no == phone){
                return true
            }
        }
    }

  return false;
}

/**
* 这里是对自定义函数的详细说明
*
* @cnName 消费金额和笔数及订单比
* @funType 淘宝
* @notation prefix
* @param data type:jsonObject cnName:data 淘宝原始信息
  @param phone type:jsonObject cnName:phone 联系人电话
* @return {jsonObject}
*/
function taobao_consumption(data,phone) {

  var result = {};

  if (data === null || phone === null) {
    return result;
  }

  if(!data.recentdeliveraddress || !data.recentdeliveraddress.length){
    return result;
  }

  if(!data.tradedetails || !data.tradedetails.length){
    return result;
  }


    var dateArray = dateWithYM(5);
    var trade_id_array = [];
    var tradedetails = data.tradedetails;
    for(var i = 0; i < tradedetails.length; i++){
         var trade_createtime = tradedetails[i].trade_createtime;
         trade_createtime = trade_createtime.substring(0,7);
         if(dateArray.indexOf(trade_createtime) >= 0){
            if(tradedetails[i].trade_status == "TRADE_FINISHED"){
                trade_id_array.push(tradedetails[i].trade_id)
            }
        }
    }

    console.log(trade_id_array)

     var money = 0;
     var length = 0;
     var sumLength = 0;
    var recentdeliveraddress = data.recentdeliveraddress;
    for(var i = 0; i < recentdeliveraddress.length; i++){
         if(trade_id_array.indexOf(recentdeliveraddress[i].trade_id) >= 0){
            if(recentdeliveraddress[i].deliver_mobilephone == phone){
                money = money + tradedetails[i].actual_fee * 100;
                length += 1;
            }

            sumLength += 1;
        }
    }


    if(length > 0){
        result.sumMoney = money;
        result.sumNumber = length;
        result.singleMoney = money / length;
        result.singleNumber = length / 5;
        result.ratio = (length / sumLength).toFixed(4) * 100 + "%";
    }

  return result;
}

function dateWithYM(month){
    // 创建现在的时间
    var date = new Date()
    // 获取年
    var year = date.getFullYear()
    // 获取月
    var mon = date.getMonth() + 1;
    // 获取日
    var day = date.getDate();

    if(day >= 20){
        mon += 1;
    }

    var array = []
    for (var i = 0;i < month;i++) {
        mon = mon - 1
        if (mon <= 0) {
            year = year - 1
            mon = mon + 12
        }
        if (mon < 10) {
            mon = '0' + mon
        }

        array[i] = year + '-' + mon
    }

    return array;

}




