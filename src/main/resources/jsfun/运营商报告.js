/**
* 这里是对自定义函数的详细说明
*
* @cnName 数组长度
* @funType 数组
* @notation prefix
*  @param data type:jsonObject cnName:data 数组数据
* @return {number}
*/
function array_length(data) {

  if (data === null || data.length === 0) {
    return 0;
  }

  return data.length;
}


/**
* 这里是对自定义函数的详细说明
*
* @cnName 联系人通话时长
* @funType 运营商
* @notation prefix
*  @param data type:jsonObject cnName:data 运营商报告
@param phone type:jsonObject cnName:phone 联系人电话号码
* @return {number}
*/
function call_duration(data,phone) {

  if (data === null) {
    return 0;
  }

  if (data.collection_contact && data.collection_contact.length>0) {
    var collection_contact = data.collection_contact;
    for (var i = 0; i < collection_contact.length; i ++) {
        if(collection_contact[i].phone_num == phone){
            return collection_contact[i].call_time;
        }
    }

  }

  return 0;
}

/**
* 这里是对自定义函数的详细说明
*
* @cnName 联系人通话次数
* @funType 运营商
* @notation prefix
*  @param data type:jsonObject cnName:data 运营商报告
@param phone type:jsonObject cnName:phone 联系人电话号码
* @return {number}
*/
function call_times(data,phone) {

  if (data === null) {
    return 0;
  }

  if (data.collection_contact && data.collection_contact.length>0) {
    var collection_contact = data.collection_contact;
    for (var i = 0; i < collection_contact.length; i ++) {
        if(collection_contact[i].phone_num == phone){
            return collection_contact[i].call_cnt;
        }
    }

  }

  return 0;
}


/**
* 这里是对自定义函数的详细说明
*
* @cnName 风险通话行为监测
* @funType 运营商
* @notation prefix
*  @param data type:jsonObject cnName:data 运营商报告
* @return {Number}
*/
function callReport(data) {

  var result = {};

  if (data === null) {
    return result;
  }

  if (data.behavior_check && data.behavior_check.length>0) {
    var behavior_check = data.behavior_check;
    for (var i = 0; i < behavior_check.length; i ++) {
      if (behavior_check[i].check_point == "regular_circle") {
        result.regular_circle = behavior_check[i].score;
      } else if (behavior_check[i].check_point == "phone_used_time") {
        result.phone_used_time = behavior_check[i].score;
      } else if (behavior_check[i].check_point == "phone_silent") {
        result.phone_silent = behavior_check[i].score;
      } else if (behavior_check[i].check_point == "phone_power_off") {
        result.phone_power_off = behavior_check[i].score;
      } else if (behavior_check[i].check_point == "contact_each_other") {
        result.contact_each_other = behavior_check[i].score;
      } else if (behavior_check[i].check_point == "contact_macao") {
        result.contact_macao = behavior_check[i].score;
      } else if (behavior_check[i].check_point == "contact_110") {
        result.contact_110 = behavior_check[i].score;
      } else if (behavior_check[i].check_point == "contact_120") {
        result.contact_120 = behavior_check[i].score;
      } else if (behavior_check[i].check_point == "contact_lawyer") {
        result.contact_lawyer = behavior_check[i].score;
      } else if (behavior_check[i].check_point == "contact_court") {
        result.contact_court = behavior_check[i].score;
      } else if (behavior_check[i].check_point == "contact_loan") {
        result.contact_loan = behavior_check[i].score;
      } else if (behavior_check[i].check_point == "contact_bank") {
        result.contact_bank = behavior_check[i].score;
      } else if (behavior_check[i].check_point == "contact_credit_card") {
        result.contact_credit_card = behavior_check[i].score;
      } else if (behavior_check[i].check_point == "contact_collection") {
        result.contact_collection = behavior_check[i].score;
      } else if (behavior_check[i].check_point == "contact_night") {
        result.contact_night = behavior_check[i].score;
      }
    }
  }


  return result;

}


/**
* 这里是对自定义函数的详细说明
*
* @cnName 联系人是否命中通话记录top3
* @funType 运营商
* @notation prefix
*  @param data type:jsonObject cnName:data 运营商报告
*  @param phone type:jsonObject cnName:phone 电话号码
* @return {boolean}
*/
function top3(data, phone) {

  if (data === null || phone === null) {
    return false;
  }

  if (data.friend_circle && data.friend_circle.peer_num_top_list.length>0) {
    var peer_num_top_list = data.friend_circle.peer_num_top_list;
    for(var i = 0; i < peer_num_top_list.length; i ++){
        if(peer_num_top_list[i].key == "peer_num_top3_6m"){
            console.log(true)
            if(peer_num_top_list[i].top_item && peer_num_top_list[i].top_item.length > 0){
                var top_item = peer_num_top_list[i].top_item;
                for(var j = 0; j < top_item.length; j ++){
                console.log(top_item[j].peer_number)
                    if(top_item[j].peer_number == phone){
                        return true;
                    }
                }
            }
        }
    }

  }

  return false;
}



