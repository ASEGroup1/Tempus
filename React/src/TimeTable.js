import React, {Component} from 'react';
import superagent from 'superagent';

const BASE_URL = "http://localhost:9000/";

export class TimeTable extends Component {
  componentDidMount() {
    this.setState({timeTable: TimeTable.getTimeTimeTable()});

    console.debug(TimeTable.getTimeTimeTable());
  }

  render() {
    return (<div>Timetable</div>)
  }

  static getTimeTimeTable() {
    return superagent.get("http://localhost:9000/get-generated-schedule").withCredentials().then( res => {
      return res.body;
    }).catch(err => {
      return err;
    })
    }
}
