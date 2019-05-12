import React, {Component} from "react";
import "./App.css";
import {Home} from "./views/Home";
import {
	Route,
	BrowserRouter as Router
} from "react-router-dom";
import {DataManagement} from "./views/DataManagement";
import {DSL} from "./views/DSL";
import {Timetables} from "./views/Timetables";
import Navbar from "react-bootstrap/Navbar";
import Nav from "react-bootstrap/Nav";
import {AddDSL} from "./views/AddDSL";
import { library } from '@fortawesome/fontawesome-svg-core'
import {faCalendarAlt, faDatabase, faUserClock, faWrench, faUserGraduate, faChalkboardTeacher, faBuilding, faSave, faTimesCircle} from '@fortawesome/free-solid-svg-icons';
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";

library.add(faCalendarAlt, faDatabase, faWrench, faUserClock, faUserGraduate, faChalkboardTeacher, faBuilding, faSave, faTimesCircle);

class App extends Component {
	render() {
		return (
			<Router>
				<div className="App">
					<Navbar bg="dark" variant="dark" sticky="top">
						<Navbar.Brand href="/"><FontAwesomeIcon icon="user-clock"/> &nbsp; Tempus</Navbar.Brand>
						<Nav className="mr-auto">
							<Nav.Link href="/dataManagement"><FontAwesomeIcon icon="database"/> &nbsp;Data Management</Nav.Link>
							<Nav.Link href="/dsl"><FontAwesomeIcon icon="wrench"/> &nbsp;Custom Constraints</Nav.Link>
							<Nav.Link href="/timetables"><FontAwesomeIcon icon="calendar-alt"/> &nbsp;Timetable</Nav.Link>
						</Nav>
					</Navbar>
					<div className="content">
						<Route exact path="/" component={Home}/>
						<Route path="/dataManagement" component={DataManagement}/>
						<Route path="/dsl" component={DSL}/>
						<Route path="/add-dsl" component={AddDSL}/>
						<Route path="/timetables" component={Timetables}/>
					</div>
				</div>
			</Router>
		);
	}
}

export default App;
