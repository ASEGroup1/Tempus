import React, {Component} from "react";
import "./App.css";
import {Home} from "./views/Home"
import {
	Route,
	NavLink,
	BrowserRouter as Router
} from "react-router-dom";
import {DataManagement} from "./views/DataManagement";
import {DSL} from "./views/DSL";
import {Output} from "./views/Output";

class App extends Component {
	render() {
		return (
			<Router>
				<div className="App">
					<h1 align="left" style={{marginLeft: '12px'}}>Tempus</h1>
					<ul className="header">
						<li><a href="/">Home</a></li>
						<li><a href="/dataManagement">Data Management</a></li>
						<li><a href="/dsl">DSL/Timetabling Constraints</a></li>
						<li><a href="/outputs">View Timetables</a></li>
					</ul>
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
