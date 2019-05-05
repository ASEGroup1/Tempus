import React from "react";
import Table from "react-bootstrap/Table";
import ButtonGroup from "react-bootstrap/ButtonGroup";
import Button from "react-bootstrap/Button";
import {getTimetable} from "../../RequestManager";
import Dropdown from "react-bootstrap/Dropdown";
import SelectSearch from "react-select-search";

const weeks = Array.apply(null, {length: 13}).map(Number.call, Number).splice(1);
const DAYS = ["", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday"];
const WEEK_LENGTH = 5;

const TIMETABLE_TYPE = {
	ROOM: "Room",
	STUDENT: "Student"
};

export class Timetable extends React.Component {
	constructor(props) {
		super(props);

		this.populateTimetable();
		this.state = {
			timetable: [],
			fullRoomTimetable: {},
			fullStudentTimetable: {},
			weekIndex: 1,
			timetableType: TIMETABLE_TYPE.STUDENT
		};
	}

	room = "1C";

	populateTimetable = async () => {
		await this.setState({
			fullRoomTimetable: await getTimetable('room'),
			fullStudentTimetable: await getTimetable('student')
		});
		console.debug(this.state.fullRoomTimetable, this.state.fullStudentTimetable);
		this.generateSchedule(1);
	};

	generateSchedule(w) {
		this.setState({
			timetable: (this.state.timetableType === TIMETABLE_TYPE.ROOM ? this.state.fullRoomTimetable[this.room] : this.state.fullStudentTimetable)
				.slice((w - 1) * WEEK_LENGTH, (w - 1) * WEEK_LENGTH + WEEK_LENGTH),
			weekIndex: w
		});
	}

	genTable() {
		let body = [];
		let time = 8;
		let head = [];

		for (let i = 0; i < 6; i++)
			head.push(<th>{DAYS[i]}</th>);

		let fullTable = [(<thead>
		<tr>{head}</tr>
		</thead>)];

		for (let i = 0; i <= 11; i++) {
			let rows = [];
			for (let j = 0; j <= 4; j++)
				rows.push(<td>{this.state.timetable[j][i]}</td>);

			rows.unshift(<td>{++time}:00</td>);
			body.push(<tr>{rows}</tr>);
		}

		fullTable.push(<tbody>{body}</tbody>);

		return fullTable;
	}

	changeRoom(r) {
		this.room = r;
		this.generateSchedule(this.state.weekIndex);
	}

	swapTimetable(type) {
		this.setState({timetableType: type});
	}

	render() {
		return (
			(this.state.timetable.length > 0) ?
				<div>
					<h1>Timetable for {this.state.timetableType}</h1>
					<Dropdown
						style={{float: 'left', height: '800px', width: '200px', zIndex: 50, background: 'transparent'}}>
						<Dropdown.Toggle>Timetable Types</Dropdown.Toggle>

						<Dropdown.Menu>
							<Dropdown.Item onClick={() => this.swapTimetable(TIMETABLE_TYPE.ROOM)}>Rooms</Dropdown.Item>
							<Dropdown.Item onClick={() => this.swapTimetable(TIMETABLE_TYPE.STUDENT)}>Student</Dropdown.Item>
						</Dropdown.Menu>
					</Dropdown>

					<ButtonGroup>
						{weeks.map(w => (<Button onClick={() => this.generateSchedule(w)}>{w}</Button>))}
					</ButtonGroup>

					{this.state.timetableType === TIMETABLE_TYPE.ROOM ? <Dropdown
						style={{float: 'right', height: '800px', width: '200px', zIndex: 50, background: 'transparent'}}>
						<Dropdown.Toggle>Rooms</Dropdown.Toggle>
						<Dropdown.Menu>
							{Object.keys(this.state.fullRoomTimetable).map(r => <Dropdown.Item
								onClick={() => this.changeRoom(r)}>{r}</Dropdown.Item>)}
						</Dropdown.Menu>
					</Dropdown> : ""}
					<br/>
					<Table striped bordered hover variant="dark" style={{position: 'absolute', top: '300px'}}>
						{this.genTable()}
					</Table>
				</div> : ""
		);
	}
}