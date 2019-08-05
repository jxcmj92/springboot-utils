
/**
* 这里是对自定义函数的详细说明
*
* @cnName 获取最近一次公积金信息
* @funType 公积金
* @notation prefix
* @param data type:jsonObject cnName:data 公积金信息
* @return {jsonObject}
*/
function latelyFund(data) {

  var result = {};

  if (data === null) {
    return result;
  }

  var bill_record = data.bill_record;
  if (!bill_record || !bill_record.length) {
    return result;
  }

  var max = bill_record[0];

  for (var i = 1; i < bill_record.length; i++) {
    var item = bill_record[i];

    if (new Date(item.month).getTime() - new Date(max.month).getTime() > 0) {
      max = item
    }
  }

    var sumMonth = 0;
    var income = 0;
    var ratio = 0;
    for (var j = 0; j < bill_record.length; j++) {
      var item2 = bill_record[j];
      if (max.corporation_name == item2.corporation_name) {
        sumMonth = sumMonth + 1;
        income = income + item2.income;
        ratio = ratio + parseFloat(item2.corporation_ratio) + parseFloat(item2.customer_ratio);
      }
    }


    if (ratio === 0) {
      return 0;
    }


  result.corporation_name = max.corporation_name;
  result.base_income = (max.income) / (parseFloat(max.corporation_ratio) + parseFloat(max.customer_ratio));
  result.sumMonth = sumMonth;
  result.average_income = income / ratio;
  result.months = bill_record.length;
  return result;
}





