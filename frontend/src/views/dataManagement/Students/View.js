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
		this.state = {student: null, editingStudent: false};
		
	}
	
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
					<Button type="submit" className="float-right">View</Button>
				</Form>
				<ViewEditContainer student={this.state.student} editingStudent={this.state.editingStudent}/>
			</div>
		);
	}
	
	
	submit(e) {
		e.preventDefault();
		let rtnStudent = {
			"studentId": "1",
			"currentFehqLevelCompleted": "2",
			"personId": "3",
			"firstName": "4",
			"lastName": "5",
			"otherNames": "6"
		};
		
		request.get("http://localhost:9000/api/student/" + e.target["studentId"].value)
			.set("Accept", "application/json")
			.set("Access-Control-Allow-Origin", "*")
			.then(result => {
				alert("Got Student\n Response: \n" + JSON.stringify(result.body))
			});
		
		this.setState({student: rtnStudent, editingStudent: false});
		console.log(this.state.student);
		
		
	}

}