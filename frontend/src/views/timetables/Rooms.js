import React from "react";
import Table from "react-bootstrap/Table";
import ButtonGroup from "react-bootstrap/ButtonGroup";
import Button from "react-bootstrap/Button";
import {getTimetable} from "../../RequestManager";

const weeks = Array.apply(null, {length: 13}).map(Number.call, Number).splice(1);
const DAYS = ["", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday"];
const WEEK_LENGTH = 5;

export class Rooms extends React.Component {

	fullTimetable = [];

	constructor(props) {
		super(props);

		this.populateRoomTimetable();
		this.state = {timetable: [], fullTimetable: {}, weekIndex: 1};
	}

	populateRoomTimetable = async () => {
		await this.setState({fullTimetable: await getTimetable()});
		console.debug(this.state.fullTimetable);
		this.generateSchedule(1);
	};

	generateSchedule(w) {
		//avoids splice changing values
		let fullTimeTable = this.state.fullTimetable;

		this.setState({
			timetable: fullTimeTable["1B2"].splice((this.state.weekIndex - 1) * WEEK_LENGTH, WEEK_LENGTH),
			weekIndex: w
		});
		console.debug(this.state.timetable)
	}

	genTable() {
		let body = [];
		let time = 8;
		let head = [];

		for (let i = 0; i < 6; i++)
			head.push(<th>{DAYS[i]}</th>);

		let fullTable = [(<thead><tr>{head}</tr></thead>)];

		for (let i = 0; i <= 11; i++) {
			let rows = [];
			for (let j = 0; j <= 4; j++) {
				rows.push((j !== 0) ? <td>{this.state.timetable[j][i]}</td> : <td>{++time}:00</td>);
			}


			body.push(<tr>{rows}</tr>);
		}

		fullTable.push(<tbody>{body}</tbody>);

		return fullTable;
	}

	render() {
		return (
			(Object.keys(this.state.timetable).length > 0) ?
				<div>
					<p>Timetable - Rooms</p>
					<ButtonGroup>
						{weeks.map(w => (<Button onClick={() => this.generateSchedule(w)}>{w}</Button>))}
					</ButtonGroup>
					<br/>
					<Table striped bordered hover variant="dark">
						{this.genTable()}
					</Table>
				</div> : ""
		);
	}
}