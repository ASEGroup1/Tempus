import {NavBar} from "./NavBar";
import React from "react";

export class Banner extends React.Component {
	render() {
		return <div id="banner">
					<h1 id="logo">Tempus</h1>
					<NavBar/>
				</div>;
	}
}