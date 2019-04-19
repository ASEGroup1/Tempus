import React from "react";
import Form from "react-bootstrap/Form";
import Row from "react-bootstrap/Row";
import Col from "react-bootstrap/Col";
import Button from "react-bootstrap/Button";

export class Add extends React.Component {
	render() {
		return (
			<div>
				<h1>Add Student Form</h1>
				<Form>
					<Form.Group as={Row} controlID="studentID">
						<Form.Label column sm={2}>Student ID</Form.Label>
						<Col sm={10}>
						<Form.Control type="number"/>
						</Col>
					</Form.Group>
					<Form.Group as={Row} controlID="currentFehqLevelCompleted">
						<Form.Label column sm={2}>Current Highest FEHQ Level Completed</Form.Label>
						<Col sm={10}>
							<Form.Control type="number"/>
						</Col>
					</Form.Group>
					<Form.Group as={Row} controlID="personID">
						<Form.Label column sm={2}>Person ID</Form.Label>
						<Col sm={10}>
							<Form.Control type="number"/>
						</Col>
					</Form.Group>
					<Form.Group as={Row} controlID="firstName">
						<Form.Label column sm={2}>First Name</Form.Label>
						<Col sm={10}>
							<Form.Control type="text"/>
						</Col>
					</Form.Group>
					<Form.Group as={Row} controlID="lastName">
						<Form.Label column sm={2}>Last Name</Form.Label>
						<Col sm={10}>
							<Form.Control type="text"/>
						</Col>
					</Form.Group>
					<Form.Group as={Row} controlID="otherNames">
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
}