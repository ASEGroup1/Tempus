import Button from "react-bootstrap/Button";
import * as request from "superagent";
import {Display} from "./Display";
import {Edit} from "./Edit";
import React from "react";

export class ViewEditContainer extends React.Component {
	constructor(props){
		super(props);
		this.state = {editingStudent: props.editingStudent};
	}
	
	render() {
		if (this.props.student == null) {
			return null;
		} else if (this.state.editingStudent) {
			return <Edit student={this.props.student}/>;
		} else {
			return (
				<div>
					<Display student={this.props.student}/>
					<Button className="float-right" onClick={() => {
						this.deleteStudent()
					}}>Delete</Button>
					<Button className="float-right" onClick={() => {
						this.editStudent()
					}}>Edit</Button>
				</div>
			);
		}
		
	}
	
	deleteStudent() {
		request.del("/api/student/" + this.props.student.studentId)
			.set("Accept", "application/json")
			.set("Access-Control-Allow-Origin", "*")
			.then(result => {
				alert("Deleted Student\n Response: \n" + JSON.stringify(result.body));
			})
			.catch(error => {
				alert("Failed To Delete Sutduent \n Response: \n" + JSON.stringify(error));
			});
		this.setState({student: null});
	}
	
	editStudent() {
		this.setState({editingStudent: true});
	}
}