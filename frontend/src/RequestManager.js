import request from 'superagent';

const BASE_URL = 'http://' + window.location.hostname + ':9000/';

export async function getTimetable(timetableType) {
	return await request.get(BASE_URL + 'get-generated-' + timetableType + '-schedule-json').then(res => {
		if (res.text != null) return JSON.parse(res.text);
		return res;
	}).catch(err => {
		console.error(JSON.stringify(err));
		return null;
	});
}

export async function getTimetableNames() {
	return await request.get(BASE_URL + 'timetable-names').then(res => {
		if (res.text != null) return JSON.parse(res.text);
		return res;
	}).catch(err => {
		console.error(JSON.stringify(err));
		return null;
	});
}

export async function loadTimetable(name) {
	console.debug(BASE_URL + 'timetable/' + name)
	return await request.get(BASE_URL + 'timetable/' + name).then(res => {
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

export async function post(extension, data) {
	return await request.post(BASE_URL + extension)
		.send(data)
		.then(res => {
			if (res.body != null) {
				return res.body;
			} else {
				return res;
			}
		}).catch(err => {
			return null;
		});
}

export async function get(extension) {
	return await request.get(BASE_URL + extension).then(res => {
		if (res.text != null) {
			return res.text;
		} else {
			return res;
		}
	}).catch(err => {
		return null;
	});
}
