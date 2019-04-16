import React from "react";
import Navbar from "react-bootstrap/Navbar";
import Nav from "react-bootstrap/Nav";
import {Route} from "react-router-dom";
import {Home} from "./Home";
import {Student} from "./dataManagement/Student";
import {Module} from "./dataManagement/Module";

export class DataManagement extends React.Component {
	render() {
		return (
			<div>
			<Navbar bg="dark" variant="dark" sticky="top">
				<Navbar.Brand href="/dataManagement">Data Management</Navbar.Brand>
				<Nav className="mr-auto">
					<Nav.Link href="/dataManagement/student">Student</Nav.Link>
					<Nav.Link href="/dataManagement/module">Module</Nav.Link>
				</Nav>
			</Navbar>
				<div>
					<Route exact path="/" component={Home}/>
					<Route path="/dataManagement/student" component={Student}/>
					<Route path="/dataManagement/module" component={Module}/>
				</div>
			</div>
		);
	}
}