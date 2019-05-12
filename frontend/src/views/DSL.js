import React from "react";
import * as NetLib from '../RequestManager.js';
import Button from "react-bootstrap/Button";
import {DSLDelete} from "./DSL/DSLDelete.js";
import {DSLUpload} from "./DSL/DSLUpload.js";
import Table from "react-bootstrap/Table";

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
					<p>
						<h2>DSL/Timetabling Constraints</h2>
						<p>Current Filters:</p>
						<p><Button onClick = {() => {this.setState({addRedirect: true})}}><h3> Add New Filter </h3></Button></p>
						{this.state.list? <Table striped bordered hover variant="dark" style={{fontSize: '16pt'}}>
							<thead>
								<tr>
									<th>Filter Name</th>
									<th>Filter Code</th>
								</tr>
							</thead>
							<tbody>
								{this.buildTable()}
							</tbody>
						</Table>
						:
							"Loading"
						}
					</p>
                </div>
                <div style={{position: 'absolute', left: '35%'}}>
					<DSLDelete callback = {() => this.update()}/>
                    <DSLUpload callback = {() => this.update()}/>
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