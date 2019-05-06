import React from "react";
import Navbar from "react-bootstrap/Navbar";
import Nav from "react-bootstrap/Nav";
import {Home} from "./Home";
import {Timetable} from "./timetables/Timetable";
import {Route} from "react-router-dom";

export class Timetables extends React.Component {
	render() {
		return (
			<div>
				<Navbar bg="dark" variant="dark" sticky="top">
					<Navbar.Brand href="/timetables">Timetables</Navbar.Brand>
				</Navbar>
				<div>
					<Route exact path="/" component={Home}/>
					<Route path="/timetables/" component={Timetable}/>
				</div>
			</div>
		);
	}
}