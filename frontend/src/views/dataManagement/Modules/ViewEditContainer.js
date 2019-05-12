import Button from "react-bootstrap/Button";
import * as request from "superagent";
import {Display} from "./Display";
import {Edit} from "./Edit";
import React from "react";

export class ViewEditContainer extends React.Component {
	constructor(props){
		super(props);
		this.state = {editingModule: props.editingModule};
	}
	
	render() {
		if (this.props.module == null) {
			return null;
		} else if (this.state.editingModule) {
			return <Edit module={this.props.module}/>;
		} else {
			return (
				<div>
					<Display module={this.props.module}/>
					<Button className="float-right" onClick={() => {
						this.deleteModule()
					}}>Delete</Button>
					<Button className="float-right" onClick={() => {
						this.editModule()
					}}>Edit</Button>
				</div>
			);
		}
		
	}
	
	deleteModule() {
		request.del("/api/module/" + this.props.module.moduleId)
			.set("Accept", "application/json")
			.set("Access-Control-Allow-Origin", "*")
			.then(result => {
				alert("Deleted Module\n Response: \n" + JSON.stringify(result.body));
			})
			.catch(error => {
				alert("Failed To Delete Module \n Response: \n" + JSON.stringify(error));
			});
		this.setState({module: null});
	}
	
	editModule() {
		this.setState({editingModule: true});
	}
}