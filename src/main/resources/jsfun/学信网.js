
/**
* 这里是对自定义函数的详细说明
*
* @cnName 最高学历
* @funType 学信网
* @notation prefix
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
* @funType 学信网
* @notation prefix
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
* @funType 学信网
* @notation prefix
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