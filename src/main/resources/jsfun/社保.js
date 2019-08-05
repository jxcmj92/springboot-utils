
/**
* 这里是对自定义函数的详细说明
*
* @cnName 获取最近一次社保信息
* @funType 社保
* @notation prefix
* @param data type:jsonObject cnName:data 社保信息
* @return {jsonObject}
*/
function latelySecurity(data) {

  var result = {};

  if (data === null) {
    return result;
  }

  var insurance_record = data.insurance_record;
  if (!insurance_record || !insurance_record.length) {
    return result;
  }

  var max = insurance_record[0];

  for (var i = 1; i < insurance_record.length; i++) {
    var item = insurance_record[i];
     if(item.insurance_type == "养老保险"){
        if (new Date(item.month).getTime() - new Date(max.month).getTime() > 0) {
          max = item
        }
     }
  }

  result.corporation_name = max.corporation_name;
  result.base_number = max.base_number / 100;

   var sumMonth = 0;
   var base_number = 0;
    var length = 0;

    for (var j = 0; j < insurance_record.length; j++) {
      var item2 = insurance_record[j];
      if (max.corporation_name == item2.corporation_name) {
        base_number = base_number + item2.base_number;
        length = length + 1;
        sumMonth = sumMonth + 1;
      }
    }


    if (length === 0) {
      return 0;
    }

    result.sumMonth = sumMonth;
    result.average_base_number = base_number / (length * 100);

  return result;
}





