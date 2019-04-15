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
import {NavBar} from "./NavBar";
import {SideBar} from "./SideBar";
import {Banner} from "./Banner"

class App extends Component {
	render() {
		return (
			<Router>
				<div className="App">

					<Banner></Banner>
					<div id="outer-container">
						<SideBar></SideBar>
						<div className="content">
							<Route exact path="/" component={Home}/>
							<Route path="/dataManagement" component={DataManagement}/>
							<Route path="/dsl" component={DSL}/>
							<Route path="/outputs" component={Output}/>
						</div>
					
					</div>
				</div>
			</Router>
		);
	}
}

export default App;
