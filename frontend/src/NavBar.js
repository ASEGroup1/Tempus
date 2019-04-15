import React from "react";

export class NavBar extends React.Component {
	render() {
		return <div id="navbar">
			<ul id="navbar">
				<li><a href="/">Home</a></li>
				<li><a href="/dataManagement">Data Management</a></li>
				<li><a href="/dsl">DSL/ Timetabling Constraints</a></li>
				<li><a href="/outputs">TimeTable Outputs</a></li>
			</ul>
		</div>;
	}
}