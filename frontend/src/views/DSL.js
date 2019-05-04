import React from "react";
import * as NetLib from '../lib/NetworkLib.js';
import Form from "react-bootstrap/Form";
import Button from "react-bootstrap/Button";
import {AddDSL} from "./AddDSL.js";
import {Redirect} from "react-router-dom";

export class DSL extends React.Component {


	render() {
		return <DSLList/>;
	}
}

// Shows Current filters
class DSLList extends React.Component {

	constructor(props) {
		super(props);
		this.state = { list: [],
			addRedirect: false
		};
	}

	componentDidMount() {
		this.update()
	}

	update(){
		NetLib.get("dsl").then(res => JSON.parse(res)).then(res =>
			this.setState({list: res})
		);
	}

	render() {
		return (
			<div>

				{this.state.addRedirect? <Redirect to={"/add-dsl"} /> : null}
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
                    <Button onClick = {() => {this.setState({addRedirect: true})}}> Add DSL </Button>
                </div>
                <div>
                    <DSLUpload callback = {() => this.update()}/>
                </div>
                <div>
                    <DSLDelete callback = {() => this.update()}/>
                </div>
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

class DSLDelete extends React.Component{
		constructor(props){
			super(props);
			this.text = "";
			this.callback = props["callback"]
		}


		render(){
			return <Form>
				<Form.Group controlId="DeleteList">
					<Form.Label>Delete:</Form.Label>
					<Form.Control as="textarea" onChange={(event) => this.text = event.target.value}/>
				</Form.Group>

				<Button type="submit" onClick = {() => {
					NetLib.post("dsl/remove", {dsl: this.text}).then(this.callback)
                }}> Delete</Button>

			</Form>
		}

}


class DSLUpload extends React.Component{
    constructor(props){
        super(props);
        this.text = "";
        this.callback = props["callback"]
    }


    render(){
        return <Form>
            <Form.Group controlId="Upload">
                <Form.Label>Upload DSL's:</Form.Label>
                <Form.Control as="textarea" onChange={(event) => this.text = event.target.value}/>
            </Form.Group>

            <Button type="submit" onClick = {() => {
                NetLib.post("dsl/add", {dsl: this.text}).then(this.callback)
            }}> Upload</Button>

        </Form>
    }

}