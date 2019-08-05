
/**
* 这里是对自定义函数的详细说明
*
* @cnName 违法涉诉个人涉诉查询-A接口
* @funType 天行数科
* @notation prefix
* @param data type:jsonObject cnName:data 个人涉诉查询-A接口
* @return {jsonObject}
*/
function courtCase(data) {

    var result = {};
    var ZXGG = [];
    var SXGG = [];
    var other = [];

    if (data === null) {
        return result;
    }

    if (!data.data.pageData || !data.data.pageData.length) {
         return result;
    }


    var pageData = data.data.pageData;
    for(var i = 0; i< pageData.length; i++){
        if(pageData[i].dataType == "ZXGG"){
            var obj = {};
            obj.time =pageData[i].time;
            obj.name =pageData[i].name;
            obj.executionTarget =pageData[i].executionTarget;
            obj.court =pageData[i].court;
            obj.caseNO =pageData[i].caseNO;
            obj.caseStatus =pageData[i].caseStatus;
            ZXGG.push(obj);
        }else if(pageData[i].dataType == "SXGG"){
            var obj1 = {};
            obj1.postTime =pageData[i].postTime;
            obj1.name =pageData[i].name;
            obj1.province =pageData[i].province;
            obj1.gender =pageData[i].gender;
            obj1.evidenceCode =pageData[i].evidenceCode;
            obj1.time =pageData[i].time;
            obj1.executableUnit =pageData[i].executableUnit;
            obj1.specificCircumstances =pageData[i].specificCircumstances;
            obj1.obligations =pageData[i].obligations;
            obj1.court =pageData[i].court;
            obj1.implementationStatus =pageData[i].implementationStatus;
            SXGG.push(obj1);
        }else{
            var obj2 = {};
            obj2.time =pageData[i].time;
            obj2.dataType =pageData[i].dataType;
            obj2.caseNO =pageData[i].caseNO;
            obj2.court =pageData[i].court;
            obj2.content =pageData[i].content;
            other.push(obj2);
        }
    }

    result.zxgg = ZXGG;
    result.sxgg = SXGG;
    result.ohter = other;

    return result;
}






