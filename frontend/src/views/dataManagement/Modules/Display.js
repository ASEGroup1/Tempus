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
						 <td>Module ID: </td>
						 <td>{this.props.module.moduleId}</td>
					 </tr>
					 <tr>
						 <td>Module Code: </td>
						 <td>{this.props.module.moduleCode}</td>
					 </tr>
					 <tr>
						 <td>Module Name: </td>
						 <td>{this.props.module.moduleName}</td>
					 </tr>
					 <tr>
						 <td>Module Description: </td>
						 <td>{this.props.module.moduleDescription}</td>
					 </tr>
					 <tr>
						 <td>Terms: </td>
						 <td>{this.props.module.terms}</td>
					 </tr>
					</tbody>
				</Table>
			</div>
		);
	}
}