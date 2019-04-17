import React from "react";
import Navbar from "react-bootstrap/Navbar";
import Nav from "react-bootstrap/Nav";
import {Home} from "../Home";
import {Route} from "react-router-dom";
import {Add} from "./Students/Add";
import {Edit} from "./Students/Edit";
import {ListAll} from "./Students/ListAll";
import {Delete} from "./Students/Delete";

export class Students extends React.Component {
	render() {
		return (
			<div>
				<Navbar bg="dark" variant="dark" sticky="top">
					<Navbar.Brand href="/dataManagement/students">Students</Navbar.Brand>
					<Nav className="mr-auto">
						<Nav.Link href="/dataManagement/students/add">Add</Nav.Link>
						<Nav.Link href="/dataManagement/students/edit">Edit</Nav.Link>
						<Nav.Link href="/dataManagement/students/delete">Delete</Nav.Link>
						<Nav.Link href="/dataManagement/students/listAll">List All</Nav.Link>
					</Nav>
				</Navbar>
				<div>
					<Route exact path="/" component={Home}/>
					<Route path="/dataManagement/students/add" component={Add}/>
					<Route path="/dataManagement/students/edit" component={Edit}/>
					<Route path="/dataManagement/students/delete" component={Delete}/>
					<Route path="/dataManagement/students/listAll" component={ListAll}/>
				</div>
			</div>
		);
	}
}