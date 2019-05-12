import React from "react";
import Navbar from "react-bootstrap/Navbar";
import Nav from "react-bootstrap/Nav";
import {Home} from "../Home";
import {Route} from "react-router-dom";
import {Add} from "./ModuleStudents/Add";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";

export class ModuleStudentMapping extends React.Component {
	render() {
		return (
			<div>
				<Navbar bg="dark" variant="dark" sticky="top">
					<Navbar.Brand href="/dataManagement/moduleStudents"><FontAwesomeIcon icon="route"/> &nbsp; Module Student Mappings</Navbar.Brand>
					<Nav className="mr-auto">
						<Nav.Link href="/dataManagement/moduleStudents/add">Add</Nav.Link>
					</Nav>
				</Navbar>
				<div>
					<Route exact path="/" component={Home}/>
					<Route path="/dataManagement/moduleStudents/add" component={Add}/>
				</div>
			</div>
		);
	}
}