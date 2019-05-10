import React from "react";
import Navbar from "react-bootstrap/Navbar";
import Nav from "react-bootstrap/Nav";
import {Home} from "../Home";
import {Route} from "react-router-dom";
import {Add} from "./Students/Add";
import {View} from "./Students/View";

export class Students extends React.Component {
	render() {
		return (
			<div>
				<Navbar bg="dark" variant="dark" sticky="top">
					<Navbar.Brand href="/dataManagement/students">Students</Navbar.Brand>
					<Nav className="mr-auto">
						<Nav.Link href="/dataManagement/students/add">Add</Nav.Link>
						<Nav.Link href="/dataManagement/students/view">View</Nav.Link>
					</Nav>
				</Navbar>
				<div>
					<Route exact path="/" component={Home}/>
					<Route path="/dataManagement/students/add" component={Add}/>
					<Route path="/dataManagement/students/view" component={View}/>
				</div>
			</div>
		);
	}
}