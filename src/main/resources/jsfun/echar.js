
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