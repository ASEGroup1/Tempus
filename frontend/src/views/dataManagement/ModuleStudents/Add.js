import React from "react";
import Form from "react-bootstrap/Form";
import Row from "react-bootstrap/Row";
import Col from "react-bootstrap/Col";
import Button from "react-bootstrap/Button";
import * as request from "superagent";

export class Add extends React.Component {
	render() {
		return (
			<div>
				<h1>Map Student to Module Form</h1>
				<Form className="form" onSubmit={(e) => this.submit(e)}>
					<Form.Group as={Row} controlId="studentId">
						<Form.Label column sm={2}>Student ID</Form.Label>
						<Col sm={10}>
							<Form.Control type="number"/>
						</Col>
					</Form.Group>
					<Form.Group as={Row} controlId="moduleId">
						<Form.Label column sm={2}>Module ID</Form.Label>
						<Col sm={10}>
							<Form.Control type="number"/>
						</Col>
					</Form.Group>
					<Button type="submit" className="float-right">Add</Button>
				</Form>
			</div>
		);
	}
	
	submit(e) {
		e.preventDefault();
		// The following is currently throwing a cors error
		request.put("/api/student/map/" + e.target["studentId"].value + "/" + e.target["moduleId"].value)
			.set("Accept", "application/json")
			.set("Access-Control-Allow-Origin", "*")
			.then(result => alert("Added Student\n Response: \n" + JSON.stringify(result.body)))
			.catch(error => alert("Unable to map student to course\n Response: \n" + JSON.stringify(error)));
	}
}