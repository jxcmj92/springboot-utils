
/**
* 这里是对自定义函数的详细说明
*
* @cnName 联系人是否命中通话记录top10
* @funType 运营商
* @notation prefix
* @param data type:jsonObject cnName:data 运营商原始数据
* @param phone type:jsonObject cnName:phone 电话号码
  @param length type:jsonObject cnName:length 电话号码
* @return {boolean}
*/
function top10(data, phone, length) {
    if (data === null || phone === null) {
        return false
    }

    // 创建现在的时间
    var date = new Date()
    // 获取年
    var year = date.getFullYear()
    // 获取月
    var mon = date.getMonth() + 2
    var array = []
    for (var i = 0;i < 6;i++) {
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
    var items = []

    if (data.calls && data.calls.length > 0) {
        for (var i = 0; i < data.calls.length; i++) {
            var calls = data.calls[i]
            if (array.indexOf(calls.bill_month) >= 0) {
                if (calls.items && calls.items.length > 0) {
                    var item = calls.items
                    for (var j = 0; j < item.length; j ++) {
                        items.push(item[j])
                    }
                }
            }
        }
    }

    var phone_array = []

    var obj = {}
    for (var k = 0; k < items.length; k++) {
        if (items[k].duration > 10) {
            var peer_number = items[k].peer_number
            if (obj[peer_number] === undefined) {
                obj[peer_number] = 1
                phone_array.push({
                    peer_number: peer_number,
                    counts: obj[peer_number]
                })
            } else {
                for (var kk = 0; kk < phone_array.length; kk++) {
                    var phoneitem = phone_array[kk]
                    if (phoneitem.peer_number === peer_number) {
                        var counts = obj[peer_number] += 1
                        phone_array[kk].counts = counts
                    }
                }

            }

        }
    }

    for (var m = 0; m < phone_array.length - 1; m++) {
        for (var mm = 0; mm < phone_array.length - 1 - m; mm++) {
            if (phone_array[mm].counts < phone_array[mm + 1].counts) {
                var temp = phone_array[mm]
                phone_array[mm] = phone_array[mm + 1]
                phone_array[mm + 1] = temp
            }
        }
    }


    for (var n = 0; n < phone_array.length; n++) {
        if (n < length) {
            if (phone_array[n].peer_number == phone) {
                return true
            }
        }
    }

    return false
}
