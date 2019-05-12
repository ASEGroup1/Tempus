import React from "react";
import Navbar from "react-bootstrap/Navbar";
import Nav from "react-bootstrap/Nav";
import {Home} from "../Home";
import {Add} from "./Modules/Add";
import {View} from "./Modules/View";
import {Route} from "react-router-dom";

export class Modules extends React.Component {
	render() {
		return (
			<div>
				<Navbar bg="dark" variant="dark" sticky="top">
					<Navbar.Brand href="/dataManagement/modules">Modules</Navbar.Brand>
					<Nav className="mr-auto">
						<Nav.Link href="/dataManagement/modules/add">Add</Nav.Link>
						<Nav.Link href="/dataManagement/modules/view">View</Nav.Link>
					</Nav>
				</Navbar>
				<div>
					<Route exact path="/" component={Home}/>
					<Route path="/dataManagement/modules/add" component={Add}/>
					<Route path="/dataManagement/modules/view" component={View}/>
				</div>
			</div>
		);
	}
}