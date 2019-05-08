import React from "react";
import Table from "react-bootstrap/Table";
import ButtonGroup from "react-bootstrap/ButtonGroup";
import Button from "react-bootstrap/Button";
import {getTimetable} from "../../RequestManager";
import Dropdown from "react-bootstrap/Dropdown";
import SelectSearch from "react-select-search";
import {ClipLoader} from "react-spinners";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";

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
			weekIndex: 1
		};
	}

	room = "1C";
	timetableType = TIMETABLE_TYPE.STUDENT

	populateTimetable = async () => {
		await this.setState({
			fullRoomTimetable: await getTimetable('room'),
			fullStudentTimetable: await getTimetable('student')
		});
		console.debug(this.state.fullRoomTimetable, this.state.fullStudentTimetable);
		this.generateSchedule(1);
	};

	newStudent = async () => {
		this.setState({fullStudentTimetable: [], timetable: []});
		await this.setState({
			fullStudentTimetable: await getTimetable('student')
		});
		this.generateSchedule(1);
	};

	newRooms = async () => {
		this.setState({fullRoomTimetable: [], timetable: []});
		await this.setState({
			fullRoomTimetable: await getTimetable('room')
		});
		this.generateSchedule(1);
	};

	generateSchedule(w) {
		this.setState({
			timetable: (this.timetableType === TIMETABLE_TYPE.ROOM ? this.state.fullRoomTimetable[this.room] : this.state.fullStudentTimetable)
				.slice((w - 1) * WEEK_LENGTH, (w - 1) * WEEK_LENGTH + WEEK_LENGTH),
			weekIndex: w
		});
		console.debug(this.state.timetable);
	};

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
		this.timetableType = type;
		this.generateSchedule(this.state.weekIndex);
	}

	render() {
		console.debug('rendered');

		return (
			(this.state.timetable.length > 0) ?
				<div>
					<h1>Timetable for {this.timetableType} {this.timetableType === TIMETABLE_TYPE.ROOM ? "- " + this.room : ""}</h1>
					<Dropdown
						style={{float: 'left', height: '800px', width: '200px', zIndex: 50, background: 'transparent'}}>
						<Dropdown.Toggle>Timetable Types</Dropdown.Toggle>

						<Dropdown.Menu>
							<Dropdown.Item onClick={() => this.swapTimetable(TIMETABLE_TYPE.ROOM)}><FontAwesomeIcon icon="building"/> &nbsp; Rooms</Dropdown.Item>
							<Dropdown.Item onClick={() => this.swapTimetable(TIMETABLE_TYPE.STUDENT)}><FontAwesomeIcon icon="user-graduate"/>&nbsp; Student</Dropdown.Item>
						</Dropdown.Menu>
					</Dropdown>

					<ButtonGroup>
						{weeks.map(w => (<Button onClick={() => this.generateSchedule(w)}>{w}</Button>))}
					</ButtonGroup>

					{this.timetableType === TIMETABLE_TYPE.ROOM ? <Dropdown
						style={{float: 'right', height: '800px', width: '200px', zIndex: 50, background: 'transparent'}}>
						<Dropdown.Toggle>Rooms</Dropdown.Toggle>
						<Dropdown.Menu>
							{Object.keys(this.state.fullRoomTimetable).map(r => <Dropdown.Item
								onClick={() => this.changeRoom(r)}>{r}</Dropdown.Item>)}
						</Dropdown.Menu>
					</Dropdown> : <Button style={{float: 'right'}} onClick={() => this.newStudent()}>Generate new student</Button>}

					{this.timetableType === TIMETABLE_TYPE.ROOM ? <Button style={{float: 'right'}} onClick={() => this.newRooms()}>New Rooms timetable</Button> : ""}
					<br/>
					<Table striped bordered hover variant="dark" style={{position: 'absolute', top: '300px'}}>
						{this.genTable()}
					</Table>
				</div> : <div>
					<br/><br/><br/><br/>
					<ClipLoader sizeUnit={"px"} size={300} color={'#123abc'} loading={true}/>
					<h1> Generating Timetable </h1>
				</div>
		);
	}
}
