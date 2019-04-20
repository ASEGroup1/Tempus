import React from "react";
import Form from "react-bootstrap/Form";
import Row from "react-bootstrap/Row";
import Col from "react-bootstrap/Col";
import Button from "react-bootstrap/Button";
import axios from "axios";

export class Add extends React.Component {
	render() {
		return (
			<div>
				<h1>Add Student Form</h1>
				<Form className="form" onSubmit={(e) => this.submit(e)}>
					<Form.Group as={Row} controlId="studentId">
						<Form.Label column sm={2}>Student ID</Form.Label>
						<Col sm={10}>
							<Form.Control type="number"/>
						</Col>
					</Form.Group>
					<Form.Group as={Row} controlId="currentFehqLevelCompleted">
						<Form.Label column sm={2}>Current Highest FEHQ Level Completed</Form.Label>
						<Col sm={10}>
							<Form.Control type="number"/>
						</Col>
					</Form.Group>
					<Form.Group as={Row} controlId="personId">
						<Form.Label column sm={2}>Person ID</Form.Label>
						<Col sm={10}>
							<Form.Control type="number"/>
						</Col>
					</Form.Group>
					<Form.Group as={Row} controlId="firstName">
						<Form.Label column sm={2}>First Name</Form.Label>
						<Col sm={10}>
							<Form.Control type="text"/>
						</Col>
					</Form.Group>
					<Form.Group as={Row} controlId="lastName">
						<Form.Label column sm={2}>Last Name</Form.Label>
						<Col sm={10}>
							<Form.Control type="text"/>
						</Col>
					</Form.Group>
					<Form.Group as={Row} controlId="otherNames">
						<Form.Label column sm={2}>Other Name(s)</Form.Label>
						<Col sm={10}>
							<Form.Control type="text"/>
						</Col>
					</Form.Group>
					<Button type="submit" className="float-right">Add</Button>
				</Form>
			</div>
		);
	}
	
	submit(e) {
		e.preventDefault();
		let student = {
			"studentId": e.target["studentId"].value,
			"currentFehqLevelCompleted": e.target["currentFehqLevelCompleted"].value,
			"personId": e.target["personId"].value,
			"firstName": e.target["firstName"].value,
			"lastName": e.target["lastName"].value,
			"otherNames": e.target["otherNames"].value
		};
		// The following is currently throwing a cors error
		// axios.post("https://ptsv2.com/t/mv902-1555726666/post", student).then(resp => console.log(resp)).catch(err => console.error(err));
		console.log(student);
	}
}