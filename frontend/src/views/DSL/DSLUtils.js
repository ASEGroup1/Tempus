import Form from "react-bootstrap/Form";
import React from "react";


export default {
    validArgs: [],
    numParams: -1,
    generateComboBox(options, formName, label, callback, selected = "") {
        return <Form.Group controlId={formName}>
            <Form.Label style={{marginRight: 10}}>{label}:</Form.Label>
            <Form.Control as="select" onChange={callback} defaultValue={selected}>
                {options}
            </Form.Control>
        </Form.Group>;
    }
};