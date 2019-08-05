

/**
* 这里是对自定义函数的详细说明
*
* @cnName 本人为收件人的订单数量排名
* @funType 淘宝
* @notation prefix
* @param data type:jsonObject cnName:data 淘宝报告
  @param phone type:jsonObject cnName:phone 联系人电话
* @return {Number}
*/
function taobao_order_top(data,phone) {
  if (data === null || phone === null) {
    return 0;
  }

   if(data.address_analysis.commonly_used_address.deliver_phone){
        var deliver_phone = data.address_analysis.commonly_used_address.deliver_phone;
        var top = 1;
        for(var key in deliver_phone){
            if(deliver_phone[key] == phone){
                return top;
            }
            top += 1;
        }
   }

  return 0;
}










