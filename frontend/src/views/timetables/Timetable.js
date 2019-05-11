import React from "react";
import Table from "react-bootstrap/Table";
import {Modal, Button, ButtonGroup, InputGroup, FormControl} from "react-bootstrap"
import {getTimetable, loadTimetable, saveTimetable, getTimetableNames} from "../../RequestManager";
import Dropdown from "react-bootstrap/Dropdown";
import SelectSearch from "react-select-search";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";

const weeks = Array.apply(null, {length: 13}).map(Number.call, Number).splice(1);
const DAYS = ["", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday"];
const WEEK_LENGTH = 5;

const TIMETABLE_TYPE = {
	ROOM: "Room",
	STUDENT: "Student"
};

const MODAL_STATES = {
	NONE: null,
	SAVE: 'save',
	LOAD: 'load'
};

export class Timetable extends React.Component {
	constructor(props) {
		super(props);
		this.handleChange.bind(this);
		this.populateTimetable(null);
		this.state = {
			timetable: [],
			fullRoomTimetable: {},
			fullStudentTimetable: {},
			weekIndex: 1,
			timetableType: TIMETABLE_TYPE.ROOM,
			modal: MODAL_STATES.NONE,
			loaded: false
		};
	}

	room = "1C";
	newTimetableName = "TEST";

	populateTimetable = async (name) => {
		this.setState({timetable: []})
		if(name !== null) {
			console.debug(name)
			await this.setState({fullRoomTimetable: await loadTimetable(name)});
		} else await this.setState({
			fullRoomTimetable: await getTimetable('room'),
			fullStudentTimetable: await getTimetable('student'),
			timetableNames: await getTimetableNames()
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

	handleChange(event) {
		this.setState({newTimetableName: event.target.value});
	}

	addTimetable = async (name) => {
		await saveTimetable(name);
		await this.setState({timetableNames: await getTimetableNames()});
		this.setState({modal: MODAL_STATES.NONE, loaded: false});
	}

	saveTimetableModal() {
		return (<Modal show={this.state.modal === MODAL_STATES.SAVE} >
          <Modal.Header closeButton>
            <Modal.Title>Save Timetable</Modal.Title>
          </Modal.Header>
          <Modal.Body>Save the current permutation of the timetable, for all rooms?
	          <br/><br/>
	          <InputGroup className="mb-3">
		          <InputGroup.Prepend>
			          <InputGroup.Text id="basic-addon1">Name</InputGroup.Text>
		          </InputGroup.Prepend>
		          <FormControl
			          ref={(inputRef) => {this.newTimetableName = inputRef}}
			          placeholder="Timetable Name"
			          aria-describedby="basic-addon1"
		          />
	          </InputGroup>
	          </Modal.Body>
          <Modal.Footer>
	          <Button variant="primary" onClick={() => this.setState({modal: MODAL_STATES.NONE})}>Close</Button>
	          <Button variant="primary" onClick={() => this.addTimetable(this.newTimetableName.value)}><FontAwesomeIcon icon="save"/>&nbsp; Save Timetable</Button>
          </Modal.Footer>
        </Modal>)
	}

	render() {
		return (
			(this.state.timetable.length > 0) ?
				<div>
					{this.saveTimetableModal()}
					<h1>Timetable for {this.state.timetableType} {this.state.timetableType === TIMETABLE_TYPE.ROOM ? "- " + this.room : ""}</h1>
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
					<Button style={{float: 'right'}} onClick={() => this.setState({modal: MODAL_STATES.SAVE})}>Save Timetable</Button>
					<Dropdown
						style={{float: 'right', height: '800px', width: '200px', zIndex: 50, background: 'transparent'}}>
						<Dropdown.Toggle>Load Timetable</Dropdown.Toggle>
						<Dropdown.Menu>
							{this.state.timetableNames.map(name => <Dropdown.Item
								onClick={() => this.populateTimetable(name)}>{name}</Dropdown.Item>)}
						</Dropdown.Menu>
					</Dropdown>
					<br/>
					<Table striped bordered hover variant="dark" style={{position: 'absolute', top: '300px'}}>
						{this.genTable()}
					</Table>
				</div> : ""
		);
	}
}
