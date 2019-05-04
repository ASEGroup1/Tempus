import request from 'superagent';

const BASE_URL = 'http://localhost:9000/';

export async function getTimetable() {
	return await request.get(BASE_URL + 'get-generated-student-schedule-json').then(res => {
		if (res.text != null) return JSON.parse(res.text);
		return res;
	}).catch(err => {
		console.error(JSON.stringify(err));
		return null;
	});
}