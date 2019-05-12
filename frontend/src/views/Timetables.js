import React from "react";
import Navbar from "react-bootstrap/Navbar";
import {Home} from "./Home";
import {Timetable} from "./timetables/Timetable";
import {Route} from "react-router-dom";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";

export class Timetables extends React.Component {
	render() {
		return (
			<div>
				<Navbar bg="dark" variant="dark" sticky="top">
					<Navbar.Brand href="/timetables"><FontAwesomeIcon icon="calendar-alt"/> &nbsp; Timetables</Navbar.Brand>
				</Navbar>
				<div>
					<Route exact path="/" component={Home}/>
					<Route path="/timetables/" component={Timetable}/>
				</div>
			</div>
		);
	}
}
