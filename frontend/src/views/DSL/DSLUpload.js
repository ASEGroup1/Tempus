import React from "react";
import Form from "react-bootstrap/Form";
import Button from "react-bootstrap/Button";
import * as NetLib from "../../lib/NetworkLib";
import Col from "react-bootstrap/Col";

export class DSLUpload extends React.Component{
    constructor(props){
        super(props);
        this.text = "";
        this.callback = props["callback"]
    }


    render(){
        return(
            <Form inline>
                <Form.Group controlId="Upload">
                    <Form.Label style={{marginRight: 10}}><h4>Upload DSL</h4></Form.Label>
                    <Form.Control placeholder="Enter DSL" as="textarea" onChange={(event) => this.text = event.target.value}/>
                </Form.Group>

                <Button type="submit" onClick = {() => {
                    NetLib.post("dsl/add", {dsl: this.text}).then(this.callback)
                }}> <h4>Upload</h4></Button>
            </Form>
        )
    }
}