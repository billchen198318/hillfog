function lastDay(y, m) {
	return new Date(y, m+1, 0).getDate();
}

function isDate(dateStr) {
    return (new Date(dateStr) !== "Invalid Date") && !isNaN(new Date(dateStr));
}

function getStartDate(freq, dateStr) {
	if (null == dateStr || dateStr.length != 10) {
		return '';
	}
	if (!isDate(dateStr)) {
		return '';
	}
	var year = parseInt(dateStr.substring(0, 4));
	var mon = parseInt(dateStr.substring(5, 7));
	var day = parseInt(dateStr.substring(8, 10));
	if ('6' == freq || '5' == freq || '4' == freq) {
		return dateStr.substring(0, 4) + '-01-01';
	}
	if ('3' == freq) {
		return dateStr.substring(0, 7) + '-01';
	}
	if ('2' == freq) {
		var dayStr = '01';
		if (day >= 1 && day < 8 ) {
			dayStr = '01';
		}
		if (day >= 8 && day < 15 ) {
			dayStr = '08';
		}
		if (day >= 15 && day < 22 ) {
			dayStr = '15';
		}
		if (day >= 22 ) {
			dayStr = '22';
		}
		return dateStr.substring(0, 7) + '-' + dayStr;
	}
	return dateStr;
}

function getEndDate(freq, dateStr) {
	if (null == dateStr || dateStr.length != 10) {
		return '';
	}
	if (!isDate(dateStr)) {
		return '';
	}	
	var year = parseInt(dateStr.substring(0, 4));
	var mon = parseInt(dateStr.substring(5, 7));	
	var day = parseInt(dateStr.substring(8, 10));
	if ('6' == freq || '5' == freq || '4' == freq) {
		return dateStr.substring(0, 4) + '-12-' + lastDay( year,  11 );
	}
	if ('3' == freq) {
		return dateStr.substring(0, 7) + '-' + lastDay( year,  mon-1 );
	}
	if ('2' == freq) {
		var dayStr = '28';
		if (day >= 1 && day < 8) {
			dayStr = '07';
		}
		if (day >= 8 && day < 15) {
			dayStr = '14';
		}
		if (day >= 15 && day < 22) {
			dayStr = '21';
		}
		if (day >= 22) {
			dayStr = lastDay( year,  mon-1 ) + '';
		}
		return dateStr.substring(0, 7) + '-' + dayStr;
	}
	return dateStr;
}
