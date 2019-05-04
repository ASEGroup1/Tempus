import React from "react";
import Form from "react-bootstrap/Form";
import Button from "react-bootstrap/Button";
import * as NetLib from "../../lib/NetworkLib";
import Utils from "./DSLUtils";
import {Redirect} from "react-router-dom";
import {Constraint} from "./Constraint";

export class DSLCreation extends React.Component{

    constructor(props){
        super(props);
        this.state = {
            hasWhere: false,
            dslText: "",
            redirect: false
        };
        this.name= "name";
        this.whereText= "";
        this.bodyText= "";
        Utils.numParams=1;
    }

    update(){
        let text = ("filter " + this.name + "(" + "Param1" + (Utils.numParams == 2? ", Param2": "") + ") {\n\t" + this.bodyText.trim().replace(/\n/g, "\n\t") + "\n} " + (
            this.state.hasWhere? "where (" + this.whereText + ")": ""));
        console.log(text);
        this.setState({dslText: text});
    }



    render() {
        return <div>
            {this.state.redirect? <Redirect to="/dsl" />: null}
            <Form>
                <Form.Row>
                    <Form.Group controlId="name">
                        <Form.Label>Name:</Form.Label>
                        <Form.Control onChange={(event) => {this.name = event.target.value; this.update()}} defaultValue="name" />
                    </Form.Group>

                    <Form.Group controlId="params">
                        <Form.Label>Parameter Count:</Form.Label>
                        <Form.Control as="select" onChange={(event) => {Utils.numParams = parseInt(event.target.value); this.update()}} defaultValue="1">
                            <option>1</option>
                            <option>2</option>
                        </Form.Control>
                    </Form.Group>

                    <Form.Group controlId="whereCheck">
                        <Form.Check type="checkbox" label="Where" onChange = {(event) => {
                            this.setState({hasWhere: event.target.checked});
                            this.whereText = "";
                            this.update()
                        }
                        }/>
                    </Form.Group>
                </Form.Row>
            </Form>
            <div>
                Body:
                <Constraint callback = {(text) => {
                    this.bodyText = text;
                    this.update()
                }}/>
            </div>
            {this.state.hasWhere === true?
                <div>
                    Where:
                    <Constraint callback = {(text) => {
                        this.whereText = text;
                        this.update()
                    }}/>
                </div> : null
            }

            <Form><Form.Group controlId="dslText">
                <Form.Label>DSL Text:</Form.Label>
                <Form.Control readOnly as="textarea" value={this.state.dslText} rows = {5}/>
            </Form.Group>
            </Form>
            <Button onClick = {() => {
                NetLib.post("dsl/add", {dsl: this.state.dslText}).then(() =>this.setState({redirect: true}))

            }}>Submit </Button>
        </div>
    }
}