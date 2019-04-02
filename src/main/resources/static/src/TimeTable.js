import React, {Component} from 'react';
import request from 'superagent';

const BASE_URL = "http://localhost:9000/";

export class TimeTable extends Component {

	render() {
		return (<button onClick={this.getTimeTimeTable}>Console log timetable</button>)
	}

	async getTimeTimeTable() {
		console.log('Sent request');
		return request.get("http://localhost:9000/get-generated-schedule").withCredentials().then(res => {
			console.log(res.body)
			return res.body;
		}).catch(err => {
			console.log(err)
			return err;
		})
	}
}