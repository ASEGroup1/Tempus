import React from "react";
import Form from "react-bootstrap/Form";
import Button from "react-bootstrap/Button";
import * as NetLib from "../../lib/NetworkLib";
import Col from "react-bootstrap/Col";

export class DSLDelete extends React.Component{
    constructor(props){
        super(props);
        this.text = "";
        this.callback = props["callback"]
    }


    render(){
        return (
            <Form inline>
                <Form.Group controlId="DeleteList">
                    <Form.Label style={{marginRight: 10}}><h4>Delete Filters</h4></Form.Label>
                    <Form.Control placeholder="FilterName; FilterName2; ..." as="textarea" onChange={(event) => this.text = event.target.value}/>
                </Form.Group>

                <Button type="submit" onClick = {() => {
                    NetLib.post("dsl/remove", {dsl: this.text}).then(this.callback)
                }}> <h4>Delete</h4></Button>
            </Form>
        )
    }
}