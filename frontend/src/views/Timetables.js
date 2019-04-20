import React from "react";
import Navbar from "react-bootstrap/Navbar";
import Nav from "react-bootstrap/Nav";
import {Home} from "./Home";
import {Rooms} from "./timetables/Rooms";
import {Students} from "./timetables/Students";
import {Route} from "react-router-dom";

export class Timetables extends React.Component {
	render() {
		return (
			<div>
				<Navbar bg="dark" variant="dark" sticky="top">
					<Navbar.Brand href="/timetables">Timetables</Navbar.Brand>
					<Nav className="mr-auto">
						<Nav.Link href="/timetables/rooms">Rooms</Nav.Link>
						<Nav.Link href="/timetables/students">Students</Nav.Link>
					</Nav>
				</Navbar>
				<div>
					<Route exact path="/" component={Home}/>
					<Route path="/timetables/rooms" component={Rooms}/>
					<Route path="/timetables/students" component={Students}/>
				</div>
			</div>
		);
	}
}