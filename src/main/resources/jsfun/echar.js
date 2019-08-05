/**
* 这里是对自定义函数的详细说明
*
* @cnName 公积金历史缴纳月份数
* @funType 公积金
* @notation postfix
* @param data type:jsonObject cnName:data 公积金信息
* @return {jsonObject}
*/
function latelyFundMonths(data) {
  if (data === null) {
    return 0;
  }

  var bill_record = data.bill_record;
  if (!bill_record || !bill_record.length) {
    return 0;
  }

  return bill_record.length;
}


/**
* 这里是对自定义函数的详细说明
*
* @cnName 公积金最近一次缴纳单位的缴纳月份数
* @funType 公积金
* @notation postfix
* @param data type:jsonObject cnName:data 公积金信息
* @return {jsonObject}
*/
function latelyFundCompanyMonth(data) {
  if (data === null) {
    return 0;
  }

  var bill_record = data.bill_record;
  if (!bill_record || !bill_record.length) {
    return 0;
  }

  var max = bill_record[0];

  for (var i = 0; i < bill_record.length; i++) {
    var item = bill_record[i];

    if (new Date(item.month).getTime() - new Date(max.month).getTime() > 0) {
      max = item
    }
  }

  var sumMonth = 0;

  for (var j = 0; j < bill_record.length; j++) {
    var item2 = bill_record[j];
    if (max.corporation_name == item2.corporation_name) {
      sumMonth = sumMonth + 1;
    }
  }

  return sumMonth;
}


/**
* 这里是对自定义函数的详细说明
*
* @cnName 公积金最近一次缴纳单位的平均基数
* @funType 公积金
* @notation postfix
* @param data type:jsonObject cnName:data 公积金信息
* @return {jsonObject}
*/
function latelyFundCompanyMonthMoney(data) {
  if (data === null) {
    return 0;
  }

  var bill_record = data.bill_record;
  if (!bill_record || !bill_record.length) {
    return 0;
  }

  var max = bill_record[0];

  for (var i = 0; i < bill_record.length; i++) {
    var item = bill_record[i];

    if (new Date(item.month).getTime() - new Date(max.month).getTime() > 0) {
      max = item
    }
  }

  var income = 0;
  var ratio = 0;

  for (var j = 0; j < bill_record.length; j++) {
    var item2 = bill_record[j];
    if (max.corporation_name == item2.corporation_name) {
      income = income + item2.income;
      ratio = ratio + parseFloat(item2.corporation_ratio) + parseFloat(item2.customer_ratio);
    }
  }


  if (ratio === 0) {
    return 0;
  }


  return income / ratio;
}


/**
* 这里是对自定义函数的详细说明
*
* @cnName 公积金最近一次缴存基数
* @funType 公积金
* @notation postfix
* @param data type:jsonObject cnName:data 公积金信息
* @return {jsonObject}
*/
function latelyFundMoney(data) {
  if (data === null) {
    return {};
  }

  var bill_record = data.bill_record;
  if (!bill_record || !bill_record.length) {
    return {};
  }

  var max = bill_record[0];

  for (var i = 1; i < bill_record.length; i++) {
    var item = bill_record[i];

    if (new Date(item.month).getTime() - new Date(max.month).getTime() > 0) {
      max = item
    }
  }

  return (max.income) / (parseFloat(max.corporation_ratio) + parseFloat(max.customer_ratio));
}

/**
* 这里是对自定义函数的详细说明
*
* @cnName 公积金最近一次缴纳单位
* @funType 公积金
* @notation postfix
* @param data type:jsonObject cnName:data 公积金信息
* @return {jsonObject}
*/
function latelyFundCompany(data) {
  if (data === null) {
    return "";
  }

  var bill_record = data.bill_record;
  if (!bill_record || !bill_record.length) {
    return "";
  }

  var max = bill_record[0];

  for (var i = 1; i < bill_record.length; i++) {
    var item = bill_record[i];

    if (new Date(item.month).getTime() - new Date(max.month).getTime() > 0) {
      max = item
    }
  }

  return max.corporation_name;
}



/**
* 这里是对自定义函数的详细说明
*
* @cnName 获取最近一次公积金信息
* @funType 公积金
* @notation postfix
* @param data type:jsonObject cnName:data 公积金信息
* @return {jsonObject}
*/
function latelyFund(data) {
  if (data === null) {
    return {};
  }

  var bill_record = data.bill_record;
  if (!bill_record || !bill_record.length) {
    return {};
  }

  var max = bill_record[0];

  for (var i = 1; i < bill_record.length; i++) {
    var item = bill_record[i];

    if (new Date(item.month).getTime() - new Date(max.month).getTime() > 0) {
      max = item
    }
  }

  return max;
}

