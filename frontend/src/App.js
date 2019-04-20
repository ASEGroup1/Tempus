import React, {Component} from "react";
import "./App.css";
import {Home} from "./views/Home";
import {
	Route,
	BrowserRouter as Router
} from "react-router-dom";
import {DataManagement} from "./views/DataManagement";
import {DSL} from "./views/DSL";
import {Output} from "./views/Output";
import Navbar from "react-bootstrap/Navbar";
import Nav from "react-bootstrap/Nav";


class App extends Component {
	render() {
		return (
			<Router>
				<div className="App">
					<Navbar bg="dark" variant="dark" sticky="top">
						<Navbar.Brand href="/">Tempus</Navbar.Brand>
						<Nav className="mr-auto">
							<Nav.Link href="/dataManagement">Data Management</Nav.Link>
							<Nav.Link href="/dsl">DSL/Timetabling Constraints</Nav.Link>
							<Nav.Link href="/outputs">TimeTable Outputs</Nav.Link>
						</Nav>
					</Navbar>
					<div className="content">
						<Route exact path="/" component={Home}/>
						<Route path="/dataManagement" component={DataManagement}/>
						<Route path="/dsl" component={DSL}/>
						<Route path="/outputs" component={Output}/>
					</div>
				</div>
			</Router>
		);
	}
}

export default App;
