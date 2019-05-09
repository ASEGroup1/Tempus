import request from 'superagent';

const BASE_URL = 'http://' + window.location.hostname + ':9000/';

export async function getTimetable(timetableType) {
	console.debug(BASE_URL + 'get-generated-' + timetableType + '-schedule-json')
	return await request.get(BASE_URL + 'get-generated-' + timetableType + '-schedule-json').then(res => {
		if (res.text != null) return JSON.parse(res.text);
		return res;
	}).catch(err => {
		console.error(JSON.stringify(err));
		return null;
	});
}

export async function saveTimetable(name) {
	return await request.post(BASE_URL + 'timetable/' + name)
		.then(res => {
		if (res.text != null) return JSON.parse(res.text);
		return true;
	}).catch(err => {
		console.error(JSON.stringify(err));
		return false;
	});
}
