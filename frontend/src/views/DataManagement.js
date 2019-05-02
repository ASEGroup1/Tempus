import React from "react";
import Navbar from "react-bootstrap/Navbar";
import Nav from "react-bootstrap/Nav";
import {Route} from "react-router-dom";
import {Home} from "./Home";
import {Students} from "./dataManagement/Students";
import {Modules} from "./dataManagement/Modules";

export class DataManagement extends React.Component {
	render() {
		return (
			<div>
			<Navbar bg="dark" variant="dark" sticky="top">
				<Navbar.Brand href="/dataManagement">Data Management</Navbar.Brand>
				<Nav className="mr-auto">
					<Nav.Link href="/dataManagement/students">Students</Nav.Link>
					<Nav.Link href="/dataManagement/modules">Modules</Nav.Link>
				</Nav>
			</Navbar>
				<div>
					<Route exact path="/" component={Home}/>
					<Route path="/dataManagement/students" component={Students}/>
					<Route path="/dataManagement/modules" component={Modules}/>
				</div>
			</div>
		);
	}
}