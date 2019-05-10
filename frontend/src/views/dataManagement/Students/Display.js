import React from "react";
import Table from "react-bootstrap/Table";

export class Display extends React.Component {
	render() {
		return (
			<div className="table">
				<Table striped bordered hover variant="dark">
					<thead>
					<tr>
						<th>Property Name</th>
						<th>Value</th>
					</tr>
					</thead>
					<tbody>
					 <tr>
						 <td>Student ID: </td>
						 <td>{this.props.student.studentId}</td>
					 </tr>
					 <tr>
						 <td>Current Highest FEHQ Level Completed: </td>
						 <td>{this.props.student.currentFehqLevelCompleted}</td>
					 </tr>
					 <tr>
						 <td>Person ID: </td>
						 <td>{this.props.student.personId}</td>
					 </tr>
					 <tr>
						 <td>First Name: </td>
						 <td>{this.props.student.firstName}</td>
					 </tr>
					 <tr>
						 <td>Last Name: </td>
						 <td>{this.props.student.lastName}</td>
					 </tr>
					 <tr>
						 <td>Other Name(s): </td>
						 <td>{this.props.student.otherNames}</td>
					 </tr>
					</tbody>
				</Table>
			</div>
		);
	}
}