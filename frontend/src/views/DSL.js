import React from "react";
import * as NetLib from '../lib/NetworkLib.js';
import Form from "react-bootstrap/Form";
import Button from "react-bootstrap/Button";

export class DSL extends React.Component {


	render() {
		return <Constraint/>;
	}
}

// Shows Current filters
class DSLList extends React.Component {

	constructor(props) {
		super(props);
		this.state = { list: [] };
	}

	componentDidMount() {
		NetLib.get("dsl").then(res => JSON.parse(res)).then(res =>
			this.setState({list: res})
		);
	}

	render() {
		return (
			<div>
				<h2>Current Filters: </h2>
				<table align={"left"}>
					<thead>
					<tr>
						<th>Filter Name</th>
						<th>Filter Code</th>
					</tr>
					</thead>
					<tbody>
					{this.buildTable()}
					</tbody>
				</table>
		</div>
		)
	}



	buildTable(){
		let body = [];
		for (let key in this.state.list) {
			body.push(<tr>
				<td key={key+"Name"}>{key}</td>
				<td key={key+"Code"} align={"left"}>
					{this.state.list[key].split("\n").map((e, n) => {
						return <div>{e.replace("\t", "&nbsp")}</div>
					})}</td>
			</tr>);
		}

		return body;
	}
}

// Create a new Constraint
class Constraint extends React.Component{

	constructor(props){
		super(props);
		this.done = false;
		this.state = {validArgs: []}
	}

	componentDidMount() {
		NetLib.get("dsl/references").then(res => JSON.parse(res)).then(res =>
			this.setState({validArgs: res})
		);
	}

	isDone(){
		return this.done;
	}

	getConstraintText(){
		if (this.done){
			return "";
		}else {
			return null;
		}
	}

	render() {
		return this.state.validArgs.length ? <ConstraintOperation params={2} arguments={this.state.validArgs}/> : <p>Loading</p>
	}


	// Options


}


class ConstraintOperation extends React.Component{

	constructor(props){
		super(props);
		this.done = false;
		console.log(props);
		this.state = {
			option: '',
			argType: props["arguments"]["0"]["type"],
			isArg2: true,
			arg2UserInput: false,
			validated: false
		};
		this.params = props["params"];
		this.validArgs = props["arguments"];
	}

	getOperations(){
		return [
			["==", /.*/],
			["!=", /.*/],
			[">",  /float|int/],
			[">=", /float|int/],
			["<",  /float|int/],
			["<=", /float|int/],
			["||", /boolean/],
			["&&", /boolean/],
			["!", /boolean/]
		]
	}

	isDone(){
		return this.done;
	}

	getConstraintText(){
		if (this.done){
			return "";
		}else {
			return null;
		}
	}

	/**
	 * Generate a combobox.
	 * @param options, of the combobox
	 * @param formName, name of the group
	 * @param label, to display
	 * @param callback, function to be called when the combobox changes
	 */
	generateComboBox(options, formName, label, callback){
		return <Form.Group controlId={formName}>
			<Form.Label>{label}:</Form.Label>
			<Form.Control as="select" onChange={callback}>
				{options}
			</Form.Control>
		</Form.Group>
	}

	/**
	 * Generate a list of possible parameters
	 * @param userInput, if user input is valid
	 * @param formName, name of the group
	 * @param label, to display
	 * @param callback, function to be called when the combobox changes
	 */
	getParameterOption(userInput, formName, label, callback) {
		let options = [];
		options.push(<option>Param1</option>);
		if(this.params === 2){
			options.push(<option>Param2</option>);
		}
		if(userInput){
			options.push(<option>Manuel</option>);
		}

		return this.generateComboBox(options, formName, label, callback)
	}

	/**
	 * Generate a list of valid arguments
	 * @param type, to search for
	 * @param formName, name of the group
	 * @param label, to display
	 * @param callback, function to be called when the combobox changes
	 */
	getArgumentOption(type, formName, label, callback){
		let options = [];
		let args = [];
		let typeRegex = type? new RegExp(type.match(/float|int/) ? "float|int": type): /.*/;
		for (let key in this.validArgs){
			if(this.validArgs[key]["type"].match(typeRegex)){
				args.push(key)
			}
		}
		for(let arg in args){
			let t = this.validArgs[args[arg]];
			options.push(<option value = {args[arg]}>{t["reference"] + ": " + t["type"]}</option>)
		}

		return this.generateComboBox(options, formName, label, callback)
	}

	/**
	 * Generate a combobox showing the valid operations
	 * @param type, parameter type
	 * @param formName, name of the group
	 * @param label, to display
	 * @param callback, function to be called when the combobox changes
	 */
	getOperationOption(type, formName, label, callback){
		let options = [];
		let operations = this.getOperations()
		for (let o in operations){
			if (!type || type.match(operations[o][1])) {
				options.push(<option value={operations[o]}>{operations[o][0]}</option>)
			}
		}

		return this.generateComboBox(options, formName, label, callback)
	}


	getUserInputField(type, formName, label, callback){

		var regex = '';
		var placeholderText = '';
		switch (type){
			case "boolean":
				let options = [];
				options.push(<option>True</option>);
				options.push(<option>False</option>)
				return this.generateComboBox(options, formName, label, null);

			case "int":
			case "float":
				regex = "[+-]?([0-9]*[.])?[0-9]+";
				placeholderText = "1.0";
				break;
			case "char":
				regex = ".";
				placeholderText = "c";
				break;
			case "String":
				regex = ".*";
				placeholderText = "Some text";
				break;
		}

		return <Form.Group controlId={formName}>
			<Form.Label>{label}:</Form.Label>
			<Form.Control
				required
				type="text"
				pattern={regex}
				placeholder={placeholderText}
				isValid={this.state.validated}
			/>
			<Form.Control.Feedback type="invalid">
				Please specify a valid {type}.
			</Form.Control.Feedback>
		</Form.Group>
	}

	handleSubmit(event) {
		const form = event.currentTarget;
		event.preventDefault();
		if (form.checkValidity() === false) {
			event.stopPropagation();
		}
		this.setState({validated: true})
	}

	render() {
		return <Form
			noValidate
			validated={this.state.validated}
			onSubmit={e => this.handleSubmit(e)}>
			{this.getParameterOption(false, "ConstraintOperation.Parameter1", "Parameter 1", null)}
			{this.getArgumentOption(null, "ConstraintOperation.Argument1", "Argument 1", (event) => {
				// Set type of argument 1
				this.setState({argType: this.validArgs[event.target.value]["type"]})
			})}

			{this.getOperationOption(this.state.argType, "ConstraintOperation.Operation", "Operation", (event) => {
				this.setState({isArg2: event.target.value[0] != "!"})
			})}


			{this.state.isArg2?
				this.getParameterOption(true, "ConstraintOperation.Parameter2", "Parameter 2", (event) => {
					// store whether specified value is user input
					this.setState({arg2UserInput: event.target.value === "Manuel"})
				}): null}
			{this.state.isArg2 && this.state.arg2UserInput ?
				this.getUserInputField(this.state.argType, "ConstraintOperation.Argument1", "Argument 2", null)
				:this.getArgumentOption(this.state.argType, "ConstraintOperation.Argument1", "Argument 2", null)
			}
			<Button type="submit">Submit</Button>
		</Form>

	}

}