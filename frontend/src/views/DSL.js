import React from "react";
import * as NetLib from '../lib/NetworkLib.js';
import Button from "react-bootstrap/Button";
import {DSLDelete} from "./DSL/DSLDelete.js";
import {DSLUpload} from "./DSL/DSLUpload.js";

import {Redirect} from "react-router-dom";

// Shows Current filters
export class DSL extends React.Component {

	constructor(props) {
		super(props);
		this.state = { list: null,
			addRedirect: false
		};
	}

	componentDidMount() {
		this.update()
	}

	update(){
		NetLib.get("dsl").then(res => JSON.parse(res)).then(res =>
			this.setState({list: res})
		);
	}

	render() {
		return (
			<div>

				{this.state.addRedirect? <Redirect to={"/add-dsl"} /> : null}
				<div>
                    <h2>Current Filters: </h2>
					{this.state.list? <table align={"left"}>
						<thead>
						<tr>
							<th>Filter Name</th>
							<th>Filter Code</th>
						</tr>
						</thead>
						<tbody>
						{this.buildTable()}
						</tbody>
					</table>
					:
						"Loading"
					}
                    <Button onClick = {() => {this.setState({addRedirect: true})}}> Add DSL </Button>
                </div>
                <div>
                    <DSLUpload callback = {() => this.update()}/>
                </div>
                <div>
                    <DSLDelete callback = {() => this.update()}/>
                </div>
		</div>
		)
	}

	buildTable(){
		let body = [];
		for (let key in this.state.list) {
			body.push(<tr>
				<td key={key+"Name"}>{key}</td>
				<td key={key+"Code"} align={"left"}>
					{this.state.list[key].split("\n").map((e, n) => {
						return <div>{e.replace("\t", "&nbsp")}</div>
					})}</td>
			</tr>);
		}

		return body;
	}
}