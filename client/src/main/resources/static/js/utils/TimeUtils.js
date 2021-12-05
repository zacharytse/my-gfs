//时间戳转换日期方法    date:时间戳数字
function timestampToDate(date) {
	var date = new Date(date);
	var YY = date.getFullYear() + '-';
	var MM = (date.getMonth() + 1 < 10 ? '0' + (date.getMonth() + 1) : date.getMonth() + 1) + '-';
	var DD = (date.getDate() < 10 ? '0' + (date.getDate()) : date.getDate());
	return YY + MM + DD;
}
//日期字符串转时间戳
function dateToTimestamp(date) {
	if (date.length > 0) {
		date = date.substring(0, 19);
		date = date.replace(/-/g, '/'); //必须把日期'-'转为'/'
		var timestamp = new Date(date).getTime();
		return timestamp;
	} else {
		return null;
	}
}
//时间戳转换日期+时间方法    date:时间戳数字
function timestampToDateTime(date) {
	var date = new Date(date);
	var YY = date.getFullYear() + '-';
	var MM = (date.getMonth() + 1 < 10 ? '0' + (date.getMonth() + 1) : date.getMonth() + 1) + '-';
	var DD = (date.getDate() < 10 ? '0' + (date.getDate()) : date.getDate());
	var hh = (date.getHours() < 10 ? '0' + date.getHours() : date.getHours()) + ':';
	var mm = (date.getMinutes() < 10 ? '0' + date.getMinutes() : date.getMinutes()) + ':';
	var ss = (date.getSeconds() < 10 ? '0' + date.getSeconds() : date.getSeconds());
	return YY + MM + DD + " " + hh + mm + ss;
}

/**
 * 获取日期中文格式
 */
function initDate(date) {
	var datetime = date.getFullYear() + "年" +
		((date.getMonth() + 1) > 10 ? (date.getMonth() + 1) : "0" + (date.getMonth() + 1)) + "月" +
		(date.getDate() < 10 ? "0" + date.getDate() : date.getDate()) + "日 " +
		(date.getHours() < 10 ? '0' + date.getHours() : date.getHours()) + "时" +
		(date.getMinutes() < 10 ? '0' + date.getMinutes() : date.getMinutes()) + "分" +
		(date.getSeconds() < 10 ? '0' + date.getSeconds() : date.getSeconds()) + "秒";
	return datetime;
}