/**
* 这里是对自定义函数的详细说明
*
* @cnName 风险通话行为监测
* @funType 运营商报告
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
* @cnName 是否命中法院失信人
* @funType 天行数科
* @notation postfix
* @param data type:jsonObject cnName:data 接口返回信息
* @return {boolean}
*/
function hitDishonester(data){
    var hit = false;

    if(data === null){
        return hit;
    }

    if(data.checkStatus == "NO_DATA"){
        return hit;
    }

    if(data.checkStatus == "EXIST"){
        if(data.statistic.sxggResultSize == "0"){
           return hit;
        }else{
            var pageData = data.pageData;
            for(var i = 0; i < pageData.length; i++){
                var detail =  pageData[i];
                if(detail.dataType == "SXGG"){
                    if(detail.implementationStatus != "全部已履行"){
                        hit = true;
                        break;
                    }
                }
            }
        }
    }

   return hit;

}

/**
* 这里是对自定义函数的详细说明
*
* @cnName 是否命中法院被执行人
* @funType 天行数科
* @notation postfix
* @param data type:jsonObject cnName:data 接口返回信息
* @return {boolean}
*/
function hitExecutor(data){
    var hit = false;

    if(data === null){
        return hit;
    }

    if(data.checkStatus == "NO_DATA"){
        return hit;
    }

    if(data.checkStatus == "EXIST"){
        if(data.statistic.zxggResultSize == "0"){
           return hit;
        }else{
            var pageData = data.pageData;
            for(var i = 0; i < pageData.length; i++){
                var detail =  pageData[i];
                if(detail.dataType == "ZXGG"){
                    if(detail.caseStatus == "0"){
                        hit = true;
                        break;
                    }
                }
            }
        }
    }

   return hit;

}


/**
* 这里是对自定义函数的详细说明
*
* @cnName 最高学历
* @funType 学信网函数
* @notation postfix
* @param students_educations type:jsonObject cnName:students_educations 学信网信息
* @return {jsonObject}
*/
function highest_educational(students_educations) {
  if(students_educations === null){
    return {};
  }

  var educations = students_educations.education_list;
  if (!educations || !educations.length) {
    return {};
  }

  var map = {};
  map['专科'] = 1;
  map['专科(高职)'] = 2;
  map['本科'] = 3;
  map['硕士研究生'] = 4;
  map['博士研究生'] = 5;

  var max = educations[0];

  for (var i = 1; i < educations.length; i++) {
    var item = educations[i];
    if (map[item.edu_level] - map[max.edu_level] > 0) {
      max = item;
    }
  }

  return max;
}



/**
* 这里是对自定义函数的详细说明
*
* @cnName 当前学籍信息
* @funType 学信网函数
* @notation postfix
* @param students_educations type:jsonObject cnName:students_educations 学信网信息
* @return {jsonObject}
*/
function current_student(students_educations) {
  if(students_educations === null){
    return {};
  }

  var students = students_educations.studentInfo_list;
  if (!students || !students.length) {
    return {};
  }

  var max = students[0];

  for (var i = 1; i < students.length; i++) {
    var item = students[i];

    if(new Date(item.enrollment_time).getTime() - new Date(max.enrollment_time).getTime() > 0){
       max = item
    }

  }

  return max;
}

/**
* 这里是对自定义函数的详细说明
*
* @cnName 最高学籍信息
* @funType 学信网函数
* @notation postfix
* @param students_educations type:jsonObject cnName:students_educations 学信网信息
* @return {jsonObject}
*/
function highest_student(students_educations) {
  if(students_educations === null){
    return {};
  }

  var students = students_educations.studentInfo_list;
  if (!students || !students.length) {
    return {};
  }

 var map = {};
   map['专科'] = 1;
   map['专科(高职)'] = 2;
   map['本科'] = 3;
   map['硕士研究生'] = 4;
   map['博士研究生'] = 5;

     var max = students[0];

     for (var i = 1; i < students.length; i++) {
       var item = students[i];
       if (map[item.level] - map[max.level] > 0) {
         max = item;
       }
     }

  return max;
}
