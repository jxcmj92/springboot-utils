/**
* 这里是对自定义函数的详细说明
*
* @cnName 通过key获取value
* @funType 数组
* @notation prefix
*  @param data type:jsonObject cnName:data 数组数据
 @param data type:jsonObject cnName:data key1
  @param data type:jsonObject cnName:data value1
   @param data type:jsonObject cnName:data key2
* @return {number}
*/
function getValueByKey(array, key1, value1, key2) {

  if (array === null || array.length === 0) {
    return 0;
  }

  for (var i = 0; i < array.length; i ++) {
      if(array[i][key1] == value1){
          if(array[i][key2]){
             return array[i][key2];
          }
      }
  }

  return 0;
}
