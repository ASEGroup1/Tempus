import React from "react";
import Form from "react-bootstrap/Form";
import Row from "react-bootstrap/Row";
import Col from "react-bootstrap/Col";
import Button from "react-bootstrap/Button";
import * as request from "superagent";
import {ViewEditContainer} from "./ViewEditContainer";

export class View extends React.Component {
	constructor(props) {
		super(props);
		this.state = {module: null, editingModule: false};
		
	}
	
	render() {
		return (
			<div>
				<h1>View Module Form</h1>
				<Form className="form" onSubmit={(e) => this.submit(e)}>
					<Form.Group as={Row} controlId="moduleId">
						<Form.Label column sm={2}>Module ID</Form.Label>
						<Col sm={10}>
							<Form.Control type="number"/>
						</Col>
					</Form.Group>
					<Button type="submit" className="float-right">View</Button>
				</Form>
				<ViewEditContainer module={this.state.module} editingModule={this.state.editingModule}/>
			</div>
		);
	}
	
	
	submit(e) {
		e.preventDefault();
		let rtnModule = {
			"moduleId": 1,
			"moduleCode": "a",
			"moduleName": "a",
			"moduleDescription": "a",
			"terms":[1,2]
		};
		
		request.get("http://localhost:9000/api/module/" + e.target["moduleId"].value)
			.set("Accept", "application/json")
			.set("Access-Control-Allow-Origin", "*")
			.then(result => alert("Got Module\n Response: \n" + JSON.stringify(result.body)))
			.catch(error => alert("Unable to Get module\nResponse: \n"+JSON.stringify(error)));
		
		this.setState({module: rtnModule, editingModule: false});
		
		
	}

}