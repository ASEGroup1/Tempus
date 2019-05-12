import React from "react";
import Form from "react-bootstrap/Form";
import Row from "react-bootstrap/Row";
import Col from "react-bootstrap/Col";
import Button from "react-bootstrap/Button";
import * as request from "superagent";

export class Edit extends React.Component {
	render() {
		return (
			<div>
				<h2>Edit Student</h2>
				<Form className="form" onSubmit={(e) => this.submit(e)}>
					<Form.Group as={Row} controlId="studentId">
						<Form.Label column sm={2}>Student ID</Form.Label>
						<Col sm={10}>
							<Form.Control type="number" defaultValue={this.props.student.studentId}/>
						</Col>
					</Form.Group>
					<Form.Group as={Row} controlId="currentFehqLevelCompleted">
						<Form.Label column sm={2}>Current Highest FEHQ Level Completed</Form.Label>
						<Col sm={10}>
							<Form.Control type="number" defaultValue={this.props.student.currentFehqLevelCompleted}/>
						</Col>
					</Form.Group>
					<Form.Group as={Row} controlId="personId">
						<Form.Label column sm={2}>Person ID</Form.Label>
						<Col sm={10}>
							<Form.Control type="number" defaultValue={this.props.student.personId}/>
						</Col>
					</Form.Group>
					<Form.Group as={Row} controlId="firstName">
						<Form.Label column sm={2}>First Name</Form.Label>
						<Col sm={10}>
							<Form.Control type="text" defaultValue={this.props.student.firstName}/>
						</Col>
					</Form.Group>
					<Form.Group as={Row} controlId="lastName">
						<Form.Label column sm={2}>Last Name</Form.Label>
						<Col sm={10}>
							<Form.Control type="text" defaultValue={this.props.student.lastName}/>
						</Col>
					</Form.Group>
					<Form.Group as={Row} controlId="otherNames">
						<Form.Label column sm={2}>Other Name(s)</Form.Label>
						<Col sm={10}>
							<Form.Control type="text" defaultValue={this.props.student.otherNames}/>
						</Col>
					</Form.Group>
					<Button type="submit" className="float-right">Save</Button>
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
		request.post("/api/student/")
			.send(student)
			.set("Accept", "application/json")
			.set("Access-Control-Allow-Origin", "*")
			.then(result => {
				alert("Saved Student\n Response: \n" + JSON.stringify(result.body))
			});
		console.log(student);
		alert("Student Saved");
		window.location = "/dataManagement/students/view";
		
	}
	
}